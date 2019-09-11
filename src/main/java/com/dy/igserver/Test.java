package com.dy.igserver;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Optional;

public class Test {
    public static void main(String[] args) {
        try {
            MqttClient client = new MqttClient("tcp://39.108.78.60:10908", "testclient");
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setUserName("admin");
            connectOptions.setPassword("admin".toCharArray());
            //Properties props = new Properties();
            //props.setProperty("com.ibm.ssl.keyStore", "p12test.keystore");
            //props.setProperty("com.ibm.ssl.keyStorePassword","Aikey.srv");
            //connectOptions.setSSLProperties(props);
            client.connect(connectOptions);
            // client app
            System.out.println("connected...");
            MqttMessage  message = new MqttMessage();
            message.setPayload("test123".getBytes());
            client.publish("/news", message);
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
