package com.thoughtworks.bluetooth.monitor;

import java.util.stream.Stream;

/**
 * Created by rcooper on 4/20/16.
 */
public class RunWatcher {



    public static void main(String... args) throws Exception {

        Process leScan = Runtime.getRuntime().exec("hcitool lescan");
        Process btmon = Runtime.getRuntime().exec("btmon");
        BluetoothCtl bluetoothCtl = new BluetoothCtl();
        Watcher watcher = new Watcher(
                Stream.of(
                        leScan.getInputStream(),
                        bluetoothCtl.inputStream,
                        btmon.getInputStream()
                )
        );


    }
}
