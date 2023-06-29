package ru.rockstar;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.api.utils.Helper;
import ru.rockstar.api.utils.render.DrawHelper;

import java.awt.*;

public class SplashProgress implements Helper {

    public static ResourceLocation resourceLocation = new ResourceLocation("rockstar/launch.png");
    public static int Progress;
    public static int maxProgress = 7;
    public static FontRenderer fontRenderer;

    public static void update() {
        SplashProgress.drawSplash(mc.getTextureManager());
    }

    public static void setProgress(int progress) {
        Progress = progress;
        SplashProgress.update();
    }

    public static void drawSplash(TextureManager textureManager) {
        fontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), false);
        if (mc.gameSettings.language != null) {
            fontRenderer.setUnicodeFlag(mc.isUnicode());
            fontRenderer.setBidiFlag(mc.mcLanguageManager.isCurrentLanguageBidirectional());
        }
        mc.mcResourceManager.registerReloadListener(fontRenderer);
        int scaleFactor = sr.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0, sr.getScaledWidth(), sr.getScaledHeight(), 0, 1000, 3000);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0, 0, -2000);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        textureManager.bindTexture(resourceLocation);
        GlStateManager.resetColor();
        GlStateManager.color(1, 1, 1, 1);
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
        SplashProgress.drawProgress();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(sr.getScaledWidth() * scaleFactor, sr.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        mc.updateDisplay();
    }

    private static void drawProgress() {
        if (mc.gameSettings == null) {
            return;
        }
        float calc = Progress / 7f * sr.getScaledWidth() * 0.595F;
        GlStateManager.resetColor();
        GlStateManager.TextureState.textureName = -1;
        DrawHelper.drawCircle(10, sr.getScaledHeight() - 10, 0, 360, 4.5f, 1, false, new Color(100, 100, 100));
        DrawHelper.drawCircle(10, sr.getScaledHeight() - 10, 0 - 60, 360 - 60, 5.5f, 1, false, new Color(100, 100, 100));
        DrawHelper.drawCircle(10, sr.getScaledHeight() - 10, sr.getScaledHeight() - 97, 86 + calc, 5, 1, false, new Color(0, 255, 255));
    }
}
