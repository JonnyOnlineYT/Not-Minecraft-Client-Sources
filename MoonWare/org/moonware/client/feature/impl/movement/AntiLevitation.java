package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.init.MobEffects;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class AntiLevitation extends Feature {

    public AntiLevitation() {
        super("AntiLevitation", "Убирает эффект левитации", Type.Other);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.isPotionActive(MobEffects.LEVITATION)) {
            Minecraft.player.removeActivePotionEffect(MobEffects.LEVITATION);
        }
    }
}