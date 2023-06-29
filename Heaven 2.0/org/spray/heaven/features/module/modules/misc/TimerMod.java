package org.spray.heaven.features.module.modules.misc;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.features.module.modules.display.ArrayListMod;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.ui.clickui.Colors;
import org.spray.heaven.ui.draggable.Dragging;
import org.spray.heaven.util.MathUtil;
import org.spray.heaven.util.MovementUtil;
import org.spray.heaven.util.render.ColorUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

@ModuleInfo(name = "Timer", category = Category.MISC, visible = true, key = Keyboard.KEY_NONE)
public class TimerMod extends Module {

	private final Setting value = register(new Setting("Value", 2.0, 0.3, 15.0));
	private final Setting smart = register(new Setting("Smart", false));
	public final Dragging drag = Wrapper.createDrag(this, "smarttimer", 40, 40);

	private boolean active;
	private int ticks;

	@Override
	public void onDisable() {
		mc.getTimer().reset();
	}

	@Override
	public void onRender(int scaleWidth, int scaleHeight, float delta) {
		if (smart.isToggle()) {
			drag.setWidth(60);
			drag.setHeight(20);
			float x = drag.getX();
			float y = drag.getY();

			float currentPos = MathUtil.clamp((ticks) / (50), 0, 1);
			float pos = (100 - (ticks * 2f)) / 100;
			
			ArrayListMod list = Wrapper.getModule().get("Arraylist");

			Drawable.drawBlurredShadow((int) x - 2, (int) y, (int) drag.getWidth() + 4, (int) drag.getHeight(), 9,
					new Color(17, 17, 17, 210));
			RoundedShader.drawRound((int) x - 2, (int) y, (int) drag.getWidth() + 4, (int) drag.getHeight(), 4,
					new Color(46, 46, 46, 250));
			IFont.WEB_SMALL.drawCenteredString(MathUtil.round(100 - (ticks * 2f), 1) + "%",
					(float) x + drag.getWidth() / 2, (float) y, -1);
			RoundedShader.drawGradientHorizontal(x + 2, y + drag.getHeight() - 7, (drag.getWidth() - 4) * pos, 3, 2,
					ColorUtil.getListColor(list, 0),
					ColorUtil.getListColor(list, 190));
		}
	}

	@EventTarget
	public void onMotion(MotionEvent event) {
		setSuffix(String.valueOf(MathUtil.round(value.getValue(), 1)));
		if (event.getType() != EventType.PRE)
			return;

		if (mc.player != null && mc.world != null) {
			if (smart.isToggle()) {
				if (ticks <= 50 && !active && MovementUtil.isMoving()) {
					ticks++;
					mc.getTimer().setSpeed((float) value.getValue());
				}
				if (ticks == 50) {
					active = true;
				}
				if (active) {
					mc.getTimer().reset();
					if (mc.getTimer().getSpeed() <= 1)
						ticks--;
				}
				if (ticks == 0) {
					active = false;
				}
			} else
				mc.getTimer().setSpeed((float) value.getValue());
		} else
			mc.getTimer().reset();
	}
}
