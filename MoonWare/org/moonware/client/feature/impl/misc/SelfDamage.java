package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class SelfDamage extends Feature {

    private int jumps;

    public SelfDamage() {
        super("SelfDamage", "Вы наносите себе дамаг", Type.Other);
    }

    public void onEnable() {
        jumps = 0;
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        if (jumps < 14) {
            Minecraft.timer.renderPartialTicks = 4;
            for (int i = 0; i < 20; i++) {
                event.setOnGround(false);
            }
        }
        if (Minecraft.player.onGround) {
            if (jumps < 14) {
                Minecraft.player.jump();
                jumps++;
            } else {
                toggle();
            }
        }
    }
}
