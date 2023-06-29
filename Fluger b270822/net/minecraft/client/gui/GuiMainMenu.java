/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.gui;

import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.ChangeLog;
import ru.fluger.client.Fluger;
import ru.fluger.client.helpers.input.MouseHelper;
import ru.fluger.client.helpers.render.RoundedUtil;
import ru.fluger.client.helpers.render.animbackground.animbackground;
import ru.fluger.client.helpers.render.rect.RectHelper;
import ru.fluger.client.ui.GuiAltManager;
import ru.fluger.client.ui.button.GuiMainMenuButton;
import ru.fluger.client.ui.button.ImageButton;
import ru.fluger.client.ui.button.ImgButton;
import ru.squad47.UserData;

public class GuiMainMenu
extends GuiScreen {
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private double scrollOffset;
    public static animbackground backgroundShader;
    public ChangeLog log = new ChangeLog();

    public GuiMainMenu() {
    }

    @Override
    public void initGui() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        this.buttonList.add(new GuiMainMenuButton(1, width / 2 - 50, height / 2 - 50, 100, 5, "\u0421\u043f\u0438\u0441\u043e\u043a \u0441\u0435\u0440\u0432\u0435\u0440\u043e\u0432"));
        this.buttonList.add(new GuiMainMenuButton(0, width / 2 - 50, height / 2 - 50, 100, 5, "\u041e\u0434\u0438\u043d\u043e\u0447\u043d\u0430\u044f \u0438\u0433\u0440\u0430", true));
        this.buttonList.add(new GuiMainMenuButton(2, width / 2 - 50, height / 2 - 50, 100, 5, "\u041c\u0435\u043d\u0435\u0434\u0436\u0435\u0440 \u0430\u043a\u043a\u0430\u0443\u043d\u0442\u043e\u0432", true));
        this.buttonList.add(new GuiMainMenuButton(3, width / 2 - 50, height / 2 - 50, 100, 5, "\u041d\u0430\u0441\u0442\u0440\u043e\u0439\u043a\u0438", true));
        this.buttonList.add(new ImgButton(new ResourceLocation("nightmare/clickgui/vk.png"), 1337, 100, 100, 70, 10, "\u0412\u043a\u043e\u043d\u0442\u0430\u043a\u0442\u0435"));
        this.buttonList.add(new ImgButton(new ResourceLocation("nightmare/clickgui/youtube.png"), 228, 100, 100, 85, 10, "YouTube \u043a\u0430\u043d\u0430\u043b"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        RectHelper.drawRect(0.0, 0.0, this.width, this.height, new Color(10, 10, 10).getRGB());
        int x = this.width / 2;
        int y = this.height / 2 + 20;
        int width = 400;
        int height = 100;
        boolean small = this.width < 600;
        boolean bl = small;
        if (small) {
            width = 400;
            height = 130;
        }
        if (!small) {
            width = 400;
            height = 150;
        }
        int headerWidth = 116;
        int headerHeight = 25;
        int headerX = x - width / 2 + 6;
        int headerY = y - height / 2 - 40;
        int offfsetX = 0;
        int offset = 0;
        for (GuiButton b : this.buttonList) {
            if (b instanceof ImgButton) {
                b.xPosition = x - width / 2 + offfsetX + 10;
                b.yPosition = y - height / 2 + height + 10;
                offfsetX += b.getButtonWidth();
                continue;
            }
            if (!b.displayString.contains("\u0421\u043f\u0438\u0441\u043e\u043a \u0441\u0435\u0440\u0432\u0435\u0440\u043e\u0432")) {
                b.xPosition = x - width / 2 + 45 + offset;
                b.yPosition = y - height / 2 + height - 20;
                offset += b.getButtonWidth() + 5;
                continue;
            }
            b.yPosition = y - height / 2 - 20;
        }
        RoundedUtil.drawGradientRound(x - width / 2, y - height / 2, width, height, 10.0f, new Color(5, 5, 5), new Color(10, 10, 10), new Color(10, 10, 10), new Color(5, 5, 5));
        RoundedUtil.drawGradientRound(headerX, headerY, headerWidth, headerHeight, 8.0f, new Color(90, 0, 200), new Color(100, 0, 255), new Color(40, 50, 220), new Color(40, 40, 255));
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.mc.ubuntuFontRender18.drawCenteredString("FLUGER CLIENT SLIV SRC BY TUSKEVICH", x, y - height / 2 - 50, -1);
        this.mc.ubuntuFontRender14.drawCenteredString("version: penis flugera", x, y - height / 2 - 40, -1);
        this.mc.fontRenderer.drawString("\u041b\u043e\u0433\u0438\u043d: " + UserData.instance().getName(), headerX + 26, headerY + 7, -1);
        this.mc.fontRenderer.drawString("\u0410\u043a\u0442\u0438\u0432\u043d\u043e \u0434\u043e: " + UserData.instance().getLicenseDate(), headerX + 26, headerY + 15, -1);
        this.mc.fontRenderer.drawString("\u041f\u043e\u0441\u043b\u0435\u0434\u043d\u0438\u0435 \u043e\u0431\u043d\u043e\u0432\u043b\u0435\u043d\u0438\u0435:", x - width / 2 + 10, y - height / 2 + 10, -1);
        int logOffset = 0;
        for (String s : this.log.changelog) {
            this.mc.fontRenderer.drawString(s, x - width / 2 + 12, y - height / 2 + 20 + logOffset, -1);
            logOffset += 10;
        }
    }

    public static void drawTexturedModalRect(ResourceLocation location, double x, double y, int textureX, int textureY, double width, double height) {
        boolean alpha_test = GL11.glIsEnabled((int)3008);
        GL11.glEnable((int)3008);
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex((float)textureX * 0.00390625f, (float)((double)textureY + height) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((float)((double)textureX + width) * 0.00390625f, (float)((double)textureY + height) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((float)((double)textureX + width) * 0.00390625f, (float)textureY * 0.00390625f).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex((float)textureX * 0.00390625f, (float)textureY * 0.00390625f).endVertex();
        tessellator.draw();
        if (alpha_test) {
            GL11.glEnable((int)3008);
        } else {
            GL11.glDisable((int)3008);
        }
    }

    public static void drawTexturedRect(ResourceLocation location, double xStart, double yStart, double width, double height, double scale) {
        GL11.glPushMatrix();
        GL11.glEnable((int)3042);
        GL11.glScaled((double)scale, (double)scale, (double)scale);
        GuiMainMenu.drawTexturedModalRect(location, xStart / scale, yStart / scale, 0, 0, width, height);
        GL11.glDisable((int)3042);
        GL11.glPopMatrix();
    }

    protected boolean isHovered(int x, int y, int mouseX, int mouseY) {
        return MouseHelper.isHovered(x, y, x + this.width, y + this.height, mouseX, mouseY);
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 1337: {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(new URI("https://vk.com/mincedclient"));
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case 228: {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.browse(new URI("https://vk.com/mincedclient"));
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiAltManager(this));
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 4: {
                this.mc.shutdown();
            }
        }
        super.actionPerformed(button);
    }
}

