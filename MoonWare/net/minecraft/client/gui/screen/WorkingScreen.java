package net.minecraft.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Progress;
import net.minecraft.util.math.MathHelper;

public class WorkingScreen extends Screen implements Progress {
    private String title = "";
    private String text = "";
    private int progress = -1;
    private boolean done;

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public void done() {
        done = true;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        if (done) {
            Minecraft.openScreen(null);
            return;
        }
        drawDefaultBackground();
        drawCenteredString(font, title, width / 2, 70, -1);
        drawCenteredString(font, text, width / 2, 90, -1);
        if (progress >= 0) {
            drawHorizontalLine(width / 2 - 50, width / 2 + 50, 110, 0xFF808080);
            drawHorizontalLine(width / 2 - 50, width / 2 - 50 + MathHelper.clamp(progress, 0, 100), 110, 0xFF80FF80);
        }
        super.draw(mouseX, mouseY, partialTick);
    }
}
