package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.motion.EventSafeWalk;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class SafeWalk extends Feature {

    public SafeWalk() {
        super("SafeWalk", "Не дает упасть вам с блока", Type.Other);
    }

    @EventTarget
    public void onSafeWalk(EventSafeWalk event) {
        if (Minecraft.player == null || Minecraft.world == null)
            return;
        event.setCancelled(Minecraft.player.onGround);
    }
}
