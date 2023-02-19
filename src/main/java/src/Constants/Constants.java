package src.Constants;

public class Constants {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATA_FILE_PATH = "data.txt";

    public static final String SECRET_MID_CODE = "_cr#t$q_";

    public static final String CONSOLE_SELECT_OPTION = "Select an Option: ";
    public static final String CONSOLE_INVALID_OPTION = "Invalid option selected";
    public static final String CONSOLE_INVALID_OPTION_RETRY = CONSOLE_INVALID_OPTION + ", Please try again: ";
    public static final String CONSOLE_VALUE_YES = "yes";
    public static final String CONSOLE_VALUE_NO = "no";

    public enum ConsoleOptions {
        addNewDoctor("A"),
        deleteADoctor("B"),
        deleteAllDoctors("C"),
        showAllDoctors("D"),
        factoryResetSystem("E"),
        saveChanges("F"),
        launchApplication("LAUNCH"),
        exit("EXIT");

        private String value;
        ConsoleOptions(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static ConsoleOptions convertValueToOption(String option) {
            option = option.replace(" ", "").toUpperCase();
            for (ConsoleOptions consoleOptions : ConsoleOptions.values()) {
                if (consoleOptions.getValue().contentEquals(option)) {
                    return consoleOptions;
                }
            }
            return null;
        }
    }
}
