package com.rtomyj.yugiohAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Contains main method.
 */
@SpringBootApplication
@EnableScheduling
public class YugiohAPIMain {


	/**
	 * Inits the program. Main method for program.
	 * @param args Java arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(YugiohAPIMain.class, args);
	}

}
