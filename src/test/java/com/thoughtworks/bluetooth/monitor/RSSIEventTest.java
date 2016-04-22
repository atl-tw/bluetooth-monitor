package com.thoughtworks.bluetooth.monitor;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Created by rcooper on 4/20/16.
 */
public class RSSIEventTest {


    @Test
    public void parseHCIEventPacket() throws Exception {
        Optional<RSSIEvent> o = RSSIEvent.parsePacket("test","C8:69:CD:3E:20:F1", TestConstants.HCI_EVENT);
        RSSIEvent e = o.orElse(new RSSIEvent("test","a", Integer.MAX_VALUE));
        assertEquals("C8:69:CD:3E:20:F1", e.macAddress);
        assertEquals(-105, e.signalStrength);
    }

    @Test
    public void parseBTMONPacket() throws Exception {
        Optional<RSSIEvent> o = RSSIEvent.parsePacket("test","C8:69:CD:83:BD:E7", TestConstants.BT_MON_EVENT);
        RSSIEvent e = o.orElse(new RSSIEvent("test", "a", Integer.MAX_VALUE));
        assertEquals("C8:69:CD:83:BD:E7", e.macAddress);
        assertEquals(-98, e.signalStrength);
    }

}