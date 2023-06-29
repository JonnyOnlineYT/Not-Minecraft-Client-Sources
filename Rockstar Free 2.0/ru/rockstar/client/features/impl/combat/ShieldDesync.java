package ru.rockstar.client.features.impl.combat;

import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemShield;
import net.minecraft.world.*;
import ru.rockstar.*;
import ru.rockstar.api.event.*;
import ru.rockstar.api.event.event.*;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.*;
import ru.rockstar.client.features.impl.movement.Jesus;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class ShieldDesync extends Feature
{
	public static ListSetting desyncMode;
    public static NumberSetting desyncDelay = new NumberSetting("ShieldDesyncDelay", 10F, 0, 500, 10F, () -> true);
    public ShieldDesync() {
        super("ShieldDesync", "Десинкает щиты читов", 0, Category.COMBAT);
        desyncMode = new ListSetting("Sorting Mode", "Default", () -> true, "New", "Press", "Default", "Packet", "FastClick");
        addSettings(desyncMode, desyncDelay);
    }
    
    @EventTarget
    public void onEventPreMotion(final EventPreMotionUpdate event) {
    	if (KillAura.target != null) {
    		String mode = desyncMode.getOptions();
    		if (timerHelper.hasReached(desyncDelay.getNumberValue())) {
    			timerHelper.reset();
    		} else {
    			return;
    		}
        	if (mode.equalsIgnoreCase("Default")) {
        		if (Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled() && ShieldDesync.mc.player.isBlocking() && KillAura.target != null) {
                    ShieldDesync.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.NORTH));
                    ShieldDesync.mc.playerController.processRightClick(ShieldDesync.mc.player, ShieldDesync.mc.world, EnumHand.OFF_HAND);
                }
        	}
        	if (mode.equalsIgnoreCase("Press")) {
        		if (Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
        			if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
        				if ((mc.player.ticksExisted) % 0.01f == 0) {
        					mc.gameSettings.keyBindUseItem.pressed = false;
                		} else {
                			mc.gameSettings.keyBindUseItem.pressed = true;
                		}
        			}
        		}
        	}
        	if (mode.equalsIgnoreCase("FastClick")) {
        		if (Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
        			if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
        				if (KillAura.target == null) {
                			return;
                		}
                		if (KillAura.target != null) {
                			boolean shield;
                	        if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
                	            shield = true;
                	            mc.gameSettings.keyBindUseItem.pressed = shield;
                		} else {
                	            shield = false;
                	            mc.gameSettings.keyBindUseItem.pressed = shield;
                		}
        			}
        		}
        		}
        	}
        	if (mode.equalsIgnoreCase("Packet")) {
        		if (Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
        			if (Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
        				if (KillAura.target != null) {
        					if (timerHelper.hasReached(200)) {
        						if (ShieldDesync.mc.player.ticksExisted % 1 == 0) {
        							ShieldDesync.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.NORTH));
        	                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            					}
        					}
        				} else {
        					timerHelper.reset();
        				}
                    }
        		}
        	}
        	if (mode.equalsIgnoreCase("New")) {
        		if (Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
        			if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
        				 if (mc.player.getHeldItemOffhand().getItem() != Items.TOTEM_OF_UNDYING && InventoryHelper.getTotemAtHotbar() != -1) {
        					 if ((Jesus.mc.player.ticksExisted) % 1f == 0) {
             					mc.playerController.windowClick(0, InventoryHelper.getShieldAtHotbar(), 1, ClickType.QUICK_MOVE, mc.player);
             					mc.gameSettings.keyBindUseItem.pressed = true;
                     		} else {
                     			mc.playerController.windowClick(0, InventoryHelper.getShieldAtHotbar(), 1, ClickType.QUICK_MOVE, mc.player);
                     			mc.gameSettings.keyBindUseItem.pressed = true;
                     		}
        				 }
        			}
        		}
        	}
    	}
    }
}
