package net.minecraft.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTracker
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final WorldServer theWorld;
    private final Set<EntityTrackerEntry> trackedEntities = Sets.newHashSet();
    private final IntHashMap<EntityTrackerEntry> trackedEntityHashTable = new IntHashMap<EntityTrackerEntry>();
    private int maxTrackingDistanceThreshold;

    public EntityTracker(WorldServer theWorldIn)
    {
        theWorld = theWorldIn;
        maxTrackingDistanceThreshold = theWorldIn.getInstanceServer().getPlayerList().getEntityViewDistance();
    }

    public static long getPositionLong(double value)
    {
        return MathHelper.lFloor(value * 4096.0D);
    }

    public static void updateServerPosition(Entity entityIn, double x, double y, double z)
    {
        entityIn.serverPosX = getPositionLong(x);
        entityIn.serverPosY = getPositionLong(y);
        entityIn.serverPosZ = getPositionLong(z);
    }

    public void trackEntity(Entity entityIn)
    {
        if (entityIn instanceof EntityPlayerMP)
        {
            trackEntity(entityIn, 512, 2);
            EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;

            for (EntityTrackerEntry entitytrackerentry : trackedEntities)
            {
                if (entitytrackerentry.getTrackedEntity() != entityplayermp)
                {
                    entitytrackerentry.updatePlayerEntity(entityplayermp);
                }
            }
        }
        else if (entityIn instanceof EntityFishHook)
        {
            addEntityToTracker(entityIn, 64, 5, true);
        }
        else if (entityIn instanceof EntityArrow)
        {
            addEntityToTracker(entityIn, 64, 20, false);
        }
        else if (entityIn instanceof EntitySmallFireball)
        {
            addEntityToTracker(entityIn, 64, 10, false);
        }
        else if (entityIn instanceof EntityFireball)
        {
            addEntityToTracker(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntitySnowball)
        {
            addEntityToTracker(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityLlamaSpit)
        {
            addEntityToTracker(entityIn, 64, 10, false);
        }
        else if (entityIn instanceof EntityEnderPearl)
        {
            addEntityToTracker(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityEnderEye)
        {
            addEntityToTracker(entityIn, 64, 4, true);
        }
        else if (entityIn instanceof EntityEgg)
        {
            addEntityToTracker(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityPotion)
        {
            addEntityToTracker(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityExpBottle)
        {
            addEntityToTracker(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityFireworkRocket)
        {
            addEntityToTracker(entityIn, 64, 10, true);
        }
        else if (entityIn instanceof EntityItem)
        {
            addEntityToTracker(entityIn, 64, 20, true);
        }
        else if (entityIn instanceof EntityMinecart)
        {
            addEntityToTracker(entityIn, 80, 3, true);
        }
        else if (entityIn instanceof EntityBoat)
        {
            addEntityToTracker(entityIn, 80, 3, true);
        }
        else if (entityIn instanceof EntitySquid)
        {
            addEntityToTracker(entityIn, 64, 3, true);
        }
        else if (entityIn instanceof EntityWither)
        {
            addEntityToTracker(entityIn, 80, 3, false);
        }
        else if (entityIn instanceof EntityShulkerBullet)
        {
            addEntityToTracker(entityIn, 80, 3, true);
        }
        else if (entityIn instanceof EntityBat)
        {
            addEntityToTracker(entityIn, 80, 3, false);
        }
        else if (entityIn instanceof EntityDragon)
        {
            addEntityToTracker(entityIn, 160, 3, true);
        }
        else if (entityIn instanceof IAnimals)
        {
            addEntityToTracker(entityIn, 80, 3, true);
        }
        else if (entityIn instanceof EntityTNTPrimed)
        {
            addEntityToTracker(entityIn, 160, 10, true);
        }
        else if (entityIn instanceof EntityFallingBlock)
        {
            addEntityToTracker(entityIn, 160, 20, true);
        }
        else if (entityIn instanceof EntityHanging)
        {
            addEntityToTracker(entityIn, 160, Integer.MAX_VALUE, false);
        }
        else if (entityIn instanceof EntityArmorStand)
        {
            addEntityToTracker(entityIn, 160, 3, true);
        }
        else if (entityIn instanceof EntityXPOrb)
        {
            addEntityToTracker(entityIn, 160, 20, true);
        }
        else if (entityIn instanceof EntityAreaEffectCloud)
        {
            addEntityToTracker(entityIn, 160, Integer.MAX_VALUE, true);
        }
        else if (entityIn instanceof EntityEnderCrystal)
        {
            addEntityToTracker(entityIn, 256, Integer.MAX_VALUE, false);
        }
        else if (entityIn instanceof EntityEvokerFangs)
        {
            addEntityToTracker(entityIn, 160, 2, false);
        }
    }

    public void trackEntity(Entity entityIn, int trackingRange, int updateFrequency)
    {
        addEntityToTracker(entityIn, trackingRange, updateFrequency, false);
    }

    /**
     * Args : Entity, trackingRange, updateFrequency, sendVelocityUpdates
     */
    public void addEntityToTracker(Entity entityIn, int trackingRange, int updateFrequency, boolean sendVelocityUpdates)
    {
        try
        {
            if (trackedEntityHashTable.containsItem(entityIn.getEntityId()))
            {
                throw new IllegalStateException("Entity is already tracked!");
            }

            EntityTrackerEntry entitytrackerentry = new EntityTrackerEntry(entityIn, trackingRange, maxTrackingDistanceThreshold, updateFrequency, sendVelocityUpdates);
            trackedEntities.add(entitytrackerentry);
            trackedEntityHashTable.addKey(entityIn.getEntityId(), entitytrackerentry);
            entitytrackerentry.updatePlayerEntities(theWorld.playerEntities);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding entity to track");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity To Track");
            crashreportcategory.addCrashSection("Tracking range", trackingRange + " blocks");
            crashreportcategory.setDetail("Update interval", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    String s = "Once per " + updateFrequency + " ticks";

                    if (updateFrequency == Integer.MAX_VALUE)
                    {
                        s = "Maximum (" + s + ")";
                    }

                    return s;
                }
            });
            entityIn.addEntityCrashInfo(crashreportcategory);
            trackedEntityHashTable.lookup(entityIn.getEntityId()).getTrackedEntity().addEntityCrashInfo(crashreport.makeCategory("Entity That Is Already Tracked"));

            try
            {
                throw new ReportedException(crashreport);
            }
            catch (ReportedException reportedexception)
            {
                LOGGER.error("\"Silently\" catching entity tracking error.", reportedexception);
            }
        }
    }

    public void untrackEntity(Entity entityIn)
    {
        if (entityIn instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)entityIn;

            for (EntityTrackerEntry entitytrackerentry : trackedEntities)
            {
                entitytrackerentry.removeFromTrackedPlayers(entityplayermp);
            }
        }

        EntityTrackerEntry entitytrackerentry1 = trackedEntityHashTable.removeObject(entityIn.getEntityId());

        if (entitytrackerentry1 != null)
        {
            trackedEntities.remove(entitytrackerentry1);
            entitytrackerentry1.sendDestroyEntityPacketToTrackedPlayers();
        }
    }

    public void updateTrackedEntities()
    {
        List<EntityPlayerMP> list = Lists.newArrayList();

        for (EntityTrackerEntry entitytrackerentry : trackedEntities)
        {
            entitytrackerentry.updatePlayerList(theWorld.playerEntities);

            if (entitytrackerentry.playerEntitiesUpdated)
            {
                Entity entity = entitytrackerentry.getTrackedEntity();

                if (entity instanceof EntityPlayerMP)
                {
                    list.add((EntityPlayerMP)entity);
                }
            }
        }

        for (int i = 0; i < list.size(); ++i)
        {
            EntityPlayerMP entityplayermp = list.get(i);

            for (EntityTrackerEntry entitytrackerentry1 : trackedEntities)
            {
                if (entitytrackerentry1.getTrackedEntity() != entityplayermp)
                {
                    entitytrackerentry1.updatePlayerEntity(entityplayermp);
                }
            }
        }
    }

    public void updateVisibility(EntityPlayerMP player)
    {
        for (EntityTrackerEntry entitytrackerentry : trackedEntities)
        {
            if (entitytrackerentry.getTrackedEntity() == player)
            {
                entitytrackerentry.updatePlayerEntities(theWorld.playerEntities);
            }
            else
            {
                entitytrackerentry.updatePlayerEntity(player);
            }
        }
    }

    public void sendToAllTrackingEntity(Entity entityIn, Packet<?> packetIn)
    {
        EntityTrackerEntry entitytrackerentry = trackedEntityHashTable.lookup(entityIn.getEntityId());

        if (entitytrackerentry != null)
        {
            entitytrackerentry.sendPacketToTrackedPlayers(packetIn);
        }
    }

    public void sendToTrackingAndSelf(Entity entityIn, Packet<?> packetIn)
    {
        EntityTrackerEntry entitytrackerentry = trackedEntityHashTable.lookup(entityIn.getEntityId());

        if (entitytrackerentry != null)
        {
            entitytrackerentry.sendToTrackingAndSelf(packetIn);
        }
    }

    public void removePlayerFromTrackers(EntityPlayerMP player)
    {
        for (EntityTrackerEntry entitytrackerentry : trackedEntities)
        {
            entitytrackerentry.removeTrackedPlayerSymmetric(player);
        }
    }

    /**
     * Send packets to player for every tracked entity in this chunk that is either leashed to something or someone, or
     * has passengers
     */
    public void sendLeashedEntitiesInChunk(EntityPlayerMP player, Chunk chunkIn)
    {
        List<Entity> list = Lists.newArrayList();
        List<Entity> list1 = Lists.newArrayList();

        for (EntityTrackerEntry entitytrackerentry : trackedEntities)
        {
            Entity entity = entitytrackerentry.getTrackedEntity();

            if (entity != player && entity.chunkCoordX == chunkIn.xPosition && entity.chunkCoordZ == chunkIn.zPosition)
            {
                entitytrackerentry.updatePlayerEntity(player);

                if (entity instanceof EntityLiving && ((EntityLiving)entity).getLeashedToEntity() != null)
                {
                    list.add(entity);
                }

                if (!entity.getPassengers().isEmpty())
                {
                    list1.add(entity);
                }
            }
        }

        if (!list.isEmpty())
        {
            for (Entity entity1 : list)
            {
                player.connection.sendPacket(new SPacketEntityAttach(entity1, ((EntityLiving)entity1).getLeashedToEntity()));
            }
        }

        if (!list1.isEmpty())
        {
            for (Entity entity2 : list1)
            {
                player.connection.sendPacket(new SPacketSetPassengers(entity2));
            }
        }
    }

    public void setViewDistance(int p_187252_1_)
    {
        maxTrackingDistanceThreshold = (p_187252_1_ - 1) * 16;

        for (EntityTrackerEntry entitytrackerentry : trackedEntities)
        {
            entitytrackerentry.setMaxRange(maxTrackingDistanceThreshold);
        }
    }
}
