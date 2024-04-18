package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RawMidiListenerTest implements RawMidiListener{
    MidiBus mybus;

    // Test that turns on and off Ab
    @Test
    public void sendMidiMessageTest() throws InterruptedException{
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c") ;
        Integer[] rawMsgList = new Integer[3];
        byte[] rawBytes = new byte[3];

//        RawMidiListener listener = null;
        mybus.addMidiListener(this);

        rawBytes[0] = (byte) 144;
        rawBytes[1] = (byte) 80;
        rawBytes[2] = (byte) 127;

        mybus.sendMessage(rawBytes);

        Thread.sleep(2000);

        rawBytes[0] = (byte) 128;
        rawBytes[1] = (byte) 80;
        rawBytes[2] = (byte) 0;

        mybus.sendMessage(rawBytes);
        Thread.sleep(100);
    }

    // Test that seems to fail turning the note off ?
    // Nevermind
    

    @Test
    public void sendMidiMessageFailTest() throws InterruptedException{
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c") ;
        Integer[] rawMsgList = new Integer[3];
        byte[] rawBytes = new byte[3];

//        RawMidiListener listener = null;
        mybus.addMidiListener(this);

        rawBytes[0] = (byte) 144;
        rawBytes[1] = (byte) 64;
        rawBytes[2] = (byte) 127;

        mybus.sendMessage(rawBytes);

        Thread.sleep(2000);

        rawBytes[0] = (byte) 128;
        rawBytes[1] = (byte) 64;
        rawBytes[2] = (byte) 0;

        mybus.sendMessage(rawBytes);
        Thread.sleep(100);
    }

    


    @Override
    public void rawMidiMessage(byte[] data){
        System.out.println("here");
        for (int i = 0; i < data.length; i++){
            System.out.println("raw midi: ");
            System.out.println(data[i]);
        }
    }

}