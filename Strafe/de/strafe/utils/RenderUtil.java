package de.strafe.utils;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

public class RenderUtil {

    private static final FloatBuffer windowPosition = BufferUtils.createFloatBuffer(4);
    private static final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer modelMatrix = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer projectionMatrix = GLAllocation.createDirectFloatBuffer(16);
    private static final Frustum frustum = new Frustum();
    private int colorDelay = 11;
    private int colorLength = 110;
    private int displayWidth = 0, displayHeight = 0;
    private static ScaledResolution scaledResolution;
    private static int lastWidth, lastHeight, lastScaledWidth, lastScaledHeight, lastGuiScale;
    public static float delta;

    public static int rainbow(float seconds, float saturation, float brightness, long index) {
        float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (seconds * 1000);
        return Color.HSBtoRGB(hue, saturation, brightness);
    }
    public static void renderObjectCircle(double x, double y, double round, int color) {
        arc(x, y, 0, 360, (float) round, color);
    }

    public static void bindTexture(int texture) {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public static void drawCornerBox(double x, double y, double x2, double y2, double lw, Color color) {
        double width = Math.abs(x2 - x);
        double height = Math.abs(y2 - y);
        double halfWidth = width / 4;
        double halfHeight = height / 4;
        start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth((float) lw);
        setColor(color);

        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x + halfWidth, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y + halfHeight);
        GL11.glEnd();


        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x, y + height - halfHeight);
        GL11.glVertex2d(x, y + height);
        GL11.glVertex2d(x + halfWidth, y + height);
        GL11.glEnd();

        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x + width - halfWidth, y + height);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y + height - halfHeight);
        GL11.glEnd();

        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x + width, y + halfHeight);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x + width - halfWidth, y);
        GL11.glEnd();

        GL11.glPopMatrix();
        stop2D();
    }


    private static final FloatBuffer WND_POS_BUFFER = GLAllocation.createDirectFloatBuffer(4);
    private static final IntBuffer VIEWPORT_BUFFER = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer MODEL_MATRIX_BUFFER = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION_MATRIX_BUFFER = GLAllocation.createDirectFloatBuffer(16);
    private static final IntBuffer SCISSOR_BUFFER = GLAllocation.createDirectIntBuffer(16);




    public static double animateProgress(final double current, final double target, final double speed) {
        if (current < target) {
            final double inc = 1.0 / Minecraft.getDebugFPS() * speed;
            if (target - current < inc) {
                return target;
            } else {
                return current + inc;
            }
        } else if (current > target) {
            final double inc = 1.0 / Minecraft.getDebugFPS() * speed;
            if (current - target < inc) {
                return target;
            } else {
                return current - inc;
            }
        }

        return current;
    }

    public static double bezierBlendAnimation(double t) {
        return t * t * (3.0 - 2.0 * t);
    }

    public static void glDrawTriangle(final double x, final double y,
                                      final double x1, final double y1,
                                      final double x2, final double y2,
                                      final int colour) {
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable blending
        final boolean restore = glEnableBlend();
        // Enable anti-aliasing
        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

        glColour(colour);

        // Start drawing a triangle
        glBegin(GL_TRIANGLES);
        {
            glVertex2d(x, y);
            glVertex2d(x1, y1);
            glVertex2d(x2, y2);
        }
        glEnd();

        // Enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blending
        glRestoreBlend(restore);
        // Disable anti-aliasing
        glDisable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }

    public static void glDrawFramebuffer(final int framebufferTexture, final int width, final int height) {
        // Bind the texture of our framebuffer
        glBindTexture(GL_TEXTURE_2D, framebufferTexture);
        // Disable alpha testing so fading out outline works
        glDisable(GL_ALPHA_TEST);
        // Make sure blend is enabled
        final boolean restore = glEnableBlend();
        // Draw the frame buffer texture upside-down
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 1);
            glVertex2f(0, 0);

            glTexCoord2f(0, 0);
            glVertex2f(0, height);

            glTexCoord2f(1, 0);
            glVertex2f(width, height);

            glTexCoord2f(1, 1);
            glVertex2f(width, 0);
        }
        glEnd();
        // Restore blend
        glRestoreBlend(restore);
        // Restore alpha test
        glEnable(GL_ALPHA_TEST);
    }

    public static void glDrawPlusSign(final double x,
                                      final double y,
                                      final double size,
                                      final double rotation,
                                      final int colour) {
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable blending
        final boolean restore = glEnableBlend();
        // Enable anti-aliasing
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        // Set line width
        glLineWidth(1.f);
        // Push new matrix
        glPushMatrix();
        // Translate matrix
        glTranslated(x, y, 0);
        // Rotate matrix by rotation value (do after translation
        glRotated(rotation, 0, 1, 1);
        // Disable depth
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);

        glColour(colour);

        // Start drawing a triangle
        glBegin(GL_LINES);
        {
            // Horizontal stroke
            glVertex2d(-(size / 2.0), 0);
            glVertex2d(size / 2.0, 0);
            // Vertical stroke
            glVertex2d(0, -(size / 2.0));
            glVertex2d(0, size / 2.0);
        }
        glEnd();

        // Enable depth
        glEnable(GL_DEPTH_TEST);
        glDepthMask(true);
        // Pop off old matrix (restore)
        glPopMatrix();
        // Enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blending
        glRestoreBlend(restore);
        // Disable anti-aliasing
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
    }

    public static void glDrawFilledEllipse(final double x,
                                           final double y,
                                           final double radius,
                                           final int startIndex,
                                           final int endIndex,
                                           final int polygons,
                                           final boolean smooth,
                                           final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();

        if (smooth) {
            // Enable anti-aliasing
            glEnable(GL_POLYGON_SMOOTH);
            glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        }
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);
        // Required because of minecraft optimizations
        glDisable(GL_CULL_FACE);

        // Begin triangle fan
        glBegin(GL_POLYGON);
        {
            // Specify center vertex
            glVertex2d(x, y);

            for (double i = startIndex; i <= endIndex; i++) {
                final double theta = 2.0 * Math.PI * i / polygons;
                // Specify triangle fan vertices in a circle (size=radius) around x & y
                glVertex2d(x + radius * Math.cos(theta), y + radius * Math.sin(theta));
            }
        }
        // Draw the triangle fan
        glEnd();

        // Disable blending
        glRestoreBlend(restore);

        if (smooth) {
            // Disable anti-aliasing
            glDisable(GL_POLYGON_SMOOTH);
            glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
        }
        // See above
        glEnable(GL_CULL_FACE);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawFilledEllipse(final double x,
                                           final double y,
                                           final float radius,
                                           final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Enable anti-aliasing
        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);
        // See the point size aka radius
        glPointSize(radius);

        glBegin(GL_POINTS);
        {
            glVertex2d(x, y);
        }
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Disable anti-aliasing
        glDisable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glScissorBox(final double x, final double y,
                                    final double width, final double height,
                                    final ScaledResolution scaledResolution) {
        if (!glIsEnabled(GL_SCISSOR_TEST))
            glEnable(GL_SCISSOR_TEST);

        final int scaling = scaledResolution.getScaleFactor();

        glScissor((int) (x * scaling),
                (int) ((scaledResolution.getScaledHeight() - (y + height)) * scaling),
                (int) (width * scaling),
                (int) (height * scaling));
    }

    public static void glRestoreScissor() {
        if (!glIsEnabled(GL_SCISSOR_TEST))
            glEnable(GL_SCISSOR_TEST);

        // Restore the last saved scissor box
        glScissor(SCISSOR_BUFFER.get(0), SCISSOR_BUFFER.get(1),
                SCISSOR_BUFFER.get(2), SCISSOR_BUFFER.get(3));
    }

    public static void glEndScissor() {
        glDisable(GL_SCISSOR_TEST);
    }

    public static double[] worldToScreen(final double[] positionVector,
                                         final AxisAlignedBB boundingBox,
                                         final double[] projection,
                                         final double[] projectionBuffer) {
        final double[][] bounds = {
                {boundingBox.minX, boundingBox.minY, boundingBox.minZ},
                {boundingBox.minX, boundingBox.maxY, boundingBox.minZ},
                {boundingBox.minX, boundingBox.maxY, boundingBox.maxZ},
                {boundingBox.minX, boundingBox.minY, boundingBox.maxZ},
                {boundingBox.maxX, boundingBox.minY, boundingBox.minZ},
                {boundingBox.maxX, boundingBox.maxY, boundingBox.minZ},
                {boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ},
                {boundingBox.maxX, boundingBox.minY, boundingBox.maxZ}
        };

        final double[] position;

        // null when chests (don't need pos vector proj. for chests)
        if (positionVector != null) {
            if (!worldToScreen(positionVector, projectionBuffer, projection[2]))
                return null;

            position = new double[]{
                    projection[0], projection[1], // screen max width/height
                    -1.f, -1.f, // negative placeholder values for > comparison
                    projectionBuffer[0], projectionBuffer[1] // player position vector x/y
            };
        } else {
            position = new double[]{
                    projection[0], projection[1], // screen max width/height
                    -1.f, -1.f, // negative placeholder values for > comparison
            };
        }

        for (final double[] vector : bounds) {
            if (worldToScreen(vector, projectionBuffer, projection[2])) {
                final double projected_x = projectionBuffer[0];
                final double projected_y = projectionBuffer[1];

                position[0] = Math.min(position[0], projected_x);
                position[1] = Math.min(position[1], projected_y);
                position[2] = Math.max(position[2], projected_x);
                position[3] = Math.max(position[3], projected_y);
            }
        }

        return position;
    }

    public static boolean worldToScreen(double[] in, double[] out, double scaling) {
        glGetFloat(GL_MODELVIEW_MATRIX, MODEL_MATRIX_BUFFER);
        glGetFloat(GL_PROJECTION_MATRIX, PROJECTION_MATRIX_BUFFER);
        glGetInteger(GL_VIEWPORT, VIEWPORT_BUFFER);

        if (GLU.gluProject((float) in[0], (float) in[1], (float) in[2],
                MODEL_MATRIX_BUFFER, PROJECTION_MATRIX_BUFFER,
                VIEWPORT_BUFFER, WND_POS_BUFFER)) {
            final float zCoordinate = WND_POS_BUFFER.get(2);
            // Check z coordinate is within bounds 0-<1.0
            if (zCoordinate < 0.0F || zCoordinate > 1.0F) return false;

            out[0] = WND_POS_BUFFER.get(0) / scaling; // window pos (x) / scaled resolution scale (normal = 2)
            // GL handles the 'y' window coordinate inverted to Minecraft
            // subtract window pos y from bottom of screen and divide by scaled res scale
            out[1] = (Display.getHeight() - WND_POS_BUFFER.get(1)) / scaling;
            return true;
        }

        return false;
    }

    public static void glColour(final int color) {
        glColor4ub((byte) (color >> 16 & 0xFF),
                (byte) (color >> 8 & 0xFF),
                (byte) (color & 0xFF),
                (byte) (color >> 24 & 0xFF));
    }

    public static void glDrawGradientLine(final double x,
                                          final double y,
                                          final double x1,
                                          final double y1,
                                          final float lineWidth,
                                          final int colour) {
        // Enable blending (required for anti-aliasing)
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set line width
        glLineWidth(lineWidth);
        // Enable line anti-aliasing
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

        glShadeModel(GL_SMOOTH);

        final int noAlpha = removeAlphaComponent(colour);

        glDisable(GL_ALPHA_TEST);

        // Begin line
        glBegin(GL_LINE_STRIP);
        {
            // Start
            glColour(noAlpha);
            glVertex2d(x, y);
            // Middle
            final double dif = x1 - x;

            glColour(colour);
            glVertex2d(x + dif * 0.4, y);

            glVertex2d(x + dif * 0.6, y);
            // End
            glColour(noAlpha);
            glVertex2d(x1, y1);
        }
        // Draw the line
        glEnd();

        glEnable(GL_ALPHA_TEST);

        glShadeModel(GL_FLAT);

        // Restore blend
        glRestoreBlend(restore);
        // Disable line anti-aliasing
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawLine(final double x,
                                  final double y,
                                  final double x1,
                                  final double y1,
                                  final float lineWidth,
                                  final boolean smoothed,
                                  final int colour) {
        // Enable blending (required for anti-aliasing)
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set line width
        glLineWidth(lineWidth);

        if (smoothed) {
            // Enable line anti-aliasing
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        }

        glColour(colour);

        // Begin line
        glBegin(GL_LINES);
        {
            // Start
            glVertex2d(x, y);
            // End
            glVertex2d(x1, y1);
        }
        // Draw the line
        glEnd();

        // Restore blend
        glRestoreBlend(restore);
        if (smoothed) {
            // Disable line anti-aliasing
            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        }
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawPlayerFace(final double x,
                                        final double y,
                                        final double width,
                                        final double height,
                                        final ResourceLocation skinLocation) {
        // Bind skin texture
        Minecraft.getMinecraft().getTextureManager().bindTexture(skinLocation);
        // Colour solid
        glColor4f(1, 1, 1, 1);
        final float eightPixelOff = 1.0F / 8;

        glBegin(GL_QUADS);
        {
            glTexCoord2f(eightPixelOff, eightPixelOff);
            glVertex2d(x, y);

            glTexCoord2f(eightPixelOff, eightPixelOff * 2);
            glVertex2d(x, y + height);

            glTexCoord2f(eightPixelOff * 2, eightPixelOff * 2);
            glVertex2d(x + width, y + height);

            glTexCoord2f(eightPixelOff * 2, eightPixelOff);
            glVertex2d(x + width, y);
        }
        glEnd();
    }

    public static void glDrawSidewaysGradientRect(final double x,
                                                  final double y,
                                                  final double width,
                                                  final double height,
                                                  final int startColour,
                                                  final int endColour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable vertex colour changing
        glShadeModel(GL_SMOOTH);

        // Begin rect
        glBegin(GL_QUADS);
        {
            // Start fade
            glColour(startColour);
            glVertex2d(x, y);
            glVertex2d(x, y + height);
            // End fade
            glColour(endColour);
            glVertex2d(x + width, y + height);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();

        // Restore shade model
        glShadeModel(GL_FLAT);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blending
        glRestoreBlend(restore);
    }

    public static void glDrawFilledRect(final double x,
                                        final double y,
                                        final double x1,
                                        final double y1,
                                        final int startColour,
                                        final int endColour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Enable vertex colour changing
        glShadeModel(GL_SMOOTH);

        // Begin rect
        glBegin(GL_QUADS);
        {
            // Start fade
            glColour(startColour);
            glVertex2d(x, y);
            glColour(endColour);
            glVertex2d(x, y1);
            // End fade
            glVertex2d(x1, y1);
            glColour(startColour);
            glVertex2d(x1, y);
        }
        // Draw the rect
        glEnd();

        // Restore shade model
        glShadeModel(GL_FLAT);

        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blending
        glRestoreBlend(restore);
    }

    public static void glDrawOutlinedQuad(final double x,
                                          final double y,
                                          final double width,
                                          final double height,
                                          final float thickness,
                                          final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);

        glLineWidth(thickness);

        // Begin rect
        glBegin(GL_LINE_LOOP);
        {
            glVertex2d(x, y);
            glVertex2d(x, y + height);
            glVertex2d(x + width, y + height);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();

        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blend
        glRestoreBlend(restore);
    }

    public static void drawHollowRoundedRect(double x,
                                             double y,
                                             double width,
                                             double height,
                                             double cornerRadius,
                                             boolean smoothed,
                                             Color color) {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_BLEND);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255.0F, color.getAlpha() / 255F);
        glLineWidth(1.0f);
        glBegin(GL_LINE_LOOP);
        double cornerX = x + width - cornerRadius;
        double cornerY = y + height - cornerRadius;
        for (int i = 0; i <= 90; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + width - cornerRadius;
        cornerY = y + cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 90; i <= 180; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + cornerRadius;
        cornerY = y + cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 180; i <= 270; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        cornerX = x + cornerRadius;
        cornerY = y + height - cornerRadius;
        glBegin(GL_LINE_LOOP);
        for (int i = 270; i <= 360; i += 30)
            glVertex2d(cornerX + Math.sin(i * Math.PI / 180.0D) * cornerRadius, cornerY + Math.cos(i * Math.PI / 180.0D) * cornerRadius);
        glEnd();
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glDrawLine(x + cornerRadius, y, x + width - cornerRadius, y, 1.0f, smoothed, color.getRGB());
        glDrawLine(x + cornerRadius, y + height, x + width - cornerRadius, y + height, 1.0f, smoothed, color.getRGB());
        glDrawLine(x, y + cornerRadius, x, y + height - cornerRadius, 1.0f, smoothed, color.getRGB());
        glDrawLine(x + width, y + cornerRadius, x + width, y + height - cornerRadius, 1.0f, smoothed, color.getRGB());
    }

    public static void glDrawOutlinedQuadGradient(final double x,
                                                  final double y,
                                                  final double width,
                                                  final double height,
                                                  final float thickness,
                                                  final int colour, final int secondaryColour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);

        glLineWidth(thickness);

        // Begin rect
        glShadeModel(GL_SMOOTH);
        glBegin(GL_LINE_LOOP);
        {
            // Set color
            glColour(colour);
            glVertex2d(x, y);
            glVertex2d(x, y + height);
            // Set second color
            glColour(secondaryColour);
            glVertex2d(x + width, y + height);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();
        glShadeModel(GL_FLAT);

        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
        // Disable blend
        glRestoreBlend(restore);
    }





    public static void glDrawFilledQuad(final double x,
                                        final double y,
                                        final double width,
                                        final double height,
                                        final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);

        // Begin rect
        glBegin(GL_QUADS);
        {
            glVertex2d(x, y);
            glVertex2d(x, y + height);
            glVertex2d(x + width, y + height);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawFilledQuad(final double x,
                                        final double y,
                                        final double width,
                                        final double height,
                                        final int startColour,
                                        final int endColour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);

        glShadeModel(GL_SMOOTH);

        // Begin rect
        glBegin(GL_QUADS);
        {
            glColour(startColour);
            glVertex2d(x, y);

            glColour(endColour);
            glVertex2d(x, y + height);
            glVertex2d(x + width, y + height);

            glColour(startColour);
            glVertex2d(x + width, y);
        }
        // Draw the rect
        glEnd();

        glShadeModel(GL_FLAT);

        // Disable blending
        glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawFilledRect(final double x,
                                        final double y,
                                        final double x1,
                                        final double y1,
                                        final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);

        // Begin rect
        glBegin(GL_QUADS);
        {
            glVertex2d(x, y);
            glVertex2d(x, y1);
            glVertex2d(x1, y1);
            glVertex2d(x1, y);
        }
        // Draw the rect
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawArcFilled(final double x,
                                       final double y,
                                       final float radius,
                                       final float angleStart,
                                       final float angleEnd,
                                       final int segments,
                                       final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);
        // Required because of minecraft optimizations
        glDisable(GL_CULL_FACE);
        // Translate to centre of arc
        glTranslated(x, y, 0);
        // Begin triangle fan
        glBegin(GL_POLYGON);
        {
            // Specify center vertex
            glVertex2f(0.f, 0.f);

            final float[][] vertices = getArcVertices(radius, angleStart, angleEnd, segments);

            for (float[] vertex : vertices) {
                // Specify triangle fan vertices in a circle (size=radius) around x & y
                glVertex2f(vertex[0], vertex[1]);
            }
        }
        // Draw the triangle fan
        glEnd();
        // Restore matrix
        glTranslated(-x, -y, 0);
        // Disable blending
        glRestoreBlend(restore);
        // See above
        glEnable(GL_CULL_FACE);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static float[][] getArcVertices(final float radius,
                                           final float angleStart,
                                           final float angleEnd,
                                           final int segments) {
        final float range = Math.max(angleStart, angleEnd) - Math.min(angleStart, angleEnd);
        final int nSegments = Math.max(2, Math.round((360.f / range) * segments));
        final float segDeg = range / nSegments;

        final float[][] vertices = new float[nSegments + 1][2];
        for (int i = 0; i <= nSegments; i++) {
            final float angleOfVert = (angleStart + i * segDeg) / 180.f * (float) Math.PI;
            vertices[i][0] = ((float) Math.sin(angleOfVert)) * radius;
            vertices[i][1] = ((float) -Math.cos(angleOfVert)) * radius;
        }

        return vertices;
    }


    public static void glDrawArcOutline(final double x,
                                        final double y,
                                        final float radius,
                                        final float angleStart,
                                        final float angleEnd,
                                        final float lineWidth,
                                        final int colour) {
        // Derive segments from size
        final int segments = (int) (radius * 4);
        // Enable blending
        final boolean restore = glEnableBlend();
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set the width of the line
        glLineWidth(lineWidth);
        // Set color
        glColour(colour);
        // Enable triangle smoothing
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        // Translate to centre of arc
        glTranslated(x, y, 0);
        // Begin triangle fan
        glBegin(GL_LINE_STRIP);
        {
            final float[][] vertices = getArcVertices(radius, angleStart, angleEnd, segments);

            for (float[] vertex : vertices) {
                // Specify triangle fan vertices in a circle (size=radius) around x & y
                glVertex2f(vertex[0], vertex[1]);
            }
        }
        // Draw the triangle fan
        glEnd();
        // Restore matrix
        glTranslated(-x, -y, 0);
        // Disable triangle smoothing
        glDisable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        // Disable blending
        glRestoreBlend(restore);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }


    public static void glDrawPoint(final double x,
                                   final double y,
                                   final float radius,
                                   final ScaledResolution scaledResolution,
                                   final int colour) {
        // Enable blending
        final boolean restore = glEnableBlend();
        // Enable anti-aliasing
        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set color
        glColour(colour);
        // See the point size aka radius
        glPointSize(radius * glGetFloat(GL_MODELVIEW_MATRIX) * scaledResolution.getScaleFactor());

        glBegin(GL_POINTS);
        {
            glVertex2d(x, y);
        }
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Disable anti-aliasing
        glDisable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static void glDrawRoundedOutline(final double x,
                                            final double y,
                                            final double width,
                                            final double height,
                                            final float lineWidth,
                                            final RoundingMode1 roundingMode,
                                            final float rounding,
                                            final int colour) {
        boolean bLeft = false;
        boolean tLeft = false;
        boolean bRight = false;
        boolean tRight = false;

        switch (roundingMode) {
            case TOP:
                tLeft = true;
                tRight = true;
                break;
            case BOTTOM:
                bLeft = true;
                bRight = true;
                break;
            case FULL:
                tLeft = true;
                tRight = true;
                bLeft = true;
                bRight = true;
                break;
            case LEFT:
                bLeft = true;
                tLeft = true;
                break;
            case RIGHT:
                bRight = true;
                tRight = true;
                break;
            case TOP_LEFT:
                tLeft = true;
                break;
            case TOP_RIGHT:
                tRight = true;
                break;
            case BOTTOM_LEFT:
                bLeft = true;
                break;
            case BOTTOM_RIGHT:
                bRight = true;
                break;
        }

        // Translate matrix to top-left of rect
        glTranslated(x, y, 0);
        // Enable blending
        final boolean restore = glEnableBlend();

        if (tLeft) {
            // Top left
            glDrawArcOutline(rounding, rounding, rounding,
                    270.f, 360.f, lineWidth, colour);
        }

        if (tRight) {
            // Top right
            glDrawArcOutline(width - rounding, rounding, rounding,
                    0.f, 90.f, lineWidth, colour);
        }

        if (bLeft) {
            // Bottom left
            glDrawArcOutline(rounding, height - rounding, rounding,
                    180, 270, lineWidth, colour);
        }

        if (bRight) {
            // Bottom right
            glDrawArcOutline(width - rounding, height - rounding, rounding,
                    90, 180, lineWidth, colour);
        }

        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set colour
        glColour(colour);

        // Begin polygon
        glBegin(GL_LINES);
        {
            if (tLeft) {
                glVertex2d(0.0, rounding);
            } else {
                glVertex2d(0.0, 0.0);
            }

            if (bLeft) {
                glVertex2d(0, height - rounding);
                glVertex2d(rounding, height);
            } else {
                glVertex2d(0.0, height);
                glVertex2d(0.0, height);
            }

            if (bRight) {
                glVertex2d(width - rounding, height);
                glVertex2d(width, height - rounding);
            } else {
                glVertex2d(width, height);
                glVertex2d(width, height);
            }

            if (tRight) {
                glVertex2d(width, rounding);
                glVertex2d(width - rounding, 0.0);
            } else {
                glVertex2d(width, 0.0);
                glVertex2d(width, 0.0);
            }

            if (tLeft) {
                glVertex2d(rounding, 0.0);
            } else {
                glVertex2d(0.0, 0.0);
            }
        }
        // Draw polygon
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Translate matrix back (instead of creating a new matrix with glPush/glPop)
        glTranslated(-x, -y, 0);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    // TODO :: Do this shader (its not hard)

    private static final String CIRCLE_FRAG_SHADER =
            "#version 120\n" +
                    "\n" +
                    "uniform float innerRadius;\n" +
                    "uniform vec4 colour;\n" +
                    "\n" +
                    "void main() {\n" +
                    "   vec2 pixel = gl_TexCoord[0].st;\n" +
                    "   vec2 centre = vec2(0.5, 0.5);\n" +
                    "   float d = length(pixel - centre);\n" +
                    "   float c = smoothstep(d+innerRadius, d+innerRadius+0.01, 0.5-innerRadius);\n" +
                    "   float a = smoothstep(0.0, 1.0, c) * colour.a;\n" +
                    "   gl_FragColor = vec4(colour.rgb, a);\n" +
                    "}\n";

    public static final String VERTEX_SHADER =
            "#version 120 \n" +
                    "\n" +
                    "void main() {\n" +
                    "    gl_TexCoord[0] = gl_MultiTexCoord0;\n" +
                    "    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;\n" +
                    "}";




    private static final String ROUNDED_QUAD_FRAG_SHADER =
            "#version 120\n" +
                    "uniform float width;\n" +
                    "uniform float height;\n" +
                    "uniform float radius;\n" +
                    "uniform vec4 colour;\n" +
                    "\n" +
                    "float SDRoundedRect(vec2 p, vec2 b, float r) {\n" +
                    "    vec2 q = abs(p) - b + r;\n" +
                    "    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r;\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 size = vec2(width, height);\n" +
                    "    vec2 pixel = gl_TexCoord[0].st * size;\n" +
                    "    vec2 centre = 0.5 * size;\n" +
                    "    float b = SDRoundedRect(pixel - centre, centre, radius);\n" +
                    "    float a = 1.0 - smoothstep(0, 1.0, b);\n" +
                    "    gl_FragColor = vec4(colour.rgb, colour.a * a);\n" +
                    "}";



    private static final String RAINBOW_FRAG_SHADER =
            "#version 120\n" +
                    "uniform float width;\n" +
                    "uniform float height;\n" +
                    "uniform float radius;\n" +
                    "uniform float u_time;\n" +
                    "\n" +
                    "float SDRoundedRect(vec2 p, vec2 b, float r) {\n" +
                    "    vec2 q = abs(p) - b + r;\n" +
                    "    return min(max(q.x, q.y), 0.0) + length(max(q, 0.0)) - r;\n" +
                    "}\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 size = vec2(width, height);\n" +
                    "    vec2 pixel = gl_TexCoord[0].st * size;\n" +
                    "    vec2 centre = 0.5 * size;\n" +
                    "    float b = SDRoundedRect(pixel - centre, centre, radius);\n" +
                    "    float a = 1.0 - smoothstep(0, 1.0, b);\n" +
                    "    vec3 colour = 0.5 + 0.5*cos(u_time+gl_TexCoord[0].st.x+vec3(0,2,4));\n" +
                    "    gl_FragColor = vec4(colour, a);\n" +
                    "}";



    public static void glDrawRoundedRect(final double x,
                                         final double y,
                                         final double width,
                                         final double height,
                                         final RoundingMode1 roundingMode,
                                         final float rounding,
                                         final float scaleFactor,
                                         final int colour) {
        boolean bLeft = false;
        boolean tLeft = false;
        boolean bRight = false;
        boolean tRight = false;

        switch (roundingMode) {
            case TOP:
                tLeft = true;
                tRight = true;
                break;
            case BOTTOM:
                bLeft = true;
                bRight = true;
                break;
            case FULL:
                tLeft = true;
                tRight = true;
                bLeft = true;
                bRight = true;
                break;
            case LEFT:
                bLeft = true;
                tLeft = true;
                break;
            case RIGHT:
                bRight = true;
                tRight = true;
                break;
            case TOP_LEFT:
                tLeft = true;
                break;
            case TOP_RIGHT:
                tRight = true;
                break;
            case BOTTOM_LEFT:
                bLeft = true;
                break;
            case BOTTOM_RIGHT:
                bRight = true;
                break;
        }

        final float alpha = (colour >> 24 & 0xFF) / 255.f;

        // Enable blending
        final boolean restore = glEnableBlend();

        // Set colour
        glColour(colour);

        // Translate matrix to top-left of rect
        glTranslated(x, y, 0);
        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);

        // Begin polygon
        glBegin(GL_POLYGON);
        {
            if (tLeft) {
                glVertex2d(rounding, rounding);
                glVertex2d(0, rounding);
            } else {
                glVertex2d(0, 0);
            }

            if (bLeft) {
                glVertex2d(0, height - rounding);
                glVertex2d(rounding, height - rounding);
                glVertex2d(rounding, height);
            } else {
                glVertex2d(0, height);
            }

            if (bRight) {
                glVertex2d(width - rounding, height);
                glVertex2d(width - rounding, height - rounding);
                glVertex2d(width, height - rounding);
            } else {
                glVertex2d(width, height);
            }

            if (tRight) {
                glVertex2d(width, rounding);
                glVertex2d(width - rounding, rounding);
                glVertex2d(width - rounding, 0);
            } else {
                glVertex2d(width, 0);
            }

            if (tLeft) {
                glVertex2d(rounding, 0);
            }
        }
        // Draw polygon
        glEnd();

        // Enable anti-aliasing
        glEnable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);

        // Set point size
        glPointSize(rounding * 2.f * glGetFloat(GL_MODELVIEW_MATRIX) * scaleFactor);

        glBegin(GL_POINTS);
        {
            if (tLeft) {
                // Top left
                glVertex2d(rounding, rounding);
            }

            if (tRight) {
                // Top right
                glVertex2d(width - rounding, rounding);
            }

            if (bLeft) {
                // Bottom left
                glVertex2d(rounding, height - rounding);
            }

            if (bRight) {
                // Bottom right
                glVertex2d(width - rounding, height - rounding);
            }
        }
        glEnd();

        // Disable anti-aliasing
        glDisable(GL_POINT_SMOOTH);
        glHint(GL_POINT_SMOOTH_HINT, GL_DONT_CARE);
        // Disable blending
        glRestoreBlend(restore);
        // Translate matrix back (instead of creating a new matrix with glPush/glPop)
        glTranslated(-x, -y, 0);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }


    public static void glDrawRoundedRectEllipse(final double x,
                                                final double y,
                                                final double width,
                                                final double height,
                                                final RoundingMode1 roundingMode,
                                                final int roundingDef,
                                                final double roundingLevel,
                                                final int colour) {
        boolean bLeft = false;
        boolean tLeft = false;
        boolean bRight = false;
        boolean tRight = false;

        switch (roundingMode) {
            case TOP:
                tLeft = true;
                tRight = true;
                break;
            case BOTTOM:
                bLeft = true;
                bRight = true;
                break;
            case FULL:
                tLeft = true;
                tRight = true;
                bLeft = true;
                bRight = true;
                break;
            case LEFT:
                bLeft = true;
                tLeft = true;
                break;
            case RIGHT:
                bRight = true;
                tRight = true;
                break;
            case TOP_LEFT:
                tLeft = true;
                break;
            case TOP_RIGHT:
                tRight = true;
                break;
            case BOTTOM_LEFT:
                bLeft = true;
                break;
            case BOTTOM_RIGHT:
                bRight = true;
                break;
        }

        // Translate matrix to top-left of rect
        glTranslated(x, y, 0);
        // Enable triangle anti-aliasing
        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        // Enable blending
        final boolean restore = glEnableBlend();

        if (tLeft) {
            // Top left
            glDrawFilledEllipse(roundingLevel, roundingLevel, roundingLevel,
                    (int) (roundingDef * 0.5), (int) (roundingDef * 0.75),
                    roundingDef, false, colour);
        }

        if (tRight) {
            // Top right
            glDrawFilledEllipse(width - roundingLevel, roundingLevel, roundingLevel,
                    (int) (roundingDef * 0.75), roundingDef,
                    roundingDef, false, colour);
        }

        if (bLeft) {
            // Bottom left
            glDrawFilledEllipse(roundingLevel, height - roundingLevel, roundingLevel,
                    (int) (roundingDef * 0.25), (int) (roundingDef * 0.5),
                    roundingDef, false, colour);
        }

        if (bRight) {
            // Bottom right
            glDrawFilledEllipse(width - roundingLevel, height - roundingLevel, roundingLevel,
                    0, (int) (roundingDef * 0.25),
                    roundingDef, false, colour);
        }

        // Enable triangle anti-aliasing (to save performance on next poly draw)
        glDisable(GL_POLYGON_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);

        // Disable texture drawing
        glDisable(GL_TEXTURE_2D);
        // Set colour
        glColour(colour);

        // Begin polygon
        glBegin(GL_POLYGON);
        {
            if (tLeft) {
                glVertex2d(roundingLevel, roundingLevel);
                glVertex2d(0, roundingLevel);
            } else {
                glVertex2d(0, 0);
            }

            if (bLeft) {
                glVertex2d(0, height - roundingLevel);
                glVertex2d(roundingLevel, height - roundingLevel);
                glVertex2d(roundingLevel, height);
            } else {
                glVertex2d(0, height);
            }

            if (bRight) {
                glVertex2d(width - roundingLevel, height);
                glVertex2d(width - roundingLevel, height - roundingLevel);
                glVertex2d(width, height - roundingLevel);
            } else {
                glVertex2d(width, height);
            }

            if (tRight) {
                glVertex2d(width, roundingLevel);
                glVertex2d(width - roundingLevel, roundingLevel);
                glVertex2d(width - roundingLevel, 0);
            } else {
                glVertex2d(width, 0);
            }

            if (tLeft) {
                glVertex2d(roundingLevel, 0);
            }
        }
        // Draw polygon
        glEnd();

        // Disable blending
        glRestoreBlend(restore);
        // Translate matrix back (instead of creating a new matrix with glPush/glPop)
        glTranslated(-x, -y, 0);
        // Re-enable texture drawing
        glEnable(GL_TEXTURE_2D);
    }

    public static boolean glEnableBlend() {
        final boolean wasEnabled = glIsEnabled(GL_BLEND);

        if (!wasEnabled) {
            glEnable(GL_BLEND);
            glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        }

        return wasEnabled;
    }





    public static void glRestoreBlend(final boolean wasEnabled) {
        if (!wasEnabled) {
            glDisable(GL_BLEND);
        }
    }

    public static float interpolate(final float old, final float now, final float progress) {
        return old + (now - old) * progress;
    }

    public static double interpolate(final double old, final double now, final double progress) {
        return old + (now - old) * progress;
    }

    public static Vec3 interpolate(final Vec3 old, final Vec3 now, final double progress) {
        final Vec3 difVec = now.subtract(old);
        return new Vec3(old.xCoord + difVec.xCoord * progress,
                old.yCoord + difVec.yCoord * progress,
                old.zCoord + difVec.zCoord * progress);
    }

    public static double[] interpolate(final Entity entity, final float partialTicks) {
        return new double[]{
                interpolate(entity.prevPosX, entity.posX, partialTicks),
                interpolate(entity.prevPosY, entity.posY, partialTicks),
                interpolate(entity.prevPosZ, entity.posZ, partialTicks),
        };
    }

    public static AxisAlignedBB interpolate(final Entity entity,
                                            final AxisAlignedBB boundingBox,
                                            final float partialTicks) {
        final float invertedPT = 1.0f - partialTicks;
        return boundingBox.offset(
                (entity.posX - entity.prevPosX) * -invertedPT,
                (entity.posY - entity.prevPosY) * -invertedPT,
                (entity.posZ - entity.prevPosZ) * -invertedPT
        );
    }

    public static void glDrawBoundingBox(final AxisAlignedBB bb,
                                         final float lineWidth,
                                         final boolean filled) {
        if (filled) {
            // 4 sides
            glBegin(GL_QUAD_STRIP);
            {
                glVertex3d(bb.minX, bb.minY, bb.minZ);
                glVertex3d(bb.minX, bb.maxY, bb.minZ);

                glVertex3d(bb.maxX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.minZ);

                glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

                glVertex3d(bb.minX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.maxZ);

                glVertex3d(bb.minX, bb.minY, bb.minZ);
                glVertex3d(bb.minX, bb.maxY, bb.minZ);
            }
            glEnd();

            // Bottom
            glBegin(GL_QUADS);
            {
                glVertex3d(bb.minX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.minY, bb.maxZ);
            }
            glEnd();

            glCullFace(GL_FRONT);

            // Top
            glBegin(GL_QUADS);
            {
                glVertex3d(bb.minX, bb.maxY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            }
            glEnd();

            glCullFace(GL_BACK);
        }


        if (lineWidth > 0) {
            glLineWidth(lineWidth);

            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

            glBegin(GL_LINE_STRIP);
            {
                // Bottom
                glVertex3d(bb.minX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.minY, bb.minZ);

                // Top
                glVertex3d(bb.minX, bb.maxY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.minZ);
            }
            glEnd();

            glBegin(GL_LINES);
            {
                glVertex3d(bb.maxX, bb.minY, bb.minZ);
                glVertex3d(bb.maxX, bb.maxY, bb.minZ);

                glVertex3d(bb.maxX, bb.minY, bb.maxZ);
                glVertex3d(bb.maxX, bb.maxY, bb.maxZ);

                glVertex3d(bb.minX, bb.minY, bb.maxZ);
                glVertex3d(bb.minX, bb.maxY, bb.maxZ);
            }
            glEnd();

            glDisable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        }
    }

    public static void blockESPBox(double x, double y, double z, float width, float v, int red, int green, int blue, float v1) {
    }

    public static void drawSolidBlockESP(double x, double y, double z, float width, float v, int red, int green, int blue, float v1) {
    }


    public enum RoundingMode {
        TOP_LEFT,
        BOTTOM_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,

        LEFT,
        RIGHT,

        TOP,
        BOTTOM,

        FULL
    }

    public static void doGlScissor(float x, float y, float windowWidth2, float windowHeight2) {
        int scaleFactor = 1;
        float k = mc.gameSettings.guiScale;
        if (k == 0) {
            k = 1000;
        }
        while (scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320
                && mc.displayHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }
        GL11.glScissor((int) (x * scaleFactor), (int) (mc.displayHeight - (y + windowHeight2) * scaleFactor),
                (int) (windowWidth2 * scaleFactor), (int) (windowHeight2 * scaleFactor));
    }


    public static void arc(double x, double y, double start, double end, float radius, int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arc(float x, float y, float start, float end, float radius, int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static int getColorFromPercentage(float current, float max) {
        float percentage = (current / max) / 3;
        return Color.HSBtoRGB(percentage, 1.0F, 1.0F);
    }
    public static double getProtection(EntityLivingBase target) {
        double protection = 0.0;
        for (int i = 0; i <= 3; ++i) {
            ItemStack stack = target.getCurrentArmor(i);
            if (stack == null) continue;
            if (stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor)stack.getItem();
                protection += (double)armor.damageReduceAmount;
            }
            protection += (double)EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack) * 0.25;
        }
        return protection;
    }

    public static double getWeaponStrength(ItemStack stack) {
        double damage = 0.0;
        if (stack != null) {
            if (stack.getItem() instanceof ItemSword) {
                ItemSword sword = (ItemSword)stack.getItem();
                damage += (double)sword.getDamageVsEntity();
            }
            if (stack.getItem() instanceof ItemTool) {
                ItemTool tool = (ItemTool)stack.getItem();
                damage += (double)tool.getToolMaterial().getDamageVsEntity();
            }
            damage += (double) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25;
        }
        return damage;
    }







    public static void drawStack(boolean renderOverlay, ItemStack stack, float x, float y) {
        GL11.glPushMatrix();

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.theWorld != null) {
            RenderHelper.enableGUIStandardItemLighting();
        }

        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();

        mc.getRenderItem().zLevel = -150.0F;
        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) x, (int) y);

        if (renderOverlay) {
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, (int) x, (int) y, String.valueOf(stack.stackSize));
        }

        mc.getRenderItem().zLevel = 0.0F;

        GlStateManager.enableBlend();
        final float z = 0.5F;

        GlStateManager.scale(z, z, z);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();

        GL11.glPopMatrix();
    }

    public static void blockESPBox(BlockPos blockPos) {

        double x =
                blockPos.getX()
                        - Minecraft.getMinecraft().getRenderManager().renderPosX;
        double y =
                blockPos.getY()
                        - Minecraft.getMinecraft().getRenderManager().renderPosY;
        double z =
                blockPos.getZ()
                        - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glColor4d(0, 1, 0, 0.15F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        //drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glColor4d(0, 0, 1, 0.5F);
        RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z,
                x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void drawEsp(EntityLivingBase ent, float pTicks, int hexColor, int hexColorIn) {
        if (!ent.isEntityAlive()) {
            return;
        }
        double x = RenderUtil.getDiff(ent.lastTickPosX, ent.posX, pTicks, RenderManager.renderPosX);
        double y = RenderUtil.getDiff(ent.lastTickPosY, ent.posY, pTicks, RenderManager.renderPosY);
        double z = RenderUtil.getDiff(ent.lastTickPosZ, ent.posZ, pTicks, RenderManager.renderPosZ);
        RenderUtil.boundingBox((Entity) ent, x, y, z, hexColor, hexColorIn);
    }

    public static void boundingBox(Entity entity, double x, double y, double z, int color, int colorIn) {
        GlStateManager.pushMatrix();
        GL11.glLineWidth((float) 1.0f);
        AxisAlignedBB var11 = entity.getEntityBoundingBox();
        AxisAlignedBB var12 = new AxisAlignedBB(var11.minX - entity.posX + x, var11.minY - entity.posY + y, var11.minZ - entity.posZ + z, var11.maxX - entity.posX + x, var11.maxY - entity.posY + y, var11.maxZ - entity.posZ + z);
        if (color != 0) {
            GlStateManager.disableDepth();
            RenderUtil.filledBox(var12, colorIn);
            RenderUtil.disableLighting();
            drawOutlinedBoundingBox((AxisAlignedBB) var12, (int) color);
        }

        GlStateManager.popMatrix();
    }

    private static double getDiff(double lastI, double i, float ticks, double ownI) {
        return lastI + (i - lastI) * (double) ticks - ownI;
    }

    public static void disableLighting() {
        OpenGlHelper.setActiveTexture((int) OpenGlHelper.lightmapTexUnit);
        GL11.glDisable((int) 3553);
        OpenGlHelper.setActiveTexture((int) OpenGlHelper.defaultTexUnit);
        GL11.glEnable((int) 3042);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glEnable((int) 2848);
        GL11.glDisable((int) 2896);
        GL11.glDisable((int) 3553);
    }

    public static void filledBox(AxisAlignedBB bb, int color) {
        float var11 = (float) (color >> 24 & 255) / 255.0f;
        float var6 = (float) (color >> 16 & 255) / 255.0f;
        float var7 = (float) (color >> 8 & 255) / 255.0f;
        float var8 = (float) (color & 255) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(bb.minX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.minX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.minZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.maxY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        worldRenderer.pos(bb.maxX, bb.minY, bb.maxZ).color(var6, var7, var8, var11).endVertex();
        tessellator.draw();

    }


    public static void arcEllipse(float x, float y, float start, float end, float w, float h, int color) {
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);

        float temp;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float R = (float) (color >> 16 & 255) / 255.0F;
        float G = (float) (color >> 8 & 255) / 255.0F;
        float B = (float) (color & 255) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(R, G, B, alpha);
        float i;
        float ldx;
        float ldy;
        if (alpha > 0.5F) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(2);
            GL11.glBegin(3);

            for (i = end; i >= start; i -= 4.0F) {
                ldx = (float) Math.cos((double) i * Math.PI / 180.0D) * w * 1.001F;
                ldy = (float) Math.sin((double) i * Math.PI / 180.0D) * h * 1.001F;
                GL11.glVertex2f(x + ldx, y + ldy);
            }

            GL11.glEnd();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        GL11.glBegin(6);

        for (i = end; i >= start; i -= 4.0F) {
            ldx = (float) Math.cos((double) i * Math.PI / 180.0D) * w;
            ldy = (float) Math.sin((double) i * Math.PI / 180.0D) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }

        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void line(Vec firstPoint, Vec secondPoint, int color) {
        line(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY(), color);
    }

    private static void line(double x, double y, double x1, double y1, int color) {
        GL11.glPushMatrix();
        boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean texture2D = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        boolean lineSmooth = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1.5F);
        GlStateManager.color((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, (float)(color >> 24 & 255) / 255.0F);
        GL11.glBegin(1);
        GL11.glVertex2f((float)x1, (float)y1);
        GL11.glVertex2f((float)x, (float)y);
        GL11.glEnd();
        if (!lineSmooth) {
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        if (texture2D) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        if (!blend) {
            GL11.glDisable(GL11.GL_BLEND);
        }

        GL11.glPopMatrix();
    }


    public static void arcEllipse(double x, double y, double start, double end, double w, double h, int color) {
        GlStateManager.color(0.0F, 0.0F, 0.0F);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.0F);

        float temp;
        if (start > end) {
            temp = (float) end;
            end = start;
            start = temp;
        }
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float R = (float) (color >> 16 & 255) / 255.0F;
        float G = (float) (color >> 8 & 255) / 255.0F;
        float B = (float) (color & 255) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.color(R, G, B, alpha);
        float i;
        float ldx;
        float ldy;
        if (alpha > 0.5F) {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(2);
            GL11.glBegin(3);

            for (i = (float) end; i >= start; i -= 4.0F) {
                ldx = (float) ((float) Math.cos((double) i * Math.PI / 180.0D) * w * 1.001F);
                ldy = (float) ((float) Math.sin((double) i * Math.PI / 180.0D) * h * 1.001F);
                GL11.glVertex2f((float) (x + ldx), (float) (y + ldy));
            }

            GL11.glEnd();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        GL11.glBegin(6);

        for (i = (float) end; i >= start; i -= 4.0F) {
            ldx = (float) ((float) Math.cos((double) i * Math.PI / 180.0D) * w);
            ldy = (float) ((float) Math.sin((double) i * Math.PI / 180.0D) * h);
            GL11.glVertex2f((float) (x + ldx), (float) (y + ldy));
        }

        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static Framebuffer createFramebuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.displayWidth || framebuffer.framebufferHeight != Minecraft.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Minecraft.displayWidth, Minecraft.displayHeight, true);
        }
        return framebuffer;
    }

    public static void drawRect(final double x, final double y, final double width, final double height, final Color color) {
        Gui.drawRect(x, y, x + width, y + height, color.getRGB());
    }

    public static void drawRect(final double x, final double y, final double width, final double height, final int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawGradientRect(final double x, final double y, final double width, final double height, final Color startColor, final Color endColor) {
        Gui.drawGradientRect(x, y, x + width, y + height, startColor.getRGB(), endColor.getRGB());
    }

    public static void drawGradientRect(final int x, final double y, final double width, final double height, final int startColor, final int endColor) {
        Gui.drawGradientRect(x, y, x + width, y + height, startColor, endColor);
    }

    public static void drawRoundedRect(final double x, final double y, final double width, final double height, final double cr, final Color color) {
        Stencil stencil = new Stencil();
        stencil.clear();
        stencil.startLayer();
        stencil.setBuffer(true);
        final double x1 = x + width;
        final double y1 = y + height;
        drawFullCircle((x + cr), (y + cr), cr, color.getRGB());
        drawFullCircle((x + cr), (y1 - cr), cr, color.getRGB());
        drawFullCircle((x1 - cr), (y + cr), cr, color.getRGB());
        drawFullCircle((x1 - cr), (y1 - cr), cr, color.getRGB());
        Gui.drawRect(x, y + cr, x1, y1 - cr, color.getRGB());
        Gui.drawRect(x + cr, y, x1 - cr, y1, color.getRGB());
        stencil.cropInside();
        Gui.drawRect(x, y, x1, y1, color.getRGB());
        stencil.stopLayer();
        stencil.clear();
    }

    public static void drawArrow(float x, float y, boolean up, int hexColor) {
        GL11.glPushMatrix();
        GL11.glScaled(1.3, 1.3, 1.3);

        x /= 1.3;
        y /= 1.3;
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        hexColor(hexColor);
        GL11.glLineWidth(2);

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x, y + (up ? 4 : 0));
        GL11.glVertex2d(x + 3, y + (up ? 0 : 4));
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x + 3, y + (up ? 0 : 4));
        GL11.glVertex2d(x + 6, y + (up ? 4 : 0));
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }

    public static void hexColor(int hexColor) {
        float red = (hexColor >> 16 & 0xFF) / 255.0F;
        float green = (hexColor >> 8 & 0xFF) / 255.0F;
        float blue = (hexColor & 0xFF) / 255.0F;
        float alpha = (hexColor >> 24 & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void drawCheckMark(float x, float y, int width, int color) {
        float f = (color >> 24 & 255) / 255.0f;
        float f1 = (color >> 16 & 255) / 255.0f;
        float f2 = (color >> 8 & 255) / 255.0f;
        float f3 = (color & 255) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(3);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x + width - 6.5, y + 3);
        GL11.glVertex2d(x + width - 11.5, y + 10);
        GL11.glVertex2d(x + width - 13.5, y + 8);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static double getAnimationState(double n, final double n2, final double n3) {
        final float n4 = (float)(RenderUtil.delta * n3);
        if (n < n2) {
            if (n + n4 < n2) {
                n += n4;
            }else {
                n = n2;
            }
        }else if (n - n4 > n2) {
            n -= n4;
        }else {
            n = n2;
        }
        return n;
    }

    public static int getRainbow(int speed, int offset, float s) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, s, 1f).getRGB();

    }

    public static void prepareScissorBox(final double x, final double y, final double width, final double height) {
        int factor = getScaledResolution().getScaleFactor();
        GL11.glScissor((int) (x * (float) factor), (int) (((float) getScaledResolution().getScaledHeight() - (y + height)) * (float) factor), (int) (((x + width) - x) * (float) factor), (int) (((y + height) - y) * (float) factor));
    }


    public static void drawFullCircle(double cx, double cy, double r, final int c) {
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        r *= 2.0D;
        cx *= 2.0D;
        cy *= 2.0D;
        float f = (c >> 24 & 0xFF) / 255.0F;
        float f1 = (c >> 16 & 0xFF) / 255.0F;
        float f2 = (c >> 8 & 0xFF) / 255.0F;
        float f3 = (c & 0xFF) / 255.0F;
        boolean blend = GL11.glIsEnabled(3042);
        boolean texture2d = GL11.glIsEnabled(3553);
        boolean line = GL11.glIsEnabled(2848);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360; i++) {
            double x = Math.sin(i * Math.PI / 180.0D) * r;
            double y = Math.cos(i * Math.PI / 180.0D) * r;
            GL11.glVertex2d(cx + x, cy + y);
        }
        GL11.glEnd();
        f = (c >> 24 & 0xFF) / 255.0F;
        f1 = (c >> 16 & 0xFF) / 255.0F;
        f2 = (c >> 8 & 0xFF) / 255.0F;
        f3 = (c & 0xFF) / 255.0F;
        GL11.glColor4f(f1, f2, f3, f);
        if (!line)
            GL11.glDisable(2848);
        if (texture2d)
            GL11.glEnable(3553);
        if (!blend)
            GL11.glDisable(3042);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        GL11.glPopMatrix();
    }


    public static void drawLoadingCircle(EntityLivingBase entityLivingBase, float x, float y) {
        int rot = (int) ((entityLivingBase.getHealth() / entityLivingBase.getMaxHealth()) * 10);
        drawCircle(x, y, 10, rot, -1);

    }

    public static void drawBordered(double x2, double y2, double width, double height, double length, int innerColor, int outerColor) {
        Gui.drawRect(x2, y2, x2 + width, y2 + height, innerColor);
        Gui.drawRect(x2 - length, y2, x2, y2 + height, outerColor);
        Gui.drawRect(x2 - length, y2 - length, x2 + width, y2, outerColor);
        Gui.drawRect(x2 + width, y2 - length, x2 + width + length, y2 + height + length, outerColor);
        Gui.drawRect(x2 - length, y2 + height, x2 + width, y2 + height + length, outerColor);
    }

    public static void drawBorder(double left, double top, double width, double height, double lineWidth, int color) {
        Gui.drawRect(left, top, left + width, top + lineWidth, color);
        Gui.drawRect(left, top, left + lineWidth, top + height, color);
        Gui.drawRect(left, top + height - lineWidth, left + width, top + height, color);
        Gui.drawRect(left + width - lineWidth, top, left + width, top + height, color);
    }

    public static void drawBorder(int x, int y, int w, int h) {
        Gui.drawRect(x, y, (x + w), (y + h), -16777216);
        Gui.drawRect(x + 0.5D, y + 0.5D, (x + w) - 0.5D, (y + h) - 0.5D, -12829636);
        Gui.drawRect((x + 1), (y + 1), (x + w - 1), (y + h - 1), -14145496);
        Gui.drawRect((x + 2), (y + 2), (x + w - 2), (y + h - 2), -12829636);
        Gui.drawRect(x + 2.5D, y + 2.5D, (x + w) - 2.5D, (y + h) - 2.5D, -15527149);
    }


    public static void drawRect2(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (color >> 24 & 0xFF) / 255.0F;
        float f = (color >> 16 & 0xFF) / 255.0F;
        float f1 = (color >> 8 & 0xFF) / 255.0F;
        float f2 = (color & 0xFF) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static int getRainbow(int speed, int offset) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, 0.75f, 1.0f).getRGB();
    }


    public static void drawOutlinedBoundingBox(AxisAlignedBB axisAlignedBB, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }


    public static void drawEntityOnScreen(int posX, int posY, float scale, EntityLivingBase ent) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.color(255, 255, 255);
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        final RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(1F);
        rendermanager.setRenderShadow(false);
        rendermanager.renderEntityWithPosYaw(ent, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }




    public static void drawBorderedBox(double x, double y, double x2, double y2, Color color, boolean bordered) {
        start2D();
        GL11.glPushMatrix();

        if (bordered) {
            GL11.glLineWidth(2f);

            setColor(new Color(0xff000000));

            GL11.glBegin(GL_LINE_STRIP);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x + (x2 - x), y);
            GL11.glVertex2d(x + (x2 - x), y + (y2 - y));
            GL11.glVertex2d(x, y + (y2 - y));
            GL11.glVertex2d(x, y);
            GL11.glEnd();
        }

        GL11.glLineWidth(1f);
        setColor(color);

        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + (x2 - x), y);
        GL11.glVertex2d(x + (x2 - x), y + (y2 - y));
        GL11.glVertex2d(x, y + (y2 - y));
        GL11.glVertex2d(x, y);
        GL11.glEnd();

        GL11.glPopMatrix();
        stop2D();
    }

    public static void drawBorderedRect(float x, float y, float x2, float y2, float l1, int col1, int col2) {
        drawRect(x, y, x2, y2, col2);

        final float f = (col1 >> 24 & 0xFF) / 255.0F, // @off
                f1 = (col1 >> 16 & 0xFF) / 255.0F,
                f2 = (col1 >> 8 & 0xFF) / 255.0F,
                f3 = (col1 & 0xFF) / 255.0F; // @on

        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();

        enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glColor4f(1, 1, 1, 255);
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
    }


    public static void drawBorderedRect(double x, double y, double x2, double y2, float l1, int col1, int col2) {
        drawRect((float) x, (float) y, (float) x2, (float) y2, col2);

        final float f = (col1 >> 24 & 0xFF) / 255.0F, // @off
                f1 = (col1 >> 16 & 0xFF) / 255.0F,
                f2 = (col1 >> 8 & 0xFF) / 255.0F,
                f3 = (col1 & 0xFF) / 255.0F; // @on

        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        glEnable(2848);

        GL11.glPushMatrix();
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glLineWidth(l1);
        GL11.glBegin(1);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
        GL11.glColor4f(255, 1, 1, 255);
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        float x1 = x + width, // @off
                y1 = y + height;
        final float f = (color >> 24 & 0xFF) / 255.0F,
                f1 = (color >> 16 & 0xFF) / 255.0F,
                f2 = (color >> 8 & 0xFF) / 255.0F,
                f3 = (color & 0xFF) / 255.0F; // @on
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GlStateManager.enableBlend();
        glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glBegin(GL11.GL_POLYGON);
        final double v = Math.PI / 180;

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + MathHelper.sin((float) (i * v)) * (radius * -1), y + radius + MathHelper.cos((float) (i * v)) * (radius * -1));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + MathHelper.sin((float) (i * v)) * (radius * -1), y1 - radius + MathHelper.cos((float) (i * v)) * (radius * -1));
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + MathHelper.sin((float) (i * v)) * radius, y1 - radius + MathHelper.cos((float) (i * v)) * radius);
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + MathHelper.sin((float) (i * v)) * radius, y + radius + MathHelper.cos((float) (i * v)) * radius);
        }

        GL11.glEnd();

        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void drawUnfilledRoundedRect(float x, float y, float width, float height, float radius, int color) {
        float x1 = x + width, // @off
                y1 = y + height;
        final float f = (color >> 24 & 0xFF) / 255.0F,
                f1 = (color >> 16 & 0xFF) / 255.0F,
                f2 = (color >> 8 & 0xFF) / 255.0F,
                f3 = (color & 0xFF) / 255.0F; // @on
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GlStateManager.enableBlend();
        glEnable(GL11.GL_LINE_SMOOTH);

        glBegin(GL_LINE_LOOP);
        final double v = Math.PI / 180;

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + MathHelper.sin((float) (i * v)) * (radius * -1), y + radius + MathHelper.cos((float) (i * v)) * (radius * -1));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + MathHelper.sin((float) (i * v)) * (radius * -1), y1 - radius + MathHelper.cos((float) (i * v)) * (radius * -1));
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + MathHelper.sin((float) (i * v)) * radius, y1 - radius + MathHelper.cos((float) (i * v)) * radius);
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + MathHelper.sin((float) (i * v)) * radius, y + radius + MathHelper.cos((float) (i * v)) * radius);
        }

        GL11.glEnd();

        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_LINE_SMOOTH);
        glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void start2D() {
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
    }

    public static void stop2D() {
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        enableTexture2D();
        GlStateManager.disableBlend();
        glColor4f(1, 1, 1, 1);
    }

    public static void setColor(Color color) {
        float alpha = (color.getRGB() >> 24 & 0xFF) / 255.0F;
        float red = (color.getRGB() >> 16 & 0xFF) / 255.0F;
        float green = (color.getRGB() >> 8 & 0xFF) / 255.0F;
        float blue = (color.getRGB() & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void startDrawing() {
        GL11.glEnable(3042);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(2929);
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(Minecraft.timer.renderPartialTicks, 0);
    }

    public static void stopDrawing() {
        GL11.glDisable(3042);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(2929);
        GlStateManager.disableBlend();
    }

    public static void drawLine(Entity entity, double[] color, double x, double y, double z) {
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        if (color.length >= 4) {
            if (color[3] <= 0.1) return;
            GL11.glColor4d(color[0], color[1], color[2], color[3]);
        } else {
            GL11.glColor3d(color[0], color[1], color[2]);
        }
        GL11.glLineWidth(1.5f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }




    public static void drawSolidBlockESP(double x, double y, double z, int color) {
        double xPos = x - RenderManager.renderPosX, yPos = y - RenderManager.renderPosY, zPos = z - RenderManager.renderPosZ;
        float f = (float) (color >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (color & 0xFF) / 255.0f;
        float f4 = (float) (color >> 24 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(f, f2, f3, f4);
        drawOutlinedBoundingBox(new AxisAlignedBB(xPos, yPos, zPos, xPos + 1.0, yPos + 1.0, zPos + 1.0), (int) color);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawSolidBlockESP(BlockPos pos, int color) {
        double xPos = pos.getX() - RenderManager.renderPosX, yPos = pos.getY() - RenderManager.renderPosY, zPos = pos.getZ() - RenderManager.renderPosZ;
        double height = mc.theWorld.getBlockState(pos).getBlock().getBlockBoundsMaxY() - mc.theWorld.getBlockState(pos).getBlock().getBlockBoundsMinY();
        float f = (float) (color >> 16 & 0xFF) / 255.0f;
        float f2 = (float) (color >> 8 & 0xFF) / 255.0f;
        float f3 = (float) (color & 0xFF) / 255.0f;
        float f4 = (float) (color >> 24 & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(f, f2, f3, f4);
        drawOutlinedBoundingBox(new AxisAlignedBB(xPos, yPos, zPos, xPos + 1.0, yPos + height, zPos + 1.0), (int) color);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glDisable(3042);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(2929);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static Vec3 interpolateRender(EntityPlayer player) {
        float part = Minecraft.timer.renderPartialTicks;
        double interpX = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) part;
        double interpY = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) part;
        double interpZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) part;
        return new Vec3(interpX, interpY, interpZ);
    }

    public static float[] getRGBAs(int rgb) {
        return new float[]{(rgb >> 16 & 255) / 255F, (rgb >> 8 & 255) / 255F, (rgb & 255) / 255F, (rgb >> 24 & 255) / 255F};
    }


    public static void drawRoundedRect(double x, double y, double width, double height, float cornerRadius) {
        final int slices = 10;

        drawFillRectangle(x + cornerRadius, y, width - 2 * cornerRadius, height);
        drawFillRectangle(x, y + cornerRadius, cornerRadius, height - 2 * cornerRadius);
        drawFillRectangle(x + width - cornerRadius, y + cornerRadius, cornerRadius, height - 2 * cornerRadius);

        drawCirclePart(x + cornerRadius, y + cornerRadius, -MathHelper.PI, -MathHelper.PId2, cornerRadius, slices);
        drawCirclePart(x + cornerRadius, y + height - cornerRadius, -MathHelper.PId2, 0.0F, cornerRadius, slices);

        drawCirclePart(x + width - cornerRadius, y + cornerRadius, MathHelper.PId2, MathHelper.PI, cornerRadius, slices);
        drawCirclePart(x + width - cornerRadius, y + height - cornerRadius, 0, MathHelper.PId2, cornerRadius, slices);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GlStateManager.disableBlend();

        //GlStateManager.enableAlpha();
        //GlStateManager.alphaFunc(GL11.GL_NOTEQUAL, 0);
    }

    public static double easeInOutQuad(double x) {
        return x < 0.5 ? 2 * x * x : 1 - Math.pow((-2 * x + 2), 2) / 2;
    }

    public static void cropBox(float x, float y, float width, float height) {
        final ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int factor = scale.getScaleFactor();
        glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - height) * factor), (int) ((width - x) * factor), (int) ((height - y) * factor));
    }

    public static void makeCropBox(float left, float top, float right, float bottom) {
        glPushMatrix();
        glEnable(GL_SCISSOR_TEST);
        cropBox(left, top, right, bottom);
    }

    public static void drawPlatform(final Entity entity, final Color color) {
        final Timer timer = Minecraft.timer;
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.renderPartialTicks - RenderManager.renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.renderPartialTicks - RenderManager.renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.renderPartialTicks - RenderManager.renderPosZ;
        final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);
        drawAxisAlignedBB(new AxisAlignedBB(axisAlignedBB.minX - 0.1, axisAlignedBB.minY - 0.1, axisAlignedBB.minZ - 0.1, axisAlignedBB.maxX + 0.1, axisAlignedBB.maxY + 0.2, axisAlignedBB.maxZ + 0.1), color);
    }

    public static void pre3D() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glHint(3154, 4354);
    }

    public static void post3D() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (!GL11.glIsEnabled(2896)) {
            GL11.glEnable(2896);
        }
    }

    public static void drawBoundingBox(final AxisAlignedBB aa) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawRoundedRectangle(double left, double top, double right, double bottom, double radius, int color) {
        glScaled(0.5D, 0.5D, 0.5D);
        left *= 2.0D;
        top *= 2.0D;
        right *= 2.0D;
        bottom *= 2.0D;
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        GlStateManager.enableBlend();
        glColor(color);
        glBegin(9);

        int i;
        for (i = 0; i <= 90; i += 1)
            glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, top + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 1)
            glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 1)
            glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 1)
            glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, top + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glScaled(2.0D, 2.0D, 2.0D);
        glColor4d(1, 1, 1, 1);
    }

    public static void drawUnfilledRectangle(double left, double top, double right, double bottom, double radius, int color) {
        glScaled(0.5D, 0.5D, 0.5D);
        left *= 2.0D;
        top *= 2.0D;
        right *= 2.0D;
        bottom *= 2.0D;
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_LINE_SMOOTH);
        GlStateManager.enableBlend();
        glColor(color);
        glBegin(GL_LINE_LOOP);

        int i;
        for (i = 0; i <= 90; i += 1)
            glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, top + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 90; i <= 180; i += 1)
            glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        for (i = 0; i <= 90; i += 1)
            glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        for (i = 90; i <= 180; i += 1)
            glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, top + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glEnable(GL_TEXTURE_2D);
        glScaled(2.0D, 2.0D, 2.0D);
        glColor4d(1, 1, 1, 1);
    }

    public static void drawAxisAlignedBB(final AxisAlignedBB axisAlignedBB, final Color color) {
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        glColor2(color);
        drawFilledBox(axisAlignedBB);
        GlStateManager.resetColor();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }

    private static final ResourceLocation shader = new ResourceLocation("cat/blur.json");
    public static Minecraft mc = Minecraft.getMinecraft();
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static Framebuffer buffer;
    private static ShaderGroup blurShader;

    public static float animate(float target, float current, float speed) {
        boolean larger = (target > current);
        speed = range(speed * 10 / delta, 0, 1);
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = dif * speed;
        if (factor < 0.001f)
            factor = 0.001f;
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }

    public static void drawArrow(double x, double y, int lineWidth, int color, double length) {
        start2D();
        GL11.glPushMatrix();
        GL11.glLineWidth(lineWidth);
        setColor(new Color(color));
        GL11.glBegin(GL_LINE_STRIP);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x + 3, y + length);
        GL11.glVertex2d(x + 3 * 2, y);
        GL11.glEnd();
        GL11.glPopMatrix();
        stop2D();
    }




    public static float range(float v, float min, float max) {
        return Math.max(Math.min(v, max), min);
    }

    public static void rect(final float x, final float y, final float x2, final float y2, final int color) {
        Gui.drawRect(x, y, x2, y2, color);
        GlStateManager.resetColor();
    }

    public static void rect(final float x, final float y, final float x2, final float y2, final Color color) {
        Gui.drawRect(x, y, x2, y2, color.getRGB());
        GlStateManager.resetColor();
    }

    public static void rect(final double x, final double y, final double x2, final double y2, final Color color) {
        Gui.drawRect(x, y, x2, y2, color.getRGB());
        GlStateManager.resetColor();
    }

    public static void crop(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int factor = scaledResolution.getScaleFactor();
        glScissor((int) (x * factor), (int) ((scaledResolution.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static void initFboAndShader() {
        try {
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(Minecraft.displayWidth, Minecraft.displayHeight);
            buffer = blurShader.mainFramebuffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void blur(float x, float y, float x2, float y2, ScaledResolution sc) {
        int factor = sc.getScaleFactor();
        int factor2 = sc.getScaledWidth();
        int factor3 = sc.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        crop(x, y, x2, y2);
        buffer.framebufferHeight = Minecraft.displayHeight;
        buffer.framebufferWidth = Minecraft.displayWidth;
        GlStateManager.resetColor();
        blurShader.loadShaderGroup(Minecraft.timer.renderPartialTicks);
        buffer.bindFramebuffer(true);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void blur(float x, float y, float x2, float y2) {
        GlStateManager.disableAlpha();
        blur(x, y, x2, y2, new ScaledResolution(mc));
        GlStateManager.enableAlpha();
    }

    public static float drawScaledFont(FontRenderer f, String text, float x, float y, int color, boolean shadow, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);
        f.drawString(text, 0, 0, color, shadow);
        GlStateManager.popMatrix();
        return f.getStringWidth(text) * scale;
    }

    public static void glColor(final Color color) {
        final float red = color.getRed() / 255F;
        final float green = color.getGreen() / 255F;
        final float blue = color.getBlue() / 255F;
        final float alpha = color.getAlpha() / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }




    public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        worldRenderer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public static void glColor2(final Color color) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        final float alpha = color.getAlpha() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }


    public static void drawRadius(final Entity entity, final double radius, final float partialTicks, final int points, final float width, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = interpolate(entity.prevPosX, entity.posX, partialTicks) - RenderManager.viewerPosX;
        final double y = interpolate(entity.prevPosY, entity.posY, partialTicks) - RenderManager.viewerPosY;
        final double z = interpolate(entity.prevPosZ, entity.posZ, partialTicks) - RenderManager.viewerPosZ;
        color(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 6.283185307179586 / points), y, z + radius * Math.sin(i * 6.283185307179586 / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void enableBlending() {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
    }

    public static void disableTexture2D() {
        GL11.glDisable(3553);
    }

    public static void enableTexture2D() {
        GL11.glEnable(3553);
    }

    public static void enableDepth() {
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
    }

    public static void disableDepth() {
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
    }

    public static void drawFilledCircleNoBorder(float x, float y, float radius, int color) {
        enableRender2D();
        setColor(color);
        int vertices = (int) Math.min(Math.max(radius, 45.0F), 360.0F);
        GL11.glBegin(9);

        for (int i = 0; i < vertices; ++i) {
            double angleRadians = 6.283185307179586D * (double) i / (double) vertices;
            GL11.glVertex2d((double) x + Math.sin(angleRadians) * (double) radius, (double) y + Math.cos(angleRadians) * (double) radius);
        }

        GL11.glEnd();
        disableRender2D();
    }


    public static int transparency(final int color, final double alpha) {
        final Color c = new Color(color);
        final float r = 0.003921569f * c.getRed();
        final float g = 0.003921569f * c.getGreen();
        final float b = 0.003921569f * c.getBlue();
        return new Color(r, g, b, (float)alpha).getRGB();
    }


    public static void setColor(int colorHex) {
        float alpha = (float) (colorHex >> 24 & 255) / 255.0F;
        float red = (float) (colorHex >> 16 & 255) / 255.0F;
        float green = (float) (colorHex >> 8 & 255) / 255.0F;
        float blue = (float) (colorHex & 255) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static int reAlpha(int color, float alpha) {
        try {
            Color c = new Color(color);
            float r = ((float) 1 / 255) * c.getRed();
            float g = ((float) 1 / 255) * c.getGreen();
            float b = ((float) 1 / 255) * c.getBlue();
            return new Color(r, g, b, alpha).getRGB();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return color;
    }


    public static void enableRender2D() {
        GL11.glEnable(3042);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0F);
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


    public static void disableBlending() {
        GL11.glDisable(3042);
    }

    public static int drawHealth(final EntityLivingBase entityLivingBase) {
        final float health = entityLivingBase.getHealth();
        final float maxHealth = entityLivingBase.getMaxHealth();
        return Color.HSBtoRGB(Math.max(0.0f, Math.min(health, maxHealth) / maxHealth) / 3.0f, 1.0f, 0.75f) | 0xFF000000;
    }


    public static int rainbow(final int count, final float bright, final float st) {
        double v1 = Math.ceil((double)(System.currentTimeMillis() + count * 109)) / 5.0;
        return Color.getHSBColor(((float)((v1 %= 360.0) / 360.0) < 0.5) ? (-(float)(v1 / 360.0)) : ((float)(v1 / 360.0)), st, bright).getRGB();
    }

    public static int getRainbow(final float seconds, final float saturation, final float brightness) {
        final float hue = System.currentTimeMillis() % (int)(seconds * 1000.0f) / (seconds * 1000.0f);
        final int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static int getRainbow(final float seconds, final float saturation, final float brightness, final long index) {
        final float hue = (System.currentTimeMillis() + index) % (int)(seconds * 1000.0f) / (seconds * 1000.0f);
        final int color = Color.HSBtoRGB(hue, saturation, brightness);
        return color;
    }

    public static boolean isHovering(float mouseX, float mouseY, float xLeft, float yUp, float xRight, float yBottom) {
        return mouseX > xLeft && mouseX < xRight && mouseY > yUp && mouseY < yBottom;
    }




    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustum.setPosition(current.posX, current.posY, current.posZ);
        return frustum.isBoundingBoxInFrustum(bb);
    }

    public static void scissor(int x, int y, int xSize, int ySize) {
        ScaledResolution res = new ScaledResolution(mc);
        x = x * res.getScaleFactor();
        ySize = ySize * res.getScaleFactor();
        y = mc.displayHeight - (y * res.getScaleFactor()) - ySize;
        xSize = xSize * res.getScaleFactor();
        GL11.glScissor(x, y, xSize, ySize);
    }

    public static void drawBorderedRect(double x, double y, double width, double height, double lineSize, int borderColor, int color) {
        Gui.drawRect(x, y, x + width, y + height, color);
        Gui.drawRect(x, y, x + width, y + lineSize, borderColor);
        Gui.drawRect(x, y, x + lineSize, y + height, borderColor);
        Gui.drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        Gui.drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }


    public static void drawGradientSideways12(double left, double top, double right, double bottom, int col1, int col2) {
        drawGradientSideways(left, top, right + left, bottom + top, col1, col2);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 255) / 255.0f;
        float f1 = (col1 >> 16 & 255) / 255.0f;
        float f2 = (col1 >> 8 & 255) / 255.0f;
        float f3 = (col1 & 255) / 255.0f;
        float f4 = (col2 >> 24 & 255) / 255.0f;
        float f5 = (col2 >> 16 & 255) / 255.0f;
        float f6 = (col2 >> 8 & 255) / 255.0f;
        float f7 = (col2 & 255) / 255.0f;
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

    public static void drawRoundedRect1232(double x, double y, double width, double height, double radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glBegin(GL11.GL_POLYGON);

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + 0 + +(Math.sin((i * Math.PI / 180)) * (0 * -1)), y + 0 + (Math.cos((i * Math.PI / 180)) * (0 * -1)));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + (Math.sin((i * Math.PI / 180)) * (radius * -1)), y1 - radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y1 - radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - 0 + (Math.sin((i * Math.PI / 180)) * 0), y + 0 + (Math.cos((i * Math.PI / 180)) * 0));
        }

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }



    public static void drawCircle2(double x, double y, double radius, int c) {
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glEnable(GL_POLYGON_SMOOTH);
        float alpha = (float) (c >> 24 & 255) / 255.0f;
        float red = (float) (c >> 16 & 255) / 255.0f;
        float green = (float) (c >> 8 & 255) / 255.0f;
        float blue = (float) (c & 255) / 255.0f;
        boolean blend = GL11.glIsEnabled(3042);
        boolean line = GL11.glIsEnabled(2848);
        boolean texture = GL11.glIsEnabled(3553);
        if (!blend) {
            GL11.glEnable(3042);
        }
        if (!line) {
            GL11.glEnable(2848);
        }
        if (texture) {
            GL11.glDisable(3553);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(9);
        int i = 0;
        while (i <= 360) {
            GL11.glVertex2d(
                    x + Math.sin((double) i * 3.141526 / 180.0) * radius,
                    y + Math.cos((double) i * 3.141526 / 180.0) * radius);
            ++i;
        }
        GL11.glEnd();
        if (texture) {
            GL11.glEnable(3553);
        }
        if (!line) {
            GL11.glDisable(2848);
        }
        if (!blend) {
            GL11.glDisable(3042);
        }
        GL11.glDisable(GL_POLYGON_SMOOTH);
        GL11.glClear(0);
    }

    public static void drawCircle(double d, double e, float r, int c) {
        float f = (c >> 24 & 0xFF) / 255.0f;
        float f2 = (c >> 16 & 0xFF) / 255.0f;
        float f3 = (c >> 8 & 0xFF) / 255.0f;
        float f4 = (c & 0xFF) / 255.0f;
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(2848);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin(i * Math.PI / 180.0) * (r / 2);
            double y2 = Math.cos(i * Math.PI / 180.0) * (r / 2);
            GL11.glVertex2d(d + r / 2 + x2, e + r / 2 + y2);
        }
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i <= 360; ++i) {
            double x2 = Math.sin(i * Math.PI / 180.0) * ((r / 2));
            double y2 = Math.cos(i * Math.PI / 180.0) * ((r / 2));
            GL11.glVertex2d(d + ((r / 2)) + x2, e + ((r / 2)) + y2);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }
        RenderUtil.drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }

    public static int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 0.75F) | 0xFF000000;
    }

    public static int getRainbow(long currentMillis, int speed, int offset) {
        return Color.HSBtoRGB(1.0F - ((currentMillis + offset) % speed) / (float) speed,
                0.9F, 0.9F);
    }

    public static int getRainbowFelix(int speed, int offset, float s) {
        float hue = (System.currentTimeMillis() + offset) % speed;
        hue /= speed;
        return Color.getHSBColor(hue, s, 1f).getRGB();

    }

    public static double interpolate1(double old,
                                      double now,
                                      float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public static void drawHLine(float x, float y, float x1, int y1) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }
        RenderUtil.drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }

    public static int removeAlphaComponent(final int colour) {
        final int red = colour >> 16 & 0xFF;
        final int green = colour >> 8 & 0xFF;
        final int blue = colour & 0xFF;

        return ((red & 0xFF) << 16) |
                ((green & 0xFF) << 8) |
                (blue & 0xFF);
    }


    public static void drawRoundedRect2(final double x, final double y, final double width, final double height, double radius, int color) {
        RenderUtil.drawRoundedRect(x, y, width - x, height - y, radius, color);
    }

    public static void drawRoundedRect(double x, double y, double width, double height, double radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glBegin(GL11.GL_POLYGON);

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + +(Math.sin((i * Math.PI / 180)) * (radius * -1)), y + radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x + radius + (Math.sin((i * Math.PI / 180)) * (radius * -1)), y1 - radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y1 - radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y + radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }

    public static void drawRoundedRect32(double x, double y, double width, double height, double radius, int color) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        double x1 = x + width;
        double y1 = y + height;
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5, 0.5, 0.5);

        x *= 2;
        y *= 2;
        x1 *= 2;
        y1 *= 2;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glBegin(GL11.GL_POLYGON);

        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(x + radius + +(Math.sin((i * Math.PI / 180)) * (radius * -1)), y + radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
        }

//        for (int i = 90; i <= 180; i += 3) {
//            GL11.glVertex2d(x + radius + (Math.sin((i * Math.PI / 180)) * (radius * -1)), y1 - radius + (Math.cos((i * Math.PI / 180)) * (radius * -1)));
//        }

//        for (int i = 0; i <= 90; i += 3) {
//            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y1 - radius + (Math.cos((i * Math.PI / 180)) * radius));
//        }
//
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(x1 - radius + (Math.sin((i * Math.PI / 180)) * radius), y + radius + (Math.cos((i * Math.PI / 180)) * radius));
        }

        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glScaled(2, 2, 2);

        GL11.glPopAttrib();
        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

    }

    public static void drawFillRectangle(double x, double y, double width, double height) {
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(x, y + height);
        GL11.glVertex2d(x + width, y + height);
        GL11.glVertex2d(x + width, y);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }

    public static void drawCirclePart(double x, double y, float fromAngle, float toAngle, float radius, int slices) {
        GlStateManager.enableBlend();
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(x, y);
        final float increment = (toAngle - fromAngle) / slices;

        for (int i = 0; i <= slices; i++) {
            final float angle = fromAngle + i * increment;

            final float dX = MathHelper.sin(angle);
            final float dY = MathHelper.cos(angle);

            GL11.glVertex2d(x + dX * radius, y + dY * radius);
        }
        GL11.glEnd();
    }

    public static void destroyCropBox() {
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
    }

    public static void drawImage12(ResourceLocation image, float x, float y, float width, float height) {
        drawImage12(image, x, y, width, height, 255);
    }

    public static void drawImage12(ResourceLocation image, float x, float y, float width, float height, float opacity) {
        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(1, 1, 1, opacity / 255);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GlStateManager.color(1, 1, 1, 1);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }





    public static void drawImageWithTint(ResourceLocation image, float x, float y, float width, float height, Color color) {
        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GlStateManager.color(1, 1, 1, 1);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }


    public static void colorRGBA(int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;

        GlStateManager.color(r, g, b, a);
    }


    public static void drawRoundedRect5(float n, float n2, float n3, float n4, final int n5, final int n6) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(n *= 2.0f, (n2 *= 2.0f) + 1.0f, (n4 *= 2.0f) - 2.0f, n5);
        drawVLine((n3 *= 2.0f) - 1.0f, n2 + 1.0f, n4 - 2.0f, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n2, n5);
        drawHLine(n + 2.0f, n3 - 3.0f, n4 - 1.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n2 + 1.0f, n5);
        drawHLine(n3 - 2.0f, n3 - 2.0f, n4 - 2.0f, n5);
        drawHLine(n + 1.0f, n + 1.0f, n4 - 2.0f, n5);
        drawRect(n + 1.0f, n2 + 1.0f, n3 - 1.0f, n4 - 1.0f, n6);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawRoundRect(float x, float y, float x1, float y1, int color) {
        drawRoundedRect5(x, y, x1, y1, color, color);
        GlStateManager.color(1, 1, 1);
    }

    public static void drawGradientRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        enableGL2D();
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        glColor(topColor);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        glColor(bottomColor);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        disableGL2D();
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

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static boolean inBounds(float x, float y, float w, float h, int mouseX, int mouseY) {
        return (mouseX >= x && mouseX <= w && mouseY >= y && mouseY <= h);
    }

    public static void startScissorBox(final ScaledResolution sr, final int x, final int y, final int width, final int height) {
        final int sf = sr.getScaleFactor();
        GL11.glScissor(x * sf, (sr.getScaledHeight() - (y + height)) * sf, width * sf, height * sf);
    }

    public static void drawRoundedRect19(final float x, final float y, final float width, final float height,
                                         final float radius, final int color) {
        float x2 = x + ((radius / 2f) + 0.5f);
        float y2 = y + ((radius / 2f) + 0.5f);
        float calcWidth = (width - ((radius / 2f) + 0.5f));
        float calcHeight = (height - ((radius / 2f) + 0.5f));
        // top (pink)
        relativeRect(x2 + radius / 2f, y2 - radius / 2f - 0.5f, x2 + calcWidth - radius / 2f, y + calcHeight - radius / 2f,
                color);
        // bottom (yellow)
        relativeRect(x2 + radius / 2f, y2, x2 + calcWidth - radius / 2f, y2 + calcHeight + radius / 2f + 0.5f, color);
        // left (red)
        relativeRect((x2 - radius / 2f - 0.5f), y2 + radius / 2f, x2 + calcWidth, y2 + calcHeight - radius / 2f, color);
        // right (green)
        relativeRect(x2, y2 + radius / 2f + 0.5f, x2 + calcWidth + radius / 2f + 0.5f, y2 + calcHeight - radius / 2f,
                color);

        // left top circle
        polygonCircle(x, y - 0.15, radius * 2, 360, color);
        // right top circle
        polygonCircle(x + calcWidth - radius + 1.0, y - 0.15, radius * 2, 360, color);
        // left bottom circle
        polygonCircle(x, y + calcHeight - radius + 1, radius * 2, 360, color);
        // right bottom circle
        polygonCircle(x + calcWidth - radius + 1, y + calcHeight - radius + 1, radius * 2, 360, color);
    }

    public static final void polygonCircle(final double x, final double y, double sideLength, final double degree,
                                           final int color) {
        sideLength *= 0.5;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GlStateManager.disableAlpha();

        glColor(color);

        GL11.glLineWidth(1);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        // since its filled, otherwise GL_LINE_STRIP
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (double i = 0; i <= degree; i++) {
            final double angle = i * (Math.PI * 2) / degree;

            GL11.glVertex2d(x + (sideLength * Math.cos(angle)) + sideLength,
                    y + (sideLength * Math.sin(angle)) + sideLength);
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GlStateManager.enableAlpha();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void relativeRect(final float left, final float top, final float right, final float bottom,
                                    final int color) {

        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(color);
		/*        worldRenderer.begin -> .func_181668_a
        worldRenderer.pos -> .func_181662_b*/
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(left, bottom, 0).endVertex();
        worldRenderer.pos(right, bottom, 0).endVertex();
        worldRenderer.pos(right, top, 0).endVertex();
        worldRenderer.pos(left, top, 0).endVertex();

        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static ScaledResolution getScaledResolution() {
        final int displayWidth = Display.getWidth();
        final int displayHeight = Display.getHeight();
        final int guiScale = Minecraft.getMinecraft().gameSettings.guiScale;
        if (displayWidth != lastScaledWidth || displayHeight != lastScaledHeight || guiScale != lastGuiScale) {
            lastScaledWidth = displayWidth;
            lastScaledHeight = displayHeight;
            lastGuiScale = guiScale;
            return scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        }
        return scaledResolution;
    }

    public static void drawPing(float x, float y, NetworkPlayerInfo playerInfo) {
        GlStateManager.color(1, 1, 1, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
        int offset = 0;
        if (playerInfo != null) {
            if (playerInfo.getResponseTime() < 0) offset = 5;
            if (playerInfo.getResponseTime() < 150) offset = 0;
            if (playerInfo.getResponseTime() < 300) offset = 1;
            if (playerInfo.getResponseTime() < 600) offset = 2;
            if (playerInfo.getResponseTime() < 1000) offset = 3;
            else offset = 4;
        }
        drawTexturedModalRect((int) (x - 11), (int) y, 0, 176 + offset * 8, 10, 8);
    }

    public static void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + height, 0).tex((float) textureX * f, (float) (textureY + height) * f).endVertex();
        worldrenderer.pos(x + width, y + height, 0).tex((float) (textureX + width) * f, (float) (textureY + height) * f).endVertex();
        worldrenderer.pos(x + width, y, 0).tex((float) (textureX + width) * f, (float) textureY * f).endVertex();
        worldrenderer.pos(x, y, 0).tex((float) textureX * f, (float) textureY * f).endVertex();
        tessellator.draw();
    }

    public static int astolfo(float seconds, float saturation, float brightness, float index) {
        float speed = 3000f;
        float hue = (System.currentTimeMillis() % (int) (seconds * 1000)) + index;
        while (hue > speed)
            hue -= speed;
        hue /= speed;
        if (hue > 0.5)
            hue = 0.5F - (hue - 0.5f);
        hue += 0.5F;
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public static int fade(Color color, int count, int index) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % 2000L / 1000.0f + index / (float) count * 2.0f) % 2.0f - 1.0f);
        brightness = 0.5f + 0.5f * brightness;
        return Color.HSBtoRGB(hsb[0], hsb[1], brightness % 2.0f);
    }

    public static float animate(double target, double current, double speed) {
        boolean larger = (target > current);
        if (speed < 0.0F) speed = 0.0F;
        else if (speed > 1.0F) speed = 1.0F;
        double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif * speed;
        if (factor < 0.1f) factor = 0.1F;
        if (larger) current += factor;
        else current -= factor;
        return (float) current;
    }


    public static double progressiveAnimation(final double now, final double desired, final double speed) {
        final double dif = Math.abs(now - desired);
        final int fps = Minecraft.getDebugFPS();
        if (dif > 0.0) {
            double animationSpeed = MathUtils.roundToDecimalPlace(Math.min(10.0, Math.max(0.05, 144.0 / fps * (dif / 10.0) * speed)), 0.05);
            if (dif < animationSpeed) {
                animationSpeed = dif;
            }
            if (now < desired) {
                return now + animationSpeed;
            }
            if (now > desired) {
                return now - animationSpeed;
            }
        }
        return now;
    }

    public static double interpolate(double old,
                                     double now,
                                     float partialTicks) {
        return old + (now - old) * partialTicks;
    }

    public static double interpolateScale(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static Vector3f project2D(int scaleFactor, float x, float y, float z) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
        if (GLU.gluProject(x, y, z, modelMatrix, projectionMatrix, viewport, windowPosition))
            return new Vector3f(windowPosition.get(0) / scaleFactor, (Minecraft.displayHeight - windowPosition.get(1)) / scaleFactor, windowPosition.get(2));
        return null;
    }

    public static boolean isHovered(float x, float y, float w, float h, int mouseX, int mouseY) {
        return (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h);
    }

    public static boolean isHoveredFull(float x, float y, float w, float h, int mouseX, int mouseY) {
        return (mouseX >= x && mouseX <= w && mouseY >= y && mouseY <= h);
    }

    public static void color(Color color) {
        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
    }

    public static void color(int color, int alpha) {
        float[] rgba = convertRGB(color);
        glColor4f(rgba[0], rgba[1], rgba[2], alpha / 255f);
    }

    public static float[] convertRGB(int rgb) {
        float a = (rgb >> 24 & 0xFF) / 255.0f;
        float r = (rgb >> 16 & 0xFF) / 255.0f;
        float g = (rgb >> 8 & 0xFF) / 255.0f;
        float b = (rgb & 0xFF) / 255.0f;
        return new float[]{r, g, b, a};
    }

    public static void color(int color) {
        float[] rgba = convertRGB(color);
        GL11.glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
    }

    public static Color pulseBrightness(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs(((float) (System.currentTimeMillis() % 2000L) / 1000.0F + (float) index / (float) count * 2.0F) % 2.0F - 1.0F);
        brightness = 0.5F + 0.5F * brightness;
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], brightness % 2.0F));
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;

        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static int getColorSwitch(Color firstColor, Color secondColor, float time, int index, long timePerIndex, double speed, double alpha) {
        long now = (long) (speed * System.currentTimeMillis() + -index * timePerIndex);

        float rd = (firstColor.getRed() - secondColor.getRed()) / time;
        float gd = (firstColor.getGreen() - secondColor.getGreen()) / time;
        float bd = (firstColor.getBlue() - secondColor.getBlue()) / time;

        float rd2 = (secondColor.getRed() - firstColor.getRed()) / time;
        float gd2 = (secondColor.getGreen() - firstColor.getGreen()) / time;
        float bd2 = (secondColor.getBlue() - firstColor.getBlue()) / time;

        int re1 = Math.round(secondColor.getRed() + rd * (now % (long) time));
        int ge1 = Math.round(secondColor.getGreen() + gd * (now % (long) time));
        int be1 = Math.round(secondColor.getBlue() + bd * (now % (long) time));
        int re2 = Math.round(firstColor.getRed() + rd2 * (now % (long) time));
        int ge2 = Math.round(firstColor.getGreen() + gd2 * (now % (long) time));
        int be2 = Math.round(firstColor.getBlue() + bd2 * (now % (long) time));

        if (now % ((long) time * 2L) < (long) time) {
            return getColor((int) alpha, re2, ge2, be2);
        } else {
            return getColor((int) alpha, re1, ge1, be1);
        }
    }

    public static int getColor(int A, int R, int G, int B) {
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }

    public static Color brighter(Color color, float factor) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();
        int i = (int) (1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) return new Color(i, i, i, alpha);
        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;
        return new Color(Math.min((int) (r / factor), 255), Math.min((int) (g / factor), 255), Math.min((int) (b / factor), 255), alpha);
    }

    public static Color darker(Color color, float factor) {
        return new Color(Math.max((int) (color.getRed() * factor), 0), Math.max((int) (color.getGreen() * factor), 0), Math.max((int) (color.getBlue() * factor), 0), color.getAlpha());
    }


    public static int darker(final int color, final float factor) {
        final int r = (int) ((color >> 16 & 0xFF) * factor);
        final int g = (int) ((color >> 8 & 0xFF) * factor);
        final int b = (int) ((color & 0xFF) * factor);
        final int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) | (a & 0xFF) << 24;
    }

    public static void prepareScissorBox(float x, float y, float width, float height) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        prepareScissorBox(x, y, width, height, scaledResolution);
    }

    public static void prepareScissorBox(float x, float y, float width, float height, ScaledResolution scaledResolution) {
        int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scaledResolution.getScaledHeight() - height) * factor), (int) ((width - x) * factor), (int) ((height - y) * factor));
    }

    public static void drawImage(ResourceLocation image, float x, float y, float width, float height) {
        drawImage(image, x, y, width, height, 255);
    }

    public static void drawImage(final int x, final int y, final int width, final int height, final ResourceLocation image, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawRect1(double x, double y, double width, double height, int color) {
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        GL11.glColor4f(f1, f2, f3, f);
        Gui.drawRect(x, y, x + width, y + height, color);
    }

    public static void drawImage(float x, float y, final int width, final int height, final ResourceLocation image, Color color) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, (int) y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }

    public static void drawImage(final int x, final int y, final int width, final int height, final ResourceLocation image) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1, 1, 1, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, (float) width, (float) height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }


    public static void drawImage(ResourceLocation image, float x, float y, float width, float height, float opacity) {
        glPushMatrix();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        glColor4f(1.0f, 1.0f, 1.0f, opacity / 255);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        glDepthMask(true);
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glPopMatrix();
    }

    public static void drawRect(float x, float y, float width, float height, int color) {
        glPushMatrix();
        glDisable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        color(color);
        glBegin(GL_QUADS);
        glVertex2d(width, y);
        glVertex2d(x, y);
        glVertex2d(x, height);
        glVertex2d(width, height);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        glDisable(GL_LINE_SMOOTH);
        glPopMatrix();
    }

    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
        GL11.glPushMatrix();
        cx *= 2.0F;
        cy *= 2.0F;
        float f = (c >> 24 & 0xFF) / 255.0F;
        float f1 = (c >> 16 & 0xFF) / 255.0F;
        float f2 = (c >> 8 & 0xFF) / 255.0F;
        float f3 = (c & 0xFF) / 255.0F;
        float theta = (float) (6.2831852D / num_segments);
        float p = (float) Math.cos(theta);
        float s = (float) Math.sin(theta);
        float x = r *= 2.0F;
        float y = 0.0F;
        enableGL2D();
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(2);
        int ii = 0;
        while (ii < num_segments) {
            GL11.glVertex2f(x + cx, y + cy);
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ii++;
        }
        GL11.glEnd();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
        GlStateManager.color(1, 1, 1, 1);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(float cx, float cy, float radius, float num_segments, Color color) {
        double theta = 2 * Math.PI / num_segments;
        double c = Math.cos(theta); //precalculate the sine and cosine
        double s = Math.sin(theta);
        double t;
        double x = radius; //we start at angle = 0
        double y = 0;
        glBegin(GL_LINE_LOOP);
        for (int ii = 0; ii < num_segments; ii++) {
            color(color);
            glVertex2d(x + cx, y + cy); //output vertex
            //apply the rotation matrix
            t = x;
            x = c * x - s * y;
            y = s * t + c * y;
        }
        glEnd();
    }


    public enum RoundingMode1 {
        TOP_LEFT,
        BOTTOM_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,

        LEFT,
        RIGHT,

        TOP,
        BOTTOM,

        FULL
    }

}
