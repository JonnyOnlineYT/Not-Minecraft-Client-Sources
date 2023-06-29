package net.minecraft.inventory;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.math.MathHelper;

public class SlotFurnaceOutput extends Slot {
   private final EntityPlayer thePlayer;
   private int removeCount;

   public SlotFurnaceOutput(EntityPlayer player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
      super(inventoryIn, slotIndex, xPosition, yPosition);
      this.thePlayer = player;
   }

   public boolean isItemValid(ItemStack stack) {
      return false;
   }

   public ItemStack decrStackSize(int amount) {
      if (this.getHasStack()) {
         this.removeCount += Math.min(amount, this.getStack().func_190916_E());
      }

      return super.decrStackSize(amount);
   }

   public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_) {
      this.onCrafting(p_190901_2_);
      super.func_190901_a(p_190901_1_, p_190901_2_);
      return p_190901_2_;
   }

   protected void onCrafting(ItemStack stack, int amount) {
      this.removeCount += amount;
      this.onCrafting(stack);
   }

   protected void onCrafting(ItemStack stack) {
      stack.onCrafting(this.thePlayer.world, this.thePlayer, this.removeCount);
      if (!this.thePlayer.world.isRemote) {
         int i = this.removeCount;
         float f = FurnaceRecipes.instance().getSmeltingExperience(stack);
         int j;
         if (f == 0.0F) {
            i = 0;
         } else if (f < 1.0F) {
            j = MathHelper.floor((float)i * f);
            if (j < MathHelper.ceil((float)i * f) && Math.random() < (double)((float)i * f - (float)j)) {
               ++j;
            }

            i = j;
         }

         while(i > 0) {
            j = EntityXPOrb.getXPSplit(i);
            i -= j;
            this.thePlayer.world.spawnEntityInWorld(new EntityXPOrb(this.thePlayer.world, this.thePlayer.posX, this.thePlayer.posY + 0.5D, this.thePlayer.posZ + 0.5D, j));
         }
      }

      this.removeCount = 0;
   }
}
