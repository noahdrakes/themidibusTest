package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class WhiteBoxTests implements SimpleMidiListener {

    String globalpitch;
    int globalVelocity;
    int globalNoteChannel;
    int noteOnCount = 0;
    int noteOffCount = 0;

    int noteStateChangeCount = 0;

    // A LIST OF THE LAST 20 RECENTLY PLAYED NOTES
    String[] pitchesRecentlyPlayedList = new String[100];

    // A LIST OF THE LAST 20 NOTE STATES
    // 1 = ON, 0 = OFF
    Integer[] noteStateRecenetlyPlayedList = new Integer[200];
    int getGlobalControllerChangeCount = 0;


    MidiBus mybus;

    Note note;

    ControlChange cc;

    static String[] pitchMap = new String[] {
            "C",
            "C#",
            "D",
            "D#",
            "E",
            "F",
            "F#",
            "G",
            "G#",
            "A",
            "A#",
            "B"
    };

    // MIDI OUT TESTS //

    @Test
    public void verifyPitchMidiOutTest() throws InterruptedException {

        // TRY USING THROUGH PORT TO SIMULATE IN AND OUT TO READ BACK
        // MAYBE THE TINY MIDI KEYBOARD WILL WORK

        MidiBus midibus = new MidiBus(this,"Studio 68c", "Studio 68c");
        midibus.sendTimestamps(false);

        midibus.sendNoteOn(0, 50, 127);
        Thread.sleep(5000);
        midibus.sendNoteOff(0, 50, 127);

        String realPitch = pitchMap[50 % 12];

        while (noteOnCount == 0){
            Thread.sleep(100);
        }

        Assertions.assertTrue(note.name() == realPitch);
    }

    @Test
    public void verifyNOteStateChangeTest() throws InterruptedException {
        MidiBus midibus = new MidiBus(this,"Studio 68c", "Studio 68c");
        midibus.sendTimestamps(false);

        for (int i = 0; i < 20; i++){
            midibus.sendNoteOn(0, 50, 127);
            Thread.sleep(100);
            midibus.sendNoteOff(0, 50, 127);
            Thread.sleep(100);
        }

        Assertions.assertTrue(noteStateRecenetlyPlayedList[noteStateChangeCount - 1] == 0);
    }

    @Test
    public void verifyVelocityChangeTest() throws InterruptedException {
        MidiBus midibus = new MidiBus(this,"Studio 68c", "Studio 68c");
        midibus.sendTimestamps(false);

        for (int i = 0; i <= 50; i++){

            midibus.sendNoteOn(0, 50, i);
            Thread.sleep(100);
            midibus.sendNoteOff(0, 50, i);
            Thread.sleep(100);
        }

        // DETERMINE IF I NEED TO USE THIS GLOBAL VAR OR IF THIS GETTER FUNCTION WORKS
//        Assertions.assertEquals(globalVelocity, 50);
        Assertions.assertEquals(note.velocity, 50);
    }

    @Test
    public void controllerChangeTest() throws InterruptedException {
        MidiBus midibus = new MidiBus(this,"Studio 68c", "Studio 68c");
        midibus.sendTimestamps(false);

        midibus.sendNoteOn(0, 70, 127);
        Thread.sleep(1000);
        midibus.sendControllerChange(0, 1, 127);
        Thread.sleep(4000);

        midibus.sendControllerChange(0, 1, 0);
        Thread.sleep(1000);
        midibus.sendNoteOff(0, 70, 127);

        Assertions.assertEquals(cc.channel, 0);
        Assertions.assertEquals(cc.number, 1);
        Assertions.assertEquals(cc.value, 0);

    }


    // __________________________________________________________ //
    // SIMPLE MIDI IMPLEMENTATION //
    // __________________________________________________________ //

    @Override
    public void noteOn(int channel, int pitch, int velocity) {

        note = new Note(channel,pitch,velocity);
        String note_pitch = note.name();
        pitchesRecentlyPlayedList[noteOnCount] = note_pitch;
        noteStateRecenetlyPlayedList[noteStateChangeCount] = 1;

        // prints out note
        System.out.println(note.toString());

        noteOnCount++;
        noteStateChangeCount++;
    }

    @Override
    public void noteOff(int channel, int pitch, int velocity) {

        Note note = new Note(channel,pitch,velocity);
        String note_pitch = note.name();
        noteStateRecenetlyPlayedList[noteStateChangeCount] = 0;

        System.out.println("Note OFF -> pitch: " + note_pitch + " velocity: " + velocity + " channel " + channel);

        noteOffCount++;
        noteStateChangeCount++;
    }

    @Override
    public void controllerChange(int channel, int number, int value) {
        System.out.println("Controller Change: " + number + " with value " + value + " on channel " + channel);

        cc = new ControlChange(channel, number, value);
        // might not need globals
    }

}
