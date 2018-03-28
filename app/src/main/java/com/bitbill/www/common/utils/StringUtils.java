package com.bitbill.www.common.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.TextUtils;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;

import com.bitbill.www.app.AppConstants;
import com.bitbill.www.crypto.CryptoConstants;
import com.bitbill.www.crypto.utils.EncryptUtils;
import com.bitbill.www.model.wallet.db.entity.Wallet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 字符串处理工具 <br>
 * Created by zhuyuanbao on 2016/2/29.<br>
 */
public class StringUtils {

    public static final Pattern IS_CHINESE_CHAR = Pattern.compile("[\u4e00-\u9fa5]");
    public static final Pattern IS_ENGLISH_CHAR = Pattern.compile("[a-zA-Z]");
    public final static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static Pattern PHONE_NUM = Pattern
            .compile("\\d{11}");// 手机号码11位
    private final static Pattern WALLET_ID = Pattern
            .compile("^[a-zA-Z][0-9a-zA-Z_]{5,19}$");//6-20位 以字母开头，支持字母、数字和"_”
    private final static Pattern WALLET_ID_START = Pattern
            .compile("^[a-zA-Z]\\w*");//以字母开头”
    private final static Pattern MNEMONIC = Pattern
            .compile(" +");//空格分隔
    private final static Pattern EMAILER = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern NUMBER_STR = Pattern
            .compile("^[0-9]*$");
    private final static Pattern LOWER_STR = Pattern
            .compile("^[a-z]*$");
    private final static Pattern UPPER_STR = Pattern
            .compile("^[A-Z]*$");
    private final static Pattern SPECIAL_STR = Pattern
            .compile("^[^A-Za-z0-9]*$");

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

    public static boolean isApkUrl(String apkUrl) {
        return isUrl(apkUrl) && apkUrl.toLowerCase().endsWith(".apk");
    }

