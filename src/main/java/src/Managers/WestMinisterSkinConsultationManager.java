package src.Managers;

import src.Application.MainApp;
import src.Constants.Constants;
import src.Users.Doctor;
import src.Utils.CommonUtils;

import java.text.ParseException;

public class WestMinisterSkinConsultationManager implements SkinConsultationManager {
    private ConsoleManager consoleManager;
    private DataManager dataManager;

    public void run() {
        consoleManager = ConsoleManager.createConsole();
        dataManager = DataManager.getInstance();

        consoleManager.displayConsoleMenu();
        redirectFromOption(consoleManager.getUserSelectedOption());
    }

    private void showMenu() {
        consoleManager.displayConsoleMenu();
        redirectFromOption(consoleManager.getUserSelectedOption());
    }

    private boolean isExitTypedAsInput(String input) {
        return Constants.ConsoleOptions.exit.getValue().contentEquals(input.replace(" ", "").toUpperCase());
    }

    private void redirectFromOption(Constants.ConsoleOptions option) {
        switch (option) {
            case addNewDoctor:
                new AddDoctor().start();
                break;
            case deleteADoctor:
                new DeleteDoctor().deleteOne();
                break;
            case deleteAllDoctors:
                new DeleteDoctor().deleteAll();
                break;
            case showAllDoctors:
                new ViewDoctors().showAllInfo();
                break;
            case factoryResetSystem:
                new FactoryResetSystem().reset();
                break;
            case saveChanges:
                dataManager.saveData();
                consoleManager.printSuccess("All the changes have been saved!");
                break;
            case launchApplication:
//                consoleManager.printWarning("GUI implementation is yet to come!");
                new MainApp();
                break;
            default:
                if (dataManager.anyChanges()) {
                    consoleManager.printWarning("Unsaved Changes detected, Do you want to save changes before exiting?");
                    boolean isDone = false;

                    do {
                        System.out.print("\n(yes/ no): ");
                        String input = consoleManager.getUserInput();
                        switch (input.replace(" ", "").toLowerCase()) {
                            case "yes":
                                isDone = true;
                                dataManager.saveData();
                                consoleManager.printSuccess("Changes Saved!");
                                break;
                            case "no":
                                isDone = true;
                                break;
                            default:
                                consoleManager.printErr("Invalid value, try again!");
                        }

                    } while (!isDone);
                }
                System.exit(0);
        }

        showMenu();
    }


    private class AddDoctor {

        private Doctor start() {
            if (dataManager.getDoctorCount() == 10) {
                consoleManager.printErr("Maximum of 10 Doctors are added to the system. Please remove an existing Doctor to add a new Doctor!");
                showMenu();
                return null;
            }

            consoleManager.println("Submit required Details to onboard a Doctor, Type \"exit\" to cancel the operation!");
            String licenseNumber = getLicenseNumber();
            if (licenseNumber == null) {
                return null;
            }

            Doctor.Specialisation specialisation = getSpecialisation();
            if (specialisation == null) {
                return null;
            }

            String name = getName();
            if (name == null) {
                return null;
            }

            String surName = getSurname();
            if (surName == null) {
                return null;
            }

            String email = getEmail();
            if (email == null) {
                return null;
            }

            String contactNumber = getContactNumber();
            if (contactNumber == null) {
                return null;
            }

            String dateOfBirth = getDateOfBBirth();
            if (dateOfBirth == null) {
                return null;
            }

            Doctor doctor = new Doctor();
            doctor.setLicenseNumber(licenseNumber);
            doctor.setSpecialisation(specialisation);
            doctor.setName(name);
            doctor.setSurName(surName);
            doctor.setEmail(email);
            doctor.setContactNumber(contactNumber);
            doctor.setDateOfBirth(dateOfBirth);

            dataManager.addNewDoctor(doctor);
            consoleManager.printSuccess("New Doctor has been added!");
            return doctor;
        }

