package net.minecraft.entity;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityTrackerEntry
{
    private static final Logger LOGGER = LogManager.getLogger();

    /** The entity that this EntityTrackerEntry tracks. */
    private final Entity trackedEntity;
    private final int range;
    private int maxRange;

    /** check for sync when ticks % updateFrequency==0 */
    private final int updateFrequency;

    /** The encoded entity X position. */
    private long encodedPosX;

    /** The encoded entity Y position. */
    private long encodedPosY;

    /** The encoded entity Z position. */
    private long encodedPosZ;

    /** The encoded entity yaw rotation. */
    private int encodedRotationYaw;

    /** The encoded entity pitch rotation. */
    private int encodedRotationPitch;
    private int lastHeadMotion;
    private double lastTrackedEntityMotionX;
    private double lastTrackedEntityMotionY;
    private double motionZ;
    public int updateCounter;
    private double lastTrackedEntityPosX;
    private double lastTrackedEntityPosY;
    private double lastTrackedEntityPosZ;
    private boolean updatedPlayerVisibility;
    private final boolean sendVelocityUpdates;

    /**
     * every 400 ticks a  full teleport packet is sent, rather than just a "move me +x" command, so that position
     * remains fully synced.
     */
    private int ticksSinceLastForcedTeleport;
    private List<Entity> passengers = Collections.emptyList();
    private boolean ridingEntity;
    private boolean onGround;
    public boolean playerEntitiesUpdated;
    private final Set<EntityPlayerMP> trackingPlayers = Sets.newHashSet();

    public EntityTrackerEntry(Entity entityIn, int rangeIn, int maxRangeIn, int updateFrequencyIn, boolean sendVelocityUpdatesIn)
    {
        trackedEntity = entityIn;
        range = rangeIn;
        maxRange = maxRangeIn;
        updateFrequency = updateFrequencyIn;
        sendVelocityUpdates = sendVelocityUpdatesIn;
        encodedPosX = EntityTracker.getPositionLong(entityIn.posX);
        encodedPosY = EntityTracker.getPositionLong(entityIn.posY);
        encodedPosZ = EntityTracker.getPositionLong(entityIn.posZ);
        encodedRotationYaw = MathHelper.floor(entityIn.rotationYaw * 256.0F / 360.0F);
        encodedRotationPitch = MathHelper.floor(entityIn.rotationPitch * 256.0F / 360.0F);
        lastHeadMotion = MathHelper.floor(entityIn.getRotationYawHead() * 256.0F / 360.0F);
        onGround = entityIn.onGround;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (p_equals_1_ instanceof EntityTrackerEntry)
        {
            return ((EntityTrackerEntry)p_equals_1_).trackedEntity.getEntityId() == trackedEntity.getEntityId();
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return trackedEntity.getEntityId();
    }

    public void updatePlayerList(List<EntityPlayer> players)
    {
        playerEntitiesUpdated = false;

        if (!updatedPlayerVisibility || trackedEntity.getDistanceSq(lastTrackedEntityPosX, lastTrackedEntityPosY, lastTrackedEntityPosZ) > 16.0D)
        {
            lastTrackedEntityPosX = trackedEntity.posX;
            lastTrackedEntityPosY = trackedEntity.posY;
            lastTrackedEntityPosZ = trackedEntity.posZ;
            updatedPlayerVisibility = true;
            playerEntitiesUpdated = true;
            updatePlayerEntities(players);
        }

        List<Entity> list = trackedEntity.getPassengers();

        if (!list.equals(passengers))
        {
            passengers = list;
            sendPacketToTrackedPlayers(new SPacketSetPassengers(trackedEntity));
        }

        if (trackedEntity instanceof EntityItemFrame && updateCounter % 10 == 0)
        {
            EntityItemFrame entityitemframe = (EntityItemFrame) trackedEntity;
            ItemStack itemstack = entityitemframe.getDisplayedItem();

            if (itemstack.getItem() instanceof ItemMap)
            {
                MapData mapdata = Items.FILLED_MAP.getMapData(itemstack, trackedEntity.world);

                for (EntityPlayer entityplayer : players)
                {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
                    mapdata.updateVisiblePlayers(entityplayermp, itemstack);
                    Packet<?> packet = Items.FILLED_MAP.createMapDataPacket(itemstack, trackedEntity.world, entityplayermp);

                    if (packet != null)
                    {
                        entityplayermp.connection.sendPacket(packet);
                    }
                }
            }

            sendMetadataToAllAssociatedPlayers();
        }

        if (updateCounter % updateFrequency == 0 || trackedEntity.isAirBorne || trackedEntity.getDataManager().isDirty())
        {
            if (trackedEntity.isRiding())
            {
                int j1 = MathHelper.floor(trackedEntity.rotationYaw * 256.0F / 360.0F);
                int l1 = MathHelper.floor(trackedEntity.rotationPitch * 256.0F / 360.0F);
                boolean flag3 = Math.abs(j1 - encodedRotationYaw) >= 1 || Math.abs(l1 - encodedRotationPitch) >= 1;

                if (flag3)
                {
                    sendPacketToTrackedPlayers(new SPacketEntity.S16PacketEntityLook(trackedEntity.getEntityId(), (byte)j1, (byte)l1, trackedEntity.onGround));
                    encodedRotationYaw = j1;
                    encodedRotationPitch = l1;
                }

                encodedPosX = EntityTracker.getPositionLong(trackedEntity.posX);
                encodedPosY = EntityTracker.getPositionLong(trackedEntity.posY);
                encodedPosZ = EntityTracker.getPositionLong(trackedEntity.posZ);
                sendMetadataToAllAssociatedPlayers();
                ridingEntity = true;
            }
            else
            {
                ++ticksSinceLastForcedTeleport;
                long i1 = EntityTracker.getPositionLong(trackedEntity.posX);
                long i2 = EntityTracker.getPositionLong(trackedEntity.posY);
                long j2 = EntityTracker.getPositionLong(trackedEntity.posZ);
                int k2 = MathHelper.floor(trackedEntity.rotationYaw * 256.0F / 360.0F);
                int i = MathHelper.floor(trackedEntity.rotationPitch * 256.0F / 360.0F);
                long j = i1 - encodedPosX;
                long k = i2 - encodedPosY;
                long l = j2 - encodedPosZ;
                Packet<?> packet1 = null;
                boolean flag = j * j + k * k + l * l >= 128L || updateCounter % 60 == 0;
                boolean flag1 = Math.abs(k2 - encodedRotationYaw) >= 1 || Math.abs(i - encodedRotationPitch) >= 1;

                if (updateCounter > 0 || trackedEntity instanceof EntityArrow)
                {
                    if (j >= -32768L && j < 32768L && k >= -32768L && k < 32768L && l >= -32768L && l < 32768L && ticksSinceLastForcedTeleport <= 400 && !ridingEntity && onGround == trackedEntity.onGround)
                    {
                        if ((!flag || !flag1) && !(trackedEntity instanceof EntityArrow))
                        {
                            if (flag)
                            {
                                packet1 = new SPacketEntity.S15PacketEntityRelMove(trackedEntity.getEntityId(), j, k, l, trackedEntity.onGround);
                            }
                            else if (flag1)
                            {
                                packet1 = new SPacketEntity.S16PacketEntityLook(trackedEntity.getEntityId(), (byte)k2, (byte)i, trackedEntity.onGround);
                            }
                        }
                        else
                        {
                            packet1 = new SPacketEntity.S17PacketEntityLookMove(trackedEntity.getEntityId(), j, k, l, (byte)k2, (byte)i, trackedEntity.onGround);
                        }
                    }
                    else
                    {
                        onGround = trackedEntity.onGround;
                        ticksSinceLastForcedTeleport = 0;
                        resetPlayerVisibility();
                        packet1 = new SPacketEntityTeleport(trackedEntity);
                    }
                }

                boolean flag2 = sendVelocityUpdates;

                if (trackedEntity instanceof EntityLivingBase && ((EntityLivingBase) trackedEntity).isElytraFlying())
                {
                    flag2 = true;
                }

                if (flag2 && updateCounter > 0)
                {
                    double d0 = trackedEntity.motionX - lastTrackedEntityMotionX;
                    double d1 = trackedEntity.motionY - lastTrackedEntityMotionY;
                    double d2 = trackedEntity.motionZ - motionZ;
                    double d3 = 0.02D;
                    double d4 = d0 * d0 + d1 * d1 + d2 * d2;

                    if (d4 > 4.0E-4D || d4 > 0.0D && trackedEntity.motionX == 0.0D && trackedEntity.motionY == 0.0D && trackedEntity.motionZ == 0.0D)
                    {
                        lastTrackedEntityMotionX = trackedEntity.motionX;
                        lastTrackedEntityMotionY = trackedEntity.motionY;
                        motionZ = trackedEntity.motionZ;
                        sendPacketToTrackedPlayers(new SPacketEntityVelocity(trackedEntity.getEntityId(), lastTrackedEntityMotionX, lastTrackedEntityMotionY, motionZ));
                    }
                }

                if (packet1 != null)
                {
                    sendPacketToTrackedPlayers(packet1);
                }

                sendMetadataToAllAssociatedPlayers();

                if (flag)
                {
                    encodedPosX = i1;
                    encodedPosY = i2;
                    encodedPosZ = j2;
                }

                if (flag1)
                {
                    encodedRotationYaw = k2;
                    encodedRotationPitch = i;
                }

                ridingEntity = false;
            }

            int k1 = MathHelper.floor(trackedEntity.getRotationYawHead() * 256.0F / 360.0F);

            if (Math.abs(k1 - lastHeadMotion) >= 1)
            {
                sendPacketToTrackedPlayers(new SPacketEntityHeadLook(trackedEntity, (byte)k1));
                lastHeadMotion = k1;
            }

            trackedEntity.isAirBorne = false;
        }

        ++updateCounter;

        if (trackedEntity.velocityChanged)
        {
            sendToTrackingAndSelf(new SPacketEntityVelocity(trackedEntity));
            trackedEntity.velocityChanged = false;
        }
    }

    /**
     * Sends the entity metadata (DataWatcher) and attributes to all players tracking this entity, including the entity
     * itself if a player.
     */
    private void sendMetadataToAllAssociatedPlayers()
    {
        EntityDataManager entitydatamanager = trackedEntity.getDataManager();

        if (entitydatamanager.isDirty())
        {
            sendToTrackingAndSelf(new SPacketEntityMetadata(trackedEntity.getEntityId(), entitydatamanager, false));
        }

        if (trackedEntity instanceof EntityLivingBase)
        {
            AttributeMap attributemap = (AttributeMap)((EntityLivingBase) trackedEntity).getAttributeMap();
            Set<IAttributeInstance> set = attributemap.getAttributeInstanceSet();

            if (!set.isEmpty())
            {
                sendToTrackingAndSelf(new SPacketEntityProperties(trackedEntity.getEntityId(), set));
            }

            set.clear();
        }
    }

    /**
     * Send the given packet to all players tracking this entity.
     */
    public void sendPacketToTrackedPlayers(Packet<?> packetIn)
    {
        for (EntityPlayerMP entityplayermp : trackingPlayers)
        {
            entityplayermp.connection.sendPacket(packetIn);
        }
    }

    public void sendToTrackingAndSelf(Packet<?> packetIn)
    {
        sendPacketToTrackedPlayers(packetIn);

        if (trackedEntity instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) trackedEntity).connection.sendPacket(packetIn);
        }
    }

    public void sendDestroyEntityPacketToTrackedPlayers()
    {
        for (EntityPlayerMP entityplayermp : trackingPlayers)
        {
            trackedEntity.removeTrackingPlayer(entityplayermp);
            entityplayermp.removeEntity(trackedEntity);
        }
    }

    public void removeFromTrackedPlayers(EntityPlayerMP playerMP)
    {
        if (trackingPlayers.contains(playerMP))
        {
            trackedEntity.removeTrackingPlayer(playerMP);
            playerMP.removeEntity(trackedEntity);
            trackingPlayers.remove(playerMP);
        }
    }

    public void updatePlayerEntity(EntityPlayerMP playerMP)
    {
        if (playerMP != trackedEntity)
        {
            if (isVisibleTo(playerMP))
            {
                if (!trackingPlayers.contains(playerMP) && (isPlayerWatchingThisChunk(playerMP) || trackedEntity.forceSpawn))
                {
                    trackingPlayers.add(playerMP);
                    Packet<?> packet = createSpawnPacket();
                    playerMP.connection.sendPacket(packet);

                    if (!trackedEntity.getDataManager().isEmpty())
                    {
                        playerMP.connection.sendPacket(new SPacketEntityMetadata(trackedEntity.getEntityId(), trackedEntity.getDataManager(), true));
                    }

                    boolean flag = sendVelocityUpdates;

                    if (trackedEntity instanceof EntityLivingBase)
                    {
                        AttributeMap attributemap = (AttributeMap)((EntityLivingBase) trackedEntity).getAttributeMap();
                        Collection<IAttributeInstance> collection = attributemap.getWatchedAttributes();

                        if (!collection.isEmpty())
                        {
                            playerMP.connection.sendPacket(new SPacketEntityProperties(trackedEntity.getEntityId(), collection));
                        }

                        if (((EntityLivingBase) trackedEntity).isElytraFlying())
                        {
                            flag = true;
                        }
                    }

                    lastTrackedEntityMotionX = trackedEntity.motionX;
                    lastTrackedEntityMotionY = trackedEntity.motionY;
                    motionZ = trackedEntity.motionZ;

                    if (flag && !(packet instanceof SPacketSpawnMob))
                    {
                        playerMP.connection.sendPacket(new SPacketEntityVelocity(trackedEntity.getEntityId(), trackedEntity.motionX, trackedEntity.motionY, trackedEntity.motionZ));
                    }

                    if (trackedEntity instanceof EntityLivingBase)
                    {
                        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
                        {
                            ItemStack itemstack = ((EntityLivingBase) trackedEntity).getItemStackFromSlot(entityequipmentslot);

                            if (!itemstack.isEmpty())
                            {
                                playerMP.connection.sendPacket(new SPacketEntityEquipment(trackedEntity.getEntityId(), entityequipmentslot, itemstack));
                            }
                        }
                    }

                    if (trackedEntity instanceof EntityPlayer)
                    {
                        EntityPlayer entityplayer = (EntityPlayer) trackedEntity;

                        if (entityplayer.isPlayerSleeping())
                        {
                            playerMP.connection.sendPacket(new SPacketUseBed(entityplayer, new BlockPos(trackedEntity)));
                        }
                    }

                    if (trackedEntity instanceof EntityLivingBase)
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase) trackedEntity;

                        for (PotionEffect potioneffect : entitylivingbase.getActivePotionEffects())
                        {
                            playerMP.connection.sendPacket(new SPacketEntityEffect(trackedEntity.getEntityId(), potioneffect));
                        }
                    }

                    if (!trackedEntity.getPassengers().isEmpty())
                    {
                        playerMP.connection.sendPacket(new SPacketSetPassengers(trackedEntity));
                    }

                    if (trackedEntity.isRiding())
                    {
                        playerMP.connection.sendPacket(new SPacketSetPassengers(trackedEntity.getRidingEntity()));
                    }

                    trackedEntity.addTrackingPlayer(playerMP);
                    playerMP.addEntity(trackedEntity);
                }
            }
            else if (trackingPlayers.contains(playerMP))
            {
                trackingPlayers.remove(playerMP);
                trackedEntity.removeTrackingPlayer(playerMP);
                playerMP.removeEntity(trackedEntity);
            }
        }
    }

    public boolean isVisibleTo(EntityPlayerMP playerMP)
    {
        double d0 = playerMP.posX - (double) encodedPosX / 4096.0D;
        double d1 = playerMP.posZ - (double) encodedPosZ / 4096.0D;
        int i = Math.min(range, maxRange);
        return d0 >= (double)(-i) && d0 <= (double)i && d1 >= (double)(-i) && d1 <= (double)i && trackedEntity.isSpectatedByPlayer(playerMP);
    }

    private boolean isPlayerWatchingThisChunk(EntityPlayerMP playerMP)
    {
        return playerMP.getServerWorld().getPlayerChunkMap().isPlayerWatchingChunk(playerMP, trackedEntity.chunkCoordX, trackedEntity.chunkCoordZ);
    }

    public void updatePlayerEntities(List<EntityPlayer> players)
    {
        for (int i = 0; i < players.size(); ++i)
        {
            updatePlayerEntity((EntityPlayerMP)players.get(i));
        }
    }

    private Packet<?> createSpawnPacket()
    {
        if (trackedEntity.isDead)
        {
            LOGGER.warn("Fetching addPacket for removed entity");
        }

        if (trackedEntity instanceof EntityPlayerMP)
        {
            return new SPacketSpawnPlayer((EntityPlayer) trackedEntity);
        }
        else if (trackedEntity instanceof IAnimals)
        {
            lastHeadMotion = MathHelper.floor(trackedEntity.getRotationYawHead() * 256.0F / 360.0F);
            return new SPacketSpawnMob((EntityLivingBase) trackedEntity);
        }
        else if (trackedEntity instanceof EntityPainting)
        {
            return new SPacketSpawnPainting((EntityPainting) trackedEntity);
        }
        else if (trackedEntity instanceof EntityItem)
        {
            return new SPacketSpawnObject(trackedEntity, 2, 1);
        }
        else if (trackedEntity instanceof EntityMinecart)
        {
            EntityMinecart entityminecart = (EntityMinecart) trackedEntity;
            return new SPacketSpawnObject(trackedEntity, 10, entityminecart.getType().getId());
        }
        else if (trackedEntity instanceof EntityBoat)
        {
            return new SPacketSpawnObject(trackedEntity, 1);
        }
        else if (trackedEntity instanceof EntityXPOrb)
        {
            return new SPacketSpawnExperienceOrb((EntityXPOrb) trackedEntity);
        }
        else if (trackedEntity instanceof EntityFishHook)
        {
            Entity entity2 = ((EntityFishHook) trackedEntity).func_190619_l();
            return new SPacketSpawnObject(trackedEntity, 90, entity2 == null ? trackedEntity.getEntityId() : entity2.getEntityId());
        }
        else if (trackedEntity instanceof EntitySpectralArrow)
        {
            Entity entity1 = ((EntitySpectralArrow) trackedEntity).shootingEntity;
            return new SPacketSpawnObject(trackedEntity, 91, 1 + (entity1 == null ? trackedEntity.getEntityId() : entity1.getEntityId()));
        }
        else if (trackedEntity instanceof EntityTippedArrow)
        {
            Entity entity = ((EntityArrow) trackedEntity).shootingEntity;
            return new SPacketSpawnObject(trackedEntity, 60, 1 + (entity == null ? trackedEntity.getEntityId() : entity.getEntityId()));
        }
        else if (trackedEntity instanceof EntitySnowball)
        {
            return new SPacketSpawnObject(trackedEntity, 61);
        }
        else if (trackedEntity instanceof EntityLlamaSpit)
        {
            return new SPacketSpawnObject(trackedEntity, 68);
        }
        else if (trackedEntity instanceof EntityPotion)
        {
            return new SPacketSpawnObject(trackedEntity, 73);
        }
        else if (trackedEntity instanceof EntityExpBottle)
        {
            return new SPacketSpawnObject(trackedEntity, 75);
        }
        else if (trackedEntity instanceof EntityEnderPearl)
        {
            return new SPacketSpawnObject(trackedEntity, 65);
        }
        else if (trackedEntity instanceof EntityEnderEye)
        {
            return new SPacketSpawnObject(trackedEntity, 72);
        }
        else if (trackedEntity instanceof EntityFireworkRocket)
        {
            return new SPacketSpawnObject(trackedEntity, 76);
        }
        else if (trackedEntity instanceof EntityFireball)
        {
            EntityFireball entityfireball = (EntityFireball) trackedEntity;
            SPacketSpawnObject spacketspawnobject = null;
            int i = 63;

            if (trackedEntity instanceof EntitySmallFireball)
            {
                i = 64;
            }
            else if (trackedEntity instanceof EntityDragonFireball)
            {
                i = 93;
            }
            else if (trackedEntity instanceof EntityWitherSkull)
            {
                i = 66;
            }

            if (entityfireball.shootingEntity != null)
            {
                spacketspawnobject = new SPacketSpawnObject(trackedEntity, i, ((EntityFireball) trackedEntity).shootingEntity.getEntityId());
            }
            else
            {
                spacketspawnobject = new SPacketSpawnObject(trackedEntity, i, 0);
            }

            spacketspawnobject.setSpeedX((int)(entityfireball.accelerationX * 8000.0D));
            spacketspawnobject.setSpeedY((int)(entityfireball.accelerationY * 8000.0D));
            spacketspawnobject.setSpeedZ((int)(entityfireball.accelerationZ * 8000.0D));
            return spacketspawnobject;
        }
        else if (trackedEntity instanceof EntityShulkerBullet)
        {
            SPacketSpawnObject spacketspawnobject1 = new SPacketSpawnObject(trackedEntity, 67, 0);
            spacketspawnobject1.setSpeedX((int)(trackedEntity.motionX * 8000.0D));
            spacketspawnobject1.setSpeedY((int)(trackedEntity.motionY * 8000.0D));
            spacketspawnobject1.setSpeedZ((int)(trackedEntity.motionZ * 8000.0D));
            return spacketspawnobject1;
        }
        else if (trackedEntity instanceof EntityEgg)
        {
            return new SPacketSpawnObject(trackedEntity, 62);
        }
        else if (trackedEntity instanceof EntityEvokerFangs)
        {
            return new SPacketSpawnObject(trackedEntity, 79);
        }
        else if (trackedEntity instanceof EntityTNTPrimed)
        {
            return new SPacketSpawnObject(trackedEntity, 50);
        }
        else if (trackedEntity instanceof EntityEnderCrystal)
        {
            return new SPacketSpawnObject(trackedEntity, 51);
        }
        else if (trackedEntity instanceof EntityFallingBlock)
        {
            EntityFallingBlock entityfallingblock = (EntityFallingBlock) trackedEntity;
            return new SPacketSpawnObject(trackedEntity, 70, Block.getStateId(entityfallingblock.getBlock()));
        }
        else if (trackedEntity instanceof EntityArmorStand)
        {
            return new SPacketSpawnObject(trackedEntity, 78);
        }
        else if (trackedEntity instanceof EntityItemFrame)
        {
            EntityItemFrame entityitemframe = (EntityItemFrame) trackedEntity;
            return new SPacketSpawnObject(trackedEntity, 71, entityitemframe.facingDirection.getHorizontalIndex(), entityitemframe.getHangingPosition());
        }
        else if (trackedEntity instanceof EntityLeashKnot)
        {
            EntityLeashKnot entityleashknot = (EntityLeashKnot) trackedEntity;
            return new SPacketSpawnObject(trackedEntity, 77, 0, entityleashknot.getHangingPosition());
        }
        else if (trackedEntity instanceof EntityAreaEffectCloud)
        {
            return new SPacketSpawnObject(trackedEntity, 3);
        }
        else
        {
            throw new IllegalArgumentException("Don't know how to add " + trackedEntity.getClass() + "!");
        }
    }

    /**
     * Remove a tracked player from our list and tell the tracked player to destroy us from their world.
     */
    public void removeTrackedPlayerSymmetric(EntityPlayerMP playerMP)
    {
        if (trackingPlayers.contains(playerMP))
        {
            trackingPlayers.remove(playerMP);
            trackedEntity.removeTrackingPlayer(playerMP);
            playerMP.removeEntity(trackedEntity);
        }
    }

    public Entity getTrackedEntity()
    {
        return trackedEntity;
    }

    public void setMaxRange(int maxRangeIn)
    {
        maxRange = maxRangeIn;
    }

    public void resetPlayerVisibility()
    {
        updatedPlayerVisibility = false;
    }
}
