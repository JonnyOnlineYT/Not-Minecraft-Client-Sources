package org.moonware.client.feature.impl.hud;

import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class InventoryPreview
        extends Feature {
    public static BooleanSetting glow;
    public static BooleanSetting shadow;
    public static NumberSetting shadowRadius;
    public static NumberSetting scale;

    public InventoryPreview() {
        super("InventoryPreview", "Показывает на экране вашь инвентарь", Type.Hud);
        glow = new BooleanSetting("Glow", true, () -> true);

        shadow = new BooleanSetting("Shadow", false, () -> true);
        shadowRadius = new NumberSetting("Shadow Radius", 50.0f, 25.0f, 100.0f, 5.0f, () -> shadow.getCurrentValue());
        scale = new NumberSetting("Scale", 1.0F, 0.1F, 2.0F, 0.01f, () -> true);
        addSettings(glow, shadow, shadowRadius, scale);
    }
}
