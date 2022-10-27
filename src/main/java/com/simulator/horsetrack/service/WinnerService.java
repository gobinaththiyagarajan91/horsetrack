package com.simulator.horsetrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class WinnerService {

    @Value("#{${horse.index}}")
    LinkedHashMap<Integer, String> horseIndex;

    @Value("#{${horse.odds}}")
    Map<String, Integer> horseOdds;

    @Value("#{${horse.winner.status}}")
    Map<String, String> winnerStatus;

    public LinkedHashMap<Integer, String> getHorseIndex() {
        return horseIndex;
    }

    public Map<String, Integer> getHorseAndOdds() {
        return horseOdds;
    }

    public Map<String, String> getWinnerStatus() {
        return winnerStatus;
    }


}
