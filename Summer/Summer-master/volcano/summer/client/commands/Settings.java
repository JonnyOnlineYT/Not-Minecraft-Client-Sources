package volcano.summer.client.commands;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.command.Command;
import volcano.summer.client.modules.combat.AntiBot;
import volcano.summer.client.modules.combat.AutoArmor;
import volcano.summer.client.modules.combat.Criticals;
import volcano.summer.client.modules.combat.Velocity;
import volcano.summer.client.modules.combat.aura.KillAura;
import volcano.summer.client.modules.misc.Breaker;
import volcano.summer.client.modules.misc.InvCleaner;
import volcano.summer.client.modules.movement.Dolphin;
import volcano.summer.client.modules.movement.Flight;
import volcano.summer.client.modules.movement.InventoryMove;
import volcano.summer.client.modules.movement.Jesus;
import volcano.summer.client.modules.movement.NoSlowDown;
import volcano.summer.client.modules.movement.Speed;
import volcano.summer.client.modules.movement.Sprint;
import volcano.summer.client.modules.movement.Step;
import volcano.summer.client.modules.player.ChestSteal;
import volcano.summer.client.modules.player.NoFall;
import volcano.summer.client.modules.player.Phase;
import volcano.summer.client.modules.player.Scaffold;
import volcano.summer.client.modules.render.HUD;

public class Settings extends Command {
	public Settings() {
		super("Settings", "<hypixel/mineplex/cubecraft>");
	}

