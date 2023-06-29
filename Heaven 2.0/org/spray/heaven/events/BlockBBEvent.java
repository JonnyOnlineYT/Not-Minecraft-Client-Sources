package org.spray.heaven.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class BlockBBEvent extends EventCancellable {

	private int x;
	private int y;
	private int z;

	private AxisAlignedBB boundingBox;

	public BlockBBEvent(BlockPos blockPos, AxisAlignedBB boundingBox) {
		this.boundingBox = boundingBox;
		this.x = blockPos.getX();
		this.y = blockPos.getY();
		this.z = blockPos.getZ();
	}
	
	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(AxisAlignedBB boundingBox) {
		this.boundingBox = boundingBox;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

}
