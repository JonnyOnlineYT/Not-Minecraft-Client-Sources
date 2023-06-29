package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.Feature;
import org.moonware.client.helpers.render.cosmetic.CosmeticRender;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class Cosmetics extends Feature {
    public static ListSetting wingsmode = new ListSetting("Wing mode", "gray", () -> true, "gray","white");
    public static BooleanSetting wings = new BooleanSetting("Wings","Крылья на вас, правда летать на них нельзя",true,() -> true);
    public static NumberSetting scale = new NumberSetting("Scale",0.30F,0.40F,2.0F,0.01F, () -> true);
    public Cosmetics() {
        super("Cosmetics","Позволяет менять свой визуальный внешний вид", Type.Visuals);
        addSettings(wingsmode, wings, scale);
    }

    @Override
    public void onEnable() {
        for (RenderPlayer render : Minecraft.getRenderManager().getSkinMap().values()) {
            render.addLayer(new CosmeticRender(render));
        }
    }
}
