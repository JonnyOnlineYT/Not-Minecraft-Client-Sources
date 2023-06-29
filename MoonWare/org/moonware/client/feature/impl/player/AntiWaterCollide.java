package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class AntiWaterCollide extends Feature {
    public AntiWaterCollide(){
        super("AntiWaterCollide","Убирает коллизию воды", Type.Other);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player.isInWater()) {
        }
    }
}
