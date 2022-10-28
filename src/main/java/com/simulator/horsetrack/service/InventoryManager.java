package com.simulator.horsetrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class InventoryManager {

    @Value("#{${denomination.inventory}}")
    private LinkedHashMap<Integer, Integer> denominationInventoryConstant;

    private LinkedHashMap<Integer, Integer> denominationInventory;

    @PostConstruct
    private void init() {
        denominationInventory = new LinkedHashMap<>();
        restockInventory();
    }

    public void restockInventory() {
        denominationInventory.putAll(denominationInventoryConstant);
    }

    public Map<Integer, Integer> getDenominationInventory() {
        return denominationInventory;
    }

}
