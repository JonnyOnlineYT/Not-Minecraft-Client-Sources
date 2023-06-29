package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.player.InventoryHelper;

public class MLG extends Feature {

    public MLG() {
        super("MLG", "Ставит воду под тобой", Type.Other);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        int oldSlot = Minecraft.player.inventory.currentItem;
        if (Minecraft.player.fallDistance > 5) {
            RayTraceResult traceResult = Minecraft.world.rayTraceBlocks(Minecraft.player.getPositionVector(), Minecraft.player.getPositionVector().addVector(0, -15, 0), true, false, false);
            if (traceResult != null && InventoryHelper.findWaterBucket() > 0 && traceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                event.setPitch(86);
                Minecraft.player.inventory.currentItem = InventoryHelper.findWaterBucket();
                Minecraft.playerController.processRightClick(Minecraft.player, Minecraft.world, EnumHand.MAIN_HAND);
                Minecraft.player.inventory.currentItem = oldSlot;
            }
        }
    }

}
