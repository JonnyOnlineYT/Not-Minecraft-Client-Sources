package ru.wendoxd.modules.impl.player;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventEntitySync;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.player.MoveUtils;

public class NoWeb extends Module {

	private Frame noweb_frame = new Frame("NoWeb");
	public static CheckBox noweb = new CheckBox("NoWeb").markArrayList("NoWeb");
	public static SelectBox mode = new SelectBox("Mode", new String[] { "Vanilla", "Matrix", "Solid" },
			() -> noweb.isEnabled(true));
	private final Slider speed = new Slider("Speed", 1, 0.1, 2, 0.2, () -> noweb.isEnabled(true) && mode.get() == 1);
	private final Slider jumpmotion = new Slider("Motion Y", 1, 0, 10, 0.5,
			() -> noweb.isEnabled(true) && mode.get() == 1);

	@Override
	protected void initSettings() {
		noweb.markSetting("NoWeb");
		noweb_frame.register(noweb, mode, speed, jumpmotion);
		MenuAPI.movement.register(noweb_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntitySync && noweb.isEnabled(false)) {
			if (mode.get() == 0) {
				if (mc.player.isInWeb) {
					mc.player.isInWeb = false;
				}
			} else if (mode.get() == 1) {
				BlockPos blockPos = new BlockPos(mc.player.posX, mc.player.posY - 0.6, mc.player.posZ);
				Block block = mc.world.getBlockState(blockPos).getBlock();
				if (mc.player.isInWeb) {
					mc.player.motionY += 2;
				} else if (Block.getIdFromBlock(block) == 30) {
					mc.player.motionY += jumpmotion.getFloatValue();
					MoveUtils.setSpeed(speed.getFloatValue());
					mc.gameSettings.keyBindJump.pressed = false;
				}
			}
		}
	}
}
