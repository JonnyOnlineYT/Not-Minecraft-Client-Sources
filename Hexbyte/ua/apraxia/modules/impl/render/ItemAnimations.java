package ua.apraxia.modules.impl.render;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.eventapi.events.impl.player.EventViewModel;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;

public class ItemAnimations extends Module {
    public static ModeSetting anim = new ModeSetting("Animation", "Default", "Block", "Swing", "Swipe", "Fap", "Wrap", "Spin", "Жопа", "None");
    public static SliderSetting speed = new SliderSetting("Swing Speed", 8, 1, 25, 1);
    public static SliderSetting swingSpeed = new SliderSetting("Fap Speed", 20, 10, 40, 1);
    public static SliderSetting wrapSpeed = new SliderSetting("Wrap Speed", 10, 2, 20, 1);
    public static SliderSetting spinSpeed = new SliderSetting("Spin Speed", 5, 2, 10, 1);
    public ItemAnimations() {
       super("ItemAnimations", Categories.Render);
           addSetting(anim, speed, swingSpeed, wrapSpeed, spinSpeed);
        }
    @EventTarget
    public void onUpdate(EventUpdate event) {
    }

    @EventTarget
    public void onSidePerson(EventViewModel event) {
    }

}