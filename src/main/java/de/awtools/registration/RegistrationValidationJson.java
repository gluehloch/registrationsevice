package de.awtools.registration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegistrationValidationJson {

    private final String nickname;
    private final String applicationName;
    private final Set<RegistrationValidation.ValidationCode> validationCodes = new HashSet<>();

    public static RegistrationValidationJson of(RegistrationValidation rv) {
        return new RegistrationValidationJson(rv.getNickname(),
                rv.getApplicationName(), rv.getValidationCodes());
    }

    private RegistrationValidationJson(String nickname,
            String applicationName,
            Set<RegistrationValidation.ValidationCode> validationCodes) {

        this.nickname = nickname;
        this.applicationName = applicationName;
        this.validationCodes.addAll(validationCodes);
    }

    public String getNickname() {
        return nickname;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public Set<RegistrationValidation.ValidationCode> getValidationCodes() {
        return Collections.unmodifiableSet(validationCodes);
    }

}
