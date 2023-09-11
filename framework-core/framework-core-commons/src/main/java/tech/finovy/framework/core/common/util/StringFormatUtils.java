package tech.finovy.framework.core.common.util;

public class StringFormatUtils {
    public static final char DOT = '.';
    private static final char MINUS = '-';
    private static final char UNDERLINE = '_';

    /**
     * camelTo underline format
     *
     * @param param
     * @return formatted string
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * underline to camel
     *
     * @param param
     * @return formatted string
     */
    public static String underlineToCamel(String param) {
        return formatCamel(param, UNDERLINE);
    }

    /**
     * minus to camel
     *
     * @param param
     * @return formatted string
     */
    public static String minusToCamel(String param) {
        return formatCamel(param, MINUS);
    }

    /**
     * dot to camel
     *
     * @param param
     * @return formatted string
     */
    public static String dotToCamel(String param) {
        return formatCamel(param, DOT);
    }

    /**
     * format camel
     *
     * @param param
     * @param sign
     * @return formatted string
     */
    private static String formatCamel(String param, char sign) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == sign) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


}
