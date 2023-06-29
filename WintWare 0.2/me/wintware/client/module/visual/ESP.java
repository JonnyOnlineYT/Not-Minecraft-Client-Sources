package me.wintware.client.module.visual;

import java.awt.Color;
import java.util.Iterator;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.visual.ColorUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class ESP extends Module {
   public static Setting mobs;
   public static Setting player;

   public ESP() {
      super("ESP", Category.Visuals);
      Main.instance.setmgr.rSetting(player = new Setting("Player", this, true));
      Main.instance.setmgr.rSetting(mobs = new Setting("Mobs", this, false));
   }

   @EventTarget
   public void on3D(Event3D event) {
      Iterator var2 = mc.world.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity e = (Entity)var2.next();
         if (e instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase)e;
            if (this.canRender(entity)) {
               this.drawEntityBox(entity);
            }
         }
      }

   }

   public void drawEntityBox(EntityLivingBase p) {
      mc.getRenderManager();
      double x = p.lastTickPosX + (p.posX - p.lastTickPosX) * (double)mc.timer.field_194147_b - RenderManager.renderPosX;
      mc.getRenderManager();
      double y = p.lastTickPosY + (p.posY - p.lastTickPosY) * (double)mc.timer.field_194147_b - RenderManager.renderPosY;
      mc.getRenderManager();
      double z = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * (double)mc.timer.field_194147_b - RenderManager.renderPosZ;
      GL11.glPushMatrix();
      GlStateManager.enableBlend();
      GL11.glTranslated(x, y, z);
      GL11.glScalef(0.03F, 0.03F, 0.03F);
      GL11.glRotated((double)(-mc.getRenderManager().playerViewY), 0.0D, 1.0D, 0.0D);
      GlStateManager.disableDepth();
      Gui.drawRect(-20.0D, 73.0D, 19.0D, 72.0D, -1);
      Gui.drawRect(-20.0D, 73.0D, -19.9999D, 0.0D, -1);
      Gui.drawRect(-20.0D, 1.0D, 19.7D, 0.0D, -1);
      Gui.drawRect(20.0D, 73.0D, 19.7D, 0.0D, -1);
      float progress = p.getHealth() / p.getMaxHealth();
      int healthY = 73;
      Gui.drawRect(20.0D, 73.0D, 19.0D, 0.0D, -1);
      Gui.drawRect(20.3D, (double)healthY, 21.999D, 0.0D, (new Color(0, 0, 0)).getRGB());
      Gui.drawRect(20.3D, (double)((int)((float)healthY * progress)), 21.999D, 0.0D, ColorUtils.getHealthColor(p).getRGB());
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
      GlStateManager.enableDepth();
      GL11.glPopMatrix();
   }

   public boolean canRender(EntityLivingBase player) {
      if (mc.player != null) {
         if (!(player instanceof EntityPlayer) && !(player instanceof EntityAnimal) && !(player instanceof EntityMob) && !(player instanceof EntityVillager)) {
            return false;
         } else if (player instanceof EntityPlayer) {
            return true;
         } else {
            return player == mc.player ? false : player.isEntityAlive();
         }
      } else {
         return false;
      }
   }
}
