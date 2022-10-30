package com.simulator.horsetrack.service;

import com.simulator.horsetrack.constants.InputTypes;
import com.simulator.horsetrack.constants.RaceResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HorseTrack implements Simulator {

    @Autowired
    private Scanner scanner;

    @Autowired
    private InventoryManager inventoryManagerService;

    @Autowired
    private WinnerManager winnerManager;

    @Autowired
    private PayoutCalculator payoutCalculator;

    @Override
    public void start() {
        try {
            while (true) {
                displayInventoryAndWinner();
                String userInput = scanner.nextLine();
                if (Objects.isNull(userInput) || userInput.trim().isEmpty()) {
                    continue;
                }
                userInput = userInput.trim();
                String inputType = inputValidation(userInput);
                if (InputTypes.WINNER.name().equalsIgnoreCase(inputType)) {
                    checkAndSetWinner(userInput);
                } else if (InputTypes.WAGER.name().equalsIgnoreCase(inputType)) {
                    checkWager(userInput);
                } else if (InputTypes.RESTOCK.name().equalsIgnoreCase(inputType)) {
                    reStock();
                } else if (InputTypes.QUIT.name().equalsIgnoreCase(inputType)) {
                    break;
                } else {
                    System.out.println("Invalid Command: " + userInput);
                }
            }
        } catch (RuntimeException ex) {
            System.err.println("Exception has occurred " + ex);
            ex.printStackTrace();
        }
    }

    private void reStock() {
        inventoryManagerService.restockInventory();
    }

    private void checkWager(String userInput) {
        String[] userInputArray = userInput.split("\\s+", 2);
        if (Integer.parseInt(userInputArray[1]) < 1 || Pattern.compile("[0-9]*['. '][0-9]*").matcher(userInputArray[1]).find()) {
            System.out.println("Invalid Bet: " + userInputArray[1]);
            return;
        }

        if (Integer.parseInt(userInputArray[0]) < 1 || Integer.parseInt(userInputArray[0]) > winnerManager.getHorseIndex().size()) {
            System.out.println("Invalid Horse Number: " + userInputArray[0]);
            return;
        }

        if (checkWinner(userInputArray[0])) {
            calculateBetAmount(userInput);
        }
    }

    private void checkAndSetWinner(String userInput) {
        int index = extractWinnerIndex(userInput);
        boolean isSuccessfully = winnerManager.setWinner(index);
        if (!isSuccessfully) {
            System.out.println("Invalid Horse Number: " + index);
        }
    }

    private boolean checkWinner(String betIndex) {
        String horseName = winnerManager.getHorseIndex().get(Integer.parseInt(betIndex.trim()));
        boolean matchBetweenBetAndWon = RaceResult.WON.getResult().equals(winnerManager.getWinnerStatus().get(horseName));
        if (!matchBetweenBetAndWon) {
            System.out.println("No Payout: " + horseName);
        }
        return matchBetweenBetAndWon;
    }

    private void calculateBetAmount(String userInput) {

        String[] userInputArray = userInput.split("\\s+", 2);

        Map<String, Integer> horseOdds = payoutCalculator.getHorseAndOdds();

        Map<Integer, Integer> denominationInventory = inventoryManagerService.getDenominationInventory();

        String horseBetName = winnerManager.getHorseIndex().get(Integer.parseInt(userInputArray[0].trim()));

        int totalAmountAfterWin = Integer.parseInt(userInputArray[1]) * horseOdds.get(horseBetName);

        Map<Integer, Integer> resultMap = payoutCalculator.setDenominationInventory(denominationInventory).
                dispenseCash(totalAmountAfterWin);
        if (resultMap.isEmpty()) {
            System.out.println("Insufficient Funds:  " + totalAmountAfterWin);
        } else {
            System.out.println("Payout: " + horseBetName + ",$" + totalAmountAfterWin);
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


    private void displayInventoryAndWinner() {
        Map<String, String> winnerStatusMap = winnerManager.getWinnerStatus();
        Map<String, Integer> horseOdds = payoutCalculator.getHorseAndOdds();
        System.out.println("Inventory:");
        inventoryManagerService.getDenominationInventory().entrySet().forEach(a -> System.out.println("$" + a.getKey() + "," + a.getValue()));
        System.out.println("Horses:");
        winnerManager.getHorseIndex().entrySet().forEach(a -> {
            String value = a.getValue();
            System.out.println(a.getKey() + "," + a.getValue() + "," + horseOdds.get(value) + "," + winnerStatusMap.get(value));
        });
    }

    private String inputValidation(String input) {
        if ("r".equalsIgnoreCase(input)) {
            return InputTypes.RESTOCK.name();
        } else if ("q".equalsIgnoreCase(input)) {
            return InputTypes.QUIT.name();
        } else if (Pattern.compile("w\\s+[0-9]+", Pattern.CASE_INSENSITIVE).matcher(input).matches()) {
            return InputTypes.WINNER.name();
        } else if (Pattern.compile("[0-9]+\\s+[0-9]\\d*(\\.\\d+)?$").matcher(input).matches()) {
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
