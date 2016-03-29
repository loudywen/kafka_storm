package com.devon.mqttkafka.MQTT_Kafka;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.devon.mqttkafka.MQTT_Kafka.bridge.Bridge;

/**
 * Hello world!
 *
 */
public class MQTT_Kafka 
{
    public static void main( String[] args )
    {
    	Bridge bridge = new Bridge();
    	bridge.kafkaTopic ="demo";
    	String[] topics = {"devonlao11/#"};
    	
    	try {
    		System.out.println("Listening");
			bridge.connect("tcp://messagesight.demos.ibm.com:1883", "devonkafka1", "172.17.16.169:2181");
			
			bridge.subscribe(topics);
			
			
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
