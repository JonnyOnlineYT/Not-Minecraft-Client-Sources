package ua.apraxia.utility.math;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import ua.apraxia.Hexbyte;


public class CastUtility {

    private final List<EntityType> casts = new ArrayList<>();

    public static EntityType isInstanceof(Entity entity, EntityType... types) {
        for (EntityType type : types) {
            if (type == EntityType.VILLAGERS && entity instanceof EntityVillager) {
                return type;
            }
            if (type == EntityType.PLAYERS && entity instanceof EntityPlayer) {
                    return type;
                }
            if (type == EntityType.MOBS && entity instanceof EntityMob) {
                return type;
            }
            if (type == EntityType.ANIMALS && (entity instanceof EntityAnimal || entity instanceof EntityGolem)) {
                return type;
            }
        }
        return null;
    }

    public CastUtility apply(EntityType type) {
        this.casts.add(type);
        return this;
    }

    public EntityType[] build() {
        return this.casts.toArray(new EntityType[0]);
    }

    public enum EntityType {
        PLAYERS, MOBS, ANIMALS,FRIENDS,  VILLAGERS;
    }
}
