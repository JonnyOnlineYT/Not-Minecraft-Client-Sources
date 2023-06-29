package org.spray.heaven.features.module.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.main.Wrapper;

@ModuleInfo(name = "MiddleClickFriend", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class MiddleClickFriend extends Module {

	private boolean clicked;

	@Override
	public void onUpdate() {
		if (mc.currentScreen != null)
			return;

		if (Mouse.isButtonDown(2)) {
			if (!this.clicked) {
				RayTraceResult result = mc.objectMouseOver;
				if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY) {
					Entity entity = result.entityHit;
					if (entity instanceof EntityPlayer) {
						Wrapper.getFriend().switchFriend(entity.getName());
					}
				}
			}
			this.clicked = true;
		} else {
			this.clicked = false;
		}
	}

}
