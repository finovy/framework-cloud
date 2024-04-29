package tech.finovy.framework.security.auth.common;

public class SecurityConstants {
    public static final String AUTHORIZATION_BEARER = "Bearer";
    public static final String ROLE = "ROLE_";
    public static final String FROM_IN = "Y";
    public static final String FROM = "from";
    public static final String HEADER_FROM_IN = "from=Y";
    public static final String OAUTH_TOKEN_URL = "/oauth2/token";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String MOBILE = "mobile";
    public static final String BCRYPT = "{bcrypt}";
    public static final String NOOP = "{noop}";
    public static final String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";
    public static final String USERNAME = "username";
    public static final String DETAILS_USER = "user_info";
    public static final String DETAILS_USER_ID = "user_id";
    public static final String DETAILS_LICENSE = "license";
    public static final long CODE_TIME = 60L;
    public static final String CODE_SIZE = "6";
    public static final String CLIENT_CREDENTIALS = "client_credentials";
    public static final String CLIENT_ID = "clientId";
    public static final String SMS_PARAMETER_NAME = "mobile";
    public static final String CUSTOM_CONSENT_PAGE_URI = "/token/confirm_access";
    public static final String DEFAULT_IGNORE_URL_HEALTHCHECK = "/healthcheck/**";
    public static final String DEFAULT_IGNORE_URL_WEBJARS = "/webjars/**";
    public static final String DEFAULT_IGNORE_URL_SWAGGER_DOC = "/doc.html";
    public static final String DEFAULT_IGNORE_URL_SWAGGER_API_DOCS = "/v3/api-docs/**";
    public static final String DEFAULT_IGNORE_URL_ACTUATOR = "/actuator/**";
    public static final String DEFAULT_IGNORE_URL_CSS = "/css/**";
    public static final String DEFAULT_IGNORE_URL_ERROR = "/error";

    private SecurityConstants() {
    }
}
