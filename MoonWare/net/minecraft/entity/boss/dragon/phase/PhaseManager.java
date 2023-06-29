package net.minecraft.entity.boss.dragon.phase;

import net.minecraft.entity.boss.EntityDragon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final EntityDragon dragon;
    private final IPhase[] phases = new IPhase[PhaseList.getTotalPhases()];
    private IPhase phase;

    public PhaseManager(EntityDragon dragonIn)
    {
        dragon = dragonIn;
        setPhase(PhaseList.HOVER);
    }

    public void setPhase(PhaseList<?> phaseIn)
    {
        if (phase == null || phaseIn != phase.getPhaseList())
        {
            if (phase != null)
            {
                phase.removeAreaEffect();
            }

            phase = getPhase(phaseIn);

            if (!dragon.world.isRemote)
            {
                dragon.getDataManager().set(EntityDragon.PHASE, Integer.valueOf(phaseIn.getId()));
            }

            LOGGER.debug("Dragon is now in phase {} on the {}", phaseIn, dragon.world.isRemote ? "client" : "server");
            phase.initPhase();
        }
    }

    public IPhase getCurrentPhase()
    {
        return phase;
    }

    public <T extends IPhase> T getPhase(PhaseList<T> phaseIn)
    {
        int i = phaseIn.getId();

        if (phases[i] == null)
        {
            phases[i] = phaseIn.createPhase(dragon);
        }

        return (T) phases[i];
    }
}
