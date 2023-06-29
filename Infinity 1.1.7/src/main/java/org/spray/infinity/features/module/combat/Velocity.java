package org.spray.infinity.features.module.combat;

import java.util.ArrayList;
import java.util.Arrays;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.mixin.IEntityVelocityUpdateS2CPacket;
import org.spray.infinity.mixin.IExplosionS2CPacket;
import org.spray.infinity.utils.Helper;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

@ModuleInfo(name = "Velocity", key = -2, visible = true, desc = "Anti knockback", category = Category.COMBAT)
public class Velocity extends Module {

	public Setting mode = new Setting(this, "Mode", "Matrix 6.1.0",
			new ArrayList<>(Arrays.asList("Packet", "Reverse", "Matrix 6.1.0")));

	public Setting vertical = new Setting(this, "Vertical", 0.0D, 0.0D, 1.0D);
	public Setting horizontal = new Setting(this, "Horizontal", 0.0D, 0.0D, 1.0D);

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.RECIEVE)) {
			if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket) {
				EntityVelocityUpdateS2CPacket ep = (EntityVelocityUpdateS2CPacket) event.getPacket();
				if (ep.getId() == Helper.getPlayer().getId()) {
					double velX = (ep.getVelocityX() / 8000d - Helper.getPlayer().getVelocity().x)
							* horizontal.getCurrentValueDouble();
					double velY = (ep.getVelocityY() / 8000d - Helper.getPlayer().getVelocity().y)
							* vertical.getCurrentValueDouble();
					double velZ = (ep.getVelocityZ() / 8000d - Helper.getPlayer().getVelocity().z)
							* horizontal.getCurrentValueDouble();

					int x = (int) (velX * 8000 + Helper.getPlayer().getVelocity().x * 8000);
					int y = (int) (velY * 8000 + Helper.getPlayer().getVelocity().y * 8000);
					int z = (int) (velZ * 8000 + Helper.getPlayer().getVelocity().z * 8000);

					switch (mode.getCurrentMode()) {
					case "Packet":
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityX((int) x);
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityY((int) y);
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityZ((int) z);
						break;

					case "Reverse":
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityX((int) -x);
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityY((int) -y);
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityZ((int) -z);
						break;

					case "Matrix 6.1.0":
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityX(x);
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityY(y);
						((IEntityVelocityUpdateS2CPacket) ep).setVelocityZ(z);
						break;
					}
				}
			}

			if (event.getPacket() instanceof ExplosionS2CPacket) {
				ExplosionS2CPacket es = (ExplosionS2CPacket) event.getPacket();
				double velX = (es.getPlayerVelocityX() / 8000d - Helper.getPlayer().getVelocity().x)
						* horizontal.getCurrentValueDouble();
				double velY = (es.getPlayerVelocityY() / 8000d - Helper.getPlayer().getVelocity().y)
						* vertical.getCurrentValueDouble();
				double velZ = (es.getPlayerVelocityZ() / 8000d - Helper.getPlayer().getVelocity().z)
						* horizontal.getCurrentValueDouble();

				int x = (int) (velX * 8000 + Helper.getPlayer().getVelocity().x * 8000);
				int y = (int) (velY * 8000 + Helper.getPlayer().getVelocity().y * 8000);
				int z = (int) (velZ * 8000 + Helper.getPlayer().getVelocity().z * 8000);

				switch (mode.getCurrentMode()) {
				case "Packet":
					((IExplosionS2CPacket) es).setX(x);
					((IExplosionS2CPacket) es).setY(y);
					((IExplosionS2CPacket) es).setZ(z);
					break;

				case "Reverse":
					((IExplosionS2CPacket) es).setX(-x);
					((IExplosionS2CPacket) es).setY(-y);
					((IExplosionS2CPacket) es).setZ(-z);
					break;

				case "Matrix 6.1.0":
					((IExplosionS2CPacket) es).setX(x);
					((IExplosionS2CPacket) es).setY(y);
					((IExplosionS2CPacket) es).setZ(z);
					break;
				}
			}
		}
	}

	/* EntityMixin action */
	public void pushAway(Entity e, Entity e2, CallbackInfo ci) {
		if (e != Helper.getPlayer() && e2 != Helper.getPlayer() || !mode.getCurrentMode().equals("Matrix 6.1.0"))
			return;

		double x = e2.getX() - e.getX();
		double z = e2.getZ() - e.getZ();
		double dist = Math.max(Math.abs(x), Math.abs(z));

		if (dist < 0.01)
			return;

		dist = Math.sqrt(dist);
		x /= dist;
		z /= dist;

		double multiplier = 1.0D / dist;
		if (multiplier > 1.0D) {
			multiplier = 1.0D;
		}

		double collisionReduction = 0.05000000074505806D;

		x *= multiplier * 0.05 * collisionReduction;
		z *= multiplier * 0.05 * collisionReduction;

		addPushVelocity(e, Helper.getPlayer(), -x, -z);
		addPushVelocity(e2, Helper.getPlayer(), x, z);

		ci.cancel();
	}

	private void addPushVelocity(Entity e, ClientPlayerEntity player, double x, double z) {
		if (e.equals(player) && !player.isRiding()) {
			Entity entity = player;
			entity.setVelocity(entity.getVelocity().getX() + x, entity.getVelocity().getY(),
					entity.getVelocity().getZ() + z);
			player.velocityDirty = true;
		}
	}

}
