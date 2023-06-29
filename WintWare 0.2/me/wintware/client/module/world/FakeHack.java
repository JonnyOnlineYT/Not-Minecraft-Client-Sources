package me.wintware.client.module.world;

import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventPreMotionUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.combat.RotationUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;

public class FakeHack extends Module {
   private float yaw;

   public FakeHack() {
      super("FakeHack", Category.World);
   }

   @EventTarget
   public void onPreMotion(EventPreMotionUpdate event) {
      Iterator var2 = mc.world.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity e = (Entity)var2.next();
         if (e instanceof EntityOtherPlayerMP) {
            EntityOtherPlayerMP otherPlayer = (EntityOtherPlayerMP)e;
            otherPlayer.rotationYaw = event.getYaw();
            float[] lol = RotationUtil.getRotations(e);
            this.yaw -= 22.0F;
            if (this.yaw <= -180.0F) {
               this.yaw = 180.0F;
            }

            otherPlayer.rotationYaw = lol[0];
            otherPlayer.rotationPitchHead = lol[1];
            otherPlayer.renderYawOffset = lol[0];
            otherPlayer.rotationYawHead = lol[0];
            if (mc.player.getCooledAttackStrength(0.0F) == 1.0F) {
               otherPlayer.swingArm(EnumHand.MAIN_HAND);
            }
         }
      }

   }
}
