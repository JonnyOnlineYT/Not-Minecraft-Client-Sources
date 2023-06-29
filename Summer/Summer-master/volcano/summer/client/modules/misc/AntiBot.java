package volcano.summer.client.modules.misc;

import java.util.ArrayList;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.potion.PotionEffect;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketRecieve;
import volcano.summer.client.events.EventPreMotionUpdate;

public class AntiBot extends Module {

	public static ArrayList<EntityPlayer> removedBots = new ArrayList();

	public AntiBot() {
		super("AntiBot", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	private boolean isInTablist(EntityPlayer player) {
		NetworkPlayerInfo var48 = new NetworkPlayerInfo(player.getGameProfile());
		var48.getResponseTime();
		if (mc.isSingleplayer()) {
			return true;
		}
		for (Object o : mc.getNetHandler().func_175106_d()) {
			NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
			if (playerInfo.func_178845_a().getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isBot(EntityPlayer player) {
		return removedBots.contains(player);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventPreMotionUpdate) {
			for (Object o : mc.theWorld.loadedEntityList) {
				Entity entity = (Entity) o;
				if ((entity instanceof EntityPlayer)) {
					EntityPlayer maybeBot = (EntityPlayer) entity;
					if ((maybeBot != mc.thePlayer) && ((!isInTablist(maybeBot)) || (maybeBot.capabilities.isFlying)
							|| (maybeBot.capabilities.allowFlying) || (maybeBot.capabilities.disableDamage)
							|| ((maybeBot.isInvisible()) && (!maybeBot.onGround))
							|| (!maybeBot.isPotionApplicable(new PotionEffect(1, 0)))
							|| ((maybeBot.noClip) && (maybeBot.fallDistance > 1.0F) && (!maybeBot.onGround))
							|| ((maybeBot.noClip) && (maybeBot.isInvisible())) || (maybeBot.hasCustomName())
							|| ((maybeBot.isInvisible()) && (maybeBot.isAirBorne))
							|| ((maybeBot.isInvisible()) && (maybeBot.fallDistance > 1.0F))
							|| ((maybeBot.getCurrentEquippedItem() == null) && (maybeBot.isInvisible())
									&& (!maybeBot.isCollidedVertically))
							|| (maybeBot.capabilities.getFlySpeed() > mc.thePlayer.capabilities.getFlySpeed()))) {
						if ((maybeBot.getCurrentEquippedItem() == null) && (maybeBot.isInvisible())
								&& (!maybeBot.isCollidedVertically)) {

							System.out.println("Successfully removed a bot. (Collide&Items)");
						}
						if ((maybeBot.noClip) && (maybeBot.fallDistance > 1.0F) && (!maybeBot.onGround)) {

							System.out.println("Successfully removed a bot. (NoClip1)");
						}
						if ((maybeBot.noClip) && (maybeBot.isInvisible())) {

							System.out.println("Successfully removed a bot. (NoClip2)");
						}
						if (maybeBot.capabilities.isFlying) {

							System.out.println("Successfully removed a bot. (Flying1)");
						}
						if (maybeBot.capabilities.allowFlying) {

							System.out.println("Successfully removed a bot. (Flying2)");
						}
						if (maybeBot.capabilities.getFlySpeed() > mc.thePlayer.capabilities.getFlySpeed()) {

							System.out.println("Successfully removed a bot. (Flying3)");
						}
						if (maybeBot.capabilities.disableDamage) {

							System.out.println("Successfully removed a bot. (DamageDisable)");
						}
						if ((maybeBot.isInvisible()) && (!maybeBot.onGround)) {

							System.out.println("Successfully removed a bot. (Invs&Ground)");
						}
						if ((maybeBot.isInvisible()) && (maybeBot.isAirBorne)) {

							System.out.println("Successfully removed a bot. (Invs&Air)");
						}
						if ((maybeBot.isInvisible()) && (maybeBot.fallDistance > 1.0F)) {

							System.out.println("Successfully removed a bot. (Invs&Fall)");
						}
						if (!maybeBot.isPotionApplicable(new PotionEffect(1, 0))) {

							System.out.println("Successfully removed a bot. (Potion)");
						}
						if (maybeBot.hasCustomName()) {
							System.out.println("Successfully removed a bot. (Name)");
						}
						if (!isInTablist(maybeBot)) {
							System.out.println("Successfully removed a bot. (Tablist)");
						}
						mc.theWorld.removeEntity(maybeBot);
						removedBots.add(maybeBot);
					}
				}
			}
		}
		if (event instanceof EventPacketRecieve) {
			if ((((EventPacketRecieve) event).getPacket() instanceof S0EPacketSpawnObject)) {
				S0EPacketSpawnObject packet = (S0EPacketSpawnObject) ((EventPacketRecieve) event).getPacket();
				((EventPacketRecieve) event).setCancelled(true);
			}
		}
	}
}