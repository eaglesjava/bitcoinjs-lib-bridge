package com.bitbill.www.common.utils;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.webkit.URLUtil;

import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具 <br>
 * Created by zhuyuanbao on 2016/2/29.<br>
 */
public class StringUtils {

    public static final Pattern IS_CHINESE_CHAR = Pattern.compile("[\u4e00-\u9fa5]");
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("[!@#$%^&*_]*.*[a-zA-Z][!@#$%^&*_]*.*[0-9][!@#$%^&*_]*|[!@#$%^&*_]*.*[0-9][!@#$%^&*_]*.*[a-zA-Z][!@#$%^&*_]*");
    private final static Pattern PHONE_NUM = Pattern
            .compile("\\d{11}");// 手机号码11位
    private final static Pattern RECOMMEND_CODE = Pattern
            .compile(".*[a-zA-Z0-9]");// 推荐人推荐码
    private final static Pattern EMAILER = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern NUMBER_STR = Pattern
            .compile("^[0-9]*$");
    private final static Pattern IMG_URL = Pattern
            .compile(".*?(gif|jpeg|png|jpg|bmp)");
    private final static Pattern URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");
    private static final Pattern IPV4_PATTERN =
            Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern IPV6_STD_PATTERN =
            Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN =
            Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    /**
     * 判断全文都是否为中文
     *
     * @param str
     * @return
     */
    public static boolean isChineseCharacters(String str) {
        //如果字符为空 返回false
        if (!isNotEmpty(str)) {
            return false;
        }
        boolean flag = true;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            java.util.regex.Matcher match = IS_CHINESE_CHAR.matcher(ch + "");
            if (!match.matches()) {
                //有非中文的字符 返回false
                flag = false;
                break;
            }
        }
        return flag;
    }

    /**
     * 判断全文是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChineseCharacters(String str) {
        //如果字符为空 返回false
        if (!isNotEmpty(str)) {
            return false;
        }
        boolean flag = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            java.util.regex.Matcher match = IS_CHINESE_CHAR.matcher(ch + "");
            if (match.matches()) {
                //有中文的字符 返回true
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * <pre>
     * 判断是否是一个合法的手机号码
     * 规则:11位数字并且已数字1开头
     * </pre>
     *
     * @param phoneNum
     * @return
     */
    public static boolean isPhoneNum(String phoneNum) {
        if (!StringUtils.isNotEmpty(phoneNum))
            return false;
        return PHONE_NUM.matcher(getRealPhone(phoneNum)).matches() && phoneNum.startsWith("1");
    }

    /**
     * <pre>
     * 判断是否是一个合法的推荐码
     * 规则:6位数字、字母组合
     * </pre>
     *
     * @param jobNum 推荐人推荐码
     * @return
     */
    public static boolean isRecommendCode(String jobNum) {
        if (!StringUtils.isNotEmpty(jobNum))
            return false;
        return RECOMMEND_CODE.matcher(getRealPhone(jobNum)).matches();
    }

    /**
     * <pre>
     * 判断是否是一个合法的手机号码
     * </pre>
     *
     * @param phoneNum
     * @return
     */
    public static boolean isPhoneNumValide(String phoneNum) {
        if (!StringUtils.isNotEmpty(phoneNum))
            return false;
        return PHONE_NUM.matcher(getRealPhone(phoneNum)).matches() && phoneNum.startsWith("1");
    }


    /**
     * 去除空格得到真实的手机字符串
     *
     * @param phoneNum
     * @return
     */
    public static String getRealPhone(String phoneNum) {
        if (!StringUtils.isNotEmpty(phoneNum))
            return "";
        return phoneNum.trim().replaceAll(" ", "");
    }

    /**
     * <pre>
     * 判断是不是一个合法的电子邮件地址
     * </pre>
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return EMAILER.matcher(email).matches();
    }

    /**
     * <pre>
     * 判断一个url是否为图片url
     * </pre>
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (url == null || url.trim().length() == 0)
            return false;
        return IMG_URL.matcher(url).matches();
    }

    /**
     * <pre>
     * 判断是否为一个合法的url地址
     * </pre>
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        //使用android系统的url合法性判断工具
        return URLUtil.isValidUrl(str);
    }

    public static boolean isIPv4Address(final String input) {
        if (!isNotEmpty(input)) {
            return false;
        }
        return IPV4_PATTERN.matcher(input).matches();

    }

    public static boolean isIPv6StdAddress(final String input) {
        if (!isNotEmpty(input)) {
            return false;
        }
        return IPV6_STD_PATTERN.matcher(input).matches();

    }

    public static boolean isIPv6HexCompressedAddress(final String input) {
        if (!isNotEmpty(input)) {
            return false;
        }
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();

    }

    public static boolean isIPv6Address(final String input) {
        if (!isNotEmpty(input)) {
            return false;
        }
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);

    }

    /**
     * 是否是数字字符串
     *
     * @param string
     * @return
     */
    public static boolean isNumber(String string) {
        if (!isNotEmpty(string)) {
            return false;
        }
        return NUMBER_STR.matcher(string).matches();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt(obj.toString(), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    public static String getString(String s) {
        return s == null ? "" : s;
    }

    /**
     * 将一个InputStream流转换成字符串
     *
     * @param is
     * @return
     */
    public static String toConvertString(InputStream is) {
        StringBuffer res = new StringBuffer();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader read = new BufferedReader(isr);
        try {
            String line;
            line = read.readLine();
            while (line != null) {
                res.append(line + "<br>");
                line = read.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != isr) {
                    isr.close();
                    isr.close();
                }
                if (null != read) {
                    read.close();
                    read = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
            }
        }
        return res.toString();
    }

    /***
     * 截取字符串
     *
     * @param start 从那里开始，0算起
     * @param num   截取多少个
     * @param str   截取的字符串
     * @return
     */
    public static String getSubString(int start, int num, String str) {
        if (str == null) {
            return "";
        }
        int leng = str.length();
        if (start < 0) {
            start = 0;
        }
        if (start > leng) {
            start = leng;
        }
        if (num < 0) {
            num = 1;
        }
        int end = start + num;
        if (end > leng) {
            end = leng;
        }
        return str.substring(start, end);
    }

    /**
     * 获取当前时间为每年第几周
     *
     * @return
     */
    public static int getWeekOfYear() {
        return getWeekOfYear(new Date());
    }

    /**
     * 获取当前时间为每年第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        int week = c.get(Calendar.WEEK_OF_YEAR) - 1;
        week = week == 0 ? 52 : week;
        return week > 0 ? week : 1;
    }

    public static int[] getCurrentDate() {
        int[] dateBundle = new int[3];
        String[] temp = getDateTime("yyyy-MM-dd").split("-");

        for (int i = 0; i < 3; i++) {
            try {
                dateBundle[i] = Integer.parseInt(temp[i]);
            } catch (Exception e) {
                dateBundle[i] = 0;
            }
        }
        return dateBundle;
    }

    /**
     * 返回当前系统时间
     */
    public static String getDateTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * 判断短信效验码是否有效
     *
     * @param smsCode
     * @return
     */
    public static boolean isValidSmsCode(String smsCode) {
        //是否是六位数字
        return smsCode != null && isNumber(smsCode) && smsCode.length() == 6;
    }

    /**
     * 注册、修改、重置密码校验
     *
     * @param loginPwd
     * @return
     */
    public static boolean isValidLoginPwdFormat(String loginPwd) {
        //规则：六位以上 包含数字和字母
        return StringUtils.isNotEmpty(loginPwd) && loginPwd.length() >= 6 && PASSWORD_PATTERN.matcher(loginPwd).matches();
    }

    /**
     * <pre>
     * 判断是否为一个合法的短信验证码
     * </pre>
     *
     * @param code
     * @return
     */
    public static boolean isValidCode(String code) {
        // 验证码6位
        return StringUtils.isNotEmpty(code) && code.length() == 6;
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isSpace(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * <pre>
     * 判断字符串非空 null 返回false
     * </pre>
     *
     * @param str
     * @return null NULL  blank : return false
     */
    public static boolean isNotEmpty(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return !str.toString().trim().toUpperCase().equals("NULL");
    }

    /**
     * <pre>
     * 判断字符串为处理后的默认空串 —— 返回false
     * </pre>
     *
     * @param str
     * @return ——  blank : return false
     */
    public static boolean isDefaultEmpty(CharSequence str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.toString().trim().equals("——");
    }

    /**
     * 将字符串按指定规则转换成字符串数组
     *
     * @param originStr  元串
     * @param regularStr 分割规则
     */
    public static String[] convertStrToArray(String originStr, String regularStr) {
        if (isNotEmpty(originStr) && regularStr != null) {
            return originStr.split(regularStr);
        }
        return new String[]{};
    }

    /**
     * 替换手机号中间四位为****号
     *
     * @param originPwd
     * @return
     */
    public static String getSecretPwd(String originPwd) {
        if (isNotEmpty(originPwd) && originPwd.length() == 11) {
            StringBuffer buffer = new StringBuffer(originPwd);
            return buffer.replace(3, 7, "****").toString();
        }
        return "未知";
    }

    /**
     * 用户名规则
     * 是否是有效的用户名（不得超过十个字）
     *
     * @param username
     * @return
     */
    public static boolean isValidateUserName(String username) {
        if (!StringUtils.isNotEmpty(username)) {
            return false;
        }
        if (username.length() > 10) {
            return false;
        }

        String regex = "^[a-zA-Z0-9\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(username);
        return match.matches();
    }

    /**
     * 获取格式化的金额字符串 格式:10.00 -> 10
     *
     * @param damount
     * @return
     */
    public static String getFormatedAmount(double damount) {
        return getFormatedDollers(damount, 2);
    }

    /**
     * 格式化金额字符串，保留指定小数
     *
     * @param amount 金额
     * @param d      保留小数位数
     * @return
     */
    public static String getFormatedDollers(double amount, int d) {
        NumberFormat format = NumberFormat.getInstance();
        //设置截断模式
        format.setRoundingMode(RoundingMode.FLOOR);
        //设置允许的最大保留小数
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        return format.format(amount);
    }

    /**
     * 格式化金额字符串，保留指定小数
     * //todo 保证float转double不会丢失精度
     *
     * @param amount float 金额
     * @param d      保留小数位数
     * @return
     */
    public static String getFormatedDollers(float amount, int d) {
        BigDecimal b = new BigDecimal(String.valueOf(amount));
        NumberFormat format = NumberFormat.getInstance();
        //设置截断模式
        format.setRoundingMode(RoundingMode.FLOOR);
        //设置允许的最大保留小数
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        return format.format(b.doubleValue());
    }

    /**
     * 格式化（截断）金额字符串，指定最大小数
     *
     * @param amount 金额
     * @param d      最大保留小数位数
     * @return
     */
    public static String getFormatedDollersMaxEnd(double amount, int d) {
        NumberFormat format = NumberFormat.getInstance();
        //设置截断模式
        format.setRoundingMode(RoundingMode.FLOOR);
        //设置允许的最大保留小数
        format.setMaximumFractionDigits(d);
        return format.format(amount);
    }

    /**
     * 格式化（截断）金额字符串，指定最大2位小数
     *
     * @param amount 金额
     * @return
     */
    public static String getFormatedDollersMaxEnd(double amount) {
        return getFormatedDollersMaxEnd(amount, 2);
    }

    /**
     * 格式化金额字符串，保留指定小数
     * 使用四舍五入方式
     *
     * @param amount 金额
     * @param d      保留小数位数
     * @return
     */
    public static String getFormatedDollersHalfup(double amount, int d) {
        NumberFormat format = NumberFormat.getInstance();
        //设置四舍五入模式
        format.setRoundingMode(RoundingMode.HALF_UP);
        //设置允许的最大保留小数
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        return format.format(amount);
    }

    /**
     * 格式化金额字符串，保留指定小数
     * 使用四舍五入方式
     * 不显示千分位
     *
     * @param amount 金额
     * @param d      保留小数位数
     * @return
     */
    public static String getFormatedDollersHalfupNoGroup(double amount, int d) {
        NumberFormat format = NumberFormat.getInstance();
        //不显示千分位逗号
        format.setGroupingUsed(false);
        //设置四舍五入模式
        format.setRoundingMode(RoundingMode.HALF_UP);
        //设置允许的最大保留小数
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        return format.format(amount);
    }

    /**
     * 格式化金额字符串，保留指定小数
     *
     * @param amount 金额
     * @param d      保留小数位数
     * @return
     */
    public static String getFormatedDollers(int amount, int d) {
        NumberFormat format = NumberFormat.getInstance();
        //设置允许的最大保留小数
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        return format.format(amount);
    }

    /**
     * 保留两位小数
     *
     * @param amount
     * @return
     */
    public static String getRemainTwoNum(double amount) {
        //保留两位四舍五入
        return getFormatedAmount(amount);
    }

    /**
     * 获取真实字符串 移除空串 null " "等
     *
     * @param s
     * @return
     */
    private static String getRealString(String s) {
        if (!isNotEmpty(s)) {
            return "";
        }
        if (s.trim().equalsIgnoreCase("null")) {
            return "";
        }
        return s.trim();
    }

    /**
     * 验证身份证号码是否合法
     *
     * @param idNo
     * @return
     */
    public static boolean isValidIdNo(String idNo) {
        if (!isNotEmpty(idNo)) {
            return false;
        }
        return getRealString(idNo).length() == 18;
    }


    /**
     * 获取指定字符串部分长度字号缩小到0.75（相对于默认字号）
     * 例如：+2461.81（小数点后两位缩小）
     *
     * @param shrink 缩放比例
     * @param temp   字符串
     * @param start  起点
     * @param end    结束位置
     * @return
     */
    public static SpannableStringBuilder getSelectionShrinkStr(float shrink, String temp, int start, int end) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(temp);
        spannableStringBuilder.setSpan(new RelativeSizeSpan(shrink), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 获取指定字符串部分长度字号缩小到0.75（相对于默认字号）
     * 例如：+2461.81（小数点后两位缩小）
     *
     * @param temp  字符串
     * @param start 起点
     * @param end   结束位置
     * @return
     */
    public static SpannableStringBuilder getSelectionShrinkStr(String temp, int start, int end) {
        return getSelectionShrinkStr(0.75f, temp, start, end);
    }

    /**
     * 获取指定字符串部分长度字号缩小到0.75（相对于默认字号）
     * 例如：+2461.81（+、小数点后两位缩小）
     *
     * @param temp  字符串
     * @param start 起点
     * @param end   结束位置
     * @return
     */
    public static SpannableStringBuilder getFormatSelectionShrinkStr(String temp, int start, int end) {
        SpannableStringBuilder spannableStringBuilder = getSelectionShrinkStr(temp, start, end);
        spannableStringBuilder.setSpan(new RelativeSizeSpan(0.75f), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 设置指定部分字符串颜色
     *
     * @param temp  字符串
     * @param start 起点
     * @param end   结束位置
     * @return
     */
    public static SpannableStringBuilder getSelectionColorStr(String temp, int start, int end, int colorId) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(temp);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(colorId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 设置指定部分字符串颜色和缩小
     *
     * @param temp  字符串
     * @param start 起点
     * @param end   结束位置
     * @return
     */
    public static SpannableStringBuilder getSelectionColorAndShrinkStr(String temp, int start, int end, int colorId) {
        SpannableStringBuilder spannableStringBuilder = getSelectionShrinkStr(temp, start, end);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(colorId), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 设置指定部分字符串颜色
     *
     * @param temp  字符串
     * @param start 起点
     * @param end   结束位置
     * @return
     */
    public static SpannableStringBuilder getSelectionColorStr(String temp, int start, int end, String color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(temp);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor(color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }

    /**
     * 设置指定部分字符串颜色
     *
     * @param color 颜色值
     * @param temp  字符串
     * @param start 起点
     * @param end   结束位置
     * @return
     */
    public static SpannableStringBuilder getSelectionForegroundStr(int color, String temp, int start, int end) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(temp);
        spannableStringBuilder.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色
        return spannableStringBuilder;
    }

    /**
     * 判断是否是pdf url
     *
     * @param pdfUrl
     * @return
     */
    public static boolean isPdfUrl(String pdfUrl) {
        if (!isUrl(pdfUrl)) {
            return false;
        }
        return pdfUrl.toLowerCase().endsWith(".pdf");
    }

    /**
     * 获取数字和字母组合字符数组
     *
     * @return
     */
    public static char[] getInputTypePwdChars() {
        char[] temp = new char[62];
        int len = 0;
        for (char i = 'a'; i < 'Z'; i++) {
            temp[len] = i;
            len++;
        }
        for (char j = '0'; j <= '9'; j++) {
            temp[len] = j;
        }
        return temp;
    }

    public static boolean needUpdate(String appVersion, String comPareVersion) {
        if (isNotEmpty(appVersion) && isNotEmpty(comPareVersion)) {
            String[] appV = appVersion.split("\\.");
            String[] comPV = comPareVersion.split("\\.");
            for (int i = 0; i < appV.length && i < comPV.length; i++) {
                try {
                    if (Integer.valueOf(appV[i]) < Integer.valueOf(comPV[i])) {
                        return true;
                    } else if (Integer.valueOf(appV[i]) > Integer.valueOf(comPV[i])) {
                        return false;
                    }

                } catch (Exception e) {
                    return false;
                }
            }

            if (appV.length < comPV.length) {
                return true;
            }
        }
        return false;
    }


    /**
     * 替换url里指定参数
     *
     * @param url
     * @param queryName
     * @param replaceParam
     * @return
     */
    public static String replaceUrlParma(String url, String queryName, String replaceParam) {
        if (isNotEmpty(url) && isNotEmpty(queryName) && isNotEmpty(replaceParam)) {
            int index = url.indexOf(queryName + "=");
            if (index > 0 && ('?' == url.charAt(index - 1) || '&' == url.charAt(index - 1))) {
                //找到queryName + "=" 并且前一个字符是?/&
                StringBuilder sb = new StringBuilder();
                sb.append(url.substring(0, index)).append(queryName + "=")
                        .append(replaceParam);
                int idx = url.indexOf("&", index);
                if (idx != -1) {
                    sb.append(url.substring(idx));
                }
                url = sb.toString();
            }

        }
        return url;
    }

    /**
     * 比较两个字符串是否等 不区分大小写
     *
     * @param a
     * @param b
     * @return
     */
    public static boolean equals(String a, String b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            return a.equalsIgnoreCase(b);
        }
        return false;
    }

    public static boolean isWalletNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 0;
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 20;
    }


    /**
     * 加密助记词并更新wallet
     *
     * @param mnemonic 助记词
     * @param seedHex
     * @param tradePwd 密码
     * @param wallet   钱包实体用于更新相关字段   @return
     */
    public static String encryptMnemonicAndSeedHex(String mnemonic, String seedHex, String tradePwd, Wallet wallet) {
        byte[] encryptKey = EncryptUtils.encryptSHA256(tradePwd.getBytes(Charset.defaultCharset()));
        String encryptMnemonic = EncryptUtils.encryptAES2HexString(mnemonic.getBytes(Charset.defaultCharset()), encryptKey);
        String mnemonicHash = getSHA256Hex(mnemonic);
        String encryptSeedHex = EncryptUtils.encryptAES2HexString(seedHex.getBytes(Charset.defaultCharset()), encryptKey);
        String seedHexHash = getSHA256Hex(seedHex);
        if (wallet != null) {
            wallet.setEncryptMnemonic(encryptMnemonic);
            wallet.setMnemonicHash(mnemonicHash);
            wallet.setEncryptSeed(encryptSeedHex);
            wallet.setSeedHexHash(seedHexHash);
            wallet.setUpdatedAt(System.currentTimeMillis());
            wallet.setTradePwd(tradePwd);
        }
        return mnemonicHash;
    }

    public static String getSHA256Hex(String mnemonic) {
        return EncryptUtils.encryptSHA256ToString(mnemonic.getBytes(Charset.defaultCharset()));
    }

    /**
     * 解密助记词
     *
     * @param encryptMnemonic 加密助记词的十六进制字符串
     * @param key             密码
     * @return
     */
    public static String decryptMnemonic(String encryptMnemonic, String key) {
        byte[] encryptKey = EncryptUtils.encryptSHA256(key.getBytes(Charset.defaultCharset()));
        String decryptMnemonic = new String(EncryptUtils.decryptHexStringAES(encryptMnemonic, encryptKey), Charset.defaultCharset());
        return decryptMnemonic;
    }
}
