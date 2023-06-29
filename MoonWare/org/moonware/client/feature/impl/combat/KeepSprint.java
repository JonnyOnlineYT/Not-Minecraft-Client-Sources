package org.moonware.client.feature.impl.combat;

import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class KeepSprint extends Feature {

    public static NumberSetting speed;
    public static BooleanSetting setSprinting;

    public KeepSprint() {
        super("KeepSprint", "Повзоляет редактировать скорость игрока при ударе", Type.Combat);
        speed = new NumberSetting("Keep Speed", 1, 0.5F, 2, 0.01F, () -> true);
        setSprinting = new BooleanSetting("Set Sprinting", true, () -> true);
        addSettings(setSprinting, speed);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        setSuffix("" + MathematicHelper.round(speed.getNumberValue(), 2));
    }
}