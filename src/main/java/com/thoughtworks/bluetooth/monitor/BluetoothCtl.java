package com.thoughtworks.bluetooth.monitor;

import java.io.*;

import static com.google.common.base.Preconditions.checkState;

/**
 * A wrapper around the bluetooth control process.
 * Created by rcooper on 4/20/16.
 */
public class BluetoothCtl {

    private final Process proc;
    private final PrintWriter writer;
    public final InputStream inputStream;

    public BluetoothCtl(String bluetoothControlCommand) throws IOException {
        this.proc = Runtime.getRuntime().exec(bluetoothControlCommand);
        this.writer = new PrintWriter(new OutputStreamWriter(proc.getOutputStream()));
        this.inputStream = proc.getInputStream();
    }

    public BluetoothCtl() throws IOException {
        this("bluetoothctl");
    }

    private void checkProc() {
        checkState(proc.isAlive(), "bluetoothctl process has died.");
    }

    public void powerUpAndScan(){
        writer.println("power on");
        writer.println("agent on");
        writer.println("discoverable on");
        writer.println("scan on");
    }

    public void powerOff(){
        writer.println("power off");
    }

    public void powerCycle(){
        powerOff();
        powerUpAndScan();
    }





}
