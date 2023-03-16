package com.austin.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@SpringBootApplication
@EnableKafka
public class SpringDbApplication {
	private static ApplicationContext applicationContext;
	public static void main( String[] args ) {
		applicationContext = SpringApplication.run(SpringDbApplication.class, args);
		//insertData();
	}
}