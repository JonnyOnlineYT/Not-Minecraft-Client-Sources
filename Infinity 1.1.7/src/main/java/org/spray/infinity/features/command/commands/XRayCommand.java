package org.spray.infinity.features.command.commands;

import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.command.CommandInfo;
import org.spray.infinity.features.module.visual.XRay;
import org.spray.infinity.main.Infinity;

import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;

@CommandInfo(name = "xray", desc = "Add or delete xray blocks")
public class XRayCommand extends Command {

	@Override
	public void command(String[] args, String msg) {
		int blockID = Integer.parseInt(args[1]);
		XRay xray = ((XRay) Infinity.getModuleManager().get(XRay.class));
		switch (args[0]) {
		case "add":
			xray.block.getBlocks().add(Registry.BLOCK.get(blockID));
			send(Formatting.GREEN + Registry.BLOCK.get(blockID).getName().getString() + Formatting.GRAY
					+ " block has been added to the" + Formatting.WHITE + " XRay list");
			break;
		case "del":
			if (xray.block.getBlocks().contains(Registry.BLOCK.get(blockID))) {
				xray.block.getBlocks().remove(Registry.BLOCK.get(blockID));
				send(Formatting.RED + Registry.BLOCK.get(blockID).getName().getString() + Formatting.GRAY
						+ " was successfully removed from" + Formatting.WHITE + " XRay list");
			} else
				send(Formatting.WHITE + Registry.BLOCK.get(blockID).getName().getString() + Formatting.RED
						+ " block is missing from the xray sheet");
			break;
		case "clear":
			xray.block.getBlocks().clear();
			send("XRay list was successfully cleared");
			break;
		}
	}

	@Override
	public void error() {
		send(Formatting.GRAY + "Please use" + Formatting.WHITE + ":");
		send(Formatting.WHITE + prefix + "xray add " + Formatting.AQUA + "blockID");
		send(Formatting.WHITE + prefix + "xray del " + Formatting.AQUA + "blockID");
		send(Formatting.WHITE + prefix + "xray clear");
	}

}
