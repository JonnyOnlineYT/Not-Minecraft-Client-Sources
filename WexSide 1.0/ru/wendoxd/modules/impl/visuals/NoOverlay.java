package ru.wendoxd.modules.impl.visuals;

import net.minecraft.init.MobEffects;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.events.impl.render.EventGameOverlay;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class NoOverlay extends Module {

	private Frame nooverlay_frame = new Frame("NoOverlay");
	public static CheckBox nooverlay = new CheckBox("NoOverlay").markArrayList("NoOverlay");
	public static MultiSelectBox type = new MultiSelectBox("Type",
			new String[] { "Hurt", "PumpkinHud", "Totem", "Cam.Bound", "Fire", "Light", "BossBar", "ChatRect", "Fog",
					"ArmorStand", "Weather", "Blindness", "Cam.Smooth", "Glint", "Scoreboard", "Background" },
			() -> nooverlay.isEnabled(true));

	@Override
	protected void initSettings() {
		nooverlay.markSetting("NoOverlay");
		nooverlay_frame.register(nooverlay, type);
		MenuAPI.hud.register(nooverlay_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (nooverlay.isEnabled(false)) {
			if (event instanceof EventGameOverlay) {
				if (type.get(0) && ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.Hurt) {
					((EventGameOverlay) event).setCanceled();
				}
				if (type.get(1)
						&& ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.PumpkinOverlay) {
					((EventGameOverlay) event).setCanceled();
				}
				if (type.get(2)
						&& ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.TotemPop) {
					((EventGameOverlay) event).setCanceled();
				}
				if (type.get(3)
						&& ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.CameraBounds) {
					((EventGameOverlay) event).setCanceled();
				}
				if (type.get(4) && ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.Fire) {
					((EventGameOverlay) event).setCanceled();
				}
				if (type.get(5) && ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.Light) {
					((EventGameOverlay) event).setCanceled();
				}
				if (type.get(6)
						&& ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.BossBar) {
					((EventGameOverlay) event).setCanceled();
				}
				if (type.get(7)
						&& ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.ChatRect) {
					((EventGameOverlay) event).setCanceled();
				}
				if (type.get(8) && ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.WaterFog
						|| ((EventGameOverlay) event).getOverlayType() == EventGameOverlay.OverlayType.LavaFog) {
					((EventGameOverlay) event).setCanceled();
				}
			}
			if (event instanceof EventUpdate) {
				if (type.get(10)) {
					mc.world.setRainStrength(0);
					mc.world.setThunderStrength(0);
				}
				if (type.get(11) && (mc.player.isPotionActive(MobEffects.BLINDNESS)
						|| mc.player.isPotionActive(MobEffects.NAUSEA))) {
					mc.player.removePotionEffect(MobEffects.NAUSEA);
					mc.player.removePotionEffect(MobEffects.BLINDNESS);
				}
				if (type.get(12)) {
					mc.gameSettings.smoothCamera = false;
				}
			}
		}
	}
}
