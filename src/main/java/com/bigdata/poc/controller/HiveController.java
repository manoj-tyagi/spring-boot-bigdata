package com.bigdata.poc.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hive")
public class HiveController {
	private static final Logger logger = LoggerFactory.getLogger(HiveController.class);

	@Autowired
	@Qualifier("hiveJdbcTemplate")
	private JdbcTemplate hiveJdbcTemplate;

	@GetMapping("/select")
	public List<Map<String, Object>> select() {
		String sql = "select * from emp";
		List<Map<String, Object>> rows = hiveJdbcTemplate.queryForList(sql);
		return rows;
	}
	
}
