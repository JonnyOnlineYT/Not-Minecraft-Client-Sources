package net.minecraft.client.gui;

import de.strafe.Strafe;
import de.strafe.font.MCFontRenderer;
import de.strafe.utils.DrawMenuLogo;
import de.strafe.utils.LoginUtil;
import de.strafe.utils.RenderUtil;
import de.strafe.utils.UIButton;
import joptsimple.internal.Strings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui() {
        Strafe.INSTANCE.discordRP.update("Idle", "MainMenu");

        int i = 24;
        int j = this.height / 4 + 48;

        this.buttonList.add(new UIButton(1, this.width / 2 - 100, j, I18n.format("menu.singleplayer")));
        this.buttonList.add(new UIButton(2, this.width / 2 - 100, j + i * 1, I18n.format("menu.multiplayer")));
        this.buttonList.add(new UIButton(187, this.width / 2 - 100, j + i * 2, "Clipboard Login"));

        this.buttonList.add(new UIButton(0, this.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options")));
        this.buttonList.add(new UIButton(4, this.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit")));
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 4:
                this.mc.shutdown();
                break;
            case 187:
                final String[] split = getClipboardString().trim().split(".");
                if (split.length == 2)LoginUtil.login(split[0], split[1]);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.disableAlpha();
        this.drawDefaultBackground();
        DrawMenuLogo.drawString(5, Strafe.INSTANCE.NAME, this.width * 0.1f - this.fontRendererObj.getStringWidth(Strafe.INSTANCE.NAME) * 0.5f, this.height * 0.05f, Color.ORANGE.getRGB());
        String s1 = Strings.join(Strafe.INSTANCE.AUTHORS, " & ");
        String s2 = Strings.join(Collections.singletonList(Strafe.INSTANCE.VERSION), " & ");
        this.drawString(this.fontRendererObj, s2, this.width - this.fontRendererObj.getStringWidth(s1) - -6, this.height - 10, Color.GREEN.getRGB());
        this.drawString(this.fontRendererObj, mc.session.getUsername(), 2 - this.fontRendererObj.getStringWidth( mc.session.getUsername()) / 28  , this.height - 10, 0x05D8B3);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
