package ua.apraxia.modules.settings.impl;

import ua.apraxia.modules.settings.Setting;

public class SliderSetting extends Setting {
    public float value;
    public float min;
    public float max;
    public float widthAnimated;
    public float printAnimated;
    public float increment;
    public boolean pressed;
    public int alphaText;

    public SliderSetting(String name, float value, float min, float max, float increment) {
        super(name);
        this.value = value;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }
    public void setValueNumber(float value) {
        this.value = value;
    }
}
