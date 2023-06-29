package ua.apraxia.draggable.component.impl;

import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.DraggableComponent;
import ua.apraxia.modules.impl.display.PotionHUD;

public class Potions extends DraggableComponent {
    public Potions() {
        super("Potions", 0, 1, 4, 1);
    }

    @Override
    public boolean allowDraw() {
        return Hexbyte.getInstance().moduleManagment.getModule(PotionHUD.class).isModuleState();
    }
}
