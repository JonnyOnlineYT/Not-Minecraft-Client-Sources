package ru.wendoxd.events.impl.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventBlockRender extends Event implements IEventCancelable {

    private boolean canceled;
    private final IBlockState state;
    private final BlockPos pos;
    private final IBlockAccess access;
    private final BufferBuilder bufferBuilder;

    public EventBlockRender(IBlockState state, BlockPos pos, IBlockAccess access, BufferBuilder bufferBuilder) {
        this.state = state;
        this.pos = pos;
        this.access = access;
        this.bufferBuilder = bufferBuilder;
    }

    public IBlockState getState() {
        return state;
    }

    public BufferBuilder getBufferBuilder() {
        return bufferBuilder;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockAccess getAccess() {
        return access;
    }

    @Override
    public void setCanceled() {
        canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

}