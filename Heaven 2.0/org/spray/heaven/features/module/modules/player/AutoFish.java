package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;

@ModuleInfo(name = "AutoFish", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AutoFish extends Module {

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (!event.getType().equals(EventType.RECIEVE))
			return;
		
		if (event.getPacket() instanceof SPacketSoundEffect) {
			SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
			if (packet.getCategory() == SoundCategory.NEUTRAL
					&& packet.getSound() == SoundEvents.ENTITY_BOBBER_SPLASH) {
				if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod
						|| mc.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod) {
					Wrapper.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
					mc.player.swingArm(EnumHand.MAIN_HAND);
					Wrapper.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
					mc.player.swingArm(EnumHand.MAIN_HAND);
				}
			}
		}
	}
}
