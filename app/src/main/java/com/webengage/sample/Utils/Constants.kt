package com.webengage.sample.Utils


object Constants {
    //HMS
    val HMS_APP_ID: String? = "104734947"
    const val HMS_TOKEN_SCOPE = "HCM"

    //MI demo app key
    //APP-Secret -- WLbeRrvZ6N/eyEb/DmPy2w==
    const val MI_APP_ID = "2882303761519006316"
    const val MI_APP_KEY = "5221900635316"

    // Deeplinks
    private const val HTTPS = "https"
    private const val SURVEY_HOST = "survey.com"
    const val SURVEY_DEEPLINK = "$HTTPS://$SURVEY_HOST"

    // Log keys
    const val DEBUG_LOGGING = "debug-log"
    const val TAG = "WebEngage-Sample-App"
    const val WEBENGAGE_DEBUG_TAG = "webengage"

    // Log types
    const val APP_INSTALLED = "App Installed"
    const val PUSH_RECEIVED = "Push Received"
    const val PUSH_SHOWN = "Push Shown"
    const val PUSH_CLICKED = "Push Clicked"
    const val PUSH_ACTION_CLICKED = "Push Action Clicked"
    const val PUSH_DISMISSED = "Push Dismissed"
    const val IN_APP_SHOWN = "In-app Shown"
    const val IN_APP_CLICKED = "In-app Clicked"
    const val IN_APP_DISMISSED = "In-app Dismissed"
    const val EVENT_TRACKED = "Event Tracked"
    const val GEOFENCE_RECEIVED = "Geofence Received"

    // Shared preference keys
    const val ADVERTISING_ID = "advertising_id"
    const val SDK_MINIFIED = "sdk_minified"
    const val STRICT_MODE = "strict_mode"
    const val WEBENGAGE_ENGAGED = "webengage_engaged"
    const val AUTOENGAGE_ENABLED = "autoengage_enabled"
    const val SPLASH_ENABLED = "splash_enabled"
    const val PUSH_ENABLED = "push_enabled"
    const val ACTIVITY_LIFECYCLE_REGISTERED = "activity_lifecycle_registered"
    const val DEFAULT_LICENSE_CODE = "aa131d2c" // "~2024c585";

    const val LICENSE_CODE = "license_code"
    const val LOCAL_HOST = "local_host"
    const val PREV_LICENSE_CODE = "prev_license_code"
    const val CUID = "username"
    const val PREV_LOGIN = "prev_login"
    const val FIRST_NAME = "firstname"
    const val LAST_NAME = "lastname"
    const val EMAIL = "email"
    const val HASHED_EMAIL = "hashed_email"
    const val PHONE = "phone"
    const val HASHED_PHONE = "hashed_phone"
    const val COMPANY = "company"
    const val GENDER = "gender"
    const val BIRTHDATE = "birthdate"
    const val PUSH_OPTIN = "push_optin"
    const val INAPP_OPTIN = "inapp_optin"
    const val SMS_OPTIN = "sms_optin"
    const val EMAIL_OPTIN = "email_optin"
    const val ANR_TRACES = "anr_traces"
    const val CRASH_REPORT = "crash_report"
    const val ANONYMOUS_ID = "anonymous_id"
    const val FCM_REG_ID = "fcm_reg_id"
    const val LOG_OPTION = "log_option"
    const val ORDER_ID = "order_id"
    const val EVERY_ACTIVITY_IS_SCREEN = "every_activity_is_screen"
    const val ENVIRONMENT = "sdk_environment"
    const val SHOW_SYSTEM_TRAY_NOTIFICATION = "show_system_tray_notification"
    const val TRACK_APP_LAUNCH = "track_app_launch"
    const val NEVER_ASK_LOCATION_PERMISSION_AGAIN = "never_ask_location_permission_again"
    const val PUSH_COUNT = "push_count"

    // Screens
    const val COUNT = "count"
    const val USER_PROFILE_SCREEN = "User Profile"
    const val EVENTS_SCREEN = "Events"
    const val PUSH_SCREEN = "Push"
    const val INAPP_SCREEN = "In-app"
    const val SURVEY_SCREEN = "Survey"
    const val WEBVIEW_SCREEN = "WebView"
    const val CUSTOM_SCREEN = "Screen"
    const val COMMAND_SCREEN = "Command Screen"
    const val FILE_SCREEN = "File Screen"

    // Keys
    const val MESSAGE_DATA = "message_data"
    const val TITLE = "title"
    const val URL = "url"

    // Values
    const val NULL = "null"
    const val BLANK = "blank"
    const val SPACE = "space"
    const val DATE_ISO_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val CHANNEL_ID = "test_channel"
    private const val GEOFENCE_EXPIRATION_IN_HOURS: Long = 12
    const val DISABLED = 0
    const val DEFAULT = 1
    const val VERBOSE = 2
    const val FCM_SERVER_KEY =
        "AAAAEemEUmU:APA91bGpkg7bY7L9417MfJJCPAX4BOmu2oP8QkwCHRbdLNgCjJ9Kgalslpz5dUfy0I8_SejVNUoY_2ukyiIUnM6dVmUv29kmTeDu6gYbyEsmqPJnuT3JPHEc0OJWdZUSa4LgOKa0gfEZ"
    const val GLOBAL_BASE_PATH = "https://msdk-files.webengage.com/sdk/2/0.1/"
    const val EXPERIMENT_ID = "23pi0ii"
    const val VARIATION_ID = "31764417"
    const val BLOCKING_LAYOUT_ID = "~fg00aaa"
    const val CLASSIC_MODAL_LAYOUT_ID = "1af576b9"
    const val FOOTER_LAYOUT_ID = "6ic379h"
    const val HEADER_LAYOUT_ID = "i78egad"
    const val PAGE_BLOCKER_ID = "~483819e"
    const val TEST_LAYOUT_ID = ""

    // Separators
    const val KEY_VALUE_SEPARATOR = ":"
    const val DOTS = "..."
}