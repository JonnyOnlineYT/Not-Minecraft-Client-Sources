package volcano.summer.client.modules.combat.aura;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventPacketRecieve;
import volcano.summer.client.events.EventPacketSend;
import volcano.summer.client.events.EventPostMotionUpdate;
import volcano.summer.client.events.EventPreMotionUpdate;
import volcano.summer.client.events.EventRender2D;
import volcano.summer.client.events.EventRender3D;
import volcano.summer.client.events.EventUpdate;
import volcano.summer.client.modules.combat.AntiBot;
import volcano.summer.client.modules.combat.Criticals;
import volcano.summer.client.modules.combat.aura.modes.Single;
import volcano.summer.client.modules.combat.aura.modes.Switch;
import volcano.summer.client.modules.combat.aura.modes.Tick;
import volcano.summer.client.modules.movement.Speed;
import volcano.summer.client.util.R3DUtils;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class KillAura extends Module {
	public static KillAura CLASS;
	public Value<Boolean> AutoBlock = new Value<Boolean>("AutoBlock", "autoblock", true, this);
	public Value<Boolean> keepsprint = new Value<Boolean>("KeepSprint", "keepsprint", true, this);
	public Value<Boolean> walls = new Value<Boolean>("Walls", "walls", true, this);
	public Value<Boolean> ESP = new Value<Boolean>("ESP", "esp", false, this);
	public Value<Boolean> AutoDisable = new Value<Boolean>("AutoDisable", "autodisable", true, this);
	public Value<Boolean> Friends = new Value<Boolean>("Friends", "friends", true, this);
	public Value<Boolean> Teams = new Value<Boolean>("Teams", "teams", false, this);
	public Value<Boolean> MobsAnimals = new Value<Boolean>("Mobs/Animals", "mobs/animals", false, this);
	public Value<Boolean> Invisible = new Value<Boolean>("Invisible", "invisible", false, this);
	public Value<Boolean> Swing = new Value<Boolean>("Swing", "swing", true, this);
	public Value<Double> reach = new ClampedValue<Double>("Range", "range", 4.3, 1.0, 7.0, this);
	//public Value<Double> switchdelay = new ClampedValue<Double>("SwitchDelay", "switchdelay", 500.0, 1.0, 1000.0, this);
	public Value<Double> clicks = new ClampedValue<Double>("Cps", "cps", 12.0, 3.0, 20.0, this);
	public Value<Double> fov = new ClampedValue<Double>("FOV", "fov", 360.0, 10.0, 360.0, this);
	public static ModeValue auraMode;
	public double cps = 12;
	private HashMap<Entity, Integer> render = new HashMap();
	public double range = 4.3;
	public float mil = (float) (1000 / (cps));
	public ArrayList<AuraMode> modes;
	public int current;
	public ArrayList<Entity> targets;
	public static boolean isBlocking = false;

	public KillAura() {
		super("KillAura", 0, Category.COMBAT);
		this.modes = new ArrayList();
		this.modes.add(new Single(this));
		this.modes.add(new Switch(this));
		this.modes.add(new Tick(this));
		this.current = 0;
		CLASS = this;

		ArrayList<String> options = new ArrayList<>();
		for (AuraMode aura : modes) {
			options.add(aura.name);
		}
		auraMode = new ModeValue("AuraMode", "Mode", this.modes.get(this.current).name,
				new String[] { "Single", /*Switch",*/ "Tick" }, this);
	}

	@Override
	public void onEnable() {
		this.modes.get(this.current).onEnable();
	}

	@Override
	public void onDisable() {
		this.modes.get(this.current).onDisable();
	}

	@Override
	public void onEvent(Event e) {
		if (e instanceof EventRender3D) {
			if (!this.ESP.getValue())
				return;
			if (targets == null)
				return;
			if (targets.size() <= 0)
				return;
			renderVisuals();
		}

		if (e instanceof EventUpdate) {
			if (this.getState()) {
				if (mc.thePlayer.getHealth() <= 0 || mc.thePlayer.isDead) {
					Summer.tellPlayer("KillAura was toggled off due to death.");
					this.toggleMod();
				}
				cps = this.clicks.getValue().floatValue();
				range = this.reach.getValue().floatValue();
				mil = (float) (1000 / (cps));
				int i = 0;
				for (AuraMode aura : modes) {
					if (this.auraMode.getStringValue().equalsIgnoreCase(aura.name)) {
						this.current = i;
					}
					i++;
				}
			}
		}
		if (e instanceof EventPacketSend) {

			if (!this.keepsprint.getValue())
				return;
			if (((EventPacketSend) e).getPacket() instanceof C0BPacketEntityAction
					&& ((EventPacketSend) e).getPacket() instanceof C02PacketUseEntity) {
				final C0BPacketEntityAction packet = (C0BPacketEntityAction) ((EventPacketSend) e).getPacket();
				final C02PacketUseEntity useEntity = (C02PacketUseEntity) ((EventPacketSend) e).getPacket();
				if (useEntity.getAction() == C02PacketUseEntity.Action.ATTACK) {
					final C0BPacketEntityAction.Action func_180764_b = packet.func_180764_b();
					packet.func_180764_b();
					if (func_180764_b == C0BPacketEntityAction.Action.STOP_SPRINTING) {
						((EventPacketSend) e).setCancelled(true);
					}
				}
			}
		}
		if (e instanceof EventPacketRecieve) {
			if (((EventPacketRecieve) e).getPacket() instanceof S07PacketRespawn) {
				Summer.tellPlayer("KillAura was toggled off due to respawn.");
				this.toggleMod();
			}
		}
		if (e instanceof EventRender2D) {
			setDisplayName("" + this.modes.get(this.current).getName());
		}
		if (e instanceof EventPreMotionUpdate) {
			targets = getTargets();
			if (targets.size() <= 0)
				return;
			this.modes.get(this.current).pre((EventPreMotionUpdate) e);
		}
		if (e instanceof EventPostMotionUpdate) {
			if (targets.size() <= 0)
				return;
			this.modes.get(this.current).onPost((EventPostMotionUpdate) e);
			if (AutoBlock.getValue() && mc.thePlayer.getHeldItem() != null
					&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
				mc.thePlayer.sendQueue.getNetworkManager()
						.sendPacketNoEvent((new C08PacketPlayerBlockPlacement(
								new BlockPos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE,
								mc.thePlayer.getCurrentEquippedItem(), 0, 0, 0)));
				mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), mc.thePlayer.getHeldItem().getMaxItemUseDuration());
				KillAura.isBlocking = true;
//				Summer.tellPlayer("b");
			}
			boolean crits = Summer.moduleManager.getModule(Criticals.class).getState()
					&& !Criticals.critsmode.getStringValue().equalsIgnoreCase("Jump");
			if (mc.thePlayer.ticksExisted % 2 == 0) {
				if (crits && (mc.thePlayer.isCollidedVertically || mc.thePlayer.onGround)
						&& !Summer.moduleManager.getModule(Speed.class).getState()) {
					// canJump = true;
					((EventPostMotionUpdate) e).setY(((EventPostMotionUpdate) e).getY() + 0.062511);
					((EventPostMotionUpdate) e).setGround(false);
				}
			}
			float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(),
					mc.thePlayer.getCreatureAttribute());
			if (sharpLevel > 0)
				mc.thePlayer.onEnchantmentCritical(getCurrentEntity());
		}
	}

	public void esp(double d, double d1, double d2) {
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(3042);
		GL11.glLineWidth(3.0F);
		GL11.glDisable(2896);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glDepthMask(false);
		Color color = new Color(250, 45, 45, 100);
		R3DUtils.setColor(color);
		R3DUtils.drawBoundingBox(new AxisAlignedBB(d, d1, d2, d + 1.0D, d1 + 2.0D, d2 + 1.0D));
		GL11.glEnable(2896);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDepthMask(true);
		GL11.glDisable(3042);
		GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
	}

	private void renderVisuals() {
		ArrayList<Entity> canRender = new ArrayList();
		for (Entity entity : this.render.keySet()) {
			double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * this.mc.timer.renderPartialTicks;

			double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * this.mc.timer.renderPartialTicks;

			double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * this.mc.timer.renderPartialTicks;
			GlStateManager.translate(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ);
			esp(posX - 0.5D, posY, posZ - 0.5D);
			GlStateManager.translate(RenderManager.renderPosX, RenderManager.renderPosY, RenderManager.renderPosZ);
			canRender.add(entity);
		}
		for (Entity entity : canRender) {
			int renderframe = this.render.get(entity).intValue();
			renderframe++;
			this.render.remove(entity);
			if ((renderframe <= 50 & entity.isEntityAlive())) {
				this.render.put(entity, Integer.valueOf(renderframe));
			}
		}
	}

	public Entity getCurrentEntity() {
		if (this.auraMode.getStringValue().equalsIgnoreCase("switch"))
			return this.targets.get(Switch.current);
		else
			return this.targets.get(0);

	}

	public void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				this.mc.thePlayer);
	}

	public void dura(Entity entity, boolean crit) {
		if (crit) {
			for (double offset : new double[] { 0.06D, 0.0D, 0.03D, 0.0D }) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
						mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
			}
		} else {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
		}
		this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
		this.swing();
	}

	public void attack(Entity e) {
		if (Summer.moduleManager.getModule(Criticals.class).getState()) {
			Criticals.doCrits();
		}
		swing();
		if (e instanceof EntityPlayer)
			mc.playerController.attackEntity(mc.thePlayer, e);
		this.render.put(getCurrentEntity(), Integer.valueOf(0));
		
	}

	public ArrayList<Entity> getTargets() {
		ArrayList<Entity> t = new ArrayList();
		mc.theWorld.getLoadedEntityList().stream().forEach(e -> {
			if (this.vaildEntity((Entity) e))
				t.add((Entity) e);
		});
		t.sort((target1, target2) -> Math
				.round(mc.thePlayer.getDistanceToEntity(target1) - mc.thePlayer.getDistanceToEntity(target2)));
		return t;
	}

	private boolean vaildEntity(Entity entity) {
		if (!(entity instanceof EntityLivingBase)) {
			return false;
		}
		if (!(entity.isEntityAlive())) {
			return false;
		}
		if (entity == mc.thePlayer) {
			return false;
		}
		if (((EntityLivingBase) entity).isPlayerSleeping() && Summer.moduleManager.getModule(AntiBot.class).getState()
				&& AntiBot.antibotMode.getStringValue().equalsIgnoreCase("Mineplex")) {
			return false;
		}
		if (mc.thePlayer.getDistanceToEntity(entity) > range) {
			return false;
		}
		/*
		 * Cubecraft Antibot
		 */
		if ((Summer.moduleManager.getModule(AntiBot.class).getState()
				&& AntiBot.antibotMode.getStringValue().equalsIgnoreCase("Cubecraft"))
				&& (entity instanceof EntityMob && ((EntityMob) entity).isInvisibleToPlayer(mc.thePlayer)
						&& ((EntityMob) entity).isInvisible())) {
			entity.setInvisible(false);
			if (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword
					|| mc.thePlayer.getHeldItem().getItem() instanceof ItemAxe) {
				mc.thePlayer.swingItem();
				mc.playerController.attackEntity(mc.thePlayer, (entity));
				mc.theWorld.removeEntity((entity));
				Summer.tellPlayer("Cubecraft Bot has been removed. (" + ((EntityMob) entity).getName() + ")");
				return false;
			}
		}
		/*
		 * Hypixel Antibot
		 */
		if ((Summer.moduleManager.getModule(AntiBot.class).getState())
				&& (AntiBot.antibotMode.getStringValue().equalsIgnoreCase("WatchDog"))) {
			if ((entity instanceof EntityPlayer)) {
				EntityPlayer maybeBot = (EntityPlayer) entity;
				if (maybeBot.hasCustomName()) {
					System.out.println("Successfully removed a bot. (Name)");
					return false;
				}
				if (!isInTablist(maybeBot)) {
					System.out.println("Successfully removed a bot. (Tablist)");
					return false;
				}
			}
		}
		if (entity.isInvisible() && !this.Invisible.getValue()) {
			return false;
		}
		if (entity instanceof EntityGuardian || entity instanceof EntityMob && !this.MobsAnimals.getValue()
				|| entity instanceof EntityAnimal && !this.MobsAnimals.getValue()
				|| entity instanceof EntityVillager && !this.MobsAnimals.getValue()) {
			return false;
		}
		if (entity instanceof EntityPlayer && this.Teams.getValue() && getTabName((EntityPlayer) entity).length() > 2
				&& getTabName(mc.thePlayer).startsWith(getTabName((EntityPlayer) entity).substring(0, 2))) {
			return false;
		}
		if (FriendManager.isFriend(entity.getName()) && this.Friends.getValue()) {
			return false;
		}
		if (!mc.thePlayer.canEntityBeSeen(entity) && !this.walls.getValue()) {
			return false;
		}
		return true;
	}

	public static String getTabName(EntityPlayer player) {
		String realName = "";
		for (Object o5 : mc.ingameGUI.getTabList().getPlayerList()) {
			NetworkPlayerInfo o = (NetworkPlayerInfo) o5;
			String mcName = mc.ingameGUI.getTabList().func_175243_a(o);
			if ((mcName.contains(player.getName())) && (player.getName() != mcName)) {
				realName = mcName;
			}
		}
		return realName;
	}

	public double getAps() {
		return this.clicks.getValue().floatValue()
				+ (new Random().nextInt((int) (((Double) ((ClampedValue) this.clicks).getMax()).floatValue() / 2))
						+ Math.random() - Math.random() / 1.25F);
	}

	private static boolean isInTablist(EntityPlayer player) {
		NetworkPlayerInfo var48 = new NetworkPlayerInfo(player.getGameProfile());
		var48.getResponseTime();
		if (mc.isSingleplayer()) {
			return true;
		}
		for (Object o : mc.getNetHandler().func_175106_d()) {
			NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
			if (playerInfo.func_178845_a().getName().equalsIgnoreCase(player.getName())) {
				return true;
			}
		}
		return false;
	}

	private void swing() {
		if (this.Swing.getValue())
			mc.thePlayer.swingItem();
		else
			mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
	}
}