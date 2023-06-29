package volcano.summer.client.events;

import net.minecraft.block.Block;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import volcano.summer.base.manager.event.Event;

public class EventBlockBB extends Event {


	public static Block block;
	public BlockPos pos;
	public AxisAlignedBB boundingBox;
	public int x;
	public int y;
	public int z;

	public void setBlock(Block block, AxisAlignedBB boundingBox, int x, int y, int z, BlockPos pos) {
		this.block = block;
		this.boundingBox = boundingBox;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Block getBlock() {
		return block;
	}

	public AxisAlignedBB getBoundingBox() {
		return boundingBox;
	}

	public BlockPos pos() {
		return pos;
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
