package Celestial.module.impl.Render;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.player.EventViewModel;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.ui.settings.impl.NumberSetting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;

public class ViewModel extends Module {
    public static NumberSetting rightX = new NumberSetting("RightX", 0, -2, 2, 0.1F, () -> true);
    public static NumberSetting rightY = new NumberSetting("RightY", 0.2F, -2, 2, 0.1F, () -> true);
    public static NumberSetting rightZ = new NumberSetting("RightZ", 0.0F, -2, 2, 0.1F, () -> true);
    public static NumberSetting leftX = new NumberSetting("LeftX", 0, -2, 2, 0.1F, () -> true);
    public static NumberSetting leftY = new NumberSetting("LeftY", 0.2F, -2, 2, 0.1F, () -> true);
    public static NumberSetting leftZ = new NumberSetting("LeftZ", 0.0F, -2, 2, 0.1F, () -> true);

    public ViewModel() {
        super("ViewModel", "Изменяет положение рук", ModuleCategory.Render);
        addSettings(rightX, rightY, rightZ, leftX, leftY, leftZ);
    }

    @EventTarget
    public void onSidePerson(EventViewModel event) {
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(rightX.getCurrentValue(), rightY.getCurrentValue(), rightZ.getCurrentValue());
        }
        if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(-leftX.getCurrentValue(), leftY.getCurrentValue(), leftZ.getCurrentValue());
        }
    }
}
