package org.spray.heaven.features.module.modules.misc;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.Timer;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;

@ModuleInfo(name = "AutoJoin", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class AutoJoin extends Module {

	private final Setting targetSlot = register(new Setting("Target Slot", 1, 1, 54));
	private final Setting maxDelay = register(new Setting("Max Delay", 570.0D, 100D, 10000D));
	private final Setting minDelay = register(new Setting("Min Delay", 400.0D, 100D, 10000D));

	private final Timer timer = new Timer();

	@Override
	public void onUpdate() {
		setSuffix((int) targetSlot.getValue() + " slot");
		if (mc.currentScreen instanceof GuiChest) {
			for (int i = 0; i < targetSlot.getMaxValue() - 1; i++) {
				int target = (int) targetSlot.getValue();
				if ((target - 1) == i) {

					Slot slot = mc.player.openContainer.getSlot(i);

					if (timer.hasReached(MathUtil.random(minDelay.getValue(), maxDelay.getValue()))) {
						quickItem(i);
					}
				}
			}
		}
	}

	private void quickItem(int slot) {
		mc.playerController.windowClick(mc.player.openContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
		timer.reset();
	}

}
