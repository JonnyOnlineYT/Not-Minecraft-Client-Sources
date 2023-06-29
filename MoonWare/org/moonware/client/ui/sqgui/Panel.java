package org.moonware.client.ui.sqgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.hud.ClickGui;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.GradientUtil;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.blur.GaussianBlur;
import org.moonware.client.helpers.misc.TimerHelper;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.helpers.render.GlowUtil;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.ui.sqgui.component.AnimationState;
import org.moonware.client.ui.sqgui.component.Component;
import org.moonware.client.ui.sqgui.component.DraggablePanel;
import org.moonware.client.ui.sqgui.component.ExpandableComponent;
import org.moonware.client.ui.sqgui.component.impl.ModuleComponent;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

public final class Panel extends DraggablePanel {
    Minecraft mc = Minecraft.getMinecraft();
    public static final int PANEL_WIDTH = 110;
    public static final int X_ITEM_OFFSET = 1;
    public double scissorBoxHeight;
    public static final int ITEM_HEIGHT = 17;
    public static final int HEADER_HEIGHT = 25;
    private final List<Feature> features;
    public Type type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;
    static boolean inPanel;
    int y2;

    public Panel(Type category, int x, int y) {
        super(null, category.getName(), x, y, PANEL_WIDTH, HEADER_HEIGHT);

        int moduleY = HEADER_HEIGHT;

        state = AnimationState.STATIC;
        features = MoonWare.featureManager.getFeaturesForCategory(category);
        for (Feature module : features) {
            components.add(new ModuleComponent(this, module, X_ITEM_OFFSET, moduleY, PANEL_WIDTH - (X_ITEM_OFFSET * 2), ITEM_HEIGHT));
            moduleY += ITEM_HEIGHT;
        }
        type = category;
    }
    
    public void setY2(int y2) {
    	this.y2 = y2;
    }
    
    public int getY2() {
    	return y2;
    }

    public static void applyBloom(float x,float y,float width,float height, int radius, Color color1,Color color2, Color color3, Color color4) {
        GradientUtil.applyGradient(x, y, width, height,1f,color1,color2,color3,color4,
                () -> GlowUtil.drawBlurredShadow(x, y, width, height,radius,new Color(255,255,255),0));
    }
    public static void applyBloomWithMask(float x,float y,float width,float height, int radius,float maskRadius, Color color1,Color color2, Color color3, Color color4) {
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(x,y,width,height,maskRadius,new Color(-1));
        StencilUtil.readStencilBuffer(2);
        GradientUtil.applyGradient(x, y, width, height,255 / 255f,color1,color2,color3,color4,
                () -> GlowUtil.drawBlurredShadow(x, y, width, height,radius,new Color(255,255,255),0));
        StencilUtil.uninitStencilBuffer();
    }

