package org.moonware.client.feature.impl.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.ShaderEvent;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.MathUtils;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.GLUtil;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Radar extends Feature {

    public final NumberSetting size = new NumberSetting("Size", 90, 125, 75, 1);
    public final ListSetting colorMode = new ListSetting("Color", "Sync", "Sync", "Analogous", "Tenacity", "Gradient", "Modern");
    public final ListSetting degree = new ListSetting("Degree", "30", "30", "-30");
    private final ColorSetting color1 = new ColorSetting("Color 1", ClientHelper.getClientColor());
    private final ColorSetting color2 = new ColorSetting("Color 2", ClientHelper.getAlternateClientColor());
    public final BooleanSetting targets = new BooleanSetting("Players",true,() -> true);
    private static NumberSetting xC = new NumberSetting("X",300,1,700,1);
    private static NumberSetting yC = new NumberSetting("Y",300,1,700,1);

    private static NumberSetting xCC = new NumberSetting("X",0,1,700,1);
    private static NumberSetting yCC = new NumberSetting("Y",0,1,700,1);


    //public final Dragging drag = Tenacity.INSTANCE.createDrag(this, "radar", 5, 40);
    private final List<EntityLivingBase> entities = new ArrayList<>();

    public Radar() {
        super("Radar","Shows entites on a gui", Type.Hud);
        //color1.addParent(colorMode, modeSetting -> modeSetting.is("Gradient") || modeSetting.is("Analogous"));
        //color2.addParent(colorMode, modeSetting -> modeSetting.is("Gradient") && !modeSetting.is("Analogous"));
        //degree.addParent(colorMode, modeSetting -> modeSetting.is("Analogous"));
        addSettings(colorMode, xCC, yCC, color1, color2, degree, size);
    }

    @EventTarget
    private void ShaderEvent(ShaderEvent event) {
        float x = yC.getCurrentValue(), y = yC.getCurrentValue(), size = this.size.getCurrentValue(), middleX = x + size / 2f, middleY = y + size / 2f;
        RoundedUtil.drawRound(x, y, size, size, 6, false, Color.WHITE);
    }

    private Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE, gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE;


    @EventTarget
    private void onRender2D(EventRender2D eventRender2D) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-xCC.getCurrentValue(), -yCC.getCurrentIntValue(), 1);
        getEntities();
        float x = yC.getCurrentValue(), y = yC.getCurrentValue(), size = this.size.getCurrentValue(), middleX = x + size / 2f, middleY = y + size / 2f;

        Color lineColor = new Color(255, 255, 255, 180);

        boolean HudMod = true;
        switch (colorMode.getCurrentMode()) {
            case "Sync":
                //HudMod hudMod = (HudMod) Tenacity.INSTANCE.getModuleCollection().get(HudMod.class);

                Color[] colors = ClientHelper.getClientColorR();
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, colors[0], colors[1], HudMod);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, colors[0], colors[1], HudMod);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, colors[0], colors[1], HudMod);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, colors[0], colors[1], HudMod);
                break;
            case "Tenacity":
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, ClientHelper.getClientColor(), ClientHelper.getAlternateClientColor(),HudMod);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, ClientHelper.getClientColor(), ClientHelper.getAlternateClientColor(), HudMod);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, ClientHelper.getClientColor(), ClientHelper.getAlternateClientColor(), HudMod);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, ClientHelper.getClientColor(), ClientHelper.getAlternateClientColor(), HudMod);
                break;
            case "Gradient":
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, color1.getColorc(), color2.getColorc(), HudMod);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, color1.getColorc(), color2.getColorc(), HudMod);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, color1.getColorc(), color2.getColorc(), HudMod);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, color1.getColorc(), color2.getColorc(), HudMod);
                break;
            case "Analogous":
                int val = degree.equals("30") ? 0 : 1;
                Color analogous = ColorUtil.getAnalogousColor(color1.getColorc())[val];
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, color1.getColorc(), analogous, HudMod);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, color1.getColorc(), analogous, HudMod);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, color1.getColorc(), analogous, HudMod);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, color1.getColorc(), analogous, HudMod);
                break;
            case "Modern":
                RoundedUtil.drawRoundOutline(x, y, size, size, 6, .5f, new Color(10, 10, 10, 80), new Color(-2));
                break;
            case "Dark":
                lineColor = new Color(120, 120, 120);
                RoundedUtil.drawRoundOutline(x, y, size, size, 6, .5f, new Color(10, 10, 10, 80), new Color(90, 90, 90, 180));
                break;
        }
        boolean outlinedRadar = !(colorMode.equals("Modern"));
        if(outlinedRadar) {
            RoundedUtil.drawGradientRound(x,y,size,size, 6, ColorUtil.applyOpacity(gradientColor4, .85f), gradientColor1, gradientColor3, gradientColor2);
        }


        if(outlinedRadar) {
            Gui.drawRect2(x - 1, y + (size / 2f - .5), size + 2, 1, lineColor.getRGB());
            Gui.drawRect2(x + (size / 2f - .5), y - 1, 1, size + 2, lineColor.getRGB());
        }else {
            Gui.drawRect2(x + 1, y + (size / 2f - .5), size - 2, 1, lineColor.getRGB());
            Gui.drawRect2(x + (size / 2f - .5), y + 1, 1, size - 2, lineColor.getRGB());
        }

        StencilUtil.initStencilToWrite();
        RenderUtil.renderRoundedRect(x, y, size, size, 6, -1);
        StencilUtil.readStencilBuffer(1);
        GLUtil.rotate(middleX, middleY, Minecraft.player.rotationYaw, () -> {
            for (EntityLivingBase entity : entities) {
                double xDiff = MathUtils.interpolate(entity.prevPosX, entity.posX, Minecraft.timer.renderPartialTicks) - MathUtils.interpolate(Minecraft.player.prevPosX, Minecraft.player.posX, Minecraft.timer.renderPartialTicks);
                double zDiff = MathUtils.interpolate(entity.prevPosZ, entity.posZ, Minecraft.timer.renderPartialTicks) - MathUtils.interpolate(Minecraft.player.prevPosZ, Minecraft.player.posZ, Minecraft.timer.renderPartialTicks);
                if ((xDiff + zDiff) < (size / 2f)) {
                    float translatedX = (float) (middleX - xDiff);
                    float translatedY = (float) (middleY - zDiff);
                    RoundedUtil.drawRound(translatedX, translatedY, 3, 3, 1f, new Color(255, 255, 255, 255));
                    //Gui.drawRect2(translatedX, translatedY, 3, 3, -1);
                }
            }
        });
        StencilUtil.uninitStencilBuffer();
        GlStateManager.popMatrix();
    }


    public void getEntities() {
        entities.clear();
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                if (entity instanceof EntityPlayer && entity != null && !entity.isInvisible()) {
                    entities.add((EntityLivingBase) entity);
                }
            }
        }
    }


    @Override
    public void onEnable() {
        super.onEnable();
    }
}
