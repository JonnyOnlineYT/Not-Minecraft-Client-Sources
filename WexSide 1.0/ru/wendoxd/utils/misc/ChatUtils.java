package ru.wendoxd.utils.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils {

	private static final Minecraft mc = Minecraft.getMinecraft();

	public static void addChatMessage(String message) {
		mc.player.addChatMessage(new TextComponentString("\2477[" + ChatFormatting.DARK_GRAY + "W"
				+ ChatFormatting.GRAY + "ex" + ChatFormatting.DARK_GRAY + "S" + ChatFormatting.GRAY + "ide"
				+ ChatFormatting.RESET + "\2477] " + ChatFormatting.RESET + message));
	}

}
