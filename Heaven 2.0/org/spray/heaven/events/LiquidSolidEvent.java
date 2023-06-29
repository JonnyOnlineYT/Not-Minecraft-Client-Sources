package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;

public class LiquidSolidEvent extends EventCancellable {
	
    private final BlockLiquid blockLiquid;
    private final BlockPos pos;

    public LiquidSolidEvent(BlockLiquid blockLiquid, BlockPos pos) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
    }

    public BlockLiquid getBlock() {
        return this.blockLiquid;
    }

    public BlockPos getPos() {
        return this.pos;
    }

}
