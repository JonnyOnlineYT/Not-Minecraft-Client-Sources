package ua.apraxia.modules.impl.render;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.player.EventViewModel;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel extends Module {
    public static SliderSetting rightX = new SliderSetting("Right X", 0, -2, 2, 0.1F);
    public static SliderSetting rightY = new SliderSetting("Right Y", 0.2F, -2, 2, 0.1F);
    public static SliderSetting rightZ = new SliderSetting("Right Z", 0.0F, -2, 2, 0.1F);
    public static SliderSetting leftX = new  SliderSetting("Left X", 0, -2, 2, 0.1F);
    public static SliderSetting leftY = new  SliderSetting("Left Y", 0.2F, -2, 2, 0.1F);
    public static SliderSetting leftZ = new  SliderSetting("Left Z", 0.0F, -2, 2, 0.1F);

    public ViewModel() {
        super("ViewModel", Categories.Render);
        addSetting(rightX, rightY, rightZ, leftX, leftY, leftZ);
    }

    @EventTarget
    public void onSidePerson(EventViewModel event) {
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightX.value, rightY.value, rightZ.value);
        }
        if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(-leftX.value, leftY.value, leftZ.value);
        }
    }
}
