package org.spray.heaven.ui.altscreens;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.altscreens.screens.Add;
import org.spray.heaven.util.file.ClientConfig;
import org.spray.heaven.util.render.BlurUtil;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.RenderUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static net.minecraft.client.gui.GuiMainMenu.backgroundShader;
import static net.minecraft.client.gui.GuiMainMenu.initTime;

public class AltScreen extends GuiScreen {

    public int x = 150, y = 100,lastMX, lastMY;
    public boolean drag;
    public Session selected;


    public static ArrayList<Session> sessions = new ArrayList<>();

    @Override
    public void initGui() {
        super.initGui();
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (Drawable.isHovered(mouseX, mouseY, x, y, 100, 110)) {
            if (!drag) {
                drag = true;
                this.lastMX = x - mouseX;
                this.lastMY = y - mouseY;
            }
        }
        int i = 35;
        for (Session sex : sessions) {
            if (Drawable.isHovered(mouseX, mouseY, 20, i, 150, 30)) {
                selected = sex;
                selected.run();
            }
            i += 35;
        }
        if (Drawable.isHovered(mouseX, mouseY, 20,5, 100, 15)) {
            mc.displayGuiScreen(new Add(this));
        }
        if (Drawable.isHovered(mouseX, mouseY,   150,5, 100, 15)) {
            if (selected != null) {
                sessions.remove(selected);
                selected = null;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (drag) {
            drag = false;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drag(mouseX, mouseY);
        GlStateManager.enableAlpha();
        GlStateManager.disableCull();

        backgroundShader.useShader(this.width, this.height, mouseX, mouseY,
                (System.currentTimeMillis() - initTime) / 1000f);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();
        GL20.glUseProgram(0);
        // я новая порнстар
        BlurUtil.draw(0,0, new ScaledResolution(mc).getScaledWidth(), 25, 15);
        Drawable.drawRectWH(0,0, new ScaledResolution(mc).getScaledWidth(), 25, new Color(25, 25, 25, 150).getRGB());
        Drawable.drawRectWH(20,5, 100, 15, new Color(25, 25, 25, 200).getRGB());
        Drawable.drawRectWH(150,5, 100, 15, new Color(25, 25, 25, 200).getRGB());
        IFont.ROBOTO_SMALL.drawCenteredString("Add", 70, 15 / 2f, -1);
        IFont.ROBOTO_SMALL.drawCenteredString("Remove", 200, 15 / 2f, -1);
        int i = 35;

        for (Session sex : sessions) {
            BlurUtil.draw(20, i, 150, 30, 15);
            Drawable.drawRectWH(20, i, 150, 30, new Color(20, 15, 15, 150).getRGB());
            IFont.rubik1.drawString(sex.altName, 50, i + 3, selected == sex ? new Color(0, 229, 176).getRGB() : -1);
            IFont.rubik2.drawString(sex.pass, 50, i + 17, selected == sex ? selected.failed ? new Color(255, 65, 65).getRGB() : -1 : -1);
            Drawable.downloadImage(String.format("https://minotar.net/cube/%s/64.png", sex.altName), 23, i + 2f, 24, 30);
            i += 35;
        }
    }



    public void drag(int mouseX, int mouseY) {
        if (drag) {
            x = mouseX + lastMX;
            y = mouseY + lastMY;
        }
    }
}
