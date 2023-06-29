package org.moonware.client.feature.impl.combat;

import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.settings.impl.NumberSetting;

public class Reach extends Feature {

    public static NumberSetting reachValue;

    public Reach() {
        super("Reach", "Увеличивает дистанцию удара", Type.Combat);
        reachValue = new NumberSetting("Expand", 3.2F, 3, 5, 0.1F, () -> true);
        addSettings(reachValue);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix("" + MathematicHelper.round(reachValue.getNumberValue(), 1));
    }
}