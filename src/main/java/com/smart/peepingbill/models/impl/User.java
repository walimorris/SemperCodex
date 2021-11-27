package com.smart.peepingbill.models.impl;

import org.bson.types.ObjectId;

/**
 * Defines the POJO used for CRUD operations on database for {@link com.smart.peepingbill.PeepingBillApplication}
 */
public class User {
    private ObjectId id;
    private String user;
    private String password;

    public User() {}

    public User(String userName, String password) {
        this.user = userName;
        this.password = password;
    }

    /**
     * Gets {@link User} object id.
     * @return {@link ObjectId}
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Set {@link User} ObjectId.
     * @param id {@link ObjectId}
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Get username.
     * @return {@link String}
     */
    public String getUserName() {
        return this.user;
    }

    /**
     * Set user-name.
     * @param userName {@link String}
     */
    public void setUserName(String userName) {
        this.user = userName;
    }

    /**
     * Get temporary user password.
     * @return {@link String}
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set temporary user password.
     * @param password {@link String}
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format("User [id = %h, username = %s]", this.id, this.user);
    }
}
