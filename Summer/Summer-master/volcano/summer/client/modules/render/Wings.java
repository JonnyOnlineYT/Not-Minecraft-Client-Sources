package volcano.summer.client.modules.render;

import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class Wings extends Module {
	public static Value<Double> red;
	public static Value<Double> green;
	public static Value<Double> blue;
	public static Value<Double> size;
	public static Value<Boolean> rainbow;

	public Wings() {
		super("Wings", 0, Category.RENDER);
		rainbow = new Value<Boolean>("Rainbow", "rainbow", false, this);
		red = new ClampedValue<Double>("Red", "red", 255.0, 1.0, 255.0, this);
		green = new ClampedValue<Double>("Green", "green", 255.0, 1.0, 255.0, this);
		blue = new ClampedValue<Double>("Blue", "blue", 0.0, 1.0, 255.0, this);
		size = new ClampedValue<Double>("Size", "size", 80.0, 1.0, 450.0, this);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {

	}

}
