package ua.apraxia.modules.impl.other;

import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.SliderSetting;

public class ItemScroller extends Module {
    public static SliderSetting scrollerDelay = new SliderSetting("Delay", 80, 0, 100, 1);

    public ItemScroller() {
        super("ItemScroller", Categories.Other);
        addSetting(scrollerDelay);

    }
}