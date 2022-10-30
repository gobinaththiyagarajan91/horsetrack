package com.simulator.horsetrack.service;

import com.simulator.horsetrack.constants.RaceResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class WinnerManager {

    @Value("#{${horse.index}}")
    private LinkedHashMap<Integer, String> horseIndex;

    @Value("#{${horse.winner.status}}")
    private Map<String, String> winnerStatusConstant;

    private Map<String, String> winnerStatus;

    private String previousWinner;

    @PostConstruct
    private void init() {
        winnerStatus = new HashMap<>();
        winnerStatus.putAll(winnerStatusConstant);
        winnerStatus.entrySet().forEach(a -> {
            if (RaceResult.WON.getResult().equals(winnerStatus.get(a.getKey()))) {
                previousWinner = a.getKey();
            }
        });
    }

    public Map<Integer, String> getHorseIndex() {
        return horseIndex;
    }

    public Map<String, String> getWinnerStatus() {
        return winnerStatus;
    }

    public boolean setWinner(int index) {
        if (index > 0 && index <= horseIndex.size()) {
            String horseName = horseIndex.get(index);
            if (RaceResult.WON.getResult().equals(winnerStatus.get(horseName))) {
                return true;
            }
            winnerStatus.put(horseName, RaceResult.WON.getResult());
            if (Objects.nonNull(previousWinner)) {
                winnerStatus.put(previousWinner, RaceResult.LOST.getResult());
            }
            previousWinner = horseName;
            return true;
        } else {
            return false;
        }

    }
}
