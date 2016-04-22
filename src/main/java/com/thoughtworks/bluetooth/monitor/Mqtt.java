package com.thoughtworks.bluetooth.monitor;

import com.google.api.client.util.ExponentialBackOff;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rcooper on 4/22/16.
 */
public class Mqtt {

    private static final ExponentialBackOff CONNECT_BACKOFF = new ExponentialBackOff.Builder()
            .setInitialIntervalMillis((int) TimeUnit.SECONDS.toMillis(1))
            .setMultiplier(2.0)
            .setMaxIntervalMillis((int)TimeUnit.MINUTES.toMillis(15))
            .build();
    private static final Logger LOGGER = Logger.getLogger(Tracker.class.getCanonicalName());

    protected String topic        = "bluetooth";
    protected int qos             = 0;
    protected String broker       = "tcp://atliot.com:1883";
    protected String clientId     = "bt-"+ InetAddress.getLocalHost().getHostName()+"-"+System.currentTimeMillis();
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient client = new MqttClient(broker, clientId, persistence);
    protected Consumer<MqttMessage> onMessage;

    public Mqtt() throws MqttException, UnknownHostException {
    }


    public void publish(byte[] payload){
        MqttMessage message = new MqttMessage(payload);
        message.setQos(qos);
        try {
            client.publish(topic, message);
        } catch (MqttException e) {
            LOGGER.log(Level.SEVERE, "Failed to send message", e);
        }
    }

    public void subscribe() throws MqttException {
        this.client.subscribe(topic, qos);
    }

    public void connect() {

        try {
            Thread.sleep(CONNECT_BACKOFF.nextBackOffMillis());
            final MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            LOGGER.info("Connecting to broker: " + broker);
            client.connect(connOpts);
            LOGGER.info("Connected.");
            CONNECT_BACKOFF.reset();
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    LOGGER.log(Level.WARNING, "Connection lost", cause);
                    Mqtt.this.connect();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if(onMessage != null){
                        onMessage.accept(message);
                    }
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
}