        private String getLicenseNumber() {
            String licenseNumber;
            while (true) {
                licenseNumber = consoleManager.createCustomUserInput("Enter License Number: ");
                if (isExitTypedAsInput(licenseNumber)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return null;
                } else if (dataManager.hasDoctorWithLicenseNumber(licenseNumber)) {
                    consoleManager.printErr("License number already exists!");
                } else {
                    break;
                }
            }
            return licenseNumber;
        }

        private Doctor.Specialisation getSpecialisation() {
            String specialisation;
            Doctor.Specialisation type;

            consoleManager.println("These are the available Specialisations [");
            for (Doctor.Specialisation value : Doctor.Specialisation.values()) {
                consoleManager.println("    " + value.getSpecialisation() + ",");
            }
            consoleManager.println("]");

            while (true) {
                specialisation = consoleManager.createCustomUserInput("Enter Doctor Specialisation: ");
                type = Doctor.Specialisation.findType(specialisation);

                if (isExitTypedAsInput(specialisation)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return null;
                } else if (type == null) {
                    consoleManager.printErr("Invalid Specialisation!");
                } else {
                    break;
                }
            }
            return type;
        }

        private String getName() {
            String name;
            while (true) {
                name = consoleManager.createCustomUserInput("Enter Doctor Name: ");
                if (isExitTypedAsInput(name)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return null;
                } else if (CommonUtils.isEmptyString(name)) {
                    consoleManager.printErr("Name cannot be empty!");
                } else if (CommonUtils.numbersOnly(name)) {
                    consoleManager.printErr("Name cannot be only digits!");
                } else {
                    break;
                }
            }
            return name;
        }

        private String getSurname() {
            String name;
            while (true) {
                name = consoleManager.createCustomUserInput("Enter Doctor Surname: ");
                if (isExitTypedAsInput(name)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return null;
                } else if (CommonUtils.isEmptyString(name)) {
                    consoleManager.printErr("Surname cannot be empty!");
                } else if (CommonUtils.numbersOnly(name)) {
                    consoleManager.printErr("Surname cannot be only digits!");
                } else {
                    break;
                }
            }
            return name;
        }

        private String getEmail() {
            String email;
            while (true) {
                email = consoleManager.createCustomUserInput("Enter Email: ");
                if (isExitTypedAsInput(email)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return null;
                } else if (CommonUtils.isEmailValid(email)) {
                    break;
                } else {
                    consoleManager.printErr("Invalid email!");
                }
            }
            return email;
        }

        private String getContactNumber() {
            String contactNumber;
            while (true) {
                contactNumber = consoleManager.createCustomUserInput("Enter Contact Number (94XXXXXXXXX): ");
                if (isExitTypedAsInput(contactNumber)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return null;
                } else if (CommonUtils.isContactNumberValid(contactNumber)) {
                    break;
                } else {
                    consoleManager.printErr("Invalid Contact Number!");
                }
            }
            return contactNumber;
        }

        private String getDateOfBBirth() {
            String dateOfBirth;
            while (true) {
                dateOfBirth = consoleManager.createCustomUserInput("Enter Date Of Birth (" + Constants.DATE_FORMAT + "): ");
                if (isExitTypedAsInput(dateOfBirth)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return null;
                }

                try {
                    CommonUtils.parseDate(dateOfBirth);
                    break;
                } catch (ParseException e) {
                    consoleManager.printErr("Invalid Date or Date Format!");
                }
            }
            return dateOfBirth;
        }
    }


    private class DeleteDoctor {

        private Doctor deleteOne() {
            String licenseNumber = getLicenseNumber();
            if (licenseNumber == null) {
                return null;
            }

            Doctor doctor = dataManager.removeDoctor(licenseNumber);
            consoleManager.printSuccess("Dr. " + doctor.getName() + " has been removed from the system!");
            return doctor;
        }

