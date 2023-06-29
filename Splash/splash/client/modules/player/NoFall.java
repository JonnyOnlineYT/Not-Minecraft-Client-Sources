package splash.client.modules.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.modules.combat.Criticals;
import splash.client.modules.movement.AntiVoid;
import splash.client.modules.movement.LongJump;
import splash.client.modules.movement.Speed;

/**
 * Author: Cystems
 * 
 * Credits: 
 * 		Spec aka Crypticc for:
 * 			- Watchdog nofall
 */
public class NoFall extends Module {

    public ModeValue<Mode> noFallModeValue = new ModeValue<>("Mode", Mode.GROUND, this);
    public BooleanValue<Boolean> packet = new BooleanValue<>("Use Packets", false, this);
    private double estFallDist;
    private boolean fallen;
    private int division;//To be used for aac nofall

	public NoFall() {
		super("NoFall", "Cancels fall damage.", ModuleCategory.PLAYER);
	}

	@Override
    public void onEnable() {
    	division = 1;
    	estFallDist = 0;
    	fallen = false;
    	super.onEnable();
    }
    
	@Collect
    public void onUpdate(EventPlayerUpdate eventUpdate) {
    	if(mc.thePlayer.fallDistance >= 2.75) {
    		switch(noFallModeValue.getValue()) {
    		case CANCEL: 
    			mc.thePlayer.onGround = true;
    		break;    
    		case WATCHDOG:
    		break;
    		case GROUND: 
    			if (mc.thePlayer.fallDistance > 2.75) {
    				if (packet.getValue()) {
    					mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer(true));
    				} else {
    					eventUpdate.setGround(true);
    				}
    			}
    		break;
			case VERUS:
				break;
			default:
				break;
    		}
    	}
    }	
    
    @Collect
    public void onMove(EventMove e) {
		if (noFallModeValue.getValue().equals(Mode.WATCHDOG) && AntiVoid.isBlockUnder() && !mc.thePlayer.onGround && !Splash.getInstance().getModuleManager().getModuleByClass(LongJump.class).isModuleActive()) {
				if (mc.thePlayer.fallDistance > 2.2) {
					Speed cheatSpeed = ((Speed)Splash.getInstance().getModuleManager().getModuleByClass(Speed.class));
					
					cheatSpeed.ticksNeeded = (int)mc.thePlayer.fallDistance;
					e.setX(mc.thePlayer.motionX *= .65);
					e.setZ(mc.thePlayer.motionZ *= .65);
					for (int i= 0; i < (int)mc.thePlayer.fallDistance; i++) {
						Criticals crits = ((Criticals)Splash.getInstance().getModuleManager().getModuleByClass(Criticals.class));
						crits.accumulatedFall = 0;
						mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.serverSideYaw, mc.thePlayer.serverSidePitch, true));
					}
					mc.thePlayer.fallDistance *= .2;
				}
		}
    }
    
	@Collect
	public void onpacket(EventPacketSend eventPacketSend) {
		if (noFallModeValue.getValue().equals(Mode.VERUS)) {
			if (eventPacketSend.getSentPacket() instanceof C03PacketPlayer) {
				C03PacketPlayer packet = (C03PacketPlayer) eventPacketSend.getSentPacket();
				packet.onGround = mc.thePlayer.onGround || (mc.thePlayer.ticksExisted % 2 == 0 && mc.thePlayer.fallDistance > 2.75);
			}
		}
	}
    public enum Mode {
    	CANCEL, WATCHDOG, GROUND, VERUS;
    }
}	
