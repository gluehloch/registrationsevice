package de.awtools.registration.register;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegistrationValidationJson {

    private final String nickname;
    private final String applicationName;
    private final Set<DefaultRegistrationValidation.ValidationCode> validationCodes = new HashSet<>();

    public static RegistrationValidationJson of(final Validation rv) {
        return new RegistrationValidationJson(
                rv.getNickname(),
                rv.getApplicationName(),
                rv.getValidationCodes());
    }

    private RegistrationValidationJson(final String nickname,
            final String applicationName,
            final Set<DefaultRegistrationValidation.ValidationCode> validationCodes) {

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

    public Set<DefaultRegistrationValidation.ValidationCode> getValidationCodes() {
        return Collections.unmodifiableSet(validationCodes);
    }

}
