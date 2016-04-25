package com.demo.tempstorm.Temp_Storm;

import java.util.Map;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class HttpRequesttBolt implements IRichBolt {

//	private static Logger log = LoggerFactory.getLogger(HttpRequesttBolt.class);
	private Client client;
	private String tempUrl = "http://localhost:8080/rest/restapi/sendtemp?temp=";
	//private String tempUrl2;
	private OutputCollector collector;

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		client = Client.create();
		this.collector = collector;

	}

	public void execute(Tuple input) {
		String data = input.getStringByField("tempValue");
		String email = input.getStringByField("email");
		String timestamp = input.getStringByField("dateTimeValue");
		tempUrl = tempUrl+data+"&email="+email+"&timestamp="+timestamp;
		//tempUrl.replaceAll(" ", "%20");
		//log.info(tempUrl);
		WebResource webResource = client.resource(tempUrl.replaceAll(" ", "%20"));
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		// a successful response returns 200
		if (response.getStatus() != 200) {
			collector.fail(input);
		//	log.error("HTTP Error: " + response.getStatus());
		
		}

		String result = response.getEntity(String.class);
		//log.info("Response from the Server: ");
		//log.info(result);
		
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
