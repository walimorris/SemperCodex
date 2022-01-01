package com.smart.peepingbill.util.constants;

/**
 * Constants Utility class for constant values throughout the {@link com.smart.peepingbill.PeepingBillApplication}.
 */
public final class PeepingConstants {
    public static final String USER_DATABASE = "smart";
    public static final String USER_COLLECTION = "users";
    public static final String APPLICATION_SIGNATURE_NAME = "Peep!ng Bill";
    public static final String USER_UNAVAILABLE = "user_unavailable";
    public static final String CREATE_USER_VIEW = "create-user.fxml";
    public static final String LOGIN_VIEW = "login-view.fxml";
    public static final String CREATE_USER_VIEW_TITLE = "Create User";
    public static final String PAD_LANDING_VIEW = "pad-landing-view.fxml";
    public static final String PAD_LANDING_VIEW_TITLE = "Smart Pad Builder";
    public static final String PASSWORD_MISMATCH = "password_mismatch";
    public static final String MISSING_PROPERTY = "missing_property";
    public static final String HAS_SPECIAL_CHARACTERS = "hasSpecialCharacters";
    public static final String HAS_UPPERCASE_CHARACTER = "hasUpperCaseCharacter";
    public static final String HAS_NUMBER = "hasNumber";
    public static final String INVALID_PASSWORD_ENTRY = "invalid_password_entry";
    public static final String SUCCESS = "success";
    public static final String STATUS = "status";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String REGION = "region";
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String LINUX = "Linux";
    public static final String CITY = "city";
    public static final String ZIP = "zip";
    public static final String ISP = "isp";
    public static final String SUBMIT = "submit";
    public static final String MOBILE = "mobile";
    public static final String PORT = "PORT";
    public static final String STATE = "STATE";
    public static final String SERVICE = "SERVICE";
    public static final String PROXY = "proxy";
    public static final String HOSTING = "hosting";
    public static final String CREATE_USER = "create_user";
    public static final String LOGIN_USER = "login_user";
    public static final String CURRENT_DIRECTORY = ".";
    public static final String EMPTY_STRING = "";
    public static final String TEST_PASS_FAIL_1 = "testpassword";
    public static final String TEST_PASS_FAIL_2 = "testpassfail";
    public static final String TEST_PASS_SUCCESS_1 = "test$Password1";
    public static final String TEST_PASS_SUCCESS_2 = "!THIS2ShallPass";
    public static final String TEST_USER_NAME_SUCCESS_1 = "testUserPass";
    public static final String CHECKING_SYSTEM_PROPERTIES = "Checking system properties...";
    public static final String ERROR_GETTING_SYSTEM_PROPERTIES = "Error getting '{}' from system properties: {}";
    public static final String SYSTEM_CHECK_PASS = "System check pass, os: {} {} {}";
    public static final String PEEPING_BILL_LOGIN_SCENE_APPLIED = "PeepingBillApplication Login scene applied";
    public static final String ERROR_CREATING_PRIMARY_STAGE_SCENE = "Error creating Primary Stage Scene from {} Login Page {}: ";
    public static final String SYSTEM_SUPPORT_MESSAGE = "Application currently supports Linux systems";

    // application path from content root
    public static final String APPLICATION_PROPERTIES = "src/main/resources/application.properties";

    public static final String MONGO_CONNECTION_STRING = "MONGOCONNECTIONSTR";
    public static final String MK_QUERY_STR = "MKQUERYSTR";
    public static final String MISSING_VALID_SPECIAL_CHARACTER_MESSAGE_1 = "_missing valid special character";
    public static final String MISSING_VALID_SPECIAL_CHARACTER_MESSAGE_2 = "missing valid special character";
    public static final String MISSING_NUMERIC_VALUE_MESSAGE_1 = "_missing numeric value";
    public static final String MISSING_NUMERIC_VALUE_MESSAGE_2 = "missing numeric value";
    public static final String MISSING_UPPERCASE_CHARACTER_MESSAGE_1 = "_missing uppercase character";
    public static final String MISSING_UPPERCASE_CHARACTER_MESSAGE_2 = "missing uppercase character";
    public static final String EMPTY_USER_CREDENTIAL_TEXT = "*User credentials are empty";
    public static final String EMPTY_PASSWORD_CREDENTIAL_TEXT = "*Password credential empty";
    public static final String EMPTY_USER_CREDENTIALS_TEXT = "*User credentials are empty";
    public static final String INVALID_CREDENTIALS_MESSAGE_1 = "Invalid User or Password credential(s). Try again.";
    public static final String ERROR_SUGGEST_RESTART_MESSAGE_1 = "Unknown error has occurred, suggest restart application";

