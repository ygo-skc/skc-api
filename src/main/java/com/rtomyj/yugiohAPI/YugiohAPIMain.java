package com.rtomyj.yugiohAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YugiohAPIMain {


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(YugiohAPIMain.class, args);
	}

}
