package de.awtools.registration;

public class RegistrationValidationJson {

    public RegistrationValidationJson() {
    }

    public RegistrationValidationJson(
            RegistrationValidation registrationValidation) {

        this.nickname = registrationValidation.getNickname();
        this.validationCode = registrationValidation.getValidationCode()
                .getCode();
    }

    private String nickname;
    private int validationCode;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

}
