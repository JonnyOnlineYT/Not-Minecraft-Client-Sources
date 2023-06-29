package ua.apraxia.modules.settings.impl;

import ua.apraxia.modules.settings.Setting;

public class BooleanSetting extends Setting {

    public boolean value;
    public double animation;

    public BooleanSetting(String name, boolean value) {
        super(name);
        this.value = value;
    }
    public void setBoolValue(boolean value) {
        this.value = value;
    }

}
