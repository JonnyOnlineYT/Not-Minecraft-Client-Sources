package org.moonware.client.feature.impl.misc;

import org.moonware.client.utils.MWUtils;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.NumberSetting;

public class ItemScroller extends Feature {

    public static NumberSetting scrollerDelay;

    public ItemScroller() {
        super("ItemScroller", "Позволяет быстро лутать сундуки при нажатии на шифт и ЛКМ", Type.Other);
        scrollerDelay = new NumberSetting("Scroller Delay", 0, 0, 1000, 50, () -> true);
        addSettings(scrollerDelay);
    }

    @Override
    public void onEnable() {
        for (int i = 0; i < 3; i++) {
            MWUtils.sendChat("Зажмите шифт и левую кнопку мыши, что бы быстро лутать сундуки!");
        }
        super.onEnable();
    }
}
