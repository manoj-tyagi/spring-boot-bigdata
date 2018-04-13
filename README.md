All the code in the project will work on cloudera quick start VM setup (CDH 5.13). However it can work on any cloudera setup by making few changes.

# spring-boot-bigdata
In order to run the spark code , you need to copy data in hadoop cluster that you want to analyse.
Follow below steps to copy data in hadoop

* Go to cloudera quick start VM and open command prompt
* create data directory on hadoop in /user/cloudera directory
sudo -u cloudera hdfs dfs -mkdir /user/cloudera/data

* copy <<2016-stack-overflow-survey-responses.csv>> file from local directory "spring-boot-bigdata/data" to hadoop
hadoop fs -copyFromLocal spring-boot-bigdata/data/2016-stack-overflow-survey-responses.csv /user/cloudera/data/

# How to run Spark Job
* Go to cloudera quick start VM and open command prompt
* run below command 
spark2-submit --verbose --deploy-mode client --master yarn /home/cloudera/CodeBase/spring-bigdata/target/spring-bigdata.jar
