package org.spray.infinity.utils;

import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.mixin.IPlayerMoveC2SPacket;
import org.spray.infinity.mixin.IPlayerPositionLookS2CPacket;

import com.darkmagician6.eventapi.types.EventType;

import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.KeepAliveS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class PacketUtil {

	/**
	 * Packet rotation
	 * 
	 * @param event
	 * @param yaw
	 * @param pitch
	 */
	public static void setRotation(PacketEvent event, float yaw, float pitch) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket) {
				PlayerMoveC2SPacket cp = (PlayerMoveC2SPacket) event.getPacket();
				if (!Float.isNaN(yaw) || !Float.isNaN(pitch)) {
					((IPlayerMoveC2SPacket) cp).setYaw(yaw);
					((IPlayerMoveC2SPacket) cp).setPitch(pitch);
					((IPlayerMoveC2SPacket) cp).setLook(true);
				}
			}
		}
	}

	/**
	 * Dl9 drugih buget viden rotacii tvoei cameri ,a ne packetov
	 * 
	 * @param event
	 */
	public static void cancelServerRotation(PacketEvent event) {
		if (event.getType().equals(EventType.RECIEVE)) {
			if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
				PlayerPositionLookS2CPacket serverLook = (PlayerPositionLookS2CPacket) event.getPacket();
				((IPlayerPositionLookS2CPacket) serverLook).setYaw(Helper.getPlayer().getYaw());
				((IPlayerPositionLookS2CPacket) serverLook).setPitch(Helper.getPlayer().getPitch());
			}
		}
	}

	public static void setPosition(PacketEvent event, double x, double y, double z) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket) {
				PlayerMoveC2SPacket cp = (PlayerMoveC2SPacket) event.getPacket();
				((IPlayerMoveC2SPacket) cp).setX(x);
				((IPlayerMoveC2SPacket) cp).setY(y);
				((IPlayerMoveC2SPacket) cp).setZ(z);
				((IPlayerMoveC2SPacket) cp).setPos(true);
			}
		}
	}

	public static void setPosition(PacketEvent event, double x, double y, double z, boolean ground) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket) {
				PlayerMoveC2SPacket cp = (PlayerMoveC2SPacket) event.getPacket();
				((IPlayerMoveC2SPacket) cp).setX(x);
				((IPlayerMoveC2SPacket) cp).setY(y);
				((IPlayerMoveC2SPacket) cp).setZ(z);
				((IPlayerMoveC2SPacket) cp).setOnGround(ground);
				((IPlayerMoveC2SPacket) cp).setPos(true);
			}
		}
	}

	public static void setOnGround(PacketEvent event, boolean ground) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket) {
				PlayerMoveC2SPacket cp = (PlayerMoveC2SPacket) event.getPacket();
				((IPlayerMoveC2SPacket) cp).setOnGround(ground);
			}
		}
	}

	public static void setY(PacketEvent event, double y) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket) {
				PlayerMoveC2SPacket cp = (PlayerMoveC2SPacket) event.getPacket();
				((IPlayerMoveC2SPacket) cp).setY(y);
			}
		}
	}

	public static void cancelMotionPackets(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket) {
				event.cancel();
			}
		}
	}

	public static void cancelCommandPackets(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof ClientCommandC2SPacket) {
				event.cancel();
			}
		}
	}

	public static void cancelServerKeepAlive(PacketEvent event) {
		if (event.getType().equals(EventType.RECIEVE)) {
			if (event.getPacket() instanceof KeepAliveS2CPacket) {
				event.cancel();
			}
		}
	}

	public static void cancelKeepAlive(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {
			if (event.getPacket() instanceof KeepAliveC2SPacket) {
				event.cancel();
			}
		}
	}
	
	public static Entity getEntity(PlayerInteractEntityC2SPacket packet) {
		PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
		packet.write(packetBuf);

		return Helper.getWorld().getEntityById(packetBuf.readVarInt());
	}
	
	public static InteractType getInteractType(PlayerInteractEntityC2SPacket packet) {
		PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
		packet.write(packetBuf	);

		packetBuf.readVarInt();
		return packetBuf.readEnumConstant(InteractType.class);
	}

	public static enum InteractType {
		INTERACT,
		ATTACK,
		INTERACT_AT
	}

}
