package Celestial.drag.comp.impl;

import Celestial.drag.comp.DragComp;
import Celestial.Smertnix;
import Celestial.module.impl.Movement.Timer;

public class DragTimer extends DragComp {

    public DragTimer() {
        super("Timer", 160, 400, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Smertnix.instance.featureManager.getFeature(Timer.class).isEnabled();
    }
}
