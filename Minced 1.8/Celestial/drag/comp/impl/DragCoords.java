package Celestial.drag.comp.impl;

import Celestial.drag.comp.DragComp;
import Celestial.Smertnix;
import Celestial.module.impl.Render.Hud;

public class DragCoords extends DragComp {

    public DragCoords() {
        super("Coordinates", 350, 25, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Smertnix.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.coords.getCurrentValue();
    }
}
