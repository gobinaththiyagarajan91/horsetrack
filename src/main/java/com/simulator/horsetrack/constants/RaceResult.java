package com.simulator.horsetrack.constants;

public enum RaceResult {
    WON("won"), LOST("lost");

    private String result;

    public String getResult()
    {
        return this.result;
    }
    
    private RaceResult(String action)
    {
        this.result = action;
    }
}
