package org.spray.infinity.features.module.combat;

import org.spray.infinity.event.ClickEvent;
import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MathAssist;
import org.spray.infinity.utils.PacketUtil;
import org.spray.infinity.utils.PacketUtil.InteractType;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;

@ModuleInfo(category = Category.COMBAT, desc = "Hit a player from a long distance", key = -2, name = "Reach", visible = true)
public class Reach extends Module {

	public Setting maxReach = new Setting(this, "Max Range", 4.1D, 0D, 6.0D);
	public Setting minReach = new Setting(this, "Min Range", 3.8D, 0D, 6.0D);

	public Setting packetSpoof = new Setting(this, "Packets Spoof", false);

	private double reach;
	private double lastReach = 0;

	@Override
	public void onEnable() {
		lastReach = 0;
	}

	@Override
	public void onUpdate() {
		reach = MathAssist.random(minReach.getCurrentValueDouble(), maxReach.getCurrentValueDouble());

		if (lastReach != 0)
			setSuffix(String.valueOf(MathAssist.round(lastReach, 1)));
	}

	@EventTarget
	public void onClick(ClickEvent event) {
		EntityUtil.updateTargetRaycast(Helper.MC.targetedEntity, reach, Helper.getPlayer().getYaw(),
				Helper.getPlayer().getPitch());

	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
			PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket) event.getPacket();
			if (PacketUtil.getInteractType(packet) == InteractType.INTERACT_AT) {

				lastReach = Helper.getPlayer().distanceTo(PacketUtil.getEntity(packet));
				if (packetSpoof.isToggle())
					PacketUtil.cancelKeepAlive(event);
			}
		}
	}
}
