package Celestial.drag.comp.impl;

import Celestial.drag.comp.DragComp;
import Celestial.Smertnix;
import Celestial.module.impl.Render.Hud;

public class DragPotion extends DragComp {

    public DragPotion() {
        super("Potion Status", 50, 100, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Smertnix.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.potions.getCurrentValue();
    }
}
