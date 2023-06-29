/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package ru.fluger.client.ui.drag.imp;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.BigDecimal;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.ui.drag.Drag;

public class WorldInfo
extends Drag {
    public WorldInfo() {
        this.setWidth(88.0f);
        this.setHeight(20.0f);
        this.name = "WorldInfo";
    }

    @Override
    public void init() {
        this.x = 5.0f;
        this.y = 300.0f;
    }

    @Override
    public void render(int x, int y) {
        if (!HUD.worldInfo.getCurrentValue()) {
            return;
        }
        double prevPosX = this.mc.player.posX - this.mc.player.prevPosX;
        double prevPosZ = this.mc.player.posZ - this.mc.player.prevPosZ;
        float distance = (float)Math.sqrt(prevPosX * prevPosX + prevPosZ * prevPosZ);
        BigDecimal speedValue = MathematicHelper.round(distance * 15.5f, 1);
        ClientHelper.getFontRender().drawStringWithShadow("XYZ: " + (Object)ChatFormatting.WHITE + Math.round(this.mc.player.posX) + " " + Math.round(this.mc.player.posY) + " " + Math.round(this.mc.player.posZ), this.x, this.y + 18.0f, -1);
    }
}

