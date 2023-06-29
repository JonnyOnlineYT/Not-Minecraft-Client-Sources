package ru.rockstar.client.features.impl.player;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class ItemScroller extends Feature {

    public static NumberSetting scrollerDelay;

    public ItemScroller() {
        super("ItemScroller", "Позволяет быстро лутать сундуки при нажатии на шифт и ЛКМ", 0, Category.MISC);

        scrollerDelay = new NumberSetting("Scroller Delay", 0, 0, 1000, 50, () -> true);
        addSettings(scrollerDelay);

    }
}
