package org.moonware.client.utils;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class MWDrawUtils {
    public static void scaledScissors(int x, int y, int width, int height) {
        GL11.glScissor(x * Minecraft.getScaleFactor(), (Minecraft.getScaledRoundedHeight() - y - height) * Minecraft.getScaleFactor(), width * Minecraft.getScaleFactor(), height * Minecraft.getScaleFactor());
    }

}
