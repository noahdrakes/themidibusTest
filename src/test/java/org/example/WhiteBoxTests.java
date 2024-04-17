package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class WhiteBoxTests implements SimpleMidiListener {

    int globalpitch;
    int globalVelocity;
    int globalNoteChannel;
    int noteOnCount = 0;
    int noteOffCount = 0;

    // A LIST OF THE LAST 20 RECENTLY PLAYED NOTES
    String[] pitchesRecentlyPlayedList = new String[20];
    int getGlobalControllerChangeCount = 0;

    int globalControllerChannel;
    int globalControllerValue;
    int globalControllerNumber;

    String pitchBuffer;

    MidiBus mybus;

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
//        midibus.addMidiListener(this);

        midibus.sendNoteOn(0, 50, 127);
        Thread.sleep(5000);
        midibus.sendNoteOff(0, 50, 127);
//        midibus.sendNoteOn(0, 15, 127);


//
        while (noteOnCount == 0){
            Thread.sleep(100);
        }

        Assertions.assertTrue(globalVelocity == 127);

    }



    // SIMPLE MIDI IMPLEMENTATION
    @Override
    public void noteOn(int channel, int pitch, int velocity) {

        Note note = new Note(channel,pitch,velocity);
        String note_pitch = note.name();
        pitchesRecentlyPlayedList[noteOnCount] = note_pitch;

        System.out.println("Note On -> pitch: " + note_pitch + " velocity: " + velocity + " channel " + channel);
        globalVelocity = velocity;
        globalpitch = pitch;
        globalNoteChannel = channel;

        noteOnCount++;
    }

    @Override
    public void noteOff(int channel, int pitch, int velocity) {
        System.out.println("Note Off: " + pitch + " with velocity " + velocity + " on channel " + channel);
        globalVelocity = velocity;
        globalpitch = pitch;
        globalNoteChannel = channel;
        noteOffCount++;
    }

    @Override
    public void controllerChange(int channel, int number, int value) {
        System.out.println("Controller Change: " + number + " with value " + value + " on channel " + channel);
        globalControllerChannel = channel;
        globalControllerNumber = number;
        globalControllerValue = value;
    }

}
