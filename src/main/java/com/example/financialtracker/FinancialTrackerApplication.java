package com.example.financialtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan("com.example")
@EnableScheduling
@EnableTransactionManagement
public class FinancialTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialTrackerApplication.class, args);
	}

}
