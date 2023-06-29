package ua.apraxia.modules.impl.legit;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.SliderSetting;

public class HitBox extends Module {

    public static SliderSetting hitboxSize = new SliderSetting("HitBox Size", 0.25f, 0.1f, 2.f, 0.1f);

    public HitBox() {
        super("HitBox", Categories.Legit);
        addSetting(hitboxSize);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        setSuffix("" + hitboxSize.value);
    }
}
