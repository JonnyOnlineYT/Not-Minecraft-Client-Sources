package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseChargingPlayer extends PhaseBase
{
    private static final Logger LOGGER = LogManager.getLogger();
    private Vec3d targetLocation;
    private int timeSinceCharge;

    public PhaseChargingPlayer(EntityDragon dragonIn)
    {
        super(dragonIn);
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    public void doLocalUpdate()
    {
        if (targetLocation == null)
        {
            LOGGER.warn("Aborting charge player as no target was set.");
            dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        }
        else if (timeSinceCharge > 0 && timeSinceCharge++ >= 10)
        {
            dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        }
        else
        {
            double d0 = targetLocation.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ);

            if (d0 < 100.0D || d0 > 22500.0D || dragon.isCollidedHorizontally || dragon.isCollidedVertically)
            {
                ++timeSinceCharge;
            }
        }
    }

    /**
     * Called when this phase is set to active
     */
    public void initPhase()
    {
        targetLocation = null;
        timeSinceCharge = 0;
    }

    public void setTarget(Vec3d p_188668_1_)
    {
        targetLocation = p_188668_1_;
    }

    /**
     * Returns the maximum amount dragon may rise or fall during this phase
     */
    public float getMaxRiseOrFall()
    {
        return 3.0F;
    }

    @Nullable

    /**
     * Returns the location the dragon is flying toward
     */
    public Vec3d getTargetLocation()
    {
        return targetLocation;
    }

    public PhaseList<PhaseChargingPlayer> getPhaseList()
    {
        return PhaseList.CHARGING_PLAYER;
    }
}
