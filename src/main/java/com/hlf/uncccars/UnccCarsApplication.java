package com.hlf.uncccars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UnccCarsApplication {
	
	static {
		System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
	} 

	public static void main(String[] args) {
		SpringApplication.run(UnccCarsApplication.class, args);
		
	}

}
