package volcano.summer.client.modules.movement;

import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketSend;

public class Blink extends Module {

	private EntityOtherPlayerMP playerCopy;
	private Queue<C03PacketPlayer> queue = new LinkedList();

	public Blink() {
		super("Blink", 0, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		if (this.mc.thePlayer == null) {
			return;
		}
		this.playerCopy = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile());
		this.playerCopy.inventory = this.mc.thePlayer.inventory;
		this.playerCopy.inventoryContainer = this.mc.thePlayer.inventoryContainer;
		this.playerCopy.setPositionAndRotation(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ,
				this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch);
		this.playerCopy.rotationYawHead = this.mc.thePlayer.rotationYawHead;
		this.mc.theWorld.addEntityToWorld(-1, this.playerCopy);
	}

	@Override
	public void onDisable() {
		for (Packet packet : this.queue) {
			this.mc.thePlayer.sendQueue.addToSendQueue(packet);
		}
		this.queue.clear();
		this.mc.theWorld.removeEntityFromWorld(-1);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPacketSend) {
			if (((((EventPacketSend) event).packet instanceof C0BPacketEntityAction))
					|| ((((EventPacketSend) event).packet instanceof C03PacketPlayer))
					|| ((((EventPacketSend) event).packet instanceof C02PacketUseEntity))
					|| ((((EventPacketSend) event).packet instanceof C0APacketAnimation))
					|| ((((EventPacketSend) event).packet instanceof C08PacketPlayerBlockPlacement))) {
				if (((((EventPacketSend) event).getPacket() instanceof C03PacketPlayer))
						&& ((mc.thePlayer.posX != mc.thePlayer.lastTickPosX)
								|| (mc.thePlayer.posY != mc.thePlayer.lastTickPosY)
								|| (mc.thePlayer.posZ != mc.thePlayer.lastTickPosZ))) {
					((EventPacketSend) event).setCancelled(true);
					this.queue.offer((C03PacketPlayer) ((EventPacketSend) event).getPacket());
					setDisplayName("" + this.queue.size());
				}
			}
		}
	}
}
