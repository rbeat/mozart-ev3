package gq.rbeat.mozart;

public class EV3Brick {
	   BluetoothEV3Service os;

	   EV3Brick(BluetoothEV3Service mNxtService) {
	       os = mNxtService;
	   }
	   
	   public void send(String n, String s ) {
		   byte[] cmd = new byte[11+n.length()+s.length()];
		   int pos=0;
		   
	       cmd[0] = (byte)( (cmd.length - 2) & 0xFF );
	       cmd[1] = (byte)( (cmd.length - 2) >> 8 );
	       cmd[2] = (byte)0x00; 
	       cmd[3] = (byte)0x00;
	       cmd[4] = (byte)0x81;
	       cmd[5] = (byte)0x9E;
	       cmd[6] = (byte)(n.length()+1);// +1 is cause of the 0 at the end of the string.
	       pos = 7;
		   for(int i = 0; i < n.length() && i < 255; i++ )
		   {
		         cmd[pos] = (byte)n.charAt(i);
		         pos++;
		   }
		   cmd[pos] = (byte)0x00; 
		   pos++;
		   cmd[pos] = (byte)( (s.length()+1) & 0xFF );
		   pos++;
		   cmd[pos] = (byte)( (s.length()+1) >> 8 );
		   pos++;
		   for(int i = 0; i < s.length(); i++ )
		   {
		         cmd[pos] = (byte)s.charAt(i);
		         pos++;
		   }	
		   cmd[pos] = (byte)0x00; 
		   
	       os.write( cmd );
	       
	   }


	   public void send(String n, byte b ) {
		   byte[] cmd = new byte[14+n.length()];
		   int pos=0;
		   
	       cmd[0] = (byte)( (cmd.length - 2) & 0xFF );
	       cmd[1] = (byte)( (cmd.length - 2) >> 8 );
	       cmd[2] = (byte)0x00; 
	       cmd[3] = (byte)0x00;
	       cmd[4] = (byte)0x81;
	       cmd[5] = (byte)0x9E;
	       cmd[6] = (byte)(n.length()+1);
	       pos = 7;
		   for(int i = 0; i < n.length() && i < 255; i++ )
		   {
		         cmd[pos] = (byte)n.charAt(i);
		         pos++;
		   }
		   cmd[pos] = (byte)0x00; 
		   pos++;
		   cmd[pos] = (byte)0x04;
		   pos++;
		   cmd[pos] = (byte)0x00;
		   pos++;
		   cmd[pos] = (byte)0x00;
		   pos++;
		   cmd[pos] = (byte)0x00;
		   pos++;
		   
		   int bitsVal = Float.floatToIntBits((float)b);
	        String padded = String.format("%32s", Integer.toBinaryString(bitsVal)).replace(' ', '0');
	        //System.out.println(padded);
	        String a1 = padded.substring(0,8);
	        String b2 = padded.substring(8,16);
	        String c3 = padded.substring(16,24);
	        String d4 = padded.substring(24,32);
	        
	        int aa = Integer.parseInt(a1, 2);
	        int bb = Integer.parseInt(b2, 2);
	        int cc = Integer.parseInt(c3, 2);
	        int dd = Integer.parseInt(d4, 2);
		   
	        cmd[pos] = (byte) bb;
			pos++;
			cmd[pos] = (byte) aa;

			
	       os.write( cmd );
	       
	   } 
	   
	   public void send(String n, boolean l ) {
		   byte[] cmd = new byte[11+n.length()];
		   int pos=0;
		   
	       cmd[0] = (byte)( (cmd.length - 2) & 0xFF );
	       cmd[1] = (byte)( (cmd.length - 2) >> 8 );
	       cmd[2] = (byte)0x00; 
	       cmd[3] = (byte)0x00;
	       cmd[4] = (byte)0x81;
	       cmd[5] = (byte)0x9E;
	       cmd[6] = (byte)(n.length()+1);
	       pos = 7;
		   for(int i = 0; i < n.length() && i < 255; i++ )
		   {
		         cmd[pos] = (byte)n.charAt(i);
		         pos++;
		   }
		   cmd[pos] = (byte)0x00; 
		   pos++;
		   cmd[pos] = (byte)0x01;
		   pos++;
		   cmd[pos] = (byte)0x00;
		   pos++;
		   if (l)
			   cmd[pos] = (byte)0x01;
		   else
			   cmd[pos] = (byte)0x00;
		   
	       os.write( cmd );
	       
	   }




