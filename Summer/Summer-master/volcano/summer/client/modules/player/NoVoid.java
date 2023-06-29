package volcano.summer.client.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventTick;

public class NoVoid extends Module {

	public NoVoid() {
		super("NoVoid", 0, Category.PLAYER);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	public static Block getBlock(BlockPos block) {
		return Minecraft.getMinecraft().theWorld.getBlockState(block).getBlock();
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventTick) && (mc.theWorld != null)) {
			boolean hasGround = false;
			for (int i = 1; i < this.mc.thePlayer.posY; i++) {
				BlockPos pos = new BlockPos(this.mc.thePlayer.posX, i, this.mc.thePlayer.posZ);
				if (!(getBlock(pos) instanceof BlockAir)) {
					hasGround = true;
				}
			}
			if ((!hasGround) && (this.mc.thePlayer.posY <= 0.0D)) {
				this.mc.thePlayer.motionY = 0.1D;
			}
		}
	}
}
