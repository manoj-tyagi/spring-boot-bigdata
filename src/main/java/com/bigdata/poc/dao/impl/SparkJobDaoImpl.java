package com.bigdata.poc.dao.impl;

import static org.apache.spark.sql.functions.avg;
import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.max;

import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.RelationalGroupedDataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkJobDaoImpl {
	private static final String AGE_MIDPOINT = "age_midpoint";
	private static final String SALARY_MIDPOINT = "salary_midpoint";
	private static final String SALARY_MIDPOINT_BUCKET = "salary_midpoint_bucket";

	public static void main(String[] args) {
		SparkSession sparkSession = SparkSession.builder().appName("WordCountJob001").master("yarn")
				.config("spark.hadoop.fs.defaultFS", "hdfs://quickstart.cloudera:8020")
				// for cloudera (8050 for hdp)
				.config("spark.hadoop.yarn.resourcemanager.address", "quickstart.cloudera:8032")
				.config("spark.yarn.jars", "hdfs://quickstart.cloudera:8020/user/cloudera/jars/*.jar")
				.config("spark.hadoop.yarn.application.classpath",
						"$HADOOP_CONF_DIR, $HADOOP_COMMON_HOME/*, $HADOOP_COMMON_HOME/lib/*,"
								+ "$HADOOP_HDFS_HOME/*, $HADOOP_HDFS_HOME/lib/*,"
								+ "$HADOOP_YARN_HOME/*, $HADOOP_YARN_HOME/lib/*,"
								+ "$HADOOP_MAPRED_HOME/* ,$HADOOP_MAPRED_HOME/lib/*")
				.getOrCreate();

		DataFrameReader dataFrameReader = sparkSession.read();

		Dataset<Row> responses = dataFrameReader.option("header", "true").csv("hdfs://quickstart.cloudera:8020/user/cloudera/data/2016-stack-overflow-survey-responses.csv");

		System.out.println("=== Print out schema ===");
		responses.printSchema();

		System.out.println("=== Print 20 records of responses table ===");
		responses.show(20);

		System.out.println("=== Print the so_region and self_identification columns of gender table ===");
		responses.select(col("so_region"), col("self_identification")).show();

		System.out.println("=== Print records where the response is from Afghanistan ===");
		responses.filter(col("country").equalTo("Afghanistan")).show();

		System.out.println("=== Print the count of occupations ===");
		RelationalGroupedDataset groupedDataset = responses.groupBy(col("occupation"));
		groupedDataset.count().show();

		System.out.println("=== Cast the salary mid point and age mid point to integer ===");
		Dataset<Row> castedResponse = responses.withColumn(SALARY_MIDPOINT, col(SALARY_MIDPOINT).cast("integer"))
				.withColumn(AGE_MIDPOINT, col(AGE_MIDPOINT).cast("integer"));

		System.out.println("=== Print out casted schema ===");
		castedResponse.printSchema();

		System.out.println("=== Print records with average mid age less than 20 ===");
		castedResponse.filter(col(AGE_MIDPOINT).$less(20)).show();

		System.out.println("=== Print the result by salary middle point in descending order ===");
		castedResponse.orderBy(col(SALARY_MIDPOINT).desc()).show();

		System.out.println(
				"=== Group by country and aggregate by average salary middle point and max age middle point ===");
		RelationalGroupedDataset datasetGroupByCountry = castedResponse.groupBy("country");
		datasetGroupByCountry.agg(avg(SALARY_MIDPOINT), max(AGE_MIDPOINT)).show();

		Dataset<Row> responseWithSalaryBucket = castedResponse.withColumn(SALARY_MIDPOINT_BUCKET, col(SALARY_MIDPOINT).divide(20000).cast("integer").multiply(20000));

		System.out.println("=== With salary bucket column ===");
		responseWithSalaryBucket.select(col(SALARY_MIDPOINT), col(SALARY_MIDPOINT_BUCKET)).show();

		System.out.println("=== Group by salary bucket ===");
		responseWithSalaryBucket.groupBy(SALARY_MIDPOINT_BUCKET).count().orderBy(col(SALARY_MIDPOINT_BUCKET)).show();
		
		sparkSession.stop();
	}
}
