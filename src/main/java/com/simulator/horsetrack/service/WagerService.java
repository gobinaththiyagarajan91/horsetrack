package com.simulator.horsetrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
public class WagerService {

    @Value("#{${horse.odds}}")
    private Map<String, Integer> horseOdds;

    private Map<Integer, Integer> denominationInventory;

    public Map<String, Integer> getHorseAndOdds() {
        return horseOdds;
    }

    public WagerService setDenominationInventory(Map<Integer, Integer> denominationInventory) {
        this.denominationInventory = denominationInventory;
        return this;
    }

    public Map<Integer, Integer> dispenceCash(int totalAmountAfterWin, int availableCash) {

        Map<Integer, Integer> resultMap = new HashMap<>();

        Map<Integer, Integer> sortedCashInventory = new TreeMap<>(Collections.reverseOrder());
        sortedCashInventory.putAll(denominationInventory);

        if (totalAmountAfterWin < availableCash) {
            for (Map.Entry<Integer, Integer> a : sortedCashInventory.entrySet()) {
                if (totalAmountAfterWin > 0) {
                    int temp = totalAmountAfterWin / a.getKey();
                    int totalValue = a.getKey() * a.getValue();
                    if (totalValue >= temp) {
                        denominationInventory.put(a.getKey(), a.getValue() - temp);
                        resultMap.put(a.getKey(), temp);
                    } else {
                        resultMap.clear();
                        break;
                    }
                    totalAmountAfterWin = totalAmountAfterWin - temp * a.getKey();
                }
            }
        }
        return resultMap;
    }


}
