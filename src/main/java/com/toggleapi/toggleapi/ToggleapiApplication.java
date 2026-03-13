package com.toggleapi.toggleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ToggleapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToggleapiApplication.class, args);
	}

}
