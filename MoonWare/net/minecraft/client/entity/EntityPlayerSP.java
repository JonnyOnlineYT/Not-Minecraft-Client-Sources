package net.minecraft.client.entity;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.ChatEvent;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.SprintStateEvent;
import baritone.api.event.events.type.EventState;
import baritone.behavior.LookBehavior;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ElytraSound;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.Component;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.motion.EventMove;
import org.moonware.client.event.events.impl.packet.EventMessage;
import org.moonware.client.event.events.impl.player.*;
import org.moonware.client.event.types.EventType;
import org.moonware.client.feature.impl.combat.particle.EventMotion;
import org.moonware.client.feature.impl.misc.FreeCam;
import org.moonware.client.feature.impl.misc.PortalFeatures;
import org.moonware.client.feature.impl.player.AntiPush;
import org.moonware.client.feature.impl.player.NoSlowDown;

import javax.annotation.Nullable;
import java.util.List;

public class EntityPlayerSP extends AbstractClientPlayer {
    public final NetHandlerPlayClient connection;
    private final StatisticsManager statWriter;
    private final RecipeBook field_192036_cb;
    private int permissionLevel;

    /**
     * The last X position which was transmitted to the server, used to determine when the X position changes and needs
     * to be re-trasmitted
     */
    private double lastReportedPosX;

    /**
     * The last Y position which was transmitted to the server, used to determine when the Y position changes and needs
     * to be re-transmitted
     */
    private double lastReportedPosY;

    /**
     * The last Z position which was transmitted to the server, used to determine when the Z position changes and needs
     * to be re-transmitted
     */
    private double lastReportedPosZ;

    /**
     * The last yaw value which was transmitted to the server, used to determine when the yaw changes and needs to be
     * re-transmitted
     */
    private float lastReportedYaw;

    /**
     * The last pitch value which was transmitted to the server, used to determine when the pitch changes and needs to
     * be re-transmitted
     */
    private float lastReportedPitch;
    private boolean prevOnGround;

    /**
     * the last sneaking state sent to the server
     */
    public boolean serverSneakState;

    /**
     * the last sprinting state sent to the server
     */
    public boolean serverSprintState;

    /**
     * Reset to 0 every time position is sent to the server, used to send periodic updates every 20 ticks even when the
     * player is not moving.
     */
    private int positionUpdateTicks;
    public boolean hasValidHealth;
    private String serverBrand;
    public MovementInput movementInput;
    protected Minecraft mc;

    /**
     * Used to tell if the player pressed forward twice. If this is at 0 and it's pressed (And they are allowed to
     * sprint, aka enough food on the ground etc) it sets this to 7. If it's pressed and it's greater than 0 enable
     * sprinting.
     */
    protected int sprintToggleTimer;

    /**
     * Ticks left before sprinting is disabled.
     */
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    public int horseJumpPowerCounter;
    public float horseJumpPower;

    /**
     * The amount of time an entity has been in a Portal
     */
    public float timeInPortal;

    /**
     * The amount of time an entity has been in a Portal the previous tick
     */
    public float prevTimeInPortal;
    public boolean handActive;
    private EnumHand activeHand;
    public boolean rowingBoat;
    private boolean autoJumpEnabled = true;
    private int autoJumpTime;
    public boolean wasFallFlying;

