package com.thoughtworks.bluetooth.monitor;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rcooper on 4/20/16.
 */
public abstract class Event {

    private static final Pattern MAC_ADDRESS_PATTERN = Pattern.compile("(([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2}))");

    public final String hostname;
    public final String macAddress;

    protected Event(String hostname, String macAddress) {
        this.hostname = hostname;
        this.macAddress = macAddress;
    }


    public static Optional<String> extractMacAddress(java.lang.String data){
        Matcher m = MAC_ADDRESS_PATTERN.matcher(data);
        String mac = m.find() ?
                m.group(1) :
                null;
        return java.util.Optional.ofNullable(mac);
    }

    public abstract String toJsonString();
}
