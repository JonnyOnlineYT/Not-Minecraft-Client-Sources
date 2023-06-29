/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.drag.imp;

import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import ru.fluger.client.Fluger;
import ru.fluger.client.GifEngine;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.RoundedUtil;
import ru.fluger.client.ui.drag.Drag;
import ru.fluger.client.ui.drag.imp.SessionInfo;
import ru.squad47.UserData;

public class WaterMark
extends Drag {
    public static GifEngine watermark = new GifEngine(new ResourceLocation("nightmare/girls/pika.gif"), 16, 16);
    int animation = 0;
    boolean animate = false;

    public WaterMark() {
        this.setHeight(this.mc.fontRenderer.getFontHeight() + 1);
        this.setWidth(this.mc.fontRenderer.getStringWidth(Fluger.name));
        this.name = "WaterMark";
    }

    @Override
    public void render(int mouseX, int mouseY) {
        if (HUD.logo.getCurrentValue()) {
            this.height = 10.0f;
            RenderHelper.drawBlurredShadow(this.x - 1.0f, this.y - 1.0f, this.width + 2.0f, this.height + 2.0f, 10, PaletteHelper.TwoColorEffect(new Color(40, 50, 220), new Color(90, 0, 200), 20.0));
            if (this.animation >= 255) {
                this.animate = true;
            }
            if (this.animation <= 0) {
                this.animate = false;
            }
            if (this.animate) {
                --this.animation;
            }
            if (!this.animate) {
                ++this.animation;
            }
            this.animation = MathHelper.clamp(this.animation, 0, 255);
            RoundedUtil.drawGradientRound(this.x, this.y, this.width, this.height, 4.0f, new Color(40, 50, 220), new Color(40, 40, 255), new Color(90, 0, 200), new Color(100, 0, 255));
            String id = UserData.instance().getID();
            String user = UserData.instance().getName();
            String text = "SLIV SRC FLUGER CLIENT BY MINCED CLIENT" + id + " USER: " + user;
            this.mc.smallfontRenderer.drawString(text, this.x + 16.0f, this.y + 3.5f, -1);
            int textWidth = this.mc.smallfontRenderer.getStringWidth(text);
            this.width = textWidth + 20;
            SessionInfo.watermark.update();
            SessionInfo.watermark.bind((int)this.x, (int)this.y + 1);
        }
    }

    @Override
    public void init() {
        this.x = 10.0f;
        this.y = 10.0f;
        super.init();
    }
}

