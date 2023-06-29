package org.spray.infinity.features.module.player;

import java.util.ArrayList;
import java.util.Arrays;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.event.RotationEvent;
import org.spray.infinity.event.TickEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.MoveUtil;
import org.spray.infinity.utils.RotationUtil;
import org.spray.infinity.utils.Timer;
import org.spray.infinity.utils.block.BlockUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(category = Category.PLAYER, desc = "Placing block", key = -2, name = "Scaffold", visible = true)
public class Scaffold extends Module {

	private Setting mode = new Setting(this, "Mode", "Normal", new ArrayList<>(Arrays.asList("Normal", "Safe")));

	private Setting maxDelay = new Setting(this, "Max Delay", 200D, 0D, 500D);
	private Setting minDelay = new Setting(this, "Min Delay", 200D, 0D, 500D);

	private Setting blockTake = new Setting(this, "Block Take", "Switch",
			new ArrayList<>(Arrays.asList("Pick", "Switch")));
	private Setting eagle = new Setting(this, "Eagle", false);
	public Setting safeWalk = new Setting(this, "SafeWalk", true);

	private Setting speed = new Setting(this, "Rotation Speed", 140D, 0D, 180D);

	public Setting airPlace = new Setting(this, "Air Place", false);

	private Timer timer = new Timer();

	private PlaceData pData;

	private float[] look;

	// target pos
	public static BlockPos pos;

	@Override
	public void onEnable() {
		timer.reset();
	}

	@Override
	public void onDisable() {
		if (Helper.MC.options.keySneak.isPressed())
			Helper.MC.options.keySneak.setPressed(false);

		pos = null;
	}

	@Override
	public void onUpdate() {

		// eagle
		BlockPos eaglePos = new BlockPos(Helper.getPlayer().getX(), Helper.getPlayer().getY() - 1,
				Helper.getPlayer().getZ());

		if (eagle.isToggle()) {
			if (Helper.MC.world.getBlockState(eaglePos).getBlock() != Blocks.AIR) {
				Helper.MC.options.keySneak.setPressed(false);
			} else {
				Helper.MC.options.keySneak.setPressed(true);
			}
		}

		if (mode.getCurrentMode().equalsIgnoreCase("Safe")) {
			MoveUtil.strafe(MoveUtil.calcMoveYaw(), 0.06);
		}
	}

