package Celestial.module.impl.Render;


import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.NumberSetting;

public class ItemPhysics extends Module {
    public static NumberSetting physicsSpeed;

    public ItemPhysics() {
        super("ItemPhysics", ModuleCategory.Render);
                physicsSpeed = new NumberSetting("Physics Speed", 0.5F, 0.1F, 5.0F, 0.5F, () -> true);
        addSettings(physicsSpeed);
    }
}
