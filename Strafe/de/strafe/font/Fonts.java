package de.strafe.font;

import net.minecraft.util.*;
import java.awt.*;
import net.minecraft.client.*;

public class Fonts
{
    public static MCFontRenderer astolfoArray;
    
    public static void startFonts() {
        Fonts.astolfoArray = new MCFontRenderer(fontFromTTF(new ResourceLocation("Strafe/fonts/normal.ttf"), 16.0f, 0), true, false);
    }
    
    public static Font fontFromTTF(final ResourceLocation fontLocation, final float fontSize, final int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
}
