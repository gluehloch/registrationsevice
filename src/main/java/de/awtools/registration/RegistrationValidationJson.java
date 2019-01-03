package de.awtools.registration;

public class RegistrationValidationJson {

    public RegistrationValidationJson() {
    }

    public RegistrationValidationJson(
            RegistrationValidation registrationValidation) {

        this.validationCode = registrationValidation.getValidationCode()
                .getCode();
    }

    private int validationCode;

    public int getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

}
