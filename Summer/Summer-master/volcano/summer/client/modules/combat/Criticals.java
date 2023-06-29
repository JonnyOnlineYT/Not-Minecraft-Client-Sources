package volcano.summer.client.modules.combat;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.modules.movement.Speed;
import volcano.summer.client.value.ModeValue;

public class Criticals extends Module {

	public static ModeValue critsmode;
	private static long lastCheck = AuraUtil.getSystemTime();
	private static long prevMS = 0L;

	public Criticals() {
		super("Criticals", 0, Category.COMBAT);
		critsmode = new ModeValue("CritMode", "Mode", "Packet",
				new String[] { "Packet", "Jump", "Hypixel", "HypixelTEST" }, this);
	}

	@Override
	public void onEnable() {
		AuraUtil.reset();
	}

	@Override
	public void onDisable() {
		AuraUtil.reset();
	}

	public static class AuraUtil {

		public static void reset() {
			lastCheck = getSystemTime();
			prevMS = getSystemTime();
		}

		private static long getSystemTime() {
			return System.nanoTime() / (long) (1E6);
		}

		public static boolean hasReach(float mil) {
			return getTimePassed() >= (mil);
		}

		private static long getTimePassed() {
			return getSystemTime() - lastCheck;
		}

		public static boolean hasReached(double milliseconds) {
			return getSystemTime() - prevMS >= 1000.0F / (float) milliseconds;
		}
	}

	@Override
	public void onEvent(Event event) {
		if (critsmode.getStringValue().equalsIgnoreCase("Jump")) {
			setDisplayName("Jump");
		} else if (critsmode.getStringValue().equalsIgnoreCase("Packet")) {
			setDisplayName("Packet");
		} else if (critsmode.getStringValue().equalsIgnoreCase("Hypixel")) {
			setDisplayName("Hypixel");
		} else if (critsmode.getStringValue().equalsIgnoreCase("HypixelTEST")) {
			setDisplayName("HypixelTEST");
		} else {
			setDisplayName("");
		}

	}

	public static void doCrits() {

		if (!mc.thePlayer.onGround || mc.thePlayer.isInWater() || mc.thePlayer.isOnLadder())
			return;
		if (critsmode.getStringValue().equalsIgnoreCase("HypixelTEST")) {

		}
		if (critsmode.getStringValue().equalsIgnoreCase("Hypixel")) {

			if (EventPacketSend.getPacket() instanceof C02PacketUseEntity) {
				C02PacketUseEntity packet = (C02PacketUseEntity) EventPacketSend.getPacket();
				if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
					if (mc.thePlayer.onGround) {

						if (!Summer.moduleManager.getModule(Speed.class).state)
							doCrits();
						Summer.tellPlayer("Crit");
					}
				}

			}
		}
		if (critsmode.getStringValue().equalsIgnoreCase("Packet")) {
			if (Summer.moduleManager.getModule(Speed.class).getState()) {
				return;
			}
			if (!mc.thePlayer.onGround) {
				return;
			}
			if (mc.thePlayer.func_180799_ab() || mc.thePlayer.isInWater()) {
				return;
			}
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 0.05, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY + 0.012511, mc.thePlayer.posZ, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
					mc.thePlayer.posY, mc.thePlayer.posZ, false));
//			Summer.tellPlayer("Crit");
		}
		if (critsmode.getStringValue().equalsIgnoreCase("Jump")) {
			mc.thePlayer.motionY = 0.4F;
		}
	}
}
