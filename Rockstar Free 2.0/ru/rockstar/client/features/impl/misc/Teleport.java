package ru.rockstar.client.features.impl.misc;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventMouseKey;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class Teleport extends Feature {


    public Teleport(){
        super("Teleport","Телепортирует вас когда вы нажимаете на колёсико мыши", 0, Category.MOVEMENT);
    }
@EventTarget
    public void onMouse(EventMouseKey eventMouseKey) {
        if(!this.isToggled()){
            return;
        }

        BlockPos tpc = mc.objectMouseOver.getBlockPos();

        if(eventMouseKey.getKey() == 2 || eventMouseKey.getKey() == 1){
                int x1 = (int) (tpc.getX()-mc.player.posX);
                int y1 = (int) (tpc.getY()-mc.player.posY);
                int z1 = (int) (tpc.getZ()-mc.player.posZ);
                float endX = x1;
            	float endZ = z1;
            	float endY = y1;
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
            	}
            }

    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        super.onDisable();
    }
}