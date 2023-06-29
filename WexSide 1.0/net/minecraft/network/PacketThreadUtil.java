package net.minecraft.network;

import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil {
	public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packetIn, final T processor,
			IThreadListener scheduler) throws ThreadQuickExitException {
		if (!scheduler.isCallingFromMinecraftThread()) {
			scheduler.addScheduledTask(new Runnable() {
				public void run() {
					packetIn.processPacket(processor);
				}
			});
			throw ThreadQuickExitException.INSTANCE;
		}
	}
}
