package ru.rockstar.client.features.impl.display;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import optifine.CustomColors;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventRender2D;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.api.utils.render.Translate;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.lwjgl.input.Keyboard;

public class HUD extends Feature {
    float xd = 0.0F;
    private static FontRenderer font;
    public static String clientName;
    public static float count = 0.0F;
    public TimerHelper timer;
    double potionCheck = 0.0D;
    ScaledResolution sr = new ScaledResolution(mc);
    double width = sr.getScaledWidth() - 2;
    public static double time = 4;
    float listOffset = 8.0F;
    float yDist = 4.0F;
    int yTotal = 0;

    Feature nextModule = null;
    int colorArray = -1;
    public static float globalOffset;

    public HUD() {
        super("HUD", "���������� ���������� �� ������", 0, Category.DISPLAY);

    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        float target = mc.currentScreen instanceof GuiChat ? 15.0F : 0.0F;
        float delta = globalOffset - target;
        globalOffset -= delta / (float) Math.max(1, Minecraft.getDebugFPS()) * 10.0F;
        if (!Double.isFinite(globalOffset)) {
            globalOffset = 0.0F;
        }

        if (globalOffset > 15.0F) {
            globalOffset = 15.0F;
        }

        if (globalOffset < 0.0F) {
            globalOffset = 0.0F;
        }
        this.hotBar();
        renderPotionStatus(e.getResolution());
    }

    public void hotBar() {
        if (!Main.instance.featureDirector.getFeatureByClass(Hotbar.class).isToggled()) {
            ScaledResolution sr = new ScaledResolution(mc);
       //     this.xd = AnimationHelper.animation(this.xd, mc.currentScreen instanceof GuiChat ? (float) (sr.getScaledHeight() - 22) : (float) (sr.getScaledHeight() - 9), 1.0E-4F);
         //   double prevX = mc.player.posX - mc.player.prevPosX;
         //   double prevZ = mc.player.posZ - mc.player.prevPosZ;
         //   double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ);
        //    double currSpeed = lastDist * 15.3571428571D;
//
          //  String speed = String.format("%.2f blocks/sec", currSpeed);
        //    NetHandlerPlayClient var12 = Objects.requireNonNull(mc.getConnection());
         //   float var10003 = this.xd + -9.0F;
        //    if (!ClientFont.minecraftfont.getBoolValue()) {
     // //        String cords = "X:" + (int) mc.player.posX + " Y:" + (int) mc.player.posY + " Z:" + (int) mc.player.posZ;
       //  /   //    ClientHelper.getFontRender().drawStringWithShadow(cords, 3, var10003 + 10, -1);
       //     //   ClientHelper.getFontRender().drawStringWithShadow("FPS: " + ChatFormatting.GRAY + Minecraft.getDebugFPS(), 2.0F, this.xd + -18.0F, -1);
           //     ClientHelper.getFontRender().drawStringWithShadow(speed, 2.0F, var10003, -1);
         //   } else {
        //        String cords = "X:" + (int) mc.player.posX + " Y:" + (int) mc.player.posY + " Z:" + (int) mc.player.posZ;
            //    mc.fontRendererObj.drawStringWithShadow(cords, 3, var10003 + 10, -1);
             //   mc.fontRendererObj.drawStringWithShadow("FPS: " + ChatFormatting.GRAY + Minecraft.getDebugFPS(), 2.0F, this.xd + -18.0F, -1);
            //    mc.fontRendererObj.drawStringWithShadow(speed, 2.0F, var10003, -1);
            }
        }
    

    public static void renderPotionStatus(ScaledResolution scaledResolution) {
        float offset = globalOffset;
        float pY = -2;

        List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
        potions.sort(Comparator.comparingDouble(effect -> mc.fontRendererObj.getStringWidth((Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName()))));

        for (PotionEffect effect : potions) {
            Potion potion = Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()));
            assert potion != null;
            String name = I18n.format(potion.getName());
            String PType = "";

