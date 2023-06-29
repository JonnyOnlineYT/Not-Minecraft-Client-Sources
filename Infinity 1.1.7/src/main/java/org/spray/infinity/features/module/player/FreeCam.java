package org.spray.infinity.features.module.player;

import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.event.PlayerMoveEvent;
import org.spray.infinity.event.PushOutBlockEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.PacketUtil;
import org.spray.infinity.utils.entity.OtherPlayer;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.util.math.Vec3d;

@ModuleInfo(category = Category.PLAYER, desc = "Allows you to fly freely and explore the territory", key = -2, name = "FreeCam", visible = true)
public class FreeCam extends Module {

	private Setting speed = new Setting(this, "Speed", 0.8, 0, 2.0);

	private OtherPlayer spawnPlayer = null;

	private boolean prevFly;
	private float prevFlySpeed;

	private double x, y, z;
	private float yaw, pitch;

	@Override
	public void onEnable() {
		x = Helper.getPlayer().getX();
		y = Helper.getPlayer().getY();
		z = Helper.getPlayer().getZ();
		yaw = Helper.getPlayer().getYaw();
		pitch = Helper.getPlayer().getPitch();

		spawnPlayer = new OtherPlayer(Helper.getPlayer());
		spawnPlayer.setBoundingBox(spawnPlayer.getBoundingBox().expand(0.1));
		spawnPlayer.spawn();

		prevFly = Helper.getPlayer().getAbilities().flying;
		prevFlySpeed = Helper.getPlayer().getAbilities().getFlySpeed();
	}

	@Override
	public void onDisable() {
		if (spawnPlayer != null)
			spawnPlayer.despawn();
		spawnPlayer = null;

		Helper.getPlayer().getAbilities().flying = prevFly;
		Helper.getPlayer().getAbilities().setFlySpeed(prevFlySpeed);

		Helper.getPlayer().refreshPositionAndAngles(x, y, z, yaw, pitch);
		Helper.getPlayer().setVelocity(Vec3d.ZERO);
	}

	@Override
	public void onUpdate() {
		Helper.getPlayer().setOnGround(false);
		Helper.getPlayer().getAbilities().setFlySpeed((float) (speed.getCurrentValueDouble() / 5));
		Helper.getPlayer().getAbilities().flying = true;
	}

	@EventTarget
	public void onPushBlock(PushOutBlockEvent event) {
		event.cancel();
	}

	@EventTarget
	public void onClientMove(PlayerMoveEvent event) {
		Helper.getPlayer().noClip = true;
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		PacketUtil.cancelMotionPackets(event);
		PacketUtil.cancelCommandPackets(event);
	}

}
