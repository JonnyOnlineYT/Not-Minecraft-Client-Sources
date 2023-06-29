//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ua.apraxia.gui.menu;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.other.DiscordUtility;
import ua.apraxia.utility.other.HoverUtility;
import ua.apraxia.utility.render.GLSL;
import ua.apraxia.utility.render.RenderUtility;
import ua.apraxia.utility.render.RoundedUtility;

public class GuiMainMenu extends GuiScreen {
    public static int counter = 1;
    private final long initTime = System.currentTimeMillis();
    public static int bg = 0;
    private GLSL backgroundShader;
    float offset;
    private List<String> buttons = new ArrayList();
    private String selectedButton = "";
    private GuiWorldSelection guiWorldSelection;
    private GuiMultiplayer guiMultiplayer;
    private GuiOptions guiOptions;
    public GuiMainMenu() {
    }
    public void initGui() {
        try {
            this.backgroundShader = new GLSL("/assets/noise.fsh");
        }
        catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
        new DiscordUtility().update("", "");
        this.buttons.clear();
        this.buttons.addAll(Arrays.asList("Одиночная игра", "Сетевая игра", "Настройки", "Аккаунты"));
    }
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.backgroundShader.useShader(sr.getScaledWidth(), sr.getScaledHeight(), mouseX, mouseY, (float)(System.currentTimeMillis() - this.initTime) / 1500.0f);
        GL11.glBegin((int)7);
        GL11.glVertex2f((float)-1.0f, (float)-1.0f);
        GL11.glVertex2f((float)-1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)1.0f);
        GL11.glVertex2f((float)1.0f, (float)-1.0f);
        GL11.glEnd();
        GL20.glUseProgram((int)0);
        GlStateManager.disableCull();
        GlStateManager.pushMatrix();
        RoundedUtility.drawRound((float)(sr.getScaledWidth() / 2 - 70), (float)(sr.getScaledHeight() / 2 - 40), 140.0F, 120.0F, 5.0F, new Color(0, 0, 0, 110));
        RenderUtility.drawBlur(() ->  RoundedUtility.drawRound((float)(sr.getScaledWidth() / 2 - 70), (float)(sr.getScaledHeight() / 2 - 40), 140.0F, 120.0F, 5.0F, new Color(0, 0, 0, 110)), 15);
        Fonts.sfbolt30.drawCenteredString("hexbyte", (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() / 2 - 30), -1);
        this.offset = 0.0F;
        for(Iterator var5 = this.buttons.iterator(); var5.hasNext(); this.offset += 20.0F) {
            String string = (String)var5.next();
            Fonts.medium14.drawCenteredString(string, (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() / 2 - 40 + 34 + 5) + this.offset, -1);
            RoundedUtility.drawRound((float)(sr.getScaledWidth() / 2 - 55), (float)(sr.getScaledHeight() / 2 - 40 + 34) + this.offset, 110.0F, 15.0F, 3.0F,  HoverUtility.isHovered(mouseX, mouseY, (double)(sr.getScaledWidth() / 2 - 55), (double)((float)(sr.getScaledHeight() / 2 - 40 + 34) + this.offset), 110.0, 15.0) ? new Color(255, 255, 255, 18) : new Color(255, 255, 255, 10));
        }
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.offset = 0.0F;
        for(Iterator var5 = this.buttons.iterator(); var5.hasNext(); this.offset += 20.0F) {
            String string = (String)var5.next();
            if (HoverUtility.isHovered(mouseX, mouseY, (double)(sr.getScaledWidth() / 2 - 55), (double)((float)(sr.getScaledHeight() / 2 - 40 + 34) + this.offset), 110.0, 15.0)) {
                this.selectedButton = string;
            }
        }
        switch (this.selectedButton) {
            case "Одиночная игра":
                this.guiWorldSelection = new GuiWorldSelection(this.guiWorldSelection);
                this.mc.displayGuiScreen(this.guiWorldSelection);
                this.selectedButton = "";
                break;
            case "Сетевая игра":
                this.guiMultiplayer = new GuiMultiplayer(this.guiMultiplayer);
                this.mc.displayGuiScreen(this.guiMultiplayer);
                this.selectedButton = "";
                break;
            case "Настройки":
                this.guiOptions = new GuiOptions(this.guiOptions, this.mc.gameSettings);
                this.mc.displayGuiScreen(this.guiOptions);
                this.selectedButton = "";
                break;
            case "Аккаунты":
                GuiScreen changeUser = new AltLogin();
                this.mc.displayGuiScreen(changeUser);
                this.selectedButton = "";
        }
    }
}
