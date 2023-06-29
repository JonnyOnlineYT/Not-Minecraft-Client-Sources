package me.wintware.client.module.visual;

import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ShadowESP extends Module {
   public ShadowESP() {
      super("ShadowEsp", Category.Visuals);
   }

   public void onDisable() {
      Iterator var1 = mc.world.playerEntities.iterator();

      while(var1.hasNext()) {
         EntityPlayer player = (EntityPlayer)var1.next();
         if (player.isGlowing()) {
            player.setGlowing(false);
         }
      }

      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      Iterator var2 = mc.world.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity player = (Entity)var2.next();
         if (player instanceof EntityPlayer) {
            player.setGlowing(true);
         }
      }

   }
}
