package com.tryking.timer.utils;

import java.util.ArrayList;
import java.util.Arrays;
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
}
