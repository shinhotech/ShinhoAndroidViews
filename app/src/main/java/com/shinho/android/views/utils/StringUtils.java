package com.shinho.android.views.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

/**
 * 字符串工具类
 */
public class StringUtils {

    /**
     * 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 是否都未空
     */
    public static boolean isAllEmpty(String... strs) {
        for (String str : strs) {
            if (!isEmpty(str)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否包含有空
     */
    public static boolean hasEmpty(String... strs) {
        if (strs == null) return false;
        for (String str : strs) {
            if (isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含有相同的值，不考虑空值
     */
    public static boolean hasEqual(String... strs) {
        HashSet<String> set = new HashSet<>();
        for (String str : strs) {
            if (isEmpty(str)) continue;
            if (!set.add(str)) return true;
        }
        return false;
    }

    /**
     * 获取金额文字
     *
     * @param money 金钱，单位：分
     * @return 转换成元单位的金额信息
     */
    public static String getMoney(Long money) {
        if (money == null) return null;
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(new BigDecimal(money).divide(new BigDecimal(100), 2, RoundingMode.FLOOR).doubleValue());
    }

    /**
     * 获取千分位格式金额文字
     *
     * @param str 可以是任何Object子类型，对应单位为元
     * @return 千分位格式金额，如：1,000,000.01
     */
    public static String addComma(Object str) {
        if (str == null) return "";
        Double number = Double.parseDouble(str.toString());
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        return decimalFormat.format(number);
    }

    /**
     * 显示消息时间字符串，区分今日和之前的
     *
     * @param time 毫秒时间戳
     * @return [今天 ]HH:mm
     */
//    public static String getMessageTimeStr(long time) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time);
//
//        if (DateUtils.isToday(calendar)) {
//            return "今天 " + DateUtils.calendar2str(calendar, DateUtils.PATTERN_TIME);
//        } else {
//            return DateUtils.calendar2str(calendar, DateUtils.PATTERN_DATE);
//        }
//    }

    /**
     * 根据时间段修改问候语：
     *
     * @param nickname 问候对象名称 XX
     * @return XX 早上好
     * <p>
     * <br>
     * 不同时间段问候语如下：<br>
     * 6:01-9:00 早上好<br>
     * 9:01-11:00 上午好<br>
     * 11:01-13:00 中午好<br>
     * 13:01-18:00 下午好<br>
     * 18:01-6:00 晚上好
     */
    public static String getHelloStr(String nickname) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 9) {
            return "早上好！" + nickname;
        } else if (hour >= 9 && hour < 11) {
            return "上午好！" + nickname;
        } else if (hour >= 11 && hour < 13) {
            return "中午好！" + nickname;
        } else if (hour >= 13 && hour < 18) {
            return "下午好！" + nickname;
        } else {
            return "晚上好！" + nickname;
        }
    }

    /**
     * 获取缺省处理的字符串
     *
     * @param str 原字符串
     * @return 原字符串 或 无（原字符串为空）
     */
    public static String getStringOrEmpty(String str) {
        return StringUtils.isEmpty(str) ? "无" : str;
    }

    /**
     * 获取缺省处理的字符串
     *
     * @param str 原字符串
     * @param def 缺省内容
     * @return 原字符串 或 缺省内容（原字符串为空）
     */
    public static String getStringOrEmpty(String str, String def) {
        return StringUtils.isEmpty(str) ? def : str;
    }

    /**
     * 获取距离
     *
     * @param distance 距离长度，单位：米
     * @return 距离长度，如800m 或 1.5km
     * <p>
     * <br>
     * 不同尺度处理如下：<br>
     * 1km以内 显示 800m<br>
     * 1km以上 显示 1.5km
     */
    public static String getDistance(double distance) {
        if (distance <= 0) return "";

        String distanceStr;
        if (distance < 1000) {
            // 1km以内 显示 800m
            distanceStr = distance + "m ";
        } else {
            // 1km以上 显示 1.5km
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            distanceStr = decimalFormat.format(distance / 1000) + "km ";
        }
        return distanceStr;
    }

    /**
     * 获取高亮文字
     *
     * @param highLight      高亮文字
     * @param highLightColor 高亮文字颜色
     * @param content        原字符串
     * @return 高亮处理后的 SpannableString 对象
     */
    public static SpannableString getHighLightStr(String highLight, int highLightColor, String content) {
        if (content == null) content = "";
        return getHighLightStr(highLight, highLightColor, content, content.length());
    }

    /**
     * 获取高亮文字
     *
     * @param highLight      高亮文字
     * @param highLightColor 高亮文字颜色
     * @param content        原字符串
     * @param startLastIndex 倒数startLastIndex索引位置之后，才开始处理高亮
     * @return 高亮处理后的 SpannableString 对象
     */
    public static SpannableString getHighLightStr(String highLight, int highLightColor, String content, int startLastIndex) {
        if (content == null) content = "";
        SpannableString ss = new SpannableString(content);

        int beginIndex = content.length() - startLastIndex;
        if (beginIndex < 0 || content.length() < beginIndex) {
            return ss;
        }
        String prepareCompareString = content.substring(content.length() - startLastIndex);
        if (!StringUtils.isEmpty(highLight) && prepareCompareString.contains(highLight)) {
            int index = content.length() - startLastIndex + prepareCompareString.indexOf(highLight);
            ForegroundColorSpan span = new ForegroundColorSpan(highLightColor);
            ss.setSpan(span, index, index + highLight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return ss;
    }

    /**
     * 数组转为字符串，用逗号,隔开。数组为空返回 ""
     */
    public static String list2str(List<String> list) {
        if (list == null || list.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * 逗号,分割的字符串转为list，忽略空字符串部分。
     */
    public static List<String> str2list(String str) {
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }
        list.addAll(Arrays.asList(str.split(",")));
        return list;
    }

    /**
     * 获取百分比。最多保留两位小数，并且不四舍五入 0.5468 -> 54.6%
     *
     * @param rate 0~1 数字
     * @return rate对应百分比
     */
    public static String getPercent(float rate) {
        int percent = (int) (rate * 10000);
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(percent / 100f) + "%";
    }

    /**
     * 驼峰命名转为下划线命名(小写) ShinhoHome -> shinho_home
     *
     * @param para 驼峰命名字符串
     * @return para对应下划线命名
     */
    public static String HumpToUnderlineLower(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 是否为手机号，简单判断：1开始11位。
     *
     * @param str 手机号字符串
     */
    public static boolean isPhone(String str) {
        if (isEmpty(str)) return false;
        return str.length() == 11 && str.startsWith("1");
    }

}
