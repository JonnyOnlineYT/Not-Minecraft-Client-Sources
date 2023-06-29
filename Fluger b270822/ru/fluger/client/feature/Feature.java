package ru.fluger.client.feature;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import ru.fluger.client.Fluger;
import ru.fluger.client.event.EventManager;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.impl.misc.ModuleSoundAlert;
import ru.fluger.client.helpers.misc.SoundHelper;
import ru.fluger.client.helpers.misc.TimerHelper;
import ru.fluger.client.helpers.render.ScreenHelper;
import ru.fluger.client.settings.Configurable;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.ui.notifications.PreviewType;

public class Feature
extends Configurable {
    protected static Minecraft mc;
    public Type type;
    public boolean state;
    public boolean visible = true;
    private String label;
    private String suffix;
    public static TimerHelper timerHelper;
    private int bind;
    public float animYto;
    private String desc;
    public ScreenHelper screenHelper;

    public Feature(String label, String desc, Type type) {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
        }
        this.screenHelper = new ScreenHelper(0.0f, 0.0f);
        this.label = label;
        this.desc = desc;
        this.type = type;
        this.bind = 0;
        this.state = false;
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", Boolean.valueOf(this.getState()));
        object.addProperty("keyIndex", (Number)this.getBind());
        object.addProperty("visible", Boolean.valueOf(this.isVisible()));
        JsonObject propertiesObject = new JsonObject();
        if (this.getOptions() != null) {
            for (Setting set : this.getOptions()) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.getName(), Boolean.valueOf(((BooleanSetting)set).getCurrentValue()));
                    continue;
                }
                if (set instanceof ListSetting) {
                    propertiesObject.addProperty(set.getName(), ((ListSetting)set).getCurrentMode());
                    continue;
                }
                if (set instanceof NumberSetting) {
                    propertiesObject.addProperty(set.getName(), (Number)Float.valueOf(((NumberSetting)set).getCurrentValue()));
                    continue;
                }
                if (!(set instanceof ColorSetting)) continue;
                propertiesObject.addProperty(set.getName(), (Number)((ColorSetting)set).getColor());
            }
            object.add("Settings", (JsonElement)propertiesObject);
        }
        return object;
    }

    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setState(object.get("state").getAsBoolean());
            }
            if (object.has("visible")) {
                this.setVisible(object.get("visible").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                this.setBind(object.get("keyIndex").getAsInt());
            }
            for (Setting set : this.getOptions()) {
                JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null || propertiesObject == null || !propertiesObject.has(set.getName())) continue;
                if (set instanceof BooleanSetting) {
                    ((BooleanSetting)set).setValue(propertiesObject.get(set.getName()).getAsBoolean());
                    continue;
                }
                if (set instanceof ListSetting) {
                    ((ListSetting)set).setCurrentMode(propertiesObject.get(set.getName()).getAsString());
                    continue;
                }
                if (set instanceof NumberSetting) {
                    ((NumberSetting)set).setCurrentValue(propertiesObject.get(set.getName()).getAsFloat());
                    continue;
                }
                if (!(set instanceof ColorSetting)) continue;
                ((ColorSetting)set).setColor(propertiesObject.get(set.getName()).getAsInt());
            }
        }
    }

    public ScreenHelper getTranslate() {
        return this.screenHelper;
    }

    public String getSuffix() {
        return this.suffix == null ? this.label : this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.suffix = this.getLabel() + (Object)((Object)TextFormatting.GRAY) + " " + suffix;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHidden() {
        return !this.visible;
    }

    public void setHidden(boolean visible) {
        this.visible = !visible;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getBind() {
        return this.bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Type getType() {
        return this.type;
    }

    public void setCategory(Type type) {
        this.type = type;
    }

    public void onEnable() {
        EventManager.register(this);
        if (!this.getLabel().contains("ClickGui") && !this.getLabel().contains("Client Font") && !this.getLabel().contains("Notifications")) {
        }
    }

    public void onDisable() {
        EventManager.unregister(this);
        if (!this.getLabel().contains("ClickGui") && !this.getLabel().contains("Client Font") && !this.getLabel().contains("Notifications")) {
        }
    }

    public void toggle() {
        this.state = !this.state;
        boolean bl = this.state;
        if (this.state) {
            this.onEnable();
            Fluger.instance.notifications.add(PreviewType.INFORMATION, "\u0424\u0443\u043d\u043a\u0446\u0438\u044f \u00a7a" + this.label + "\u00a7f \u0432\u043a\u043b\u044e\u0447\u0435\u043d\u0430!", 5, false);
        } else {
            this.onDisable();
            Fluger.instance.notifications.add(PreviewType.INFORMATION, "\u0424\u0443\u043d\u043a\u0446\u0438\u044f \u00a7c" + this.label + "\u00a7f \u0432\u044b\u043a\u043b\u044e\u0447\u0435\u043d\u0430!", 5, false);
        }
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        if (state) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.state = state;
    }

    static {
        timerHelper = new TimerHelper();
    }
}
