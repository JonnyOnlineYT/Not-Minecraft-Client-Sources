package ru.rockstar.client.features.impl.misc;

import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventMouseKey;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class TeleportBack extends Feature {

	public boolean teleport;
    public TeleportBack(){
        super("Teleport","Телепортирует вас обратно после смерти", 0, Category.MISC);
    }
    public int x,y,z;
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getHealth() < 0 || !mc.player.isEntityAlive() || mc.currentScreen instanceof GuiGameOver) {
        	x = (int) (mc.player.posX);
            y = (int) (mc.player.posY);
            z = (int) (mc.player.posZ);
            mc.player.respawnPlayer();
            mc.displayGuiScreen(null);
            teleport = true;
        }
        
        if (teleport) {
        	mc.player.sendChatMessage("/rtp");
            float endX = x;
        	float endZ = z;
        	float endY = y;
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