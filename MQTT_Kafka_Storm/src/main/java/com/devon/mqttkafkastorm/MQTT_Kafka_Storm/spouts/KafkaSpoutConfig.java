package com.devon.mqttkafkastorm.MQTT_Kafka_Storm.spouts;

import backtype.storm.spout.SchemeAsMultiScheme;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class KafkaSpoutConfig {

	public SpoutConfig config(String zkHostsStr, String kafkaTopic, String consumerGroupID){
		
		ZkHosts zkHosts = new ZkHosts(zkHostsStr);
		// Create the KafkaSpout configuration
		// Second argument is the topic name
		// Third argument is the ZooKeeper root for Kafka
		// Fourth argument is consumer group id
		SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, kafkaTopic, "", consumerGroupID);
		// Specify that the kafka messages are String
		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		return kafkaConfig;
	}
}
