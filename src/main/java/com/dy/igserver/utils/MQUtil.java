package com.dy.igserver.utils;


import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQUtil {
    //定义ActivMQ的连接地址
    private static final String MQTT_URL = "tcp://localhost:10908" ;// "mqtt://192.168.1.143:10908"; tcp://39.108.78.60:10908
    private static final String USERNAME="admin";
    private static final String PASSWORD="admin";
    //定义发送消息的队列名称
    //private static final String QUEUE_NAME = "MyMessage";

    public static MqttClient getClient(String clientId){
        MqttClient client =null;
        try{
            client = new MqttClient(MQTT_URL, clientId);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setUserName(USERNAME);
            connectOptions.setPassword(PASSWORD.toCharArray());
            //Properties props = new Properties();
            //props.setProperty("com.ibm.ssl.keyStore", "p12test.keystore");
            //props.setProperty("com.ibm.ssl.keyStorePassword","Aikey.srv");
            //connectOptions.setSSLProperties(props);
            client.connect(connectOptions);
        }catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }
    public static int mqttSendMessage(MqttClient mc,String topic,String msg){
        try{
            MqttMessage message = new MqttMessage();
            message.setPayload(msg.getBytes());
            mc.publish(topic, message);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
            return 2;
        }
    }

    public static MqttClient getCallBackClient(String clientId, MqttCallback mqttCallback){
        MqttClient client =null;
        try{
            client = new MqttClient(MQTT_URL, clientId);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setUserName(USERNAME);
            connectOptions.setPassword(PASSWORD.toCharArray());
            //Properties props = new Properties();
            //props.setProperty("com.ibm.ssl.keyStore", "p12test.keystore");
            //props.setProperty("com.ibm.ssl.keyStorePassword","Aikey.srv");
            //connectOptions.setSSLProperties(props);
            client.setCallback(mqttCallback);
            client.connect(connectOptions);
        }catch (Exception e){
            e.printStackTrace();
        }
        return client;
    }



}
