package volcano.summer.client.modules.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ModeValue;

public class NoFall extends Module {

	TimerUtil timer = new TimerUtil();
	boolean shouldResend = false;
	public ModeValue noFallMode;

	public NoFall() {
		super("NoFall", 0, Category.PLAYER);
		noFallMode = new ModeValue("NFMode", "Mode", "Vanilla", new String[] { "AAC", "Vanilla" }, this);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {
		this.shouldResend = false;
	}

	public static Block getBlock(double x, double y, double z) {
		return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
	}

	@Override
	public void onEvent(Event event) {
		if (noFallMode.getStringValue().equalsIgnoreCase("AAC")) {
			setDisplayName("AAC");
		} else if (noFallMode.getStringValue().equalsIgnoreCase("Vanilla")) {
			setDisplayName("Vanilla");
		} else {
			setDisplayName(null);
		}
		if (noFallMode.getStringValue().equalsIgnoreCase("AAC")) {
			if (event instanceof EventUpdate) {
				if ((!this.timer.a(500L)) && (mc.thePlayer.onGround) && (this.shouldResend)) {
					mc.thePlayer.motionY = 0.0D;
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, true));
					mc.thePlayer.motionX = 0.0D;
					mc.thePlayer.motionZ = 0.0D;

					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
							mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
				} else {
					this.shouldResend = false;
				}
				if (mc.thePlayer.fallDistance >= 2.0F) {
					for (int i = 0; i < 2; i++) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(
								mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
					}
					this.shouldResend = true;
					this.timer.setLastMs();
				}
			}
		}
		if (noFallMode.getStringValue().equalsIgnoreCase("Vanilla")) {
			if (event instanceof EventPacketSend) {
				if ((((EventPacketSend) event).getPacket() instanceof C03PacketPlayer)) {
					C03PacketPlayer packet = (C03PacketPlayer) ((EventPacketSend) event).getPacket();
					if (Minecraft.thePlayer.fallDistance > 3.0F) {
						packet.field_149474_g = true;
					}
				}
			}
		}
	}
}