package de.strafe.modules;

import com.eventapi.EventManager;
import de.strafe.settings.Setting;
import de.strafe.utils.IMinecraft;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Module implements IMinecraft {

    public List<Setting> settings;

    private final String name;
    private final Category category;
    public int key;
    public boolean toggled;

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        this.category = category;
        this.toggled = false;
        this.settings = new ArrayList<Setting>();
    }

    public final void toggle() {
        toggled = !toggled;
        if (toggled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void setKey(int key) {
        this.key = key;
    }
    public void onEnable() {
        EventManager.register(this);
    }
    public void onDisable() {
        EventManager.unregister(this);
    }

    public void addSettings(final Setting... settings) {
        for (final Setting setting : settings) {
            this.addSetting(setting);
        }
    }

    public final Category getCategory() {
        return category;
    }



    public void addSetting(final Setting setting) {
        this.getSettings().add(setting);
    }

    public List<Setting> getSettings() {
        return this.settings;
    }

    public void setSettings(final List<Setting> settings) {
        this.settings = settings;
    }


}