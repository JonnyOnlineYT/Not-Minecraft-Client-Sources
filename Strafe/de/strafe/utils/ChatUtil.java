package de.strafe.utils;

import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

@UtilityClass
public class ChatUtil {

    public void sendChatWithPrefix(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§5Strafe§7] " + message));
    }

}
