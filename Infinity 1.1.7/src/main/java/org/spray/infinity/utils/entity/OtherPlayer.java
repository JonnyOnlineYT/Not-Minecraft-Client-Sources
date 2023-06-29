package org.spray.infinity.utils.entity;

import org.spray.infinity.utils.Helper;

import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class OtherPlayer extends OtherClientPlayerEntity {

	public OtherPlayer() {
		this(Helper.getPlayer());
	}

	public OtherPlayer(PlayerEntity player) {
		this(player, player.getX(), player.getY(), player.getZ());
	}

	public OtherPlayer(PlayerEntity player, double x, double y, double z) {
		super(Helper.getWorld(), player.getGameProfile());
		copyPositionAndRotation(player);
		setYaw(headYaw = bodyYaw = player.getYaw());
		getInventory().main.set(getInventory().selectedSlot, player.getMainHandStack());
		getInventory().offHand.set(0, player.getOffHandStack());
		getInventory().armor.set(0, player.getInventory().armor.get(0));
		getInventory().armor.set(1, player.getInventory().armor.get(1));
		getInventory().armor.set(2, player.getInventory().armor.get(2));
		getInventory().armor.set(3, player.getInventory().armor.get(3));
	}

	public void spawn() {
		Helper.getWorld().addEntity(this.getId(), this);
	}

	public void despawn() {
		Helper.getWorld().removeEntity(this.getId(), Entity.RemovalReason.DISCARDED);
	}

}
