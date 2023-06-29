package me.wintware.client.utils.visual;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

public class RenderUtil {
   public static double interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }

   public static void drawBoundingBox(AxisAlignedBB axisalignedbb) {
      GL11.glBegin(7);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ);
      GL11.glVertex3d(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ);
      GL11.glEnd();
   }

   public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
      int sections = 6;
      double dAngle = 6.283185307179586D / (double)sections;
      GL11.glPushAttrib(8192);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glBegin(6);

      for(int i = 0; i < sections; ++i) {
         float x = (float)((double)radius * Math.sin((double)i * dAngle));
         float y = (float)((double)radius * Math.cos((double)i * dAngle));
         GL11.glColor4f((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
         GL11.glVertex2f((float)xx + x, (float)yy + y);
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glEnd();
      GL11.glPopAttrib();
   }

   public static final void drawSmoothRect(float left, float top, float right, float bottom, int color) {
      GL11.glEnable(3042);
      GL11.glEnable(2848);
      drawRect((double)left, (double)top, (double)right, (double)bottom, color);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      drawRect((double)(left * 2.0F - 1.0F), (double)(top * 2.0F), (double)(left * 2.0F), (double)(bottom * 2.0F - 1.0F), color);
      drawRect((double)(left * 2.0F), (double)(top * 2.0F - 1.0F), (double)(right * 2.0F), (double)(top * 2.0F), color);
      drawRect((double)(right * 2.0F), (double)(top * 2.0F), (double)(right * 2.0F + 1.0F), (double)(bottom * 2.0F - 1.0F), color);
      drawRect((double)(left * 2.0F), (double)(bottom * 2.0F - 1.0F), (double)(right * 2.0F), (double)(bottom * 2.0F), color);
      GL11.glDisable(3042);
      GL11.glScalef(2.0F, 2.0F, 2.0F);
   }

   public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
      GL11.glEnable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      Minecraft.getMinecraft().getTextureManager().bindTexture(image);
      Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, (float)width, (float)height);
      disableGL2D();
      GL11.glPopMatrix();
   }

   public static void drawCircle(float cx, double cy, float r, int num_segments, int c) {
      GL11.glPushMatrix();
      cx *= 2.0F;
      cy *= 2.0D;
      float f = (float)(c >> 24 & 255) / 255.0F;
      float f1 = (float)(c >> 16 & 255) / 255.0F;
      float f2 = (float)(c >> 8 & 255) / 255.0F;
      float f3 = (float)(c & 255) / 255.0F;
      float theta = (float)(6.2831852D / (double)num_segments);
      float p = (float)Math.cos((double)theta);
      float s = (float)Math.sin((double)theta);
      float x = r *= 2.0F;
      float y = 0.0F;
      GL11.glEnable(2848);
      enableGL2D();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glBegin(2);

      for(int ii = 0; ii < num_segments; ++ii) {
         GL11.glVertex2f(x + cx, (float)((double)y + cy));
         float t = x;
         x = p * x - s * y;
         y = s * t + p * y;
      }

      GL11.glEnd();
      GL11.glScalef(2.0F, 2.0F, 2.0F);
      disableGL2D();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public static void prepareScissorBox(float x, float y, float x2, float y2) {
      ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
      int factor = ScaledResolution.getScaleFactor();
      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
   }

   public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
      float f = (float)(col1 >> 24 & 255) / 255.0F;
      float f1 = (float)(col1 >> 16 & 255) / 255.0F;
      float f2 = (float)(col1 >> 8 & 255) / 255.0F;
      float f3 = (float)(col1 & 255) / 255.0F;
      float f4 = (float)(col2 >> 24 & 255) / 255.0F;
      float f5 = (float)(col2 >> 16 & 255) / 255.0F;
      float f6 = (float)(col2 >> 8 & 255) / 255.0F;
      float f7 = (float)(col2 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glShadeModel(7425);
      GL11.glPushMatrix();
      GL11.glBegin(7);
      GL11.glColor4f(f1, f2, f3, f);
      GL11.glVertex2d(left, top);
      GL11.glVertex2d(left, bottom);
      GL11.glColor4f(f5, f6, f7, f4);
      GL11.glVertex2d(right, bottom);
      GL11.glVertex2d(right, top);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   public static void enableGL2D() {
      GL11.glDisable(2929);
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
   }

   public static void disableGL2D() {
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glEnable(2929);
      GL11.glDisable(2848);
      GL11.glHint(3154, 4352);
      GL11.glHint(3155, 4352);
   }

   public static void relativeRect(float left, float top, float right, float bottom, int color) {
      float f3;
      if (left < right) {
         f3 = left;
         left = right;
         right = f3;
      }

      if (top < bottom) {
         f3 = top;
         top = bottom;
         bottom = f3;
      }

      f3 = (float)(color >> 24 & 255) / 255.0F;
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuffer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f, f1, f2, f3);
      bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
      bufferBuilder.pos((double)left, (double)bottom, 0.0D).endVertex();
      bufferBuilder.pos((double)right, (double)bottom, 0.0D).endVertex();
      bufferBuilder.pos((double)right, (double)top, 0.0D).endVertex();
      bufferBuilder.pos((double)left, (double)top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
      drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0D), top - (!borderIncludedInBounds ? borderWidth : 0.0D), right + (!borderIncludedInBounds ? borderWidth : 0.0D), bottom + (!borderIncludedInBounds ? borderWidth : 0.0D), borderColor);
      drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0D), top + (borderIncludedInBounds ? borderWidth : 0.0D), right - (borderIncludedInBounds ? borderWidth : 0.0D), bottom - (borderIncludedInBounds ? borderWidth : 0.0D), insideColor);
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
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuffer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f, f1, f2, f3);
      bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
      bufferBuilder.pos(left, bottom, 0.0D).endVertex();
      bufferBuilder.pos(right, bottom, 0.0D).endVertex();
      bufferBuilder.pos(right, top, 0.0D).endVertex();
      bufferBuilder.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public static void drawRectOpacity(double left, double top, double right, double bottom, float opacity) {
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

      Tessellator tessellator = Tessellator.getInstance();
      BufferBuilder bufferBuilder = tessellator.getBuffer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(0.1F, 0.1F, 0.1F, opacity);
      bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
      bufferBuilder.pos(left, bottom, 0.0D).endVertex();
      bufferBuilder.pos(right, bottom, 0.0D).endVertex();
      bufferBuilder.pos(right, top, 0.0D).endVertex();
      bufferBuilder.pos(left, top, 0.0D).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }
}
