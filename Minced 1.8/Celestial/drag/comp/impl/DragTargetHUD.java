package Celestial.drag.comp.impl;

import Celestial.drag.comp.DragComp;
import Celestial.Smertnix;
import Celestial.module.impl.Render.TargetHUD;

public class DragTargetHUD extends DragComp {

    public DragTargetHUD() {
        super("TargetHUD", 350, 25, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Smertnix.instance.featureManager.getFeature(TargetHUD.class).isEnabled();
    }
}
