package me.wintware.client.module.combat;

import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class TriggerBot extends Module {
   public TriggerBot() {
      super("TriggerBot", Category.Combat);
   }

   @EventTarget
   public void onEventUpdate(EventUpdate e) {
      Entity entity = mc.objectMouseOver.entityHit;
      if (entity != null && !((double)mc.player.getDistanceToEntity(entity) > 3.5D) && !(entity instanceof EntityEnderCrystal) && !entity.isDead && !(((EntityLivingBase)entity).getHealth() <= 0.0F) && entity instanceof EntityPlayer) {
         if (mc.player.getCooledAttackStrength(0.0F) == 1.0F) {
            mc.playerController.attackEntity(mc.player, entity);
            mc.player.swingArm(EnumHand.MAIN_HAND);
         }

      }
   }
}
