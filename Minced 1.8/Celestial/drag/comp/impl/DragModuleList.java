package Celestial.drag.comp.impl;

import Celestial.drag.comp.DragComp;
import Celestial.Smertnix;
import Celestial.module.impl.Render.ModuleList;

public class DragModuleList extends DragComp {

    public DragModuleList() {
        super("ModuleList", 350, 25, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Smertnix.instance.featureManager.getFeature(ModuleList.class).isEnabled();
    }
}