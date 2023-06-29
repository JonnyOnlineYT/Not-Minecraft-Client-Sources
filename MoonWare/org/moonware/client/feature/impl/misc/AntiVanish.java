package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventUpdate;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;

import java.util.ArrayList;
import java.util.List;

public class AntiVanish extends Feature {

    private final List<Entity> e = new ArrayList<>();

    public AntiVanish() {
        super("Anti Vanish", "Позволяет увидеть невидимых существ", Type.Other);
    }

    @Override
    public void onEnable() {
        for (Entity entity : e) {
            entity.setInvisible(true);
        }
        e.clear();
        super.onEnable();
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        for (Entity entity : Minecraft.world.loadedEntityList) {
            if (entity.isInvisible() && entity instanceof EntityPlayer) {
                entity.setInvisible(false);
                e.add(entity);
            }
        }
    }
}