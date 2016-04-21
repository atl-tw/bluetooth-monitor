package com.thoughtworks.bluetooth.monitor;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rcooper on 4/20/16.
 */
public class EventTest {

    @Test
    public void extractMacAddress() throws Exception {
        assertEquals( "C8:69:CD:3E:20:F1", Event.extractMacAddress(TestConstants.HCI_EVENT).get());
        assertEquals( "C8:69:CD:83:BD:E7", Event.extractMacAddress(TestConstants.BT_MON_EVENT).get());
    }

}