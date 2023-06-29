package splash.client.modules.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

import java.security.spec.ECPrivateKeySpec;
import java.util.LinkedList;
import java.util.PrimitiveIterator.OfDouble;

import javax.naming.InsufficientResourcesException;

import org.apache.commons.codec.language.bm.Rule.RPattern;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketSend;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.modules.combat.Aura;
import splash.client.modules.combat.Aura.BlockMode;
import splash.client.modules.combat.Criticals;
import splash.client.modules.misc.Disabler;
import splash.client.modules.visual.UI.Case;
import splash.utilities.math.MathUtils;
import splash.utilities.player.BlockUtils;
import splash.utilities.player.MovementUtils;
import splash.utilities.player.PlayerUtils;
import splash.utilities.system.ClientLogger;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 17:41, 30-May-20
 * Project: Client
 */
public class Flight extends Module {

    private Stopwatch timer = new Stopwatch();

    private double x, y, z, mineplexSpeed, lastDist, speed, randomValue;
    private boolean back,down,done, damageFly, allowed, onGroundCheck;
    public int counter, ticks, maxSize;
    public boolean reset;
    public boolean blocked;
	public float timerSpeed;
	public long lastDisable;
	private Stopwatch packetFlush;
    public NumberValue<Double> prop_speed = new NumberValue<>("Speed", 2.5D, 0.1D, 5D, this);
    public BooleanValue<Boolean> antikick = new BooleanValue<>("Anti Kick", true, this);
    public BooleanValue<Boolean> viewBobbing = new BooleanValue<>("View Bobbing", true, this);
    public ModeValue<Mode> mode = new ModeValue<>("Mode", Mode.VANILLA, this);
    private LinkedList<Packet> packets = new LinkedList<>();

    public Flight() {
        super("Flight", "Lets you fly.", ModuleCategory.MOVEMENT);
        setModuleMacro(Keyboard.KEY_R);
        packetFlush = new Stopwatch();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (onServer("hypixel") && antikick.getValue()) {
        	ClientLogger.printToMinecraft("AntiKick may result in watchdog bans, please do not use it");
        }
        
        onGroundCheck = mc.thePlayer.onGround;
        if (mc.thePlayer.isMoving() && System.currentTimeMillis() - Splash.INSTANCE.lastFlag >= 5000 && !mc.gameSettings.keyBindSprint.isKeyDown() && System.currentTimeMillis() - lastDisable > 1000L) {
        	damageFly = true;
        	allowed = !allowed;
        } else {
        	damageFly = false;
        }
        
        x = mc.thePlayer.posX; 
    	y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        mineplexSpeed = 0;
        counter = 0;
        speed = 0;
        timer.reset();
        done = false;
        back = false;
        down = false;
        ticks = 0;
        back = false;
        maxSize = 39;
        if (mode.getValue() == Mode.VANILLA) {
        	mc.thePlayer.capabilities.isFlying = true;
        }
        
        if (mode.getValue().equals(Mode.WATCHDOG)) {
        	randomValue = MathUtils.getRandomInRange(0.001111111111F, 0.001555555555F);
        	Disabler disabler = (Disabler) Splash.getInstance().getModuleManager().getModuleByClass(Disabler.class);
        	disabler.sendDisable();
        	mc.thePlayer.cameraYaw = 0.15f;
        } else { 
        	if (!mode.getValue().equals(Mode.SENTINEL)) randomValue = mc.thePlayer.posY;
        }
        if(mode.getValue().equals(Mode.GWEN)) {
           mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
        if (mode.getValue().equals(Mode.TEST)) {
        	mc.thePlayer.setStill();
        }
        
		Criticals crits = ((Criticals)Splash.getInstance().getModuleManager().getModuleByClass(Criticals.class));
		crits.forceUpdate();
		crits.waitTicks = 4;
    }

    @Override
    public void onDisable() {
        if (mode.getValue().equals(Mode.SENTINEL)) {
        	mc.thePlayer.motionX = 0.0D;
            mc.thePlayer.motionZ = 0.0D;
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.5F, mc.thePlayer.posZ);
            mc.gameSettings.keyBindForward.pressed = false;
        }
        
        if (mode.getValue() == Mode.VANILLA) {
        	mc.thePlayer.capabilities.isFlying = false;
        }
        if (mode.getValue().equals(Mode.WATCHDOG)) {
//            EventMove.distance = (mc.thePlayer.posY - MovementUtils.getGroundLevel());
//            EventMove.stillTime = 20;
        	mc.thePlayer.motionY = 0;
            if (!packets.isEmpty()) {
                packets.forEach(packet -> {
                	mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
					mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
                });
                packets.clear();
            }
        	((Step) Splash.getInstance().getModuleManager().getModuleByClass(Step.class)).waitTicks = 3;
        	Disabler disabler = (Disabler) Splash.getInstance().getModuleManager().getModuleByClass(Disabler.class);
        	disabler.sendDisable();
        }
        mc.timer.timerSpeed = 1.0f;
        lastDisable = System.currentTimeMillis();
        super.onDisable();
    }
    