	public void writeMailBox(long[] array) {
		//12-00-xx-xx-80-00-00-AE-00-06-81-32-00-82-84-03-82-B4-00-01
		//12-00-00-00-81-9e-04-61-62-63-00-07-00-104  101  108  108  111   33    0
		byte[] cmd = new byte[26];

		cmd[0] = (byte) 0x18; //18 bytes packet (not including the 2 byte header)
		cmd[1] = (byte) 0x00; //"
		cmd[2] = (byte) 0x00; //message counter
		cmd[3] = (byte) 0x00; //message counter
		cmd[4] = (byte) 0x81; //System command
		cmd[5] = (byte) 0x9E; //write to mailbox
		cmd[6] = (byte) 0x04; //length of mailbox name
		cmd[7] = (byte) 0x61; //a
		cmd[8] = (byte) 0x62; //b
		cmd[9] = (byte) 0x63; //c
		cmd[10] = (byte) 0x00; //end of mailbox name
		cmd[11] = (byte) 0x07; //length of message
		cmd[12] = (byte) 0x0d; //end of length of message
		cmd[13] = (byte) 0x61; //a
		cmd[14] = (byte) 0x62; //b
		cmd[15] = (byte) 0x63; //c
		cmd[16] = (byte) 0x64;
		cmd[17] = (byte) 0x65;
		cmd[18] = (byte) 0x66;
		cmd[19] = (byte) 0x68; //h
		cmd[20] = (byte) 0x65; //e
		cmd[21] = (byte) 0x6c; //l
		cmd[22] = (byte) 0x6c; //l
		cmd[23] = (byte) 0x6f; //o
		cmd[24] = (byte) 0x21; //!
		cmd[25] = (byte) 0x00; //end of packet
		os.write( cmd );
	}

	public void sendNote(String note){
		//♯
		int length_note = note.length();
		switch (length_note){
			case 1: {
				byte[] cmd = new byte[15];

				cmd[0] = (byte) 0x0d; //13 bytes packet (not including the 2 byte header)
				cmd[1] = (byte) 0x00; //"
				cmd[2] = (byte) 0x00; //message counter
				cmd[3] = (byte) 0x00; //message counter
				cmd[4] = (byte) 0x81; //System command
				cmd[5] = (byte) 0x9E; //write to mailbox
				cmd[6] = (byte) 0x04; //length of mailbox name
				cmd[7] = (byte) 0x61; //a
				cmd[8] = (byte) 0x62; //b
				cmd[9] = (byte) 0x63; //c
				cmd[10] = (byte) 0x00; //end of mailbox name
				cmd[11] = (byte) 0x02; //length of message
				cmd[12] = (byte) 0x0d; //end of length of message
				if(note.equals("g"))
					cmd[13] = (byte) 0x67;
				if(note.equals("f"))
					cmd[13] = (byte) 0x66;
				if(note.equals("e"))
					cmd[13] = (byte) 0x65;
				if(note.equals("d"))
					cmd[13] = (byte)0x64;
				if(note.equals("c"))
					cmd[13] = (byte)0x63;
				if(note.equals("b"))
					cmd[13] = (byte)0x62;
				if(note.equals("a"))
					cmd[13] = (byte)0x61;
				if(note.equals("h"))
					cmd[13] = (byte)0x68;
				cmd[14] = (byte) 0x00; //end of packet
				os.write( cmd );
				break;
			}
			case 2:{
				byte[] cmd = new byte[16];

				cmd[0] = (byte) 0x0e; //14 bytes packet (not including the 2 byte header)
				cmd[1] = (byte) 0x00; //"
				cmd[2] = (byte) 0x00; //message counter
				cmd[3] = (byte) 0x00; //message counter
				cmd[4] = (byte) 0x81; //System command
				cmd[5] = (byte) 0x9E; //write to mailbox
				cmd[6] = (byte) 0x04; //length of mailbox name
				cmd[7] = (byte) 0x61; //a
				cmd[8] = (byte) 0x62; //b
				cmd[9] = (byte) 0x63; //c
				cmd[10] = (byte) 0x00; //end of mailbox name
				cmd[11] = (byte) 0x02; //length of message
				cmd[12] = (byte) 0x0d; //end of length of message

				if(note.equals("eh")) {
					cmd[13] = (byte) 0x65;
					cmd[14] = (byte) 0x68;
				}
				if(note.equals("su")) {
					cmd[13] = (byte) 0x68;
					cmd[14] = (byte) 0x68;
				}
				cmd[15] = (byte) 0x00; //end of packet
				os.write( cmd );
				break;
			}
			default:{
				byte[] cmd = new byte[15];

				cmd[0] = (byte) 0x0d; //13 bytes packet (not including the 2 byte header)
				cmd[1] = (byte) 0x00; //"
				cmd[2] = (byte) 0x00; //message counter
				cmd[3] = (byte) 0x00; //message counter
				cmd[4] = (byte) 0x81; //System command
				cmd[5] = (byte) 0x9E; //write to mailbox
				cmd[6] = (byte) 0x04; //length of mailbox name
				cmd[7] = (byte) 0x61; //a
				cmd[8] = (byte) 0x62; //b
				cmd[9] = (byte) 0x63; //c
				cmd[10] = (byte) 0x00; //end of mailbox name
				cmd[11] = (byte) 0x02; //length of message
				cmd[12] = (byte) 0x0d; //end of length of message
				cmd[13] = (byte) 0x68; //h
				cmd[14] = (byte) 0x00; //end of packet
				os.write( cmd );
				break;
			}
		}
	}


	}
