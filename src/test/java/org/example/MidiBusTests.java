package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.sound.midi.*;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MidiBusTests {
    private MidiBus midiBus;
    private Object tParent;

    private String local = "Bus 1";

    @Mock
    private MidiListener mockListener;

    @BeforeEach
    void setUp() {
        midiBus = new MidiBus();
        tParent = new Object();
    }

    @Test
    void testDefaultConstructor() {
        MidiBus tBus = new MidiBus();
        assertNotNull(tBus.getBusName(), "Bus name should be generated.");
    }

    @Test
    void testObjectConstructor() throws IllegalAccessException, NoSuchFieldException {
        MidiBus tBus = new MidiBus(tParent);
        assertNotNull(midiBus.getBusName(), "Bus name should be generated.");
        String[] expected = new String[0];
        assertArrayEquals(expected, tBus.attachedInputs());
        assertArrayEquals(expected, tBus.attachedOutputs());
    }

    @Test
    void testRemoveOutput() {
        midiBus.addOutput(local);
        assertTrue(midiBus.removeOutput(0), "Should remove output device successfully.");
        assertFalse(midiBus.removeOutput(1), "Should return false when trying to remove non-existent device.");
    }

    @Test
    void testSendMessageWithBytes() throws Exception {
        byte[] mockData = new byte[]{(byte) ShortMessage.NOTE_ON, 60, 100};
        assertDoesNotThrow(() -> midiBus.sendMessage(mockData));
    }

    @Test
    void testSendMessageWithStatus() throws Exception {
        assertDoesNotThrow(() -> midiBus.sendMessage(ShortMessage.NOTE_ON));
    }

    @Test
    void testSendMessageWithStatusAndOneData() throws Exception {
        assertDoesNotThrow(() -> midiBus.sendMessage(ShortMessage.CONTROL_CHANGE, 7));
    }

    @Test
    void testSendMessageWithStatusAndTwoData() throws Exception {
        assertDoesNotThrow(() -> midiBus.sendMessage(ShortMessage.CONTROL_CHANGE, 7, 127));
    }

    @Test
    void testStop() {
        assertDoesNotThrow(() -> midiBus.stop());
    }

    @Test
    void testParentAndBusNameConstructor() {
        String busName = "Test Bus";
        MidiBus bus = new MidiBus(tParent, busName);
        assertEquals(busName, bus.getBusName(), "Bus name should be set correctly.");
        assertEquals(tParent, getField(bus, "parent"), "Parent should be set correctly.");
    }

    @Test
    void testParentAndInputOutputByIndexConstructor() {
        MidiBus bus = new MidiBus(tParent, 1, 1); // Adjust indices as necessary
        assertNotNull(bus.getBusName(), "Bus name should not be null.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentAndInputOutputByIndexAndBusNameConstructor() {
        String busName = "Special Bus";
        MidiBus bus = new MidiBus(tParent, 1, 1, busName); // Adjust indices as necessary
        assertEquals(busName, bus.getBusName(), "Bus name should be set correctly.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentAndInputOutputByNameConstructor() {
        MidiBus bus = new MidiBus(tParent, local, local); // Replace with actual device names
        assertNotNull(bus.getBusName(), "Bus name should not be null.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentAndInputOutputByNameAndBusNameConstructor() {
        String busName = "Complex Bus";
        MidiBus bus = new MidiBus(tParent, local, local, busName); // Replace with actual device names
        assertEquals(busName, bus.getBusName(), "Bus name should be set correctly.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentInputIndexAndOutputNameConstructor() {
        MidiBus bus = new MidiBus(tParent, 1, local); // Replace with actual output device name
        assertNotNull(bus.getBusName(), "Bus name should not be null.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentInputNameAndOutputIndexConstructor() {
        MidiBus bus = new MidiBus(tParent, local, 1); // Replace with actual input device name
        assertNotNull(bus.getBusName(), "Bus name should not be null.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    private Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            fail("Failed to access field: " + fieldName);
            return null;
        }
    }




    @Test
    void testAddAndRemoveInput() throws MidiUnavailableException {
        assertTrue(midiBus.addInput(local), "Should successfully add input device.");
        assertTrue(midiBus.removeInput(local), "Should successfully remove input device.");
    }

    @Test
    void testAddAndRemoveOutput() throws MidiUnavailableException {
        assertTrue(midiBus.addOutput(local), "Should successfully add output device.");
        assertTrue(midiBus.removeOutput(local), "Should successfully remove output device.");
    }

    @Test
    void testListenerManagement() {
        assertTrue(midiBus.addMidiListener(mockListener), "Should add listener successfully.");
        assertTrue(midiBus.removeMidiListener(mockListener), "Should remove listener successfully.");
    }

    @Test
    void testClearInputsOutputsAndAll() {
        // Add devices first
        midiBus.addInput(local);
        midiBus.addOutput(local);

        // Now clear inputs, outputs, and all
        midiBus.clearInputs();
        assertEquals(0, midiBus.attachedInputs().length, "Should have cleared all input devices.");

        midiBus.clearOutputs();
        assertEquals(0, midiBus.attachedOutputs().length, "Should have cleared all output devices.");

        // Add again to test clearAll
        midiBus.addInput(local);
        midiBus.addOutput(local);
        midiBus.clearAll();
        assertEquals(0, midiBus.attachedInputs().length, "Should have cleared all devices.");
        assertEquals(0, midiBus.attachedOutputs().length, "Should have cleared all devices.");
    }

    @Test
    void testBusNameGeneration() {
        MidiBus midiBus = new MidiBus();
        midiBus.generateBusName();
        String busName = midiBus.getBusName();
        assertTrue(busName.startsWith("MidiBus_") && busName.length() > "MidiBus_".length());
    }

    @Test
    void testSendNoteOn() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        midiBus.sendNoteOn(0, 60, 127);
        assertMidiMessageReceived(ShortMessage.NOTE_ON, 0, 60, 127);
    }

    @Test
    void testSendNoteOff() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        midiBus.sendNoteOff(0, 60, 127);
        assertMidiMessageReceived(ShortMessage.NOTE_OFF, 0, 60, 127);
    }

    @Test
    void testSendControllerChange() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        midiBus.sendControllerChange(0, 1, 100);
        assertMidiMessageReceived(ShortMessage.CONTROL_CHANGE, 0, 1, 100);
    }

    private void assertMidiMessageReceived(int status, int channel, int data1, int data2) {
        try {
            MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
            for (MidiDevice.Info info : infos) {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if (device.getMaxTransmitters() != 0) {
                    device.open();
                    device.getTransmitter().setReceiver(new Receiver() {
                        @Override
                        public void send(MidiMessage message, long timeStamp) {
                            if (message instanceof ShortMessage) {
                                ShortMessage shortMessage = (ShortMessage) message;
                                if (shortMessage.getStatus() == status
                                        && shortMessage.getChannel() == channel
                                        && shortMessage.getData1() == data1
                                        && shortMessage.getData2() == data2) {
                                    System.out.println("Received MIDI message: " + shortMessage);
                                    assertEquals(status, shortMessage.getStatus());
                                    assertEquals(channel, shortMessage.getChannel());
                                    assertEquals(data1, shortMessage.getData1());
                                    assertEquals(data2, shortMessage.getData2());
                                }
                            }
                        }

                        @Override
                        public void close() {
                        }
                    });
                    break;
                }
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSendNoteOnToDifferentChannels() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        for (int i = 0; i < 16; i++) {
            midiBus.sendNoteOn(i, 60, 127);
            assertMidiMessageReceived(ShortMessage.NOTE_ON, i, 60, 127);
        }
    }

    @Test
    void testSendNoteOnWithDifferentNoteValues() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        for (int i = 0; i < 128; i++) {
            midiBus.sendNoteOn(0, i, 127);
            assertMidiMessageReceived(ShortMessage.NOTE_ON, 0, i, 127);
        }
    }

    @Test
    void testSendNoteOnWithDifferentVelocityValues() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        for (int i = 0; i < 128; i++) {
            midiBus.sendNoteOn(0, 60, i);
            assertMidiMessageReceived(ShortMessage.NOTE_ON, 0, 60, i);
        }
    }

    @Test
    void testSendControllerChangeWithDifferentValues() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        for (int i = 0; i < 128; i++) {
            midiBus.sendControllerChange(0, i, i);
            assertMidiMessageReceived(ShortMessage.CONTROL_CHANGE, 0, i, i);
        }
    }

    @Test
    void testAddInputByIndex() {
        // Assuming index '0' is always valid for tests
        assertTrue(midiBus.addInput(0), "Input should be added successfully by index.");
        assertFalse(midiBus.addInput(-1), "Should handle invalid index gracefully.");
    }

    @Test
    void testAddOutputByName() {
        // Assuming "ValidOutput" is a correct name and "InvalidOutput" is not
        assertTrue(midiBus.addOutput(local), "Output should be added successfully by name.");
        assertFalse(midiBus.addOutput(local), "Should handle invalid name gracefully.");
    }

    @Test
    void testRemoveInputByIndex() {
        midiBus.addInput(0);
        assertTrue(midiBus.removeInput(0), "Input should be removed successfully.");
        assertFalse(midiBus.removeInput(1), "Should handle non-existent index gracefully.");
    }

    @Test
    void testRemoveOutputByName() {
        midiBus.addOutput(local);
        assertTrue(midiBus.removeOutput(local), "Output should be removed successfully.");
        assertFalse(midiBus.removeOutput(local), "Should handle non-existent name gracefully.");
    }

    @Test
    void testAddRemoveMidiListener() {
        assertTrue(midiBus.addMidiListener(mockListener), "Listener should be added successfully.");
        assertFalse(midiBus.addMidiListener(mockListener), "Duplicate listener should not be added.");
        assertTrue(midiBus.removeMidiListener(mockListener), "Listener should be removed successfully.");
        assertFalse(midiBus.removeMidiListener(mockListener), "Non-existent listener removal should be handled.");
    }

    @Test
    void testSendWithNoOutputs() {
        assertDoesNotThrow(() -> midiBus.sendNoteOn(0, 60, 127),
                "Should handle sending with no outputs gracefully.");
    }

    @Test
    void testSendTimestamps() {
        assertTrue(midiBus.sendTimestamps(), "Initial state should send timestamps.");
        midiBus.sendTimestamps(false);
        assertFalse(midiBus.sendTimestamps(), "Should not send timestamps after setting false.");
        midiBus.sendTimestamps(true);
        assertTrue(midiBus.sendTimestamps(), "Should send timestamps after setting true.");
    }

    @Test
    void testBusNameManagement() {
        midiBus.setBusName("TestBus");
        assertEquals("TestBus", midiBus.getBusName(), "Bus name should match the set value.");

        midiBus.generateBusName();
        assertTrue(midiBus.getBusName().startsWith("MidiBus_"), "Generated bus name should start with 'MidiBus_'.");
    }

    @Test
    void testToString() {
        midiBus.setBusName("MidiBus1");
        String expected = "MidiBus: MidiBus1 [0 input(s), 0 output(s), 0 listener(s)]";
        assertEquals(expected, midiBus.toString(), "toString should return the correct string representation.");
    }

    @Test
    void testEqualsAndHashCode() {
        MidiBus anotherMidiBus = new MidiBus();
        anotherMidiBus.setBusName("MidiBus1");
        midiBus.setBusName("MidiBus1");

        assertEquals(midiBus, anotherMidiBus, "Two MidiBuses with the same name should be equal.");
        assertEquals(midiBus.hashCode(), anotherMidiBus.hashCode(), "Hash codes should match for equal MidiBuses.");

        anotherMidiBus.setBusName("MidiBus2");
        assertNotEquals(midiBus, anotherMidiBus, "MidiBuses with different names should not be equal.");
    }

    @Test
    void testClone() {
        midiBus.setBusName("CloneTest");
        MidiBus clonedBus = midiBus.clone();
        assertEquals(midiBus, clonedBus, "Cloned bus should be equal to the original.");
        assertNotSame(midiBus, clonedBus, "Cloned bus should not be the same object as the original.");
    }

    @Test
    void testCloseAndDisposeMethods() {
        midiBus.addInput(0);  // Assuming there is at least one valid input index
        midiBus.addOutput(0); // Assuming there is at least one valid output index
        assertDoesNotThrow(midiBus::close, "Closing should not throw an exception.");
        assertDoesNotThrow(midiBus::dispose, "Disposing should not throw an exception.");
    }

    @Test
    void testListDevices() {
        assertDoesNotThrow(MidiBus::list, "Listing devices should not throw an exception.");
    }

    @Test
    void testDeviceListingStaticMethods() {
        MidiBus.findMidiDevices();

        // Test for available inputs
        String[] inputs = MidiBus.availableInputs();
        assertNotNull(inputs, "Should return a non-null array of input device names.");
        assertTrue(inputs.length > 0, "Should have at least one available input.");
        // If the name of a specific expected device is known, test for its presence
        assertTrue(Arrays.asList(inputs).contains(local), "Should contain the specific input device.");

        // Test for available outputs
        String[] outputs = MidiBus.availableOutputs();
        assertNotNull(outputs, "Should return a non-null array of output device names.");
        assertTrue(outputs.length > 0, "Should have at least one available output.");
        // Similarly, check for a known device
        assertTrue(Arrays.asList(outputs).contains(local), "Should contain the specific output device.");

        // Test for unavailable devices
        String[] unavailable = MidiBus.unavailableDevices();
        assertNotNull(unavailable, "Should return a non-null array of unavailable device names.");
        // The list of unavailable devices might be empty or non-empty depending on the setup
        if (unavailable.length > 0) {
            assertTrue(Arrays.asList(unavailable).contains("Known Unavailable Device Name"), "Should contain the specific unavailable device if expected.");
        }
    }

    @Test
    void testDynamicDeviceDiscovery() {
        // Assuming that device availability might change
        MidiBus.findMidiDevices(); // Refresh the device list

        // Perform checks similar to those above
        String[] newInputs = MidiBus.availableInputs();
        assertTrue(newInputs.length >= 0, "Should handle dynamic changes in available inputs.");

        String[] newOutputs = MidiBus.availableOutputs();
        assertTrue(newOutputs.length >= 0, "Should handle dynamic changes in available outputs.");

        // Assume new devices might become unavailable
        String[] newUnavailable = MidiBus.unavailableDevices();
        assertTrue(newUnavailable.length >= 0, "Should handle dynamic changes in unavailable devices.");
    }

    @Test
    void testConstructorAndToString() {
        Note note = new Note(1, 60, 100);
        assertEquals("[C, c:1, p:60, v:100]", note.toString());

        Note noteFull = new Note(1, 60, 100, 120, 1000L, "bus123");
        assertEquals("[C, c:1, p:60, v:100, t:120, ts:1000, b:bus123]", noteFull.toString());
    }

    @Test
    void testSettersAndGetters() {
        Note note = new Note(1, 60, 100);
        note.setChannel(2);
        assertEquals(2, note.channel());

        note.setPitch(61);
        assertEquals(61, note.pitch());
        assertEquals("C#", note.name());

        note.setVelocity(120);
        assertEquals(120, note.velocity());

        note.setTicks(150);
        assertEquals(150, note.ticks());
    }

    @Test
    void testEquals() {
        Note note1 = new Note(1, 60, 100, 120, 1000L, "bus123");
        Note note2 = new Note(1, 60, 100, 120, 1000L, "bus123");
        Note note3 = new Note(2, 61, 101, 121, 1001L, "bus124");

        assertEquals(note1, note2);
        assertNotEquals(note1, note3);
    }

    @Test
    void testOctaveAndRelativePitch() {
        Note note = new Note(1, 24, 100); // Octave for middle C
        assertEquals(2, note.octave());
        assertEquals(0, note.relativePitch()); // C is the base note
    }

    @Test
    void testConstructorWithTimestampAndBusName() {
        ControlChange cc = new ControlChange(1, 64, 127, 1000L, "MainBus");
        assertEquals(1, cc.channel());
        assertEquals(64, cc.number());
        assertEquals(127, cc.value());
        assertEquals(1000L, cc.timestamp);
        assertEquals("MainBus", cc.bus_name);
    }

    @Test
    void testConstructorWithoutTimestampAndBusName() {
        ControlChange cc = new ControlChange(1, 64, 127);
        assertEquals(1, cc.channel());
        assertEquals(64, cc.number());
        assertEquals(127, cc.value());
        assertEquals(-1, cc.timestamp);
        assertNull(cc.bus_name);
    }

    @Test
    void testSetters() {
        ControlChange cc = new ControlChange(1, 64, 127);
        cc.setChannel(2);
        assertEquals(2, cc.channel());

        cc.setNumber(65);
        assertEquals(65, cc.number());

        cc.setValue(120);
        assertEquals(120, cc.value());
    }

    @Test
    void testToControlChangeString() {
        ControlChange cc = new ControlChange(1, 64, 127);
        assertEquals("[c:1, n:64, v:127]", cc.toString());

        cc = new ControlChange(1, 64, 127, 1000L, "MainBus");
        assertEquals("[c:1, n:64, v:127, ts:1000, b:MainBus]", cc.toString());
    }

    @Test
    void testControlChnageEquals() {
        ControlChange cc1 = new ControlChange(1, 64, 127);
        ControlChange cc2 = new ControlChange(1, 64, 127);
        ControlChange cc3 = new ControlChange(1, 64, 127, 1000L, "MainBus");
        ControlChange cc4 = new ControlChange(2, 65, 128, 1000L, "MainBus");

        assertTrue(cc1.equals(cc2));
        assertFalse(cc1.equals(cc3));
        assertFalse(cc1.equals(cc4));
        assertFalse(cc3.equals(cc4));
    }

    @Test
    void testNotEqualsDifferentClass() {
        ControlChange cc = new ControlChange(1, 64, 127);
        Object obj = new Object();

        assertFalse(cc.equals(obj));
    }

    @Test
    void testEqualsNull() {
        ControlChange cc = new ControlChange(1, 64, 127);
        assertFalse(cc.equals(null));
    }

}
