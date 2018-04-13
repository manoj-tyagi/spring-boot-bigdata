package com.bigdata.poc.conf;


import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class SparkConfig {
	private static final Logger logger = LoggerFactory.getLogger(SparkConfig.class);

	@Autowired
	private Environment env;

	@Bean(name = "sparkSession")
	@Qualifier("sparkSession")
	public SparkSession sparkSession() {
		SparkSession session = SparkSession
				.builder()
				.appName("WordCount-001")
				.master("yarn")
				.config("spark.hadoop.fs.defaultFS", "hdfs://192.168.21.130:8020")
				// for cloudera (8050 for hdp)
				.config("spark.hadoop.yarn.resourcemanager.address", "192.168.21.130:8032")
				.config("spark.yarn.jars", "hdfs://192.168.21.130:8020/user/cloudera/jars/*.jar")
				.config("spark.submit.deployMode", "client")	
				.config("spark.driver.host", "192.168.21.130")					
				.config("spark.hadoop.yarn.application.classpath",
						"$HADOOP_CONF_DIR, "+
				         "$HADOOP_COMMON_HOME/*, "+ 
						 "$HADOOP_COMMON_HOME/lib/*, "+ 
						 "$HADOOP_HDFS_HOME/*, "+ 
						 "$HADOOP_HDFS_HOME/lib/*, "+ 
						 "$HADOOP_YARN_HOME/*, "+
						 "$HADOOP_YARN_HOME/lib/*, "+
						 "$HADOOP_MAPRED_HOME/*, "+
						 "$HADOOP_MAPRED_HOME/lib/*")
				.getOrCreate();
		return session;
	}
}
