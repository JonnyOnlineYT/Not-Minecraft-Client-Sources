package net.minecraft.client.gui.screen;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Formatting;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class DeathScreen extends Screen {
    private final Component cause;
    @Override
    public void init() {
        widgets.add(new ButtonWidget(width / 2 - 100, height / 4 + 72, I18n.format(
                Minecraft.world.getWorldInfo().isHardcoreModeEnabled() ? "deathScreen.spectate" :
                        "deathScreen.respawn"), btn -> Minecraft.player.respawnPlayer()));
        widgets.add(new ButtonWidget(width / 2 - 100, height / 4 + 96, I18n
                .format("deathScreen.titleScreen"), btn -> {
            Minecraft.world.quit();
            minecraft.loadWorld(null);
            Minecraft.openScreen(new TitleScreen());
        }));
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawGradientRect(0, 0, width, height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        drawCenteredString(font, I18n.format(Minecraft.world.getWorldInfo().isHardcoreModeEnabled() ? "deathScreen.title.hardcore" : "deathScreen.title"), width / 2 / 2, 30, -1);
        GlStateManager.popMatrix();
        drawCenteredString(font, I18n.format("deathScreen.score") + ": " + Formatting.YELLOW + Minecraft.player.getScore(), width / 2, 100, -1);
        if (cause != null) {
            drawCenteredString(font, cause.asFormattedString(), width / 2, 85, -1);
            if (mouseY > 85 && mouseY < 85 + font.height) {
                Component component = getComponentAt(mouseX);
                if (component != null && component.getStyle().getHoverEvent() != null) {
                    drawTooltip(component, mouseX, mouseY);
                }
            }
        }
        super.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public boolean pauses() {
        return false;
    }

    @Override
    public boolean escapeCloses() {
        return false;
    }

    @Nullable
    public Component getComponentAt(int mouseX) {
        if (cause == null) return null;
        int i = Minecraft.font.getStringWidth(cause.asFormattedString());
        int j = width / 2 - i / 2;
        int k = width / 2 + i / 2;
        int l = j;
        if (mouseX < j || mouseX > k) return null;
        for (Component itextcomponent : cause) {
            l += Minecraft.font.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(itextcomponent.asString(), false));
            if (l > mouseX) {
                return itextcomponent;
            }
        }
        return null;
    }
}
