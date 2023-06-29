package ru.wendoxd.modules.impl.player;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class AutoFish extends Module {
	public static Frame frame = new Frame("AutoFish");
	public static CheckBox autoFish = new CheckBox("AutoFish");

	public void initSettings() {
		frame.register(autoFish);
		MenuAPI.player.register(frame);
	}

	public void onEvent(Event evt) {
		if (evt instanceof EventReceivePacket && autoFish.isEnabled(false)) {
			EventReceivePacket event = (EventReceivePacket) evt;
			if (event.getPacket() instanceof SPacketSoundEffect) {
				final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
				if (packet.getCategory() == SoundCategory.NEUTRAL
						&& packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH
						&& (AutoFish.mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod
								|| AutoFish.mc.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod)) {
					AutoFish.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
					AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
					AutoFish.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
					AutoFish.mc.player.swingArm(EnumHand.MAIN_HAND);
				}
			}
		}
	}
}
