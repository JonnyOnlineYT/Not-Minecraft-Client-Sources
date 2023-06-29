package net.minecraft.client.gui.screen;

import net.minecraft.client.resources.I18n;

public class LevelLoadingScreen extends Screen {
    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        drawDefaultBackground();
        drawCenteredString(font, I18n.format("multiplayer.downloadingTerrain"), width / 2, height / 2 - 50, -1);
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
}
