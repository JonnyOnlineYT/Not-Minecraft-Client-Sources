package ru.wendoxd.events.impl.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventInteractBlock extends Event implements IEventCancelable {

    private boolean canceled;
    private BlockPos pos;
    private EnumFacing face;

    public EventInteractBlock(BlockPos pos, EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }

    public BlockPos getPos() {
        return pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public EnumFacing getFace() {
        return face;
    }

    public void setFace(EnumFacing face) {
        this.face = face;
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
