package net.minecraft.client.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import splash.Splash;
import splash.client.events.chat.ChatEvent;
import splash.client.events.player.EventBlockPush;
import splash.client.events.player.EventLivingUpdate;
import splash.client.events.player.EventMove;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventPostMotionUpdate;
import splash.client.events.player.EventRenderInsideBlock;
import splash.client.events.player.EventSlow;
import splash.client.events.player.EventUpdate;
import splash.utilities.system.ClientLogger;
import me.hippo.systems.lwjeb.event.Stage;

public class EntityPlayerSP extends AbstractClientPlayer {
	public final NetHandlerPlayClient sendQueue;
	private final StatFileWriter statWriter;

	/**
	 * The last X position which was transmitted to the server, used to
	 * determine when the X position changes and needs to be re-trasmitted
	 */
	private double lastReportedPosX;

	/**
	 * The last Y position which was transmitted to the server, used to
	 * determine when the Y position changes and needs to be re-transmitted
	 */
	private double lastReportedPosY;

	/**
	 * The last Z position which was transmitted to the server, used to
	 * determine when the Z position changes and needs to be re-transmitted
	 */
	private double lastReportedPosZ;

	/**
	 * The last yaw value which was transmitted to the server, used to determine
	 * when the yaw changes and needs to be re-transmitted
	 */
	private float lastReportedYaw;

	/**
	 * The last pitch value which was transmitted to the server, used to
	 * determine when the pitch changes and needs to be re-transmitted
	 */
	private float lastReportedPitch;

	/** the last sneaking state sent to the server */
	private boolean serverSneakState;

	/** the last sprinting state sent to the server */
	private boolean serverSprintState;

	/**
	 * Reset to 0 every time position is sent to the server, used to send
	 * periodic updates every 20 ticks even when the player is not moving.
	 */
	private int positionUpdateTicks;
	private boolean hasValidHealth;
	private String clientBrand;
	public MovementInput movementInput;
	protected Minecraft mc;

	/**
	 * Used to tell if the player pressed forward twice. If this is at 0 and
	 * it's pressed (And they are allowed to sprint, aka enough food on the
	 * ground etc) it sets this to 7. If it's pressed and it's greater than 0
	 * enable sprinting.
	 */
	protected int sprintToggleTimer;

	/** Ticks left before sprinting is disabled. */
	public int sprintingTicksLeft;
	public float renderArmYaw;
	public float renderArmPitch;
	public float prevRenderArmYaw;
	public float prevRenderArmPitch;
	private int horseJumpPowerCounter;
	private float horseJumpPower;

	/** The amount of time an entity has been in a Portal */
	public float timeInPortal;

	/** The amount of time an entity has been in a Portal the previous tick */
	public float prevTimeInPortal;
	
	public float serverSideYaw;
	public float serverSidePitch;
	public boolean fuckUpTheClient;
	
