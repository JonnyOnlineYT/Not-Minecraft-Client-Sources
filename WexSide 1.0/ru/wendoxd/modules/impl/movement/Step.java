package ru.wendoxd.modules.impl.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.events.impl.player.EventEntityStep;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.player.MoveUtils;

public class Step extends Module {

	private Frame step_frame = new Frame("Step");
	private final CheckBox step = new CheckBox("Step").markArrayList("Step");
	private final SelectBox mode = new SelectBox("Mode", new String[] { "Vanilla", "Matrix", "SunRise" },
			() -> step.isEnabled(true));
	private final Slider height = new Slider("Height", 1, 1, 10, 0.1, () -> step.isEnabled(true));
	private boolean resetTimer;

	@Override
	protected void initSettings() {
		step.markSetting("Step");
		step_frame.register(step, mode, height);
		MenuAPI.movement.register(step_frame);
	}

	public int o;

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntityStep && step.isEnabled(false)) {
			if (resetTimer) {
				resetTimer = false;
				mc.timer.timerSpeed = 1;
			}
			if (((EventEntityStep) event).getType() == EventEntityStep.Type.PRE && !MoveUtils.isInLiquid()) {
				if (mc.player.isCollidedVertically && !mc.gameSettings.keyBindJump.pressed) {
					((EventEntityStep) event).setStepHeight(height.getFloatValue());
				}
			} else if (((EventEntityStep) event).getType() == EventEntityStep.Type.POST) {
				boolean canStep = mc.player.getEntityBoundingBox().minY - mc.player.posY >= 0.625;
				if (mode.get() == 1) {
					if (canStep) {
						resetTimer = true;
						mc.timer.timerSpeed = 0.37f;
						mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
								mc.player.posY + 0.42, mc.player.posZ, false));
					}
				} else if (mode.get() == 0) {
					if (canStep) {
						mc.player.stepHeight = height.getFloatValue();
					}
				} else if (mode.get() == 2) {
					if (canStep) {
						resetTimer = true;
						mc.timer.timerSpeed = 0.24f;
						mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
								mc.player.posY + 0.42, mc.player.posZ, false));
					}
				}
			}
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == step) {
				if (!((EventSwapState) event).getState()) {
					mc.player.stepHeight = 0.625f;
					mc.timer.timerSpeed = 1;
				}
			}
		}
	}
}
