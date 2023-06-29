package org.moonware.client.ui.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.ScreenHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.button.ImageButton;
import org.moonware.client.ui.clickgui.component.Component;
import org.moonware.client.ui.clickgui.component.ExpandableComponent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends Screen {

    public static boolean escapeKeyInUse;
    public static GuiSearcher search;
    public List<Panel> components = new ArrayList<>();
    public ScreenHelper screenHelper;

    public static double animation;
    public boolean exit;
    public Type type;
    protected ArrayList<ImageButton> imageButtons = new ArrayList<>();
    private org.moonware.client.ui.clickgui.component.Component selectedPanel;
    private static Namespaced ANIME_GIRL;

    public ClickGuiScreen() {
        int height = 20;
        int x = 20;
        int y = 80;
        for (Type type : Type.values()) {
            this.type = type;
            components.add(new Panel(type, x, y));
            selectedPanel = new Panel(type, x, y);
            y += height + 6;
        }
        screenHelper = new ScreenHelper(0, 0);
    }

    @Override
    public void init() {
        if (ClickGui.girlMode.currentMode.equals("Random")) {
            ANIME_GIRL = new Namespaced("moonware/girls/girl" + (int) MWUtils.randomFloat(1.0f, 21.0f) + ".png");
        }
        screenHelper = new ScreenHelper(0, 0);
        ScaledResolution sr = new ScaledResolution(minecraft);
        imageButtons.clear();
        //this.imageButtons.add(new ImageButton(new Namespaced("moonware/cfg.png"), (int) (((sr.getScaledWidth()) - 25) / ClickGui.scale.getNumberValue()), (int) ((sr.getScaledHeight() - mc.font.height - 14) / ClickGui.scale.getNumberValue()), (int) (14 / ClickGui.scale.getNumberValue()), (int) (14 / ClickGui.scale.getNumberValue()), "", 22));
        //this.imageButtons.add(new ImageButton(new ResourceLocation("moonware/hud.png"), (int) (((sr.getScaledWidth() - (mc.fontRendererObj.getStringWidth("Welcome"))) - 70) / ClickGui.scale.getNumberValue()), (int) ((sr.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 50) / ClickGui.scale.getNumberValue()), 40, 40, "", 18));
        search = new GuiSearcher(1337, Minecraft.font, (int) (width / ClickGui.scale.getNumberValue()) - MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth("Search Feature...") - 90, 4, 150, 18);

        if (ClickGui.blur.getBoolValue()) {
            //mc.entityRenderer.loadShader(new Namespaced("shaders/post/blur.json"));
        }
        if (ClickGui.background.getBoolValue()) {

        }
        /*
        Minecraft mc = Minecraft.getMinecraft();
        //ScaledResolution sr = new ScaledResolution(mc);
        GL11.glPushMatrix();
        GL11.glColor4d(animation, animation, animation, 255);
        String animeGirlStr = "";
        if (ClickGui.girlMode.currentMode.equals("Girl1")) {
            animeGirlStr = "girl1";
        } else if (ClickGui.girlMode.currentMode.equals("Girl2")) {
            animeGirlStr = "girl2";
        } else if (ClickGui.girlMode.currentMode.equals("Girl3")) {
            animeGirlStr = "girl3";
        } else if (ClickGui.girlMode.currentMode.equals("Girl4")) {
            animeGirlStr = "girl4";
        } else if (ClickGui.girlMode.currentMode.equals("Girl5")) {
            animeGirlStr = "girl5";
        } else if (ClickGui.girlMode.currentMode.equals("Girl6")) {
            animeGirlStr = "girl6";
        } else if (ClickGui.girlMode.currentMode.equals("Girl7")) {
            animeGirlStr = "girl7";
        }else if (ClickGui.girlMode.currentMode.equals("Girl8")) {
            animeGirlStr = "girl8";
        }else if (ClickGui.girlMode.currentMode.equals("Girl9")) {
            animeGirlStr = "girl9";
        }else if (ClickGui.girlMode.currentMode.equals("Girl10")) {
            animeGirlStr = "girl10";
        }else if (ClickGui.girlMode.currentMode.equals("Girl11")) {
            animeGirlStr = "girl11";
        }else if (ClickGui.girlMode.currentMode.equals("Girl12")) {
            animeGirlStr = "girl12";
        }else if (ClickGui.girlMode.currentMode.equals("Girl13")) {
            animeGirlStr = "girl13";
        }else if (ClickGui.girlMode.currentMode.equals("Girl14")) {
            animeGirlStr = "girl14";
        }else if (ClickGui.girlMode.currentMode.equals("Girl15")) {
            animeGirlStr = "girl15";
        }else if (ClickGui.girlMode.currentMode.equals("Girl16")) {
            animeGirlStr = "girl16";
        }else if (ClickGui.girlMode.currentMode.equals("Girl17")) {
            animeGirlStr = "girl17";
        }else if (ClickGui.girlMode.currentMode.equals("Girl18")) {
            animeGirlStr = "girl18";
        }else if (ClickGui.girlMode.currentMode.equals("Girl19")) {
            animeGirlStr = "girl19";
        }else if (ClickGui.girlMode.currentMode.equals("Girl20")) {
            animeGirlStr = "girl20";
        }
        if (!ClickGui.girlMode.currentMode.equals("Random")) {
            ANIME_GIRL = new ResourceLocation("moonware/girls/" + animeGirlStr + ".png");
        }
        mc.getTextureManager().bindTexture(ANIME_GIRL);
        RenderHelper2.drawImage(ANIME_GIRL, (float)((double)sr.getScaledWidth() - 350.0), sr.getScaledHeight() - 370, 400.0f, 400.0f, Color.WHITE);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();

         */
        super.init();
    }

    public static void callback() {
        if (ClickGui.girl.getCurrentValue()) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution sr = new ScaledResolution(mc);
            GL11.glPushMatrix();
            GL11.glColor4d(255, 255, 255, 255);
            String animeGirlStr = "";
            if (ClickGui.girlMode.currentMode.equals("Girl1")) {
                animeGirlStr = "girl1";
            } else if (ClickGui.girlMode.currentMode.equals("Girl2")) {
                animeGirlStr = "girl2";
            } else if (ClickGui.girlMode.currentMode.equals("Girl3")) {
                animeGirlStr = "girl3";
            } else if (ClickGui.girlMode.currentMode.equals("Girl4")) {
                animeGirlStr = "girl4";
            } else if (ClickGui.girlMode.currentMode.equals("Girl5")) {
                animeGirlStr = "girl5";
            } else if (ClickGui.girlMode.currentMode.equals("Girl6")) {
                animeGirlStr = "girl6";
            } else if (ClickGui.girlMode.currentMode.equals("Girl7")) {
                animeGirlStr = "girl7";
            } else if (ClickGui.girlMode.currentMode.equals("Girl9")) {
                animeGirlStr = "girl9";
            } else if (ClickGui.girlMode.currentMode.equals("Girl10")) {
                animeGirlStr = "girl10";
            } else if (ClickGui.girlMode.currentMode.equals("Girl11")) {
                animeGirlStr = "girl11";
            } else if (ClickGui.girlMode.currentMode.equals("Girl12")) {
                animeGirlStr = "girl12";
            } else if (ClickGui.girlMode.currentMode.equals("Girl13")) {
                animeGirlStr = "girl13";
            } else if (ClickGui.girlMode.currentMode.equals("Girl14")) {
                animeGirlStr = "girl14";
            } else if (ClickGui.girlMode.currentMode.equals("Girl15")) {
                animeGirlStr = "girl15";
            } else if (ClickGui.girlMode.currentMode.equals("Girl16")) {
                animeGirlStr = "girl16";
            } else if (ClickGui.girlMode.currentMode.equals("Girl17")) {
                animeGirlStr = "girl17";
            } else if (ClickGui.girlMode.currentMode.equals("Girl18")) {
                animeGirlStr = "girl18";
            } else if (ClickGui.girlMode.currentMode.equals("Girl19")) {
                animeGirlStr = "girl19";
            } else if (ClickGui.girlMode.currentMode.equals("Girl20")) {
                animeGirlStr = "girl20";
            }
            if (!ClickGui.girlMode.currentMode.equals("Random")) {
                ANIME_GIRL = new Namespaced("moonware/girls/" + animeGirlStr + ".png");
            }
            Minecraft.getTextureManager().bindTexture(ANIME_GIRL);
            RenderHelper2.drawImage(ANIME_GIRL, (float) ((double) sr.getScaledWidth() - 350.0), sr.getScaledHeight() - 370, 400.0f, 400.0f, Color.WHITE);
            GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        if (ClickGui.blur.get())
            RenderUtils2.drawBlur(5,() -> RoundedUtil.drawRound(- 20, -20, Minecraft.width + 40, Minecraft.height + 40,2,new Color(0x1421)));
        GlStateManager.scale(ClickGui.scale.getNumberValue(), ClickGui.scale.getNumberValue(), ClickGui.scale.getNumberValue());
        callback();
        mouseX = (int)((float)mouseX / ClickGui.scale.getNumberValue());
        mouseY = (int)((float)mouseY / ClickGui.scale.getNumberValue());

        ScaledResolution sr = new ScaledResolution(minecraft);
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
                color = PaletteHelper.astolfo(true, width);
                break;
            case "Color Two":
                color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twocolor.getRGB(), (float) Math.abs(((((System.currentTimeMillis() / speed) / speed) + height * 6L / 60 * 2) % 2) - 1)));
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1);
                break;
            case "Category":
                color = new Color(type.getColor());
                break;
            case "Static":
                color = onecolor;
                break;
        }
        Color color1 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 100);
        if (ClickGui.background.getBoolValue()) {
            //RectHelper.drawRectBetter( (int) (((sr.getScaledWidth()) - 27) / ClickGui.scale.getNumberValue()), (int) ((sr.getScaledHeight() - mc.font.height - 15) / ClickGui.scale.getNumberValue()), (int) (18 / ClickGui.scale.getNumberValue()), (int) (18 / ClickGui.scale.getNumberValue()),new Color(2,1,1,241).getRGB());
            drawGradientRect(0, 0, width / ClickGui.scale.getNumberValue(), height / ClickGui.scale.getNumberValue(), ClickGui.backgroundColor.getColorValue(), ClickGui.backgroundColor2.getColorValue());
        }

        if (exit) {

        } else {
            screenHelper.interpolate(width, height, 6 * Minecraft.frameTime / 6);
        }

        GlStateManager.pushMatrix();
        GL11.glTranslatef(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
        GL11.glScaled(screenHelper.getX() / sr.getScaledWidth(), screenHelper.getY() / sr.getScaledHeight(), 1);
        GL11.glTranslatef((float) (-sr.getScaledWidth()) / 2.0f, (float) (-sr.getScaledHeight()) / 2.0f, 0.0f);

        for (Panel panel : components) {
            panel.drawComponent(sr, mouseX, mouseY);
        }

        //search.drawTextBox();
        //RectHelper.drawGradientRect(width - mc.fontRenderer.getStringWidth("Search Feature...") - 90, 4, sr.getScaledWidth() - 10, 4, color.getRGB(), color.darker().getRGB());
        if (search.getText().isEmpty() && !search.isFocused()) {
            //mc.fontRenderer.drawStringWithShadow("Search Feature...", (width / ClickGui.scale.getNumberValue()) - mc.fontRenderer.getStringWidth("Search Feature...") - 50, 10, -1);
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
        }
        super.draw(mouseX, mouseY, partialTick);
    }

    public void updateMouseWheel(int mouseX) {
        mouseX = (int)((float)mouseX / ClickGui.scale.getNumberValue());
        int scrollWheel = Mouse.getDWheel();
        for (org.moonware.client.ui.clickgui.component.Component panel : components) {
            float x = (float)panel.getX();
            // Вправа - 205
            // Влева - 203
            // вверх - 200
            // вниз - 208
            if (Keyboard.isKeyDown(205)) {
                panel.setX(panel.getX() + 4);
            }
            if (Keyboard.isKeyDown(203)) {
                panel.setX(panel.getX() - 4);
            }
            if (Keyboard.isKeyDown(200)) {
                panel.setY(panel.getY() - 4);
            }
            if (Keyboard.isKeyDown(208)) {
                panel.setY(panel.getY() + 4);
            }
            if (ClickGui.scrollMode.currentMode.equals("One Panel") && (!((float)mouseX > x) || !((float)mouseX < x + panel.getWidth()))) continue;
            if (ClickGui.scrollInversion.getBoolValue()) {
                if (scrollWheel < 0) {
                    panel.setY((int) (panel.getY() - (double)ClickGui.scrollSpeed.getNumberValue()));
                    continue;
                }
                if (scrollWheel <= 0) continue;
                panel.setY((int) (panel.getY() + (double)ClickGui.scrollSpeed.getNumberValue()));
                continue;
            }
            if (scrollWheel > 0) {
                panel.setY((int) (panel.getY() - (double)ClickGui.scrollSpeed.getNumberValue()));
                continue;
            }
            if (scrollWheel >= 0) continue;
            panel.setY((int) (panel.getY() + (double)ClickGui.scrollSpeed.getNumberValue()));
        }

//        System.out.println(Keyboard.getEventKey());
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
        search.textboxKeyTyped(c, key);

        if ((c == '\t' || c == '\r') && search.isFocused()) {
            search.setFocused(!search.isFocused());
        }

        escapeKeyInUse = false;
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {
        mouseX = (int)((float)mouseX / ClickGui.scale.getNumberValue());
        mouseY = (int)((float)mouseY / ClickGui.scale.getNumberValue());
        search.setFocused(false);
        search.setText("");
        search.mouseClicked(mouseX, mouseY, button);

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
                component.onMouseClick(mouseX, mouseY, button);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        mouseY = (int)((float)mouseY / ClickGui.scale.getNumberValue());
        selectedPanel.onMouseRelease(button);
    }

    @Override
    public void onClosed() {
        screenHelper = new ScreenHelper(0, 0);
        Minecraft.gameRenderer.theShaderGroup = null;
        super.onClosed();
    }
}
