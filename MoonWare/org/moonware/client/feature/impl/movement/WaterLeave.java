package org.moonware.client.feature.impl.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.NumberSetting;

public class WaterLeave extends Feature {

    private final NumberSetting leaveMotion;

    public WaterLeave() {
        super("WaterLeave", "Игрок высоко прыгает при погружении в воду", Type.Movement);
        leaveMotion = new NumberSetting("Leave Motion", 10, 0.5F, 20, 0.5F, () -> true);
        addSettings(leaveMotion);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {

        if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 0.95, Minecraft.player.posZ)).getBlock() == Blocks.WATER) {
            Minecraft.player.motionY = leaveMotion.getNumberValue();
            Minecraft.player.onGround = true;
            Minecraft.player.isAirBorne = true;
        }
        if (Minecraft.player.isInWater() || Minecraft.player.isInLava()) {
            Minecraft.player.onGround = true;
        }
        if (Minecraft.world.getBlockState(new BlockPos(Minecraft.player.posX, Minecraft.player.posY + 0.0000001, Minecraft.player.posZ)).getBlock() == Blocks.WATER) {
            Minecraft.player.motionY = 0.06f;
        }
    }
}
