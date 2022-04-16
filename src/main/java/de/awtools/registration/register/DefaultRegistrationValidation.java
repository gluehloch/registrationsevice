package de.awtools.registration.register;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultRegistrationValidation implements Validation {

    private final String nickname;
    private final String applicationName;
    private final Set<ValidationCode> validationCodes = new HashSet<>();

    public DefaultRegistrationValidation(String nickname, String applicationName) {
        this(nickname, applicationName, null);
    }

    public DefaultRegistrationValidation(String nickname, String applicationName,
                                         ValidationCode code) {
        
        this.nickname = nickname;
        this.applicationName = applicationName;
        if (code != null) {
            this.validationCodes.add(code);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public Set<ValidationCode> getValidationCodes() {
        return Collections.unmodifiableSet(validationCodes);
    }

    public void addValidationCode(ValidationCode validationCode) {
        validationCodes.add(validationCode);
    }

    @Override
    public boolean ok() {
        return validationCodes.size() == 0 || (validationCodes.size() == 1
                && validationCodes.contains(ValidationCode.OK));
    }

    @Override
    public String toString() {
        return String.format(
                "RegistrationValidationJson nickname=[%s], code=[%s]",
                nickname, validationCodes);
    }

}
