package com.ly.add.sponge.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 处理MD5 HASH等算法的信息摘要
 * @author hxf14879
 *
 */
public class MessageDigestUtil {

    private final static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    
    private final static String ALGORITHM_SHA1 = "SHA1";

    private final static String ALGORITHM_MD5 = "MD5";
    
    /**
     * SHA-1 安全哈希算法
     * @param msg
     * @return
     */
    public static String getSHA1(String msg) {
        return digest(msg, ALGORITHM_SHA1);
    }
    
    /**
     * MD5 信息-摘要算法5
     * @param msg
     * @return
     */
    public static String getMD5(String msg) {
        return digest(msg, ALGORITHM_MD5);
    }

    /**
     * 信息摘要处理过程
     * @param msg
     * @param algorithm
     * @return
     */
    private static String digest(String msg, String algorithm) {
            byte[] btInput = msg.getBytes();
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            digest.update(btInput);
            byte[] md = digest.digest();
            int len = md.length;
            int k = 0;
            char tag[] = new char[len * 2];
            for (int i = 0; i < len; i++) {
                byte t = md[i];
                tag[k++] = hexDigits[t >>> 4 & 0xf];
                tag[k++] = hexDigits[t & 0xf];
            }
            return new String(tag);        
    }
}
