package ma.bcp.utils;

public class Constants {
    public class Security {
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String BASIC_PREFIX = "Basic ";
    }

    public class Date {
        public static final String ISO_8601 = "YYYY-MM-DDTHH:MM:SSZ";
        public static final String RFC_3339 = "yyyy-MM-dd'T'HH:mm:ssZ";
        public static final String DATE_ONLY = "YYYY-MM-DD";
        public static final String TIME_ONLY = "HH:mm:ss";
        public static final String FILE_NAMING = "yyyyMMddHHmmss";
    }

    public class StatusCode {

        public static final int SUCCESS = 200; // Success

        public static final int INVALID_ARGUMENT = 400; // Bad request, e.g., invalid parameters

        public static final int UNAUTHORIZED = 401; // Username or password incorrect

        public static final int FORBIDDEN = 403; // No permission

        public static final int NOT_FOUND = 404; // Not found

        public static final int INTERNAL_SERVER_ERROR = 500; // Server internal error

    }
}