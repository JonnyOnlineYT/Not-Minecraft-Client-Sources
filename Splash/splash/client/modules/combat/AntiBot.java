package splash.client.modules.combat;

import java.util.ArrayList;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.MathHelper;
import splash.Splash;
import splash.Splash.GAMEMODE;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.player.EventPlayerUpdate;
import splash.utilities.system.ClientLogger;

public class AntiBot extends Module {

	public static ArrayList<EntityPlayer> bots = new ArrayList<>();
	private ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.WATCHDOG, this);
	public BooleanValue<Boolean> removeValue = new BooleanValue<>("Remove World", true, this);
	public BooleanValue<Boolean> botNotificationValue = new BooleanValue<>("Notification", true, this);

	public AntiBot() {
		super("Antibot", "Removes bots", ModuleCategory.COMBAT);
	}

	public enum Mode {
		WATCHDOG, GWEN, REFLEX
	}

	@Override
	public void onEnable() {
		super.onEnable();
		bots.clear();
	}

	@Override
	public void onDisable() {
		super.onDisable();
		bots.clear();
	}

	private boolean isInTablist (EntityLivingBase player){
		if (mc.isSingleplayer()) {
			return true;
		}
		for (Object o : mc.getNetHandler().getPlayerInfoMap()) {
			NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
			if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}

	@Collect
	public void onPacket(EventPacketReceive eventPacketReceive) {
		if (mode.getValue().equals(Mode.WATCHDOG)) {
			if (mc.thePlayer != null && onServer("hypixel") && Splash.getInstance().getGameMode().equals(GAMEMODE.SKYWARS)) {
				if (eventPacketReceive.getReceivedPacket() instanceof S18PacketEntityTeleport) {
					S18PacketEntityTeleport packet = (S18PacketEntityTeleport) eventPacketReceive.getReceivedPacket();
					Entity entity = mc.theWorld.getEntityByID(packet.getEntityId());
					if (entity != null && entity instanceof EntityPlayer) {
						if (entity.isInvisible()) {
							bots.add((EntityPlayer) entity);
						}
					}
				}
			}
		}
	}

	@Collect
	public void onUpdate(EventPlayerUpdate event) {
		for (Entity entity : mc.theWorld.loadedEntityList) {
			if ((entity instanceof EntityPlayer)) {
				EntityPlayer player = (EntityPlayer)entity; 

				if (mode.getValue().equals(Mode.REFLEX)) {
					if (player.isInvisible() && player.isEntityInsideOpaqueBlock()) {
						bots.add(player);
					}
				}
				if (mode.getValue().equals(Mode.WATCHDOG)) {
					if (player.getName().startsWith("§c") && !isInTablist(player) && player.isInvisible()) {
						if (!bots.contains(player)) {
							bots.add(player);
						}
					}
					if (player.isInvisible() && !bots.contains(player)) {
						float xDist = (float) (mc.thePlayer.posX - player.posX);
						float zDist = (float) (mc.thePlayer.posZ - player.posZ);
						double horizontalReaach = MathHelper.sqrt_float(xDist * xDist + zDist * zDist);
						if (horizontalReaach < .6) {
							double vert = mc.thePlayer.posY - player.posY;
							if (vert <= 5 && vert > 1) {
								if (mc.thePlayer.ticksExisted % 5 == 0) {
									bots.add(player);
								}
							}
						}
					}
					if (bots.contains(player) && player.hurtTime > 0 || player.fallDistance > 0) {
						bots.remove(player);
					}
				} else if (mode.getValue().equals(Mode.GWEN)) {
					if (player.getHealth() != Float.NaN && player != mc.thePlayer) {
						mc.theWorld.removeEntity(player);
					}
				}
			}
			

		}
		if (!bots.isEmpty() && mc.thePlayer.ticksExisted % 20 == 0) {
			for (int i = 0; i < bots.size(); i++) {
				if (bots.contains(bots.get(i))) {
					if (!mc.theWorld.playerEntities.contains(bots.get(i))) bots.remove(bots.get(i));
				}	
			}	
			if (removeValue.getValue()) {
				for (EntityPlayer entityPlayer : bots) {
					if (!entityPlayer.getName().equalsIgnoreCase(mc.thePlayer.getName())) 	mc.theWorld.removeEntity(entityPlayer);
				}
			}
		}
	}
}