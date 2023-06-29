package ru.rockstar.client.features.impl.misc;

import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventManager;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventSendPacket;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class KTLeave extends Feature
{
    
    public KTLeave() {
        super("KTLeave", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043b\u0438\u0432\u043d\u0443\u0442\u044c \u0432\u043e \u0432\u0440\u0435\u043c\u044f \u043f\u0432\u043f \u0431\u0435\u0437 \u043f\u043e\u0442\u0435\u0440\u0438 \u0432\u0435\u0449\u0435\u0439",0, Category.MISC);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
    	float endX = 1900;
    	float endZ = -3900;
    	float endY = 65;
    	if(mc.player.isSneaking() && mc.player.ticksExisted % 8 == 0) {
    	Main.msg("Пытаюсь телепортироваться на " + endX + " " + endY + " " + endZ, true);
    	mc.player.motionY += 0.2f;
    	mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, false));
    	mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
    	mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, true));
    	return;
    	}
    	if(mc.player.posX == endX && mc.player.posZ == endZ) {
    	Main.msg("§aУспешное телепортирование!", true);
    	toggle();
    	}
    }
}
