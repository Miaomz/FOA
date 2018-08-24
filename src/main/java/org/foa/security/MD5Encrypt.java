package org.foa.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 王川源
 * 本类用于对用户密码进行MD5加密
 */
public class MD5Encrypt {

    public static String MD5(String old){
        String newS = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(old.getBytes("utf-8"));
            byte[] md = messageDigest.digest();
            //将密文转为16进制字符串
            StringBuffer md5Buffer = new StringBuffer();
            for (byte aMd : md) {
                if (Integer.toHexString(0xFF & aMd).length() == 1)
                    md5Buffer.append("0").append(Integer.toHexString(0xFF & aMd));
                else md5Buffer.append(Integer.toHexString(0xFF & aMd));
            }
            newS = md5Buffer.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newS;
    }

}

