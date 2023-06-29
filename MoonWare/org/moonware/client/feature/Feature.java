package org.moonware.client.feature;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ClientSounds;
import org.moonware.client.feature.impl.hud.Notifications;
import org.moonware.client.feature.impl.misc.StreamerMode;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.settings.Configurable;
import org.moonware.client.settings.Setting;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.ui.notifications.MNotify;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWUtils;

import java.awt.event.KeyEvent;

public class Feature extends Configurable implements Helper, Comparable<Feature> {

    public static boolean reloadModules;
    public Type type;
    public boolean state;
    public boolean isHovered;
    public double stateGuiAnim;
    public double hoverGuiAnim;
    public double hoverBooleanGuiAnim;
    public boolean visible = true;
    public boolean expanded;
    public ScreenHelper screenHelper = new ScreenHelper(0, 0);
    private String label, suffix;
    private int bind;
    private String desc;
    boolean pushButton;
    public float animYto;
    public static float allowedClickGuiHeight = 300;
    private static double Animate;
    private int rotVal;
    private int trVal;
    private int trYVal;
    public Feature(String label, String desc, Type type) {
        this.label = label;
        this.desc = desc;
        this.type = type;
        bind = 0;
        state = false;
        isHovered = false;
        stateGuiAnim = 0;
        hoverGuiAnim = 0;
        hoverBooleanGuiAnim = 0;
        this.rotVal = 0;
        this.trVal = 0;
        this.trYVal = 0;
    }

    public double getrotVal() {
        this.rotVal =  Interpolator.linear(this.rotVal,  isHovered ? 180 + 90 : 90    , 2F / 5);
        return this.rotVal;
    }
    public double gettrVal() {
        this.trVal = Interpolator.linear(this.trVal, isHovered ? 5:0    , 2F / 5);;
        return this.trVal;
    }
    public double gettrYVal() {
        this.trYVal = Interpolator.linear(this.trYVal, isHovered ? 2:0   , 2F / 5);;
        return this.trYVal;
    }
    public boolean hasMode() {
        return suffix != null;
    }
    public static double getAnimWithFeature(Feature feature) {
        Animate = Interpolator.linear(Animate, feature.getState() ? 10 : 0,2f/520);
        return Animate;
    }
    public double getHoverGuiAnim(int offset) {
        hoverGuiAnim = Interpolator.linear(hoverGuiAnim, isHovered ? getSettings().size() * offset : 0,2f /60);
        boolean tr = true;
        if (tr) {
            float offsetSettings = 0;
            for (Setting setting : getSettings()) {
                if (!setting.isVisible())continue;
                if (setting instanceof BooleanSetting) {
                    offsetSettings += offset;
                }else if (setting instanceof NumberSetting) {
                    offsetSettings += offset;
                }else if (setting instanceof ListSetting) {
                    offsetSettings += offset;
                }else if (setting instanceof ColorSetting) {
                    offsetSettings += offset;
                }
            }
            this.hoverGuiAnim = Interpolator.linear(hoverGuiAnim, isHovered ? offsetSettings : 0,2f /120);;
        }
        return hoverGuiAnim;
    }
    public double getHoverBooleanGuiAnim(int offset) {
        int anim = 0;
        for (int i = 0 ; i < getSettings().size() ; i++) {
            Setting setting = getSettings().get(i);
            if (setting instanceof BooleanSetting) {
                anim += offset;
            }
        }
        hoverBooleanGuiAnim = Interpolator.linear(hoverBooleanGuiAnim, anim,2f /60);
        return hoverBooleanGuiAnim;
    }
    public double getStateGuiAnim() {
        stateGuiAnim = Interpolator.linear(stateGuiAnim, getState() ? 10 : 0, 2f /60);
        return stateGuiAnim;
    }
    public boolean isHovered() {
        return isHovered;
    }
    public void setHovered(boolean hoverState) {
        isHovered = hoverState;
    }
    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }

    public static String getPlayerName() {
        return MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() ? "Protected" : Minecraft.getSession().getUsername();
    }
    public static String getEntityName(Entity entity) {
        return MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && (StreamerMode.ownName.getCurrentValue() && entity.getName().equalsIgnoreCase(Minecraft.getSession().getUsername())) && (StreamerMode.friendNames.get() && MoonWare.friendManager.isFriend(entity.getName())) && (StreamerMode.otherNames.get()) ? "Protected" : entity.getName();
    }
    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", getState());
        object.addProperty("keyIndex", getBind());
        object.addProperty("visible", isVisible());
        JsonObject propertiesObject = new JsonObject();
        for (Setting set : getSettings()) {
            if (getSettings() != null) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.getName(), ((BooleanSetting) set).getBoolValue());
                } else if (set instanceof ListSetting) {
                    propertiesObject.addProperty(set.getName(), ((ListSetting) set).getCurrentMode());
                } else if (set instanceof NumberSetting) {
                    propertiesObject.addProperty(set.getName(), ((NumberSetting) set).getNumberValue());
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
                setState(object.get("state").getAsBoolean());
            }
            if (object.has("visible")) {
                setVisible(object.get("visible").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                setBind(object.get("keyIndex").getAsInt());
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

    public ScreenHelper getScreenHelper() {
        return screenHelper;
    }

    public String getSuffix() {
        return suffix == null ? label : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.suffix = getLabel() + " " + suffix;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isHidden() {
        return !visible;
    }

    public void setHidden(boolean visible) {
        this.visible = !visible;
    }

    public String getLabel() {
        return label;
    }
    public ScreenHelper getTranslate() {
        return screenHelper;
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

    public Type getType() {
        return type;
    }

    public void setCategory(Type type) {
        this.type = type;
    }


    public void onEnable() {
        if (MoonWare.featureManager.getFeatureByClass(ClientSounds.class).getState()) {
            MWUtils.playSound("enable.wav");
        }
        EventManager.register(this);
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.state.getBoolValue()) {
            NotificationManager.publicity(getLabel(), "is enabled", (int) ((int) Notifications.timetooff.getCurrentValue() + Math.random() * 2), NotificationType.INFO);
            MNotify.send("Module Info", getLabel() + " is enabled", (int) Notifications.timetooff.getCurrentValue(), org.moonware.client.ui.notifications.impl.Type.INFO);
        }
    }

    public void onDisable() {
        if (MoonWare.featureManager.getFeatureByClass(ClientSounds.class).getState()) {
            MWUtils.playSound("disable.wav");
        }
        EventManager.unregister(this);
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Notifications")) && Notifications.state.getBoolValue()) {
            NotificationManager.publicity(getLabel(), "is disabled", (int) ((int) Notifications.timetooff.getCurrentValue() + Math.random() * 2), NotificationType.INFO);
            MNotify.send("Module Info", getLabel() + " is disabled", (int) Notifications.timetooff.getCurrentValue(), org.moonware.client.ui.notifications.impl.Type.INFO);

        }
    }

    public void toggle() {
        state = !state;
        MoonWare.configManager.saveConfig("default");
        if (state) {
            onEnable();
        } else {
            onDisable();
        }
    }


    public boolean getState() {
        return state;
    }

    public void toggleSilent() {
        state = !state;
        if (state) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void setState(boolean state) {
        if (state) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.state = state;
    }
    public void keyPressed(KeyEvent e) {
        pushButton= e.getKeyCode() == KeyEvent.VK_DELETE;
    }

    @Override
    public int compareTo(Feature o) {
        return label.compareTo(o.getLabel());
    }


}
