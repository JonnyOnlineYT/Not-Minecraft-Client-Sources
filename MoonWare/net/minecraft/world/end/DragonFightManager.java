package net.minecraft.world.end;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.*;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeEndDecorator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class DragonFightManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Predicate<EntityPlayerMP> VALID_PLAYER = Predicates.and (EntitySelectors.IS_ALIVE, EntitySelectors.withinRange(0.0D, 128.0D, 0.0D, 192.0D));
    private final BossInfoServer bossInfo = (BossInfoServer)(new BossInfoServer(new TranslatableComponent("entity.EnderDragon.name"), BossInfo.Color.PINK, BossInfo.Overlay.PROGRESS)).setPlayEndBossMusic(true).setCreateFog(true);
    private final WorldServer world;
    private final List<Integer> gateways = Lists.newArrayList();
    private final BlockPattern portalPattern;
    private int ticksSinceDragonSeen;
    private int aliveCrystals;
    private int ticksSinceCrystalsScanned;
    private int ticksSinceLastPlayerScan;
    private boolean dragonKilled;
    private boolean previouslyKilled;
    private UUID dragonUniqueId;
    private boolean scanForLegacyFight = true;
    private BlockPos exitPortalLocation;
    private DragonSpawnManager respawnState;
    private int respawnStateTicks;
    private List<EntityEnderCrystal> crystals;

    public DragonFightManager(WorldServer worldIn, NBTTagCompound compound)
    {
        world = worldIn;

        if (compound.hasKey("DragonKilled", 99))
        {
            if (compound.hasUniqueId("DragonUUID"))
            {
                dragonUniqueId = compound.getUniqueId("DragonUUID");
            }

            dragonKilled = compound.getBoolean("DragonKilled");
            previouslyKilled = compound.getBoolean("PreviouslyKilled");

            if (compound.getBoolean("IsRespawning"))
            {
                respawnState = DragonSpawnManager.START;
            }

            if (compound.hasKey("ExitPortalLocation", 10))
            {
                exitPortalLocation = NBTUtil.getPosFromTag(compound.getCompoundTag("ExitPortalLocation"));
            }
        }
        else
        {
            dragonKilled = true;
            previouslyKilled = true;
        }

        if (compound.hasKey("Gateways", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("Gateways", 3);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                gateways.add(Integer.valueOf(nbttaglist.getIntAt(i)));
            }
        }
        else
        {
            gateways.addAll(ContiguousSet.create(Range.closedOpen(Integer.valueOf(0), Integer.valueOf(20)), DiscreteDomain.integers()));
            Collections.shuffle(gateways, new Random(worldIn.getSeed()));
        }

        portalPattern = FactoryBlockPattern.start().aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("       ", "       ", "       ", "   #   ", "       ", "       ", "       ").aisle("  ###  ", " #   # ", "#     #", "#  #  #", "#     #", " #   # ", "  ###  ").aisle("       ", "  ###  ", " ##### ", " ##### ", " ##### ", "  ###  ", "       ").where('#', BlockWorldState.hasState(BlockMatcher.forBlock(Blocks.BEDROCK))).build();
    }

    public NBTTagCompound getCompound()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        if (dragonUniqueId != null)
        {
            nbttagcompound.setUniqueId("DragonUUID", dragonUniqueId);
        }

        nbttagcompound.setBoolean("DragonKilled", dragonKilled);
        nbttagcompound.setBoolean("PreviouslyKilled", previouslyKilled);

        if (exitPortalLocation != null)
        {
            nbttagcompound.setTag("ExitPortalLocation", NBTUtil.createPosTag(exitPortalLocation));
        }

        NBTTagList nbttaglist = new NBTTagList();
        Iterator iterator = gateways.iterator();

        while (iterator.hasNext())
        {
            int i = ((Integer)iterator.next()).intValue();
            nbttaglist.appendTag(new NBTTagInt(i));
        }

        nbttagcompound.setTag("Gateways", nbttaglist);
        return nbttagcompound;
    }

    public void tick()
    {
        bossInfo.setVisible(!dragonKilled);

        if (++ticksSinceLastPlayerScan >= 20)
        {
            updateplayers();
            ticksSinceLastPlayerScan = 0;
        }

        if (!bossInfo.getPlayers().isEmpty())
        {
            if (scanForLegacyFight)
            {
                LOGGER.info("Scanning for legacy world dragon fight...");
                loadChunks();
                scanForLegacyFight = false;
                boolean flag = hasDragonBeenKilled();

                if (flag)
                {
                    LOGGER.info("Found that the dragon has been killed in this world already.");
                    previouslyKilled = true;
                }
                else
                {
                    LOGGER.info("Found that the dragon has not yet been killed in this world.");
                    previouslyKilled = false;
                    generatePortal(false);
                }

                List<EntityDragon> list = world.getEntities(EntityDragon.class, EntitySelectors.IS_ALIVE);

                if (list.isEmpty())
                {
                    dragonKilled = true;
                }
                else
                {
                    EntityDragon entitydragon = list.get(0);
                    dragonUniqueId = entitydragon.getUniqueID();
                    LOGGER.info("Found that there's a dragon still alive ({})", entitydragon);
                    dragonKilled = false;

                    if (!flag)
                    {
                        LOGGER.info("But we didn't have a portal, let's remove it.");
                        entitydragon.setDead();
                        dragonUniqueId = null;
                    }
                }

                if (!previouslyKilled && dragonKilled)
                {
                    dragonKilled = false;
                }
            }

            if (respawnState != null)
            {
                if (crystals == null)
                {
                    respawnState = null;
                    respawnDragon();
                }

                respawnState.process(world, this, crystals, respawnStateTicks++, exitPortalLocation);
            }

            if (!dragonKilled)
            {
                if (dragonUniqueId == null || ++ticksSinceDragonSeen >= 1200)
                {
                    loadChunks();
                    List<EntityDragon> list1 = world.getEntities(EntityDragon.class, EntitySelectors.IS_ALIVE);

                    if (list1.isEmpty())
                    {
                        LOGGER.debug("Haven't seen the dragon, respawning it");
                        func_192445_m();
                    }
                    else
                    {
                        LOGGER.debug("Haven't seen our dragon, but found another one to use.");
                        dragonUniqueId = list1.get(0).getUniqueID();
                    }

                    ticksSinceDragonSeen = 0;
                }

                if (++ticksSinceCrystalsScanned >= 100)
                {
                    findAliveCrystals();
                    ticksSinceCrystalsScanned = 0;
                }
            }
        }
    }

    protected void setRespawnState(DragonSpawnManager state)
    {
        if (respawnState == null)
        {
            throw new IllegalStateException("Dragon respawn isn't in progress, can't skip ahead in the animation.");
        }
        else
        {
            respawnStateTicks = 0;

            if (state == DragonSpawnManager.END)
            {
                respawnState = null;
                dragonKilled = false;
                EntityDragon entitydragon = func_192445_m();

                for (EntityPlayerMP entityplayermp : bossInfo.getPlayers())
                {
                    CriteriaTriggers.field_192133_m.func_192229_a(entityplayermp, entitydragon);
                }
            }
            else
            {
                respawnState = state;
            }
        }
    }

    private boolean hasDragonBeenKilled()
    {
        for (int i = -8; i <= 8; ++i)
        {
            for (int j = -8; j <= 8; ++j)
            {
                Chunk chunk = world.getChunkFromChunkCoords(i, j);

                for (TileEntity tileentity : chunk.getTileEntityMap().values())
                {
                    if (tileentity instanceof TileEntityEndPortal)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Nullable
    private BlockPattern.PatternHelper findExitPortal()
    {
        for (int i = -8; i <= 8; ++i)
        {
            for (int j = -8; j <= 8; ++j)
            {
                Chunk chunk = world.getChunkFromChunkCoords(i, j);

                for (TileEntity tileentity : chunk.getTileEntityMap().values())
                {
                    if (tileentity instanceof TileEntityEndPortal)
                    {
                        BlockPattern.PatternHelper blockpattern$patternhelper = portalPattern.match(world, tileentity.getPos());

                        if (blockpattern$patternhelper != null)
                        {
                            BlockPos blockpos = blockpattern$patternhelper.translateOffset(3, 3, 3).getPos();

                            if (exitPortalLocation == null && blockpos.getX() == 0 && blockpos.getZ() == 0)
                            {
                                exitPortalLocation = blockpos;
                            }

                            return blockpattern$patternhelper;
                        }
                    }
                }
            }
        }

        int k = world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION).getY();

        for (int l = k; l >= 0; --l)
        {
            BlockPattern.PatternHelper blockpattern$patternhelper1 = portalPattern.match(world, new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION.getX(), l, WorldGenEndPodium.END_PODIUM_LOCATION.getZ()));

            if (blockpattern$patternhelper1 != null)
            {
                if (exitPortalLocation == null)
                {
                    exitPortalLocation = blockpattern$patternhelper1.translateOffset(3, 3, 3).getPos();
                }

                return blockpattern$patternhelper1;
            }
        }

        return null;
    }

    private void loadChunks()
    {
        for (int i = -8; i <= 8; ++i)
        {
            for (int j = -8; j <= 8; ++j)
            {
                world.getChunkFromChunkCoords(i, j);
            }
        }
    }

    private void updateplayers()
    {
        Set<EntityPlayerMP> set = Sets.newHashSet();

        for (EntityPlayerMP entityplayermp : world.getPlayers(EntityPlayerMP.class, VALID_PLAYER))
        {
            bossInfo.addPlayer(entityplayermp);
            set.add(entityplayermp);
        }

        Set<EntityPlayerMP> set1 = Sets.newHashSet(bossInfo.getPlayers());
        set1.removeAll(set);

        for (EntityPlayerMP entityplayermp1 : set1)
        {
            bossInfo.removePlayer(entityplayermp1);
        }
    }

    private void findAliveCrystals()
    {
        ticksSinceCrystalsScanned = 0;
        aliveCrystals = 0;

        for (WorldGenSpikes.EndSpike worldgenspikes$endspike : BiomeEndDecorator.getSpikesForWorld(world))
        {
            aliveCrystals += world.getEntitiesWithinAABB(EntityEnderCrystal.class, worldgenspikes$endspike.getTopBoundingBox()).size();
        }

        LOGGER.debug("Found {} end crystals still alive", aliveCrystals);
    }

    public void processDragonDeath(EntityDragon dragon)
    {
        if (dragon.getUniqueID().equals(dragonUniqueId))
        {
            bossInfo.setPercent(0.0F);
            bossInfo.setVisible(false);
            generatePortal(true);
            spawnNewGateway();

            if (!previouslyKilled)
            {
                world.setBlockState(world.getHeight(WorldGenEndPodium.END_PODIUM_LOCATION), Blocks.DRAGON_EGG.getDefaultState());
            }

            previouslyKilled = true;
            dragonKilled = true;
        }
    }

    private void spawnNewGateway()
    {
        if (!gateways.isEmpty())
        {
            int i = gateways.remove(gateways.size() - 1).intValue();
            int j = (int)(96.0D * Math.cos(2.0D * (-Math.PI + 0.15707963267948966D * (double)i)));
            int k = (int)(96.0D * Math.sin(2.0D * (-Math.PI + 0.15707963267948966D * (double)i)));
            generateGateway(new BlockPos(j, 75, k));
        }
    }

    private void generateGateway(BlockPos pos)
    {
        world.playEvent(3000, pos, 0);
        (new WorldGenEndGateway()).generate(world, new Random(), pos);
    }

    private void generatePortal(boolean active)
    {
        WorldGenEndPodium worldgenendpodium = new WorldGenEndPodium(active);

        if (exitPortalLocation == null)
        {
            for (exitPortalLocation = world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION).down(); world.getBlockState(exitPortalLocation).getBlock() == Blocks.BEDROCK && exitPortalLocation.getY() > world.getSeaLevel(); exitPortalLocation = exitPortalLocation.down())
            {
            }
        }

        worldgenendpodium.generate(world, new Random(), exitPortalLocation);
    }

    private EntityDragon func_192445_m()
    {
        world.getChunkFromBlockCoords(new BlockPos(0, 128, 0));
        EntityDragon entitydragon = new EntityDragon(world);
        entitydragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        entitydragon.setLocationAndAngles(0.0D, 128.0D, 0.0D, world.rand.nextFloat() * 360.0F, 0.0F);
        world.spawnEntityInWorld(entitydragon);
        dragonUniqueId = entitydragon.getUniqueID();
        return entitydragon;
    }

    public void dragonUpdate(EntityDragon dragonIn)
    {
        if (dragonIn.getUniqueID().equals(dragonUniqueId))
        {
            bossInfo.setPercent(dragonIn.getHealth() / dragonIn.getMaxHealth());
            ticksSinceDragonSeen = 0;

            if (dragonIn.hasCustomName())
            {
                bossInfo.setName(dragonIn.getDisplayName());
            }
        }
    }

    public int getNumAliveCrystals()
    {
        return aliveCrystals;
    }

    public void onCrystalDestroyed(EntityEnderCrystal crystal, DamageSource dmgSrc)
    {
        if (respawnState != null && crystals.contains(crystal))
        {
            LOGGER.debug("Aborting respawn sequence");
            respawnState = null;
            respawnStateTicks = 0;
            resetSpikeCrystals();
            generatePortal(true);
        }
        else
        {
            findAliveCrystals();
            Entity entity = world.getEntityFromUuid(dragonUniqueId);

            if (entity instanceof EntityDragon)
            {
                ((EntityDragon)entity).onCrystalDestroyed(crystal, new BlockPos(crystal), dmgSrc);
            }
        }
    }

    public boolean hasPreviouslyKilledDragon()
    {
        return previouslyKilled;
    }

    public void respawnDragon()
    {
        if (dragonKilled && respawnState == null)
        {
            BlockPos blockpos = exitPortalLocation;

            if (blockpos == null)
            {
                LOGGER.debug("Tried to respawn, but need to find the portal first.");
                BlockPattern.PatternHelper blockpattern$patternhelper = findExitPortal();

                if (blockpattern$patternhelper == null)
                {
                    LOGGER.debug("Couldn't find a portal, so we made one.");
                    generatePortal(true);
                }
                else
                {
                    LOGGER.debug("Found the exit portal & temporarily using it.");
                }

                blockpos = exitPortalLocation;
            }

            List<EntityEnderCrystal> list1 = Lists.newArrayList();
            BlockPos blockpos1 = blockpos.up(1);

            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
            {
                List<EntityEnderCrystal> list = world.getEntitiesWithinAABB(EntityEnderCrystal.class, new AxisAlignedBB(blockpos1.offset(enumfacing, 2)));

                if (list.isEmpty())
                {
                    return;
                }

                list1.addAll(list);
            }

            LOGGER.debug("Found all crystals, respawning dragon.");
            respawnDragon(list1);
        }
    }

    private void respawnDragon(List<EntityEnderCrystal> crystalsIn)
    {
        if (dragonKilled && respawnState == null)
        {
            for (BlockPattern.PatternHelper blockpattern$patternhelper = findExitPortal(); blockpattern$patternhelper != null; blockpattern$patternhelper = findExitPortal())
            {
                for (int i = 0; i < portalPattern.getPalmLength(); ++i)
                {
                    for (int j = 0; j < portalPattern.getThumbLength(); ++j)
                    {
                        for (int k = 0; k < portalPattern.getFingerLength(); ++k)
                        {
                            BlockWorldState blockworldstate = blockpattern$patternhelper.translateOffset(i, j, k);

                            if (blockworldstate.getBlockState().getBlock() == Blocks.BEDROCK || blockworldstate.getBlockState().getBlock() == Blocks.END_PORTAL)
                            {
                                world.setBlockState(blockworldstate.getPos(), Blocks.END_STONE.getDefaultState());
                            }
                        }
                    }
                }
            }

            respawnState = DragonSpawnManager.START;
            respawnStateTicks = 0;
            generatePortal(false);
            crystals = crystalsIn;
        }
    }

    public void resetSpikeCrystals()
    {
        for (WorldGenSpikes.EndSpike worldgenspikes$endspike : BiomeEndDecorator.getSpikesForWorld(world))
        {
            for (EntityEnderCrystal entityendercrystal : world.getEntitiesWithinAABB(EntityEnderCrystal.class, worldgenspikes$endspike.getTopBoundingBox()))
            {
                entityendercrystal.setEntityInvulnerable(false);
                entityendercrystal.setBeamTarget(null);
            }
        }
    }
}
