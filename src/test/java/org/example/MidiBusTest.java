package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MidiBusTest {
    @Test
    public void simpleMidiTest(){
        MidiBus myBus = new MidiBus();
        myBus.sendTimestamps(false);
        String[] availableInputs = MidiBus.availableInputs();



        System.out.println("AVAILABLE INPUTS");
        System.out.println("_________________");

        for (int i = 0; i < availableInputs.length; i++){
            System.out.println(i + " " + availableInputs[i]);
        }

        System.out.println("AVAILABLE OUTPUTS");
        System.out.println("_________________");

        for (int i = 0; i < availableInputs.length; i++){
            System.out.println(i+ " " + availableInputs[i]);

        }



    }


}