    // log text/message constants
    public static final String LOG_LOGIN_PROCESSED_MESSAGE = "LoginProcess_login_processed_message: ";
    public static final String LOG_LOGIN_PROCESS_LOGIN_ACTIVATED_MESSAGE = "LoginProcess_login_activated: processing for user ";
    public static final String LOG_CREATE_USER_PROCESS_MESSAGE = "CreateUserProcess_create_user_process_initiated";
    public static final String LOG_ERROR_LOADING_CREATE_USER_SCENE = "Error loading Create User Scene: ";
    public static final String LOG_ERROR_LOADING_PAD_BUILDER_SCENE = "Error loading Pad Builder Scene: ";
    public static final String LOG_LOGIN_CREDENTIAL_ERROR_PREFIX = "LoginProcess_login_credential_error: ";

    // network constants
    public static final String MAC_ADDRESS_1 = "MAC Address";
    public static final String MAC_ADDRESS_2 = "macAddress";
    public static final String GATEWAY = "_gateway";
    public static final String HOST = "host";
    public static final String HOST_SMART_NODE = "hostSmartNode";
    public static final String DEVICE_SMART_NODE = "deviceSmartNode";
    public static final String CLIENT = "client";
    public static final String HOST_NAME = "hostName";
    public static final String IP_ADDRESS = "ipAddress";
    public static final String LOCAL_IP_ADDRESS = "localIpAddress";
    public static final String EXTERNAL_IP_ADDRESS = "externalIpAddress";
    public static final String MAC_LOOKUP_API_URL = "https://api.macaddress.io/v1?apiKey=";
    public static final String MAC_LOOKUP_API_JSON_SUFFIX = "&output=json&search=";
    public static final String AWS_IP_CHECK_ENDPOINT = "https://checkip.amazonaws.com";
    public static final String IP_API_JSON_ENDPOINT = "http://ip-api.com/json/";
    public static final String DEEP_IP_SUFFIX = "?fields=status,message,region,city,zip,isp,org,mobile,proxy,hosting";

    // Mac Vendor Details
    public static final String VENDOR_DETAILS = "vendorDetails";
    public static final String BLOCK_DETAILS = "blockDetails";
    public static final String VENDOR_OUI = "vendorOui";
    public static final String OUI = "oui";
    public static final String VENDOR_IS_PRIVATE = "vendorIsPrivate";
    public static final String IS_PRIVATE = "isPrivate";
    public static final String VENDOR_COMPANY_NAME = "vendorCompanyName";
    public static final String COMPANY_NAME = "companyName";
    public static final String VENDOR_COMPANY_ADDRESS = "vendorCompanyAddress";
    public static final String COMPANY_ADDRESS = "companyAddress";
    public static final String VENDOR_COUNTRY_CODE = "vendorCountryCode";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String BLOCK_FOUND = "blockFound";
    public static final String BORDER_LEFT = "borderLeft";
    public static final String BORDER_RIGHT = "borderRight";
    public static final String BLOCK_SIZE = "blockSize";
    public static final String ASSIGNMENT_BLOCK_SIZE = "assignmentBlockSize";
    public static final String DATE_CREATED = "dateCreated";
    public static final String DATE_UPDATED = "dateUpdated";

    // Node constants
    public static final String SMART_DEVICE_NODE_TYPE = "smartDeviceNodeType";
    public static final String DEVICE = "Device";
    public static final String SMART_DEVICE_NODE_DETAILS = "smartDeviceNodeDetails";
    public static final String SMART_DEVICE_MAC_ADDRESS_DETAILS = "smartDeviceMacAddressDetails";
    public static final String SMART_DEVICE_REGION = "smartDeviceRegion";
    public static final String SMART_DEVICE_CITY = "smartDeviceCity";
    public static final String SMART_DEVICE_ZIP = "smartDeviceZip";
    public static final String SMART_DEVICE_ISP = "smartDeviceIsp";
    public static final String SMART_DEVICE_IS_ON_MOBILE = "isOnMobile";
    public static final String SMART_DEVICE_IS_ON_PROXY = "isOnProxy";
    public static final String SMART_DEVICE_IS_ON_HOST = "isOnHost";
    public static final String SMART_DEVICE_DEEP_NETWORK_PROPERTIES = "smartDeviceDeepNetProperties";

