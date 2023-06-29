package ua.apraxia.utility.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static ua.apraxia.utility.Utility.mc;
import static ua.apraxia.utility.render.ShaderUtility.drawQuads;

public class RoundedUtility {

    public static ShaderUtility roundedShader = new ShaderUtility("roundedRect");
    public static ShaderUtility roundedOutlineShader = new ShaderUtility("roundedOutlineRect");
    private static final ShaderUtility roundedTexturedShader = new ShaderUtility("stardust/shaders/round.frag");
    private static final ShaderUtility roundedGradientShader = new ShaderUtility("roundedRectGradient");


    public static void drawRound(float x, float y, float width, float height, float radius, Color color) {
        drawRound(x, y, width, height, radius, false, color);
    }

    public static void drawRoundScale(float x, float y, float width, float height, float radius, Color color, float scale) {
        drawRound(x + width - width * scale, y + height / 2f - ((height / 2f) * scale),
                width * scale, height * scale, radius, false, color);
    }

    public static void drawRoundCircle(float x, float y, float radius, Color color) {
        drawRound(x - (radius / 2), y - (radius / 2), radius, radius, (radius / 2) - 0.5f, color);
    }


    public static void drawRoundCircleOut(float x, float y, float radius, float thikness, Color color, Color sidecolor) {
        drawRoundOutline(x - (radius / 2), y - (radius / 2), radius, radius, (radius / 2) - 0.5f, thikness, color, sidecolor);
    }

    public static void drawGradientHorizontal(float x, float y, float width, float height, float radius, Color left, Color right) {
        drawGradientRound(x, y, width, height, radius, left, left, right, right);
    }
    public static void drawGradientVertical(float x, float y, float width, float height, float radius, Color top, Color bottom) {
        drawGradientRound(x, y, width, height, radius, bottom, top, bottom, top);
    }

    public static void drawGradientRound(float x, float y, float width, float height, float radius, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight) {
        RenderUtility.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedGradientShader.init();
        setupRoundedRectUniforms(x, y, width, height, radius, roundedGradientShader);
        // Bottom Left
        roundedGradientShader.setUniformf("color1", bottomLeft.getRed() / 255f, bottomLeft.getGreen() / 255f, bottomLeft.getBlue() / 255f, 80 / 255f);
        //Top left
        roundedGradientShader.setUniformf("color2", topLeft.getRed() / 255f, topLeft.getGreen() / 255f, topLeft.getBlue() / 255f, 80 / 255f);
        //Bottom Right
        roundedGradientShader.setUniformf("color3", bottomRight.getRed() / 255f, bottomRight.getGreen() / 255f, bottomRight.getBlue() / 255f, 80 / 255f);
        //Top Right
        roundedGradientShader.setUniformf("color4", topRight.getRed() / 255f, topRight.getGreen() / 255f, topRight.getBlue() / 255f, 80 / 255f);
        drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedGradientShader.unload();
        GlStateManager.disableBlend();
    }


    public static void drawRound(float x, float y, float width, float height, float radius, boolean blur, Color color) {
        RenderUtility.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        roundedShader.init();

        setupRoundedRectUniforms(x, y, width, height, radius, roundedShader);
        roundedShader.setUniformi("blur", blur ? 1 : 0);
        roundedShader.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedShader.unload();
        GlStateManager.disableBlend();
    }


    public static void drawRoundOutline(float x, float y, float width, float height, float radius, float thickness,
                                                   Color insideColor, Color outlineColor)
    {
        ScaledResolution sr = new ScaledResolution(mc);

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        roundedOutlineShader.init();

        roundedOutlineShader.setUniformf("location", x * sr.getScaleFactor(),
                mc.displayHeight - (height * sr.getScaleFactor()) - (y * sr.getScaleFactor()));
        roundedOutlineShader.setUniformf("size", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        roundedOutlineShader.setUniformf("radius", radius * sr.getScaleFactor());
        roundedOutlineShader.setUniformf("thickness", thickness * sr.getScaleFactor());
        roundedOutlineShader.setUniformf("color",
                insideColor.getRed() / 255f, insideColor.getGreen() / 255f, insideColor.getBlue() / 255f, insideColor.getAlpha() / 255f);
        roundedOutlineShader.setUniformf("outlineColor",
                outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, outlineColor.getAlpha() / 255f);

        drawQuads(x - (2 + thickness), y - (2 + thickness), width + (4 + thickness * 2), height + (4 + thickness * 2));

        roundedOutlineShader.unload();

        GlStateManager.enableAlpha();
    }


    public static void drawRoundTextured(float x, float y, float width, float height, float radius, float alpha) {
        RenderUtility.resetColor();
        roundedTexturedShader.init();
        roundedTexturedShader.setUniformi("textureIn", 0);
        setupRoundedRectUniforms(x, y, width, height, radius, roundedTexturedShader);
        roundedTexturedShader.setUniformf("alpha", alpha);
        drawQuads(x - 1, y - 1, width + 2, height + 2);
        roundedTexturedShader.unload();
        GlStateManager.disableBlend();
    }

    private static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtility roundedTexturedShader) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        roundedTexturedShader.setUniformf("location", x * sr.getScaleFactor(),
                (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        roundedTexturedShader.setUniformf("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        roundedTexturedShader.setUniformf("radius", radius * sr.getScaleFactor());
    }


}
