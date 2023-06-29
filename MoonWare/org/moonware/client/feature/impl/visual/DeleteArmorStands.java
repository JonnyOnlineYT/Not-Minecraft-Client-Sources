package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class DeleteArmorStands
        extends Feature {
    public DeleteArmorStands() {
        super("DeleteArmorStands", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0443\u0434\u0430\u043b\u044f\u0435\u0442 \u0432\u0441\u0435 \u0430\u0440\u043c\u043e\u0440-\u0441\u0442\u0435\u043d\u0434\u044b \u0441 \u043c\u0438\u0440\u0430", Type.Other);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.player == null || Minecraft.world == null) {
            return;
        }
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity == null || !(entity instanceof EntityArmorStand)) continue;
            Minecraft.world.removeEntity(entity);
        }
    }
}
