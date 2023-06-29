package splash.client.modules.combat;

import java.util.PrimitiveIterator.OfDouble;

import org.lwjgl.input.Mouse;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import splash.Splash;
import splash.api.event.events.player.PlayerJumpEvent;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventStep;
import splash.client.modules.movement.Flight;
import splash.client.modules.movement.Speed;
import splash.client.modules.movement.Speed.Mode;
import splash.client.modules.player.Scaffold;
import splash.utilities.math.MathUtils;
import splash.utilities.player.BlockUtils;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 18:07, 13-Jun-20
 * Project: Client
 */
public class Criticals extends Module {
    private Stopwatch critTimer = new Stopwatch();
    private double groundSpoofDist = 0.001;
	public double posY;
    private boolean cubeCrit, forceUpdate;
    private float yaw, pitch;
	public int airTime, waitTicks;
	public ModeValue<Mode> modeValue = new ModeValue<>("Mode", Mode.SPOOF, this);
	public NumberValue<Integer> ticks = new NumberValue<Integer>("Ticks", 2, 2, 5, this);
	public double accumulatedFall;

	public Criticals() {
		super("Criticals", "Crits without you moving", ModuleCategory.COMBAT);
	}

	public enum Mode {
		SPOOF, POSITION, MINEPLEX, PACKET, SJUMP
	}
	
	@Override
    public void onEnable() {
		super.onEnable();
		airTime = 0;
		waitTicks = 3;
    	groundSpoofDist = 1.0E-13D;
    }
	
	@Override
    public void onDisable() {
		super.onDisable();
		airTime = 0;
        groundSpoofDist = 0.001;
    }
	
	@Collect
	public void onJumpEvent(PlayerJumpEvent e) {
        if (airTime != 0 && mc.thePlayer.isMoving()) { 
        	waitTicks = 4;
    		sendPosition(0,0,0, true, mc.thePlayer.isMoving());
        	e.setCancelled(true);
    		mc.thePlayer.motionY = .42D;
            airTime = 0;
        } else {
            e.setCancelled(false);
        }
	}
	
    @Collect
    public void onBlockStep(EventStep event) {
        if (mc.thePlayer == null)
            return;
        if (mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY < .626 && mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY > .4) {
            waitTicks = 4; 
        }
    }
	
	@Collect
	public void onUpdate(EventPacketSend event) {
		if(mc.thePlayer == null || !interferanceFree()) return;
		
		if (event.getSentPacket() instanceof C02PacketUseEntity) {
			C02PacketUseEntity packet = (C02PacketUseEntity) event.getSentPacket();
			if (packet.getAction() == C02PacketUseEntity.Action.ATTACK) {
				if (modeValue.getValue() == Mode.SJUMP && airTime == 0 && mc.thePlayer.hurtTime == 0 && waitTicks == 0 && interferanceFree()) {
					airTime = 13;
				}
				if (modeValue.getValue() == Mode.PACKET) {
					airTime = ticks.getValue() + 1;
					sendPosition(0,0.1232225,0, false, mc.thePlayer.isMoving());
					sendPosition(0,1.0554E-9,0, false, mc.thePlayer.isMoving());
					sendPosition(0,0,0, true, mc.thePlayer.isMoving());
					critTimer.reset();
				} else if (modeValue.getValue() == Mode.MINEPLEX) {
                	airTime = ticks.getValue() + 1;
                	sendPosition(0,0.0000498345848,0, false, mc.thePlayer.isMoving());
                	sendPosition(0,0.0000392222555,0, false, mc.thePlayer.isMoving());
				}
			}
		}
	}
	
