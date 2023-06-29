/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 *  org.lwjgl.opengl.GL20
 */
package ru.fluger.client.ui;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ru.fluger.client.Fluger;
import ru.fluger.client.UIRender;
import ru.fluger.client.ui.button.GuiMainMenuButton;

public class GuiAltManager
extends GuiScreen {
    public GuiScreen old;
    private ResourceLocation rs;
    public GuiTextField field;

    public GuiAltManager(GuiScreen old) {
        this.old = old;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiMainMenuButton(1, this.width / 2 - 120, this.height / 2 + 60, 80, 8, "Login"));
        this.buttonList.add(new GuiMainMenuButton(2, this.width / 2 - 35, this.height / 2 + 60, 80, 8, "Random"));
        this.buttonList.add(new GuiMainMenuButton(3, this.width / 2 + 50, this.height / 2 + 60, 80, 8, "Back"));
        this.field = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 95, this.height / 3 + 40, 200, 17);
        this.field.setText("Login");
        this.field.setMaxStringLength(15);
    }

    private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager textureManager = this.mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        boolean flag1;
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.disableCull();
 //       GuiMainMenu.backgroundShader.useShader(this.width, this.height * 4 + 150, mouseX, mouseY, (float)(System.currentTimeMillis() - Fluger.instance.time) / 1000.0f);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)-1.0f, (float)-1.0f);
        GL11.glVertex2f((float)-1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)-1.0f);
        GL11.glEnd();
        GL20.glUseProgram((int)0);
        GlStateManager.popMatrix();
        UIRender.drawRect(this.field.xPosition, this.field.yPosition, this.field.xPosition + this.field.getWidth(), this.field.yPosition + 17, Integer.MIN_VALUE);
        GL11.glPushMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        int x = this.width / 2 - 18;
        int y = this.height / 5;
        this.getDownloadImageSkin(this.rs, this.mc.session.getUsername());
        this.rs = AbstractClientPlayer.getLocationSkin(this.mc.session.getUsername());
        if (this.rs != null) {
            this.mc.getTextureManager().bindTexture(this.rs);
            Gui.drawScaledCustomSizeModalRect(x, y, 4.0f, 4.0f, 4.0f, 4.0f, 32.0f, 32.0f, 32.0f, 32.0f);
            this.mc.fontRenderer.drawCenteredString(this.mc.session.getUsername(), x + 16, y + 40, -1);
        }
        GL11.glPopMatrix();
        int fieldColor = new Color(40, 40, 40, 255).getRGB();
        if (this.field.getText().length() > 0) {
            this.mc.clickguismall.drawString(this.field.getText(), this.field.xPosition + 5, this.field.yPosition + 6, -1);
        } else {
            this.mc.clickguismall.drawString("Login", this.field.xPosition + 5, this.field.yPosition + 6, -1);
        }
        boolean bl = flag1 = this.field.isFocused() && this.field.cursorCounter / 6 % 2 == 0;
        if (flag1 && this.field.getText().length() > 0) {
            this.mc.clickguismall.drawString("_", this.field.xPosition + 5 + this.mc.clickguismall.getStringWidth(this.field.getText()) + 1, this.field.yPosition + 6, -1);
        }
        this.mc.comfortaa.drawCenteredString("AltManager", this.width / 2, this.height / 8, -1);
        this.mc.smallfontRenderer.drawCenteredString("\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0442\u0443\u0442 \u043d\u0438\u043a:", this.width / 2 - 65, this.height / 3 + 32, Integer.MAX_VALUE);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        this.field.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.field.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.field.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 2) {
            this.mc.session.username = "Fluger" + new Random().nextInt(1000000);
        }
        if (button.id == 1) {
            this.mc.session.username = this.field.getText();
        }
        if (button.id == 3) {
            this.mc.displayGuiScreen(this.old);
        }
        super.actionPerformed(button);
    }
}

