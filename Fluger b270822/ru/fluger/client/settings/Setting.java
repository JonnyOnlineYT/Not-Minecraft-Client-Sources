/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.settings;

import java.util.function.Supplier;

public class Setting {
    protected String name;
    protected Supplier<Boolean> visible;

    public boolean isVisible() {
        return true;
    }

    public void setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

