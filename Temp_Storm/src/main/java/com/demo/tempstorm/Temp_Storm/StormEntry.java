package com.demo.tempstorm.Temp_Storm;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import backtype.storm.Config;
import backtype.storm.LocalCluster;

/**
 * Hello world!
 *
 */
public class StormEntry {
	public static void main(String[] args) throws IOException {

		TopologyBuilder1 t = new TopologyBuilder1();
		Config config = new Config();
		config.setDebug(false);
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("tempstorm", config, t.build());
		
	}
}