	public EntityPlayerSP(Minecraft mcIn, World worldIn, NetHandlerPlayClient netHandler, StatFileWriter statFile) {
		super(worldIn, netHandler.getGameProfile());
		this.sendQueue = netHandler;
		this.statWriter = statFile;
		this.mc = mcIn;
		this.dimension = 0;
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

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	public void mountEntity(Entity entityIn) {
		super.mountEntity(entityIn);

		if (entityIn instanceof EntityMinecart) {
			this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart) entityIn));
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	public void onUpdate() {
		if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
			EventUpdate eventUpdate = new EventUpdate();
			Splash.getInstance().getEventBus().publish(eventUpdate);
			super.onUpdate();

			if (this.isRiding()) {
				this.sendQueue.addToSendQueue(
						new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch, this.onGround));
				this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing, this.moveForward,
						this.movementInput.jump, this.movementInput.sneak));
			} else {
				this.onUpdateWalkingPlayer();

				EventPostMotionUpdate eventPostMotionUpdate = new EventPostMotionUpdate(this.posX, this.posY, this.posZ,
						this.onGround);
				
				Splash.getInstance().getEventBus().publish(eventPostMotionUpdate);
			}
		}
	}

	/**
	 * called every tick when the player is on foot. Performs all the things
	 * that normally happen during movement.
	 */
	public void onUpdateWalkingPlayer() {
		
		EventPlayerUpdate pre = new EventPlayerUpdate(posX, getEntityBoundingBox().minY, posZ, rotationYaw, rotationPitch, onGround);
		
		pre.setStage(Stage.PRE);
		
		Splash.INSTANCE.getEventBus().publish(pre);//
		serverSideYaw = pre.getYaw();
		serverSidePitch = pre.getPitch();

        if (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0) {
        	rotationYawHead = serverSideYaw;
        }
		boolean flag = this.isSprinting();

		if (flag != this.serverSprintState) {
			if (flag) {
				this.sendQueue
						.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SPRINTING));
			} else {
				this.sendQueue
						.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SPRINTING));
			}

			this.serverSprintState = flag;
		}

		boolean flag1 = this.isSneaking();

		if (flag1 != this.serverSneakState) {
			if (flag1) {
				this.sendQueue
						.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.START_SNEAKING));
			} else {
				this.sendQueue
						.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.STOP_SNEAKING));
			}

			this.serverSneakState = flag1;
		}

		if (this.isCurrentViewEntity()) {
			double d0 = pre.getX() - this.lastReportedPosX;
			double d1 = pre.getY() - this.lastReportedPosY;
			double d2 = pre.getZ() - this.lastReportedPosZ;
			double d3 = (double) (pre.getYaw() - pre.getLastYaw());
			double d4 = (double) (pre.getPitch() - pre.getLastPitch());
			boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
			boolean flag3 = d3 != 0.0D || d4 != 0.0D;
            double y = pre.getY();
            float yaw = pre.getYaw();
            float pitch = pre.getPitch();
            boolean onGround = pre.getGroundState();
            double posX = pre.getX();
            double posY = pre.getY();
            double posZ = pre.getZ();
			if (this.ridingEntity == null) {
				if (flag2 && flag3) {
                    sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(posX, y, posZ, yaw, pitch, onGround));
				} else if (flag2) {
                    sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, onGround));
				} else if (flag3) {
                    sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(yaw, pitch, onGround));
				} else {
                    sendQueue.addToSendQueue(new C03PacketPlayer(onGround));
				}
			} else {
				this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D,
						this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
				flag2 = false;
			}

			++this.positionUpdateTicks;

			if (flag2) {
				this.lastReportedPosX = posX;
				this.lastReportedPosY = posY;
				this.lastReportedPosZ = posZ;
				this.positionUpdateTicks = 0;
			}

			if (flag3) {
				pre.setLastYaw(lastReportedYaw = yaw);
				pre.setLastPitch(lastReportedPitch = pitch);
			}
		}
		
		pre.setStage(Stage.POST);
		Splash.INSTANCE.getEventBus().publish(pre);
	}
	
	
	@Override
	public void moveEntity(double x, double y, double z) {
		EventMove eventMove = new EventMove(x, y, z, this.onGround);

        Splash.getInstance().getEventBus().publish(eventMove);
        
        if (!eventMove.isCancellled())
        	 super.moveEntity(eventMove.getX(), eventMove.getY(), eventMove.getZ());
	}
	

	/**
	 * Called when player presses the drop item key
	 */
	public EntityItem dropOneItem(boolean dropAll) {
		C07PacketPlayerDigging.Action c07packetplayerdigging$action = dropAll
				? C07PacketPlayerDigging.Action.DROP_ALL_ITEMS : C07PacketPlayerDigging.Action.DROP_ITEM;
		this.sendQueue.addToSendQueue(
				new C07PacketPlayerDigging(c07packetplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
		return null;
	}

	/**
	 * Joins the passed in entity item with the world. Args: entityItem
	 */
	protected void joinEntityItemWithWorld(EntityItem itemIn) {
	}

	/**
	 * Sends a chat message from the player. Args: chatMessage
	 */
	public void sendChatMessage(String message) {
		ChatEvent chatEvent = new ChatEvent(message);
		Splash.getInstance().getEventBus().publish(chatEvent);

		if (chatEvent.getChatMessage().startsWith(".")) {

		} else {
			this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
		}
	}

	/**
	 * Swings the item the player is holding.
	 */
	public void swingItem() {
		super.swingItem();
		this.sendQueue.addToSendQueue(new C0APacketAnimation());
	}

	public void respawnPlayer() {
		this.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
	}

	/**
	 * Deals damage to the entity. If its a EntityPlayer then will take damage
	 * from the armor first and then health second with the reduced value. Args:
	 * damageAmount
	 */
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {
		if (!this.isEntityInvulnerable(damageSrc)) {
			this.setHealth(this.getHealth() - damageAmount);
		}
	}

	/**
	 * set current crafting inventory back to the 2x2 square
	 */
	public void closeScreen() {
		this.sendQueue.addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
		this.closeScreenAndDropStack();
	}

	public void closeScreenAndDropStack() {
		this.inventory.setItemStack((ItemStack) null);
		super.closeScreen();
		this.mc.displayGuiScreen((GuiScreen) null);
	}

	/**
	 * Updates health locally.
	 */
	public void setPlayerSPHealth(float health) {
		if (this.hasValidHealth) {
			float f = this.getHealth() - health;

			if (f <= 0.0F) {
				this.setHealth(health);

				if (f < 0.0F) {
					this.hurtResistantTime = this.maxHurtResistantTime / 2;
				}
			} else {
				this.lastDamage = f;
				this.setHealth(this.getHealth());
				this.hurtResistantTime = this.maxHurtResistantTime;
				this.damageEntity(DamageSource.generic, f);
				this.hurtTime = this.maxHurtTime = 10;
			}
		} else {
			this.setHealth(health);
			this.hasValidHealth = true;
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
		this.sendQueue.addToSendQueue(new C13PacketPlayerAbilities(this.capabilities));
	}

	/**
	 * returns true if this is an EntityPlayerSP, or the logged in player.
	 */
	public boolean isUser() {
		return true;
	}

	protected void sendHorseJump() {
		this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.RIDING_JUMP,
				(int) (this.getHorseJumpPower() * 100.0F)));
	}

	public void sendHorseInventory() {
		this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this, C0BPacketEntityAction.Action.OPEN_INVENTORY));
	}

	public void setClientBrand(String brand) {
		this.clientBrand = brand;
	}

	public String getClientBrand() {
		return this.clientBrand;
	}

	public StatFileWriter getStatFileWriter() {
		return this.statWriter;
	}
    public boolean isEntityInsideOpaqueBlock() {
    	EventRenderInsideBlock e = new EventRenderInsideBlock();
        Splash.getInstance().getEventBus().publish(e);
        if (e.isCancellled()) {
            return false;
        }
        return super.isEntityInsideOpaqueBlock();
    }
	public void addChatComponentMessage(IChatComponent chatComponent) {
		this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
	}

	protected boolean pushOutOfBlocks(double x, double y, double z) {
		EventBlockPush blockPushEvent = new EventBlockPush();
        Splash.getInstance().getEventBus().publish(blockPushEvent);

        if (blockPushEvent.isCancellled())
            return false;

		if (this.noClip) {
			return false;
		} else {
			BlockPos blockpos = new BlockPos(x, y, z);
			double d0 = x - (double) blockpos.getX();
			double d1 = z - (double) blockpos.getZ();

			if (!this.isOpenBlockSpace(blockpos)) {
				int i = -1;
				double d2 = 9999.0D;

				if (this.isOpenBlockSpace(blockpos.west()) && d0 < d2) {
					d2 = d0;
					i = 0;
				}

				if (this.isOpenBlockSpace(blockpos.east()) && 1.0D - d0 < d2) {
					d2 = 1.0D - d0;
					i = 1;
				}

				if (this.isOpenBlockSpace(blockpos.north()) && d1 < d2) {
					d2 = d1;
					i = 4;
				}

				if (this.isOpenBlockSpace(blockpos.south()) && 1.0D - d1 < d2) {
					d2 = 1.0D - d1;
					i = 5;
				}

				float f = 0.1F;

				if (i == 0) {
					this.motionX = (double) (-f);
				}

				if (i == 1) {
					this.motionX = (double) f;
				}

				if (i == 4) {
					this.motionZ = (double) (-f);
				}

				if (i == 5) {
					this.motionZ = (double) f;
				}
			}

			return false;
		}
	}

	/**
	 * Returns true if the block at the given BlockPos and the block above it
	 * are NOT full cubes.
	 */
	private boolean isOpenBlockSpace(BlockPos pos) {
		return !this.worldObj.getBlockState(pos).getBlock().isNormalCube()
				&& !this.worldObj.getBlockState(pos.up()).getBlock().isNormalCube();
	}

	/**
	 * Set sprinting switch for Entity.
	 */
	public void setSprinting(boolean sprinting) {
		super.setSprinting(sprinting);
		this.sprintingTicksLeft = sprinting ? 600 : 0;
	}

	/**
	 * Sets the current XP, total XP, and level number.
	 */
	public void setXPStats(float currentXP, int maxXP, int level) {
		this.experience = currentXP;
		this.experienceTotal = maxXP;
		this.experienceLevel = level;
	}

	/**
	 * Send a chat message to the CommandSender
	 */
	public void addChatMessage(IChatComponent component) {
		this.mc.ingameGUI.getChatGUI().printChatMessage(component);
	}

	/**
	 * Returns {@code true} if the CommandSender is allowed to execute the
	 * command, {@code false} if not
	 */
	public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
		return permLevel <= 0;
	}

	/**
	 * Get the position in the world. <b>{@code null} is not allowed!</b> If you
	 * are not an entity in the world, return the coordinates 0, 0, 0
	 */
	public BlockPos getPosition() {
		return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
	}

	public void playSound(String name, float volume, float pitch) {
		this.worldObj.playSound(this.posX, this.posY, this.posZ, name, volume, pitch, false);
	}

	/**
	 * Returns whether the entity is in a server world
	 */
	public boolean isServerWorld() {
		return true;
	}

	public boolean isRidingHorse() {
		return this.ridingEntity != null && this.ridingEntity instanceof EntityHorse
				&& ((EntityHorse) this.ridingEntity).isHorseSaddled();
	}

	public float getHorseJumpPower() {
		return this.horseJumpPower;
	}

	public void openEditSign(TileEntitySign signTile) {
		this.mc.displayGuiScreen(new GuiEditSign(signTile));
	}

	public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic) {
		this.mc.displayGuiScreen(new GuiCommandBlock(cmdBlockLogic));
	}

	/**
	 * Displays the GUI for interacting with a book.
	 */
	public void displayGUIBook(ItemStack bookStack) {
		Item item = bookStack.getItem();

		if (item == Items.writable_book) {
			this.mc.displayGuiScreen(new GuiScreenBook(this, bookStack, true));
		}
	}

	/**
	 * Displays the GUI for interacting with a chest inventory. Args:
	 * chestInventory
	 */
	public void displayGUIChest(IInventory chestInventory) {
		String s = chestInventory instanceof IInteractionObject ? ((IInteractionObject) chestInventory).getGuiID()
				: "minecraft:container";

		if ("minecraft:chest".equals(s)) {
			this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
		} else if ("minecraft:hopper".equals(s)) {
			this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
		} else if ("minecraft:furnace".equals(s)) {
			this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
		} else if ("minecraft:brewing_stand".equals(s)) {
			this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
		} else if ("minecraft:beacon".equals(s)) {
			this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
		} else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
			this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
		} else {
			this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
		}
	}

	public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
		this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, horseInventory, horse));
	}

	public void displayGui(IInteractionObject guiOwner) {
		String s = guiOwner.getGuiID();

		if ("minecraft:crafting_table".equals(s)) {
			this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.worldObj));
		} else if ("minecraft:enchanting_table".equals(s)) {
			this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.worldObj, guiOwner));
		} else if ("minecraft:anvil".equals(s)) {
			this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.worldObj));
		}
	}

	public void displayVillagerTradeGui(IMerchant villager) {
		this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.worldObj));
	}

	/**
	 * Called when the player performs a critical hit on the Entity. Args:
	 * entity that was hit critically
	 */
	public void onCriticalHit(Entity entityHit) {
		this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
	}

	public void onEnchantmentCritical(Entity entityHit) {
		this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
	}

	/**
	 * Returns if this entity is sneaking.
	 */
	public boolean isSneaking() {
		boolean flag = this.movementInput != null ? this.movementInput.sneak : false;
		return flag && !this.sleeping;
	}

	public void updateEntityActionState() {
		super.updateEntityActionState();

		if (this.isCurrentViewEntity()) {
			this.moveStrafing = this.movementInput.moveStrafe;
			this.moveForward = this.movementInput.moveForward;
			this.isJumping = this.movementInput.jump;
			this.prevRenderArmYaw = this.renderArmYaw;
			this.prevRenderArmPitch = this.renderArmPitch;
			this.renderArmPitch = (float) ((double) this.renderArmPitch
					+ (double) (this.rotationPitch - this.renderArmPitch) * 0.5D);
			this.renderArmYaw = (float) ((double) this.renderArmYaw
					+ (double) (this.rotationYaw - this.renderArmYaw) * 0.5D);
		}
	}

	protected boolean isCurrentViewEntity() {
		return this.mc.getRenderViewEntity() == this;
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	public void onLivingUpdate() {
		EventLivingUpdate eventLivingUpdate = new EventLivingUpdate();
		Splash.getInstance().getEventBus().publish(eventLivingUpdate);

		if (this.sprintingTicksLeft > 0) {
			--this.sprintingTicksLeft;

			if (this.sprintingTicksLeft == 0) {
				this.setSprinting(false);
			}
		}

		if (this.sprintToggleTimer > 0) {
			--this.sprintToggleTimer;
		}

		this.prevTimeInPortal = this.timeInPortal;

		if (this.inPortal) {
			if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
				this.mc.displayGuiScreen((GuiScreen) null);
			}

			if (this.timeInPortal == 0.0F) {
				this.mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("portal.trigger"),
						this.rand.nextFloat() * 0.4F + 0.8F));
			}

			this.timeInPortal += 0.0125F;

			if (this.timeInPortal >= 1.0F) {
				this.timeInPortal = 1.0F;
			}

			this.inPortal = false;
		} else if (this.isPotionActive(Potion.confusion)
				&& this.getActivePotionEffect(Potion.confusion).getDuration() > 60) {
			this.timeInPortal += 0.006666667F;

			if (this.timeInPortal > 1.0F) {
				this.timeInPortal = 1.0F;
			}
		} else {
			if (this.timeInPortal > 0.0F) {
				this.timeInPortal -= 0.05F;
			}

			if (this.timeInPortal < 0.0F) {
				this.timeInPortal = 0.0F;
			}
		}

		if (this.timeUntilPortal > 0) {
			--this.timeUntilPortal;
		}

		boolean flag = this.movementInput.jump;
		boolean flag1 = this.movementInput.sneak;
		float f = 0.8F;
		boolean flag2 = this.movementInput.moveForward >= f;
		this.movementInput.updatePlayerMoveState();

		EventSlow slowdown = new EventSlow();
		Splash.INSTANCE.getEventBus().publish(slowdown);

		if (this.isUsingItem() && !this.isRiding() && !slowdown.isCancellled()) {
			this.movementInput.moveStrafe *= 0.2F;
			this.movementInput.moveForward *= 0.2F;
			this.sprintToggleTimer = 0;
		}

		this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D,
				this.posZ + (double) this.width * 0.35D);
		this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D,
				this.posZ - (double) this.width * 0.35D);
		this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D,
				this.posZ - (double) this.width * 0.35D);
		this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, this.getEntityBoundingBox().minY + 0.5D,
				this.posZ + (double) this.width * 0.35D);
		boolean flag3 = (float) this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

		if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= f && !this.isSprinting() && flag3
				&& !this.isUsingItem() && !this.isPotionActive(Potion.blindness)) {
			if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
				this.sprintToggleTimer = 7;
			} else {
				this.setSprinting(true);
			}
		}

		if (!this.isSprinting() && this.movementInput.moveForward >= f && flag3 && !this.isUsingItem()
				&& !this.isPotionActive(Potion.blindness) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
			this.setSprinting(true);
		}

		if (this.isSprinting() && (this.movementInput.moveForward < f || this.isCollidedHorizontally || !flag3) && !slowdown.isCancellled()) {
			this.setSprinting(false);
		}

		if (this.capabilities.allowFlying) {
			if (this.mc.playerController.isSpectatorMode()) {
				if (!this.capabilities.isFlying) {
					this.capabilities.isFlying = true;
					this.sendPlayerAbilities();
				}
			} else if (!flag && this.movementInput.jump) {
				if (this.flyToggleTimer == 0) {
					this.flyToggleTimer = 7;
				} else {
					this.capabilities.isFlying = !this.capabilities.isFlying;
					this.sendPlayerAbilities();
					this.flyToggleTimer = 0;
				}
			}
		}

		if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
			if (this.movementInput.sneak) {
				this.motionY -= (double) (this.capabilities.getFlySpeed() * 3.0F);
			}

			if (this.movementInput.jump) {
				this.motionY += (double) (this.capabilities.getFlySpeed() * 3.0F);
			}
		}

		if (this.isRidingHorse()) {
			if (this.horseJumpPowerCounter < 0) {
				++this.horseJumpPowerCounter;

				if (this.horseJumpPowerCounter == 0) {
					this.horseJumpPower = 0.0F;
				}
			}

			if (flag && !this.movementInput.jump) {
				this.horseJumpPowerCounter = -10;
				this.sendHorseJump();
			} else if (!flag && this.movementInput.jump) {
				this.horseJumpPowerCounter = 0;
				this.horseJumpPower = 0.0F;
			} else if (flag) {
				++this.horseJumpPowerCounter;

				if (this.horseJumpPowerCounter < 10) {
					this.horseJumpPower = (float) this.horseJumpPowerCounter * 0.1F;
				} else {
					this.horseJumpPower = 0.8F + 2.0F / (float) (this.horseJumpPowerCounter - 9) * 0.1F;
				}
			}
		} else {
			this.horseJumpPower = 0.0F;
		}

		super.onLivingUpdate();

		if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
			this.capabilities.isFlying = false;
			this.sendPlayerAbilities();
		}
	}

	public void setMoveSpeedAris(EventMove event, double speed) {

		/*
		 * Okay first off, lemme get one goddamn thing straight, everyone says
		 * ``OH U SKID ARIS SPEED CODE`` but yet again, EVERYONE FUCKING USES IT
		 *
		 * If you can name 1 person with a hypixel damage fly/bhop or a
		 * nocheatplus bhop faster than 1.2% vanilla bhopping that isnt using
		 * aris speed code, id be genuinly impressed
		 */

		double forward = Minecraft.getMinecraft().thePlayer.movementInput.moveForward,
				strafe = Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe,
				yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
		if (forward == 0.0 && strafe == 0.0) {

		} else {
			if (forward != 0) {
				if (strafe > 0) {
					yaw += forward > 0 ? -45 : 45;
				} else if (strafe < 0) {
					yaw += forward > 0 ? 45 : -45;
				}
				strafe = 0;
				if (forward > 0) {
					forward = 1;
				} else if (forward < 0) {
					forward = -1;
				}
			}
			event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 88.0F))
					+ strafe * speed * Math.sin(Math.toRadians(yaw + 87.9F)));
			event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 88.0F))
					- strafe * speed * Math.cos(Math.toRadians(yaw + 87.9F)));

		}
	}

	public void setSpeed(double speed) {
		boolean isMovingStraight;
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		double yaw = player.rotationYaw;
		boolean isMoving = (player.moveForward != 0.0f) || (player.moveStrafing != 0.0f);
		boolean isMovingForward = player.moveForward > 0.0f;
		boolean isMovingBackward = player.moveForward < 0.0f;
		boolean isMovingRight = player.moveStrafing > 0.0f;
		boolean isMovingLeft = player.moveStrafing < 0.0f;
		boolean isMovingSideways = isMovingLeft || isMovingRight;
		isMovingStraight = isMovingForward || isMovingBackward;
		if (isMoving) {
			if (isMovingForward && !isMovingSideways) {
				yaw += 0.0;
			} else if (isMovingBackward && !isMovingSideways) {
				yaw += 180.0;
			} else if (isMovingForward && isMovingLeft) {
				yaw += 45.0;
			} else if (isMovingForward) {
				yaw -= 45.0;
			} else if (!isMovingStraight && isMovingLeft) {
				yaw += 90.0;
			} else if (!isMovingStraight && isMovingRight) {
				yaw -= 90.0;
			} else if (isMovingBackward && isMovingLeft) {
				yaw += 126.0;
			} else if (isMovingBackward) {
				yaw -= 126.0;
			}
			yaw = Math.toRadians(yaw);
			player.motionX = isMoving ? (-Math.sin(yaw)) * speed : 0;
			player.motionZ = isMoving ? Math.cos(yaw) * speed : 0;
		}
	}

	  public boolean isMoving() {
        return (moveForward != 0.0f) || (moveStrafing != 0.0f);
    }
	  
	 public void setStill() {
		 mc.thePlayer.motionX = 0;
		 mc.thePlayer.motionY = 0;
		 mc.thePlayer.motionZ = 0;
	 }

	public boolean isMovingOnGround() {
		return this.isMoving() && this.onGround;
	}

    public float getDirection() {
        float direction = this.rotationYaw;
        if (this.moveForward < 0.0F)
            direction += 180.0F;
        float forward = 1.0F;
        if (this.moveForward < 0.0F)
            forward = -0.5F;
        else if (this.moveForward > 0.0F)
            forward = 0.5F;
        if (this.moveStrafing > 0.0F)
            direction -= 90.0F * forward;
        else if (this.moveStrafing < 0.0F)
            direction += 90.0F * forward;
        direction *= 0.017453292F;
        return direction;
    }
    
    public double getSpeed() {
    	return Math.sqrt((motionX * 2) + (motionZ * 2));
    }
    
    public boolean checkBlockAbove(float offset) {
        boolean liquid = false;
        final int y = (int) (getEntityBoundingBox().minY + 2);
        for (int x = MathHelper.floor_double(getEntityBoundingBox().minX); x < MathHelper
                .floor_double(getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(getEntityBoundingBox().minZ); z < MathHelper
                    .floor_double(getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (!(block instanceof BlockAir)) {
                    liquid = true;
                }
            }
        }
        return liquid;
    }

	public void reallyFuckItUp() {
		mc.timer.timerSpeed = 1337;
	}
}
