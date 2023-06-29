package ru.wendoxd.utils.misc;

import java.util.Random;

public class RandomUtils {

    public static String randomString(int length) {
        StringBuilder sb = new StringBuilder();
        String array = "abcdefgwexsideonthebeat1234567890";
        array += array.toUpperCase();
        for (int i = 0; i < length; i++) {
            sb.append(array.toCharArray()[new Random().nextInt(array.length())]);
        }
        return sb.toString();
    }
}
