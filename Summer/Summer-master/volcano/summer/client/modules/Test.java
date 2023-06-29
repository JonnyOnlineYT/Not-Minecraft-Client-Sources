package volcano.summer.client.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventTick;

public class Test extends Module {

	private float rotationYaw, rotationPitch;

	public Test() {
		super("Test", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
		this.rotationYaw = mc.thePlayer.rotationYaw;
		this.rotationPitch = mc.thePlayer.rotationPitch;
	}

	@Override
	public void onDisable() {
		this.rotationYaw = 0;
		this.rotationPitch = 0;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventTick) {
			mc.thePlayer.setSprinting(false);

			mc.thePlayer.rotationYaw = this.rotationYaw;
			mc.thePlayer.rotationPitch = this.rotationPitch;

			mc.thePlayer.motionY += 0.01;
			mc.thePlayer.motionX *= 0.5f;
			mc.thePlayer.motionZ *= 0.5f;

			double yaw = Math.toRadians(this.rotationYaw);
			double x = -Math.sin(yaw) * 9;
			double z = Math.cos(yaw) * 9;
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x,
					mc.thePlayer.posY + 1, mc.thePlayer.posZ + z, mc.thePlayer.onGround));
		}
	}
}