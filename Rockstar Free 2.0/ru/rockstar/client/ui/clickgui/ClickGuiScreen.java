package ru.rockstar.client.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.Main;
import ru.rockstar.api.utils.other.ParticleEngine;
import ru.rockstar.api.utils.other.SoundHelper;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.api.utils.render.glsandbox.animbackground;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.impl.display.ClickGUI;
import ru.rockstar.client.features.impl.visuals.SwingAnimations;
import ru.rockstar.client.ui.clickgui.component.Component;
import ru.rockstar.client.ui.clickgui.component.ExpandableComponent;
import ru.rockstar.client.ui.settings.button.ImageButton;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import javafx.animation.Interpolator;

import static ru.rockstar.client.ui.GuiConfig.search;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
    private ru.rockstar.client.ui.clickgui.component.Component selectedPanel;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    public static float progress;
    public static float progress2;
    private long lastMS;

    public ClickGuiScreen() {
        try {
            this.backgroundShader = new animbackground("/noise.fsh");
        }
        catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
		int height = 20;
		this.progress = 0.0f;
		this.progress2 = 0.0f;
        int x = 5;
        int y = 15;
        for (Category type : Category.values()) {
            this.type = type;
            this.components.add(new Panel(type, x, y));
            selectedPanel = new Panel(type, x, y);
            x += height + 85;
        }
        this.screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void initGui() {
        
        if (ClickGUI.sound.getBoolValue()) {
        	SoundHelper.playSound("gui.wav");
        }
    	
    	this.lastMS = System.currentTimeMillis();
        ScaledResolution sr = new ScaledResolution(mc);
        this.screenHelper = new ScreenHelper(0, 0);
        this.imageButtons.clear();
        this.progress = 0.0f;
        this.progress2 = 0.0f;
        this.imageButtons.add(new ImageButton(new ResourceLocation("rockstar/config.png"), (int) 5.5, sr.getScaledHeight()- 25, 20, 20, "Я гей, но никто об этом не узнает", 22));
        this.imageButtons.add(new ImageButton(new ResourceLocation("rockstar/icons/visuals.png"), (int) 25.5, sr.getScaledHeight()- 25, 20, 20, "Я гей, но никто об этом не узнает", 24));
        //this.imageButtons.add(new ImageButton(new ResourceLocation("rockstar/esp.png"), (int) 45.5, sr.getScaledHeight()- 25, 20, 20, "Я гей, но никто об этом не узнает", 20));
        if (ClickGUI.blur.getBoolValue()) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blurArea.json"));
        }
        super.initGui();
    }
    public static void main(String[] args) throws Exception {
        String imageUrl = "http://www.avajava.com/images/avajavalogo.jpg";
        String destinationFile = "image.jpg";

        saveImage(imageUrl, destinationFile);
    }
    
    public static double createAnimation(final double phase) {
        return 1.0 - Math.pow(1.0 - phase, 3.0);
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
        
        if (ClickGUI.animation.getBoolValue()) {
        	 this.progress = (float)Interpolator.LINEAR.interpolate((double)this.progress, 1, 0.1);
        	 
        		 this.progress2 = (float)Interpolator.LINEAR.interpolate((double)this.progress2, (this.progress >= 0.9f) ? 1 : 0, 0.1);
        } else {
        	this.progress = 1.0f;
        	 this.progress2 = 1f;
        }
        
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
        GlStateManager.pushMatrix();
        
        
        if (ClickGUI.background.getBoolValue()) {
            if (mode.equalsIgnoreCase("Top")) {
                drawDefaultBackground();
                this.drawGradientRect(0, 0, sr.getScaledWidth(), (int) (sr.getScaledHeight() * (1 + 1 - ClickGuiScreen.progress2)), color.getRGB(), none.getRGB());
            } else if (mode.equalsIgnoreCase("Bottom")) {
                drawDefaultBackground();
                this.drawGradientRect(0, 0, sr.getScaledWidth(), (int) (sr.getScaledHeight() * (1 + ClickGuiScreen.progress2)), none.getRGB(), color.getRGB());
            } else if (mode.equalsIgnoreCase("Everywhere")) {
                drawDefaultBackground();
                this.drawGradientRect(0, 0, sr.getScaledWidth(), (int) (sr.getScaledHeight()), DrawHelper.setAlpha(color, (int) (100 * (ClickGuiScreen.progress2))).getRGB(), DrawHelper.setAlpha(color, (int) (100 * (ClickGuiScreen.progress2))).getRGB());
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
        /*
        try {
			drawTexturedRect(Main.getResourceLocation(), 5, 171.5f, 256, 256, 0.1);
		} catch (Exception e) {
			Main.resourceLocation = new ResourceLocation("rockstar/avatar.png");
		}*/
        	
        	//DrawHelper.drawRectWithGlow(sr.getScaledWidth() - mc.mntsb_20.getStringWidth("Username: " + CFontUser.username) - 5 - 5,sr.getScaledHeight() - 25 - 5, sr.getScaledWidth() - 5,sr.getScaledHeight() - 5, 8, 10, new Color(0,0,0,255));
        	
        
        
        
        

        
        
        
        	
        	
        	
        	
        	
        
        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, mouseY);
        }


        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        GlStateManager.popMatrix();


        updateMouseWheel(mouseX, mouseY);

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
    
    public static void drawTexturedModalRect(ResourceLocation location, double x, double y, int textureX, int textureY,
			double width, double height) {
		boolean alpha_test = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		Minecraft.getMinecraft().getTextureManager().bindTexture(location);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, y + height, 0)
				.tex((float) (textureX) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
		bufferbuilder.pos(x + width, y + height, 0)
				.tex((float) (textureX + width) * 0.00390625F, (float) (textureY + height) * 0.00390625F).endVertex();
		bufferbuilder.pos(x + width, y, 0)
				.tex((float) (textureX + width) * 0.00390625F, (float) (textureY) * 0.00390625F).endVertex();
		bufferbuilder.pos(x, y, 0).tex((float) (textureX) * 0.00390625F, (float) (textureY) * 0.00390625F).endVertex();
		tessellator.draw();
		if (alpha_test) {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		} else {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}
	}

    
    public static void drawTexturedRect(ResourceLocation location, double xStart, double yStart, double width,
			double height, double scale) {
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glScaled(scale, scale, scale);
		drawTexturedModalRect(location, xStart / scale, yStart / scale, 0, 0, width, height);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

    public void updateMouseWheel(int mouseX, int mouseY) {
        int scrollWheel = Mouse.getDWheel();
        for (ru.rockstar.client.ui.clickgui.component.Component panel : components) {
        	if (ClickGUI.style.getCurrentMode().equalsIgnoreCase("Rockstar New")) {
                	if (mouseX < panel.getX() + 100 && mouseX > panel.getX() && mouseY < 240 && mouseY > 24) {
                		if (scrollWheel > 0 && panel.getY() < 25) {
                            panel.setY(panel.getY() + 15);
                        }
                			if (scrollWheel < 0) {
                            	panel.setY(panel.getY() - 15);
                            }
                	}
        	} else {
        		 if (scrollWheel > 0) {
                     panel.setY(panel.getY() + 15);
                 }
                 if (scrollWheel < 0) {
                     panel.setY(panel.getY() - 15);
                 }
        	}
        }
    }

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
