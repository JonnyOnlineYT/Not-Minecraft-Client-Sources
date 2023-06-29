package org.spray.heaven.protect;

import net.minecraft.init.Bootstrap;

public class Crasher {

    public static void crash() {
        Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
        System.exit(-2);
    }

}
