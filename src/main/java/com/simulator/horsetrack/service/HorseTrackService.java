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

    @Autowired
    private WagerService wagerService;

    @Override
    public void startTrack() {

        while (true) {
            printIntialContext();
            String userInput = scanner.nextLine();
            String inputType = inputValidation(userInput);
            if (InputTypes.WINNER.name().equalsIgnoreCase(inputType)) {
                setWinner(userInput);
            } else if (InputTypes.WAGER.name().equalsIgnoreCase(inputType)) {
                setWaggerBet(userInput);
            } else if (InputTypes.RESTOCK.name().equalsIgnoreCase(inputType)) {
                reStock();
            } else if (InputTypes.QUIT.name().equalsIgnoreCase(inputType)) {
                break;
            } else {
                System.out.println("Invalid input: "+userInput);
            }
        }
    }


    private void reStock(){
        inventoryService.restockInventory();
    }

    private void setWaggerBet(String userInput){
        String[] userInputArray = userInput.split("\\s+", 2);
        if (verifyWinning(userInputArray[0])) {
            calculateBetWinner(userInput, inventoryService.getTotalSum());
        }
    }

    private void setWinner(String userInput) {
        int index = extractWinnerIndex(userInput);
        boolean isSuccessfull = winnerService.setWinner(index);
        if (isSuccessfull) {

        } else {
            System.out.println("Invalid Horse Number: " + index);
        }

    }

    private boolean verifyWinning(String betIndex) {
        System.out.println("winnerService.getHorseIndex() " + winnerService.getHorseIndex().size());
        String horseName = winnerService.getHorseIndex().get(Integer.parseInt(betIndex.trim()));
        boolean matchBetweenBetAndWon = "won".equals(winnerService.getWinnerStatus().get(horseName));
        if (!matchBetweenBetAndWon) {
            System.out.println("No Payout: " + horseName);
        }
        return matchBetweenBetAndWon;
    }

    private void calculateBetWinner(String userInput, int totalInventoryCash) {

        String[] userInputArray = userInput.split("\\s+", 2);

        String horseBetName = winnerService.getHorseIndex().get(Integer.parseInt(userInputArray[0].trim()));

        Map<String, Integer> horseOdds = wagerService.getHorseAndOdds();

        int totalAmountAfterWin = Integer.parseInt(userInputArray[1]) * horseOdds.get(horseBetName);

        Map<Integer, Integer> denominationInventory = inventoryService.getDenominationInventory();

        Map<Integer, Integer> resultMap = wagerService.setDenominationInventory(denominationInventory).
                dispenceCash(totalAmountAfterWin, totalInventoryCash);

        if (resultMap.isEmpty()) {
            System.out.println("Insufficient Funds:  " + totalAmountAfterWin);
        } else {
            System.out.println("Payout: " + horseBetName + "," + totalAmountAfterWin);
            System.out.println("Dispensing:");
            denominationInventory.entrySet().stream().forEach(a -> {
                if (Objects.nonNull(resultMap.get(a.getKey()))) {
                    System.out.println("$" + a.getKey() + "," + resultMap.get(a.getKey()));
                } else {
                    System.out.println("$" + a.getKey() + "," + 0);
                }
            });
        }
    }


    private void printIntialContext() {
        System.out.println("Inventory:");
        inventoryService.getDenominationInventory().entrySet().forEach(a -> System.out.println("$" + a.getKey() + "," + a.getValue()));

        System.out.println("Horses:");
        Map<String, String> winnerStatus = winnerService.getWinnerStatus();
        Map<String, Integer> horseOdds = wagerService.getHorseAndOdds();

        winnerService.getHorseIndex().entrySet().forEach(a -> {
            String value = a.getValue();
            System.out.println(a.getKey() + "," + a.getValue() + "," + horseOdds.get(value) + "," + winnerStatus.get(value));
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
        } else if (Pattern.compile("w\\s+[0-9]+", Pattern.CASE_INSENSITIVE).matcher(input).matches()) {
            return InputTypes.WINNER.name();
        } else if (Pattern.compile("[0-9]+\\s+[0-9]+").matcher(input).matches()) {
            return InputTypes.WAGER.name();
        }
        return "";
    }

    private int extractWinnerIndex(String userInput) {
        int index = 0;
        Matcher matcher = Pattern.compile("\\d+").matcher(userInput);
        while (matcher.find()) {
            index = index + Integer.parseInt(matcher.group());
        }
        return index;
    }
}
