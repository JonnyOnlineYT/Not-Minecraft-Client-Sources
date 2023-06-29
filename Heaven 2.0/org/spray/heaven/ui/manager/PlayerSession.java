package org.spray.heaven.ui.manager;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spray.heaven.util.file.ClientConfig;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;

import java.awt.*;
import java.io.IOException;

import static net.minecraft.client.gui.GuiMainMenu.initTime;

public class PlayerSession extends GuiScreen {

    public int panelX = 100, panelY = 50, lastX, lastY;
    public int panelWidth = 100, panelHeight = 110;
    boolean drag;

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (Drawable.isHovered(mouseX, mouseY, panelX, panelY, panelWidth, panelHeight)) {
            if (!drag) {
                this.lastX = panelX - mouseX;
                this.lastY = panelY - mouseY;
                this.drag = true;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (this.drag) {
            this.drag = false;
        }
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (ClientConfig.GUI_BACKGROUND.isToggle()) {
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();

            GuiMainMenu.backgroundShader.useShader(this.width, this.height, mouseX, mouseY,
                    (System.currentTimeMillis() - initTime) / 1000f);

            GL11.glBegin(GL11.GL_QUADS);

            GL11.glVertex2f(-1f, -1f);
            GL11.glVertex2f(-1f, 1f);
            GL11.glVertex2f(1f, 1f);
            GL11.glVertex2f(1f, -1f);

            GL11.glEnd();

            // Unbind shader
            GL20.glUseProgram(0);
        }

        Drawable.drawRectWH(panelX, panelY, panelWidth, panelHeight, new Color(22, 21, 21, 150).getRGB());
        drag(mouseX, mouseY);
    }
    public void drag(int mouseX, int mouseY) {
        if (this.drag) {
            this.panelX = mouseX + this.lastX;
            this.panelY = mouseY + this.lastY;
        }

    }
}
