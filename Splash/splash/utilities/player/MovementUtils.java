package splash.utilities.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import splash.Splash;
import splash.client.events.player.EventMove;
import splash.client.modules.combat.Aura;
import splash.client.modules.combat.TargetStrafe;

public class MovementUtils {
	static Minecraft mc = Minecraft.getMinecraft();

	public static double fallPacket() {
		double i;
		for (i = mc.thePlayer.posY; i > getGroundLevel(); i -= 8.0) {
			if (i < getGroundLevel()) {
				i = getGroundLevel();
			}
			mc.thePlayer.sendQueue.addToSendQueue(
					new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
		}
		return i;
	}

	public static void ascendPacket() {
		for (double i = getGroundLevel(); i < mc.thePlayer.posY; i += 8.0) {
			if (i > mc.thePlayer.posY) {
				i = mc.thePlayer.posY;
			}
			mc.thePlayer.sendQueue.addToSendQueue(
					new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
		}
	}

	public static double getGroundLevel() {
		for (int i = (int) Math.round(mc.thePlayer.posY); i > 0; --i) {
			final AxisAlignedBB box = mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
			box.minY = i - 1;
			box.maxY = i;
			if (!isColliding(box) || !(box.minY <= mc.thePlayer.posY)) {
				continue;
			}
			return i;
		}
		return 0.0;
	}

	public static boolean isColliding(final AxisAlignedBB box) {
		return mc.theWorld.checkBlockCollision(box);
	}
}