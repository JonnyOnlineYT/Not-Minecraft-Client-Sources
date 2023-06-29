package volcano.summer.client.modules.combat;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPreMotionUpdate;

public class AutoSword extends Module {

	public AutoSword() {
		super("AutoSword", 0, Category.COMBAT);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	protected void swap(int slot, int hotbarNum) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
	}

	private float getSharpnessLevel(ItemStack stack) {
		if (!(stack.getItem() instanceof ItemSword)) {
			return 0.0F;
		}
		return EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack) * 1.25F;
	}

	@Override
	public void onEvent(Event event) {
		if ((event instanceof EventPreMotionUpdate) && (mc.currentScreen == null)
				&& (mc.thePlayer.getCurrentEquippedItem() != null)
				&& ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))) {
			ItemSword currentSword = (ItemSword) mc.thePlayer.getCurrentEquippedItem().getItem();
			for (int i = 0; i < 45; i++) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					Item item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
					if ((item instanceof ItemSword)) {
						float itemDamage = getSharpnessLevel(mc.thePlayer.inventoryContainer.getSlot(i).getStack())
								+ ((ItemSword) item).func_150931_i();
						float currentDamage = getSharpnessLevel(mc.thePlayer.getCurrentEquippedItem())
								+ currentSword.func_150931_i();
						if (itemDamage > currentDamage) {
							swap(i, mc.thePlayer.inventory.currentItem);
						}
					}
				}
			}
		}
	}
}
