package org.moonware.client.ui.guiWithScroll;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.Utils.blur.GaussianBlur;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.render.rect.DrawHelper;
import org.moonware.client.ui.button.ImageButton;
import org.moonware.client.ui.guiWithScroll.component.ExpandableComponent;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {
    public static boolean escapeKeyInUse;
    public List<Panel> components = new ArrayList<>();
    public List<ConfigPanel> componentsC = new ArrayList<>();
    public ScreenHelper screenHelper;
    public boolean exit;
    public Type type;
    private Panel selectedPanel;
    private ConfigPanel selectedPanelC;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    public static float progress;
    public static float progress2;
    private long lastMS;
    private int wheel;
    private int scroll;

    public ClickGuiScreen() {
		int height = 20;
		progress = 0.0f;
		progress2 = 0.0f;
        int x = 5;
        int xx = 5;
        int y = 20;
        for (Type type : Type.values()) {
            this.type = type;
            components.add(new Panel(type, x, y));
            selectedPanel = new Panel(type, x, y);
            x += height + 95;
        }
        componentsC.add(new ConfigPanel(Type.Combat,x,y));
        screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void init() {
    	lastMS = System.currentTimeMillis();
        ScaledResolution sr = new ScaledResolution(minecraft);
        screenHelper = new ScreenHelper(0, 0);
        imageButtons.clear();
        progress = 0.0f;
        progress2 = 0.0f;
        super.init();
    }
    public static void main(String[] args) throws Exception {
        String imageUrl = "http://www.avajava.com/images/avajavalogo.jpg";
        String destinationFile = "image.jpg";

        saveImage(imageUrl, destinationFile);
    }
    
    public static double createAnimation(double phase) {
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
    public void draw(int mouseX, int mouseY, float partialTick) {
        if (ClickGui.blur.get()) {
            GaussianBlur.renderBlur(12);
        }
        ScaledResolution sr = new ScaledResolution(minecraft);
    //    DrawHelper.drawSmoothRect(2, 2, 30, 30, new Color(1, 1, 1, 148).getRGB());
        
        if (true) {
        	 if (progress >= 1.0f) {
                 progress = 1.0f;
             }
             else {
                 progress = ((System.currentTimeMillis() - lastMS) / 325.0f) * 2;
             }
             if (progress2 >= 1f) {
                 progress2 = 1f;
             }
             else {
                 progress2 = ((System.currentTimeMillis() - lastMS) / 650.0f) * 2;
             }
        } else {
        	progress = 1.0f;
        	 progress2 = 1f;
        }
        
        Color color = Color.WHITE;
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        switch (ClickGui.clickGuiColor.currentMode) {
            case "Client":
                color = ClientHelper.getClientColor();
                break;
            case "Fade":
                color = new Color(ClickGui.color.getColorValue());
                break;
            case "Astolfo":
                color = DrawHelper.astolfoColor(20, width);
                break;
            case "Color Two":
                color = new Color(DrawHelper.fadeColorRich(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + height * 6L / 60 * 2) % 2) - 1)));
                break;
            case "Rainbow":
                color = DrawHelper.rainbow2(300, 1, 1);
                break;
            case "Category":
                color = new Color(type.getColor());
                break;
            case "Static":
                color = onecolor;
                break;
        }
        Color none = new Color(0, 0, 0, 0);
        String mode = "Everywhere";
        GlStateManager.pushMatrix();
        if (ClickGui.background.getBoolValue()) {
            if (mode.equalsIgnoreCase("Top")) {
                drawDefaultBackground();
                drawGradientRect(0, 0, sr.getScaledWidth(), (int) (sr.getScaledHeight() * (1 + 1 - progress2)), color.getRGB(), none.getRGB());
            } else if (mode.equalsIgnoreCase("Bottom")) {
                drawDefaultBackground();
                drawGradientRect(0, 0, sr.getScaledWidth(), (int) (sr.getScaledHeight() * (1 + progress2)), none.getRGB(), color.getRGB());
            } else if (mode.equalsIgnoreCase("Everywhere")) {
                drawDefaultBackground();
                drawGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), DrawHelper.setAlpha(color, (int) (100 * (progress2))).getRGB(), DrawHelper.setAlpha(color, (int) (100 * (progress2))).getRGB());
            } else if (mode.equalsIgnoreCase("Shader")) {
            	GlStateManager.disableCull();
                long initTime = System.currentTimeMillis();
                //this.backgroundShader.useShader(sr.getScaledWidth() + 80000, sr.getScaledHeight(), mouseX, mouseY, (float) (System.currentTimeMillis() - initTime) / 5000.0f);
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
        	
        	
        	//RectHelper.drawRectWithGlow(sr.getScaledWidth() - mc.motBold20.getStringWidth("Username: " + CFontUser.username) - 5 - 5,sr.getScaledHeight() - 25 - 5, sr.getScaledWidth() - 5,sr.getScaledHeight() - 5, 8, 10, new Color(0,0,0,255));
        	//mc.mntsb_20.drawStringWithShadow("Username: " + CFontUser.username,sr.getScaledWidth() - mc.mntsb_20.getStringWidth("Username: " + CFontUser.username) - 3 - 5,sr.getScaledHeight() - 20 - 5, -1);
        	
        	///String date2 = (new SimpleDateFormat("YYYY-MM-DD")).format(new Timestamp(Long.parseLong(Provider.getKeyAuth().getApi().getUserData().getExpiry())).getTime());
        	
        	
        	//String role = Provider.getKeyAuth().getApi().getUserData().getSubscription();
        	
        	
        	//mc.mntsb_20.drawStringWithShadow("Role: " + role,sr.getScaledWidth() - mc.mntsb_20.getStringWidth("Username: " + CFontUser.username) - 3 - 5,sr.getScaledHeight() - 10 - 5, -1);
        
        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, mouseY);
        }
        for (ConfigPanel panel : componentsC) {
            //panel.drawComponent(sr, mouseX, mouseY);
        }


        for (ImageButton imageButton : imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (Mouse.isButtonDown(0)) {
                imageButton.onClick(mouseX, mouseY);
            }
        }
        GlStateManager.popMatrix();


        updateMouseWheel(mouseX);

        if (exit) {
            screenHelper.interpolate(0, 0, 2);
            if (screenHelper.getY() < 200) {
                exit = false;
                Minecraft.openScreen(null);
                if (Minecraft.screen == null) {
                    minecraft.setIngameFocus();
                }
            }
        } else {
            screenHelper.interpolate(width, height, 3 * Minecraft.frameTime / 6);
        }
        super.draw(mouseX, mouseY, partialTick);
    }

    public void updateMouseWheel(int mouseX) {
        int scrollWheel = Mouse.getDWheel();
        for (Panel panel : components) {
        	if (true) {
        		if (Panel.inPanel) {
                    if (mouseX < panel.getX() + 100 && mouseX > panel.getX()) {
                        if (scrollWheel > 0 && panel.getY() < 40) {
                            panel.setY(panel.getY() + ClickGui.scrollSpeed.getCurrentIntValue());
                        }
                        if (panel.getY() > 33) {
                            panel.setY(33);
                        }
                        if (scrollWheel < 0) {
                            panel.setY(panel.getY() - ClickGui.scrollSpeed.getCurrentIntValue());
                        }
                    }
        		} else {
        			for (Panel panel2 : components) {
        				if (scrollWheel > 0) {
                            panel2.setY2(panel2.getY2() + 15);
                        }
                        if (scrollWheel < 0) {
                            panel2.setY2(panel2.getY2() - 15);
                        }
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
        for (ConfigPanel panel : componentsC) {
            if (true) {
                if (Panel.inPanel) {
                    if (mouseX < panel.getX() + 100 && mouseX > panel.getX()) {
                        if (scrollWheel > 0 && panel.getY() < 40) {
                            panel.setY(panel.getY() + ClickGui.scrollSpeed.getCurrentIntValue());
                        }
                        if (panel.getY() > 40) {
                            panel.setY(40);
                        }
                        if (scrollWheel < 0) {
                            panel.setY(panel.getY() - ClickGui.scrollSpeed.getCurrentIntValue());
                        }
                    }
                } else {
                    for (Panel panel2 : components) {
                        if (scrollWheel > 0) {
                            panel2.setY2(panel2.getY2() + 15);
                        }
                        if (scrollWheel < 0) {
                            panel2.setY2(panel2.getY2() - 15);
                        }
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
    public void keyPressed(int key, char c) {
        if (key == 1)
            exit = true;

        if (exit)
            return;

        selectedPanel.onKeyPress(key);

        if (!escapeKeyInUse) {
            super.keyPressed(key, c);
        }

        escapeKeyInUse = false;
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {

        for (Panel component : components) {
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                selectedPanel = component;
                component.onMouseClick(mouseX, mouseY, button);
                break;
            }
        }
        for (ConfigPanel component : componentsC) {
            int x = component.getX();
            int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                ExpandableComponent expandableComponent = component;
                if (expandableComponent.isExpanded())
                    cHeight = expandableComponent.getHeightWithExpand();
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                selectedPanelC = component;
                component.onMouseClick(mouseX, mouseY, button);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        selectedPanel.onMouseRelease(button);
    }

    @Override
    public void onClosed() {
        screenHelper = new ScreenHelper(0, 0);
        Minecraft.gameRenderer.theShaderGroup = null;
        super.onClosed();
    }
}
