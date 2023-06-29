package net.minecraft.client.audio;

import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;

public class GuardianSound extends MovingSound
{
    private final EntityGuardian guardian;

    public GuardianSound(EntityGuardian guardian)
    {
        super(SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE);
        this.guardian = guardian;
        attenuationType = ISound.AttenuationType.NONE;
        repeat = true;
        repeatDelay = 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        if (!guardian.isDead && guardian.hasTargetedEntity())
        {
            xPosF = (float) guardian.posX;
            yPosF = (float) guardian.posY;
            zPosF = (float) guardian.posZ;
            float f = guardian.getAttackAnimationScale(0.0F);
            volume = 0.0F + 1.0F * f * f;
            pitch = 0.7F + 0.5F * f;
        }
        else
        {
            donePlaying = true;
        }
    }
}
