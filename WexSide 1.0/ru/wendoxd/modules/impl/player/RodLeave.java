package ru.wendoxd.modules.impl.player;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.modules.impl.visuals.Crosshair;
import ru.wendoxd.utils.misc.ChatUtils;

public class RodLeave extends Module {
	public static int entityID = -1;
	public int prevId, ticks;

	public void initSettings() {
	}

	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			if (entityID != -1) {
				if (getHand() == null) {
					ChatUtils.addChatMessage(ChatFormatting.GRAY + "У вас нет удочки в руках.");
					Crosshair.setEnabled(false);
					entityID = -1;
					return;
				}
				if (mc.player.world.getEntityByID(entityID) != null) {
					EntityPlayer tp = (EntityPlayer) mc.player.world.getEntityByID(entityID);
					Crosshair.setGPS(tp.posX, tp.posZ);
					if (mc.player.getDistanceToEntityXZ(mc.world.getEntityByID(entityID)) < 8
							&& mc.player.fishEntity != null && mc.player.fishEntity.caughtEntity == mc.player) {
						mc.playerController.processRightClick(mc.player, mc.world, getHand());
						for (int i = 0; i < 3; i++) {
							mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
						}
						mc.player.setPosition(tp.posX, tp.posY, tp.posZ);
						ChatUtils.addChatMessage(ChatFormatting.GRAY + "Вы телепортированы к игроку.");
						entityID = -1;
						Crosshair.setEnabled(false);
					}
				} else {
					entityID = -1;
					ChatUtils.addChatMessage(ChatFormatting.GRAY + "Цель пропала.");
					Crosshair.setEnabled(false);
				}
			}
		}
	}

	public static EnumHand getHand() {
		if (mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.FISHING_ROD) {
			return EnumHand.MAIN_HAND;
		}
		if (mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() == Items.FISHING_ROD) {
			return EnumHand.OFF_HAND;
		}
		return null;
	}
}
