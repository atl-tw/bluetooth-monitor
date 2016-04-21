package com.thoughtworks.bluetooth.monitor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rcooper on 4/21/16.
 */
public class DeviceInfoEventTest {

    @Test
    public void parsePacket() throws Exception {
        DeviceInfoEvent even = DeviceInfoEvent.parsePacket("abc", TestConstants.HCI_EVENT).get();
        assertEquals("Apple, Inc.", even.company);
        assertEquals("Apple TV", even.type);
    }

}