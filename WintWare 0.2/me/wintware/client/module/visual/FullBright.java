package me.wintware.client.module.visual;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Module {
   public FullBright() {
      super("FullBright", Category.Visuals);
      ArrayList<String> bright = new ArrayList();
      bright.add("Gamma");
      bright.add("Potion");
      Main.instance.setmgr.rSetting(new Setting("Bright Mode", this, "Gamma", bright));
   }

   @EventTarget
   public void onPreMotion(EventUpdate event) {
      if (this.getState()) {
         String mode = Main.instance.setmgr.getSettingByName("Bright Mode").getValString();
         if (mode.equalsIgnoreCase("Gamma")) {
            mc.player.removePotionEffect(Potion.getPotionById(16));
            mc.gameSettings.gammaSetting = 1000.0F;
         }

         if (mode.equalsIgnoreCase("Potion")) {
            mc.player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 817, 1));
         }
      }

   }

   public void onDisable() {
      super.onDisable();
      mc.gameSettings.gammaSetting = 0.1F;
      mc.player.removePotionEffect(Potion.getPotionById(16));
   }
}
