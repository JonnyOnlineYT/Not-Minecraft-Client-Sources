package Celestial.ui.altmanager;

import Celestial.utils.render.RenderUtils;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.ui.altmanager.alt.Alt;
import Celestial.ui.altmanager.alt.AltManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;


import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.net.Proxy;

public class GuiAddAlt extends GuiScreen {

    private final GuiAltManager manager;
    private PasswordField password;
    private String status;
    private GuiTextField username;

    GuiAddAlt(GuiAltManager manager) {
        this.status = TextFormatting.GRAY + "Idle...";
        this.manager = manager;
    }

    private static void setStatus(GuiAddAlt guiAddAlt, String status) {
        guiAddAlt.status = status;
    }

    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
                login.start();
                break;
            case 1:
                this.mc.displayGuiScreen(this.manager);
                break;
            case 2:
                String data;
                try {
                    data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                } catch (Exception var4) {
                    return;
                }

                if (data.contains(":")) {
                    String[] credentials = data.split(":");
                    this.username.setText(credentials[0]);
                    this.password.setText(credentials[1]);
                }
        }

    }

    public void drawScreen(int i, int j, float f) {
        RenderUtils.drawSmoothRect(0, 0, width, height, (new Color(22, 22, 22, 255)).getRGB());
        this.username.drawTextBox();
        this.password.drawTextBox();
        mc.neverlose500_18.drawCenteredString("Add Account", width / 2F, 15, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            mc.neverlose500_18.drawStringWithShadow("Username / E-Mail", width / 2 - 96, 66, -7829368);
        }

        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            mc.neverlose500_18.drawStringWithShadow("Password", width / 2 - 96, 106, -7829368);
        }

        mc.neverlose500_18.drawCenteredString(this.status, width / 2F, 30, -1);
        super.drawScreen(i, j, f);
    }

    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiAltButton(0, width / 2 - 100, height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiAltButton(1, width / 2 - 100, height / 4 + 116 + 12, "Back"));
        this.buttonList.add(new GuiAltButton(2, width / 2 - 100, height / 4 + 92 - 12, "Import User:Pass"));
        this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, width / 2 - 100, 100, 200, 20);
    }

    protected void keyTyped(char par1, int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }

        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }

    protected void mouseClicked(int par1, int par2, int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }

    private class AddAltThread extends Thread {
        private final String password;
        private final String username;

        AddAltThread(String username, String password) {
            this.username = username;
            this.password = password;
            GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GRAY + "Idle...");
        }

        private void checkAndAddAlt(String username, String password) {
            try {
                YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service.createUserAuthentication(Agent.MINECRAFT);
                auth.setUsername(username);
                auth.setPassword(password);

                try {
                    auth.logIn();
                    AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working));

                   /* try {
                        Main.instance.fileManager.getFile(Alts.class).saveFile();
                    } catch (Exception var6) {
                    }*/

                    GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Added alt - " + ChatFormatting.RED + (this.username) + ChatFormatting.BOLD + "(license)");
                } catch (AuthenticationException var7) {
                    GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Connect failed!");
                    var7.printStackTrace();
                }
            } catch (Throwable e) {
                GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Error");
                e.printStackTrace();
            }
        }

        public void run() {
            if (this.password.equals("")) {
                AltManager.registry.add(new Alt(this.username, ""));
                GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Added alt - " + ChatFormatting.RED + (this.username) + ChatFormatting.BOLD + "(non license)");
            } else {
                GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.AQUA + "Trying connect...");
                this.checkAndAddAlt(this.username, this.password);
            }
        }
    }
}
