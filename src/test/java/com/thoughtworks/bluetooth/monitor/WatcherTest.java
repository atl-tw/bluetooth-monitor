package com.thoughtworks.bluetooth.monitor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * Created by rcooper on 4/21/16.
 */
public class WatcherTest {
    @Test
    public void dispatchHciEvent() throws Exception {
        Watcher watcher = new Watcher(Stream.empty());
        List<Event> events = new ArrayList<>(2);
        Set<Class> classes = new HashSet<>(2);
        watcher.addListener(RSSIEvent.class, e ->  { events.add(e); classes.add(e.getClass()); } );
        watcher.addListener(DeviceInfoEvent.class, e-> {events.add(e); classes.add(e.getClass()); } );
        watcher.parseAndDispatch(new StringBuilder(TestConstants.HCI_EVENT));
        assertEquals(2, events.size());
        assertTrue(classes.contains(RSSIEvent.class));
        assertTrue(classes.contains(DeviceInfoEvent.class));
    }

}