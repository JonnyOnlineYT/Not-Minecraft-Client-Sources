package Celestial.ui.altmanager;

import Celestial.utils.render.RenderUtils;
import Celestial.ui.altmanager.alt.Alt;
import Celestial.ui.altmanager.alt.AltLoginThread;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;

public final class GuiAltLogin extends GuiScreen {
    private final GuiScreen previousScreen;
    private PasswordField password;
    private AltLoginThread thread;
    private GuiTextField username;

    public GuiAltLogin(GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }

    protected void actionPerformed(GuiButton button) {
        try {
            switch (button.id) {
                case 0:
                    (this.thread = new AltLoginThread(new Alt(this.username.getText(), this.password.getText()))).start();
                    break;
                case 1:
                    this.mc.displayGuiScreen(this.previousScreen);
                    break;
                case 2:
                    String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (data.contains(":")) {
                        String[] credentials = data.split(":");
                        this.username.setText(credentials[0]);
                        this.password.setText(credentials[1]);
                    }
            }

        } catch (Throwable e) {
            throw new RuntimeException();
        }
    }

    public void drawScreen(int x, int y, float z) {
        RenderUtils.drawSmoothRect(0, 0, width, height, (new Color(22, 22, 22, 255)).getRGB());
        this.username.drawTextBox();
        this.password.drawTextBox();
        mc.neverlose500_18.drawStringWithShadow("Alt Login", width / 2F - 10, 20, -1);
        mc.neverlose500_18.drawStringWithShadow(this.thread == null ? TextFormatting.GRAY + "Alts..." : this.thread.getStatus(), width / 2F, 29, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            mc.neverlose500_18.drawStringWithShadow("Username / E-Mail", width / 2 - 96, 66, -7829368);
        }

        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            mc.neverlose500_18.drawStringWithShadow("Password", width / 2 - 96, 106, -7829368);
        }

        super.drawScreen(x, y, z);
    }

    public void initGui() {
        int height1 = height / 4 + 24;
        this.buttonList.add(new GuiAltButton(0, width / 2 - 100, height1 + 72 + 12, "Login"));
        this.buttonList.add(new GuiAltButton(1, width / 2 - 100, height1 + 72 + 12 + 24, "Back"));
        this.buttonList.add(new GuiAltButton(2, width / 2 - 100, height1 + 72 + 12 - 24, "Import User:Pass"));
        this.username = new GuiTextField(height1, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }

    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
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

    protected void mouseClicked(int x, int y, int button) {
        try {
            super.mouseClicked(x, y, button);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }

}