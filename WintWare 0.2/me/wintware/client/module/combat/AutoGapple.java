package me.wintware.client.module.combat;

import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;

public class AutoGapple extends Module {
   private boolean eating = false;
   Setting health;

   public AutoGapple() {
      super("AutoGapple", Category.Combat);
      Main.instance.setmgr.rSetting(this.health = new Setting("Health", this, 15.0D, 1.0D, 20.0D, false));
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      if ((double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()) > this.health.getValDouble() && this.eating) {
         this.eating = false;
         this.stop();
      } else if (this.canEat()) {
         if (this.isFood(mc.player.getHeldItemOffhand()) && (double)mc.player.getHealth() <= this.health.getValDouble() && this.canEat()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            this.eating = true;
         }

         if (!this.canEat()) {
            this.stop();
         }

      }
   }

   public static boolean isNullOrEmptyStack(ItemStack itemStack) {
      return itemStack == null || itemStack.func_190926_b();
   }

   boolean isFood(ItemStack itemStack) {
      return !isNullOrEmptyStack(itemStack) && itemStack.getItem() instanceof ItemAppleGold;
   }

   public boolean canEat() {
      return mc.objectMouseOver == null || !(mc.objectMouseOver.entityHit instanceof EntityVillager);
   }

   void stop() {
      KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
   }
}
