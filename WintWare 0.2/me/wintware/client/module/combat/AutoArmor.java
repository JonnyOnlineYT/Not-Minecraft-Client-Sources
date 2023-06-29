package me.wintware.client.module.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovementInput;

public class AutoArmor extends Module {
   public AutoArmor() {
      super("AutoArmor", Category.Combat);
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (!(mc.currentScreen instanceof GuiContainer) || mc.currentScreen instanceof InventoryEffectRenderer) {
         InventoryPlayer inventory = mc.player.inventory;
         if (MovementInput.moveStrafe == 0.0F) {
            if (MovementInput.moveStrafe == 0.0F) {
               int[] bestArmorSlots = new int[4];
               int[] bestArmorValues = new int[4];

               int slot;
               ItemStack stack;
               ItemArmor item;
               for(slot = 0; slot < 4; ++slot) {
                  bestArmorSlots[slot] = -1;
                  stack = inventory.armorItemInSlot(slot);
                  if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                     item = (ItemArmor)stack.getItem();
                     bestArmorValues[slot] = this.getArmorValue(item, stack);
                  }
               }

               int j;
               for(slot = 0; slot < 36; ++slot) {
                  stack = inventory.getStackInSlot(slot);
                  if (!isNullOrEmpty(stack) && stack.getItem() instanceof ItemArmor) {
                     item = (ItemArmor)stack.getItem();
                     j = item.armorType.getIndex();
                     int armorValue = this.getArmorValue(item, stack);
                     if (armorValue > bestArmorValues[j]) {
                        bestArmorSlots[j] = slot;
                        bestArmorValues[j] = armorValue;
                     }
                  }
               }

               ArrayList<Integer> types = new ArrayList(Arrays.asList(0, 1, 2, 3));
               Collections.shuffle(types);
               Iterator iterator = types.iterator();

               while(iterator.hasNext()) {
                  int i = (Integer)iterator.next();
                  j = bestArmorSlots[i];
                  if (j != -1) {
                     ItemStack oldArmor = inventory.armorItemInSlot(i);
                     if (isNullOrEmpty(oldArmor) || inventory.getFirstEmptyStack() != -1) {
                        if (j < 9) {
                           j += 36;
                        }

                        if (!isNullOrEmpty(oldArmor)) {
                           mc.playerController.windowClick(0, 8 - i, 0, ClickType.QUICK_MOVE, mc.player);
                        }

                        mc.playerController.windowClick(0, j, 0, ClickType.QUICK_MOVE, mc.player);
                        break;
                     }
                  }
               }

            }
         }
      }
   }

   int getArmorValue(ItemArmor item, ItemStack stack) {
      int armorPoints = item.damageReduceAmount;
      int prtPoints = false;
      int armorToughness = (int)item.toughness;
      int armorType = item.getArmorMaterial().getDamageReductionAmount(EntityEquipmentSlot.LEGS);
      Enchantment protection = Enchantments.PROTECTION;
      int prtLvl = EnchantmentHelper.getEnchantmentLevel(protection, stack);
      EntityPlayerSP player = mc.player;
      DamageSource dmgSource = DamageSource.causePlayerDamage(player);
      int prtPoints = protection.calcModifierDamage(prtLvl, dmgSource);
      return armorPoints * 5 + prtPoints * 3 + armorToughness + armorType;
   }

   public static boolean isNullOrEmpty(ItemStack stack) {
      return stack == null || stack.func_190926_b();
   }
}
