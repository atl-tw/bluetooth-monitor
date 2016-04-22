package com.thoughtworks.bluetooth.monitor;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.logging.Logger;

/**
 * Created by rcooper on 4/21/16.
 */
public class Publisher<T extends Event> extends Mqtt implements Watcher.Listener<T> {

    private static final Logger LOGGER = Logger.getLogger(Publisher.class.getCanonicalName());

    public Publisher() throws Exception {
    }

    @Override
    public void onEvent(Event event) {
        publish(event.toJsonString().getBytes());
        LOGGER.info("Sent: "+event.toJsonString());
    }
}
