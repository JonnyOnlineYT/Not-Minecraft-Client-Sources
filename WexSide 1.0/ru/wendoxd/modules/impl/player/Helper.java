package ru.wendoxd.modules.impl.player;

import net.minecraft.init.Items;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventCalculateCooldown;
import ru.wendoxd.events.impl.player.EventRightClick;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class Helper extends Module {
	private Frame helper_frame = new Frame("GoldenAppleTimer");
	private final CheckBox goldenAppleHelper = (CheckBox) new CheckBox("GoldenAppleTimer").markArrayList("GAppleTimer")
			.markDescription("Кулдаун на использование золотого яблока");
	public static long lastGappleConsumeTime;

	@Override
	protected void initSettings() {
		goldenAppleHelper.markSetting("GAppleHelper");
		helper_frame.register(goldenAppleHelper);
		MenuAPI.player.register(helper_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRightClick && goldenAppleHelper.isEnabled(false) && mc.getCurrentServerData() != null
				&& mc.getCurrentServerData().serverIP != null) {
			long time = System.currentTimeMillis() - lastGappleConsumeTime;
			if (time < 2600) {
				((EventRightClick) event).setCanceled();
			}
		}
		if (event instanceof EventCalculateCooldown && goldenAppleHelper.isEnabled(false)
				&& mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null) {
			if (((EventCalculateCooldown) event).getStack() == Items.GOLDEN_APPLE) {
				long time = System.currentTimeMillis() - lastGappleConsumeTime;
				if (time < 2600) {
					((EventCalculateCooldown) event).setCooldown(time / 2600f);
				}
			}
		}
	}
}
