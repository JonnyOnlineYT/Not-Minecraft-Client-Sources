package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class EnchantmentThorns extends Enchantment {
   public EnchantmentThorns(Enchantment.Rarity rarityIn, EntityEquipmentSlot... slots) {
      super(rarityIn, EnumEnchantmentType.ARMOR_CHEST, slots);
      this.setName("thorns");
   }

   public int getMinEnchantability(int enchantmentLevel) {
      return 10 + 20 * (enchantmentLevel - 1);
   }

   public int getMaxEnchantability(int enchantmentLevel) {
      return super.getMinEnchantability(enchantmentLevel) + 50;
   }

   public int getMaxLevel() {
      return 3;
   }

   public boolean canApply(ItemStack stack) {
      return stack.getItem() instanceof ItemArmor ? true : super.canApply(stack);
   }

   public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {
      Random random = user.getRNG();
      ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.THORNS, user);
      if (shouldHit(level, random)) {
         if (attacker != null) {
            attacker.attackEntityFrom(DamageSource.causeThornsDamage(user), (float)getDamage(level, random));
         }

         if (!itemstack.func_190926_b()) {
            itemstack.damageItem(3, user);
         }
      } else if (!itemstack.func_190926_b()) {
         itemstack.damageItem(1, user);
      }

   }

   public static boolean shouldHit(int level, Random rnd) {
      if (level <= 0) {
         return false;
      } else {
         return rnd.nextFloat() < 0.15F * (float)level;
      }
   }

   public static int getDamage(int level, Random rnd) {
      return level > 10 ? level - 10 : 1 + rnd.nextInt(4);
   }
}
