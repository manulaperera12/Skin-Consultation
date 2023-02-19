package src.Consultation;

import src.Managers.DataManager;
import src.Users.Doctor;
import src.Users.Patient;
import src.Utils.CommonUtils;

import java.util.Map;

public class Consultation {
    public Consultation() {}
    public Consultation(Map<String, Object> data) {
        setDoctor(DataManager.getInstance().getDoctor(((Map) data.get("doctor")).getOrDefault("licenseNumber", "").toString()));
        setPatient(DataManager.getInstance().getPatient(((Map) data.get("patient")).getOrDefault("id", "").toString()));
        setDate(data.getOrDefault("date", "").toString());
        setTimeSlot(data.getOrDefault("timeSlot", "").toString());
        setNotes(CommonUtils.decrypt(data.getOrDefault("notes", "").toString()));
        setCost(Double.parseDouble(data.getOrDefault("cost", "0").toString()));
    }

    private Doctor doctor;
    private Patient patient;
    private String date;
    private String timeSlot;
    private String notes;
    private double cost;

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean anyFieldNull(boolean validateCost) {
        return getDoctor() == null || getPatient() == null ||
                getDate() == null || getTimeSlot() == null ||
                getTimeSlot().replace(" ", "").isEmpty() || getNotes() == null ||
                getNotes().replace(" ", "").isEmpty() || (validateCost && getCost() <= 0);
    }
}
