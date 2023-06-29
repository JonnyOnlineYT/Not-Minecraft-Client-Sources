package ru.wendoxd.modules.impl.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class FastBow extends Module {

	private Frame fastbow_frame = new Frame("FastBow");
	private final CheckBox fastbow = new CheckBox("FastBow").markArrayList("FastBow");
	private final Slider ticks = new Slider("Ticks", 1, 0, 10, 0.1, () -> fastbow.isEnabled(true));

	@Override
	protected void initSettings() {
		fastbow.markSetting("FastBow");
		fastbow_frame.register(fastbow, ticks);
		MenuAPI.combat.register(fastbow_frame);
		ItemEnderPearl.class.getClass();
		NetHandlerPlayServer.class.getClass();
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate && fastbow.isEnabled(false)) {
			if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive()
					&& mc.player.getItemInUseMaxCount() >= ticks.getFloatValue()) {
				mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
						BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
				mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
				mc.player.stopActiveHand();
			}
		}
	}
}
