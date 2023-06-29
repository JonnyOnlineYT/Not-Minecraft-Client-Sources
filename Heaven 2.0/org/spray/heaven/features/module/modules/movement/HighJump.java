package org.spray.heaven.features.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.math.MathHelper;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.EntityUtil;

import java.util.concurrent.TimeUnit;

@ModuleInfo(name = "HighJump", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class HighJump extends Module {

	@EventTarget
	public void motion1(MotionEvent event) {
		if (mc.player.onGround) {
			mc.player.jump();
		}
		new Thread(() -> {
			mc.player.motionY = 9f;
			try {
				TimeUnit.MILLISECONDS.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mc.player.motionY = 8.742f;
			this.disable();
		}).start();
	}
}
