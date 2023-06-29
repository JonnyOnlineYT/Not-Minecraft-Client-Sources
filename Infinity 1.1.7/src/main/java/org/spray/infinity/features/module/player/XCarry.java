package org.spray.infinity.features.module.player;

import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.mixin.ICloseHandledScreenC2SPacket;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;

@ModuleInfo(category = Category.PLAYER, desc = "Does not allow items that are in the crafting slot to drop ", key = -2, name = "XCarry", visible = true)
public class XCarry extends Module {

	private boolean active;

	@Override
	public void onEnable() {
		active = false;
	}

	@Override
	public void onDisable() {
		if (active)
			Helper.sendPacket(new CloseHandledScreenC2SPacket(Helper.getPlayer().playerScreenHandler.syncId));
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (!event.getType().equals(EventType.SEND))
			return;

		if (event.getPacket() instanceof CloseHandledScreenC2SPacket
				&& ((ICloseHandledScreenC2SPacket) event.getPacket())
						.getSyncId() == Helper.getPlayer().playerScreenHandler.syncId) {
			active = true;
			event.cancel();
		}
	}

}
