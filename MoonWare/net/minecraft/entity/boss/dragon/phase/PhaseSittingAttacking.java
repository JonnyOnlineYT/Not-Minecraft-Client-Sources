package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.SoundEvents;

public class PhaseSittingAttacking extends PhaseSittingBase
{
    private int attackingTicks;

    public PhaseSittingAttacking(EntityDragon dragonIn)
    {
        super(dragonIn);
    }

    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    public void doClientRenderEffects()
    {
        dragon.world.playSound(dragon.posX, dragon.posY, dragon.posZ, SoundEvents.ENTITY_ENDERDRAGON_GROWL, dragon.getSoundCategory(), 2.5F, 0.8F + dragon.getRNG().nextFloat() * 0.3F, false);
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    public void doLocalUpdate()
    {
        if (attackingTicks++ >= 40)
        {
            dragon.getPhaseManager().setPhase(PhaseList.SITTING_FLAMING);
        }
    }

    /**
     * Called when this phase is set to active
     */
    public void initPhase()
    {
        attackingTicks = 0;
    }

    public PhaseList<PhaseSittingAttacking> getPhaseList()
    {
        return PhaseList.SITTING_ATTACKING;
    }
}
