package com.smart.peepingbill.util.constants;

/**
 * Constants Utility class for constant values throughout the {@link com.smart.peepingbill.PeepingBillApplication}.
 */
public final class PeepingConstants {
    public static final String USER_DATABASE = "smart";
    public static final String USER_COLLECTION = "users";
    public static final String USER_UNAVAILABLE = "user_unavailable";
    public static final String CREATE_USER_VIEW = "create-user.fxml";
    public static final String CREATE_USER_VIEW_TITLE = "Create User";
    public static final String PASSWORD_MISMATCH = "password_mismatch";
    public static final String MISSING_PROPERTY = "missing_property";
    public static final String HAS_SPECIAL_CHARACTERS = "hasSpecialCharacters";
    public static final String HAS_UPPERCASE_CHARACTER = "hasUpperCaseCharacter";
    public static final String HAS_NUMBER = "hasNumber";
    public static final String INVALID_PASSWORD_ENTRY = "invalid_password_entry";
    public static final String SUCCESS = "success";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String CREATE_USER = "create_user";
    public static final String LOGIN_USER = "login_user";
    public static final String CURRENT_DIRECTORY = ".";
    public static final String EMPTY_STRING = "";
    public static final String TEST_PASS_FAIL_1 = "testpassword";
    public static final String TEST_PASS_FAIL_2 = "testpassfail";
    public static final String TEST_PASS_SUCCESS_1 = "test$Password1";
    public static final String TEST_PASS_SUCCESS_2 = "!THIS2ShallPass";
    public static final String TEST_USER_NAME_SUCCESS_1 = "testUserPass";

    // application path from content root
    public static final String APPLICATION_PROPERTIES = "src/main/resources/application.properties";
    public static final String MONGO_CONNECTION_STRING = "MONGOCONNECTIONSTR";
    public static final String MISSING_VALID_SPECIAL_CHARACTER_MESSAGE_1 = "_missing valid special character";
    public static final String MISSING_VALID_SPECIAL_CHARACTER_MESSAGE_2 = "missing valid special character";
    public static final String MISSING_NUMERIC_VALUE_MESSAGE_1 = "_missing numeric value";
    public static final String MISSING_NUMERIC_VALUE_MESSAGE_2 = "missing numeric value";
    public static final String MISSING_UPPERCASE_CHARACTER_MESSAGE_1 = "_missing uppercase character";
    public static final String MISSING_UPPERCASE_CHARACTER_MESSAGE_2 = "missing uppercase character";

    /**
     * Prevents native class from calling this constructor. The caller references constants using
     * <tt>PeepingConstants.SUCCESS</tt> etc.
     */
    private PeepingConstants() {
        throw new AssertionError();
    }
}
