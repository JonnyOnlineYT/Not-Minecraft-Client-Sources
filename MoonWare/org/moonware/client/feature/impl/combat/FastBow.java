package org.moonware.client.feature.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.Helper;
import org.moonware.client.settings.impl.NumberSetting;

public class FastBow extends Feature {

    private final NumberSetting ticks;

    public FastBow() {
        super("FastBow", "При зажатии на ПКМ игрок быстро стреляет из лука", Type.Combat);
        ticks = new NumberSetting("Bow Ticks", 1.5f, 1.5f, 10, 0.5f, () -> true);
        addSettings(ticks);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (Minecraft.player.inventory.getCurrentItem().getItem() instanceof ItemBow && Minecraft.player.isBowing() && Minecraft.player.getItemInUseMaxCount() >= ticks.getNumberValue()) {
            Minecraft.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Minecraft.player.getHorizontalFacing()));
            Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            Minecraft.player.stopActiveHand();
        }
    }
}
