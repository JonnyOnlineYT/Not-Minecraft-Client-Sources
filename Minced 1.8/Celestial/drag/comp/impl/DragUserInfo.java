package Celestial.drag.comp.impl;

import Celestial.Smertnix;
import Celestial.drag.comp.DragComp;
import Celestial.module.impl.Render.Hud;

public class DragUserInfo extends DragComp {

    public DragUserInfo() {
        super("User Info", 500, 25, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Smertnix.instance.featureManager.getFeature(Hud.class).isEnabled();
    }
}
