package ru.rockstar.client.features.impl.visuals;

import java.awt.*;

import net.minecraft.client.gui.ScaledResolution;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventRender2D;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Crosshair extends Feature {

    public static ColorSetting colorGlobal;
    public BooleanSetting dynamic;
    public BooleanSetting tmode;
    public BooleanSetting circle;
    public NumberSetting width;
    public NumberSetting gap;
    public NumberSetting length;
    public NumberSetting dynamicGap;

    public Crosshair() {
        super("Crosshair", "Изменяет ваш прицел",0, Category.VISUALS);
        circle = new BooleanSetting("Circle", false, () -> true);
        dynamic = new BooleanSetting("Dynamic", false, () -> true);
        dynamicGap = new NumberSetting("Dynamic Gap", 3, 1, 20, 1, dynamic::getBoolValue);
        gap = new NumberSetting("Gap", 2, 0F, 10F, 0.1F, () -> true);
        tmode = new BooleanSetting("T-Mode",false,() -> true);
        colorGlobal = new ColorSetting("Crosshair Color", new Color(0xFFFFFF).getRGB(), () -> true);
        width = new NumberSetting("Width", 1, 0F, 8, 1F, () -> true);
        length = new NumberSetting("Length", 3, 0.5F, 30F, 1F, () -> true);
        addSettings(circle, tmode , dynamic, dynamicGap, gap, colorGlobal, width, length);
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        int crosshairColor = colorGlobal.getColorValue();
        float screenWidth = event.getResolution().getScaledWidth();
        float screenHeight = event.getResolution().getScaledHeight();
        float width = screenWidth / 2;
        float height = screenHeight / 2;
        boolean dyn = dynamic.getBoolValue();
        float dyngap = dynamicGap.getNumberValue();
        float wid = this.width.getNumberValue();
        float len = length.getNumberValue();
        boolean isMoving = dyn && MovementHelper.isMoving();
        float gaps = isMoving ? dyngap : gap.getNumberValue();
        if (circle.getBoolValue()) {
        	ScaledResolution sr = new ScaledResolution(mc);
        	final float cooldawn = mc.player.getCooledAttackStrength(0);
			final double hpWidth2 = 360.0 * cooldawn;
			int width1 = sr.getScaledWidth();
	        int height1 = sr.getScaledHeight();
	        float middleX = width1 / 2.0F;
	        float middleY = height1 / 2.0F;
			DrawHelper.drawCircle(middleX, middleY, (float) 0.0f, (float) hpWidth2, 3, 3, false, new Color(crosshairColor));
        } else {
        	 DrawHelper.drawRect(width - gaps - len, height - (wid / 2), width - gaps, height + (wid / 2), crosshairColor);
        	 DrawHelper.drawRect(width + gaps, height - (wid / 2), width + gaps + len, height + (wid / 2), crosshairColor);
             if(!tmode.getBoolValue()) {
                 DrawHelper.drawRect(width - (wid / 2), height - gaps - len, width + (wid / 2), height - gaps, crosshairColor);
             }
             DrawHelper.drawRect(width - (wid / 2), height + gaps, width + (wid / 2), height + gaps + len, crosshairColor);
        }
    }
}
