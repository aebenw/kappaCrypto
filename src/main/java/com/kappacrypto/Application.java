package com.kappacrypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Application {


	public static void main(String[] args) {
		// TODO: Deal with threading
		// Create client classes
		//
		SpringApplication.run(Application.class, args);
//		Twitter client = new Twitter();
//		client.getTweets("AltcoinDailyio");
//		client.createStreamRules();
//		client.streamTweets();
	}

}
