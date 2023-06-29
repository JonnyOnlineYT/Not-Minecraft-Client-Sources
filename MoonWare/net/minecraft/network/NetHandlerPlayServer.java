package net.minecraft.network;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;
import com.google.common.util.concurrent.Futures;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketClientSettings;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEnchantItem;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlaceRecipe;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.network.play.client.CPacketSteerBoat;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.Mirror;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Namespaced;
import net.minecraft.util.Rotation;
import net.minecraft.util.ServerRecipeBookHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.Formatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NetHandlerPlayServer implements INetHandlerPlayServer, ITickable
{
    private static final Logger LOGGER = LogManager.getLogger();
    public final NetworkManager netManager;
    private final MinecraftServer serverController;
    public EntityPlayerMP playerEntity;
    private int networkTickCount;
    private long field_194402_f;
    private boolean field_194403_g;
    private long field_194404_h;

    /**
     * Incremented by 20 each time a user sends a chat message, decreased by one every tick. Non-ops kicked when over
     * 200
     */
    private int chatSpamThresholdCount;
    private int itemDropThreshold;
    private final IntHashMap<Short> pendingTransactions = new IntHashMap<Short>();
    private double firstGoodX;
    private double firstGoodY;
    private double firstGoodZ;
    private double lastGoodX;
    private double lastGoodY;
    private double lastGoodZ;
    private Entity lowestRiddenEnt;
    private double lowestRiddenX;
    private double lowestRiddenY;
    private double lowestRiddenZ;
    private double lowestRiddenX1;
    private double lowestRiddenY1;
    private double lowestRiddenZ1;
    private Vec3d targetPos;
    private int teleportId;
    private int lastPositionUpdate;
    private boolean floating;

    /**
     * Used to keep track of how the player is floating while gamerules should prevent that. Surpassing 80 ticks means
     * kick
     */
    private int floatingTickCount;
    private boolean vehicleFloating;

    /**
     * Used to keep track of how long the player is floating in a vehicle. Surpassing 80 means a kick
     */
    private int vehicleFloatingTickCount;
    private int movePacketCounter;
    private int lastMovePacketCounter;
    private ServerRecipeBookHelper field_194309_H = new ServerRecipeBookHelper();

    public NetHandlerPlayServer(MinecraftServer server, NetworkManager networkManagerIn, EntityPlayerMP playerIn)
    {
        serverController = server;
        netManager = networkManagerIn;
        networkManagerIn.setNetHandler(this);
        playerEntity = playerIn;
        playerIn.connection = this;
    }

    /**
     * Like the old updateEntity(), except more generic.
     */
    public void update()
    {
        captureCurrentPosition();
        playerEntity.onUpdateEntity();
        playerEntity.setPositionAndRotation(firstGoodX, firstGoodY, firstGoodZ, playerEntity.rotationYaw, playerEntity.rotationPitch);
        ++networkTickCount;
        lastMovePacketCounter = movePacketCounter;

        if (floating)
        {
            if (++floatingTickCount > 80)
            {
                LOGGER.warn("{} was kicked for floating too long!", playerEntity.getName());
                func_194028_b(new TranslatableComponent("multiplayer.disconnect.flying"));
                return;
            }
        }
        else
        {
            floating = false;
            floatingTickCount = 0;
        }

        lowestRiddenEnt = playerEntity.getLowestRidingEntity();

        if (lowestRiddenEnt != playerEntity && lowestRiddenEnt.getControllingPassenger() == playerEntity)
        {
            lowestRiddenX = lowestRiddenEnt.posX;
            lowestRiddenY = lowestRiddenEnt.posY;
            lowestRiddenZ = lowestRiddenEnt.posZ;
            lowestRiddenX1 = lowestRiddenEnt.posX;
            lowestRiddenY1 = lowestRiddenEnt.posY;
            lowestRiddenZ1 = lowestRiddenEnt.posZ;

            if (vehicleFloating && playerEntity.getLowestRidingEntity().getControllingPassenger() == playerEntity)
            {
                if (++vehicleFloatingTickCount > 80)
                {
                    LOGGER.warn("{} was kicked for floating a vehicle too long!", playerEntity.getName());
                    func_194028_b(new TranslatableComponent("multiplayer.disconnect.flying"));
                    return;
                }
            }
            else
            {
                vehicleFloating = false;
                vehicleFloatingTickCount = 0;
            }
        }
        else
        {
            lowestRiddenEnt = null;
            vehicleFloating = false;
            vehicleFloatingTickCount = 0;
        }

        serverController.theProfiler.startSection("keepAlive");
        long i = currentTimeMillis();

        if (i - field_194402_f >= 15000L)
        {
            if (field_194403_g)
            {
                func_194028_b(new TranslatableComponent("disconnect.timeout"));
            }
            else
            {
                field_194403_g = true;
                field_194402_f = i;
                field_194404_h = i;
                sendPacket(new SPacketKeepAlive(field_194404_h));
            }
        }

        serverController.theProfiler.endSection();

        if (chatSpamThresholdCount > 0)
        {
            --chatSpamThresholdCount;
        }

        if (itemDropThreshold > 0)
        {
            --itemDropThreshold;
        }

        if (playerEntity.getLastActiveTime() > 0L && serverController.getMaxPlayerIdleMinutes() > 0 && MinecraftServer.getCurrentTimeMillis() - playerEntity.getLastActiveTime() > (long)(serverController.getMaxPlayerIdleMinutes() * 1000 * 60))
        {
            func_194028_b(new TranslatableComponent("multiplayer.disconnect.idling"));
        }
    }

    private void captureCurrentPosition()
    {
        firstGoodX = playerEntity.posX;
        firstGoodY = playerEntity.posY;
        firstGoodZ = playerEntity.posZ;
        lastGoodX = playerEntity.posX;
        lastGoodY = playerEntity.posY;
        lastGoodZ = playerEntity.posZ;
    }

    public NetworkManager getNetworkManager()
    {
        return netManager;
    }

    public void func_194028_b(Component p_194028_1_)
    {
        netManager.sendPacket(new SPacketDisconnect(p_194028_1_), new GenericFutureListener < Future <? super Void >> ()
        {
            public void operationComplete(Future <? super Void > p_operationComplete_1_) throws Exception
            {
                netManager.closeChannel(p_194028_1_);
            }
        });
        netManager.disableAutoRead();
        Futures.getUnchecked(serverController.addScheduledTask(new Runnable()
        {
            public void run()
            {
                netManager.checkDisconnected();
            }
        }));
    }

    /**
     * Processes player movement input. Includes walking, strafing, jumping, sneaking; excludes riding and toggling
     * flying/sprinting
     */
    public void processInput(CPacketInput packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.setEntityActionState(packetIn.getStrafeSpeed(), packetIn.func_192620_b(), packetIn.isJumping(), packetIn.isSneaking());
    }

    private static boolean isMovePlayerPacketInvalid(CPacketPlayer packetIn)
    {
        if (Doubles.isFinite(packetIn.getX(0.0D)) && Doubles.isFinite(packetIn.getY(0.0D)) && Doubles.isFinite(packetIn.getZ(0.0D)) && Floats.isFinite(packetIn.getPitch(0.0F)) && Floats.isFinite(packetIn.getYaw(0.0F)))
        {
            return Math.abs(packetIn.getX(0.0D)) > 3.0E7D || Math.abs(packetIn.getY(0.0D)) > 3.0E7D || Math.abs(packetIn.getZ(0.0D)) > 3.0E7D;
        }
        else
        {
            return true;
        }
    }

    private static boolean isMoveVehiclePacketInvalid(CPacketVehicleMove packetIn)
    {
        return !Doubles.isFinite(packetIn.getX()) || !Doubles.isFinite(packetIn.getY()) || !Doubles.isFinite(packetIn.getZ()) || !Floats.isFinite(packetIn.getPitch()) || !Floats.isFinite(packetIn.getYaw());
    }

    public void processVehicleMove(CPacketVehicleMove packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());

        if (isMoveVehiclePacketInvalid(packetIn))
        {
            func_194028_b(new TranslatableComponent("multiplayer.disconnect.invalid_vehicle_movement"));
        }
        else
        {
            Entity entity = playerEntity.getLowestRidingEntity();

            if (entity != playerEntity && entity.getControllingPassenger() == playerEntity && entity == lowestRiddenEnt)
            {
                WorldServer worldserver = playerEntity.getServerWorld();
                double d0 = entity.posX;
                double d1 = entity.posY;
                double d2 = entity.posZ;
                double d3 = packetIn.getX();
                double d4 = packetIn.getY();
                double d5 = packetIn.getZ();
                float f = packetIn.getYaw();
                float f1 = packetIn.getPitch();
                double d6 = d3 - lowestRiddenX;
                double d7 = d4 - lowestRiddenY;
                double d8 = d5 - lowestRiddenZ;
                double d9 = entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ;
                double d10 = d6 * d6 + d7 * d7 + d8 * d8;

                if (d10 - d9 > 100.0D && (!serverController.isSinglePlayer() || !serverController.getServerOwner().equals(entity.getName())))
                {
                    LOGGER.warn("{} (vehicle of {}) moved too quickly! {},{},{}", entity.getName(), playerEntity.getName(), Double.valueOf(d6), Double.valueOf(d7), Double.valueOf(d8));
                    netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }

                boolean flag = worldserver.getCollisionBoxes(entity, entity.getEntityBoundingBox().contract(0.0625D)).isEmpty();
                d6 = d3 - lowestRiddenX1;
                d7 = d4 - lowestRiddenY1 - 1.0E-6D;
                d8 = d5 - lowestRiddenZ1;
                entity.moveEntity(MoverType.PLAYER, d6, d7, d8);
                double d11 = d7;
                d6 = d3 - entity.posX;
                d7 = d4 - entity.posY;

                if (d7 > -0.5D || d7 < 0.5D)
                {
                    d7 = 0.0D;
                }

                d8 = d5 - entity.posZ;
                d10 = d6 * d6 + d7 * d7 + d8 * d8;
                boolean flag1 = false;

                if (d10 > 0.0625D)
                {
                    flag1 = true;
                    LOGGER.warn("{} moved wrongly!", entity.getName());
                }

                entity.setPositionAndRotation(d3, d4, d5, f, f1);
                boolean flag2 = worldserver.getCollisionBoxes(entity, entity.getEntityBoundingBox().contract(0.0625D)).isEmpty();

                if (flag && (flag1 || !flag2))
                {
                    entity.setPositionAndRotation(d0, d1, d2, f, f1);
                    netManager.sendPacket(new SPacketMoveVehicle(entity));
                    return;
                }

                serverController.getPlayerList().serverUpdateMovingPlayer(playerEntity);
                playerEntity.addMovementStat(playerEntity.posX - d0, playerEntity.posY - d1, playerEntity.posZ - d2);
                vehicleFloating = d11 >= -0.03125D && !serverController.isFlightAllowed() && !worldserver.checkBlockCollision(entity.getEntityBoundingBox().expandXyz(0.0625D).addCoord(0.0D, -0.55D, 0.0D));
                lowestRiddenX1 = entity.posX;
                lowestRiddenY1 = entity.posY;
                lowestRiddenZ1 = entity.posZ;
            }
        }
    }

    public void processConfirmTeleport(CPacketConfirmTeleport packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());

        if (packetIn.getTeleportId() == teleportId)
        {
            playerEntity.setPositionAndRotation(targetPos.xCoord, targetPos.yCoord, targetPos.zCoord, playerEntity.rotationYaw, playerEntity.rotationPitch);

            if (playerEntity.isInvulnerableDimensionChange())
            {
                lastGoodX = targetPos.xCoord;
                lastGoodY = targetPos.yCoord;
                lastGoodZ = targetPos.zCoord;
                playerEntity.clearInvulnerableDimensionChange();
            }

            targetPos = null;
        }
    }

    public void func_191984_a(CPacketRecipeInfo p_191984_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_191984_1_, this, playerEntity.getServerWorld());

        if (p_191984_1_.func_194156_a() == CPacketRecipeInfo.Purpose.SHOWN)
        {
            playerEntity.func_192037_E().func_194074_f(p_191984_1_.func_193648_b());
        }
        else if (p_191984_1_.func_194156_a() == CPacketRecipeInfo.Purpose.SETTINGS)
        {
            playerEntity.func_192037_E().func_192813_a(p_191984_1_.func_192624_c());
            playerEntity.func_192037_E().func_192810_b(p_191984_1_.func_192625_d());
        }
    }

    public void func_194027_a(CPacketSeenAdvancements p_194027_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_194027_1_, this, playerEntity.getServerWorld());

        if (p_194027_1_.func_194162_b() == CPacketSeenAdvancements.Action.OPENED_TAB)
        {
            Namespaced resourcelocation = p_194027_1_.func_194165_c();
            Advancement advancement = serverController.func_191949_aK().func_192778_a(resourcelocation);

            if (advancement != null)
            {
                playerEntity.func_192039_O().func_194220_a(advancement);
            }
        }
    }

    /**
     * Processes clients perspective on player positioning and/or orientation
     */
    public void processPlayer(CPacketPlayer packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());

        if (isMovePlayerPacketInvalid(packetIn))
        {
            func_194028_b(new TranslatableComponent("multiplayer.disconnect.invalid_player_movement"));
        }
        else
        {
            WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);

            if (!playerEntity.playerConqueredTheEnd)
            {
                if (networkTickCount == 0)
                {
                    captureCurrentPosition();
                }

                if (targetPos != null)
                {
                    if (networkTickCount - lastPositionUpdate > 20)
                    {
                        lastPositionUpdate = networkTickCount;
                        setPlayerLocation(targetPos.xCoord, targetPos.yCoord, targetPos.zCoord, playerEntity.rotationYaw, playerEntity.rotationPitch);
                    }
                }
                else
                {
                    lastPositionUpdate = networkTickCount;

                    if (playerEntity.isRiding())
                    {
                        playerEntity.setPositionAndRotation(playerEntity.posX, playerEntity.posY, playerEntity.posZ, packetIn.getYaw(playerEntity.rotationYaw), packetIn.getPitch(playerEntity.rotationPitch));
                        serverController.getPlayerList().serverUpdateMovingPlayer(playerEntity);
                    }
                    else
                    {
                        double d0 = playerEntity.posX;
                        double d1 = playerEntity.posY;
                        double d2 = playerEntity.posZ;
                        double d3 = playerEntity.posY;
                        double d4 = packetIn.getX(playerEntity.posX);
                        double d5 = packetIn.getY(playerEntity.posY);
                        double d6 = packetIn.getZ(playerEntity.posZ);
                        float f = packetIn.getYaw(playerEntity.rotationYaw);
                        float f1 = packetIn.getPitch(playerEntity.rotationPitch);
                        double d7 = d4 - firstGoodX;
                        double d8 = d5 - firstGoodY;
                        double d9 = d6 - firstGoodZ;
                        double d10 = playerEntity.motionX * playerEntity.motionX + playerEntity.motionY * playerEntity.motionY + playerEntity.motionZ * playerEntity.motionZ;
                        double d11 = d7 * d7 + d8 * d8 + d9 * d9;

                        if (playerEntity.isPlayerSleeping())
                        {
                            if (d11 > 1.0D)
                            {
                                setPlayerLocation(playerEntity.posX, playerEntity.posY, playerEntity.posZ, packetIn.getYaw(playerEntity.rotationYaw), packetIn.getPitch(playerEntity.rotationPitch));
                            }
                        }
                        else
                        {
                            ++movePacketCounter;
                            int i = movePacketCounter - lastMovePacketCounter;

                            if (i > 5)
                            {
                                LOGGER.debug("{} is sending move packets too frequently ({} packets since last tick)", playerEntity.getName(), Integer.valueOf(i));
                                i = 1;
                            }

                            if (!playerEntity.isInvulnerableDimensionChange() && (!playerEntity.getServerWorld().getGameRules().getBoolean("disableElytraMovementCheck") || !playerEntity.isElytraFlying()))
                            {
                                float f2 = playerEntity.isElytraFlying() ? 300.0F : 100.0F;

                                if (d11 - d10 > (double)(f2 * (float)i) && (!serverController.isSinglePlayer() || !serverController.getServerOwner().equals(playerEntity.getName())))
                                {
                                    LOGGER.warn("{} moved too quickly! {},{},{}", playerEntity.getName(), Double.valueOf(d7), Double.valueOf(d8), Double.valueOf(d9));
                                    setPlayerLocation(playerEntity.posX, playerEntity.posY, playerEntity.posZ, playerEntity.rotationYaw, playerEntity.rotationPitch);
                                    return;
                                }
                            }

                            boolean flag2 = worldserver.getCollisionBoxes(playerEntity, playerEntity.getEntityBoundingBox().contract(0.0625D)).isEmpty();
                            d7 = d4 - lastGoodX;
                            d8 = d5 - lastGoodY;
                            d9 = d6 - lastGoodZ;

                            if (playerEntity.onGround && !packetIn.isOnGround() && d8 > 0.0D)
                            {
                                playerEntity.jump();
                            }

                            playerEntity.moveEntity(MoverType.PLAYER, d7, d8, d9);
                            playerEntity.onGround = packetIn.isOnGround();
                            double d12 = d8;
                            d7 = d4 - playerEntity.posX;
                            d8 = d5 - playerEntity.posY;

                            if (d8 > -0.5D || d8 < 0.5D)
                            {
                                d8 = 0.0D;
                            }

                            d9 = d6 - playerEntity.posZ;
                            d11 = d7 * d7 + d8 * d8 + d9 * d9;
                            boolean flag = false;

                            if (!playerEntity.isInvulnerableDimensionChange() && d11 > 0.0625D && !playerEntity.isPlayerSleeping() && !playerEntity.interactionManager.isCreative() && playerEntity.interactionManager.getGameType() != GameType.SPECTATOR)
                            {
                                flag = true;
                                LOGGER.warn("{} moved wrongly!", playerEntity.getName());
                            }

                            playerEntity.setPositionAndRotation(d4, d5, d6, f, f1);
                            playerEntity.addMovementStat(playerEntity.posX - d0, playerEntity.posY - d1, playerEntity.posZ - d2);

                            if (!playerEntity.noClip && !playerEntity.isPlayerSleeping())
                            {
                                boolean flag1 = worldserver.getCollisionBoxes(playerEntity, playerEntity.getEntityBoundingBox().contract(0.0625D)).isEmpty();

                                if (flag2 && (flag || !flag1))
                                {
                                    setPlayerLocation(d0, d1, d2, f, f1);
                                    return;
                                }
                            }

                            floating = d12 >= -0.03125D;
                            floating &= !serverController.isFlightAllowed() && !playerEntity.capabilities.allowFlying;
                            floating &= !playerEntity.isPotionActive(MobEffects.LEVITATION) && !playerEntity.isElytraFlying() && !worldserver.checkBlockCollision(playerEntity.getEntityBoundingBox().expandXyz(0.0625D).addCoord(0.0D, -0.55D, 0.0D));
                            playerEntity.onGround = packetIn.isOnGround();
                            serverController.getPlayerList().serverUpdateMovingPlayer(playerEntity);
                            playerEntity.handleFalling(playerEntity.posY - d3, packetIn.isOnGround());
                            lastGoodX = playerEntity.posX;
                            lastGoodY = playerEntity.posY;
                            lastGoodZ = playerEntity.posZ;
                        }
                    }
                }
            }
        }
    }

    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch)
    {
        setPlayerLocation(x, y, z, yaw, pitch, Collections.emptySet());
    }

    public void setPlayerLocation(double x, double y, double z, float yaw, float pitch, Set<SPacketPlayerPosLook.EnumFlags> relativeSet)
    {
        double d0 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X) ? playerEntity.posX : 0.0D;
        double d1 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y) ? playerEntity.posY : 0.0D;
        double d2 = relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Z) ? playerEntity.posZ : 0.0D;
        targetPos = new Vec3d(x + d0, y + d1, z + d2);
        float f = yaw;
        float f1 = pitch;

        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.Y_ROT))
        {
            f = yaw + playerEntity.rotationYaw;
        }

        if (relativeSet.contains(SPacketPlayerPosLook.EnumFlags.X_ROT))
        {
            f1 = pitch + playerEntity.rotationPitch;
        }

        if (++teleportId == Integer.MAX_VALUE)
        {
            teleportId = 0;
        }

        lastPositionUpdate = networkTickCount;
        playerEntity.setPositionAndRotation(targetPos.xCoord, targetPos.yCoord, targetPos.zCoord, f, f1);
        playerEntity.connection.sendPacket(new SPacketPlayerPosLook(x, y, z, yaw, pitch, relativeSet, teleportId));
    }

    /**
     * Processes the player initiating/stopping digging on a particular spot, as well as a player dropping items?. (0:
     * initiated, 1: reinitiated, 2? , 3-4 drop item (respectively without or with player control), 5: stopped; x,y,z,
     * side clicked on;)
     */
    public void processPlayerDigging(CPacketPlayerDigging packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);
        BlockPos blockpos = packetIn.getPosition();
        playerEntity.markPlayerActive();

        switch (packetIn.getAction())
        {
            case SWAP_HELD_ITEMS:
                if (!playerEntity.isSpectator())
                {
                    ItemStack itemstack = playerEntity.getHeldItem(EnumHand.OFF_HAND);
                    playerEntity.setHeldItem(EnumHand.OFF_HAND, playerEntity.getHeldItem(EnumHand.MAIN_HAND));
                    playerEntity.setHeldItem(EnumHand.MAIN_HAND, itemstack);
                }

                return;

            case DROP_ITEM:
                if (!playerEntity.isSpectator())
                {
                    playerEntity.dropItem(false);
                }

                return;

            case DROP_ALL_ITEMS:
                if (!playerEntity.isSpectator())
                {
                    playerEntity.dropItem(true);
                }

                return;

            case RELEASE_USE_ITEM:
                playerEntity.stopActiveHand();
                return;

            case START_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
            case STOP_DESTROY_BLOCK:
                double d0 = playerEntity.posX - ((double)blockpos.getX() + 0.5D);
                double d1 = playerEntity.posY - ((double)blockpos.getY() + 0.5D) + 1.5D;
                double d2 = playerEntity.posZ - ((double)blockpos.getZ() + 0.5D);
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > 36.0D)
                {
                    return;
                }
                else if (blockpos.getY() >= serverController.getBuildLimit())
                {
                    return;
                }
                else
                {
                    if (packetIn.getAction() == CPacketPlayerDigging.Action.START_DESTROY_BLOCK)
                    {
                        if (!serverController.isBlockProtected(worldserver, blockpos, playerEntity) && worldserver.getWorldBorder().contains(blockpos))
                        {
                            playerEntity.interactionManager.onBlockClicked(blockpos, packetIn.getFacing());
                        }
                        else
                        {
                            playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                        }
                    }
                    else
                    {
                        if (packetIn.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK)
                        {
                            playerEntity.interactionManager.blockRemoving(blockpos);
                        }
                        else if (packetIn.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK)
                        {
                            playerEntity.interactionManager.cancelDestroyingBlock();
                        }

                        if (worldserver.getBlockState(blockpos).getMaterial() != Material.AIR)
                        {
                            playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
                        }
                    }

                    return;
                }

            default:
                throw new IllegalArgumentException("Invalid player action");
        }
    }

    public void processRightClickBlock(CPacketPlayerTryUseItemOnBlock packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);
        EnumHand enumhand = packetIn.getHand();
        ItemStack itemstack = playerEntity.getHeldItem(enumhand);
        BlockPos blockpos = packetIn.getPos();
        EnumFacing enumfacing = packetIn.getDirection();
        playerEntity.markPlayerActive();

        if (blockpos.getY() < serverController.getBuildLimit() - 1 || enumfacing != EnumFacing.UP && blockpos.getY() < serverController.getBuildLimit())
        {
            if (targetPos == null && playerEntity.getDistanceSq((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D) < 64.0D && !serverController.isBlockProtected(worldserver, blockpos, playerEntity) && worldserver.getWorldBorder().contains(blockpos))
            {
                playerEntity.interactionManager.processRightClickBlock(playerEntity, worldserver, itemstack, enumhand, blockpos, enumfacing, packetIn.getFacingX(), packetIn.getFacingY(), packetIn.getFacingZ());
            }
        }
        else
        {
            TranslatableComponent textcomponenttranslation = new TranslatableComponent("build.tooHigh", serverController.getBuildLimit());
            textcomponenttranslation.getStyle().setColor(Formatting.RED);
            playerEntity.connection.sendPacket(new SPacketChat(textcomponenttranslation, ChatType.GAME_INFO));
        }

        playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos));
        playerEntity.connection.sendPacket(new SPacketBlockChange(worldserver, blockpos.offset(enumfacing)));
    }

    /**
     * Processes block placement and block activation (anvil, furnace, etc.)
     */
    public void processPlayerBlockPlacement(CPacketPlayerTryUseItem packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);
        EnumHand enumhand = packetIn.getHand();
        ItemStack itemstack = playerEntity.getHeldItem(enumhand);
        playerEntity.markPlayerActive();

        if (!itemstack.isEmpty())
        {
            playerEntity.interactionManager.processRightClick(playerEntity, worldserver, itemstack, enumhand);
        }
    }

    public void handleSpectate(CPacketSpectate packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());

        if (playerEntity.isSpectator())
        {
            Entity entity = null;

            for (WorldServer worldserver : serverController.worldServers)
            {
                if (worldserver != null)
                {
                    entity = packetIn.getEntity(worldserver);

                    if (entity != null)
                    {
                        break;
                    }
                }
            }

            if (entity != null)
            {
                playerEntity.setSpectatingEntity(playerEntity);
                playerEntity.dismountRidingEntity();

                if (entity.world == playerEntity.world)
                {
                    playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                }
                else
                {
                    WorldServer worldserver1 = playerEntity.getServerWorld();
                    WorldServer worldserver2 = (WorldServer)entity.world;
                    playerEntity.dimension = entity.dimension;
                    sendPacket(new SPacketRespawn(playerEntity.dimension, worldserver1.getDifficulty(), worldserver1.getWorldInfo().getTerrainType(), playerEntity.interactionManager.getGameType()));
                    serverController.getPlayerList().updatePermissionLevel(playerEntity);
                    worldserver1.removeEntityDangerously(playerEntity);
                    playerEntity.isDead = false;
                    playerEntity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);

                    if (playerEntity.isEntityAlive())
                    {
                        worldserver1.updateEntityWithOptionalForce(playerEntity, false);
                        worldserver2.spawnEntityInWorld(playerEntity);
                        worldserver2.updateEntityWithOptionalForce(playerEntity, false);
                    }

                    playerEntity.setWorld(worldserver2);
                    serverController.getPlayerList().preparePlayer(playerEntity, worldserver1);
                    playerEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
                    playerEntity.interactionManager.setWorld(worldserver2);
                    serverController.getPlayerList().updateTimeAndWeatherForPlayer(playerEntity, worldserver2);
                    serverController.getPlayerList().syncPlayerInventory(playerEntity);
                }
            }
        }
    }

    public void handleResourcePackStatus(CPacketResourcePackStatus packetIn)
    {
    }

    public void processSteerBoat(CPacketSteerBoat packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        Entity entity = playerEntity.getRidingEntity();

        if (entity instanceof EntityBoat)
        {
            ((EntityBoat)entity).setPaddleState(packetIn.getLeft(), packetIn.getRight());
        }
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(Component reason)
    {
        LOGGER.info("{} lost connection: {}", playerEntity.getName(), reason.asString());
        serverController.refreshStatusNextTick();
        TranslatableComponent textcomponenttranslation = new TranslatableComponent("multiplayer.player.left", playerEntity.getDisplayName());
        textcomponenttranslation.getStyle().setColor(Formatting.YELLOW);
        serverController.getPlayerList().sendChatMsg(textcomponenttranslation);
        playerEntity.mountEntityAndWakeUp();
        serverController.getPlayerList().playerLoggedOut(playerEntity);

        if (serverController.isSinglePlayer() && playerEntity.getName().equals(serverController.getServerOwner()))
        {
            LOGGER.info("Stopping singleplayer server as player logged out");
            serverController.initiateShutdown();
        }
    }

    public void sendPacket(Packet<?> packetIn)
    {
        if (packetIn instanceof SPacketChat)
        {
            SPacketChat spacketchat = (SPacketChat)packetIn;
            EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility = playerEntity.getChatVisibility();

            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.HIDDEN && spacketchat.getChatType() != ChatType.GAME_INFO)
            {
                return;
            }

            if (entityplayer$enumchatvisibility == EntityPlayer.EnumChatVisibility.SYSTEM && !spacketchat.isSystem())
            {
                return;
            }
        }

        try
        {
            netManager.sendPacket(packetIn);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Sending packet");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Packet being sent");
            crashreportcategory.setDetail("Packet class", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return packetIn.getClass().getCanonicalName();
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Updates which quickbar slot is selected
     */
    public void processHeldItemChange(CPacketHeldItemChange packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());

        if (packetIn.getSlotId() >= 0 && packetIn.getSlotId() < InventoryPlayer.getHotbarSize())
        {
            playerEntity.inventory.currentItem = packetIn.getSlotId();
            playerEntity.markPlayerActive();
        }
        else
        {
            LOGGER.warn("{} tried to set an invalid carried item", playerEntity.getName());
        }
    }

    /**
     * Process chat messages (broadcast back to clients) and commands (executes)
     */
    public void processChatMessage(CPacketChatMessage packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());

        if (playerEntity.getChatVisibility() == EntityPlayer.EnumChatVisibility.HIDDEN)
        {
            TranslatableComponent textcomponenttranslation = new TranslatableComponent("chat.cannotSend");
            textcomponenttranslation.getStyle().setColor(Formatting.RED);
            sendPacket(new SPacketChat(textcomponenttranslation));
        }
        else
        {
            playerEntity.markPlayerActive();
            String s = packetIn.getMessage();
            s = s.trim();

            for (int i = 0; i < s.length(); ++i)
            {
                if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i)))
                {
                    func_194028_b(new TranslatableComponent("multiplayer.disconnect.illegal_characters"));
                    return;
                }
            }

            if (s.startsWith("/"))
            {
                handleSlashCommand(s);
            }
            else
            {
                Component itextcomponent = new TranslatableComponent("chat.type.text", playerEntity.getDisplayName(), s);
                serverController.getPlayerList().sendChatMsgImpl(itextcomponent, false);
            }

            chatSpamThresholdCount += 20;

            if (chatSpamThresholdCount > 200 && !serverController.getPlayerList().canSendCommands(playerEntity.getGameProfile()))
            {
                func_194028_b(new TranslatableComponent("disconnect.spam"));
            }
        }
    }

    /**
     * Handle commands that start with a /
     */
    private void handleSlashCommand(String command)
    {
        serverController.getCommandManager().executeCommand(playerEntity, command);
    }

    public void handleAnimation(CPacketAnimation packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.markPlayerActive();
        playerEntity.swingArm(packetIn.getHand());
    }

    /**
     * Processes a range of action-types: sneaking, sprinting, waking from sleep, opening the inventory or setting jump
     * height of the horse the player is riding
     */
    public void processEntityAction(CPacketEntityAction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.markPlayerActive();

        switch (packetIn.getAction())
        {
            case START_SNEAKING:
                playerEntity.setSneaking(true);
                break;

            case STOP_SNEAKING:
                playerEntity.setSneaking(false);
                break;

            case START_SPRINTING:
                playerEntity.setSprinting(true);
                break;

            case STOP_SPRINTING:
                playerEntity.setSprinting(false);
                break;

            case STOP_SLEEPING:
                if (playerEntity.isPlayerSleeping())
                {
                    playerEntity.wakeUpPlayer(false, true, true);
                    targetPos = new Vec3d(playerEntity.posX, playerEntity.posY, playerEntity.posZ);
                }

                break;

            case START_RIDING_JUMP:
                if (playerEntity.getRidingEntity() instanceof IJumpingMount)
                {
                    IJumpingMount ijumpingmount1 = (IJumpingMount) playerEntity.getRidingEntity();
                    int i = packetIn.getAuxData();

                    if (ijumpingmount1.canJump() && i > 0)
                    {
                        ijumpingmount1.handleStartJump(i);
                    }
                }

                break;

            case STOP_RIDING_JUMP:
                if (playerEntity.getRidingEntity() instanceof IJumpingMount)
                {
                    IJumpingMount ijumpingmount = (IJumpingMount) playerEntity.getRidingEntity();
                    ijumpingmount.handleStopJump();
                }

                break;

            case OPEN_INVENTORY:
                if (playerEntity.getRidingEntity() instanceof AbstractHorse)
                {
                    ((AbstractHorse) playerEntity.getRidingEntity()).openGUI(playerEntity);
                }

                break;

            case START_FALL_FLYING:
                if (!playerEntity.onGround && playerEntity.motionY < 0.0D && !playerEntity.isElytraFlying() && !playerEntity.isInWater())
                {
                    ItemStack itemstack = playerEntity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                    if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isBroken(itemstack))
                    {
                        playerEntity.setElytraFlying();
                    }
                }
                else
                {
                    playerEntity.clearElytraFlying();
                }

                break;

            default:
                throw new IllegalArgumentException("Invalid client command!");
        }
    }

    /**
     * Processes interactions ((un)leashing, opening command block GUI) and attacks on an entity with players currently
     * equipped item
     */
    public void processUseEntity(CPacketUseEntity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);
        Entity entity = packetIn.getEntityFromWorld(worldserver);
        playerEntity.markPlayerActive();

        if (entity != null)
        {
            boolean flag = playerEntity.canEntityBeSeen(entity);
            double d0 = 36.0D;

            if (!flag)
            {
                d0 = 9.0D;
            }

            if (playerEntity.getDistanceSqToEntity(entity) < d0)
            {
                if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT)
                {
                    EnumHand enumhand = packetIn.getHand();
                    playerEntity.func_190775_a(entity, enumhand);
                }
                else if (packetIn.getAction() == CPacketUseEntity.Action.INTERACT_AT)
                {
                    EnumHand enumhand1 = packetIn.getHand();
                    entity.applyPlayerInteraction(playerEntity, packetIn.getHitVec(), enumhand1);
                }
                else if (packetIn.getAction() == CPacketUseEntity.Action.ATTACK)
                {
                    if (entity instanceof EntityItem || entity instanceof EntityXPOrb || entity instanceof EntityArrow || entity == playerEntity)
                    {
                        func_194028_b(new TranslatableComponent("multiplayer.disconnect.invalid_entity_attacked"));
                        serverController.logWarning("Player " + playerEntity.getName() + " tried to attack an invalid entity");
                        return;
                    }

                    playerEntity.attackTargetEntityWithCurrentItem(entity);
                }
            }
        }
    }

    /**
     * Processes the client status updates: respawn attempt from player, opening statistics or achievements, or
     * acquiring 'open inventory' achievement
     */
    public void processClientStatus(CPacketClientStatus packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.markPlayerActive();
        CPacketClientStatus.State cpacketclientstatus$state = packetIn.getStatus();

        switch (cpacketclientstatus$state)
        {
            case PERFORM_RESPAWN:
                if (playerEntity.playerConqueredTheEnd)
                {
                    playerEntity.playerConqueredTheEnd = false;
                    playerEntity = serverController.getPlayerList().recreatePlayerEntity(playerEntity, 0, true);
                    CriteriaTriggers.field_193134_u.func_193143_a(playerEntity, DimensionType.THE_END, DimensionType.OVERWORLD);
                }
                else
                {
                    if (playerEntity.getHealth() > 0.0F)
                    {
                        return;
                    }

                    playerEntity = serverController.getPlayerList().recreatePlayerEntity(playerEntity, 0, false);

                    if (serverController.isHardcore())
                    {
                        playerEntity.setGameType(GameType.SPECTATOR);
                        playerEntity.getServerWorld().getGameRules().setOrCreateGameRule("spectatorsGenerateChunks", "false");
                    }
                }

                break;

            case REQUEST_STATS:
                playerEntity.getStatFile().sendStats(playerEntity);
        }
    }

    /**
     * Processes the client closing windows (container)
     */
    public void processCloseWindow(CPacketCloseWindow packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.closeContainer();
    }

    /**
     * Executes a container/inventory slot manipulation as indicated by the packet. Sends the serverside result if they
     * didn't match the indicated result and prevents further manipulation by the player until he confirms that it has
     * the same open container/inventory
     */
    public void processClickWindow(CPacketClickWindow packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.markPlayerActive();

        if (playerEntity.openContainer.windowId == packetIn.getWindowId() && playerEntity.openContainer.getCanCraft(playerEntity))
        {
            if (playerEntity.isSpectator())
            {
                NonNullList<ItemStack> nonnulllist = NonNullList.func_191196_a();

                for (int i = 0; i < playerEntity.openContainer.inventorySlots.size(); ++i)
                {
                    nonnulllist.add(playerEntity.openContainer.inventorySlots.get(i).getStack());
                }

                playerEntity.updateCraftingInventory(playerEntity.openContainer, nonnulllist);
            }
            else
            {
                ItemStack itemstack2 = playerEntity.openContainer.slotClick(packetIn.getSlotId(), packetIn.getUsedButton(), packetIn.getClickType(), playerEntity);

                if (ItemStack.areItemStacksEqual(packetIn.getClickedItem(), itemstack2))
                {
                    playerEntity.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
                    playerEntity.isChangingQuantityOnly = true;
                    playerEntity.openContainer.detectAndSendChanges();
                    playerEntity.updateHeldItem();
                    playerEntity.isChangingQuantityOnly = false;
                }
                else
                {
                    pendingTransactions.addKey(playerEntity.openContainer.windowId, Short.valueOf(packetIn.getActionNumber()));
                    playerEntity.connection.sendPacket(new SPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), false));
                    playerEntity.openContainer.setCanCraft(playerEntity, false);
                    NonNullList<ItemStack> nonnulllist1 = NonNullList.func_191196_a();

                    for (int j = 0; j < playerEntity.openContainer.inventorySlots.size(); ++j)
                    {
                        ItemStack itemstack = playerEntity.openContainer.inventorySlots.get(j).getStack();
                        ItemStack itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
                        nonnulllist1.add(itemstack1);
                    }

                    playerEntity.updateCraftingInventory(playerEntity.openContainer, nonnulllist1);
                }
            }
        }
    }

    public void func_194308_a(CPacketPlaceRecipe p_194308_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_194308_1_, this, playerEntity.getServerWorld());
        playerEntity.markPlayerActive();

        if (!playerEntity.isSpectator() && playerEntity.openContainer.windowId == p_194308_1_.func_194318_a() && playerEntity.openContainer.getCanCraft(playerEntity))
        {
            field_194309_H.func_194327_a(playerEntity, p_194308_1_.func_194317_b(), p_194308_1_.func_194319_c());
        }
    }

    /**
     * Enchants the item identified by the packet given some convoluted conditions (matching window, which
     * should/shouldn't be in use?)
     */
    public void processEnchantItem(CPacketEnchantItem packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.markPlayerActive();

        if (playerEntity.openContainer.windowId == packetIn.getWindowId() && playerEntity.openContainer.getCanCraft(playerEntity) && !playerEntity.isSpectator())
        {
            playerEntity.openContainer.enchantItem(playerEntity, packetIn.getButton());
            playerEntity.openContainer.detectAndSendChanges();
        }
    }

    /**
     * Update the server with an ItemStack in a slot.
     */
    public void processCreativeInventoryAction(CPacketCreativeInventoryAction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());

        if (playerEntity.interactionManager.isCreative())
        {
            boolean flag = packetIn.getSlotId() < 0;
            ItemStack itemstack = packetIn.getStack();

            if (!itemstack.isEmpty() && itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("BlockEntityTag", 10))
            {
                NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("BlockEntityTag");

                if (nbttagcompound.hasKey("x") && nbttagcompound.hasKey("y") && nbttagcompound.hasKey("z"))
                {
                    BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
                    TileEntity tileentity = playerEntity.world.getTileEntity(blockpos);

                    if (tileentity != null)
                    {
                        NBTTagCompound nbttagcompound1 = tileentity.writeToNBT(new NBTTagCompound());
                        nbttagcompound1.removeTag("x");
                        nbttagcompound1.removeTag("y");
                        nbttagcompound1.removeTag("z");
                        itemstack.setTagInfo("BlockEntityTag", nbttagcompound1);
                    }
                }
            }

            boolean flag1 = packetIn.getSlotId() >= 1 && packetIn.getSlotId() <= 45;
            boolean flag2 = itemstack.isEmpty() || itemstack.getMetadata() >= 0 && itemstack.getCount() <= 64 && !itemstack.isEmpty();

            if (flag1 && flag2)
            {
                if (itemstack.isEmpty())
                {
                    playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), ItemStack.EMPTY);
                }
                else
                {
                    playerEntity.inventoryContainer.putStackInSlot(packetIn.getSlotId(), itemstack);
                }

                playerEntity.inventoryContainer.setCanCraft(playerEntity, true);
            }
            else if (flag && flag2 && itemDropThreshold < 200)
            {
                itemDropThreshold += 20;
                EntityItem entityitem = playerEntity.dropItem(itemstack, true);

                if (entityitem != null)
                {
                    entityitem.setAgeToCreativeDespawnTime();
                }
            }
        }
    }

    /**
     * Received in response to the server requesting to confirm that the client-side open container matches the servers'
     * after a mismatched container-slot manipulation. It will unlock the player's ability to manipulate the container
     * contents
     */
    public void processConfirmTransaction(CPacketConfirmTransaction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        Short oshort = pendingTransactions.lookup(playerEntity.openContainer.windowId);

        if (oshort != null && packetIn.getUid() == oshort.shortValue() && playerEntity.openContainer.windowId == packetIn.getWindowId() && !playerEntity.openContainer.getCanCraft(playerEntity) && !playerEntity.isSpectator())
        {
            playerEntity.openContainer.setCanCraft(playerEntity, true);
        }
    }

    public void processUpdateSign(CPacketUpdateSign packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.markPlayerActive();
        WorldServer worldserver = serverController.worldServerForDimension(playerEntity.dimension);
        BlockPos blockpos = packetIn.getPosition();

        if (worldserver.isBlockLoaded(blockpos))
        {
            IBlockState iblockstate = worldserver.getBlockState(blockpos);
            TileEntity tileentity = worldserver.getTileEntity(blockpos);

            if (!(tileentity instanceof TileEntitySign))
            {
                return;
            }

            TileEntitySign tileentitysign = (TileEntitySign)tileentity;

            if (!tileentitysign.getIsEditable() || tileentitysign.getPlayer() != playerEntity)
            {
                serverController.logWarning("Player " + playerEntity.getName() + " just tried to change non-editable sign");
                return;
            }

            String[] astring = packetIn.getLines();

            for (int i = 0; i < astring.length; ++i)
            {
                tileentitysign.signText[i] = new TextComponent(Formatting.strip(astring[i]));
            }

            tileentitysign.markDirty();
            worldserver.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
        }
    }

    /**
     * Updates a players' ping statistics
     */
    public void processKeepAlive(CPacketKeepAlive packetIn)
    {
        if (field_194403_g && packetIn.getKey() == field_194404_h)
        {
            int i = (int)(currentTimeMillis() - field_194402_f);
            playerEntity.ping = (playerEntity.ping * 3 + i) / 4;
            field_194403_g = false;
        }
        else if (!playerEntity.getName().equals(serverController.getServerOwner()))
        {
            func_194028_b(new TranslatableComponent("disconnect.timeout"));
        }
    }

    private long currentTimeMillis()
    {
        return System.nanoTime() / 1000000L;
    }

    /**
     * Processes a player starting/stopping flying
     */
    public void processPlayerAbilities(CPacketPlayerAbilities packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.capabilities.isFlying = packetIn.isFlying() && playerEntity.capabilities.allowFlying;
    }

    /**
     * Retrieves possible tab completions for the requested command string and sends them to the client
     */
    public void processTabComplete(CPacketTabComplete packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        List<String> list = Lists.newArrayList();

        for (String s : serverController.getTabCompletions(playerEntity, packetIn.getMessage(), packetIn.getTargetBlock(), packetIn.hasTargetBlock()))
        {
            list.add(s);
        }

        playerEntity.connection.sendPacket(new SPacketTabComplete(list.toArray(new String[list.size()])));
    }

    /**
     * Updates serverside copy of client settings: language, render distance, chat visibility, chat colours, difficulty,
     * and whether to show the cape
     */
    public void processClientSettings(CPacketClientSettings packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        playerEntity.handleClientSettings(packetIn);
    }

    /**
     * Synchronizes serverside and clientside book contents and signing
     */
    public void processCustomPayload(CPacketCustomPayload packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, playerEntity.getServerWorld());
        String s = packetIn.getChannelName();

        if ("MC|BEdit".equals(s))
        {
            PacketBuffer packetbuffer = packetIn.getBufferData();

            try
            {
                ItemStack itemstack = packetbuffer.readItemStackFromBuffer();

                if (itemstack.isEmpty())
                {
                    return;
                }

                if (!ItemWritableBook.isNBTValid(itemstack.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack itemstack1 = playerEntity.getHeldItemMainhand();

                if (itemstack1.isEmpty())
                {
                    return;
                }

                if (itemstack.getItem() == Items.WRITABLE_BOOK && itemstack.getItem() == itemstack1.getItem())
                {
                    itemstack1.setTagInfo("pages", itemstack.getTagCompound().getTagList("pages", 8));
                }
            }
            catch (Exception exception6)
            {
                LOGGER.error("Couldn't handle book info", exception6);
            }
        }
        else if ("MC|BSign".equals(s))
        {
            PacketBuffer packetbuffer1 = packetIn.getBufferData();

            try
            {
                ItemStack itemstack3 = packetbuffer1.readItemStackFromBuffer();

                if (itemstack3.isEmpty())
                {
                    return;
                }

                if (!ItemWrittenBook.validBookTagContents(itemstack3.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack itemstack4 = playerEntity.getHeldItemMainhand();

                if (itemstack4.isEmpty())
                {
                    return;
                }

                if (itemstack3.getItem() == Items.WRITABLE_BOOK && itemstack4.getItem() == Items.WRITABLE_BOOK)
                {
                    ItemStack itemstack2 = new ItemStack(Items.WRITTEN_BOOK);
                    itemstack2.setTagInfo("author", new NBTTagString(playerEntity.getName()));
                    itemstack2.setTagInfo("title", new NBTTagString(itemstack3.getTagCompound().getString("title")));
                    NBTTagList nbttaglist = itemstack3.getTagCompound().getTagList("pages", 8);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        String s1 = nbttaglist.getStringTagAt(i);
                        Component itextcomponent = new TextComponent(s1);
                        s1 = Component.Serializer.componentToJson(itextcomponent);
                        nbttaglist.set(i, new NBTTagString(s1));
                    }

                    itemstack2.setTagInfo("pages", nbttaglist);
                    playerEntity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemstack2);
                }
            }
            catch (Exception exception7)
            {
                LOGGER.error("Couldn't sign book", exception7);
            }
        }
        else if ("MC|TrSel".equals(s))
        {
            try
            {
                int k = packetIn.getBufferData().readInt();
                Container container = playerEntity.openContainer;

                if (container instanceof ContainerMerchant)
                {
                    ((ContainerMerchant)container).setCurrentRecipeIndex(k);
                }
            }
            catch (Exception exception5)
            {
                LOGGER.error("Couldn't select trade", exception5);
            }
        }
        else if ("MC|AdvCmd".equals(s))
        {
            if (!serverController.isCommandBlockEnabled())
            {
                playerEntity.addChatMessage(new TranslatableComponent("advMode.notEnabled"));
                return;
            }

            if (!playerEntity.canUseCommandBlock())
            {
                playerEntity.addChatMessage(new TranslatableComponent("advMode.notAllowed"));
                return;
            }

            PacketBuffer packetbuffer2 = packetIn.getBufferData();

            try
            {
                int l = packetbuffer2.readByte();
                CommandBlockBaseLogic commandblockbaselogic1 = null;

                if (l == 0)
                {
                    TileEntity tileentity = playerEntity.world.getTileEntity(new BlockPos(packetbuffer2.readInt(), packetbuffer2.readInt(), packetbuffer2.readInt()));

                    if (tileentity instanceof TileEntityCommandBlock)
                    {
                        commandblockbaselogic1 = ((TileEntityCommandBlock)tileentity).getCommandBlockLogic();
                    }
                }
                else if (l == 1)
                {
                    Entity entity = playerEntity.world.getEntityByID(packetbuffer2.readInt());

                    if (entity instanceof EntityMinecartCommandBlock)
                    {
                        commandblockbaselogic1 = ((EntityMinecartCommandBlock)entity).getCommandBlockLogic();
                    }
                }

                String s6 = packetbuffer2.readStringFromBuffer(packetbuffer2.readableBytes());
                boolean flag2 = packetbuffer2.readBoolean();

                if (commandblockbaselogic1 != null)
                {
                    commandblockbaselogic1.setCommand(s6);
                    commandblockbaselogic1.setTrackOutput(flag2);

                    if (!flag2)
                    {
                        commandblockbaselogic1.setLastOutput(null);
                    }

                    commandblockbaselogic1.updateCommand();
                    playerEntity.addChatMessage(new TranslatableComponent("advMode.setCommand.success", s6));
                }
            }
            catch (Exception exception4)
            {
                LOGGER.error("Couldn't set command block", exception4);
            }
        }
        else if ("MC|AutoCmd".equals(s))
        {
            if (!serverController.isCommandBlockEnabled())
            {
                playerEntity.addChatMessage(new TranslatableComponent("advMode.notEnabled"));
                return;
            }

            if (!playerEntity.canUseCommandBlock())
            {
                playerEntity.addChatMessage(new TranslatableComponent("advMode.notAllowed"));
                return;
            }

            PacketBuffer packetbuffer3 = packetIn.getBufferData();

            try
            {
                CommandBlockBaseLogic commandblockbaselogic = null;
                TileEntityCommandBlock tileentitycommandblock = null;
                BlockPos blockpos1 = new BlockPos(packetbuffer3.readInt(), packetbuffer3.readInt(), packetbuffer3.readInt());
                TileEntity tileentity2 = playerEntity.world.getTileEntity(blockpos1);

                if (tileentity2 instanceof TileEntityCommandBlock)
                {
                    tileentitycommandblock = (TileEntityCommandBlock)tileentity2;
                    commandblockbaselogic = tileentitycommandblock.getCommandBlockLogic();
                }

                String s7 = packetbuffer3.readStringFromBuffer(packetbuffer3.readableBytes());
                boolean flag3 = packetbuffer3.readBoolean();
                TileEntityCommandBlock.Mode tileentitycommandblock$mode = TileEntityCommandBlock.Mode.valueOf(packetbuffer3.readStringFromBuffer(16));
                boolean flag = packetbuffer3.readBoolean();
                boolean flag1 = packetbuffer3.readBoolean();

                if (commandblockbaselogic != null)
                {
                    EnumFacing enumfacing = playerEntity.world.getBlockState(blockpos1).getValue(BlockCommandBlock.FACING);

                    switch (tileentitycommandblock$mode)
                    {
                        case SEQUENCE:
                            IBlockState iblockstate3 = Blocks.CHAIN_COMMAND_BLOCK.getDefaultState();
                            playerEntity.world.setBlockState(blockpos1, iblockstate3.withProperty(BlockCommandBlock.FACING, enumfacing).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                            break;

                        case AUTO:
                            IBlockState iblockstate2 = Blocks.REPEATING_COMMAND_BLOCK.getDefaultState();
                            playerEntity.world.setBlockState(blockpos1, iblockstate2.withProperty(BlockCommandBlock.FACING, enumfacing).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                            break;

                        case REDSTONE:
                            IBlockState iblockstate = Blocks.COMMAND_BLOCK.getDefaultState();
                            playerEntity.world.setBlockState(blockpos1, iblockstate.withProperty(BlockCommandBlock.FACING, enumfacing).withProperty(BlockCommandBlock.CONDITIONAL, Boolean.valueOf(flag)), 2);
                    }

                    tileentity2.validate();
                    playerEntity.world.setTileEntity(blockpos1, tileentity2);
                    commandblockbaselogic.setCommand(s7);
                    commandblockbaselogic.setTrackOutput(flag3);

                    if (!flag3)
                    {
                        commandblockbaselogic.setLastOutput(null);
                    }

                    tileentitycommandblock.setAuto(flag1);
                    commandblockbaselogic.updateCommand();

                    if (!net.minecraft.util.StringUtils.isNullOrEmpty(s7))
                    {
                        playerEntity.addChatMessage(new TranslatableComponent("advMode.setCommand.success", s7));
                    }
                }
            }
            catch (Exception exception3)
            {
                LOGGER.error("Couldn't set command block", exception3);
            }
        }
        else if ("MC|Beacon".equals(s))
        {
            if (playerEntity.openContainer instanceof ContainerBeacon)
            {
                try
                {
                    PacketBuffer packetbuffer4 = packetIn.getBufferData();
                    int i1 = packetbuffer4.readInt();
                    int k1 = packetbuffer4.readInt();
                    ContainerBeacon containerbeacon = (ContainerBeacon) playerEntity.openContainer;
                    Slot slot = containerbeacon.getSlot(0);

                    if (slot.getHasStack())
                    {
                        slot.decrStackSize(1);
                        IInventory iinventory = containerbeacon.getTileEntity();
                        iinventory.setField(1, i1);
                        iinventory.setField(2, k1);
                        iinventory.markDirty();
                    }
                }
                catch (Exception exception2)
                {
                    LOGGER.error("Couldn't set beacon", exception2);
                }
            }
        }
        else if ("MC|ItemName".equals(s))
        {
            if (playerEntity.openContainer instanceof ContainerRepair)
            {
                ContainerRepair containerrepair = (ContainerRepair) playerEntity.openContainer;

                if (packetIn.getBufferData() != null && packetIn.getBufferData().readableBytes() >= 1)
                {
                    String s5 = ChatAllowedCharacters.filterAllowedCharacters(packetIn.getBufferData().readStringFromBuffer(32767));

                    if (s5.length() <= 35)
                    {
                        containerrepair.updateItemName(s5);
                    }
                }
                else
                {
                    containerrepair.updateItemName("");
                }
            }
        }
        else if ("MC|Struct".equals(s))
        {
            if (!playerEntity.canUseCommandBlock())
            {
                return;
            }

            PacketBuffer packetbuffer5 = packetIn.getBufferData();

            try
            {
                BlockPos blockpos = new BlockPos(packetbuffer5.readInt(), packetbuffer5.readInt(), packetbuffer5.readInt());
                IBlockState iblockstate1 = playerEntity.world.getBlockState(blockpos);
                TileEntity tileentity1 = playerEntity.world.getTileEntity(blockpos);

                if (tileentity1 instanceof TileEntityStructure)
                {
                    TileEntityStructure tileentitystructure = (TileEntityStructure)tileentity1;
                    int l1 = packetbuffer5.readByte();
                    String s8 = packetbuffer5.readStringFromBuffer(32);
                    tileentitystructure.setMode(TileEntityStructure.Mode.valueOf(s8));
                    tileentitystructure.setName(packetbuffer5.readStringFromBuffer(64));
                    int i2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    int j2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    int k2 = MathHelper.clamp(packetbuffer5.readInt(), -32, 32);
                    tileentitystructure.setPosition(new BlockPos(i2, j2, k2));
                    int l2 = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    int i3 = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    int j = MathHelper.clamp(packetbuffer5.readInt(), 0, 32);
                    tileentitystructure.setSize(new BlockPos(l2, i3, j));
                    String s2 = packetbuffer5.readStringFromBuffer(32);
                    tileentitystructure.setMirror(Mirror.valueOf(s2));
                    String s3 = packetbuffer5.readStringFromBuffer(32);
                    tileentitystructure.setRotation(Rotation.valueOf(s3));
                    tileentitystructure.setMetadata(packetbuffer5.readStringFromBuffer(128));
                    tileentitystructure.setIgnoresEntities(packetbuffer5.readBoolean());
                    tileentitystructure.setShowAir(packetbuffer5.readBoolean());
                    tileentitystructure.setShowBoundingBox(packetbuffer5.readBoolean());
                    tileentitystructure.setIntegrity(MathHelper.clamp(packetbuffer5.readFloat(), 0.0F, 1.0F));
                    tileentitystructure.setSeed(packetbuffer5.readVarLong());
                    String s4 = tileentitystructure.getName();

                    if (l1 == 2)
                    {
                        if (tileentitystructure.save())
                        {
                            playerEntity.addChatComponentMessage(new TranslatableComponent("structure_block.save_success", s4), false);
                        }
                        else
                        {
                            playerEntity.addChatComponentMessage(new TranslatableComponent("structure_block.save_failure", s4), false);
                        }
                    }
                    else if (l1 == 3)
                    {
                        if (!tileentitystructure.isStructureLoadable())
                        {
                            playerEntity.addChatComponentMessage(new TranslatableComponent("structure_block.load_not_found", s4), false);
                        }
                        else if (tileentitystructure.load())
                        {
                            playerEntity.addChatComponentMessage(new TranslatableComponent("structure_block.load_success", s4), false);
                        }
                        else
                        {
                            playerEntity.addChatComponentMessage(new TranslatableComponent("structure_block.load_prepare", s4), false);
                        }
                    }
                    else if (l1 == 4)
                    {
                        if (tileentitystructure.detectSize())
                        {
                            playerEntity.addChatComponentMessage(new TranslatableComponent("structure_block.size_success", s4), false);
                        }
                        else
                        {
                            playerEntity.addChatComponentMessage(new TranslatableComponent("structure_block.size_failure"), false);
                        }
                    }

                    tileentitystructure.markDirty();
                    playerEntity.world.notifyBlockUpdate(blockpos, iblockstate1, iblockstate1, 3);
                }
            }
            catch (Exception exception1)
            {
                LOGGER.error("Couldn't set structure block", exception1);
            }
        }
        else if ("MC|PickItem".equals(s))
        {
            PacketBuffer packetbuffer6 = packetIn.getBufferData();

            try
            {
                int j1 = packetbuffer6.readVarIntFromBuffer();
                playerEntity.inventory.pickItem(j1);
                playerEntity.connection.sendPacket(new SPacketSetSlot(-2, playerEntity.inventory.currentItem, playerEntity.inventory.getStackInSlot(playerEntity.inventory.currentItem)));
                playerEntity.connection.sendPacket(new SPacketSetSlot(-2, j1, playerEntity.inventory.getStackInSlot(j1)));
                playerEntity.connection.sendPacket(new SPacketHeldItemChange(playerEntity.inventory.currentItem));
            }
            catch (Exception exception)
            {
                LOGGER.error("Couldn't pick item", exception);
            }
        }
    }
}
