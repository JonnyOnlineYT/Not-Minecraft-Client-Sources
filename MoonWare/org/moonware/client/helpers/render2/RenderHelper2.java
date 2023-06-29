package org.moonware.client.helpers.render2;

import com.jhlabs.image.GaussianFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.feature.impl.combat.TargetESP;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.feature.impl.visual.EntityESP;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.blur.GaussianBlur;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.ui.sqgui.Panel;
import org.moonware.client.utils.MWUtils;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static net.minecraft.client.renderer.GlStateManager.disableBlend;
import static net.minecraft.client.renderer.GlStateManager.enableTexture2D;
import static org.lwjgl.opengl.GL11.*;

public class RenderHelper2
        implements Helper {
    private static final Matrix4f modelMatrix = new Matrix4f();
    private static final Matrix4f projectionMatrix = new Matrix4f();
    private static final Frustum frustum = new Frustum();
    private static HashMap<Integer, Integer> shadowCache = new HashMap();
    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static Namespaced shader;
    public static HashMap<Integer, Integer> blurSpotCache;

    public static void setColor(Color color) {
        float alpha = (color.getRGB() >> 24 & 0xFF) / 255.0F;
        float red = (color.getRGB() >> 16 & 0xFF) / 255.0F;
        float green = (color.getRGB() >> 8 & 0xFF) / 255.0F;
        float blue = (color.getRGB() & 0xFF) / 255.0F;
        glColor4f(red, green, blue, alpha);
    }
    public static void renderBloom(float radius, Runnable r,Runnable two) {

        StencilUtil.initStencilToWrite();
        r.run();
        StencilUtil.readStencilBuffer(1);
        //GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
        StencilUtil.initStencilToWrite();
        two.run();
        StencilUtil.readStencilBuffer(1);
        //GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();

    }
    public static int getArrayAstolfo(double counter, int alpha) {
        final int width = 110;
        EntityESP playerESP = new EntityESP();

        double rainbowState = Math.ceil(System.currentTimeMillis() - (long) counter * width) / 11;
        rainbowState %= 360;
        float hue = (float) (rainbowState / 360) < 0.5 ? -((float) (rainbowState / 360)) : (float) (rainbowState / 360);

        float[] colors =ColorUtil.rgbToHSL(ColorUtil.skyRainbow(22,14));
        Color color = Color.getHSBColor(hue, colors[1], colors[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }
    public static int getArrayRainbow(double counter, int alpha) {
        final int width = 110;

        double rainbowState = Math.ceil(System.currentTimeMillis() - (long) counter * width) / 11;
        rainbowState %= 360;

        float[] colors = ColorUtil.rgbToHSL(PaletteHelper.rainbow(22,14,1.5F));
        Color color = Color.getHSBColor((float) (rainbowState / 360), colors[1], colors[2]);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha).getRGB();
    }
    public static Color mixColors(Color color1, Color color2, double percent) {
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color1.getRed() * percent + color2.getRed() * inverse_percent);
        int greenPart = (int) (color1.getGreen() * percent + color2.getGreen() * inverse_percent);
        int bluePart = (int) (color1.getBlue() * percent + color2.getBlue() * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }
    public static Color mixColors(int a, int b, float ratio) {
        if (ratio > 1f) {
            ratio = 1f;
        } else if (ratio < 0f) {
            ratio = 0f;
        }
        float iRatio = 1.0f - ratio;

        int aA = (a >> 24 & 0xff);
        int aR = ((a & 0xff0000) >> 16);
        int aG = ((a & 0xff00) >> 8);
        int aB = (a & 0xff);

        int bA = (b >> 24 & 0xff);
        int bR = ((b & 0xff0000) >> 16);
        int bG = ((b & 0xff00) >> 8);
        int bB = (b & 0xff);

        int A = ((int)(aA * iRatio) + (int)(bA * ratio));
        int R = ((int)(aR * iRatio) + (int)(bR * ratio));
        int G = ((int)(aG * iRatio) + (int)(bG * ratio));
        int B = ((int)(aB * iRatio) + (int)(bB * ratio));

        return new Color(A << 24 | R << 16 | G << 8 | B);
    }

    public static void drawRainbowRound(double x,double y,double width,double height,float range, boolean glow,boolean glowback, boolean outline,boolean astolfo,double plus,double plus2)
    {
        if (!HUD.enterAstolfo.get()) {
            double counter = 0;
            double counter1 = 40;
            double counter2 = 80;
            double counter3 = 120;
            double counter4 = 160;
            double randomPlus = MWUtils.randomDouble(0, 1);
            Color color1 = Color.WHITE, color2 = Color.WHITE, color3 = Color.WHITE, color4 = Color.WHITE;
            if (astolfo) {
                for (double i = 0; i <= 15; i += plus2) {
                    color1 = new Color(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)).getRGB());
                    counter += plus;
                }
                for (double i = 0; i <= 15; i += plus2) {
                    color2 = new Color(new Color(astolfo ? getArrayAstolfo(counter2, 255) : getArrayRainbow(counter2, 255)).getRGB());
                    counter2 += plus;
                }
                for (double i = 0; i <= 15; i += plus2) {
                    color3 = new Color(new Color(astolfo ? getArrayAstolfo(counter3, 255) : getArrayRainbow(counter3, 255)).getRGB());
                    counter3 += plus;
                }
                for (double i = 0; i <= 15; i += plus2) {
                    color4 = new Color(new Color(astolfo ? getArrayAstolfo(counter4, 255) : getArrayRainbow(counter4, 255)).getRGB());
                    counter4 += plus;
                }
            } else {
                for (double iu = 0; iu <= 1; iu += 2) {
                    for (double i = 0; i <= 1115; i += plus2) {
                        //color1 = new Color(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)).getRGB());
                        color1 = new Color(PaletteHelper.fadeColor(HUD.timerColor.getColor(), HUD.timerColor2.getColorValue(), (float) counter1));
                        counter1 += i;
                    }
                    for (double i = 0; i <= 1115; i += plus2) {
                        //color2 = new Color(new Color(astolfo ? getArrayAstolfo(counter2, 255) : getArrayRainbow(counter2, 255)).getRGB());
                        color2 = new Color(PaletteHelper.fadeColor(HUD.timerColor.getColor(), HUD.timerColor2.getColorValue(), (float) counter2));
                        counter2 += i;
                    }
                    for (double i = 0; i <= 1115; i += plus2) {
                        //color3 = new Color(new Color(astolfo ? getArrayAstolfo(counter3, 255) : getArrayRainbow(counter3, 255)).getRGB());
                        color3 = new Color(PaletteHelper.fadeColor(HUD.timerColor.getColor(), HUD.timerColor2.getColorValue(), (float) counter3));
                        counter3 += i;
                    }
                    for (double i = 0; i <= 1115; i += plus2) {
                        //color4 = new Color(new Color(astolfo ? getArrayAstolfo(counter4, 255) : getArrayRainbow(counter4, 255)).getRGB());
                        color4 = new Color(PaletteHelper.fadeColor(HUD.timerColor.getColor(), HUD.timerColor2.getColorValue(), (float) counter4));
                        counter4 += i;
                    }
                    Color colorThreeE = mixColors(color1.getRGB(), color4.getRGB(), 0.5f);
                    if (HUD.hueInterpolation.get()) {
                        //renderBlurredShadow(colorThreeE, x, y, width, height, 14);
                    }
                    //RoundedUtil.drawRound((float) x, (float) y, (float) width, (float) height, range,new Color(255,255,255,255));
                    RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, ColorUtil.applyOpacity(color1,255), ColorUtil.applyOpacity(color2,255), ColorUtil.applyOpacity(color3,255), ColorUtil.applyOpacity(color4,255));
                    if (outline) {
                        RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 0.3f, new Color(255, 255, 255, 0), new Color(255, 255, 255, 150));
                    }

                }
            }
            //Color colorOneE = mixColors(color1,color2,1);
            //Color colorTwoE = mixColors(color3,color4,1);
            if (astolfo) {
                Color colorThreeE = mixColors(color1.getRGB(), color4.getRGB(), 0.5f);
                if (HUD.hueInterpolation.get()) {

                }
                RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, color1, color2, color3, color4);
                if (outline) {
                    RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 0.3f, new Color(255, 255, 255, 0), new Color(255, 255, 255, 150));
                }
            }
        }else {

            Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE, gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE;

            if (!HUD.MoonColors.get()) {
                if (!HUD.useCustomColors.get()) {
                    gradientColor1 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 0, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                    gradientColor2 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 90, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                    gradientColor3 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 180, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                    gradientColor4 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 270, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                } else {
                    gradientColor1 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 0, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                    gradientColor2 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 90, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                    gradientColor3 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 180, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                    gradientColor4 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), 270, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                }
                Color colorThreeE = mixColors(gradientColor1.getRGB(), gradientColor4.getRGB(), 0.5f);
                if (HUD.hueInterpolation.get() && glow) {
                    //RenderUtils2.drawBlurredShadow((float) x, (float) y, (float) width, (float) height, 20, DrawHelper.getColorWithOpacity(colorThreeE, 255));
                }
                if (glowback) {
                    Color finalGradientColor = gradientColor4;
                    Color finalGradientColor1 = gradientColor1;
                    Color finalGradientColor2 = gradientColor3;
                    Color finalGradientColor3 = gradientColor2;
                    StencilUtil.initStencilToWrite();
                    RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, ColorUtil.applyOpacity(gradientColor4, .45f), ColorUtil.applyOpacity(gradientColor1, .45f), ColorUtil.applyOpacity(gradientColor3, .45f), ColorUtil.applyOpacity(gradientColor2, .45f));
                    StencilUtil.readStencilBuffer(3);
                    StencilUtil.uninitStencilBuffer();
                    //StencilUtil.initStencilToWrite();
                    //RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, ColorUtil.applyOpacity(gradientColor4, .45f), ColorUtil.applyOpacity(gradientColor1, .45f), ColorUtil.applyOpacity(gradientColor3, .45f), ColorUtil.applyOpacity(gradientColor2, .45f));
                    //StencilUtil.readStencilBuffer(2);
                    if (HUD.glow.get()) {
                        if (HUD.colored.get() || HUD.nurikMode.get())
                            GlowUtil.drawBlurredShadow((float) (x - 1), (float) (y - 1), (float) (width + 2), (float) (height + 2), 14, gradientColor4, 1);
                        else {
                            GlowUtil.drawBlurredShadow((float) (x), (float) (y), (float) (width), (float) (height), 14, gradientColor4, 1);
                        }
                    }
                    //StencilUtil.uninitStencilBuffer();

                    if (HUD.colored.get() || HUD.nurikMode.get()) {
                        RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, ColorUtil.applyOpacity(gradientColor4, HUD.nurikAlpha.getCurrentValue() / 100), ColorUtil.applyOpacity(gradientColor1, HUD.nurikAlpha.getCurrentValue() / 100), ColorUtil.applyOpacity(gradientColor3, HUD.nurikAlpha.getCurrentValue() / 100), ColorUtil.applyOpacity(gradientColor2, HUD.nurikAlpha.getCurrentValue() / 100));
                        Panel.applyBloom((float) x, (float) y, (float) width, (float) height,32,gradientColor1, gradientColor2, gradientColor3, gradientColor4);
                        if (outline) {
                            RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 0.3f, new Color(255, 255, 255, 0), new Color(255, 255, 255, 100));
                        }
                    }else {
                        RoundedUtil.drawRound((float) x, (float) y, (float) width, (float) height,range,new Color(31,31,31,HUD.nurikAlpha.getCurrentIntValue()));

                    }
                }else{
                    StencilUtil.initStencilToWrite();
                    RoundedUtil.drawGradientHorizontal((float) x, (float) y + 0.3f, (float) width, (float) height - 0.3f, range, gradientColor4, gradientColor2);
                    StencilUtil.readStencilBuffer(1);
                    Color color = Color.white;
                    int ii = 4;
                    if (height == 0.1f) {
                        ii = 2;
                    }
                    for (int i = 0 ; i < width + 92 ; i+= 1) {
                        if (!HUD.MoonColors.get()) {
                            if (!HUD.useCustomColors.get()) {
                                color = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), (int) (((int) (i) + System.currentTimeMillis() / 24) * ii), oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                            } else {
                                color = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(), (int) (((int) (i) + System.currentTimeMillis() / 24) * ii), HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                            }
                        }else{
                            color = new Color(ColorUtil.rainbow((int) (((int) (i) + System.currentTimeMillis() / 24) * 44),HUD.time.getCurrentIntValue()));
                        }
                        Gui.drawRect(x - 3 + i, y - 8, x - 3 + i + 1, y + height + 8, color.getRGB());
                        //System.out.print(System.currentTimeMillis() / 94);
                        //DrawHelper.drawImage(new Namespaced("moonware/lgbt.png"), (int) (0 - System.currentTimeMillis() / 40000000 * 1000),0,Minecraft.width,Minecraft.height,-1);
                    }
                    StencilUtil.uninitStencilBuffer();
                    if (outline) {
                        RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 0.3f, new Color(255, 255, 255, 0), new Color(255, 255, 255, 100));
                    }

                }
                 if (HUD.hueInterpolation.get()) {

                }
            }else {
                for (int i = 0; i < 40 ; i++) {
                    gradientColor1 =  new Color(ColorUtil.rainbow(8,HUD.time.getCurrentIntValue()));
                }
                for (int i = 80 ; i < 140 ; i++) {
                    gradientColor2 = ColorUtil.interpolateColorsBackAndForth(HUD.time.getCurrentIntValue(),i, new Color(ColorUtil.rainbow(2,HUD.time.getCurrentIntValue())) , new Color(0x632D85),HUD.hueInterpolation.get());
                }
                RoundedUtil.drawGradientHorizontal((float) x, (float) y, (float) width, (float) height,range,gradientColor1,gradientColor2);
                //RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, ColorUtil.applyOpacity(gradientColor4, HUD.nurikAlpha.getCurrentValue() / 100), ColorUtil.applyOpacity(gradientColor1, HUD.nurikAlpha.getCurrentValue() / 100), ColorUtil.applyOpacity(gradientColor3, HUD.nurikAlpha.getCurrentValue() / 100), ColorUtil.applyOpacity(gradientColor2, HUD.nurikAlpha.getCurrentValue() / 100));

            }
        }

    }
    public static void drawRainbowRoundGui(double x,double y,double width,double height,float range, boolean glow,boolean glowback, boolean outline,boolean astolfo,double plus,double plus2)
    {
        if (!ClickGui.colored.get()) {
            if (outline)
                RoundedUtil.drawRoundOutline((float) x, (float) y, (float) width, (float) height,range,1,new Color(51,51,51),new Color(255,255,255,outline? 100 : 0));
            else{
                RoundedUtil.drawRound((float) x, (float) y, (float) width, (float) height,range,new Color(51,51,51));

            }
            if (!glow) {
                //RenderUtils2.drawBlur(3, () -> RoundedUtil.drawRoundOutline((float) x, (float) y, (float) width, (float) height, range, 1, new Color(51, 51, 51), new Color(255, 255, 255, 100)));
            }
            }else {

            Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE, gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE;

            boolean tr = true;
            boolean h = HUD.hueInterpolation.get();
            if (!tr) {
                if (!ClickGui.selfColors.get()) {
                    gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                    gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                    gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                    gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                } else {
                    gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, ClickGui.colorOnee.getColorc(), ClickGui.colorTwoo.getColorc(), HUD.hueInterpolation.get());
                    gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, ClickGui.colorOnee.getColorc(), ClickGui.colorTwoo.getColorc(), HUD.hueInterpolation.get());
                    gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, ClickGui.colorOnee.getColorc(), ClickGui.colorTwoo.getColorc(), HUD.hueInterpolation.get());
                    gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, ClickGui.colorOnee.getColorc(), ClickGui.colorTwoo.getColorc(), HUD.hueInterpolation.get());
                }
                Color colorThreeE = mixColors(gradientColor1.getRGB(), gradientColor4.getRGB(), 0.5f);
                if (HUD.hueInterpolation.get() && glow) {
                    //RenderHelper2.renderBlurredShadow(colorThreeE, x, y, width, height, 14);
                    //RenderUtils2.drawBlurredShadow((float) x, (float) y, (float) width, (float) height, 20, DrawHelper.getColorWithOpacity(colorThreeE, 255));
                    //GlowUtil.drawBlurredGlow(x,y, x + width,y + height, colorThreeE.getRGB());
                }
                RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, gradientColor4, gradientColor1, gradientColor3, gradientColor2);
               if (outline) {
                    RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 1, new Color(255, 255, 255, 0), new Color(255, 255, 255, 100));
                }
            }else {
                if (!ClickGui.selfColors.get()) {
                    gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, oneColorGradient(), alternateColorGradient(), h);
                    gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, oneColorGradient(), alternateColorGradient(), h);
                    gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, oneColorGradient(), alternateColorGradient(), h);
                    gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, oneColorGradient(), alternateColorGradient(), h);
                } else {
                    gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, ClickGui.colorOnee.getColorc(), ClickGui.colorTwoo.getColorc(), h);
                    gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, ClickGui.colorOnee.getColorc(), ClickGui.colorTwoo.getColorc(), h);
                    gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, ClickGui.colorOnee.getColorc(), ClickGui.colorTwoo.getColorc(), h);
                    gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, ClickGui.colorOnee.getColorc(), ClickGui.colorTwoo.getColorc(), h );
                }
                if (glowback) {
                    RoundedUtil.drawGradientHorizontal((float) x, (float) y, (float) width, (float) height, range, gradientColor1, gradientColor2);
                }else{
                    StencilUtil.initStencilToWrite();
                    RoundedUtil.drawGradientHorizontal((float) x, (float) y, (float) width, (float) height, range, ColorUtil.applyOpacity(gradientColor1,.65f), ColorUtil.applyOpacity(gradientColor2,.65f));
                    StencilUtil.readStencilBuffer(1);
                    GaussianBlur.renderBlur(3);
                    StencilUtil.uninitStencilBuffer();
                    RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, ColorUtil.applyOpacity(gradientColor4, .65f), ColorUtil.applyOpacity(gradientColor1, .55f), ColorUtil.applyOpacity(gradientColor3, .55f), ColorUtil.applyOpacity(gradientColor2, .55f));

                }
            }
        }
    }
    public static final Color oneColorGradient() {
        return new Color(153, 0, 255);
    }
    public static final Color alternateColorGradient() {
        return new Color(71, 134, 168);
    }

    public static void drawRainbowRoundDarker(double x,double y,double width,double height,int range, boolean glow,boolean glowback, boolean outline,boolean astolfo,double plus,double plus2)
    {
        if (!HUD.enterAstolfo.get()) {
            double counter = 0;
            double counter1 = 40;
            double counter2 = 80;
            double counter3 = 120;
            double counter4 = 160;
            double randomPlus = MWUtils.randomDouble(0, 1);
            Color color1 = Color.WHITE, color2 = Color.WHITE, color3 = Color.WHITE, color4 = Color.WHITE;
            if (astolfo) {
                for (double i = 0; i <= 15; i += plus2) {
                    color1 = new Color(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)).getRGB());
                    counter += plus;
                }
                for (double i = 0; i <= 15; i += plus2) {
                    color2 = new Color(new Color(astolfo ? getArrayAstolfo(counter2, 255) : getArrayRainbow(counter2, 255)).getRGB());
                    counter2 += plus;
                }
                for (double i = 0; i <= 15; i += plus2) {
                    color3 = new Color(new Color(astolfo ? getArrayAstolfo(counter3, 255) : getArrayRainbow(counter3, 255)).getRGB());
                    counter3 += plus;
                }
                for (double i = 0; i <= 15; i += plus2) {
                    color4 = new Color(new Color(astolfo ? getArrayAstolfo(counter4, 255) : getArrayRainbow(counter4, 255)).getRGB());
                    counter4 += plus;
                }
            } else {
                for (double iu = 0; iu <= 1; iu += 2) {
                    for (double i = 0; i <= 1115; i += plus2) {
                        //color1 = new Color(new Color(astolfo ? getArrayAstolfo(counter, 255) : getArrayRainbow(counter, 255)).getRGB());
                        color1 = new Color(PaletteHelper.fadeColor(HUD.timerColor.getColor(), HUD.timerColor2.getColorValue(), (float) counter1));
                        counter1 += i;
                    }
                    for (double i = 0; i <= 1115; i += plus2) {
                        //color2 = new Color(new Color(astolfo ? getArrayAstolfo(counter2, 255) : getArrayRainbow(counter2, 255)).getRGB());
                        color2 = new Color(PaletteHelper.fadeColor(HUD.timerColor.getColor(), HUD.timerColor2.getColorValue(), (float) counter2));
                        counter2 += i;
                    }
                    for (double i = 0; i <= 1115; i += plus2) {
                        //color3 = new Color(new Color(astolfo ? getArrayAstolfo(counter3, 255) : getArrayRainbow(counter3, 255)).getRGB());
                        color3 = new Color(PaletteHelper.fadeColor(HUD.timerColor.getColor(), HUD.timerColor2.getColorValue(), (float) counter3));
                        counter3 += i;
                    }
                    for (double i = 0; i <= 1115; i += plus2) {
                        //color4 = new Color(new Color(astolfo ? getArrayAstolfo(counter4, 255) : getArrayRainbow(counter4, 255)).getRGB());
                        color4 = new Color(PaletteHelper.fadeColor(HUD.timerColor.getColor(), HUD.timerColor2.getColorValue(), (float) counter4));
                        counter4 += i;
                    }
                    Color colorThreeE = mixColors(color1.getRGB(), color4.getRGB(), 0.5f);
                    if (HUD.hueInterpolation.get()) {
                        renderBlurredShadow(colorThreeE, x, y, width, height, 14);
                    }
                    //RoundedUtil.drawRound((float) x, (float) y, (float) width, (float) height, range,new Color(255,255,255,255));
                    RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, ColorUtil.applyOpacity(color1,255).darker().darker().darker().darker(), ColorUtil.applyOpacity(color2,255).darker().darker().darker().darker(), ColorUtil.applyOpacity(color3,255).darker().darker().darker().darker(), ColorUtil.applyOpacity(color4,255).darker().darker().darker().darker());
                    if (outline) {
                        RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 0.3f, new Color(255, 255, 255, 0), new Color(255, 255, 255, 150));
                    }
                }
            }
            //Color colorOneE = mixColors(color1,color2,1);
            //Color colorTwoE = mixColors(color3,color4,1);
            if (astolfo) {
                Color colorThreeE = mixColors(color1.getRGB(), color4.getRGB(), 0.5f);
                if (HUD.hueInterpolation.get()) {
                    renderBlurredShadow(colorThreeE, x, y, width, height, 14);
                    //GlowUtil.drawBlurredGlow(x,y, x + width,y + height, colorThreeE.getRGB());
                }
                RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, color1, color2, color3, color4);
                if (outline) {
                    RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 0.3f, new Color(255, 255, 255, 0), new Color(255, 255, 255, 150));
                }
            }
        }else {
            Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE, gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE;

            if (!HUD.useCustomColors.get()) {
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, oneColorGradient(), alternateColorGradient(), HUD.hueInterpolation.get());
            }else {
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
            }
            RoundedUtil.drawGradientRound((float) x, (float) y, (float) width, (float) height, range, gradientColor4, gradientColor1, gradientColor3, gradientColor2);
            //RenderUtils2.drawColorRect(x,y,width,height,gradientColor4,gradientColor1,gradientColor3,gradientColor2);
            if (outline) {
                RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 0.3f, new Color(255, 255, 255, 0), new Color(255, 255, 255, 150));
            }
            //RoundedUtil.drawRoundOutline((float) x - 1, (float) y - 1, (float) width + 2, (float) height + 2, range, 0.3f, new Color(0, 255, 255, 255), new Color(255, 255, 255, 150));

        }
    }

    public static void drawRect(int mode, double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f, f1, f2, f3);
        bufferbuilder.begin(mode, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, bottom, 0.0D).endVertex();
        bufferbuilder.pos(right, top, 0.0D).endVertex();
        bufferbuilder.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        enableTexture2D();
        disableBlend();
    }
    public static void drawRect(double left, double top, double right, double bottom, int color) {
        drawRect(7, left, top, right, bottom, color);
    }

    public static void inShaderFBO() {
        try {
            blurShader = new ShaderGroup(Minecraft.getTextureManager(), Minecraft.getResourceManager(), Minecraft.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(Minecraft.width, Minecraft.height);
            buffer = blurShader.mainFramebuffer;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void drawBlurredShadow(float x, float y, float width, float height, int blurRadius, Color color) {
        GlStateManager.alphaFunc(GL_GREATER, 0.01f);

        width = width + blurRadius * 2;
        height = height + blurRadius * 2;
        x = x - blurRadius;
        y = y - blurRadius;

        float _X = x - 0.25f;
        float _Y = y + 0.25f;

        int identifier = (int) (width * height + width + color.hashCode() * blurRadius + blurRadius);

        glEnable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        glEnable(GL_ALPHA_TEST);
        GlStateManager.enableBlend();

        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);

            GlStateManager.bindTexture(texId);
        } else {
            BufferedImage original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB);

            Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int) (width - blurRadius * 2), (int) (height - blurRadius * 2));
            g.dispose();

            GaussianFilter op = new GaussianFilter(blurRadius);

            BufferedImage blurred = op.filter(original, null);

            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            shadowCache.put(identifier, texId);
        }

        glColor4f(1f, 1f, 1f, 1f);

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); // top left
        glVertex2f(_X, _Y);

        glTexCoord2f(0, 1); // bottom left
        glVertex2f(_X, _Y + height);

        glTexCoord2f(1, 1); // bottom right
        glVertex2f(_X + width, _Y + height);

        glTexCoord2f(1, 0); // top right
        glVertex2f(_X + width, _Y);
        glEnd();

        GlStateManager.resetColor();
        disableBlend();
        glDisable(GL_ALPHA_TEST);
        glEnable(GL_CULL_FACE);
    }

    private static void shaderConfigFix(float intensity, float blurWidth, float blurHeight) {
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }

    public static void blurAreaBoarder(float x, float f, float width, float height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(Helper.mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            inShaderFBO();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        glScissor((int)(x * (float)factor), (int)((float) Minecraft.height - f * (float)factor - height * (float)factor) + 1, (int)(width * (float)factor), (int)(height * (float)factor));
        glEnable(3089);
        shaderConfigFix(intensity, blurWidth, blurHeight);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(Minecraft.timer.renderPartialTicks);
        Minecraft.getFramebuffer().bindFramebuffer(true);
        glDisable(3089);
    }

    public static void blurAreaBoarder(int x, int y, int width, int height, float intensity) {
        ScaledResolution scale = new ScaledResolution(Helper.mc);
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
            inShaderFBO();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;
        glScissor(x * factor, Minecraft.height - y * factor - height * factor, width * factor, height * factor);
        glEnable(3089);
        shaderConfigFix(intensity, 1.0f, 0.0f);
        buffer.bindFramebuffer(true);
        blurShader.loadShaderGroup(Minecraft.timer.renderPartialTicks);
        Minecraft.getFramebuffer().bindFramebuffer(true);
        glDisable(3089);
    }

    public static void scissorRect(float x, float y, float width, double height) {
        ScaledResolution sr = new ScaledResolution(Helper.mc);
        int factor = sr.getScaleFactor();
        glScissor((int)(x * (float)factor), (int)(((double)sr.getScaledHeight() - height) * (double)factor), (int)((width - x) * (float)factor), (int)((height - (double)y) * (double)factor));
    }

    public static void renderBlur(int x, int y, int width, int height, int blurWidth, int blurHeight, int blurRadius) {
        glEnable(3042);
        glDisable(3553);
        glDisable(2884);
        blurAreaBoarder(x, y, width, height, blurRadius, blurWidth, blurHeight);
        glDisable(3042);
        glEnable(3553);
        glEnable(2884);
        glEnable(3008);
        glEnable(3553);
        glEnable(3042);
    }

    public static void renderBlur(int x, int y, int width, int height, float blurRadius) {
        glEnable(3042);
        glDisable(3553);
        glDisable(2884);
        blurAreaBoarder(x, y, width, height, blurRadius);
        glDisable(3042);
        glEnable(3553);
        glEnable(2884);
        glEnable(3008);
        glEnable(3553);
        glEnable(3042);
    }

    public static void renderBlurredShadow(Color color, double x, double y, double width, double height, int blurRadius) {
        glEnable(GL_BLEND);
        glEnable(3042);

        glDisable(3553);
        glDisable(2884);
        renderBlurredShadow2(x, y, width, height, blurRadius, color);
        glDisable(3042);
        glEnable(3553);
        glEnable(2884);
        glEnable(3008);
        glEnable(3553);
        glEnable(3042);
        glDisable(GL_BLEND);
    }

    public static void renderBlurredShadow2(double x, double y, double width, double height, int blurRadius, Color color) {
        GlStateManager.alphaFunc(516, 0.01f);
        float _X = (float)((x -= blurRadius) - 0.25);
        float _Y = (float)((y -= blurRadius) + 0.25);
        int identifier = (int)((width += blurRadius * 2) * (height += blurRadius * 2) + width + (double)(color.hashCode() * blurRadius) + (double)blurRadius);
        glEnable(3553);
        glDisable(2884);
        glEnable(3008);
        glEnable(3042);
        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        } else {
            try {
                width = MathHelper.clamp(width, 0.01, width);
                height = MathHelper.clamp(height, 0.01, height);
                BufferedImage original = new BufferedImage((int) width, (int) height, 2);
                Graphics g = original.getGraphics();
                g.setColor(color);
                g.fillRect(blurRadius, blurRadius, (int) width - blurRadius * 2, (int) height - blurRadius * 2);
                g.dispose();
                GaussianFilter op = new GaussianFilter(blurRadius);
                BufferedImage blurred = op.filter(original, null);
                texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
                shadowCache.put(identifier, texId);
            }catch (Exception ex) {
            }
        }
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glBegin(7);
        glTexCoord2f(0.0f, 0.0f);
        glVertex2f(_X, _Y);
        glTexCoord2f(0.0f, 1.0f);
        glVertex2f(_X, _Y + (float)((int)height));
        glTexCoord2f(1.0f, 1.0f);
        glVertex2f(_X + (float)((int)width), _Y + (float)((int)height));
        glTexCoord2f(1.0f, 0.0f);
        glVertex2f(_X + (float)((int)width), _Y);
        glEnd();
        glDisable(3553);
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        glColor4f(red, green, blue, alpha);
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glPushMatrix();
        glLineWidth(1.0f);
        glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            glVertex2d((double)x + Math.sin((double)i * Math.PI / 180.0) * (double)radius, (double)y + Math.cos((double)i * Math.PI / 180.0) * (double)radius);
        }
        glEnd();
        glPopMatrix();
        glEnable(3553);
        glDisable(2848);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }

    public static void setColor(int color) {
        glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
    }

    public static void drawEntityBox(Entity entity, Color color, boolean fullBox, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(770, 771);
        glEnable(3042);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        glDisable(2929);
        GlStateManager.depthMask(false);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) Minecraft.timer.renderPartialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) Minecraft.timer.renderPartialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) Minecraft.timer.renderPartialTicks - RenderManager.renderPosZ;
        AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX - entity.posX + x - 0.05, axisAlignedBB.minY - entity.posY + y, axisAlignedBB.minZ - entity.posZ + z - 0.05, axisAlignedBB.maxX - entity.posX + x + 0.05, axisAlignedBB.maxY - entity.posY + y + 0.15, axisAlignedBB.maxZ - entity.posZ + z + 0.05);
        GlStateManager.glLineWidth(2.0f);
        glEnable(2848);
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, alpha);
        if (fullBox) {
            drawColorBox(axisAlignedBB2, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, alpha);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
        }
        drawSelectionBoundingBox(axisAlignedBB2);
        GlStateManager.glLineWidth(2.0f);
        enableTexture2D();
        glEnable(2929);
        GlStateManager.depthMask(true);
        disableBlend();
        GlStateManager.popMatrix();
    }

    public static void blockEsp(BlockPos blockPos, Color c, boolean outline) {
        double x = (double)blockPos.getX() - RenderManager.renderPosX;
        double y = (double)blockPos.getY() - RenderManager.renderPosY;
        double z = (double)blockPos.getZ() - RenderManager.renderPosZ;
        glPushMatrix();
        glBlendFunc(770, 771);
        glEnable(3042);
        glLineWidth(2.0f);
        glDisable(3553);
        glDisable(2929);
        glDepthMask(false);
        GlStateManager.color((float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f, 0.15f);
        drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
            drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        }
        glLineWidth(2.0f);
        glEnable(3553);
        glEnable(2929);
        glDepthMask(true);
        glDisable(3042);
        glPopMatrix();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawCircle(Color color, float x, float y, float radius, int start, int end) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        color(color);
        glEnable(2848);
        glLineWidth(2.0f);
        glBegin(3);
        for (float i = (float)end; i >= (float)start; i -= 4.0f) {
            glVertex2f((float)((double)x + Math.cos((double)i * Math.PI / 180.0) * (double)(radius * 1.001f)), (float)((double)y + Math.sin((double)i * Math.PI / 180.0) * (double)(radius * 1.001f)));
        }
        glEnd();
        glDisable(2848);
        enableTexture2D();
        disableBlend();
    }

    public static void drawCircle3D(Entity entity, double radius, float partialTicks, int points, float width, int color) {
        glPushMatrix();
        glDisable(3553);
        glEnable(2848);
        glHint(3154, 4354);
        glDisable(2929);
        glLineWidth(width);
        glEnable(3042);
        glBlendFunc(770, 771);
        glDisable(2929);
        glBegin(3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
        setColor(color);
        for (int i = 0; i <= points; ++i) {
            glVertex3d(x + radius * Math.cos((float)i * ((float)Math.PI * 2) / (float)points), y, z + radius * Math.sin((float)i * ((float)Math.PI * 2) / (float)points));
        }
        glEnd();
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
        glDisable(2848);
        glEnable(2929);
        glEnable(3553);
        glPopMatrix();
    }
    public static void drawCircle3DA(Entity entity, double radius, float partialTicks, int points, float width, int color) {
        glPushMatrix();
        glDisable(3553);
        glEnable(2848);
        glHint(3154, 4354);
        glDisable(2929);
        glLineWidth(width);
        glEnable(3042);
        glBlendFunc(770, 771);
        glDisable(2929);
        glBegin(3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
        setColor(color);
        for (int i = 0; i <= points; ++i) {
            boolean fal = TargetESP.astolfo.get();
            Color color1 =!fal ? ColorUtil.rainbow(7, points, 1, 1, .5f) : ColorUtil.skyRainbow(7,points);

            glVertex3d(x + radius * Math.cos((float)i * ((float)Math.PI * 2) / (float)points), y, z + radius * Math.sin((float)i * ((float)Math.PI * 2) / (float)points));
        }
        glEnd();
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
        glDisable(2848);
        glEnable(2929);
        glEnable(3553);
        glPopMatrix();
    }

    public static void color(float red, float green, float blue, float alpha) {
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void color(float red, float green, float blue) {
        color(red, green, blue, 1.0f);
    }

    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }

    public static void color(int hexColor) {
        float red = (float)(hexColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(hexColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(hexColor & 0xFF) / 255.0f;
        float alpha = (float)(hexColor >> 24 & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }

    public static void drawArrow(float x, float y, float scale, float width, boolean up, int hexColor) {
        glPushMatrix();
        glScaled(scale, scale, scale);
        glEnable(2848);
        glDisable(3553);
        color(hexColor);
        glLineWidth(width);
        glBegin(1);
        glVertex2d(x /= scale, (y /= scale) + (float)(up ? 4 : 0));
        glVertex2d(x + 3.0f, y + (float)(up ? 0 : 4));
        glEnd();
        glBegin(1);
        glVertex2d(x + 3.0f, y + (float)(up ? 0 : 4));
        glVertex2d(x + 6.0f, y + (float)(up ? 4 : 0));
        glEnd();
        glEnable(3553);
        glDisable(2848);
        glPopMatrix();
    }

    public static void drawCircle(float xx, float yy, float radius, Color col, float width, float position, float round) {
        int sections = 100;
        double dAngle = (double)(round * 2.0f) * Math.PI / (double)sections;
        float x = 0.0f;
        float y = 0.0f;
        glPushMatrix();
        glEnable(3042);
        glDisable(3553);
        glBlendFunc(770, 771);
        glEnable(2848);
        glLineWidth(width);
        glShadeModel(7425);
        glBegin(2);
        int i = (int)position;
        while ((float)i < position + (float)sections) {
            x = (float)((double)radius * Math.cos((double)i * dAngle));
            y = (float)((double)radius * Math.sin((double)i * dAngle));
            glColor4f((float)col.getRed() / 255.0f, (float)col.getGreen() / 255.0f, (float)col.getBlue() / 255.0f, (float)col.getAlpha() / 255.0f);
            glVertex2f(xx + x, yy + y);
            ++i;
        }
        for (i = (int)(position + (float)sections); i > (int)position; --i) {
            x = (float)((double)radius * Math.cos((double)i * dAngle));
            y = (float)((double)radius * Math.sin((double)i * dAngle));
            glColor4f((float)col.getRed() / 255.0f, (float)col.getGreen() / 255.0f, (float)col.getBlue() / 255.0f, (float)col.getAlpha() / 255.0f);
            glVertex2f(xx + x, yy + y);
        }
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        glEnd();
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        glPopMatrix();
    }

    public static void drawImage(Namespaced namespaced, float x, float y, float width, float height, Color color) {
        glPushMatrix();
        glDisable(2929);
        glEnable(3042);
        glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
        Minecraft.getTextureManager().bindTexture(namespaced);
        glTexParameteri(3553, 10240, 9729);
        glTexParameteri(3553, 10241, 9729);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        glDepthMask(true);
        glDisable(3042);
        glEnable(2929);
        glPopMatrix();
    }

    private static void transform(Vector4f vec, Matrix4f matrix) {
        float x = vec.x;
        float y = vec.y;
        float z = vec.z;
        vec.x = x * matrix.m00 + y * matrix.m10 + z * matrix.m20 + matrix.m30;
        vec.y = x * matrix.m01 + y * matrix.m11 + z * matrix.m21 + matrix.m31;
        vec.z = x * matrix.m02 + y * matrix.m12 + z * matrix.m22 + matrix.m32;
        vec.w = x * matrix.m03 + y * matrix.m13 + z * matrix.m23 + matrix.m33;
    }

    private static boolean isVisible(Vector4f pos, int width, int height) {
        double wid = width;
        double position = pos.x;
        if (position >= 0.0 && position <= wid) {
            wid = height;
            position = pos.y;
            return position >= 0.0 && position <= wid;
        }
        return false;
    }

    public static void renderItem(ItemStack itemStack, int x, int y) {
        enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        GlStateManager.enableDepth();
        GlStateManager.disableAlpha();
        Minecraft.getRenderItem().zLevel = -150.0f;
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        Minecraft.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        Minecraft.getRenderItem().renderItemOverlays(Minecraft.font, itemStack, x, y);
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        Minecraft.getRenderItem().zLevel = 0.0f;
    }

    public static void drawColorBox(AxisAlignedBB axisalignedbb, float red, float green, float blue, float alpha) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder buffer = ts.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        buffer.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
    }

    public static boolean isInViewFrustum(Entity entity) {
        return isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    private static boolean isInViewFrustum(AxisAlignedBB bb) {
        Entity current = Minecraft.getRenderViewEntity();
        if (current != null) {
            frustum.setPosition(current.posX, current.posY, current.posZ);
        }
        return frustum.isBoundingBoxInFrustum(bb);
    }

    public static void blockEsp(BlockPos blockPos, Color c, boolean outline, double length, double length2) {
        double x = (double)blockPos.getX() - RenderManager.renderPosX;
        double y = (double)blockPos.getY() - RenderManager.renderPosY;
        double z = (double)blockPos.getZ() - RenderManager.renderPosZ;
        glPushMatrix();
        glBlendFunc(770, 771);
        glEnable(3042);
        glLineWidth(2.0f);
        glDisable(3553);
        glDisable(2929);
        glDepthMask(false);
        GlStateManager.color((float)c.getRed() / 255.0f, (float)c.getGreen() / 255.0f, (float)c.getBlue() / 255.0f, 0.15f);
        drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
            drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length));
        }
        glLineWidth(2.0f);
        glEnable(3553);
        glEnable(2929);
        glDepthMask(true);
        glDisable(3042);
        glPopMatrix();
    }

    public static void drawTriangle(float x, float y, float size, float vector, int color) {
        glTranslated(x, y, 0.0);
        glRotatef(180.0f + vector, 0.0f, 0.0f, 1.0f);
        float alpha = (float)(color >> 24 & 0xFF) / 255.0f;
        float red = (float)(color >> 16 & 0xFF) / 255.0f;
        float green = (float)(color >> 8 & 0xFF) / 255.0f;
        float blue = (float)(color & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
        glEnable(3042);
        glDisable(3553);
        glEnable(2848);
        glBlendFunc(770, 771);
        glLineWidth(1.0f);
        glBegin(6);
        glVertex2d(0.0, size);
        glVertex2d(1.0f * size, -size);
        glVertex2d(-(1.0f * size), -size);
        glEnd();
        glDisable(2848);
        glEnable(3553);
        glDisable(3042);
        glRotatef(-180.0f - vector, 0.0f, 0.0f, 1.0f);
        glTranslated(-x, -y, 0.0);
    }

    static {
        shader = new Namespaced("shaders/post/blur.json");
        blurSpotCache = new HashMap();
    }
}
