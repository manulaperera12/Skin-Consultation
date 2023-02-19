package src.Managers;

import src.Constants.Constants;
import src.Consultation.Consultation;
import src.Users.Doctor;
import src.Users.Patient;
import src.Utils.CommonUtils;
import src.Utils.JsonUtils;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class DataManager {
    private DataManager() {}

    private static Map<String, Doctor> doctors;
    private static Map<String, Patient> patients;
    private static List<Consultation> consultations;
    private static boolean anyChanges;

    private static DataManager INSTANCE;

    public static synchronized DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
            INSTANCE.loadData();
        }
        return INSTANCE;
    }

    {
        doctors = new HashMap<>();
        patients = new HashMap<>();
        consultations = new ArrayList<>();
        anyChanges = false;
    }

    private void loadData() {
        File file = new File(Constants.DATA_FILE_PATH);

        if (file.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                try {
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        stringBuilder.append(line);
                    }

                    convertToSmartObjects(JsonUtils.readJsonAsMap(stringBuilder.toString()));

                    reader.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            saveData();
        }
    }

    private void convertToSmartObjects(Map<String, Object> data) {
        Map<String, Object> dataSet = (Map<String, Object>) data.get(DataTypes.Doctors.getType());
        dataSet.forEach((k, v) -> {
            doctors.put(k, new Doctor((Map<String, Object>) v));
        });

        dataSet = (Map<String, Object>) data.get(DataTypes.Patients.getType());
        dataSet.forEach((k, v) -> {
            patients.put(k, new Patient((Map<String, Object>) v));
        });

        ((List<Map<String, Object>>) data.get(DataTypes.Consultations.getType())).forEach(v -> {
            consultations.add(new Consultation(v));
        });
    }

    public void saveData() {
        Map<String, Object> data = new HashMap<>();
        data.put(DataTypes.Doctors.getType(), doctors);
        data.put(DataTypes.Patients.getType(), patients);
        data.put(DataTypes.Consultations.getType(), consultations);
        try{
            File file = new File(Constants.DATA_FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(JsonUtils.toJsonFromObject(data).getBytes());
            fos.flush();
            fos.close();

            anyChanges = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getDoctorCount() {
        return doctors.size();
    }

    public int getPatientCount() {
        return patients.size();
    }

    public int getConsultationCount() {
        return consultations.size();
    }

    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors.values());
    }

    public List<Patient> getPatients() {
        return new ArrayList<>(patients.values());
    }

    public List<Consultation> getConsultations() {
        return consultations;
    }

    public Doctor getDoctor(String licenseNumber) {
        return doctors.get(licenseNumber);
    }

    public Patient getPatient(String id) {
        return patients.get(id);
    }

    public void addNewDoctor(Doctor doctor) {
        if (doctors.size() == 10) {
            throw new RuntimeException("Maximum number of doctors are allocated, Please remove an existing doctor to add a new doctor!");
        } else if (doctor.anyFieldNull()) {
            throw new RuntimeException("Required information are missing about the doctor, Please provide them and continue!");
        } else if (doctors.containsKey(doctor.getLicenseNumber())) {
            throw new RuntimeException("Doctor with license number [" + doctor.getLicenseNumber() + "] is already registered in the system!");
        } else if (!CommonUtils.isContactNumberValid(doctor.getContactNumber())) {
            throw new RuntimeException("Contact number is not in valid format. Please enter the number starting with 94!");
        } else if (!CommonUtils.isEmailValid(doctor.getEmail())) {
            throw new RuntimeException("Email is not in valid format!");
        }

        doctors.put(doctor.getLicenseNumber(), doctor);
        anyChanges = true;
    }

    public void addNewPatient(Patient patient) {
        if (patient.anyFieldNull()) {
            throw new RuntimeException("Required information are missing about the patient, Please provide them and continue!");
        } else if (!CommonUtils.isContactNumberValid(patient.getContactNumber())) {
            throw new RuntimeException("Contact number is not in valid format. Please enter the number starting with 94!");
        } else if (!CommonUtils.isEmailValid(patient.getEmail())) {
            throw new RuntimeException("Email is not in valid format!");
        }

        try {
            CommonUtils.parseDate(patient.getDateOfBirth());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid Date or Date Format!");
        }

        patient.setId(String.valueOf(patients.size() + 1));

        patients.put(patient.getId(), patient);
        anyChanges = true;
    }

    public void addNewConsultation(Consultation consultation) {
        if (consultation.anyFieldNull(false)) {
            throw new RuntimeException("Required information are missing about the consultation, Please provide them and continue!");
        }

        try {
            Date date = CommonUtils.parseDate(consultation.getDate());
            if (CommonUtils.isTodayDate(date) || CommonUtils.isFutureDate(date)) {

            } else {
                throw new RuntimeException("Date must be today or future date!");
            }
        } catch (ParseException e) {
            throw new RuntimeException("Invalid Date!");
        }

        consultations.forEach(consultationData-> {
            if (consultationData.getDoctor().getLicenseNumber().contentEquals(consultation.getDoctor().getLicenseNumber()) &&
                consultationData.getDate().contentEquals(consultation.getDate()) &&
                consultationData.getTimeSlot().contentEquals(consultation.getTimeSlot())
            ) {
                throw new RuntimeException("Doctor has an appointment on selected time slot. Please select different time slot!");
            } else if (consultationData.getDoctor().getLicenseNumber().contentEquals(consultation.getDoctor().getLicenseNumber()) &&
                    consultationData.getPatient().getId().contentEquals(consultation.getPatient().getId()) &&
                    consultationData.getCost() != 15
            ) {
                consultation.setCost(25);
            }
        });

        consultation.setNotes(CommonUtils.encrypt(consultation.getNotes()));

        consultations.add(consultation);
        anyChanges = true;
    }

    public boolean hasDoctorWithLicenseNumber(String licenseNumber) {
        return doctors.containsKey(licenseNumber);
    }

    public Doctor removeDoctor(String licenseNumber) {
        if (hasDoctorWithLicenseNumber(licenseNumber)) {
            Doctor doctor = getDoctor(licenseNumber);
            removeConsultationsByDoctor(doctor);
            doctors.remove(licenseNumber);
            anyChanges = true;
            return doctor;
        } else {
            throw new RuntimeException("No Doctor found with License Number [" + licenseNumber + "]!");
        }
    }

    public void removeConsultationsByDoctor(Doctor doctor) {
        int preCount = consultations.size();
        Iterator<Consultation> iterator = consultations.iterator();
        while (iterator.hasNext()) {
            Consultation consultation = iterator.next();
            if (consultation.getDoctor().getLicenseNumber().contentEquals(doctor.getLicenseNumber())) {
                iterator.remove();
            }
        }

        if (preCount != consultations.size()) {
            anyChanges = true;
        }
    }

    public void removeConsultation(Consultation consultation) {
        Iterator<Consultation> iterator = consultations.iterator();
        while (iterator.hasNext()) {
            Consultation consultationData = iterator.next();
            if (consultationData.getDoctor().getLicenseNumber().contentEquals(consultation.getDoctor().getLicenseNumber()) &&
                consultationData.getPatient().getId().contentEquals(consultation.getPatient().getId()) &&
                consultationData.getDate().contentEquals(consultation.getDate()) &&
                consultationData.getTimeSlot().contentEquals(consultation.getTimeSlot())
            ) {
                iterator.remove();
                anyChanges = true;
            }
        }
    }

    public void removeAllDoctors() {
        doctors.clear();
        consultations.clear();
        anyChanges = true;
    }

    public void removeAllConsultations() {
        consultations.clear();
        anyChanges = true;
    }

    public void removeAllPatients() {
        patients.clear();
        consultations.clear();
        anyChanges = true;
    }

    public boolean anyChanges() {
        return anyChanges;
    }
}

enum DataTypes {
    Doctors("Doctors"),
    Patients("Patients"),
    Consultations("Consultations");

    private final String type;
    DataTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
