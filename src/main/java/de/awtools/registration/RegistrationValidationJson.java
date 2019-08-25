package de.awtools.registration;

import java.util.Set;

public class RegistrationValidationJson {

    private String nickname;
    private Set<RegistrationValidation.ValidationCode> validationCodes;

    public RegistrationValidationJson() {
    }

    public RegistrationValidationJson(String nickname,
            Set<RegistrationValidation.ValidationCode> validationCodes) {

        this.nickname = nickname;
        this.validationCodes = validationCodes;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
