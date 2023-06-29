package org.moonware.client.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;

import javax.imageio.ImageIO;
import java.io.InputStream;

public class LoadingScreen {
    public static DynamicTexture texture;
    public static String state;
    static {
        try (InputStream in = LoadingScreen.class.getResourceAsStream("/assets/moonware/launch.png")) {
            texture = new DynamicTexture(ImageIO.read(in));
            texture.updateDynamicTexture();
        } catch (Exception ignored) {}
    }

    public static void setState(String state) {
        LoadingScreen.state = state;
        draw();
    }

    public static void draw() {
        int width = Minecraft.width;
        int height = Minecraft.height;
        Framebuffer framebuffer = new Framebuffer(width, height, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, width, height, 0, 1000, 3000);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, 0, -2000);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        if (texture != null) {
            GlStateManager.color(1F, 1F, 1F);
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(texture.getGlTextureId());
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlTextureId());
            GL11.glBegin(GL11.GL_TRIANGLES);
            GL11.glTexCoord2d(1D, 0D);
            GL11.glVertex2d(width, 0D);
            GL11.glTexCoord2d(0D, 0D);
            GL11.glVertex2d(0D, 0D);
            GL11.glTexCoord2d(0D, 1D);
            GL11.glVertex2d(0D, height);
            GL11.glTexCoord2d(0D, 1D);
            GL11.glVertex2d(0D, height);
            GL11.glTexCoord2d(1D, 1D);
            GL11.glVertex2d(width, height);
            GL11.glTexCoord2d(1D, 0D);
            GL11.glVertex2d(width, 0D);
            GL11.glEnd();
        }
        GL11.glPushMatrix();
        GL11.glScaled(2, 2, 2);
        MWFont.MONTSERRAT_BOLD.get(24).drawCenter(state, width / 2F / 2F, (height - 24F) / 2F, -1);
        GL11.glPopMatrix();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(width, height);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        Minecraft.updateDisplay();
        Thread.yield();
    }
}
