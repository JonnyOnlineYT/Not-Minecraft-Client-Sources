/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.drag.imp;

import java.awt.Color;
import java.util.Date;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import ru.fluger.client.Fluger;
import ru.fluger.client.GifEngine;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.RoundedUtil;
import ru.fluger.client.ui.drag.Drag;

public class SessionInfo
extends Drag {
    private double cooldownBarWidth;
    private double hurttimeBarWidth;
    public static GifEngine watermark = new GifEngine(new ResourceLocation("nightmare/girls/meme.gif"), 16, 16);

    public SessionInfo() {
        this.setWidth(145.0f);
        this.setHeight(52.0f);
        this.name = "Indicators";
    }

    @Override
    public void render(int mouseX, int mouseY) {
        HUD hud = (HUD)Fluger.instance.featureManager.getFeatureByClass(HUD.class);
        if (!hud.state) {
            return;
        }
        if (!HUD.sessioninfo.getCurrentValue()) {
            return;
        }
        int x = (int)this.x;
        int y = (int)this.y;
        int width = (int)this.getWidth();
        int height = (int)this.getHeight();
        this.setWidth(100.0f);
        this.setHeight(36.0f);
        RenderHelper.drawBlurredShadow(this.x - 1.0f, this.y - 1.0f, this.width + 2.0f, this.height + 2.0f, 10, PaletteHelper.TwoColorEffect(new Color(40, 50, 220), new Color(90, 0, 200), 20.0));
        RoundedUtil.drawGradientRound(this.x, this.y, this.width, this.height, 4.0f, new Color(40, 50, 220), new Color(40, 40, 255), new Color(90, 0, 200), new Color(100, 0, 255));
        this.mc.clickguismall.drawString("Session info", this.x + 2.0f, this.y + 3.0f, -1);
        String server = "localhost";
        if (this.mc.getCurrentServerData() != null) {
            server = this.mc.getCurrentServerData().serverIP;
        }
        Date date = new Date();
        String time = String.valueOf(date.getHours()) + ":" + date.getMinutes() + ":" + date.getSeconds();
        this.mc.fontRenderer.drawString("\u0421\u0435\u0440\u0432\u0435\u0440: " + server, this.x + 3.0f, this.y + 12.0f, -1);
        this.mc.fontRenderer.drawString("\u041d\u0438\u043a: " + this.mc.session.username, this.x + 3.0f, this.y + 20.0f, -1);
        this.mc.fontRenderer.drawString("\u0412\u0440\u0435\u043c\u044f: " + time, this.x + 3.0f, this.y + 28.0f, -1);
    }

    public void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(startColor >> 16 & 0xFF) / 255.0f;
        float f3 = (float)(startColor >> 8 & 0xFF) / 255.0f;
        float f4 = (float)(startColor & 0xFF) / 255.0f;
        float f5 = (float)(endColor >> 24 & 0xFF) / 255.0f;
        float f6 = (float)(endColor >> 16 & 0xFF) / 255.0f;
        float f7 = (float)(endColor >> 8 & 0xFF) / 255.0f;
        float f8 = (float)(endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, Gui.zLevel).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, top, Gui.zLevel).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, bottom, Gui.zLevel).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(right, bottom, Gui.zLevel).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static int getCenter(int width, int rectWidth) {
        return width / 2 - rectWidth / 2;
    }

    @Override
    public void init() {
        this.x = 40.0f;
        this.y = 40.0f;
        super.init();
    }
}

