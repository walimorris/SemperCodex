package com.smart.peepingbill.util.enums;

/**
 * Defines the SpecialCharacters Enum for class {@link com.smart.peepingbill.models.impl.PasswordImpl}.
 */
public enum SpecialCharacters {
    EXCLAMATION('!'),
    AT('@'),
    MONEY('$'),
    PERCENTAGE('%'),
    CAROT('^'),
    AND('&'),
    ASTERISK('*'),
    PLUS('+'),
    MINUS('-'),
    GREATERTHAN('>'),
    LESSTHAN('<');

    private final char value;

    SpecialCharacters(char c) {
        this.value = c;
    }

    public char toChar() {
        return this.value;
    }
}
