package ua.apraxia.eventapi.events.impl.player;

import net.minecraft.block.Block;
import ua.apraxia.eventapi.events.Event;
import ua.apraxia.eventapi.events.callables.EventCancellable;

public class EventFullCube extends EventCancellable implements Event {

    private boolean canceled;
    private Block block;

    public EventFullCube(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public void setCanceled() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

}
