package org.moonware.client.ui.components.draggable.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.Formatting;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.components.draggable.HudComponent;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PotionComponent extends HudComponent {
    public PotionComponent() {
        super(4, 230, 100, 25);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        setVisible(HUD.potion.get());
        super.draw(mouseX, mouseY, partialTick);;
        boolean nurik = HUD.nurikMode.get();
        //if (Minecraft.player.getActivePotionEffects().size() == 0)return;
        boolean isEmptyandChat = Minecraft.player.getActivePotionEffects().size() == 0 && !nurik && Minecraft.screen instanceof ChatScreen;
        boolean isEmpty = Minecraft.player.getActivePotionEffects().size() == 0;

        int x = this.x - 4;
        int y = this.y - 4;
        if (Minecraft.gameSettings.showDebugInfo) return;
        width = 100;
        for (PotionEffect effect : Minecraft.player.getActivePotionEffects()) {
            String text = I18n.format(effect.getEffectName()) + " " + (effect.getAmplifier() + 1) + " " + Potion.getDurationString(effect);
            width = Math.max(width, MWFont.GREYCLIFFCF_MEDIUM.get(18).getWidth(text) + 27);
        }
        height = (nurik || isEmptyandChat  ? 25 : isEmpty ? 0 : 15) + Minecraft.player.getActivePotionEffects().size() * 12 - 8;
        int width = this.width +  8;
        int height = this.height + 8;
        if (HUD.colored.get() || nurik) {
            HUD.nurikAlpha.setMaxValue(100);
            RenderHelper2.drawRainbowRound(x + 4, y + 4, width - 8, height - 8, 3, true, true, true, true, 2, 4);
        }else{
            HUD.nurikAlpha.setMaxValue(255);
            RoundedUtil.drawRound(x + 4, y + 4,width - 8, height - 8, 1.4f,new Color(31,31,31,HUD.nurikAlpha.getCurrentIntValue()));

            RoundedUtil.drawRound(x + 4.5f, y + 4.5f,width -9, height - 8.5f, 1.4f,new Color(31,31,31,HUD.nurikAlpha.getCurrentIntValue()));

            GlowUtil.drawBlurredShadow(x + 4, y + 4,width - 8, height - 8, 14,new Color(31,31,31),2);
        }
        if ( nurik) {
            MWFont.GREYCLIFFCF_MEDIUM.get(27).drawShadow(Formatting.BOLD + "Potions", x + 5, y + 9 - 5, -1);
            Gui.drawRect(x + 5, y + 22 - 5, x + width - 5, y + 21 + 2 - 5, 0x28FFFFFF);
        }
        int i = !nurik ? -1 : 0;
        for (PotionEffect effect : Minecraft.player.getActivePotionEffects()) {
            String left = I18n.format(effect.getEffectName()) + " " + ((effect.getAmplifier() + 1) > 1 ? (Formatting.RED) + "" + (effect.getAmplifier() + 1) : "");
            String right = Potion.getDurationString(effect);
            MWFont.GREYCLIFFCF_MEDIUM.get(18).draw(left, x + 6, y + 22 + i * 12, -1);
            MWFont.GREYCLIFFCF_MEDIUM.get(18).draw(right, x + width - 6 - 5 - MWFont.GREYCLIFFCF_MEDIUM.get(16).getWidth(right), y + 22 + i * 12, -1);
            i++;
        }

        if (!nurik) {
            if (Minecraft.screen instanceof ChatScreen && isEmpty) {
                List<String> fakePotions = new ArrayList<>();
                fakePotions.add("Зелье");
                int ii = -2;
                for (String effect : fakePotions) {
                    String left = I18n.format(effect) + " " + ((1) > 1 ? (Formatting.RED) + "" + (0 + 1) : "");
                    String right = "1:00";
                    MWFont.GREYCLIFFCF_MEDIUM.get(18).draw(left, x + 6, y + 22 + i * 12, -1);
                    MWFont.GREYCLIFFCF_MEDIUM.get(18).draw(right, x + width - 6 - 5 - MWFont.GREYCLIFFCF_MEDIUM.get(16).getWidth(right), y + 22 + i * 12, -1);
                    ii++;
                }
            }
        }
    }
}