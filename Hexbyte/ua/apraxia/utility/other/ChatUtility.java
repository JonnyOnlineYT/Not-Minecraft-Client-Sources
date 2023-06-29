//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.utility.other;

import ua.apraxia.utility.Utility;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtility implements Utility {
    public static String ChatPrefix;

    public ChatUtility() {
    }

    public static void addChatMessage(String message) {
        mc.player.sendMessage(new TextComponentString(ChatPrefix + message));
    }

    static {
        ChatPrefix = "§7[" + TextFormatting.WHITE + "hexbyte§7]  -> " + TextFormatting.GRAY;
    }
}
