package org.moonware.client.feature.impl.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.rect.RectHelper;

import java.awt.*;
import java.util.Objects;

import static org.moonware.client.feature.impl.combat.Aura.*;
import static org.moonware.client.feature.impl.combat.KillAura.target;

public class TargetHUD extends Feature {
    public static double healthBarWidth;
    private double hui;
    public TargetHUD() {
        super("TargetHUD", "Показывает на экране с кем вы сражаетесь", Type.Hud);
    }

    @EventTarget
    public void onRender(EventRender2D e) {
        if (target != null) {
            float x = new ScaledResolution(Helper.mc).getScaledWidth() / 2.0f - thudX.getNumberValue(); // draw TargetHUD
            float y = new ScaledResolution(Helper.mc).getScaledHeight() / 2.0f + thudY.getNumberValue(); // draw TargetHUD
            String healthStr = String.valueOf((int) target.getHealth() / 2.0f);

            double helath = (target.getHealth() / target.getMaxHealth() * 355);
            hui = MathHelper.clampedLerp((float) hui, (float) helath, 7 * Feature.deltaTime());

            double hpWidth = (target.getHealth() / target.getMaxHealth() * 98);
            healthBarWidth = MathHelper.clampedLerp((float) healthBarWidth, (float) hpWidth, 7 * Feature.deltaTime());

            RectHelper.drawRect(x + 123.5, y - 11.5, x + 270, y + 38f, new Color(0x90000000, true).getRGB());
            Gui.drawRect(x + 167.0f, y + 23.0f, x + 261.0f, y + 33.3f, new Color(31, 31, 31, 113).getRGB());
            RectHelper.drawGradientRect((int) x + 172.0f, (int) y + 29.0f, (int) x + 205.0f + healthBarWidth - 44, (int) y + 26.0f, 15, 15);
//   ping                     mc.neverlose700_14.drawString((Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) + " ms", x + 163, y + 10, -1);


            String pvpState = "";
            if (Minecraft.player.getHealth() == target.getHealth()) {
                pvpState = Formatting.GREEN + "Winning";
            } else if (Minecraft.player.getHealth() < target.getHealth()) {
                pvpState = Formatting.RED + "Losing";
            } else if (Minecraft.player.getHealth() > target.getHealth()) {
                pvpState = Formatting.GREEN + "Winning";
            }
            MWFont.SF_UI_DISPLAY_REGULAR.get(12).draw(pvpState,(int) x + 233, (int) y + 16, -1);

            String dist = String.format("%.1f ", Minecraft.player.getDistanceToEntity(target));
            String healthh = String.format("%.1f ", target.getHealth());

            RectHelper.drawGradientRect1(x + 167.0f, y + 23.0f, x + 208.0f + healthBarWidth - 44, y + 33.0f, new Color(255,133,0).getRGB(), new Color(255,55,255).getRGB());
            MWFont.SF_UI_DISPLAY_REGULAR.get(16).draw("" + healthh, x + 185.0f + 31f - Minecraft.font.getStringWidth(healthStr) / 2.0f, y + 26f, -1);


            double cooldownPercentage = MathHelper.clamp(Minecraft.player.getCooledAttackStrength(1), 0.0, 1.0);
            double cooldownWidth = 5.0 * cooldownPercentage;


            Minecraft.getRenderItem().renderItemOverlays(MWFont.SF_UI_DISPLAY_REGULAR.get(16), target.getHeldItem(EnumHand.OFF_HAND), (int) x + 205, (int) y + 6);
            Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 195, (int) y + 7);

            Minecraft.getRenderItem().renderItemOverlays(MWFont.SF_UI_DISPLAY_REGULAR.get(16), target.getHeldItem(EnumHand.MAIN_HAND), (int) x + 178, (int) y + 6);
            Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.MAIN_HAND), (int) x + 167, (int) y + 7);
            MWFont.SF_UI_DISPLAY_REGULAR.get(16).drawShadow("\u2764",
                    x + 128.0f + 46.0f + MWFont.SF_UI_DISPLAY_REGULAR.get(16).getWidth(healthStr), y + 19.5f,
                    ClientHelper.getClientColor().getRGB());

            MWFont.SF_UI_DISPLAY_REGULAR.get(16).drawShadow(target.getName(), x + 168, y - 3.0f, -1);
            drawHead(Objects.requireNonNull(Objects.requireNonNull(Helper.mc.getConnection()).getPlayerInfo(target.getUniqueID())
                    .getLocationSkin()), (int) x + 127, (int) y - 4);
        } else {
            float x = new ScaledResolution(Helper.mc).getScaledWidth() / 2.0f - thudX.getNumberValue(); // draw TargetHUD
            float y = new ScaledResolution(Helper.mc).getScaledHeight() / 2.0f + thudY.getNumberValue(); // draw TargetHUD
            String healthStr = String.valueOf((int) target.getHealth() / 2.0f);

            double helath = (target.getHealth() / target.getMaxHealth() * 355);
            hui = MathHelper.clampedLerp((float) hui, (float) helath, 7 * Feature.deltaTime());

            double hpWidth = (target.getHealth() / target.getMaxHealth() * 98);
            healthBarWidth = MathHelper.clampedLerp((float) healthBarWidth, (float) hpWidth, 7 * Feature.deltaTime());

            RectHelper.drawRect(x + 123.5, y - 11.5, x + 270, y + 38f, new Color(0x90000000, true).getRGB());
            Gui.drawRect(x + 167.0f, y + 23.0f, x + 261.0f, y + 33.3f, new Color(31, 31, 31, 113).getRGB());
            RectHelper.drawGradientRect((int) x + 172.0f, (int) y + 29.0f, (int) x + 205.0f + healthBarWidth - 44, (int) y + 26.0f, 15, 15);
//   ping                     mc.neverlose700_14.drawString((Objects.requireNonNull(mc.getConnection()).getPlayerInfo(mc.player.getUniqueID()).getResponseTime()) + " ms", x + 163, y + 10, -1);


            String pvpState = "";
            if (Minecraft.player.getHealth() == target.getHealth()) {
                pvpState = Formatting.GREEN + "Winning";
            } else if (Minecraft.player.getHealth() < target.getHealth()) {
                pvpState = Formatting.RED + "Losing";
            } else if (Minecraft.player.getHealth() > target.getHealth()) {
                pvpState = Formatting.GREEN + "Winning";
            }
            MWFont.SF_UI_DISPLAY_REGULAR.get(12).draw(pvpState,(int) x + 233, (int) y + 16, -1);

            String dist = String.format("%.1f ", Minecraft.player.getDistanceToEntity(target));
            String healthh = String.format("%.1f ", target.getHealth());

            RectHelper.drawGradientRect1(x + 167.0f, y + 23.0f, x + 208.0f + healthBarWidth - 44, y + 33.0f, new Color(255,133,0).getRGB(), new Color(255,55,255).getRGB());
            MWFont.SF_UI_DISPLAY_REGULAR.get(16).draw("" + healthh, x + 185.0f + 31f - Minecraft.font.getStringWidth(healthStr) / 2.0f, y + 26f, -1);


            double cooldownPercentage = MathHelper.clamp(Minecraft.player.getCooledAttackStrength(1), 0.0, 1.0);
            double cooldownWidth = 5.0 * cooldownPercentage;


            Minecraft.getRenderItem().renderItemOverlays(MWFont.SF_UI_DISPLAY_REGULAR.get(16), target.getHeldItem(EnumHand.OFF_HAND), (int) x + 205, (int) y + 6);
            Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int) x + 195, (int) y + 7);

            Minecraft.getRenderItem().renderItemOverlays(MWFont.SF_UI_DISPLAY_REGULAR.get(16), target.getHeldItem(EnumHand.MAIN_HAND), (int) x + 178, (int) y + 6);
            Minecraft.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.MAIN_HAND), (int) x + 167, (int) y + 7);
            MWFont.SF_UI_DISPLAY_REGULAR.get(16).drawShadow("\u2764",
                    x + 128.0f + 46.0f + MWFont.SF_UI_DISPLAY_REGULAR.get(16).getWidth(healthStr), y + 19.5f,
                    ClientHelper.getClientColor().getRGB());

            MWFont.SF_UI_DISPLAY_REGULAR.get(16).drawShadow(target.getName(), x + 168, y - 3.0f, -1);
            drawHead(Objects.requireNonNull(Objects.requireNonNull(Helper.mc.getConnection()).getPlayerInfo(target.getUniqueID())
                    .getLocationSkin()), (int) x + 127, (int) y - 4);
        }
    }
    public void drawHead(Namespaced skin, int width, int height) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(width, height, 8, 8, 8, 8, 37, 37, 64, 64);
    }
}
