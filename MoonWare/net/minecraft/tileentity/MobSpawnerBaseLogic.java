package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Namespaced;
import net.minecraft.util.StringUtils;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public abstract class MobSpawnerBaseLogic
{
    /** The delay to spawn. */
    private int spawnDelay = 20;
    private final List<WeightedSpawnerEntity> potentialSpawns = Lists.newArrayList();
    private WeightedSpawnerEntity randomEntity = new WeightedSpawnerEntity();

    /** The rotation of the mob inside the mob spawner */
    private double mobRotation;

    /** the previous rotation of the mob inside the mob spawner */
    private double prevMobRotation;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;

    /** Cached instance of the entity to render inside the spawner. */
    private Entity cachedEntity;
    private int maxNearbyEntities = 6;

    /** The distance from which a player activates the spawner. */
    private int activatingRangeFromPlayer = 16;

    /** The range coefficient for spawning entities around. */
    private int spawnRange = 4;

    @Nullable
    private Namespaced func_190895_g()
    {
        String s = randomEntity.getNbt().getString("id");
        return StringUtils.isNullOrEmpty(s) ? null : new Namespaced(s);
    }

    public void func_190894_a(@Nullable Namespaced p_190894_1_)
    {
        if (p_190894_1_ != null)
        {
            randomEntity.getNbt().setString("id", p_190894_1_.toString());
        }
    }

    /**
     * Returns true if there's a player close enough to this mob spawner to activate it.
     */
    private boolean isActivated()
    {
        BlockPos blockpos = getSpawnerPosition();
        return getSpawnerWorld().isAnyPlayerWithinRangeAt((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, activatingRangeFromPlayer);
    }

    public void updateSpawner()
    {
        if (!isActivated())
        {
            prevMobRotation = mobRotation;
        }
        else
        {
            BlockPos blockpos = getSpawnerPosition();

            if (getSpawnerWorld().isRemote)
            {
                double d3 = (float)blockpos.getX() + getSpawnerWorld().rand.nextFloat();
                double d4 = (float)blockpos.getY() + getSpawnerWorld().rand.nextFloat();
                double d5 = (float)blockpos.getZ() + getSpawnerWorld().rand.nextFloat();
                getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D);
                getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);

                if (spawnDelay > 0)
                {
                    --spawnDelay;
                }

                prevMobRotation = mobRotation;
                mobRotation = (mobRotation + (double)(1000.0F / ((float) spawnDelay + 200.0F))) % 360.0D;
            }
            else
            {
                if (spawnDelay == -1)
                {
                    resetTimer();
                }

                if (spawnDelay > 0)
                {
                    --spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < spawnCount; ++i)
                {
                    NBTTagCompound nbttagcompound = randomEntity.getNbt();
                    NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
                    World world = getSpawnerWorld();
                    int j = nbttaglist.tagCount();
                    double d0 = j >= 1 ? nbttaglist.getDoubleAt(0) : (double)blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) spawnRange + 0.5D;
                    double d1 = j >= 2 ? nbttaglist.getDoubleAt(1) : (double)(blockpos.getY() + world.rand.nextInt(3) - 1);
                    double d2 = j >= 3 ? nbttaglist.getDoubleAt(2) : (double)blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double) spawnRange + 0.5D;
                    Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, false);

                    if (entity == null)
                    {
                        return;
                    }

                    int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1)).expandXyz(spawnRange)).size();

                    if (k >= maxNearbyEntities)
                    {
                        resetTimer();
                        return;
                    }

                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0F, 0.0F);

                    if (entityliving == null || entityliving.getCanSpawnHere() && entityliving.isNotColliding())
                    {
                        if (randomEntity.getNbt().getSize() == 1 && randomEntity.getNbt().hasKey("id", 8) && entity instanceof EntityLiving)
                        {
                            ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), null);
                        }

                        AnvilChunkLoader.spawnEntity(entity, world);
                        world.playEvent(2004, blockpos, 0);

                        if (entityliving != null)
                        {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }

                if (flag)
                {
                    resetTimer();
                }
            }
        }
    }

    private void resetTimer()
    {
        if (maxSpawnDelay <= minSpawnDelay)
        {
            spawnDelay = minSpawnDelay;
        }
        else
        {
            int i = maxSpawnDelay - minSpawnDelay;
            spawnDelay = minSpawnDelay + getSpawnerWorld().rand.nextInt(i);
        }

        if (!potentialSpawns.isEmpty())
        {
            setNextSpawnData(WeightedRandom.getRandomItem(getSpawnerWorld().rand, potentialSpawns));
        }

        broadcastEvent(1);
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        spawnDelay = nbt.getShort("Delay");
        potentialSpawns.clear();

        if (nbt.hasKey("SpawnPotentials", 9))
        {
            NBTTagList nbttaglist = nbt.getTagList("SpawnPotentials", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                potentialSpawns.add(new WeightedSpawnerEntity(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (nbt.hasKey("SpawnData", 10))
        {
            setNextSpawnData(new WeightedSpawnerEntity(1, nbt.getCompoundTag("SpawnData")));
        }
        else if (!potentialSpawns.isEmpty())
        {
            setNextSpawnData(WeightedRandom.getRandomItem(getSpawnerWorld().rand, potentialSpawns));
        }

        if (nbt.hasKey("MinSpawnDelay", 99))
        {
            minSpawnDelay = nbt.getShort("MinSpawnDelay");
            maxSpawnDelay = nbt.getShort("MaxSpawnDelay");
            spawnCount = nbt.getShort("SpawnCount");
        }

        if (nbt.hasKey("MaxNearbyEntities", 99))
        {
            maxNearbyEntities = nbt.getShort("MaxNearbyEntities");
            activatingRangeFromPlayer = nbt.getShort("RequiredPlayerRange");
        }

        if (nbt.hasKey("SpawnRange", 99))
        {
            spawnRange = nbt.getShort("SpawnRange");
        }

        if (getSpawnerWorld() != null)
        {
            cachedEntity = null;
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound p_189530_1_)
    {
        Namespaced resourcelocation = func_190895_g();

        if (resourcelocation == null)
        {
            return p_189530_1_;
        }
        else
        {
            p_189530_1_.setShort("Delay", (short) spawnDelay);
            p_189530_1_.setShort("MinSpawnDelay", (short) minSpawnDelay);
            p_189530_1_.setShort("MaxSpawnDelay", (short) maxSpawnDelay);
            p_189530_1_.setShort("SpawnCount", (short) spawnCount);
            p_189530_1_.setShort("MaxNearbyEntities", (short) maxNearbyEntities);
            p_189530_1_.setShort("RequiredPlayerRange", (short) activatingRangeFromPlayer);
            p_189530_1_.setShort("SpawnRange", (short) spawnRange);
            p_189530_1_.setTag("SpawnData", randomEntity.getNbt().copy());
            NBTTagList nbttaglist = new NBTTagList();

            if (potentialSpawns.isEmpty())
            {
                nbttaglist.appendTag(randomEntity.toCompoundTag());
            }
            else
            {
                for (WeightedSpawnerEntity weightedspawnerentity : potentialSpawns)
                {
                    nbttaglist.appendTag(weightedspawnerentity.toCompoundTag());
                }
            }

            p_189530_1_.setTag("SpawnPotentials", nbttaglist);
            return p_189530_1_;
        }
    }

    public Entity getCachedEntity()
    {
        if (cachedEntity == null)
        {
            cachedEntity = AnvilChunkLoader.readWorldEntity(randomEntity.getNbt(), getSpawnerWorld(), false);

            if (randomEntity.getNbt().getSize() == 1 && randomEntity.getNbt().hasKey("id", 8) && cachedEntity instanceof EntityLiving)
            {
                ((EntityLiving) cachedEntity).onInitialSpawn(getSpawnerWorld().getDifficultyForLocation(new BlockPos(cachedEntity)), null);
            }
        }

        return cachedEntity;
    }

    /**
     * Sets the delay to minDelay if parameter given is 1, else return false.
     */
    public boolean setDelayToMin(int delay)
    {
        if (delay == 1 && getSpawnerWorld().isRemote)
        {
            spawnDelay = minSpawnDelay;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_)
    {
        randomEntity = p_184993_1_;
    }

    public abstract void broadcastEvent(int id);

    public abstract World getSpawnerWorld();

    public abstract BlockPos getSpawnerPosition();

    public double getMobRotation()
    {
        return mobRotation;
    }

    public double getPrevMobRotation()
    {
        return prevMobRotation;
    }
}
