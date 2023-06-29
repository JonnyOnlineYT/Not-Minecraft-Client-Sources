package ru.rockstar.client.features.impl.display;

import java.awt.Color;

import net.minecraft.client.gui.ScaledResolution;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class DamageInfo extends Feature {
    public DamageInfo() {
        super("DamageInfo", "Визуально показывает получаемый урон", 0, Category.DISPLAY);
    }

    @EventTarget
    public void hotbar(Event2D event2D) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mc.player.hurtTime > 0) {
        	DrawHelper.drawGradientRect(0, 0, sr.getScaledWidth(), 10, new Color(255,0,0,255).getRGB(), new Color(0,0,0,0).getRGB());
        	DrawHelper.drawGradientRect(0, sr.getScaledHeight() - 12, sr.getScaledWidth(), sr.getScaledHeight(), new Color(0,0,0,0).getRGB(), new Color(255,0,0,255).getRGB());
        	DrawHelper.drawGradientRect1(12, sr.getScaledHeight(), 0, 0, new Color(0,0,0,0).getRGB(), new Color(255,0,0,255).getRGB());
        	DrawHelper.drawGradientRect1(sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth() - 12, 0, new Color(255,0,0,255).getRGB(), new Color(0,0,0,0).getRGB());
        }
    }
}
