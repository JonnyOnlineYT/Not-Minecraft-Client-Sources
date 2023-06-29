package me.wintware.client.clickgui.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public final class GLUtils {
   private static final Random random = new Random();
   public static List<Integer> vbos = new ArrayList();
   public static List<Integer> textures = new ArrayList();

   public static void glScissor(int[] rect) {
      glScissor((float)rect[0], (float)rect[1], (float)(rect[0] + rect[2]), (float)(rect[1] + rect[3]));
   }

   public static void glScissor(float x, float y, float x1, float y1) {
      int scaleFactor = getScaleFactor();
      GL11.glScissor((int)(x * (float)scaleFactor), (int)((float)Minecraft.getMinecraft().displayHeight - y1 * (float)scaleFactor), (int)((x1 - x) * (float)scaleFactor), (int)((y1 - y) * (float)scaleFactor));
   }

   public static int getScaleFactor() {
      int scaleFactor = 1;
      boolean isUnicode = Minecraft.getMinecraft().isUnicode();
      int guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
      if (guiScale == 0) {
         guiScale = 1000;
      }

      while(scaleFactor < guiScale && Minecraft.getMinecraft().displayWidth / (scaleFactor + 1) >= 320 && Minecraft.getMinecraft().displayHeight / (scaleFactor + 1) >= 240) {
         ++scaleFactor;
      }

      if (isUnicode && scaleFactor % 2 != 0 && scaleFactor != 1) {
         --scaleFactor;
      }

      return scaleFactor;
   }

   public static int getMouseX() {
      return Mouse.getX() * getScreenWidth() / Minecraft.getMinecraft().displayWidth;
   }

   public static int getMouseY() {
      return getScreenHeight() - Mouse.getY() * getScreenHeight() / Minecraft.getMinecraft().displayWidth - 1;
   }

   public static int getScreenWidth() {
      return Minecraft.getMinecraft().displayWidth / getScaleFactor();
   }

   public static int getScreenHeight() {
      return Minecraft.getMinecraft().displayHeight / getScaleFactor();
   }

   public static boolean isHovered(int x, int y, int width, int height, int mouseX, int mouseY) {
      return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY < y + height;
   }

   public static int genVBO() {
      int id = GL15.glGenBuffers();
      vbos.add(id);
      GL15.glBindBuffer(34962, id);
      return id;
   }

   public static int getTexture() {
      int textureID = GL11.glGenTextures();
      textures.add(textureID);
      return textureID;
   }

   public static int applyTexture(int texId, File file, int filter, int wrap) throws IOException {
      applyTexture(texId, ImageIO.read(file), filter, wrap);
      return texId;
   }

   public static int applyTexture(int texId, BufferedImage image, int filter, int wrap) {
      int[] pixels = new int[image.getWidth() * image.getHeight()];
      image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
      ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

      for(int y = 0; y < image.getHeight(); ++y) {
         for(int x = 0; x < image.getWidth(); ++x) {
            int pixel = pixels[y * image.getWidth() + x];
            buffer.put((byte)(pixel >> 16 & 255));
            buffer.put((byte)(pixel >> 8 & 255));
            buffer.put((byte)(pixel & 255));
            buffer.put((byte)(pixel >> 24 & 255));
         }
      }

      buffer.flip();
      applyTexture(texId, image.getWidth(), image.getHeight(), buffer, filter, wrap);
      return texId;
   }

   public static int applyTexture(int texId, int width, int height, ByteBuffer pixels, int filter, int wrap) {
      GL11.glBindTexture(3553, texId);
      GL11.glTexParameteri(3553, 10241, filter);
      GL11.glTexParameteri(3553, 10240, filter);
      GL11.glTexParameteri(3553, 10242, wrap);
      GL11.glTexParameteri(3553, 10243, wrap);
      GL11.glPixelStorei(3317, 1);
      GL11.glTexImage2D(3553, 0, 32856, width, height, 0, 6408, 5121, pixels);
      GL11.glBindTexture(3553, 0);
      return texId;
   }

   public static void cleanup() {
      GL15.glBindBuffer(34962, 0);
      GL11.glBindTexture(3553, 0);
      Iterator iterator = vbos.iterator();

      int texture;
      while(iterator.hasNext()) {
         texture = (Integer)iterator.next();
         GL15.glDeleteBuffers(texture);
      }

      iterator = textures.iterator();

      while(iterator.hasNext()) {
         texture = (Integer)iterator.next();
         GL11.glDeleteTextures(texture);
      }

   }

   public static void glColor(float red, float green, float blue, float alpha) {
      GlStateManager.color(red, green, blue, alpha);
   }

   public static void glColor(Color color) {
      GlStateManager.color((float)color.getRed() / 255.0F, (float)color.getGreen() / 255.0F, (float)color.getBlue() / 255.0F, (float)color.getAlpha() / 255.0F);
   }

   public static void glColor(int color) {
      GlStateManager.color((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, (float)(color >> 24 & 255) / 255.0F);
   }

   public static Color getHSBColor(float hue, float sturation, float luminance) {
      return Color.getHSBColor(hue, sturation, luminance);
   }

   public static Color getRandomColor(int saturationRandom, float luminance) {
      float hue = random.nextFloat();
      float saturation = (float)((random.nextInt(saturationRandom) + saturationRandom) / saturationRandom + saturationRandom);
      return getHSBColor(hue, saturation, luminance);
   }

   public static Color getRandomColor() {
      return getRandomColor(1000, 0.6F);
   }
}
