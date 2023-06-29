package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;

public class Eagle extends Feature {

    public Eagle() {
        super("Eagle", "Нажимает шифт когда строишься", Type.Combat);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        BlockPos pos = new BlockPos(Minecraft.player.posX, Minecraft.player.posY - 1, Minecraft.player.posZ);

        Minecraft.gameSettings.keyBindSneak.pressed = Minecraft.world.getBlockState(pos).getBlock() == Blocks.AIR;
    }
}
