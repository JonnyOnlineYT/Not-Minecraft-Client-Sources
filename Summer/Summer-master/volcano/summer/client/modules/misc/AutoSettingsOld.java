package volcano.summer.client.modules.misc;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventTick;
import volcano.summer.client.modules.combat.AntiBot;
import volcano.summer.client.modules.combat.AutoArmor;
import volcano.summer.client.modules.combat.Criticals;
import volcano.summer.client.modules.combat.Velocity;
import volcano.summer.client.modules.combat.aura.KillAura;
import volcano.summer.client.modules.movement.Flight;
import volcano.summer.client.modules.movement.InventoryMove;
import volcano.summer.client.modules.movement.NoSlowDown;
import volcano.summer.client.modules.movement.Speed;
import volcano.summer.client.modules.movement.Step;
import volcano.summer.client.modules.player.ChestSteal;
import volcano.summer.client.modules.player.NoFall;
import volcano.summer.client.value.ModeValue;

public class AutoSettingsOld extends Module {

	public ModeValue settings = new ModeValue("SettingsMode", "Mode", "Hypixel",
			new String[] { "Hypixel", "Mineplex", "Cubecraft" }, this);

	public AutoSettingsOld() {
		super("AutoSettingsOld", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
		if (this.settings.getStringValue().equalsIgnoreCase("hypixel")) {
			/* Modules */
			Summer.moduleManager.getModule(Step.class).setState(false);
			Summer.moduleManager.getModule(AntiBot.class).setState(true);
			Summer.moduleManager.getModule(Velocity.class).setState(true);
			Summer.moduleManager.getModule(NoFall.class).setState(true);
			Summer.moduleManager.getModule(InventoryMove.class).setState(true);
			Summer.moduleManager.getModule(AutoArmor.class).setState(true);
			Summer.moduleManager.getModule(NoSlowDown.class).setState(true);
			Summer.moduleManager.getModule(MCF.class).setState(true);
			/* Settings */
			KillAura.CLASS.AutoBlock.setValue(false);
			KillAura.CLASS.keepsprint.setValue(false);
			KillAura.CLASS.walls.setValue(true);
			KillAura.CLASS.Invisible.setValue(false);
			KillAura.CLASS.Swing.setValue(true);
			KillAura.CLASS.reach.setValue(4.5);
			KillAura.CLASS.clicks.setValue(12.0);
			ChestSteal.delay.setValue(60.0);
			AutoArmor.delay.setValue(255.0);
			// Velocity.THIS.hypixel.setValue(true);
			Flight.flightMode.setStringValue("Hypixel");
			Speed.speedMode.setStringValue("LowHop");
			KillAura.auraMode.setStringValue("Switch");
			AntiBot.antibotMode.setStringValue("WatchDog");
			Criticals.critsmode.setStringValue("Hypixel");
			/* Notifications */
			Summer.tellPlayer("Settings were updated for hypixel server.");
			Summer.tellPlayer("DO NOT use: Scaffold, Breaker.");
			Summer.tellPlayer("AntiBot has been updated to 'Hypixel' mode.");
			Summer.tellPlayer("Criticals has been updated to 'Hypixel' mode.");
			Summer.tellPlayer("Speed has been updated to 'LowHop' mode.");
			Summer.tellPlayer("Flight has been updated to 'Hypixel' mode.");
			Summer.tellPlayer("KillAura settings were updated for Hypixel.");
		} else if (this.settings.getStringValue().equalsIgnoreCase("mineplex")) {
			Summer.tellPlayer("Settings were updated for 'Mineplex' server.");
			/* Modules */
			Summer.moduleManager.getModule(AntiBot.class).setState(true);
			Summer.moduleManager.getModule(Criticals.class).setState(true);
			Summer.moduleManager.getModule(Velocity.class).setState(true);
			Summer.moduleManager.getModule(NoFall.class).setState(true);
			Summer.moduleManager.getModule(NoSlowDown.class).setState(true);
			Summer.moduleManager.getModule(MCF.class).setState(true);
			/* Settings */
			KillAura.CLASS.AutoBlock.setValue(true);
			KillAura.CLASS.keepsprint.setValue(true);
			KillAura.CLASS.walls.setValue(true);
			KillAura.CLASS.Invisible.setValue(false);
			KillAura.CLASS.Swing.setValue(true);
			KillAura.CLASS.reach.setValue(5.5);
			KillAura.CLASS.clicks.setValue(13.0);
			// Velocity.THIS.hypixel.setValue(false);
			AntiBot.antibotMode.setStringValue("Mineplex");
			Criticals.critsmode.setStringValue("Packet");
			Speed.speedMode.setStringValue("Gwen");
			KillAura.auraMode.setStringValue("Switch");
			Flight.flightMode.setStringValue("Mineplex");
			/* Notifications */
			Summer.tellPlayer("AntiBot has been updated to 'Mineplex' mode.");
			Summer.tellPlayer("Criticals has been updated to 'Packet' mode.");
			Summer.tellPlayer("Flight has been updated to 'Mineplex' mode.");
			Summer.tellPlayer("Speed has been updated to 'Gwen' mode.");
			Summer.tellPlayer("KillAura settings were updated for Mineplex.");
		} else if (this.settings.getStringValue().equalsIgnoreCase("Cubecraft")) {
			/* Modules */
			Summer.moduleManager.getModule(Step.class).setState(false);
			Summer.moduleManager.getModule(AntiBot.class).setState(false);
			Summer.moduleManager.getModule(Velocity.class).setState(false);
			Summer.moduleManager.getModule(NoFall.class).setState(false);
			Summer.moduleManager.getModule(InventoryMove.class).setState(true);
			Summer.moduleManager.getModule(AutoArmor.class).setState(true);
			Summer.moduleManager.getModule(NoSlowDown.class).setState(true);
			Summer.moduleManager.getModule(MCF.class).setState(true);
			/* Settings */
			KillAura.CLASS.AutoBlock.setValue(true);
			KillAura.CLASS.keepsprint.setValue(true);
			KillAura.CLASS.walls.setValue(true);
			KillAura.CLASS.Invisible.setValue(false);
			KillAura.CLASS.Swing.setValue(true);
			KillAura.CLASS.reach.setValue(4.5);
			KillAura.CLASS.clicks.setValue(12.0);
			// Velocity.THIS.hypixel.setValue(false);
			Speed.speedMode.setStringValue("SlowHop");
			KillAura.auraMode.setStringValue("Switch");
			Flight.flightMode.setStringValue("Cubecraft");
			/* Cubecraft */
			Summer.tellPlayer("Flight has been updated to 'Cubecraft' mode.");
			Summer.tellPlayer("Speed has been updated to 'SlowHop' mode.");
			Summer.tellPlayer("KillAura settings were updated for Cubecraft.");
		}
	}

	@Override
	public void onDisable() {

	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventTick && mc.theWorld != null) {
			if (this.settings.getStringValue().equalsIgnoreCase("hypixel")) {
				setDisplayName("Hypixel");
			} else if (this.settings.getStringValue().equalsIgnoreCase("mineplex")) {
				setDisplayName("Mineplex");
			} else if (this.settings.getStringValue().equalsIgnoreCase("cubecraft")) {
				setDisplayName("CubeCraft");
			} else {
				setDisplayName("");
			}
		}
	}
}
