package org.moonware.client.feature.impl.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.Formatting;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

public class PearlLogger extends Feature {

    private boolean canSend;

    public PearlLogger() {
        super("PearlLogger", "Показывает координаты эндер-перла игроков", Type.Other);
    }

    @Override
    public void onEnable() {
        canSend = true;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (Minecraft.world == null || Minecraft.player == null)
            return;
        Entity enderPearl = null;
        for (Entity e : Minecraft.world.loadedEntityList) {
            if (e instanceof EntityEnderPearl) {
                enderPearl = e;
                break;
            }
        }
        if (enderPearl == null) {
            canSend = true;
            return;
        }
        EntityPlayer throwerEntity = null;
        for (EntityPlayer entity : Minecraft.world.playerEntities) {
            if (throwerEntity != null) {
                if (throwerEntity.getDistanceToEntity(enderPearl) <= entity.getDistanceToEntity(enderPearl)) {
                    continue;
                }
            }
            throwerEntity = entity;
        }
        String facing = enderPearl.getHorizontalFacing().toString();
        if (facing.equals("west")) {
            facing = "east";
        } else if (facing.equals("east")) {
            facing = "west";
        }
        if (throwerEntity == Minecraft.player) {
            return;
        }
        String pos = Formatting.GOLD + facing + Formatting.WHITE + " | " + Formatting.LIGHT_PURPLE + enderPearl.getPosition().getX() + " " + enderPearl.getPosition().getY() + " " + enderPearl.getPosition().getZ();
        if (throwerEntity != null && canSend) {
            MWUtils.sendChat(MoonWare.friendManager.isFriend(throwerEntity.getName()) ? Formatting.GREEN + throwerEntity.getName() + Formatting.WHITE + " thrown pearl on " + pos : Formatting.RED + throwerEntity.getName() + Formatting.WHITE + " thrown pearl on " + pos);
            canSend = false;
        }
    }
}
