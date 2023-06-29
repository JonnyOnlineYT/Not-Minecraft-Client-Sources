package ua.apraxia.modules.impl.render;


import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.utility.Utility;
import ua.apraxia.utility.math.MathUtility;
import ua.apraxia.utility.render.RenderUtility;

import java.awt.*;



public class Crosshair extends Module {
    public static ColorSetting color = new ColorSetting("Circle Color", new Color(111, 90, 253).getRGB());
    private float coldwnLast;

    public Crosshair() {
        super("Crosshair", Categories.Render);
        addSetting(color);
    }

    @EventTarget
    public void onRender2D(final EventRender2D event) {
        int crosshairColor = color.color;

        final float screenWidth = (float)event.getResolution().getScaledWidth();
        final float screenHeight = (float)event.getResolution().getScaledHeight();
        final float width = screenWidth / 2.0f;
        final float height = screenHeight / 2.0f;


        final double cinc = Utility.mc.player.getCooledAttackStrength(0.0f) * 359.0f;
        this.coldwnLast = (float) MathUtility.lerp(this.coldwnLast, (float)cinc, 0.30000001192092896);
        /*if (rainbow.value)
                crosshairColor = ColorUtil.rainbow(7,5, 1, 1, 1).getRGB(); */
        RenderUtility.drawCircle(width, height, 1.0f, 360.0f, 4, new Color(52, 52, 52, 190).hashCode(),3);
       /* if (rainbow.value)
            RenderUtil.drawRainbowCircle(width, height, 1.0f, 1.0f + this.coldwnLast, 4, 3); */
        RenderUtility.drawCircle(width, height, 1.0f, 1.0f + this.coldwnLast, 4, crosshairColor, 3);
    }
}