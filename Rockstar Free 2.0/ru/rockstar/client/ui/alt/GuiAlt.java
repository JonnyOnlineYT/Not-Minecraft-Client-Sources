package ru.rockstar.client.ui.alt;

import java.awt.Color;
import java.io.IOException;
import java.security.SecureRandom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
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
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.glsandbox.animbackground;

import org.lwjgl.input.Keyboard;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;


public final class GuiAlt
        extends GuiScreen {
    private ResourceLocation resourceLocation;

    private PasswordField password;
    private final GuiScreen previousScreen;
    private AltLoginThread thread;
    private GuiTextField username;
    private final long initTime = System.currentTimeMillis();
    private static String alphabet = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
    private static final SecureRandom secureRandom = new SecureRandom();
    private animbackground backgroundShader;

    public GuiAlt(GuiScreen previousScreen) {
        try {
            this.backgroundShader = new animbackground("/noise.fsh");
        } catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
        this.previousScreen = previousScreen;

    }

    public static String randomString(int strLength) {
        StringBuilder stringBuilder = new StringBuilder(strLength);
        for (int i = 0; i < strLength; ++i) {
            stringBuilder.append(alphabet.charAt(secureRandom.nextInt(alphabet.length())));
        }
        return stringBuilder.toString();
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        switch (button.id) {
            case 2: {
                this.thread = new AltLoginThread("Rockstar" + GuiAlt.randomString(6), "");
                this.thread.start();
                break;
            }
            case 0: {
                this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
                this.thread.start();
            }
        }
    }
    private void getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
        TextureManager textureManager = mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(AbstractClientPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }
    @Override
    public void drawScreen(int x2, int y2, float z2) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        GlStateManager.disableCull();
        this.backgroundShader.useShader(sr.getScaledWidth()+700, sr.getScaledHeight(), x2, y2, (float) (System.currentTimeMillis() - this.initTime) /
                1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);

        GlStateManager.disableCull();
        int lol = height / 4 + 24;

        DrawHelper.drawRectWithGlow(width / 2 - 120, lol -10, width / 2 + 110, lol + 110, 5,3, new Color(0, 0, 0, 42));


        this.username.drawTextBoxalt();
        this.password.drawTextBox();
        Minecraft.getMinecraft().neverlose500_18.drawCenteredString("AltManager", width / 2 + -6, lol, -1);

        Minecraft.getMinecraft().neverlose500_15.drawCenteredString(this.thread == null ? TextFormatting.GRAY + "" : this.thread.getStatus(), width / 2 - 5, lol + 98, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            Minecraft.getMinecraft().neverlose500_16.drawStringWithShadow("Email", width / 2 - 52, lol + 24, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            Minecraft.getMinecraft().neverlose500_16.drawStringWithShadow("Pass", width / 2 - 52, lol + 44, -7829368);
        }



        GlStateManager.color(1, 1, 1, 1);

        this.resourceLocation = AbstractClientPlayer.getLocationSkin(this.mc.session.getUsername());
        this.getDownloadImageSkin(this.resourceLocation, this.mc.session.getUsername());
        mc.getTextureManager().bindTexture(this.resourceLocation);
        Gui.drawScaledCustomSizeModalRect(width / 2 - 99, lol + 22, 8.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f);


        super.drawScreen(x2, y2, z2);
    }

    @Override
    public void initGui() {
        int lol = height / 4 + 24;
        this.buttonList.add(new GuiButton(0, width / 2 - 50, lol + 60, 90, 13, "Login"));
        this.buttonList.add(new GuiButton(2, width / 2 - 50, lol + 64 + 12, 90, 13, "Random name"));
        this.username = new GuiTextField(lol, Minecraft.fontRendererObj, width / 2 - 55, lol + 20, 100, 13);
        this.password = new PasswordField(Minecraft.fontRendererObj, width / 2 - 55, lol + 40, 100, 13);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    @Override
    protected void mouseClicked(int x2, int y2, int button) {
        try {
            super.mouseClicked(x2, y2, button);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x2, y2, button);
        this.password.mouseClicked(x2, y2, button);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }

    static {
        alphabet = alphabet + alphabet.toLowerCase();
    }
}

