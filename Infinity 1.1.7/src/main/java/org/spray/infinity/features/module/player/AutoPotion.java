package org.spray.infinity.features.module.player;

import java.util.HashMap;
import java.util.Map.Entry;

import org.spray.infinity.event.MotionEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.InvUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

@ModuleInfo(category = Category.PLAYER, desc = "Automatically throws certain potions", key = -2, name = "AutoPotion", visible = true)
public class AutoPotion extends Module {

	HashMap<StatusEffect, Setting> potions;

	public AutoPotion() {
		this.potions = new HashMap<StatusEffect, Setting>();

		this.potions.put(StatusEffects.STRENGTH, new Setting(this, "Strength Potion", true));
		this.potions.put(StatusEffects.SPEED, new Setting(this, "Speed Potion", true));
		this.potions.put(StatusEffects.FIRE_RESISTANCE, new Setting(this, "Fire Resistance", true));
		this.potions.put(StatusEffects.JUMP_BOOST, new Setting(this, "Jump Boost", false));

		this.addSettings(this.potions.values());
	}

	private Setting delay = new Setting(this, "Delay", 5.2D, 3D, 20D);

	private int timer;

	private int next;

	@Override
	public void onDisable() {

	}

	@EventTarget
	public void onMotionTick(MotionEvent event) {
		if (!Helper.getPlayer().isOnGround())
			return;

		int i = 1;
		for (Entry<StatusEffect, Setting> entry : this.potions.entrySet()) {
			StatusEffect effect = (StatusEffect) entry.getKey();
			Setting sett = (Setting) entry.getValue();
			int slot = InvUtil.findPotionHotbar(effect, true);

			if (sett.isToggle() && !EntityUtil.checkActivePotion(effect))
				next = i;
			if (timer > 0) {
				timer--;
				return;
			}

			if (sett.isToggle() && slot != -2 && !EntityUtil.checkActivePotion(effect)) {
				if (next == i) {
					event.setPitch(90);
					baff(event, slot);
					next += 1;
				}
			}
			i++;
		}
	}

	private void baff(MotionEvent event, int slot) {
		int preSlot = Helper.getPlayer().getInventory().selectedSlot;

		Helper.getPlayer().getInventory().selectedSlot = slot;

		Helper.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(Helper.getPlayer().getYaw(), 90F,
				Helper.getPlayer().isOnGround()));
		Helper.MC.interactionManager.interactItem(Helper.getPlayer(), Helper.getWorld(), Hand.MAIN_HAND);

		timer = (int) delay.getCurrentValueDouble();

		Helper.getPlayer().getInventory().selectedSlot = preSlot;
		Helper.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(Helper.getPlayer().getYaw(),
				Helper.getPlayer().getPitch(), Helper.getPlayer().isOnGround()));
	}

}
