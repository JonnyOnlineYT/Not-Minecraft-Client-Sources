package Celestial.utils.math;

import Celestial.utils.Helper;

public class Randomizer
        implements Helper {
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
