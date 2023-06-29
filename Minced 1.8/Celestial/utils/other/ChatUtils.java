package Celestial.utils.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.utils.Helper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtils implements Helper {

    public static String chatPrefix =ChatFormatting.WHITE + "\2477(" + TextFormatting.LIGHT_PURPLE + "Celestial" + ChatFormatting.WHITE +"\2477) " + ChatFormatting.RESET;

    public static void addChatMessage(String message) {
        mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
    }
}
