package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PianoInputTest implements SimpleMidiListener{

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

    MidiBus mybus;


    // PIANO INPUT WHITEBOX AND DEMO TESTS //
    @Test
    public void lowestVelocityTest() throws InterruptedException{
        System.out.println("PLAY KEYBOARD AT SOFTEST VELOCITY");

        midiTest();

        while (noteOnCount < 1){
            Thread.sleep(100);
        }

        Assertions.assertTrue(globalVelocity < 50);
    }
    @Test
    public void maxVelocityTest() throws InterruptedException{
        System.out.println("PLAY KEYBOARD AT GREATEST VELOCITY!!");

        midiTest();

        while (noteOnCount < 1) {
            Thread.sleep(100); // Add a short delay to avoid busy-waiting
        }

        Assertions.assertTrue(globalVelocity > 100);
    }

    @Test
    public void verifyPitch() throws InterruptedException {
        System.out.println("PLAY C - E - G Sequenctially");

        midiTest();

        while (noteOnCount < 3){
            Thread.sleep(100);
        }

        Assertions.assertTrue(pitchesRecentlyPlayedList[0] == "C"
                && pitchesRecentlyPlayedList[1] == "E" && pitchesRecentlyPlayedList[2] == "G");

    }


    // -------------------------------------------------------------------------------------------



    // Simple Midi Listenener Implementation
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

    public int getVelocity(){
        return globalVelocity;
    }

    public void midiTest() {
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c");
        // Replace "Name of your MIDI keyboard" with the actual name as it appears in your MIDI software or settings
    }
}
