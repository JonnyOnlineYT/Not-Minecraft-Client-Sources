package ua.apraxia.modules.impl.render;


import net.minecraft.client.renderer.GlStateManager;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRenderScoreboard;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;

public class ScoreboardFeatures extends Module {
    public static BooleanSetting noScore = new BooleanSetting("No Scoreboard", false);
    public static SliderSetting x = new SliderSetting("Scoreboard X", 0, -1000, 1000, 1);
    public static SliderSetting y = new SliderSetting("Scoreboard Y", 0, -500, 500, 1);

    public ScoreboardFeatures() {
        super("Scoreboard", Categories.Render);
        addSetting(noScore, x, y);
    }

    @EventTarget
    public void onRenderScoreboard(EventRenderScoreboard event) {
        if (event.isPre()) {
            GlStateManager.translate(-x.value, y.value, 12);
        } else {
            GlStateManager.translate(x.value, -y.value, 12);
        }
    }
}
