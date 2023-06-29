package org.spray.heaven.features.module.modules.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.StepEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.network.play.client.CPacketPlayer;

@ModuleInfo(name = "Step", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Step extends Module {

	private Setting mode = register(
			new Setting("Mode", "MatrixLast", new ArrayList<>(Arrays.asList("Vanilla", "MatrixLast"))));

	private boolean resetTimer;
	private double previousX, previousY, previousZ;
	private double offsetX, offsetY, offsetZ;
	private double frozenX, frozenZ;
	private byte cancelStage;

	@Override
	public void onDisable() {
		mc.player.stepHeight = 0.5F;
		mc.getTimer().reset();
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());

//        if (!mode.getCurrentMode().equalsIgnoreCase("Vanilla"))
//            return;

		switch (mode.getCurrentMode()) {
		case "Vanilla":
			if (this.resetTimer) {
				this.resetTimer = false;
				mc.getTimer().setSpeed(1.0F);
			}
			if (mc.player.isCollidedVertically && !mc.gameSettings.keyBindJump.isPressed())
				mc.player.stepHeight = 1.0F;
			else {
				mc.player.stepHeight = 0.5F;
			}

			double minY = mc.player.getEntityBoundingBox().minY - mc.player.posY;
			boolean canStep = minY >= 0.625;

			if (canStep) {
				Wrapper.sendPacket(
						new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ, false));
				this.resetTimer = true;
				mc.getTimer().setSpeed(minY < 2.0 ? 0.6F : 0.3F);
			}
			break;
		}
	}

	@EventTarget
	public void onStep(StepEvent event) {
		// Entity.java | 935, 1040 line code
		if (mode.getCurrentMode().equalsIgnoreCase("MatrixLast")) {
			if (event.getStepHeight() > 1)
				return;

			if (event.getType().equals(EventType.PRE))
				event.setStepHeight(1);
			if (event.getType().equals(EventType.POST)) {
				if (event.getStepHeight() <= 1) {
					Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketPlayer.Position(mc.player.posX,
							mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
				}

			}
		}
	}
}
