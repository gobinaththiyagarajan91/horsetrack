package com.simulator.horsetrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class Config {
    @Bean
    public Scanner getScanner() {
        return new Scanner(System.in);
    }
}
