package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.world.World;

public class EntityOtherPlayerMP extends AbstractClientPlayer
{
    private int otherPlayerMPPosRotationIncrements;
    private double otherPlayerMPX;
    private double otherPlayerMPY;
    private double otherPlayerMPZ;
    private double otherPlayerMPYaw;
    private double otherPlayerMPPitch;

    public EntityOtherPlayerMP(World worldIn, GameProfile gameProfileIn)
    {
        super(worldIn, gameProfileIn);
        stepHeight = 1.0F;
        noClip = true;
        renderOffsetY = 0.25F;
    }

    /**
     * Checks if the entity is in range to render.
     */
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = getEntityBoundingBox().getAverageEdgeLength() * 10.0D;

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * getRenderDistanceWeight();
        return distance < d0 * d0;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        return true;
    }

    /**
     * Set the position and rotation values directly without any clamping.
     */
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        otherPlayerMPX = x;
        otherPlayerMPY = y;
        otherPlayerMPZ = z;
        otherPlayerMPYaw = yaw;
        otherPlayerMPPitch = pitch;
        otherPlayerMPPosRotationIncrements = posRotationIncrements;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        renderOffsetY = 0.0F;
        super.onUpdate();
        prevLimbSwingAmount = limbSwingAmount;
        double d0 = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        limbSwingAmount += (f - limbSwingAmount) * 0.4F;
        limbSwing += limbSwingAmount;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (otherPlayerMPPosRotationIncrements > 0)
        {
            double d0 = posX + (otherPlayerMPX - posX) / (double) otherPlayerMPPosRotationIncrements;
            double d1 = posY + (otherPlayerMPY - posY) / (double) otherPlayerMPPosRotationIncrements;
            double d2 = posZ + (otherPlayerMPZ - posZ) / (double) otherPlayerMPPosRotationIncrements;
            double d3;

            for (d3 = otherPlayerMPYaw - (double) rotationYaw; d3 < -180.0D; d3 += 360.0D)
            {
            }

            while (d3 >= 180.0D)
            {
                d3 -= 360.0D;
            }

            rotationYaw = (float)((double) rotationYaw + d3 / (double) otherPlayerMPPosRotationIncrements);
            rotationPitch = (float)((double) rotationPitch + (otherPlayerMPPitch - (double) rotationPitch) / (double) otherPlayerMPPosRotationIncrements);
            --otherPlayerMPPosRotationIncrements;
            setPosition(d0, d1, d2);
            setRotation(rotationYaw, rotationPitch);
        }

        prevCameraYaw = cameraYaw;
        updateArmSwingProgress();
        float f1 = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
        float f = (float)Math.atan(-motionY * 0.20000000298023224D) * 15.0F;

        if (f1 > 0.1F)
        {
            f1 = 0.1F;
        }

        if (!onGround || getHealth() <= 0.0F)
        {
            f1 = 0.0F;
        }

        if (onGround || getHealth() <= 0.0F)
        {
            f = 0.0F;
        }

        cameraYaw += (f1 - cameraYaw) * 0.4F;
        cameraPitch += (f - cameraPitch) * 0.8F;
        world.theProfiler.startSection("push");
        collideWithNearbyEntities();
        world.theProfiler.endSection();
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(Component component)
    {
        Minecraft.ingameGUI.getChatGUI().printChatMessage(component);
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
        return false;
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition()
    {
        return new BlockPos(posX + 0.5D, posY + 0.5D, posZ + 0.5D);
    }
}
