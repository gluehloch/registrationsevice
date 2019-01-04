package de.awtools.registration;

public class RegistrationValidation {

    public enum ValidationCode {
        OK(1), //
        KNOWN_DATA(1000), //
        KNOWN_NICKNAME(1001), //
        KNOWN_MAILADDRESS(1002), //
        UNKNOWN_APPLICATION(1003), //
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
    private final ValidationCode validationCode;

    public RegistrationValidation(String nickname) {
        this.nickname = nickname;
        this.validationCode = ValidationCode.OK;
    }

    public RegistrationValidation(String nickname, ValidationCode code) {
        this.nickname = nickname;
        this.validationCode = code;
    }

    public String getNickname() {
        return nickname;
    }

    public ValidationCode getValidationCode() {
        return validationCode;
    }

    @Override
    public String toString() {
        return String.format(
                "RegistrationValidationJson nickname=[%s], code=[%s]",
                nickname, validationCode);
    }

}
