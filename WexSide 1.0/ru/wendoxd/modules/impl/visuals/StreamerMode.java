package ru.wendoxd.modules.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.render.EventEntityRenderName;
import ru.wendoxd.events.impl.render.EventRenderSkin;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class StreamerMode extends Module {

	private Frame streamermode_frame = new Frame("NameProtect");
	public static CheckBox streamermode = new CheckBox("NameProtect").markArrayList("NameProtect");
	public static MultiSelectBox type = new MultiSelectBox("Type", new String[] { "Own Name", "Other Names",
			"Tab Spoof", "Skin Spoof", "Board Spoof", "Warp Spoof", "Auth Spoof", "Friend Spoof", "Alt Spoof" },
			() -> streamermode.isEnabled(true));

	@Override
	protected void initSettings() {
		streamermode_frame.register(streamermode, type);
		MenuAPI.hud.register(streamermode_frame);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventEntityRenderName && streamermode.isEnabled(false)) {
			if (((EventEntityRenderName) event).getEntity() instanceof EntityPlayer
					&& !WexSide.friendManager.isFriend(((EventEntityRenderName) event).getName()) && type.get(1)) {
				((EventEntityRenderName) event).setName(ChatFormatting.RED + "Protected");
			}
			if (((EventEntityRenderName) event).getEntity() instanceof EntityPlayer
					&& WexSide.friendManager.isFriend(((EventEntityRenderName) event).getEntity().getName())
					&& type.get(7)) {
				((EventEntityRenderName) event).setName(ChatFormatting.GREEN + "Protected");
			}
		}
		if (event instanceof EventRenderSkin && streamermode.isEnabled(false) && type.get(3)) {
			((EventRenderSkin) event).setCanceled();
		}
	}
}
