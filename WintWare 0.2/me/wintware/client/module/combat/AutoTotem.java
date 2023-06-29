package me.wintware.client.module.combat;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.other.TimerUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;

public class AutoTotem extends Module {
   public Setting health;
   TimerUtils timer;

   public AutoTotem() {
      super("AutoTotem", Category.Combat);
      Main.instance.setmgr.rSetting(this.health = new Setting("Health", this, 10.0D, 0.1D, 20.0D, false));
   }

   public int totems() {
      int count = 0;

      for(int i = 0; i < mc.player.inventory.getSizeInventory(); ++i) {
         ItemStack stack = mc.player.inventory.getStackInSlot(i);
         if (!(stack.getItem() instanceof ItemAir) && stack.getItem() == Items.TOTEM_OF_UNDYING) {
            ++count;
         }
      }

      return count;
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
      }

      if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && this.totem() != -1 && (mc.currentScreen instanceof GuiInventory || mc.currentScreen == null)) {
         mc.playerController.windowClick(0, this.totem(), 1, ClickType.PICKUP, mc.player);
         mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
      }

   }

   public int totem() {
      for(int i = 0; i < 45; ++i) {
         ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
         if (itemStack.getItem() == Items.TOTEM_OF_UNDYING) {
            return i;
         }
      }

      return -1;
   }
}
