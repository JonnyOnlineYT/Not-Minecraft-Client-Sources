package splash.client.modules.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import splash.Splash;
import splash.Splash.GAMEMODE;
import splash.api.event.events.player.PlayerJumpEvent;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.modules.combat.Aura;
import splash.client.modules.combat.TargetStrafe;
import splash.client.modules.misc.Disabler;
import splash.client.modules.player.Scaffold;
import splash.utilities.math.MathUtils;
import splash.utilities.math.rotation.RotationUtils;
import splash.utilities.player.BlockUtils;
import splash.utilities.player.MovementUtils;
import splash.utilities.system.ClientLogger;

import java.util.Random;
import java.awt.Event;
import java.util.Arrays;
import java.util.List;
import java.util.PrimitiveIterator.OfDouble;

import org.apache.http.auth.MalformedChallengeException;
import org.lwjgl.opengl.APPLEVertexArrayRange;

import com.sun.jna.MethodResultContext;

/**
 * Author: Ice Created: 17:41, 30-May-20 Project: Client
 */
public class Speed extends Module {
	
	private double speed, currentDistance, lastDistance, jumpY, x,y,z;
	public boolean prevOnGround, usedOnGround;
	public static double waterSpeed;
	public int stage, ticksNeeded;
	public static int hopStage;
	public BooleanValue<Boolean> waterSpeedValue = new BooleanValue<Boolean>("Water Speed", true, this);
	public NumberValue<Double> valueSpeed = new NumberValue<Double>("Speed", 0.5D, 0.1D, 5D, this);
	public ModeValue<Mode> modeValue = new ModeValue<Mode>("Mode", Mode.VANILLA, this);
	public Speed() {
		super("Speed", "Lets you go faster.", ModuleCategory.MOVEMENT);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		speed = ticksNeeded = 0;
		prevOnGround = mc.thePlayer.onGround;
		lastDistance = 0;
		if (modeValue.getValue().equals(Mode.WATCHDOG) || modeValue.getValue().equals(Mode.LOWHOP)) {
			Disabler disabler = (Disabler) Splash.getInstance().getModuleManager().getModuleByClass(Disabler.class);
			disabler.sendDisable();
		}
		hopStage = 1;
		waterSpeed = 0.1;
		stage = 0;
		speed = .15;
		jumpY = mc.thePlayer.posY;
	}

