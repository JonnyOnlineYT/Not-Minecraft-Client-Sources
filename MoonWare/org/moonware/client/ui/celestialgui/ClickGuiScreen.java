/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Namespaced;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.MWFont;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.render.animations.EasingHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.button.ImageButton;
import org.moonware.client.ui.celestialgui.component.Component;
import org.moonware.client.ui.celestialgui.component.impl.ExpandableComponent;
import org.moonware.client.ui.celestialgui.component.impl.component.property.impl.SliderPropertyComponent;
import org.moonware.client.ui.celestialgui.component.impl.panel.impl.CategoryPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen
extends Screen {
    private static Namespaced ANIME_GIRL;
    public static double phase;
    public static double animation;
    public static boolean escapeKeyInUse;
    public static GuiSearcher search;
    private static ClickGuiScreen instance;
    private final List<org.moonware.client.ui.celestialgui.component.Component> components = new ArrayList<org.moonware.client.ui.celestialgui.component.Component>();
    private final Palette palette;
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private org.moonware.client.ui.celestialgui.component.Component selectedPanel;
    public static Screen oldScreen;
    private float progress;
    private long lastMS;

    public ClickGuiScreen() {
        instance = this;
        palette = Palette.DEFAULT;
        Type[] categories = Type.values();
        for (int i = categories.length - 1; i >= 0; --i) {
            components.add(new CategoryPanel(categories[i], 18 + 128 * i, 10.0f));
            selectedPanel = new CategoryPanel(categories[i], 18 + 128 * i, 10.0f);
        }
        oldScreen = this;
    }

    public static ClickGuiScreen getInstance() {
        return instance;
    }

    @Override
    public void init() {
        if (ClickGui.girlMode.currentMode.equals("Random")) {
            ANIME_GIRL = new Namespaced("moonware/girls/girl" + (int) MWUtils.randomFloat(1.0f, 7.0f) + ".png");
        }
        lastMS = System.currentTimeMillis();
        progress = 0.0f;
        ScaledResolution sr = new ScaledResolution(minecraft);
        imageButtons.clear();
        imageButtons.add(new ImageButton(new Namespaced("moonware/brush.png"), (int) ((sr.getScaledWidth() - FontStorage.robotoRegularFontRender.getWidth("Wellcome") - 19)/ ClickGui.scale.getCurrentValue()), (int) ((sr.getScaledHeight() - FontStorage.robotoRegularFontRender.getHeight() - 55)/ ClickGui.scale.getCurrentValue()), (int) (50/ ClickGui.scale.getCurrentValue()), (int) (50/ ClickGui.scale.getCurrentValue()), "", 18));
        imageButtons.add(new ImageButton(new Namespaced("moonware/config.png"), (int) ((sr.getScaledWidth() - FontStorage.robotoRegularFontRender.getWidth("Wellcome") - 89)/ ClickGui.scale.getCurrentValue()), (int) ((sr.getScaledHeight() - FontStorage.robotoRegularFontRender.getHeight() - 55) / ClickGui.scale.getCurrentValue()), (int) (50/ ClickGui.scale.getCurrentValue()), (int) (50/ ClickGui.scale.getCurrentValue()), "", 22));
        search = new GuiSearcher(1337, Minecraft.font, (int) (width / ClickGui.scale.getNumberValue()) - MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth("Search Feature...") - 90, 4, 150, 18);
        super.init();
    }

    public static void callback() {
        if (animation == 0.0 || !ClickGui.girl.getCurrentValue()) {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        GL11.glPushMatrix();
        GL11.glColor4d(animation, animation, animation,255);
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
        }
        if (!ClickGui.girlMode.currentMode.equals("Random")) {
            ANIME_GIRL = new Namespaced("moonware/girls/" + animeGirlStr + ".png");
        }
        Minecraft.getTextureManager().bindTexture(ANIME_GIRL);
        RenderHelper2.drawImage(ANIME_GIRL, (float)((double)sr.getScaledWidth() - 350.0 * animation), sr.getScaledHeight() - 370, 400.0f, 400.0f, Color.WHITE);
        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public static double createAnimation(double phase) {
        return 1.0 - Math.pow(1.0 - phase, 3.0);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        ScaledResolution sr = new ScaledResolution(minecraft);
        drawDefaultBackground();
        if (ClickGui.background.getCurrentValue()) {
            int cl = ClickGui.backgroundColor.getColor();
            drawGradientRect(0.0f, 0.0f, width, height, cl, cl);
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(ClickGui.scale.getCurrentValue(), ClickGui.scale.getCurrentValue(), ClickGui.scale.getCurrentValue());
        mouseX = (int) ((float) mouseX / ClickGui.scale.getCurrentValue());
        mouseY = (int) ((float) mouseY / ClickGui.scale.getCurrentValue());
        if (ClickGui.backGroundBlur.getCurrentValue()) {
            if (Minecraft.gameSettings.ofFastRender) {
                Minecraft.gameSettings.ofFastRender = false;
            }
            RenderHelper2.renderBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (int) ClickGui.backGroundBlurStrength.getCurrentValue());
        }
        progress = progress >= 1.0f ? 1.0f : (float) (System.currentTimeMillis() - lastMS) / 850.0f;
        double trueAnim = EasingHelper.easeOutQuart(progress);
        for (org.moonware.client.ui.celestialgui.component.Component component : components) {
            if (component == null) continue;
            component.drawComponent(sr, mouseX, mouseY);
            updateMouseWheel(mouseX);
        }
        float x = (float) sr.getScaledWidth() / 2.0f + 320.0f;
        float y = 12.0f;
        int left = (int) x;
        int bottom = (int) (y + 12.0f);
        Color onecolor = new Color(ClickGui.color.getColor());
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color2 = c.getRGB();

        for (ImageButton imageButton : imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (!Mouse.isButtonDown(0)) continue;
            imageButton.onClick(mouseX, mouseY);
        }
        if (ClickGui.glow.getCurrentValue()) {
            //RenderHelper2.renderBlurredShadow(new Color(color2), width - mc.fontRenderer.getStringWidth("Search Feature...") - 90, 4.0, 160.0, 4.0, 22);
        }
        search.drawTextBox();
        RectHelper.drawGradientRect(width - MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth("Search Feature...") - 90, 4, sr.getScaledWidth() - 10, 4, onecolor.getRGB(), onecolor.darker().getRGB());
        if (search.getText().isEmpty() && !search.isFocused()) {
            MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow("Search Feature...", (width / ClickGui.scale.getNumberValue()) - MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth("Search Feature...") - 50, 10, -1);
        }
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    public void updateMouseWheel(int mouseX) {
        mouseX = (int)((float)mouseX / ClickGui.scale.getCurrentValue());
        int scrollWheel = Mouse.getDWheel();
        for (org.moonware.client.ui.celestialgui.component.Component panel : components) {
            float x = (float)panel.getX();
            if (ClickGui.scrollMode.currentMode.equals("One Panel") && (!((float)mouseX > x) || !((float)mouseX < x + panel.getWidth()))) continue;
            if (ClickGui.scrollInversion.getCurrentValue()) {
                if (scrollWheel < 0) {
                    panel.setY((float)(panel.getY() - (double)ClickGui.scrollSpeed.getCurrentValue()));
                    continue;
                }
                if (scrollWheel <= 0) continue;
                panel.setY((float)(panel.getY() + (double)ClickGui.scrollSpeed.getCurrentValue()));
                continue;
            }
            if (scrollWheel > 0) {
                panel.setY((float)(panel.getY() - (double)ClickGui.scrollSpeed.getCurrentValue()));
                continue;
            }
            if (scrollWheel >= 0) continue;
            panel.setY((float)(panel.getY() + (double)ClickGui.scrollSpeed.getCurrentValue()));
        }
    }

    public Palette getPalette() {
        return palette;
    }

    @Override
    public void keyPressed(int key, char c) {
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
        mouseX = (int)((float)mouseX / ClickGui.scale.getCurrentValue());
        mouseY = (int)((float)mouseY / ClickGui.scale.getCurrentValue());
        search.setFocused(false);
        search.setText("");
        search.mouseClicked(mouseX, mouseY, button);
        for (Component component : components) {
            ExpandableComponent expandableComponent;
            float x = (float)component.getX();
            float y = (float)component.getY();
            float cHeight = component.getHeight();
            if (component instanceof ExpandableComponent && (expandableComponent = (ExpandableComponent)component).isExpanded()) {
                cHeight = expandableComponent.getHeightWithExpand();
            }
            if (!((float)mouseX > x) || !((float)mouseY > y) || !((float)mouseX < x + component.getWidth()) || !((float)mouseY < y + cHeight)) continue;
            selectedPanel = component;
            component.onMouseClick(mouseX, mouseY, button);
            break;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        mouseY = (int)((float)mouseY / ClickGui.scale.getCurrentValue());
        selectedPanel.onMouseRelease(button);
    }

    @Override
    public void onClosed() {
        Minecraft.gameRenderer.theShaderGroup = null;
        Minecraft.screen = null;
        SliderPropertyComponent.sliding2 = false;
        super.onClosed();
    }
}

