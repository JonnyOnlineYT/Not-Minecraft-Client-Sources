package me.wintware.client.utils.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils {
   public static void addChatMessage(String message) {
      Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "[WW] " + ChatFormatting.RESET + message));
   }
}
