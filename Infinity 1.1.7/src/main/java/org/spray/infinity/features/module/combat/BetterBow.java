package org.spray.infinity.features.module.combat;

import org.spray.infinity.features.Category;
import org.spray.infinity.features.Module;
import org.spray.infinity.features.ModuleInfo;
import org.spray.infinity.features.Setting;
import org.spray.infinity.utils.Helper;
import org.spray.infinity.utils.render.FontUtils;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BowItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(category = Category.COMBAT, desc = "When holding the bowstring and LKM, the bow will automatically spam with arrows", key = -2, name = "BetterBow", visible = true)
public class BetterBow extends Module {

	private Setting counter = new Setting(this, "Count Helper", true);
	private Setting autoShoot = new Setting(this, "Auto Shoot", false);

	private Setting delay = new Setting(this, "Delay", 2.5, 0.1, 25.0);

	@Override
	public void onUpdate() {
		if (Helper.getPlayer().getMainHandStack().getItem() instanceof BowItem && Helper.getPlayer().isUsingItem()) {

			if (Helper.getPlayer().getItemUseTime() >= delay.getCurrentValueDouble()) {

				if (autoShoot.isToggle() || Helper.MC.options.keyAttack.isPressed()) {
					Helper.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN,
							Helper.getPlayer().getHorizontalFacing()));
					Helper.sendPacket(new PlayerInteractItemC2SPacket(Helper.getPlayer().getActiveHand()));
					Helper.getPlayer().stopUsingItem();
				}
			}
		}
	}

	@Override
	public void onRender(MatrixStack matrices, float tickDelta, int width, int height) {
		String count = "Counting... " + Formatting.BLUE + Helper.getPlayer().getItemUseTime();

		if (Helper.getPlayer().getItemUseTime() >= delay.getCurrentValueDouble())
			if (!autoShoot.isToggle())
				count = "Delay Ready " + Formatting.GRAY + "press " + Formatting.BLUE + "LKM" + Formatting.GRAY
						+ " for use";

		if (counter.isToggle()) {

			if (Helper.getPlayer().getMainHandStack().getItem() instanceof BowItem
					&& Helper.getPlayer().isUsingItem()) {
				FontUtils.drawHCenteredString(matrices, count, width / 2, height / 2 - 70, -1);
			}

		}
	}

}
