package de.strafe.settings;

import de.strafe.modules.Module;
import de.strafe.settings.impl.BooleanSetting;
import de.strafe.settings.impl.ModeSetting;
import de.strafe.settings.impl.NumberSetting;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Setting<T> {

    public final Supplier<Boolean> dependency;
    public final List<ValueChangeListener<T>> valueChangeListeners;
    public String name;
    public String parentValue;
    public String parentMode;
    public boolean hidden;
    public T value;
    public Setting parent;
    public String description;



    public Setting getSetting() {
        return this;
    }

    public Setting(final String label, final T value, final Supplier<Boolean> dependency) {
        this.valueChangeListeners = new ArrayList<ValueChangeListener<T>>();
        this.name = label;
        this.value = value;
        this.dependency = dependency;
    }

    Type type;

    public boolean isType(Type t) {
        return (this.type == t);
    }

    public Setting parent(Setting set) {
        this.parent = set;
        return this;
    }

    public Setting mode(String mode) {
        this.parentMode = mode;
        return this;
    }

    public static List<Setting> getSettingsOfType(Module mod, Type t) {
        if (mod.settings.isEmpty())
            return null;
        List<Setting> arr = new ArrayList<>();
        for (Setting set : mod.settings) {
            switch (t) {
                case BOOLEAN:
                    if (set instanceof BooleanSetting)
                        arr.add(set);
                case MODE:
                    if (set instanceof ModeSetting)
                        arr.add(set);
                case VALUE:
                    if (set instanceof NumberSetting)
                        arr.add(set);
            }
        }
        return arr;
    }

    public enum Type {
        BOOLEAN, MODE, VALUE, TEXT, CATEGORY, COLOR;
    }


    public boolean isAvailable() {
        return this.dependency.get();
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }

    public String getParentMode() {
        return this.parentMode;
    }
}
