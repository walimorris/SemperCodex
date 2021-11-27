package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.Password;
import com.smart.peepingbill.util.constants.PeepingConstants;
import com.smart.peepingbill.util.enums.SpecialCharacters;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the code for password validation for {@link com.smart.peepingbill.PeepingBillApplication}
 */
public class PasswordImpl implements Password {
    private static final Logger LOG = LoggerFactory.getLogger(PasswordImpl.class);

    private String password;

    public PasswordImpl(String password) {
        this.password = password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void voidPassword() {
        this.password = null;
    }

    @Override
    public String isPasswordValid(String password) {
        if (validInitialPropertiesForPassword(password)) {
            return containsValidPasswordCharacters(password);
        }
        return PeepingConstants.INVALID_PASSWORD_ENTRY;
    }

    /**
     * Determines if possible password contains the valid initial characteristics of a possible
     * stored password.
     * @param password : {@link String} password
     * @return boolean
     */
    private boolean validInitialPropertiesForPassword(String password) {
        return StringUtils.isNotEmpty(password) && password.length() > 10 && password.length() <= 128
                && !StringUtils.containsWhitespace(password);
    }

    /**
     * Builds password validation map that contains boolean values for each required properties a
     * password must contain for validation ands returns 'success' if these validation characters
     * exist or a message detailing which properties do not exist.
     * @param password : {@link String} password.
     * @return {@link String} validation message.
     */
    private String containsValidPasswordCharacters(String password) {
        Map<String, Boolean> passwordValidationMap = new HashMap<>(Map.of(PeepingConstants.HAS_SPECIAL_CHARACTERS, Boolean.FALSE,
                PeepingConstants.HAS_NUMBER, Boolean.FALSE, PeepingConstants.HAS_UPPERCASE_CHARACTER, Boolean.FALSE));

        SpecialCharacters[] specialCharacters = SpecialCharacters.values();
        for (char c : password.toCharArray()) {
            if (passwordValidationMap.get(PeepingConstants.HAS_SPECIAL_CHARACTERS) != Boolean.TRUE) {
                for (SpecialCharacters character : specialCharacters) {
                    if (character.toChar() == c) {
                        passwordValidationMap.put(PeepingConstants.HAS_SPECIAL_CHARACTERS, Boolean.TRUE);
                        break;
                    }
                }
            }

            if (StringUtils.isNumeric(String.valueOf(c))) {
                passwordValidationMap.put(PeepingConstants.HAS_NUMBER, Boolean.TRUE);
            }

            if (StringUtils.isAllUpperCase(String.valueOf(c).substring(0,1))) {
                passwordValidationMap.put(PeepingConstants.HAS_UPPERCASE_CHARACTER, Boolean.TRUE);
            }
        }
        return buildValidatePasswordMessage(passwordValidationMap);
    }

    /**
     * Builds detailed validation message based on missing properties required for password validation.
     * @param passwordValidationMap : {@link Map} which maps {@link String} required password validation
     *                              properties to a {@link Boolean} value.
     * @return {@link String} detailed password validation message.
     */
    private String buildValidatePasswordMessage(Map<String, Boolean> passwordValidationMap) {
        StringBuilder passwordValidationMessage = new StringBuilder();
        passwordValidationMap.forEach((property, value) -> {
            if (value.equals(Boolean.FALSE)) {
                switch(property) {
                    case PeepingConstants.HAS_SPECIAL_CHARACTERS:
                        passwordValidationMessage.append(appendToValidationMessage(passwordValidationMessage, PeepingConstants.MISSING_VALID_SPECIAL_CHARACTER_MESSAGE_1,
                                PeepingConstants.MISSING_VALID_SPECIAL_CHARACTER_MESSAGE_2));
                        break;
                    case PeepingConstants.HAS_NUMBER:
                        passwordValidationMessage.append(appendToValidationMessage(passwordValidationMessage, PeepingConstants.MISSING_NUMERIC_VALUE_MESSAGE_1,
                                PeepingConstants.MISSING_NUMERIC_VALUE_MESSAGE_2));
                        break;
                    default:
                        passwordValidationMessage.append(appendToValidationMessage(passwordValidationMessage, PeepingConstants.MISSING_UPPERCASE_CHARACTER_MESSAGE_1,
                                PeepingConstants.MISSING_UPPERCASE_CHARACTER_MESSAGE_2));
                        break;
                }
            }
        });
        LOG.info("Invalid Password creation message created ' {} '", passwordValidationMessage);
        return StringUtils.isNotEmpty(passwordValidationMessage) ? passwordValidationMessage.toString()
                : PeepingConstants.SUCCESS;
    }

    /**
     * Password validation message helper. Helps build password validation message.
     * @param validationMessage current validation message
     * @param message1 possible message to append to current validation message.
     * @param message2 possible message to append to current validation message.
     * @return {@link String} possible message appended to current validation message.
     */
    private String appendToValidationMessage(StringBuilder validationMessage, String message1, String message2 ) {
        validationMessage.append(StringUtils.isNotEmpty(validationMessage) ?
                validationMessage.append(message1) : validationMessage.append(message2));

        return validationMessage.toString();
    }
}
