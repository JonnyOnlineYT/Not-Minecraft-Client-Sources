package splash.client.modules.combat;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.modules.combat.Criticals.Mode;
import splash.client.modules.movement.Speed;
import splash.utilities.system.ClientLogger;

/**
 * Author: Ice
 * Created: 17:52, 30-May-20
 * Project: Client
 */
public class Velocity extends Module {
	public int counter;
	public ModeValue<Mode> modeValue = new ModeValue<>("Mode", Mode.SPOOF, this);
	public NumberValue<Double> horiz = new NumberValue<Double>("Horizontal", 0.0, 0.0, 1.00, this);
	public NumberValue<Double> vert = new NumberValue<Double>("Vertical", 0.0, 0.0, 1.00, this);

    public Velocity() {
        super("Velocity", "Disables knockback", ModuleCategory.COMBAT);
    }

    public enum Mode {
		VELT, PACKET, AGCTEST, DISLOCATE ,SPOOF
	}

    @Collect
    public void onPacketReceive(EventPacketReceive eventPacketReceive) { 
        if(eventPacketReceive.getReceivedPacket() instanceof S12PacketEntityVelocity) {
           S12PacketEntityVelocity packet = (S12PacketEntityVelocity) eventPacketReceive.getReceivedPacket();
           if (packet.getEntityID() != mc.thePlayer.getEntityId()) {
               return;
           }
           if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
        	   double velX = getVelocity(packet.getMotionX(), vert.getValue() *.1);
        	   double velY = getVelocity(packet.getMotionY(), vert.getValue() *.1);
        	   double velZ = getVelocity(packet.getMotionZ(), horiz.getValue() *.1);
               switch (modeValue.getValue()) {
               case AGCTEST: 
            	   if (mc.thePlayer.hurtTime == 8) {
                       mc.thePlayer.setVelocity((packet.getMotionX() / 8000), -((packet.getMotionY() / 8000)), (packet.getMotionZ() / 8000));
                       eventPacketReceive.setCancelled(true);
            	   }
               break;
               case DISLOCATE:
            	   eventPacketReceive.setCancelled(counter >= 3);
            	   if (counter++ >= 3) {
            		   mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX + (packet.getMotionX() / 8000), mc.thePlayer.lastTickPosY + (packet.getMotionY() / 8000), mc.thePlayer.lastTickPosZ + (packet.getMotionZ() / 8000));
            		   counter = 0;
            	   }
               break;
               case PACKET:
            	   packet.motionX *= horiz.getValue();
            	   packet.motionY *= vert.getValue();
            	   packet.motionZ *= horiz.getValue();
                   eventPacketReceive.setCancelled(true);
            	   break;
               case VELT: 
            	   velY = getVelocity(packet.getMotionY(), 94.15);
            	   mc.thePlayer.setVelocity(velX, velY, velZ);
            	   mc.thePlayer.addVelocity(velX, 0.12F, velZ);
                   eventPacketReceive.setCancelled(true);
            	   break;
               case SPOOF:
            	   sendPosition(packet.getMotionX() / 8000, packet.getMotionY() / 8000, packet.getMotionZ() / 8000, mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, packet.getMotionY() / 8000, 0.0)).size() > 0, mc.thePlayer.isMoving());

                   eventPacketReceive.setCancelled(true);
                   break; 
               } 
           }
        }
 
        if(eventPacketReceive.getReceivedPacket() instanceof S27PacketExplosion) {
        	S27PacketExplosion s27PacketExplosion = (S27PacketExplosion) eventPacketReceive.getReceivedPacket();
        	s27PacketExplosion.field_149152_f = 0;
        	s27PacketExplosion.field_149153_g = 0;
        	s27PacketExplosion.field_149159_h = 0;
        	eventPacketReceive.setCancelled(true);
        } 
    }
    public double getVelocity(double motionX, double reduction) {
        return (motionX / 8000) * reduction;
    }

}
