package org.spray.heaven.protect;

import org.spray.heaven.protect.components.StateCheck;
import org.spray.keyauth.KeyAuthProvider;

import by.radioegor146.nativeobfuscator.Native;

@Native
public class Provider {

    private static final KeyAuthProvider keyAuth;
    private static ProtectRegister protectRegister;
    private static final StateCheck checker;
    public static boolean LOGGED;

    static {
        keyAuth = new KeyAuthProvider();

        checker = new StateCheck();
    }

    public static KeyAuthProvider getKeyAuth() {
        return keyAuth;
    }

    public static ProtectRegister getProtectRegister() {
        return protectRegister;
    }

    public static void setProtectRegister(ProtectRegister protectRegister) {
        Provider.protectRegister = protectRegister;
    }

    public static StateCheck getChecker() {
        return checker;
    }

}
