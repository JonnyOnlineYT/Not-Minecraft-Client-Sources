package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBow;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.math.RotationHelper;

public class AimBot extends Feature {

    public AimBot() {
        super("AimBot", "", Type.Combat);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (Minecraft.player.getHeldItemMainhand().getItem() instanceof ItemBow && Minecraft.player.getDistanceToEntity(entity) < 6 && entity != null) {
                float[] rots = RotationHelper.getRotations(entity,false);
                event.setYaw(rots[0]);
                Minecraft.player.rotationYawHead = rots[0];
                Minecraft.player.renderYawOffset = rots[0];
            }
        }
    }
}
