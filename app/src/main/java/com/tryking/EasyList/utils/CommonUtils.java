package com.tryking.EasyList.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Tryking on 2016/5/20.
 */
public class CommonUtils {
    /**
     * 将字符串以“，”分割转化为字符串数组
     *
     * @param str
     * @return
     */
    public static String[] convertStrToArray(String str) {
        String[] strArray = null;
        strArray = str.split(",");
        return strArray;
    }

    public static String[] addStrToArray(int position, String[] strs, String str) {
        String[] front = Arrays.copyOfRange(strs, 0, position + 1);
        String[] behind = Arrays.copyOfRange(strs, position + 1, strs.length);
        String[] newStrs = new String[front.length + behind.length + 1];
        for (int i = 0; i < front.length; i++) {
            newStrs[i] = front[i];
        }
        newStrs[front.length] = str;
        for (int i = 0; i < behind.length; i++) {
            newStrs[front.length + 1 + i] = behind[i];
        }
        return newStrs;
    }

    public static String convertArrayToStr(String[] strs) {
        String str = "";
        for (int i = 0; i < strs.length; i++) {
            str = str + "," + strs[i];
        }
        return str;
    }

    public static String addSignToStr(String str) {
        String newStr = str.substring(0, 2) + " : " + str.substring(2, 4);
        return newStr;
    }

    /**
     * 将带年月日的日期转化为不带汉字的
     *
     * @param oldStr
     * @return
     */
    public static String clearHanZiFromStr(String oldStr) {
        String newStr = oldStr.replace("年", "")
                .replace("月", "")
                .replace("日", "");
        return newStr;
    }

    /**
     * 往List中添加一个数据
     *
     * @param list
     * @param i
     * @param i
     * @return
     */
    public static List addIntToList(ArrayList<Integer> list, int position, int i) {
        ArrayList<Integer> newList = list;
        newList.add(position, i);
        return newList;
    }

    /**
     * 把int型的数据转化为String类型，即往前加0凑成4位
     *
     * @param i
     * @return
     */
    public static String intToStr(int i) {
        if (i > 999) {
            return i + "";
        } else if (i > 99) {
            return "0" + i;
        } else if (i > 9) {
            return "00" + i;
        } else {
            return "000" + i;
        }
    }

    /**
     * 将List转化为String，并以，分割开
     *
     * @param list
     * @return
     */
    public static String listToString(List list) {
        String s = "";
        for (int i = 0; i < list.size(); i++) {
            if (s == "") {
                s = intToStr((Integer) list.get(i));
            } else {
                s = s + "," + intToStr((Integer) list.get(i));
            }
        }
        return s;
    }

    /**
     * 删除字符串中的字符串
     *
     * @param oldStr
     * @param str
     * @return
     */
    public static String deleteStr(String oldStr, String str) {
        int i = oldStr.indexOf(str);
        String s;
        if (i != -1) {
            s = oldStr.substring(0, i) + oldStr.substring(i + str.length());
            return s;
        }
        return oldStr;
    }

    /**
     * 根据给定的开始时间和结束时间计算出持续时间
     *
     * @param startTime 开始时间的4位String类型
     * @param endTime   结束时间的4位String类型
     * @return 持续时间
     */
    public static String durationTime(String startTime, String endTime) {
//        int endMinutes = Integer.parseInt(endTime.substring(0, 2)) * 60 + Integer.parseInt(endTime.substring(2, 4));
//        int startMinutes = Integer.parseInt(startTime.substring(0, 2)) * 60 + Integer.parseInt(startTime.substring(2, 4));
//        int duration = endMinutes - startMinutes;
        int duration = durationMinutes(startTime, endTime);
        if (duration < 0) {
            throw new IllegalArgumentException("endTime need bigger than startTime");
        } else {
            int hours = duration / 60;
            int minutes = duration % 60;
            String str = hours + "小时" + minutes + "分钟";
            return str;
        }
    }

    /**
     * 根据给定的开始时间和结束时间计算出持续时间分钟数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 持续时间的分钟数
     */
    public static int durationMinutes(String startTime, String endTime) {
        int endMinutes = Integer.parseInt(endTime.substring(0, 2)) * 60 + Integer.parseInt(endTime.substring(2, 4));
        int startMinutes = Integer.parseInt(startTime.substring(0, 2)) * 60 + Integer.parseInt(startTime.substring(2, 4));
        int duration = endMinutes - startMinutes;
        return duration;
    }

    /**
     * 得到一个浮点型数的小数点后一位四舍五入值
     *
     * @param original
     * @return
     */
    public static String getApproximation(float original) {
        String s = String.valueOf(original);
        String newStr = "";
        if (s.contains(".")) {
            String[] split = s.split("\\.");
            int i = Integer.parseInt(split[1]);
            if (i == 0) {
                newStr = split[0];
            } else if (i < 10) {
                newStr = split[0] + "." + i;
            } else {
                String sub1 = split[1].substring(0, 1);
                String sub2 = split[1].substring(1, 2);
                int s1 = Integer.parseInt(sub1);
                int s2 = Integer.parseInt(sub2);
                if (s2 >= 55) {
                    s1 += 1;
                }
                newStr = split[0] + "." + s1;
            }
        } else {
            newStr = s;
        }
        return newStr;
    }

    /**
     * 得到当前日期的day天后的值，day为负则向前
     *
     * @param day
     * @return
     */
    public static String getPreviousDay(String currentDate, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date current = dateFormat.parse(currentDate);
            Calendar c = Calendar.getInstance();
            c.setTime(current);
            c.add(Calendar.DAY_OF_YEAR, day);
            return dateFormat.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给单字符串的日添加一个0
     *
     * @param s
     * @return
     */
    public static String add0toOneChar(String s) {
        if (s.length() == 2) {
            return s;
        } else if (s.length() == 1) {
            return 0 + s;
        } else {
            throw new IllegalArgumentException("字符串必须为1～2位");
        }
    }

    /**
     * 给日期添加汉字年月
     *
     * @param date
     * @return
     */
    public static String addNianYueToDate(String date) {
        String s = date.substring(0, 4) + "年" + date.substring(4, 6) + "月";
        return s;
    }

    /**
     * 给日期添加汉字年月日
     *
     * @param date
     * @return
     */
    public static String addZitoDate(String date) {
        String s = date.substring(0, 4) + "年" + date.substring(4, 6) + "月" + date.substring(6) + "日";
        return s;
    }

    /**
     * 由日期得到月份
     *
     * @param date
     * @return
     */
    public static String getMonthFromDate(String date) {
        String s = date.substring(0, 4) + date.substring(4, 6);
        return s;
    }
}
