package ua.apraxia.modules.impl.display;

import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.impl.Potions;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.other.DiscordUtility;
import ua.apraxia.utility.render.RenderUtility;
import ua.apraxia.utility.render.RoundedUtility;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.optifine.CustomColors;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static ua.apraxia.modules.impl.display.HUD.hudColor;

public class PotionHUD extends Module {
    public PotionHUD() {
        super("PotionHUD", Categories.Display);
        addSetting();
    }
    int height;

         @EventTarget
         public void onRender(EventRender2D e) {

             if (this.mc.player == null || this.mc.world == null) {
                return;
              }
             ScaledResolution sr = new ScaledResolution(mc);
             Potions dwm = (Potions) Hexbyte.getInstance().draggable.getDraggableComponentByClass(Potions.class);
             dwm.setWidth(100);
             dwm.setHeight(17);
             GlStateManager.pushMatrix();
             GlStateManager.enableTexture2D();
             GlStateManager.pushMatrix();
             GlStateManager.enableAlpha();
             List<PotionEffect> potions = new ArrayList<>(mc.player.getActivePotionEffects());
             potions.sort(Comparator.comparingDouble(effect -> mc.fontRenderer.getStringWidth((Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName()))));
             RenderUtility.drawBlur(() -> RoundedUtility.drawRound(dwm.getX(),dwm.getY(), 100, 15 + height - 0.5f, 0, new Color(9, 9, 9, 255)), 7);
             RoundedUtility.drawRound(dwm.getX(),dwm.getY(), 100, 13 - 0.5f, 2, new Color(9, 9, 9, 255));
             Fonts.sfbolt14.drawCenteredString("Potions", (float) (dwm.getX() + 1.5) + 27, dwm.getY() - 13.0f + 2.5f + 15, -1);
             Fonts.icons20.drawCenteredString("q", (float) (dwm.getX() + 1.5) + 5, dwm.getY() - 13.0f + 2.5f + 15, new Color(hudColor.color).getRGB());

        if (potions.isEmpty()) {
           // Fonts.medium14.drawString("", dwm.getX() + x2 + 5, dwm.getY() + y2 + height + 6, Color.GRAY.getRGB());
            height = -7;
        } else {
            for (PotionEffect potion : potions) {
                Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
                String level = I18n.format(effect.getName());
                if (potion.getAmplifier() == 1) {
                    level = level + " " + I18n.format("enchantment.level.2");
                } else if (potion.getAmplifier() == 2) {
                    level = level + " " + I18n.format("enchantment.level.3");
                } else if (potion.getAmplifier() == 3) {
                    level = level + " " + I18n.format("enchantment.level.4");
                }
                int getPotionColor = -1;
                if ((potion.getDuration() < 200)) {
                    getPotionColor = new Color(255, 103, 32).getRGB();
                } else if (potion.getDuration() < 400) {
                    getPotionColor = new Color(231, 143, 32).getRGB();
                } else if (potion.getDuration() > 400) {
                    getPotionColor = new Color(255, 255, 255).getRGB();
                }

                String durationString = Potion.getDurationString(potion);


                Fonts.medium14.drawString(level, dwm.getX() + 5, dwm.getY() + ((potions.indexOf(potion) + 1) * 12) + 6, new Color(255, 255, 255).getRGB());
                Fonts.medium14.drawString(durationString, dwm.getX()+ 95 - Fonts.medium14.getStringWidth(durationString), dwm.getY() + ((potions.indexOf(potion) + 1) * 12) + 6, getPotionColor);


                height = potions.size() * 12;

                if (mc.world == null) {
                    potions.remove(potions.indexOf(potion));
                }
            }
        }

        GlStateManager.popMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
    }
}