    @Override
    public void drawComponent(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        components.sort(new SorterHelper());
        components2.sort(new SorterHelper());
    	TimerHelper timerHelper = new TimerHelper();
    	int color = 0;
    	int x = getX();
        int y = getY();
        setExpanded(true);
        Color onecolor = new Color(ClickGui.color.getColorValue());
        Color twocolor = new Color(ClickGui.colorTwo.getColorValue());
        double speed = ClickGui.speed.getNumberValue();
        if (!true) {
        	if (dragging) {
                setX(mouseX - prevX);
                setY(mouseY - prevY);
            }
        }
        if (Keyboard.isKeyDown(205)) {
            setX(getX() + 4);
        }
        if (Keyboard.isKeyDown(203)) {
            setX(getX() - 4);
        }
        int width = getWidth();
        int height = getHeight();
        int headerHeight;
        int heightWithExpand = getHeightWithExpand();
        headerHeight = (isExpanded() ? heightWithExpand : height);

        float extendedHeight = 2;
        int yTotal = 0;

        boolean animated = false;
        int m = 5;
        int z = 10;
        if (ClickGui.blur.get()) {
            if (ClickGui.colored.get()) {

                //RenderHelper2.drawRainbowRoundGui(x, y2 + 30 + 1 - 3, width, MathHelper.clamp(heightWithExpand, 0, 2 - 3 + 18 + (y2 + 260 + 4) - 50), 3, true, false, true, true, 2, 4);
                drawUltraGradientPanelBG(x, y2 + 30 + 1 - 2.5f, width, MathHelper.clamp(heightWithExpand, 0, 2 - 3 + 18 + (y2 + 260 + 4) - 50), 3);

            } else{
                StencilUtil.initStencilToWrite();
                RoundedUtil.drawRound(x, y2 + 30 + 1 - 3, width, MathHelper.clamp(heightWithExpand, 0, 2 - 3 + 18 + (y2 + 260 + 4) - 50), 3,new Color(31,31,31,140));
                StencilUtil.readStencilBuffer(1);
                GaussianBlur.renderBlur(5);
                StencilUtil.uninitStencilBuffer();
                RoundedUtil.drawRound(x, y2 + 30 + 1 - 3, width, MathHelper.clamp(heightWithExpand, 0, 2 - 3 + 18 + (y2 + 260 + 4) - 50), 3,new Color(31,31,31,140));
            }
        }
        if (ClickGui.colored.get()) {

            //RenderHelper2.drawRainbowRoundGui(x, y2 + 30 + 1 - 3, width, MathHelper.clamp(heightWithExpand, 0, 2 - 3 + 18 + (y2 + 260 + 4) - 50), 3, true, false, true, true, 2, 4);
            drawUltraGradientPanelBG(x, y2 + 30 + 1 - 2.5f, width, MathHelper.clamp(heightWithExpand, 0, 2 - 3 + 18 + (y2 + 260 + 4) - 50), 3);
        }else{
            StencilUtil.initStencilToWrite();
            RoundedUtil.drawRound(x, y2 + 30 + 1 - 3, width, MathHelper.clamp(heightWithExpand, 0, 2 - 3 + 18 + (y2 + 260 + 4) - 50), 3,new Color(31,31,31,140));
            StencilUtil.readStencilBuffer(1);
            GaussianBlur.renderBlur(5);
            StencilUtil.uninitStencilBuffer();
            RoundedUtil.drawRound(x, y2 + 30 + 1 - 3, width, MathHelper.clamp(heightWithExpand, 0, 2 - 3 + 18 + (y2 + 260 + 4) - 50), 3,new Color(31,31,31,140));
        }
        RectHelper.drawRect(x, y2 + 48 + 1 - 4, x + width, y2 + 48 + 1 - 3, new Color(255, 255, 255, 92).getRGB());

        StencilUtil.initStencilToWrite();
        RectHelper.drawRect((x - 1 + 1 - 1) - 1, y2 + 80 + 1 - 4, (x - 1 + 1 - 1) +(width + 1 - 5 + 3) + (1 + 1) + 4, y2 + 80 + 1 - 3 +1 - 3 + 18 + (y2 + 260 + 4) - 47, new Color(28, 28, 28, 23).getRGB());
        StencilUtil.readStencilBuffer(2);
        //RectHelper.drawRectWithGlow((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (x - 1 + 1 - 1) +(width + 1 - 5 + 3) + (1 + 1) + 2, y2 + 30 + 1 - 3 +1 - 3 + 18 + (y2 + 260 + 4) - 48, type == Type.Combat ?  9 : 5, type == Type.Combat ? 4.5f : 8,new Color(28, 28, 28, 98));
        //RenderUtils2.drawBlurredShadow((x - 1 + 1 - 2), y2 + 30 + 1 - 4, (width + 1 - 5 + 3) + (1 + 1) + 5, 1 - 3 + 18 + (y2 + 260 + 4) - 46, 7, new Color(28, 28, 28, 255));
        StencilUtil.uninitStencilBuffer();
        GlStateManager.pushMatrix();
        //RenderUtils2.drawBlur(4, () -> RectHelper.drawRect((x - 1 + 1 - 1) - 1, y2 + 30 + 1 - 3 + 18.5f, (x - 1 + 1 - 1) +(width + 1 - 5 + 3) + (1 + 1) + 4, y2 + 30 + 1 - 3 +1 - 3 + 18 + (y2 + 260 + 4) - 47, new Color(28, 28, 28, 68).getRGB()));
        //RectHelper.drawRect((x - 1 + 1 - 1) - 1, y2 + 30 + 1 - 3 + 18.5f, (x - 1 + 1 - 1) +(width + 1 - 5 + 3) + (1 + 1) + 4, y2 + 30 + 1 - 3 +1 - 3 + 18 + (y2 + 260 + 4) - 47, new Color(28, 28, 28, 68).getRGB());



        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderHelper2.scissorRect(x - 1 + 1 - 80, y2 + 30 + 1 - 3 - 8, Minecraft.width,y2 + 30 + 1 -3 + 20 - 1);
        //RenderHelper2.drawRainbowRoundGui((x - 1 + 1 - 1), y2 + 30 + 1 - 3.5f, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 52, 1, true,true,false,true,2,4);
        GlStateManager.disable(GL11.GL_SCISSOR_TEST);
        ///DrawHelper.drawGradientRect((x - 1 - 2 + 3) * (1 + 1 - GuiScreen.progress), y2 + 48 + 1 - 3, (x - 1 + 2 - 1) * (1 + 1 - GuiScreen.progress)+ (width + 1) - (1 + 1 - GuiScreen.progress), 1 - 3 + 18 + (y2 + 50.3f + 4) - 17, new Color(0,0,0,55).getRGB(), new Color(0,0,0,0).getRGB());





        MWFont.GREYCLIFFCF_MEDIUM.get(30).drawCenterShadow(Formatting.BOLD + getName(), (x + width / 2) , y2 + 30 + HEADER_HEIGHT / 2F - 5.5f + 1 - 8, Color.WHITE.getRGB());
        Color color1;
        Color color2;
        Color color3;
        Color color4;
        Color textColor;
        //RoundedUtil.drawGradientRound((x - 1 + 1 - 1), y2 + 30 + 1 - 3, (width + 1 - 5 + 3) + (1 + 1) + 2, 1 - 3 + 18 + (y2 + 260 + 4) - 48, 7, new Color(255,255,255,200)));
        GlStateManager.popMatrix();

        inPanel = mouseY < y2 + 260 || mouseY > y2 + 30;

        super.drawComponent(scaledResolution, mouseX, mouseY);

        if (isExpanded()) {
                    for (Component component : components) {
                component.setY(height);
                component.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                    }
                }
                height += cHeight;
            }
        }
        if (isExpanded()) {
            for (Component component : components2) {
                component.setY(height);
                component.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded()) {
                        if (!(component instanceof ModuleComponent))
                            cHeight = expandableComponent.getHeightWithExpand() + 5;
                        else
                            cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                height += cHeight;
            }
        }
    }

    @Override
    public void onPress(int mouseX, int mouseY, int button) {
        if (button == 0 && !dragging) {
            dragging = true;
            prevX = mouseX - getX();
            prevY = mouseY - getY();
        }
    }
    static final HashMap<Integer, Integer> shadowCache = new HashMap<>();
    public void drawUltraGradientPanelBG(float x, float y, float width, float height, float range) {
        float y1 = y + 0.1f;
        //Helpers
        Color colorh1 = Color.WHITE,colorh2 = Color.WHITE,colorh3 = Color.WHITE,colorh4 = Color.WHITE,colorh5,colorh6,colorh7;
        //Basic
        Color color1 = Color.WHITE,color2 = Color.WHITE,color3 = Color.WHITE,color4 = Color.WHITE;

        //Creating
        int a = 255;
        boolean b = ClickGui.colored.get();
        String theme = "b";
        if (b) {
            theme = ClickGui.colorListof.getCurrentMode();
        }
        if (theme.equalsIgnoreCase("fire and ice")) {
            colorh1 = new Color(255, 0, 136, a);
            colorh2 = new Color(255, 0, 0, a);
            colorh3 = new Color(0, 149, 255,a);
            colorh4 = new Color(0, 34, 255,a);
        }else {
            colorh1 = new Color(0, 187, 255, a);
            colorh2 = new Color(255, 214, 214, a);
            colorh3 = new Color(255, 234, 0, a);
            colorh4 = new Color(255, 0, 251, a);
        }

        //interpolating
        boolean trueColor = false;
        Color twocolor = ColorUtil.applyOpacity(ClickGui.colorTwoo.getColorc(),a / 255f) ;
        Color onecolor = ColorUtil.applyOpacity(ClickGui.colorOnee.getColorc(),a / 255f) ;
        if (theme.equalsIgnoreCase("default")) {
            color1 = ColorUtil.interpolateColorsBackAndForth(7, 1, colorh1, colorh2, trueColor);
            color2 = ColorUtil.interpolateColorsBackAndForth(7, 30, colorh2, colorh3, trueColor);
            color3 = ColorUtil.interpolateColorsBackAndForth(7, 30, colorh3, colorh4, trueColor);
            color4 = ColorUtil.interpolateColorsBackAndForth(7, 30, colorh4, colorh1, trueColor);
        }else if (theme.equalsIgnoreCase("custom")){
            color1 = ColorUtil.interpolateColorsBackAndForth((int) 7, 0, twocolor, onecolor, true);
            color2 = ColorUtil.interpolateColorsBackAndForth((int) 7, 90, twocolor, onecolor, true);
            color3 = ColorUtil.interpolateColorsBackAndForth((int) 7, 180, twocolor, onecolor, true);
            color4 = ColorUtil.interpolateColorsBackAndForth((int) 7, 270, twocolor, onecolor, true);
        }else if (theme.equalsIgnoreCase("rainbow")){
            color1 = ColorUtil.rainbow(15, 0, 1, 1, 1);
            color2 = ColorUtil.rainbow(15, 90, 1, 1, 1);
            color3 = ColorUtil.rainbow(15, 180, 1, 1, 1);
            color4 = ColorUtil.rainbow(15, 270, 1, 1, 1);
        }else if (theme.equalsIgnoreCase("astolfo")){
            color1 = ColorUtil.skyRainbow(15, 0);
            color2 = ColorUtil.skyRainbow(15, 90);
            color3 = ColorUtil.skyRainbow(15, 180);
            color4 = ColorUtil.skyRainbow(15, 270);
        }
        glPushMatrix();
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawGradientRound(x + 0.5f,y1 + 0.5f,width -  1,height - 2f,range, ColorUtil.applyOpacity(color4, a / 255.0f), ColorUtil.applyOpacity(color1, a / 255.0f), ColorUtil.applyOpacity(color3, a / 255.0f), ColorUtil.applyOpacity(color2, a / 255.0f));
        StencilUtil.readStencilBuffer(1);
        for (double mp = 1 ; mp > 0 ; mp -= 1) {
            double finalMp = mp *12;
            double finalMp2 = mp;

            float startVal = 0.3f;
            double finalMp1 = mp;
            Color finalColor4 = color2;
            Color finalColor5 = color3;
            Color finalColor6 = color1;
            Color finalColor7 = color4;
            Runnable r = () -> RoundedUtil.drawGradientRound((float) (x - finalMp1), (float) (y - finalMp1), (float) (width + 2* finalMp1), (float) (height + 2 * finalMp1),range, RectHelper.injectAlpha(finalColor7, a).brighter(), RectHelper.injectAlpha(finalColor6, a).brighter(), RectHelper.injectAlpha(finalColor5, a).brighter(), RectHelper.injectAlpha(finalColor4, a).brighter());

            //r.run();

        }
        StencilUtil.uninitStencilBuffer();
        Color finalColor = color4;
        Color finalColor1 = color1;
        Color finalColor2 = color3;
        Color finalColor3 = color2;
        //RenderUtils2.drawBlur(4,() ->  RoundedUtil.drawGradientRound(x,y + 0.5f,width,height - 1f,range, ColorUtil.applyOpacity(finalColor, a / 255.0f), ColorUtil.applyOpacity(finalColor1, a / 255.0f), ColorUtil.applyOpacity(finalColor2, a / 255.0f), ColorUtil.applyOpacity(finalColor3, a / 255.0f)));
        if (!theme.equalsIgnoreCase("default")) {
            RoundedUtil.drawGradientRound(x, y1+ 0.5f, width, height - 1f, range, ColorUtil.applyOpacity(color4, a / 255.0f), ColorUtil.applyOpacity(color1, a / 255.0f), ColorUtil.applyOpacity(color3, a / 255.0f), ColorUtil.applyOpacity(color2, a / 255.0f));
            applyBloomWithMask(x, y1 + 0.5f, width, height - 1f, (int) 42,range, ColorUtil.applyOpacity(color4, a / 255.0f), ColorUtil.applyOpacity(color1, a / 255.0f), ColorUtil.applyOpacity(color3, a / 255.0f), ColorUtil.applyOpacity(color2, a / 255.0f));
        }else {
            RoundedUtil.drawGradientRound(x, y1 + 0.5f, width, height - 1f, range, ColorUtil.applyOpacity(color4, a / 255.0f), ColorUtil.applyOpacity(color1, a / 255.0f), ColorUtil.applyOpacity(color3, a / 255.0f), ColorUtil.applyOpacity(color2, a / 255.0f));
            applyBloomWithMask(x, y1 + 0.5f, width, height - 1f,(int) 52,range, ColorUtil.applyOpacity(color4, a / 255.0f), ColorUtil.applyOpacity(color1, a / 255.0f), ColorUtil.applyOpacity(color3, a / 255.0f), ColorUtil.applyOpacity(color2, a / 255.0f));
        }
        RoundedUtil.drawRoundOutline(x - 1.5f, y1+ 0.5f - 1.5f, width + 3f, height - 1f + 3f, range,0.01f,ColorUtil.applyOpacity(new Color(255,255,255), 0.01f / 255.0f), ColorUtil.applyOpacity(new Color(255,255,255), 60 / 255.0f));

        glPopMatrix();
    }

    @Override
    public void onMouseRelease(int button) {
        super.onMouseRelease(button);
        dragging = false;
    }

    @Override
    public boolean canExpand() {
        return !features.isEmpty();
    }

    private int height;
    @Override
    public int getHeightWithExpand() {
        int height = getHeight();
        if (isExpanded()) {
            for (Component component : components) {
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    ExpandableComponent expandableComponent = (ExpandableComponent) component;
                    if (expandableComponent.isExpanded())
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                }
                height += cHeight;
            }
        }
        this.height = (int) AnimationHelper.animation(this.height,height,0);
        return this.height;
    }
}
