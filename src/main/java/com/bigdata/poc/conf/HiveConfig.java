package com.bigdata.poc.conf;



import java.io.IOException;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class HiveConfig {
	private static final Logger logger = LoggerFactory.getLogger(HiveConfig.class);

	@Autowired
	private Environment env;

	@Bean(name = "hiveJdbcDataSource")
	@Qualifier("hiveJdbcDataSource")
	public BasicDataSource dataSource() {
		/*System.setProperty("javax.security.auth.useSubjectCredsOnly", "true");
		System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");
		org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
		conf.set("hadoop.security.authentication", "kerberos");
		UserGroupInformation.setConfiguration(conf);
		try {
			UserGroupInformation.loginUserFromKeytab("principle", "keyTabLocation");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Error Occurred while loggin in using kerberos", e);
		}*/
		
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(env.getProperty("hive.url"));
		dataSource.setDriverClassName(env.getProperty("hive.driver-class-name"));
		dataSource.setUsername(env.getProperty("hive.username"));
		dataSource.setPassword(env.getProperty("hive.password"));
		//dataSource.setConnectionProperties("propertyName1=value;propertyName2=value;");
		logger.debug("Hive DataSource");
		return dataSource;
	}

	@Bean(name = "hiveJdbcTemplate")
	public JdbcTemplate hiveJdbcTemplate(@Qualifier("hiveJdbcDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
