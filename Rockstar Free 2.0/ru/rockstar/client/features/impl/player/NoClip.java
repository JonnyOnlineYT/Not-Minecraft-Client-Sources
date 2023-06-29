package ru.rockstar.client.features.impl.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventFullCube;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventReceivePacket;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class NoClip extends Feature {
    public static NumberSetting speed;
    public static NumberSetting magicspeed;
    public static BooleanSetting customSpeed;
    public static BooleanSetting onlyInBlocks;
    public static ListSetting mode;
    
    public NoClip() {
        super("NoClip", "Позволяет ходить сквозь стены", 0, Category.MOVEMENT);
        onlyInBlocks = new BooleanSetting("Only In Blocks", false, () -> true);
        mode = new ListSetting("NoClip Mode", "Default", () -> true, "Default", "MagicGrief", "Packet", "Really World");
        speed = new NumberSetting("Speed", 0.02F, 0F, 2, 0.01F, () -> true);
        magicspeed = new NumberSetting("TimerSpeed", 5F, 0F, 20, 1F, () -> mode.getOptions().equalsIgnoreCase("MagicGrief"));
        addSettings(mode, speed, magicspeed);
    }
    
    @EventTarget
    public void onUpdate(EventReceivePacket event) {
    	String Mode = mode.getOptions();
    	 if (Mode.equalsIgnoreCase("Default")) {
    		 mc.player.motionY = 0.00001;
    			mc.player.noClip = true;
    	            if (customSpeed.getBoolValue()) {
    	                MovementHelper.setSpeed(speed.getNumberValue() == 0 ? MovementHelper.getBaseMoveSpeed() : speed.getNumberValue());
    	            }
    	            if (mc.gameSettings.keyBindJump.isKeyDown()) {
    	                mc.player.motionY = 0.4;
    	            }
    	            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
    	                mc.player.motionY = -0.4;
    	            }
    	 } else if (Mode.equalsIgnoreCase("MagicGrief")) {
    			mc.player.motionY = 0.00001;
    			mc.player.noClip = true;
    	    		mc.timer.timerSpeed = magicspeed.getNumberValue();
    	    		mc.player.noClip = true;
    	    		mc.player.motionY = 0.00001;
    	            if (customSpeed.getBoolValue()) {
    	                MovementHelper.setSpeed(speed.getNumberValue() == 0 ? MovementHelper.getBaseMoveSpeed() : speed.getNumberValue());
    	            }
    	 } else if (Mode.equalsIgnoreCase("Packet")) {
    		 final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
             NoClip.mc.player.connection.sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
             NoClip.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), NoClip.mc.player.posY, packet.getZ(), packet.getYaw(), packet.getPitch(), false));
             NoClip.mc.player.setPosition(packet.getX(), NoClip.mc.player.posY, packet.getZ());
             event.setCancelled(true);
 	 } else if (mode.currentMode.equals("Really World")) {
 		double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;
        double yaw = mc.player.rotationYaw * 0.017453292;
        if (mc.player.isSneaking()) {
        	float endX = (float) ((float) x);
            float endY = (float) y - 2;
            float endZ = (float) ((float) z);
        	if(mc.player.ticksExisted % 8 == 0) {
                 mc.player.motionY += 0.2f;
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, true));
                 return;
            }
        }
        if (mc.gameSettings.keyBindForward.isKeyDown()) {
        	float endX = (float) ((float) x - Math.sin(yaw) * speed.getNumberValue());
            float endY = (float) y;
            float endZ = (float) ((float) z + Math.cos(yaw) * speed.getNumberValue());
        	if(mc.player.ticksExisted % 8 == 0) {
                 mc.player.motionY += 0.2f;
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, true));
                 return;
            }
        }
        if (mc.gameSettings.keyBindBack.isKeyDown()) {
        	float endX = (float) ((float) x + Math.sin(yaw) * speed.getNumberValue());
            float endY = (float) y;
            float endZ = (float) ((float) z - Math.cos(yaw) * speed.getNumberValue());
        	if(mc.player.ticksExisted % 8 == 0) {
                 mc.player.motionY += 0.2f;
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, true));
                 return;
            }
        }
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
        	float endX = (float) ((float) x);
            float endY = (float) y + 2;
            float endZ = (float) ((float) z);
        	if(mc.player.ticksExisted % 8 == 0) {
                 mc.player.motionY += 0.2f;
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, false));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX, endY, endZ, true));
                 mc.player.connection.sendPacket(new CPacketPlayer.Position(endX + 0.5, endY + 0.1, endZ - 0.5, true));
                 return;
            }
        }
 	 }
    }
    
    @EventTarget
    public void onFullCube(final EventFullCube event) {
            event.setCancelled(!NoClip.mode.currentMode.equals("Packet") && !NoClip.mode.currentMode.equals("New Lorent"));
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotionUpdate event) {
        if (NoClip.mode.currentMode.equals("Packet")) {
        	NoClip.mc.player.setVelocity(0.0, 0.0, 0.0);
            event.setCancelled(true);
            float speedY = 0.0f;
            if (NoClip.mc.player.movementInput.jump) {
                if (!this.timerHelper.hasReached(3000.0)) {
                    speedY = ((NoClip.mc.player.ticksExisted % 20 == 0) ? -0.04f : 0.031f);
                }
                else {
                    this.timerHelper.reset();
                    speedY = -0.08f;
                }
            }
            else if (NoClip.mc.player.movementInput.sneak) {
                speedY = -0.0031f;
            }
            final double[] dir = MovementHelper.forward(0.019999999552965164);
            NoClip.mc.player.motionX = dir[0];
            NoClip.mc.player.motionY = speedY;
            NoClip.mc.player.motionZ = dir[1];
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(NoClip.mc.player.posX + NoClip.mc.player.motionX, NoClip.mc.player.posY + NoClip.mc.player.motionY, NoClip.mc.player.posZ + NoClip.mc.player.motionZ, NoClip.mc.player.rotationYaw, NoClip.mc.player.rotationPitch, NoClip.mc.player.onGround));
            final double x = NoClip.mc.player.posX + NoClip.mc.player.motionX;
            double y = NoClip.mc.player.posY + NoClip.mc.player.motionY;
            final double z = NoClip.mc.player.posZ + NoClip.mc.player.motionZ;
            y += 1337.0;
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, NoClip.mc.player.onGround));
        }
    }

    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        if (NoClip.mode.currentMode.equals("Packet")) {
            NoClip.mc.player.setVelocity(0.0, 0.0, 0.0);
            event.setCancelled(true);
            float speedY = 0.0f;
            if (NoClip.mc.player.movementInput.jump) {
                if (!this.timerHelper.hasReached(3000.0)) {
                    speedY = ((NoClip.mc.player.ticksExisted % 20 == 0) ? -0.04f : 0.031f);
                }
                else {
                    this.timerHelper.reset();
                    speedY = -0.08f;
                }
            }
            else if (NoClip.mc.player.movementInput.sneak) {
                speedY = -0.0031f;
            }
            final double[] dir = MovementHelper.forward(this.speed.getNumberValue());
            NoClip.mc.player.motionX = dir[0];
            NoClip.mc.player.motionY = speedY;
            NoClip.mc.player.motionZ = dir[1];
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(NoClip.mc.player.posX + NoClip.mc.player.motionX, NoClip.mc.player.posY + NoClip.mc.player.motionY, NoClip.mc.player.posZ + NoClip.mc.player.motionZ, NoClip.mc.player.rotationYaw, NoClip.mc.player.rotationPitch, NoClip.mc.player.onGround));
            final double x = NoClip.mc.player.posX + NoClip.mc.player.motionX;
            double y = NoClip.mc.player.posY + NoClip.mc.player.motionY;
            final double z = NoClip.mc.player.posZ + NoClip.mc.player.motionZ;
            y += 1337.0;
            NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, NoClip.mc.player.onGround));
        }
    }


    public static boolean isNoClip(Entity entity) {
        if (Main.instance.featureDirector.getFeatureByClass(NoClip.class).isToggled() && mc.player != null
                && (mc.player.ridingEntity == null || entity == mc.player.ridingEntity))
            return true;
        return entity.noClip;

    }

    public void onDisable() {
    	mc.timer.timerSpeed = 1;
        mc.player.noClip = false;
        super.onDisable();
    }
}