        private boolean deleteAll() {
            boolean deleteAll = getConfirmation();
            if (!deleteAll) {
                return false;
            }

            dataManager.removeAllDoctors();
            consoleManager.printSuccess("All the doctors and consultations has been removed from the system!");
            return true;
        }

        private String getLicenseNumber() {
            String licenseNumber;
            while (true) {
                licenseNumber = consoleManager.createCustomUserInput("Enter License Number: ");
                if (isExitTypedAsInput(licenseNumber)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return null;
                } else if (dataManager.hasDoctorWithLicenseNumber(licenseNumber)) {
                    break;
                } else {
                    consoleManager.printErr("No Doctor found matching to License number entered!");
                }
            }
            return licenseNumber;
        }

        private boolean getConfirmation() {
            String input;
            while (true) {
                input = consoleManager.createCustomUserInput("Do you want to remove all the doctors (yes/ no): ");
                if (isExitTypedAsInput(input)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return false;
                } else if (CommonUtils.removeWhitespaces(input).toLowerCase().contentEquals(Constants.CONSOLE_VALUE_NO)) {
                    return false;
                } else if (CommonUtils.removeWhitespaces(input).toLowerCase().contentEquals(Constants.CONSOLE_VALUE_YES)){
                    break;
                } else {
                    consoleManager.printErr("Invalid Confirmation!");
                }
            }
            return true;
        }
    }

    private class ViewDoctors {

        private boolean viewAll() {
            boolean showInfo = showAllInfo();
            return showInfo;
        }

        private boolean showAllInfo() {
            consoleManager.println("Found " + dataManager.getDoctorCount() + " Doctors!");
            for (Doctor doctor : dataManager.getDoctors()) {
                consoleManager.println("# " + doctor.getName() + " " + doctor.getSurName() + " [" + doctor.getLicenseNumber() + "]");
            }

            while (true) {
                String licenseNumber = consoleManager.createCustomUserInput("Type License number of doctor to view full info, \"exit\" to go to menu: ");
                if (isExitTypedAsInput(licenseNumber)) {
                    return false;
                } else if (dataManager.hasDoctorWithLicenseNumber(licenseNumber)) {
                    showInfo(licenseNumber);
                } else {
                    consoleManager.printErr("Invalid License number entered!");
                }
            }
        }

        private void showInfo(String licenseNumber) {
            Doctor doctor = dataManager.getDoctor(licenseNumber);
            consoleManager.println("- Name              : " + doctor.getName());
            consoleManager.println("- Surname           : " + doctor.getSurName());
            consoleManager.println("- License Number    : " + doctor.getLicenseNumber());
            consoleManager.println("- Specialisation    : " + doctor.getSpecialisation().getSpecialisation());
            consoleManager.println("- Email             : " + doctor.getEmail());
            consoleManager.println("- Contact Number    : " + doctor.getContactNumber());
            consoleManager.println("- Date Of Birth     : " + doctor.getDateOfBirth());
        }
    }

    private class FactoryResetSystem {

        private boolean reset() {
            boolean deleteAll = getConfirmation();
            if (!deleteAll) {
                return false;
            }

            dataManager.removeAllDoctors();
            dataManager.removeAllPatients();
            consoleManager.printSuccess("Factory Reset completed!");
            return true;
        }

        private boolean getConfirmation() {
            String input;
            while (true) {
                input = consoleManager.createCustomUserInput("Do you want to factory reset the system (yes/ no): ");
                if (isExitTypedAsInput(input)) {
                    consoleManager.println("Operation was cancelled by the user!");
                    return false;
                } else if (CommonUtils.removeWhitespaces(input).toLowerCase().contentEquals(Constants.CONSOLE_VALUE_NO)) {
                    return false;
                } else if (CommonUtils.removeWhitespaces(input).toLowerCase().contentEquals(Constants.CONSOLE_VALUE_YES)){
                    break;
                } else {
                    consoleManager.printErr("Invalid Confirmation!");
                }
            }
            return true;
        }
    }
}
