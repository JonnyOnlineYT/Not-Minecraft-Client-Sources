package net.minecraft.client.audio;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class ElytraSound extends MovingSound
{
    private final EntityPlayerSP player;
    private int time;

    public ElytraSound(EntityPlayerSP p_i47113_1_)
    {
        super(SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS);
        player = p_i47113_1_;
        repeat = true;
        repeatDelay = 0;
        volume = 0.1F;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        ++time;

        if (!player.isDead && (time <= 20 || player.isElytraFlying()))
        {
            xPosF = (float) player.posX;
            yPosF = (float) player.posY;
            zPosF = (float) player.posZ;
            float f = MathHelper.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY);
            float f1 = f / 2.0F;

            if ((double)f >= 0.01D)
            {
                volume = MathHelper.clamp(f1 * f1, 0.0F, 1.0F);
            }
            else
            {
                volume = 0.0F;
            }

            if (time < 20)
            {
                volume = 0.0F;
            }
            else if (time < 40)
            {
                volume = (float)((double) volume * ((double)(time - 20) / 20.0D));
            }

            float f2 = 0.8F;

            if (volume > 0.8F)
            {
                pitch = 1.0F + (volume - 0.8F);
            }
            else
            {
                pitch = 1.0F;
            }
        }
        else
        {
            donePlaying = true;
        }
    }
}
