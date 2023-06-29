package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.NumberSetting;

public class PushAttack extends Feature {

    private final NumberSetting clickCoolDown;

    public PushAttack() {
        super("PushAttack", "Позволяет бить на ЛКМ не смотря на использование предметов", Type.Combat);
        clickCoolDown = new NumberSetting("Click CoolDown", 1F, 0.5F, 1F, 0.1F, () -> true);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.getCooledAttackStrength(0) == clickCoolDown.getNumberValue() && Minecraft.gameSettings.keyBindAttack.pressed) {
            Helper.mc.clickMouse();
        }
    }
}
