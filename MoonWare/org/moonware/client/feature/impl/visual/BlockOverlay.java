package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.misc.ClientHelper;
import org.moonware.client.helpers.palette.PaletteHelper;
import org.moonware.client.helpers.render.RenderHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ColorSetting;
import org.moonware.client.settings.impl.ListSetting;

import java.awt.*;

public class BlockOverlay extends Feature {

    public ListSetting colorMode = new ListSetting("Color Box Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");
    public ColorSetting colorPicker = new ColorSetting("Color BlockOverlay", -1, () -> colorMode.currentMode.equals("Custom"));
    public BooleanSetting outline = new BooleanSetting("Outline BlockOverlay", false, () -> true);

    public BlockOverlay() {
        super("BlockOverlay", "Показывает блоки на которые вы навелись", Type.Visuals);
        addSettings(colorMode, colorPicker, outline);
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        int color = 0;
        switch (colorMode.currentMode) {
            case "Client":
                color = ClientHelper.getClientColor().getRGB();
                break;
            case "Custom":
                color = colorPicker.getColorValue();
                break;
            case "Astolfo":
                color = PaletteHelper.astolfo(false, Minecraft.objectMouseOver.getBlockPos().getY()).getRGB();
                break;
            case "Rainbow":
                color = PaletteHelper.rainbow(300, 1, 1).getRGB();
                break;
        }
        if (Minecraft.world.getBlockState(Minecraft.objectMouseOver.getBlockPos()).getBlock() != Blocks.AIR) {
            GlStateManager.pushMatrix();
            RenderHelper.blockEsp(new BlockPos(Minecraft.objectMouseOver.getBlockPos().getX(), Minecraft.objectMouseOver.getBlockPos().getY(), Minecraft.objectMouseOver.getBlockPos().getZ()), new Color(color), outline.getBoolValue());
            GlStateManager.popMatrix();
        }
    }

}
