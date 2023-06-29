package ru.rockstar.client.features.impl.movement;


import net.minecraft.network.play.client.*;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.misc.FreeCam;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import net.minecraft.network.*;

public class BedrockClip extends Feature
{
    public BedrockClip() {
        super("BedrockClip", "Телепортирует игрока под бедрок", 0, Category.MOVEMENT);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
    	float endX = (float) mc.player.posX;
    	float endZ = (float) mc.player.posZ;
    	float endY = -3;
    	if(mc.player.ticksExisted % 8 == 0) {
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
