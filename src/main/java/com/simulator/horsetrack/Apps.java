package com.simulator.horsetrack;

import com.simulator.horsetrack.service.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Apps {

	@Autowired
	private Simulator simulationService;


	public static void main(String[] args) {
		SpringApplication.run(Apps.class, args);
	}

	@PostConstruct
	public void start() {
		simulationService.start();
	}

}
