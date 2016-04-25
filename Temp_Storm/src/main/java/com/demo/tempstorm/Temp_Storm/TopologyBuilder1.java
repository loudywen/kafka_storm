package com.demo.tempstorm.Temp_Storm;

import backtype.storm.generated.StormTopology;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class TopologyBuilder1 {

	public StormTopology build() {

		ZkHosts hosts = new ZkHosts("172.17.16.184");
		hosts.refreshFreqSecs = 99999;
		SpoutConfig spoutConfig = new SpoutConfig(hosts, "demo", "", "test-consumer-group");
		spoutConfig.startOffsetTime = -1;
		spoutConfig.ignoreZkOffsets = true;
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		TopologyBuilder topology = new TopologyBuilder();
		topology.setSpout("kafkaspout", kafkaSpout, 4);
		topology.setBolt("extractjsonbolt", new ExtractJsonBolt1(), 4).shuffleGrouping("kafkaspout");
		topology.setBolt("filterbolt", new FilterBolt(), 4).fieldsGrouping("extractjsonbolt", new Fields("email"));
		topology.setBolt("notificationbolt", new NotificationBolt1(), 4).fieldsGrouping("filterbolt", new Fields("email"));
	//	topology.setBolt("socketbolt", new SocketBolt(), 4).shuffleGrouping("notificationbolt");
		topology.setBolt("httprequestbolt", new HttpRequesttBolt(), 4).shuffleGrouping("notificationbolt");


		return topology.createTopology();

	}

}
