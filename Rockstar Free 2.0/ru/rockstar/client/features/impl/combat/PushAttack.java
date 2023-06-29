package ru.rockstar.client.features.impl.combat;

import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class PushAttack extends Feature {

    private final NumberSetting clickCoolDown;

    public PushAttack() {
        super("PushAttack", "Позволяет бить на ЛКМ не смотря на использование предметов",0, Category.COMBAT);
        clickCoolDown = new NumberSetting("Click CoolDown", 1F, 0.5F, 1F, 0.1F, () -> true);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getCooledAttackStrength(0) == clickCoolDown.getNumberValue() && mc.gameSettings.keyBindAttack.pressed) {
            mc.clickMouse();
        }
    }
}
