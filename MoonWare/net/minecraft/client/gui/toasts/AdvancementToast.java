package net.minecraft.client.gui.toasts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementDisplay;
import net.minecraft.advancements.FrameType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@RequiredArgsConstructor
public class AdvancementToast implements Toast {
    @Getter private final Advancement advancement;
    private boolean shown;

    @Override
    public Toast.Visibility draw(long delta) {
        AdvancementDisplay display = advancement.getDisplay();
        if (display == null) return Visibility.HIDE;
        Minecraft.getTextureManager().bindTexture(Toast.TOASTS);
        GlStateManager.color(1F, 1F, 1F);
        Gui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);
        List<String> list = Minecraft.font.split(display.func_192297_a().asFormattedString(), 125);
        int i = display.func_192291_d() == FrameType.CHALLENGE ? 16746751 : 16776960;
        if (list.size() == 1) {
            Minecraft.font.drawString(I18n.format("advancements.toast." + display.func_192291_d().func_192307_a()), 30, 7, i | -16777216);
            Minecraft.font.drawString(display.func_192297_a().asFormattedString(), 30, 18, -1);
        } else {
            if (delta < 1500L) {
                int k = MathHelper.floor(MathHelper.clamp((float) (1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 67108864;
                Minecraft.font.drawString(I18n.format("advancements.toast." + display.func_192291_d().func_192307_a()), 30, 11, i | k);
            } else {
                int i1 = MathHelper.floor(MathHelper.clamp((float) (delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int l = 16 - list.size() * Minecraft.font.height / 2;
                for (String s : list) {
                    Minecraft.font.drawString(s, 30, l, 16777215 | i1);
                    l += Minecraft.font.height;
                }
            }
        }
        if (!shown && delta > 0L) {
            shown = true;
            if (display.func_192291_d() == FrameType.CHALLENGE) {
                Minecraft.getSoundHandler().playSound(PositionedSoundRecord.func_194007_a(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));
            }
        }
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, display.func_192298_b(), 8, 8);
        return delta >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
