"Moz@rt EV3" - the app for the “Mozart will play for you” project.
------------------------------------------------------------------------------------------------------------

App authors:
------------------------------------------------------------------------------------------------------------
Rodion Grinberg <admin@rbeat.gq> - Android App, EV3 Executable
Sergey Horef <s@horef.com> - Additional help with Android App

Initial project authors:
------------------------------------------------------------------------------------------------------------
Viliam Vald – The leader & hardware engineer
Daniella Dubok – Writer & designer
Solomon Geidarov – Scientist
Rodion Grinberg – Software engineer

Project supervisors:
------------------------------------------------------------------------------------------------------------
Ofer Danino
Itamar Feldman
Dr. Gidi Kaplan

Final release date: 
------------------------------------------------------------------------------------------------------------
Jun, 3rd 2019

------------------------------------------------------------------------------------------------------------

Abstract
------------------------------------------------------------------------------------------------------------

This project aims to translate any guitar melody to a cute little xylophone
melody. Our inspiration to the project came from our mighty will to research
the music and how in fact sounds are working. In this project we will develop
an app that will listen to the guitar’s melody which will be played by one of the
teammates or one of the people from the audience and convert the sounds to
a language that the robot will read and play the melody on the xylophone with
his both hands.


Our goal
------------------------------------------------------------------------------------------------------------

The project’s goal is to successfully convert guitar’s melody to xylophone’s melody.


Our project’s importance
------------------------------------------------------------------------------------------------------------

With this project we continue the research of sound waves and create common language of all instruments which creates cooperation between different instruments.


The Android App (Moz@rt EV3)
------------------------------------------------------------------------------------------------------------

Android application is presented as “ears” of the system.

It allows to transform guitar sounds to notes, and then send them to the EV3 Brick.

When the button “START LISTENING” is pressed, the application uses the phone’s microphone to record sound at 44100Hz sample rate to a buffer, where recording is being processed. If one of the examples of sample rates (Notes, that we included in sample rate array) is found in the processed audio, the app will send the note in the text form via Bluetooth.

Full documentation
------------------------------------------------------------------------------------------------------------
Hebrew: http://bit.ly/mozart_ev3_he
English: http://bit.ly/mozart_ev3_en
