package net.minecraft.world;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;

public class BossInfoServer extends BossInfo
{
    private final Set<EntityPlayerMP> players = Sets.newHashSet();
    private final Set<EntityPlayerMP> readOnlyPlayers;
    private boolean visible;

    public BossInfoServer(Component nameIn, BossInfo.Color colorIn, BossInfo.Overlay overlayIn)
    {
        super(MathHelper.getRandomUUID(), nameIn, colorIn, overlayIn);
        readOnlyPlayers = Collections.unmodifiableSet(players);
        visible = true;
    }

    public void setPercent(float percentIn)
    {
        if (percentIn != percent)
        {
            super.setPercent(percentIn);
            sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PCT);
        }
    }

    public void setColor(BossInfo.Color colorIn)
    {
        if (colorIn != color)
        {
            super.setColor(colorIn);
            sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_STYLE);
        }
    }

    public void setOverlay(BossInfo.Overlay overlayIn)
    {
        if (overlayIn != overlay)
        {
            super.setOverlay(overlayIn);
            sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_STYLE);
        }
    }

    public BossInfo setDarkenSky(boolean darkenSkyIn)
    {
        if (darkenSkyIn != darkenSky)
        {
            super.setDarkenSky(darkenSkyIn);
            sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public BossInfo setPlayEndBossMusic(boolean playEndBossMusicIn)
    {
        if (playEndBossMusicIn != playEndBossMusic)
        {
            super.setPlayEndBossMusic(playEndBossMusicIn);
            sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public BossInfo setCreateFog(boolean createFogIn)
    {
        if (createFogIn != createFog)
        {
            super.setCreateFog(createFogIn);
            sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_PROPERTIES);
        }

        return this;
    }

    public void setName(Component nameIn)
    {
        if (!Objects.equal(nameIn, name))
        {
            super.setName(nameIn);
            sendUpdate(SPacketUpdateBossInfo.Operation.UPDATE_NAME);
        }
    }

    private void sendUpdate(SPacketUpdateBossInfo.Operation operationIn)
    {
        if (visible)
        {
            SPacketUpdateBossInfo spacketupdatebossinfo = new SPacketUpdateBossInfo(operationIn, this);

            for (EntityPlayerMP entityplayermp : players)
            {
                entityplayermp.connection.sendPacket(spacketupdatebossinfo);
            }
        }
    }

    /**
     * Makes the boss visible to the given player.
     */
    public void addPlayer(EntityPlayerMP player)
    {
        if (players.add(player) && visible)
        {
            player.connection.sendPacket(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.ADD, this));
        }
    }

    /**
     * Makes the boss non-visible to the given player.
     */
    public void removePlayer(EntityPlayerMP player)
    {
        if (players.remove(player) && visible)
        {
            player.connection.sendPacket(new SPacketUpdateBossInfo(SPacketUpdateBossInfo.Operation.REMOVE, this));
        }
    }

    public void setVisible(boolean visibleIn)
    {
        if (visibleIn != visible)
        {
            visible = visibleIn;

            for (EntityPlayerMP entityplayermp : players)
            {
                entityplayermp.connection.sendPacket(new SPacketUpdateBossInfo(visibleIn ? SPacketUpdateBossInfo.Operation.ADD : SPacketUpdateBossInfo.Operation.REMOVE, this));
            }
        }
    }

    public Collection<EntityPlayerMP> getPlayers()
    {
        return readOnlyPlayers;
    }
}