            if (effect.getAmplifier() == 1) {
                name = name + " 2";
            } else if (effect.getAmplifier() == 2) {
                name = name + " 3";
            } else if (effect.getAmplifier() == 3) {
                name = name + " 4";
            }

            if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
                PType = PType + " " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = PType + " " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = PType + " " + Potion.getDurationString(effect);
            }

            int getPotionColor = -1;
            if ((effect.getDuration() < 200)) {
                getPotionColor = new Color(215, 59, 59).getRGB();
            } else if (effect.getDuration() < 400) {
                getPotionColor = new Color(231, 143, 32).getRGB();
            } else if (effect.getDuration() > 400) {
                getPotionColor = new Color(172, 171, 171).getRGB();
            }
            if (!ClientFont.minecraftfont.getBoolValue()) {
                mc.fontRendererObj.drawStringWithShadow(name, scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(name + PType) - 3, scaledResolution.getScaledHeight() - 28 + pY - offset, potion.getLiquidColor());
                mc.fontRendererObj.drawStringWithShadow(PType, scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType) - 2, scaledResolution.getScaledHeight() - 28 + pY - offset, getPotionColor);
            } else {
                ClientHelper.getFontRender().drawStringWithShadow(name, scaledResolution.getScaledWidth() - ClientHelper.getFontRender().getStringWidth(name + PType) - 3, scaledResolution.getScaledHeight() - 28 + pY - offset, potion.getLiquidColor());
                ClientHelper.getFontRender().drawStringWithShadow(PType, scaledResolution.getScaledWidth() - ClientHelper.getFontRender().getStringWidth(PType) - 2, scaledResolution.getScaledHeight() - 28 + pY - offset, getPotionColor);
            }
            pY -= 11;
        }
    }

    public void renderArrayList(ScaledResolution sr) {
    	/*
        double width = sr.getScaledWidth() - (ArreyList.rectRight.getBoolValue() ? 1 : 0);
        float yDist = 1;
        int yTotal = 0;
        for (int i = 0; i < Main.instance.featureDirector.getFeatureList().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
        }

        if (Main.instance.featureDirector.getFeatureByClass(ArreyList.class).isToggled()) {
            Main.instance.featureDirector.getFeatureList().sort(Comparator.comparing(module -> !ClientFont.minecraftfont.getBoolValue() ? -ClientHelper.getFontRender().getStringWidth(module.getModuleName()) : -mc.fontRendererObj.getStringWidth(module.getModuleName())));
            for (Feature feature : Main.instance.featureDirector.getFeatureList()) {
                if (!feature.getModuleName().equals("ClickGui")) {
                    Translate translate = feature.getTranslate();
                    String moduleLabel = feature.getModuleName();
                    float listOffset = ArreyList.height.getNumberValue();
                    float length = !ClientFont.minecraftfont.getBoolValue() ? ClientHelper.getFontRender().getStringWidth(moduleLabel) : mc.fontRendererObj.getStringWidth(moduleLabel);
                    float featureX = (float) (width - length);
                    boolean enable = feature.isToggled();
                    if (enable) {
                        translate.arrayListAnim(featureX, yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                    } else {
                        translate.arrayListAnim((float) (width), yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                    }

                    int yPotion = 0;

                    for (PotionEffect potion : mc.player.getActivePotionEffects()) {
                        if (potion.getPotion().isBeneficial()) {
                            yPotion = 26;
                        }
                        if (potion.getPotion().isBadEffect()) {
                            yPotion = 26 * 2;
                        }
                    }

                    double translateY = translate.getY();
                    double translateX = translate.getX() - (ArreyList.rectRight.getBoolValue() ? 2.5 : 1.5);
                    int color = 0;
                    int color2 = 0;

                    double time = ArreyList.time.getNumberValue();
                    String mode = ArreyList.arrayColor.getOptions();
                    String mode4 = ArreyList.bgColor.getOptions();
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
                                color = DrawHelper.TwoColoreffect(new Color(ArreyList.onecolor.getColorValue()), new Color(ArreyList.twocolor.getColorValue()), (double) Math.abs(System.currentTimeMillis() / (long) time) / 100.0D + 3.0D * (yDist * 2.55D / 60.0D)).getRGB();
                                break;
                            case "none":
                                color = -1;
                                break;
                        }
                        
                        switch (mode4.toLowerCase()) {
                        case "rainbow":
                            color2 = DrawHelper.rainbowNew((int) (yDist * 200 * 0.1f), 0.8f, 1.0f);
                            break;
                        case "astolfo":
                            color2 = DrawHelper.astolfoColors45(yDist, yTotal, 0.5f, 5).getRGB();
                            break;
                        case "pulse":
                            color2 = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0F * (yDist * 2.55) / 60).getRGB();
                            break;
                        case "custom":
                            color2 = DrawHelper.TwoColoreffect(new Color(ArreyList.bgonecolor.getColorValue()), new Color(ArreyList.bgtwocolor.getColorValue()), (double) Math.abs(System.currentTimeMillis() / (long) time) / 100.0D + 3.0D * (yDist * 2.55D / 60.0D)).getRGB();
                            break;
                        case "none":
                            color2 = -1;
                            break;
                    }
                        //  - ClientHelper.getFontRender().getStringWidth(moduleLabel) / 2
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(-ArreyList.x.getNumberValue(), ArreyList.y.getNumberValue(), 1.0D);
                        if (ArreyList.shadow.getBoolValue()) {
                            DrawHelper.drawGlow(translateX + 2D, translateY - 6D, width, translateY + listOffset + 5D, ArreyList.shadowcolor.getColorValue());
                        }
                        if (ArreyList.backGround.getBoolValue()) {
                        	DrawHelper.drawGradientRect1(translateX - 2f, translateY - 1f, width, translateY + listOffset - 1D, color2,  color2);
                        }
                        if (ArreyList.border.getBoolValue()) {
                            DrawHelper.drawNewRect(translateX - 2.6D, translateY - 1, translateX - 2D, translateY + listOffset - 1D, color);
                        }
                        DrawHelper.drawGlow(0, 0,1, 1, new Color(0,0,0).getRGB());
                        Feature nextModule = null;
                        int nextIndex = Main.instance.instance.featureDirector.getFeatureList().indexOf(feature) + 1;
                        if (Main.instance.instance.featureDirector.getFeatureList().size() > nextIndex) {
                            nextModule = getNextEnabledModule((ArrayList<Feature>) Main.instance.instance.featureDirector.getFeatureList(), nextIndex);
                        }

                        if (!ClientFont.minecraftfont.getBoolValue()) {
                            String modeArrayFont = ClientFont.fontMode.getOptions();
                            float y = ArreyList.fonty.getNumberValue();
                            if (!ClientFont.minecraftfont.getBoolValue()) {
                                ClientHelper.getFontRender().drawStringWithShadow(moduleLabel, translateX - 0.5f, translateY + y - 1, color);
                            }
                        } else {
                            mc.fontRendererObj.drawStringWithShadow(moduleLabel, (float) translateX, (float) translateY + 1F, color);
                        }

                        if (ArreyList.rectRight.getBoolValue()) {
                            DrawHelper.drawNewRect(width, translateY - 1, width + 1, translateY + listOffset - 1.0D, color);
                        }
                        if (nextModule != null) {
                            double font = !ClientFont.minecraftfont.getBoolValue() ? ClientHelper.getFontRender().getStringWidth(nextModule.getModuleName()) : mc.fontRendererObj.getStringWidth(nextModule.getModuleName());
                            double dif = (length - font);
                            if (ArreyList.border.getBoolValue()) {
                            	String mode2 = ArreyList.borderMode.getOptions();
                            	if (mode2.equalsIgnoreCase("All")) {
                            		DrawHelper.drawNewRect(translateX - 2.6D, translateY + listOffset - 1D, width, translateY + (double) listOffset - 0.6, color);
                            	} if (mode2.equalsIgnoreCase("Single")) {
                            		DrawHelper.drawNewRect(translateX - 2, translateY - 1, translateX - 1, translateY + listOffset - 1.0D, color);
                            	}
                            }
                        } else {
                        	String mode2 = ArreyList.borderMode.getOptions();
                            if (ArreyList.border.getBoolValue()) {
                            	if (mode2.equalsIgnoreCase("All")) {
                            		DrawHelper.drawNewRect(translateX - 2.6D, translateY + listOffset - 1D, width, translateY + (double) listOffset - 0.6, color);
                            	} if (mode2.equalsIgnoreCase("Single")) {
                            		DrawHelper.drawNewRect(translateX - 2, translateY - 1, translateX - 1, translateY + listOffset - 1.0D, color);
                            	}
                            }
                        }

                        yDist += listOffset;

                        GlStateManager.popMatrix();
                    }
                }
            }
        }*/
    }
    
    public void renderKeyBinds(ScaledResolution sr) {
    	double width = sr.getScaledWidth();
        float yDist = 1;
        int yTotal = 0;
        for (int i = 0; i < Main.instance.featureDirector.getFeatureList().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
        }
        float listOffset2 = 8.0F;
        if (Main.instance.featureDirector.getFeatureByClass(KeyBinds.class).isToggled()) {
            Main.instance.featureDirector.getFeatureList().sort(Comparator.comparing(module -> !ClientFont.minecraftfont.getBoolValue() ? -ClientHelper.getFontRender().getStringWidth(module.getModuleName()) : -mc.fontRendererObj.getStringWidth(module.getModuleName())));
            for (Feature feature : Main.instance.featureDirector.getFeatureList()) {
                if (!feature.getModuleName().equals("ClickGui")) {
                	if (feature.getKey() != 0) {
                		Translate translate = feature.getTranslate();
                        String moduleLabel = feature.getLabel();
                        float length = !ClientFont.minecraftfont.getBoolValue() ? ClientHelper.getFontRender().getStringWidth(moduleLabel) : mc.fontRendererObj.getStringWidth(moduleLabel);
                        float featureX = (float) (width - length);
                        boolean enable = feature.isToggled();
                        if (enable) {
                            translate.arrayListAnim(featureX, yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                        } else {
                            translate.arrayListAnim((float) (width), yDist, (float) (0.1f * Minecraft.frameTime) / 6, (float) (0.3f * Minecraft.frameTime) / 6);
                        }
                        float x2 = KeyBinds.x.getNumberValue();
                        float y2 = KeyBinds.y.getNumberValue();
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
                        

                        boolean visible = translate.getX() < width;
                        if (visible) {
                                    color = -1;
                            GlStateManager.pushMatrix();
                            if (KeyBinds.mode.getCurrentMode().equalsIgnoreCase("Rockstar New")) {
                                RoundedUtil.drawHorizontalGradientOutlinedRoundedRect(sr.getScaledHeight() - x2 - 14 + 123 - 4, y2 - 15, 110.5f, (float) (translateY + 9 + 15), 7, 1.5f, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
                            } else if (KeyBinds.mode.getCurrentMode().equalsIgnoreCase("Rockstar")) {
                                DrawHelper.drawGradientRoundedRect(sr.getScaledHeight() - x2 - 14 + 123 - 4, y2 - 15, 110.5f, translateY + 9 + 15, 7, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
                            } else if (KeyBinds.mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {
                            	Color l1 = ClientHelper.getClientColor().brighter();
                                Color l2 = ClientHelper.getClientColor();
                            	
                            	RoundedUtil.drawRound(sr.getScaledHeight() - x2 - 14 + 123 - 4, y2 - 15, 110.5f, (float) (translateY + 9 + 15) + 2, 5, new Color(40,40,40));
                            	RoundedUtil.drawGradientRound(sr.getScaledHeight() - x2 - 14 + 123 - 4 + 2, y2 - 15 + 2, 12, 12, 4, l2, l1, l1, l2);
                            	
                            	mc.i30.drawString("k", sr.getScaledHeight() - x2 - 14 + 123 - 4 + 2 + .5f, y2 - 15 + 2 + 2.5f, -1);
                            	 
                            	Gui.drawRect(sr.getScaledHeight() - x2 - 14 + 123 - 4 + 2 + .5f,  y2 - 15 + 2 + 2.5f + 10.5f, sr.getScaledHeight() - x2 - 14 + 123 - 4 + 2 + .5f + 20,  y2 - 15 + 2 + 2.5f + 5 + 10, new Color(40,40,40).getRGB());
                                  
                            }

                        if (KeyBinds.mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {
                            mc.mntsb_20.drawStringWithShadow("Бинды", sr.getScaledHeight() - x2 - 10 + 140 + mc.mntsb.getStringWidth("Бинды") / 2 - 22, y2 - 10 - 1, -1);
                        } else {
                        	mc.mntsb_16.drawStringWithShadow("Бинды", sr.getScaledHeight() - x2 - 10 + 140 + mc.mntsb.getStringWidth("Бинды") / 2, y2 - 10, -1);
                        }
            
                            GlStateManager.translate(-KeyBinds.x.getNumberValue(), KeyBinds.y.getNumberValue(), 1.0D);
                            if (moduleLabel != null) {
                            } else {
                            	mc.mntsb.drawStringWithShadow("None", sr.getScaledHeight() - x2 - mc.mntsb.getStringWidth("None") / 2 + 384, translateY + y - 1 + 1, color);
                            }
                            Feature nextModule = null;
                            int nextIndex = Main.instance.instance.featureDirector.getFeatureList().indexOf(feature) + 1;
                            if (Main.instance.instance.featureDirector.getFeatureList().size() > nextIndex) {
                                nextModule = getNextEnabledModule((ArrayList<Feature>) Main.instance.instance.featureDirector.getFeatureList(), nextIndex);
                            }

                                String modeArrayFont = ClientFont.fontMode.getOptions();
                                
                                if (moduleLabel != null) {
                                	if (KeyBinds.mode.getCurrentMode().equalsIgnoreCase("Rockstar Styled")) {
                                	mc.mntsb.drawString(TextFormatting.GRAY + moduleLabel + " [" + Keyboard.getKeyName(feature.getKey()) + "]", sr.getScaledHeight() - 225 - mc.mntsb.getStringWidth(moduleLabel + " [" + Keyboard.getKeyName(feature.getKey()) + "]") / 2 + 386, (float) (translateY + y - 1 + 1 + 1.5f), color);
                                	} else {
                                	mc.mntsb.drawStringWithShadow(moduleLabel + TextFormatting.DARK_GRAY + " [" + Keyboard.getKeyName(feature.getKey()) + "]", sr.getScaledHeight() - 225 - mc.mntsb.getStringWidth(moduleLabel + " [" + Keyboard.getKeyName(feature.getKey()) + "]") / 2 + 386, translateY + y - 1 + 1, color);
                                }
                                	} else {
                                	mc.mntsb.drawStringWithShadow("None", sr.getScaledHeight() - x2 - mc.mntsb.getStringWidth("None") / 2 + 384, translateY + y - 1 + 1, color);
                                }

                            if (nextModule != null) {
                                double font = !ClientFont.minecraftfont.getBoolValue() ? ClientHelper.getFontRender().getStringWidth(nextModule.getModuleName()) : mc.fontRendererObj.getStringWidth(nextModule.getModuleName());
                                double dif = (length - font);
                            }

                            
                            yDist += listOffset2;

                            GlStateManager.popMatrix();
                        }
                	}
                }
            }
        }
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


    public static int rainbow(int delay, long index) {
        double rainbowState = Math.ceil((double) (System.currentTimeMillis() + index + (long) delay)) / 15.0D;
        rainbowState %= 360.0D;
        return Color.getHSBColor((float) (rainbowState / 360.0D), 0.4F, 1.0F).getRGB();

    }
}