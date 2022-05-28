package cn.maiaimei.framework.util;

import org.apache.commons.lang3.StringUtils;

public class RegexUtils {
    private static final String REGEX_ALPHABET = "^[a-zA-Z]{%s,%s}$";
    private static final String REGEX_NUMBER = "^[0-9]{%s,%s}$";
    private static final String REGEX_CHINESE_CHARACTER = "^[\\u4e00-\\u9fa5]{%s,%s}$";
    private static final String REGEX_MOBILE = "^1[3456789]\\d{9}$";
    private static final String REGEX_EMAIL = "^\\w+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";
    private static final String REGEX_PASSWORD_CONTAIN_ALPHABET_NUMBER = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{%s,%s}$";
    private static final String REGEX_PASSWORD_CONTAIN_ALPHABET_NUMBER_SYMBOL = "^(?![0-9]+$)(?![a-zA-Z]+$)(?![0-9a-zA-Z]+$)(?![0-9\\W]+$)(?![a-zA-Z\\W]+$)[0-9A-Za-z\\W]{%s,%s}$";

    private RegexUtils() {

    }

    public static boolean isValidAlphabet(String value) {
        return isValid(value, String.format(REGEX_ALPHABET, 0, StringUtils.EMPTY));
    }

    public static boolean isValidAlphabet(String value, int min) {
        return isValid(value, String.format(REGEX_ALPHABET, min, StringUtils.EMPTY));
    }

    public static boolean isValidAlphabet(String value, int min, int max) {
        return isValid(value, String.format(REGEX_ALPHABET, min, max));
    }

    public static boolean isValidNumber(String value) {
        return isValid(value, String.format(REGEX_NUMBER, 0, StringUtils.EMPTY));
    }

    public static boolean isValidNumber(String value, int min) {
        return isValid(value, String.format(REGEX_NUMBER, min, StringUtils.EMPTY));
    }

    public static boolean isValidNumber(String value, int min, int max) {
        return isValid(value, String.format(REGEX_NUMBER, min, max));
    }

    public static boolean isValidChineseCharacter(String value) {
        return isValid(value, String.format(REGEX_CHINESE_CHARACTER, 0, StringUtils.EMPTY));
    }

    public static boolean isValidChineseCharacter(String value, int min) {
        return isValid(value, String.format(REGEX_CHINESE_CHARACTER, min, StringUtils.EMPTY));
    }

    public static boolean isValidChineseCharacter(String value, int min, int max) {
        return isValid(value, String.format(REGEX_CHINESE_CHARACTER, min, max));
    }

    public static boolean isValidMobile(String value) {
        return isValid(value, REGEX_MOBILE);
    }

    public static boolean isValidEmail(String value) {
        return isValid(value, REGEX_EMAIL);
    }

    public static boolean isValidPassword(PasswordType type, String value, int min, int max) {
        switch (type) {
            case CONTAIN_ALPHABET_NUMBER:
                return isValid(value, String.format(REGEX_PASSWORD_CONTAIN_ALPHABET_NUMBER, min, max));
            case CONTAIN_ALPHABET_NUMBER_SYMBOL:
                return isValid(value, String.format(REGEX_PASSWORD_CONTAIN_ALPHABET_NUMBER_SYMBOL, min, max));
            default:
                return false;
        }
    }

    public enum PasswordType {
        /**
         * 只能包含字母和数字
         */
        CONTAIN_ALPHABET_NUMBER,
        /**
         * 必须包含字母、数字和特殊字符
         */
        CONTAIN_ALPHABET_NUMBER_SYMBOL
    }

    private static boolean isValid(String value, String regex) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        return value.matches(regex);
    }
}
