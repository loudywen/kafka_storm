package com.demo.tempstorm.Temp_Storm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.tempstorm.model.D;
import com.demo.tempstorm.model.Datetime;
import com.demo.tempstorm.model.Temp;
import com.demo.tempstorm.model.TempInput;
import com.google.gson.Gson;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class ExtractJsonBolt1 implements IRichBolt {
	private Gson gson;
	private OutputCollector collector;
	private static final Logger logger = LogManager.getLogger(ExtractJsonBolt1.class);

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		gson = new Gson();

	}

	public void execute(Tuple input) {

		try {
			String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			TempInput jsonString = gson.fromJson(input.getString(0), TempInput.class);
			D d = jsonString.getD();
			String email = d.getEmail();
			Temp temp = d.getTemp();
			Datetime dateTime = d.getDatetime();
			collector.emit(new Values(email, temp.getValue(), currentTime));
			logger.info("ExtractJsonBolt Done!");
			collector.ack(input);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			collector.fail(input);
		}

	}

	public void cleanup() {

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("email", "tempValue", "dateTimeValue"));

	}

	public Map<String, Object> getComponentConfiguration() {

		return null;
	}

}
