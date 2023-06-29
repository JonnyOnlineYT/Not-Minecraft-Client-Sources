package ru.wendoxd.modules.impl.movement;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.BlockUtils;

public class Eagle extends Module {

	private Frame eagle_frame = new Frame("Eagle");
	private final CheckBox eagle = (CheckBox) new CheckBox("Eagle").markArrayList("Eagle")
			.markDescription("Автоматический Shift на краю блока");

	@Override
	protected void initSettings() {
		eagle.markSetting("Eagle");
		eagle_frame.register(eagle);
		MenuAPI.movement.register(eagle_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && eagle.isEnabled(false)) {
			BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY - 1, mc.player.posZ);
			mc.gameSettings.keyBindSneak.pressed = mc.player.onGround && BlockUtils.getBlock(pos) == Blocks.AIR;
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == eagle) {
				if (!(((EventSwapState) event).getState())) {
					mc.gameSettings.keyBindSneak.pressed = false;
				}
			}
		}
	}
}
