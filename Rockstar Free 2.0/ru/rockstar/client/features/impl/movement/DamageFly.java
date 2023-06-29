package ru.rockstar.client.features.impl.movement;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ru.rockstar.Main;
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

public class DamageFly extends Feature {

    public static NumberSetting motionBoost;
    public TimerHelper timerUtils = new TimerHelper();
    boolean hui = false;
    boolean getDamage;
    boolean isVelocity;
    public static ListSetting flyMode;
    public static int tick;
    boolean velocity;
    double motion;
    public static int ticks;
    private final NumberSetting Ticks;
    private final NumberSetting Speed;
    public static BooleanSetting autoDisable;
    public static BooleanSetting autoBow;
    
    public DamageFly() {
        super("DamageFly", "Позволяет вам летать от дамага", 0, Category.MOVEMENT);
        this.Ticks = new NumberSetting("Ticks", 24f, 1f, 40f, 1f, () -> true);
        flyMode = new ListSetting("DamageFly Mode", "Old Matrix", () -> true, "Old Matrix", "New Matrix");
        this.Speed = new NumberSetting("Speed", 35f, 1f, 50f, 5f, () -> true);
        autoDisable = new BooleanSetting("AutoDisable", "Автоматически выключает дамагфлай", false, () -> true);
        autoBow = new BooleanSetting("AutoFastBow", "Автоматически выстреливает, как только лук натянулся", false, () -> true);
        this.addSettings(flyMode, Ticks, Speed, autoDisable, autoBow);
    }
    @EventTarget
    private void onPacket(EventReceivePacket event) {
    	 String mode = flyMode.getCurrentMode();
    	 if (mode.equalsIgnoreCase("Old Matrix")) {
    		 if (event.getPacket() instanceof SPacketEntityVelocity) {
    	            if (((SPacketEntityVelocity) event.getPacket()).getMotionY() > 0) {
    	                isVelocity = true;
    	            }
    	            if ((((SPacketEntityVelocity) event.getPacket()).getMotionY() / 8000.0D) > 0.2) {
    	                motion = (((SPacketEntityVelocity) event.getPacket()).getMotionY() / 8000.0D);
    	                velocity = true;
    	            }
    	        }
    	 } else {
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
    }
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    	String mode = flyMode.getCurrentMode();
   	 if (mode.equalsIgnoreCase("Old Matrix")) {
   		ticks = (int) Ticks.getNumberValue();
        if(mc.player.hurtTime == 9)
        {
            getDamage = true;
        }
        if(getDamage)
        {
        	mc.gameSettings.keyBindUseItem.pressed = false;
            this.tick += 1 / mc.timer.timerSpeed;
            mc.player.motionY = motion;
            mc.player.jumpMovementFactor = 0.01f * Speed.getNumberValue();
            MovementHelper.setSpeed(MovementHelper.getSpeed());
        }
        if(tick >= Ticks.getNumberValue())
        {
            getDamage = false;
            if (mc.player.onGround) {
            	tick = 0;
            }
            if (autoDisable.getBoolValue()) {
            	this.toggle();
            }
        }
        if (mc.player.onGround == true && getDamage != true && autoBow.getBoolValue() && mc.gameSettings.keyBindForward.pressed) {
    		if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isBowing() && mc.player.getItemInUseMaxCount() >= 1.5) {
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                mc.player.stopActiveHand();
            }
    	}
   	 } else {
   		 if (mc.player.hurtTime > 0) {
   			 if (mc.player.onGround) {
   				 mc.player.jump();
   			 }
   			MovementHelper.setSpeed(MovementHelper.getSpeed());
   			 mc.player.speedInAir = 0.2f;
   		 } else {
   			 mc.player.speedInAir = 0.02f;
   		 }
   	 }
    }
    
    @EventTarget
    public void onPreMotion(EventPreMotionUpdate event) {
    	String mode = flyMode.getCurrentMode();
    }
    
    @Override
    public void onDisable() {
    	 mc.player.jumpMovementFactor = 0.01f;
    	 mc.player.speedInAir = 0.02f;
    	mc.timer.timerSpeed = 1.0f;
    	super.onDisable();
    }
    @Override
    public void onEnable() {
    	getDamage = false;
    	tick = 0;
    	mc.timer.timerSpeed = 1.0f;
    	super.onEnable();
    }
}