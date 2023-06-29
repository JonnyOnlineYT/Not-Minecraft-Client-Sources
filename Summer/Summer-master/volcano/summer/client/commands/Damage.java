package volcano.summer.client.commands;

import net.minecraft.network.play.client.C03PacketPlayer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;

public class Damage extends Command {

	public Damage() {
		super("damage", "<hearts>");
	}

	@Override
	public void run(String message) {
		Summer.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY + 1.0D, mc.thePlayer.posZ, mc.thePlayer.onGround));
		Summer.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY + 0.5D, mc.thePlayer.posZ, mc.thePlayer.onGround));
		Summer.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
				mc.thePlayer.posY - (2.5F + Float.parseFloat(message.split(" ")[1])), mc.thePlayer.posZ,
				mc.thePlayer.onGround));
	}
}