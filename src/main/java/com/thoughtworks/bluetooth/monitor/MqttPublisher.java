package com.thoughtworks.bluetooth.monitor;

import com.google.api.client.util.ExponentialBackOff;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rcooper on 4/21/16.
 */
public class MqttPublisher<T extends Event> implements Watcher.Listener<T> {

    private static final Logger LOGGER = Logger.getLogger(MqttPublisher.class.getCanonicalName());
    private static final ExponentialBackOff CONNECT_BACKOFF = new ExponentialBackOff.Builder()
            .setInitialIntervalMillis((int) TimeUnit.SECONDS.toMillis(1))
            .setMultiplier(2.0)
            .setMaxIntervalMillis((int)TimeUnit.MINUTES.toMillis(15))
            .build();
    String topic        = "bluetooth";
    int qos             = 2;
    String broker       = "tcp://atliot.com:1883";
    String clientId     = "BTMonitor";
    MemoryPersistence persistence = new MemoryPersistence();
    MqttClient sampleClient = new MqttClient(broker, clientId, persistence);


    public MqttPublisher() throws MqttException {

    }

    public void connect() {

        try {
            Thread.sleep(CONNECT_BACKOFF.nextBackOffMillis());
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            LOGGER.info("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            LOGGER.info("Connected.");
            CONNECT_BACKOFF.reset();
            sampleClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                        MqttPublisher.this.connect();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        } catch (Exception e){
            LOGGER.log(Level.WARNING, "Problem connecting to MQTT", e);
            connect();
        }
    }


    @Override
    public void onEvent(Event event) {
        try {
            MqttMessage message = new MqttMessage(event.toJsonString().getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            LOGGER.info("INFO: "+event.toJsonString());
        } catch(MqttException me) {
            LOGGER.log(Level.WARNING, "Failed to send message", me);
        }
    }
}
