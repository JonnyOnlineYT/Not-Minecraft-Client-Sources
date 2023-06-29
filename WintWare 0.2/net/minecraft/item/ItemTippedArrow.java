package net.minecraft.item;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemTippedArrow extends ItemArrow {
   public ItemStack func_190903_i() {
      return PotionUtils.addPotionToItemStack(super.func_190903_i(), PotionTypes.POISON);
   }

   public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
      EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
      entitytippedarrow.setPotionEffect(stack);
      return entitytippedarrow;
   }

   public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab) {
      if (this.func_194125_a(itemIn)) {
         Iterator var3 = PotionType.REGISTRY.iterator();

         while(var3.hasNext()) {
            PotionType potiontype = (PotionType)var3.next();
            if (!potiontype.getEffects().isEmpty()) {
               tab.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potiontype));
            }
         }
      }

   }

   public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
      PotionUtils.addPotionTooltip(stack, tooltip, 0.125F);
   }

   public String getItemStackDisplayName(ItemStack stack) {
      return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("tipped_arrow.effect."));
   }
}
