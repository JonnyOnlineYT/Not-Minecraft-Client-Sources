package Celestial.drag.comp.impl;

import Celestial.drag.comp.DragComp;
import Celestial.Smertnix;
import Celestial.module.impl.Render.Hud;

public class DragSessionInfo extends DragComp {

    public DragSessionInfo() {
        super("Session Info", 0, 10, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Smertnix.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.sessionInfo.getCurrentValue();
    }
}
