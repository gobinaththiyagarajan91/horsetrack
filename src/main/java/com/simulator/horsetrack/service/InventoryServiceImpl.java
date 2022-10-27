package com.simulator.horsetrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class InventoryServiceImpl {

    @Value("#{${denomination.inventory}}")
    LinkedHashMap<String, Integer> denominationInventoryConstant;

    LinkedHashMap<String, Integer> denominationInventory;

    public void initializeInventory() {
        denominationInventory = new LinkedHashMap<>();
        denominationInventory.putAll(denominationInventoryConstant);
    }

    public void restockInventory() {
        denominationInventory.putAll(denominationInventoryConstant);
    }

    public Map<String, Integer> getDenominationInventory() {
        return denominationInventory;
    }
}
