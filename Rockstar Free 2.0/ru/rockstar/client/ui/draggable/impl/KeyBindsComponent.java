package ru.rockstar.client.ui.draggable.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.Translate;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.display.ClientFont;
import ru.rockstar.client.features.impl.display.KeyBinds;
import ru.rockstar.client.features.impl.display.Keystrokes;
import ru.rockstar.client.ui.draggable.DraggableModule;

public class KeyBindsComponent extends DraggableModule {
	public int lastA = 0;
    public int lastW = 0;
    public int lastS = 0;
    public int lastD = 0;
    public int lastJ = 0;
    public int lastS2 = 0;
    public long deltaAnim;
    float listOffset = 8.0F;
    public static int x;
    public static int y;
    
    public KeyBindsComponent() {
        super("KeyBindsComponent", 500, 600);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public void render(int mouseX, int mouseY) {
    	double width = sr.getScaledWidth();
        float yDist = 1;
        int yTotal = 0;
        for (int i = 0; i < Main.instance.featureDirector.getFeatureList().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
        }

        if (Main.instance.featureDirector.getFeatureByClass(KeyBinds.class).isToggled()) {
            Main.instance.featureDirector.getFeatureList().sort(Comparator.comparing(module -> !ClientFont.minecraftfont.getBoolValue() ? -ClientHelper.getFontRender().getStringWidth(module.getModuleName()) : -mc.fontRendererObj.getStringWidth(module.getModuleName())));
            for (Feature feature : Main.instance.featureDirector.getFeatureList()) {
                		Translate translate = feature.getTranslate();
                        String moduleLabel = feature.getModuleName();
                        float length = !ClientFont.minecraftfont.getBoolValue() ? ClientHelper.getFontRender().getStringWidth(moduleLabel) : mc.fontRendererObj.getStringWidth(moduleLabel);
                        float featureX = (float) (width - length);
                        boolean enable = feature.isToggled();
                        if (enable) {
                            translate.arrayListAnim(featureX, yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                        } else {
                            translate.arrayListAnim((float) (width), yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                        }
                        x = getX();
                        y = getY();
                        double translateY = translate.getY();
                        float y = KeyBinds.fonty.getNumberValue();
                        
                        

                        int yPotion = 0;

                        for (PotionEffect potion : mc.player.getActivePotionEffects()) {
                            if (potion.getPotion().isBeneficial()) {
                                yPotion = 26;
                            }
                            if (potion.getPotion().isBadEffect()) {
                                yPotion = 26 * 2;
                            }
                        }

                        double translateX = translate.getX() + 1.5;
                        int color = 0;
                        

                        double time = KeyBinds.time.getNumberValue();
                        String mode = KeyBinds.arrayColor.getOptions();
                        boolean visible = translate.getX() < width;
                        if (visible) {
                            switch (mode.toLowerCase()) {
                                case "rainbow":
                                    color = DrawHelper.rainbowNew((int) (yDist * 200 * 0.1f), 0.8f, 1.0f);
                                    break;
                                case "astolfo":
                                    color = DrawHelper.astolfoColors45(yDist, yTotal, 0.5f, 5).getRGB();
                                    break;
                                case "pulse":
                                    color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0F * (yDist * 2.55) / 60).getRGB();
                                    break;
                                case "custom":
                                    color = DrawHelper.TwoColoreffect(new Color(ru.rockstar.client.features.impl.display.ArrayList.onecolor.getColorValue()), new Color(ru.rockstar.client.features.impl.display.ArrayList.twocolor.getColorValue()), (double) Math.abs(System.currentTimeMillis() / (long) time) / 100.0D + 3.0D * (yDist * 2.55D / 60.0D)).getRGB();
                                    break;
                                case "none":
                                    color = -1;
                                    break;
                            }
                            GlStateManager.pushMatrix();
                            DrawHelper.drawRect(sr.getScaledHeight() - x - 14 + 123, y - 1, sr.getScaledHeight() - x + 14 + 197, y + 9, new Color(30,30,30,255).getRGB());
                            DrawHelper.drawGradientRect1(sr.getScaledHeight() - x - 14 + 120, y - 15, sr.getScaledHeight() - x + 14 + 200, y - 1,ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                            mc.mntsb_16.drawStringWithShadow("KeyBinds", sr.getScaledHeight() - x - 10 + 130 + mc.mntsb.getStringWidth("KeyBinds") / 2, y - 10, -1);
                            GlStateManager.translate(-KeyBinds.x.getNumberValue(), KeyBinds.y.getNumberValue(), 1.0D);
                            Feature nextModule = null;
                            int nextIndex = Main.instance.instance.featureDirector.getFeatureList().indexOf(feature) + 1;

                                String modeArrayFont = ClientFont.fontMode.getOptions();

                            
                            yDist += listOffset;

                            GlStateManager.popMatrix();
                }
            }
        }
        super.render(mouseX, mouseY);
    }

    private static Feature getNextEnabledModule(ArrayList<Feature> features, int startingIndex) {
        for (int i = startingIndex; i < features.size(); i++) {
            Feature feature = features.get(i);
            if (feature.isToggled()) {
                if (!feature.getModuleName().equals("ClickGui")) {
                    return feature;
                }
            }
        }
        return null;
    }
    @Override
    public void draw() {
    	double width = sr.getScaledWidth();
        float yDist = 1;
        int yTotal = 0;
        for (int i = 0; i < Main.instance.featureDirector.getFeatureList().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
        }

        if (Main.instance.featureDirector.getFeatureByClass(KeyBinds.class).isToggled()) {
            Main.instance.featureDirector.getFeatureList().sort(Comparator.comparing(module -> !ClientFont.minecraftfont.getBoolValue() ? -ClientHelper.getFontRender().getStringWidth(module.getModuleName()) : -mc.fontRendererObj.getStringWidth(module.getModuleName())));
            for (Feature feature : Main.instance.featureDirector.getFeatureList()) {
                		Translate translate = feature.getTranslate();
                        String moduleLabel = feature.getModuleName();
                        float length = !ClientFont.minecraftfont.getBoolValue() ? ClientHelper.getFontRender().getStringWidth(moduleLabel) : mc.fontRendererObj.getStringWidth(moduleLabel);
                        float featureX = (float) (width - length);
                        boolean enable = feature.isToggled();
                        if (enable) {
                            translate.arrayListAnim(featureX, yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                        } else {
                            translate.arrayListAnim((float) (width), yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                        }
                        x = getX();
                        y = getY();
                        double translateY = translate.getY();
                        float y = KeyBinds.fonty.getNumberValue();
                        
                        

                        int yPotion = 0;

                        for (PotionEffect potion : mc.player.getActivePotionEffects()) {
                            if (potion.getPotion().isBeneficial()) {
                                yPotion = 26;
                            }
                            if (potion.getPotion().isBadEffect()) {
                                yPotion = 26 * 2;
                            }
                        }

                        double translateX = translate.getX() + 1.5;
                        int color = 0;
                        

                        double time = KeyBinds.time.getNumberValue();
                        String mode = KeyBinds.arrayColor.getOptions();
                        boolean visible = translate.getX() < width;
                        if (visible) {
                            switch (mode.toLowerCase()) {
                                case "rainbow":
                                    color = DrawHelper.rainbowNew((int) (yDist * 200 * 0.1f), 0.8f, 1.0f);
                                    break;
                                case "astolfo":
                                    color = DrawHelper.astolfoColors45(yDist, yTotal, 0.5f, 5).getRGB();
                                    break;
                                case "pulse":
                                    color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0F * (yDist * 2.55) / 60).getRGB();
                                    break;
                                case "custom":
                                    color = DrawHelper.TwoColoreffect(new Color(ru.rockstar.client.features.impl.display.ArrayList.onecolor.getColorValue()), new Color(ru.rockstar.client.features.impl.display.ArrayList.twocolor.getColorValue()), (double) Math.abs(System.currentTimeMillis() / (long) time) / 100.0D + 3.0D * (yDist * 2.55D / 60.0D)).getRGB();
                                    break;
                                case "none":
                                    color = -1;
                                    break;
                            }
                            GlStateManager.pushMatrix();
                            DrawHelper.drawRect(sr.getScaledHeight() - x - 14 + 123, y - 1, sr.getScaledHeight() - x + 14 + 197, y + 9, new Color(30,30,30,255).getRGB());
                            DrawHelper.drawGradientRect1(sr.getScaledHeight() - x - 14 + 120, y - 15, sr.getScaledHeight() - x + 14 + 200, y - 1,ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                            mc.mntsb_16.drawStringWithShadow("KeyBinds", sr.getScaledHeight() - x - 10 + 130 + mc.mntsb.getStringWidth("KeyBinds") / 2, y - 10, -1);
                            GlStateManager.translate(-KeyBinds.x.getNumberValue(), KeyBinds.y.getNumberValue(), 1.0D);
                            Feature nextModule = null;
                            int nextIndex = Main.instance.instance.featureDirector.getFeatureList().indexOf(feature) + 1;

                                String modeArrayFont = ClientFont.fontMode.getOptions();

                            
                            yDist += listOffset;

                            GlStateManager.popMatrix();
                }
            }
        }
        super.draw();
    }
}