package de.strafe.settings.impl;


import de.strafe.settings.Setting;

import java.util.function.Supplier;

public class StringSetting extends Setting {
    public String text;

    public StringSetting(final String name, final String defaultText, final Supplier<Boolean> dependency) {
        super(name, defaultText, dependency);
        this.name = name;
        this.text = defaultText;
    }


    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
