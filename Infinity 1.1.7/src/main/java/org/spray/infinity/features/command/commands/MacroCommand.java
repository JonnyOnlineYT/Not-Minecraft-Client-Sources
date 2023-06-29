package org.spray.infinity.features.command.commands;

import org.spray.infinity.features.command.Command;
import org.spray.infinity.features.command.CommandInfo;
import org.spray.infinity.features.component.macro.Macro;
import org.spray.infinity.main.Infinity;

import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Formatting;

@CommandInfo(name = "macro", desc = "Binding message for key to chat")
public class MacroCommand extends Command {

	@Override
	public void command(String[] args, String msg) {
		String macroMsg = msg.replace(getName() + " " + args[0] + " " + args[1] + " ", "");
		if (args[0].equalsIgnoreCase("add")) {
			Infinity.getMacroManager().getList().add(new Macro(macroMsg,
					InputUtil.fromTranslationKey("key.keyboard." + args[1].toLowerCase()).getCode()));
			send(Formatting.GRAY + "Macro added to key " + Formatting.WHITE + args[1]);
			Infinity.getMacroManager().save();
		} else if (args[0].equalsIgnoreCase("del")) {
			Infinity.getMacroManager()
					.del(InputUtil.fromTranslationKey("key.keyboard." + args[1].toLowerCase()).getCode());
			send(Formatting.GRAY + "Macro deleted from key " + Formatting.WHITE + args[1]);
			Infinity.getMacroManager().save();
		} else if (args[0].equalsIgnoreCase("list")) {
			if (Infinity.getMacroManager().getList().isEmpty()) {
				send(Formatting.GRAY + "Macro binds is empty");
			} else {
				Infinity.getMacroManager().getList().forEach(macro -> {
					send(macro.getKey() + " -> " + Formatting.GRAY + macro.getMessage());
				});
			}
		} else if (args[0].equalsIgnoreCase("clear")) {
			Infinity.getMacroManager().getList().clear();
			send(Formatting.GRAY + "Macros cleared");
			Infinity.getMacroManager().save();
		}
	}

	@Override
	public void error() {
		send(Formatting.GRAY + "Please use" + Formatting.WHITE + ":");
		send(Formatting.WHITE + prefix + "macro add " + Formatting.GRAY + "<" + Formatting.AQUA + "key" + Formatting.GRAY
				+ ">" + Formatting.GRAY + " message");
		send(Formatting.WHITE + prefix + "macro del " + Formatting.AQUA + "key");
		send(Formatting.WHITE + prefix + "macro list");
		send(Formatting.WHITE + prefix + "macro clear");

	}

}
