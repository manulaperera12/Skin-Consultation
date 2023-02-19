package src.Users;

import src.Utils.CommonUtils;

import java.util.Map;

public class Doctor extends Person {
    public Doctor() {}

    public Doctor(Map<String, Object> data) {
        setName(data.getOrDefault("name", "").toString());
        setLicenseNumber(data.getOrDefault("licenseNumber", "").toString());
        setSurName(data.getOrDefault("surName", "").toString());
        setEmail(data.getOrDefault("email", "").toString());
        setContactNumber(data.getOrDefault("contactNumber", "").toString());
        setDateOfBirth(data.getOrDefault("dateOfBirth", "").toString());
        setSpecialisation(Specialisation.findType(data.getOrDefault("specialisation", "").toString()));
    }

    public enum Specialisation {
        CosmeticDermatology("Cosmetic Dermatology"),
        MedicalDermatology("Medical Dermatology"),
        PaediatricDermatology("Paediatric Dermatology");

        private String type;
        Specialisation(String type) {
            this.type = type;
        }

        public String getSpecialisation() {
            return type;
        }

        public static Specialisation findType(String keyType) {
            keyType = CommonUtils.removeWhitespaces(keyType);
            for (Specialisation value : Specialisation.values()) {
                if (keyType.contentEquals(CommonUtils.removeWhitespaces(value.type))) {
                    return value;
                }
            }
            return null;
        }
    }

    private String licenseNumber;
    private Specialisation specialisation;

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Specialisation getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(Specialisation specialisation) {
        this.specialisation = specialisation;
    }

    @Override
    protected boolean anySubFieldNull() {
        return getSpecialisation() == null || getLicenseNumber() == null || getLicenseNumber().replace(" ", "").contentEquals("");
    }
}
