package net.minecraft.client.gui.toasts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.Component;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class SystemToast implements Toast {
    @Getter private final Component title;
    @Getter @Nullable private final Component text;

    @Override
    public Toast.Visibility draw(long delta) {
        Minecraft.getTextureManager().bindTexture(Toast.TOASTS);
        GlStateManager.color(1, 1, 1);
        Gui.drawTexturedModalRect(0, 0, 0, 64, 160, 32);
        if (text == null) {
            Minecraft.font.drawString(title.asFormattedString(), 18, 12, -256);
        } else {
            Minecraft.font.drawString(title.asFormattedString(), 18, 7, -256);
            Minecraft.font.drawString(text.asFormattedString(), 18, 18, -1);
        }
        return delta < 5000L ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }
}
