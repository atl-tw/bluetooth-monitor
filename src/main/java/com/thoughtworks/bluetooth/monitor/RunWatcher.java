package com.thoughtworks.bluetooth.monitor;

import java.util.stream.Stream;

/**
 * Created by rcooper on 4/20/16.
 */
public class RunWatcher {



    public static void main(String... args) throws Exception {

        Process leScan = Runtime.getRuntime().exec("hcitool lescan");
        System.out.println("lescan started");
        Process btmon = Runtime.getRuntime().exec("btmon");
        System.out.println("btmon started");
        BluetoothCtl bluetoothCtl = new BluetoothCtl();
        bluetoothCtl.powerCycle();
        System.out.println("bluetoothctl started");
        Watcher watcher = Watcher.startWatcher(
                Stream.of(
                        leScan.getInputStream(),
                        bluetoothCtl.inputStream,
                        btmon.getInputStream()
                )
        );
        MqttPublisher publisher = new MqttPublisher();
        publisher.connect();
        watcher.addListener(RSSIEvent.class, publisher);
        watcher.addListener(DeviceInfoEvent.class, publisher);
        System.out.println("Started.");
        while(true){
            Thread.sleep(Long.MAX_VALUE);
        }
    }
}
