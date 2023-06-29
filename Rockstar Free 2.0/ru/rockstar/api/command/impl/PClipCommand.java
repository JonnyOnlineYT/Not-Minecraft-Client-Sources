package ru.rockstar.api.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import ru.rockstar.Main;
import ru.rockstar.api.command.CommandAbstract;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.world.InventoryHelper;
import ru.rockstar.client.features.impl.movement.Flight;

public class PClipCommand extends CommandAbstract {

    Minecraft mc = Minecraft.getMinecraft();

    public PClipCommand() {
        super("eclip", "eclip", "§6.pclip + | - §3<value> | down", "eclip");
    }

    @Override
    public void execute(String... args) {
    	boolean aboba = false;
    	boolean a1 = false;
    	boolean a2 = false;
    	boolean a3 = false;
    	boolean hui = false;
    	if (!mc.player.onGround && mc.player.fallDistance > 0 && a1) {
			mc.getConnection().sendPacket((Packet)new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
    		mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY - mc.player.posY - 2, mc.player.posZ);
		}
    	if (!mc.player.onGround && mc.player.fallDistance > 0 && a2) {
			mc.getConnection().sendPacket((Packet)new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
    		mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + Double.parseDouble(args[2]), mc.player.posZ);
		}
    	if (!mc.player.onGround && mc.player.fallDistance > 0 && a3) {
			mc.getConnection().sendPacket((Packet)new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
    		mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY - Double.parseDouble(args[2]), mc.player.posZ);
		}
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("eclip")) {
                try {
                    if (args[1].equals("down")) {
                    	a1 = true;
                    	if (hui == true) {
                			mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
                		}
                    		if (mc.player.onGround != true) {
                        		mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
                                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                                hui = true;
                        	}
                    		if (mc.player.onGround) {
                    			mc.player.jump();
                    			aboba = true;
                    		}
                    		if (!mc.player.onGround && mc.player.fallDistance > 0) {
                    			mc.getConnection().sendPacket((Packet)new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        		mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY - mc.player.posY - 2, mc.player.posZ);
                    		}
                    }
                    if (args[1].equals("+")) {
                    	a2 = true;
                    	if (hui == true) {
                			mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
                		}
                    		if (mc.player.onGround != true) {
                        		mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
                                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                                hui = true;
                        	}
                    		if (mc.player.onGround) {
                    			mc.player.jump();
                    		}
                    		if (!mc.player.onGround && mc.player.fallDistance > 0) {
                    			mc.getConnection().sendPacket((Packet)new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        		mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY + Double.parseDouble(args[2]), mc.player.posZ);
                    		}
                    		if (mc.player.onGround != true) {
                        		mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
                                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                                hui = true;
                        	}
                    }
                    if (args[1].equals("-")) {
                    	a3 = true;
                    	if (hui == true) {
                			mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
                		}
                    		if (mc.player.onGround != true) {
                        		mc.playerController.windowClick(0, InventoryHelper.getElytraAtHotbar(), 1, ClickType.PICKUP, mc.player);
                                mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, mc.player);
                                hui = true;
                        	}
                    		if (mc.player.onGround) {
                    			mc.player.jump();
                    		}
                    		if (!mc.player.onGround && mc.player.fallDistance > 0) {
                    			mc.getConnection().sendPacket((Packet)new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        		mc.player.setPositionAndUpdate(mc.player.posX, mc.player.posY - Double.parseDouble(args[2]), mc.player.posZ);
                    		}
                    }
                } catch (Exception ignored) {
                }
            }
        } else {
        	aboba = false;
        	a1 = false;
        	a2 = false;
        	a3 = false;
            Main.msg(getUsage(),true);
        }
    }
}
