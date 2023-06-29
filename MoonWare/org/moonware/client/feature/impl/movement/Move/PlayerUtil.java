package org.moonware.client.feature.impl.movement.Move;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.*;

public class PlayerUtil
{
    private static Minecraft mc = Minecraft.getMinecraft();
    private static EntityPlayerSP thePlayer = Minecraft.player;
    public static Integer findItem(Item item) {
        for (int i = 8; i > 0; i--) {
            ItemStack itemStack = thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) {
                if (item == null) return i;
            }
        }

        for (int i = 8; i > 0; i--) {
            ItemStack itemStack = thePlayer.inventory.getStackInSlot(i);
            if (!(itemStack.getItem() instanceof ItemPotion || itemStack.getItem() instanceof ItemEnderPearl || itemStack.getItem() instanceof ItemBed || itemStack.getItem() instanceof ItemSword || itemStack.getItem() instanceof ItemFood || itemStack.getItem() instanceof ItemBow || itemStack.getItem() instanceof ItemFishingRod)) {
                if (item == null) return i;
                continue;
            }

            if (itemStack.getItem() == item) {
                return i;
            }
        }

        return null;
    }
}
