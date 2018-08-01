package com.phr.tx.service.message;

import org.springframework.boot.SpringApplication;
import com.phr.tx.service.message.config.ApplicationConfig;


public class ApplicationStart {

	public static void main(String[] args) throws Exception{
		SpringApplication.run(ApplicationConfig.class, args);
	}
}
