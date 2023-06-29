package Celestial.module.impl.Combat;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.OnEatFood;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemAppleGold;

public class AppleGoldenTimer extends Module {
    public static boolean cooldown;
    private boolean isEated;
    private NumberSetting ticks = new NumberSetting("Cooldown", 3, 1, 6, 0.5f, NumberSetting.NumberType.SEC);

    public AppleGoldenTimer() {
        super("CDApple", "CoolDown на поедание яблока.", ModuleCategory.Combat);
        addSettings(ticks);
    }

    @EventTarget
    public void onUpdate(OnEatFood eventUpdate) {
        if (!(eventUpdate.getItem().getItem() instanceof ItemAppleGold)) return;
        Helper.mc.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, ticks.getCurrentValueInt() * 20);
    }
}