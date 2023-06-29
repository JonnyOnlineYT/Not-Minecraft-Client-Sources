/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.event.events.impl.player;

import net.minecraft.block.BlockObsidian;
import net.minecraft.util.math.BlockPos;
import ru.fluger.client.event.events.callables.EventCancellable;

public class ObsidianPlaceEvent
extends EventCancellable {
    private final BlockObsidian block;
    private final BlockPos pos;

    public BlockObsidian getBlock() {
        return this.block;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public ObsidianPlaceEvent(BlockObsidian block, BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }
}

