package com.simulator.horsetrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WagerService {

    @Value("#{${horse.odds}}")
    private Map<String, Integer> horseOdds;

    public Map<String, Integer> getHorseAndOdds() {
        return horseOdds;
    }




}
