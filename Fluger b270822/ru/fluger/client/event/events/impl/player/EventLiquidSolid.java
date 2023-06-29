/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import ru.fluger.client.event.events.callables.EventCancellable;

public class EventLiquidSolid
extends EventCancellable {
    private final BlockLiquid blockLiquid;
    private final BlockPos pos;
    private AxisAlignedBB collision;

    public EventLiquidSolid(BlockLiquid blockLiquid, BlockPos pos) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
        this.collision = Block.NULL_AABB;
    }

    public BlockLiquid getBlock() {
        return this.blockLiquid;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public AxisAlignedBB getCollision() {
        return this.collision;
    }

    public void setCollision(AxisAlignedBB collision) {
        this.collision = collision;
    }
}

