package org.spray.heaven.features.module.modules.movement;

import org.lwjgl.input.Keyboard;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.MovementUtil;

@ModuleInfo(name = "BedrockClip", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class BedrockClip extends Module {

	private Setting onDamage = register(new Setting("On damage", true));
	private boolean clipped;

	@Override
	public void onEnable() {
		if (!onDamage.isToggle()) {
			clip();
			clipped = true;
		}
	}
	
	@Override
	public void onDisable() {
		clipped = false;
	}

	@EventTarget
	public void onMotionUpdate(MotionEvent event) {
		if (onDamage.isToggle() && mc.player.hurtTime > 0 && mc.player.posY > 0) {
			clip();
			clipped = true;
		}
		if (clipped) {
			mc.player.onGround = true;
			mc.player.motionY = 0;
			event.cancel();
		}
	}
	
	private void clip() {
		MovementUtil.vClip(-(mc.player.posY + 10));
		mc.getConnection().sendPacket(new CPacketVehicleMove(mc.player));
		mc.getConnection().sendPacket(new CPacketPlayer(true));
	}

}
