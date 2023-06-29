package ru.wendoxd.modules.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.misc.EventMouseTick;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.SelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.InventoryUtils;

public class Auto extends Module {
	public static Frame autoTotemFrame = new Frame("AutoTotem"), middleClickPearlFrame = new Frame("MiddleClickPearl");
	public static CheckBox autoTotem = new CheckBox("AutoTotem").markSetting("AutoTotem").markArrayList("AutoTotem");
	public static SelectBox type = new SelectBox("Type", new String[] { "Matrix", "Other" },
			() -> autoTotem.isEnabled(true));
	public static CheckBox checkCrystal = new CheckBox("Check Crystal", () -> autoTotem.isEnabled(true))
			.markSetting("Check Crystal");
	public static CheckBox fall = new CheckBox("Fall", () -> autoTotem.isEnabled(true)).markSetting("Fall");
	public static Slider fallDistance = new Slider("Distance", 1, 4, 40, 0, () -> autoTotem.isEnabled(true));
	public static CheckBox obs = new CheckBox("Obsidian", () -> autoTotem.isEnabled(true)).markSetting("Fall");
	public static Slider obsdist = new Slider("Distance", 1, 4, 40, 0, () -> autoTotem.isEnabled(true));
	public static Slider crystaldst = new Slider("Distance", 1, 0, 20, 0.5,
			() -> autoTotem.isEnabled(true) && checkCrystal.isEnabled(true));
	public static CheckBox checkTnt = new CheckBox("Check TNT", () -> autoTotem.isEnabled(true))
			.markSetting("Check TNT");
	public static Slider tntdst = new Slider("Distance", 1, 0, 20, 0.5,
			() -> autoTotem.isEnabled(true) && checkTnt.isEnabled(true));
	public static CheckBox middleClickPearl = new CheckBox("MiddleClickPearl").markSetting("MCP");
	public static Slider health = new Slider("Health", 1, 0, 20, 0.5, () -> autoTotem.isEnabled(true));
	public static CheckBox absorptionHP = (CheckBox) new CheckBox("+ Absorption", () -> autoTotem.isEnabled(true))
			.markDescription("Ваше хп для автототема\nбудет просчитываться с учетом золотых сердечек");
	public static int swapBack = -1;
	public static long delay;

