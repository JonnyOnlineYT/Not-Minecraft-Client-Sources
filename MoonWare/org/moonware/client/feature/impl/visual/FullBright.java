package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.ListSetting;

public class FullBright extends Feature {

    public static ListSetting brightMode;

    public FullBright() {
        super("FullBright", "Убирает темноту в игре", Type.Visuals);
        brightMode = new ListSetting("FullBright Mode", "Gamma", () -> true, "Gamma", "Potion");
        addSettings(brightMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (getState()) {
            String mode = brightMode.getOptions();
            if (mode.equalsIgnoreCase("Gamma")) {
                Minecraft.gameSettings.gamma = 1000F;
            }
            if (mode.equalsIgnoreCase("Potion")) {
                Minecraft.player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 817, 1));
            } else {
                Minecraft.player.removePotionEffect(Potion.getPotionById(16));
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.gameSettings.gamma = 1F;
        Minecraft.player.removePotionEffect(Potion.getPotionById(16));
    }
}
