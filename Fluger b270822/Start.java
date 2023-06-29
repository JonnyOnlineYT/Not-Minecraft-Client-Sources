/*
 * Decompiled with CFR 0.150.
 */
import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {
    public static String hwid = "flex";

    public static void main(String[] args) {
        Main.hwid = hwid;
        Main.main(Start.concat(new String[]{"--version", "Fluger", "--accessToken", "0", "--assetIndex", "1.12", "--userProperties", "{}"}, args));
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}

