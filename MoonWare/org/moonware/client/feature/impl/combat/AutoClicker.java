package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.settings.impl.NumberSetting;

public class AutoClicker extends Feature {

    public NumberSetting minCps = new NumberSetting("Min", 6, 1, 20, 1, () -> true, NumberSetting.NumberType.APS);
    public NumberSetting maxCps = new NumberSetting("Max", 10, 1, 20, 1, () -> true, NumberSetting.NumberType.APS);

    public TimerHelper timerHelper = new TimerHelper();

    public AutoClicker() {
        super("AutoClicker", "Кликает определенный cps", Type.Combat);
        addSettings(minCps, maxCps);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        int cps;
        if (Minecraft.gameSettings.keyBindAttack.isKeyDown() && !Minecraft.player.isUsingItem()) {
            cps = (int) MWUtils.randomFloat(maxCps.getNumberValue(), minCps.getNumberValue());
            if (timerHelper.hasReached(1000 / cps)) {
                Helper.mc.clickMouse();
                timerHelper.reset();
            }
        }
    }

}
