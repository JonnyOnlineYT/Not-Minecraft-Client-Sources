package net.minecraft.client.network;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.ChunkEvent;
import baritone.api.event.events.type.EventState;
import baritone.cache.CachedChunk;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.screen.DisconnectScreen;
import net.minecraft.client.gui.screen.LevelLoadingScreen;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.MultiplayerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.toasts.RecipeToast;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleItemPickup;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.renderer.debug.DebugRendererNeighborsUpdate;
import net.minecraft.client.renderer.debug.DebugRendererPathfinding;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
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
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityLlamaSpit;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerHorseChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketResourcePackStatus;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketCamera;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketCombatEvent;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketCooldown;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.network.play.server.SPacketEffect;
import net.minecraft.network.play.server.SPacketEntity;
import net.minecraft.network.play.server.SPacketEntityAttach;
import net.minecraft.network.play.server.SPacketEntityEffect;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketEntityHeadLook;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketEntityProperties;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityTeleport;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketHeldItemChange;
import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketKeepAlive;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import net.minecraft.network.play.server.SPacketPlayerAbilities;
import net.minecraft.network.play.server.SPacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.network.play.server.SPacketRemoveEntityEffect;
import net.minecraft.network.play.server.SPacketResourcePackSend;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.network.play.server.SPacketScoreboardObjective;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.play.server.SPacketServerDifficulty;
import net.minecraft.network.play.server.SPacketSetExperience;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.network.play.server.SPacketSignEditorOpen;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.network.play.server.SPacketSpawnPosition;
import net.minecraft.network.play.server.SPacketStatistics;
import net.minecraft.network.play.server.SPacketTabComplete;
import net.minecraft.network.play.server.SPacketTeams;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraft.network.play.server.SPacketTitle;
import net.minecraft.network.play.server.SPacketUnloadChunk;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.network.play.server.SPacketUpdateHealth;
import net.minecraft.network.play.server.SPacketUpdateScore;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.network.play.server.SPacketUseBed;
import net.minecraft.network.play.server.SPacketWindowItems;
import net.minecraft.network.play.server.SPacketWindowProperty;
import net.minecraft.network.play.server.SPacketWorldBorder;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.Namespaced;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.util.text.Formatting;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.combat.Velocity;

public class NetHandlerPlayClient implements INetHandlerPlayClient
{
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * The NetworkManager instance used to communicate with the server (used only by handlePlayerPosLook to update
     * positioning and handleJoinGame to inform the server of the client distribution/mods)
     */
    private final NetworkManager netManager;
    private final GameProfile profile;

    /**
     * Seems to be either null (integrated server) or an instance of either GuiMultiplayer (when connecting to a server)
     * or GuiScreenReamlsTOS (when connecting to MCO server)
     */
    private final Screen screenServer;

    /**
     * Reference to the Minecraft instance, which many handler methods operate on
     */
    private Minecraft gameController;

    /**
     * Reference to the current ClientWorld instance, which many handler methods operate on
     */
    public ClientLevel clientWorldController;

    /**
     * True if the client has finished downloading terrain and may spawn. Set upon receipt of S08PacketPlayerPosLook,
     * reset upon respawning
     */
    private boolean doneLoadingTerrain;
    private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();
    public int currentServerMaxPlayers = 20;
    private boolean hasStatistics;
    private final ClientAdvancementManager field_191983_k;

    /**
     * Just an ordinary random number generator, used to randomize audio pitch of item/orb pickup and randomize both
     * particlespawn offset and velocity
     */
    private final Random avRandomizer = new Random();

    public NetHandlerPlayClient(Minecraft mcIn, Screen p_i46300_2_, NetworkManager networkManagerIn, GameProfile profileIn)
    {
        gameController = mcIn;
        screenServer = p_i46300_2_;
        netManager = networkManagerIn;
        profile = profileIn;
        field_191983_k = new ClientAdvancementManager(mcIn);
    }

    /**
     * Clears the WorldClient instance associated with this NetHandlerPlayClient
     */
    public void cleanup()
    {
        clientWorldController = null;
    }

