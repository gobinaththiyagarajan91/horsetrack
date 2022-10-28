package com.simulator.horsetrack.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class WinnerService {

    @Value("#{${horse.index}}")
    private LinkedHashMap<Integer, String> horseIndex;



    @Value("#{${horse.winner.status}}")
    private Map<String, String> winnerStatusConstant;

    private Map<String, String> winnerStatus;

    private String previousWinner;

    @PostConstruct
    private void init(){
        winnerStatus = new HashMap<>();
        winnerStatus.putAll(winnerStatusConstant);
        winnerStatus.entrySet().forEach(a->{
            if("won".equals(winnerStatus.get(a.getKey()))){
                previousWinner = a.getKey();
            }
        });
    }

    public LinkedHashMap<Integer, String> getHorseIndex() {
        return horseIndex;
    }

    public Map<String, String> getWinnerStatus() {
        return winnerStatus;
    }

    public String getPreviousWinner() {
        return previousWinner;
    }

    public void setWinner(int index){
        if(index >0 && index<horseIndex.size()){
            String horseName = horseIndex.get(index);
            winnerStatus.put(horseName,"won");
            if(Objects.nonNull(previousWinner)){
                winnerStatus.put(previousWinner,"lost");
            }
            previousWinner = horseName;
        }else{
            System.out.println("Invalid Horse Number: "+index);
        }

    }


}
