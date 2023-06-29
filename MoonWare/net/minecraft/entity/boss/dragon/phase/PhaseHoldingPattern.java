package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.gen.feature.WorldGenEndPodium;

public class PhaseHoldingPattern extends PhaseBase
{
    private Path currentPath;
    private Vec3d targetLocation;
    private boolean clockwise;

    public PhaseHoldingPattern(EntityDragon dragonIn)
    {
        super(dragonIn);
    }

    public PhaseList<PhaseHoldingPattern> getPhaseList()
    {
        return PhaseList.HOLDING_PATTERN;
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    public void doLocalUpdate()
    {
        double d0 = targetLocation == null ? 0.0D : targetLocation.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ);

        if (d0 < 100.0D || d0 > 22500.0D || dragon.isCollidedHorizontally || dragon.isCollidedVertically)
        {
            findNewTarget();
        }
    }

    /**
     * Called when this phase is set to active
     */
    public void initPhase()
    {
        currentPath = null;
        targetLocation = null;
    }

    @Nullable

    /**
     * Returns the location the dragon is flying toward
     */
    public Vec3d getTargetLocation()
    {
        return targetLocation;
    }

    private void findNewTarget()
    {
        if (currentPath != null && currentPath.isFinished())
        {
            BlockPos blockpos = dragon.world.getTopSolidOrLiquidBlock(new BlockPos(WorldGenEndPodium.END_PODIUM_LOCATION));
            int i = dragon.getFightManager() == null ? 0 : dragon.getFightManager().getNumAliveCrystals();

            if (dragon.getRNG().nextInt(i + 3) == 0)
            {
                dragon.getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
                return;
            }

            double d0 = 64.0D;
            EntityPlayer entityplayer = dragon.world.getNearestAttackablePlayer(blockpos, d0, d0);

            if (entityplayer != null)
            {
                d0 = entityplayer.getDistanceSqToCenter(blockpos) / 512.0D;
            }

            if (entityplayer != null && (dragon.getRNG().nextInt(MathHelper.abs((int)d0) + 2) == 0 || dragon.getRNG().nextInt(i + 2) == 0))
            {
                strafePlayer(entityplayer);
                return;
            }
        }

        if (currentPath == null || currentPath.isFinished())
        {
            int j = dragon.initPathPoints();
            int k = j;

            if (dragon.getRNG().nextInt(8) == 0)
            {
                clockwise = !clockwise;
                k = j + 6;
            }

            if (clockwise)
            {
                ++k;
            }
            else
            {
                --k;
            }

            if (dragon.getFightManager() != null && dragon.getFightManager().getNumAliveCrystals() >= 0)
            {
                k = k % 12;

                if (k < 0)
                {
                    k += 12;
                }
            }
            else
            {
                k = k - 12;
                k = k & 7;
                k = k + 12;
            }

            currentPath = dragon.findPath(j, k, null);

            if (currentPath != null)
            {
                currentPath.incrementPathIndex();
            }
        }

        navigateToNextPathNode();
    }

    private void strafePlayer(EntityPlayer player)
    {
        dragon.getPhaseManager().setPhase(PhaseList.STRAFE_PLAYER);
        dragon.getPhaseManager().getPhase(PhaseList.STRAFE_PLAYER).setTarget(player);
    }

    private void navigateToNextPathNode()
    {
        if (currentPath != null && !currentPath.isFinished())
        {
            Vec3d vec3d = currentPath.getCurrentPos();
            currentPath.incrementPathIndex();
            double d0 = vec3d.xCoord;
            double d1 = vec3d.zCoord;
            double d2;

            while (true)
            {
                d2 = vec3d.yCoord + (double)(dragon.getRNG().nextFloat() * 20.0F);

                if (d2 >= vec3d.yCoord)
                {
                    break;
                }
            }

            targetLocation = new Vec3d(d0, d2, d1);
        }
    }

    public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc, @Nullable EntityPlayer plyr)
    {
        if (plyr != null && !plyr.capabilities.disableDamage)
        {
            strafePlayer(plyr);
        }
    }
}
