package ua.apraxia.draggable.component.impl;

import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.DraggableComponent;
import ua.apraxia.modules.impl.display.HUD;

import static ua.apraxia.modules.impl.display.HUD.armor;

public class Armor extends DraggableComponent {
    public Armor() {
        super("Armor", 0, 1, 4, 1);
    }

    @Override
    public boolean allowDraw() {
        return Hexbyte.getInstance().moduleManagment.getModule(HUD.class).isModuleState() && armor.value;
    }
}
