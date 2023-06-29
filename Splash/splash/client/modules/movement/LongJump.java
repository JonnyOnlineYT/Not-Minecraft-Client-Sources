package splash.client.modules.movement;

import java.util.LinkedList;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.potion.Potion;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventUpdate;
import splash.client.modules.misc.Disabler;
import splash.client.modules.movement.Flight.Mode;
import splash.utilities.system.ClientLogger;

public class LongJump extends Module {
    public int stage, groundTicks;
    public double lastDistance;
    public double movementSpeed;
    private LinkedList<Packet> packets = new LinkedList<>();
    
	public LongJump() {
		super("LongJump", "Watchdog crying right now", ModuleCategory.MOVEMENT);
	}
	
	@Override
	public void onEnable() {
		lastDistance = movementSpeed = 0.0D;
		stage = groundTicks = 0;
		Disabler disabler = (Disabler) Splash.getInstance().getModuleManager().getModuleByClass(Disabler.class);
		disabler.sendDisable();
        packets.clear();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		Disabler disabler = (Disabler) Splash.getInstance().getModuleManager().getModuleByClass(Disabler.class);
		disabler.sendDisable();
		super.onDisable();
	}

	@Collect
	public void onUpdate(EventPlayerUpdate eventUpdate) {
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
        
		if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
			if (stage < 2) {
				eventUpdate.setY(mc.thePlayer.posY + 1.8995E-35D);
			}
			if (stage > 2 && mc.thePlayer.isMoving()) {
				activateModule();
			}
		}
	}

	@Collect
	public void onMove(EventMove moveEvent) {
        if (stage == 1) {
        	movementSpeed = 0;
        } else if (stage == 2) {  
        	moveEvent.setY(mc.thePlayer.motionY = moveEvent.getMotionY(moveEvent.getLegitMotion()));
            movementSpeed = 1.9 * moveEvent.getMovementSpeed();
        } else if (stage == 3) {
            movementSpeed = 2.14999 * moveEvent.getMovementSpeed();
        } else if (stage == 4) {
        	movementSpeed *= 1.22;
        } else {
        	if (stage < 15) {
                if (mc.thePlayer.motionY < 0) {
                    moveEvent.setY(mc.thePlayer.motionY *= .7225f);
                }
	            movementSpeed = lastDistance - lastDistance / 159;
        	} else {
        		movementSpeed *= .75;
        	}
        }
        moveEvent.setMoveSpeed(Math.max(movementSpeed, moveEvent.getMovementSpeed()));
    	stage++; 
	}
}
