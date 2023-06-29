package ru.wendoxd.modules.impl.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventEntitySync;
import ru.wendoxd.events.impl.player.EventMove;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.player.MoveUtils;

public class Jesus extends Module {
	private Frame jesus_frame = new Frame("Jesus");
	public static CheckBox jesus = new CheckBox("Jesus").markArrayList("Jesus");
	public static SelectBox mode = new SelectBox("Mode", new String[] { "New Matrix", "Old Matrix" },
			() -> jesus.isEnabled(true));
	public static Slider speed = new Slider("Speed", 1, 0.1, 10, 0.1, () -> jesus.isEnabled(true));
	public static boolean jesusTick;
	public static boolean swap;
	public static int ticks;

	@Override
	protected void initSettings() {
		jesus.markSetting("Jesus");
		jesus_frame.register(jesus, mode, speed);
		MenuAPI.movement.register(jesus_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
		}
		if (event instanceof EventMove && jesus.isEnabled(false)) {
			EventMove move = (EventMove) event;
			BlockPos pos = new BlockPos(move.from());
			Block block = mc.world.getBlockState(pos).getBlock();
			if (mode.get() == 0) {
				if (block instanceof BlockLiquid) {
					Speed.waterTicks = 10;
					move.motion().yCoord = 0.19;
					move.motion().xCoord = 0;
					move.motion().zCoord = 0;
				} else if (mc.world.getBlockState(new BlockPos(move.to())).getBlock() instanceof BlockLiquid) {
					BlockLiquid.overrideAABB = true;
					Speed.waterTicks = 10;
					boolean reallyWorld = false;
					try {
						reallyWorld = mode.get() == 0;
					} catch (Exception e) {
						e.printStackTrace();
					}
					float f;
					MoveUtils.setSpeed(
							f = ((ticks) % (reallyWorld ? 3 : 2) == 0 ? speed.getFloatValue() - 0.01f : 0.14f));
					if (mc.player.posY % 1 == 0) {
						move.motion().yCoord = 0;
					}
					jesusTick = true;
					swap = true;
					move.motion().xCoord = mc.player.motionX;
					move.motion().zCoord = mc.player.motionZ;
					move.collisionOffset().yCoord = -.7;
					mc.player.motionY = 0;
					mc.player.motionZ = 0;
					mc.player.motionX = 0;
				}
			}
		}
		if (event instanceof EventEntitySync && jesus.isEnabled(false)) {
			EventEntitySync ees = (EventEntitySync) event;
			if (swap) {
				boolean reallyWorld = false;
				try {
					reallyWorld = mode.get() == 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (reallyWorld) {
					ees.setPosY(ees.getPosY() + (ticks % 3 == 0 ? -0.02 : ticks % 3 == 1 ? 0.02 : 0.03));
				} else {
					ees.setPosY(ees.getPosY() + ticks % 2 == 0 ? -0.02 : 0.02);
				}
				ticks++;
				if (ees.getPosY() % 1 == 0) {
					ees.setPosY(ees.getPosY() - 0.02);
				}
				ees.setOnGround(false);
			}
			swap = false;
		}
	}
}
