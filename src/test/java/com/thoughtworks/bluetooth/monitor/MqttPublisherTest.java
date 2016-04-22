package com.thoughtworks.bluetooth.monitor;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Test;

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
//        Mqtt client = new Mqtt(broker, clientId, persistence);
//        MqttConnectOptions connOpts = new MqttConnectOptions();
//        connOpts.setCleanSession(true);
//        client.connect(connOpts);
//        client.subscribe("bluetooth");
//        client.setCallback(new MqttCallback() {
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

        Publisher publisher = new Publisher();
        publisher.connect();


        publisher.onEvent(new RSSIEvent("test", "abc", 123));
    }

}