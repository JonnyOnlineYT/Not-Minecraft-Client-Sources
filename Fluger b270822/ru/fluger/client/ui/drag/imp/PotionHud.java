/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.ui.drag.imp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import optifine.CustomColors;
import ru.fluger.client.feature.impl.hud.HUD;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.ui.drag.Drag;

public class PotionHud
extends Drag {
    public Gui gui = new Gui();

    public PotionHud() {
        this.setWidth(100.0f);
        this.setHeight(150.0f);
        this.name = "PotionHud";
    }

    @Override
    public void init() {
        this.x = 10.0f;
        this.y = 100.0f;
    }

    @Override
    public void render(int x, int y) {
        if (!HUD.potion.getCurrentValue()) {
            return;
        }
        int xOff = 21;
        int yOff = 14;
        int counter = 16;
        Collection<PotionEffect> collection = this.mc.player.getActivePotionEffects();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableLighting();
        int listOffset = 23;
        if (collection.size() > 5) {
            listOffset = 132 / (collection.size() - 1);
        }
        ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>(this.mc.player.getActivePotionEffects());
        potions.sort(Comparator.comparingDouble(effect -> ClientHelper.getFontRender().getStringWidth(Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(effect.getEffectName()))).getName())));
        for (PotionEffect potion : potions) {
            Potion effect2 = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            assert (effect2 != null);
            if (effect2.hasStatusIcon() && HUD.potionIcons.getCurrentValue()) {
                this.mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                int statusIconIndex = effect2.getStatusIconIndex();
                this.gui.drawTexturedModalRect(this.x + (float)xOff - 20.0f, this.y + (float)counter - (float)yOff, statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 18, 18);
            }
            String level = I18n.format(effect2.getName(), new Object[0]);
            if (potion.getAmplifier() == 1) {
                level = level + " " + I18n.format("enchantment.level.2", new Object[0]);
            } else if (potion.getAmplifier() == 2) {
                level = level + " " + I18n.format("enchantment.level.3", new Object[0]);
            } else if (potion.getAmplifier() == 3) {
                level = level + " " + I18n.format("enchantment.level.4", new Object[0]);
            }
            int getPotionColor = -1;
            if (potion.getDuration() < 200) {
                getPotionColor = new Color(215, 59, 59).getRGB();
            } else if (potion.getDuration() < 400) {
                getPotionColor = new Color(231, 143, 32).getRGB();
            } else if (potion.getDuration() > 400) {
                getPotionColor = new Color(172, 171, 171).getRGB();
            }
            String durationString = Potion.getDurationString(potion);
            ClientHelper.getFontRender().drawStringWithShadow(level, this.x + (float)xOff, this.y + (float)counter - (float)yOff, -1);
            ClientHelper.getFontRender().drawStringWithShadow(durationString, this.x + (float)xOff, this.y + (float)counter + 10.0f - (float)yOff, -1);
            counter += listOffset;
        }
        this.setHeight(counter - listOffset + 4);
    }
}

