package ru.rockstar.client.features.impl.visuals;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;

import java.awt.*;

public class ChestEsp extends Feature {
    public static BooleanSetting espOutline;
    public static ColorSetting chestColor;

    public ChestEsp() {
        super("ChestEsp", "Показывает сундуки через стены", 0, Category.VISUALS);
        chestColor = new ColorSetting("Chest Color", new Color(0xFFFFFF).getRGB(), () -> true);
        espOutline = new BooleanSetting("ESP Outline", false, () -> true);
        addSettings(espOutline,chestColor);
    }

    @EventTarget
    public void onRender3D(Event3D event) {

        if (mc.player != null || mc.world != null) {
            for (TileEntity entity : mc.world.loadedTileEntityList) {
                BlockPos pos = entity.getPos();
                if (entity instanceof TileEntityChest) {
                    DrawHelper.blockEsp(pos, new Color(chestColor.getColorValue()), espOutline.getBoolValue());


                }
            }
        }
    }
}