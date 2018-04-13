package com.bigdata.poc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bigdata.poc.dao.SparkDao;

@RestController
@RequestMapping("/spark")
public class SparkController {

	@Autowired
	private SparkDao sparkDao;

	@GetMapping("/wordcount")
	public long select() {
		return sparkDao.getWordCount();
	}
}
