package de.awtools.registration.register;

import java.util.function.Function;

/**
 * Transformiert ein Validationsergebnis in was anderes ... :-)
 *
 * @param <RESULT>
 */
public class ValidationResultMapper<RESULT> {

    private final Validation validation;
    private Function<Validation, RESULT> positiveMapper;

    private ValidationResultMapper(Validation validation) {
        this.validation = validation;
    }

    public static <RESULT> ValidationResultMapper<RESULT> of(Validation validation) {
        return new ValidationResultMapper(validation);
    }

    public NegativeMapper<RESULT> ifValid(Function<Validation, RESULT> function) {
        this.positiveMapper = function;
        return new NegativeMapper<>(this);
    }

    public static class NegativeMapper<RESULT> {
        private ValidationResultMapper<RESULT> validationResultMapper;
        private NegativeMapper(ValidationResultMapper validationResultMapper) {
            this.validationResultMapper = validationResultMapper;
        }

        public RESULT orElse(Function<Validation, RESULT> negativeMapper) {
            if (validationResultMapper.validation.ok()) {
                return validationResultMapper.positiveMapper.apply(validationResultMapper.validation);
            }

            return negativeMapper.apply(validationResultMapper.validation);
        }
    }

}
