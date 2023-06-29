package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventUpdate;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.BooleanSetting;
import Celestial.ui.settings.impl.ColorSetting;
import Celestial.ui.settings.impl.ListSetting;

import java.awt.*;

public class CustomModel extends Module {
    public static ListSetting modelMode = new ListSetting("Model Mode", "Rabbit", () -> true, "Rabbit");
    public static BooleanSetting friendHighlight;
    public static ListSetting bodyColor;
    public static ListSetting eyeColor;
    public static ListSetting legsColor;
    public static ColorSetting bodyCustomColor;
    public static ColorSetting eyeCustomColor;
    public static ColorSetting legsCustomColor;
    public static BooleanSetting customColor = new BooleanSetting("Custom Color", false, () -> modelMode.currentMode.equals("Demon"));
    public static ColorSetting demonColor = new ColorSetting("Demon Color", new Color(255, 255, 255).getRGB(), () -> customColor.getCurrentValue() && modelMode.currentMode.equals("Demon"));

    public static BooleanSetting onlyMe;
    public static BooleanSetting friends;

    public CustomModel() {
        super("CustomModel", "Изменяет скин игрока", ModuleCategory.Render);
        friendHighlight = new BooleanSetting("Friend Highlight", true, () -> modelMode.currentMode.equals("Amogus"));
        bodyColor = new ListSetting("Amogus Body Color Mode", "Custom", () -> modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        eyeColor = new ListSetting("Amogus Eye Color Mode", "Custom", () -> modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        legsColor = new ListSetting("Amogus Legs Color Mode", "Custom", () -> modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        bodyCustomColor = new ColorSetting("Amogus Body Color", Color.RED.getRGB(), () -> modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom"));
        eyeCustomColor = new ColorSetting("Amogus Eye Color", Color.CYAN.getRGB(), () -> modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom"));
        legsCustomColor = new ColorSetting("Amogus Legs Color", Color.RED.getRGB(), () -> modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom"));
        onlyMe = new BooleanSetting("Only Me", true);
        friends = new BooleanSetting("Friends", false, () -> onlyMe.getCurrentValue());
        addSettings(modelMode, onlyMe, friends);
    }


    @EventTarget
    public void onUpdate(EventUpdate event) {
    }
}
