package volcano.summer.client.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventBlockBB;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.events.EventTick;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class Phase extends Module {

	public static ModeValue phasemode;
	public static Value<Double> Distance;
	private int resetNext;

	public Phase() {
		super("Phase", 0, Category.PLAYER);
		phasemode = new ModeValue("PMode", "Mode", "NCP", new String[] { "NCP", "Vanilla" }, this);
		Distance = new ClampedValue<Double>("Distance", "distance", 0.2, 0.1, 1.0, this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	public static boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper
				.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
			for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper
					.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; y++) {
				for (int z = MathHelper
						.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper
								.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
					Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					AxisAlignedBB boundingBox;
					if ((block != null) && (!(block instanceof BlockAir))
							&& ((boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld,
									new BlockPos(x, y, z),
									Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)))) != null)
							&& (Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String getDirection() {
		return Minecraft.getMinecraft().func_175606_aa().func_174811_aO().name();
	}

	@Override
	public void onEvent(Event event) {
		if (phasemode.getStringValue().equalsIgnoreCase("NCP")) {
			setDisplayName("NCP");
			if (event instanceof EventBlockBB) {
				if (((mc.thePlayer.isCollidedHorizontally) || (Phase.isInsideBlock()))
						&& (((EventBlockBB) event).getBoundingBox() != null)
						&& (((EventBlockBB) event).getBoundingBox().maxY > mc.thePlayer.boundingBox.minY)) {
					((EventBlockBB) event).setBoundingBox(null);
				}
			}
		}
		if ((event instanceof EventPreMotionUpdate) && (mc.thePlayer.isCollidedHorizontally)
				&& (!Phase.isInsideBlock())) {
			double distance = this.Distance.getValue().floatValue();
			double x = MovementInput.moveForward * distance * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F))
					+ MovementInput.moveStrafe * distance * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
			double z = MovementInput.moveForward * distance * Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F))
					- MovementInput.moveStrafe * distance * Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
			mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
			mc.thePlayer.boundingBox.offsetAndUpdate(x, 0.0D, z);
		}
		if (phasemode.getStringValue().equalsIgnoreCase("Vanilla")) {
			setDisplayName("Vanilla");
			if (event instanceof EventBlockBB) {
				if (((Phase.isInsideBlock()) && (!mc.gameSettings.keyBindSprint.getIsKeyPressed())
						&& (((EventBlockBB) event).getY() > mc.thePlayer.getEntityBoundingBox().minY - 0.4D)
						&& (((EventBlockBB) event).getY() < mc.thePlayer.getEntityBoundingBox().maxY + 1.0D))
						|| ((Phase.isInsideBlock()) && (mc.gameSettings.keyBindSneak.getIsKeyPressed()))) {
					((EventBlockBB) event).setBoundingBox(null);
				}
			}
		}
		if (event instanceof EventPreMotionUpdate) {
			if ((mc.thePlayer.isCollidedHorizontally) && (!Phase.isInsideBlock())) {
				if (getDirection().equalsIgnoreCase("EAST")) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							mc.thePlayer.posX + 0.5D, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
					mc.thePlayer.setPosition(mc.thePlayer.posX + 1.0D, mc.thePlayer.posY, mc.thePlayer.posZ);
				} else if (getDirection().equalsIgnoreCase("WEST")) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
							mc.thePlayer.posX - 0.5D, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
					mc.thePlayer.setPosition(mc.thePlayer.posX - 1.0D, mc.thePlayer.posY, mc.thePlayer.posZ);
				} else if (getDirection().equalsIgnoreCase("NORTH")) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ - 0.5D, mc.thePlayer.onGround));
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ - 1.0D);
				} else if (getDirection().equalsIgnoreCase("SOUTH")) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ + 0.5D, mc.thePlayer.onGround));
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + 1.0D);
				}
			}
		}
		if (mc.theWorld != null) {
			if (event instanceof EventTick) {
				if ((mc.thePlayer.isCollidedHorizontally) && (mc.gameSettings.keyBindSneak.getIsKeyPressed())) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY, mc.thePlayer.posZ, true));
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ, true));
				}
			}
		}
	}
}
