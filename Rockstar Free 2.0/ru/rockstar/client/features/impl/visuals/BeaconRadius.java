package ru.rockstar.client.features.impl.visuals;

import java.awt.Color;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.utils.PalatteHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class BeaconRadius extends Feature
{
    private final ListSetting colorMode;
    private final ColorSetting customColor;
    private final BooleanSetting outline;
    
    public BeaconRadius() {
        super("BeaconRadius", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0437\u043e\u043d\u0443 \u0440\u0430\u0434\u0438\u0443\u0441\u0430 \u043c\u0430\u044f\u043a\u0430", 0, Category.VISUALS);
        this.colorMode = new ListSetting("Circle Mode", "Custom", () -> true, new String[] { "Astolfo", "Rainbow", "Client", "Custom", "Cosmo" });
        this.customColor = new ColorSetting("Custom Color", Color.WHITE.getRGB(), () -> this.colorMode.currentMode.equals("Custom"));
        this.outline = new BooleanSetting("Outline", true, () -> true);
        this.addSettings(this.colorMode, this.customColor, this.outline);
    }
    
    @EventTarget
    public void onRender3D(final Event3D event) {
        final int oneColor = this.customColor.getColorValue();
        int color = 0;
        final String currentMode = this.colorMode.currentMode;
        switch (currentMode) {
            case "Client": {
                color = ClientHelper.getClientColor().getRGB();
                break;
            }
            case "Custom": {
                color = oneColor;
                break;
            }
            case "Astolfo": {
                color = PalatteHelper.astolfo(5000.0f, 1).getRGB();
                break;
            }
            case "Rainbow": {
                color = PalatteHelper.rainbow(300, 1.0f, 1.0f).getRGB();
                break;
            }
        }
        for (final TileEntity entity : BeaconRadius.mc.world.loadedTileEntityList) {
            if (entity instanceof TileEntityBeacon) {
                final float beaconLevel = (float)((TileEntityBeacon)entity).getLevels();
                final float beaconRad = (beaconLevel == 1.0f) ? 21.0f : ((beaconLevel == 2.0f) ? 31.0f : ((beaconLevel == 3.0f) ? 41.0f : ((beaconLevel == 4.0f) ? 51.0f : 0.0f)));
                final int points = 360;
                if (this.outline.getBoolValue()) {
                    DrawHelper.drawCircle3D(entity, beaconRad - 0.006, event.getPartialTicks(), points, 6.0f, Color.BLACK.getRGB());
                    DrawHelper.drawCircle3D(entity, beaconRad + 0.006, event.getPartialTicks(), points, 6.0f, Color.BLACK.getRGB());
                }
                DrawHelper.drawCircle3D(entity, beaconRad, event.getPartialTicks(), points, 2.0f, color);
            }
        }
    }
}
