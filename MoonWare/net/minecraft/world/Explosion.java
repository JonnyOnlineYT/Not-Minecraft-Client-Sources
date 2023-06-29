package net.minecraft.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Explosion
{
    /** whether or not the explosion sets fire to blocks around it */
    private final boolean isFlaming;

    /** whether or not this explosion spawns smoke particles */
    private final boolean isSmoking;
    private final Random explosionRNG;
    private final World worldObj;
    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;
    private final Entity exploder;
    private final float explosionSize;
    private final List<BlockPos> affectedBlockPositions;
    private final Map<EntityPlayer, Vec3d> playerKnockbackMap;

    public Explosion(World worldIn, Entity entityIn, double x, double y, double z, float size, List<BlockPos> affectedPositions)
    {
        this(worldIn, entityIn, x, y, z, size, false, true, affectedPositions);
    }

    public Explosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean smoking, List<BlockPos> affectedPositions)
    {
        this(worldIn, entityIn, x, y, z, size, flaming, smoking);
        affectedBlockPositions.addAll(affectedPositions);
    }

    public Explosion(World worldIn, Entity entityIn, double x, double y, double z, float size, boolean flaming, boolean smoking)
    {
        explosionRNG = new Random();
        affectedBlockPositions = Lists.newArrayList();
        playerKnockbackMap = Maps.newHashMap();
        worldObj = worldIn;
        exploder = entityIn;
        explosionSize = size;
        explosionX = x;
        explosionY = y;
        explosionZ = z;
        isFlaming = flaming;
        isSmoking = smoking;
    }

    /**
     * Does the first part of the explosion (destroy blocks)
     */
    public void doExplosionA()
    {
        Set<BlockPos> set = Sets.newHashSet();
        int i = 16;

        for (int j = 0; j < 16; ++j)
        {
            for (int k = 0; k < 16; ++k)
            {
                for (int l = 0; l < 16; ++l)
                {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15)
                    {
                        double d0 = (float)j / 15.0F * 2.0F - 1.0F;
                        double d1 = (float)k / 15.0F * 2.0F - 1.0F;
                        double d2 = (float)l / 15.0F * 2.0F - 1.0F;
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                        d0 = d0 / d3;
                        d1 = d1 / d3;
                        d2 = d2 / d3;
                        float f = explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
                        double d4 = explosionX;
                        double d6 = explosionY;
                        double d8 = explosionZ;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F)
                        {
                            BlockPos blockpos = new BlockPos(d4, d6, d8);
                            IBlockState iblockstate = worldObj.getBlockState(blockpos);

                            if (iblockstate.getMaterial() != Material.AIR)
                            {
                                float f2 = exploder != null ? exploder.getExplosionResistance(this, worldObj, blockpos, iblockstate) : iblockstate.getBlock().getExplosionResistance(null);
                                f -= (f2 + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && (exploder == null || exploder.verifyExplosion(this, worldObj, blockpos, iblockstate, f)))
                            {
                                set.add(blockpos);
                            }

                            d4 += d0 * 0.30000001192092896D;
                            d6 += d1 * 0.30000001192092896D;
                            d8 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        affectedBlockPositions.addAll(set);
        float f3 = explosionSize * 2.0F;
        int k1 = MathHelper.floor(explosionX - (double)f3 - 1.0D);
        int l1 = MathHelper.floor(explosionX + (double)f3 + 1.0D);
        int i2 = MathHelper.floor(explosionY - (double)f3 - 1.0D);
        int i1 = MathHelper.floor(explosionY + (double)f3 + 1.0D);
        int j2 = MathHelper.floor(explosionZ - (double)f3 - 1.0D);
        int j1 = MathHelper.floor(explosionZ + (double)f3 + 1.0D);
        List<Entity> list = worldObj.getEntitiesWithinAABBExcludingEntity(exploder, new AxisAlignedBB(k1, i2, j2, l1, i1, j1));
        Vec3d vec3d = new Vec3d(explosionX, explosionY, explosionZ);

        for (int k2 = 0; k2 < list.size(); ++k2)
        {
            Entity entity = list.get(k2);

            if (!entity.isImmuneToExplosions())
            {
                double d12 = entity.getDistance(explosionX, explosionY, explosionZ) / (double)f3;

                if (d12 <= 1.0D)
                {
                    double d5 = entity.posX - explosionX;
                    double d7 = entity.posY + (double)entity.getEyeHeight() - explosionY;
                    double d9 = entity.posZ - explosionZ;
                    double d13 = MathHelper.sqrt(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D)
                    {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = worldObj.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(DamageSource.causeExplosionDamage(this), (float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f3 + 1.0D)));
                        double d11 = d10;

                        if (entity instanceof EntityLivingBase)
                        {
                            d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase)entity, d10);
                        }

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof EntityPlayer)
                        {
                            EntityPlayer entityplayer = (EntityPlayer)entity;

                            if (!entityplayer.isSpectator() && (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying))
                            {
                                playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Does the second part of the explosion (sound, particles, drop spawn)
     */
    public void doExplosionB(boolean spawnParticles)
    {
        worldObj.playSound(null, explosionX, explosionY, explosionZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);

        if (explosionSize >= 2.0F && isSmoking)
        {
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
        }
        else
        {
            worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, explosionX, explosionY, explosionZ, 1.0D, 0.0D, 0.0D);
        }

        if (isSmoking)
        {
            for (BlockPos blockpos : affectedBlockPositions)
            {
                IBlockState iblockstate = worldObj.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (spawnParticles)
                {
                    double d0 = (float)blockpos.getX() + worldObj.rand.nextFloat();
                    double d1 = (float)blockpos.getY() + worldObj.rand.nextFloat();
                    double d2 = (float)blockpos.getZ() + worldObj.rand.nextFloat();
                    double d3 = d0 - explosionX;
                    double d4 = d1 - explosionY;
                    double d5 = d2 - explosionZ;
                    double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                    d3 = d3 / d6;
                    d4 = d4 / d6;
                    d5 = d5 / d6;
                    double d7 = 0.5D / (d6 / (double) explosionSize + 0.1D);
                    d7 = d7 * (double)(worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F);
                    d3 = d3 * d7;
                    d4 = d4 * d7;
                    d5 = d5 * d7;
                    worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, (d0 + explosionX) / 2.0D, (d1 + explosionY) / 2.0D, (d2 + explosionZ) / 2.0D, d3, d4, d5);
                    worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5);
                }

                if (iblockstate.getMaterial() != Material.AIR)
                {
                    if (block.canDropFromExplosion(this))
                    {
                        block.dropBlockAsItemWithChance(worldObj, blockpos, worldObj.getBlockState(blockpos), 1.0F / explosionSize, 0);
                    }

                    worldObj.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 3);
                    block.onBlockDestroyedByExplosion(worldObj, blockpos, this);
                }
            }
        }

        if (isFlaming)
        {
            for (BlockPos blockpos1 : affectedBlockPositions)
            {
                if (worldObj.getBlockState(blockpos1).getMaterial() == Material.AIR && worldObj.getBlockState(blockpos1.down()).isFullBlock() && explosionRNG.nextInt(3) == 0)
                {
                    worldObj.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap()
    {
        return playerKnockbackMap;
    }

    @Nullable

    /**
     * Returns either the entity that placed the explosive block, the entity that caused the explosion or null.
     */
    public EntityLivingBase getExplosivePlacedBy()
    {
        if (exploder == null)
        {
            return null;
        }
        else if (exploder instanceof EntityTNTPrimed)
        {
            return ((EntityTNTPrimed) exploder).getTntPlacedBy();
        }
        else
        {
            return exploder instanceof EntityLivingBase ? (EntityLivingBase) exploder : null;
        }
    }

    public void clearAffectedBlockPositions()
    {
        affectedBlockPositions.clear();
    }

    public List<BlockPos> getAffectedBlockPositions()
    {
        return affectedBlockPositions;
    }
}