    /**
     * Registers some server properties (gametype,hardcore-mode,terraintype,difficulty,player limit), creates a new
     * WorldClient and sets the player initial dimension
     */
    public void handleJoinGame(SPacketJoinGame packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.playerController = new PlayerControllerMP(gameController, this);
        clientWorldController = new ClientLevel(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), packetIn.getDimension(), packetIn.getDifficulty(), Minecraft.profiler);
        Minecraft.gameSettings.difficulty = packetIn.getDifficulty();
        gameController.loadWorld(clientWorldController);
        Minecraft.player.dimension = packetIn.getDimension();
        Minecraft.openScreen(new LevelLoadingScreen());
        Minecraft.player.setEntityId(packetIn.getPlayerId());
        currentServerMaxPlayers = packetIn.getMaxPlayers();
        Minecraft.player.setReducedDebug(packetIn.isReducedDebugInfo());
        Minecraft.playerController.setGameType(packetIn.getGameType());
        Minecraft.gameSettings.sendSettingsToServer();
        netManager.sendPacket(new CPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString("1.12.2")));
    }

    /**
     * Spawns an instance of the objecttype indicated by the packet and sets its position and momentum
     */
    public void handleSpawnObject(SPacketSpawnObject packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        Entity entity = null;

        if (packetIn.getType() == 10)
        {
            entity = EntityMinecart.create(clientWorldController, d0, d1, d2, EntityMinecart.Type.getById(packetIn.getData()));
        }
        else if (packetIn.getType() == 90)
        {
            Entity entity1 = clientWorldController.getEntityByID(packetIn.getData());

            if (entity1 instanceof EntityPlayer)
            {
                entity = new EntityFishHook(clientWorldController, (EntityPlayer)entity1, d0, d1, d2);
            }

            packetIn.setData(0);
        }
        else if (packetIn.getType() == 60)
        {
            entity = new EntityTippedArrow(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 91)
        {
            entity = new EntitySpectralArrow(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 61)
        {
            entity = new EntitySnowball(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 68)
        {
            entity = new EntityLlamaSpit(clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0D, (double)packetIn.getSpeedY() / 8000.0D, (double)packetIn.getSpeedZ() / 8000.0D);
        }
        else if (packetIn.getType() == 71)
        {
            entity = new EntityItemFrame(clientWorldController, new BlockPos(d0, d1, d2), EnumFacing.getHorizontal(packetIn.getData()));
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 77)
        {
            entity = new EntityLeashKnot(clientWorldController, new BlockPos(MathHelper.floor(d0), MathHelper.floor(d1), MathHelper.floor(d2)));
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 65)
        {
            entity = new EntityEnderPearl(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 72)
        {
            entity = new EntityEnderEye(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 76)
        {
            entity = new EntityFireworkRocket(clientWorldController, d0, d1, d2, ItemStack.EMPTY);
        }
        else if (packetIn.getType() == 63)
        {
            entity = new EntityLargeFireball(clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0D, (double)packetIn.getSpeedY() / 8000.0D, (double)packetIn.getSpeedZ() / 8000.0D);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 93)
        {
            entity = new EntityDragonFireball(clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0D, (double)packetIn.getSpeedY() / 8000.0D, (double)packetIn.getSpeedZ() / 8000.0D);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 64)
        {
            entity = new EntitySmallFireball(clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0D, (double)packetIn.getSpeedY() / 8000.0D, (double)packetIn.getSpeedZ() / 8000.0D);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 66)
        {
            entity = new EntityWitherSkull(clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0D, (double)packetIn.getSpeedY() / 8000.0D, (double)packetIn.getSpeedZ() / 8000.0D);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 67)
        {
            entity = new EntityShulkerBullet(clientWorldController, d0, d1, d2, (double)packetIn.getSpeedX() / 8000.0D, (double)packetIn.getSpeedY() / 8000.0D, (double)packetIn.getSpeedZ() / 8000.0D);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 62)
        {
            entity = new EntityEgg(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 79)
        {
            entity = new EntityEvokerFangs(clientWorldController, d0, d1, d2, 0.0F, 0, null);
        }
        else if (packetIn.getType() == 73)
        {
            entity = new EntityPotion(clientWorldController, d0, d1, d2, ItemStack.EMPTY);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 75)
        {
            entity = new EntityExpBottle(clientWorldController, d0, d1, d2);
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 1)
        {
            entity = new EntityBoat(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 50)
        {
            entity = new EntityTNTPrimed(clientWorldController, d0, d1, d2, null);
        }
        else if (packetIn.getType() == 78)
        {
            entity = new EntityArmorStand(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 51)
        {
            entity = new EntityEnderCrystal(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 2)
        {
            entity = new EntityItem(clientWorldController, d0, d1, d2);
        }
        else if (packetIn.getType() == 70)
        {
            entity = new EntityFallingBlock(clientWorldController, d0, d1, d2, Block.getStateById(packetIn.getData() & 65535));
            packetIn.setData(0);
        }
        else if (packetIn.getType() == 3)
        {
            entity = new EntityAreaEffectCloud(clientWorldController, d0, d1, d2);
        }

        if (entity != null)
        {
            EntityTracker.updateServerPosition(entity, d0, d1, d2);
            entity.rotationPitch = (float)(packetIn.getPitch() * 360) / 256.0F;
            entity.rotationYaw = (float)(packetIn.getYaw() * 360) / 256.0F;
            Entity[] aentity = entity.getParts();

            if (aentity != null)
            {
                int i = packetIn.getEntityID() - entity.getEntityId();

                for (Entity entity2 : aentity)
                {
                    entity2.setEntityId(entity2.getEntityId() + i);
                }
            }

            entity.setEntityId(packetIn.getEntityID());
            entity.setUniqueId(packetIn.getUniqueId());
            clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);

            if (packetIn.getData() > 0)
            {
                if (packetIn.getType() == 60 || packetIn.getType() == 91)
                {
                    Entity entity3 = clientWorldController.getEntityByID(packetIn.getData() - 1);

                    if (entity3 instanceof EntityLivingBase && entity instanceof EntityArrow)
                    {
                        ((EntityArrow)entity).shootingEntity = entity3;
                    }
                }

                entity.setVelocity((double)packetIn.getSpeedX() / 8000.0D, (double)packetIn.getSpeedY() / 8000.0D, (double)packetIn.getSpeedZ() / 8000.0D);
            }
        }
    }

    /**
     * Spawns an experience orb and sets its value (amount of XP)
     */
    public void handleSpawnExperienceOrb(SPacketSpawnExperienceOrb packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        Entity entity = new EntityXPOrb(clientWorldController, d0, d1, d2, packetIn.getXPValue());
        EntityTracker.updateServerPosition(entity, d0, d1, d2);
        entity.rotationYaw = 0.0F;
        entity.rotationPitch = 0.0F;
        entity.setEntityId(packetIn.getEntityID());
        clientWorldController.addEntityToWorld(packetIn.getEntityID(), entity);
    }

    /**
     * Handles globally visible entities. Used in vanilla for lightning bolts
     */
    public void handleSpawnGlobalEntity(SPacketSpawnGlobalEntity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        Entity entity = null;

        if (packetIn.getType() == 1)
        {
            entity = new EntityLightningBolt(clientWorldController, d0, d1, d2, false);
        }

        if (entity != null)
        {
            EntityTracker.updateServerPosition(entity, d0, d1, d2);
            entity.rotationYaw = 0.0F;
            entity.rotationPitch = 0.0F;
            entity.setEntityId(packetIn.getEntityId());
            clientWorldController.addWeatherEffect(entity);
        }
    }

    /**
     * Handles the spawning of a painting object
     */
    public void handleSpawnPainting(SPacketSpawnPainting packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPainting entitypainting = new EntityPainting(clientWorldController, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
        entitypainting.setUniqueId(packetIn.getUniqueId());
        clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitypainting);
    }

    /**
     * Sets the velocity of the specified entity to the specified value
     */
    public void handleEntityVelocity(SPacketEntityVelocity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityID());

        if (entity != null)
        {
            if (Velocity.velocityMode.currentMode.equals("Matrix") && MoonWare.featureManager.getFeatureByClass(Velocity.class).getState()) {
                if (packetIn.getEntityID() == Minecraft.player.getEntityId()) {
                    entity.setVelocity(0.0D, packetIn.getMotionY() / 8000.0D, packetIn.getMotionZ() / 8000.0D / 2.5D);
                   // MovementHelper.strafe();
                }
            } else {
                entity.setVelocity((double) packetIn.getMotionX() / 8000.0D, (double) packetIn.getMotionY() / 8000.0D, (double) packetIn.getMotionZ() / 8000.0D);
            }
        }
    }

    /**
     * Invoked when the server registers new proximate objects in your watchlist or when objects in your watchlist have
     * changed -> Registers any changes locally
     */
    public void handleEntityMetadata(SPacketEntityMetadata packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity != null && packetIn.getDataManagerEntries() != null)
        {
            entity.getDataManager().setEntryValues(packetIn.getDataManagerEntries());
        }
    }

    /**
     * Handles the creation of a nearby player entity, sets the position and held item
     */
    public void handleSpawnPlayer(SPacketSpawnPlayer packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        float f = (float)(packetIn.getYaw() * 360) / 256.0F;
        float f1 = (float)(packetIn.getPitch() * 360) / 256.0F;
        EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(Minecraft.world, getPlayerInfo(packetIn.getUniqueId()).getGameProfile());
        entityotherplayermp.prevPosX = d0;
        entityotherplayermp.lastTickPosX = d0;
        entityotherplayermp.prevPosY = d1;
        entityotherplayermp.lastTickPosY = d1;
        entityotherplayermp.prevPosZ = d2;
        entityotherplayermp.lastTickPosZ = d2;
        EntityTracker.updateServerPosition(entityotherplayermp, d0, d1, d2);
        entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
        clientWorldController.addEntityToWorld(packetIn.getEntityID(), entityotherplayermp);
        List < EntityDataManager.DataEntry<? >> list = packetIn.getDataManagerEntries();

        if (list != null)
        {
            entityotherplayermp.getDataManager().setEntryValues(list);
        }
    }

    /**
     * Updates an entity's position and rotation as specified by the packet
     */
    public void handleEntityTeleport(SPacketEntityTeleport packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity != null)
        {
            double d0 = packetIn.getX();
            double d1 = packetIn.getY();
            double d2 = packetIn.getZ();
            EntityTracker.updateServerPosition(entity, d0, d1, d2);

            if (!entity.canPassengerSteer())
            {
                float f = (float)(packetIn.getYaw() * 360) / 256.0F;
                float f1 = (float)(packetIn.getPitch() * 360) / 256.0F;

                if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D)
                {
                    entity.setPositionAndRotationDirect(entity.posX, entity.posY, entity.posZ, f, f1, 0, true);
                }
                else
                {
                    entity.setPositionAndRotationDirect(d0, d1, d2, f, f1, 3, true);
                }

                entity.onGround = packetIn.getOnGround();
            }
        }
    }

    /**
     * Updates which hotbar slot of the player is currently selected
     */
    public void handleHeldItemChange(SPacketHeldItemChange packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (InventoryPlayer.isHotbar(packetIn.getHeldItemHotbarIndex()))
        {
            Minecraft.player.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
        }
    }

    /**
     * Updates the specified entity's position by the specified relative moment and absolute rotation. Note that
     * subclassing of the packet allows for the specification of a subset of this data (e.g. only rel. position, abs.
     * rotation or both).
     */
    public void handleEntityMovement(SPacketEntity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null)
        {
            entity.serverPosX += packetIn.getX();
            entity.serverPosY += packetIn.getY();
            entity.serverPosZ += packetIn.getZ();
            double d0 = (double)entity.serverPosX / 4096.0D;
            double d1 = (double)entity.serverPosY / 4096.0D;
            double d2 = (double)entity.serverPosZ / 4096.0D;

            if (!entity.canPassengerSteer())
            {
                float f = packetIn.isRotating() ? (float)(packetIn.getYaw() * 360) / 256.0F : entity.rotationYaw;
                float f1 = packetIn.isRotating() ? (float)(packetIn.getPitch() * 360) / 256.0F : entity.rotationPitch;
                entity.setPositionAndRotationDirect(d0, d1, d2, f, f1, 3, false);
                entity.onGround = packetIn.getOnGround();
            }
        }
    }

    /**
     * Updates the direction in which the specified entity is looking, normally this head rotation is independent of the
     * rotation of the entity itself
     */
    public void handleEntityHeadLook(SPacketEntityHeadLook packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null)
        {
            float f = (float)(packetIn.getYaw() * 360) / 256.0F;
            entity.setRotationYawHead(f);
        }
    }

    /**
     * Locally eliminates the entities. Invoked by the server when the items are in fact destroyed, or the player is no
     * longer registered as required to monitor them. The latter  happens when distance between the player and item
     * increases beyond a certain treshold (typically the viewing distance)
     */
    public void handleDestroyEntities(SPacketDestroyEntities packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        for (int i = 0; i < packetIn.getEntityIDs().length; ++i)
        {
            clientWorldController.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
        }
    }

    public void handlePlayerPosLook(SPacketPlayerPosLook packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = Minecraft.player;
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        float f = packetIn.getYaw();
        float f1 = packetIn.getPitch();

        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X))
        {
            d0 += entityplayer.posX;
        }
        else
        {
            entityplayer.motionX = 0.0D;
        }

        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y))
        {
            d1 += entityplayer.posY;
        }
        else
        {
            entityplayer.motionY = 0.0D;
        }

        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Z))
        {
            d2 += entityplayer.posZ;
        }
        else
        {
            entityplayer.motionZ = 0.0D;
        }

        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X_ROT))
        {
            f1 += entityplayer.rotationPitch;
        }

        if (packetIn.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y_ROT))
        {
            f += entityplayer.rotationYaw;
        }

        entityplayer.setPositionAndRotation(d0, d1, d2, f, f1);
        netManager.sendPacket(new CPacketConfirmTeleport(packetIn.getTeleportId()));
        netManager.sendPacket(new CPacketPlayer.PositionRotation(entityplayer.posX, entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch, false));

        if (!doneLoadingTerrain)
        {
            Minecraft.player.prevPosX = Minecraft.player.posX;
            Minecraft.player.prevPosY = Minecraft.player.posY;
            Minecraft.player.prevPosZ = Minecraft.player.posZ;
            doneLoadingTerrain = true;
            Minecraft.openScreen(null);
        }
    }

    /**
     * Received from the servers PlayerManager if between 1 and 64 blocks in a chunk are changed. If only one block
     * requires an update, the server sends S23PacketBlockChange and if 64 or more blocks are changed, the server sends
     * S21PacketChunkData
     */
    public void handleMultiBlockChange(SPacketMultiBlockChange packetIn)
    {
        ChunkPos pos = new ChunkPos(packetIn.getChangedBlocks()[0].getPos());
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            EntityPlayerSP player = ibaritone.getPlayerContext().player();
            if (player != null && player.connection == this) {
                ibaritone.getGameEventHandler().onChunkEvent(
                        new ChunkEvent(
                                EventState.POST,
                                ChunkEvent.Type.POPULATE_FULL,
                                pos.chunkXPos,
                                pos.chunkZPos
                        )
                );
            }
        }

        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        for (SPacketMultiBlockChange.BlockUpdateData spacketmultiblockchange$blockupdatedata : packetIn.getChangedBlocks())
        {
            clientWorldController.invalidateRegionAndSetBlock(spacketmultiblockchange$blockupdatedata.getPos(), spacketmultiblockchange$blockupdatedata.getBlockState());
        }
    }

    /**
     * Updates the specified chunk with the supplied data, marks it for re-rendering and lighting recalculation
     */
    public void handleChunkData(SPacketChunkData packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.doChunkLoad())
        {
            clientWorldController.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);
        }

        clientWorldController.invalidateBlockReceiveRegion(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);
        Chunk chunk = clientWorldController.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());


        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            EntityPlayerSP player = ibaritone.getPlayerContext().player();
            if (player != null && player.connection == this) {
                ibaritone.getGameEventHandler().onChunkEvent(
                        new ChunkEvent(
                                EventState.PRE,
                                packetIn.doChunkLoad() ? ChunkEvent.Type.POPULATE_FULL : ChunkEvent.Type.POPULATE_PARTIAL,
                                packetIn.getChunkX(),
                                packetIn.getChunkZ()
                        )
                );
            }
        }

        chunk.fillChunk(packetIn.getReadBuffer(), packetIn.getExtractedSize(), packetIn.doChunkLoad());
        clientWorldController.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);

        if (!packetIn.doChunkLoad() || !(clientWorldController.provider instanceof WorldProviderSurface))
        {
            chunk.resetRelightChecks();
        }

        for (NBTTagCompound nbttagcompound : packetIn.getTileEntityTags())
        {
            BlockPos blockpos = new BlockPos(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"));
            TileEntity tileentity = clientWorldController.getTileEntity(blockpos);

            if (tileentity != null)
            {
                tileentity.readFromNBT(nbttagcompound);
            }
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            EntityPlayerSP player = ibaritone.getPlayerContext().player();
            if (player != null && player.connection == this) {
                ibaritone.getGameEventHandler().onChunkEvent(
                        new ChunkEvent(
                                EventState.POST,
                                packetIn.doChunkLoad() ? ChunkEvent.Type.POPULATE_FULL : ChunkEvent.Type.POPULATE_PARTIAL,
                                packetIn.getChunkX(),
                                packetIn.getChunkZ()
                        )
                );
            }
        }
    }

    public void processChunkUnload(SPacketUnloadChunk packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        clientWorldController.doPreChunk(packetIn.getX(), packetIn.getZ(), false);
    }

    /**
     * Updates the block and metadata and generates a blockupdate (and notify the clients)
     */
    public void handleBlockChange(SPacketBlockChange packetIn)
    {
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            EntityPlayerSP player = ibaritone.getPlayerContext().player();
            if (player != null && player.connection == this) {
                ibaritone.getGameEventHandler().onChunkEvent(
                        new ChunkEvent(
                                EventState.POST,
                                ChunkEvent.Type.POPULATE_FULL,
                                packetIn.getBlockPosition().getX() >> 4,
                                packetIn.getBlockPosition().getZ() >> 4
                        )
                );
            }
        }

        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        clientWorldController.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
    }

    /**
     * Closes the network channel
     */
    public void handleDisconnect(SPacketDisconnect packetIn)
    {
        netManager.closeChannel(packetIn.getReason());
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(Component reason)
    {
        gameController.loadWorld(null);

        if (screenServer != null)
        {
            Minecraft.openScreen(new DisconnectScreen(screenServer, new TranslatableComponent("disconnect.lost"), reason));
        }
        else
        {
            Minecraft.openScreen(new DisconnectScreen(new MultiplayerScreen(), new TranslatableComponent("disconnect.lost"), reason));
        }
    }

    public void sendPacket(Packet<?> packetIn)
    {
        netManager.sendPacket(packetIn);
    }

    public void handleCollectItem(SPacketCollectItem packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getCollectedItemEntityID());
        EntityLivingBase entitylivingbase = (EntityLivingBase) clientWorldController.getEntityByID(packetIn.getEntityID());

        if (entitylivingbase == null)
        {
            entitylivingbase = Minecraft.player;
        }

        if (entity != null)
        {
            if (entity instanceof EntityXPOrb)
            {
                clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 0.1F, (avRandomizer.nextFloat() - avRandomizer.nextFloat()) * 0.35F + 0.9F, false);
            }
            else
            {
                clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, (avRandomizer.nextFloat() - avRandomizer.nextFloat()) * 1.4F + 2.0F, false);
            }

            if (entity instanceof EntityItem)
            {
                ((EntityItem)entity).getEntityItem().func_190920_e(packetIn.func_191208_c());
            }

            Minecraft.effectRenderer.addEffect(new ParticleItemPickup(clientWorldController, entity, entitylivingbase, 0.5F));
            clientWorldController.removeEntityFromWorld(packetIn.getCollectedItemEntityID());
        }
    }

    /**
     * Prints a chatmessage in the chat GUI
     */
    public void handleChat(SPacketChat packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        if (packetIn.getChatType() == ChatType.GAME_INFO) {
            Minecraft.ingameGUI.setActionBar(packetIn.getChatComponent(), false);
        } else {
            Minecraft.ingameGUI.getChatGUI().printChatMessage(packetIn.getChatComponent());
        }
    }

    /**
     * Renders a specified animation: Waking up a player, a living entity swinging its currently held item, being hurt
     * or receiving a critical hit by normal or magical means
     */
    public void handleAnimation(SPacketAnimation packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityID());

        if (entity != null)
        {
            if (packetIn.getAnimationType() == 0)
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                entitylivingbase.swingArm(EnumHand.MAIN_HAND);
            }
            else if (packetIn.getAnimationType() == 3)
            {
                EntityLivingBase entitylivingbase1 = (EntityLivingBase)entity;
                entitylivingbase1.swingArm(EnumHand.OFF_HAND);
            }
            else if (packetIn.getAnimationType() == 1)
            {
                entity.performHurtAnimation();
            }
            else if (packetIn.getAnimationType() == 2)
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                entityplayer.wakeUpPlayer(false, false, false);
            }
            else if (packetIn.getAnimationType() == 4)
            {
                Minecraft.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
            }
            else if (packetIn.getAnimationType() == 5)
            {
                Minecraft.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    /**
     * Retrieves the player identified by the packet, puts him to sleep if possible (and flags whether all players are
     * asleep)
     */
    public void handleUseBed(SPacketUseBed packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        packetIn.getPlayer(clientWorldController).trySleep(packetIn.getBedPosition());
    }

    /**
     * Spawns the mob entity at the specified location, with the specified rotation, momentum and type. Updates the
     * entities Datawatchers with the entity metadata specified in the packet
     */
    public void handleSpawnMob(SPacketSpawnMob packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        double d0 = packetIn.getX();
        double d1 = packetIn.getY();
        double d2 = packetIn.getZ();
        float f = (float)(packetIn.getYaw() * 360) / 256.0F;
        float f1 = (float)(packetIn.getPitch() * 360) / 256.0F;
        EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.getEntityType(), Minecraft.world);

        if (entitylivingbase != null)
        {
            EntityTracker.updateServerPosition(entitylivingbase, d0, d1, d2);
            entitylivingbase.renderYawOffset = (float)(packetIn.getHeadPitch() * 360) / 256.0F;
            entitylivingbase.rotationYawHead = (float)(packetIn.getHeadPitch() * 360) / 256.0F;
            Entity[] aentity = entitylivingbase.getParts();

            if (aentity != null)
            {
                int i = packetIn.getEntityID() - entitylivingbase.getEntityId();

                for (Entity entity : aentity)
                {
                    entity.setEntityId(entity.getEntityId() + i);
                }
            }

            entitylivingbase.setEntityId(packetIn.getEntityID());
            entitylivingbase.setUniqueId(packetIn.getUniqueId());
            entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
            entitylivingbase.motionX = (float)packetIn.getVelocityX() / 8000.0F;
            entitylivingbase.motionY = (float)packetIn.getVelocityY() / 8000.0F;
            entitylivingbase.motionZ = (float)packetIn.getVelocityZ() / 8000.0F;
            clientWorldController.addEntityToWorld(packetIn.getEntityID(), entitylivingbase);
            List < EntityDataManager.DataEntry<? >> list = packetIn.getDataManagerEntries();

            if (list != null)
            {
                entitylivingbase.getDataManager().setEntryValues(list);
            }
        }
        else
        {
            LOGGER.warn("Skipping Entity with id {}", packetIn.getEntityType());
        }
    }

    public void handleTimeUpdate(SPacketTimeUpdate packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.world.setTotalWorldTime(packetIn.getTotalWorldTime());
        Minecraft.world.setWorldTime(packetIn.getWorldTime());
    }

    public void handleSpawnPosition(SPacketSpawnPosition packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.player.setSpawnPoint(packetIn.getSpawnPos(), true);
        Minecraft.world.getWorldInfo().setSpawn(packetIn.getSpawnPos());
    }

    public void handleSetPassengers(SPacketSetPassengers packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity == null)
        {
            LOGGER.warn("Received passengers for unknown entity");
        }
        else
        {
            boolean flag = entity.isRidingOrBeingRiddenBy(Minecraft.player);
            entity.removePassengers();

            for (int i : packetIn.getPassengerIds())
            {
                Entity entity1 = clientWorldController.getEntityByID(i);

                if (entity1 != null)
                {
                    entity1.startRiding(entity, true);

                    if (entity1 == Minecraft.player && !flag)
                    {
                        Minecraft.ingameGUI.setActionBar(I18n.format("mount.onboard", GameSettings.getKeyDisplayString(Minecraft.gameSettings.keyBindSneak.getKeyCode())), false);
                    }
                }
            }
        }
    }

    public void handleEntityAttach(SPacketEntityAttach packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());
        Entity entity1 = clientWorldController.getEntityByID(packetIn.getVehicleEntityId());

        if (entity instanceof EntityLiving)
        {
            if (entity1 != null)
            {
                ((EntityLiving)entity).setLeashedToEntity(entity1, false);
            }
            else
            {
                ((EntityLiving)entity).clearLeashed(false, false);
            }
        }
    }

    /**
     * Invokes the entities' handleUpdateHealth method which is implemented in LivingBase (hurt/death),
     * MinecartMobSpawner (spawn delay), FireworkRocket & MinecartTNT (explosion), IronGolem (throwing,...), Witch
     * (spawn particles), Zombie (villager transformation), Animal (breeding mode particles), Horse (breeding/smoke
     * particles), Sheep (...), Tameable (...), Villager (particles for breeding mode, angry and happy), Wolf (...)
     */
    public void handleEntityStatus(SPacketEntityStatus packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null)
        {
            if (packetIn.getOpCode() == 21)
            {
                Minecraft.getSoundHandler().playSound(new GuardianSound((EntityGuardian)entity));
            }
            else if (packetIn.getOpCode() == 35)
            {
                int i = 40;
                Minecraft.effectRenderer.func_191271_a(entity, EnumParticleTypes.TOTEM, 30);
                clientWorldController.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);

                if (entity == Minecraft.player)
                {
                    Minecraft.gameRenderer.func_190565_a(new ItemStack(Items.TOTEM_OF_UNDYING));
                }
            }
            else
            {
                entity.handleStatusUpdate(packetIn.getOpCode());
            }
        }
    }

    public void handleUpdateHealth(SPacketUpdateHealth packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.player.setPlayerSPHealth(packetIn.getHealth());
        Minecraft.player.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        Minecraft.player.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }

    public void handleSetExperience(SPacketSetExperience packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.player.setXPStats(packetIn.getExperienceBar(), packetIn.getTotalExperience(), packetIn.getLevel());
    }

    public void handleRespawn(SPacketRespawn packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.getDimensionID() != Minecraft.player.dimension)
        {
            doneLoadingTerrain = false;
            Scoreboard scoreboard = clientWorldController.getScoreboard();
            clientWorldController = new ClientLevel(this, new WorldSettings(0L, packetIn.getGameType(), false, Minecraft.world.getWorldInfo().isHardcoreModeEnabled(), packetIn.getWorldType()), packetIn.getDimensionID(), packetIn.getDifficulty(), Minecraft.profiler);
            clientWorldController.setWorldScoreboard(scoreboard);
            gameController.loadWorld(clientWorldController);
            Minecraft.player.dimension = packetIn.getDimensionID();
            Minecraft.openScreen(new LevelLoadingScreen());
        }

        gameController.setDimensionAndSpawnPlayer(packetIn.getDimensionID());
        Minecraft.playerController.setGameType(packetIn.getGameType());
    }

    /**
     * Initiates a new explosion (sound, particles, drop spawn) for the affected blocks indicated by the packet.
     */
    public void handleExplosion(SPacketExplosion packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Explosion explosion = new Explosion(Minecraft.world, null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
        explosion.doExplosionB(true);
        Minecraft.player.motionX += packetIn.getMotionX();
        Minecraft.player.motionY += packetIn.getMotionY();
        Minecraft.player.motionZ += packetIn.getMotionZ();
    }

    /**
     * Displays a GUI by ID. In order starting from id 0: Chest, Workbench, Furnace, Dispenser, Enchanting table,
     * Brewing stand, Villager merchant, Beacon, Anvil, Hopper, Dropper, Horse
     */
    public void handleOpenWindow(SPacketOpenWindow packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayerSP entityplayersp = Minecraft.player;

        if ("minecraft:container".equals(packetIn.getGuiId()))
        {
            entityplayersp.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else if ("minecraft:villager".equals(packetIn.getGuiId()))
        {
            entityplayersp.displayVillagerTradeGui(new NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else if ("EntityHorse".equals(packetIn.getGuiId()))
        {
            Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

            if (entity instanceof AbstractHorse)
            {
                entityplayersp.openGuiHorseInventory((AbstractHorse)entity, new ContainerHorseChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
                entityplayersp.openContainer.windowId = packetIn.getWindowId();
            }
        }
        else if (!packetIn.hasSlots())
        {
            entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else
        {
            IInventory iinventory = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
            entityplayersp.displayGUIChest(iinventory);
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
    }

    /**
     * Handles pickin up an ItemStack or dropping one in your inventory or an open (non-creative) container
     */
    public void handleSetSlot(SPacketSetSlot packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = Minecraft.player;
        ItemStack itemstack = packetIn.getStack();
        int i = packetIn.getSlot();

        if (packetIn.getWindowId() == -1)
        {
            entityplayer.inventory.setItemStack(itemstack);
        }
        else if (packetIn.getWindowId() == -2)
        {
            entityplayer.inventory.setInventorySlotContents(i, itemstack);
        }
        else
        {
            boolean flag = false;

            if (Minecraft.screen instanceof GuiContainerCreative)
            {
                GuiContainerCreative guicontainercreative = (GuiContainerCreative) Minecraft.screen;
                flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.INVENTORY.getTabIndex();
            }

            if (packetIn.getWindowId() == 0 && packetIn.getSlot() >= 36 && i < 45)
            {
                if (!itemstack.isEmpty())
                {
                    ItemStack itemstack1 = entityplayer.inventoryContainer.getSlot(i).getStack();

                    if (itemstack1.isEmpty() || itemstack1.getCount() < itemstack.getCount())
                    {
                        itemstack.func_190915_d(5);
                    }
                }

                entityplayer.inventoryContainer.putStackInSlot(i, itemstack);
            }
            else if (packetIn.getWindowId() == entityplayer.openContainer.windowId && (packetIn.getWindowId() != 0 || !flag))
            {
                entityplayer.openContainer.putStackInSlot(i, itemstack);
            }
        }
    }

    /**
     * Verifies that the server and client are synchronized with respect to the inventory/container opened by the player
     * and confirms if it is the case.
     */
    public void handleConfirmTransaction(SPacketConfirmTransaction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Container container = null;
        EntityPlayer entityplayer = Minecraft.player;

        if (packetIn.getWindowId() == 0)
        {
            container = entityplayer.inventoryContainer;
        }
        else if (packetIn.getWindowId() == entityplayer.openContainer.windowId)
        {
            container = entityplayer.openContainer;
        }

        if (container != null && !packetIn.wasAccepted())
        {
            sendPacket(new CPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
        }
    }

    /**
     * Handles the placement of a specified ItemStack in a specified container/inventory slot
     */
    public void handleWindowItems(SPacketWindowItems packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = Minecraft.player;

        if (packetIn.getWindowId() == 0)
        {
            entityplayer.inventoryContainer.func_190896_a(packetIn.getItemStacks());
        }
        else if (packetIn.getWindowId() == entityplayer.openContainer.windowId)
        {
            entityplayer.openContainer.func_190896_a(packetIn.getItemStacks());
        }
    }

    /**
     * Creates a sign in the specified location if it didn't exist and opens the GUI to edit its text
     */
    public void handleSignEditorOpen(SPacketSignEditorOpen packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        TileEntity tileentity = clientWorldController.getTileEntity(packetIn.getSignPosition());

        if (!(tileentity instanceof TileEntitySign))
        {
            tileentity = new TileEntitySign();
            tileentity.setWorldObj(clientWorldController);
            tileentity.setPos(packetIn.getSignPosition());
        }

        Minecraft.player.openEditSign((TileEntitySign)tileentity);
    }

    /**
     * Updates the NBTTagCompound metadata of instances of the following entitytypes: Mob spawners, command blocks,
     * beacons, skulls, flowerpot
     */
    public void handleUpdateTileEntity(SPacketUpdateTileEntity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (Minecraft.world.isBlockLoaded(packetIn.getPos()))
        {
            TileEntity tileentity = Minecraft.world.getTileEntity(packetIn.getPos());
            int i = packetIn.getTileEntityType();
            boolean flag = i == 2 && tileentity instanceof TileEntityCommandBlock;

            if (i == 1 && tileentity instanceof TileEntityMobSpawner || flag || i == 3 && tileentity instanceof TileEntityBeacon || i == 4 && tileentity instanceof TileEntitySkull || i == 5 && tileentity instanceof TileEntityFlowerPot || i == 6 && tileentity instanceof TileEntityBanner || i == 7 && tileentity instanceof TileEntityStructure || i == 8 && tileentity instanceof TileEntityEndGateway || i == 9 && tileentity instanceof TileEntitySign || i == 10 && tileentity instanceof TileEntityShulkerBox || i == 11 && tileentity instanceof TileEntityBed)
            {
                tileentity.readFromNBT(packetIn.getNbtCompound());
            }

            if (flag && Minecraft.screen instanceof GuiCommandBlock)
            {
                ((GuiCommandBlock) Minecraft.screen).updateGui();
            }
        }
    }

    /**
     * Sets the progressbar of the opened window to the specified value
     */
    public void handleWindowProperty(SPacketWindowProperty packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = Minecraft.player;

        if (entityplayer.openContainer != null && entityplayer.openContainer.windowId == packetIn.getWindowId())
        {
            entityplayer.openContainer.updateProgressBar(packetIn.getProperty(), packetIn.getValue());
        }
    }

    public void handleEntityEquipment(SPacketEntityEquipment packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityID());

        if (entity != null)
        {
            entity.setItemStackToSlot(packetIn.getEquipmentSlot(), packetIn.getItemStack());
        }
    }

    /**
     * Resets the ItemStack held in hand and closes the window that is opened
     */
    public void handleCloseWindow(SPacketCloseWindow packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.player.closeScreenAndDropStack();
    }

    /**
     * Triggers Block.onBlockEventReceived, which is implemented in BlockPistonBase for extension/retraction, BlockNote
     * for setting the instrument (including audiovisual feedback) and in BlockContainer to set the number of players
     * accessing a (Ender)Chest
     */
    public void handleBlockAction(SPacketBlockAction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.world.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
    }

    /**
     * Updates all registered IWorldAccess instances with destroyBlockInWorldPartially
     */
    public void handleBlockBreakAnim(SPacketBlockBreakAnim packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.world.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
    }

    public void handleChangeGameState(SPacketChangeGameState packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer = Minecraft.player;
        int i = packetIn.getGameState();
        float f = packetIn.getValue();
        int j = MathHelper.floor(f + 0.5F);

        if (i >= 0 && i < SPacketChangeGameState.MESSAGE_NAMES.length && SPacketChangeGameState.MESSAGE_NAMES[i] != null)
        {
            entityplayer.addChatComponentMessage(new TranslatableComponent(SPacketChangeGameState.MESSAGE_NAMES[i]), false);
        }

        if (i == 1)
        {
            clientWorldController.getWorldInfo().setRaining(true);
            clientWorldController.setRainStrength(0.0F);
        }
        else if (i == 2)
        {
            clientWorldController.getWorldInfo().setRaining(false);
            clientWorldController.setRainStrength(1.0F);
        }
        else if (i == 3)
        {
            Minecraft.playerController.setGameType(GameType.getByID(j));
        }
        else if (i == 4)
        {
            if (j == 0)
            {
                Minecraft.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
                Minecraft.openScreen(new LevelLoadingScreen());
            }
            else if (j == 1)
            {
                Minecraft.openScreen(new GuiWinGame(true, () ->
                {
                    Minecraft.player.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
                }));
            }
        }
        else if (i == 5)
        {
            GameSettings gamesettings = Minecraft.gameSettings;

            if (f == 101.0F)
            {
                Minecraft.ingameGUI.getChatGUI().printChatMessage(new TranslatableComponent("demo.help.movement", GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode())));
            }
            else if (f == 102.0F)
            {
                Minecraft.ingameGUI.getChatGUI().printChatMessage(new TranslatableComponent("demo.help.jump", GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode())));
            }
            else if (f == 103.0F)
            {
                Minecraft.ingameGUI.getChatGUI().printChatMessage(new TranslatableComponent("demo.help.inventory", GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode())));
            }
        }
        else if (i == 6)
        {
            clientWorldController.playSound(entityplayer, entityplayer.posX, entityplayer.posY + (double)entityplayer.getEyeHeight(), entityplayer.posZ, SoundEvents.ENTITY_ARROW_HIT_PLAYER, SoundCategory.PLAYERS, 0.18F, 0.45F);
        }
        else if (i == 7)
        {
            clientWorldController.setRainStrength(f);
        }
        else if (i == 8)
        {
            clientWorldController.setThunderStrength(f);
        }
        else if (i == 10)
        {
            clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayer.posX, entityplayer.posY, entityplayer.posZ, 0.0D, 0.0D, 0.0D);
            clientWorldController.playSound(entityplayer, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.HOSTILE, 1.0F, 1.0F);
        }
    }

    /**
     * Updates the worlds MapStorage with the specified MapData for the specified map-identifier and invokes a
     * MapItemRenderer for it
     */
    public void handleMaps(SPacketMaps packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        MapItemRenderer mapitemrenderer = Minecraft.gameRenderer.getMapItemRenderer();
        MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), Minecraft.world);

        if (mapdata == null)
        {
            String s = "map_" + packetIn.getMapId();
            mapdata = new MapData(s);

            if (mapitemrenderer.func_191205_a(s) != null)
            {
                MapData mapdata1 = mapitemrenderer.func_191207_a(mapitemrenderer.func_191205_a(s));

                if (mapdata1 != null)
                {
                    mapdata = mapdata1;
                }
            }

            Minecraft.world.setItemData(s, mapdata);
        }

        packetIn.setMapdataTo(mapdata);
        mapitemrenderer.updateMapTexture(mapdata);
    }

    public void handleEffect(SPacketEffect packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.isSoundServerwide())
        {
            Minecraft.world.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
        else
        {
            Minecraft.world.playEvent(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
        }
    }

    public void func_191981_a(SPacketAdvancementInfo p_191981_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_191981_1_, this, gameController);
        field_191983_k.func_192799_a(p_191981_1_);
    }

    public void func_194022_a(SPacketSelectAdvancementsTab p_194022_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_194022_1_, this, gameController);
        Namespaced resourcelocation = p_194022_1_.func_194154_a();

        if (resourcelocation == null)
        {
            field_191983_k.func_194230_a(null, false);
        }
        else
        {
            Advancement advancement = field_191983_k.func_194229_a().func_192084_a(resourcelocation);
            field_191983_k.func_194230_a(advancement, false);
        }
    }

    /**
     * Updates the players statistics or achievements
     */
    public void handleStatistics(SPacketStatistics packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        for (Map.Entry<StatBase, Integer> entry : packetIn.getStatisticMap().entrySet())
        {
            StatBase statbase = entry.getKey();
            int k = entry.getValue().intValue();
            Minecraft.player.getStatFileWriter().unlockAchievement(Minecraft.player, statbase, k);
        }

        hasStatistics = true;

        if (Minecraft.screen instanceof IProgressMeter)
        {
            ((IProgressMeter) Minecraft.screen).func_193026_g();
        }
    }

    public void func_191980_a(SPacketRecipeBook p_191980_1_)
    {
        RecipeBook recipebook;
        PacketThreadUtil.checkThreadAndEnqueue(p_191980_1_, this, gameController);
        recipebook = Minecraft.player.func_192035_E();
        recipebook.func_192813_a(p_191980_1_.func_192593_c());
        recipebook.func_192810_b(p_191980_1_.func_192594_d());
        SPacketRecipeBook.State spacketrecipebook$state = p_191980_1_.func_194151_e();
        label21:

        switch (spacketrecipebook$state)
        {
            case REMOVE:
                Iterator iterator = p_191980_1_.func_192595_a().iterator();

                while (true)
                {
                    if (!iterator.hasNext())
                    {
                        break label21;
                    }

                    IRecipe irecipe = (IRecipe)iterator.next();
                    recipebook.func_193831_b(irecipe);
                }

            case INIT:
                p_191980_1_.func_192595_a().forEach(recipebook::func_194073_a);
                p_191980_1_.func_193644_b().forEach(recipebook::func_193825_e);
                break;

            case ADD:
                p_191980_1_.func_192595_a().forEach((p_194025_2_) ->
                {
                    recipebook.func_194073_a(p_194025_2_);
                    recipebook.func_193825_e(p_194025_2_);
                    RecipeToast.addOrUpdate(p_194025_2_);
                });
        }

        RecipeBookClient.field_194087_f.forEach((p_194023_1_) ->
        {
            p_194023_1_.func_194214_a(recipebook);
        });

        if (Minecraft.screen instanceof IRecipeShownListener)
        {
            ((IRecipeShownListener) Minecraft.screen).func_192043_J_();
        }
    }

    public void handleEntityEffect(SPacketEntityEffect packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity instanceof EntityLivingBase)
        {
            Potion potion = Potion.getPotionById(packetIn.getEffectId());

            if (potion != null)
            {
                PotionEffect potioneffect = new PotionEffect(potion, packetIn.getDuration(), packetIn.getAmplifier(), packetIn.getIsAmbient(), packetIn.doesShowParticles());
                potioneffect.setPotionDurationMax(packetIn.isMaxDuration());
                ((EntityLivingBase)entity).addPotionEffect(potioneffect);
            }
        }
    }

    public void handleCombatEvent(SPacketCombatEvent packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.eventType == SPacketCombatEvent.Event.ENTITY_DIED)
        {
            Entity entity = clientWorldController.getEntityByID(packetIn.playerId);

            if (entity == Minecraft.player)
            {

                for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
                    EntityPlayerSP player = ibaritone.getPlayerContext().player();
                    if (player != null && player.connection == this) {
                        ibaritone.getGameEventHandler().onPlayerDeath();
                    }
                }

                Minecraft.openScreen(new DeathScreen(packetIn.deathMessage));
            }
        }
    }

    public void handleServerDifficulty(SPacketServerDifficulty packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.world.getWorldInfo().setDifficulty(packetIn.getDifficulty());
        Minecraft.world.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
    }

    public void handleCamera(SPacketCamera packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity != null)
        {
            gameController.setRenderViewEntity(entity);
        }
    }

    public void handleWorldBorder(SPacketWorldBorder packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        packetIn.apply(clientWorldController.getWorldBorder());
    }

    @SuppressWarnings("incomplete-switch")
    public void handleTitle(SPacketTitle packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        SPacketTitle.Type spackettitle$type = packetIn.getType();
        String s = null;
        String s1 = null;
        String s2 = packetIn.getMessage() != null ? packetIn.getMessage().asFormattedString() : "";

        switch (spackettitle$type)
        {
            case TITLE:
                s = s2;
                break;

            case SUBTITLE:
                s1 = s2;
                break;

            case ACTIONBAR:
                Minecraft.ingameGUI.setActionBar(s2, false);
                return;

            case RESET:
                Minecraft.ingameGUI.displayTitle("", "", -1, -1, -1);
                Minecraft.ingameGUI.setDefaultTitlesTimes();
                return;
        }

        Minecraft.ingameGUI.displayTitle(s, s1, packetIn.getFadeInTime(), packetIn.getDisplayTime(), packetIn.getFadeOutTime());
    }

    public void handlePlayerListHeaderFooter(SPacketPlayerListHeaderFooter packetIn)
    {
        Minecraft.ingameGUI.getTabList().setHeader(packetIn.getHeader().asFormattedString().isEmpty() ? null : packetIn.getHeader());
        Minecraft.ingameGUI.getTabList().setFooter(packetIn.getFooter().asFormattedString().isEmpty() ? null : packetIn.getFooter());
    }

    public void handleRemoveEntityEffect(SPacketRemoveEntityEffect packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = packetIn.getEntity(clientWorldController);

        if (entity instanceof EntityLivingBase)
        {
            ((EntityLivingBase)entity).removeActivePotionEffect(packetIn.getPotion());
        }
    }

    @SuppressWarnings("incomplete-switch")
    public void handlePlayerListItem(SPacketPlayerListItem packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        for (SPacketPlayerListItem.AddPlayerData spacketplayerlistitem$addplayerdata : packetIn.getEntries())
        {
            if (packetIn.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER)
            {
                playerInfoMap.remove(spacketplayerlistitem$addplayerdata.getProfile().getId());
            }
            else
            {
                NetworkPlayerInfo networkplayerinfo = playerInfoMap.get(spacketplayerlistitem$addplayerdata.getProfile().getId());

                if (packetIn.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER)
                {
                    networkplayerinfo = new NetworkPlayerInfo(spacketplayerlistitem$addplayerdata);
                    playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
                }

                if (networkplayerinfo != null)
                {
                    switch (packetIn.getAction())
                    {
                        case ADD_PLAYER:
                            networkplayerinfo.setGameType(spacketplayerlistitem$addplayerdata.getGameMode());
                            networkplayerinfo.setResponseTime(spacketplayerlistitem$addplayerdata.getPing());
                            break;

                        case UPDATE_GAME_MODE:
                            networkplayerinfo.setGameType(spacketplayerlistitem$addplayerdata.getGameMode());
                            break;

                        case UPDATE_LATENCY:
                            networkplayerinfo.setResponseTime(spacketplayerlistitem$addplayerdata.getPing());
                            break;

                        case UPDATE_DISPLAY_NAME:
                            networkplayerinfo.setDisplayName(spacketplayerlistitem$addplayerdata.getDisplayName());
                    }
                }
            }
        }
    }

    public void handleKeepAlive(SPacketKeepAlive packetIn)
    {
        sendPacket(new CPacketKeepAlive(packetIn.getId()));
    }

    public void handlePlayerAbilities(SPacketPlayerAbilities packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        EntityPlayer entityplayer1 = Minecraft.player;
        entityplayer1.capabilities.isFlying = packetIn.isFlying();
        entityplayer1.capabilities.isCreativeMode = packetIn.isCreativeMode();
        entityplayer1.capabilities.disableDamage = packetIn.isInvulnerable();
        entityplayer1.capabilities.allowFlying = packetIn.isAllowFlying();
        entityplayer1.capabilities.setFlySpeed(packetIn.getFlySpeed());
        entityplayer1.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
    }

    /**
     * Displays the available command-completion options the server knows of
     */
    public void handleTabComplete(SPacketTabComplete packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        String[] astring = packetIn.getMatches();
        Arrays.sort(astring);

        if (Minecraft.screen instanceof ITabCompleter)
        {
            ((ITabCompleter) Minecraft.screen).setCompletions(astring);
        }
    }

    public void handleSoundEffect(SPacketSoundEffect packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.world.playSound(Minecraft.player, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getSound(), packetIn.getCategory(), packetIn.getVolume(), packetIn.getPitch());
    }

    public void handleCustomSound(SPacketCustomSound packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.getSoundHandler().playSound(new PositionedSoundRecord(new Namespaced(packetIn.getSoundName()), packetIn.getCategory(), packetIn.getVolume(), packetIn.getPitch(), false, 0, ISound.AttenuationType.LINEAR, (float)packetIn.getX(), (float)packetIn.getY(), (float)packetIn.getZ()));
    }

    public void handleResourcePack(SPacketResourcePackSend packetIn)
    {
        String s = packetIn.getURL();
        String s1 = packetIn.getHash();

        if (validateResourcePackUrl(s))
        {
            if (s.startsWith("level://"))
            {
                try
                {
                    String s2 = URLDecoder.decode(s.substring("level://".length()), StandardCharsets.UTF_8.toString());
                    File file1 = new File(Minecraft.gameDir, "saves");
                    File file2 = new File(file1, s2);

                    if (file2.isFile())
                    {
                        netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                        Futures.addCallback(Minecraft.getResourcePackRepository().setResourcePackInstance(file2), createDownloadCallback(), MoreExecutors.directExecutor());
                        return;
                    }
                }
                catch (UnsupportedEncodingException var7)
                {
                }

                netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
            else
            {
                ServerData serverdata = gameController.getServerData();

                if (serverdata != null && serverdata.getResourceMode() == ServerData.ServerResourceMode.ENABLED)
                {
                    netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                    Futures.addCallback(Minecraft.getResourcePackRepository().downloadResourcePack(s, s1), createDownloadCallback(), MoreExecutors.directExecutor());
                }
                else if (serverdata != null && serverdata.getResourceMode() != ServerData.ServerResourceMode.PROMPT)
                {
                    netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
                }
                else
                {
                    gameController.addScheduledTask(new Runnable()
                    {
                        public void run()
                        {
                            Minecraft.openScreen(new ConfirmScreen(b -> {
                                gameController = Minecraft.getMinecraft();
                                ServerData serverdata1 = gameController.getServerData();

                                if (b)
                                {
                                    if (serverdata1 != null)
                                    {
                                        serverdata1.setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                    }

                                    netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.ACCEPTED));
                                    Futures.addCallback(Minecraft.getResourcePackRepository().downloadResourcePack(s, s1), createDownloadCallback(), MoreExecutors.directExecutor());
                                }
                                else
                                {
                                    if (serverdata1 != null)
                                    {
                                        serverdata1.setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                    }

                                    netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.DECLINED));
                                }

                                ServerList.saveSingleServer(serverdata1);
                                Minecraft.openScreen(null);
                            }, I18n.format("multiplayer.texturePrompt.line1"), I18n.format("multiplayer.texturePrompt.line2")));
                        }
                    });
                }
            }
        }
    }

    private boolean validateResourcePackUrl(String p_189688_1_)
    {
        try
        {
            URI uri = new URI(p_189688_1_);
            String s = uri.getScheme();
            boolean flag = "level".equals(s);

            if (!"http".equals(s) && !"https".equals(s) && !flag)
            {
                throw new URISyntaxException(p_189688_1_, "Wrong protocol");
            }
            else if (!flag || !p_189688_1_.contains("..") && p_189688_1_.endsWith("/resources.zip"))
            {
                return true;
            }
            else
            {
                throw new URISyntaxException(p_189688_1_, "Invalid levelstorage resourcepack path");
            }
        }
        catch (URISyntaxException var5)
        {
            netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            return false;
        }
    }

    private FutureCallback<Object> createDownloadCallback()
    {
        return new FutureCallback<Object>()
        {
            public void onSuccess(@Nullable Object p_onSuccess_1_)
            {
                netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
            }
            public void onFailure(Throwable p_onFailure_1_)
            {
                netManager.sendPacket(new CPacketResourcePackStatus(CPacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        };
    }

    public void handleUpdateEntityNBT(SPacketUpdateBossInfo packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Minecraft.ingameGUI.getBossbarHud().read(packetIn);
    }

    public void handleCooldown(SPacketCooldown packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.getTicks() == 0)
        {
            Minecraft.player.getCooldownTracker().removeCooldown(packetIn.getItem());
        }
        else
        {
            Minecraft.player.getCooldownTracker().setCooldown(packetIn.getItem(), packetIn.getTicks());
        }
    }

    public void handleMoveVehicle(SPacketMoveVehicle packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = Minecraft.player.getLowestRidingEntity();

        if (entity != Minecraft.player && entity.canPassengerSteer())
        {
            entity.setPositionAndRotation(packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getYaw(), packetIn.getPitch());
            netManager.sendPacket(new CPacketVehicleMove(entity));
        }
    }

    /**
     * Handles packets that have room for a channel specification. Vanilla implemented channels are "MC|TrList" to
     * acquire a MerchantRecipeList trades for a villager merchant, "MC|Brand" which sets the server brand? on the
     * player instance and finally "MC|RPack" which the server uses to communicate the identifier of the default server
     * resourcepack for the client to load.
     */
    public void handleCustomPayload(SPacketCustomPayload packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if ("MC|TrList".equals(packetIn.getChannelName()))
        {
            PacketBuffer packetbuffer = packetIn.getBufferData();

            try
            {
                int k = packetbuffer.readInt();
                Screen guiscreen = Minecraft.screen;

                if (guiscreen != null && guiscreen instanceof GuiMerchant && k == Minecraft.player.openContainer.windowId)
                {
                    IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
                    MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
                    imerchant.setRecipes(merchantrecipelist);
                }
            }
            catch (IOException ioexception)
            {
                LOGGER.error("Couldn't load trade info", ioexception);
            }
            finally
            {
                packetbuffer.release();
            }
        }
        else if ("MC|Brand".equals(packetIn.getChannelName()))
        {
            Minecraft.player.setServerBrand(packetIn.getBufferData().readStringFromBuffer(32767));
        }
        else if ("MC|BOpen".equals(packetIn.getChannelName()))
        {
            EnumHand enumhand = packetIn.getBufferData().readEnumValue(EnumHand.class);
            ItemStack itemstack = enumhand == EnumHand.OFF_HAND ? Minecraft.player.getHeldItemOffhand() : Minecraft.player.getHeldItemMainhand();

            if (itemstack.getItem() == Items.WRITTEN_BOOK)
            {
                Minecraft.openScreen(new GuiScreenBook(Minecraft.player, itemstack, false));
            }
        }
        else if ("MC|DebugPath".equals(packetIn.getChannelName()))
        {
            PacketBuffer packetbuffer1 = packetIn.getBufferData();
            int l = packetbuffer1.readInt();
            float f1 = packetbuffer1.readFloat();
            Path path = Path.read(packetbuffer1);
            ((DebugRendererPathfinding) Minecraft.debugRenderer.debugRendererPathfinding).addPath(l, path, f1);
        }
        else if ("MC|DebugNeighborsUpdate".equals(packetIn.getChannelName()))
        {
            PacketBuffer packetbuffer2 = packetIn.getBufferData();
            long i1 = packetbuffer2.readVarLong();
            BlockPos blockpos = packetbuffer2.readBlockPos();
            ((DebugRendererNeighborsUpdate) Minecraft.debugRenderer.field_191557_f).func_191553_a(i1, blockpos);
        }
        else if ("MC|StopSound".equals(packetIn.getChannelName()))
        {
            PacketBuffer packetbuffer3 = packetIn.getBufferData();
            String s = packetbuffer3.readStringFromBuffer(32767);
            String s1 = packetbuffer3.readStringFromBuffer(256);
            Minecraft.getSoundHandler().stop(s1, SoundCategory.getByName(s));
        }
    }

    /**
     * May create a scoreboard objective, remove an objective from the scoreboard or update an objectives' displayname
     */
    public void handleScoreboardObjective(SPacketScoreboardObjective packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Scoreboard scoreboard = clientWorldController.getScoreboard();

        if (packetIn.getAction() == 0)
        {
            ScoreObjective scoreobjective = scoreboard.addScoreObjective(packetIn.getObjectiveName(), IScoreCriteria.DUMMY);
            scoreobjective.setDisplayName(packetIn.getObjectiveValue());
            scoreobjective.setRenderType(packetIn.getRenderType());
        }
        else
        {
            ScoreObjective scoreobjective1 = scoreboard.getObjective(packetIn.getObjectiveName());

            if (packetIn.getAction() == 1)
            {
                scoreboard.removeObjective(scoreobjective1);
            }
            else if (packetIn.getAction() == 2)
            {
                scoreobjective1.setDisplayName(packetIn.getObjectiveValue());
                scoreobjective1.setRenderType(packetIn.getRenderType());
            }
        }
    }

    /**
     * Either updates the score with a specified value or removes the score for an objective
     */
    public void handleUpdateScore(SPacketUpdateScore packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Scoreboard scoreboard = clientWorldController.getScoreboard();
        ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getObjectiveName());

        if (packetIn.getScoreAction() == SPacketUpdateScore.Action.CHANGE)
        {
            Score score = scoreboard.getOrCreateScore(packetIn.getPlayerName(), scoreobjective);
            score.setScorePoints(packetIn.getScoreValue());
        }
        else if (packetIn.getScoreAction() == SPacketUpdateScore.Action.REMOVE)
        {
            if (StringUtils.isNullOrEmpty(packetIn.getObjectiveName()))
            {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), null);
            }
            else if (scoreobjective != null)
            {
                scoreboard.removeObjectiveFromEntity(packetIn.getPlayerName(), scoreobjective);
            }
        }
    }

    /**
     * Removes or sets the ScoreObjective to be displayed at a particular scoreboard position (list, sidebar, below
     * name)
     */
    public void handleDisplayObjective(SPacketDisplayObjective packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Scoreboard scoreboard = clientWorldController.getScoreboard();

        if (packetIn.getName().isEmpty())
        {
            scoreboard.setObjectiveInDisplaySlot(packetIn.getPosition(), null);
        }
        else
        {
            ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.getName());
            scoreboard.setObjectiveInDisplaySlot(packetIn.getPosition(), scoreobjective);
        }
    }

    /**
     * Updates a team managed by the scoreboard: Create/Remove the team registration, Register/Remove the player-team-
     * memberships, Set team displayname/prefix/suffix and/or whether friendly fire is enabled
     */
    public void handleTeams(SPacketTeams packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Scoreboard scoreboard = clientWorldController.getScoreboard();
        ScorePlayerTeam scoreplayerteam;

        if (packetIn.getAction() == 0)
        {
            scoreplayerteam = scoreboard.createTeam(packetIn.getName());
        }
        else
        {
            scoreplayerteam = scoreboard.getTeam(packetIn.getName());
        }

        if (packetIn.getAction() == 0 || packetIn.getAction() == 2)
        {
            scoreplayerteam.setTeamName(packetIn.getDisplayName());
            scoreplayerteam.setNamePrefix(packetIn.getPrefix());
            scoreplayerteam.setNameSuffix(packetIn.getSuffix());
            scoreplayerteam.setChatFormat(Formatting.fromColorIndex(packetIn.getColor()));
            scoreplayerteam.setFriendlyFlags(packetIn.getFriendlyFlags());
            Team.EnumVisible team$enumvisible = Team.EnumVisible.getByName(packetIn.getNameTagVisibility());

            if (team$enumvisible != null)
            {
                scoreplayerteam.setNameTagVisibility(team$enumvisible);
            }

            Team.CollisionRule team$collisionrule = Team.CollisionRule.getByName(packetIn.getCollisionRule());

            if (team$collisionrule != null)
            {
                scoreplayerteam.setCollisionRule(team$collisionrule);
            }
        }

        if (packetIn.getAction() == 0 || packetIn.getAction() == 3)
        {
            for (String s : packetIn.getPlayers())
            {
                scoreboard.addPlayerToTeam(s, packetIn.getName());
            }
        }

        if (packetIn.getAction() == 4)
        {
            for (String s1 : packetIn.getPlayers())
            {
                scoreboard.removePlayerFromTeam(s1, scoreplayerteam);
            }
        }

        if (packetIn.getAction() == 1)
        {
            scoreboard.removeTeam(scoreplayerteam);
        }
    }

    /**
     * Spawns a specified number of particles at the specified location with a randomized displacement according to
     * specified bounds
     */
    public void handleParticles(SPacketParticles packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);

        if (packetIn.getParticleCount() == 0)
        {
            double d0 = packetIn.getParticleSpeed() * packetIn.getXOffset();
            double d2 = packetIn.getParticleSpeed() * packetIn.getYOffset();
            double d4 = packetIn.getParticleSpeed() * packetIn.getZOffset();

            try
            {
                clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
            }
            catch (Throwable var17)
            {
                LOGGER.warn("Could not spawn particle effect {}", packetIn.getParticleType());
            }
        }
        else
        {
            for (int k = 0; k < packetIn.getParticleCount(); ++k)
            {
                double d1 = avRandomizer.nextGaussian() * (double)packetIn.getXOffset();
                double d3 = avRandomizer.nextGaussian() * (double)packetIn.getYOffset();
                double d5 = avRandomizer.nextGaussian() * (double)packetIn.getZOffset();
                double d6 = avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
                double d7 = avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
                double d8 = avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();

                try
                {
                    clientWorldController.spawnParticle(packetIn.getParticleType(), packetIn.isLongDistance(), packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3, packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
                }
                catch (Throwable var16)
                {
                    LOGGER.warn("Could not spawn particle effect {}", packetIn.getParticleType());
                    return;
                }
            }
        }
    }

    /**
     * Updates en entity's attributes and their respective modifiers, which are used for speed bonusses (player
     * sprinting, animals fleeing, baby speed), weapon/tool attackDamage, hostiles followRange randomization, zombie
     * maxHealth and knockback resistance as well as reinforcement spawning chance.
     */
    public void handleEntityProperties(SPacketEntityProperties packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, gameController);
        Entity entity = clientWorldController.getEntityByID(packetIn.getEntityId());

        if (entity != null)
        {
            if (!(entity instanceof EntityLivingBase))
            {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            }
            else
            {
                AbstractAttributeMap abstractattributemap = ((EntityLivingBase)entity).getAttributeMap();

                for (SPacketEntityProperties.Snapshot spacketentityproperties$snapshot : packetIn.getSnapshots())
                {
                    IAttributeInstance iattributeinstance = abstractattributemap.getAttributeInstanceByName(spacketentityproperties$snapshot.getName());

                    if (iattributeinstance == null)
                    {
                        iattributeinstance = abstractattributemap.registerAttribute(new RangedAttribute(null, spacketentityproperties$snapshot.getName(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE));
                    }

                    iattributeinstance.setBaseValue(spacketentityproperties$snapshot.getBaseValue());
                    iattributeinstance.removeAllModifiers();

                    for (AttributeModifier attributemodifier : spacketentityproperties$snapshot.getModifiers())
                    {
                        iattributeinstance.applyModifier(attributemodifier);
                    }
                }
            }
        }
    }

    public void func_194307_a(SPacketPlaceGhostRecipe p_194307_1_)
    {
        PacketThreadUtil.checkThreadAndEnqueue(p_194307_1_, this, gameController);
        Container container = Minecraft.player.openContainer;

        if (container.windowId == p_194307_1_.func_194313_b() && container.getCanCraft(Minecraft.player))
        {
            if (Minecraft.screen instanceof IRecipeShownListener)
            {
                GuiRecipeBook guirecipebook = ((IRecipeShownListener) Minecraft.screen).func_194310_f();
                guirecipebook.func_193951_a(p_194307_1_.func_194311_a(), container.inventorySlots);
            }
        }
    }

    /**
     * Returns this the NetworkManager instance registered with this NetworkHandlerPlayClient
     */
    public NetworkManager getNetworkManager()
    {
        return netManager;
    }

    public Collection<NetworkPlayerInfo> getPlayerInfoMap()
    {
        return playerInfoMap.values();
    }

    public NetworkPlayerInfo getPlayerInfo(UUID uniqueId)
    {
        return playerInfoMap.get(uniqueId);
    }

    @Nullable

    /**
     * Gets the client's description information about another player on the server.
     */
    public NetworkPlayerInfo getPlayerInfo(String name)
    {
        for (NetworkPlayerInfo networkplayerinfo : playerInfoMap.values())
        {
            if (networkplayerinfo.getGameProfile().getName().equals(name))
            {
                return networkplayerinfo;
            }
        }

        return null;
    }

    public GameProfile getGameProfile()
    {
        return profile;
    }

    public ClientAdvancementManager func_191982_f()
    {
        return field_191983_k;
    }
}
