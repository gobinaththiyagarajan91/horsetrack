package com.simulator.horsetrack;

import com.simulator.horsetrack.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class App {

	@Autowired
	private SimulationService simulationService;


	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@PostConstruct
	public void start() {
		simulationService.startSimulation();
	}

}
