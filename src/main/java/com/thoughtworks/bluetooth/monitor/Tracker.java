package com.thoughtworks.bluetooth.monitor;

import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rcooper on 4/21/16.
 */
public class Tracker extends Mqtt {

    private static final Logger LOGGER = Logger.getLogger(Tracker.class.getCanonicalName());
    private final DeviceRegistry registry;
    private final Clock clock;


    public Tracker(Clock clock, DeviceRegistry registry) throws Exception {
        this.onMessage = this::onMessage;
        this.clock = clock;
        this.registry = registry;
    }

    private void onMessage(MqttMessage mqttMessage) {
        String payload = new String(mqttMessage.getPayload());
        LOGGER.info("Got payload: "+payload);
        handlePayload(payload);
    }

    private void handlePayload(String payload){
        try {
            JsonObject o = JsonParser.object().from(payload);
            String type = o.getString("type");
            if(Objects.equals("rssi", type)){
                handleRSSIEvent( RSSIEvent.parseJson(o));
            } else if(Objects.equals("deviceInfo", type)){
                handleDeviceInfoEvent( DeviceInfoEvent.parseJson(o));
            }
        } catch (JsonParserException e) {
            LOGGER.log(Level.WARNING, "Failed to parse "+payload, e);
        }
    }

    private void handleDeviceInfoEvent(DeviceInfoEvent deviceInfoEvent) {
        Device device = registry.getDevice(deviceInfoEvent.macAddress);
        device.setCompany(deviceInfoEvent.company);
        device.setType(deviceInfoEvent.type);
    }

    private void handleRSSIEvent(RSSIEvent rssiEvent) {
        Device device = registry.getDevice(rssiEvent.macAddress);
        long time = clock.time();
        boolean update = device.getHost() == null ||
            device.getSignalStrength() < rssiEvent.signalStrength ||
            device.getTimestamp() < time - TimeUnit.MINUTES.toMillis(30);

        if(update){
            LOGGER.info("Updating device "+rssiEvent.hostname+"//"+rssiEvent.macAddress);
            device.setHost(rssiEvent.hostname);
            device.setTimestamp(time);
            device.setSignalStrength(rssiEvent.signalStrength);
        }

    }

    /** Abstracting away the time function so this can be tested.
     *
     */
    public interface Clock  {
        long time();
    }




}
