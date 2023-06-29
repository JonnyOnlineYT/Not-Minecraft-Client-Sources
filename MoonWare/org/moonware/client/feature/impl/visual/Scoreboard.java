package org.moonware.client.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRenderScoreboard;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class Scoreboard extends Feature {

    public static BooleanSetting noScore;
    public static BooleanSetting scoreboardPoints = new BooleanSetting("Points", false, () -> true);
    public NumberSetting x;
    public NumberSetting y;

    public Scoreboard() {
        super("Scoreboard", "Позволяет настроить скорборд на сервере", Type.Visuals);
        noScore = new BooleanSetting("No Scoreboard", false, () -> true);
        x = new NumberSetting("Scoreboard X", 0, -1000, 1000, 1, () -> !noScore.getBoolValue());
        y = new NumberSetting("Scoreboard Y", 0, -500, 500, 1, () -> !noScore.getBoolValue());
        addSettings(noScore, scoreboardPoints, x, y);
    }

    @EventTarget
    public void onRenderScoreboard(EventRenderScoreboard event) {
        if (event.isPre()) {
            GlStateManager.translate(-x.getNumberValue(), y.getNumberValue(), 12);
        } else {
            GlStateManager.translate(x.getNumberValue(), -y.getNumberValue(), 12);
        }
    }
}
