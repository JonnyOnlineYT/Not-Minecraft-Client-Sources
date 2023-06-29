package ua.apraxia.draggable.component.impl;

import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.DraggableComponent;

public class TargetHUD extends DraggableComponent {
    public TargetHUD() {
        super("TargetHUD", 0, 1, 4, 1);
    }

    @Override
    public boolean allowDraw() {
        return Hexbyte.getInstance().moduleManagment.getModule(ua.apraxia.modules.impl.display.TargetHUD.class).isModuleState();
    }
}
