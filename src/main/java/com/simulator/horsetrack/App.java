package com.simulator.horsetrack;

import com.simulator.horsetrack.service.Simulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {
    @Autowired
    private Simulator simulator;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... strings) {
        simulator.start();
    }
}
