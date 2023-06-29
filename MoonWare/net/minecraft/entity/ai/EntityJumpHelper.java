package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper
{
    private final EntityLiving entity;
    protected boolean isJumping;

    public EntityJumpHelper(EntityLiving entityIn)
    {
        entity = entityIn;
    }

    public void setJumping()
    {
        isJumping = true;
    }

    /**
     * Called to actually make the entity jump if isJumping is true.
     */
    public void doJump()
    {
        entity.setJumping(isJumping);
        isJumping = false;
    }
}
