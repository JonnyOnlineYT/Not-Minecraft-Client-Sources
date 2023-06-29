/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.helpers.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {
    public static String SHA1(String sha1) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] array = md.digest(sha1.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString(array[i] & 0xFF | 0x100).substring(1, 3));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
}

