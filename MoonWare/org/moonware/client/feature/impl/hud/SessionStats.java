package org.moonware.client.feature.impl.hud;

import org.moonware.client.utils.MWFont;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.helpers.Utils.render.RenderUtil;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SessionStats extends Feature {
    public static int gamesPlayed, killCount;
    public static long startTime = System.currentTimeMillis(), endTime = -1;
    public static final String[] KILL_TRIGGERS = {"by *", "para *", "fue destrozado a manos de *"};
    private final List<String> linesLeft = Arrays.asList("Play time", "Games played", "Kills");

    //private final Dragging dragging = Tenacity.INSTANCE.createDrag(this, "sessionstats", 40, 100);


    private final BooleanSetting animatedPlaytime = new BooleanSetting("Animated counter", true);
    public final ListSetting colorMode = new ListSetting("Color", "Sync", "Sync", "Analogous", "Tenacity", "Gradient", "Modern");
    public final ListSetting degree = new ListSetting("Degree", "30", "30", "-30");
    private final ColorSetting color1 = new ColorSetting("Color 1", ClientHelper.getClientColor());
    private final ColorSetting color2 = new ColorSetting("Color 2", ClientHelper.getAlternateClientColor());
    private static NumberSetting xC = new NumberSetting("X",300,1,700,1);
    private static NumberSetting yC = new NumberSetting("Y",300,1,700,1);

    public SessionStats() {
        super("SessionStats","Displays session stats", Type.Hud );
        //color1.addParent(colorMode, modeSetting -> modeSetting.is("Gradient") || modeSetting.is("Analogous"));
        //color2.addParent(colorMode, modeSetting -> modeSetting.is("Gradient") && !modeSetting.is("Analogous"));
        //degree.addParent(colorMode, modeSetting -> modeSetting.is("Analogous"));
        addSettings(animatedPlaytime, xC, yC, colorMode, degree, color1, color2);
    }


    @EventTarget
    private void onRender2D(EventRender2D event){
        float x = (float) xC.get(), y = (float) yC.get();
        float height = linesLeft.size() * (MWFont.MONTSERRAT_BOLD.get(18).getHeight() + 6) + 24;
        float width = 140;
        if(colorMode.equals("Modern")){
            RoundedUtil.drawRoundOutline(x, y, width, height, 6, .5f,ColorUtil.applyOpacity(Color.WHITE, .85f),ColorUtil.applyOpacity(Color.WHITE, .85f));
        }else {
            RoundedUtil.drawRound(x, y, width, height, 6, ColorUtil.applyOpacity(Color.WHITE, .85f));
        }
    }

    float playtimeWidth = 20.5f;

    private Color gradientColor1 = Color.WHITE, gradientColor2 = Color.WHITE, gradientColor3 = Color.WHITE, gradientColor4 = Color.WHITE;
    @EventTarget
    private void OnRender(EventRender2D event){
        float x = xC.getCurrentValue(), y = xC.getCurrentIntValue();
        float height = linesLeft.size() * (MWFont.MONTSERRAT_BOLD.get(18).getHeight() + 6) + 24;
        float width = 140;
        //dragging.setHeight(height);
        //dragging.setWidth(width);

        boolean hueIn = true;
        switch (colorMode.getCurrentMode()) {
            case "Sync":
                //HudMod hudMod = (HudMod) Tenacity.INSTANCE.getModuleCollection().get(HudMod.class);
                Color[] colors = ClientHelper.getClientColorR();
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, colors[0], colors[1], hueIn);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, colors[0], colors[1], hueIn);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, colors[0], colors[1], hueIn);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, colors[0], colors[1], hueIn);
                break;
            case "Tenacity":
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, ClientHelper.getClientColor(), ClientHelper.getAlternateClientColor(), hueIn);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, ClientHelper.getClientColor(), ClientHelper.getAlternateClientColor(), hueIn);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, ClientHelper.getClientColor(), ClientHelper.getAlternateClientColor(), hueIn);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, ClientHelper.getClientColor(), ClientHelper.getAlternateClientColor(), hueIn);
                break;
            case "Gradient":
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, color1.getColorc(), color2.getColorc(), hueIn);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, color1.getColorc(), color2.getColorc(), hueIn);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, color1.getColorc(), color2.getColorc(), hueIn);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, color1.getColorc(), color2.getColorc(), hueIn);
                break;
            case "Analogous":
                int val = degree.equals("30") ? 0 : 1;
                Color analogous = ColorUtil.getAnalogousColor(color1.getColorc())[val];
                gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, color1.getColorc(), analogous, hueIn);
                gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, color1.getColorc(), analogous, hueIn);
                gradientColor3 = ColorUtil.interpolateColorsBackAndForth(15, 180, color1.getColorc(), analogous, hueIn);
                gradientColor4 = ColorUtil.interpolateColorsBackAndForth(15, 270, color1.getColorc(), analogous, hueIn);
                break;
            case "Modern":
                RoundedUtil.drawRoundOutline(x, y, width, height, 6, .5f, new Color(10, 10, 10, 80), new Color(-2));
                break;
        }
        boolean outlinedRadar = !(colorMode.equals("Modern"));
        RenderUtil.setAlphaLimit(0);
        if (outlinedRadar) {
            RoundedUtil.drawGradientRound(x, y, width, height, 6, ColorUtil.applyOpacity(gradientColor4, .85f), gradientColor1, gradientColor3, gradientColor2);
            Helper.gui.drawGradientRect2(x - 1, y + 15, width + 2, 5, ColorUtil.applyOpacity(Color.BLACK, .2f).getRGB(), ColorUtil.applyOpacity(Color.BLACK, 0).getRGB());
        }else {
            Helper.gui.drawGradientRect2(x +1, y + 15, width - 2, 5, ColorUtil.applyOpacity(Color.BLACK, .2f).getRGB(), ColorUtil.applyOpacity(Color.BLACK, 0).getRGB());
        }


        MWFont.MONTSERRAT_BOLD.get(18).drawCenter("Statistics", x + width / 2, y + (colorMode.equals("Modern") ? 3 : 2), -1);




        for (int i = 0; i < linesLeft.size(); i++) {
            int offset = i * (MWFont.MONTSERRAT_BOLD.get(18).getHeight() + 6);
            MWFont.MONTSERRAT_BOLD.get(18).draw(linesLeft.get(i), x + 5, (float) (y + offset + (i == 0 ? 23.5 : 25)), -1);
        }
        int[] playTime = getPlayTime();

        playtimeWidth = (float) RenderUtil.animate(20.5 + (playTime[1] > 0 ? 20 : 0) + (playTime[0] > 0 ? 14 : 0), playtimeWidth, 0.008);

        float playtimeX = x + width - (playtimeWidth + 5);
        if (animatedPlaytime.get()) {
            drawAnimatedPlaytime(playtimeX, y, width);
        } else {
            String playtimeString = playTime[0] + "h " + playTime[1] + "m " + playTime[2] + "s";
            MWFont.MONTSERRAT_BOLD.get(18).draw(playtimeString, playtimeX + playtimeWidth - MWFont.MONTSERRAT_BOLD.get(18).getWidth(playtimeString), y + 24, -1);
        }

        List<String> linesRight = Arrays.asList(String.valueOf(gamesPlayed), String.valueOf(killCount));

        for (int i = 0; i < linesRight.size(); i++) {
            int offset = (i + 1) * (MWFont.MONTSERRAT_BOLD.get(18).getHeight() + 6);
            MWFont.MONTSERRAT_BOLD.get(18).draw(linesRight.get(i), x + width - (MWFont.MONTSERRAT_BOLD.get(18).getWidth(linesRight.get(i)) + 5), y + offset + 25, -1);
        }

    }


    //Animation values for going up and down with the time
    float hourYAnimation;
    float minuteYAnimation1;
    float minuteYAnimation2;
    float secondYAnimation2;
    float secondYAnimation1;

    //Animation values for going left or right based on the width of the other charchter
    float secondsSeperateWidthAnim1;
    float secondsSeperateWidthAnim2;
    float minuteSeperateWidthAnim1;
    float minuteSeperateWidthAnim2;

    private void drawAnimatedPlaytime(float playtimeX, float y, float width) {
        int[] playTime = getPlayTime();
        RoundedUtil.drawRoundOutline(playtimeX, y + 21, playtimeWidth, 13, 6, .5f, ColorUtil.applyOpacity(Color.WHITE, 0), Color.WHITE);
        //RoundedUtil.drawRound(playtimeX, y + 22, playtimeWidth, 11, 6, new Color(30, 30, 30));
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(playtimeX, y + 22, playtimeWidth, 11, 6, new Color(30, 30, 30));
        StencilUtil.readStencilBuffer(1);


        float secondX = playtimeX + playtimeWidth - 7;
        MWFont.MONTSERRAT_BOLD.get(18).draw("s", secondX, y + 24, -1);

        int secondsFirstPlace = (playTime[2] % 10);

        secondYAnimation2 = (float) RenderUtil.animate(20 * secondsFirstPlace, secondYAnimation2, .02);

        secondsSeperateWidthAnim1 = (float) RenderUtil.animate(MWFont.MONTSERRAT_BOLD.get(18).getWidth(String.valueOf(secondsFirstPlace)), secondsSeperateWidthAnim1, .05);

        secondX -= secondsSeperateWidthAnim1 + .5;

        for (int i = 0; i < 10; i++) {
            MWFont.MONTSERRAT_BOLD.get(18).draw(String.valueOf(i), secondX, y + 24 + (i * 20) - secondYAnimation2, -1);
        }

        int secondsSecondPlace = Math.floorDiv(playTime[2], 10);

        secondYAnimation1 = (float) RenderUtil.animate(20 * (secondsSecondPlace), secondYAnimation1, .02);

        secondsSeperateWidthAnim2 = (float) RenderUtil.animate(MWFont.MONTSERRAT_BOLD.get(18).getWidth(String.valueOf(secondsSecondPlace)), secondsSeperateWidthAnim2, .05);


        secondX -= secondsSeperateWidthAnim2 + .5;

        for (int i = 0; i < 10; i++) {
            MWFont.MONTSERRAT_BOLD.get(18).draw(String.valueOf(i), secondX, y + 24 + (i * 20) - secondYAnimation1, -1);
        }

        if (playTime[1] > 0) {

            float minuteX = playtimeX + playtimeWidth - 27;

            MWFont.MONTSERRAT_BOLD.get(18).draw("m", minuteX, y + 24, -1);

            int minuteFirstPlace = (playTime[1] % 10);

            minuteYAnimation1 = (float) RenderUtil.animate(20 * minuteFirstPlace, minuteYAnimation1, .02);

            minuteSeperateWidthAnim1 = (float) RenderUtil.animate(MWFont.MONTSERRAT_BOLD.get(18).getWidth(String.valueOf(minuteFirstPlace)), minuteSeperateWidthAnim1, .05);

            minuteX -= minuteSeperateWidthAnim1 + .5;

            for (int i = 0; i < 10; i++) {
                MWFont.MONTSERRAT_BOLD.get(18).draw(String.valueOf(i), minuteX, y + 24 + (i * 20) - minuteYAnimation1, -1);
            }

            int minuteSecondPlace = Math.floorDiv(playTime[1], 10);

            minuteYAnimation2 = (float) RenderUtil.animate(20 * (minuteSecondPlace), minuteYAnimation2, .02);

            minuteSeperateWidthAnim2 = (float) RenderUtil.animate(MWFont.MONTSERRAT_BOLD.get(18).getWidth(String.valueOf(minuteSecondPlace)), minuteSeperateWidthAnim2, .05);

            minuteX -= minuteSeperateWidthAnim2 + .5;

            for (int i = 0; i < 10; i++) {
                MWFont.MONTSERRAT_BOLD.get(18).draw(String.valueOf(i), minuteX, y + 24 + (i * 20) - minuteYAnimation2, -1);
            }

            if (playTime[0] > 0) {
                hourYAnimation = (float) RenderUtil.animate(20 * (playTime[0] % 10), hourYAnimation, .02);

                MWFont.MONTSERRAT_BOLD.get(18).draw("h", playtimeX + playtimeWidth - 44, y + 24, -1);
                for (int i = 0; i < 10; i++) {
                    MWFont.MONTSERRAT_BOLD.get(18).draw(String.valueOf(i), playtimeX + playtimeWidth - 49, y + 24 + (i * 20) - hourYAnimation, -1);
                }

            }

        }


        StencilUtil.uninitStencilBuffer();
    }

    public static int[] getPlayTime() {
        long diff = getTimeDiff();
        long diffSeconds = 0, diffMinutes = 0, diffHours = 0;
        if (diff > 0) {
            diffSeconds = diff / 1000 % 60;
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
        }
       /* String str = (int) diffSeconds + "s";
        if (diffMinutes > 0) str = (int) diffMinutes + "m " + str;
        if (diffHours > 0) str = (int) diffHours + "h " + str;*/
        return new int[]{(int) diffHours, (int) diffMinutes, (int) diffSeconds};
    }

    public static long getTimeDiff() {
        return (endTime == -1 ? System.currentTimeMillis() : endTime) - startTime;
    }

    public static void reset() {
        startTime = System.currentTimeMillis();
        endTime = -1;
        gamesPlayed = 0;
        killCount = 0;
    }


}
