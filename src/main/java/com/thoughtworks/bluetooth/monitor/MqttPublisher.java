package com.thoughtworks.bluetooth.monitor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rcooper on 4/21/16.
 */
public class MqttPublisher implements Watcher.Listener<Event> {

    private static final Logger LOGGER = Logger.getLogger(MqttPublisher.class.getCanonicalName());

    String topic        = "bluetooth";
    String content      = "Message from MqttPublishSample";
    int qos             = 2;
    String broker       = "tcp://iot.eclipse.org:1883";
    String clientId     = "BTMonitor";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttClient sampleClient = new MqttClient(broker, clientId, persistence);


    public MqttPublisher() throws MqttException {

    }

    public void connnect() throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        LOGGER.info("Connecting to broker: "+broker);
        sampleClient.connect(connOpts);
        LOGGER.info("Connected.");
    }


    private void send(Event e){
        try {
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
        } catch(MqttException me) {
            LOGGER.log(Level.WARNING, "Failed to send message", me);
        }
    }

    @Override
    public void onEvent(Event event) {

    }
}
