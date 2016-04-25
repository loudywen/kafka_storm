package com.demo.tempstorm.Temp_Storm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;

/**
 * Hello world!
 *
 */
public class StormEntry {
	public static void main(String[] args) throws IOException {

		TopologyBuilder1 t = new TopologyBuilder1();
		Config config = new Config();
		config.setDebug(false);
		//LocalCluster cluster = new LocalCluster();
		//StormTopology s = t.build();
		//cluster.submitTopology("tempstorm", config, t.build());
		
		try {
			StormSubmitter.submitTopology("Temp_Storm", config, t.build());
		} catch (AlreadyAliveException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthorizationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
