package ru.wendoxd.modules.impl.combat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShield;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventUpdate;
import ru.wendoxd.events.impl.render.EventRender3D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.Slider;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

public class TargetStrafe extends Module {

	private Frame targetstrafe_frame = new Frame("TargetStrafe");
	private final CheckBox targetStrafe = new CheckBox("TargetStrafe").markArrayList("TargetStrafe");
	private final CheckBox noSprint = new CheckBox("Disable on sprint");
	private final Slider distance = new Slider("Distance", 1, 0.1, 6, 0.45, () -> targetStrafe.isEnabled(true));
	private final Slider speedValue = new Slider("Speed", 2, 0.1, 2, 0.07, () -> targetStrafe.isEnabled(true));
	private final CheckBox damageboost = new CheckBox("Damage Boost", () -> targetStrafe.isEnabled(true));
	private final Slider damageValue = new Slider("Value", 2, 0.1, 2, 0.08,
			() -> targetStrafe.isEnabled(true) && damageboost.isEnabled(true));
	private final CheckBox drawradius = new CheckBox("Draw Radius", () -> targetStrafe.isEnabled(true));
	public static int hurt;
	private boolean switchDir = true;

	@Override
	protected void initSettings() {
		targetStrafe.markSetting("TargetStrafe");
		targetStrafe.modifyPath("Distance_2");
		speedValue.modifyPath("Speed_10");
		targetstrafe_frame.register(targetStrafe, distance, speedValue, damageboost, damageValue, drawradius, noSprint);
		MenuAPI.combat.register(targetstrafe_frame);
	}

	public boolean needToSwitch(double x, double z) {
		if (mc.player.isCollidedHorizontally || mc.gameSettings.keyBindLeft.isPressed()
				|| mc.gameSettings.keyBindRight.isPressed()) {
			return true;
		}
		for (int i = (int) (mc.player.posY + 4); i >= 0; --i) {
			BlockPos playerPos = new BlockPos(x, i, z);
			blockFIRE: {
				blockLAVA: {
					if (mc.world.getBlockState(playerPos).getBlock().equals(Blocks.LAVA))
						break blockLAVA;
					if (!mc.world.getBlockState(playerPos).getBlock().equals(Blocks.FIRE))
						break blockFIRE;
				}
				return true;
			}
			if (mc.world.isAirBlock(playerPos))
				continue;
			return false;
		}
		return true;
	}

	private double wrapDS(double x, double z) {
		double diffX = x - mc.player.posX;
		double diffZ = z - mc.player.posZ;
		return Math.toDegrees(Math.atan2(diffZ, diffX)) - 90;
	}

	private float calculateSpeed(EntityLivingBase entity) {
		float speed = speedValue.getFloatValue();
		boolean isShieldPressed = entity.isHandActive() && (entity.getHeldItemOffhand().getItem() instanceof ItemShield
				|| entity.getHeldItemMainhand().getItem() instanceof ItemShield);
		return isShieldPressed ? 0.12F : speed;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventUpdate) {
			if (mc.player.ticksExisted < 20)
				return;
			if (Aura.target != null && targetStrafe.isEnabled(false)) {
				if (mc.player.isSprinting() && noSprint.isEnabled(false)) {
					return;
				}
				EntityLivingBase entity = Aura.target;
				double speed = calculateSpeed(entity);
				if (mc.player.onGround) {
					mc.player.jump();
				}
				double wrap = Math.atan2(mc.player.posZ - entity.posZ, mc.player.posX - entity.posX);
				wrap += switchDir ? speed / mc.player.getDistanceToEntity(entity)
						: -(speed / mc.player.getDistanceToEntity(entity));
				double x = entity.posX + distance.getFloatValue() * Math.cos(wrap);
				double z = entity.posZ + distance.getFloatValue() * Math.sin(wrap);
				if (needToSwitch(x, z)) {
					switchDir = !switchDir;
					wrap += 2 * (switchDir ? speed / mc.player.getDistanceToEntity(entity)
							: -(speed / mc.player.getDistanceToEntity(entity)));
					x = entity.posX + distance.getFloatValue() * Math.cos(wrap);
					z = entity.posZ + distance.getFloatValue() * Math.sin(wrap);
				}
				if (!mc.player.onGround && damageboost.isEnabled(false) && !mc.player.lastUpdatedOnGround
						&& mc.player.hurtTime > 0) {
					speed *= damageValue.getFloatValue() * 5;
				}
				mc.player.motionX = speed * -Math.sin(Math.toRadians(wrapDS(x, z)));
				mc.player.motionZ = speed * Math.cos(Math.toRadians(wrapDS(x, z)));
			}
		}
		if (event instanceof EventRender3D && targetStrafe.isEnabled(false)) {
			if (Aura.target != null && drawradius.isEnabled(false)) {
				EntityLivingBase entity = Aura.target;
				double calcX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks()
						- mc.getRenderManager().renderPosX;
				double calcY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks()
						- mc.getRenderManager().renderPosY;
				double calcZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks()
						- mc.getRenderManager().renderPosZ;
				float radius = distance.getFloatValue();
				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LINE_SMOOTH);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glBegin(GL11.GL_LINE_STRIP);

				for (int i = 0; i <= 360; i++) {
					int rainbow = PaletteHelper.rainbow(i / 360f);
					GlStateManager.color(((rainbow >> 16) & 255) / 255F, ((rainbow >> 8) & 255) / 255f,
							(rainbow & 255) / 255F);
					GL11.glVertex3d(calcX + radius * Math.cos(Math.toRadians(i)), calcY,
							calcZ + radius * Math.sin(Math.toRadians(i)));
				}

				GL11.glEnd();
				GL11.glDisable(GL11.GL_BLEND);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_LINE_SMOOTH);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glPopMatrix();
				GlStateManager.resetColor();
			}
		}
	}
}