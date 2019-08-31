package de.awtools.registration;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RegistrationValidation {

    public enum ValidationCode {
        OK(1),
        KNOWN_DATA(1000),
        KNOWN_NICKNAME(1001),
        KNOWN_MAILADDRESS(1002),
        UNKNOWN_APPLICATION(1003),
        MISSING_ACCEPT_EMAIL(1004),
        MISSING_ACCEPT_COOKIE(1005),
        ILLEGAL_ARGUMENTS(2000);

        ValidationCode(int code) {
            this.code = code;
        }

        private final int code;

        public int getCode() {
            return code;
        }
    }

    private final String nickname;
    private String applicationName;
    private final Set<ValidationCode> validationCodes = new HashSet<>();

    public RegistrationValidation(String nickname) {
        this(nickname, null, null);
    }

    public RegistrationValidation(String nickname, ValidationCode code) {
        this(nickname, null, code);
    }

    public RegistrationValidation(String nickname, String applicationName, ValidationCode code) {
        this.nickname = nickname;
        this.applicationName = applicationName;
        if (code != null) {
            this.validationCodes.add(code);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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
