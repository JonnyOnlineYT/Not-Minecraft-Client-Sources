package net.minecraft.world.storage.loot;

import com.google.common.collect.Sets;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

public class LootContext
{
    private final float luck;
    private final WorldServer worldObj;
    private final LootTableManager lootTableManager;
    @Nullable
    private final Entity lootedEntity;
    @Nullable
    private final EntityPlayer player;
    @Nullable
    private final DamageSource damageSource;
    private final Set<LootTable> lootTables = Sets.newLinkedHashSet();

    public LootContext(float luckIn, WorldServer worldIn, LootTableManager lootTableManagerIn, @Nullable Entity lootedEntityIn, @Nullable EntityPlayer playerIn, @Nullable DamageSource damageSourceIn)
    {
        luck = luckIn;
        worldObj = worldIn;
        lootTableManager = lootTableManagerIn;
        lootedEntity = lootedEntityIn;
        player = playerIn;
        damageSource = damageSourceIn;
    }

    @Nullable
    public Entity getLootedEntity()
    {
        return lootedEntity;
    }

    @Nullable
    public Entity getKillerPlayer()
    {
        return player;
    }

    @Nullable
    public Entity getKiller()
    {
        return damageSource == null ? null : damageSource.getEntity();
    }

    public boolean addLootTable(LootTable lootTableIn)
    {
        return lootTables.add(lootTableIn);
    }

    public void removeLootTable(LootTable lootTableIn)
    {
        lootTables.remove(lootTableIn);
    }

    public LootTableManager getLootTableManager()
    {
        return lootTableManager;
    }

    public float getLuck()
    {
        return luck;
    }

    @Nullable
    public Entity getEntity(LootContext.EntityTarget target)
    {
        switch (target)
        {
            case THIS:
                return getLootedEntity();

            case KILLER:
                return getKiller();

            case KILLER_PLAYER:
                return getKillerPlayer();

            default:
                return null;
        }
    }

    public static class Builder
    {
        private final WorldServer worldObj;
        private float luck;
        private Entity lootedEntity;
        private EntityPlayer player;
        private DamageSource damageSource;

        public Builder(WorldServer worldIn)
        {
            worldObj = worldIn;
        }

        public LootContext.Builder withLuck(float luckIn)
        {
            luck = luckIn;
            return this;
        }

        public LootContext.Builder withLootedEntity(Entity entityIn)
        {
            lootedEntity = entityIn;
            return this;
        }

        public LootContext.Builder withPlayer(EntityPlayer playerIn)
        {
            player = playerIn;
            return this;
        }

        public LootContext.Builder withDamageSource(DamageSource dmgSource)
        {
            damageSource = dmgSource;
            return this;
        }

        public LootContext build()
        {
            return new LootContext(luck, worldObj, worldObj.getLootTableManager(), lootedEntity, player, damageSource);
        }
    }

    public enum EntityTarget
    {
        THIS("this"),
        KILLER("killer"),
        KILLER_PLAYER("killer_player");

        private final String targetType;

        EntityTarget(String type)
        {
            targetType = type;
        }

        public static LootContext.EntityTarget fromString(String type)
        {
            for (LootContext.EntityTarget lootcontext$entitytarget : values())
            {
                if (lootcontext$entitytarget.targetType.equals(type))
                {
                    return lootcontext$entitytarget;
                }
            }

            throw new IllegalArgumentException("Invalid entity target " + type);
        }

        public static class Serializer extends TypeAdapter<LootContext.EntityTarget> {
            public void write(JsonWriter p_write_1_, LootContext.EntityTarget p_write_2_) throws IOException {
                p_write_1_.value(p_write_2_.targetType);
            }

            public LootContext.EntityTarget read(JsonReader p_read_1_) throws IOException {
                return fromString(p_read_1_.nextString());
            }
        }
    }
}
