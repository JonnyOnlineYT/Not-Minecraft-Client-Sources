/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client.ui.csgo;

import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.Fluger;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.ui.clickgui.ClickGuiScreen;
import ru.fluger.client.ui.csgo.element.imp.flow.FlowPanel;

public class CSGOScreen
extends GuiScreen {
    private FlowPanel flow;
    private int animation = 0;
    boolean ok = false;

    @Override
    public void initGui() {
        ScaledResolution rs = new ScaledResolution(this.mc);
        int width = Fluger.scale.calc(rs.getScaledWidth());
        int height = Fluger.scale.calc(rs.getScaledHeight());
        int panelWidth = 240;
        int panelHeight = 300;
        if (this.flow == null) {
            this.flow = new FlowPanel(width / 2 - panelWidth, height / 2 - panelHeight / 2, panelWidth, panelHeight);
        }
        this.animation = 800;
        this.ok = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution rs = new ScaledResolution(this.mc);
        if (ClickGui.backGroundBlur.getCurrentValue()) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderHelper.renderBlur(0, 0, Fluger.scale.calc(rs.getScaledWidth()), Fluger.scale.calc(rs.getScaledHeight()), (int)ClickGui.backGroundBlurStrength.getCurrentValue());
        } else {
            RenderHelper.renderBlur(0, 0, 0, 0, 0);
        }
        mouseX = Fluger.scale.calc(mouseX);
        mouseY = Fluger.scale.calc(mouseY);
        Fluger.scale.pushScale();
        ClickGuiScreen.callback();
        GL11.glPushMatrix();
        this.flow.render(mouseX, mouseY);
        GL11.glPopMatrix();
        Fluger.scale.popScale();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        mouseX = Fluger.scale.calc(mouseX);
        mouseY = Fluger.scale.calc(mouseY);
        this.flow.mouse(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        this.flow.handleMouse();
        super.handleMouseInput();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        mouseX = Fluger.scale.calc(mouseX);
        mouseY = Fluger.scale.calc(mouseY);
        this.flow.realesed(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.flow.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        ClickGuiScreen.animation = 200.0;
        super.onGuiClosed();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

