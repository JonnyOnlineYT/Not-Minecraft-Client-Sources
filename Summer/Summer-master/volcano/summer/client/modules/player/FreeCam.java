package volcano.summer.client.modules.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventBlockBB;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class FreeCam extends Module {

	private double oldX;
	private double oldY;
	private double oldZ;
	private float oldYaw;
	private float oldPitch;
	public static EntityOtherPlayerMP player;
	public static Value<Double> flyspeed;

	public FreeCam() {
		super("FreeCam", 0, Category.PLAYER);
		flyspeed = new ClampedValue<Double>("FlySpeed", "flyspeed", 1.0, 1.0, 5.0, this);
	}

	@Override
	public void onEnable() {
		this.mc.thePlayer.noClip = true;
		this.oldX = this.mc.thePlayer.posX;
		this.oldY = this.mc.thePlayer.posY;
		this.oldZ = this.mc.thePlayer.posZ;
		this.oldYaw = this.mc.thePlayer.rotationYaw;
		this.oldPitch = this.mc.thePlayer.rotationPitch;
		(this.player = new EntityOtherPlayerMP(this.mc.theWorld, this.mc.thePlayer.getGameProfile()))
				.clonePlayer(this.mc.thePlayer, true);
		this.player.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
		this.player.rotationYawHead = this.mc.thePlayer.rotationYaw;
		this.player.rotationPitch = this.mc.thePlayer.rotationPitch;
		this.player.setSneaking(this.mc.thePlayer.isSneaking());
		this.mc.theWorld.addEntityToWorld(64199, this.player);
	}

	@Override
	public void onDisable() {
		this.mc.thePlayer.noClip = false;
		this.mc.thePlayer.capabilities.isFlying = false;
		this.mc.thePlayer.setPositionAndRotation(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch);
		this.mc.theWorld.removeEntity(this.player);
		this.mc.timer.timerSpeed = 1.0F;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			if (mc.theWorld == null) {
				this.setState(false);
			}
			setDisplayName("" + this.flyspeed.getValue().floatValue());
			mc.thePlayer.noClip = true;
			mc.thePlayer.fallDistance = 0.0F;
			mc.thePlayer.onGround = false;

			mc.thePlayer.capabilities.isFlying = false;
			mc.thePlayer.motionX = 0.0D;
			mc.thePlayer.motionY = 0.0D;
			mc.thePlayer.motionZ = 0.0D;
			mc.thePlayer.renderArmPitch += 500.0F;
			mc.thePlayer.jumpMovementFactor = this.flyspeed.getValue().floatValue();
			if (mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.motionY += this.flyspeed.getValue().floatValue();
			}
			if (mc.gameSettings.keyBindSneak.pressed) {
				mc.thePlayer.motionY -= this.flyspeed.getValue().floatValue();
			}
		}
		if (event instanceof EventBlockBB) {
			((EventBlockBB) event).boundingBox = null;
		}
		if (event instanceof EventPacketSend) {
			if (((((EventPacketSend) event).packet instanceof C03PacketPlayer))
					|| ((((EventPacketSend) event).packet instanceof C0BPacketEntityAction))
					|| ((((EventPacketSend) event).packet instanceof C0APacketAnimation))
					|| ((((EventPacketSend) event).packet instanceof C02PacketUseEntity))
					|| ((((EventPacketSend) event).packet instanceof C09PacketHeldItemChange))
					|| ((((EventPacketSend) event).packet instanceof C07PacketPlayerDigging))) {
				((EventPacketSend) event).setCancelled(true);
			}
		}
	}
}
