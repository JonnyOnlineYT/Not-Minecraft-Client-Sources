package de.strafe.settings.impl;

import de.strafe.settings.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting<String> {
    public int index;
    public List<String> modes;
    private String selected;

    public ModeSetting(final String name, final String defaultMode, final Supplier<Boolean> dependency, final String... modes) {
        super(name, defaultMode, dependency);
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
        this.selected = this.modes.get(this.index);
    }

    public ModeSetting(final String name, final String defaultMode, final String... modes) {
        this(name, defaultMode, () -> true, modes);
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
        this.selected = this.modes.get(this.index);
    }

    public List<String> getModes() {
        return this.modes;
    }

    public String getMode() {
        return this.modes.get(this.index);
    }

    public boolean is(final String mode) {
        return this.index == this.modes.indexOf(mode);
    }

    public void cycle() {
        if (this.index < this.modes.size() - 1) {
            ++this.index;
            this.selected = this.modes.get(this.index);
        } else {
            this.index = 0;
        }
        this.selected = this.modes.get(0);
    }

    public String getSelected() {
        return this.selected;
    }

    public void setSelected(final String selected) {
        this.selected = selected;
        this.index = this.modes.indexOf(selected);
    }
}
