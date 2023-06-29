package org.moonware.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;
import net.minecraft.util.text.Formatting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.config.Config;
import org.moonware.client.settings.config.ConfigManager;
import org.moonware.client.ui.button.ConfigGuiButton;
import org.moonware.client.ui.button.ImageButton;
import org.moonware.client.ui.shader.notification.NotificationManager;
import org.moonware.client.ui.shader.notification.NotificationType;

import java.awt.*;
import java.util.ArrayList;

public class GuiConfig extends Screen {

    public static GuiTextField search;
    public static Config selectedConfig;
    public ScreenHelper screenHelper;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private int width, height;
    private float scrollOffset;

    public GuiConfig() {
        screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void actionPerformed(ButtonWidget button) {
        if (button.id == 1) {
            MoonWare.configManager.saveConfig(search.getText());
            MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "saved config: " + Formatting.RED + "\"" + search.getText() + "\"");
            NotificationManager.publicity("Config", Formatting.GREEN + "Successfully " + Formatting.WHITE + "created config: " + Formatting.RED + "\"" + search.getText() + "\"", 4, NotificationType.SUCCESS);
            ConfigManager.getLoadedConfigs().clear();
            MoonWare.configManager.load();
            search.setFocused(false);
            search.setText("");
        }
        if (selectedConfig != null) {
            if (button.id == 2) {
                if (MoonWare.configManager.loadConfig(selectedConfig.getName())) {
                    MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "loaded config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", Formatting.GREEN + "Successfully " + Formatting.WHITE + "loaded config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                } else {
                    MWUtils.sendChat(Formatting.RED + "Failed " + Formatting.WHITE + "load config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", Formatting.RED + "Failed " + Formatting.WHITE + "load config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.ERROR);
                }
            } else if (button.id == 3) {
                if (MoonWare.configManager.saveConfig(selectedConfig.getName())) {
                    MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "saved config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", Formatting.GREEN + "Successfully " + Formatting.WHITE + "saved config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                    ConfigManager.getLoadedConfigs().clear();
                    MoonWare.configManager.load();
                } else {
                    MWUtils.sendChat(Formatting.RED + "Failed " + Formatting.WHITE + "to save config: " + Formatting.RED + "\"" + search.getText() + "\"");
                    NotificationManager.publicity("Config", Formatting.RED + "Failed " + Formatting.WHITE + "to save config: " + Formatting.RED + "\"" + search.getText() + "\"", 4, NotificationType.ERROR);
                }
            } else if (button.id == 4) {
                if (MoonWare.configManager.deleteConfig(selectedConfig.getName())) {
                    MWUtils.sendChat(Formatting.GREEN + "Successfully " + Formatting.WHITE + "deleted config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", Formatting.GREEN + "Successfully " + Formatting.WHITE + "deleted config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.SUCCESS);
                } else {
                    MWUtils.sendChat(Formatting.RED + "Failed " + Formatting.WHITE + "to delete config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"");
                    NotificationManager.publicity("Config", Formatting.RED + "Failed " + Formatting.WHITE + "to delete config: " + Formatting.RED + "\"" + selectedConfig.getName() + "\"", 4, NotificationType.ERROR);
                }
            }
        }
        super.actionPerformed(button);
    }

    private boolean isHoveredConfig(int x, int y, int width, int height, int mouseX, int mouseY) {
        return MWUtils.isHovered(x, y, x + width, y + height, mouseX, mouseY);
    }

    @Override
    public void init() {
        screenHelper = new ScreenHelper(0, 0);
        ScaledResolution sr = new ScaledResolution(minecraft);
        width = sr.getScaledWidth() / 2;
        height = sr.getScaledHeight() / 2;
        search = new GuiTextField(228, Minecraft.font, width - 100, height + 62, 85, 13);
        widgets.add(new ConfigGuiButton(1, width - 105, height + 102, "Create"));
        widgets.add(new ConfigGuiButton(2, width - 40, height - 48, "Load"));
        widgets.add(new ConfigGuiButton(3, width - 40, height - 65, "Save"));
        widgets.add(new ConfigGuiButton(4, width - 40, height - 82, "Delete"));
        imageButtons.clear();
        imageButtons.add(new ImageButton(new Namespaced("moonware/close-button.png"), width + 106, height - 135, 8, 8, "", 19));
        super.init();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        ScaledResolution sr = new ScaledResolution(minecraft);
        drawDefaultBackground();
        screenHelper.interpolate(sr.getScaledWidth(), sr.getScaledHeight(), 6);
        GL11.glPushMatrix();
        for (Config config : MoonWare.configManager.getContents()) {
            if (config != null) {
                if (Mouse.hasWheel()) {
                    if (isHoveredConfig(width - 100, height - 122, 151, height + 59, mouseX, mouseY)) {
                        int wheel = Mouse.getDWheel();
                        if (wheel < 0) {
                            scrollOffset += 13;
                            if (scrollOffset < 0) {
                                scrollOffset = 0;
                            }
                        } else if (wheel > 0) {
                            scrollOffset -= 13;
                            if (scrollOffset < 0) {
                                scrollOffset = 0;
                            }
                        }
                    }
                }
            }
        }
        GlStateManager.pushMatrix();
//        RectHelper.drawSkeetRectWithoutBorder(width - 70, height - 80, width + 80, height + 20);
        Color color1 = new Color(ClientHelper.getClientColor().getRGB());
        Color color2 = DrawHelper.fade(color1,10,100);
        Color color3 = color2;
        Color color4 = color2;
        RoundedUtil.drawGradientRound(width - 110,height - 140,233, 217,2,color1,color2,color3,color4);
        // RectHelper.drawSkeetButton(width - 70, height - 80, width + 20, height + 90);
        FontStorage.circleregular.drawOutline("Config System", width - 100, height - 135, -1);
        search.drawTextBox();

        if (search.getText().isEmpty() && !search.isFocused()) {
            MWFont.MONTSERRAT_BOLD.get(16).draw("...", width - 97, height + 65, PaletteHelper.getColor(200));
        }
        for (ImageButton imageButton : imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        int yDist = 0;
        int color;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderHelper.scissorRect(0F, height - 124, width + 35, height + 60);
        RoundedUtil.drawRoundOutline(width - 100,height - 124,width + 35,height + 60,7,2,new Color(0, 255, 203, 255),new Color(87, 216, 255, 210));

        for (Config config : MoonWare.configManager.getContents()) {
            if (config != null) {
                if (isHoveredConfig(width - 96, (int) (height - 117 + yDist - scrollOffset), Minecraft.font.getStringWidth(config.getName()) + 5, 14, mouseX, mouseY)) {
                    color = -1;
                    if (Mouse.isButtonDown(0)) {
                        selectedConfig = new Config(config.getName());
                    }
                } else {
                    color = PaletteHelper.getColor(200);
                }
                if (selectedConfig != null && config.getName().equals(selectedConfig.getName())) {
                    RectHelper.drawBorder(width - 98, (height - 119 + yDist) - scrollOffset, width + Minecraft.font.getStringWidth(config.getName()) - 90, (height - 107 + yDist) - scrollOffset, 0.65F, new Color(255, 255, 255, 75).getRGB(), new Color(0, 0, 0, 255).getRGB(), true);

                }
                Minecraft.font.drawStringWithOutline(config.getName(), width - 95, (height - 117 + yDist) - scrollOffset, color);
                yDist += 15;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        GL11.glPopMatrix();
        super.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {
        search.mouseClicked(mouseX, mouseY, button);
        if (scrollOffset < 0) {
            scrollOffset = 0;
        }
        super.mousePressed(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(int key, char c) {
        for (Config config : MoonWare.configManager.getContents()) {
            if (config != null) {
                if (key == 200) {
                    scrollOffset += 13;
                } else if (key == 208) {
                    scrollOffset -= 13;
                }
                if (scrollOffset < 0) {
                    scrollOffset = 0;
                }
            }
        }
        search.textboxKeyTyped(c, key);
        search.setText(search.getText().replace(" ", ""));
        if ((c == '\t' || c == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }
        super.keyPressed(key, c);
    }

    @Override
    public void onClosed() {
        selectedConfig = null;
        super.onClosed();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
