package de.awtools.registration;

public class RegistrationValidation {

    public enum ValidationCode {
        OK(1), //
        KNOWN_DATA(1000), //
        KNOWN_NICKNAME(1001), //
        KNOWN_MAILADDRESS(1002), //
        ILLEGAL_ARGUMENTS(1003);

        ValidationCode(int code) {
            this.code = code;
        }

        private final int code;

        public int getCode() {
            return code;
        }

    }

    private final ValidationCode validationCode;

    public RegistrationValidation() {
        validationCode = ValidationCode.OK;
    }
    
    public RegistrationValidation(ValidationCode code) {
        this.validationCode = code;
    }

    public ValidationCode getValidationCode() {
        return validationCode;
    }
    
    @Override
    public String toString() {
        return String.format("RegistrationValidationJson code=[%s]", validationCode);
    }

}
