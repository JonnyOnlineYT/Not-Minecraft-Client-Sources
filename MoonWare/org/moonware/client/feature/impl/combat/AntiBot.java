package org.moonware.client.feature.impl.combat;

import baritone.api.event.events.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.Formatting;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.event.events.impl.player.RespawnEvent;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AntiBot extends Feature {
    public static List<Entity> isBotPlayer = new ArrayList<>();
    public static ListSetting antiBotMode = new ListSetting("Anti Bot Mode", "Matrix", () -> true, "Matrix", "Reflex");
    public BooleanSetting tabCheck = new BooleanSetting("Tab Check", false, () -> true);
    public BooleanSetting invisIgnore = new BooleanSetting("Invisible Ignore", "���������� ��������� ���������", false, () -> true);
    private HashMap<String, Integer> TabTicks = new HashMap<>();
    public AntiBot() {
        super("AntiBot", "Удаляет ботов",  Type.Combat);
        addSettings(antiBotMode, tabCheck, invisIgnore);
    }

    public static boolean isInTablist(EntityLivingBase entity) {
        for (NetworkPlayerInfo bot : Minecraft.player.connection.getPlayerInfoMap()) {
            if (bot != null && bot.getGameProfile().getName().contains(entity.getName())) {
                return true;
            }
        }

        return false;
    }
    @EventTarget
    public void onTick(TickEvent event) {
        for (Entity player : Minecraft.world.loadedEntityList) {
            String name = Formatting.strip(player.getName());

            if (!TabTicks.containsKey(name)) {
                TabTicks.put(name, 0);
            }

            if (isInTablist((EntityLivingBase) player) && tabCheck.getBoolValue()) {
                int before = TabTicks.get(name);
                TabTicks.remove(name);
                TabTicks.put(name, before + 1);
            }
        }
    }

    @EventTarget
    public void onRespawn(RespawnEvent event) {
        TabTicks.clear();
    }
    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        String abmode = antiBotMode.getOptions();
        for (Entity entity : Minecraft.world.loadedEntityList) {
            switch (abmode) {
                case "Matrix":
                    if (entity != Minecraft.player && entity.ticksExisted < 5 && entity instanceof EntityOtherPlayerMP) {
                        if (((EntityOtherPlayerMP) entity).hurtTime > 0 && Minecraft.player.getDistanceToEntity(entity) <= 25 && Minecraft.player.connection.getPlayerInfo(entity.getUniqueID()).getResponseTime() != 0) {
                            isBotPlayer.add(entity);
                            //mc.world.removeEntity(entity);
                        }
                        if (isInTablist((EntityLivingBase) entity) && tabCheck.getBoolValue()) {
                            isBotPlayer.add(entity);
                            //mc.world.removeEntity(entity);
                        }
                    }
                    break;
                case "Reflex":
                    if (entity.getDisplayName().asString().length() == 8 && Minecraft.player.posY < entity.posY && entity.ticksExisted == 1 && !entity.isCollidedVertically && !entity.isEntityInsideOpaqueBlock() && entity.fallDistance == 0 && !(entity.posX == 0) && !(entity.posZ == 0)) {
                        isBotPlayer.add(entity);
                        break;
                    }
                    if (invisIgnore.getBoolValue() && entity.isInvisible() && entity != Minecraft.player) {
                        isBotPlayer.add(entity);
                    }
            }
        }
    }
    @Override
    public void onDisable() {
        isBotPlayer.clear();
        super.onDisable();
    }
    public List<Entity> getBotPlayers() {
        return isBotPlayer;
    }
}
