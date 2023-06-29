package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class MovingSoundMinecartRiding extends MovingSound
{
    private final EntityPlayer player;
    private final EntityMinecart minecart;

    public MovingSoundMinecartRiding(EntityPlayer playerRiding, EntityMinecart minecart)
    {
        super(SoundEvents.ENTITY_MINECART_INSIDE, SoundCategory.NEUTRAL);
        player = playerRiding;
        this.minecart = minecart;
        attenuationType = ISound.AttenuationType.NONE;
        repeat = true;
        repeatDelay = 0;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        if (!minecart.isDead && player.isRiding() && player.getRidingEntity() == minecart)
        {
            float f = MathHelper.sqrt(minecart.motionX * minecart.motionX + minecart.motionZ * minecart.motionZ);

            if ((double)f >= 0.01D)
            {
                volume = 0.0F + MathHelper.clamp(f, 0.0F, 1.0F) * 0.75F;
            }
            else
            {
                volume = 0.0F;
            }
        }
        else
        {
            donePlaying = true;
        }
    }
}
