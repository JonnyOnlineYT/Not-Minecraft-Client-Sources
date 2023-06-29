package me.wintware.client.module.visual;

import java.awt.Color;
import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class ChestESP extends Module {
   public ChestESP() {
      super("ChestESP", Category.Visuals);
   }

   @EventTarget
   public void onEvent(Event3D e) {
      Iterator var2 = mc.world.loadedTileEntityList.iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         if (o instanceof TileEntityChest) {
            TileEntityLockable storage = (TileEntityLockable)o;
            this.drawESPOnStorage(storage, (double)storage.getPos().getX(), (double)storage.getPos().getY(), (double)storage.getPos().getZ());
         }

         Iterator var7 = mc.world.loadedTileEntityList.iterator();

         while(var7.hasNext()) {
            Object o1 = var7.next();
            if (o instanceof TileEntityEnderChest) {
               TileEntityEnderChest storage1 = (TileEntityEnderChest)o;
               this.drawChestEspOnStorage(storage1, (double)storage1.getPos().getX(), (double)storage1.getPos().getY(), (double)storage1.getPos().getZ());
            }
         }
      }

   }

   private void drawESPOnStorage(TileEntityLockable storage, double x, double y, double z) {
      if (!storage.isLocked()) {
         TileEntityChest chest = (TileEntityChest)storage;
         Vec3d vec;
         Vec3d vec2;
         if (chest.adjacentChestZNeg != null) {
            vec = new Vec3d(x + 0.0625D, y, z - 0.9375D);
            vec2 = new Vec3d(x + 0.9375D, y + 0.875D, z + 0.9375D);
         } else if (chest.adjacentChestXNeg != null) {
            vec = new Vec3d(x + 0.9375D, y, z + 0.0625D);
            vec2 = new Vec3d(x - 0.9375D, y + 0.875D, z + 0.9375D);
         } else {
            if (chest.adjacentChestXPos != null || chest.adjacentChestZPos != null) {
               return;
            }

            vec = new Vec3d(x + 0.0625D, y, z + 0.0625D);
            vec2 = new Vec3d(x + 0.9375D, y + 0.875D, z + 0.9375D);
         }

         GL11.glPushMatrix();
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDisable(3553);
         GL11.glEnable(2848);
         GL11.glDisable(2929);
         GL11.glDepthMask(false);
         mc.entityRenderer.setupCameraTransform(mc.timer.field_194147_b, 2);
         double rainbowState = Math.ceil((double)(System.currentTimeMillis() + 300L + 300L)) / 15.0D;
         rainbowState %= 360.0D;
         Color chamsColor = Color.getHSBColor((float)(rainbowState / 360.0D), 0.4F, 1.0F);
         GL11.glColor4d((double)((float)chamsColor.getRed() / 255.0F), (double)((float)chamsColor.getGreen() / 255.0F), (double)((float)chamsColor.getBlue() / 255.0F), (double)((float)chamsColor.getAlpha() / 255.0F));
         double var10002 = vec.xCoord;
         mc.getRenderManager();
         var10002 -= RenderManager.renderPosX;
         double var10003 = vec.yCoord;
         mc.getRenderManager();
         var10003 -= RenderManager.renderPosY;
         double var10004 = vec.zCoord;
         mc.getRenderManager();
         var10004 -= RenderManager.renderPosZ;
         double var10005 = vec2.xCoord;
         mc.getRenderManager();
         var10005 -= RenderManager.renderPosX;
         double var10006 = vec2.yCoord;
         mc.getRenderManager();
         var10006 -= RenderManager.renderPosY;
         double var10007 = vec2.zCoord;
         mc.getRenderManager();
         RenderUtil.drawBoundingBox(new AxisAlignedBB(var10002, var10003, var10004, var10005, var10006, var10007 - RenderManager.renderPosZ));
         GL11.glDisable(2848);
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GL11.glDepthMask(true);
         GL11.glDisable(3042);
         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

   private void drawChestEspOnStorage(TileEntityEnderChest storage, double x, double y, double z) {
      new Vec3d(x + 0.0625D, y, z - 0.9375D);
      new Vec3d(x + 0.9375D, y + 0.875D, z + 0.9375D);
      new Vec3d(x + 0.9375D, y, z + 0.0625D);
      new Vec3d(x - 0.9375D, y + 0.875D, z + 0.9375D);
      Vec3d vec = new Vec3d(x + 0.0625D, y, z + 0.0625D);
      Vec3d vec2 = new Vec3d(x + 0.9375D, y + 0.875D, z + 0.9375D);
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glDisable(2929);
      GL11.glDepthMask(false);
      mc.entityRenderer.setupCameraTransform(mc.timer.field_194147_b, 2);
      double rainbowState = Math.ceil((double)(System.currentTimeMillis() + 300L + 300L)) / 15.0D;
      rainbowState %= 360.0D;
      Color chamsColor = new Color(203, 1, 208);
      GL11.glColor4d((double)((float)chamsColor.getRed() / 255.0F), (double)((float)chamsColor.getGreen() / 255.0F), (double)((float)chamsColor.getBlue() / 255.0F), (double)((float)chamsColor.getAlpha() / 255.0F));
      double var10002 = vec.xCoord;
      mc.getRenderManager();
      var10002 -= RenderManager.renderPosX;
      double var10003 = vec.yCoord;
      mc.getRenderManager();
      var10003 -= RenderManager.renderPosY;
      double var10004 = vec.zCoord;
      mc.getRenderManager();
      var10004 -= RenderManager.renderPosZ;
      double var10005 = vec2.xCoord;
      mc.getRenderManager();
      var10005 -= RenderManager.renderPosX;
      double var10006 = vec2.yCoord;
      mc.getRenderManager();
      var10006 -= RenderManager.renderPosY;
      double var10007 = vec2.zCoord;
      mc.getRenderManager();
      RenderUtil.drawBoundingBox(new AxisAlignedBB(var10002, var10003, var10004, var10005, var10006, var10007 - RenderManager.renderPosZ));
      GL11.glDisable(2848);
      GL11.glEnable(3553);
      GL11.glEnable(2929);
      GL11.glDepthMask(true);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }
}
