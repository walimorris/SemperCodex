package com.smart.peepingbill.models;

/**
 * Defines the interface for {@link com.smart.peepingbill.models.impl.PasswordImpl} class.
 */
public interface Password {

    /**
     * Set password for validation.
     * @param password : {@link String} password to validate.
     */
    void setPassword(String password);

    /**
     * Get password to validate.
     * @return {@link String}
     */
    String getPassword();

    /**
     * Validate current password.
     * @param password : {@link String} password to validate.
     * @return {@link String}
     */
    String isPasswordValid(String password);

    /**
     * Void and remove current password.
     */
    void voidPassword();
}
