package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class BlockInteractEvent extends EventCancellable {

	private BlockPos pos;
	private EnumFacing face;

	public BlockInteractEvent(BlockPos pos, EnumFacing face) {
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
}
