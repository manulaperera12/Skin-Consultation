package src.Managers;

import src.Constants.Constants;

import java.util.HashSet;
import java.util.Scanner;

public class ConsoleManager {
    private ConsoleManager() {
        this.scannerService = new Scanner(System.in);
        this.options = new HashSet<>();

        for (Constants.ConsoleOptions value : Constants.ConsoleOptions.values()) {
            options.add(value.getValue());
        }
    }

    private Scanner scannerService;
    private HashSet<String> options;

    public static ConsoleManager createConsole() {
        return new ConsoleManager();
    }

    public void displayConsoleMenu() {
        System.out.println(
                "\n********************************************************************\n" +
                "                    Skin Consultation Console Menu\n" +
                "********************************************************************");
        displayOptions();
        System.out.println("********************************************************************\n");
    }

    public void displayOptions() {
        System.out.println(
                "\n\n       " + Constants.ConsoleOptions.addNewDoctor.getValue() + " - Add new Doctor\n" +
                "       " + Constants.ConsoleOptions.deleteADoctor.getValue() + " - Delete a Doctor\n" +
                "       " + Constants.ConsoleOptions.deleteAllDoctors.getValue() + " - Delete all Doctors\n" +
                "       " + Constants.ConsoleOptions.showAllDoctors.getValue() + " - Show all Doctors\n" +
                "       " + Constants.ConsoleOptions.factoryResetSystem.getValue() + " - Factory Reset System\n" +
                "       " + Constants.ConsoleOptions.saveChanges.getValue() + " - Save Changes\n" +
                "       " + Constants.ConsoleOptions.launchApplication.getValue() + " - Launch Application\n" +
                "       " + Constants.ConsoleOptions.exit.getValue() + " - Exit Application\n\n"
        );
    }

    public Constants.ConsoleOptions getUserSelectedOption() {
        System.out.print(Constants.CONSOLE_SELECT_OPTION);
        String input = scannerService.nextLine();
        if (!isSelectedOptionValid(input)) {
            while (true) {
                input = createCustomUserInput(Constants.CONSOLE_INVALID_OPTION_RETRY);
                if (isSelectedOptionValid(input)) {
                    break;
                }
            }
        }
        return Constants.ConsoleOptions.convertValueToOption(input);
    }

    public String createCustomUserInput(String inputText) {
        System.out.print(inputText);
        return scannerService.nextLine();
    }

    public String getUserInput() {
        return scannerService.nextLine();
    }

    public void printNewLine() {
        System.out.println();
    }

    public void println(String output) {
        System.out.println(output);
    }

    public void printErr(String errMessage) {
        println("*Error - " + errMessage);
    }

    public void printWarning(String warningMessage) {
        println("*Warning - " + warningMessage);
    }

    public void printSuccess(String successMessage) {
        println("*Success - " + successMessage);
    }

    private boolean isSelectedOptionValid(String input) {
        return options.contains(input.toUpperCase().replace(" ", ""));
    }
}
