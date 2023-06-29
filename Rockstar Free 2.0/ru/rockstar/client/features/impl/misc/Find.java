package ru.rockstar.client.features.impl.misc;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.Event;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.api.event.event.EventReceivePacket;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.math.MathHelper;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.Velocity;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Find extends Feature {
	public NumberSetting radius = new NumberSetting("Radius", 1000, 100, 5000, 10, () -> true);

    public Find() {
        super("Find", "Поиск игроков.", 0, Category.MISC);
        addSettings(radius);
    }

    @EventTarget
    public void onFind(EventPreMotion eventPreMotionUpdate) {
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK,
                new BlockPos(MathHelper.getRandomInRange(-radius.getNumberValue(), radius.getNumberValue()), 0,
                        MathHelper.getRandomInRange(-radius.getNumberValue(), radius.getNumberValue())), EnumFacing.DOWN));
    }

    @EventTarget
    public void onFindReceive(EventReceivePacket eventReceivePacket) {
        SPacketBlockChange packetBlockChange = (SPacketBlockChange) eventReceivePacket.getPacket();
        if (eventReceivePacket.getPacket() instanceof SPacketBlockChange) {
            Main.msg(TextFormatting.WHITE + "Игрок замечен на кординатах > " + TextFormatting.RED +
                    packetBlockChange.getBlockPosition().getX() + " " + packetBlockChange.getBlockPosition().getZ(), true);
            NotificationPublisher.queue("Player Tracker", "Игрок замечен на кординатах > " + TextFormatting.RED +
                    packetBlockChange.getBlockPosition().getX() + " " + packetBlockChange.getBlockPosition().getZ(), NotificationType.INFO);

        }
    }
}
