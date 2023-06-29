package ru.rockstar.client.features.impl.visuals;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ListSetting;

import java.util.Objects;

public class FullBright extends Feature {

    public static ListSetting brightMode;

    public FullBright() {
        super("FullBright", "Убирает темноту в игре",0, Category.VISUALS);
        brightMode = new ListSetting("FullBright Mode", "Gamma", () -> true, "Gamma", "Potion");
        addSettings(brightMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (isToggled()) {
            String mode = brightMode.getOptions();
            if (mode.equalsIgnoreCase("Gamma")) {
                mc.gameSettings.gammaSetting = 1000F;
            }
            if (mode.equalsIgnoreCase("Potion")) {
                mc.player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 817, 1));
            } else {
                mc.player.removePotionEffect(Potion.getPotionById(16));
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.gameSettings.gammaSetting = 1F;
        mc.player.removePotionEffect(Potion.getPotionById(16));
    }
}
