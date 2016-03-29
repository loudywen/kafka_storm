package com.devon.mqttkafkastorm.MQTT_Kafka_Storm;


import com.devon.mqttkafkastorm.MQTT_Kafka_Storm.bolts.ExtractJsonBolt;
import com.devon.mqttkafkastorm.MQTT_Kafka_Storm.bolts.NotificationBolt;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	ZkHosts hosts = new ZkHosts("172.17.16.162");
		hosts.refreshFreqSecs = 99999;
		SpoutConfig spoutConfig = new SpoutConfig(hosts, "demo", "", "test-consumer-group");
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		spoutConfig.startOffsetTime = -1;
		spoutConfig.ignoreZkOffsets = true;

		//KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

		Config config = new Config();

		// config.setDebug(true );

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafkaspout", new KafkaSpout(spoutConfig),4);
		builder.setBolt("extractjsonbolt", new ExtractJsonBolt(),4).shuffleGrouping("kafkaspout");
		builder.setBolt("notification", new NotificationBolt(),4).fieldsGrouping("extractjsonbolt", new Fields("email"));

		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("kafkastorm", config, builder.createTopology());
    }
}
