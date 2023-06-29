package volcano.summer.client.commands;

import net.minecraft.entity.player.EntityPlayer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;

public class Info extends Command {

	public Info() {
		super("Info", "<player>");
	}

	@Override
	public void run(String message) {
		try {
			String name = message.split(" ")[1];
			for (int x = 0; x < Summer.mc.theWorld.loadedEntityList.size(); x++) {
				if ((mc.theWorld.loadedEntityList.get(x) instanceof EntityPlayer)) {
					EntityPlayer ent = (EntityPlayer) mc.theWorld.loadedEntityList.get(x);
					if (ent.getName().equalsIgnoreCase(name)) {
						Summer.tellPlayer("Username: " + ent.getName());
						boolean checkHealth = ent.getHealth() == 20.0F;
						Summer.tellPlayer(
								"Health: " + (checkHealth ? "20"
										: new StringBuilder(
												String.valueOf(Math.round(ent.getHealth() * 100.0F / 100.0F)))
														.append("/20").toString()));
						Summer.tellPlayer("Distance: " + (int) Math.abs(ent.getDistanceToEntity(mc.thePlayer)));
						Summer.tellPlayer("XYZ: " + (int) ent.posX + ", " + (int) ent.posY + ", " + (int) ent.posZ);
					}
				}
			}
		} catch (Exception localException) {
			Summer.tellPlayer("Incorrect Syntax! Options: " + this.getArguments());
		}
	}
}
