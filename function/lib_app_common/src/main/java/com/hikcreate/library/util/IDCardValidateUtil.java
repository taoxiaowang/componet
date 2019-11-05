package com.hikcreate.library.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UserInfo: gongwei
 * Date: 2015-04-28
 * Time: 10:53
 * Description:验证身份证工具类
 */
@SuppressWarnings("ALL")
public class IDCardValidateUtil {

    // 这两个数组用于身份证最后一位的校验，来源于网络
    private static int[] arrInt = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static String[] arrCh = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    // 身份证省的编码
    private static int[] citys = {11// :"北京"
            , 12// :"天津"
            , 13// :"河北"
            , 14// :"山西"
            , 15// :"内蒙古"
            , 21// :"辽宁"
            , 22// :"吉林"
            , 23// :"黑龙江"
            , 31// :"上海"
            , 32// :"江苏"
            , 33// :"浙江"
            , 34// :"安徽"
            , 35// :"福建"
            , 36// :"江西"
            , 37// :"山东"
            , 41// :"河南"
            , 42// :"湖北"
            , 43// :"湖南"
            , 44// :"广东"
            , 45// :"广西"
            , 46// :"海南"
            , 50// :"重庆"
            , 51// :"四川"
            , 52// :"贵州"
            , 53// :"云南"
            , 54// :"西藏"
            , 61// :"陕西"
            , 62// :"甘肃"
            , 63// :"青海"
            , 64// :"宁夏"
            , 65// :"新疆"
            , 71// :"台湾"
            , 81// :"香港"
            , 82// :"澳门"
            , 91 // :"国外"
    };


    /**
     * 检查身份证号码正确性
     */
    @Deprecated
    //Del by Gv. 2019.9.11. 因为夏令时误差，会导致checkBirthday()在校验生日时异常返回false，废弃此方法。如：1988-04-10，1989-04-16，1990-04-15，1991-04-14
    public static boolean isCardNo(String card) {
        if (TextUtils.isEmpty(card)) return false;
        card = card.replace("x", "X");
        card = card.replace("*", "X");
        return checkFormat(card) && checkProvince(card) && checkBirthday(card) && checkParity(card);
    }

    /**
     * 检查号码是否符合规范，包括长度，类型
     */
    public static boolean checkFormat(String card) {
        // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
        Pattern p = Pattern.compile("(\\d{15})|(\\d{17}(\\d|X))");
        Matcher m = p.matcher(card);
        return m.matches();
    }

    /**
     * 取身份证前两位,校验省份
     */
    private static boolean checkProvince(String card) {
        int province = Integer.parseInt(card.substring(0, 2));
        List<Integer> l = new ArrayList<>();
        for (int ct : citys) {
            l.add(ct);
        }
        return l.contains(province);
    }

    /**
     * 检查生日是否正确
     */
    private static boolean checkBirthday(String card) {
        try {
            int len = card.length();
            // 身份证15位时，次序为省（3位）市（3位）年（2位）月（2位）日（2位）校验位（3位），皆为数字
            if (len == 15) {
                int year = Integer.parseInt(card.substring(6, 8));
                int month = Integer.parseInt(card.substring(8, 10));
                int day = Integer.parseInt(card.substring(10, 12));
                //add by gw for fix bug start
                if (month <= 0) return false;
                //add by gw for fix bug end
                Date birthday = new Date("19" + year + "/" + month + "/" + day);
                return verifyBirthday(Integer.parseInt("19" + year), month, day,
                        birthday);
            }
            // 身份证18位时，次序为省（3位）市（3位）年（4位）月（2位）日（2位）校验位（4位），校验位末尾可能为X
            if (len == 18) {
                int year = Integer.parseInt(card.substring(6, 10));
                int month = Integer.parseInt(card.substring(10, 12));
                int day = Integer.parseInt(card.substring(12, 14));
                //add by gw for fix bug start
                if (month <= 0) return false;
                //add by gw for fix bug end
                Date birthday = new Date(year + "/" + month + "/" + day);
                return verifyBirthday(year, month, day, birthday);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 校验日期
     */
    private static boolean verifyBirthday(int year, int month, int day, Date birthday) {
        Calendar calBirthday = Calendar.getInstance();
        calBirthday.setTime(birthday);
        int now_year = Calendar.getInstance().get(Calendar.YEAR);
        // 年月日是否合理
        if (year == calBirthday.get(Calendar.YEAR)
                && month == (calBirthday.get(Calendar.MONTH) + 1)
                && day == calBirthday.get(Calendar.DAY_OF_MONTH)) {
            // 判断年份的范围（3岁到100岁之间)
            int time = now_year - year;
            if (time >= 3 && time <= 100) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 校验位的检测
     */
    private static boolean checkParity(String card) {
        // 15位转18位
        card = changeFivteenToEighteen(card);
        int len = card.length();
        if (len == 18) {
            int cardTemp = 0;
            String valnum;
            for (int i = 0; i < 17; i++) {
                cardTemp += Integer.parseInt(card.substring(i, i + 1)) * arrInt[i];
            }
            valnum = arrCh[cardTemp % 11];
            if (valnum.equals(card.substring(17))) {
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * 15位转18位身份证号
     */
    private static String changeFivteenToEighteen(String card) {
        if (card.length() == 15) {
            int cardTemp = 0;
            card = card.substring(0, 6) + "19" + card.substring(6);
            for (int i = 0; i < 17; i++) {
                cardTemp += Integer.parseInt(card.substring(i, i + 1))
                        * arrInt[i];
            }
            card += arrCh[cardTemp % 11];
            return card;
        }
        return card;
    }
}
