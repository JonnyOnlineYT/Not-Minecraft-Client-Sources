package net.minecraft.village;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;

public class VillageSiege
{
    private final World worldObj;
    private boolean hasSetupSiege;
    private int siegeState = -1;
    private int siegeCount;
    private int nextSpawnTime;

    /** Instance of Village. */
    private Village theVillage;
    private int spawnX;
    private int spawnY;
    private int spawnZ;

    public VillageSiege(World worldIn)
    {
        worldObj = worldIn;
    }

    /**
     * Runs a single tick for the village siege
     */
    public void tick()
    {
        if (worldObj.isDaytime())
        {
            siegeState = 0;
        }
        else if (siegeState != 2)
        {
            if (siegeState == 0)
            {
                float f = worldObj.getCelestialAngle(0.0F);

                if ((double)f < 0.5D || (double)f > 0.501D)
                {
                    return;
                }

                siegeState = worldObj.rand.nextInt(10) == 0 ? 1 : 2;
                hasSetupSiege = false;

                if (siegeState == 2)
                {
                    return;
                }
            }

            if (siegeState != -1)
            {
                if (!hasSetupSiege)
                {
                    if (!trySetupSiege())
                    {
                        return;
                    }

                    hasSetupSiege = true;
                }

                if (nextSpawnTime > 0)
                {
                    --nextSpawnTime;
                }
                else
                {
                    nextSpawnTime = 2;

                    if (siegeCount > 0)
                    {
                        spawnZombie();
                        --siegeCount;
                    }
                    else
                    {
                        siegeState = 2;
                    }
                }
            }
        }
    }

    private boolean trySetupSiege()
    {
        List<EntityPlayer> list = worldObj.playerEntities;
        Iterator iterator = list.iterator();

        while (true)
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            EntityPlayer entityplayer = (EntityPlayer)iterator.next();

            if (!entityplayer.isSpectator())
            {
                theVillage = worldObj.getVillageCollection().getNearestVillage(new BlockPos(entityplayer), 1);

                if (theVillage != null && theVillage.getNumVillageDoors() >= 10 && theVillage.getTicksSinceLastDoorAdding() >= 20 && theVillage.getNumVillagers() >= 20)
                {
                    BlockPos blockpos = theVillage.getCenter();
                    float f = (float) theVillage.getVillageRadius();
                    boolean flag = false;

                    for (int i = 0; i < 10; ++i)
                    {
                        float f1 = worldObj.rand.nextFloat() * ((float)Math.PI * 2F);
                        spawnX = blockpos.getX() + (int)((double)(MathHelper.cos(f1) * f) * 0.9D);
                        spawnY = blockpos.getY();
                        spawnZ = blockpos.getZ() + (int)((double)(MathHelper.sin(f1) * f) * 0.9D);
                        flag = false;

                        for (Village village : worldObj.getVillageCollection().getVillageList())
                        {
                            if (village != theVillage && village.isBlockPosWithinSqVillageRadius(new BlockPos(spawnX, spawnY, spawnZ)))
                            {
                                flag = true;
                                break;
                            }
                        }

                        if (!flag)
                        {
                            break;
                        }
                    }

                    if (flag)
                    {
                        return false;
                    }

                    Vec3d vec3d = findRandomSpawnPos(new BlockPos(spawnX, spawnY, spawnZ));

                    if (vec3d != null)
                    {
                        break;
                    }
                }
            }
        }

        nextSpawnTime = 0;
        siegeCount = 20;
        return true;
    }

    private boolean spawnZombie()
    {
        Vec3d vec3d = findRandomSpawnPos(new BlockPos(spawnX, spawnY, spawnZ));

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            EntityZombie entityzombie;

            try
            {
                entityzombie = new EntityZombie(worldObj);
                entityzombie.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(entityzombie)), null);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                return false;
            }

            entityzombie.setLocationAndAngles(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, worldObj.rand.nextFloat() * 360.0F, 0.0F);
            worldObj.spawnEntityInWorld(entityzombie);
            BlockPos blockpos = theVillage.getCenter();
            entityzombie.setHomePosAndDistance(blockpos, theVillage.getVillageRadius());
            return true;
        }
    }

    @Nullable
    private Vec3d findRandomSpawnPos(BlockPos pos)
    {
        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos = pos.add(worldObj.rand.nextInt(16) - 8, worldObj.rand.nextInt(6) - 3, worldObj.rand.nextInt(16) - 8);

            if (theVillage.isBlockPosWithinSqVillageRadius(blockpos) && WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldObj, blockpos))
            {
                return new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }

        return null;
    }
}
