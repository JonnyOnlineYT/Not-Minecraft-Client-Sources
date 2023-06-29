package org.spray.heaven.features.module.modules.player;

import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.MotionEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.util.Timer;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;

@ModuleInfo(name = "AutoPotion", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AutoPotion extends Module {

	private final Setting hotberOnly = register(new Setting("Hotbar Only", true));
	private final Setting delay = register(new Setting("Delay", 100, 10, 1000));
	private final Setting onlyGround = register(new Setting("Only Ground", true));
	private final Setting strenght = register(new Setting("Strenght", true));
	private final Setting speed = register(new Setting("Speed", true));
	private final Setting fire_resistance = register(new Setting("Fire Resistance", true));

	private float yaw, pitch, random;
	public Timer timerHelper = new Timer();

	@EventTarget
	public void onPre(MotionEvent event) {
		if (timerHelper.hasReached(delay.getValue())) {
			if (strenght.isToggle()) {
				if (!mc.player.isPotionActive(MobEffects.STRENGTH)) {
					throwPotion(5);
				}
			}
			if (speed.isToggle()) {
				if (!mc.player.isPotionActive(MobEffects.SPEED)) {
					throwPotion(1);
				}
			}
			if (fire_resistance.isToggle()) {
				if (!mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
					throwPotion(12);
				}
			}
			timerHelper.reset();
		}

	}

	private int getPotionIndexInv(int id) {
		for (int i = 0; i < 45; i++) {
			int index = i < 9 ? i + 36 : i;
			for (PotionEffect potion : PotionUtils.getEffectsFromStack(mc.player.inventory.getStackInSlot(index))) {
				if (potion.getPotion() == Potion.getPotionById(id)
						&& mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION)
					return index;
			}
		}
		return -1;
	}

	private int getPotionIndexHb(int id) {
		for (int i = 0; i < 9; i++) {
			for (PotionEffect potion : PotionUtils.getEffectsFromStack(mc.player.inventory.getStackInSlot(i))) {
				if (potion.getPotion() == Potion.getPotionById(id)
						&& mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION)
					return i;
			}
		}
		return -1;
	}

	private void throwPotion(int potionId) {
		if (onlyGround.isToggle() && !mc.player.onGround) {
			return;
		}

		int index = -1;
		if (getPotionIndexHb(potionId) == -1) {
			index = getPotionIndexInv(potionId);
		} else {
			index = getPotionIndexHb(potionId);
		}

		if (index == -1)
			return;
		if (hotberOnly.isToggle() && index > 9) {
			mc.playerController.windowClick(0, index, 0, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, mc.player);
			throwPot(index);
			mc.playerController.windowClick(0, 44, 0, ClickType.PICKUP, mc.player);
		} else {
			throwPot(index);
		}

	}

	private void throwPot(int index) {
		mc.player.connection.sendPacket(new CPacketHeldItemChange(index < 9 ? index : 8));
		mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90, mc.player.onGround));
		pitch = 90;
		mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
		mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
	}

	enum Potions {
		STRENGTH, SPEED, FIRERES
	}
}