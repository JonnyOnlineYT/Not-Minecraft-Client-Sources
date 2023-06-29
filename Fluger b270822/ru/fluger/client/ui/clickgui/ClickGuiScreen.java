/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package ru.fluger.client.ui.clickgui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.Fluger;
import ru.fluger.client.GifEngine;
import ru.fluger.client.TextureEngine;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.feature.impl.hud.ClickGui;
import ru.fluger.client.helpers.render.AnimationHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.helpers.render.animations.EasingHelper;
import ru.fluger.client.ui.button.ImageButton;
import ru.fluger.client.ui.clickgui.GuiSearcher;
import ru.fluger.client.ui.clickgui.Palette;
import ru.fluger.client.ui.clickgui.component.Component;
import ru.fluger.client.ui.clickgui.component.impl.ExpandableComponent;
import ru.fluger.client.ui.clickgui.component.impl.component.property.impl.SliderPropertyComponent;
import ru.fluger.client.ui.clickgui.component.impl.panel.impl.CategoryPanel;

public class ClickGuiScreen
extends GuiScreen {
    private static ResourceLocation ANIME_GIRL;
    public static double phase;
    public static double animation;
    public static boolean escapeKeyInUse;
    public static GuiSearcher search;
    private static ClickGuiScreen instance;
    private CategoryPanel front = null;
    private final List<CategoryPanel> components = new ArrayList<CategoryPanel>();
    private final Palette palette;
    protected ArrayList<ImageButton> imageButtons = new ArrayList();
    private Component selectedPanel;
    public static GuiScreen oldScreen;
    private float progress = 0.0f;
    private long lastMS = 0L;
    private int animeIndex = 1;
    private static TextureEngine girl1;
    private static TextureEngine girl2;
    private static TextureEngine girl3;
    private static TextureEngine girl4;
    public static GifEngine banana;
    public static float girl_animation;

    public ClickGuiScreen() {
        banana = new GifEngine(new ResourceLocation("nightmare/girls/animated.gif"), 400, 400);
        instance = this;
        this.palette = Palette.DEFAULT;
        Type[] categories = Type.values();
        for (int i = categories.length - 1; i >= 0; --i) {
            this.components.add(new CategoryPanel(categories[i], 18 + 128 * i, 10.0f));
            this.selectedPanel = new CategoryPanel(categories[i], 18 + 128 * i, 10.0f);
        }
        oldScreen = this;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public static void callback() {
        Minecraft mc = Minecraft.getMinecraft();
        if (animation > 0.0) {
            animation = AnimationHelper.animation(animation, 0.0, 5.0);
        }
        ScaledResolution sr = new ScaledResolution(mc);
        if (!ClickGui.girl.getCurrentValue()) {
            return;
        }
        if (ClickGui.girlMode.getCurrentMode().equalsIgnoreCase("Animated")) {
            banana.update();
            banana.bind(Fluger.scale.calc(sr.getScaledWidth()) - 200 + (int)animation, Fluger.scale.calc(sr.getScaledHeight()) - 200);
            return;
        }
        GL11.glPushMatrix();
        GL11.glColor4d((double)animation, (double)animation, (double)animation, (double)animation);
        if (ClickGui.girlMode.index == 0) {
            girl1.bind(Fluger.scale.calc(sr.getScaledWidth()) - girl1.getWidth() / 2 + (int)animation, Fluger.scale.calc(sr.getScaledHeight()) - girl1.getHeight() / 2);
        }
        if (ClickGui.girlMode.index == 1) {
            girl2.bind(Fluger.scale.calc(sr.getScaledWidth()) - girl2.getWidth() / 2 + (int)animation, Fluger.scale.calc(sr.getScaledHeight()) - girl2.getHeight() / 2);
        }
        if (ClickGui.girlMode.index == 2) {
            girl3.bind(Fluger.scale.calc(sr.getScaledWidth()) - girl3.getWidth() / 3 - 20 + (int)animation, Fluger.scale.calc(sr.getScaledHeight()) - girl3.getHeight() / 2);
        }
        if (ClickGui.girlMode.index == 3) {
            girl4.bind(Fluger.scale.calc(sr.getScaledWidth()) - girl4.getWidth() / 3 - 40 + (int)animation, Fluger.scale.calc(sr.getScaledHeight()) - girl4.getHeight() / 2);
        }
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    public static ClickGuiScreen getInstance() {
        return instance;
    }

    @Override
    public void initGui() {
        this.lastMS = System.currentTimeMillis();
        this.progress = 0.0f;
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.imageButtons.clear();
        search = new GuiSearcher(1337, this.mc.fontRendererObj, sr.getScaledWidth() / 2 + 320, 10, 150, 18);
        super.initGui();
    }

    public static double createAnimation(double phase) {
        return 1.0 - Math.pow(1.0 - phase, 3.0);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.drawDefaultBackground();
        if (this.front != null) {
            this.components.remove(this.front);
            this.components.add(this.components.size(), this.front);
        }
        for (CategoryPanel p : this.components) {
            if (p == this.front || !p.dragging) continue;
            p.dragging = false;
        }
        Fluger.scale.pushScale();
        mouseX = Fluger.scale.calc(mouseX);
        mouseY = Fluger.scale.calc(mouseY);
        if (ClickGui.backGroundBlur.getCurrentValue()) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderHelper.renderBlur(0, 0, Fluger.scale.calc(sr.getScaledWidth()), Fluger.scale.calc(sr.getScaledHeight()), (int)ClickGui.backGroundBlurStrength.getCurrentValue());
        }
        ClickGuiScreen.callback();
        this.progress = this.progress >= 1.0f ? 1.0f : (float)(System.currentTimeMillis() - this.lastMS) / 850.0f;
        double trueAnim = EasingHelper.easeOutQuart(this.progress);
        for (Component component : this.components) {
            if (component == null) continue;
            component.drawComponent(sr, mouseX, mouseY);
            this.updateMouseWheel();
        }
        for (ImageButton imageButton : this.imageButtons) {
            imageButton.draw(mouseX, mouseY, Color.WHITE);
            if (!Mouse.isButtonDown((int)0)) continue;
            imageButton.onClick(mouseX, mouseY);
        }
        Fluger.scale.popScale();
    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (Component component : this.components) {
            float x = (float)component.getX();
            if (scrollWheel < 0) {
                component.setY((float)(component.getY() - 13.0));
                continue;
            }
            if (scrollWheel <= 0) continue;
            component.setY((float)(component.getY() + 13.0));
        }
    }

    public Palette getPalette() {
        return this.palette;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!escapeKeyInUse) {
            super.keyTyped(typedChar, keyCode);
        }
        escapeKeyInUse = false;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        mouseX = Fluger.scale.calc(mouseX);
        mouseY = Fluger.scale.calc(mouseY);
        search.setFocused(false);
        search.setText("");
        search.mouseClicked(mouseX, mouseY, mouseButton);
        for (CategoryPanel component : this.components) {
            CategoryPanel expandableComponent;
            float x = (float)component.getX();
            float y = (float)component.getY();
            float cHeight = component.getHeight();
            if (component instanceof ExpandableComponent && (expandableComponent = component).isExpanded()) {
                cHeight = ((ExpandableComponent)expandableComponent).getHeightWithExpand();
            }
            if (!((float)mouseX > x && (float)mouseY > y && (float)mouseX < x + component.getWidth() && (float)mouseY < y + cHeight)) continue;
            this.front = component;
            this.selectedPanel = component;
            break;
        }
        if (this.front != null) {
            this.front.onMouseClick(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.components.stream().filter(p -> p != this.front).forEach(p -> p.onMouseRelease(state));
        if (this.front != null) {
            this.front.onMouseRelease(state);
        }
    }

    @Override
    public void onGuiClosed() {
        animation = 200.0;
        this.mc.entityRenderer.theShaderGroup = null;
        this.mc.currentScreen = null;
        SliderPropertyComponent.sliding2 = false;
        super.onGuiClosed();
    }

    static {
        girl1 = new TextureEngine("nightmare/girls/1.png", Fluger.scale, 452, 600);
        girl2 = new TextureEngine("nightmare/girls/2.png", Fluger.scale, 590, 687);
        girl3 = new TextureEngine("nightmare/girls/3.png", Fluger.scale, 1024, 576);
        girl4 = new TextureEngine("nightmare/girls/4.png", Fluger.scale, 567, 800);
        girl_animation = 200.0f;
    }
}

