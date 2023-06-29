package org.spray.heaven.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PushOutBlockEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import com.darkmagician6.eventapi.EventTarget;

@ModuleInfo(name = "NoPush", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class NoPush extends Module {

	public final Setting water = register(new Setting("Water", true));
	public final Setting entity = register(new Setting("Entities", true));
	public final Setting blocks = register(new Setting("Blocks", true));

	// Returnable callback EntityLivingBase.java canBePushed()V method

	@EventTarget
	public void onPushOutOfBlock(PushOutBlockEvent event) {
		if (blocks.isToggle())
			event.cancel();
	}
}
