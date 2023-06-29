package ru.rockstar.client.features.impl.movement;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventManager;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPacket;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventReceivePacket;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.notifications.NotificationPublisher;
import ru.rockstar.api.utils.notifications.NotificationType;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import static net.minecraft.util.math.MathHelper.cos;
import static net.minecraft.util.math.MathHelper.sin;

import java.util.Arrays;

import com.mojang.realmsclient.gui.ChatFormatting;

public class Flight extends Feature {

    public static ListSetting flyMode;
    private final BooleanSetting lagbackCheck;
    public static NumberSetting speed;
    public static NumberSetting motionBoost;
    public TimerHelper timerUtils = new TimerHelper();
    boolean hui = false;
    private boolean flaging = false;
    boolean getDamage;
    boolean isVelocity;
    int tick;
    boolean yes = true;
    boolean velocity;
    double motion;
    public Flight() {
        super("Flight", "Позволяет вам летать без креатив режима", 0, Category.MOVEMENT);
        this.lagbackCheck = new BooleanSetting("Lagback Check", "Возвращает назад если античит спамит чеками", false, () -> true);
        flyMode = new ListSetting("Flight Mode", "Vanilla", () -> true, "Vanilla", "Matrix Long", "Matrix New", "WellMore", "Matrix Glide", "Matrix FakeFlag", "Packet");
        motionBoost = new NumberSetting("Motion Boost", 0.6F, 0.1F, 8F, 0.1F, () -> flyMode.currentMode.equals("Matrix Damage"));
        speed = new NumberSetting("Flight Speed", 5F, 0.01F, 10F, 0.01F, () -> flyMode.currentMode.equals("Matrix New") || flyMode.currentMode.equals("Vanilla") || flyMode.currentMode.equalsIgnoreCase("Matrix Glide") ||  flyMode.currentMode.equals("WellMore"));
        //   TPAmount = new NumberSetting("TPAmount", 25, 5, 100, 1, () -> flyMode.currentMode.equals("ReallyWorld"));
        addSettings(flyMode, speed, lagbackCheck, motionBoost);
    }
    @EventTarget
    public void onPacket(EventReceivePacket event) {
        String modes = flyMode.getOptions();
        if (modes.equalsIgnoreCase("Matrix New")) {
        	if (event.getPacket() instanceof SPacketEntityVelocity) {
                if (((SPacketEntityVelocity) event.getPacket()).getMotionY() > 0) {
                    isVelocity = true;
                }
                if ((((SPacketEntityVelocity) event.getPacket()).getMotionY() / 8000.0D) > 0.2) {
                    motion = (((SPacketEntityVelocity) event.getPacket()).getMotionY() / 8000.0D);
                    velocity = true;
                }
            }
        }
        Packet<?> p = event.getPacket();
        if (modes.equalsIgnoreCase("WellMore")) {
            if (event.isIncoming() && timerHelper.hasReached(1000)) {
                if (p instanceof SPacketPlayerPosLook && mc.player != null) {
                    event.setCancelled(true);
                    timerHelper.reset();
                }
            }
        }
        
        if (flyMode.currentMode.equals("Packet")) {
              event.setCancelled(true); 
          }
    }
    @Override
    public void onDisable() {
        super.onDisable();
    	mc.timer.timerSpeed = 1;
        if (hui == true && mc.player.onGround) {
			mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
			hui = false;
		}
        mc.player.speedInAir = 0.02f;
        mc.player.capabilities.setFlySpeed(1.0f);
        mc.player.speedInAir = 0.02f;
        mc.timer.timerSpeed = 1.0f;
        mc.player.capabilities.isFlying = false;
        tick = 0;
        if (flyMode.getOptions().equalsIgnoreCase("WellMore")) {
            mc.player.motionY = 0;
            mc.player.motionX = 0;
            mc.player.motionZ = 0;
        }
    }
    
    @Override
    public void onEnable() {
    	yes = true;
        super.onEnable();
    }
    
    @EventTarget
    public void EventSendPacket(final EventPacket event) {
    	
    }
    