	@Override
	public void run(final String message) {
		if (message.split(" ").length == 2) {
			if (message.split(" ")[1].equalsIgnoreCase("hypixel")) {
				/* Modules */
				Summer.moduleManager.getModule(HUD.class).setState(true);
				Summer.moduleManager.getModule(Step.class).setState(false);
				Summer.moduleManager.getModule(AntiBot.class).setState(true);
				Summer.moduleManager.getModule(Velocity.class).setState(true);
				Summer.moduleManager.getModule(NoFall.class).setState(true);
				Summer.moduleManager.getModule(InventoryMove.class).setState(true);
				Summer.moduleManager.getModule(AutoArmor.class).setState(true);
				Summer.moduleManager.getModule(NoSlowDown.class).setState(true);
				Summer.moduleManager.getModule(Jesus.class).setState(false);
				Summer.moduleManager.getModule(Dolphin.class).setState(false);
				Summer.moduleManager.getModule(Criticals.class).setState(true);
				/* Settings */
				KillAura.CLASS.AutoBlock.setValue(true);
				KillAura.CLASS.keepsprint.setValue(true);
				KillAura.CLASS.walls.setValue(true);
				KillAura.CLASS.Invisible.setValue(false);
				KillAura.CLASS.Swing.setValue(true);
				KillAura.CLASS.reach.setValue(4.3);
				KillAura.CLASS.clicks.setValue(7.0);
				Scaffold.scaffoldMode.setStringValue("Hypixel");
				ChestSteal.delay.setValue(115.0);
				Phase.phasemode.setStringValue("Vanilla");
				AutoArmor.delay.setValue(255.0);
				Velocity.INSTANCE.velocityMode.setStringValue("Hypixel");
				Flight.flightMode.setStringValue("Hypixel");
				Speed.speedMode.setStringValue("SlowHop");
				KillAura.auraMode.setStringValue("Single");
				AntiBot.antibotMode.setStringValue("WatchDog");
				Criticals.critsmode.setStringValue("Hypixel");
				AutoArmor.auto.openinv.setValue(false);
				InvCleaner.openinv.setValue(false);
				Scaffold.scaffoldMode.setStringValue("Hypixel");
				Scaffold.CLASS.tower.setValue(false);
				Scaffold.CLASS.swing.setValue(false);
				/* Notifications */
				Summer.tellPlayer("Settings were updated for hypixel server.");
				Summer.tellPlayer("Modules that do not bypass were disabled.");
				Summer.tellPlayer("AntiBot has been updated to 'Hypixel' mode.");
				Summer.tellPlayer("Criticals has been updated to 'Hypixel' mode.");
				Summer.tellPlayer("Speed has been updated to 'SlowHop' mode.");
				Summer.tellPlayer("Flight has been updated to 'Hypixel' mode.");
				Summer.tellPlayer("KillAura settings were updated for Hypixel.");
				Summer.tellPlayer("Scaffold has been updated to 'Hypixel' mode.");
			} else if (message.split(" ")[1].equalsIgnoreCase("mineplex")) {
				Summer.tellPlayer("Settings were updated for 'Mineplex' server.");
				/* Modules */
				Summer.moduleManager.getModule(AntiBot.class).setState(true);
				Summer.moduleManager.getModule(Criticals.class).setState(true);
				Summer.moduleManager.getModule(Velocity.class).setState(true);
				Summer.moduleManager.getModule(NoSlowDown.class).setState(true);
				Summer.moduleManager.getModule(Sprint.class).setState(true);
				Summer.moduleManager.getModule(AutoArmor.class).setState(true);
				/* Settings */
				KillAura.CLASS.AutoBlock.setValue(true);
				KillAura.CLASS.keepsprint.setValue(true);
				KillAura.CLASS.walls.setValue(true);
				KillAura.CLASS.Invisible.setValue(false);
				KillAura.CLASS.Swing.setValue(true);
				KillAura.CLASS.reach.setValue(5.5);
				KillAura.CLASS.clicks.setValue(13.0);
				Breaker.CLASS.delay.setValue(30.0);
				Breaker.CLASS.reach.setValue(5.0);
				Velocity.INSTANCE.velocityMode.setStringValue("Normal");
				AntiBot.antibotMode.setStringValue("Mineplex");
				Criticals.critsmode.setStringValue("Packet");
				Speed.speedMode.setStringValue("SlowHop");
				KillAura.auraMode.setStringValue("Switch");
				Flight.flightMode.setStringValue("Mineplex");
				/* Notifications */
				Summer.tellPlayer("AntiBot has been updated to 'Mineplex' mode.");
				Summer.tellPlayer("Criticals has been updated to 'Packet' mode.");
				Summer.tellPlayer("Speed has been updated to 'SlowHop' mode.");
				Summer.tellPlayer("KillAura settings were updated for Mineplex.");
				Summer.tellPlayer("Don't use: fly, nofall.");
			} else if (message.split(" ")[1].equalsIgnoreCase("cubecraft")) {
				/* Modules */
				Summer.moduleManager.getModule(Step.class).setState(false);
				Summer.moduleManager.getModule(Velocity.class).setState(true);
				Summer.moduleManager.getModule(NoFall.class).setState(false);
				Summer.moduleManager.getModule(InventoryMove.class).setState(false);
				Summer.moduleManager.getModule(AutoArmor.class).setState(true);
				Summer.moduleManager.getModule(NoSlowDown.class).setState(true);
				Summer.moduleManager.getModule(AntiBot.class).setState(true);
				Summer.moduleManager.getModule(Criticals.class).setState(true);
				Summer.moduleManager.getModule(Sprint.class).setState(true);
				/* Settings */
				KillAura.CLASS.AutoBlock.setValue(false);
				KillAura.CLASS.keepsprint.setValue(false);
				KillAura.CLASS.walls.setValue(true);
				KillAura.CLASS.Invisible.setValue(false);
				KillAura.CLASS.Swing.setValue(true);
				KillAura.CLASS.reach.setValue(4.3);
				KillAura.CLASS.clicks.setValue(14.0);
				KillAura.CLASS.walls.setValue(false);
				AutoArmor.auto.delay.setValue(255.0);
				AutoArmor.auto.openinv.setValue(true);
				ChestSteal.delay.setValue(125.0);
				ChestSteal.silent.setValue(false);
//				Scaffold.CLASS.delay.setValue(300.0);
//				Scaffold.CLASS.silent.setValue(false);
//				Scaffold.CLASS.legit.setValue(false);
				Velocity.INSTANCE.velocityMode.setStringValue("Motion");
				Speed.speedMode.setStringValue("Lowhop");
				AntiBot.antibotMode.setStringValue("Cubecraft");
				InvCleaner.openinv.setValue(true);
				/* Cubecraft */
				Summer.tellPlayer("Don't use: fly.");
				Summer.tellPlayer("Speed has been updated to 'SlowHop' mode.");
				Summer.tellPlayer("KillAura settings were updated for Cubecraft.");
			}
		} else {
			Summer.tellPlayer("Incorrect Syntax! AutoSettings <hypixel/mineplex/cubecraft>");
		}
	}
}