    // operating system constants
    public static final String SMART_DEVICE_SYSTEM_PROPERTIES = "smartDeviceSystemProperties";
    public static final String OPERATING_SYSTEM_PROPERTY = "os.name";
    public static final String SYSTEM_USER_HOME = "user.home";
    public static final String OPERATING_SYSTEM_ARCHITECTURE_PROPERTY = "os.arch";
    public static final String SYSTEM_LANGUAGE = "user.language";
    public static final String OPERATING_SYSTEM_VERSION = "os.version";
    public static final String SYSTEM_USER_COUNTRY = "user.country";
    public static final String SMART_DEVICE_OS = "smartDeviceOperatingSystem";
    public static final String SMART_DEVICE_OS_ARCHITECTURE = "smartDeviceArchitecture";
    public static final String SMART_DEVICE_SYSTEM_LANGUAGE = "smartDeviceLanguage";
    public static final String SMART_DEVICE_OS_VERSION = "smartDeviceVersion";
    public static final String SMART_DEVICE_USER_COUNTRY = "smartDeviceUserCountry";

    // nmap constants
    public static final String NMAP_SCAN_REPORT = "Nmap scan report";
    public static final String NMAP_SCANNING = "Scanning";
    public static final String NMAP_HOST_OS_SCAN_NOT_NECESSARY = "host os scan not necessary";
    public static final String NMAP_ALL_1000_SCANNED_PORTS = "All 1000 scanned ports";
    public static final String NMAP_ARE_CLOSED = "are closed";
    public static final String NMAP_ALL_1000_PORTS_ARE_CLOSED = "All 1000 scanned ports are closed";
    public static final String NMAP_AGGRESSIVE_OS_GUESSES = "Aggressive OS guesses";
    public static final String NMAP_WARNING = "Warning";
    public static final String NMAP_TOO_MANY_FINGERPRINTS_MATCH = "Too many fingerprints match";
    public static final String NMAP_OS_DETAILS = "OS details";
    public static final String NMAP_RUNNING = "Running";
    public static final String NMAP_DEVICE_SCAN_FOR_MAC_ADDRESSES = "sudo -S nmap -sn";
    public static final String NMAP_DEVICE_SCAN_FOR_OS = "sudo nmap -Pn -v -O";
    public static final String NMAP_CONTAINS_PORT_STATE_SERVICE = "port_state-service";

    // SnapShot file constants
    public static final String CODEX_SNAP_SHOT_DIRECTORY = "/CodexSnapShots";
    public static final String CODEX_SNAP_SHOT_JSON_DIRECTORY = "/JSONSnapShots";
    public static final String CODEX_SNAP_SHOT_PDF_DIRECTORY = "/PDFSnapsShots";
    public static final String CODEX_SNAP_SHOT_TXT_DIRECTORY = "/TXTSnapsShots";
    public static final String JSON_FILE_TYPE = ".json";
    public static final String PDF_FILE_TYPE = ".pdf";
    public static final String TXT_FILE_TYPE = ".txt";
    public static final String JSON = "json";
    public static final String PDF = "pdf";
    public static final String TXT = "txt";

    // ui renditions
    public static final String HACKER_GREEN = "#20c20e";
    public static final String BLACK = "#000";
    public static final float HOST_NODE_V = 150.0f;
    public static final float HOST_NODE_V1 = 67.5f;
    public static final float HOST_NODE_V2 = 50.0f;

    public static final float NODE_DIVISOR = 1.5f;

    public static final float DEVICE_NODE_V = HOST_NODE_V / NODE_DIVISOR;
    public static final float DEVICE_NODE_V1 = HOST_NODE_V1 / NODE_DIVISOR;
    public static final float DEVICE_NODE_V2 = HOST_NODE_V2 / NODE_DIVISOR;

    /**
     * Prevents native class from calling this constructor. The caller references constants using
     * <tt>PeepingConstants.SUCCESS</tt> etc.
     */
    private PeepingConstants() {
        throw new AssertionError();
    }
}
