package com.smart.peepingbill.models.impl;

import com.smart.peepingbill.models.impl.PasswordImpl;
import com.smart.peepingbill.util.constants.PeepingConstants;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class PasswordImplTest {
    private static final String[] ALL_PASSWORD_MISSING_PROPERTY_MESSAGES = {PeepingConstants.MISSING_VALID_SPECIAL_CHARACTER_MESSAGE_2, PeepingConstants.MISSING_UPPERCASE_CHARACTER_MESSAGE_2,
            PeepingConstants.MISSING_NUMERIC_VALUE_MESSAGE_2};

    PasswordImpl testPasswordSuccess1 = new PasswordImpl(PeepingConstants.TEST_PASS_SUCCESS_1);
    PasswordImpl testPasswordSuccess2 = new PasswordImpl();
    PasswordImpl testPasswordFail1 = new PasswordImpl(PeepingConstants.TEST_PASS_FAIL_1);
    PasswordImpl testPasswordFail2 = new PasswordImpl(PeepingConstants.TEST_PASS_FAIL_2);
    List<PasswordImpl> passwordList = List.of(testPasswordSuccess1, testPasswordSuccess2, testPasswordFail1, testPasswordFail2);


    @Test
    void setPassword() {
        testPasswordSuccess2.setPassword(PeepingConstants.TEST_PASS_SUCCESS_2);
        Assertions.assertEquals(testPasswordSuccess2.getPassword(), PeepingConstants.TEST_PASS_SUCCESS_2);
    }

    @Test
    void getPassword() {
        // Set password success 2
        testPasswordSuccess2.setPassword(PeepingConstants.TEST_PASS_SUCCESS_2);

        Assertions.assertEquals(testPasswordSuccess1.getPassword(), PeepingConstants.TEST_PASS_SUCCESS_1);
        Assertions.assertEquals(testPasswordSuccess2.getPassword(), PeepingConstants.TEST_PASS_SUCCESS_2);
        Assertions.assertEquals(testPasswordFail1.getPassword(), PeepingConstants.TEST_PASS_FAIL_1);
        Assertions.assertEquals(testPasswordFail2.getPassword(), PeepingConstants.TEST_PASS_FAIL_2);
    }

    @Test
    void isPasswordValid() {
        // Set password success 2
        testPasswordSuccess2.setPassword(PeepingConstants.TEST_PASS_SUCCESS_2);

        String testSuccess1 = testPasswordSuccess1.isPasswordValid(testPasswordSuccess1.getPassword());
        String testSuccess2 = testPasswordSuccess2.isPasswordValid(testPasswordSuccess2.getPassword());
        String testFail1 = testPasswordFail1.isPasswordValid(testPasswordFail1.getPassword());
        String testFail2 = testPasswordFail2.isPasswordValid(testPasswordFail2.getPassword());
        Assertions.assertTrue(StringUtils.equals(testSuccess1, PeepingConstants.SUCCESS));
        Assertions.assertTrue(StringUtils.equals(testSuccess2, PeepingConstants.SUCCESS));

        // test that invalid passwords contains the reasons
        String[] invalidPasswordMessagesAll = ALL_PASSWORD_MISSING_PROPERTY_MESSAGES;

        for (String message : invalidPasswordMessagesAll) {
            Assertions.assertTrue(StringUtils.contains(testFail1, message));
        }

        Assertions.assertTrue(StringUtils.containsAny(testFail2, invalidPasswordMessagesAll));
    }

    @Test
    void voidPassword() {
        testPasswordSuccess1.voidPassword();
        testPasswordSuccess2.voidPassword();
        testPasswordFail1.voidPassword();
        testPasswordFail2.voidPassword();
        passwordList.forEach(( password ) -> Assertions.assertNull(password.getPassword()));
    }
}