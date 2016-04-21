package com.thoughtworks.bluetooth.monitor;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import static org.junit.Assert.*;

/**
 * Created by rcooper on 4/21/16.
 */
public class MqttPublisherTest {
    @Test
    public void onEvent() throws Exception {



        String topic        = "bluetooth";
        int qos             = 2;
        String broker       = "tcp://atliot.com:1883";
        String clientId     = "BTMonitor";
        MemoryPersistence persistence = new MemoryPersistence();
//        MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
//        MqttConnectOptions connOpts = new MqttConnectOptions();
//        connOpts.setCleanSession(true);
//        sampleClient.connect(connOpts);
//        sampleClient.subscribe("bluetooth");
//        sampleClient.setCallback(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable cause) {
//                cause.printStackTrace();
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                System.out.println("Got "+new String(message.getPayload()));
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//                System.out.println("Delivery complete.");
//            }
//        });

        Thread.sleep(5000);

        MqttPublisher publisher = new MqttPublisher();
        publisher.connect();


        publisher.onEvent(new RSSIEvent("abc", 123));
    }

}