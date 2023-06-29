package org.spray.infinity.features.module.combat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.spray.infinity.event.PacketEvent;
import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.main.Infinity;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.PacketUtil;
import org.spray.infinity.utils.RotationUtil;
import org.spray.infinity.utils.entity.EntityUtil;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Formatting;

@ModuleInfo(category = Category.COMBAT, desc = "Ignore / Remove npc by created anti-cheat ", key = -2, name = "AntiBot", visible = true)
public class AntiBot extends Module {

	public Setting mode = new Setting(this, "Mode", "Matrix 6.1.1",
			new ArrayList<>(Arrays.asList("Matrix 6.1.1", "Custom", "Need Hit")));

	// Need Hit
	private Setting ignoreFriends = new Setting(this, "Ignore Friends", true)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Need Hit"));

	// Custom
	private Setting invisible = new Setting(this, "Invisible", false)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Custom"));
	private Setting entityID = new Setting(this, "Entity ID", true)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Custom"));
	private Setting zeroHealth = new Setting(this, "Zero Health", false)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Custom"));
	private Setting remove = new Setting(this, "Remove Bots", false)
			.setVisible(() -> mode.getCurrentMode().equalsIgnoreCase("Custom"));

	private List<Integer> bots = new ArrayList<>();
	private List<Integer> needHit = new ArrayList<>();

	@Override
	public void onDisable() {
		needHit.clear();
		bots.clear();
	}

	@Override
	public void onUpdate() {
		setSuffix(mode.getCurrentMode());

		if (Helper.getPlayer() == null || Helper.getWorld() == null)
			return;

		for (Object object : Helper.getWorld().getEntities()) {
			if (object instanceof LivingEntity) {
				LivingEntity entity = (LivingEntity) object;
				if (entity instanceof PlayerEntity && !(entity instanceof ArmorStandEntity)
						&& entity != Helper.getPlayer()) {
					PlayerEntity bot = (PlayerEntity) entity;
					if (mode.getCurrentMode().equalsIgnoreCase("Custom")) {
						if (containsBot(bot)) {
							message(bot.getName().getString());
							bots.add(bot.getId());

							if (remove.isToggle())
								Helper.getWorld().removeEntity(bot.getId(), Entity.RemovalReason.DISCARDED);
						}
					} else if (mode.getCurrentMode().equalsIgnoreCase("Matrix 6.1.1")) {
						boolean botContains = RotationUtil.isInFOV(bot, Helper.getPlayer(), 60)
								&& Helper.getPlayer().distanceTo(bot) > 6.1 && bot.canSee(Helper.getPlayer());
						boolean speedAnalysis = bot.getStatusEffect(StatusEffects.SPEED) == null
								&& bot.getStatusEffect(StatusEffects.JUMP_BOOST) == null
								&& bot.getStatusEffect(StatusEffects.LEVITATION) == null && !bot.isTouchingWater()
								&& bot.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA
								&& !bot.hasVehicle() && EntityUtil.getSpeedBPS(bot) > 13 && !bot.isOnGround()
								&& !bot.velocityDirty;

						if (botContains && speedAnalysis) {
							Helper.getWorld().removeEntity(entity.getId(), Entity.RemovalReason.DISCARDED);
							message(bot.getName().getString());
						}
					}
				}
			}
		}
	}

	@EventTarget
	public void onPacket(PacketEvent event) {
		if (event.getType().equals(EventType.SEND)) {
			if (!mode.getCurrentMode().equalsIgnoreCase("Need Hit"))
				return;

			if (event.getPacket() instanceof PlayerInteractEntityC2SPacket) {
				PlayerInteractEntityC2SPacket pa = (PlayerInteractEntityC2SPacket) event.getPacket();
				if (PacketUtil.getEntity(pa).equals(Helper.getPlayer())
						|| !(PacketUtil.getEntity(pa) instanceof PlayerEntity))
					return;

				if (!needHit.contains(PacketUtil.getEntity(pa).getId())) {
					if (ignoreFriends.isToggle()
							&& Infinity.getFriend().contains(PacketUtil.getEntity(pa).getName().getString()))
						return;

					needHit.add(PacketUtil.getEntity(pa).getId());
					Helper.message(Formatting.GRAY + "[AntiBot] " + Formatting.WHITE
							+ PacketUtil.getEntity(pa).getName().getString() + Formatting.GRAY + " added to targets");
				}
			}
		}
	}

	public boolean isBot(Entity entity) {
		if (!isEnabled())
			return false;

		if (bots.contains(entity.getId()))
			return true;

		return false;
	}

	private boolean containsBot(PlayerEntity bot) {
		if (invisible.isToggle() && bot.isInvisible() && !bots.contains(bot.getId()))
			return true;

		if (entityID.isToggle() && bot.getId() >= 1000000000 && !bots.contains(bot.getId()))
			return true;

		if (zeroHealth.isToggle() && bot.getHealth() <= 0 && !bots.contains(bot.getId()))
			return true;

		return false;
	}

	public boolean isHitted(Entity entity) {
		return needHit.contains(entity.getId());
	}

	private void message(String bot) {
		if (remove.isToggle() && mode.getCurrentMode().equalsIgnoreCase("Custom"))
			Helper.message(
					Formatting.GRAY + "[AntiBot] " + Formatting.WHITE + "Removed a bot: " + Formatting.GREEN + bot);
		else
			Helper.message(
					Formatting.GRAY + "[AntiBot] " + Formatting.WHITE + "Bot detected: " + Formatting.GREEN + bot);
	}

}
