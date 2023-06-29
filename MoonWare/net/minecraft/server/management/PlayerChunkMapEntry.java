package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerChunkMapEntry
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final PlayerChunkMap playerChunkMap;
    private final List<EntityPlayerMP> players = Lists.newArrayList();
    private final ChunkPos pos;
    private final short[] changedBlocks = new short[64];
    @Nullable
    private Chunk chunk;
    private int changes;
    private int changedSectionFilter;
    private long lastUpdateInhabitedTime;
    private boolean sentToPlayers;

    public PlayerChunkMapEntry(PlayerChunkMap mapIn, int chunkX, int chunkZ)
    {
        playerChunkMap = mapIn;
        pos = new ChunkPos(chunkX, chunkZ);
        chunk = mapIn.getWorldServer().getChunkProvider().loadChunk(chunkX, chunkZ);
    }

    public ChunkPos getPos()
    {
        return pos;
    }

    public void addPlayer(EntityPlayerMP player)
    {
        if (players.contains(player))
        {
            LOGGER.debug("Failed to add player. {} already is in chunk {}, {}", player, Integer.valueOf(pos.chunkXPos), Integer.valueOf(pos.chunkZPos));
        }
        else
        {
            if (players.isEmpty())
            {
                lastUpdateInhabitedTime = playerChunkMap.getWorldServer().getTotalWorldTime();
            }

            players.add(player);

            if (sentToPlayers)
            {
                sendNearbySpecialEntities(player);
            }
        }
    }

    public void removePlayer(EntityPlayerMP player)
    {
        if (players.contains(player))
        {
            if (sentToPlayers)
            {
                player.connection.sendPacket(new SPacketUnloadChunk(pos.chunkXPos, pos.chunkZPos));
            }

            players.remove(player);

            if (players.isEmpty())
            {
                playerChunkMap.removeEntry(this);
            }
        }
    }

    /**
     * Provide the chunk at the player's location. Can fail, returning false, if the player is a spectator floating
     * outside of any pre-existing chunks, and the server is not configured to allow chunk generation for spectators.
     */
    public boolean providePlayerChunk(boolean canGenerate)
    {
        if (chunk != null)
        {
            return true;
        }
        else
        {
            if (canGenerate)
            {
                chunk = playerChunkMap.getWorldServer().getChunkProvider().provideChunk(pos.chunkXPos, pos.chunkZPos);
            }
            else
            {
                chunk = playerChunkMap.getWorldServer().getChunkProvider().loadChunk(pos.chunkXPos, pos.chunkZPos);
            }

            return chunk != null;
        }
    }

    public boolean sendToPlayers()
    {
        if (sentToPlayers)
        {
            return true;
        }
        else if (chunk == null)
        {
            return false;
        }
        else if (!chunk.isPopulated())
        {
            return false;
        }
        else
        {
            changes = 0;
            changedSectionFilter = 0;
            sentToPlayers = true;
            Packet<?> packet = new SPacketChunkData(chunk, 65535);

            for (EntityPlayerMP entityplayermp : players)
            {
                entityplayermp.connection.sendPacket(packet);
                playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(entityplayermp, chunk);
            }

            return true;
        }
    }

    /**
     * Send packets to player for:
     *  - nearby tile entities
     *  - nearby entities that are leashed
     *  - nearby entities with
     */
    public void sendNearbySpecialEntities(EntityPlayerMP player)
    {
        if (sentToPlayers)
        {
            player.connection.sendPacket(new SPacketChunkData(chunk, 65535));
            playerChunkMap.getWorldServer().getEntityTracker().sendLeashedEntitiesInChunk(player, chunk);
        }
    }

    public void updateChunkInhabitedTime()
    {
        long i = playerChunkMap.getWorldServer().getTotalWorldTime();

        if (chunk != null)
        {
            chunk.setInhabitedTime(chunk.getInhabitedTime() + i - lastUpdateInhabitedTime);
        }

        lastUpdateInhabitedTime = i;
    }

    public void blockChanged(int x, int y, int z)
    {
        if (sentToPlayers)
        {
            if (changes == 0)
            {
                playerChunkMap.addEntry(this);
            }

            changedSectionFilter |= 1 << (y >> 4);

            if (changes < 64)
            {
                short short1 = (short)(x << 12 | z << 8 | y);

                for (int i = 0; i < changes; ++i)
                {
                    if (changedBlocks[i] == short1)
                    {
                        return;
                    }
                }

                changedBlocks[changes++] = short1;
            }
        }
    }

    public void sendPacket(Packet<?> packetIn)
    {
        if (sentToPlayers)
        {
            for (int i = 0; i < players.size(); ++i)
            {
                (players.get(i)).connection.sendPacket(packetIn);
            }
        }
    }

    public void update()
    {
        if (sentToPlayers && chunk != null)
        {
            if (changes != 0)
            {
                if (changes == 1)
                {
                    int i = (changedBlocks[0] >> 12 & 15) + pos.chunkXPos * 16;
                    int j = changedBlocks[0] & 255;
                    int k = (changedBlocks[0] >> 8 & 15) + pos.chunkZPos * 16;
                    BlockPos blockpos = new BlockPos(i, j, k);
                    sendPacket(new SPacketBlockChange(playerChunkMap.getWorldServer(), blockpos));

                    if (playerChunkMap.getWorldServer().getBlockState(blockpos).getBlock().hasTileEntity())
                    {
                        sendBlockEntity(playerChunkMap.getWorldServer().getTileEntity(blockpos));
                    }
                }
                else if (changes == 64)
                {
                    sendPacket(new SPacketChunkData(chunk, changedSectionFilter));
                }
                else
                {
                    sendPacket(new SPacketMultiBlockChange(changes, changedBlocks, chunk));

                    for (int l = 0; l < changes; ++l)
                    {
                        int i1 = (changedBlocks[l] >> 12 & 15) + pos.chunkXPos * 16;
                        int j1 = changedBlocks[l] & 255;
                        int k1 = (changedBlocks[l] >> 8 & 15) + pos.chunkZPos * 16;
                        BlockPos blockpos1 = new BlockPos(i1, j1, k1);

                        if (playerChunkMap.getWorldServer().getBlockState(blockpos1).getBlock().hasTileEntity())
                        {
                            sendBlockEntity(playerChunkMap.getWorldServer().getTileEntity(blockpos1));
                        }
                    }
                }

                changes = 0;
                changedSectionFilter = 0;
            }
        }
    }

    private void sendBlockEntity(@Nullable TileEntity be)
    {
        if (be != null)
        {
            SPacketUpdateTileEntity spacketupdatetileentity = be.getUpdatePacket();

            if (spacketupdatetileentity != null)
            {
                sendPacket(spacketupdatetileentity);
            }
        }
    }

    public boolean containsPlayer(EntityPlayerMP player)
    {
        return players.contains(player);
    }

    public boolean hasPlayerMatching(Predicate<EntityPlayerMP> predicate)
    {
        return Iterables.tryFind(players, predicate).isPresent();
    }

    public boolean hasPlayerMatchingInRange(double range, Predicate<EntityPlayerMP> predicate)
    {
        int i = 0;

        for (int j = players.size(); i < j; ++i)
        {
            EntityPlayerMP entityplayermp = players.get(i);

            if (predicate.apply(entityplayermp) && pos.getDistanceSq(entityplayermp) < range * range)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isSentToPlayers()
    {
        return sentToPlayers;
    }

    @Nullable
    public Chunk getChunk()
    {
        return chunk;
    }

    public double getClosestPlayerDistance()
    {
        double d0 = Double.MAX_VALUE;

        for (EntityPlayerMP entityplayermp : players)
        {
            double d1 = pos.getDistanceSq(entityplayermp);

            if (d1 < d0)
            {
                d0 = d1;
            }
        }

        return d0;
    }
}
