package com.dmsc.libraryserviceapi.configuration;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Getter
@Setter
public class HashingProperties implements Validator {
    public static final String PREFIX = "library.identifier.hashing";

    @NotNull
    private HashingValues type;
    private String key;

    @Override
    public boolean supports(Class<?> clazz) {
        return HashingProperties.class.equals(clazz);
    }

    /**
     * Validates the provided {@code target} object to ensure that the required
     * properties are set correctly based on the hashing type specified.
     *
     * <p>
     * If the hashing type is set to {@link HashingValues#AES} and the key is not
     * provided (i.e., it is empty or null), an error will be added to the {@code errors}
     * object, indicating that the key field is required.
     * </p>
     *
     * @param target the object to validate, expected to be of type {@link HashingProperties}
     * @param errors the {@link Errors} object to hold any validation errors that occur during the process
     */
    @Override
    public void validate(Object target, Errors errors) {
        HashingProperties hashingProperties = (HashingProperties) target;
        if (hashingProperties.getType() == HashingValues.AES && (!StringUtils.hasText(hashingProperties.getKey()))) {
            errors.rejectValue(
                "key",
                "key.empty",
                "When AES hashing is enabled, key fields is required"
            );

        }
    }

    /**
     * Supported Hashing implementation
     */
    enum HashingValues {
        BASE64,
        AES
    }
}
