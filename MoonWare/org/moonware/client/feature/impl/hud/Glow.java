package org.moonware.client.feature.impl.hud;

import net.minecraft.client.renderer.GlStateManager;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.ArrayList;

import java.awt.*;

public class Glow extends ArrayList {


    public static void onRender2D() {
        if (ArrayList.glow.getBoolValue()) {
            GlStateManager.pushMatrix();
            Color colors = new Color(ArrayList.glowcol.getColorValue());
            double translateY = ArrayList.PtranslateY;
            double translateX = ArrayList.PtranslateX;
            double widthglow = ArrayList.Pwidthglow;
            double listOffset = ArrayList.PlistOffset;
            double width = ArrayList.PwidthSSS;
            //RenderHelper2.renderBlurredShadow(new Color(colors.getRed(),colors.getGreen(),colors.getBlue(), 113),translateX - 2, translateY + fontY.getCurrentValue(),width, listOffset + 4, (int) glowRadius.getCurrentValue());
            GlStateManager.popMatrix();
        }
    }
}
