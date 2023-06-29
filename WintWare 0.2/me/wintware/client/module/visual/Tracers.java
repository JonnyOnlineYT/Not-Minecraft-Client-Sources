package me.wintware.client.module.visual;

import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class Tracers extends Module {
   public Tracers() {
      super("Tracers", Category.Visuals);
   }

   @EventTarget
   public void onEvent3D(Event3D event) {
      boolean old = mc.gameSettings.viewBobbing;
      mc.gameSettings.viewBobbing = false;
      mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 2);
      mc.gameSettings.viewBobbing = old;
      GL11.glPushMatrix();
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      GL11.glDepthMask(false);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(3042);
      GL11.glLineWidth(1.5F);
      Iterator var3 = mc.world.loadedEntityList.iterator();

      while(var3.hasNext()) {
         Entity entity = (Entity)var3.next();
         if (entity != mc.player && entity instanceof EntityPlayer) {
            assert mc.getRenderViewEntity() != null;

            mc.getRenderViewEntity().getDistanceToEntity(entity);
            double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) - mc.getRenderManager().viewerPosX;
            double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) - mc.getRenderManager().viewerPosY;
            double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
            GL11.glColor4f(255.0F, 255.0F, 255.0F, 255.0F);
            Vec3d vec3d = new Vec3d(0.0D, 0.0D, 1.0D);
            vec3d = vec3d.rotatePitch(-((float)Math.toRadians((double)mc.player.rotationPitch)));
            Vec3d vec3d2 = vec3d.rotateYaw(-((float)Math.toRadians((double)mc.player.rotationYaw)));
            GL11.glBegin(2);
            GL11.glVertex3d(vec3d2.xCoord, (double)mc.player.getEyeHeight() + vec3d2.yCoord, vec3d2.zCoord);
            GL11.glVertex3d(d, d2 + 1.1D, d3);
            GL11.glEnd();
         }
      }

      GL11.glDisable(3042);
      GL11.glDepthMask(true);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glPopMatrix();
   }
}
