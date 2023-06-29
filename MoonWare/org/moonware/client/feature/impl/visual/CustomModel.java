package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;

public class CustomModel
        extends Feature {
    public static ListSetting modelMode = new ListSetting("Model Mode", "Amogus",() -> true, "Amogus", "Jeff Killer", "Crab", "Demon", "Red Panda", "Chinchilla", "Freddy Bear", "CupHead", "Sonic", "Crazy Rabbit", "None");
    public static BooleanSetting friendHighlight;
    public static ListSetting bodyColor;
    public static ListSetting eyeColor;
    public static ListSetting legsColor;
    public static ColorSetting bodyCustomColor;
    public static ColorSetting eyeCustomColor;
    public static ColorSetting legsCustomColor;
    public static BooleanSetting googlyEyes;
    public static NumberSetting googlyEyesSize;
    public static BooleanSetting onlyMe;
    public static BooleanSetting friends;
    public static ColorSetting demonColor;
    public static BooleanSetting wings;
    public static ColorSetting wingsColor;

    public CustomModel() {
        super("CustomModel", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0440\u0435\u0434\u0430\u043a\u0442\u0438\u0440\u043e\u0432\u0430\u0442\u044c \u043c\u043e\u0434\u0435\u043b\u044c \u0438\u0433\u0440\u043e\u043a\u043e\u0432", Type.Visuals);
        demonColor = new ColorSetting("Demon Color", Color.GRAY.getRGB(), () -> modelMode.currentMode.equals("Demon"));
        wings = new BooleanSetting("Wings", true, () -> modelMode.currentMode.equals("None"));
        wingsColor = new ColorSetting("Wings Color", Color.GRAY.getRGB(), () -> modelMode.currentMode.equals("None") && wings.getCurrentValue());
        friendHighlight = new BooleanSetting("Friend Highlight", true, () -> modelMode.currentMode.equals("Amogus"));
        bodyColor = new ListSetting("Amogus Body Color Mode", "Custom", () -> modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        eyeColor = new ListSetting("Amogus Eye Color Mode", "Custom", () -> modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        legsColor = new ListSetting("Amogus Legs Color Mode", "Custom", () -> modelMode.currentMode.equals("Amogus"), "Custom", "Client", "Rainbow", "Astolfo");
        bodyCustomColor = new ColorSetting("Amogus Body Color", Color.RED.getRGB(), () -> modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom"));
        eyeCustomColor = new ColorSetting("Amogus Eye Color", Color.CYAN.getRGB(), () -> modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom"));
        legsCustomColor = new ColorSetting("Amogus Legs Color", Color.RED.getRGB(), () -> modelMode.currentMode.equals("Amogus") && bodyColor.currentMode.equals("Custom"));
        googlyEyes = new BooleanSetting("Googly Eyes", false, () -> false);
        googlyEyesSize = new NumberSetting("Google Eyes Size", 0.75f, 0.7f, 1.5f, 0.01f, () -> false);
        onlyMe = new BooleanSetting("Only Me", true, () -> true);
        friends = new BooleanSetting("Friends", false, () -> onlyMe.getBoolValue());
        addSettings(modelMode, demonColor, wings, wingsColor, friendHighlight, bodyColor, bodyCustomColor, eyeColor, eyeCustomColor, legsColor, legsCustomColor, googlyEyes, googlyEyesSize, onlyMe, friends);
    }

    public static boolean isEnabled() {
        return MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState();
    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix(modelMode.getCurrentMode());
    }
    public static boolean isValid(Entity entity) {
        return (!onlyMe.isToggle() || entity == Minecraft.player
                || MoonWare.friendManager.getFriends().contains(entity.getName()) && friends.isToggle());
    }
}
