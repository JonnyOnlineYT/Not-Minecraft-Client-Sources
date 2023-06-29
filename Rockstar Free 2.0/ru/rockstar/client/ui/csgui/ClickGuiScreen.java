package ru.rockstar.client.ui.csgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.api.utils.other.ParticleEngine;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.glsandbox.animbackground;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.features.impl.visuals.SwingAnimations;
import ru.rockstar.client.ui.csgui.component.Component;
import ru.rockstar.client.ui.csgui.component.ExpandableComponent;
import ru.rockstar.client.ui.settings.button.ImageButton;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import static ru.rockstar.client.ui.GuiConfig.search;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ClickGuiScreen extends GuiScreen {
    public static boolean escapeKeyInUse;
    public List<Panel> components = new ArrayList<>();
    public ScreenHelper screenHelper;
    public boolean exit = false;
    public Category type;
    private animbackground backgroundShader;
    private ru.rockstar.client.ui.csgui.component.Component selectedPanel;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();

    public ClickGuiScreen() {
        try {
            this.backgroundShader = new animbackground("/noise.fsh");
        }
        catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
		int height = 20;
        int x = 30;
        int y = 20;
        for (Category type : Category.values()) {
            this.type = type;
            this.components.add(new Panel(type, x, y));
            selectedPanel = new Panel(type, x, y);
            x += height + 50;
        }
        this.screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void initGui() {
    	
    	
        ScaledResolution sr = new ScaledResolution(mc);
        this.screenHelper = new ScreenHelper(0, 0);
        this.imageButtons.clear();
        this.imageButtons.add(new ImageButton(new ResourceLocation("rockstar/config.png"), (int) 5.5, sr.getScaledHeight()- 25, 20, 20, "Я гей, но никто об этом не узнает", 22));

        if (ClickGUI.blur.getBoolValue()) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
        super.initGui();
    }
    public static void main(String[] args) throws Exception {
        String imageUrl = "http://www.avajava.com/images/avajavalogo.jpg";
        String destinationFile = "image.jpg";

        saveImage(imageUrl, destinationFile);
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        ScaledResolution sr = new ScaledResolution(mc);
    //    DrawHelper.drawSmoothRect(2, 2, 30, 30, new Color(1, 1, 1, 148).getRGB());

        Color color = Color.WHITE;
        Color onecolor = new Color(ClickGUI.color.getColorValue());
        Color twocolor = new Color(ClickGUI.colorTwo.getColorValue());
        double speed = ClickGUI.speed.getNumberValue();
        switch (ClickGUI.clickGuiColor.currentMode) {
            case "Client":
                color = ClientHelper.getClientColor();
                break;
            case "Fade":
                color = new Color(ClickGUI.color.getColorValue());
                break;
            case "Astolfo":
                color = DrawHelper.astolfo(true, width);
                break;
            case "Color Two":
                color = new Color(DrawHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + height * 6L / 60 * 2) % 2) - 1)));
                break;
            case "Rainbow":
                color = DrawHelper.rainbow(300, 1, 1);
                break;
            case "Category":
                color = new Color(type.getColor());
                break;
            case "Static":
                color = onecolor;
                break;
        }
        Color none = new Color(0, 0, 0, 0);
        String mode = ClickGUI.backGroundMode.getOptions();
        if (ClickGUI.background.getBoolValue()) {
            if (mode.equalsIgnoreCase("Top")) {
                drawDefaultBackground();
                this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), color.getRGB(), none.getRGB());
            } else if (mode.equalsIgnoreCase("Bottom")) {
                drawDefaultBackground();
                this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), none.getRGB(), color.getRGB());
            } else if (mode.equalsIgnoreCase("Everywhere")) {
                drawDefaultBackground();
                this.drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), DrawHelper.setAlpha(color, 100).getRGB(), DrawHelper.setAlpha(color, 100).getRGB());
            } else if (mode.equalsIgnoreCase("Shader")) {
            	GlStateManager.disableCull();
                long initTime = System.currentTimeMillis();
                this.backgroundShader.useShader(sr.getScaledWidth() + 80000, sr.getScaledHeight(), mouseX, mouseY, (float) (System.currentTimeMillis() - initTime) / 5000.0f);
                GL11.glBegin(7);
                GL11.glVertex2f(-1.0f, -1.0f);
                GL11.glVertex2f(-1.0f, 1.0f);
                GL11.glVertex2f(1.0f, 1.0f);
                GL11.glVertex2f(1.0f, -1.0f);
                GL11.glEnd();
                GL20.glUseProgram(0);

                GlStateManager.disableCull();
            }
        }
        
        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, mouseY);
        }


        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }



        if (exit) {
            screenHelper.interpolate(0, 0, 2);
            if (screenHelper.getY() < 200) {
                exit = false;
                this.mc.displayGuiScreen(null);
                if (this.mc.currentScreen == null) {
                    this.mc.setIngameFocus();
                }
            }
        } else {
            screenHelper.interpolate(width, height, 3 * Minecraft.frameTime / 6);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
 /*
    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (Panel panel : components) {
            if (scrollWheel > 0) {
                panel.setY(panel.getY() + 15);
            }
            if (scrollWheel < 0) {
                panel.setY(panel.getY() - 15);
            }
        }
    */

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1)
            exit = true;

        if (exit)
            return;

        selectedPanel.onKeyPress(keyCode);

        if (!escapeKeyInUse) {
            super.keyTyped(typedChar, keyCode);
        }

        escapeKeyInUse = false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        for (Component component : components) {
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = (ExpandableComponent) component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                selectedPanel = component;
                component.onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        selectedPanel.onMouseRelease(state);
    }

    @Override
    public void onGuiClosed() {
        this.screenHelper = new ScreenHelper(0, 0);
        mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }
}
