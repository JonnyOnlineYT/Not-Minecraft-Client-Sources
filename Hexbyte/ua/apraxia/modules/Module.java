package ua.apraxia.modules;

import com.google.gson.JsonObject;
import ua.apraxia.eventapi.EventManager;
import ua.apraxia.modules.settings.SetManager;
import ua.apraxia.modules.settings.Setting;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.notification.NotificationMode;
import ua.apraxia.notification.NotificationRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;

import java.util.Arrays;
import java.util.List;

public class Module extends SetManager {

    private String moduleName;
    private Categories moduleCategory;
    public int moduleKey;
    public float animYto;
    private String suffix;
    public int swipeanim;

    private boolean moduleState;
    public static Minecraft mc = Minecraft.getMinecraft();
    public boolean opened = true;


   public Module(String name, Categories category) {
        this.moduleCategory = category;
        this.moduleName = name;
        moduleKey = 0;
    }




    public String getSuffix() {
        return suffix == null ? moduleName : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.suffix = moduleName + " " + suffix;
    }


    public void onEnable() {
        EventManager.register(this);
        NotificationRenderer.queue(getModuleName(), TextFormatting.GREEN + "включен(а)", 2, NotificationMode.SUCCESS);
    }

    public void onDisable() {
        EventManager.unregister(this);
        NotificationRenderer.queue(getModuleName(), TextFormatting.RED + "выключен(а)", 2, NotificationMode.SUCCESS);
    }


    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("state", isModuleState());
        object.addProperty("keyIndex", getBind());
        JsonObject propertiesObject = new JsonObject();
        for (Setting set : this.getSetting()) {
            if ( this.getSetting() != null) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.name, ((BooleanSetting) set).value);
                } else if (set instanceof ModeSetting) {
                    propertiesObject.addProperty(set.name, ((ModeSetting) set).get());
                } else if (set instanceof SliderSetting) {
                    propertiesObject.addProperty(set.name, ((SliderSetting) set).value);
                } else if (set instanceof ColorSetting) {
                    propertiesObject.addProperty(set.name, ((ColorSetting) set).color);
                }
            }
            object.add("Settings", propertiesObject);
        }
        return object;
    }

    public void load(JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setState(object.get("state").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                this.setBind(object.get("keyIndex").getAsInt());
            }
            for (Setting set : getSetting()) {
                JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null)
                    continue;
                if (propertiesObject == null)
                    continue;
                if (!propertiesObject.has(set.name))
                    continue;
                if (set instanceof BooleanSetting) {
                    ((BooleanSetting) set).setBoolValue(propertiesObject.get(set.name).getAsBoolean());
                } else if (set instanceof ModeSetting) {
                    ((ModeSetting) set).setListMode(propertiesObject.get(set.name).getAsString());
                } else if (set instanceof SliderSetting) {
                    ((SliderSetting) set).setValueNumber(propertiesObject.get(set.name).getAsFloat());
                } else if (set instanceof ColorSetting) {
                    ((ColorSetting) set).setColorValue(propertiesObject.get(set.name).getAsInt());
                }
            }
        }
    }
    public Categories getModuleCategory() {
        return moduleCategory;
    }

    public boolean isModuleState() {
        return moduleState;
    }

    public String getModuleName() {
        return moduleName;
    }

    public int getBind() {
        return moduleKey;
    }

    public void setBind(int bind) {
        this.moduleKey = bind;
    }

    public void toggle() {
        moduleState = !moduleState;
        if(moduleState)
            onEnable();
         else
            onDisable();
    }
    public void setState(boolean moduleState) {
        if (moduleState) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.moduleState = moduleState;
    }

    public List<Setting> getSetting(){
        return settingList;
    }

    public void addSetting(Setting... setting){
        settingList.addAll(Arrays.asList(setting));
    }

}
