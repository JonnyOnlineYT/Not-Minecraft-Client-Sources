package net.minecraft.enchantment;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

public enum EnumEnchantmentType {
   ALL {
      public boolean canEnchantItem(Item itemIn) {
         EnumEnchantmentType[] var2 = EnumEnchantmentType.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            EnumEnchantmentType enumenchantmenttype = var2[var4];
            if (enumenchantmenttype != EnumEnchantmentType.ALL && enumenchantmenttype.canEnchantItem(itemIn)) {
               return true;
            }
         }

         return false;
      }
   },
   ARMOR {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemArmor;
      }
   },
   ARMOR_FEET {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.FEET;
      }
   },
   ARMOR_LEGS {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.LEGS;
      }
   },
   ARMOR_CHEST {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.CHEST;
      }
   },
   ARMOR_HEAD {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemArmor && ((ItemArmor)itemIn).armorType == EntityEquipmentSlot.HEAD;
      }
   },
   WEAPON {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemSword;
      }
   },
   DIGGER {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemTool;
      }
   },
   FISHING_ROD {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemFishingRod;
      }
   },
   BREAKABLE {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn.isDamageable();
      }
   },
   BOW {
      public boolean canEnchantItem(Item itemIn) {
         return itemIn instanceof ItemBow;
      }
   },
   WEARABLE {
      public boolean canEnchantItem(Item itemIn) {
         boolean flag = itemIn instanceof ItemBlock && ((ItemBlock)itemIn).getBlock() instanceof BlockPumpkin;
         return itemIn instanceof ItemArmor || itemIn instanceof ItemElytra || itemIn instanceof ItemSkull || flag;
      }
   };

   private EnumEnchantmentType() {
   }

   public abstract boolean canEnchantItem(Item var1);

   // $FF: synthetic method
   EnumEnchantmentType(Object x2) {
      this();
   }
}
