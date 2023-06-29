package ru.rockstar.client.features.impl.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventReceivePacket;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.player.NoClip;
import ru.rockstar.client.ui.settings.impl.ListSetting;

public class NoFall extends Feature {
	public static ListSetting noFallMode;
    public TimerHelper timerHelper = new TimerHelper();

    public NoFall() {
        super("NoFall", "ѕозвол€ет получить меньший дамаг при падении", 0, Category.PLAYER);
        noFallMode = new ListSetting("NoFall Mode", "Vanilla", () -> true, "Vanilla", "GroundCancel", "Spartan", "MagicGrief", "Matrix", "Matrix New", "Hypixel");
        addSettings(noFallMode);
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String mode = noFallMode.getOptions();
        this.setSuffix(mode, true);
        if (mode.equalsIgnoreCase("MagicGrief") && mc.player.fallDistance > 2 || mc.player.onGround) {
        	mc.player.motionY = 0.00001;
    		mc.player.noClip = true;
        }
		
		
        if (mode.equalsIgnoreCase("Vanilla")) {
            if (mc.player.fallDistance > 3) {
                event.setOnGround(true);
                mc.player.connection.sendPacket(new CPacketPlayer(true));
            }
        } else if (mode.equalsIgnoreCase("Spartan")) {
            if (mc.player.fallDistance > 7f) {
                if (timerHelper.hasReached(150L)) {
                	mc.timer.timerSpeed = 4;
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
                    timerHelper.reset();
                } else {
                	mc.timer.timerSpeed = 1;
                    mc.player.onGround = false;
                }
            }
        } else if (mode.equalsIgnoreCase("Hypixel")) {
            if (mc.player.fallDistance > 3.4) {
                event.setOnGround(mc.player.ticksExisted % 2 == 0);
            }
        } else if (mode.equalsIgnoreCase("Matrix")) {
        	if(mc.player.fallDistance > 3f) {
        	    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
        	    mc.player.fallDistance = 1f;
        	}
        } else if (mode.equalsIgnoreCase("GroundCancel")) {
            event.setOnGround(false);
        }
    }
    
    @EventTarget
    public void noFall(EventUpdate update) {
    	String mode = noFallMode.getOptions();
        this.setSuffix(mode, true);
        if (mode.equalsIgnoreCase("Matrix New")) {
        	if(mc.player.fallDistance > 2.5f) {
        	    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, true));
        	    mc.player.fallDistance = 1f;
        	}
        }
    }
    
    @EventTarget
    public void onUpdate(EventReceivePacket event) {
    	String mode = noFallMode.getOptions();
    	if (mode.equalsIgnoreCase("MagicGrief") && mc.player.fallDistance > 2 || mc.player.onGround) {
    		mc.player.motionY = 0.00001;
    		mc.player.noClip = true;
    		if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
    			mc.player.noClip = true;
	    		mc.player.motionY = 0.00001;
    		}
    	}
    }
    
    public void onDisable() {
    	mc.timer.timerSpeed = 1;
        mc.player.noClip = false;
        super.onDisable();
    }
}
