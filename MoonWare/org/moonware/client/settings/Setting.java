package org.moonware.client.settings;

import org.moonware.client.feature.Feature;

import java.util.function.Supplier;

public class Setting extends Configurable {

    protected String name;
    protected Supplier<Boolean> visible;
    private Feature parent;

    public boolean isVisible() {
        return visible.get();
    }


    public Feature getParentMod(){
        return parent;
    }

    public void setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
    }

    public String getName() {
        return name;
    }
    public String name() {
        return name;
    }
}
