package cn.maiaimei.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class IdNumberUtils {
    /**
     * 假设18位身份证号码:41000119910101123X  410001 19910101 123X
     * ^开头
     * [1-9]    第一位1-9中的一个      4
     * \\d{5}   五位数字              10001（前六位省市县地区）
     * (18|19|20)                   19（现阶段可能取值范围18xx-20xx年）
     * \\d{2}                       91（年份）
     * ((0[1-9])|(10|11|12))        01（月份）
     * (([0-2][1-9])|10|20|30|31)   01（日期）
     * \\d{3}   三位数字             123（第十七位奇数代表男，偶数代表女）
     * [0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
     * $结尾
     * <p>
     * 假设15位身份证号码:410001910101123  410001 910101 123
     * ^开头
     * [1-9]    第一位1-9中的一个      4
     * \\d{5}   五位数字              10001（前六位省市县地区）
     * \\d{2}                       91（年份）
     * ((0[1-9])|(10|11|12))        01（月份）
     * (([0-2][1-9])|10|20|30|31)   01（日期）
     * \\d{3}   三位数字              123（第十五位奇数代表男，偶数代表女），15位身份证不含X
     * $结尾
     */
    private static final String ID_NUMBER_FORMAT = "(^[1-9]\\d{5}(%s)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
            "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

    private static final String BIRTHDAY_FORMAT = "%s-%s-%s";

    private static final String SEX_UNKNOWN = "U";
    private static final String SEX_MALE = "M";
    private static final String SEX_FEMALE = "F";

    private IdNumberUtils() {
        throw new UnsupportedOperationException();
    }

    private static int getValidBirthYear(LocalDate localDate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DateTimeUtils.YYYY);
        String yearAsString = dtf.format(localDate);
        return Integer.parseInt(yearAsString.substring(0, 2));
    }

    private static List<Integer> getValidBirthYears() {
        List<Integer> years = new ArrayList<>();
        int year = getValidBirthYear(LocalDate.now());
        years.add(year - 2);
        years.add(year - 1);
        years.add(year);
        return years;
    }

    public static boolean isValid(String idNumber) {
        if (StringUtils.isBlank(idNumber)) {
            return false;
        }

        List<Integer> years = getValidBirthYears();
        String format = String.format(ID_NUMBER_FORMAT, StringUtils.join(years.toArray(), "|"));

        boolean matches = idNumber.matches(format);
        // 判断第18位校验值
        if (matches) {
            if (idNumber.length() == 18) {
                try {
                    char[] charArray = idNumber.toCharArray();
                    // 前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    // 这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].equalsIgnoreCase(String.valueOf(idCardLast))) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return matches;
    }

    public static String getBirthdayAsString(String idNumber) {
        String year, month, dayOfMonth;
        if (idNumber.length() == 18) {
            year = idNumber.substring(6).substring(0, 4);
            month = idNumber.substring(10).substring(0, 2);
            dayOfMonth = idNumber.substring(12).substring(0, 2);
        } else {
            year = "19" + idNumber.substring(6, 8);
            month = idNumber.substring(8, 10);
            dayOfMonth = idNumber.substring(10, 12);
        }
        return String.format(BIRTHDAY_FORMAT, year, month, dayOfMonth);
    }

    public static LocalDate getBirthday(String idNumber) {
        int year, month, dayOfMonth;
        if (idNumber.length() == 18) {
            year = Integer.parseInt(idNumber.substring(6).substring(0, 4));
            month = Integer.parseInt(idNumber.substring(10).substring(0, 2));
            dayOfMonth = Integer.parseInt(idNumber.substring(12).substring(0, 2));
        } else {
            year = Integer.parseInt("19" + idNumber.substring(6, 8));
            month = Integer.parseInt(idNumber.substring(8, 10));
            dayOfMonth = Integer.parseInt(idNumber.substring(10, 12));
        }
        return LocalDate.of(year, month, dayOfMonth);
    }

    public static String getSex(String idNumber) {
        if (idNumber.length() == 18) {
            if (Integer.parseInt(idNumber.substring(16).substring(0, 1)) % 2 == 0) {
                return SEX_FEMALE;
            } else {
                return SEX_MALE;
            }
        } else if (idNumber.length() == 15) {
            if (Integer.parseInt(idNumber.substring(14, 15)) % 2 == 0) {
                return SEX_FEMALE;
            } else {
                return SEX_MALE;
            }
        }
        return SEX_UNKNOWN;
    }

    public static int getAge(String idNumber) {
        int age = 0;
        if (StringUtils.isEmpty(idNumber)) {
            return age;
        }

        String birth = "";
        if (idNumber.length() == 18) {
            birth = idNumber.substring(6, 14);
        } else if (idNumber.length() == 15) {
            birth = "19" + idNumber.substring(6, 12);
        }

        int year = Integer.parseInt(birth.substring(0, 4));
        int month = Integer.parseInt(birth.substring(4, 6));
        int day = Integer.parseInt(birth.substring(6));
        Calendar cal = Calendar.getInstance();
        age = cal.get(Calendar.YEAR) - year;
        // 周岁计算
        if (cal.get(Calendar.MONTH) < (month - 1) || (cal.get(Calendar.MONTH) == (month - 1) && cal.get(Calendar.DATE) < day)) {
            age--;
        }
        return age;
    }

    public static String convert15To18(String idNumber) {
        // 将字符串转化为buffer进行操作
        StringBuilder stringBuffer = new StringBuilder(idNumber);
        //身份证最后一位校验码，X代表10（顺序固定）
        char[] checkIndex = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int sum = 0;
        // 在第6位插入年份的前两位19
        stringBuffer.insert(6, "19");
        for (int i = 0; i < stringBuffer.length(); i++) {
            char c = stringBuffer.charAt(i);
            // 前17位数字
            int ai = Integer.parseInt(String.valueOf(c));
            // 前17位每位对应的系数（7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 ）
            int wi = ((int) Math.pow(2, stringBuffer.length() - i)) % 11;
            // 总和（每位数字乘以系数再相加）
            sum = sum + ai * wi;
        }
        // 总和除以11求余
        int indexOf = sum % 11;
        // 根据余数作为下表在校验码数组里取值
        stringBuffer.append(checkIndex[indexOf]);
        return stringBuffer.toString();
    }

    public static boolean isValidBirthday(String value) {
        if (value == null) {
            return true;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DateTimeUtils.YYYY_MM_DD);
        try {
            LocalDate birthDate = LocalDate.parse(value, dtf);
            LocalDate today = LocalDate.now();
            if (birthDate.isAfter(today)) {
                return false;
            }
            List<Integer> years = getValidBirthYears();
            int birthYear = getValidBirthYear(birthDate);
            return years.contains(birthYear);
        } catch (Exception e) {
            return false;
        }
    }
}
