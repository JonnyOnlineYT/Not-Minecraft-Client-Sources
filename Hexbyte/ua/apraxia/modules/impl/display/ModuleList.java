package ua.apraxia.modules.impl.display;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.opengl.GL11;
import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.impl.FeatureList;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.animation.AnimationUtil;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.other.ColorUtility;
import ua.apraxia.utility.render.RenderUtility;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ModuleList extends Module {

    public static ModeSetting mode228 = new ModeSetting("Color", "Fade", "Astolfo", "Rainbow", "Custom");
    public static ModeSetting fontss = new ModeSetting("Array Fonts", "Minecraft", "SFUI", "Rubik");
    public static SliderSetting saturation = new SliderSetting("Saturation", 0.5F, 0.2F, 1.0F, 0.1F);
    public static SliderSetting brightness = new SliderSetting("Brightness", 1, 0.2F, 1.0F, 0.1F);
    public static SliderSetting opacity = new SliderSetting("Background Opacity", 100, 0, 255, 0.1F);
    public static SliderSetting offsetbg = new SliderSetting("ArrayList Offset", 0.5f, 0.1f, 10, 0.1F);
    public static SliderSetting colorSpeed = new SliderSetting("Color Speed", 5, 1, 30, 0.2F);
    public static SliderSetting colorOffset = new SliderSetting("Color Offset", 5, 1, 8, 0.1F);
    public static SliderSetting xBgPos = new SliderSetting("X BG Pos", 2, 0.5f, 10, 0.1F);
    public static SliderSetting xListPos = new SliderSetting("X ArrayList Pos", 2, 0.5f, 10, 0.1F);
    public static ColorSetting color = new ColorSetting("Array Color", new Color(111, 90, 253).getRGB());
    public static ColorSetting coloralt = new ColorSetting("Alt Color", new Color(251, 251, 255).getRGB());
    public BooleanSetting onlyBinds = new BooleanSetting("Only Binds", false);
    public BooleanSetting noVisualModules = new BooleanSetting("No Visual", false);

    public ModuleList() {
        super("ModuleList", Categories.Display);
        addSetting(color, coloralt, saturation, mode228, noVisualModules, onlyBinds,brightness, opacity, colorSpeed, colorOffset, offsetbg, xBgPos, fontss, xListPos);
    }


    @EventTarget
    public void Event2D(EventRender2D event) {
        if (!isModuleState()) return;
        FeatureList di = (FeatureList) Hexbyte.getInstance().draggable.getDraggableComponentByClass(FeatureList.class);
        di.setWidth(80);
        di.setHeight(100);
        int stringWidth;
        List<Module> activeModules = Hexbyte.getInstance().moduleManagment.getModules();
        if (fontss.is("Minecraft")) {
            activeModules.sort(Comparator.comparingDouble(s -> -mc.fontRenderer.getStringWidth(s.getModuleName())));
        } else if (fontss.is("SFUI")){
            activeModules.sort(Comparator.comparingDouble(s -> -Fonts.sfsemib16.getStringWidth(s.getModuleName())));
        } else if (fontss.is("Rubik")) {
            activeModules.sort(Comparator.comparingDouble(s -> -Fonts.rublik16.getStringWidth(s.getModuleName())));
        }
        // float displayWidth = event.getResolution().getScaledWidth() * (event.getResolution().getScaleFactor() / 2F);
        float displayWidth = di.getX();
        float yPotionOffset = 2;
        for (PotionEffect potionEffect : mc.player.getActivePotionEffects()) {
            if (potionEffect.getPotion().isBeneficial()) {
                yPotionOffset = 30;
            }
            if (potionEffect.getPotion().isBadEffect()) {
                yPotionOffset = 30 * 2;
            }
        }
        //   int y = (int) (5 + yPotionOffset);
        float y = di.getY();
        ScaledResolution rs = new ScaledResolution(this.mc);
        int width = rs.getScaledWidth();
        int height = rs.getScaledHeight();
        boolean reverse = displayWidth > (float) (width / 2);
        boolean reverseY = y > (float) (height / 2);
        int yTotal = 0;
        int offset11 = 1;


        for (int i = 0; i < Hexbyte.getInstance().moduleManagment.getAllFeatures().size(); ++i) {
            if (fontss.is("Minecraft")) {
                yTotal += mc.fontRenderer.FONT_HEIGHT + 3;
            } else if (fontss.is("SFUI")){
                yTotal += Fonts.sfsemib16.getFontHeight() + 3;
            } else if (fontss.is("Rubik")) {
                yTotal += Fonts.rublik16.getFontHeight() + 3;
            }
        }
        if (reverse) {
            for (Module module : activeModules) {
                module.animYto = AnimationUtil.Move(module.animYto, (float) (module.isModuleState() ? 1 : 0), (float) (6.5f * Hexbyte.deltaTime()), (float) (6.5f * Hexbyte.deltaTime()), (float) Hexbyte.deltaTime());
                if (module.animYto > 0.01f) {
                    if (module.getSuffix().equals("ClickGui") || noVisualModules.value && module.getModuleCategory() == Categories.Render || onlyBinds.value && module.getBind() == 0)
                        continue;
                    if (fontss.is("Minecraft")) {
                        stringWidth = mc.fontRenderer.getStringWidth(module.getModuleName()) + 3;
                        RenderUtility.drawRect228(displayWidth + 52 - mc.fontRenderer.getStringWidth(module.getModuleName()) - 5 + xBgPos.value, y -1,displayWidth + 50, y + (float) offset11 + 5.0f + offsetbg.value, RenderUtility.injectAlpha(new Color(1, 1, 1, 0), (int) opacity.value).getRGB());
                        mc.fontRenderer.drawString(module.getModuleName(), (int) (displayWidth + 52.5f - mc.fontRenderer.getStringWidth(module.getModuleName()) - 4f - xListPos.value), (int) (y + mc.fontRenderer.FONT_HEIGHT + (float) offset11 - 10), RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), 255).getRGB());
                        RenderUtility.drawRect228(displayWidth + 49, y - 1, displayWidth + 50.5f, y + 8.2f + (float) offset11 + offsetbg.value, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), 255).getRGB());

                        y += 7 + offsetbg.value;
                    } else if (fontss.is("SFUI")){
                        stringWidth = Fonts.sfsemib16.getStringWidth(module.getModuleName()) + 3;
                        RenderUtility.drawRect228(displayWidth + 50 - Fonts.sfsemib16.getStringWidth(module.getModuleName()) - 5 +  xBgPos.value, y, displayWidth + 50, y + (float) offset11 + 6.0f + offsetbg.value, RenderUtility.injectAlpha(new Color(1, 1, 1, 0), (int) opacity.value).getRGB());
                        Fonts.sfsemib16.drawString(module.getModuleName(), displayWidth + 50.5f - Fonts.sfsemib16.getStringWidth(module.getModuleName()) - 4f - xListPos.value, y + Fonts.sfbolt16.getFontHeight() + (float) offset11 - 4, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), 255).getRGB());
                        RenderUtility.drawRect228(displayWidth + 49, y, displayWidth + 50.5f, y + 8.2f + (float) offset11 + offsetbg.value, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), 255).getRGB());
                        y += 7 * module.animYto + offsetbg.value;
                    } else if (fontss.is("Rubik")) {
                        stringWidth = Fonts.rublik16.getStringWidth(module.getModuleName()) + 3;
                        RenderUtility.drawRect228(displayWidth + 50 - Fonts.rublik16.getStringWidth(module.getModuleName()) - 5 +  xBgPos.value, y, displayWidth + 50, y + (float) offset11 + 6.0f + offsetbg.value, RenderUtility.injectAlpha(new Color(1, 1, 1, 0), (int) opacity.value).getRGB());
                        Fonts.rublik16.drawString(module.getModuleName(), displayWidth + 50.5f - Fonts.rublik16.getStringWidth(module.getModuleName()) - 4f - xListPos.value, y + Fonts.rublik16.getFontHeight() + (float) offset11 - 4, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), 255).getRGB());
                        RenderUtility.drawRect228(displayWidth + 49, y, displayWidth + 50.5f, y + 8.2f + (float) offset11 + offsetbg.value, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), 255).getRGB());
                        y += 7 * module.animYto + offsetbg.value;
                    }
                }
            }
        }


        if (!reverse) {
            for (Module module : activeModules) {
                module.animYto = AnimationUtil.Move(module.animYto, (float) (module.isModuleState() ? 1 : 0), (float) (6.5f * Hexbyte.deltaTime()), (float) (6.5f * Hexbyte.deltaTime()), (float) Hexbyte.deltaTime());

                if (fontss.is("Minecraft")) {
                    if (module.animYto > 0.01f) {
                        if (module.getSuffix().equals("ClickGui") || noVisualModules.value && module.getModuleCategory() == Categories.Render || onlyBinds.value && module.getBind() == 0)
                            continue;
                        stringWidth = mc.fontRenderer.getStringWidth(module.getModuleName()) + 3;
                        GlStateManager.pushMatrix();
                        GL11.glTranslated(1, y, 1);
                        GL11.glTranslated(-1, -y, 1);
                        RenderUtility.drawRect228(displayWidth, y+0.8f, displayWidth + (float) stringWidth - 1  - xBgPos.value, y + (float) offset11 + 6 + offsetbg.value, RenderUtility.injectAlpha(new Color(1, 1, 1, 0), (int) opacity.value).getRGB());
                        mc.fontRenderer.drawString(module.getModuleName(), (int) (displayWidth + 2.5f - xListPos.value), (int) (y + 1 + (float) offset11), RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, 10), (int) (animYto * 255)).getRGB());
                        RenderUtility.drawRect228(displayWidth - 1.5f, y +0.5f, displayWidth, y + (float) offset11 + 8.5f + offsetbg.value, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), (int) (animYto * 255)).getRGB());
                        GlStateManager.popMatrix();

                        y += 7 * module.animYto + offsetbg.value;
                    }

                } else if (fontss.is("SFUI")){
                    if (module.animYto > 0.01f) {
                        if (module.getSuffix().equals("ClickGui") || noVisualModules.value && module.getModuleCategory() == Categories.Render || onlyBinds.value && module.getBind() == 0)
                            continue;
                        stringWidth = Fonts.sfsemib16.getStringWidth(module.getModuleName()) + 3;
                        GlStateManager.pushMatrix();
                        GL11.glTranslated(1, y, 1);
                        GL11.glTranslated(-1, -y, 1);
                        RenderUtility.drawRect228(displayWidth, y + 0.5f, displayWidth + (float) stringWidth + 1.5f - xBgPos.value, y + (float) offset11 + 6.5f + offsetbg.value, RenderUtility.injectAlpha(new Color(1, 1, 1, 0), (int) opacity.value).getRGB());
                        Fonts.sfsemib16.drawString(module.getModuleName(), displayWidth + 2.5f - xListPos.value, y + (float) offset11 + 2, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), (int) (animYto * 255)).getRGB());
                        RenderUtility.drawRect228(displayWidth - 1.5f, y, displayWidth, y + (float) offset11 + 8.5f + offsetbg.value, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, (float) yTotal, (int) colorSpeed.value), (int) (animYto * 255)).getRGB());
                        GlStateManager.popMatrix();
                        y += 7 * module.animYto + offsetbg.value;

                        }

                    } else if (fontss.is("Rubik")){
                        if (module.animYto > 0.01f) {
                            if (module.getSuffix().equals("ClickGui") || noVisualModules.value && module.getModuleCategory() == Categories.Render || onlyBinds.value && module.getBind() == 0)
                                continue;
                            stringWidth = Fonts.rublik16.getStringWidth(module.getModuleName()) + 3;
                            GlStateManager.pushMatrix();
                            GL11.glTranslated(1, y, 1);
                            GL11.glTranslated(-1, -y, 1);
                            RenderUtility.drawRect228(displayWidth, y + 0.5f, displayWidth + (float) stringWidth + 1.5f - xBgPos.value, y + (float) offset11 + 6.5f + offsetbg.value, RenderUtility.injectAlpha(new Color(1, 1, 1, 0), (int) opacity.value).getRGB());
                            Fonts.rublik16.drawString(module.getModuleName(), displayWidth + 2.5f - xListPos.value, y + (float) offset11 + 2, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, yTotal, (int) colorSpeed.value), (int) (animYto * 255)).getRGB());
                            RenderUtility.drawRect228(displayWidth - 1.5f, y, displayWidth, y + (float) offset11 + 8.5f + offsetbg.value, RenderUtility.injectAlpha(ColorUtility.getClientColor(y * colorOffset.value, (float) yTotal, (int) colorSpeed.value), (int) (animYto * 255)).getRGB());
                            GlStateManager.popMatrix();
                            y += 7 * module.animYto + offsetbg.value;
                        }
                    }
                }
            }
        }
    }