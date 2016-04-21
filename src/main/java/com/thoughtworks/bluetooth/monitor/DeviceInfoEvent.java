package com.thoughtworks.bluetooth.monitor;

import com.google.common.base.Splitter;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonWriter;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Created by rcooper on 4/20/16.
 */
public class DeviceInfoEvent extends Event {
    private static final Logger LOGGER = Logger.getLogger(DeviceInfoEvent.class.getCanonicalName());
    private static final Splitter LINE_SPLITTER = Splitter.on('\n')
            .trimResults()
            .omitEmptyStrings();
    public static final String COMPANY = "Company:";
    public static final String TYPE = "Type:";
    public final String company;
    public final String type;

    public DeviceInfoEvent(String macAddress, String company, String type) {
        super(macAddress);
        this.company = company;
        this.type = type;
    }


    @Override
    public String toJsonString() {
        return JsonWriter.string()
                .object()
                    .value("mac", macAddress)
                    .value("company", company)
                    .value("type", type)
                .end()
                .done();
    }

    public static Optional<DeviceInfoEvent> parsePacket(String macAddress, String data){
        if(!data.startsWith("> HCI Event: LE Meta Event")){
            return Optional.empty();
        }
        Iterable<String> lines = LINE_SPLITTER.split(data);
        String company = null;
        String type = null;
        for(String line : lines){
            if(line.startsWith(COMPANY)){
                company = line.substring(COMPANY.length()+1, line.lastIndexOf("(")).trim();
            } else if(line.startsWith(TYPE)){
                type = line.substring(TYPE.length() +1, line.lastIndexOf("(")).trim();
            }
        }
        LOGGER.info(
                new StringBuilder("found: ")
                        .append(company)
                        .append(" ")
                        .append(type)
                        .toString()
        );
        return Optional.of(new DeviceInfoEvent(macAddress, company, type));
    }


    public static DeviceInfoEvent parseJson(JsonObject o){
        return new DeviceInfoEvent(o.getString("mac"), o.getString("company"), o.getString("type"));
    }

}
