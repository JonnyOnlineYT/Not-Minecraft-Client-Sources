package ua.apraxia.eventapi.events.impl.player;

import ua.apraxia.eventapi.events.Event;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.util.math.BlockPos;

public class CrystalPlaceEvent implements Event {

    public ItemEndCrystal getBlock() {
        return block;
    }

    private final ItemEndCrystal block;

    public BlockPos getPos() {
        return pos;
    }

    private final BlockPos pos;

    public CrystalPlaceEvent(final ItemEndCrystal block, final BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }
}