    public EntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
        connection = p_i47378_3_;
        statWriter = p_i47378_4_;
        field_192036_cb = p_i47378_5_;
        mc = p_i47378_1_;
        dimension = 0;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float healAmount) {
    }

    public boolean startRiding(Entity entityIn, boolean force) {
        if (!super.startRiding(entityIn, force)) {
            return false;
        } else {
            if (entityIn instanceof EntityMinecart) {
                Minecraft.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart) entityIn));
            }

            if (entityIn instanceof EntityBoat) {
                prevRotationYaw = entityIn.rotationYaw;
                rotationYaw = entityIn.rotationYaw;
                setRotationYawHead(entityIn.rotationYaw);
            }

            return true;
        }
    }

    public void dismountRidingEntity() {
        super.dismountRidingEntity();
        rowingBoat = false;
    }

    /**
     * interpolated look vector
     */
    public Vec3d getLook(float partialTicks) {
        return Entity.getVectorForRotation(rotationPitch, rotationYaw);
    }

    /**
     * Called to update the entity's position/logic.
     */

    private float preYaw;
    private float prePitch;

    public void onUpdate() {
        if (world.isBlockLoaded(new BlockPos(posX, 0.0D, posZ))) {
            EventUpdate eventUpdate = new EventUpdate();
            EventManager.call(eventUpdate);

            IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(this);

            if (baritone != null) {
                baritone.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.PRE));
            }

            super.onUpdate();

            if (isRiding()) {
                connection.sendPacket(new CPacketPlayer.Rotation(rotationYaw, rotationPitch, onGround));
                connection.sendPacket(new CPacketInput(moveStrafing, field_191988_bg, movementInput.jump, movementInput.sneak));
                Entity entity = getLowestRidingEntity();

                if (entity != this && entity.canPassengerSteer()) {
                    connection.sendPacket(new CPacketVehicleMove(entity));
                }
            } else {
                onUpdateWalkingPlayer();
                //  if (!MoonWare.featureManager.getFeatureByClass(FreeCam.class).getState()) {
                rotationYaw = preYaw;
                rotationPitch = prePitch;
                // }
            }
        }
    }

    /**
     * called every tick when the player is on foot. Performs all the things that normally happen during movement.
     */
    private void onUpdateWalkingPlayer() {
        EventMotion motionEvent = new EventMotion(EventType.PRE, posX, posY, posZ, rotationYaw, rotationPitch,
                onGround);
        EventManager.call(motionEvent);

        if (motionEvent.isCancelled())
            return;

        boolean flag = isSprinting();

        EventPlayerState eventPreRotation = new EventPlayerState(posX, posY, posZ, rotationYaw, rotationPitch, onGround);
        EventManager.call(eventPreRotation);

        EventPreMotion eventPre = new EventPreMotion(rotationYaw, rotationPitch, posX, posY, posZ, onGround);
        EventManager.call(eventPre);

        EventPostMotion eventPost = new EventPostMotion(rotationYaw, rotationPitch);

        preYaw = rotationYaw;
        prePitch = rotationPitch;

        rotationYaw = eventPre.getYaw();
        rotationPitch = eventPre.getPitch();

        if (flag != serverSprintState) {
            if (flag) {
                connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SPRINTING));
            } else {
                connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SPRINTING));
            }

            serverSprintState = flag;
        }

        boolean flag1 = isSneaking();

        if (flag1 != serverSneakState) {
            if (flag1) {
                connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SNEAKING));
            }

            serverSneakState = flag1;
        }

        if (isCurrentViewEntity()) {
            double d0 = eventPre.getPosX() - lastReportedPosX;
            double d1 = eventPre.getPosY() - lastReportedPosY;
            double d2 = eventPre.getPosZ() - lastReportedPosZ;
            double d3 = eventPre.getYaw() - lastReportedYaw;
            double d4 = eventPre.getPitch() - lastReportedPitch;
            ++positionUpdateTicks;
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0D || d4 != 0.0D;

            if (isRiding()) {
                connection.sendPacket(new CPacketPlayer.PositionRotation(motionX, -999.0D, motionZ, eventPre.getYaw(), eventPre.getPitch(), eventPre.isOnGround()));
                flag2 = false;
            } else if (flag2 && flag3) {
                connection.sendPacket(new CPacketPlayer.PositionRotation(eventPre.getPosX(), eventPre.getPosY(), eventPre.getPosZ(), eventPre.getYaw(), eventPre.getPitch(), eventPre.isOnGround()));
            } else if (flag2) {
                if (!MoonWare.featureManager.getFeatureByClass(FreeCam.class).getState()) {
                    connection.sendPacket(new CPacketPlayer.Position(eventPre.getPosX(), eventPre.getPosY(), eventPre.getPosZ(), eventPre.isOnGround()));
                }
            } else if (flag3) {
                connection.sendPacket(new CPacketPlayer.Rotation(eventPre.getYaw(), eventPre.getPitch(), eventPre.isOnGround()));
            } else if (prevOnGround != eventPre.isOnGround()) {
                connection.sendPacket(new CPacketPlayer(eventPre.isOnGround()));
            }

            if (flag2) {
                lastReportedPosX = eventPre.getPosX();
                lastReportedPosY = eventPre.getPosY();
                lastReportedPosZ = eventPre.getPosZ();
                positionUpdateTicks = 0;
            }

            if (flag3) {
                lastReportedYaw = eventPre.getYaw();
                lastReportedPitch = eventPre.getPitch();
            }

            prevOnGround = eventPre.isOnGround();
            autoJumpEnabled = Minecraft.gameSettings.autoJump;
        }
        EventPlayerState eventPostRotation = new EventPlayerState();
        EventManager.call(eventPostRotation);
        EventManager.call(eventPost);

        IBaritone baritone1 = BaritoneAPI.getProvider().getBaritoneForPlayer(this);

        if (baritone1 != null) {
            baritone1.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.POST));
        }
    }

    @Nullable

    /**
     * Drop one item out of the currently selected stack if {@code dropAll} is false. If {@code dropItem} is true the
     * entire stack is dropped.
     */
    public EntityItem dropItem(boolean dropAll) {
        CPacketPlayerDigging.Action cpacketplayerdigging$action = dropAll ? CPacketPlayerDigging.Action.DROP_ALL_ITEMS : CPacketPlayerDigging.Action.DROP_ITEM;
        connection.sendPacket(new CPacketPlayerDigging(cpacketplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    protected ItemStack dropItemAndGetStack(EntityItem p_184816_1_) {
        return ItemStack.EMPTY;
    }

    /**
     * Sends a chat message from the player.
     */
    public void sendChatMessage(String message) {

        ChatEvent eventSend = new ChatEvent(message);
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(this);
        if (baritone == null) {
            return;
        }
        baritone.getGameEventHandler().onSendChatMessage(eventSend);
        if (eventSend.isCancelled()) {
            eventSend.cancel();
        }

        EventMessage event = new EventMessage(message);
        EventManager.call(event);

        if (!event.isCancelled() && !eventSend.isCancelled()) {
            connection.sendPacket(new CPacketChatMessage(event.getMessage()));
        }
    }

    public void swingArm(EnumHand hand) {
        super.swingArm(hand);
        connection.sendPacket(new CPacketAnimation(hand));
    }

    public void respawnPlayer() {
        connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
    }

    /**
     * Deals damage to the entity. This will take the armor of the entity into consideration before damaging the health
     * bar.
     */
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!isEntityInvulnerable(damageSrc)) {
            setHealth(getHealth() - damageAmount);
        }
    }

    /**
     * set current crafting inventory back to the 2x2 square
     */
    public void closeScreen() {
        connection.sendPacket(new CPacketCloseWindow(openContainer.windowId));
        closeScreenAndDropStack();
    }

    public void closeScreenAndDropStack() {
        inventory.setItemStack(ItemStack.EMPTY);
        super.closeScreen();
        Minecraft.openScreen(null);
    }

    /**
     * Updates health locally.
     */
    public void setPlayerSPHealth(float health) {
        if (hasValidHealth) {
            float f = getHealth() - health;

            if (f <= 0.0F) {
                setHealth(health);

                if (f < 0.0F) {
                    hurtResistantTime = maxHurtResistantTime / 2;
                }
            } else {
                lastDamage = f;
                setHealth(getHealth());
                hurtResistantTime = maxHurtResistantTime;
                damageEntity(DamageSource.generic, f);
                maxHurtTime = 10;
                hurtTime = maxHurtTime;
            }
        } else {
            setHealth(health);
            hasValidHealth = true;
        }
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(StatBase stat, int amount) {
        if (stat != null) {
            if (stat.isIndependent) {
                super.addStat(stat, amount);
            }
        }
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities() {
        connection.sendPacket(new CPacketPlayerAbilities(capabilities));
    }

    /**
     * returns true if this is an EntityPlayerSP, or the logged in player.
     */
    public boolean isUser() {
        return true;
    }

    protected void sendHorseJump() {
        connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_RIDING_JUMP, MathHelper.floor(getHorseJumpPower() * 100.0F)));
    }

    public void sendHorseInventory() {
        connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.OPEN_INVENTORY));
    }

    /**
     * Sets the brand of the currently connected server. Server brand information is sent over the {@code MC|Brand}
     * plugin channel, and is used to identify modded servers in crash reports.
     */
    public void setServerBrand(String brand) {
        serverBrand = brand;
    }

    /**
     * Gets the brand of the currently connected server. May be null if the server hasn't yet sent brand information.
     * Server brand information is sent over the {@code MC|Brand} plugin channel, and is used to identify modded servers
     * in crash reports.
     */
    public String getServerBrand() {
        return serverBrand;
    }

    public StatisticsManager getStatFileWriter() {
        return statWriter;
    }

    public RecipeBook func_192035_E() {
        return field_192036_cb;
    }

    public void func_193103_a(IRecipe p_193103_1_) {
        if (field_192036_cb.func_194076_e(p_193103_1_)) {
            field_192036_cb.func_194074_f(p_193103_1_);
            connection.sendPacket(new CPacketRecipeInfo(p_193103_1_));
        }
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int p_184839_1_) {
        permissionLevel = p_184839_1_;
    }

    public void addChatComponentMessage(Component chatComponent, boolean p_146105_2_) {
        if (p_146105_2_) {
            Minecraft.ingameGUI.setActionBar(chatComponent, false);
        } else {
            Minecraft.ingameGUI.getChatGUI().printChatMessage(chatComponent);
        }
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {

        EventPush eventPush = new EventPush();
        EventManager.call(eventPush);
        if (eventPush.isCancelled()) {
            return false;
        }

        if (MoonWare.featureManager.getFeatureByClass(AntiPush.class).getState() && AntiPush.blocks.getBoolValue())
            return false;

        if (noClip) {
            return false;
        } else {
            BlockPos blockpos = new BlockPos(x, y, z);
            double d0 = x - (double) blockpos.getX();
            double d1 = z - (double) blockpos.getZ();

            if (!isOpenBlockSpace(blockpos)) {
                int i = -1;
                double d2 = 9999.0D;

                if (isOpenBlockSpace(blockpos.west()) && d0 < d2) {
                    d2 = d0;
                    i = 0;
                }

                if (isOpenBlockSpace(blockpos.east()) && 1.0D - d0 < d2) {
                    d2 = 1.0D - d0;
                    i = 1;
                }

                if (isOpenBlockSpace(blockpos.north()) && d1 < d2) {
                    d2 = d1;
                    i = 4;
                }

                if (isOpenBlockSpace(blockpos.south()) && 1.0D - d1 < d2) {
                    d2 = 1.0D - d1;
                    i = 5;
                }

                float f = 0.1F;

                if (i == 0) {
                    motionX = -0.10000000149011612D;
                }

                if (i == 1) {
                    motionX = 0.10000000149011612D;
                }

                if (i == 4) {
                    motionZ = -0.10000000149011612D;
                }

                if (i == 5) {
                    motionZ = 0.10000000149011612D;
                }
            }

            return false;
        }
    }

    /**
     * Returns true if the block at the given BlockPos and the block above it are NOT full cubes.
     */
    private boolean isOpenBlockSpace(BlockPos pos) {
        return !world.getBlockState(pos).isNormalCube() && !world.getBlockState(pos.up()).isNormalCube();
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        super.setSprinting(sprinting);
        sprintingTicksLeft = 0;
    }

    /**
     * Sets the current XP, total XP, and level number.
     */
    public void setXPStats(float currentXP, int maxXP, int level) {
        experience = currentXP;
        experienceTotal = maxXP;
        experienceLevel = level;
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(Component component) {
        Minecraft.ingameGUI.getChatGUI().printChatMessage(component);
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return permLevel <= getPermissionLevel();
    }

    public void handleStatusUpdate(byte id) {
        if (id >= 24 && id <= 28) {
            setPermissionLevel(id - 24);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition() {
        return new BlockPos(posX + 0.5D, posY + 0.5D, posZ + 0.5D);
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        world.playSound(posX, posY, posZ, soundIn, getSoundCategory(), volume, pitch, false);
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld() {
        return true;
    }

    public void setActiveHand(EnumHand hand) {
        ItemStack itemstack = getHeldItem(hand);

        if (!itemstack.isEmpty() && !isHandActive()) {
            super.setActiveHand(hand);
            handActive = true;
            activeHand = hand;
        }
    }

    public boolean isHandActive() {
        return handActive;
    }

    public void resetActiveHand() {
        super.resetActiveHand();
        handActive = false;
    }

    public EnumHand getActiveHand() {
        return activeHand;
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);

        if (EntityLivingBase.HAND_STATES.equals(key)) {
            boolean flag = (dataManager.get(EntityLivingBase.HAND_STATES).byteValue() & 1) > 0;
            EnumHand enumhand = (dataManager.get(EntityLivingBase.HAND_STATES).byteValue() & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

            if (flag && !handActive) {
                setActiveHand(enumhand);
            } else if (!flag && handActive) {
                resetActiveHand();
            }
        }

        if (Entity.FLAGS.equals(key) && isElytraFlying() && !wasFallFlying) {
            Minecraft.getSoundHandler().playSound(new ElytraSound(this));
        }
    }

    public boolean isRidingHorse() {
        Entity entity = getRidingEntity();
        return isRiding() && entity instanceof IJumpingMount && ((IJumpingMount) entity).canJump();
    }

    public float getHorseJumpPower() {
        return horseJumpPower;
    }

    public void openEditSign(TileEntitySign signTile) {
        Minecraft.openScreen(new GuiEditSign(signTile));
    }

    public void displayGuiEditCommandCart(CommandBlockBaseLogic commandBlock) {
        Minecraft.openScreen(new GuiEditCommandBlockMinecart(commandBlock));
    }

    public void displayGuiCommandBlock(TileEntityCommandBlock commandBlock) {
        Minecraft.openScreen(new GuiCommandBlock(commandBlock));
    }

    public void openEditStructure(TileEntityStructure structure) {
        Minecraft.openScreen(new GuiEditStructure(structure));
    }

    public void openBook(ItemStack stack, EnumHand hand) {
        Item item = stack.getItem();

        if (item == Items.WRITABLE_BOOK) {
            Minecraft.openScreen(new GuiScreenBook(this, stack, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory.
     */
    public void displayGUIChest(IInventory chestInventory) {
        String s = chestInventory instanceof IInteractionObject ? ((IInteractionObject) chestInventory).getGuiID() : "minecraft:container";

        if ("minecraft:chest".equals(s)) {
            Minecraft.openScreen(new GuiChest(inventory, chestInventory));
        } else if ("minecraft:hopper".equals(s)) {
            Minecraft.openScreen(new GuiHopper(inventory, chestInventory));
        } else if ("minecraft:furnace".equals(s)) {
            Minecraft.openScreen(new GuiFurnace(inventory, chestInventory));
        } else if ("minecraft:brewing_stand".equals(s)) {
            Minecraft.openScreen(new GuiBrewingStand(inventory, chestInventory));
        } else if ("minecraft:beacon".equals(s)) {
            Minecraft.openScreen(new GuiBeacon(inventory, chestInventory));
        } else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
            if ("minecraft:shulker_box".equals(s)) {
                Minecraft.openScreen(new GuiShulkerBox(inventory, chestInventory));
            } else {
                Minecraft.openScreen(new GuiChest(inventory, chestInventory));
            }
        } else {
            Minecraft.openScreen(new GuiDispenser(inventory, chestInventory));
        }
    }

    public void openGuiHorseInventory(AbstractHorse horse, IInventory inventoryIn) {
        Minecraft.openScreen(new GuiScreenHorseInventory(inventory, inventoryIn, horse));
    }

    public void displayGui(IInteractionObject guiOwner) {
        String s = guiOwner.getGuiID();

        if ("minecraft:crafting_table".equals(s)) {
            Minecraft.openScreen(new GuiCrafting(inventory, world));
        } else if ("minecraft:enchanting_table".equals(s)) {
            Minecraft.openScreen(new GuiEnchantment(inventory, world, guiOwner));
        } else if ("minecraft:anvil".equals(s)) {
            Minecraft.openScreen(new GuiRepair(inventory, world));
        }
    }

    public void displayVillagerTradeGui(IMerchant villager) {
        Minecraft.openScreen(new GuiMerchant(inventory, villager, world));
    }

    /**
     * Called when the entity is dealt a critical hit.
     */
    public void onCriticalHit(Entity entityHit) {
        Minecraft.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
    }

    public void onEnchantmentCritical(Entity entityHit) {
        Minecraft.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking() {
        boolean flag = movementInput != null && movementInput.sneak;
        return flag && !sleeping;
    }

    public void updateEntityActionState() {
        super.updateEntityActionState();

        if (isCurrentViewEntity()) {
            moveStrafing = movementInput.moveStrafe;
            field_191988_bg = movementInput.moveForward;
            isJumping = movementInput.jump;
            prevRenderArmYaw = renderArmYaw;
            prevRenderArmPitch = renderArmPitch;
            renderArmPitch = (float) ((double) renderArmPitch + (double) (rotationPitch - renderArmPitch) * 0.5D);
            renderArmYaw = (float) ((double) renderArmYaw + (double) (rotationYaw - renderArmYaw) * 0.5D);
        }
    }

    protected boolean isCurrentViewEntity() {
        return Minecraft.getRenderViewEntity() == this;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        ++sprintingTicksLeft;

        if (sprintToggleTimer > 0) {
            --sprintToggleTimer;
        }

        prevTimeInPortal = timeInPortal;

        if (inPortal) {

            if (MoonWare.featureManager.getFeatureByClass(PortalFeatures.class).getState() && PortalFeatures.chat.getBoolValue()) {
            } else if (Minecraft.screen != null && !Minecraft.screen.pauses()) {
                if (Minecraft.screen instanceof GuiContainer) {
                    closeScreen();
                }

                Minecraft.openScreen(null);
            }

            if (timeInPortal == 0.0F) {
                Minecraft.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_PORTAL_TRIGGER, rand.nextFloat() * 0.4F + 0.8F));
            }

            timeInPortal += 0.0125F;

            if (timeInPortal >= 1.0F) {
                timeInPortal = 1.0F;
            }

            inPortal = false;
        } else if (isPotionActive(MobEffects.NAUSEA) && getActivePotionEffect(MobEffects.NAUSEA).getDuration() > 60) {
            timeInPortal += 0.006666667F;

            if (timeInPortal > 1.0F) {
                timeInPortal = 1.0F;
            }
        } else {
            if (timeInPortal > 0.0F) {
                timeInPortal -= 0.05F;
            }

            if (timeInPortal < 0.0F) {
                timeInPortal = 0.0F;
            }
        }

        if (timeUntilPortal > 0) {
            --timeUntilPortal;
        }

        boolean flag = movementInput.jump;
        boolean flag1 = movementInput.sneak;
        float f = 0.8F;
        boolean flag2 = movementInput.moveForward >= 0.8F;
        movementInput.updatePlayerMoveState();

        if (isHandActive() && !isRiding()) {
            movementInput.moveStrafe *= MoonWare.featureManager.getFeatureByClass(NoSlowDown.class).getState() ? NoSlowDown.percentage.getNumberValue() / 100 : 0.2F;
            movementInput.moveForward *= MoonWare.featureManager.getFeatureByClass(NoSlowDown.class).getState() ? NoSlowDown.percentage.getNumberValue() / 100 : 0.2F;
            sprintToggleTimer = 0;
        }

        boolean flag3 = false;

        if (autoJumpTime > 0) {
            --autoJumpTime;
            flag3 = true;
            movementInput.jump = true;
        }

        AxisAlignedBB axisalignedbb = getEntityBoundingBox();
        pushOutOfBlocks(posX - (double) width * 0.35D, axisalignedbb.minY + 0.5D, posZ + (double) width * 0.35D);
        pushOutOfBlocks(posX - (double) width * 0.35D, axisalignedbb.minY + 0.5D, posZ - (double) width * 0.35D);
        pushOutOfBlocks(posX + (double) width * 0.35D, axisalignedbb.minY + 0.5D, posZ - (double) width * 0.35D);
        pushOutOfBlocks(posX + (double) width * 0.35D, axisalignedbb.minY + 0.5D, posZ + (double) width * 0.35D);
        boolean flag4 = (float) getFoodStats().getFoodLevel() > 6.0F || capabilities.allowFlying;

        if (onGround && !flag1 && !flag2 && movementInput.moveForward >= 0.8F && !isSprinting() && flag4 && !isHandActive() && !isPotionActive(MobEffects.BLINDNESS)) {

            IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(this);
            SprintStateEvent event = new SprintStateEvent();
            baritone.getGameEventHandler().onPlayerSprintState(event);

            if (sprintToggleTimer <= 0 && !Minecraft.gameSettings.keyBindSprint.isKeyDown()) {
                sprintToggleTimer = 7;
            } else {
                setSprinting(true);
            }
        }

        if (!isSprinting() && movementInput.moveForward >= 0.8F && flag4 && !isHandActive() && !isPotionActive(MobEffects.BLINDNESS) && Minecraft.gameSettings.keyBindSprint.isKeyDown()) {
            setSprinting(true);
        }

        if (isSprinting() && (movementInput.moveForward < 0.8F || isCollidedHorizontally || !flag4)) {
            setSprinting(false);
        }

        if (capabilities.allowFlying) {
            if (Minecraft.playerController.isSpectatorMode()) {
                if (!capabilities.isFlying) {
                    capabilities.isFlying = true;
                    sendPlayerAbilities();
                }
            } else if (!flag && movementInput.jump && !flag3) {
                if (flyToggleTimer == 0) {
                    flyToggleTimer = 7;
                } else {
                    capabilities.isFlying = !capabilities.isFlying;
                    sendPlayerAbilities();
                    flyToggleTimer = 0;
                }
            }
        }

        if (movementInput.jump && !flag && !onGround && !isElytraFlying() && !capabilities.isFlying) {
            ItemStack itemstack = getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isBroken(itemstack)) {
                connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }

        wasFallFlying = isElytraFlying();

        if (capabilities.isFlying && isCurrentViewEntity()) {
            if (movementInput.sneak) {
                movementInput.moveStrafe = (float) ((double) movementInput.moveStrafe / 0.3D);
                movementInput.moveForward = (float) ((double) movementInput.moveForward / 0.3D);
                motionY -= capabilities.getFlySpeed() * 3.0F;
            }

            if (movementInput.jump) {
                motionY += capabilities.getFlySpeed() * 3.0F;
            }
        }

        if (isRidingHorse()) {
            IJumpingMount ijumpingmount = (IJumpingMount) getRidingEntity();

            if (horseJumpPowerCounter < 0) {
                ++horseJumpPowerCounter;

                if (horseJumpPowerCounter == 0) {
                    horseJumpPower = 0.0F;
                }
            }

            if (flag && !movementInput.jump) {
                horseJumpPowerCounter = -10;
                ijumpingmount.setJumpPower(MathHelper.floor(getHorseJumpPower() * 100.0F));
                sendHorseJump();
            } else if (!flag && movementInput.jump) {
                horseJumpPowerCounter = 0;
                horseJumpPower = 0.0F;
            } else if (flag) {
                ++horseJumpPowerCounter;

                if (horseJumpPowerCounter < 10) {
                    horseJumpPower = (float) horseJumpPowerCounter * 0.1F;
                } else {
                    horseJumpPower = 0.8F + 2.0F / (float) (horseJumpPowerCounter - 9) * 0.1F;
                }
            }
        } else {
            horseJumpPower = 0.0F;
        }

        super.onLivingUpdate();

        if (onGround && capabilities.isFlying && !Minecraft.playerController.isSpectatorMode()) {
            capabilities.isFlying = false;
            sendPlayerAbilities();
        }
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden() {

        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(this);
        if (baritone != null) {
            ((LookBehavior) baritone.getLookBehavior()).pig();
        }

        super.updateRidden();
        rowingBoat = false;

        if (getRidingEntity() instanceof EntityBoat) {
            EntityBoat entityboat = (EntityBoat) getRidingEntity();
            entityboat.updateInputs(movementInput.leftKeyDown, movementInput.rightKeyDown, movementInput.forwardKeyDown, movementInput.backKeyDown);
            rowingBoat |= movementInput.leftKeyDown || movementInput.rightKeyDown || movementInput.forwardKeyDown || movementInput.backKeyDown;
        }
    }

    public boolean isRowingBoat() {
        return rowingBoat;
    }

    @Nullable

    /**
     * Removes the given potion effect from the active potion map and returns it. Does not call cleanup callbacks for
     * the end of the potion effect.
     */
    public PotionEffect removeActivePotionEffect(@Nullable Potion potioneffectin) {
        if (potioneffectin == MobEffects.NAUSEA) {
            prevTimeInPortal = 0.0F;
            timeInPortal = 0.0F;
        }

        return super.removeActivePotionEffect(potioneffectin);
    }

    /**
     * Tries to move the entity towards the specified location.
     */
    public void moveEntity(MoverType x, double p_70091_2_, double p_70091_4_, double p_70091_6_) {
        double d0 = posX;
        double d1 = posZ;
        EventMove eventMove = new EventMove(p_70091_2_, p_70091_4_, p_70091_6_);
        EventManager.call(eventMove);
        super.moveEntity(x, eventMove.getX(), eventMove.getY(), eventMove.getZ());
        updateAutoJump((float) (posX - d0), (float) (posZ - d1));
    }

    public boolean isAutoJumpEnabled() {
        return autoJumpEnabled;
    }

    protected void updateAutoJump(float p_189810_1_, float p_189810_2_) {
        if (isAutoJumpEnabled()) {
            if (autoJumpTime <= 0 && onGround && !isSneaking() && !isRiding()) {
                Vec2f vec2f = movementInput.getMoveVector();

                if (vec2f.x != 0.0F || vec2f.y != 0.0F) {
                    Vec3d vec3d = new Vec3d(posX, getEntityBoundingBox().minY, posZ);
                    double d0 = posX + (double) p_189810_1_;
                    double d1 = posZ + (double) p_189810_2_;
                    Vec3d vec3d1 = new Vec3d(d0, getEntityBoundingBox().minY, d1);
                    Vec3d vec3d2 = new Vec3d(p_189810_1_, 0.0D, p_189810_2_);
                    float f = getAIMoveSpeed();
                    float f1 = (float) vec3d2.lengthSquared();

                    if (f1 <= 0.001F) {
                        float f2 = f * vec2f.x;
                        float f3 = f * vec2f.y;
                        float f4 = MathHelper.sin(rotationYaw * 0.017453292F);
                        float f5 = MathHelper.cos(rotationYaw * 0.017453292F);
                        vec3d2 = new Vec3d(f2 * f5 - f3 * f4, vec3d2.yCoord, f3 * f5 + f2 * f4);
                        f1 = (float) vec3d2.lengthSquared();

                        if (f1 <= 0.001F) {
                            return;
                        }
                    }

                    float f12 = (float) MathHelper.fastInvSqrt(f1);
                    Vec3d vec3d12 = vec3d2.scale(f12);
                    Vec3d vec3d13 = getForward();
                    float f13 = (float) (vec3d13.xCoord * vec3d12.xCoord + vec3d13.zCoord * vec3d12.zCoord);

                    if (f13 >= -0.15F) {
                        BlockPos blockpos = new BlockPos(posX, getEntityBoundingBox().maxY, posZ);
                        IBlockState iblockstate = world.getBlockState(blockpos);

                        if (iblockstate.getCollisionBoundingBox(world, blockpos) == null) {
                            blockpos = blockpos.up();
                            IBlockState iblockstate1 = world.getBlockState(blockpos);

                            if (iblockstate1.getCollisionBoundingBox(world, blockpos) == null) {
                                float f6 = 7.0F;
                                float f7 = 1.2F;

                                if (isPotionActive(MobEffects.JUMP_BOOST)) {
                                    f7 += (float) (getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.75F;
                                }

                                float f8 = Math.max(f * 7.0F, 1.0F / f12);
                                Vec3d vec3d4 = vec3d1.add(vec3d12.scale(f8));
                                float f9 = width;
                                float f10 = height;
                                AxisAlignedBB axisalignedbb = (new AxisAlignedBB(vec3d, vec3d4.addVector(0.0D, f10, 0.0D))).expand(f9, 0.0D, f9);
                                Vec3d lvt_19_1_ = vec3d.addVector(0.0D, 0.5099999904632568D, 0.0D);
                                vec3d4 = vec3d4.addVector(0.0D, 0.5099999904632568D, 0.0D);
                                Vec3d vec3d5 = vec3d12.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D));
                                Vec3d vec3d6 = vec3d5.scale(f9 * 0.5F);
                                Vec3d vec3d7 = lvt_19_1_.subtract(vec3d6);
                                Vec3d vec3d8 = vec3d4.subtract(vec3d6);
                                Vec3d vec3d9 = lvt_19_1_.add(vec3d6);
                                Vec3d vec3d10 = vec3d4.add(vec3d6);
                                List<AxisAlignedBB> list = world.getCollisionBoxes(this, axisalignedbb);

                                if (!list.isEmpty()) {
                                }

                                float f11 = Float.MIN_VALUE;
                                label86:

                                for (AxisAlignedBB axisalignedbb2 : list) {
                                    if (axisalignedbb2.intersects(vec3d7, vec3d8) || axisalignedbb2.intersects(vec3d9, vec3d10)) {
                                        f11 = (float) axisalignedbb2.maxY;
                                        Vec3d vec3d11 = axisalignedbb2.getCenter();
                                        BlockPos blockpos1 = new BlockPos(vec3d11);
                                        int i = 1;

                                        while (true) {
                                            if ((float) i >= f7) {
                                                break label86;
                                            }

                                            BlockPos blockpos2 = blockpos1.up(i);
                                            IBlockState iblockstate2 = world.getBlockState(blockpos2);
                                            AxisAlignedBB axisalignedbb1;

                                            if ((axisalignedbb1 = iblockstate2.getCollisionBoundingBox(world, blockpos2)) != null) {
                                                f11 = (float) axisalignedbb1.maxY + (float) blockpos2.getY();

                                                if ((double) f11 - getEntityBoundingBox().minY > (double) f7) {
                                                    return;
                                                }
                                            }

                                            if (i > 1) {
                                                blockpos = blockpos.up();
                                                IBlockState iblockstate3 = world.getBlockState(blockpos);

                                                if (iblockstate3.getCollisionBoundingBox(world, blockpos) != null) {
                                                    return;
                                                }
                                            }

                                            ++i;
                                        }
                                    }
                                }

                                if (f11 != Float.MIN_VALUE) {
                                    float f14 = (float) ((double) f11 - getEntityBoundingBox().minY);

                                    if (f14 > 0.5F && f14 <= f7) {
                                        autoJumpTime = 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
