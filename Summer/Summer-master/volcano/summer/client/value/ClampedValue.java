package volcano.summer.client.value;

import volcano.summer.base.manager.module.Module;

public class ClampedValue<T> extends Value<T> {
	private T min;
	private T max;
	private float sliderX;

	public ClampedValue(String name, String commandName, T value, T min, T max, Module module) {
		super(name, commandName, value, module);
		this.min = min;
		this.max = max;
	}

	public T getMax() {
		return this.max;
	}

	public void setMax(T max) {
		this.max = max;
	}

	public void set(T value) {
		this.value = value;
	}

	public T getMin() {
		return this.min;
	}

	public void setMin(T min) {
		this.min = min;
	}

	public float getSliderX() {
		return this.sliderX;
	}

	public void setSliderX(final float sliderX) {
		this.sliderX = sliderX;
	}
}