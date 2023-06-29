package ru.rockstar.client.ui.settings.impl;

import java.util.function.Supplier;

import ru.rockstar.client.ui.settings.Setting;

public class BooleanSetting extends Setting {
    private boolean state;
    private String desc;
    public int greenAnim;

    public BooleanSetting(String name, String desc, boolean state, Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.state = state;
        setVisible(visible);
    }

    public BooleanSetting(String name, boolean state, Supplier<Boolean> visible) {
        this.name = name;
        this.state = state;
        setVisible(visible);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean getBoolValue() {
        return state;
    }

    public void setBoolValue(boolean state) {
        this.state = state;
    }

	public boolean getState() {
        return state;
    }

	public int getgreenAnim() {
        this.greenAnim = this.getState() ? 255 : 1;
        return this.greenAnim;
    }
}
