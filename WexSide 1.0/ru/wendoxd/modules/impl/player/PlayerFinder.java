package ru.wendoxd.modules.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.packet.EventReceivePacket;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.utils.misc.ChatUtils;

public class PlayerFinder extends Module {
	private static Task task;

	public void onEvent(Event event) {
		if (event instanceof EventUpdate && task != null) {
			if (mc.player.hurtTime > 0) {
				task = null;
				return;
			}
			for (int i = 0; i < task.perTick; i++) {
				if (task.getZ() > task.getTargetZ()) {
					task.setX(task.getX() + 16 * task.stepRange);
					task.setZ(task.getStartZ());
				} else {
					task.setZ(task.getZ() + 16 * task.stepRange);
				}
				if (task.getX() > task.getTargetX()) {
					ChatUtils.addChatMessage(ChatFormatting.GRAY + "[ " + ChatFormatting.RED + "nocom"
							+ ChatFormatting.GRAY + " ] Мир просканирован на данной дистанции...");
					task = null;
					return;
				}
				mc.player.connection
						.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
								new BlockPos(task.getX(), 1, task.getZ()), EnumFacing.DOWN));
			}
		}
		if (event instanceof EventReceivePacket && task != null) {
			EventReceivePacket erp = (EventReceivePacket) event;
			if (erp.getPacket() instanceof SPacketBlockChange) {
				SPacketBlockChange spbc = (SPacketBlockChange) erp.getPacket();
				BlockPos pos = spbc.getBlockPosition();
				Vec3d vec = mc.player.getPositionVector();
				Vec3d vec3d = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
				if (vec3d.distanceTo(vec) > 64) {
					ChatUtils.addChatMessage(ChatFormatting.GRAY + "[ " + ChatFormatting.RED + "nocom"
							+ ChatFormatting.GRAY + " ] замечен игрок на координатах " + ChatFormatting.RED + "[ "
							+ pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ChatFormatting.GRAY + " ] | "
							+ ChatFormatting.RED + (int) vec3d.distanceTo(vec) + ChatFormatting.GRAY + "m |");
				}
			}
		}
	}

	public static void setTask(Task task) {
		PlayerFinder.task = task;
	}

	public static class Task {
		private Vec3d position;
		private double x, z, targetX, targetZ, startX, startZ;
		private int stepRange, perTick;

		public Task(Vec3d position, double dst, int stepRange, int perTick) {
			this.position = position;
			this.targetX = this.position.xCoord + dst;
			this.targetZ = this.position.zCoord + dst;
			this.x = this.position.xCoord - dst;
			this.z = this.position.zCoord - dst;
			this.startX = this.x;
			this.startZ = this.z;
			this.stepRange = stepRange;
			this.perTick = perTick;
		}

		public double getX() {
			return this.x;
		}

		public double getZ() {
			return this.z;
		}

		public double getTargetX() {
			return this.targetX;
		}

		public double getTargetZ() {
			return this.targetZ;
		}

		public double getStartX() {
			return this.startX;
		}

		public double getStartZ() {
			return this.startZ;
		}

		public void setX(double x) {
			this.x = x;
		}

		public void setZ(double z) {
			this.z = z;
		}

		public Vec3d getStartPosition() {
			return this.position;
		}
	}
}
