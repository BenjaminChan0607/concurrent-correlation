package com.cs.struc.concurrentcorrelation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.cs")
@SpringBootApplication
public class ConcurrentCorrelationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrentCorrelationApplication.class, args);
	}
}
