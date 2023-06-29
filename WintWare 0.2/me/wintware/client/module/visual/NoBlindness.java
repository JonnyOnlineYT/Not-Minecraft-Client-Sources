package me.wintware.client.module.visual;

import java.util.Objects;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.potion.Potion;

public class NoBlindness extends Module {
   public NoBlindness() {
      super("NoBlindness", Category.Visuals);
   }

   @EventTarget
   public void onUpdate(EventUpdate e) {
      mc.player.removePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(15)));
      mc.player.removePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(2)));
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }
}
