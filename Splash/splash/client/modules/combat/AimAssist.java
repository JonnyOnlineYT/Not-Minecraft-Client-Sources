package splash.client.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.lwjgl.input.Mouse;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import optfine.Reflector;
import splash.Splash;
import splash.api.friend.Friend;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.player.EventPlayerUpdate;
import splash.client.events.player.EventTick;
import splash.client.events.render.EventRender2D;
import splash.client.modules.visual.UI;
import splash.gui.ClickGui;
import splash.utilities.math.MathUtils;
import splash.utilities.math.rotation.AngleUtility;
import splash.utilities.math.rotation.RotationUtils;
import splash.utilities.math.vec.Vector;
import splash.utilities.player.PlayerUtils;
import splash.utilities.time.Stopwatch;
import splash.utilities.visual.RenderUtilities;

/**
 * Author: Cystms 
 * 
 * 6:05 PM
 * 7/13/2020
 */
public class AimAssist extends Module {
	public ModeValue<AimMode> aimMode = new ModeValue<>("Aim Mode", AimMode.SNAP, this);
	public NumberValue<Integer> angleSmoothing = new NumberValue<Integer>("Smoothing", 20, 20, 100, this);
	public NumberValue<Double> targetingDist = new NumberValue<Double>("Targeting Distance", 4.25, 2.0, 10.0, this);
	public BooleanValue<Boolean> invisibles = new BooleanValue<>("Invisibles", false, this);
	public BooleanValue<Boolean> animals = new BooleanValue<>("Animals", true, this);
	public BooleanValue<Boolean> players = new BooleanValue<>("Players", true, this);
	public BooleanValue<Boolean> dead = new BooleanValue<>("Deads", true, this);
	public BooleanValue<Boolean> mobs = new BooleanValue<>("Mobs", false, this);
	public BooleanValue<Boolean> teams = new BooleanValue<>("Teams", false, this); 
	public Entity lastAimedTarget;
	public EntityLivingBase target;
	public AngleUtility angleUtility;
	
	public double targetedarea;
	public boolean changingArea;
	
	public int maxYaw, maxPitch, targetIndex, rotationSwap;
	public float currentYaw, currentPitch, pitchincrease;
	
	public ArrayList<EntityLivingBase> targetList;

	public enum AimMode {
		ADVANCED, SMOOTHED, SNAP
	}