    public static boolean isNullOrEmpty(ItemStack stack) {
        return !(stack != null && !stack.isEmpty());
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    	int counter = 0;
        String mode = flyMode.getCurrentMode();
        if (mode.equalsIgnoreCase("Matrix Long")) {
        	if (!mc.player.onGround) {
        		counter++;
        		mc.player.motionY = 0.01f;
        		mc.timer.timerSpeed = mc.player.ticksExisted % 3 ==0 ? 2 : 0.2f;
        		
        	}
        	else {
        		counter = 0;
        		mc.timer.timerSpeed = 1;
        	}
            	}
        
        if (mode.equalsIgnoreCase("Matrix Glide")) {
            if (mc.player.onGround) {
                mc.player.jump();
                timerHelper.resetwatermark();
            } else if (!mc.player.onGround) {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                mc.player.motionY = -0.01;
                mc.player.capabilities.isFlying = true;
                mc.player.capabilities.setFlySpeed(speed.getNumberValue());
                mc.player.speedInAir = 0.02f;
                
            }
        }
        
        if (mode.equalsIgnoreCase("Matrix FakeFlag")) {
        	if (!yes)  return;
	        float yaw = (float) Math.toRadians(mc.player.rotationYaw);
	        mc.player.motionX = -sin(yaw) * 1.89;
	        mc.player.motionZ = cos(yaw) * 1.89;
	        mc.player.motionY = 0.42;
        }
    }
    
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        flaging = false;
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                flaging = true;
            }
    }
    /*
    if (mode.equalsIgnoreCase("MatrixGround")) {
    	mc.player.onGround = true;
        event.setGround(true);
        if (mc.player.onGround && mc.gameSettings.keyBindForward.pressed) {
            mc.player.jump();
            mc.player.onGround = true;
            event.setGround(true);
        }

        if (mc.player.motionY > 0 && !mc.player.isInWater()) {
            mc.player.motionY -= mc.player.fallDistance;
        }
    }
    */
    
    @EventTarget
    public void onLagbackSpeed(final EventReceivePacket e) {
    	String mode = flyMode.getCurrentMode();
    	 if (mode.equalsIgnoreCase("Matrix Long")) {
    	 } else {
    		 if (e.getPacket() instanceof SPacketPlayerPosLook && this.lagbackCheck.getBoolValue() && this.toggled) {
                 this.toggle();
                 Main.msg("Anti-cheat discovered flying", true);
             }
    	 }
    }
    
    

    @SuppressWarnings("null")
	@EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
        String mode = flyMode.getCurrentMode();

        setSuffix(flyMode.getCurrentMode(), true);
        if (mode.equalsIgnoreCase("Matrix Glide")) {
        	event.setGround(true);
        }
        else if (mode.equalsIgnoreCase("Matrix New")) {
        	int eIndex = -1;

            for (int i = 0; i < 45; i++) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                    eIndex = i;
                }
            }

            mc.player.motionY = 0.37D;

            if (mc.player.ticksExisted % 4 == 0) {
                mc.playerController.windowClick(0, eIndex, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(0, eIndex, 1, ClickType.PICKUP, mc.player);
            }

            mc.player.jumpMovementFactor = 0.115F;
            if (flaging) {
                mc.player.jumpMovementFactor = 0;
                mc.player.motionY = -0.5D;

            }
        } else if (mode.equalsIgnoreCase("Wellmore")) {
            if (mc.player.onGround) {
                mc.player.jump();
            } else {
                mc.player.motionX = 0;
                mc.player.motionZ = 0;
                mc.player.motionY = -0.01;
                MovementHelper.setSpeed(speed.getNumberValue());
                mc.player.speedInAir = 0.3f;
                if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.motionY -= speed.getNumberValue();
                } else if (mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.player.motionY += speed.getNumberValue();
                }
            }
        } else if (mode.equalsIgnoreCase("Vanilla")) {
            mc.player.capabilities.isFlying = true;
            MovementHelper.setSpeed(speed.getNumberValue());
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= 0.0001f;
            }
        }
        if (mode.equalsIgnoreCase("Packet")) {
            double speed;
			if (mc.player.movementInput.jump && !MovementHelper.isMoving()) {
				speed = 0.062D;
				mc.player.noClip = false;
			}
			else
				speed = mc.player.movementInput.sneak ? -0.062D : 0.0D;
			mc.player.noClip = true;
            double strafing = (double) MovementHelper.getSpeed();
            for (int i = 1; i < 2; i++) {
              mc.player.motionX = strafing * i * 1.0D;
              mc.player.motionY = speed * i;
              mc.player.motionZ = strafing * i * 1.0D;
              mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.motionX, mc.player.motionY, mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
            }
        }
        
    }
    @EventTarget
    public void onPacketReceive(final EventReceivePacket event) {
        if (Flight.flyMode.currentMode.equals("Packet") && event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packet.yaw = Flight.mc.player.rotationYaw;
            packet.pitch = Flight.mc.player.rotationPitch;
        }
        
        String mode = flyMode.getCurrentMode();
        if (mode.equalsIgnoreCase("Matrix Glide") && toggled) {
        	if (mc.gameSettings.keyBindJump.isKeyDown()) {
                if (timerHelper.hasReached(250)) {
                    mc.player.onGround = true;
                    mc.player.jump();
                    timerHelper.reset();
                }
            }
        }
        if (mode.equalsIgnoreCase("Matrix FakeFlag") && toggled) {
        	if(event.getPacket() instanceof SPacketPlayerPosLook) {
                yes = false;
                return;
            }	
        }
    }
}