    public void forceMove() {
		double speed = .15;
		mc.thePlayer.motionX = (-Math.sin(mc.thePlayer.getDirection())) * speed;
		mc.thePlayer.motionZ = Math.cos(mc.thePlayer.getDirection()) * speed;
    }

    public int airSlot() {
        for (int j = 0; j < 8; ++j) {
            if (mc.thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        ClientLogger.printToMinecraft("Clear a hotbar slot.");
        return -10;
    }
    
    @Collect
    public void onUpdate(EventPlayerUpdate e) {
    	Aura killaura = ((Aura)Splash.INSTANCE.getModuleManager().getModuleByClass(Aura.class));
    	if (viewBobbing.getValue()) {
    		if (mc.thePlayer.ticksExisted % 4 != 0) {
    			mc.thePlayer.cameraYaw = 0.1f;
    		}
    	}
    	if (mode.getValue().equals(Mode.WATCHDOG)) {
    		if (e.getStage().equals(Stage.PRE)) {
    			
        		if (onGroundCheck) {
        			mc.thePlayer.onGround = true;
    				double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
    				double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
    				lastDist = Math.sqrt((xDist * xDist) + (zDist * zDist));
    				if (mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally && (mc.thePlayer.posY - (int) mc.thePlayer.posY) == 0) {
    					e.setY(e.getY() + 4.25E-12);
    				}
    	        	if (!mc.thePlayer.isMoving()) {
    	        		forceMove();
    	        	}
    	        	if (!damageFly || onGroundCheck && damageFly && counter > 1) {
    	            	mc.thePlayer.motionY = 0;
    	            	double value = mc.thePlayer.ticksExisted % 3 == 0 ? -.000325 : .000325 / 2;
		        		if (BlockUtils.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ)) instanceof BlockAir) { 
		        			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + value, mc.thePlayer.posZ);
		        		}
    	        	}
        		} else {
        			if (mc.thePlayer.ticksExisted % 10 == 0) {
        				onGroundCheck = mc.thePlayer.onGround;
        			}
        		}
    		}
    	}
    }

    @Collect
    public void onPlayerMove(EventMove eventMove) {
        if (antikick.getValue() && mc.thePlayer.ticksExisted % 70 == 0 || antikick.getValue() && mc.thePlayer.ticksExisted % 50 == 0) {
        	randomValue = mc.thePlayer.posY;
            MovementUtils.fallPacket();
            MovementUtils.ascendPacket();
        } else if (antikick.getValue() && (mc.thePlayer.ticksExisted % 73 == 0 || mc.thePlayer.ticksExisted % 53 == 0)) {
        	this.done = false;
        }
        if (mode.getValue().equals(Mode.SENTINEL)) {
        	
        	/*Crediting:
        	 * 
        	 * @author: Seb
        	 * 
        	 * Modifictions: 
        	 * Speed, values, boost
        	 * */
        	if (mc.thePlayer.ticksExisted % 5 == 0) {
            	eventMove.setMoveSpeed(1.6); 
        	}
            if(mc.thePlayer.ticksExisted % 3 != 0) {
            	mc.timer.timerSpeed = 0.35F;  
            	eventMove.setY(mc.thePlayer.motionY = (3.9555e-1 + randomValue) / 2);
            	eventMove.setMoveSpeed(3.0); 
            } else { 
            	mc.timer.timerSpeed = 1.0f;
            	eventMove.setY(mc.thePlayer.motionY = -(3.9555e-1 + randomValue));

            	eventMove.setMoveSpeed(0.0);
            	randomValue = MathUtils.secRanDouble(1.0e-30d, 1.0e-20d) + MathUtils.secRanDouble(0.5e-31d, .5e-21d);
            }
        }
        if (mode.getValue() == Mode.GWEN) {
        	/*
        	 * Crediting:
        	 * @author: Dort
        	 * 
        	 * 
        	 * Modifications: speed
        	 * */
            if (airSlot() == -10){
            	eventMove.setMoveSpeed(0.0);
                return;
            }

            if (!done) {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(airSlot()));
                BlockUtils.placeHeldItemUnderPlayer();

            	eventMove.setMoveSpeed(back ? -mineplexSpeed : mineplexSpeed);
                back = !back;
                if (mc.thePlayer.isMovingOnGround() && mc.thePlayer.ticksExisted % 2 == 0) {
                    mineplexSpeed += RandomUtils.nextDouble(0.125D, 0.12505D);
                }
                if (mineplexSpeed >= prop_speed.getValue() * 1.3 && mc.thePlayer.isCollidedVertically && mc.thePlayer.isMovingOnGround()) {
                    eventMove.setY(mc.thePlayer.motionY = 0.42F);
                	eventMove.setMoveSpeed(0.0);
                    done = true;
                    return;
                }
            } else {
                mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                if (mc.thePlayer.fallDistance == 0) {
                    eventMove.setY(mc.thePlayer.motionY += 0.039);
                } else if (mc.thePlayer.fallDistance <= 1.4) {
                    eventMove.setY(mc.thePlayer.motionY += 0.032);
                }
            	eventMove.setMoveSpeed(mineplexSpeed *= 0.979);
                if (mc.thePlayer.isMoving() && mc.thePlayer.isCollidedVertically) {
                    done = false;
                }
            }
        }

        if (mode.getValue() == Mode.VAC) {
        	eventMove.setY(1.0E-4);
        	if (mc.thePlayer.isMoving()) {
        		eventMove.setMoveSpeed(eventMove.getMovementSpeed(.15));
        	}
        }
        if (mode.getValue() == Mode.VANILLA) {
        	mc.thePlayer.onGround = true;
        	mc.thePlayer.cameraYaw = mc.thePlayer.cameraPitch = 0;
            eventMove.setY(mc.thePlayer.motionY = 0);
            
            if (mc.gameSettings.keyBindJump.pressed) eventMove.setY(mc.thePlayer.motionY = mc.thePlayer.motionY = prop_speed.getValue());
	
            if (mc.gameSettings.keyBindSneak.pressed) eventMove.setY(mc.thePlayer.motionY = mc.thePlayer.motionY -= prop_speed.getValue());

        	eventMove.setMoveSpeed(prop_speed.getValue());
 
        }
        if (mode.getValue() == Mode.PVPTEMPLE) {
        	//Old pvptemple!
            mc.thePlayer.motionY = 0;
            if (mc.thePlayer.ticksExisted % 4 == 0) {
	            if (!mc.thePlayer.isMoving()) mc.thePlayer.setStill();
	
	            if (mc.gameSettings.keyBindJump.pressed) mc.thePlayer.motionY = prop_speed.getValue() / 1;
	
	            if (mc.gameSettings.keyBindSneak.pressed) mc.thePlayer.motionY = -prop_speed.getValue() / 1;

	        	eventMove.setMoveSpeed(prop_speed.getValue());
            } else {
            	eventMove.setMoveSpeed(.149);
            }
        }
        
        if (mode.getValue() == Mode.FEIRCE) {
        	mc.thePlayer.onGround = true;
        	mc.thePlayer.cameraYaw = mc.thePlayer.cameraPitch = 0; 
        	if (!done) { 
        		mc.thePlayer.setPosition(mc.thePlayer.posX, randomValue, mc.thePlayer.posZ);
        		done = true;
        	}
            if (mc.gameSettings.keyBindJump.pressed) {
            	mc.thePlayer.motionY = prop_speed.getValue() / 1;
            } else if (mc.gameSettings.keyBindSneak.pressed) {
            	 mc.thePlayer.motionY = -prop_speed.getValue() / 1;
            } else {

                if(mc.thePlayer.ticksExisted % 3 != 0) {
    	        	eventMove.setMoveSpeed(prop_speed.getValue() * .45); 
                	eventMove.setY(mc.thePlayer.motionY = (.0006) / 2);
                } else { 
    	        	eventMove.setMoveSpeed(prop_speed.getValue()); 
                	eventMove.setY(mc.thePlayer.motionY = -(.0006));
                }
            }
        }
        if (mode.getValue() == Flight.Mode.WATCHDOG){

        	if (damageFly && onGroundCheck) {
            	mc.thePlayer.onGround = true; 
        		switch (counter) {
        		case 0:
        			if (timer.delay(100)) {
        				for (int i = 0; i < 9; i++) {
        					mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + eventMove.getMotionY(eventMove.getLegitMotion()), mc.thePlayer.posZ, false));
        					mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + (eventMove.getMotionY(eventMove.getLegitMotion()) % .0000625), mc.thePlayer.posZ, false));
        					mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer(false));
        				}
        				mc.getNetHandler().addToSendQueueNoEvent(new C03PacketPlayer(true));
        				speed = eventMove.getMovementSpeed() * (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 2.219 : 2.1); 
        				timer.reset();
        				counter = 1;
        			} else {
        				speed = 0;
        				eventMove.setX(mc.thePlayer.motionX = 0);
        				eventMove.setY(mc.thePlayer.motionY = 0);
        				eventMove.setZ(mc.thePlayer.motionZ = 0);
        			}
        			break;
        		case 1: 
        			speed *= 2.149999;
        			eventMove.setY(mc.thePlayer.motionY = eventMove.getMotionY(eventMove.getLegitMotion()));
        			counter = 2;
        			break;
        		case 2:
        			if (mc.thePlayer.isPotionActive(Potion.jump)) {
            			eventMove.setY(mc.thePlayer.motionY = -(eventMove.getMotionY(eventMove.getLegitMotion()) - .01));
        			}
					speed = Math.min(prop_speed.getValue(), 1.4445f);
        			counter = 3;
        			break;
        		default: 
        			if (allowed) {
            			if (counter == 5) {
            				mc.timer.timerSpeed = 1.2f;
            			} else if (counter > 5) {
            				if (mc.thePlayer.ticksExisted % 2 == 0) {
                				if (mc.timer.timerSpeed > 1.0f) mc.timer.timerSpeed -= .1;
            				}
            			}
        			}
        			if (mc.thePlayer.isCollidedHorizontally || !mc.thePlayer.isMoving()) {
        				mc.timer.timerSpeed = 1.0f;
        				damageFly = false;
        			}
        			speed -= speed / 159;
        			counter++;
        			break;
        		}
        		eventMove.setMoveSpeed(speed == 0 ? 0 : Math.max(speed, eventMove.getMovementSpeed()));
        	}
        }
    }
    
    @Collect
    public void onPacketSend(EventPacketSend event) {

    	if (mode.getValue().equals(Mode.SENTINEL)) {
    		if (event.getSentPacket() instanceof C02PacketUseEntity || event.getSentPacket() instanceof C0APacketAnimation) {
				event.setCancelled(true);
    		}
    	}
    	if (mode.getValue().equals(Mode.WATCHDOG)) {
    		if (counter >= 1 || !damageFly) {
	    		if (event.getSentPacket() instanceof C03PacketPlayer) {
	    			if (event.getSentPacket() instanceof C03PacketPlayer) {
		    			C03PacketPlayer packetPlayer = (C03PacketPlayer) event.getSentPacket();
		    			if (packetPlayer.isMoving()) {
		    				packets.add(packetPlayer);
		    			}
	    				event.setCancelled(true);
	    			}
	    			
    				if (packets.size() >= maxSize) {
    					packets.forEach(packet -> {
    						mc.thePlayer.sendQueue.addToSendQueueNoEvent(packet);
    						mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
    					});
    					packets.clear();
    				}
	    		}
    		} else {
    			event.setCancelled(true);
    		}
    	}
    }
    
    
    public enum Mode {
        VANILLA, PVPTEMPLE, GWEN, WATCHDOG, SENTINEL, FEIRCE, VAC, TEST
    }
}
