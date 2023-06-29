package org.spray.heaven.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.Timer;

import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.util.EnumHandSide;

@ModuleInfo(name = "HandFlicker", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class HandFlicker extends Module {

	private final Setting delay = register(new Setting("Delay", 200, 1, 1500));
	private final Timer timer = new Timer();

	private EnumHandSide hand = mc.gameSettings.mainHand;

	@Override
	public void onDisable() {
		hand = mc.gameSettings.mainHand;
		mc.player.connection
				.sendPacket(new CPacketClientSettings(mc.gameSettings.language, mc.gameSettings.renderDistanceChunks,
						mc.gameSettings.chatVisibility, mc.gameSettings.chatColours, 1, hand));
	}

	@Override
	public void onUpdate() {
		if (timer.hasReached(delay.getValue())) {
			hand = hand.opposite();
			mc.player.getHeldItemMainhand();
			mc.player.connection.sendPacket(
					new CPacketClientSettings(mc.gameSettings.language, mc.gameSettings.renderDistanceChunks,
							mc.gameSettings.chatVisibility, mc.gameSettings.chatColours, 1, hand));
			timer.reset();
		}
	}
}
