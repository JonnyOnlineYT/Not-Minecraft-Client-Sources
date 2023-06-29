package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.ClipEvent;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification;
import org.spray.heaven.util.MovementUtil;

@ModuleInfo(name = "FreeCam", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class FreeCam extends Module {

	private EntityOtherPlayerMP entity;
	private Vec3d position;
	private float yaw;
	private float pitch;

	private Setting speed = register(new Setting("Speed", 1.2, 0, 5));
	private Setting spacketplayer = register(new Setting("Fly Kick Bypass", true));
	private Setting autoDisable = register(new Setting("To Disable After Die", true));
	private Setting hitDisable = register(new Setting("To Disable After Hit", true));

	@Override
	public void onEnable() {
		if (mc.world != null) {
			this.entity = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
			this.entity.copyLocationAndAnglesFrom(mc.player);
			if (mc.player.getRidingEntity() != null) {
				mc.player.dismountRidingEntity();
			}
			this.entity.rotationYaw = mc.player.rotationYaw;
			this.entity.rotationYawHead = mc.player.rotationYawHead;
			this.entity.inventory.copyInventory(mc.player.inventory);
			mc.world.addEntityToWorld(69420, this.entity);
			this.position = mc.player.getPositionVector();
			this.yaw = mc.player.rotationYaw;
			this.pitch = mc.player.rotationPitch;
			mc.player.noClip = true;
		}
	}

	@Override
	public void onDisable() {
		if (mc.world != null) {
			if (this.entity != null) {
				mc.world.removeEntity(this.entity);
			}
			if (this.position != null) {
				mc.player.setPosition(this.position.xCoord, this.position.yCoord, this.position.zCoord);
			}
			mc.player.rotationYaw = this.yaw;
			mc.player.rotationPitch = this.pitch;
			mc.player.noClip = false;
			mc.player.motionX = 0;
			mc.player.motionY = 0;
			mc.player.motionZ = 0;
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.RECIEVE) && spacketplayer.isToggle() && event.getPacket() instanceof SPacketPlayerPosLook) {
			event.cancel();
		}
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (event.getType().equals(EventType.PRE)) {
			mc.player.motionY = 0;
			mc.player.jumpMovementFactor = (float) speed.getValue();
			MovementUtil.strafe(MovementUtil.calcMoveYaw(mc.player.rotationYaw), speed.getValue());

			mc.player.setSprinting(false);

			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				mc.player.motionY += speed.getValue();
			}

			if (mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.motionY -= speed.getValue();
			}
			event.cancel();
		}

		if (mc.player.hurtTime > 0 && hitDisable.isToggle()) {
			Wrapper.getNotification().call(ChatFormatting.GRAY + "[FreeCam] " + ChatFormatting.RED + "You receive a hit - FreeCam was disabled!", Notification.Type.INFO, IFont.WEB_SMALL);
			Wrapper.getModule().get("Freecam").disable();
		}

		if (mc.player.isDead && autoDisable.isToggle()) {
			Wrapper.getNotification().call(ChatFormatting.GRAY + "[FreeCam] " + ChatFormatting.RED + "You died - FreeCam was disabled!", Notification.Type.INFO, IFont.WEB_SMALL);
			Wrapper.getModule().get("Freecam").disable();
		}
	}
	
	@EventTarget
	public void onClip(ClipEvent event) {
		event.cancel();
	}
}
