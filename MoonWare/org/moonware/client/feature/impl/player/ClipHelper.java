package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.input.EventMouse;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.math.RotationHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

public class ClipHelper extends Feature {

    public static BooleanSetting disableBlockLight;
    public static NumberSetting maxDistance;

    public ClipHelper() {
        super("ClipHelper", "Клипается по Y оси при нажатии на колесо мыши по игроку", Type.Other);
        maxDistance = new NumberSetting("Max Distance", 50, 5, 150, 1, () -> true);
        disableBlockLight = new BooleanSetting("Disable block light", true, () -> true);
        addSettings(maxDistance, disableBlockLight);
    }

    @EventTarget
    public void onMouse(EventMouse event) {
        for (Entity entity : Minecraft.world.loadedEntityList) {
            BlockPos playerPosY = new BlockPos(0, Minecraft.player.posY, 0);
            BlockPos entityPosY = new BlockPos(0, entity.posY, 0);
            if (RotationHelper.isLookingAtEntity(Minecraft.player.rotationYaw, Minecraft.player.rotationPitch, 0.15F, 0.15F, 0.15F, entity, maxDistance.getNumberValue())) {
                int findToClip = (int) entity.posY;
                if (!playerPosY.equals(entityPosY) && Minecraft.gameSettings.thirdPersonView == 0) {
                    if (event.getKey() == 2) {
                        Minecraft.player.setPositionAndUpdate(Minecraft.player.posX, findToClip, Minecraft.player.posZ);
                        MWUtils.sendChat("Clip to entity " + Formatting.RED + entity.getName() + Formatting.WHITE + " on Y " + Formatting.RED + findToClip);
                    }
                }
            }
        }
    }
}
