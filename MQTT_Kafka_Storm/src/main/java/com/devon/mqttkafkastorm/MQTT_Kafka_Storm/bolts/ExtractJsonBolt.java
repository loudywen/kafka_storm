package com.devon.mqttkafkastorm.MQTT_Kafka_Storm.bolts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import model.Datetime;
import model.Input;
import model.Temp;
import model.TempObject;

public class ExtractJsonBolt implements IRichBolt {
	private Gson gson;
	private String tempValue;
	private String dateTimeValue;
	private String email;
	private OutputCollector collector;
	private Input ex;

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("email", "tempValue", "dateTimeValue"));
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		gson = new Gson();
		ex = new Input();
	}

	public void execute(Tuple input) {
		try{
			jsontoJava(input.getString(0), collector,input);
		}catch(Exception e){
			System.out.println(e.getMessage());
			collector.fail(input);
		}
	}

	public void cleanup() {
		// TODO Auto-generated method stub
	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	public void jsontoJava(String str, OutputCollector oc,Tuple input) {
		
		String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		//System.out.println("currentTime: " + currentTime);
		ex = gson.fromJson(str, Input.class);
		TempObject to = ex.getD();
		
		Temp temp = to.getTemp();
		
		// Datetime dateTime = to.getDatetime();
		email = to.getEmail().trim();
		tempValue = temp.getValue().trim();
		dateTimeValue = currentTime;
		System.out.println("-----------------------ExtractJsonBolt-----------------------");
		System.out.println("| Email: " + email);
		System.out.println("| Temp: " + tempValue);
		System.out.println("| Time: " + dateTimeValue);
		System.out.println("-----------------------ExtractJsonBolt-----------------------");
		oc.emit(input,new Values(email, tempValue, dateTimeValue));
		oc.ack(input);
	}
}
