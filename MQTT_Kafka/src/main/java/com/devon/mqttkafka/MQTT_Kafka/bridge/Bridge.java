package com.devon.mqttkafka.MQTT_Kafka.bridge;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import kafka.javaapi.producer.Producer;

//import kafka.message.Message;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import model.Input;
import model.Temp;
import model.TempObject;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import org.kohsuke.args4j.CmdLineException;

public class Bridge implements MqttCallback {

	public String kafkaTopic;
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private MqttAsyncClient mqtt;
	private Producer<String, String> kafkaProducer;
	private static Gson gson = new GsonBuilder().create();
	private static JsonParser parser = new JsonParser();
	private static JsonObject json;

	public void connect(String serverURI, String clientId, String zkConnect) throws MqttException {
		mqtt = new MqttAsyncClient(serverURI, clientId);
		mqtt.setCallback(this);
		IMqttToken token = mqtt.connect();
		Properties props = new Properties();
		props.put("metadata.broker.list", "192.168.0.17:6667");
		props.put("zk.connect", zkConnect);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		/*props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");  
		props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");*/
		//props.put("producer.type","async");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		kafkaProducer = new Producer<String, String>(config);

		token.waitForCompletion();
		logger.info("Connected to MQTT and Kafka");
	}

	private void reconnect() throws MqttException {
		IMqttToken token = mqtt.connect();
		token.waitForCompletion();
	}

	public void subscribe(String[] mqttTopicFilters) throws MqttException {
		int[] qos = new int[mqttTopicFilters.length];
		for (int i = 0; i < qos.length; ++i) {
			qos[i] = 0;
		}
		mqtt.subscribe(mqttTopicFilters, qos);
	}

	public void connectionLost(Throwable cause) {
		logger.warn("Lost connection to MQTT server", cause);
		while (true) {
			try {
				logger.info("Attempting to reconnect to MQTT server");
				reconnect();
				logger.info("Reconnected to MQTT server, resuming");
				return;
			} catch (MqttException e) {
				logger.warn("Reconnect failed, retrying in 10 seconds", e);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
	}

	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub

	}

	public void messageArrived(String topic, MqttMessage message) throws Exception {
		byte[] payload = message.getPayload();
		
		String isJsonStr;
		String prettyFormat = null;

		if (isJson(new String(message.getPayload()))) {
			isJsonStr = "True";
			prettyFormat = toPrettyFormat(new String(message.getPayload()));

			KeyedMessage<String, String> data = new KeyedMessage<String, String>(kafkaTopic, prettyFormat);
			kafkaProducer.send(data);
			//	jsontoJava(prettyFormat);
			//test(prettyFormat);
		} else {
			isJsonStr = "False";
		}
		System.out.println("-------------------------------------------------");
		System.out.println("Topic:" + topic);
		System.out.println("Message: \n" + prettyFormat);
		System.out.println("isJson: " + isJsonStr);
		System.out.println("-------------------------------------------------");

	}

	private boolean isJson(String payload) {
		try {
			gson.fromJson(payload, Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}

	private static String toPrettyFormat(String jsonString) {

		json = parser.parse(jsonString).getAsJsonObject();

		gson = new GsonBuilder().create();
		String prettyJson = gson.toJson(json);

		return prettyJson;
	}

	
	public void jsontoJava(String str){
		Input ex = new Input();
		//json = parser.parse(str).getAsJsonObject();
		ex = gson.fromJson(str, Input.class);
		//Temp temp = new  Temp();
	//	temp = to.getTemp();
		
		TempObject to = ex.getD();
		Temp temp = to.getTemp();
	
		System.out.println(temp.getValue());
	}
	
	
	
	
	
//	private JsonParser parser1 = new JsonParser();
//	private JsonElement element;
//	private JsonObject d;
//	private JsonElement temp;
//	private JsonObject datetime;
//
//	public void test(String str) {
//		// System.out.println(str);
//		// System.out.println("inside test()");
//		element = parser1.parse(str);
//		d = element.getAsJsonObject();
//		Set<Map.Entry<String, JsonElement>> set = d.entrySet();
//		Iterator<Map.Entry<String, JsonElement>> iterator = set.iterator();
//
//		while (iterator.hasNext()) {
//			Map.Entry<String, JsonElement> entry = iterator.next();
//			String key = entry.getKey();
//			JsonElement value = entry.getValue();
//			if (!value.isJsonPrimitive()) {
//				if (value.isJsonObject()) {
//					// System.out.println("--");
//					if (key.equals("temp")) {
//						System.out.println("key: " + key);
//						JsonObject value1str = value.getAsJsonObject();
//						System.out.println("value1str: " + value1str);
//
//						System.out.println("value: " + value1str.get("value").getAsString());
//						test(value.toString());
//					} else if (key.equals("datetime")) {
//						System.out.println("key: " + key);
//						JsonObject value1str = value.getAsJsonObject();
//						System.out.println("value1str: " + value1str);
//
//						System.out.println("value: " + value1str.get("value"));
//						test(value.toString());
//					} else {
//						test(value.toString());
//					}
//				}
//			}
//		}
//
//	}
	// public static void main(String[] args) {
	// CommandLineParser parser = null;
	// try {
	// parser = new CommandLineParser();
	// parser.parse(args);
	// Bridge bridge = new Bridge();
	// bridge.connect(parser.getServerURI(), parser.getClientId(),
	// parser.getZkConnect());
	// bridge.subscribe(parser.getMqttTopicFilters());
	// } catch (MqttException e) {
	// e.printStackTrace(System.err);
	// } catch (CmdLineException e) {
	// System.err.println(e.getMessage());
	// parser.printUsage(System.err);
	// }
	// }
}