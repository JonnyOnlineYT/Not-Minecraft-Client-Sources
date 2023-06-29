package me.wintware.client.module.visual;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class SkeletonESP extends Module {
   private static Map<EntityPlayer, float[][]> entities = new HashMap();
   public final List<Entity> collectedEntities = new ArrayList();
   private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
   private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
   private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

   public SkeletonESP() {
      super("SkeletonESP", Category.Visuals);
   }

   @EventTarget
   public void onRender2D(Event3D e) {
      this.startEnd(true);
      GL11.glEnable(2903);
      GL11.glDisable(2848);
      entities.keySet().removeIf(this::doesntContain);
      Iterator var2 = mc.world.playerEntities.iterator();

      while(var2.hasNext()) {
         EntityPlayer entityPlayer = (EntityPlayer)var2.next();
         this.drawSkeleton(e, entityPlayer);
      }

      RenderUtil.drawRect(0.0D, 0.0D, 0.0D, 0.0D, 0);
      this.startEnd(false);
   }

   private void drawSkeleton(Event3D event, EntityPlayer e) {
      if (!e.isInvisible()) {
         float[][] entPos = (float[][])entities.get(e);
         if (entPos != null && e.isEntityAlive() && !e.isDead && e != mc.player && !e.isPlayerSleeping()) {
            GL11.glPushMatrix();
            GL11.glEnable(2848);
            GL11.glLineWidth(1.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            Vec3d vec = this.getVec3(event, e);
            double var10000 = vec.xCoord;
            mc.getRenderManager();
            double x = var10000 - RenderManager.renderPosX;
            var10000 = vec.yCoord;
            mc.getRenderManager();
            double y = var10000 - RenderManager.renderPosY;
            var10000 = vec.zCoord;
            mc.getRenderManager();
            double z = var10000 - RenderManager.renderPosZ;
            GL11.glTranslated(x, y, z);
            float xOff = e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * event.getPartialTicks();
            GL11.glRotatef(-xOff, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(0.0D, 0.0D, e.isSneaking() ? -0.235D : 0.0D);
            float yOff = e.isSneaking() ? 0.6F : 0.75F;
            GL11.glPushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslated(-0.125D, (double)yOff, 0.0D);
            if (entPos[3][0] != 0.0F) {
               GL11.glRotatef(entPos[3][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            if (entPos[3][1] != 0.0F) {
               GL11.glRotatef(entPos[3][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (entPos[3][2] != 0.0F) {
               GL11.glRotatef(entPos[3][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0D, 0.0D, 0.0D);
            GL11.glVertex3d(0.0D, (double)(-yOff), 0.0D);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslated(0.125D, (double)yOff, 0.0D);
            if (entPos[4][0] != 0.0F) {
               GL11.glRotatef(entPos[4][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            if (entPos[4][1] != 0.0F) {
               GL11.glRotatef(entPos[4][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (entPos[4][2] != 0.0F) {
               GL11.glRotatef(entPos[4][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0D, 0.0D, 0.0D);
            GL11.glVertex3d(0.0D, (double)(-yOff), 0.0D);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glTranslated(0.0D, 0.0D, e.isSneaking() ? 0.25D : 0.0D);
            GL11.glPushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslated(0.0D, e.isSneaking() ? -0.05D : 0.0D, e.isSneaking() ? -0.01725D : 0.0D);
            GL11.glPushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslated(-0.375D, (double)yOff + 0.55D, 0.0D);
            if (entPos[1][0] != 0.0F) {
               GL11.glRotatef(entPos[1][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            if (entPos[1][1] != 0.0F) {
               GL11.glRotatef(entPos[1][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (entPos[1][2] != 0.0F) {
               GL11.glRotatef(-entPos[1][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0D, 0.0D, 0.0D);
            GL11.glVertex3d(0.0D, -0.5D, 0.0D);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.375D, (double)yOff + 0.55D, 0.0D);
            if (entPos[2][0] != 0.0F) {
               GL11.glRotatef(entPos[2][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            if (entPos[2][1] != 0.0F) {
               GL11.glRotatef(entPos[2][1] * 57.295776F, 0.0F, 1.0F, 0.0F);
            }

            if (entPos[2][2] != 0.0F) {
               GL11.glRotatef(-entPos[2][2] * 57.295776F, 0.0F, 0.0F, 1.0F);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0D, 0.0D, 0.0D);
            GL11.glVertex3d(0.0D, -0.5D, 0.0D);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glRotatef(xOff - e.rotationYawHead, 0.0F, 1.0F, 0.0F);
            GL11.glPushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslated(0.0D, (double)yOff + 0.55D, 0.0D);
            if (entPos[0][0] != 0.0F) {
               GL11.glRotatef(entPos[0][0] * 57.295776F, 1.0F, 0.0F, 0.0F);
            }

            GL11.glBegin(3);
            GL11.glVertex3d(0.0D, 0.0D, 0.0D);
            GL11.glVertex3d(0.0D, 0.3D, 0.0D);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glRotatef(e.isSneaking() ? 25.0F : 0.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslated(0.0D, e.isSneaking() ? -0.16175D : 0.0D, e.isSneaking() ? -0.48025D : 0.0D);
            GL11.glPushMatrix();
            GL11.glTranslated(0.0D, (double)yOff, 0.0D);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.125D, 0.0D, 0.0D);
            GL11.glVertex3d(0.125D, 0.0D, 0.0D);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslated(0.0D, (double)yOff, 0.0D);
            GL11.glBegin(3);
            GL11.glVertex3d(0.0D, 0.0D, 0.0D);
            GL11.glVertex3d(0.0D, 0.55D, 0.0D);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(0.0D, (double)yOff + 0.55D, 0.0D);
            GL11.glBegin(3);
            GL11.glVertex3d(-0.375D, 0.0D, 0.0D);
            GL11.glVertex3d(0.375D, 0.0D, 0.0D);
            GL11.glEnd();
            GL11.glPopMatrix();
            GL11.glPopMatrix();
         }
      }

   }

   private Vec3d getVec3(Event3D event, EntityPlayer var0) {
      float pt = event.getPartialTicks();
      double x = var0.lastTickPosX + (var0.posX - var0.lastTickPosX) * (double)pt;
      double y = var0.lastTickPosY + (var0.posY - var0.lastTickPosY) * (double)pt;
      double z = var0.lastTickPosZ + (var0.posZ - var0.lastTickPosZ) * (double)pt;
      return new Vec3d(x, y, z);
   }

   public static void addEntity(EntityPlayer e, ModelPlayer model) {
      entities.put(e, new float[][]{{model.bipedHead.rotateAngleX, model.bipedHead.rotateAngleY, model.bipedHead.rotateAngleZ}, {model.bipedRightArm.rotateAngleX, model.bipedRightArm.rotateAngleY, model.bipedRightArm.rotateAngleZ}, {model.bipedLeftArm.rotateAngleX, model.bipedLeftArm.rotateAngleY, model.bipedLeftArm.rotateAngleZ}, {model.bipedRightLeg.rotateAngleX, model.bipedRightLeg.rotateAngleY, model.bipedRightLeg.rotateAngleZ}, {model.bipedLeftLeg.rotateAngleX, model.bipedLeftLeg.rotateAngleY, model.bipedLeftLeg.rotateAngleZ}});
   }

   public void collectEntities() {
      this.collectedEntities.clear();
      List playerEntities = mc.world.loadedEntityList;
      int playerEntitiesSize = playerEntities.size();

      for(int i = 0; i < playerEntitiesSize; ++i) {
         Entity entity = (Entity)playerEntities.get(i);
         if (this.isValid(entity)) {
            this.collectedEntities.add(entity);
         }
      }

   }

   private boolean isValid(Entity entity) {
      if (entity != mc.player && mc.gameSettings.thirdPersonView != 0) {
         if (entity.isDead) {
            return false;
         } else if (entity.isInvisible()) {
            return false;
         } else if (entity instanceof EntityAnimal) {
            return true;
         } else if (entity instanceof EntityPlayer) {
            return true;
         } else {
            return entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityDragon || entity instanceof EntityGolem;
         }
      } else {
         return false;
      }
   }

   private boolean doesntContain(EntityPlayer var0) {
      return !mc.world.playerEntities.contains(var0);
   }

   private void startEnd(boolean revert) {
      if (revert) {
         GlStateManager.pushMatrix();
         GlStateManager.enableBlend();
         GL11.glEnable(2848);
         GlStateManager.disableDepth();
         GlStateManager.disableTexture2D();
         GL11.glHint(3154, 4354);
      } else {
         GlStateManager.disableBlend();
         GlStateManager.enableTexture2D();
         GL11.glDisable(2848);
         GlStateManager.enableDepth();
         GlStateManager.popMatrix();
      }

      GlStateManager.depthMask(!revert);
   }
}
