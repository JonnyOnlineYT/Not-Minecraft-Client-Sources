package ua.apraxia.utility.math;

import ua.apraxia.utility.Utility;

public class RandomUtils
        implements Utility {
    public static double getRandom(int endnumber) {
        return Math.random() * (double)endnumber + 1.0;
    }

    public static int getIntRandom(int endnumber) {
        return (int)(Math.random() * (double)endnumber + 1.0);
    }

    public static double getDoubleRandom(int endnumber) {
        return Math.random() * (double)endnumber + 1.0;
    }

    public static double getDoubleRandom2(double endnumber) {
        return Math.random() * endnumber + 1.0;
    }
}
