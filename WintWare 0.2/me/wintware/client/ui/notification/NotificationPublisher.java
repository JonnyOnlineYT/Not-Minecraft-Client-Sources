package me.wintware.client.ui.notification;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.wintware.client.utils.animation.AnimationUtil;
import me.wintware.client.utils.animation.Translate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public final class NotificationPublisher {
   private static final List NOTIFICATIONS = new CopyOnWriteArrayList();

   public static void publish(ScaledResolution sr) {
      int srScaledHeight = sr.getScaledHeight();
      int scaledWidth = sr.getScaledWidth();
      int y = srScaledHeight - 50;

      for(Iterator var7 = NOTIFICATIONS.iterator(); var7.hasNext(); y -= 30) {
         Notification notification = (Notification)var7.next();
         Translate translate = notification.getTranslate();
         int width = notification.getWidth();
         if (!notification.getTimer().elapsed((long)notification.getTime())) {
            notification.scissorBoxWidth = AnimationUtil.animate((double)width, notification.scissorBoxWidth, 0.001D);
            translate.interpolate((double)(scaledWidth - width), (double)y, 5.0D);
         } else {
            notification.scissorBoxWidth = AnimationUtil.animate(0.0D, notification.scissorBoxWidth, 0.004D);
            if (notification.scissorBoxWidth < 1.0D) {
               NOTIFICATIONS.remove(notification);
            }

            y += 30;
         }

         float translateX = (float)translate.getX();
         float translateY = (float)translate.getY();
         GL11.glPushMatrix();
         GL11.glEnable(3089);
         prepareScissorBox((float)((double)scaledWidth - notification.scissorBoxWidth), translateY, (float)scaledWidth, translateY + 30.0F);
         drawRect((double)translateX, (double)translateY, (double)scaledWidth, (double)(translateY + 30.0F), (new Color(28, 27, 27)).getRGB());
         drawRect((double)translateX, (double)(translateY + 30.0F - 2.0F), (double)(translateX + (float)((long)width * ((long)notification.getTime() - notification.getTimer().getElapsedTime()) / (long)notification.getTime())), (double)(translateY + 30.0F), notification.getType().getColor());
         Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(notification.getTitle(), translateX + 4.0F, translateY + 4.0F, -1);
         Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(notification.getContent(), translateX + 4.0F, translateY + 17.0F, -1);
         GL11.glDisable(3089);
         GL11.glPopMatrix();
         y -= 33;
      }

   }

   public static void prepareScissorBox(float x, float y, float x2, float y2) {
      ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
      int factor = ScaledResolution.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }

   public static void drawRect(double left, double top, double right, double bottom, int color) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      float f3 = (float)(color >> 24 & 255) / 255.0F;
      float f4 = (float)(color >> 16 & 255) / 255.0F;
      float f5 = (float)(color >> 8 & 255) / 255.0F;
      float f6 = (float)(color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder worldrenderer = tessellator.getBuffer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f4, f5, f6, f3);
      worldrenderer.begin(7, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, top, 0.0D).endVertex();
      worldrenderer.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRect(int mode, double left, double top, double right, double bottom, int color) {
      double j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      float f3 = (float)(color >> 24 & 255) / 255.0F;
      float f4 = (float)(color >> 16 & 255) / 255.0F;
      float f5 = (float)(color >> 8 & 255) / 255.0F;
      float f6 = (float)(color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder worldrenderer = tessellator.getBuffer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f4, f5, f6, f3);
      worldrenderer.begin(mode, DefaultVertexFormats.POSITION);
      worldrenderer.pos(left, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, bottom, 0.0D).endVertex();
      worldrenderer.pos(right, top, 0.0D).endVertex();
      worldrenderer.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void queue(String title, String content, NotificationType type) {
      Minecraft mc = Minecraft.getMinecraft();
      FontRenderer fr = mc.fontRendererObj;
      NOTIFICATIONS.add(new Notification(title, content, type, fr));
   }
}
