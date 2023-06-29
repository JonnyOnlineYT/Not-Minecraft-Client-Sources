package volcano.summer.client.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventNoSlow;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.util.OBTimer;

public class NoSlowDown extends Module {

	OBTimer timer = new OBTimer();

	public NoSlowDown() {
		super("NoSlowDown", 0, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventNoSlow) {
			((EventNoSlow) event).setCancelled(true);
		} else if (event instanceof EventPacketSend) {
			EventPacketSend eventPacket = (EventPacketSend) event;
			if (!(eventPacket.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition))
				return;
			if (mc.thePlayer == null || !mc.thePlayer.isBlocking() || !timer.hasReached(1000))
				return;
			mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255,
					mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
			timer.reset();
		}
	}
}