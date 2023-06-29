package ru.rockstar.client.features;


import com.google.gson.JsonObject;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventManager;
import ru.rockstar.api.utils.notifications.Notification;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.api.utils.other.SoundHelper;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.Translate;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.impl.display.Notifications;
import ru.rockstar.client.features.impl.misc.ModuleSoundAlert;
import ru.rockstar.client.ui.clickgui.ScreenHelper;
import ru.rockstar.client.ui.settings.Configurable;
import ru.rockstar.client.ui.settings.Setting;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Feature extends Configurable {
    protected static Minecraft mc = Minecraft.getMinecraft();
    public static TimerHelper timerHelper = new TimerHelper();
    private final Translate translate = new Translate(0.0F, 0.0F);
    protected String label;
    protected String desc;
    private String moduleName;
    private String suffix;
    public double stateGuiAnim;
    public double hoverGuiAnim;
    public double hoverBooleanGuiAnim;
    private int key;
    private Category category;
    protected boolean toggled;
    private final AnimationHelper animation;
    public double slidex = 0;
    public double slidey = 0;
    public boolean visible = true;
    public ScreenHelper screenHelper = new ScreenHelper(0, 0);
    public float animYto;
    public boolean isHovered;
    private static double animate;
    
    public Feature(String name, String desc, int key, Category category) {
        this.label = name;
        this.desc = desc;
        this.key = key;
        this.category = category;
        toggled = false;
        animation = new AnimationHelper(150, this.isToggled());
        setup();
    }
    
    public static double getAnimWithFeature(Feature feature) {
        animate = feature.isToggled() ? 10.0 : 0.0;
        return animate;
    }

    public double getHoverGuiAnim(int offset) {
        this.hoverGuiAnim = this.isHovered ? (double)(this.getSettings().size() * offset) : 0.0;
        return this.hoverGuiAnim;
    }

    public double getHoverBooleanGuiAnim(int offset) {
        int anim = 0;
        for (int i = 0; i < this.getSettings().size(); ++i) {
            Setting setting = this.getSettings().get(i);
            if (!(setting instanceof BooleanSetting)) continue;
            anim += offset;
        }
        this.hoverBooleanGuiAnim = (double)anim;
        return this.hoverBooleanGuiAnim;
    }

    public double getStateGuiAnim() {
        this.stateGuiAnim = this.isToggled() ? 10.0 : 0.0;
        return this.stateGuiAnim;
    }

    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", isToggled());
        object.addProperty("keyIndex", getKey());
        object.addProperty("visible", isVisible());
        JsonObject propertiesObject = new JsonObject();
        for (Setting set : this.getSettings()) {
            if (this.getSettings() != null) {
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
                this.setEnabled(object.get("state").getAsBoolean());
            }
            if (object.has("visible")) {
                this.setVisible(object.get("visible").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                this.setKey(object.get("keyIndex").getAsInt());
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

    public void onEnable() {
    	String mode = ModuleSoundAlert.soundMode.getOptions();
        if (Main.instance.featureDirector.getFeatureByClass(ModuleSoundAlert.class).isToggled()) {
        	if (mode.equalsIgnoreCase("Minecraft")) {
        		mc.player.playSound(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, 1.8F, 0.71999997F);
        	}
        	if (mode.equalsIgnoreCase("Rockstar")) {
        		SoundHelper.playSound("enable.wav");
        	}
        }
        EventManager.register(this);
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Rockstar")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getBoolValue()) {
            NotificationPublisher.queue("Модуль " + TextFormatting.BOLD +  getLabel(), "Был успешно" + TextFormatting.GREEN + " включен", NotificationType.INFO);
        } else if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Rockstar")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getBoolValue()) {
            Main.msg(TextFormatting.GRAY + "[Rockstar] " + TextFormatting.WHITE + getLabel() + " was" + TextFormatting.GREEN + " enabled!", false);
        }
    }

    public void onDisable() {
    	String mode = ModuleSoundAlert.soundMode.getOptions();
        if (Main.instance.featureDirector.getFeatureByClass(ModuleSoundAlert.class).isToggled()) {
        	if (mode.equalsIgnoreCase("Minecraft")) {
        		mc.player.playSound(SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, 1.8F, 0.71999997F);
        	}
        	if (mode.equalsIgnoreCase("Rockstar")) {
        		 SoundHelper.playSound("disable.wav");
        	}
        }
        EventManager.unregister(this);
        if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Rockstar")) && (Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") || Notifications.notifMode.currentMode.equalsIgnoreCase("System")) && Notifications.state.getBoolValue()) {
            NotificationPublisher.queue("Модуль " + TextFormatting.BOLD + getLabel(), "Был успешно" + TextFormatting.RED + " выключен", NotificationType.INFO);
        } else if (!(getLabel().contains("ClickGui") || getLabel().contains("Client Font") || getLabel().contains("Rockstar")) && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getBoolValue()) {
            Main.msg( TextFormatting.GRAY + "[Rockstar] " + TextFormatting.WHITE + getLabel() + " успешно" + TextFormatting.RED +" выключен!" , false);
        }
    }

    public ScreenHelper getScreenHelper() {
        return this.screenHelper;
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


    public void setEnabled(boolean enabled) {
        if (enabled) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.toggled = enabled;
    }

    public void onToggle() {
    }

    public void toggle() {
        toggled = !toggled;
        onToggle();
        if (toggled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public String getLabel() {
        return label;
    }
    

    public void setLabel(String name) {
        this.label = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isToggled() {
        return toggled;
    }

    public String getSuffix() {
        return suffix == null ? label : suffix;
    }

    public void setSuffix(String suffix, boolean a) {
    	if (a) {
    		this.suffix = suffix;
            this.suffix = getLabel();
    	}
    }
    
    public void setHovered(boolean hoverState) {
        this.isHovered = hoverState;
    }

    public String getModuleName() {
        return moduleName == null ? label : moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setup() {
    }

    public static double deltaTime() {
        return Minecraft.getDebugFPS() > 0 ? (1.0000 / Minecraft.getDebugFPS()) : 1;
    }
    

    public AnimationHelper getAnimation() {
        return animation;
    }

    public Translate getTranslate() {
        return translate;
    }

	public boolean isHovered() {
		return this.isHovered;
	}

	
}
