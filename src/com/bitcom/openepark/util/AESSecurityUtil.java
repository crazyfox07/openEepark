package com.bitcom.openepark.util;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


public class AESSecurityUtil {
    public static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    public static final String KEY_ALGORITHM = "AES";
    public static final int KEY_SIZE = 128;

    public static String getKeyStr() throws Exception {
        byte[] b = getKey().getEncoded();
        return Base64.encodeBase64String(b);
    }


    public static Key getKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        SecretKey secretKey = kg.generateKey();
        return secretKey;
    }


    private static String decrypt(byte[] data, byte[] key) throws Exception {
        Key k = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(key);

        cipher.init(2, k, ivSpec);
        return new String(cipher.doFinal(data), "UTF-8");
    }


    public static String decrypt(String data, String key) throws Exception {
        return decrypt(Base64.decodeBase64(data), Base64.decodeBase64(key));
    }


    public static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Key k = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec ivSpec = new IvParameterSpec(key);

        cipher.init(1, k, ivSpec);
        return cipher.doFinal(data);
    }


    public static String encrypt(String data, String key) throws Exception {
        byte[] dataBytes = data.getBytes("UTF-8");
        byte[] keyBytes = Base64.decodeBase64(key);
        return Base64.encodeBase64String(encrypt(dataBytes, keyBytes));
    }

    public static void main(String[] args) throws Exception {
        String key = "PAwoajiMfO5irTw9CqCAqQ==";
        String mingwen = "topviewers";
        String miwen = encrypt(mingwen, key);
        System.out.println("密文：" + miwen);
        System.out.println("解密：" + decrypt(miwen, key));
    }
}



