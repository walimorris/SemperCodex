package com.smart.peepingbill.util;

import com.smart.peepingbill.util.constants.PeepingConstants;
import org.apache.commons.lang3.StringUtils;

/**
 * Class used to receive reason for various actions on {@link com.smart.peepingbill.PeepingBillApplication}.
 */
public class ReasonUtil {

    // Disallows instantiation of ReasonUtil
    private ReasonUtil(){
        throw new IllegalStateException(String.format("Illegal access to Utility class ' %s '", ReasonUtil.class));
    }

    /**
     * Creates a User Login {@link Reason}.
     * @return {@link Reason}
     */
    public static Reason getLoginUserReason() {
        return new Reason(PeepingConstants.LOGIN_USER);
    }

    /**
     * Creates a Create User {@link Reason}
     * @return {@link Reason}
     */
    public static Reason getCreateUserReason() {
        return new Reason(PeepingConstants.CREATE_USER);
    }


    /**
     * Inner helper class for {@link ReasonUtil} which creates a reason. A reason can be defined
     * as some explanation for subsequent actions. Ex: a login action will require a login reason
     * before processing a login.
     */
    public static class Reason {
        private final String whichReason;

        /**
         * Private constructor for outer class {@link ReasonUtil}.
         * @param reason {@link String}
         */
        private Reason(String reason) {
            this.whichReason = reason;
        }

        /**
         * Is this a login user reason.
         * @return boolean
         */
        public boolean isLoginUserReason() {
            return StringUtils.equals(this.whichReason, PeepingConstants.LOGIN_USER);
        }

        /**
         * Is this a create user reason.
         * @return boolean
         */
        public boolean isCreateUserReason() {
            return StringUtils.equals(this.whichReason, PeepingConstants.CREATE_USER);
        }

        @Override
        public String toString() {
            return this.whichReason;
        }
    }
}
