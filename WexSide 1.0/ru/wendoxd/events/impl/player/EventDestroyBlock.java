package ru.wendoxd.events.impl.player;

import net.minecraft.util.math.BlockPos;
import ru.wendoxd.events.Event;

public class EventDestroyBlock extends Event {
	private BlockPos block;

	public EventDestroyBlock(BlockPos block) {
		this.block = block;
	}

	public BlockPos getBlockPos() {
		return this.block;
	}
}
