package ua.apraxia.draggable.component.impl;

import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.DraggableComponent;

public class FeatureList extends DraggableComponent {

    public FeatureList() {
        super("FeatureList", 350, 25, 1, 1);
    }

    @Override
    public boolean allowDraw() {
        return Hexbyte.getInstance().moduleManagment.getModule(ua.apraxia.modules.impl.display.ModuleList.class).isModuleState();
    }
}