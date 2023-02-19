package src.Users;

import java.util.Map;

public class Patient extends Person {
    public Patient() {}
    public Patient(Map<String, Object> data) {
        setName(data.getOrDefault("name", "").toString());
        setId(data.getOrDefault("id", "").toString());
        setSurName(data.getOrDefault("surName", "").toString());
        setEmail(data.getOrDefault("email", "").toString());
        setContactNumber(data.getOrDefault("contactNumber", "").toString());
        setDateOfBirth(data.getOrDefault("dateOfBirth", "").toString());
    }
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    protected boolean anySubFieldNull() {
        return false;//getId() == null || getId().replace(" ", "").contentEquals("");
    }
}
