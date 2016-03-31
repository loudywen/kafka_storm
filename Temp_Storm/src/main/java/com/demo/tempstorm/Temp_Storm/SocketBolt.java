package com.demo.tempstorm.Temp_Storm;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketBolt implements IRichBolt {

	
	private static final Logger logger = LogManager.getLogger(SocketBolt.class);
	private OutputCollector collector;

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
	
		this.collector = collector;
	}

	public void execute(Tuple input) {
		final String str = input.getStringByField("email")+","+input.getStringByField("tempValue")+","+input.getStringByField("dateTimeValue");
		logger.info("========================SocketBolt  sendding data to socket=============================");
		try {
			final Socket socket;
			socket = IO.socket("http://172.17.16.126:3000");
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

				public void call(Object... args) {
					socket.emit("event", str);
					socket.disconnect();
				}

			}).on("event", new Emitter.Listener() {

				public void call(Object... args) {
				}

			}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

				public void call(Object... args) {
				}

			});
			socket.connect();
			socket.close();
		
			
		
		} catch (URISyntaxException e) {
			logger.error("URISyntaxException: " + e.getMessage());
			collector.fail(input);
		}
	
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
