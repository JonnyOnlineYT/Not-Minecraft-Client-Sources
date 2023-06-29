package ru.wendoxd.events.impl.block;

import net.minecraft.block.Block;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventCollideBlock extends Event implements IEventCancelable {

    public Block block;
    private boolean canceled;

    public EventCollideBlock(Block block) {
        this.block = block;
    }

    public Block getBlock() {
        return block;
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
