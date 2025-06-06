package com.image.autocaption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AutocaptionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutocaptionApplication.class, args);
	}

}
