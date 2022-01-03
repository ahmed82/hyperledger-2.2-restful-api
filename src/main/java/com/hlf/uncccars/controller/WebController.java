package com.hlf.uncccars.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hlf.uncccars.dto.Car;
import com.hlf.uncccars.service.AdminService;

@RestController
@RequestMapping("/api")
public class WebController {
	
	Logger logger = LoggerFactory.getLogger(WebController.class);
	
	@Autowired
	AdminService adminService;
	
	// helper function for getting connected to the gateway
	public static Gateway connect() throws Exception{
		// Load a file system based wallet for managing identities.
		Path walletPath = Paths.get("wallet");
		Wallet wallet = Wallets.newFileSystemWallet(walletPath);
		// load a CCP
		Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

		Gateway.Builder builder = Gateway.createBuilder();
		builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
		return builder.connect();
	} 
	
	@GetMapping("/enrolment")
	public void enrolment() {
		// enrolls the admin and registers the user
				try {
					adminService.EnrollAdmin();
					adminService.RegisterUser();
				} catch (Exception e) {
					System.err.println(e);
				}
	}
	
	
	@GetMapping("/cars")
	public List<Car> getAllCars(){
		return null;
	}
	
	@GetMapping("/car")
	public Car getCar(){
		return null;
	}
	
	@PostMapping("/car")
	public ResponseEntity<String> CreateCar(){
		return ResponseEntity.ok("Hello World!");
	}

}
