package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.input.EventMouse;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class PearlDebug
        extends Feature {
    public PearlDebug() {
        super("AntiPearlBlocker", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u0440\u043e\u0441\u0430\u0442\u044c \u044d\u043d\u0434\u0435\u0440 \u043f\u0435\u0440\u043b \u043f\u043e\u0434 \u0441\u0435\u0431\u044f, \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430\u0445 \u0433\u0434\u0435 \u044d\u0442\u043e \u0437\u0430\u043f\u0440\u0435\u0449\u0435\u043d\u043e", Type.Other);
    }

    @EventTarget
    public void onMouse(EventMouse event) {
        if (event.getKey() == 1 && Minecraft.player.getHeldItemMainhand().getItem() == Items.ENDER_PEARL) {
            Minecraft.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        }
    }
}
