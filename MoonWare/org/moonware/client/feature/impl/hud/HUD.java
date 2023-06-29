package org.moonware.client.feature.impl.hud;

import com.jhlabs.image.GaussianFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.Namespaced;
import org.lwjgl.opengl.GL11;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.misc.FreeCam;
import org.moonware.client.feature.impl.movement.timer;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.player.MovementHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.Interpolator;
import org.moonware.client.utils.MWFont;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class HUD extends Feature {

    public static float globalOffset;
    public static ListSetting font;
    public static EntityLivingBase target;
    public static BooleanSetting clientInfo;
    public static BooleanSetting worldInfo;
    public static BooleanSetting potion;
    public static BooleanSetting potionIcons;
    public static BooleanSetting potionTimeColor;
    public static BooleanSetting armor;
    public static BooleanSetting rustHUD;
    public static BooleanSetting indicator;
    public static ListSetting indicatorMode;
    public static float timar = 357;
    public static BooleanSetting blur = new BooleanSetting("Blur", false, () -> true);
    public static ListSetting colorList = new ListSetting("Global Color", "Astolfo", () -> true, "Astolfo", "Static", "Fade", "Rainbow", "Custom", "None", "Category");
    public static NumberSetting time = new NumberSetting("Color Time", 30, 1, 100, 1, () -> true);
    public static ColorSetting onecolor = new ColorSetting("One Color", new Color(0xFFFFFF).getRGB(), () -> colorList.currentMode.equals("Fade") && false || colorList.currentMode.equals("Custom") && false || colorList.currentMode.equals("Static") && false);
    public static ColorSetting twocolor = new ColorSetting("Two Color", new Color(0xFF0000).getRGB(), () -> colorList.currentMode.equals("Custom"));
    public static BooleanSetting targetHuds = new BooleanSetting("TargetHud", "показывает информацию о таргете на вашем экране",false, () -> false);
    public static ListSetting targetHudModes = new ListSetting("TargetHud Mode", "Moon", () -> false, "Default","Circle", "Moon", "Astolfo", "moonware", "Rich", "Novoline Old", "Novoline New", "Dev", "Minecraft", "Flux", "Test");
    public static ColorSetting targetMoonColor1 = new ColorSetting("Gradient color one", new Color(255, 133, 0).getRGB(), () -> targetHudModes.currentMode.equalsIgnoreCase("Moon"));
    public static ColorSetting targetMoonColor2 = new ColorSetting("Gradient color two", new Color(255, 55, 255).getRGB(), () -> targetHudModes.currentMode.equalsIgnoreCase("Moon"));
    public static NumberSetting thudX = new NumberSetting("TargetHud X", "Расположение таргет худа по оси X",0, -700, 700, 1, () -> targetHuds.getBoolValue());
    public static NumberSetting thudY = new NumberSetting("TargetHud Y","Расположение таргет худа по оси Y", 0, -700, 700, 1, () -> targetHuds.getBoolValue());
    public static BooleanSetting selfHud = new BooleanSetting("selfTargetHud", "показывает информацию о вас если таргет не найден",false, () -> true);
    public static ColorSetting colora = new ColorSetting("TargetHud Color", Color.blue.getRGB(), ()-> targetHuds.getBoolValue() && !targetHudModes.currentMode.equalsIgnoreCase("Moon"));
    public static BooleanSetting timerhud = new BooleanSetting("Timer", "визуально показывает в процентном соотношении сколько осталось до флага за использование таймера", true, () -> true);
    public static NumberSetting timerhudx = new NumberSetting("Timer X", "Расположение по оси X",0, -700, 700, 1, () -> false);
    public static NumberSetting timerhudy = new NumberSetting("Timer Y","Расположение по оси Y", 0, -700, 700, 1, () -> false);
    public static BooleanSetting BPShud = new BooleanSetting("BPS HUD", "визуально показывает вашу скорость передвижения", false, () -> true);
    public final EntityEquipmentSlot armorType;
    public static ColorSetting timerColor = new ColorSetting("Timer Color", Color.WHITE.getRGB(),()-> timerhud.get());
    public static BooleanSetting GapleCoolDown = new BooleanSetting("GAppleCooldown", "показывает кулдавн золотого яблока на экране", false, () ->true);
    public static NumberSetting CoolDownTimer = new NumberSetting("Cooldown speed multiplier", 1, 1, 15,0.1F,() -> true);
    public float animation;
    public static final Color blacka     = new Color(1, 1, 1, 71);
    private float cooledAttackStrength;
    private float move;

    private float health;
    private float healthhelp;
    private float movehelp;
    private float healthhelpr;
    private float radius;
    private static float radiushelp;
    private float radius2;
    private double hui;
    private double healthBarWidth;
    private double cooldownBarWidth;
    public static double timerBarWidth;
    private double timerWid;
    private double hpWidth;
    private double HealthBarWidth;
    private String enemyNickname;
    private double enemyHP;
    private double enemyDistance;
    private EntityPlayer entityPlayer;
    private EntityPlayer entit;
    private Entity entity;
    public static int diap;
    private int color = colora.getColorValue();
    private double thelp;
    public static BooleanSetting nurikMode = new BooleanSetting("Nursultan Mode",false);
    public static BooleanSetting colored = new BooleanSetting("colored",false,() -> !nurikMode.get());

    public static NumberSetting nurikAlpha = new NumberSetting("Bg alpha",25, 10, 100, 1, () -> true);
    public static BooleanSetting testAstolfoColors = new BooleanSetting("testColors", true, () -> false);

    public static BooleanSetting enterAstolfo = new BooleanSetting("InterpolateColors", true,() -> false);
    public static BooleanSetting MoonColors = new BooleanSetting("MoonWare Colors", false, () -> false);
    private static boolean n = nurikMode.get();
    private static boolean c = colored.get();
    private static boolean t = testAstolfoColors.get();

    public static BooleanSetting useCustomColors = new BooleanSetting("UseCustomColors", false,() -> nurikMode.get() || colored.get());
    public static ColorSetting onecolorHUD = new ColorSetting("OneColor", new Color(0x00E1FF).getRGB(), () -> useCustomColors.get() && (nurikMode.get() || colored.get()));
    public static ColorSetting twocolorHUD = new ColorSetting("TwoColor", new Color(0xE598FF).getRGB(), () -> useCustomColors.get() && (nurikMode.get() || colored.get()));

    public static BooleanSetting timertestAstolfoColors = new BooleanSetting("testColors Timer", false, () -> timerhud.get());
    public static BooleanSetting customHotbar = new BooleanSetting("BetterHotBar", false, () -> false);
    public static BooleanSetting customHotbarA = new BooleanSetting("BetterHotBar", false);
    public static BooleanSetting testTimerMode = new BooleanSetting("Test Timer", false,()-> timerhud.get());
    public static ColorSetting timerColor2 = new ColorSetting("Timer ColorTwo", Color.WHITE.getRGB(),()-> timerhud.get());
    public static BooleanSetting radar = new BooleanSetting("Radar",false);
    public static NumberSetting radarSize = new NumberSetting("RadarSize", 65,30,100,1,() -> !nurikMode.get() && radar.get());
    public static BooleanSetting hueInterpolation = new BooleanSetting("Use Hue",false);
    public static BooleanSetting glow = new BooleanSetting("Glow",false);

    public HUD() {
        super("HUD", "Показывает информацию на экране", Type.Hud);
        clientInfo = new BooleanSetting("Client Info", true, () -> false);
        worldInfo = new BooleanSetting("World Info", true, () -> true);
        potion = new BooleanSetting("Potion Status", false, () -> true);
        potionIcons = new BooleanSetting("Potion icons", false, ( ) -> false);
        potionTimeColor = new BooleanSetting("Potion Time Color", false,() -> false);
        armor = new BooleanSetting("Armor Status", false, () -> true);
        rustHUD = new BooleanSetting("Rust", false, () -> false);
        indicator = new BooleanSetting("Indicator", false, () -> false);
        indicatorMode = new ListSetting("Че по конфигам лазиешь?", "Че по конфигам лазиешь?", () -> indicator.getBoolValue(), "Че по конфигам лазиешь?", "Skeet");
        font = new ListSetting("FontList", "RobotoRegular", () -> true, "Minecraft","MontserratMicroBold","MontserratBold", "MontserratRegular", "MontserratLight", "Menlo", "Comfortaa", "SF UI", "Calibri", "Verdana", "CircleRegular", "RobotoRegular", "JetBrains Mono", "LucidaConsole", "Corporative Sans", "Lato", "RaleWay", "Product Sans", "Arial", "Open Sans", "Kollektif", "Ubuntu");
        addSettings(nurikMode,colored, nurikAlpha, testAstolfoColors, enterAstolfo, MoonColors, useCustomColors, onecolorHUD, twocolorHUD, colorList, time, onecolor, twocolor, font, timerhud, timertestAstolfoColors, radar, radarSize, testTimerMode, customHotbar, customHotbarA, hueInterpolation,glow, timerColor, timerColor2, timerhudx, timerhudy, worldInfo, potion, potionIcons, potionTimeColor, armor, rustHUD, blur, indicator, indicatorMode);
        armorType = null;
        thelp = 1d;
    }

    @EventTarget
    public void onUpdate() {
        customHotbar.setValue(false);

    }
    static final HashMap<Integer, Integer> shadowCache = new HashMap<>();
    public static void startSmoothLine() {
        // Отключаем "неправильную" прозрачность
        glDisable(GL_ALPHA_TEST);

        // Начало бленда
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Отключение дёрфа и 2д текстурирования
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);

        // Отключение маски дёрфа
        glDepthMask(false);

        // Включаем основную плавность
        glEnable(GL_CULL_FACE);
        glEnable(GL_LINE_SMOOTH);

        // Хинты
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
    }

    public static void endSmoothLine() {
        // Включение дёрфа и 2д текстурирования
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        // Конец бленда
        glDisable(GL_BLEND);

        // Вернем альфу на "изначальный режим"
        glEnable(GL_ALPHA_TEST);

        // Включение маски дёрфа
        glDepthMask(true);

        // Отключаем основную плавность
        glCullFace(GL_BACK);
        glDisable(GL_LINE_SMOOTH);

        // Хинты
        glHint(GL_LINE_SMOOTH_HINT, GL_DONT_CARE);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_DONT_CARE);
    }
    public static void draw2DLine(double startOfX, double startOfY, double endOfX, double endOfY, double width, Color color,Color color2, Color color3, Color color4) {
        //Включение плавности линии

        // Ширина линии
        glLineWidth((float) width);

        // Цвет линии
        // Начало отрисовки
        GL11.glBegin(GL_QUAD_STRIP);

        // Начало линии
        glColor3f(color.getRed()/ 255f,color.getGreen()/ 255f,color.getBlue()/ 255f);
        glVertex2d(startOfX, startOfY);
        // Конец линии
        glColor3f(color2.getRed()/ 255f,color2.getGreen()/ 255f,color2.getBlue()/ 255f);
        glVertex2d(endOfX, endOfY);

        // Конец отрисовки
        glEnd();

        // Выключение плавности
    }
    @EventTarget
    public void onRender2(EventRender2D e) {
        int width = 123;
        int height = 123;
        glPushMatrix();
        Color color1 = new Color(0, 217, 255);
        Color color2 = new Color(255, 0, 0);
        Color color3 = new Color(0, 255, 245);
        Color color4 = new Color(200, 0, 255);
        draw2DLine(123,123,534,123,5,color1,color2,color3,color4);

        glPopMatrix();

        Color color = new Color(31,31,31);
        final int identifier = (int) (width * height + width + color.hashCode() * 14 + 14);
        int texId = -1;
        if (shadowCache.containsKey(identifier)) {
            texId = shadowCache.get(identifier);

            GlStateManager.bindTexture(texId);
        } else {
            final BufferedImage original = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);

            final GaussianFilter op = new GaussianFilter(14);

            final BufferedImage blurred = op.filter(original, null);
            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);

            shadowCache.put(identifier, texId);
        }
        customHotbar.setValue(false);
        //font.setCurrentMode("MontserratBold");
        if (!FreeCam.enable) {
            radius = Minecraft.player.getPosition().getY();
        }
        radiushelp = radius;
        if (FreeCam.enable) {
            try {
                float x = new ScaledResolution(Helper.mc).getScaledWidth() / 2.0f - 45;
                float y = new ScaledResolution(Helper.mc).getScaledHeight() / 2.0f - 5;
                String text = "clip : " + (radius2 - radiushelp);
                RectHelper.drawRectWithGlow(x, y - 2.5f, x + FontStorage.robotoRegularFontRender.getWidth(text) + 2, y + 7,5,15, new Color(0xC8FFFFFF, true));
                radius2 = Minecraft.player.getPosition().getY();
                //mc.circleregular.drawStringWithShadow("clip : " + String.valueOf(radiushelp - radius2), x, y, 1);
                FontStorage.robotoRegularFontRender.draw("clip : " + (radius2 - radiushelp), x, y, 1);
            }catch (Exception ex) {
            }
        }
        if (indicator.getBoolValue()) {
            if (Float.isNaN(cooledAttackStrength)) {
                cooledAttackStrength = Minecraft.player.getCooledAttackStrength(0);
            }
            if (Float.isNaN(move)) {
                move = MovementHelper.getSpeed();
            }

            cooledAttackStrength = (float) Interpolator.linear(cooledAttackStrength, Minecraft.player.getCooledAttackStrength(0) * 50, 5F / Minecraft.getDebugFPS());
            movehelp = (float) Interpolator.linear(move, MovementHelper.getSpeed() * 40, 3F / Minecraft.getDebugFPS());
            if (movehelp <= 52.0f) {
                move = movehelp;
            }


            if (indicatorMode.currentMode.equals("Onetap v2")) {
                /*
                RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 80, 105, 26, new Color(61, 58, 58).getRGB());
                RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 97, 105, 2, new Color(123, 0, 255).getRGB());
                RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 95, 105, 12, new Color(61, 58, 58).getRGB());
                mc.clickguismall.drawStringWithShadow("Indicators", 19 + mc.clickguismall.getStringWidth("flags"), e.getResolution().getScaledHeight() / 2F - 92, -1);
                mc.clickguismall.drawStringWithShadow("cooldown", 10, e.getResolution().getScaledHeight() / 2F - 75, -1);
                RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
                RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, cooledAttackStrength, 1.5F, new Color(123, 0, 255).getRGB());
                mc.clickguismall.drawStringWithShadow("move", 10, e.getResolution().getScaledHeight() / 2F - 64, -1);
                RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
                RectHelper.drawSmoothRectBetter(15 + mc.clickguismall.getStringWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, move, 1.5F, new Color(123, 0, 255).getRGB());

                 */
                RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 80, 105, 26, new Color(61, 58, 58).getRGB());
                RectHelper.drawRectWithGlow(9.2f, e.getResolution().getScaledHeight() / 2F - 97, 109, e.getResolution().getScaledHeight() / 2F - 97,5,15, new Color(123, 0, 255));
                RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 95, 105, 12, new Color(61, 58, 58).getRGB());
                MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow("Indicators", 19 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("flags"), e.getResolution().getScaledHeight() / 2F - 92, -1);
                MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow("cooldown", 10, e.getResolution().getScaledHeight() / 2F - 75, -1);
                RectHelper.drawSmoothRectBetter(15 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
                RectHelper.drawRectWithGlow(15 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, 52 + cooledAttackStrength, e.getResolution().getScaledHeight() / 2F - 73,5,15, new Color(123, 0, 255));
                MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow("move", 10, e.getResolution().getScaledHeight() / 2F - 64, -1);
                RectHelper.drawSmoothRectBetter(15 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
                RectHelper.drawRectWithGlow(15 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, 52 + move, e.getResolution().getScaledHeight() / 2F - 61,5,15, new Color(123, 0, 255));

                float y = e.getResolution().getScaledHeight() / 2F - -7;

                for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                    if (feature.getBind() != 0 && !feature.getLabel().equals("ClickGui") && !feature.getLabel().equals("ClientFont") && Minecraft.player.isDead && Minecraft.player.isInWater()
                    && Minecraft.player.isInLava()) {
                        RectHelper.drawRectWithGlow(6, y, 105, 13,2, 15, new Color(61, 58, 58));
                        RectHelper.drawRectWithGlow(6, e.getResolution().getScaledHeight() / 2F - 10, 105, 2,5,15, new Color(123, 0, 255));
                        RectHelper.drawRectWithGlow(6, e.getResolution().getScaledHeight() / 2F - 8, 105, 12,2, 15, new Color(61, 58, 58));
                        MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow("keybinds", 10 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("keybinds"), e.getResolution().getScaledHeight() / 2F - 5, -1);
                        String toggled = feature.getState() ? " [toggled]" : " [disable]";
                        MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow(feature.getLabel().toLowerCase(), 10, y + 4, -1);
                        MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow(toggled, 75, y + 4, -1);
                        y += 12;
                    }
                }
            }
        }
        if (indicatorMode.currentMode.equals("Skeet") && indicator.getBoolValue()) {
            RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 80, 105, 26, new Color(61, 58, 58, 140).getRGB());
            RectHelper.drawGradientRectBetter(5.5F, e.getResolution().getScaledHeight() / 2F - 97, 106, 2, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).darker().darker().getRGB());
            RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 95, 105, 12, new Color(61, 58, 58, 140).getRGB());
            MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow("Indicators", 32 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("flags"), e.getResolution().getScaledHeight() / 2F - 92, -1);
            MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow("cooldown", 10, e.getResolution().getScaledHeight() / 2F - 75, -1);
            RectHelper.drawSmoothRectBetter(15 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
            RectHelper.drawGradientRectBetter(15 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 73, cooledAttackStrength, 1.5F, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).darker().darker().getRGB());
            MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow("move", 10, e.getResolution().getScaledHeight() / 2F - 64, -1);
            RectHelper.drawSmoothRectBetter(15 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, 50, 1.5F, new Color(30, 30, 30, 70).getRGB());
            RectHelper.drawGradientRectBetter(15 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("cooldown"), e.getResolution().getScaledHeight() / 2F - 61, move, 1.5F, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).darker().darker().getRGB());

            float y = e.getResolution().getScaledHeight() / 2F - -7;

            for (Feature feature : MoonWare.featureManager.getFeatureList()) {
                if (feature.getBind() != 0 && !feature.getLabel().equals("ClickGui") && !feature.getLabel().equals("ClientFont")) {
                    RectHelper.drawSmoothRectBetter(6, y, 105, 13, new Color(61, 58, 58, 140).getRGB());
                    RectHelper.drawGradientRectBetter(5.5F, e.getResolution().getScaledHeight() / 2F - 10, 106, 2, new Color(255, 255, 255).getRGB(), new Color(255, 255, 255).darker().darker().getRGB());
                    RectHelper.drawSmoothRectBetter(6, e.getResolution().getScaledHeight() / 2F - 8, 105, 12, new Color(61, 58, 58, 140).getRGB());
                    MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).drawShadow("keybinds", 10 + MWFont.SF_UI_DISPLAY_REGULAR.get(16.5F).getWidth("keybinds"), e.getResolution().getScaledHeight() / 2F - 5, -1);
                    String toggled = feature.getState() ? " [toggled]" : " [disable]";
                    MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow(feature.getLabel().toLowerCase(), 10, y + 5, -1);
                    MWFont.SF_UI_DISPLAY_REGULAR.get(14).drawShadow(toggled, 75, y + 5, -1);
                    y += 12;
                }
            }
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        customHotbar.setValue(false);
        float target = (Minecraft.screen instanceof ChatScreen) ? 15 : 0;
        float delta = globalOffset - target;
        globalOffset -= delta / Math.max(1, Minecraft.getDebugFPS()) * 10;
        if (!Double.isFinite(globalOffset)) {
            globalOffset = 0;
        }
        if (globalOffset > 15) {
            globalOffset = 15;
        }
        if (globalOffset < 0) {
            globalOffset = 0;
        }

    }

    @EventTarget
    public void onRenderr(EventRender2D event) {
        customHotbar.setValue(false);
        if (timerhud.getBoolValue()) {
            float x = new ScaledResolution(Helper.mc).getScaledWidth() / 2.0f - timerhudx.getNumberValue();
            float y = new ScaledResolution(Helper.mc).getScaledHeight() / 2.0f + timerhudy.getNumberValue();
            if (Float.isNaN(move)) {
                move = MovementHelper.getSpeed();
            }
            movehelp = (float) Interpolator.linear(move, MovementHelper.getSpeed() * 40, 3F / Minecraft.getDebugFPS());
            move = movehelp;
            int dias = (int) calculatePercentage(timar, 357);
            if (dias > 100) {
                dias = 100;
            }
            if (dias < 0) {
                dias = 0;
            }
            if (Minecraft.timer.timerSpeed != 1.0F && !(MoonWare.featureManager.getFeatureByClass(timer.class).getState())) {
                if (dias > 2) {
                    timar -= 0.1f;
                }
            }
            if ((!(MoonWare.featureManager.getFeatureByClass(timer.class).getState()) || timer.timer.getNumberValue() <= 1.0) && timar < 357) {
                if (move > 2.5f) {
                    /*if (Minecraft.player.ticksExisted % 19 == 0) {
                        timar += 1.5f;
                    }
                    if (dias < 76 && Minecraft.player.ticksExisted % 24 == 0) {
                        timar += 5.2;
                    }*/
                }
                if (move < 2.5f) {
                    if (Minecraft.player.ticksExisted % 4 == 0) {
                        timar += 9.3f;
                    }
                    if (Minecraft.player.ticksExisted % 16 == 0) {
                        timar += 16.3f;
                    }
                }
            }
            if (MoonWare.featureManager.getFeatureByClass(timer.class).getState()) {
                if (dias >= 5f) {
                    if (!Minecraft.player.onGround) {
                        if (move > 2.5f) {
                            if (Minecraft.player.ticksExisted % 2 == 0 && Minecraft.player.fallDistance <= 1.2 && timar >= 0) {
                                timar -= timer.timer.getNumberValue() * 8.2f;
                            } else if (Minecraft.player.ticksExisted % 2 == 0) {
                                timar -= timer.timer.getNumberValue() * 12.5f;
                            }
                        } else {
                            if (Minecraft.player.ticksExisted % 2 == 0 && Minecraft.player.fallDistance <= 1.2 && timar >= 0) {
                                timar -= timer.timer.getNumberValue() * 6.2f;
                            } else if (Minecraft.player.ticksExisted % 2 == 0) {
                                timar -= timer.timer.getNumberValue() * 11.5f;
                            }

                        }
                    } else {
                        if (move > 2.5f) {
                            if (Minecraft.player.ticksExisted % 2 == 0 && timar >= 0) {
                                timar -= timer.timer.getNumberValue() * 9.2f;
                            }
                        } else if (move < 2.5f && timar <= 357) {
                            if (Minecraft.player.ticksExisted % 4 == 0) {
                                timar += timer.timer.getNumberValue() * 4.3f;
                            }
                            if (Minecraft.player.ticksExisted % 16 == 0) {
                                timar += timer.timer.getNumberValue() * 11.3f;
                            }
                        }
                    }
                } else if (dias <= 5f) {
                    if (move < 2.5f) {
                        if (Minecraft.player.ticksExisted % 4 == 0) {
                            timar += 9.3f;
                        }
                        if (Minecraft.player.ticksExisted % 16 == 0) {
                            timar += 16.3f;
                        }
                    } else if (move > 2.5f) {
                        if (Minecraft.player.ticksExisted % 4 == 0) {
                            timar -= 0.53f;
                        }
                        if (Minecraft.player.ticksExisted % 15 == 0) {
                            timar += 1.8f;
                        }
                    }
                }
            }
            if (dias >= 100) {
                if (Minecraft.player.ticksExisted % 2 == 0) {
                    timar -= 0;
                }
            }

            int max = 357;
            float fax = 0;
            if (timar >= 0 && timar <= 358) {
                fax = timar;
            }
            int dia = (int) calculatePercentage(timar, max);
            diap = 0;
            if (dia <= 100 && dia >= 0) {
                diap = dia;
            }
        }
    }
    public void drawHead(Namespaced skin, int x, int y) {
        GL11.glColor4f(1, 1, 1, 1);
        Minecraft.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(x, y, 8, 8, 8, 8, 37, 37, 64, 64);
    }
    public static float getHurtPercent(EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / (float) 10;
    }
    public static float getRenderHurtTime(EntityLivingBase hurt) {
        customHotbar.setValue(false);
        return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? Minecraft.timer.renderPartialTicks : 0.0f);
    }
    public EntityEquipmentSlot getEquipmentSlot()
    {
        return armorType;
    }
    public static double calculatePercentage(double obtained, double total) {
        return obtained * 100 / total;
    }
    public static int rnd(int min, int max)
    {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
