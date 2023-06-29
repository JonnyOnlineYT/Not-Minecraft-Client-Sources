package org.spray.infinity.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.Packet;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public class Helper {

	public static MinecraftClient MC = MinecraftClient.getInstance();

	public static ClientPlayerEntity getPlayer() {
		return MC.player;
	}

	public static ClientWorld getWorld() {
		return MC.world;
	}

	public static void sendPacket(Packet<?> packet) {
		getPlayer().networkHandler.sendPacket(packet);
	}

	public static void message(String message) {
		MC.inGameHud.getChatHud()
				.addMessage(new LiteralText(Formatting.BLUE + "Infinity" + Formatting.WHITE + ": " + message));
	}

	public static void openScreen(Screen screen) {
		MC.openScreen(screen);
	}

	public static void openUrl(String url) {
		Util.getOperatingSystem().open(url);
	}

	public static boolean isCut(int code) {
		return code == 88 && hasControlDown() && !hasShiftDown() && !hasAltDown();
	}

	public static boolean isPaste(int code) {
		return code == 86 && hasControlDown() && !hasShiftDown() && !hasAltDown();
	}

	public static boolean isCopy(int code) {
		return code == 67 && hasControlDown() && !hasShiftDown() && !hasAltDown();
	}

	public static boolean isSelectAll(int code) {
		return code == 65 && hasControlDown() && !hasShiftDown() && !hasAltDown();
	}

	public static boolean hasControlDown() {
		if (MinecraftClient.IS_SYSTEM_MAC) {
			return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 343)
					|| InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 347);
		} else {
			return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 341)
					|| InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 345);
		}
	}

	public static boolean hasShiftDown() {
		return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 340)
				|| InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 344);
	}

	public static boolean hasAltDown() {
		return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 342)
				|| InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), 346);
	}

}