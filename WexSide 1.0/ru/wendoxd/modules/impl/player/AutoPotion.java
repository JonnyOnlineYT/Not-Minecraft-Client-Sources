package ru.wendoxd.modules.impl.player;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.combat.Aura;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.misc.TimerUtils;

public class AutoPotion extends Module {

	private Frame autopotion_frame = new Frame("AutoPotion");
	private final CheckBox autopotion = new CheckBox("AutoPotion").markArrayList("AutoPotion");
	public MultiSelectBox potions = new MultiSelectBox("Potions",
			new String[] { "Speed", "Strength", "Fireres", "Heal" }, () -> autopotion.isEnabled(true));
	public Slider h = new Slider("Health", 1, 0, 20, 0.5, () -> potions.get(3));
	public TimerUtils timerHelper = new TimerUtils();
	public static long lastBuffTime;

	@Override
	protected void initSettings() {
		autopotion.markSetting("AutoPotion");
		autopotion_frame.register(autopotion, potions, h);
		MenuAPI.player.register(autopotion_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && autopotion.isEnabled(false)) {
			if (Aura.target != null && mc.player.getCooledAttackStrength(1) > 0.5f)
				return;
			event.addCallback(() -> {
				boolean throwed = (!mc.player.isPotionActive(MobEffects.SPEED) && isPotionOnHotBar(Potions.SPEED)
						&& potions.get(0))
						|| (!mc.player.isPotionActive(MobEffects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH)
								&& potions.get(1))
						|| (!mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES)
								&& potions.get(2))
						|| mc.player.getHealth() + mc.player.getAbsorptionAmount() < h.getFloatValue() && potions.get(3)
								&& isPotionOnHotBar(Potions.HEAL);
				if (mc.player.ticksExisted > 10 && throwed && timerHelper.hasReached(1000)) {
					mc.player.forceUpdatePlayerServerPosition(mc.player.posX, mc.player.posY, mc.player.posZ,
							mc.player.rotationYaw, 90, mc.player.onGround);
					if (!mc.player.isPotionActive(MobEffects.SPEED) && isPotionOnHotBar(Potions.SPEED)
							&& potions.get(0)) {
						throwPotion(Potions.SPEED);
					}
					if (!mc.player.isPotionActive(MobEffects.STRENGTH) && isPotionOnHotBar(Potions.STRENGTH)
							&& potions.get(1)) {
						throwPotion(Potions.STRENGTH);
					}
					if (!mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && isPotionOnHotBar(Potions.FIRERES)
							&& potions.get(2)) {
						throwPotion(Potions.FIRERES);
					}
					if (mc.player.getHealth() + mc.player.getAbsorptionAmount() < h.getFloatValue() && potions.get(3)
							&& isPotionOnHotBar(Potions.HEAL)) {
						throwPotion(Potions.HEAL);
					}
					mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
					timerHelper.reset();
					lastBuffTime = System.currentTimeMillis();
				}
			});
		}
	}

	public void throwPotion(Potions potion) {
		int slot = getPotionSlot(potion);
		mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
		mc.playerController.updateController();
		mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
		mc.playerController.updateController();
	}

	public static int getPotionSlot(AutoPotion.Potions potion) {
		for (int i = 0; i < 9; ++i) {
			if (isStackPotion(mc.player.inventory.getStackInSlot(i), potion)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isPotionOnHotBar(AutoPotion.Potions potions) {
		return getPotionSlot(potions) != -1;
	}

	public static boolean isStackPotion(ItemStack stack, AutoPotion.Potions potion) {
		if (stack == null)
			return false;

		Item item = stack.getItem();

		if (item == Items.SPLASH_POTION) {
			int id = 0;

			switch (potion) {
			case STRENGTH: {
				id = 5;
				break;
			}
			case SPEED: {
				id = 1;
				break;
			}
			case FIRERES: {
				id = 12;
				break;
			}
			case HEAL: {
				id = 6;
			}
			}

			for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
				if (effect.getPotion() == Potion.getPotionById(id)) {
					return true;
				}
			}
		}
		return false;
	}

	public enum Potions {
		STRENGTH, SPEED, FIRERES, HEAL
	}
}