	@Override
	protected void initSettings() {
		crystaldst.modifyPath("AUTOTOTEM3434");
		MenuAPI.combat.register(autoTotemFrame);
		MenuAPI.player.register(middleClickPearlFrame);
		autoTotemFrame.register(autoTotem, type, health, absorptionHP, obs, obsdist, fall, fallDistance, checkCrystal,
				crystaldst, checkTnt, tntdst);
		middleClickPearlFrame.register(middleClickPearl);
		fallDistance.modifyPath("passivka");
		crystaldst.modifyPath("dawldlwa");
		tntdst.modifyPath("daowodwa");
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventMouseTick && middleClickPearl.isEnabled(false)) {
			this.middleClickPearl(event);
		}
		if (event instanceof EventUpdate) {
			if (autoTotem.isEnabled(false)) {
				if (type.get() == 1) {
					float hp = mc.player.getHealth();
					if (absorptionHP.isEnabled(false))
						hp += mc.player.getAbsorptionAmount();
					int totem = getSlotIDFromItem(Items.field_190929_cY);
					int stackSizeHand = mc.player.getHeldItemOffhand().stackSize;
					boolean handNotNull = !(mc.player.getHeldItemOffhand().getItem() instanceof ItemAir);
					boolean handTotem = mc.player.getHeldItemOffhand().getItem() == Items.field_190929_cY;
					boolean totemCheck = ((hp <= health.getDoubleValue() || checkTNT() || checkCrystal() || checkFall()
							|| checkObsidian()) && autoTotem.isEnabled(false));
					if (System.currentTimeMillis() < delay) {
						return;
					}
					if (totemCheck) {
						if (totem >= 0) {
							if (!handTotem) {
								mc.playerController.windowClick(0, totem, 1, ClickType.PICKUP, mc.player);
								mc.playerController.windowClick(0, 45, 1, ClickType.PICKUP, mc.player);
								if (handNotNull) {
									mc.playerController.windowClick(0, totem, 0, ClickType.PICKUP, mc.player);
									if (swapBack == -1)
										swapBack = totem;
								}
								delay = System.currentTimeMillis() + 300;
							}
						}
						return;
					}
					if (swapBack >= 0) {
						mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, mc.player);
						mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
						if (handNotNull)
							mc.playerController.windowClick(0, swapBack, 0, ClickType.PICKUP, mc.player);
						swapBack = -1;
						delay = System.currentTimeMillis() + 300;
						return;
					}
				} else {
					int totemSlot = getTotemSlot();
					if (totemSlot < 9 && totemSlot != -1) {
						totemSlot += 36;
					}
					float hp = mc.player.getHealth();
					if (absorptionHP.isEnabled(false)) {
						hp += mc.player.getAbsorptionAmount();
					}
					int prevCurrentItem = mc.player.inventory.currentItem;
					int currentItem = findNearestCurrentItem();
					ItemStack prevHeldItem = mc.player.getHeldItemOffhand();
					boolean totemCheck = health.getFloatValue() >= hp || checkCrystal() || checkFall() || checkTNT()
							|| checkObsidian();
					boolean handNotNull = !(mc.player.getHeldItemOffhand().getItem() instanceof ItemAir);
					boolean totemInHand = mc.player.getHeldItemOffhand().getItem() == Items.field_190929_cY;
					if (totemCheck) {
						if (totemSlot >= 0 && !totemInHand) {
							mc.playerController.windowClick(0, totemSlot, currentItem, ClickType.SWAP, mc.player);
							mc.getConnection().sendPacket(new CPacketHeldItemChange(currentItem));
							mc.player.inventory.currentItem = currentItem;
							ItemStack itemstack = mc.player.getHeldItem(EnumHand.OFF_HAND);
							mc.player.setHeldItem(EnumHand.OFF_HAND, mc.player.getHeldItem(EnumHand.MAIN_HAND));
							mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
							mc.getConnection().sendPacket(
									new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
							mc.getConnection().sendPacket(new CPacketHeldItemChange(prevCurrentItem));
							mc.player.inventory.currentItem = prevCurrentItem;
							mc.playerController.windowClick(0, totemSlot, currentItem, ClickType.SWAP, mc.player);
							if (swapBack == -1)
								swapBack = totemSlot;
							return;
						}
						if (totemInHand) {
							return;
						}
					}
					if (swapBack >= 0) {
						mc.getConnection().sendPacket(new CPacketHeldItemChange(currentItem));
						mc.player.inventory.currentItem = currentItem;
						ItemStack itemstack = mc.player.getHeldItem(EnumHand.OFF_HAND);
						mc.player.setHeldItem(EnumHand.OFF_HAND, mc.player.getHeldItem(EnumHand.MAIN_HAND));
						mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
						mc.getConnection().sendPacket(
								new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
						mc.playerController.windowClick(0, swapBack, currentItem, ClickType.SWAP, mc.player);
						mc.getConnection().sendPacket(
								new CPacketPlayerDigging(Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
						itemstack = mc.player.getHeldItem(EnumHand.OFF_HAND);
						mc.player.setHeldItem(EnumHand.OFF_HAND, mc.player.getHeldItem(EnumHand.MAIN_HAND));
						mc.player.setHeldItem(EnumHand.MAIN_HAND, itemstack);
						mc.getConnection().sendPacket(new CPacketHeldItemChange(prevCurrentItem));
						mc.player.inventory.currentItem = prevCurrentItem;
						swapBack = -1;
					}
				}
			}
		}
	}

	public static int getSlotIDFromItem(Item item) {
		int slot = -1;
		for (int i = 0; i < 36; i++) {
			ItemStack s = mc.player.inventory.getStackInSlot(i);
			if (s.getItem() == item) {
				slot = i;
				break;
			}
		}
		if (slot < 9 && slot != -1) {
			slot = slot + 36;
		}
		return slot;
	}

	public static int getTotemSlot() {
		for (int i = 0; i < 36; i++) {
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (stack.getItem() == Items.field_190929_cY) {
				return i;
			}
		}
		return -1;
	}

	public static int findNearestCurrentItem() {
		int currentItem = mc.player.inventory.currentItem;
		if (currentItem == 8) {
			return 7;
		}
		if (currentItem == 0) {
			return 1;
		}
		return currentItem - 1;
	}

	public void middleClickPearl(Event event) {
		if (((EventMouseTick) event).getButton() == 2
				&& mc.player.getCooldownTracker().getCooldown(Items.ENDER_PEARL, 1) == 0
				&& InventoryUtils.getPearls() >= 0) {
			mc.player.connection.sendPacket(new CPacketHeldItemChange(InventoryUtils.getPearls()));
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
			mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
		}
	}

	private boolean checkCrystal() {
		if (!checkCrystal.isEnabled(false)) {
			return false;
		}
		for (Entity entity : mc.world.loadedEntityList) {
			if (entity instanceof EntityEnderCrystal
					&& mc.player.getDistanceToEntity(entity) <= crystaldst.getDoubleValue()) {
				return true;
			}
		}
		return false;
	}

	private boolean checkTNT() {
		if (!checkTnt.isEnabled(false)) {
			return false;
		}
		for (Entity entity : mc.world.loadedEntityList) {
			if (entity instanceof EntityTNTPrimed && mc.player.getDistanceToEntity(entity) <= tntdst.getDoubleValue()) {
				return true;
			}
			if (entity instanceof EntityMinecartTNT
					&& mc.player.getDistanceToEntity(entity) <= tntdst.getDoubleValue()) {
				return true;
			}
		}
		return false;
	}

	private boolean IsValidBlockPos(BlockPos pos) {
		IBlockState state = mc.world.getBlockState(pos);
		return state.getBlock() instanceof BlockObsidian;
	}

	private boolean checkObsidian() {
		if (!obs.isEnabled(false)) {
			return false;
		}
		BlockPos pos = getSphere(getPlayerPosLocal(), obsdist.getFloatValue(), 6, false, true, 0).stream()
				.filter(this::IsValidBlockPos)
				.min(Comparator.comparing(blockPos -> getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
		return pos != null;
	}

	public static double getDistanceOfEntityToBlock(final Entity entity, final BlockPos blockPos) {
		return getDistance(entity.posX, entity.posY, entity.posZ, blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public static double getDistance(final double n, final double n2, final double n3, final double n4, final double n5,
			final double n6) {
		final double n7 = n - n4;
		final double n8 = n2 - n5;
		final double n9 = n3 - n6;
		return MathHelper.sqrt(n7 * n7 + n8 * n8 + n9 * n9);
	}

	public static BlockPos getPlayerPosLocal() {
		if (mc.player == null) {
			return BlockPos.ORIGIN;
		}
		return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
	}

	public static List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b,
			final boolean b2, final int n3) {
		final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
		final int x = blockPos.getX();
		final int y = blockPos.getY();
		final int z = blockPos.getZ();
		for (int n4 = x - (int) n; n4 <= x + n; ++n4) {
			for (int n5 = z - (int) n; n5 <= z + n; ++n5) {
				for (int n6 = b2 ? (y - (int) n) : y; n6 < (b2 ? (y + n) : ((float) (y + n2))); ++n6) {
					final double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0);
					if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
						list.add(new BlockPos(n4, n6 + n3, n5));
					}
				}
			}
		}
		return list;
	}

	private boolean checkFall() {
		if (!fall.isEnabled(false)) {
			return false;
		}
		return mc.player.fallDistance > fallDistance.getDoubleValue();
	}
}
