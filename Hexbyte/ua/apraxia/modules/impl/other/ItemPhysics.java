package ua.apraxia.modules.impl.other;


import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.SliderSetting;

public class ItemPhysics extends Module {
    public static SliderSetting physicsSpeed = new SliderSetting("Speed", 0.5F, 0.1F, 5.0F, 0.5F);

    public ItemPhysics() {
        super("ItemPhysics", Categories.Render);
        addSetting(physicsSpeed);
    }
}