	public AimAssist() {
		super("Aim Assist", "Aims for you", ModuleCategory.COMBAT);
		angleUtility = new AngleUtility(70, 250, 70, 200);
		targetList = new ArrayList<>(); 
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Collect
	public void onAura(EventPlayerUpdate e) {
		if (!isModuleActive() || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof ClickGui) return;
		currentYaw = e.getYaw();
		currentPitch = e.getPitch();
		updateTargetList();
		if (targetList.isEmpty() || targetList.size() - 1 < targetIndex) {
			reset(-1, e);
			return;
		}

		if (targetIndex == -1) {
			reset(0, e);
			return;
		}

		if (!PlayerUtils.isValid(targetList.get(targetIndex), targetingDist.getValue(), invisibles.getValue(), teams.getValue(), dead.getValue(), players.getValue(), animals.getValue(), mobs.getValue())) {
			reset(-1, e);
			return;
		}
		target = targetList.get(targetIndex);		
		if (e.getStage().equals(Stage.PRE)) {
			if (Mouse.isButtonDown(0)) {
				aim(e);
			}
		}
	}
	
	public void aim(EventPlayerUpdate e) {
		Vector.Vector3<Double> enemyCoords = new Vector.Vector3<>(
				target.getEntityBoundingBox().minX
						+ (target.getEntityBoundingBox().maxX - target.getEntityBoundingBox().minX) / 2,
				(target instanceof EntityPig || target instanceof EntitySpider
						? target.getEntityBoundingBox().minY - target.getEyeHeight() / 1.5 : target.posY) - Math.abs(target.posY - mc.thePlayer.posY) * .8,
				target.getEntityBoundingBox().minZ
						+ (target.getEntityBoundingBox().maxZ - target.getEntityBoundingBox().minZ) / 2);
		Vector.Vector3<Double> myCoords = new Vector.Vector3<>(mc.thePlayer.getEntityBoundingBox().minX
				+ (mc.thePlayer.getEntityBoundingBox().maxX - mc.thePlayer.getEntityBoundingBox().minX) / 2,
				mc.thePlayer.posY,
				mc.thePlayer.getEntityBoundingBox().minZ
						+ (mc.thePlayer.getEntityBoundingBox().maxZ - mc.thePlayer.getEntityBoundingBox().minZ)
								/ 2);
		AngleUtility.Angle srcAngle = new AngleUtility.Angle(e.getYaw(), e.getPitch());
		AngleUtility.Angle dstAngle = angleUtility.calculateAngle(enemyCoords, myCoords, target, rotationSwap);
		AngleUtility.Angle newSmoothing = angleUtility.smoothAngle(dstAngle, srcAngle, 300, 40 * 30);

		double x = target.posX - mc.thePlayer.posX + (target.lastTickPosX - target.posX) / 2;
		double z = target.posZ - mc.thePlayer.posZ + (target.lastTickPosZ - target.posZ) / 2;
		float destinationYaw = 0;
		if (lastAimedTarget != target) {
			changingArea = false;
			targetedarea = rotationSwap = 0;
		}
		lastAimedTarget = target;
		double smooth = (1 + ((angleSmoothing.getValue()) * .025));
		destinationYaw = RotationUtils.constrainAngle(e.getYaw() - (float) -(Math.atan2(x, z) * (58 + targetedarea)));
		float pitch = newSmoothing.getPitch();
		if (pitch > 90f) {
			pitch = 90f;
		} else if (pitch < -90.0f) {
			pitch = -90.0f;
		}
		smooth = smooth + (mc.thePlayer.getDistanceToEntity(target) * .1 + (mc.thePlayer.ticksExisted % 4 == 0 ? 40 * .001 : 0));
		destinationYaw = (float) (e.getYaw() - destinationYaw / smooth);
		boolean ticks = mc.thePlayer.ticksExisted % 20 == 0;
		if (mc.thePlayer.ticksExisted % 15 == 0) {
			if (rotationSwap++ >= 3) {
				rotationSwap = 0;
			}
			pitchincrease += changingArea ? MathUtils.getRandomInRange(-.055, -.075) : MathUtils.getRandomInRange(.055, .075);
		}
		if (pitchincrease >= .5) {
			changingArea = true;
		}
		if (pitchincrease <= -.15) {
			changingArea = false;
		}
		if (aimMode.getValue().equals(AimMode.ADVANCED)) {
			float playerYaw = mc.thePlayer.rotationYaw;
			float playerPitch = mc.thePlayer.rotationPitch;
			float sens = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
			float mult = sens * sens * sens * 8.0F;
			float dstYaw = (float) maxYaw * mult;
			float dstPitch = (float) maxPitch * mult;
			if (Math.abs(playerYaw - destinationYaw) > 2) {
				if (RotationUtils.rayCast(playerYaw, playerPitch, targetingDist.getValue()) == null) {
					maxYaw += playerYaw > destinationYaw ? -MathUtils.getRandomInRange(6, 12) : MathUtils.getRandomInRange(6, 12);
				} else {
					maxYaw *= .25;
				}
			} else {
				maxYaw *= .25;
			}
			if (Math.abs(playerPitch - AngleUtility.getRotations(target)[1]) > 2) {
				if (RotationUtils.rayCast(playerYaw, playerPitch, targetingDist.getValue()) == null) {
					maxPitch += playerPitch > AngleUtility.getRotations(target)[1] ? MathUtils.getRandomInRange(1, 2) : -MathUtils.getRandomInRange(1, 2);
				} else {
					maxPitch *= .25;
				}
			} else {
				maxPitch *= .25;
			}
			mc.thePlayer.rotationPitch = MathHelper.clamp_float((float) ((double) playerPitch - (double) dstPitch * 0.15D), -90.0F, 90.0F);
			mc.thePlayer.rotationYaw = (float) ((double) playerYaw + (double) dstYaw * 0.15D);
		} else if (aimMode.getValue().equals(AimMode.SMOOTHED)) {
			float theYaw = (float) MathUtils.preciseRound(destinationYaw, 1) + (ticks ? .243437f : .14357f);
			float thePitch = (float) MathUtils.preciseRound(pitch, 1) + (ticks ? .1335f : .13351f);
			mc.thePlayer.rotationPitch = theYaw;
			mc.thePlayer.rotationYaw = thePitch;
		} else if (aimMode.getValue().equals(AimMode.SNAP)) {
			float theYaw = (float) MathUtils.preciseRound(AngleUtility.getRotations(target)[0], 1) + (ticks ? .243437f : .14357f);
			float thePitch = (float) MathUtils.preciseRound(AngleUtility.getRotations(target)[1], 1) + (ticks ? .1335f : .13351f);
			mc.thePlayer.rotationPitch = theYaw;
			mc.thePlayer.rotationYaw = thePitch;
		}
	}

	void reset(int i, EventPlayerUpdate event) {
		target = null;
		targetIndex = i;
	}

	private void updateTargetList() {
		target = null;
		targetList.clear();
		mc.theWorld.getLoadedEntityList().forEach(entity -> {
			if (entity != null && entity instanceof EntityLivingBase) {
				if (PlayerUtils.isValid((EntityLivingBase) entity, targetingDist.getValue(), invisibles.getValue(), teams.getValue(), dead.getValue(), players.getValue(), animals.getValue(), mobs.getValue())) {
					targetList.add((EntityLivingBase) entity);
				} else if (targetList.contains(entity)) {
					targetList.remove(entity);
				}
			}
		});

		if (targetList.size() > 1) {
			targetList.sort((e1, e2) -> Float.compare(mc.thePlayer.rotationYaw - RotationUtils.getNeededRotations(e1)[0], mc.thePlayer.rotationYaw - RotationUtils.getNeededRotations(e2)[0]));
			targetList.sort((e1, e2) -> Boolean.compare(e2 instanceof EntityPlayer, e1 instanceof EntityPlayer));
		}
	}
}