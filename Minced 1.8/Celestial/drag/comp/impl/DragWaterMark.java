package Celestial.drag.comp.impl;

import Celestial.drag.comp.DragComp;
import Celestial.Smertnix;
import Celestial.module.impl.Render.Hud;

public class DragWaterMark extends DragComp {
    public DragWaterMark() {
        super("WaterMark", 0, 1, 4, 1);
    }

    @Override
    public boolean allowDraw() {
        return Smertnix.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.waterMark.getCurrentValue();
    }
}
