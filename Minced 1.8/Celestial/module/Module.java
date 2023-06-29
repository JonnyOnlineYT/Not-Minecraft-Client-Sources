package Celestial.module;

import Celestial.event.EventManager;
import Celestial.utils.Helper;
import Celestial.utils.other.ChatUtils;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.ui.celestun4ik.screenutil;
import Celestial.ui.notif.NotifModern;
import Celestial.ui.notif.NotifRender;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ColorSetting;
import Celestial.ui.settings.impl.ListSetting;
import Celestial.ui.settings.impl.NumberSetting;
import Celestial.module.impl.Render.Notifications;
import Celestial.ui.settings.Configurable;
import Celestial.ui.settings.Setting;
import net.minecraft.util.text.TextFormatting;


public class Module extends Configurable implements Helper {
    public screenutil screenHelper;
    public ModuleCategory category;
    private boolean enabled;
    public boolean state;
    public float animYto;
    private String label, suffix;
    private int bind;
    private String desc;

    public Module(String label, String desc, ModuleCategory category) {
        this.label = label;
        this.desc = desc;
        this.category = category;
        this.bind = 0;
        enabled = false;
    }

    public Module(String label, ModuleCategory category) {
        this.screenHelper = new screenutil(0.0f, 0.0f);
        this.label = label;
        this.category = category;
        this.bind = 0;
        enabled = false;
    }

    public String getSuffix() {
        return suffix == null ? label : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.suffix = getLabel() + " " + suffix;
    }
    public screenutil getTranslate()
    {
        return this.screenHelper;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setCategory(ModuleCategory category) {
        this.category = category;
    }

    public void onEnable() {
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getCurrentValue()) {
            NotifRender.queue("Module Debug", getLabel() + " " + "was " + ChatFormatting.GREEN +"Enabled!", 1, NotifModern.INFO);
        } else if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getCurrentValue()) {
            ChatUtils.addChatMessage(TextFormatting.GRAY + "[Notifications] " + getLabel() + " was" + ChatFormatting.GREEN+" enabled!");
        }
        EventManager.register(this);
    }

    public void onDisable() {
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getCurrentValue()) {
            NotifRender.queue("Module Debug", getLabel() + " " + "was " + ChatFormatting.RED + "Disabled!", 1, NotifModern.INFO);
        } else if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getCurrentValue()) {
            ChatUtils.addChatMessage(TextFormatting.GRAY + "[Notifications] " + getLabel() + " was" + " disabled!");
        }
        EventManager.unregister(this);
    }

    public void toggle() {
        this.enabled = !this.enabled;

        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public screenutil getScreenHelper() {
        return this.screenHelper;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.enabled = enabled;
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", isEnabled());
        object.addProperty("keyIndex", getBind());
        JsonObject propertiesObject = new JsonObject();
        for (Setting set : this.getSettings()) {
            if (this.getSettings() != null) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.getName(), ((BooleanSetting) set).getCurrentValue());
                } else if (set instanceof ListSetting) {
                    propertiesObject.addProperty(set.getName(), ((ListSetting) set).getCurrentMode());
                } else if (set instanceof NumberSetting) {
                    propertiesObject.addProperty(set.getName(), ((NumberSetting) set).getCurrentValue());
                } else if (set instanceof ColorSetting) {
                    propertiesObject.addProperty(set.getName(), ((ColorSetting) set).getColorValue());
                }
            }
            object.add("Settings", propertiesObject);
        }
        return object;
    }

    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setEnabled(object.get("state").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                this.setBind(object.get("keyIndex").getAsInt());
            }
            for (Setting set : getSettings()) {
                JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null)
                    continue;
                if (propertiesObject == null)
                    continue;
                if (!propertiesObject.has(set.getName()))
                    continue;
                if (set instanceof BooleanSetting) {
                    ((BooleanSetting) set).setBoolValue(propertiesObject.get(set.getName()).getAsBoolean());
                } else if (set instanceof ListSetting) {
                    ((ListSetting) set).setListMode(propertiesObject.get(set.getName()).getAsString());
                } else if (set instanceof NumberSetting) {
                    ((NumberSetting) set).setValueNumber(propertiesObject.get(set.getName()).getAsFloat());
                } else if (set instanceof ColorSetting) {
                    ((ColorSetting) set).setColorValue(propertiesObject.get(set.getName()).getAsInt());
                }
            }
        }
    }

    public boolean getState() {
        return state;
    }
}
