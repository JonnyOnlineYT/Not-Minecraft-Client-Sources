package ru.rockstar.client.features.impl.player;

import net.minecraft.network.play.client.CPacketEntityAction;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Spider extends Feature {

    public static NumberSetting climbTicks;
    private final TimerHelper timerHelper = new TimerHelper();

    public Spider() {
        super("Spider", "Автоматически взберается на стены", 0,Category.MOVEMENT);
        climbTicks = new NumberSetting("Climb Ticks", 1, 0, 5, 0.1F, () -> true);
        addSettings(climbTicks);
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        this.setSuffix("" + climbTicks.getNumberValue(), true);
        if (MovementHelper.isMoving() && mc.player.isCollidedHorizontally) {
            if (timerHelper.hasReached(climbTicks.getNumberValue() * 100)) {
                event.setGround(true);
                mc.player.onGround = true;
                mc.player.isCollidedVertically = true;
                mc.player.isCollidedHorizontally = true;
                mc.player.isAirBorne = true;
                mc.player.jump();
                timerHelper.reset();
            }
        }
        /*
          if (mc.playerController.getCurrentGameType() == GameType.ADVENTURE) {
		} else {
			int block = -1;
			for (int i = 0; i < 9; i++) {
				ItemStack s = mc.player.inventory.getStackInSlot(i);
				if (s.getItem() instanceof ItemBlock) {
					block = i;
					break;
				}
			}
			if (block == -1 && timerHelper.hasReached(1000)) {
				timerHelper.reset();
				return;
			}
			if (timerHelper.hasReached(climbTicks.getNumberValue() * 55)) {
				try {
					if (block != -1 && mc.objectMouseOver != null && mc.objectMouseOver.hitVec != null
							&& mc.objectMouseOver.getBlockPos() != null
							&& mc.objectMouseOver.sideHit != null) {
						mc.player.connection.sendPacket(new CPacketHeldItemChange(block));
						float prevPitch = mc.player.rotationPitch;
						mc.player.rotationPitch = -60;
						mc.entityRenderer.getMouseOver(1);
						Vec3d facing = mc.objectMouseOver.hitVec;
						BlockPos stack = mc.objectMouseOver.getBlockPos();
						float f = (float) (facing.xCoord - (double) stack.getX());
						float f1 = (float) (facing.yCoord - (double) stack.getY());
						float f2 = (float) (facing.zCoord - (double) stack.getZ());
						mc.player.connection.sendPacket(new CPacketEntityAction(mc.player,
								CPacketEntityAction.Action.START_SNEAKING));
						if (mc.world.getBlockState(new BlockPos(mc.player).add(0, 2, 0))
								.getBlock() == Blocks.AIR) {
							mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(stack,
									mc.objectMouseOver.sideHit, EnumHand.MAIN_HAND, f, f1, f2));
						} else {
							mc.player.connection.sendPacket(new CPacketPlayerDigging(
									Action.START_DESTROY_BLOCK, stack, mc.objectMouseOver.sideHit));
							mc.player.connection.sendPacket(new CPacketPlayerDigging(
									Action.STOP_DESTROY_BLOCK, stack, mc.objectMouseOver.sideHit));
						}
						mc.player.connection.sendPacket(new CPacketEntityAction(mc.player,
								CPacketEntityAction.Action.STOP_SNEAKING));
						mc.player.rotationPitch = prevPitch;
						mc.entityRenderer.getMouseOver(1);
						mc.player.connection
								.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
						mc.player.onGround = true;
						mc.player.isCollidedVertically = true;
						mc.player.isCollidedHorizontally = true;
						mc.player.isAirBorne = true;
						mc.player.jump();
						timerHelper.reset();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
         */
    }
}
