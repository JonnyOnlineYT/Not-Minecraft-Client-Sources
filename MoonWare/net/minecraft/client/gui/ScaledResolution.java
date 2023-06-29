package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

@Deprecated
public class ScaledResolution {
    public ScaledResolution(Minecraft minecraftClient) {}

    public int getScaledWidth() {
        return Minecraft.getScaledRoundedWidth();
    }

    public int getScaledHeight() {
        return Minecraft.getScaledRoundedHeight();
    }

    public int getScaleFactor() {
        return Minecraft.getScaleFactor();
    }
}