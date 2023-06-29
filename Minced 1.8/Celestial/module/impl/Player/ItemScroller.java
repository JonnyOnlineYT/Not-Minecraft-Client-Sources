package Celestial.module.impl.Player;

import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.NumberSetting;

public class ItemScroller extends Module {

    public static NumberSetting scrollerDelay;

    public ItemScroller() {
        super("ItemScroller", "Скорость скролла предметов", ModuleCategory.Player);

        scrollerDelay = new NumberSetting("Scroller Delay", 80, 0, 100, 1, () -> true);
        addSettings(scrollerDelay);

    }
}