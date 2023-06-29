package ua.apraxia.draggable.component.impl;

import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.DraggableComponent;
import ua.apraxia.modules.impl.display.HUD;

import static ua.apraxia.modules.impl.display.HUD.waterMark;

public class WaterMark extends DraggableComponent {
    public WaterMark() {
        super("WaterMark", 0, 1, 4, 1);
    }

    @Override
    public boolean allowDraw() {
        return Hexbyte.getInstance().moduleManagment.getModule(HUD.class).isModuleState() && waterMark.value;
    }
}
