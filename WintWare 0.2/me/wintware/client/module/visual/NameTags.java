package me.wintware.client.module.visual;

import java.awt.Color;
import java.util.Iterator;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.Event3D;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.utils.visual.ColorUtils;
import me.wintware.client.utils.visual.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class NameTags extends Module {
   protected final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
   float f;

   public NameTags() {
      super("NameTags", Category.Visuals);
   }

   @EventTarget
   public void on3D(Event3D event) {
      Iterator var2 = mc.world.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity e = (Entity)var2.next();
         if (e instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase)e;
            if (entity instanceof EntityPlayer) {
               EntityPlayer player = (EntityPlayer)entity;
               if (e instanceof EntityPlayer) {
                  double d6 = mc.getRenderManager().viewerPosX;
                  double d5 = mc.getRenderManager().viewerPosY;
                  double d4 = mc.getRenderManager().viewerPosZ;
                  double d3 = player.lastTickPosX + (e.posX - e.lastTickPosX) * (double)this.f - d6;
                  double d2 = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)this.f - d5;
                  double d = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)this.f - d4;
                  this.renderNameTag((EntityLivingBase)e, e.getName(), d3, d2, d);
               }
            }
         }
      }

   }

   void renderNameTag(EntityLivingBase entityLivingBase, String string, double d, double d2, double d3) {
      if (!(entityLivingBase instanceof EntityArmorStand) && entityLivingBase != mc.player) {
         double var10000 = entityLivingBase.lastTickPosX + (entityLivingBase.posX - entityLivingBase.lastTickPosX) * (double)mc.timer.field_194147_b;
         mc.getRenderManager();
         d = var10000 - RenderManager.renderPosX;
         var10000 = entityLivingBase.lastTickPosY + (entityLivingBase.posY - entityLivingBase.lastTickPosY) * (double)mc.timer.field_194147_b;
         mc.getRenderManager();
         d2 = var10000 - RenderManager.renderPosY;
         var10000 = entityLivingBase.lastTickPosZ + (entityLivingBase.posZ - entityLivingBase.lastTickPosZ) * (double)mc.timer.field_194147_b;
         mc.getRenderManager();
         d3 = var10000 - RenderManager.renderPosZ;
         ColorUtils.color(0, 0, 0, 255);
         EntityPlayerSP entityPlayerSP = mc.player;
         FontRenderer fontRenderer = mc.fontRendererObj;
         if (entityPlayerSP.canEntityBeSeen(entityLivingBase)) {
            ColorUtils.color(200, 200, 200, 255);
         }

         d2 += entityLivingBase.isSneaking() ? 0.5D : 0.7D;
         float f = entityPlayerSP.getDistanceToEntity(entityLivingBase) / 4.0F;
         if (f < 1.6F) {
            f = 1.6F;
         }

         if (entityLivingBase instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entityLivingBase;
            ColorUtils.color(255, 0, 0, 255);
         }

         int width = mc.fontRendererObj.getStringWidth(entityLivingBase.getDisplayName().getFormattedText());
         int n = (int)((float)ColorUtils.getHealthColor(entityLivingBase).getRGB() + entityLivingBase.getHealth());
         (new StringBuilder()).append(String.valueOf(string)).append(Math.round((float)n)).toString();
         RenderManager string2 = mc.getRenderManager();
         float f2 = f / 30.0F;
         f2 = (float)((double)f2 * 0.3D);
         GL11.glPushMatrix();
         GL11.glTranslatef((float)d, (float)d2 + 1.7F, (float)d3);
         GL11.glNormal3f(1.0F, 1.0F, 1.0F);
         GL11.glRotatef(-string2.playerViewY, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(string2.playerViewX, 1.0F, 0.0F, 0.0F);
         GL11.glScalef(-f2, -f2, f2);
         GL11.glEnable(2848);
         GL11.glDisable(2929);
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         GL11.glDepthMask(false);
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3042);
         Tessellator tessellator = Tessellator.getInstance();
         tessellator.getBuffer();
         GL11.glBlendFunc(770, 771);
         float progress = entityLivingBase.getHealth() / entityLivingBase.getMaxHealth();
         RenderUtil.drawRect((double)((int)((float)(-width) / 2.0F - 5.0F)), -13.0D, (double)((float)width / 2.0F + 5.0F), (double)(mc.fontRendererObj.FONT_HEIGHT + 2), (new Color(0, 0, 0, 80)).getRGB());
         RenderUtil.drawRect((double)((int)((float)(-width) / 2.0F - 5.0F)), (double)(mc.fontRendererObj.FONT_HEIGHT + 1), (double)((float)(-width) / 2.0F - 5.0F + ((float)width / 2.0F + 5.0F - (float)(-width) / 2.0F + 5.0F) * progress), (double)(mc.fontRendererObj.FONT_HEIGHT + 2), ColorUtils.getHealthColor(entityLivingBase).getRGB());
         GuiScreen.drawCenteredString(mc.fontRendererObj, entityLivingBase.getDisplayName().getFormattedText(), 0, -11, -1);
         GuiScreen.drawCenteredString(mc.fontRendererObj, "Health: " + (int)entityLivingBase.getHealth() / 2, 0, 0, -1);
         GL11.glDisable(3042);
         GL11.glDepthMask(true);
         GL11.glEnable(3553);
         GL11.glEnable(2929);
         GL11.glDisable(2848);
         GL11.glPopMatrix();
      }
   }
}
