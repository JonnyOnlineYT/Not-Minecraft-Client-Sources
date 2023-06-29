package ru.rockstar.client.ui;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.Main;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.api.utils.other.ParticleEngine;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.ui.clickgui.ScreenHelper;
import ru.rockstar.client.ui.settings.button.ConfigGuiButton;
import ru.rockstar.client.ui.settings.button.ImageButton;
import ru.rockstar.client.ui.settings.config.Config;
import ru.rockstar.client.ui.settings.config.ConfigManager;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiConfig extends GuiScreen {

    public static GuiTextField search;
    public static Config selectedConfig = null;
    public ScreenHelper screenHelper;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private int width, height;
    private int width2;
    private float scrollOffset;
    ParticleEngine idinahyi = new ParticleEngine();
    public Category type;

    public GuiConfig() {
        this.screenHelper = new ScreenHelper(0, 0);

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            Main.instance.configManager.saveConfig(search.getText());
            Main.msg(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "created config: " + ChatFormatting.RED + "\"" + search.getText() + "\"",true);
            NotificationPublisher.queue("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "created config: " + ChatFormatting.RED + "\"" + search.getText() + "\"",  NotificationType.SUCCESS);
            ConfigManager.getLoadedConfigs().clear();
            Main.instance.configManager.load();
            search.setFocused(false);
            search.setText("");
        }
        if (selectedConfig != null) {
            if (button.id == 2) {
                if (Main.instance.configManager.loadConfig(selectedConfig.getName())) {
                    NotificationPublisher.queue("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",  NotificationType.SUCCESS);
                } else {
                    NotificationPublisher.queue("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",  NotificationType.ERROR);
                }
            } else if (button.id == 3) {
                if (Main.instance.configManager.saveConfig(selectedConfig.getName())) {
                    NotificationPublisher.queue("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",  NotificationType.SUCCESS);
                } else {
                    NotificationPublisher.queue("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + search.getText() + "\"",  NotificationType.ERROR);
                }
            } else if (button.id == 4) {
                if (Main.instance.configManager.deleteConfig(selectedConfig.getName())) {
                    NotificationPublisher.queue("Config", ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",  NotificationType.SUCCESS);
                } else {
                    NotificationPublisher.queue("Config", ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + selectedConfig.getName() + "\"",  NotificationType.ERROR);
                }
            }
        }
        super.actionPerformed(button);
    }

    private boolean isHoveredConfig(int x, int y, int width, int height, int mouseX, int mouseY) {
        return MouseHelper.isHovered(x, y, x + width, y + height, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        if (ClickGUI.blur.getBoolValue()) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        ScaledResolution sr = new ScaledResolution(mc);


        this.screenHelper = new ScreenHelper(0, 0);
        width = sr.getScaledWidth() / 2;
        width2 = sr.getScaledWidth();
        height = sr.getScaledHeight() / 2 + 50;
        search = new GuiTextField(228, mc.fontRendererObj, width - 125, height - 133, 250, 13);
        this.buttonList.add(new ConfigGuiButton(1, width - (int) 25.5f , height - (int) 95.5f, "Create"));
        this.buttonList.add(new ConfigGuiButton(2, width - 91, height - 115, "Load"));
        this.buttonList.add(new ConfigGuiButton(3, width - 61, height - 115, "Save"));
        this.buttonList.add(new ConfigGuiButton(4, width - 25, height - 115, "Delete"));



        this.imageButtons.clear();
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        Color colora = Color.WHITE;
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        Color twocolor = new Color(ClickGUI.colorTwo.getColorValue());
        double speed = ClickGUI.speed.getNumberValue();
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Client":
                colora = ClientHelper.getClientColor();
                break;
            case "Fade":
                colora = new Color(ClickGUI.color.getColorValue());
                break;
            case "Astolfo":
                colora = DrawHelper.astolfo(true, width);
                break;
            case "Color Two":
                colora = new Color(DrawHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + height * 6L / 60 * 2) % 2) - 1)));
                break;
            case "Rainbow":
                colora = DrawHelper.rainbow(300, 1, 1);
                break;
            case "Category":
                colora = new Color(type.getColor());
                break;
            case "Static":
                colora = onecolor;
                break;
        }
        Color none = new Color(0, 0, 0, 0);

        if (ClickGUI.background.getBoolValue()) {
            this.drawDefaultBackground();
            this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), none.getRGB(), colora.getRGB());
        }


        for (Config config : Main.instance.configManager.getContents()) {
            if (config != null) {
                if (Mouse.hasWheel()) {
                    if (isHoveredConfig(width - 100, height - 122, 151, height + 59, mouseX, mouseY)) {
                        int wheel = Mouse.getDWheel();
                        if (wheel < 0) {
                            this.scrollOffset += 13;
                            if (this.scrollOffset < 0) {
                                this.scrollOffset = 0;
                            }
                        } else if (wheel > 0) {
                            this.scrollOffset -= 13;
                            if (this.scrollOffset < 0) {
                                this.scrollOffset = 0;
                            }
                        }
                    }
                }
            }
        }
        GlStateManager.pushMatrix();
        DrawHelper.drawRectWithGlow(width - 132, height - 160, width + 132, height + 25,10, 5, new Color(20, 20, 20, 230));
        DrawHelper.drawRectWithGlow(width - 25 + 155, height - 135,  width - 25 + 95, height - 95 - 26,5,10,new Color(1,1,1,130));
        mc.mntsb_20.drawCenteredStringWithShadow("Конфиг Менеджер", width - 70, height - 155, -1);
        search.drawTextBox();
        if (search.getText().isEmpty() && !search.isFocused()) {
            mc.mntsb.drawStringWithShadow( "Config name...", width - 125, height - 130, DrawHelper.getColor(200));
        }
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        int yDist = 5;
        int color;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        DrawHelper.scissorRect(0F, height - 119, width+130, height + 20);
        for (Config config : Main.instance.configManager.getContents()) {
            if (config != null) {
                if (isHoveredConfig(width-150 , (int) (height - 117 + yDist - this.scrollOffset), width+100, 14, mouseX, mouseY)) {
                    color = -1;
                    if (Mouse.isButtonDown(0)) {
                        selectedConfig = new Config(config.getName());
                    }
                } else {
                    color = DrawHelper.getColor(200);
                }
                if (selectedConfig != null && config.getName().equals(selectedConfig.getName())) {
                    DrawHelper.drawRectWithGlow(width - 125, (height - 119 + yDist) - this.scrollOffset, width +  125, (height - 107 + yDist) - this.scrollOffset, 6, 10, new Color(66, 66, 66, 255));
                }
                DrawHelper.drawRectWithGlow(width - 125, (height - 119 + yDist) - this.scrollOffset, width +  125, (height - 107 + yDist) - this.scrollOffset, 5, 10, new Color(1, 1, 1, 255));
                mc.neverlose500_16.drawCenteredString(config.getName(), width + mc.neverlose500_16.getStringWidth(config.getName()) / 2 - 120, (height - 115 + yDist) - this.scrollOffset, color);
                yDist += 20;
            }
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        search.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.scrollOffset < 0) {
            this.scrollOffset = 0;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (Config config : Main.instance.configManager.getContents()) {
            if (config != null) {
                if (keyCode == 200) {
                    this.scrollOffset += 13;
                } else if (keyCode == 208) {
                    this.scrollOffset -= 13;
                }
                if (this.scrollOffset < 0) {
                    this.scrollOffset = 0;
                }
            }
        }
        search.textboxKeyTyped(typedChar, keyCode);
        search.setText(search.getText().replace(" ", ""));
        if ((typedChar == '\t' || typedChar == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }
        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        screenHelper.interpolate(width, height, 2 * Minecraft.frameTime / 6);

        selectedConfig = null;
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
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

