package com.simulator.horsetrack.service;

import com.simulator.horsetrack.constants.InputTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HorseTrackService implements SimulationService {

    @Autowired
    private Scanner scanner;

    @Autowired
    private InventoryServiceImpl inventoryService;

    @Autowired
    private WinnerService winnerService;

    @Override
    public void startTrack() {

       while(true){
           printIntialContext();
           String userInput = scanner.nextLine();
           String inputType = inputValidation(userInput);
           if(InputTypes.WINNER.name().equalsIgnoreCase(inputType)){
               int index=extractWinnerIndex(userInput);
               winnerService.setWinner(index);
           }else if(InputTypes.WAGER.name().equalsIgnoreCase(inputType)){
               System.out.println(InputTypes.WAGER.name());
           }else if(InputTypes.RESTOCK.name().equalsIgnoreCase(inputType)){
               System.out.println(InputTypes.RESTOCK.name());
               inventoryService.restockInventory();
           }else if(InputTypes.QUIT.name().equalsIgnoreCase(inputType)){
               break;
           }
       }

    }

    private void printIntialContext(){
        System.out.println("Inventory:");
        inventoryService.getDenominationInventory().entrySet().forEach(a->{
            System.out.println(a.getKey()+","+a.getValue());
        });

        System.out.println("Horses:");
        Map<String, String> winnerStatus = winnerService.getWinnerStatus();
        Map<String, Integer>  horseOdds = winnerService.getHorseAndOdds();

        winnerService.getHorseIndex().entrySet().forEach(a->{
            String value = a.getValue();
            System.out.println(a.getKey()+","+a.getValue()+","+horseOdds.get(value)+","+winnerStatus.get(value));
        });


    }

    private String inputValidation(String input) {

        if (Objects.isNull(input) || input.trim().isEmpty()) {
            return "";
        }
        input = input.trim();
        if ("r".equalsIgnoreCase(input)) {
            return InputTypes.RESTOCK.name();
        } else if ("q".equalsIgnoreCase(input)) {
            return InputTypes.QUIT.name();
        } else if (Pattern.compile("w\\s*\\d", Pattern.CASE_INSENSITIVE).matcher(input).matches()) {
            return InputTypes.WINNER.name();
        }else if(Pattern.compile("\\d\\s*\\d").matcher(input).matches()){
            return InputTypes.WAGER.name();
        }
        return "";

    }

    public int extractWinnerIndex(String userInput){
        int index = 0;
        Matcher matcher = Pattern.compile("\\d+").matcher(userInput);
        while(matcher.find()){
          index  = index+Integer.parseInt(matcher.group());
        }
        System.out.println("userInput "+userInput);
        System.out.println("Index "+index );
        return index;
    }

}
