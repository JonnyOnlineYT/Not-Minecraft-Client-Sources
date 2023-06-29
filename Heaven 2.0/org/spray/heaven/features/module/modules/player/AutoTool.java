package org.spray.heaven.features.module.modules.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

@ModuleInfo(name = "AutoTool", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AutoTool extends Module {

	private final Setting swapBack = register(new Setting("Swap Back", true));
	private final Setting saveItem = register(new Setting("Save Item", true));
	public final Setting silentSwitch = register(new Setting("Packet Switch", true));

	public int itemIndex;
	private boolean swap;
	private long swapDelay;
	private ItemStack swapedItem = null;
	private List<Integer> lastItem = new ArrayList<>();

	@Override
	public void onUpdate() {
		if (mc.player.isCreative() || mc.player.isSpectator())
			return;

		Block hoverBlock = null;
		if (mc.objectMouseOver.getBlockPos() == null)
			return;
		hoverBlock = mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
		List<Integer> bestItem = new ArrayList<>();
		for (int i = 0; i < 9; i++) {
			if (hoverBlock == null)
				break;
			if (!(mc.player.inventory.getStackInSlot(i).getMaxDamage()
					- mc.player.inventory.getStackInSlot(i).getItemDamage() > 1) && saveItem.isToggle())
				continue;
			if (mc.player.getDigSpeed(mc.world.getBlockState(mc.objectMouseOver.getBlockPos()),
					mc.player.inventory.getStackInSlot(i)) > 1)
				bestItem.add(i);

		}

		bestItem.sort(Comparator
				.comparingDouble(x -> -mc.player.getDigSpeed(mc.world.getBlockState(mc.objectMouseOver.getBlockPos()),
						mc.player.inventory.getStackInSlot(x))));

		if (!bestItem.isEmpty() && mc.gameSettings.keyBindAttack.pressed) {
			if (mc.player.inventory.getCurrentItem() != swapedItem) {
				lastItem.add(mc.player.inventory.currentItem);
				if (silentSwitch.isToggle())
					mc.player.connection.sendPacket(new CPacketHeldItemChange(bestItem.get(0)));
				else
					mc.player.inventory.currentItem = bestItem.get(0);

				itemIndex = bestItem.get(0);
				swap = true;
			}
			swapDelay = System.currentTimeMillis();
		} else if (swap && !lastItem.isEmpty() && System.currentTimeMillis() >= swapDelay + 300
				&& swapBack.isToggle()) {

			if (silentSwitch.isToggle())
				mc.player.connection.sendPacket(new CPacketHeldItemChange(lastItem.get(0)));
			else
				mc.player.inventory.currentItem = lastItem.get(0);

			itemIndex = lastItem.get(0);
			lastItem.clear();
			swap = false;
		}
	}
}