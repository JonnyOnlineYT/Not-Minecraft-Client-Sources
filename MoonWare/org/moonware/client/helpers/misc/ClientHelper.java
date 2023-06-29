package org.moonware.client.helpers.misc;

import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.MWFont;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList;
import org.moonware.client.feature.impl.hud.HUD;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.Utils.ColorUtil;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.utils.CustomFont;

import java.awt.*;

public class ClientHelper implements Helper {
    public static final Color getAlternateClientColor() {
        return new Color(28, 167, 222);
    }

    public static Color[] getClientColorR() {
        Color color = Color.white;
        Color onecolor = new Color(HUD.onecolor.getColorValue());
        Color twoColor = new Color(HUD.twocolor.getColorValue());
        double time = HUD.time.getNumberValue();
        String mode = HUD.colorList.getOptions();
        float yDist = (float) 4;
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow((int) (yDist * 200 * time), ArrayList.rainbowSaturation.getNumberValue(), ArrayList.rainbowBright.getNumberValue());
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(false, (int) yDist);
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().darker().getRGB(), (float) Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1)));
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(onecolor.getRGB());
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twoColor.getRGB(), (float) Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1)));
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        if (HUD.enterAstolfo.get()) {
            if (HUD.useCustomColors.get()) {
                color = ColorUtil.interpolateColorsBackAndForth((int)time,(int)Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1) * 2,HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), true);
            }else{
                color = ColorUtil.interpolateColorsBackAndForth((int)time,(int)Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1) * 2, RenderHelper2.oneColorGradient(), RenderHelper2.alternateColorGradient(), true);

            }
        }
        return new Color[]{color};
    }
    public static Color getClientColor() {
        Color color = Color.white;
        Color onecolor = new Color(HUD.onecolor.getColorValue());
        Color twoColor = new Color(HUD.twocolor.getColorValue());
        double time = HUD.time.getNumberValue();
        String mode = HUD.colorList.getOptions();
        float yDist = (float) 4;
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow((int) (yDist * 200 * time), ArrayList.rainbowSaturation.getNumberValue(), ArrayList.rainbowBright.getNumberValue());
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(false, (int) yDist);
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().darker().getRGB(), (float) Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1)));
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(onecolor.getRGB());
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twoColor.getRGB(), (float) Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1)));
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        if (HUD.enterAstolfo.get()) {
            if (HUD.useCustomColors.get()) {
                color = ColorUtil.interpolateColorsBackAndForth((int)time,(int)Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1) * 2,HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
            }else{
                color = ColorUtil.interpolateColorsBackAndForth((int)time,(int)Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1) * 2, RenderHelper2.oneColorGradient(), RenderHelper2.alternateColorGradient(), HUD.hueInterpolation.get());

            }
        }
        return color;
    }

    public static Color getColor() {
        Color color = Color.white;
        Color onecolor = new Color(HUD.onecolor.getColorValue());
        Color twoColor = new Color(HUD.twocolor.getColorValue());
        double time = HUD.time.getNumberValue();
        String mode = HUD.colorList.getOptions();
        float yDist = (float) 4;
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow((int) (yDist * 200 * time), ArrayList.rainbowSaturation.getNumberValue(), ArrayList.rainbowBright.getNumberValue());
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(false, (int) yDist);
        } else if (mode.equalsIgnoreCase("Fade")) {
            color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), onecolor.darker().darker().getRGB(), (float) Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1)));
        } else if (mode.equalsIgnoreCase("Static")) {
            color = new Color(onecolor.getRGB());
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = new Color(PaletteHelper.fadeColor(onecolor.getRGB(), twoColor.getRGB(), (float) Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1)));
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        if (HUD.enterAstolfo.get()) {
            if (HUD.useCustomColors.get()) {
                color = ColorUtil.interpolateColorsBackAndForth((int)time,(int)Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1) * 2,HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), true);
            }else{
                color = ColorUtil.interpolateColorsBackAndForth((int)time,(int)Math.abs(((System.currentTimeMillis() / time) / time + yDist * 2 / 60 * 2) % 2 - 1) * 2, RenderHelper2.oneColorGradient(), RenderHelper2.alternateColorGradient(), true);

            }
        }
        return color;
    }
    public static int getColor(float hueoffset, float saturation, float brightness) {
        float speed = 4500f;
        float hue = System.currentTimeMillis() % speed / speed;
        return Color.HSBtoRGB(hue - hueoffset / 54, saturation, brightness);
    }
    public static Color getColor(float yStep, float yStepFull, int speed) {
        Color color = Color.white;
        Color onecolor = new Color(HUD.onecolor.getColor());
        Color twoColor = new Color(HUD.twocolor.getColor());
        final double time = 10.0;
        String mode = HUD.colorList.getOptions();
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = PaletteHelper.rainbow((int)(yStep * time), 0.5f, 1.0f);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PaletteHelper.astolfo(yStep, yStepFull, 0.5f, (float)speed);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = PaletteHelper.TwoColorEffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yStep * 2.55) / 60.0);
        }
        else if (mode.equalsIgnoreCase("Fade")) {
            color = PaletteHelper.TwoColorEffect(new Color(onecolor.getRGB()), new Color(onecolor.darker().darker().getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yStep * 2.55) / 60.0);
        }
        if (HUD.enterAstolfo.get()) {
            if (HUD.useCustomColors.get()) {
                color = ColorUtil.interpolateColorsBackAndForth((int)speed,(int)yStep,HUD.onecolorHUD.getColorc(), HUD.twocolorHUD.getColorc(), HUD.hueInterpolation.get());
            }else{
                color = ColorUtil.interpolateColorsBackAndForth((int)speed,(int)yStep, RenderHelper2.oneColorGradient(), RenderHelper2.alternateColorGradient(), HUD.hueInterpolation.get());

            }
        }
        return color;
    }

    public static CustomFont getFontRender() {
        CustomFont font = MWFont.SF_UI_DISPLAY_REGULAR.get(18);
        String mode = HUD.font.getOptions();
        if (mode.equalsIgnoreCase("Comfortaa")) {
            font = MWFont.MW_FONT.get(19);
        } else if (mode.equalsIgnoreCase("SF UI")) {
            font = MWFont.SF_UI_DISPLAY_REGULAR.get(15);
        } else if (mode.equalsIgnoreCase("Verdana")) {
            font = FontStorage.verdanaFontRender;
        } else if (mode.equalsIgnoreCase("RobotoRegular")) {
            font = FontStorage.robotoRegularFontRender;
        } else if (mode.equalsIgnoreCase("Lato")) {
            font = FontStorage.latoFontRender;
        } else if (mode.equalsIgnoreCase("Open Sans")) {
            font = FontStorage.openSansFontRender;
        } else if (mode.equalsIgnoreCase("Ubuntu")) {
            font = FontStorage.ubuntuFontRender;
        } else if (mode.equalsIgnoreCase("LucidaConsole")) {
            font = FontStorage.lucidaConsoleFontRenderer;
        } else if (mode.equalsIgnoreCase("Calibri")) {
            font = FontStorage.calibri;
        } else if (mode.equalsIgnoreCase("Product Sans")) {
            font = FontStorage.productsans;
        } else if (mode.equalsIgnoreCase("RaleWay")) {
            font = FontStorage.raleway;
        } else if (mode.equalsIgnoreCase("Kollektif")) {
            font = FontStorage.kollektif;
        } else if (mode.equalsIgnoreCase("CircleRegular")) {
            font = FontStorage.circleregular;
        } else if (mode.equalsIgnoreCase("MontserratRegular")) {
            font = MWFont.MONTSERRAT_REGULAR.get(19);
        } else if (mode.equalsIgnoreCase("MontserratLight")) {
            font = MWFont.MONTSERRAT_LIGHT.get(15);
        } else if (mode.equalsIgnoreCase("Menlo")) {
            font = FontStorage.menlo;
        }else if (mode.equalsIgnoreCase("MontserratMicroBold")) {
            font = MWFont.MONTSERRAT_SEMIBOLD.get(10);
        }else if (mode.equalsIgnoreCase("MontserratBold")) {
            font = MWFont.MONTSERRAT_BOLD.get(14);
            //font = mc.montserrat_semibold10;
        }
        return font;
    }
}
