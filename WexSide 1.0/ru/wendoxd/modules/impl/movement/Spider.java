package ru.wendoxd.modules.impl.movement;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventEntitySync;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.ChatUtils;
import ru.wendoxd.utils.misc.TimerUtils;

public class Spider extends Module {

	private Frame spider_frame = new Frame("Spider");
	public static final CheckBox spider = new CheckBox("Spider").markArrayList("Spider");
	private final SelectBox mode = new SelectBox("Mode", new String[] { "Vanilla", "Disabler", "Old Matrix" },
			() -> spider.isEnabled(true));
	private final Slider delay = new Slider("Delay", 1, 1, 10, 0.3, () -> spider.isEnabled(true));
	private final TimerUtils timerUtils = new TimerUtils();

	@Override
	protected void initSettings() {
		spider.markSetting("Spider");
		spider_frame.register(spider, mode, delay);
		MenuAPI.movement.register(spider_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntitySync && spider.isEnabled(false)) {
			if (mc.playerController.getCurrentGameType() == GameType.ADVENTURE && mode.get() == 1
					&& timerUtils.hasReached(1000)) {
				ChatUtils.addChatMessage("Данный режим игры [ADVENTURE] не поддерживается с данным Spider`а.");
				timerUtils.reset();
			}
			if (mc.player.isCollidedHorizontally) {
				if (mode.get() == 2) {
					if (timerUtils.hasReached(delay.getFloatValue() * 55)) {
						((EventEntitySync) event).setOnGround(true);
						mc.player.onGround = true;
						mc.player.isCollidedVertically = true;
						mc.player.isCollidedHorizontally = true;
						mc.player.isAirBorne = true;
						mc.player.jump();
						timerUtils.reset();
					}
				} else if (mode.get() == 0) {
					if (timerUtils.hasReached(delay.getFloatValue() * 55)) {
						mc.player.onGround = true;
						mc.player.jump();
						timerUtils.reset();
					}
				} else if (mode.get() == 1) {
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
						if (block == -1 && timerUtils.hasReached(1000)) {
							ChatUtils.addChatMessage("Вам нужен любой блок в хотбаре для этого Spider`а.");
							timerUtils.reset();
							return;
						}
						if (timerUtils.hasReached(delay.getFloatValue() * 55)) {
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
									((EventEntitySync) event).setOnGround(true);
									mc.player.onGround = true;
									mc.player.isCollidedVertically = true;
									mc.player.isCollidedHorizontally = true;
									mc.player.isAirBorne = true;
									mc.player.jump();
									timerUtils.reset();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
	}
}
