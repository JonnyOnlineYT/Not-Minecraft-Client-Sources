package me.wintware.client.clickgui.util;

import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class ParticleEngine {
   public CopyOnWriteArrayList<Particle> particles = Lists.newCopyOnWriteArrayList();
   public float lastMouseX;
   public float lastMouseY;

   public static void drawCircle(double x, double y, float radius, int color) {
      float alpha = (float)(color >> 24 & 255) / 255.0F;
      float red = (float)(color >> 16 & 255) / 255.0F;
      float green = (float)(color >> 8 & 255) / 255.0F;
      float blue = (float)(color & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha);
      GL11.glBegin(9);

      for(int i = 0; i <= 360; ++i) {
         GL11.glVertex2d(x + Math.sin((double)i * 3.141526D / 180.0D) * (double)radius, y + Math.cos((double)i * 3.141526D / 180.0D) * (double)radius);
      }

      GL11.glEnd();
   }

   public static void disableRender2D() {
      GL11.glDisable(3042);
      GL11.glEnable(2884);
      GL11.glEnable(3553);
      GL11.glDisable(2848);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.shadeModel(7424);
      GlStateManager.disableBlend();
      GlStateManager.enableTexture2D();
   }

   public static void enableRender2D() {
      GL11.glEnable(3042);
      GL11.glDisable(2884);
      GL11.glDisable(3553);
      GL11.glEnable(2848);
      GL11.glBlendFunc(770, 771);
      GL11.glLineWidth(1.0F);
   }

   public static void setColor(int colorHex) {
      float alpha = (float)(colorHex >> 24 & 255) / 255.0F;
      float red = (float)(colorHex >> 16 & 255) / 255.0F;
      float green = (float)(colorHex >> 8 & 255) / 255.0F;
      float blue = (float)(colorHex & 255) / 255.0F;
      GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
   }

   public static void drawLine(double startX, double startY, double endX, double endY, float thickness, int color) {
      enableRender2D();
      setColor(color);
      GL11.glLineWidth(thickness);
      GL11.glBegin(1);
      GL11.glVertex2d(startX, startY);
      GL11.glVertex2d(endX, endY);
      GL11.glEnd();
      disableRender2D();
   }

   public void render(float mouseX, float mouseY) {
      GlStateManager.enableBlend();
      GlStateManager.disableAlpha();
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      float xOffset = 0.0F;
      float yOffset = 0.0F;

      while((float)this.particles.size() < (float)sr.getScaledWidth() / 8.0F) {
         this.particles.add(new Particle(sr, (new Random()).nextFloat() + 1.0F, (new Random()).nextFloat() * 5.0F + 5.0F));
      }

      List<Particle> toRemove = Lists.newArrayList();
      int maxOpacity = 52;
      int color = -570425345;
      int mouseRadius = true;
      Iterator var10 = this.particles.iterator();

      while(true) {
         Particle particle;
         double particleX;
         double particleY;
         do {
            if (!var10.hasNext()) {
               this.particles.removeAll(toRemove);
               GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GlStateManager.enableBlend();
               GlStateManager.disableAlpha();
               this.lastMouseX = (float)GLUtils.getMouseX();
               this.lastMouseY = (float)GLUtils.getMouseY();
               return;
            }

            particle = (Particle)var10.next();
            particleX = (double)particle.x + Math.sin((double)(particle.ticks / 2.0F)) * 50.0D + (double)(-xOffset / 5.0F);
            particleY = (double)(particle.ticks * particle.speed * particle.ticks / 10.0F + -yOffset / 5.0F);
            if (particleY < (double)sr.getScaledHeight()) {
               if (particle.opacity < (float)maxOpacity) {
                  particle.opacity += 2.0F;
               }

               if (particle.opacity > (float)maxOpacity) {
                  particle.opacity = (float)maxOpacity;
               }

               new Color(255, 255, 255, (int)particle.opacity);
               float particle_thickness = 1.0F;
               int line_color = (new Color(1.0F, (1.0F - (float)(particleY / (double)sr.getScaledHeight())) / 2.0F, 1.0F, 1.0F)).getRGB();
               GlStateManager.enableBlend();
               this.drawBorderedCircle(particleX, particleY, particle.radius * particle.opacity / (float)maxOpacity, color, color);
            }

            particle.ticks = (float)((double)particle.ticks + 0.05D);
         } while(!(particleY > (double)sr.getScaledHeight()) && !(particleY < 0.0D) && !(particleX > (double)sr.getScaledWidth()) && !(particleX < 0.0D));

         toRemove.add(particle);
      }
   }

   public void drawBorderedCircle(double x, double y, float radius, int outsideC, int insideC) {
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      GL11.glScalef(0.1F, 0.1F, 0.1F);
      drawCircle(x * 10.0D, y * 10.0D, radius * 10.0F, insideC);
      GL11.glScalef(10.0F, 10.0F, 10.0F);
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(2848);
   }
}
