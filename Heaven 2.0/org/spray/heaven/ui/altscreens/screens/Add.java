package org.spray.heaven.ui.altscreens.screens;

import java.awt.Color;
import java.io.IOException;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spray.heaven.font.IFont;
import org.spray.heaven.ui.altscreens.AltScreen;
import org.spray.heaven.ui.altscreens.Session;
import org.spray.heaven.ui.widgets.TextFieldWidget;
import org.spray.heaven.util.render.Drawable;
import org.spray.heaven.util.render.shader.drawable.RoundedShader;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import static net.minecraft.client.gui.GuiMainMenu.backgroundShader;
import static net.minecraft.client.gui.GuiMainMenu.initTime;

public class Add extends GuiScreen {

    private TextFieldWidget loginField;
    private TextFieldWidget passwordField;

    public GuiScreen prevScreen;

    private String status = "";
    private int statusTime;

    public Add(GuiScreen prevScreen) {
        this.prevScreen = prevScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList
                .add(new GuiButton(0, width / 2 - 50, height / 2 + 15, 100, 16, "Добавить").disableRect().enableHeaven());
        this.buttonList
                .add(new GuiButton(1, width / 2 - 50, height / 2 + 15 + 20, 100, 16, "Назад").disableRect().enableHeaven());
        loginField = new TextFieldWidget(IFont.WEB_SMALL).setBorders(false);
        passwordField = new TextFieldWidget(IFont.WEB_SMALL).setObfedText(true).setBorders(false);

        loginField.setFillText("Email");
        passwordField.setFillText("Пароль");
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (statusTime > 0)
            statusTime--;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float tickDelta) {
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

        RoundedShader.drawRound((width >> 1) - 78, (height >> 1) - 43, 156, 110, 3, new Color(11, 12, 12, 235));
        Drawable.drawRectWH((width >> 1) - 80, (height >> 1) - 64, 160, 24, new Color(14, 15, 17, 250).getRGB());


//		Drawable.drawRectWH(width / 2 - 102, height / 4 + 1, 204, 144, 0xFF0D0D0D);
//		Drawable.drawRectWH(width / 2 - 100, height / 4 + 2, 200, 140, 0xFF070707);

        loginField.setX(width / 2 - 65);
        loginField.setY(height / 2 - (12 + 22));
        loginField.setWidth(130);
        loginField.setHeight(18);
        loginField.render(mouseX, mouseY, tickDelta);

        passwordField.setX(width / 2 - 65);
        passwordField.setY(height / 2 - 12);
        passwordField.setWidth(130);
        passwordField.setHeight(18);
        passwordField.render(mouseX, mouseY, tickDelta);

        IFont.ROBOTO_SMALL.drawVCenteredString("Alt", (width >> 1) - 58, (height >> 1) - 64, 24, -1, 1.15f);

        if (statusTime > 0)
            IFont.WEB_SMALL.drawCenteredString(status, width / 2, height / 4 + 10, -1);
        super.drawScreen(mouseX, mouseY, tickDelta);
    }

    @Override
    protected void keyTyped(char chr, int keyCode) {
        this.loginField.keyTyped(chr, keyCode);
        this.passwordField.keyTyped(chr, keyCode);
        if (chr == '\t' && (this.loginField.isFocused() || this.passwordField.isFocused())) {
            this.loginField.setFocused(!this.loginField.isFocused());
            this.passwordField.setFocused(!this.passwordField.isFocused());
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loginField.mouseClicked(par1, par2, par3);
        this.passwordField.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                if (!loginField.getText().replaceAll(" ", "").isEmpty()) {
                    AltScreen.sessions.add(new Session(loginField.getText().replaceAll(" ", ""), passwordField.getText().replaceAll(" ", "")));
                    this.mc.displayGuiScreen(this.prevScreen);
                }
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.prevScreen);
            }
        }
    }

    public void setStatus(String status) {
        this.status = status;
        statusTime = 40;
    }

}
