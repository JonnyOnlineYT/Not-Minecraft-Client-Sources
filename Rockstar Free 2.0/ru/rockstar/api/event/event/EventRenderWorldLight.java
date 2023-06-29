package ru.rockstar.api.event.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import ru.rockstar.api.event.Event;

public class EventRenderWorldLight extends Event {

    private final EnumSkyBlock enumSkyBlock;
    private final BlockPos pos;

    public EventRenderWorldLight(EnumSkyBlock enumSkyBlock, BlockPos pos) {
        this.enumSkyBlock = enumSkyBlock;
        this.pos = pos;
    }

    public EnumSkyBlock getEnumSkyBlock() {
        return enumSkyBlock;
    }

    public BlockPos getPos() {
        return pos;
    }
}