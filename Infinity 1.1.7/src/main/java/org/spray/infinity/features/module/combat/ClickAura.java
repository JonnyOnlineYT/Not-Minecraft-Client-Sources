package org.spray.infinity.features.module.combat;

import org.spray.infinity.event.ClickEvent;
import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.event.RotationEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.PacketUtil;
import org.spray.infinity.utils.RotationUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@ModuleInfo(category = Category.COMBAT, desc = "Attacking target on click", key = -2, name = "ClickAura", visible = true)
public class ClickAura extends Module {

	// targets
	private Setting players = new Setting(this, "Players", true);
	private Setting friends = new Setting(this, "Friends", false).setVisible(() -> players.isToggle());
	private Setting invisibles = new Setting(this, "Invisibles", true);
	private Setting mobs = new Setting(this, "Mobs", true);
	private Setting animals = new Setting(this, "Animals", false);

	private Setting throughWalls = new Setting(this, "Through Walls", false);

	private Setting releaseShield = new Setting(this, "Let go of the shield on impact", false);

	private Setting rotation = new Setting(this, "Rotation", true);
	private Setting look = new Setting(this, "Look View", true).setVisible(() -> rotation.isToggle());

	private Setting fov = new Setting(this, "FOV", 120D, 0D, 360D);

	private Setting range = new Setting(this, "Range", 3.5D, 0D, 6D);

	// target
	public static Entity target;

	@Override
	public void onDisable() {
		target = null;
	}

	@EventTarget
	public void onClick(ClickEvent event) {

		target = EntityUtil.getTarget(this.range.getCurrentValueDouble(), fov.getCurrentValueDouble(),
				players.isToggle(), friends.isToggle(), invisibles.isToggle(), mobs.isToggle(), animals.isToggle(),
				throughWalls.isToggle());

		if (target == null)
			return;

		if (!Helper.MC.options.keyAttack.isPressed())
			return;

		float[] rotate = RotationUtil.lookAtEntity(target);

		if (rotation.isToggle() && look.isToggle()) {
			Helper.getPlayer().setYaw(rotate[0]);
			Helper.getPlayer().setPitch(rotate[1]);
		}
		if (releaseShield.isToggle() && Helper.getPlayer().isBlocking())
			Helper.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM,
					new BlockPos(0, 0, 0), Direction.DOWN));

		Helper.MC.interactionManager.attackEntity(Helper.getPlayer(), target);
		EntityUtil.swing(true);

		if (releaseShield.isToggle() && Helper.getPlayer().isBlocking())
			Helper.sendPacket(new PlayerInteractItemC2SPacket(Hand.OFF_HAND));

	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		float[] rotate = RotationUtil.lookAtEntity(target);
		if (!Helper.MC.options.keyAttack.isPressed())
			return;

		if (rotation.isToggle()) {
			PacketUtil.setRotation(event, rotate[0], rotate[1]);
		}
	}

	@EventTarget
	public void onRotation(RotationEvent event) {
		float[] rotate = RotationUtil.lookAtEntity(target);
		if (target == null)
			return;

		if (!Helper.MC.options.keyAttack.isPressed())
			return;

		if (rotation.isToggle()) {
			event.setYaw(rotate[0]);
			event.setPitch(rotate[1]);
		}
		event.cancel();
	}

}
