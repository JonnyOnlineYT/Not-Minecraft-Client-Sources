package ru.rockstar.client.features.impl.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class FastBow extends Feature {

    private final NumberSetting ticks;

    public FastBow() {
        super("FastBow", "При зажатии на ПКМ игрок быстро стреляет из лука",0, Category.COMBAT);
        this.ticks = new NumberSetting("Bow Ticks", 1.5f, 1.5f, 10, 0.5f, () -> true);
        addSettings(ticks);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isBowing() && mc.player.getItemInUseMaxCount() >= ticks.getNumberValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.player.stopActiveHand();
        }
    }
}
