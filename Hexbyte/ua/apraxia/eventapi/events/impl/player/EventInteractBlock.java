package ua.apraxia.eventapi.events.impl.player;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import ua.apraxia.eventapi.events.Event;
import ua.apraxia.eventapi.events.callables.EventCancellable;

public class EventInteractBlock extends EventCancellable implements Event {

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

    public void setCanceled() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
