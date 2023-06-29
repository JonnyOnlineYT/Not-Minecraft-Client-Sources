package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.BlockSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;

public class TileEntitySkull extends TileEntity implements ITickable
{
    private int skullType;
    private int skullRotation;
    private GameProfile playerProfile;
    private int dragonAnimatedTicks;
    private boolean dragonAnimated;
    private static PlayerProfileCache profileCache;
    private static MinecraftSessionService sessionService;

    public static void setProfileCache(PlayerProfileCache profileCacheIn)
    {
        profileCache = profileCacheIn;
    }

    public static void setSessionService(MinecraftSessionService sessionServiceIn)
    {
        sessionService = sessionServiceIn;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setByte("SkullType", (byte)(skullType & 255));
        compound.setByte("Rot", (byte)(skullRotation & 255));

        if (playerProfile != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTUtil.writeGameProfile(nbttagcompound, playerProfile);
            compound.setTag("Owner", nbttagcompound);
        }

        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        skullType = compound.getByte("SkullType");
        skullRotation = compound.getByte("Rot");

        if (skullType == 3)
        {
            if (compound.hasKey("Owner", 10))
            {
                playerProfile = NBTUtil.readGameProfileFromNBT(compound.getCompoundTag("Owner"));
            }
            else if (compound.hasKey("ExtraType", 8))
            {
                String s = compound.getString("ExtraType");

                if (!StringUtils.isNullOrEmpty(s))
                {
                    playerProfile = new GameProfile(null, s);
                    updatePlayerProfile();
                }
            }
        }
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        if (skullType == 5)
        {
            if (world.isBlockPowered(pos))
            {
                dragonAnimated = true;
                ++dragonAnimatedTicks;
            }
            else
            {
                dragonAnimated = false;
            }
        }
    }

    public float getAnimationProgress(float p_184295_1_)
    {
        return dragonAnimated ? (float) dragonAnimatedTicks + p_184295_1_ : (float) dragonAnimatedTicks;
    }

    @Nullable
    public GameProfile getPlayerProfile()
    {
        return playerProfile;
    }

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(pos, 4, getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    public void setType(int type)
    {
        skullType = type;
        playerProfile = null;
    }

    public void setPlayerProfile(@Nullable GameProfile playerProfile)
    {
        skullType = 3;
        this.playerProfile = playerProfile;
        updatePlayerProfile();
    }

    private void updatePlayerProfile()
    {
        playerProfile = updateGameprofile(playerProfile);
        markDirty();
    }

    public static GameProfile updateGameprofile(GameProfile input)
    {
        if (input != null && !StringUtils.isNullOrEmpty(input.getName()))
        {
            if (input.isComplete() && input.getProperties().containsKey("textures"))
            {
                return input;
            }
            else if (profileCache != null && sessionService != null)
            {
                GameProfile gameprofile = profileCache.getGameProfileForUsername(input.getName());

                if (gameprofile == null)
                {
                    return input;
                }
                else
                {
                    Property property = (Property)Iterables.getFirst(gameprofile.getProperties().get("textures"), (Object)null);

                    if (property == null)
                    {
                        gameprofile = sessionService.fillProfileProperties(gameprofile, true);
                    }

                    return gameprofile;
                }
            }
            else
            {
                return input;
            }
        }
        else
        {
            return input;
        }
    }

    public int getSkullType()
    {
        return skullType;
    }

    public int getSkullRotation()
    {
        return skullRotation;
    }

    public void setSkullRotation(int rotation)
    {
        skullRotation = rotation;
    }

    public void mirror(Mirror p_189668_1_)
    {
        if (world != null && world.getBlockState(getPos()).getValue(BlockSkull.FACING) == EnumFacing.UP)
        {
            skullRotation = p_189668_1_.mirrorRotation(skullRotation, 16);
        }
    }

    public void rotate(Rotation p_189667_1_)
    {
        if (world != null && world.getBlockState(getPos()).getValue(BlockSkull.FACING) == EnumFacing.UP)
        {
            skullRotation = p_189667_1_.rotate(skullRotation, 16);
        }
    }
}
