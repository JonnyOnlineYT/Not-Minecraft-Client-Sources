package ru.wendoxd.modules.impl.movement;

import net.minecraft.util.math.MathHelper;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.menu.EventSwapState;
import ru.wendoxd.events.impl.misc.EventAnimation;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.visual.animation.DynamicAnimation;

public class Timer extends Module {
	public static Timer instance;
	private Frame timer_frame = new Frame("Timer");
	private final CheckBox timer = new CheckBox("Timer").markArrayList("Timer");
	private final Slider speed = new Slider("Speed", 2, 0.1, 10, 0.192, () -> timer.isEnabled(true));
	private final CheckBox smart = (CheckBox) new CheckBox("Smart", () -> timer.isEnabled(true))
			.markDescription("Автоматическое отключение таймера\nпри накоплении violation.");
	public Slider maxTicks = new Slider("Bound", 0, 0, 15, 0, () -> smart.isEnabled(true));
	private DynamicAnimation violation = new DynamicAnimation();
	public static long lastUpdateTime;
	public static double value;

	public Timer() {
		instance = this;
	}

	public static void init() {
		System.out.println("asda #2");
	}

	public static int getMin() {
		return -(15 - instance.maxTicks.getIntValue());
	}

	@Override
	protected void initSettings() {
		timer.markSetting("Timer");
		speed.modifyPath("Speed_2");
		timer_frame.register(timer, speed, smart, maxTicks);
		MenuAPI.player.register(timer_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && timer.isEnabled(false)) {
			update();
		}
		if (event instanceof EventAnimation) {
			this.violation.update();
		}
		if (event instanceof EventSwapState) {
			if (((EventSwapState) event).getCheckBox() == timer) {
				if (!((EventSwapState) event).getState()) {
					mc.timer.timerSpeed = 1;
				}
			}
		}
	}

	public void update() {
		if (!smart.isEnabled(false) || canEnableTimer(speed.getFloatValue() + 0.2f)) {
			mc.timer.timerSpeed = Math.max(speed.getFloatValue() + (mc.player.ticksExisted % 2 == 0 ? -0.2f : 0.2f),
					0.1f);
		} else {
			mc.timer.timerSpeed = 1;
		}
	}

	public boolean canEnableTimer(float speed) {
		double predictVl = (50.0 - (double) 50 / speed) / 50.0;
		return predictVl + value < 10 - this.speed.getDoubleValue();
	}

	public boolean canEnableTimerIgnoreSettings(float speed) {
		double predictVl = (50.0 - (double) 50 / speed) / 50.0;
		return predictVl + value < 10;
	}

	public static void m(int i) {
		long now = System.currentTimeMillis();
		long timeElapsed = now - lastUpdateTime;
		lastUpdateTime = now;
		value += (50.0 - (double) timeElapsed) / 50.0;
		value -= 0.001;
		value = MathHelper.clamp((double) value, (double) instance.getMin(), (double) 25.0);
	}
}
