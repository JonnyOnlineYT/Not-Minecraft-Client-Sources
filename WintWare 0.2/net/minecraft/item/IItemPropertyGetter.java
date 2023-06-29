package net.minecraft.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public interface IItemPropertyGetter {
   float apply(ItemStack var1, World var2, EntityLivingBase var3);
}
