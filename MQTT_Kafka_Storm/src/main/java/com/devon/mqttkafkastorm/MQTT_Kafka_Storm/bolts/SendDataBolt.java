package com.devon.mqttkafkastorm.MQTT_Kafka_Storm.bolts;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class SendDataBolt implements IRichBolt {
	private static Logger log = LoggerFactory.getLogger(SendDataBolt.class);
	private Client client;
	private String tempUrl = "http://localhost:8080/rest/restapi/sendtemp?temp=";
	private String tempUrl2;
	private OutputCollector collector;

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		client = Client.create();

	}

	public void execute(Tuple input) {
		String data = input.getStringByField("temp");
		tempUrl2 = tempUrl+data+"&"+"y=d";
		log.info(tempUrl2);
		WebResource webResource = client.resource(tempUrl2);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		// a successful response returns 200
		if (response.getStatus() != 200) {
			throw new RuntimeException("HTTP Error: " + response.getStatus());
		}

		String result = response.getEntity(String.class);
		log.info("Response from the Server: ");
		log.info(result);
		
		collector.ack(input);

	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
