/*
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gq.rbeat.mozart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TunerActivity extends AppCompatActivity implements View.OnClickListener{

    // Debugging
    //private static final String TAG = "NXTMailbox";

    //private boolean drive = false;

    // Message types sent from the BluetoothEV3Service Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothEV3Service Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Layout Views
    private TextView mTitle;
    private TextView noteView;
    private Button mSendButton0;
    private Button mQuitButton;
    private Button mConnectButton;


    // Name of the connected device
    private String mConnectedDeviceName = null;

    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;

    // Member object for the EV3 services
    private BluetoothEV3Service mEV3Service = null;

    // WakeLock
    private PowerManager.WakeLock wl;

    private boolean abs = false;
    private Button btnABS;
    Button startButton;
    Button demo;
    Button d_note;
    Button e_note;
    Button g_note;
    Button a_note;
    Button b_note;
    Button eh_note;
    Button shutup;


    private static final String TAG = TunerActivity.class.getCanonicalName();

    public static final String STATE_NEEDLE_POS = "needle_pos";
    public static final String STATE_PITCH_INDEX = "pitch_index";
    public static final String STATE_LAST_FREQ = "last_freq";
    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 443;


    private Tuning mTuning;
    private AudioProcessor mAudioProcessor;
    private ExecutorService mExecutor = Executors.newSingleThreadExecutor();



    private boolean mProcessing = false;

    private int mPitchIndex;
    private float mLastFreq;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
        if (mEV3Service != null)
            mEV3Service.stop();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Utils.checkPermission(this, Manifest.permission.RECORD_AUDIO)) {


        }

        // If BT is not on, request that it be enabled.
        // setupApp() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
            if (mEV3Service == null) {
                setupApp();

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProcessing) {
            mAudioProcessor.stop();
            mProcessing = false;
        }
    }

    // The Handler that gets information back from the BluetoothEV3Service
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothEV3Service.STATE_CONNECTED:
                            mTitle.setText(R.string.title_connected_to);
                            mTitle.append(mConnectedDeviceName);
                            break;
                        case BluetoothEV3Service.STATE_CONNECTING:
                            mTitle.setText(R.string.title_connecting);
                            break;
                        case BluetoothEV3Service.STATE_NONE:
                            mTitle.setText(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:

                    byte[] readBuf = (byte[]) msg.obj;
                    byte[] bytes = (msg.obj).toString().getBytes();

                    try {
                        Toast.makeText(TunerActivity.this, new String(readBuf, "UTF-8") + " | " +
                                new String(bytes, "UTF-8"), Toast.LENGTH_SHORT).show();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    //Bundle bundle = msg.getData();
                    //long transferedBytes = msg.getData().getLong("transferedBytes");
                    //long totalSize = bundle.getLong("totalSize");
                    //mTvTextSize.setText(transferedBytes + "/" + totalSize);


                    //msg.getData().getByteArray("transferedBytes");


                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Msg("Connected to " + mConnectedDeviceName);
                    break;
                case MESSAGE_TOAST:
                    Msg(msg.getData().getString(TOAST));
                    break;
            }
        }
    };


    private void setupApp() {
        Log.d(TAG, "setup");


        // Initialize the send button with a listener that for click events

        // Initializing the "Start driving!" button.

        //Register an OnClickListener


/*        if (mEV3Service.getState() != BluetoothEV3Service.STATE_CONNECTED) {
        Msg(R.string.title_not_connected);
            return;
        }*/

        demo.setOnClickListener(this);
        e_note.setOnClickListener(this);
        d_note.setOnClickListener(this);
        e_note.setOnClickListener(this);
        g_note.setOnClickListener(this);
        a_note.setOnClickListener(this);
        b_note.setOnClickListener(this);
        eh_note.setOnClickListener(this);
        shutup.setOnClickListener(this);
        mQuitButton = (Button) findViewById(R.id.ButtonQ);
        mQuitButton.setOnClickListener(this);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProcessing = false;
                startAudioProcessing();
            }
        });


        mConnectButton = (Button) findViewById(R.id.ButtonC);
        mConnectButton.setOnClickListener(this);


        // Initialize the BluetoothEV3Service to perform bluetooth connections
        mEV3Service = new BluetoothEV3Service(this, mHandler);
    }


    @Override
    protected void onResume() {
        super.onResume();

        wl.acquire();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mEV3Service != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mEV3Service.getState() == BluetoothEV3Service.STATE_NONE) {
                // Start the Bluetooth services
                //  mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(
                //            Sensor.TYPE_ROTATION_VECTOR), mSensorManager.SENSOR_DELAY_NORMAL);


                mEV3Service.start();
            }
        }
    }

    private void requestPermissions() {
        if (!Utils.checkPermission(this, Manifest.permission.RECORD_AUDIO)) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

                DialogUtils.showPermissionDialog(this, getString(R.string.permission_record_audio), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(TunerActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                PERMISSION_REQUEST_RECORD_AUDIO);
                    }
                });

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        PERMISSION_REQUEST_RECORD_AUDIO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_RECORD_AUDIO: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
            }

        }
    }

    private void startAudioProcessing() {
        if (mProcessing)
            return;

        mAudioProcessor = new AudioProcessor();
        mAudioProcessor.init();
        mAudioProcessor.setPitchDetectionListener(new AudioProcessor.PitchDetectionListener() {
            @Override
            public void onPitchDetected(final float freq, double avgIntensity) {

                final int index = mTuning.closestPitchIndex(freq);


                runOnUiThread(new Runnable() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void run() {
                        //Toast.makeText(TunerActivity.this, "Hello mathafacka", Toast.LENGTH_SHORT).show();
                        //mTuningView.setSelectedIndex(index, true);
                        //mNeedleView.setTickLabel(0.0F, String.format("%.02fHz", pitch.frequency));
                        //mNeedleView.animateTip(needlePos);
                        //mFrequencyView.setText(String.format("%.02fHz", freq));

                        if (freq < 107) {

                            noteView.setText("E");
                            //e_note.setBackgroundColor(getResources().getColor(R.color.redr));
                            mEV3Service.EV3.sendNote("e");
                        }

                        if (freq > 107 && freq <= 135) {

                            noteView.setText("A");
                            //a_note.setBackgroundColor(getResources().getColor(R.color.redr));
                            mEV3Service.EV3.sendNote("a");
                        }

                        if (freq > 135 && freq <= 171.83) {

                            noteView.setText("D");
                            //d_note.setBackgroundColor(getResources().getColor(R.color.redr));
                            mEV3Service.EV3.sendNote("d");
                        }

                        if (freq > 171.83 && freq <= 221.94) {

                            noteView.setText("G");
                            //g_note.setBackgroundColor(getResources().getColor(R.color.redr));
                            mEV3Service.EV3.sendNote("g");
                        }

                        if (freq > 221.94 && freq <= 271.63) {

                            noteView.setText("B");
                            //b_note.setBackgroundColor(getResources().getColor(R.color.redr));
                            mEV3Service.EV3.sendNote("b");
                        }

                        if (freq > 271.63) {

                            noteView.setText("E#");
                            //cs_note.setBackgroundColor(getResources().getColor(R.color.redr));
                            mEV3Service.EV3.sendNote("eh");
                        }
                        /*
                        if (freq > 492.88 && freq <= 542)
                            Toast.makeText(TunerActivity.this, "B", Toast.LENGTH_SHORT).show();
                            noteView.setText("B");
                            mEV3Service.EV3.sendNote("b");

                        if (freq > 542)
                            Toast.makeText(TunerActivity.this, "C#", Toast.LENGTH_SHORT).show();
                            noteView.setText("C#");
                            mEV3Service.EV3.sendNote("ch");


                        final View goodPitchView = findViewById(R.id.good_pitch_view);
                        if (goodPitchView != null) {
                            if (goodPitch) {
                                if (goodPitchView.getVisibility() != View.VISIBLE) {
                                    Utils.reveal(goodPitchView);
                                }
                            } else if (goodPitchView.getVisibility() == View.VISIBLE) {
                                Utils.hide(goodPitchView);
                            }
                        }
                        */

                        mEV3Service.EV3.sendNote("h");
                    }
                });

                mPitchIndex = index;
                mLastFreq = freq;

            }
        });
        mProcessing = true;
        mExecutor.execute(mAudioProcessor);
    }



    @Override
    protected void onPause() {
        super.onPause();
        wl.release();
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setupActivityTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mTuning = Tuning.getTuning(this, Preferences.getString(this, getString(R.string.pref_tuning_key), getString(R.string.standard_tuning_val)));

        noteView = (TextView) findViewById(R.id.noteView);

        startButton = (Button) findViewById(R.id.start);
        demo = (Button) findViewById(R.id.demo);

        d_note = (Button) findViewById(R.id.d);
        e_note = (Button) findViewById(R.id.e);
        g_note = (Button) findViewById(R.id.g);
        a_note = (Button) findViewById(R.id.a);
        b_note = (Button) findViewById(R.id.b);
        eh_note = (Button) findViewById(R.id.eh);
        shutup = (Button) findViewById(R.id.su);

        // Set up the custom title
        mTitle = (TextView) findViewById(R.id.textView);
        //mTitle.setText(R.string.app_name);
        //mTitle = (TextView) findViewById(R.id.title_right_text);

        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Msg("Bluetooth is not available");
            finish();
            return;
        }

        // Stop turn off
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, TAG);

        //mNeedleView = (NeedleView) findViewById(R.id.pitch_needle_view);
        //mNeedleView.setTickLabel(-1.0F, "-100c");
        //mNeedleView.setTickLabel(0.0F, String.format("%.02fHz", mTuning.pitches[0].frequency));
        //mNeedleView.setTickLabel(1.0F, "+100c");

        //int primaryTextColor = Utils.getAttrColor(this, android.R.attr.textColorPrimary);


        //mTuningView.setTuning(mTuning);


        //mFrequencyView = (TextView) findViewById(R.id.frequency_view);
        //mFrequencyView.setText(String.format("%.02fHz", mTuning.pitches[0].frequency));

        //ImageView goodPitchView = (ImageView) findViewById(R.id.good_pitch_view);
        //goodPitchView.setColorFilter(primaryTextColor);
        requestPermissions();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mEV3Service.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    setupApp();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Msg(R.string.bt_not_enabled_leaving);
                    finish();
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //outState.putFloat(STATE_NEEDLE_POS, mNeedleView.getTipPos());
        outState.putInt(STATE_PITCH_INDEX, mPitchIndex);
        outState.putFloat(STATE_LAST_FREQ, mLastFreq);
        super.onSaveInstanceState(outState);
    }

    private void startDeviceList() {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    private void Msg(String m) {
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    private void Msg(int m) {
        Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //mNeedleView.setTipPos(savedInstanceState.getFloat(STATE_NEEDLE_POS));
        int pitchIndex = savedInstanceState.getInt(STATE_PITCH_INDEX);
        //mNeedleView.setTickLabel(0.0F, String.format("%.02fHz", mTuning.pitches[pitchIndex].frequency));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        if(v==mConnectButton){
            if (mEV3Service.getState() == BluetoothEV3Service.STATE_NONE)
                startDeviceList();
        }

        if (v == shutup) {
            mEV3Service.EV3.sendNote("h");
            noteView.setText("Nothing is playing");

        }
        if (v == demo) {
            mEV3Service.EV3.sendNote("c");
            noteView.setText("C");
            SystemClock.sleep(1000);
            noteView.setText("C");
            mEV3Service.EV3.sendNote("c");
            SystemClock.sleep(1000);
            mEV3Service.EV3.sendNote("g");
            noteView.setText("G");
            SystemClock.sleep(1000);
            mEV3Service.EV3.sendNote("g");
            noteView.setText("G");
            SystemClock.sleep(1000);
            noteView.setText("A");
            mEV3Service.EV3.sendNote("a");
            SystemClock.sleep(1000);
            noteView.setText("A");
            mEV3Service.EV3.sendNote("a");
            SystemClock.sleep(1000);
            mEV3Service.EV3.sendNote("g");
            noteView.setText("G");
        }


        if(v==d_note){
            noteView.setText("D");
            mEV3Service.EV3.sendNote("d");
            SystemClock.sleep(200);
            mEV3Service.EV3.sendNote("h");
        }
        if(v==e_note){
            noteView.setText("E");
            mEV3Service.EV3.sendNote("e");
            SystemClock.sleep(200);
            mEV3Service.EV3.sendNote("h");
        }

        if(v==g_note){
            noteView.setText("G");
            mEV3Service.EV3.sendNote("g");
            SystemClock.sleep(200);
            mEV3Service.EV3.sendNote("h");
        }
        if(v==a_note){
            noteView.setText("A");
            mEV3Service.EV3.sendNote("a");
            SystemClock.sleep(200);
            mEV3Service.EV3.sendNote("h");
        }
        if(v==b_note){
            noteView.setText("B");
            mEV3Service.EV3.sendNote("b");
            SystemClock.sleep(200);
            mEV3Service.EV3.sendNote("h");
        }
        if(v==eh_note){
            noteView.setText("E#");
            mEV3Service.EV3.sendNote("eh");
            SystemClock.sleep(200);
            mEV3Service.EV3.sendNote("h");
        }
        if(v==mQuitButton){
            mEV3Service.EV3.send("quit",true);
            finish();
        }

    }

}
