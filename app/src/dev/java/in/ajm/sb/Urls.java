package in.ajm.sb;

public class Urls {
    public static String API_VERSION = "api_v4/";
    public static String BASE_URL = "http://apidev.sb.pro/";

    public static String API_URL = BASE_URL + API_VERSION;
    public static String API_VERSION_PARAM = "4";

    public static String PRIVACY_URL = BASE_URL + "sbname/privacy_policy";
    public static String HELP_URL = BASE_URL + "sbname/help/";
    public static String TERM_URL = BASE_URL + "sbname/terms_conditions";
    public static String PAYMENT_URL = BASE_URL + "payment?call_id=%1$s&platform=app&source=%2$s&version=" + API_VERSION_PARAM;
    public static String ADMIN_URL = API_URL + "access_admin?auth=%1$s&%2$s";
    public static String GOOGLE_API_URL = "https://maps.googleapis.com/maps/api/";
}