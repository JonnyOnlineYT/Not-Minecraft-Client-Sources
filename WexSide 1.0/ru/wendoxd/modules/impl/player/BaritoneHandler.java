package ru.wendoxd.modules.impl.player;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;

public class BaritoneHandler extends Module {
	public static Frame baritoneFrame = new Frame("Baritone");
	public static CheckBox baritone = new CheckBox("Baritone");
	public static CheckBox baritoneAutoEat = new CheckBox("AutoEat", () -> baritone.isEnabled(true));
	public static Slider feed = new Slider("Feed", 0, 1, 20, 0.49,
			() -> baritone.isEnabled(true) && baritoneAutoEat.isEnabled(true));
	public static CheckBox baritoneAntiRg = new CheckBox("Anti-RG", () -> baritone.isEnabled(true));
	public static CheckBox baritoneIgnoreInvetory = new CheckBox("Ignore Inventory", () -> baritone.isEnabled(true));

	@Override
	protected void initSettings() {
		MenuAPI.player.register(baritoneFrame);
		baritoneFrame.register(baritone, baritoneAutoEat, feed, baritoneAntiRg, baritoneIgnoreInvetory);
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(mc.player);
			if (mc.player.getFoodStats().getFoodLevel() <= feed.getFloatValue() && baritoneAutoEat.isEnabled(false)
					&& baritone.getMineProcess().isActive()) {
				ItemStack offhand = mc.player.getHeldItemOffhand();
				boolean needFindFood = !(offhand.getItem() instanceof ItemFood);
				if (needFindFood) {
					for (int i = 0; i < 36; i++) {
						ItemStack s = mc.player.inventory.getStackInSlot(i);
						if (s.getItem() instanceof ItemFood) {
							if (i < 9) {
								i = i + 36;
							}
							mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, mc.player);
							mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
							if (!(mc.player.getHeldItemOffhand().getItem() instanceof ItemAir)) {
								mc.playerController.windowClick(0, i, 0, ClickType.PICKUP, mc.player);
							}
							break;
						}
					}
				} else {
					if (!mc.player.isHandActive()) {
						mc.rightClickMouse();
					}
					mc.unpressMouse();
				}
			}
		}
	}
}
