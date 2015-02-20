package org.javabrain.mayank.storm.cookbook;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class HelloWorldTopology {

	// Running Topology on local cluster
	// mvn compile exec:java -Dexec.classpathScope=compile
	// -Dexec.mainClass=org.javabrain.mayank.storm.cookbook.HelloWorldTopology

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("randomHelloWorld", new HelloWorldSpout(), 10);
		builder.setBolt("HelloWorldBolt", new HelloWorldBolt(), 2)
				.shuffleGrouping("randomHelloWorld");

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);

			StormSubmitter.submitTopology(args[0], conf,
					builder.createTopology());
		} else {

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());
			Utils.sleep(10000);
			cluster.killTopology("test");
			cluster.shutdown();
		}

	}

}
