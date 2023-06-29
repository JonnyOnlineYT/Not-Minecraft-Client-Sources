package ru.wendoxd.events.impl.block;

import net.minecraft.block.Block;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.IEventCancelable;

public class EventFullCube extends Event implements IEventCancelable {

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

    @Override
    public void setCanceled() {
        canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

}
