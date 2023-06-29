package Celestial.utils.otherutils.gayutil;

import Celestial.utils.math.ShaderShell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Random;

public class DrawHelper {
      private static Minecraft mc = Minecraft.getMinecraft();
       private static int time;
       private static float animtest;
       private static boolean anim;
      private static int test;
    /*      */   private static float alpheble;
    /*      */   protected static float zLevel;
    /*      */   public static HashMap<Integer, Integer> blurSpotCache;
    /*      */   public static HashMap<Integer, Integer> shadowCache;
    /*   44 */   private static final Frustum frustrum = new Frustum();
    /*      */   public static int fadepon(int startColor, int endColor, float progress) {
        /*   46 */     if (progress > 1.0F) {
            /*   47 */       progress = 1.0F - progress % 1.0F;
            /*      */     }
        /*   49 */     return fadeponA(startColor, endColor, progress);
        /*      */   }
    /*      */   public static int fadeponA(int startColor, int endColor, float progress) {
        /*   52 */     float invert = 1.0F - progress;
        /*   53 */     int r = (int)((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
        /*   54 */     int g = (int)((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
        /*   55 */     int b = (int)((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
        /*   56 */     int a = (int)((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
        /*   57 */     return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
        /*      */   }
    /*      */   public static void drawOutline(AxisAlignedBB axisAlignedBB, float width, Color color) {
        /*   60 */     GL11.glPushMatrix();
        /*   61 */     GlStateManager.glLineWidth(width);
        /*   62 */     GL11.glDisable(3553);
        /*   63 */     GL11.glDisable(2929);
        /*   64 */     Tessellator tessellator = Tessellator.getInstance();
        /*   65 */     BufferBuilder buffer = tessellator.getBuffer();
        /*   66 */     buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
        /*   67 */     buffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   68 */     buffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   69 */     buffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   70 */     buffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   71 */     buffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   72 */     buffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   73 */     buffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   74 */     buffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   75 */     buffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   76 */     buffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   77 */     buffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   78 */     buffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   79 */     buffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   80 */     buffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   81 */     buffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   82 */     buffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 100).endVertex();
        /*   83 */     tessellator.draw();
        /*   84 */     GL11.glEnable(3553);
        /*   85 */     GL11.glEnable(2929);
        /*   86 */     GL11.glPopMatrix();
        /*      */   }
    /*      */
    /*      */   public static void drawGlowRoundedRect(float startX, float startY, float endX, float endY, int color, float radius, float force) {
        /*   90 */     GL11.glPushMatrix();
        /*   91 */     GL11.glEnable(3042);
        /*   92 */     GL11.glDisable(3008);
        /*   93 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
        /*   94 */     float red = (color >> 16 & 0xFF) / 255.0F;
        /*   95 */     float green = (color >> 8 & 0xFF) / 255.0F;
        /*   96 */     float blue = (color & 0xFF) / 255.0F;
        /*   97 */     ShaderShell.ROUNDED_RECT.attach();
        /*   98 */     ShaderShell.ROUNDED_RECT.set4F("color", red, green, blue, alpha);
        /*   99 */     ShaderShell.ROUNDED_RECT.set2F("resolution", (Minecraft.getMinecraft()).displayWidth,
                /*  100 */         (Minecraft.getMinecraft()).displayHeight);
        /*  101 */     ShaderShell.ROUNDED_RECT.set2F("center", (startX + (endX - startX) / 2.0F) * 2.0F, (startY + (endY - startY) / 2.0F) * 2.0F);
        /*      */
        /*  103 */     ShaderShell.ROUNDED_RECT.set2F("dst", (endX - startX - radius) * 2.0F, (endY - startY - radius) * 2.0F);
        /*  104 */     ShaderShell.ROUNDED_RECT.set1F("radius", radius);
        /*  105 */     ShaderShell.ROUNDED_RECT.set1F("force", force);
        /*  106 */     GL11.glBegin(7);
        /*  107 */     GL11.glVertex2d(endX, startY);
        /*  108 */     GL11.glVertex2d(startX, startY);
        /*  109 */     GL11.glVertex2d(startX, endY);
        /*  110 */     GL11.glVertex2d(endX, endY);
        /*  111 */     GL11.glEnd();
        /*  112 */     ShaderShell.ROUNDED_RECT.detach();
        /*  113 */     GL11.glEnable(3008);
        /*  114 */     GL11.glDisable(3042);
        /*  115 */     GL11.glPopMatrix();
        /*  116 */     Gui.drawRect(0.0F, 0.0F, 0.0F, 0.0F, 0);
        /*      */   }
    /*      */   public static Color astolfoColors1(float yDist, float yTotal) {
        /*  119 */     float speed = 3500.0F;
        /*  120 */     float hue = (float)(System.currentTimeMillis() % (int)speed) + (yTotal - yDist) * 12.0F;
        /*  121 */     while (hue > speed) {
            /*  122 */       hue -= speed;
            /*      */     }
        /*  124 */     hue /= speed;
        /*  125 */     if (hue > 0.5D) {
            /*  126 */       hue = 0.5F - hue - 0.5F;
            /*      */     }
        /*  128 */     hue += 0.5F;
        /*  129 */     return new Color(hue, 0.4F, 1.0F);
        /*      */   }
    /*      */
    /*      */   public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        /*  133 */     Tessellator tessellator = Tessellator.getInstance();
        /*  134 */     BufferBuilder worldrenderer = tessellator.getBuffer();
        /*      */
        /*  136 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        /*      */
        /*      */
        /*  139 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        /*  140 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        /*  141 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        /*  142 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        /*  143 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        /*  146 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        /*  147 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        /*  148 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        /*  149 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        /*  150 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        /*  153 */     worldrenderer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        /*  154 */     worldrenderer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        /*      */
        /*  156 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        /*  157 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        /*      */
        /*  159 */     worldrenderer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        /*  160 */     worldrenderer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        /*      */
        /*  162 */     tessellator.draw();
        /*      */   }
    /*      */
    /*      */   public static void drawFilledBox(AxisAlignedBB axisAlignedBB) {
        /*  166 */     Tessellator tessellator = Tessellator.getInstance();
        /*  167 */     BufferBuilder worldRenderer = tessellator.getBuffer();
        /*      */
        /*  169 */     worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        /*  170 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  171 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  172 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  173 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  174 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  175 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  176 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  177 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*      */
        /*  179 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  180 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  181 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  182 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  183 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  184 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  185 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  186 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*      */
        /*  188 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  189 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  190 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  191 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  192 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  193 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  194 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  195 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*      */
        /*  197 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  198 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  199 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  200 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  201 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  202 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  203 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  204 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*      */
        /*  206 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  207 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  208 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  209 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  210 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  211 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  212 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  213 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*      */
        /*  215 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  216 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  217 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  218 */     worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  219 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        /*  220 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        /*  221 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        /*  222 */     worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        /*  223 */     tessellator.draw();
        /*      */   }
    /*      */
    /*      */   public static void startSmooth() {
        /*  227 */     GL11.glEnable(2848);
        /*  228 */     GL11.glEnable(2881);
        /*  229 */     GL11.glEnable(2832);
        /*  230 */     GL11.glEnable(3042);
        /*  231 */     GL11.glBlendFunc(770, 771);
        /*  232 */     GL11.glHint(3154, 4354);
        /*  233 */     GL11.glHint(3155, 4354);
        /*  234 */     GL11.glHint(3153, 4354);
        /*      */   }
    /*      */   public static void startSmooth2() {
        /*  237 */     GL11.glEnable(2848);
        /*  238 */     GL11.glEnable(2881);
        /*  239 */     GL11.glEnable(2832);
        /*  240 */     GL11.glEnable(3042);
        /*  241 */     GL11.glBlendFunc(770, 771);
        /*      */   }
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */   public static void endSmooth() {
        /*  248 */     GL11.glDisable(2848);
        /*  249 */     GL11.glDisable(2881);
        /*  250 */     GL11.glEnable(2832);
        /*      */   }
    /*      */   public static void endSmooth2() {
        /*  253 */     GL11.glDisable(2848);
        /*  254 */     GL11.glDisable(2881);
        /*  255 */     GL11.glEnable(2832);
        /*      */   }
    /*      */
    /*      */
    /*      */   public static void enableGL2D() {
        /*  260 */     GL11.glDisable(2929);
        /*  261 */     GL11.glDisable(3553);
        /*  262 */     GL11.glBlendFunc(770, 771);
        /*  263 */     GL11.glDepthMask(true);
        /*  264 */     GL11.glEnable(2848);
        /*  265 */     GL11.glHint(3154, 4354);
        /*  266 */     GL11.glHint(3155, 4354);
        /*      */   }
    /*      */
    /*      */   public static void glColor(Color color, float alpha) {
        /*  270 */     float red = color.getRed() / 255.0F;
        /*  271 */     float green = color.getGreen() / 255.0F;
        /*  272 */     float blue = color.getBlue() / 255.0F;
        /*      */
        /*  274 */     GlStateManager.color(red, green, blue, alpha);
        /*      */   }
    /*      */
    /*      */   public static void glColor(Color color, int alpha) {
        /*  278 */     glColor(color, alpha / 255.0F);
        /*      */   }
    /*      */
    /*      */   public static void disableGL2D() {
        /*  282 */     GL11.glEnable(3553);
        /*  283 */     GL11.glEnable(2929);
        /*  284 */     GL11.glDisable(2848);
        /*  285 */     GL11.glHint(3154, 4352);
        /*  286 */     GL11.glHint(3155, 4352);
        /*      */   }
    /*      */
    /*      */   public static void enableSmoothLine(float width) {
        /*  290 */     GL11.glDisable(3008);
        /*  291 */     GL11.glEnable(3042);
        /*  292 */     GL11.glBlendFunc(770, 771);
        /*  293 */     GL11.glDisable(3553);
        /*  294 */     GL11.glDisable(2929);
        /*  295 */     GL11.glDepthMask(false);
        /*  296 */     GL11.glEnable(2884);
        /*  297 */     GL11.glEnable(2848);
        /*  298 */     GL11.glHint(3154, 4354);
        /*  299 */     GL11.glHint(3155, 4354);
        /*  300 */     GL11.glLineWidth(width);
        /*      */   }
    /*      */
    /*      */   public static void disableSmoothLine() {
        /*  304 */     GL11.glEnable(3553);
        /*  305 */     GL11.glEnable(2929);
        /*  306 */     GL11.glDisable(3042);
        /*  307 */     GL11.glEnable(3008);
        /*  308 */     GL11.glDepthMask(true);
        /*  309 */     GL11.glCullFace(1029);
        /*  310 */     GL11.glDisable(2848);
        /*  311 */     GL11.glHint(3154, 4352);
        /*  312 */     GL11.glHint(3155, 4352);
        /*      */   }
    /*      */
    /*
    /*      */
    /*      */   public static int setColor(int colorHex) {
        /*  331 */     float alpha = (colorHex >> 24 & 0xFF) / 255.0F;
        /*  332 */     float red = (colorHex >> 16 & 0xFF) / 255.0F;
        /*  333 */     float green = (colorHex >> 8 & 0xFF) / 255.0F;
        /*  334 */     float blue = (colorHex & 0xFF) / 255.0F;
        /*  335 */     GL11.glColor4f(red, green, blue, (alpha == 0.0F) ? 1.0F : alpha);
        /*  336 */     return colorHex;
        /*      */   }
    /*      */
    /*      */   public static void drawGlow(double x, double y, double x1, double y1, int color) {
        /*  340 */     GlStateManager.disableTexture2D();
        /*  341 */     GlStateManager.enableBlend();
        /*  342 */     GlStateManager.disableAlpha();
        /*  343 */     GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        /*  344 */     GlStateManager.shadeModel(7425);
        /*  345 */     drawVGradientRect((int)x, (int)y, (int)x1, (int)(y + (y1 - y) / 2.0D), setAlpha(new Color(color), 0).getRGB(), color);
        /*  346 */     drawVGradientRect((int)x, (int)(y + (y1 - y) / 2.0D), (int)x1, (int)y1, color, setAlpha(new Color(color), 0).getRGB());
        /*  347 */     int radius = (int)((y1 - y) / 2.0D);
        /*  348 */     drawPolygonPart(x, y + (y1 - y) / 2.0D, radius, 0, color, setAlpha(new Color(color), 0).getRGB());
        /*  349 */     drawPolygonPart(x, y + (y1 - y) / 2.0D, radius, 1, color, setAlpha(new Color(color), 0).getRGB());
        /*  350 */     drawPolygonPart(x1, y + (y1 - y) / 2.0D, radius, 2, color, setAlpha(new Color(color), 0).getRGB());
        /*  351 */     drawPolygonPart(x1, y + (y1 - y) / 2.0D, radius, 3, color, setAlpha(new Color(color), 0).getRGB());
        /*  352 */     GlStateManager.shadeModel(7424);
        /*  353 */     GlStateManager.disableBlend();
        /*  354 */     GlStateManager.enableAlpha();
        /*  355 */     GlStateManager.enableTexture2D();
        /*      */   }
    /*      */
    /*      */   public static void drawPolygonPart(double x, double y, int radius, int part, int color, int endcolor) {
        /*  359 */     float alpha = (color >> 24 & 0xFF) / 255.0F;
        /*  360 */     float red = (color >> 16 & 0xFF) / 255.0F;
        /*  361 */     float green = (color >> 8 & 0xFF) / 255.0F;
        /*  362 */     float blue = (color & 0xFF) / 255.0F;
        /*  363 */     float alpha2 = (endcolor >> 24 & 0xFF) / 255.0F;
        /*  364 */     float red2 = (endcolor >> 16 & 0xFF) / 255.0F;
        /*  365 */     float green2 = (endcolor >> 8 & 0xFF) / 255.0F;
        /*  366 */     float blue2 = (endcolor & 0xFF) / 255.0F;
        /*  367 */     GlStateManager.disableTexture2D();
        /*  368 */     GlStateManager.enableBlend();
        /*  369 */     GlStateManager.disableAlpha();
        /*  370 */     GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        /*  371 */     GlStateManager.shadeModel(7425);
        /*  372 */     Tessellator tessellator = Tessellator.getInstance();
        /*  373 */     BufferBuilder bufferbuilder = tessellator.getBuffer();
        /*  374 */     bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        /*  375 */     bufferbuilder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        /*  376 */     double TWICE_PI = 6.283185307179586D;
        /*  377 */     for (int i = part * 90; i <= part * 90 + 90; i++) {
            /*  378 */       double angle = 6.283185307179586D * i / 360.0D + Math.toRadians(180.0D);
            /*  379 */       bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
            /*      */     }
        /*  381 */     tessellator.draw();
        /*  382 */     GlStateManager.shadeModel(7424);
        /*  383 */     GlStateManager.disableBlend();
        /*  384 */     GlStateManager.enableAlpha();
        /*  385 */     GlStateManager.enableTexture2D();
        /*      */   }
    /*      */   public static void drawVGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        /*  388 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
        /*  389 */     float f2 = (startColor >> 16 & 0xFF) / 255.0F;
        /*  390 */     float f3 = (startColor >> 8 & 0xFF) / 255.0F;
        /*  391 */     float f4 = (startColor & 0xFF) / 255.0F;
        /*  392 */     float f5 = (endColor >> 24 & 0xFF) / 255.0F;
        /*  393 */     float f6 = (endColor >> 16 & 0xFF) / 255.0F;
        /*  394 */     float f7 = (endColor >> 8 & 0xFF) / 255.0F;
        /*  395 */     float f8 = (endColor & 0xFF) / 255.0F;
        /*  396 */     GlStateManager.disableTexture2D();
        /*  397 */     GlStateManager.enableBlend();
        /*  398 */     GlStateManager.disableAlpha();
        /*  399 */     GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        /*  400 */     GlStateManager.shadeModel(7425);
        /*  401 */     Tessellator tessellator = Tessellator.getInstance();
        /*  402 */     BufferBuilder bufferbuilder = tessellator.getBuffer();
        /*  403 */     bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        /*  404 */     bufferbuilder.pos(right, top, 0.0D).color(f2, f3, f4, f).endVertex();
        /*  405 */     bufferbuilder.pos(left, top, 0.0D).color(f2, f3, f4, f).endVertex();
        /*  406 */     bufferbuilder.pos(left, bottom, 0.0D).color(f6, f7, f8, f5).endVertex();
        /*  407 */     bufferbuilder.pos(right, bottom, 0.0D).color(f6, f7, f8, f5).endVertex();
        /*  408 */     tessellator.draw();
        /*  409 */     GlStateManager.shadeModel(7424);
        /*  410 */     GlStateManager.disableBlend();
        /*  411 */     GlStateManager.enableAlpha();
        /*  412 */     GlStateManager.enableTexture2D();
        /*      */   }
    /*      */
    /*      */
    /*      */
    /*      */   public static void drawBorderedRect(double left, double top, double right, double bottom, double borderWidth, int insideColor, int borderColor, boolean borderIncludedInBounds) {
        /*  510 */     drawRect(left - (!borderIncludedInBounds ? borderWidth : 0.0D), top - (!borderIncludedInBounds ? borderWidth : 0.0D), right + (!borderIncludedInBounds ? borderWidth : 0.0D), bottom + (!borderIncludedInBounds ? borderWidth : 0.0D), borderColor);
        /*      */
        /*      */
        /*      */
        /*  514 */     drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0D), top + (borderIncludedInBounds ? borderWidth : 0.0D), right - (borderIncludedInBounds ? borderWidth : 0.0D), bottom - (borderIncludedInBounds ? borderWidth : 0.0D), insideColor);
        /*      */   }
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */   public static final void color(Color color) {
        /*  521 */     if (color == null)
            /*  522 */       color = Color.white;
        /*  523 */     color((color.getRed() / 255.0F), (color.getGreen() / 255.0F), (color.getBlue() / 255.0F), (color.getAlpha() / 255.0F));
        /*      */   }
    /*      */
    /*      */   public static final void color(double red, double green, double blue, double alpha) {
        /*  527 */     GL11.glColor4d(red, green, blue, alpha);
        /*      */   }
    /*      */
    /*      */
    /*      */
    /*      */   public static void drawRoundedRect(float left, float top, float right, float bottom, int smooth, Color color) {
        /*  626 */     Gui.drawRect(((int)left + smooth), (int)top, ((int)right - smooth), (int)bottom, color.getRGB());
        /*  627 */     Gui.drawRect((int)left, ((int)top + smooth), (int)right, ((int)bottom - smooth), color.getRGB());
        /*  628 */     drawFilledCircle((int)left + smooth, (int)top + smooth, smooth, color);
        /*  629 */     drawFilledCircle((int)right - smooth, (int)top + smooth, smooth, color);
        /*  630 */     drawFilledCircle((int)right - smooth, (int)bottom - smooth, smooth, color);
        /*  631 */     drawFilledCircle((int)left + smooth, (int)bottom - smooth, smooth, color);
        /*      */   }
    /*      */
    /*      */   public static final void drawSmoothRect(float left, float top, float right, float bottom, int color) {
        /*  635 */     GL11.glEnable(3042);
        /*  636 */     GL11.glEnable(2848);
        /*  637 */     drawRect(left, top, right, bottom, color);
        /*  638 */     GL11.glScalef(0.5F, 0.5F, 0.5F);
        /*  639 */     drawRect((left * 2.0F - 1.0F), (top * 2.0F), (left * 2.0F), (bottom * 2.0F - 1.0F), color);
        /*  640 */     drawRect((left * 2.0F), (top * 2.0F - 1.0F), (right * 2.0F), (top * 2.0F), color);
        /*  641 */     drawRect((right * 2.0F), (top * 2.0F), (right * 2.0F + 1.0F), (bottom * 2.0F - 1.0F), color);
        /*  642 */     drawRect((left * 2.0F), (bottom * 2.0F - 1.0F), (right * 2.0F), (bottom * 2.0F), color);
        /*  643 */     GL11.glDisable(3042);
        /*  644 */     GL11.glScalef(2.0F, 2.0F, 2.0F);
        /*      */   }
    /*      */
    /*      */   public static void drawFilledCircle(int xx, int yy, float radius, Color color) {
        /*  648 */     int sections = 50;
        /*  649 */     double dAngle = 6.283185307179586D / sections;
        /*  650 */     GL11.glPushAttrib(8192);
        /*  651 */     GL11.glEnable(3042);
        /*  652 */     GL11.glDisable(3553);
        /*  653 */     GL11.glBlendFunc(770, 771);
        /*  654 */     GL11.glEnable(2848);
        /*  655 */     GL11.glBegin(6);
        /*  656 */     for (int i = 0; i < sections; i++) {
            /*  657 */       float x = (float)(radius * Math.sin(i * dAngle));
            /*  658 */       float y = (float)(radius * Math.cos(i * dAngle));
            /*  659 */       GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color
/*  660 */           .getAlpha() / 255.0F);
            /*  661 */       GL11.glVertex2f(xx + x, yy + y);
            /*      */     }
        /*  663 */     GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        /*  664 */     GL11.glEnd();
        /*  665 */     GL11.glPopAttrib();
        /*      */   }
    /*      */
    /*      */   public static Color setAlpha(Color color, int alpha) {
        /*  669 */     alpha = MathHelper.clamp(alpha, 0, 255);
        /*  670 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        /*      */   }
    /*      */   public static Color getColorWithOpacity(Color color, int alpha) {
        /*  673 */     return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        /*      */   }
    /*      */   public static void renderItem(ItemStack itemStack, int x, int y) {
        /*  676 */     GlStateManager.enableBlend();
        /*  677 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        /*  678 */     GlStateManager.enableDepth();
        /*  679 */     RenderHelper.enableGUIStandardItemLighting();
        /*  680 */     Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        /*  681 */     Minecraft.getMinecraft().getRenderItem().renderItemOverlays((Minecraft.getMinecraft()).neverlose500_13, itemStack, x, y);
        /*  682 */     RenderHelper.disableStandardItemLighting();
        /*  683 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        /*  684 */     GlStateManager.disableDepth();
        /*      */   }
    /*      */
    /*      */   public static void drawRoundedRect1(double x, double y, double x1, double y1, int insideC) {
        /*  688 */     drawRect(x + 0.5D, y, x1 - 0.5D, y + 0.5D, insideC);
        /*  689 */     drawRect(x + 0.5D, y1 - 0.5D, x1 - 0.5D, y1, insideC);
        /*  690 */     drawRect(x, y + 0.5D, x1, y1 - 0.5D, insideC);
        /*      */   }
    /*      */
    /*      */   public static void prepareScissorBox(float x, float y, float x2, float y2) {
        /*  694 */     ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        /*  695 */     int factor = scale.getScaleFactor();
        /*  696 */     GL11.glScissor((int)(x * factor), (int)((scale.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
        /*      */   }
    /*      */
    /*      */
    /*      */
    /*      */   public static void drawGradientRect(double d, double e, double e2, double g, int startColor, int endColor) {
        /*  702 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
        /*  703 */     float f1 = (startColor >> 16 & 0xFF) / 255.0F;
        /*  704 */     float f2 = (startColor >> 8 & 0xFF) / 255.0F;
        /*  705 */     float f3 = (startColor & 0xFF) / 255.0F;
        /*  706 */     float f4 = (endColor >> 24 & 0xFF) / 255.0F;
        /*  707 */     float f5 = (endColor >> 16 & 0xFF) / 255.0F;
        /*  708 */     float f6 = (endColor >> 8 & 0xFF) / 255.0F;
        /*  709 */     float f7 = (endColor & 0xFF) / 255.0F;
        /*  710 */     GlStateManager.disableTexture2D();
        /*  711 */     GlStateManager.enableBlend();
        /*  712 */     GlStateManager.disableAlpha();
        /*  713 */     GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        /*  714 */     GlStateManager.shadeModel(7425);
        /*  715 */     Tessellator tessellator = Tessellator.getInstance();
        /*  716 */     BufferBuilder bufferbuilder = tessellator.getBuffer();
        /*  717 */     bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        /*  718 */     bufferbuilder.pos(e2, e, zLevel).color(f1, f2, f3, f).endVertex();
        /*  719 */     bufferbuilder.pos(d, e, zLevel).color(f1, f2, f3, f).endVertex();
        /*  720 */     bufferbuilder.pos(d, g, zLevel).color(f5, f6, f7, f4).endVertex();
        /*  721 */     bufferbuilder.pos(e2, g, zLevel).color(f5, f6, f7, f4).endVertex();
        /*  722 */     tessellator.draw();
        /*  723 */     GlStateManager.shadeModel(7424);
        /*  724 */     GlStateManager.disableBlend();
        /*  725 */     GlStateManager.enableAlpha();
        /*  726 */     GlStateManager.enableTexture2D();
        /*      */   }
    /*      */
    /*      */   public static double interpolate(double current, double old, double scale) {
        /*  730 */     return old + (current - old) * scale;
        /*      */   }
    /*      */
    /*      */   public static boolean isInViewFrustrum(Entity entity) {
        /*  734 */     return (isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck);
        /*      */   }
    /*      */
    /*      */   private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        /*  738 */     Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        /*  739 */     frustrum.setPosition(current.posX, current.posY, current.posZ);
        /*  740 */     return frustrum.isBoundingBoxInFrustum(bb);
        /*      */   }
    /*      */
    /*      */   public static void putVertex3d(Vec3d vec) {
        /*  744 */     GL11.glVertex3d(vec.x, vec.y, vec.z);
        /*      */   }
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */   public static void drawEntityOnScreen(double posX, double posY, double scale, float mouseX, float mouseY, EntityLivingBase ent, float rotate) {
        /*  790 */     GlStateManager.enableColorMaterial();
        /*  791 */     GlStateManager.pushMatrix();
        /*  792 */     GlStateManager.translate((float)posX, (float)posY, 50.0F);
        /*  793 */     GlStateManager.scale((float)-scale, (float)scale, (float)scale);
        /*  794 */     GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        /*  795 */     float f = ent.renderYawOffset;
        /*  796 */     float f1 = ent.rotationYaw;
        /*  797 */     float f2 = ent.rotationPitch;
        /*  798 */     float f3 = ent.prevRotationYawHead;
        /*  799 */     float f4 = ent.rotationYawHead;
        /*  800 */     GlStateManager.rotate(165.0F, 0.0F, 1.0F, 0.0F);
        /*  801 */     RenderHelper.enableStandardItemLighting();
        /*  802 */     GlStateManager.rotate(-165.0F, 0.0F, 1.0F, 0.0F);
        /*  803 */     GlStateManager.rotate(-((float)Math.atan((mouseY / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        /*  804 */     ent.renderYawOffset = (float)Math.atan((mouseX / 40.0F)) * 20.0F;
        /*  805 */     ent.rotationYaw = (float)Math.atan((mouseX / 40.0F)) * 40.0F;
        /*  806 */     ent.rotationPitch = -((float)Math.atan((mouseY / 40.0F))) * 20.0F;
        /*  807 */     ent.rotationYawHead = 60.0F;
        /*  808 */     ent.prevRotationYawHead = 60.0F;
        /*  809 */     GlStateManager.translate(0.0F, 0.0F, 0.0F);
        /*  810 */     RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        /*  811 */     rendermanager.setPlayerViewY(180.0F);
        /*  812 */     rendermanager.setRenderShadow(false);
        /*  813 */     rendermanager.doRenderEntity((Entity)ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        /*  814 */     rendermanager.setRenderShadow(true);
        /*  815 */     ent.renderYawOffset = f;
        /*  816 */     ent.rotationYaw = f1;
        /*  817 */     ent.rotationPitch = f2;
        /*  818 */     ent.prevRotationYawHead = f3;
        /*  819 */     ent.rotationYawHead = f4;
        /*  820 */     GlStateManager.popMatrix();
        /*  821 */     RenderHelper.disableStandardItemLighting();
        /*  822 */     GlStateManager.disableRescaleNormal();
        /*  823 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        /*  824 */     GlStateManager.disableTexture2D();
        /*  825 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        /*      */   }
    /*      */
    /*      */   public static void drawRect2(double x, double y, double width, double height, int color) {
        /*  829 */     drawRect(x, y, x + width, y + height, color);
        /*      */   }
    /*      */
    /*      */   public static void drawNewRect(double left, double top, double right, double bottom, int color) {
        /*  833 */     if (left < right) {
            /*  834 */       double i = left;
            /*  835 */       left = right;
            /*  836 */       right = i;
            /*      */     }
        /*  838 */     if (top < bottom) {
            /*  839 */       double j = top;
            /*  840 */       top = bottom;
            /*  841 */       bottom = j;
            /*      */     }
        /*  843 */     float f3 = (color >> 24 & 0xFF) / 255.0F;
        /*  844 */     float f = (color >> 16 & 0xFF) / 255.0F;
        /*  845 */     float f1 = (color >> 8 & 0xFF) / 255.0F;
        /*  846 */     float f2 = (color & 0xFF) / 255.0F;
        /*  847 */     Tessellator tessellator = Tessellator.getInstance();
        /*  848 */     BufferBuilder vertexbuffer = tessellator.getBuffer();
        /*  849 */     GlStateManager.enableBlend();
        /*  850 */     GlStateManager.disableTexture2D();
        /*  851 */     GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        /*  852 */     GlStateManager.color(f, f1, f2, f3);
        /*  853 */     vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
        /*  854 */     vertexbuffer.pos(left, bottom, 0.0D).endVertex();
        /*  855 */     vertexbuffer.pos(right, bottom, 0.0D).endVertex();
        /*  856 */     vertexbuffer.pos(right, top, 0.0D).endVertex();
        /*  857 */     vertexbuffer.pos(left, top, 0.0D).endVertex();
        /*  858 */     tessellator.draw();
        /*  859 */     GlStateManager.enableTexture2D();
        /*  860 */     GlStateManager.disableBlend();
        /*      */   }
    /*      */   public static void drawRect(double left, double top, double right, double bottom, int color) {
        /*  863 */     if (left < right) {
            /*  864 */       double i = left;
            /*  865 */       left = right;
            /*  866 */       right = i;
            /*      */     }
        /*  868 */     if (top < bottom) {
            /*  869 */       double j = top;
            /*  870 */       top = bottom;
            /*  871 */       bottom = j;
            /*      */     }
        /*  873 */     float f3 = (color >> 24 & 0xFF) / 255.0F;
        /*  874 */     float f = (color >> 16 & 0xFF) / 255.0F;
        /*  875 */     float f1 = (color >> 8 & 0xFF) / 255.0F;
        /*  876 */     float f2 = (color & 0xFF) / 255.0F;
        /*  877 */     Tessellator tessellator = Tessellator.getInstance();
        /*  878 */     BufferBuilder bufferBuilder = tessellator.getBuffer();
        /*  879 */     GlStateManager.enableBlend();
        /*  880 */     GlStateManager.disableTexture2D();
        /*  881 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        /*  882 */     GlStateManager.color(f, f1, f2, f3);
        /*  883 */     bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        /*  884 */     bufferBuilder.pos(left, bottom, 0.0D).endVertex();
        /*  885 */     bufferBuilder.pos(right, bottom, 0.0D).endVertex();
        /*  886 */     bufferBuilder.pos(right, top, 0.0D).endVertex();
        /*  887 */     bufferBuilder.pos(left, top, 0.0D).endVertex();
        /*  888 */     tessellator.draw();
        /*  889 */     GlStateManager.enableTexture2D();
        /*  890 */     GlStateManager.disableBlend();
        /*      */   }
    /*      */
    /*      */   public static int color(int n, int n2, int n3, int n4) {
        /*  894 */     n4 = 255;
        /*  895 */     return (new Color(n, n2, n3, n4)).getRGB();
        /*      */   }
    /*      */
    /*      */   public static int rainbow(int delay, float saturation, float brightness) {
        /*  899 */     double rainbow = Math.ceil(((System.currentTimeMillis() + delay) / 16L));
        /*  900 */     rainbow %= 360.0D;
        /*  901 */     return Color.getHSBColor((float)(rainbow / 360.0D), saturation, brightness).getRGB();
        /*      */   }
    /*      */
    /*      */   public static Color getRainbow(int offset, int speed) {
        /*  905 */     float hue = (float)((System.currentTimeMillis() + offset) % speed);
        /*  906 */     hue /= speed;
        /*  907 */     return Color.getHSBColor(hue, 0.7F, 1.0F);
        /*      */   }
    /*      */
    /*      */   public static Color rainbow2(int delay, float saturation, float brightness) {
        /*  911 */     double rainbow = Math.ceil(((System.currentTimeMillis() + delay) / 16L));
        /*  912 */     rainbow %= 360.0D;
        /*  913 */     return Color.getHSBColor((float)(rainbow / 360.0D), saturation, brightness);
        /*      */   }
    /*      */
    /*      */   public static Color getHealthColor(EntityLivingBase entityLivingBase) {
        /*  917 */     float health = entityLivingBase.getHealth();
        /*  918 */     float[] fractions = { 0.0F, 0.15F, 0.55F, 0.7F, 0.9F };
        /*  919 */     Color[] colors = { new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN };
        /*  920 */     float progress = health / entityLivingBase.getMaxHealth();
        /*  921 */     return (health >= 0.0F) ? blendColors(fractions, colors, progress).brighter() : colors[0];
        /*      */   }
    /*      */
    /*      */   public static int getRandomColor() {
        /*  925 */     char[] letters = "012345678".toCharArray();
        /*  926 */     String color = "0x";
        /*  927 */     for (int i = 0; i < 6; i++) {
            /*  928 */       color = color + letters[(new Random()).nextInt(letters.length)];
            /*      */     }
        /*  930 */     return Integer.decode(color).intValue();
        /*      */   }
    /*      */
    /*      */   public static int reAlpha(int color, float alpha) {
        /*  934 */     Color c = new Color(color);
        /*  935 */     float r = 0.003921569F * c.getRed();
        /*  936 */     float g = 0.003921569F * c.getGreen();
        /*  937 */     float b = 0.003921569F * c.getBlue();
        /*  938 */     return (new Color(r, g, b, alpha)).getRGB();
        /*      */   }
    /*      */
    /*      */   public static Color getGradientOffset(Color color1, Color color2, double offset) {
        /*  942 */     if (offset > 1.0D) {
            /*  943 */       double left = offset % 1.0D;
            /*  944 */       int off = (int)offset;
            /*  945 */       offset = (off % 2 == 0) ? left : (1.0D - left);
            /*      */     }
        /*  947 */     double inverse_percent = 1.0D - offset;
        /*  948 */     int redPart = (int)(color1.getRed() * inverse_percent + color2.getRed() * offset);
        /*  949 */     int greenPart = (int)(color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        /*  950 */     int bluePart = (int)(color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        /*  951 */     return new Color(redPart, greenPart, bluePart);
        /*      */   }
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */
    /*      */   public static int toRGBA(int r, int g, int b, int a) {
        /*  982 */     return (r << 16) + (g << 8) + (b << 0) + (a << 24);
        /*      */   }
    /*      */
    /*      */   public static int toRGBA(float r, float g, float b, float a) {
        /*  986 */     return toRGBA((int)(r * 255.0F), (int)(g * 255.0F), (int)(b * 255.0F), (int)(a * 255.0F));
        /*      */   }
    /*      */
    /*      */   public static int getColor(int red, int green, int blue) {
        /*  990 */     return getColor(red, green, blue, 255);
        /*      */   }
    /*      */
    /*      */   public static int getColor(int red, int green, int blue, int alpha) {
        /*  994 */     int color = 0;
        /*  995 */     color |= alpha << 24;
        /*  996 */     color |= red << 16;
        /*  997 */     color |= green << 8;
        /*  998 */     return color |= blue;
        /*      */   }
    /*      */
    /*      */   public static int getColor(int brightness) {
        /* 1002 */     return getColor(brightness, brightness, brightness, 255);
        /*      */   }
    /*      */
    /*      */   public static int getColor(int brightness, int alpha) {
        /* 1006 */     return getColor(brightness, brightness, brightness, alpha);
        /*      */   }
    /*      */
    /*      */   public static Color fade(Color color, int index, int count) {
        /* 1010 */     float[] hsb = new float[3];
        /* 1011 */     Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        /* 1012 */     float brightness = Math.abs((
                /* 1013 */         (float)(System.currentTimeMillis() % 2000L) / 1000.0F + index / count * 2.0F) % 2.0F - 1.0F);
        /*      */
        /* 1015 */     brightness = 0.5F + 0.5F * brightness;
        /* 1016 */     hsb[2] = brightness % 2.0F;
        /* 1017 */     return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        /*      */   }
    /*      */
    /*      */
    /*      */   public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        /* 1022 */     if (fractions == null) {
            /* 1023 */       throw new IllegalArgumentException("Fractions can't be null");
            /*      */     }
        /* 1025 */     if (colors == null) {
            /* 1026 */       throw new IllegalArgumentException("Colours can't be null");
            /*      */     }
        /* 1028 */     if (fractions.length != colors.length) {
            /* 1029 */       throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
            /*      */     }
        /* 1031 */     int[] indicies = getFractionIndicies(fractions, progress);
        /* 1032 */     float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
        /* 1033 */     Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
        /* 1034 */     float max = range[1] - range[0];
        /* 1035 */     float value = progress - range[0];
        /* 1036 */     float weight = value / max;
        /* 1037 */     return blend(colorRange[0], colorRange[1], (1.0F - weight));
        /*      */   }
    /*      */
    /*      */
    /*      */   public static int[] getFractionIndicies(float[] fractions, float progress) {
        /* 1042 */     int[] range = new int[2]; int startPoint;
        /* 1043 */     for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; startPoint++);
        /*      */
        /* 1045 */     if (startPoint >= fractions.length) {
            /* 1046 */       startPoint = fractions.length - 1;
            /*      */     }
        /* 1048 */     range[0] = startPoint - 1;
        /* 1049 */     range[1] = startPoint;
        /* 1050 */     return range;
        /*      */   }
    /*      */
    /*      */   public static Color blend(Color color1, Color color2, double ratio) {
        /* 1054 */     float r = (float)ratio;
        /* 1055 */     float ir = 1.0F - r;
        /* 1056 */     float[] rgb1 = new float[3];
        /* 1057 */     float[] rgb2 = new float[3];
        /* 1058 */     color1.getColorComponents(rgb1);
        /* 1059 */     color2.getColorComponents(rgb2);
        /* 1060 */     float red = rgb1[0] * r + rgb2[0] * ir;
        /* 1061 */     float green = rgb1[1] * r + rgb2[1] * ir;
        /* 1062 */     float blue = rgb1[2] * r + rgb2[2] * ir;
        /* 1063 */     if (red < 0.0F) {
            /* 1064 */       red = 0.0F;
            /* 1065 */     } else if (red > 255.0F) {
            /* 1066 */       red = 255.0F;
            /*      */     }
        /* 1068 */     if (green < 0.0F) {
            /* 1069 */       green = 0.0F;
            /* 1070 */     } else if (green > 255.0F) {
            /* 1071 */       green = 255.0F;
            /*      */     }
        /* 1073 */     if (blue < 0.0F) {
            /* 1074 */       blue = 0.0F;
            /* 1075 */     } else if (blue > 255.0F) {
            /* 1076 */       blue = 255.0F;
            /*      */     }
        /* 1078 */     Color color = null;
        /*      */     try {
            /* 1080 */       color = new Color(red, green, blue);
            /* 1081 */     } catch (IllegalArgumentException exp) {
            /* 1082 */       NumberFormat numberFormat = NumberFormat.getNumberInstance();
            /*      */     }
        /* 1084 */     return color;
        /*      */   }
    /*      */
    /*      */
    /*      */   public static int astolfo(int delay, float offset) {
        /* 1089 */     float speed = 3000.0F;
        /* 1090 */     float hue = Math.abs((float)(System.currentTimeMillis() % delay) + -offset / 21.0F * 2.0F);
        /* 1091 */     for (; hue > speed; hue -= speed);
        /*      */
        /* 1093 */     if ((hue /= speed) > 0.5D) {
            /* 1094 */       hue = 0.5F - hue - 0.5F;
            /*      */     }
        /* 1096 */     return Color.HSBtoRGB(hue += 0.5F, 0.5F, 1.0F);
        /*      */   }
    /*      */
    /*      */   public static Color TwoColoreffect(Color cl1, Color cl2, double speed) {
        /* 1100 */     double thing = speed / 4.0D % 1.0D;
        /* 1101 */     float val = MathHelper.clamp((float)Math.sin(18.84955592153876D * thing) / 2.0F + 0.5F, 0.0F, 1.0F);
        /* 1102 */     return new Color(lerp(cl1.getRed() / 255.0F, cl2.getRed() / 255.0F, val),
                /* 1103 */         lerp(cl1.getGreen() / 255.0F, cl2.getGreen() / 255.0F, val),
                /* 1104 */         lerp(cl1.getBlue() / 255.0F, cl2.getBlue() / 255.0F, val));
        /*      */   }
    /*      */
    /*      */
    /*      */   public static int astolfoColors(int yOffset, int yTotal) {
        /* 1109 */     float speed = 2900.0F;
        /* 1110 */     float hue = (float)(System.currentTimeMillis() % (int)speed) + ((yTotal - yOffset) * 9);
        /* 1111 */     for (; hue > speed; hue -= speed);
        /*      */
        /* 1113 */     if ((hue /= speed) > 0.5D) {
            /* 1114 */       hue = 0.5F - hue - 0.5F;
            /*      */     }
        /* 1116 */     return Color.HSBtoRGB(hue += 0.5F, 0.5F, 1.0F);
        /*      */   }
    /*      */   public static Color astolfoColor(int yOffset, int yTotal) {
        /* 1119 */     float speed = 2900.0F;
        /* 1120 */     float hue = (float)(System.currentTimeMillis() % (int)speed) + ((yTotal - yOffset) * 9);
        /* 1121 */     while (hue > speed) {
            /* 1122 */       hue -= speed;
            /*      */     }
        /* 1124 */     hue /= speed;
        /* 1125 */     if (hue > 0.5D) {
            /* 1126 */       hue = 0.5F - hue - 0.5F;
            /*      */     }
        /* 1128 */     hue += 0.5F;
        /* 1129 */     return new Color(hue, 0.5F, 1.0F);
        /*      */   }
    /*      */   public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        /* 1132 */     float f = (col1 >> 24 & 0xFF) / 255.0F;
        /* 1133 */     float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        /* 1134 */     float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        /* 1135 */     float f3 = (col1 & 0xFF) / 255.0F;
        /* 1136 */     float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        /* 1137 */     float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        /* 1138 */     float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        /* 1139 */     float f7 = (col2 & 0xFF) / 255.0F;
        /* 1140 */     GL11.glEnable(3042);
        /* 1141 */     GL11.glDisable(3553);
        /* 1142 */     GL11.glBlendFunc(770, 771);
        /* 1143 */     GL11.glEnable(2848);
        /* 1144 */     GL11.glShadeModel(7425);
        /* 1145 */     GL11.glPushMatrix();
        /* 1146 */     GL11.glBegin(7);
        /* 1147 */     GL11.glColor4f(f1, f2, f3, f);
        /* 1148 */     GL11.glVertex2d(left, top);
        /* 1149 */     GL11.glVertex2d(left, bottom);
        /* 1150 */     GL11.glColor4f(f5, f6, f7, f4);
        /* 1151 */     GL11.glVertex2d(right, bottom);
        /* 1152 */     GL11.glVertex2d(right, top);
        /* 1153 */     GL11.glEnd();
        /* 1154 */     GL11.glPopMatrix();
        /* 1155 */     GL11.glEnable(3553);
        /* 1156 */     GL11.glDisable(3042);
        /*      */   }
    /*      */
    /*      */
    /*      */   public static int getTeamColor(Entity entityIn) {
        /* 1161 */     int i = -1;
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /*      */
        /* 1177 */     i = entityIn.getDisplayName().getUnformattedText().equalsIgnoreCase("пїЅf[пїЅcRпїЅf]пїЅc" + entityIn.getName()) ? getColor((new Color(255, 60, 60)).getRGB()) : (entityIn.getDisplayName().getUnformattedText().equalsIgnoreCase("пїЅf[пїЅ9BпїЅf]пїЅ9" + entityIn.getName()) ? getColor((new Color(60, 60, 255)).getRGB()) : (entityIn.getDisplayName().getUnformattedText().equalsIgnoreCase("пїЅf[пїЅeYпїЅf]пїЅe" + entityIn.getName()) ? getColor((new Color(255, 255, 60)).getRGB()) : (entityIn.getDisplayName().getUnformattedText().equalsIgnoreCase("пїЅf[пїЅaGпїЅf]пїЅa" + entityIn.getName()) ? getColor((new Color(60, 255, 60)).getRGB()) : getColor((new Color(255, 255, 255)).getRGB()))));
        /* 1178 */     return i;
        /*      */   }
    /*      */
    public static int astolfoColors4(float speed, int yOffset, float saturation) {
        float hue;
        for (hue = (float)(System.currentTimeMillis() % (long)((int)speed) + (long)yOffset); hue > speed; hue -= speed) {
        }
        if ((double)(hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, saturation, 1.0F);
    }

    /*      */
    /*      */   public static int astolfoColors5(float yDist, float yTotal, float saturation, float speedt) {
        /* 1196 */     float speed = 1800.0F;
        /* 1197 */     float hue = (float)(System.currentTimeMillis() % (int)speed) + (yTotal - yDist) * speedt;
        /* 1198 */     while (hue > speed) {
            /* 1199 */       hue -= speed;
            /*      */     }
        /* 1201 */     hue /= speed;
        /* 1202 */     if (hue > 0.5D) {
            /* 1203 */       hue = 0.5F - hue - 0.5F;
            /*      */     }
        /* 1205 */     hue += 0.5F;
        /* 1206 */     return Color.HSBtoRGB(hue, saturation, 1.0F);
        /*      */   }
    /*      */
    public static int astolfoColors45(float speed, int yOffset, float saturation) {
        float hue;
        for (hue = (float)(System.currentTimeMillis() % (long)((int)speed) + (long)yOffset); hue > speed; hue -= speed) {
        }
        if ((double)(hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, saturation, 1.0F);
    }
    /*      */
    /*      */   public static float lerp(float a, float b, float f) {
        /* 1224 */     return a + f * (b - a);
        /*      */   }
    /*      */ }

