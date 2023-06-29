package ua.apraxia.modules.settings.impl;

import ua.apraxia.modules.settings.Setting;

import java.util.ArrayList;
import java.util.Arrays;

public class ModeSetting extends Setting {
    public ArrayList<String> modes = new ArrayList<>();
    public int index;
    public boolean opened;

    public String currentMode;
    public ModeSetting(String name, String defaultMode, String... modes) {
        super(name);
        this.modes.add(defaultMode);
        this.modes.addAll(Arrays.asList(modes));
        currentMode = defaultMode;

    }
    public String getMode() {
        return currentMode;
    }
    public String get() {
        return modes.get(index);
    }
    public boolean is(String mode) {
        return get().equalsIgnoreCase(mode);
    }

    public void setListMode(String selected) {
        this.currentMode = selected;
        this.index = this.modes.indexOf(currentMode);
    }
    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }
}