	public void doUpdate(EventPlayerUpdate eventPlayerUpdate) {
		if (EventMove.stillTime > 0) return;
		if (modeValue.getValue() == Mode.POSITION) {
			Aura aura = ((Aura)Splash.INSTANCE.getModuleManager().getModuleByClass(Aura.class));
			boolean valid = onServer("ghostly") || onServer("valorhcf") ||( mc.thePlayer.fallDistance == 0.0 && !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && !mc.thePlayer.isInWater());

			if (!(!aura.isModuleActive() || aura.getCurrentTarget() == null || !valid)) {
				if (interferanceFree() || onServer("ghostly") || onServer("valorhcf")) {
					if (waitTicks == 0 && accumulatedFall <= (Splash.getInstance().getGameMode().equals(Splash.GAMEMODE.DUELS) ? 7 : 2.9)) {
						eventPlayerUpdate.setGround(false);
						forceUpdate = true;
						if (airTime >= 1) {
							double value = (.955577777777 + (accumulatedFall * 1.0e-12d));
							accumulatedFall += posY - (posY * value);
							posY *= value;
						}
						eventPlayerUpdate.setY(mc.thePlayer.posY + posY); 
						airTime++;
					} else {
						if (accumulatedFall >= (Splash.getInstance().getGameMode().equals(Splash.GAMEMODE.DUELS) ? 7 : 2.9)) {
							if (mc.thePlayer.onGround) {
								sendPosition(0, 0, 0, true, false);
								accumulatedFall = 0;
							}
						}
						eventPlayerUpdate.setY(mc.thePlayer.posY); 
						if (waitTicks > 0) {
							waitTicks--;
						}
					}
				} else { 
					eventPlayerUpdate.setY(mc.thePlayer.posY);//
					waitTicks = 3;
				}
			} else {
				eventPlayerUpdate.setY(mc.thePlayer.posY);
				waitTicks = 3;
			}
		}
	}
	
	@Collect
	public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
 
		if (modeValue.getValue() == Mode.SPOOF) {
			if (groundSpoofDist < 0.0001) {
                groundSpoofDist = 0.001;
            }
            if (mc.thePlayer.isSwingInProgress && mc.thePlayer.isCollidedVertically){
                eventPlayerUpdate.setY(eventPlayerUpdate.getY() + groundSpoofDist);
                eventPlayerUpdate.setGround(false);
                groundSpoofDist -= 1.0E-11;
            }
		}
		
		if (modeValue.getValue() == Mode.SJUMP && eventPlayerUpdate.getStage().equals(Stage.PRE)) {
            if (interferanceFree() && mc.thePlayer.hurtTime == 0){
            	if (waitTicks > 0)waitTicks--;
            	if (waitTicks > 0) return;

            	if (airTime == 13) {
            		groundSpoofDist = 0.41999998688697815;
            	} if (airTime == 12) {
            		groundSpoofDist = 0.7531999805212024;
            	} else if (airTime == 11) {
            		groundSpoofDist = 1.0013359791121417;
            	} else if (waitTicks == 10) {
            		groundSpoofDist = 1.1661092609382138;
            	} else if (airTime == 9) {
            		groundSpoofDist = 1.2491870787446828;
            	} else if (airTime == 8) {
            		groundSpoofDist = 1.2491870787446828;
            	} else if (airTime == 7) {
            		groundSpoofDist = 1.1707870772188045;
            	} else if (airTime == 6) {
            		groundSpoofDist = 1.015555072702199;
            	} else if (airTime == 5) {
            		groundSpoofDist = 0.7850277037892397;
            	} else if (airTime == 4) {
            		groundSpoofDist = 0.48071087633169896;
            	} else if (airTime == 3) {
            		groundSpoofDist = 0.1040803780930446;
            	} else if (airTime == 2) {
            		groundSpoofDist = 0;
            	}
            	eventPlayerUpdate.setY(mc.thePlayer.posY + (airTime == 0 ? 0 : groundSpoofDist));
            	eventPlayerUpdate.setGround(eventPlayerUpdate.getY() == mc.thePlayer.posY);
            	if (airTime > 0) airTime--;
            } else {
            	groundSpoofDist = 0;
            	airTime = 0;
            	waitTicks = 6;
            }
		}
	}
	
	public void forceUpdate() {
		if (!forceUpdate || airTime == 0) return;
		//You don't send c06s standing still, doing so flags any half decent anticheat - food for thought
		sendPosition(0,0,0, true, mc.thePlayer.isMoving());
		
		accumulatedFall = 0;
		forceUpdate = false;
	}
	
	public boolean interferanceFree() {
		if (Splash.INSTANCE.getModuleManager().getModuleByClass(Speed.class).isModuleActive()) return ((Speed)Splash.INSTANCE.getModuleManager().getModuleByClass(Speed.class)).modeValue.equals(Speed.Mode.SENTINEL);
		if (Splash.INSTANCE.getModuleManager().getModuleByClass(Flight.class).isModuleActive()) return false;
		if (Splash.INSTANCE.getModuleManager().getModuleByClass(Scaffold.class).isModuleActive()) return false;
		if (mc.gameSettings.keyBindJump.isKeyDown() || mc.thePlayer.isInWater() || mc.thePlayer.isInLava() || mc.thePlayer.isOnLadder()) return false;
		return (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && mc.thePlayer.fallDistance == 0.0 && mc.thePlayer.stepHeight < .7);
	}
	
}
