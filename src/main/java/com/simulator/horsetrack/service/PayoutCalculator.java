package com.simulator.horsetrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PayoutCalculator {

    @Value("#{${horse.odds}}")
    private Map<String, Integer> horseOdds;

    private Map<Integer, Integer> denominationInventory;

    public Map<String, Integer> getHorseAndOdds() {
        return horseOdds;
    }

    public PayoutCalculator setDenominationInventory(Map<Integer, Integer> denominationInventory) {
        this.denominationInventory = denominationInventory;
        return this;
    }

    public Map<Integer, Integer> dispenseCash(int totalAmountAfterWin) {

        Map<Integer, Integer> resultMap = new HashMap<>();
        Map<Integer, Integer> sortedCashInventory = new TreeMap<>(Collections.reverseOrder());
        sortedCashInventory.putAll(denominationInventory);
        Map<Integer, Integer> tempMap = new LinkedHashMap<>();
        tempMap.putAll(denominationInventory);

        if (totalAmountAfterWin <= getTotalInventoryCash()) {
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
                            value = value - 1;
                            denominationInventory.put(key, value);
                            resultMap.put(key, resultMap.get(key) != null ? resultMap.get(key) + 1 : 1);
                            totalAmountAfterWin = totalAmountAfterWin - (key);
                        }
                    }
                }
            }
            if (totalAmountAfterWin > 0) {
                denominationInventory.putAll(tempMap);
                resultMap.clear();
            }
        }
        return resultMap;
    }

    private int getTotalInventoryCash() {
        AtomicInteger totalSum = new AtomicInteger();
        denominationInventory.entrySet().forEach(a ->
                totalSum.set(totalSum.get() + (a.getKey() * a.getValue()))
        );
        return totalSum.get();
    }
}
