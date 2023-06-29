package org.spray.heaven.features.module.modules.combat;

import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.math.BlockPos;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;

@ModuleInfo(name = "BetterBow", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class BetterBow extends Module {

    private Setting counter = register(new Setting("Count Helper", true));
    private Setting autoShoot = register(new Setting("Auto Shoot", false));

    private Setting delay = register(new Setting("Delay", 2.5, 0.1, 25.0));

    @Override
    public void onUpdate() {
        if (mc.player.isHandActive() && mc.player.getActiveItemStack().getItem() == Items.BOW) {

            if (mc.player.getItemInUseMaxCount() >= delay.getValue()) {

                if (mc.gameSettings.keyBindAttack.isKeyDown() || autoShoot.isToggle()) {
                    Wrapper.sendPacket(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN,
                            mc.player.getHorizontalFacing()));
                    Wrapper.sendPacket(new CPacketPlayerTryUseItem(mc.player.getActiveHand()));
                    mc.player.stopActiveHand();
                }
            }
        }
    }

    @Override
    public void onRender(int width, int height, float delta) {
        String count = "Counting... " + ChatFormatting.GOLD + mc.player.getItemInUseMaxCount();

        if (mc.player.getItemInUseMaxCount() >= delay.getValue())
            if (!autoShoot.isToggle())
                count = "Delay Ready " + ChatFormatting.GRAY + "press " + ChatFormatting.GOLD + "LKM"
                        + ChatFormatting.GRAY + " for use";

        if (counter.isToggle()) {
            if (mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && mc.player.isHandActive())
                IFont.WEB_MIDDLE.drawCenteredString(count, width / 2, height / 2 - 70, -1, 0.65f);

        }
    }

}
