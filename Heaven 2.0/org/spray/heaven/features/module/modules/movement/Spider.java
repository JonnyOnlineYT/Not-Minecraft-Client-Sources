package org.spray.heaven.features.module.modules.movement;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.BlockUtil;
import org.spray.heaven.util.MovementUtil;
import org.spray.heaven.util.RotationUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(name = "Spider", category = Category.MOVEMENT, visible = true, key = Keyboard.KEY_NONE)
public class Spider extends Module {

	private Setting mode = register(new Setting("Mode", "Sunrise", Arrays.asList("Sunrise","Matrix","Custom")));
	private Setting delay = register(
			new Setting("Delay", 2, 0, 15).setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Custom")));
	private Setting dropBlocks = register(
			new Setting("Drop Blocks", false).setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Custom")));

	private int ticks = 0, fticks;

	@Override
	public void onDisable() {
		ticks = 0;
		fticks = 0;
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		boolean start = false;
		if (MovementUtil.isMoving() && mc.player.isCollidedHorizontally) {
			int tick = mode.getCurrentMode().equalsIgnoreCase("Sunrise") ? 2 : (int) delay.getValue();
//			if (ticks > 30) {
//				fticks++;
//			}
//			
//			if (fticks > 5) {
//				fticks = 0;
//			}
//			if (fticks < 5 && fticks > 0)
//				return;

			if (ticks > 5) {
				ticks = 0;
				return;
			}

			if (mc.player.ticksExisted % tick == 0) {

				if (mode.getCurrentMode().equalsIgnoreCase("Sunrise")) {
					int find = -2;
					for (int i = 0; i <= 8; i++)
						if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)
							find = i;

					if (find == -2)
						return;

					BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ);
					EnumFacing side = BlockUtil.getPlaceableSide(pos);
					if (side != null) {
						Wrapper.sendPacket(new CPacketHeldItemChange(find));

						BlockPos neighbour = new BlockPos(mc.player.posX, mc.player.posY + 2, mc.player.posZ)
								.offset(side);
						EnumFacing opposite = side.getOpposite();

						Vec3d hitVec = new Vec3d(neighbour).addVector(0.5, 0.5, 0.5)
								.add(new Vec3d(opposite.getDirectionVec()).scale(0.5));

						Vec2f rotation = getRotationTo(hitVec);
						event.setRotation(rotation.x, rotation.y, false);

						float x = (float) (hitVec.xCoord - (double) neighbour.getX());
						float y = (float) (hitVec.yCoord - (double) neighbour.getY());
						float z = (float) (hitVec.zCoord - (double) neighbour.getZ());

//						mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec,
//								EnumHand.MAIN_HAND);
						Wrapper.sendPacket(
								new CPacketPlayerTryUseItemOnBlock(neighbour, opposite, EnumHand.MAIN_HAND, x, y, z));
						Wrapper.sendPacket(
								new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
						if (BlockUtil.getBlock(new BlockPos(mc.player).add(0, 2, 0)) == Blocks.AIR) {
//							Wrapper.sendPacket(new CPacketPlayerTryUseItemOnBlock(neighbour, opposite,
//									EnumHand.MAIN_HAND, x, y, z));
						} else {
							Wrapper.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
									neighbour, opposite));
							Wrapper.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,
									neighbour, opposite));
						}
						Wrapper.sendPacket(
								new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
					}
				}

				mc.player.onGround = true;
//				mc.player.isCollidedVertically = true;
				mc.player.isAirBorne = true;
				mc.player.jump();

				ticks++;

				if (mode.getCurrentMode().equalsIgnoreCase("Sunrise"))
					Wrapper.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));

				if (dropBlocks.isToggle())
					for (int i = 9; i < 45; i++) {
						if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
							if (mc.player.inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock) {
								mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0,
										ClickType.THROW, mc.player);
								mc.player.jump();
								break;
							}
						}
					}
			}
		}
		if (mode.getCurrentMode().equalsIgnoreCase("Matrix")){
			if(mc.player.ticksExisted % 2 == 0) {
				mc.player.setSprinting(false);
				mc.player.fallDistance = 0;
				mc.player.onGround = true;
				mc.player.jump();
			}
		}
	}


	private Vec2f getRotationTo(Vec3d posTo) {
		EntityPlayerSP player = mc.player;
		return player != null ? getRotationTo(player.getPositionEyes(1.0f), posTo) : Vec2f.ZERO;
	}

	private Vec2f getRotationTo(Vec3d posFrom, Vec3d posTo) {
		return getRotationFromVec(posTo.subtract(posFrom));
	}

	private Vec2f getRotationFromVec(Vec3d vec) {
		double lengthXZ = Math.hypot(vec.xCoord, vec.zCoord);
		double yaw = RotationUtil.normalizeAngle(
				RotationUtil.getFixedRotation((float) (Math.toDegrees(Math.atan2(vec.zCoord, vec.xCoord)) - 90.0)));
		double pitch = RotationUtil.normalizeAngle(
				RotationUtil.getFixedRotation((float) (Math.toDegrees(-Math.atan2(vec.yCoord, lengthXZ)))));

		return new Vec2f((float) yaw, (float) pitch);
	}
}
