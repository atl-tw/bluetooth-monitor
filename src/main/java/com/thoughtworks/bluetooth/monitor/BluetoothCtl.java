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

    private void cmd(String command){
        writer.println(command);
        writer.flush();
        sleep(300);
    }

    public void powerUpAndScan(){
        cmd("power on");
        cmd("agent on");
        cmd("discoverable on");
        cmd("scan on");

    }

    public void powerOff(){
        checkProc();
        cmd("power off");
    }

    public void powerCycle(){
        checkProc();
        powerOff();
        powerUpAndScan();
    }

    private void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {

        }
    }



}
