package com.demo.tempstorm.Temp_Storm;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class FilterBolt implements IRichBolt {
	private static final Logger logger = LogManager.getLogger(FilterBolt.class);
	private Map<String, String> filter;
	private OutputCollector collector;

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		filter = new HashMap();

	}

	public void execute(Tuple input) {
		try {
			if (filter.get(input.getStringByField("email")) != null) {
				if (filter.get(input.getStringByField("email")).equals(input.getStringByField("tempValue"))) {
					logger.info("Same Temp, no emit");
				} else {
					filter.put(input.getStringByField("email"), input.getStringByField("tempValue"));
					collector.emit(new Values(input.getStringByField("email"), input.getStringByField("tempValue"), input.getStringByField("dateTimeValue")));

					logger.info("diff Temp, emit~~");
					collector.ack(input);
				}
			} else {
				filter.put(input.getStringByField("email"), input.getStringByField("tempValue"));
				collector.emit(new Values(input.getStringByField("email"), input.getStringByField("tempValue"), input.getStringByField("dateTimeValue")));

				logger.info("new email, emit~~");
				collector.ack(input);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			collector.fail(input);
		}

	}

	public void cleanup() {
		// TODO Auto-generated method stub

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("email", "tempValue", "dateTimeValue"));

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
