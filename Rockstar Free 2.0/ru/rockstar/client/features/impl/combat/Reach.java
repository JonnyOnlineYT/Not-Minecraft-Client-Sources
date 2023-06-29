package ru.rockstar.client.features.impl.combat;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Reach extends Feature {

    public static NumberSetting reachValue;

    public Reach() {
        super("Reach", "Увеличивает дистанцию удара",0, Category.COMBAT);
        reachValue = new NumberSetting("Expand", 3.2F, 3, 5, 0.1F, () -> true);
        addSettings(reachValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix("" + MathematicHelper.round(reachValue.getNumberValue(), 1), true);
    }
}