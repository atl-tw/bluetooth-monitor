package com.thoughtworks.bluetooth.monitor;

/**
 * Created by rcooper on 4/21/16.
 */
public class Device {

    private final String macAddress;
    private String company;
    private String type;
    private String host;
    private int signalStrength;
    private long timestamp;

    public Device(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(int signalStrength) {
        this.signalStrength = signalStrength;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
