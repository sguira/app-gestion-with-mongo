package com.application.mongo.app_e_feray;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;



@SpringBootApplication

public class AppEFerayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppEFerayApplication.class, args);
	}

}