	@Override
	public void onDisable() {
		super.onDisable();
		mc.timer.timerSpeed = 1f;
		if (!modeValue.getValue().equals(Mode.WATCHDOG)) {
			mc.thePlayer.jumpMovementFactor = (float) (mc.thePlayer.motionX = mc.thePlayer.motionZ = 0.0F);
		}
		if (modeValue.getValue().equals(Mode.VAC)) {
			sendPosition(0,0,0, true, mc.thePlayer.isMoving());
			mc.thePlayer.setStill();
		}
		if (modeValue.getValue().equals(Mode.SENTINEL)) {
			mc.thePlayer.motionY = 0F;
			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.2664444, mc.thePlayer.posZ);
		}
	}
	
	@Collect
	public void onJump(PlayerJumpEvent playerJumpEvent) {
		if (modeValue.getValue().equals(Mode.WATCHDOG) || modeValue.getValue().equals(Mode.LOWHOP) || modeValue.getValue().equals(Mode.AGC)) {
			playerJumpEvent.setCancelled(true);
		}
	}
	

	@Collect
	public void packetEvent(EventPlayerUpdate e) {
		if (e.getStage().equals(Stage.PRE)) {
			double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
			double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
			lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
		} else {
			if (modeValue.getValue().equals(Mode.VAC)) {
				if (prevOnGround) {
					mc.timer.timerSpeed = .2f;
					mc.thePlayer.motionX = mc.thePlayer.motionZ = mc.thePlayer.motionY = 0;
					mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true));
					mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX, mc.thePlayer.posY + .0001, mc.thePlayer.posZ + mc.thePlayer.motionZ, false));
				} else {
					if (mc.thePlayer.ticksExisted % 10 == 0) {
						prevOnGround = mc.thePlayer.onGround;
						if (prevOnGround) {
							x = mc.thePlayer.posX;
							y = mc.thePlayer.posY;
							z = mc.thePlayer.posZ;
						}
					}
				}
			}	
		}
	}

	@Collect
	public void onMove(EventMove e) {
		boolean reset = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0, mc.thePlayer.motionY, 0.0)).size() > 0 && mc.thePlayer.onGround;
		boolean hasSpeed = mc.thePlayer.isPotionActive(Potion.moveSpeed);
		boolean hasJump = mc.thePlayer.isPotionActive(Potion.jump);
		if (waterSpeedValue.getValue()) {
			if (mc.thePlayer.isInWater()) {
				e.setY(mc.thePlayer.motionY = 0.42F);
				waterSpeed = 0.5D;
				e.setMoveSpeed(waterSpeed);
			} else {
				if (waterSpeed > 0.3) {
					e.setMoveSpeed(waterSpeed *= 0.99);
				}
			}
		}
		
		if (modeValue.getValue().equals(Mode.LOWHOP)) {
			final double rounded = MathUtils.round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3);
			if (rounded == MathUtils.round(0.4, 3)) {
				e.setY(mc.thePlayer.motionY = 0.31);
			}
			else if (rounded == MathUtils.round(0.71, 3)) {
				e.setY(mc.thePlayer.motionY = 0.04);
			}
			else if (rounded == MathUtils.round(0.75, 3)) {
				e.setY(mc.thePlayer.motionY = -0.2);
			}
			else if (rounded == MathUtils.round(0.55, 3)) {
				e.setY(mc.thePlayer.motionY = -0.14);
			}
			else if (rounded == MathUtils.round(0.41, 3)) {
				e.setY(mc.thePlayer.motionY = -0.2);
			}
			
		
			if (hopStage == 1 && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
				speed = 1.7 * e.getMovementSpeed() - 0.01;
				mc.timer.timerSpeed = 1.15f;
			}
			else if (hopStage == 2 && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
				if (!mc.gameSettings.keyBindJump.isKeyDown()) {
					e.setY(mc.thePlayer.motionY = 0.4f);
					mc.timer.timerSpeed = 1f;
				}
				speed *= 1.6;
			} else if (hopStage == 3) {
				final double difference = 0.66 * (lastDistance - e.getMovementSpeed());
				speed = lastDistance - difference;
			} else {
				if ((mc.thePlayer.isCollidedVertically) && hopStage > 0) {
					hopStage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
				}
				speed = lastDistance - lastDistance / 159.0;
			}
			e.setMoveSpeed(speed = Math.max(speed, e.getMovementSpeed()));
			if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
				++hopStage;
			}
		}
		
		if (modeValue.getValue().equals(Mode.VAC)) {
    		e.setX(mc.thePlayer.motionX = 0);
    		e.setY(mc.thePlayer.motionY = 0);
    		e.setZ(mc.thePlayer.motionX = 0);
    		if (mc.thePlayer.posY - (int)mc.thePlayer.posY != 0) {
    			e.setY(.025);
    		}
    		if (mc.thePlayer.ticksExisted % 4 != 0) e.setMoveSpeed(4);
		}
		
		if (modeValue.getValue() == Mode.AGC && mc.thePlayer.isMoving()) {
			if (ticksNeeded > 0) {
				if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
					ticksNeeded --;
				}
				return;
			}
			stage += prevOnGround ? -1 : 1;
			if (stage > 8) {
				prevOnGround = true;
			} else if (stage <= 0) {
				prevOnGround = false;
			}
			if (mc.thePlayer.onGround) {
				e.setY(mc.thePlayer.motionY = e.getMotionY(.42D));
				speed = e.getMovementSpeed();
			} else {
				speed = e.getMovementSpeed() * (1 + (stage * .0015));
			}
			e.setMoveSpeed(speed);
		}
		
		if (modeValue.getValue() == Mode.SENTINEL) {
			if (mc.thePlayer.isMoving()) {
				if (mc.thePlayer.onGround) {
					if (mc.thePlayer.ticksExisted % 3 == 0) {
						mc.timer.timerSpeed = .65f;
					} else {
						mc.timer.timerSpeed = 1;
					}
					e.setMoveSpeed(mc.thePlayer.ticksExisted % 4 == 0 ? valueSpeed.getValue() : e.getMovementSpeed(.15)); 
				} else {
					mc.timer.timerSpeed = 1;
					e.setX(0);
					e.setZ(0);
				}
			} else {
				mc.timer.timerSpeed = 1;
				e.setX(0);
				e.setZ(0);
			}
		}
		if (modeValue.getValue() == Mode.VANILLA) {
			e.setMoveSpeed(valueSpeed.getValue());
		}

		if (modeValue.getValue() == Mode.PVPTEMPLE && mc.thePlayer.isMoving()) {
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				if (usedOnGround) {
					e.setMoveSpeed(speed = .1);
					if (mc.thePlayer.onGround) {
						usedOnGround = false;
					}
				} else {
					if (mc.thePlayer.onGround) {
						speed = e.getMovementSpeed() * 1.1;
						e.setY(mc.thePlayer.motionY = 0.42F);
						prevOnGround = true;
					} else {
						speed -= (double) (mc.thePlayer.fallDistance / 10.0F);
						if (speed < e.getMovementSpeed()) {
							speed = e.getMovementSpeed();
						}
					}
				}
				e.setMoveSpeed(speed);
			} else {
				usedOnGround = true;
				if (mc.thePlayer.onGround) {
					if (mc.thePlayer.ticksExisted % 4 == 0) {
						e.setMoveSpeed(valueSpeed.getValue());
					} else {
						e.setMoveSpeed(.15);
					}
				} else {
					e.setMoveSpeed(.1);
				}
			}
		}

		if (modeValue.getValue() == Mode.GWEN) {
			mc.timer.timerSpeed = 1.0f;
			if (mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
				speed = 0.8;
				final Timer timer = mc.timer;
				timer.timerSpeed *= (float) 2.14999;
				e.setY(mc.thePlayer.motionY = 0.42);
				e.setX(0.0);
				e.setZ(0.0);
				return;
			}
			speed -= 0.0045;
			e.setMoveSpeed(speed);
		}

		if (modeValue.getValue() == Mode.WATCHDOG) {
			if (!mc.thePlayer.isMoving()) {
				stage = 0;
				return;
			}
			if (Splash.getInstance().getGameMode().equals(GAMEMODE.DUELS)) { 
				if (!mc.thePlayer.onGround) {
					Aura theAura = ((Aura)Splash.getInstance().getModuleManager().getModuleByClass(Aura.class));
					if (Splash.getInstance().getModuleManager().getModuleByClass(Aura.class).isModuleActive() && theAura.getCurrentTarget() != null) {
						mc.timer.timerSpeed = 1.15f;
					} else {
						mc.timer.timerSpeed = 1.00f;
					}
				}
			}
			if (Splash.getInstance().getModuleManager().getModuleByClass(Flight.class).isModuleActive()) {
				ticksNeeded = 5;
				return;
			}
			if (ticksNeeded > 0) {
				if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround) {
					ticksNeeded --;
				}
				return;
			}
			if (stage == 0 || reset) { 
				if (mc.thePlayer.onGround) {
	            	e.setY(mc.thePlayer.motionY = e.getMotionY(0.399999987334013));
	            	speed = e.getMovementSpeed() * 2.149F;
	            	
				}
				stage = 0;
			} else if (stage == 1) {
            	speed = lastDistance - (.66 * (lastDistance - e.getMovementSpeed()));
			} else {
				speed = lastDistance - e.getMovementSpeed() / 33.399999987334013; 
			}
			e.setMoveSpeed(speed);
			stage++;
		}
	}
	
    private void setMotion(EventMove eventMove, double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float rotationYaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            eventMove.setX(0.0);
            eventMove.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    rotationYaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    rotationYaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            eventMove.setX(mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(rotationYaw + 88.0)) + strafe * speed * Math.sin(Math.toRadians(rotationYaw + 87.9000015258789)));
            eventMove.setZ(mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(rotationYaw + 88.0)) - strafe * speed * Math.cos(Math.toRadians(rotationYaw + 87.9000015258789)));
        }
    }
	
    public int getJumpEffect() {
        if (mc.thePlayer.isPotionActive(Potion.jump)) {
            return mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    
    public int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public boolean isInLiquid() {
        if (mc.thePlayer.isInWater()) {
            return true;
        }
        boolean b = false;
        final int n = (int)mc.thePlayer.getEntityBoundingBox().minY;
        for (int i = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); i < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; ++i) {
            for (int j = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); j < MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; ++j) {
                final Block getBlock = mc.theWorld.getBlockState(new BlockPos(i, n, j)).getBlock();
                if (getBlock != null && getBlock.getMaterial() != Material.air) {
                    if (!(getBlock instanceof BlockLiquid)) {
                        return false;
                    }
                    b = true;
                }
            }
        }
        return b;
    }
    
	public enum Mode {
		VANILLA, PVPTEMPLE, GWEN, WATCHDOG, SENTINEL, NCP, AGC,VAC, LOWHOP
	}
}
