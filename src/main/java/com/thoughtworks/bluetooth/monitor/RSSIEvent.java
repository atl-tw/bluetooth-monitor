package com.thoughtworks.bluetooth.monitor;

import com.google.common.base.Splitter;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonWriter;

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

    public RSSIEvent(String hostname, String macAddress, int signalStrength) {
        super(hostname, macAddress);
        this.signalStrength = signalStrength;
    }


    public static Optional<RSSIEvent> parsePacket(final String hostname, final String macAddress, String packet){
        Iterable<String> lines = LINE_SPLITTER.split(packet);
        for(String line : lines){
            RSSIEvent event = parseLine(hostname, macAddress, line);
            if(event != null){
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    private static RSSIEvent parseLine(String hostname, String macAddress, String line){
        line = line.toLowerCase();
        if(line.contains(RSSI)){
            List<String> tokens = TOKEN_SPLITTER.splitToList(line);
            for(int i=0; i < tokens.size(); i++){
                if(tokens.get(i).startsWith(RSSI) && i + 1 < tokens.size()){
                    String value = tokens.get(i + 1);
                    try {
                        int read = Integer.valueOf(value);
                        return new RSSIEvent(hostname, macAddress, read);
                    } catch(NumberFormatException nfe){
                        LOGGER.warning(String.format("Failed to parse %s from line: \"%s\"", value, line));
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toJsonString(){
        return JsonWriter.string()
                .object()
                    .value("hostname", hostname)
                    .value("type", "rssi")
                    .value("mac", macAddress)
                    .value("signalStrength", signalStrength)
                .end()
                .done();
    }

    public static RSSIEvent parseJson(JsonObject o){
        return new RSSIEvent(o.getString("hostname"), o.getString("mac"), o.getInt("signalStrength"));
    }
}
