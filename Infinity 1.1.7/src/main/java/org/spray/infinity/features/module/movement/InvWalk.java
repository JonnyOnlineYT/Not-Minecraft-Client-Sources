package org.spray.infinity.features.module.movement;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.utils.Helper;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

@ModuleInfo(category = Category.MOVEMENT, desc = "Allows you to walk with an open inventory", key = -2, name = "InvWalk", visible = true)
public class InvWalk extends Module {

	@Override
	public void onUpdate() {
		if (Helper.MC.currentScreen != null
				&& !(Helper.MC.currentScreen instanceof ChatScreen)) {
			
			for (KeyBinding k : new KeyBinding[] { Helper.MC.options.keyForward,
					Helper.MC.options.keyBack, Helper.MC.options.keyLeft,
					Helper.MC.options.keyRight, Helper.MC.options.keyJump,
					Helper.MC.options.keySprint }) {
				k.setPressed(InputUtil.isKeyPressed(Helper.MC.getWindow().getHandle(),
						InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
			}
		}
	}

}
