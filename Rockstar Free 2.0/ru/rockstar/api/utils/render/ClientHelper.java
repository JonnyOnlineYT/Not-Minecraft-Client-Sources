package ru.rockstar.api.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import ru.rockstar.api.utils.Helper;
import ru.rockstar.api.utils.PalatteHelper;
import ru.rockstar.api.utils.font.FontRenderer;
import ru.rockstar.client.features.impl.display.ClientFont;

import java.awt.*;

public class ClientHelper implements Helper {
    public static ServerData serverData;

    public static Color getClientColor() {
        Color color = Color.white;
        Color onecolor = new Color(ClientFont.onecolor.getColorValue());
        Color twoColor = new Color(ClientFont.twocolor.getColorValue());
        double time = ClientFont.time.getNumberValue();
        String mode = ClientFont.arrayColor.getOptions();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = DrawHelper.rainbow((int) (1 * 200 * 0.1f), 0.5f, 1.0f);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = PalatteHelper.astolfo(true, (int) (yDist * 4));
        } else if (mode.equalsIgnoreCase("Pulse")) {
            color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0F * (yDist * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = DrawHelper.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yDist * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        return color;
    }

    public static Color getClientColor(float yStep, float yStepFull, int speed) {
        Color color = Color.white;
        Color onecolor = new Color(ClientFont.onecolor.getColorValue());
        Color twoColor = new Color(ClientFont.twocolor.getColorValue());
        double time = ClientFont.time.getNumberValue();
        String mode = ClientFont.arrayColor.getOptions();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = DrawHelper.rainbowCol(yStep, yStepFull, 0.5f, speed);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = DrawHelper.astolfoColors45(yStep, yStepFull, 0.5f, speed);
        } else if (mode.equalsIgnoreCase("Pulse")) {
            color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (yStep * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = DrawHelper.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        return color;
    }

    public static Color getClientColor(float yStep,float astolfoastep, float yStepFull, int speed) {
        Color color = Color.white;
        Color onecolor = new Color(ClientFont.onecolor.getColorValue());
        Color twoColor = new Color(ClientFont.twocolor.getColorValue());
        double time = ClientFont.time.getNumberValue();
        String mode = ClientFont.arrayColor.getOptions();
        float yDist = (float) 4;
        int yTotal = 0;
        for (int i = 0; i < 30; i++) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = DrawHelper.rainbowCol(yStep, yStepFull, 0.5f, speed);
        } else if (mode.equalsIgnoreCase("Astolfo")) {
            color = DrawHelper.astolfoColors45(astolfoastep, yStepFull, 0.5f, speed);
        } else if (mode.equalsIgnoreCase("Pulse")) {
            color = DrawHelper.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (yStep * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("Custom")) {
            color = DrawHelper.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0F * (yStep * 2.55) / 60);
        } else if (mode.equalsIgnoreCase("None")) {
            color = new Color(255, 255, 255);
        }
        return color;
    }

    public static FontRenderer getFontRender() {
        Minecraft mc = Minecraft.getMinecraft();
        FontRenderer font = mc.sfui18;
        String mode = ClientFont.fontMode.getOptions();
        switch (mode) {
            case "Myseo":
                font = mc.neverlose500_18;
                break;
            case "SFUI":
                font = mc.sfui18;
                break;
            case "Lato":
                font = mc.lato;
                break;
            case "Roboto Regular":
                font = mc.robotoRegular;
                break;
            case "URWGeometric":
                font = mc.urwgeometric;
                break;
            case "WexSide":
                font = mc.mntsb;
                break;
            case "NeverLose":
                font = mc.neverlose500_14;
                break;
            case "Comic Sans":
                font = mc.comicsans_14;
                break;
            case "Tenacity":
                font = mc.tenacity;
                break;
            case "TenacityBold":
                font = mc.tenacityb;
                break;
            case "RubikBold":
                font = mc.rubikb;
                break;
            case "Rubik":
                font = mc.rubik;
                break;
            case "Tahoma":
                font = mc.tahoma;
                break;
            case "TahomaBold":
                font = mc.tahomab;
                break;
        }
        return font;
    }
}