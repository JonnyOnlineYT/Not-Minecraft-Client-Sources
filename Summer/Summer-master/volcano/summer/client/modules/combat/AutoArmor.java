package volcano.summer.client.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class AutoArmor extends Module {

	public static Value<Double> delay;
	public static AutoArmor auto;
	public Value<Boolean> openinv = new Value<Boolean>("OpenInv", "openinv", false, this);
	TimerUtil timer = new TimerUtil();

	public AutoArmor() {
		super("AutoArmor", 0, Category.COMBAT);
		delay = new ClampedValue<Double>("ArmorDelay", "armordelay", 120.0, 0.0, 500.0, this);
		auto = this;
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotionUpdate) {
			try {
				if (this.mc.currentScreen == null && this.openinv.getValue()) {
					return;
				}
				if (timer.isDelayComplete(this.delay.getValue().floatValue())
						&& !Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode
						&& ((Minecraft.getMinecraft().currentScreen == null
								|| Minecraft.getMinecraft().currentScreen instanceof GuiInventory))) {
					Minecraft.getMinecraft().playerController.syncCurrentPlayItem();
					for (byte b = 5; b <= 8; ++b) {
						if (this.equipArmor(b)) {
							timer.setLastMs();
							break;
						}
					}
				}

				Minecraft.getMinecraft().playerController.updateController();
			} catch (Exception e) {

			}
		}
	}

	private boolean equipArmor(byte b) {
		if (mc.theWorld != null && mc.thePlayer != null) {
			if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack() != null)
				if (!(Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack()
						.getItem() instanceof ItemArmor))
					return false;

			double currentProt = -1;
			byte slot = -1;
			ItemArmor current = null;
			if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack() != null
					&& Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack()
							.getItem() instanceof ItemArmor) {
				current = (ItemArmor) Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack()
						.getItem();
				currentProt = current.damageReduceAmount
						+ EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId,
								Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack())
						+ getsortdouble((ItemArmor) Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b)
								.getStack().getItem());
			}

			for (byte isNull = 9; isNull <= 44; ++isNull) {
				ItemStack stack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(isNull).getStack();
				if (stack != null && stack.getItem() instanceof ItemArmor) {
					ItemArmor armor = (ItemArmor) stack.getItem();
					double armorProt = armor.damageReduceAmount
							+ EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack)
							+ getsortdouble(armor);
					if (this.checkArmor(armor, b) && (current == null || currentProt < armorProt)) {
						currentProt = armorProt;
						current = armor;
						slot = isNull;
					}
				}
			}

			final byte slotfinal = slot;
			if (slot != -1) {
				boolean var9 = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(b).getStack() == null;
				Thread t = new Thread(() -> {
					try {
						if (!var9) {
							if (mc.thePlayer.inventory.getFirstEmptyStack() != -1) {
								this.clickSlot(b, 0, true);
							}
							Thread.currentThread().sleep(10);
						}
						this.clickSlot(slotfinal, 0, true);
						Thread.currentThread().sleep(10);

						if (!var9) {
							this.clickSlot(slotfinal, 0, true);
							Thread.currentThread().sleep(10);
						}
						Minecraft.getMinecraft().playerController.updateController();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});

				t.run();
				return true;
			} else {
				Minecraft.getMinecraft().playerController.updateController();
				return false;
			}
		}
		return false;
	}

	private boolean checkArmor(ItemArmor item, byte b) {
		return b == 5 && this.isHelmet(item) || b == 6 && this.isChest(item) || b == 7 && this.isLeggings(item)
				|| b == 8 && this.isBoots(item);
	}

	private boolean isHelmet(ItemArmor item) {
		return item.getUnlocalizedName().startsWith("item.helmet");
	}

	private boolean isChest(ItemArmor item) {
		return item.getUnlocalizedName().startsWith("item.chestplate");
	}

	private boolean isLeggings(ItemArmor item) {
		return item.getUnlocalizedName().startsWith("item.leggings");
	}

	private boolean isBoots(ItemArmor item) {
		return item.getUnlocalizedName().startsWith("item.boots");
	}

	private double getsortdouble(ItemArmor item) {
		if (item.getUnlocalizedName().contains("iron") || item.getUnlocalizedName().contains("diamond")) {
			return 0;
		}
		double sortvalue = 0;
		if (item.getUnlocalizedName().contains("gold")) {
			sortvalue = 0;
		}
		if (item.getUnlocalizedName().contains("leather")) {
			sortvalue = 0.2;
		}
		if (item.getUnlocalizedName().contains("chain")) {
			sortvalue = 0.4;
		}

		return sortvalue;
	}

	private void clickSlot(int slot, int mouseButton, boolean shiftClick) {
		Minecraft.getMinecraft().playerController.windowClick(
				Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, mouseButton, shiftClick ? 1 : 0,
				Minecraft.getMinecraft().thePlayer);
		Minecraft.getMinecraft().playerController.updateController();
	}

}
