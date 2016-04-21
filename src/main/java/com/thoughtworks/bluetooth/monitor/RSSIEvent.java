package com.thoughtworks.bluetooth.monitor;

import com.google.common.base.Splitter;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by rcooper on 4/20/16.
 */
public class RSSIEvent extends Event {
    private static final Logger LOGGER = Logger.getLogger(RSSIEvent.class.getCanonicalName());
    private static final Splitter TOKEN_SPLITTER = Splitter.on(" ")
                                                        .omitEmptyStrings()
                                                        .trimResults();
    private static final Splitter LINE_SPLITTER = Splitter.on('\n').omitEmptyStrings();
    public static final String RSSI = "rssi";
    public final int signalStrength;

    public RSSIEvent(String macAddress, int signalStrength) {
        super(macAddress);
        this.signalStrength = signalStrength;
    }


    public static Optional<RSSIEvent> parsePacket(final String macAddress, String packet){
        Iterable<String> lines = LINE_SPLITTER.split(packet);
        for(String line : lines){
            RSSIEvent event = parseLine(macAddress, line);
            if(event != null){
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    private static RSSIEvent parseLine(String macAddress, String line){
        line = line.toLowerCase();
        if(line.contains(RSSI)){
            List<String> tokens = TOKEN_SPLITTER.splitToList(line);
            for(int i=0; i < tokens.size(); i++){
                if(tokens.get(i).startsWith(RSSI) && i + 1 < tokens.size()){
                    String value = tokens.get(i + 1);
                    try {
                        int read = Integer.valueOf(value);
                        return new RSSIEvent(macAddress, read);
                    } catch(NumberFormatException nfe){
                        LOGGER.warning(String.format("Failed to parse %s from line: \"%s\"", value, line));
                    }
                }
            }
        }
        return null;
    }
}
