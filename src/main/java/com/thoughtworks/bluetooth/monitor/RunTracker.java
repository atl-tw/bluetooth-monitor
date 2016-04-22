package com.thoughtworks.bluetooth.monitor;

/**
 * Created by rcooper on 4/22/16.
 */
public class RunTracker {

    public static void main(String... args) throws Exception {
        Tracker t = new Tracker(System::currentTimeMillis, new DeviceRegistry());
        t.connect();
        t.subscribe();
        while(true){
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