    /**
     * 数字字符串长度
     *
     * @param string
     * @return
     */
    public static int[] countType(String string) {
        int[] counts = null;
        if (isEmpty(string)) {
            return counts;
        }
        int numberCount = 0;
        int upperCount = 0;
        int lowerCount = 0;
        int specialCount = 0;

        char[] chars = string.toCharArray();
        for (char aChar : chars) {
            if (NUMBER_STR.matcher(String.valueOf(aChar)).matches()) numberCount++;
            if (UPPER_STR.matcher(String.valueOf(aChar)).matches()) upperCount++;
            if (LOWER_STR.matcher(String.valueOf(aChar)).matches()) lowerCount++;
            if (SPECIAL_STR.matcher(String.valueOf(aChar)).matches()) specialCount++;

        }
        counts = new int[]{numberCount, upperCount, lowerCount, specialCount};
        return counts;
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
     * 获取格式化的金额字符串
     *
     * @param damount
     * @return
     */
    public static String getFormatedAmount(long damount) {
        return getFormatedDollers(damount, 8);
    }

    /**
     * 格式化金额字符串，保留指定小数
     * 不显示千分位
     *
     * @param amount 金额
     * @param d      保留小数位数
     * @return
     */
    public static String getFormatedDollers(long amount, int d) {
        NumberFormat format = NumberFormat.getInstance();
        //不显示千分位逗号
        format.setGroupingUsed(false);
        //设置截断模式
        format.setRoundingMode(RoundingMode.FLOOR);
        //设置允许的最大保留小数
        format.setMaximumFractionDigits(d);
        format.setMinimumFractionDigits(d);
        return format.format(amount);
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

    public static boolean isWalletIdValid(String id) {
        //钱包ID支持6-20位 以字母开头，支持字母、数字和"_”
        if (id == null) return false;
        return WALLET_ID.matcher(id).matches();
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 20;
    }


    /**
     * 加密助记词并更新wallet
     *
     * @param mnemonic          助记词
     * @param seedHex
     * @param extendedPublicKey 拓展公钥md5十进制
     * @param internalPublicKey
     * @param tradePwd          密码
     * @param wallet            钱包实体用于更新相关字段
     */
    public static String encryptMnemonicAndSeedHex(String mnemonic, String seedHex, String extendedPublicKey, String internalPublicKey, String tradePwd, Wallet wallet) {
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
            wallet.setMnemonic(mnemonic);
            wallet.setSeedHex(seedHex);
            wallet.setExtentedPublicKey(extendedPublicKey);
            wallet.setInternalPublicKey(internalPublicKey);
        }
        return mnemonicHash;
    }

    /**
     * 加密seedHex并更新wallet
     *
     * @param seedHex
     * @param extendedPublicKey 拓展公钥md5十进制
     * @param tradePwd          密码
     * @param wallet            钱包实体用于更新相关字段
     * @return seedHexHash
     */
    public static String encryptSeedHex(String seedHex, String extendedPublicKey, String tradePwd, Wallet wallet) {
        byte[] encryptKey = EncryptUtils.encryptSHA256(tradePwd.getBytes(Charset.defaultCharset()));
        String encryptSeedHex = EncryptUtils.encryptAES2HexString(seedHex.getBytes(Charset.defaultCharset()), encryptKey);
        String seedHexHash = getSHA256Hex(seedHex);
        if (wallet != null) {
            wallet.setEncryptSeed(encryptSeedHex);
            wallet.setSeedHexHash(seedHexHash);
            wallet.setUpdatedAt(System.currentTimeMillis());
            wallet.setTradePwd(tradePwd);
            wallet.setSeedHex(seedHex);
            wallet.setExtentedPublicKey(extendedPublicKey);
        }
        return seedHexHash;
    }

    /**
     * 校验用户输入的密码
     *
     * @param pwd    密码
     * @param wallet 钱包实体用于更新相关字段   @return
     */
    public static boolean checkUserPwd(String pwd, Wallet wallet) {
        if (wallet == null) {
            return false;
        }
        String encryptSeed = wallet.getEncryptSeed();
        if (encryptSeed == null) {
            return false;
        }
        String decryptSeedHex = decryptByPwd(encryptSeed, pwd);
        if (decryptSeedHex == null) {
            return false;
        }
        String seedHexHash = getSHA256Hex(decryptSeedHex);
        return equals(seedHexHash, wallet.getSeedHexHash());
    }

    public static String getSHA256Hex(String original) {
        return EncryptUtils.encryptSHA256ToString(original.getBytes(Charset.defaultCharset()));
    }

    /**
     * 解密助记词或seed
     *
     * @param encrypt 加密十六进制字符串
     * @param pwd     密码
     * @return
     */
    public static String decryptByPwd(String encrypt, String pwd) {
        byte[] encryptKey = EncryptUtils.encryptSHA256(pwd.getBytes(Charset.defaultCharset()));
        byte[] decryptBytes = EncryptUtils.decryptHexStringAES(encrypt, encryptKey);
        if (decryptBytes == null) {
            return null;
        }
        String decrypt = new String(decryptBytes, Charset.defaultCharset());
        return decrypt;
    }

    /**
     * 通过钱包构建ExtendedKeysHash
     *
     * @param wallets
     * @return
     */
    public static String buildExtendedKeysHash(List<Wallet> wallets) {
        String extendedKeysHash = "";
        if (isEmpty(wallets)) {
            return extendedKeysHash;
        }
        for (int i = 0; i < wallets.size(); i++) {
            String extentedPublicKey = wallets.get(i).getExtentedPublicKey();
            // check xpubkey
            if (StringUtils.isEmpty(extentedPublicKey)) {
                continue;
            }
            extendedKeysHash += EncryptUtils.encryptMD5ToString(extentedPublicKey);
            if (i < wallets.size() - 1) {
                extendedKeysHash += "|";
            }
        }
        return extendedKeysHash;
    }

    public static boolean isEmpty(List list) {
        if (list == null) {
            return true;
        }
        return list.isEmpty();
    }

    public static boolean isEmpty(String[] arrays) {
        if (arrays == null) {
            return true;
        }
        return arrays.length == 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static boolean isRequiredLength(String s) {
        if (s == null) return false;
        return s.length() >= 6 && s.length() <= 20;
    }

    /**
     * set amount type face
     *
     * @param context
     * @param amountTextView
     */
    public static void setAmountTypeface(Context context, TextView amountTextView) {
        amountTextView.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/FF_DIN_Condensed_Bold.otf"));

    }

    /**
     * 剪切钱包名称
     *
     * @param name
     * @return
     */
    public static String cutWalletName(String name) {
        if (isEmpty(name)) return "";
        if (name.length() <= 12) return name;
        return name.substring(0, 12) + "..";
    }

    public static boolean isValidMnemonic(String mnemonic) {
        return !isEmpty(mnemonic);
    }


    /**
     * 设置下划线
     *
     * @param textView
     */
    public static void setTextViewUnLine(TextView textView) {
        if (textView == null) {
            return;
        }
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textView.getPaint().setAntiAlias(true);//抗锯齿
    }

    /**
     * 以字母开头
     *
     * @param walletId
     * @return
     */
    public static boolean isValidIdStart(String walletId) {
        return WALLET_ID_START.matcher(String.valueOf(walletId.charAt(0))).matches();
    }

    /**
     * btc -> satoshi
     *
     * @param sendAmount
     * @return
     */
    public static long btc2Satoshi(String sendAmount) {
        if (sendAmount == null) return 0;
        //8位小数
        BigDecimal amount = new BigDecimal(sendAmount);
        BigDecimal satoshi = new BigDecimal(CryptoConstants.SATOSHI);
        return amount.multiply(satoshi).longValue();
    }

    public static String multiplyValue(double btcRate, String amount) {
        try {
            //8位小数
            DecimalFormat df = new DecimalFormat("#.##");
            df.setMinimumFractionDigits(2);
            df.setGroupingUsed(true);
            BigDecimal btcCnyDecimal = new BigDecimal(btcRate);
            BigDecimal amountDecimal = new BigDecimal(amount);

            BigDecimal multiply = btcCnyDecimal.multiply(amountDecimal);
            multiply.setScale(2, RoundingMode.FLOOR);
            return df.format(multiply);
        } catch (Exception e) {
            return "0.00";
        }
    }

    public static String satoshi2btc(Long fee) {
        if (fee == null) {
            return "0.00";
        }
        BigDecimal feeDecimal = new BigDecimal(fee);
        //8位小数
        DecimalFormat df = new DecimalFormat("#.########");
        df.setMinimumFractionDigits(2);
        return df.format(feeDecimal.divide(new BigDecimal(CryptoConstants.SATOSHI)));
    }

    public static Date getDate(String date) {
        //get date by string
        Date parse = null;
        try {
            parse = yyyyMMddHHmmss.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse;
    }

    public static String formatDateTime(String date) {
        //format date by local
        try {
            Date parse = yyyyMMddHHmmss.parse(date);
            return formatDateTime(parse.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDateTime(Date date) {
        if (date == null) {
            return "-- -- --";
        }
        return formatDateTime(date.getTime());
    }


    public static String formatDateTime(long dateTime) {
        return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(new Date(dateTime));
    }

    /**
     * @param dateTime
     * @return
     */
    public static String formatDate(long dateTime) {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date(dateTime));
    }

    public static void setEditable(EditText etText, boolean editable) {
        if (etText == null) {
            return;
        }
        etText.setFocusable(editable);
        etText.setFocusableInTouchMode(editable);
        etText.setLongClickable(editable);
        etText.setInputType(editable ? etText.getInputType() : InputType.TYPE_NULL);
    }

    /**
     * 联系人备份id 随机取UUID md5并取前十位
     *
     * @return
     */
    public static String getContactKey() {
        return EncryptUtils.encryptMD5ToString(DeviceUtil.getUUID()).substring(0, 10);
    }

    /**
     * 设备id 随机取UUID md5
     *
     * @return
     */
    public static String getUUIDMD5() {
        return EncryptUtils.encryptMD5ToString(DeviceUtil.getUUID());
    }

    public static String getNameLabel(String name) {

        if (isEmpty(name)) {
            return "";
        }
        return String.valueOf(name.trim().toUpperCase().charAt(0));
    }

    public static boolean isZero(String amount) {
        if (isEmpty(amount)) {
            return true;
        }
        BigDecimal bigDecimal = new BigDecimal(amount);
        return bigDecimal.doubleValue() == 0;
    }

    public static <T> List<T> removeDuplicateList(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return new ArrayList(new HashSet(list));
    }

    /**
     * 是否为空 包含"无"
     *
     * @param s
     * @return
     */
    public static boolean isNone(String s) {
        if (isEmpty(s)) {
            return true;
        }
        return s.trim().equals(AppConstants.NONE);
    }

    public static boolean equals(Locale currentLocale, Locale compareLocale) {
        if (currentLocale == null || compareLocale == null) {
            return false;
        }
        return currentLocale.getLanguage().equals(compareLocale.getLanguage())
                && currentLocale.getCountry().equals(compareLocale.getCountry());
    }
}