	@EventTarget
	public void onTick(TickEvent event) {
		if (pos == null) {
			return;
		}

		look = rotation(pos);
	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {

		pos = new BlockPos(Helper.getPlayer().getX(), Helper.getPlayer().getY() - 1, Helper.getPlayer().getZ());

		PlaceData data = getPlaceData(pos);

		if (pData != null) {
			BlockPos lookPos = pData.pos;
			Vec3d vec = new Vec3d(lookPos.getX(), lookPos.getY(), lookPos.getZ());
			float[] alwaysL = RotationUtil.lookAtVecPos(vec);

			alwaysL[0] = RotationUtil.limitAngleChange(event.getYaw(), alwaysL[0],
					(float) speed.getCurrentValueDouble());
			alwaysL[1] = RotationUtil.limitAngleChange(event.getYaw(), alwaysL[1],
					(float) speed.getCurrentValueDouble());

			event.setRotation(alwaysL[0], alwaysL[1], false);
			Helper.getPlayer().bodyYaw = alwaysL[0];
			Helper.getPlayer().headYaw = alwaysL[0];
		}

		if (Helper.MC.world.getBlockState(pos).getBlock() == Blocks.AIR) {

			if (pos == null) {
				timer.reset();
				return;
			}

			// rotation
			pData = data;
			event.setRotation(look[0], look[1], false);

			// slot calculate
			int blockSlot = -2;
			for (int i = 0; i < 9; i++) {
				ItemStack stack = Helper.getPlayer().getInventory().getStack(i);
				if (isBlock(stack.getItem())) {
					blockSlot = i;
				}
			}

			// placing
			if (blockSlot != -2) {

				if (timer.hasReached(
						Math.random() * (maxDelay.getCurrentValueDouble() - minDelay.getCurrentValueDouble())
								+ minDelay.getCurrentValueDouble())) {

					if (!safe())
						return;

					if (Helper.MC.options.keyJump.isPressed() && Helper.getPlayer().fallDistance == 0) {
						return;
					}

					int selectedSlot = Helper.getPlayer().getInventory().selectedSlot;
					Helper.getPlayer().getInventory().selectedSlot = blockSlot;

					if (EntityUtil.placeBlock(Hand.MAIN_HAND, pos, airPlace.isToggle())) {
						pos = null;
						if (blockTake.getCurrentMode().equalsIgnoreCase("Switch"))
							Helper.getPlayer().getInventory().selectedSlot = selectedSlot;
					}
					timer.reset();
				}
			}
		} else {
			timer.reset();
		}
	}

	@EventTarget
	public void onRotation(RotationEvent event) {
		if (mode.getCurrentMode().equalsIgnoreCase("Safe")) {

			if (pos == null)
				return;

			event.setYaw(look[0]);
			event.setPitch(look[1]);
			event.cancel();
		}
	}

	public float[] rotation(BlockPos pos) {
		Vec3d eyesPos = new Vec3d(Helper.getPlayer().getX(),
				Helper.getPlayer().getY() + Helper.getPlayer().getEyeHeight(Helper.getPlayer().getPose()),
				Helper.getPlayer().getZ());

		Vec3d hitVec = null;
		BlockPos neighbor = null;
		Direction side2 = null;
		for (Direction side : Direction.values()) {
			neighbor = pos.offset(side);
			side2 = side.getOpposite();

			if (Helper.getWorld().getBlockState(neighbor).isAir()) {
				neighbor = null;
				side2 = null;
				continue;
			}

			hitVec = new Vec3d(neighbor.getX(), neighbor.getY(), neighbor.getZ()).add(0.5, 0.5, 0.5)
					.add(new Vec3d(side2.getUnitVector()).multiply(0.5));
			break;
		}

		// Air place if no neighbour was found
		if (airPlace.isToggle() && hitVec == null)
			hitVec = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
		if (neighbor == null)
			neighbor = pos;
		if (side2 == null)
			side2 = Direction.UP;

		// place block
		double diffX = hitVec.x - eyesPos.x;
		double diffY = hitVec.y - eyesPos.y;
		double diffZ = hitVec.z - eyesPos.z;

		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

		return new float[] { yaw, pitch };
	}

	private PlaceData getPlaceData(BlockPos pos) {
		if (BlockUtil.canBeClicked(pos.add(0, -1, 0)))
			return new PlaceData(pos.add(0, -1, 0), Direction.UP);
		if (BlockUtil.canBeClicked(pos.add(0, 0, 1)))
			return new PlaceData(pos.add(0, 0, 1), Direction.NORTH);
		if (BlockUtil.canBeClicked(pos.add(-1, 0, 0)))
			return new PlaceData(pos.add(-1, 0, 0), Direction.EAST);
		if (BlockUtil.canBeClicked(pos.add(0, 0, -1)))
			return new PlaceData(pos.add(0, 0, -1), Direction.SOUTH);
		if (BlockUtil.canBeClicked(pos.add(1, 0, 0)))
			return new PlaceData(pos.add(1, 0, 0), Direction.WEST);

		return null;
	}

	private boolean safe() {
		if (mode.getCurrentMode().equalsIgnoreCase("Safe")) {
			if (Helper.MC.crosshairTarget.getType() == HitResult.Type.BLOCK) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private boolean isBlock(Item item) {
		if (item instanceof BlockItem) {
			BlockItem itemBlock = (BlockItem) item;
			Block block = itemBlock.getBlock();
			return block != null;
		}
		return false;
	}

	public class PlaceData {

		public BlockPos pos;
		public Direction side;

		public PlaceData(BlockPos pos, Direction side) {
			this.pos = pos;
			this.side = side;
		}
	}

}
