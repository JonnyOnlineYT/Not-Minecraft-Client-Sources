package volcano.summer.client.modules.combat;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketRecieve;
import volcano.summer.client.value.ModeValue;

public class AntiBot extends Module {

	public static ModeValue antibotMode;
	EntityPlayer ep;

	public AntiBot() {
		super("AntiBot", 0, Category.COMBAT);
		antibotMode = new ModeValue("BotMode", "Mode", "WatchDog", new String[] { "WatchDog", "Mineplex", "Cubecraft" },
				this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {
		if (AntiBot.antibotMode.getStringValue().equalsIgnoreCase("WatchDog")) {
			setDisplayName("WatchDog");
		} else if (AntiBot.antibotMode.getStringValue().equalsIgnoreCase("Mineplex")) {
			setDisplayName("Mineplex");
		} else if (AntiBot.antibotMode.getStringValue().equalsIgnoreCase("Cubecraft")) {
			setDisplayName("Cubecraft");
		} else {
			setDisplayName("");
		}
		if (event instanceof EventPacketRecieve) {
			if (!this.antibotMode.getStringValue().equalsIgnoreCase("mineplex"))
				return;
			if (!(((EventPacketRecieve) event).getPacket() instanceof S0CPacketSpawnPlayer))
				return;
			S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) ((EventPacketRecieve) event).getPacket();
			double entX = packet.func_148942_f() / 32;
			double entY = packet.func_148949_g() / 32;
			double entZ = packet.func_148946_h() / 32;
			double posX = mc.thePlayer.posX;
			double posY = mc.thePlayer.posY;
			double posZ = mc.thePlayer.posZ;
			double var7 = posX - entX;
			double var8 = posY - entY;
			double var9 = posZ - entZ;
			float distance = MathHelper.sqrt_double(var7 * var7 + var8 * var8 + var9 * var9);
			if (distance <= 17.0f && entY > mc.thePlayer.posY + 1.0 && mc.thePlayer.posX != entX
					&& mc.thePlayer.posY != entY && mc.thePlayer.posZ != entZ && mc.thePlayer.ticksExisted != 0
					&& !mc.theWorld.getSpawnPoint()
							.equals(new BlockPos(entX, mc.theWorld.getSpawnPoint().getY(), entZ))) {
				((EventPacketRecieve) event).setCancelled(true);
				Summer.tellPlayer("Removed bot.");
			}
		}
	}
}
