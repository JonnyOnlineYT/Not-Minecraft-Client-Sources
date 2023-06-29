package net.minecraft.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.Progress;
import org.lwjgl.opengl.Display;

public class LoadingScreenRenderer implements Progress {
    private final Minecraft mc;
    private final Framebuffer framebuffer;
    private String title = "";
    private String text = "";
    private int progress = -1;
    public LoadingScreenRenderer(Minecraft mcIn) {
        mc = mcIn;
        framebuffer = new Framebuffer(Minecraft.width, Minecraft.height, false);
        framebuffer.setFramebufferFilter(9728);
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void draw() {
        ScaledResolution sr = new ScaledResolution(mc);
        int factor = sr.getScaleFactor();
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        if (OpenGlHelper.isFramebufferEnabled()) {
            framebuffer.framebufferClear();
        } else {
            GlStateManager.clear(256);
        }
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, width, height, 0.0D, 100.0D, 300.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -200.0F);
        if (!OpenGlHelper.isFramebufferEnabled()) GlStateManager.clear(16640);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Minecraft.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, height, 0.0D).tex(0.0D, (height / 32.0F)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, height, 0.0D).tex(width / 32.0F, height / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, 0.0D, 0.0D).tex(width / 32.0F, 0.0D).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
        if (progress >= 0) {
            int k1 = width / 2 - 50;
            int l1 = height / 2 + 16;
            GlStateManager.disableTexture2D();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(k1, l1, 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.pos(k1, (l1 + 2), 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.pos((k1 + 100), (l1 + 2), 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.pos((k1 + 100), l1, 0.0D).color(128, 128, 128, 255).endVertex();
            bufferbuilder.pos(k1, l1, 0.0D).color(128, 255, 128, 255).endVertex();
            bufferbuilder.pos(k1, (l1 + 2), 0.0D).color(128, 255, 128, 255).endVertex();
            bufferbuilder.pos((k1 + progress), (l1 + 2), 0.0D).color(128, 255, 128, 255).endVertex();
            bufferbuilder.pos((k1 + progress), l1, 0.0D).color(128, 255, 128, 255).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Minecraft.font.drawStringWithShadow(title, (width - Minecraft.font.getStringWidth(title)) / 2F, height / 2F - 20F, -1);
        Minecraft.font.drawStringWithShadow(text, (width - Minecraft.font.getStringWidth(text)) / 2F, height / 2F + 4F, -1);
        framebuffer.unbindFramebuffer();
        if (OpenGlHelper.isFramebufferEnabled()) framebuffer.framebufferRender(width * factor, height * factor);
        mc.updateDisplay();
        try {
            Display.sync(30);
            Thread.yield();
        } catch (Exception ignored) {}
    }

    public void done() {}
}
