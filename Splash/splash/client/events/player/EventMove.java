package splash.client.events.player;


import me.hippo.systems.lwjeb.event.Cancelable;
import me.hippo.systems.lwjeb.event.MultiStage;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import splash.Splash;
import splash.client.modules.combat.Aura;
import splash.client.modules.combat.TargetStrafe;
import splash.client.modules.movement.Flight;
import splash.utilities.math.rotation.RotationUtils;
import splash.utilities.player.MovementUtils;

/**
 * Author: Ice
 * Created: 17:35, 30-May-20
 * Project: Client
 */
public class EventMove extends Cancelable {
	public static int stillTime;
	public static double distance;
	private double x;
	private double y;
	private double z;
    private boolean onGround;

    public EventMove(double x, double y, double z, boolean onGround) {
		Minecraft mc = Minecraft.getMinecraft();
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
        if (stillTime > 0) {
        	if (onGround) {
        		double speed = .15;
        		this.x = (-Math.sin(mc.thePlayer.getDirection())) * speed;
        		this.z = Math.cos(mc.thePlayer.getDirection()) * speed;
        	} else { 
        		this.z = 0;
        		this.x = 0;
        	}
            stillTime--;
        }
    }
    
    public double getMotionY(double mY) {
		if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
			mY += (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1;
		}
		return mY;
    }
    
    public double getLegitMotion() {
    	return 0.41999998688697815D;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
    
    public double getMovementSpeed() {
        double baseSpeed = 0.2873D;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public double getMovementSpeed(double baseSpeed) { 
    	double speed = baseSpeed;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            return speed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return speed;
    }
    
    double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward, strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe,
            yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;


    public void setMoveSpeed(double moveSpeed) {
    	Minecraft mc = Minecraft.getMinecraft();
		double range = ((Aura) Splash.getInstance().getModuleManager().getModuleByClass(Aura.class)).range.getValue();
		MovementInput movementInput = mc.thePlayer.movementInput;
		double moveForward = movementInput.getForward();
		boolean targetStrafe = TargetStrafe.canStrafe();
		if (targetStrafe) {
			if (mc.thePlayer.getDistanceToEntity(Aura.currentEntity) <= ((TargetStrafe) Splash.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class)).distance.getValue()) {
				moveForward = 0;
			} else {
				moveForward = 1;
			}
		}
		double moveStrafe = targetStrafe ? TargetStrafe.direction : movementInput.getStrafe();
		double yaw = targetStrafe ? RotationUtils.getNeededRotations(Aura.currentEntity)[0] : mc.thePlayer.rotationYaw;
		if (moveForward == 0.0D && moveStrafe == 0.0D) {
			setX(0.0D);
			setZ(0.0D);
		} else {
			if (moveStrafe > 0) {
				moveStrafe = 1;
			} else if (moveStrafe < 0) {
				moveStrafe = -1;
			}
			if (moveForward != 0.0D) {
				if (moveStrafe > 0.0D) {
					yaw += (moveForward > 0.0D ? -45 : 45);
				} else if (moveStrafe < 0.0D) {
					yaw += (moveForward > 0.0D ? 45 : -45);
				}
				moveStrafe = 0.0D;
				if (moveForward > 0.0D) {
					moveForward = 1.0D;
				} else if (moveForward < 0.0D) {
					moveForward = -1.0D;
				}
			}
			setX(moveForward * moveSpeed * Math.cos(Math.toRadians(yaw + 88.0)) + moveStrafe * moveSpeed * Math.sin(Math.toRadians(yaw + 87.9000015258789)));
			setZ(moveForward * moveSpeed * Math.sin(Math.toRadians(yaw + 88.0)) - moveStrafe * moveSpeed * Math.cos(Math.toRadians(yaw + 87.9000015258789)));
		}
    }
    public double getJumpBoostModifier(double baseJumpHeight) {
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
            baseJumpHeight += (float) (amplifier + 1) * 0.1F;
        }

        return baseJumpHeight;
    }
}
