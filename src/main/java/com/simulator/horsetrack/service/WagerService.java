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
                    if (a.getValue() >= temp) {
                        denominationInventory.put(a.getKey(), a.getValue() - temp);
                        resultMap.put(a.getKey(), temp);
                        totalAmountAfterWin = totalAmountAfterWin - (a.getKey() * temp);
                    } else {
                        int key = a.getKey();
                        int value = a.getValue();
                        while (totalAmountAfterWin > key && value > 0) {
                            int decrementValue = value - 1;
                            denominationInventory.put(key, decrementValue);
                            Integer resultValue = resultMap.get(key);
                            if (resultValue != null) {
                                resultMap.put(key, resultMap.get(key) + 1);
                            } else {
                                resultMap.put(key, 1);
                            }
                            totalAmountAfterWin = totalAmountAfterWin - (a.getKey());
                            value--;
                        }
                    }
                }
            }

            if (totalAmountAfterWin > 0) {
                resultMap.clear();
            }
        }
        return resultMap;
    }


}
