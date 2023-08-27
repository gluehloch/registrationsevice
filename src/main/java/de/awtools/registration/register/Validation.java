package de.awtools.registration.register;

import java.util.Set;

public interface Validation {

    boolean ok();

    String getNickname();

    String getApplicationName();

    Set<DefaultRegistrationValidation.ValidationCode>  getValidationCodes();

    default boolean isBad() {
        return !ok();
    }

    enum ValidationCode {
        // @formatter:off
        OK(1),
        KNOWN_DATA(1000),
        KNOWN_NICKNAME(1001),
        KNOWN_MAIL_ADDRESS(1002),
        UNKNOWN_APPLICATION(1003),
        MISSING_ACCEPT_EMAIL(1004),
        MISSING_ACCEPT_COOKIE(1005),
        UNKNOWN_TOKEN(1006),
        PASSWORD_TOO_SHORT(1007),
        PASSWORD_IS_TOO_SIMPEL(1008),
        NICKNAME_IS_EMPTY(1009),
        EMAIL_IS_EMPTY(1010),
        FIRSTNAME_IS_EMPTY(1011),
        EMAIL_IS_NOT_VALID(1012),
        EMAIL_IS_RESERVED(1013),
        ILLEGAL_ARGUMENTS(2000);
        // @formatter:on

        ValidationCode(int code) {
            this.code = code;
        }

        private final int code;

        public int getCode() {
            return code;
        }
    }

}
