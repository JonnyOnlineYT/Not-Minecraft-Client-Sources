package Celestial.ui;

import Celestial.ui.font.FontUtil;
import Celestial.utils.Helper;
import Celestial.utils.render.RenderUtils;
import Celestial.ui.font.MCFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class SplashProgress implements Helper {

    public static ResourceLocation resourceLocation = new ResourceLocation("minced/launch.png");
    public static int Progress;
    public static int maxProgress = 11;
    public static FontRenderer fontRenderer;
    public static MCFontRenderer pix = new MCFontRenderer(FontUtil.getFontFromTTF(new ResourceLocation("font/mntsb.ttf"), 30f, 0), true, true);
    public static MCFontRenderer pix1 = new MCFontRenderer(FontUtil.getFontFromTTF(new ResourceLocation("font/mntsb.ttf"), 15f, 0), true, true);

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
        float calcResult = 7 / 7f * sr.getScaledWidth() * 0.595F;
        float percent = (Progress / 7f) * 100;
        float color = new Color(12, 255, 210).getRGB();
        GlStateManager.resetColor();
        GlStateManager.TextureState.textureName = -1;
        pix.drawSmoothStringWithShadow("Celestial", sr.getScaledWidth() / 2.5f, sr.getScaledHeight() / 2.5f,  new Color(244,244,244).getRGB());
        pix1.drawBlurredString(String.format("%.0f", percent) + "%", 86 + calc - pix1.getStringWidth(String.format("%.0f", percent) + "%"), sr.getScaledHeight() - 87, 8, new Color(119, 119, 119, 50), -1);

        RenderUtils.drawSmoothRect(85, sr.getScaledHeight() - 101, 87 + calcResult, sr.getScaledHeight() - 96, new Color(154, 154, 154).getRGB());
        RenderUtils.drawSmoothRect(86, sr.getScaledHeight() - 100, 86 + calc, sr.getScaledHeight() - 97, (int) color);


    }
}