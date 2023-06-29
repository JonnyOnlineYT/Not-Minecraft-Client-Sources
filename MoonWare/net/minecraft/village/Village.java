package net.minecraft.village;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Village
{
    private World worldObj;
    private final List<VillageDoorInfo> villageDoorInfoList = Lists.newArrayList();

    /**
     * This is the sum of all door coordinates and used to calculate the actual village center by dividing by the number
     * of doors.
     */
    private BlockPos centerHelper = BlockPos.ORIGIN;

    /** This is the actual village center. */
    private BlockPos center = BlockPos.ORIGIN;
    private int villageRadius;
    private int lastAddDoorTimestamp;
    private int tickCounter;
    private int numVillagers;

    /** Timestamp of tick count when villager last bred */
    private int noBreedTicks;
    private final Map<String, Integer> playerReputation = Maps.newHashMap();
    private final List<Village.VillageAggressor> villageAgressors = Lists.newArrayList();
    private int numIronGolems;

    public Village()
    {
    }

    public Village(World worldIn)
    {
        worldObj = worldIn;
    }

    public void setWorld(World worldIn)
    {
        worldObj = worldIn;
    }

    /**
     * Called periodically by VillageCollection
     */
    public void tick(int tickCounterIn)
    {
        tickCounter = tickCounterIn;
        removeDeadAndOutOfRangeDoors();
        removeDeadAndOldAgressors();

        if (tickCounterIn % 20 == 0)
        {
            updateNumVillagers();
        }

        if (tickCounterIn % 30 == 0)
        {
            updateNumIronGolems();
        }

        int i = numVillagers / 10;

        if (numIronGolems < i && villageDoorInfoList.size() > 20 && worldObj.rand.nextInt(7000) == 0)
        {
            Vec3d vec3d = findRandomSpawnPos(center, 2, 4, 2);

            if (vec3d != null)
            {
                EntityIronGolem entityirongolem = new EntityIronGolem(worldObj);
                entityirongolem.setPosition(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
                worldObj.spawnEntityInWorld(entityirongolem);
                ++numIronGolems;
            }
        }
    }

    private Vec3d findRandomSpawnPos(BlockPos pos, int x, int y, int z)
    {
        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos = pos.add(worldObj.rand.nextInt(16) - 8, worldObj.rand.nextInt(6) - 3, worldObj.rand.nextInt(16) - 8);

            if (isBlockPosWithinSqVillageRadius(blockpos) && isAreaClearAround(new BlockPos(x, y, z), blockpos))
            {
                return new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            }
        }

        return null;
    }

    /**
     * Checks to see if the volume around blockLocation is clear and able to fit blockSize
     */
    private boolean isAreaClearAround(BlockPos blockSize, BlockPos blockLocation)
    {
        if (!worldObj.getBlockState(blockLocation.down()).isFullyOpaque())
        {
            return false;
        }
        else
        {
            int i = blockLocation.getX() - blockSize.getX() / 2;
            int j = blockLocation.getZ() - blockSize.getZ() / 2;

            for (int k = i; k < i + blockSize.getX(); ++k)
            {
                for (int l = blockLocation.getY(); l < blockLocation.getY() + blockSize.getY(); ++l)
                {
                    for (int i1 = j; i1 < j + blockSize.getZ(); ++i1)
                    {
                        if (worldObj.getBlockState(new BlockPos(k, l, i1)).isNormalCube())
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private void updateNumIronGolems()
    {
        List<EntityIronGolem> list = worldObj.getEntitiesWithinAABB(EntityIronGolem.class, new AxisAlignedBB(center.getX() - villageRadius, center.getY() - 4, center.getZ() - villageRadius, center.getX() + villageRadius, center.getY() + 4, center.getZ() + villageRadius));
        numIronGolems = list.size();
    }

    private void updateNumVillagers()
    {
        List<EntityVillager> list = worldObj.getEntitiesWithinAABB(EntityVillager.class, new AxisAlignedBB(center.getX() - villageRadius, center.getY() - 4, center.getZ() - villageRadius, center.getX() + villageRadius, center.getY() + 4, center.getZ() + villageRadius));
        numVillagers = list.size();

        if (numVillagers == 0)
        {
            playerReputation.clear();
        }
    }

    public BlockPos getCenter()
    {
        return center;
    }

    public int getVillageRadius()
    {
        return villageRadius;
    }

    /**
     * Actually get num village door info entries, but that boils down to number of doors. Called by
     * EntityAIVillagerMate and VillageSiege
     */
    public int getNumVillageDoors()
    {
        return villageDoorInfoList.size();
    }

    public int getTicksSinceLastDoorAdding()
    {
        return tickCounter - lastAddDoorTimestamp;
    }

    public int getNumVillagers()
    {
        return numVillagers;
    }

    /**
     * Checks to see if the distance squared between this BlockPos and the center of this Village is less than this
     * Village's villageRadius squared
     */
    public boolean isBlockPosWithinSqVillageRadius(BlockPos pos)
    {
        return center.distanceSq(pos) < (double)(villageRadius * villageRadius);
    }

    public List<VillageDoorInfo> getVillageDoorInfoList()
    {
        return villageDoorInfoList;
    }

    public VillageDoorInfo getNearestDoor(BlockPos pos)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;

        for (VillageDoorInfo villagedoorinfo1 : villageDoorInfoList)
        {
            int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);

            if (j < i)
            {
                villagedoorinfo = villagedoorinfo1;
                i = j;
            }
        }

        return villagedoorinfo;
    }

    /**
     * Returns {@link net.minecraft.village.VillageDoorInfo VillageDoorInfo} from given block position
     */
    public VillageDoorInfo getDoorInfo(BlockPos pos)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = Integer.MAX_VALUE;

        for (VillageDoorInfo villagedoorinfo1 : villageDoorInfoList)
        {
            int j = villagedoorinfo1.getDistanceToDoorBlockSq(pos);

            if (j > 256)
            {
                j = j * 1000;
            }
            else
            {
                j = villagedoorinfo1.getDoorOpeningRestrictionCounter();
            }

            if (j < i)
            {
                BlockPos blockpos = villagedoorinfo1.getDoorBlockPos();
                EnumFacing enumfacing = villagedoorinfo1.getInsideDirection();

                if (worldObj.getBlockState(blockpos.offset(enumfacing, 1)).getBlock().isPassable(worldObj, blockpos.offset(enumfacing, 1)) && worldObj.getBlockState(blockpos.offset(enumfacing, -1)).getBlock().isPassable(worldObj, blockpos.offset(enumfacing, -1)) && worldObj.getBlockState(blockpos.up().offset(enumfacing, 1)).getBlock().isPassable(worldObj, blockpos.up().offset(enumfacing, 1)) && worldObj.getBlockState(blockpos.up().offset(enumfacing, -1)).getBlock().isPassable(worldObj, blockpos.up().offset(enumfacing, -1)))
                {
                    villagedoorinfo = villagedoorinfo1;
                    i = j;
                }
            }
        }

        return villagedoorinfo;
    }

    @Nullable

    /**
     * if door not existed in this village, null will be returned
     */
    public VillageDoorInfo getExistedDoor(BlockPos doorBlock)
    {
        if (center.distanceSq(doorBlock) > (double)(villageRadius * villageRadius))
        {
            return null;
        }
        else
        {
            for (VillageDoorInfo villagedoorinfo : villageDoorInfoList)
            {
                if (villagedoorinfo.getDoorBlockPos().getX() == doorBlock.getX() && villagedoorinfo.getDoorBlockPos().getZ() == doorBlock.getZ() && Math.abs(villagedoorinfo.getDoorBlockPos().getY() - doorBlock.getY()) <= 1)
                {
                    return villagedoorinfo;
                }
            }

            return null;
        }
    }

    public void addVillageDoorInfo(VillageDoorInfo doorInfo)
    {
        villageDoorInfoList.add(doorInfo);
        centerHelper = centerHelper.add(doorInfo.getDoorBlockPos());
        updateVillageRadiusAndCenter();
        lastAddDoorTimestamp = doorInfo.getInsidePosY();
    }

    /**
     * Returns true, if there is not a single village door left. Called by VillageCollection
     */
    public boolean isAnnihilated()
    {
        return villageDoorInfoList.isEmpty();
    }

    public void addOrRenewAgressor(EntityLivingBase entitylivingbaseIn)
    {
        for (Village.VillageAggressor village$villageaggressor : villageAgressors)
        {
            if (village$villageaggressor.agressor == entitylivingbaseIn)
            {
                village$villageaggressor.agressionTime = tickCounter;
                return;
            }
        }

        villageAgressors.add(new Village.VillageAggressor(entitylivingbaseIn, tickCounter));
    }

    @Nullable
    public EntityLivingBase findNearestVillageAggressor(EntityLivingBase entitylivingbaseIn)
    {
        double d0 = Double.MAX_VALUE;
        Village.VillageAggressor village$villageaggressor = null;

        for (int i = 0; i < villageAgressors.size(); ++i)
        {
            Village.VillageAggressor village$villageaggressor1 = villageAgressors.get(i);
            double d1 = village$villageaggressor1.agressor.getDistanceSqToEntity(entitylivingbaseIn);

            if (d1 <= d0)
            {
                village$villageaggressor = village$villageaggressor1;
                d0 = d1;
            }
        }

        return village$villageaggressor == null ? null : village$villageaggressor.agressor;
    }

    public EntityPlayer getNearestTargetPlayer(EntityLivingBase villageDefender)
    {
        double d0 = Double.MAX_VALUE;
        EntityPlayer entityplayer = null;

        for (String s : playerReputation.keySet())
        {
            if (isPlayerReputationTooLow(s))
            {
                EntityPlayer entityplayer1 = worldObj.getPlayerEntityByName(s);

                if (entityplayer1 != null)
                {
                    double d1 = entityplayer1.getDistanceSqToEntity(villageDefender);

                    if (d1 <= d0)
                    {
                        entityplayer = entityplayer1;
                        d0 = d1;
                    }
                }
            }
        }

        return entityplayer;
    }

    private void removeDeadAndOldAgressors()
    {
        Iterator<Village.VillageAggressor> iterator = villageAgressors.iterator();

        while (iterator.hasNext())
        {
            Village.VillageAggressor village$villageaggressor = iterator.next();

            if (!village$villageaggressor.agressor.isEntityAlive() || Math.abs(tickCounter - village$villageaggressor.agressionTime) > 300)
            {
                iterator.remove();
            }
        }
    }

    private void removeDeadAndOutOfRangeDoors()
    {
        boolean flag = false;
        boolean flag1 = worldObj.rand.nextInt(50) == 0;
        Iterator<VillageDoorInfo> iterator = villageDoorInfoList.iterator();

        while (iterator.hasNext())
        {
            VillageDoorInfo villagedoorinfo = iterator.next();

            if (flag1)
            {
                villagedoorinfo.resetDoorOpeningRestrictionCounter();
            }

            if (!isWoodDoor(villagedoorinfo.getDoorBlockPos()) || Math.abs(tickCounter - villagedoorinfo.getInsidePosY()) > 1200)
            {
                centerHelper = centerHelper.subtract(villagedoorinfo.getDoorBlockPos());
                flag = true;
                villagedoorinfo.setIsDetachedFromVillageFlag(true);
                iterator.remove();
            }
        }

        if (flag)
        {
            updateVillageRadiusAndCenter();
        }
    }

    private boolean isWoodDoor(BlockPos pos)
    {
        IBlockState iblockstate = worldObj.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block instanceof BlockDoor)
        {
            return iblockstate.getMaterial() == Material.WOOD;
        }
        else
        {
            return false;
        }
    }

    private void updateVillageRadiusAndCenter()
    {
        int i = villageDoorInfoList.size();

        if (i == 0)
        {
            center = BlockPos.ORIGIN;
            villageRadius = 0;
        }
        else
        {
            center = new BlockPos(centerHelper.getX() / i, centerHelper.getY() / i, centerHelper.getZ() / i);
            int j = 0;

            for (VillageDoorInfo villagedoorinfo : villageDoorInfoList)
            {
                j = Math.max(villagedoorinfo.getDistanceToDoorBlockSq(center), j);
            }

            villageRadius = Math.max(32, (int)Math.sqrt(j) + 1);
        }
    }

    /**
     * Return the village reputation for a player
     */
    public int getPlayerReputation(String playerName)
    {
        Integer integer = playerReputation.get(playerName);
        return integer == null ? 0 : integer.intValue();
    }

    /**
     * Modify a players reputation in the village. Use positive values to increase reputation and negative values to
     * decrease. <br>Note that a players reputation is clamped between -30 and 10
     */
    public int modifyPlayerReputation(String playerName, int reputation)
    {
        int i = getPlayerReputation(playerName);
        int j = MathHelper.clamp(i + reputation, -30, 10);
        playerReputation.put(playerName, Integer.valueOf(j));
        return j;
    }

    /**
     * Return whether this player has a too low reputation with this village.
     */
    public boolean isPlayerReputationTooLow(String playerName)
    {
        return getPlayerReputation(playerName) <= -15;
    }

    /**
     * Read this village's data from NBT.
     */
    public void readVillageDataFromNBT(NBTTagCompound compound)
    {
        numVillagers = compound.getInteger("PopSize");
        villageRadius = compound.getInteger("Radius");
        numIronGolems = compound.getInteger("Golems");
        lastAddDoorTimestamp = compound.getInteger("Stable");
        tickCounter = compound.getInteger("Tick");
        noBreedTicks = compound.getInteger("MTick");
        center = new BlockPos(compound.getInteger("CX"), compound.getInteger("CY"), compound.getInteger("CZ"));
        centerHelper = new BlockPos(compound.getInteger("ACX"), compound.getInteger("ACY"), compound.getInteger("ACZ"));
        NBTTagList nbttaglist = compound.getTagList("Doors", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            VillageDoorInfo villagedoorinfo = new VillageDoorInfo(new BlockPos(nbttagcompound.getInteger("X"), nbttagcompound.getInteger("Y"), nbttagcompound.getInteger("Z")), nbttagcompound.getInteger("IDX"), nbttagcompound.getInteger("IDZ"), nbttagcompound.getInteger("TS"));
            villageDoorInfoList.add(villagedoorinfo);
        }

        NBTTagList nbttaglist1 = compound.getTagList("Players", 10);

        for (int j = 0; j < nbttaglist1.tagCount(); ++j)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist1.getCompoundTagAt(j);

            if (nbttagcompound1.hasKey("UUID") && worldObj != null && worldObj.getInstanceServer() != null)
            {
                PlayerProfileCache playerprofilecache = worldObj.getInstanceServer().getPlayerProfileCache();
                GameProfile gameprofile = playerprofilecache.getProfileByUUID(UUID.fromString(nbttagcompound1.getString("UUID")));

                if (gameprofile != null)
                {
                    playerReputation.put(gameprofile.getName(), Integer.valueOf(nbttagcompound1.getInteger("S")));
                }
            }
            else
            {
                playerReputation.put(nbttagcompound1.getString("Name"), Integer.valueOf(nbttagcompound1.getInteger("S")));
            }
        }
    }

    /**
     * Write this village's data to NBT.
     */
    public void writeVillageDataToNBT(NBTTagCompound compound)
    {
        compound.setInteger("PopSize", numVillagers);
        compound.setInteger("Radius", villageRadius);
        compound.setInteger("Golems", numIronGolems);
        compound.setInteger("Stable", lastAddDoorTimestamp);
        compound.setInteger("Tick", tickCounter);
        compound.setInteger("MTick", noBreedTicks);
        compound.setInteger("CX", center.getX());
        compound.setInteger("CY", center.getY());
        compound.setInteger("CZ", center.getZ());
        compound.setInteger("ACX", centerHelper.getX());
        compound.setInteger("ACY", centerHelper.getY());
        compound.setInteger("ACZ", centerHelper.getZ());
        NBTTagList nbttaglist = new NBTTagList();

        for (VillageDoorInfo villagedoorinfo : villageDoorInfoList)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setInteger("X", villagedoorinfo.getDoorBlockPos().getX());
            nbttagcompound.setInteger("Y", villagedoorinfo.getDoorBlockPos().getY());
            nbttagcompound.setInteger("Z", villagedoorinfo.getDoorBlockPos().getZ());
            nbttagcompound.setInteger("IDX", villagedoorinfo.getInsideOffsetX());
            nbttagcompound.setInteger("IDZ", villagedoorinfo.getInsideOffsetZ());
            nbttagcompound.setInteger("TS", villagedoorinfo.getInsidePosY());
            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("Doors", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (String s : playerReputation.keySet())
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            PlayerProfileCache playerprofilecache = worldObj.getInstanceServer().getPlayerProfileCache();

            try
            {
                GameProfile gameprofile = playerprofilecache.getGameProfileForUsername(s);

                if (gameprofile != null)
                {
                    nbttagcompound1.setString("UUID", gameprofile.getId().toString());
                    nbttagcompound1.setInteger("S", playerReputation.get(s).intValue());
                    nbttaglist1.appendTag(nbttagcompound1);
                }
            }
            catch (RuntimeException var9)
            {
            }
        }

        compound.setTag("Players", nbttaglist1);
    }

    /**
     * Prevent villager breeding for a fixed interval of time
     */
    public void endMatingSeason()
    {
        noBreedTicks = tickCounter;
    }

    /**
     * Return whether villagers mating refractory period has passed
     */
    public boolean isMatingSeason()
    {
        return noBreedTicks == 0 || tickCounter - noBreedTicks >= 3600;
    }

    public void setDefaultPlayerReputation(int defaultReputation)
    {
        for (String s : playerReputation.keySet())
        {
            modifyPlayerReputation(s, defaultReputation);
        }
    }

    class VillageAggressor
    {
        public EntityLivingBase agressor;
        public int agressionTime;

        VillageAggressor(EntityLivingBase agressorIn, int agressionTimeIn)
        {
            agressor = agressorIn;
            agressionTime = agressionTimeIn;
        }
    }
}
