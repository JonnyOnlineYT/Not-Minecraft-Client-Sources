package volcano.summer.client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;

public class Hclip extends Command {

	public Hclip() {
		super("Hclip", "<blocks>");
	}

	@Override
	public void run(String message) {
		double posMod = Double.parseDouble(message.split(" ")[1]);
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer.func_174811_aO() == EnumFacing.SOUTH) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + posMod);
		}
		if (mc.thePlayer.func_174811_aO() == EnumFacing.WEST) {
			mc.thePlayer.setPosition(mc.thePlayer.posX + -posMod, mc.thePlayer.posY, mc.thePlayer.posZ);
		}
		if (mc.thePlayer.func_174811_aO() == EnumFacing.EAST) {
			mc.thePlayer.setPosition(mc.thePlayer.posX + posMod, mc.thePlayer.posY, mc.thePlayer.posZ);
		}
		if (mc.thePlayer.func_174811_aO() == EnumFacing.NORTH) {
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ + -posMod);
		}
		Summer.tellPlayer("Teleported " + posMod + " blocks horizontally.");
	}
}
