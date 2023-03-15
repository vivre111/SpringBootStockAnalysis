package com.austin.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.kafka.annotation.EnableKafka;

import java.io.FileNotFoundException;
import java.text.ParseException;

@Configuration
@SpringBootApplication
@EnableKafka
public class SpringDbApplication {
	private static ApplicationContext applicationContext;
	public static void main( String[] args ) {
		applicationContext = SpringApplication.run(SpringDbApplication.class, args);
		//insertData();
	}
	private static void insertData(){
		JDBCController jdbcController = (JDBCController) applicationContext.getBean("jDBCController");
		try{
			jdbcController.createTableIfNotExist();
			jdbcController.insertAllCSV(80);
		}catch (FileNotFoundException | ParseException e){
			System.out.println("file not found");
		}
	}
}