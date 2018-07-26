package com.wangke.core.utils;


import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *@author  xu
 *@date 2017/11/30
 *@note TOTP工具类
 **/
public class TOTP {


    /**
     *
     * @param phone 手机号码
     * @return
     */
    public static String getTotpPsd(String phone){

        //以每月的1号做为开始时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        /*Log.d(TAG," 日期为 "+calendar.get(Calendar.YEAR)+" 年 "+(calendar.get(Calendar.MONTH)+1)+" 月 "+calendar.get(Calendar.DAY_OF_MONTH)+" 日 "+
                calendar.get(Calendar.HOUR_OF_DAY)+" 时 "+calendar.get(Calendar.MINUTE)+" 分 "+calendar.get(Calendar.SECOND)+" 秒");*/

        long startTime = (calendar.getTimeInMillis()/1000);
        //Log.d(TAG,"毫秒的时间为 = "+startTime);

        //以31天做为时间步长
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.MONTH, calendar2.get(Calendar.MONTH));
        calendar2.set(Calendar.DAY_OF_MONTH, +32);
        calendar2.set(Calendar.HOUR_OF_DAY, 0);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);

  /*      Log.d(TAG," 31天后 日期为 "+calendar2.get(Calendar.YEAR)+" 年 "+(calendar2.get(Calendar.MONTH)+1)+" 月 "+calendar2.get(Calendar.DAY_OF_MONTH)+" 日 "+
                calendar2.get(Calendar.HOUR_OF_DAY)+" 时 "+calendar2.get(Calendar.MINUTE)+" 分 "+calendar2.get(Calendar.SECOND)+" 秒");
        Log.d(TAG,"31天后毫秒的时间为 = "+(calendar2.getTimeInMillis()/1000));*/

        long s = (calendar2.getTimeInMillis()/1000)-(calendar.getTimeInMillis()/1000);
        String key = "23917234"+phone;//座机号码+用户手机号码做为key
        return  getTotpToken(key,startTime,0,s,4);

    }


    /**
     *  TOTP  密码生成
     * @param key           约定的key，推荐  SHA1 - 20 bytes、SHA256 - 32 bytes、SHA512 - 64 bytes
     * @param T                 开始的时间
     * @param T0                开始计数的时间步长
     * @param X                 时间步长
     * @param otpLength     返回的密码位数
     * @return
     */
    public static String getTotpToken(String key, long T, long T0, long X, int otpLength) {
        String steps = Long.toHexString((T - T0) / X).toUpperCase();
        while (steps.length() < 16)
            steps = "0" + steps;

        int length = key.length();
        String token = "";
        if (length == 64) {
            token = generateTOTP512(key, steps, otpLength + "");
        } else if (length == 32) {
            token = generateTOTP256(key, steps, otpLength + "");
        } else {
            token = generateTOTP(key, steps, otpLength + "");
        }
        return token;
    }

    /**
     * This method uses the JCE to provide the crypto algorithm. HMAC computes a
     * Hashed Message Authentication Code with the crypto hash algorithm as a
     * parameter.
     *
     * @param crypto
     *            : the crypto algorithm (HmacSHA1, HmacSHA256, HmacSHA512)
     * @param keyBytes
     *            : the bytes to use for the HMAC key
     * @param text
     *            : the message or text to be authenticated
     */
    private static byte[] hmac_sha(String crypto, byte[] keyBytes, byte[] text) {
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }

    /**
     * This method converts a HEX string to Byte[]
     *
     * @param hex
     *            : the HEX string
     *
     * @return: a byte array
     */

    private static byte[] hexStr2Bytes(String hex) {
        // Adding one byte to get the right conversion
        // Values starting with "0" can be converted
        byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();

        // Copy all the REAL bytes, not the "first"
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i + 1];
        return ret;
    }

    private static final int[] DIGITS_POWER
            // 0 1 2 3 4 5 6 7 8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    /**
     * This method generates a TOTP value for the given set of parameters.
     *
     * @param key
     *            : the shared secret, HEX encoded
     * @param time
     *            : a value that reflects a time
     * @param returnDigits
     *            : number of digits to return
     *
     * @return: a numeric String in base 10 that includes
     *          {@link } digits
     */

    public static String generateTOTP(String key, String time,
                                      String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA1");
    }

    /**
     * This method generates a TOTP value for the given set of parameters.
     *
     * @param key
     *            : the shared secret, HEX encoded
     * @param time
     *            : a value that reflects a time
     * @param returnDigits
     *            : number of digits to return
     *
     * @return: a numeric String in base 10 that includes
     *          {@link } digits
     */

    public static String generateTOTP256(String key, String time,
                                         String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA256");
    }

    /**
     * This method generates a TOTP value for the given set of parameters.
     *
     * @param key
     *            : the shared secret, HEX encoded
     * @param time
     *            : a value that reflects a time
     * @param returnDigits
     *            : number of digits to return
     *
     * @return: a numeric String in base 10 that includes
     *          {@link } digits
     */

    public static String generateTOTP512(String key, String time,
                                         String returnDigits) {
        return generateTOTP(key, time, returnDigits, "HmacSHA512");
    }

    /**
     * This method generates a TOTP value for the given set of parameters.
     *
     * @param key
     *            : the shared secret, HEX encoded
     * @param time
     *            : a value that reflects a time
     * @param returnDigits
     *            : number of digits to return
     * @param crypto
     *            : the crypto function to use
     *
     * @return: a numeric String in base 10 that includes
     *          {@link } digits
     */

    public static String generateTOTP(String key, String time,
                                      String returnDigits, String crypto) {
        int codeDigits = Integer.decode(returnDigits).intValue();
        String result = null;

        // Using the counter
        // First 8 bytes are for the movingFactor
        // Compliant with base RFC 4226 (HOTP)
        while (time.length() < 16)
            time = "0" + time;

        // Get the HEX in a Byte[]
        byte[] msg = hexStr2Bytes(time);
        byte[] k = hexStr2Bytes(key);
        byte[] hash = hmac_sha(crypto, k, msg);

        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24)
                | ((hash[offset + 1] & 0xff) << 16)
                | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result = "0" + result;
        }
        return result;
    }

    public static void main(String[] args) {
/*        // Seed for HMAC-SHA1 - 20 bytes
        String seed = "3132333435363738393031323334353637383930";
        // Seed for HMAC-SHA256 - 32 bytes
        String seed32 = "3132333435363738393031323334353637383930"
                + "313233343536373839303132";
        // Seed for HMAC-SHA512 - 64 bytes
        String seed64 = "3132333435363738393031323334353637383930"
                + "3132333435363738393031323334353637383930"
                + "3132333435363738393031323334353637383930" + "31323334";
        long T0 = 0;
        long X = 30;
        long testTime[] = {59L, 1111111109L, 1111111111L, 1234567890L,
                2000000000L, 20000000000L};

        String steps = "0";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            System.out.println("+---------------+-----------------------+"
                    + "------------------+--------+--------+");
            System.out.println("|  Time(sec)    |   Time (UTC format)   "
                    + "| Value of T(Hex)  |  TOTP  | Mode   |");
            System.out.println("+---------------+-----------------------+"
                    + "------------------+--------+--------+");

            for (int i = 0; i < testTime.length; i++) {
                long T = (testTime[i] - T0) / X;
                steps = Long.toHexString(T).toUpperCase();
                long time = Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis() / 1000;

                steps = time / 30 + "";
                while (steps.length() < 16)
                    steps = "0" + steps;
                String fmtTime = String.format("%1$-11s", testTime[i]);
                String utcTime = df.format(new Date(testTime[i] * 1000));
                System.out.print("|  " + fmtTime + "  |  " + utcTime + "  | "
                        + steps + " |");
                System.out.println(generateTOTP(seed, steps, "8", "HmacSHA1")
                        + "| SHA1   |");
                System.out.print("|  " + fmtTime + "  |  " + utcTime + "  | "
                        + steps + " |");
                System.out.println(generateTOTP(seed32, steps, "8",
                        "HmacSHA256") + "| SHA256 |");
                System.out.print("|  " + fmtTime + "  |  " + utcTime + "  | "
                        + steps + " |");
                System.out.println(generateTOTP(seed64, steps, "8",
                        "HmacSHA512") + "| SHA512 |");

                System.out.println("+---------------+-----------------------+"
                        + "------------------+--------+--------+");
            }
        } catch (final Exception e) {
            System.out.println("Error : " + e);
        }*/
    }
}