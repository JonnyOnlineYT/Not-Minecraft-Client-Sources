package org.moonware.client.settings.impl;

import org.moonware.client.settings.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@Deprecated
public class ListSetting extends Setting {

    public final List<String> modes;
    public String currentMode;
    public int index;

    public ListSetting(String name, String currentMode, Supplier<Boolean> visible, String... options) {
        this.name = name;
        modes = Arrays.asList(options);
        index = modes.indexOf(currentMode);
        this.currentMode = modes.get(index);
        setVisible(visible);
        addSettings(this);
    }
    public ListSetting(String name,Supplier<Boolean> visible, String currentMode , String... options) {
        this.name = name;
        modes = Arrays.asList(options);
        index = modes.indexOf(currentMode);
        this.currentMode = modes.get(index);
        setVisible(visible);
        addSettings(this);
    }
    public ListSetting(String name, String currentMode, String... options) {
        this.name = name;
        modes = Arrays.asList(options);
        index = modes.indexOf(currentMode);
        this.currentMode = modes.get(index);
        setVisible(() -> true);
        addSettings(this);
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setListMode(String selected) {
        currentMode = selected;
        index = modes.indexOf(selected);
    }
    public void setCurrentMode(String selected) {
        currentMode = selected;
        index = modes.indexOf(selected);
    }

    public List<String> getModes() {
        return modes;
    }

    public String getOptions() {
        return modes.get(index);
    }
}
