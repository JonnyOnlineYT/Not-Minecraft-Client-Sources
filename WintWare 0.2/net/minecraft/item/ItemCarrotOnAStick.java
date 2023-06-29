package net.minecraft.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemCarrotOnAStick extends Item {
   public ItemCarrotOnAStick() {
      this.setCreativeTab(CreativeTabs.TRANSPORTATION);
      this.setMaxStackSize(1);
      this.setMaxDamage(25);
   }

   public boolean isFull3D() {
      return true;
   }

   public boolean shouldRotateAroundWhenRendering() {
      return true;
   }

   public ActionResult<ItemStack> onItemRightClick(World itemStackIn, EntityPlayer worldIn, EnumHand playerIn) {
      ItemStack itemstack = worldIn.getHeldItem(playerIn);
      if (itemStackIn.isRemote) {
         return new ActionResult(EnumActionResult.PASS, itemstack);
      } else {
         if (worldIn.isRiding() && worldIn.getRidingEntity() instanceof EntityPig) {
            EntityPig entitypig = (EntityPig)worldIn.getRidingEntity();
            if (itemstack.getMaxDamage() - itemstack.getMetadata() >= 7 && entitypig.boost()) {
               itemstack.damageItem(7, worldIn);
               if (itemstack.func_190926_b()) {
                  ItemStack itemstack1 = new ItemStack(Items.FISHING_ROD);
                  itemstack1.setTagCompound(itemstack.getTagCompound());
                  return new ActionResult(EnumActionResult.SUCCESS, itemstack1);
               }

               return new ActionResult(EnumActionResult.SUCCESS, itemstack);
            }
         }

         worldIn.addStat(StatList.getObjectUseStats(this));
         return new ActionResult(EnumActionResult.PASS, itemstack);
      }
   }
}
