package volcano.summer.client.modules.render;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.friend.FriendManager;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventName;
import volcano.summer.client.events.EventRender2D;
import volcano.summer.client.events.EventRender3D;
import volcano.summer.client.util.MathUtil;
import volcano.summer.client.util.R2DUtils;
import volcano.summer.client.util.RotationUtils;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.Value;

public class Nametags extends Module {

	private double gradualFOVModifier;
	private Character formatChar;
	public static Map<EntityLivingBase, double[]> entityPositions = new HashMap();
	public static Value<Boolean> armor;
	public static Value<Boolean> dura;
	public static Value<Boolean> invis;
	public static Value<Double> scale;

	public Nametags() {
		super("Nametags", 0, Category.RENDER);
		this.formatChar = new Character('§');
		armor = new Value("Armor", "armor", Boolean.valueOf(true), this);
		dura = new Value("Durability", "durability", Boolean.valueOf(false), this);
		invis = new Value("Invis", "invis", Boolean.valueOf(false), this);
		scale = new ClampedValue<Double>("Scale", "scale", 1.0, 0.1, 3.0, this);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	private static void drawEnchantTag(String text, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.disableDepth();
		x *= 1;
		y -= 4;
		GL11.glScalef(0.57F, 0.57F, 0.57F);
		Minecraft.getMinecraft().fontRendererObj.func_175063_a(text, x, -36 - y, 64250);
		GlStateManager.enableDepth();
		GlStateManager.popMatrix();
	}

	private void scale(Entity ent) {
		float scale = this.scale.getValue().floatValue();
		float target = scale
				* (mc.gameSettings.fovSetting / (mc.gameSettings.fovSetting * mc.thePlayer.func_175156_o()));
		if ((this.gradualFOVModifier == 0.0D) || (Double.isNaN(this.gradualFOVModifier))) {
			this.gradualFOVModifier = target;
		}
		double gradualFOVModifier = this.gradualFOVModifier;
		double n = target - this.gradualFOVModifier;
		this.gradualFOVModifier = (gradualFOVModifier + n / (Minecraft.debugFPS * 0.7D));
		scale *= (float) this.gradualFOVModifier;
		scale *= ((mc.currentScreen == null) && (GameSettings.isKeyDown(mc.gameSettings.ofKeyBindZoom)) ? 3 : 1);
		GlStateManager.scale(scale, scale, scale);
	}

	private void updatePositions() {
		entityPositions.clear();
		float pTicks = mc.timer.renderPartialTicks;
		for (Object o : mc.theWorld.loadedEntityList) {
			Entity ent = (Entity) o;
			if ((ent != mc.thePlayer) && ((ent instanceof EntityPlayer))
					&& ((!ent.isInvisible()) || (!this.invis.getValue()))) {
				double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - mc.getRenderManager().viewerPosX;
				double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - mc.getRenderManager().viewerPosY;
				double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - mc.getRenderManager().viewerPosZ;
				y += ent.height + 0.2D;
				if (convertTo2D(x, y, z)[2] >= 0.0D) {
					if (convertTo2D(x, y, z)[2] < 1.0D) {
						entityPositions.put((EntityPlayer) ent,
								new double[] { convertTo2D(x, y, z)[0], convertTo2D(x, y, z)[1],
										Math.abs(convertTo2D(x, y + 1.0D, z, ent)[1] - convertTo2D(x, y, z, ent)[1]),
										convertTo2D(x, y, z)[2] });
					}
				}
			}
		}
	}

	private double[] convertTo2D(double x, double y, double z, Entity ent) {
		float pTicks = mc.timer.renderPartialTicks;
		float prevYaw = mc.thePlayer.rotationYaw;
		float prevPrevYaw = mc.thePlayer.prevRotationYaw;
		float[] rotations = RotationUtils.getRotationFromPosition(
				ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks,
				ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks,
				ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - 1.6D);
		Entity renderViewEntity = mc.getRenderViewEntity();
		Entity renderViewEntity2 = mc.getRenderViewEntity();
		float n = rotations[0];
		renderViewEntity2.prevRotationYaw = n;
		renderViewEntity.rotationYaw = n;
		mc.entityRenderer.setupCameraTransform(pTicks, 0);
		double[] convertedPoints = convertTo2D(x, y, z);
		mc.getRenderViewEntity().rotationYaw = prevYaw;
		mc.getRenderViewEntity().prevRotationYaw = prevPrevYaw;
		mc.entityRenderer.setupCameraTransform(pTicks, 0);
		return convertedPoints;
	}

	private double[] convertTo2D(double x, double y, double z) {
		FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
		FloatBuffer projection = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(2982, modelView);
		GL11.glGetFloat(2983, projection);
		GL11.glGetInteger(2978, viewport);
		boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCoords);
		if (result) {
			return new double[] { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
		}
		return null;
	}

	@Override
	public void onEvent(final Event event) {
		if (event instanceof EventRender3D) {
			updatePositions();
		}
		if (event instanceof EventName && ((EventName) event).entity instanceof EntityPlayer) {
			((EventName) event).setCancelled(true);
		}
		if (event instanceof EventRender2D) {
			GlStateManager.pushMatrix();
			ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
			double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0D);
			GlStateManager.scale(twoDscale, twoDscale, twoDscale);
			for (Entity ent : entityPositions.keySet()) {
				GlStateManager.pushMatrix();
				if (ent != mc.thePlayer) {
					if ((ent instanceof EntityPlayer)) {
						String str = ent.getDisplayName().getFormattedText();
						if (FriendManager.isFriend(ent.getName())) {
							str = str.replace(ent.getDisplayName().getFormattedText(),
									"§b" + FriendManager.getAliasName(ent.getName()));
						}
						String colorString = this.formatChar.toString();
						double health = MathUtil.roundToPlace(((EntityPlayer) ent).getHealth() / 2.0F, 2);
						if (health >= 6.0D) {
							colorString = String.valueOf(colorString) + "2";
						} else if (health >= 3.5D) {
							colorString = String.valueOf(colorString) + "6";
						} else {
							colorString = String.valueOf(colorString) + "4";
						}
						str = String.valueOf(str) + " " + colorString + health + " \u2764";
						double[] renderPositions = entityPositions.get(ent);
						if ((renderPositions[3] < 0.0D) || (renderPositions[3] >= 1.0D)) {
							GlStateManager.popMatrix();
							continue;
						}
						GlStateManager.translate(renderPositions[0], renderPositions[1], 0.0D);
						scale(ent);
						GlStateManager.translate(0.0D, -2.5D, 0.0D);
						int strWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(str);
						R2DUtils.rectangle(-strWidth / 2 - 3, -12.0D, strWidth / 2 + 3, 1.0D, -1191182336);
						GlStateManager.color(0.0F, 0.0F, 0.0F);
						Minecraft.getMinecraft().fontRendererObj.drawString(str, -strWidth / 2, (int) -9.0D, -1);
						GlStateManager.color(1.0F, 1.0F, 1.0F);
						if (this.armor.getValue()) {
							List<ItemStack> itemsToRender = new ArrayList();
							for (int i = 0; i < 5; i++) {
								ItemStack stack = ((EntityPlayer) ent).getEquipmentInSlot(i);
								if (stack != null) {
									itemsToRender.add(stack);
								}
							}
							int x = -(itemsToRender.size() * 9);
							Iterator<ItemStack> iterator2 = itemsToRender.iterator();
							while (iterator2.hasNext()) {
								ItemStack stack = iterator2.next();
								int durability = stack.getMaxDamage() - stack.getItemDamage();
								RenderHelper.enableGUIStandardItemLighting();
								mc.getRenderItem().func_175042_a(stack, x, -30);
								mc.getRenderItem().func_175030_a(mc.fontRendererObj, stack, x, -30);
								x += 2;
								RenderHelper.disableStandardItemLighting();
								if (stack != null) {
									int y = 21;
									int sLevel = EnchantmentHelper
											.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
									int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId,
											stack);
									int kLevel = EnchantmentHelper
											.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
									if (sLevel > 0) {
										drawEnchantTag("Sh" + sLevel, x, y);
										y -= 9;
									}
									if (fLevel > 0) {
										drawEnchantTag("Fir" + fLevel, x, y);
										y -= 9;
									}
									if (kLevel > 0) {
										drawEnchantTag("Kb" + kLevel, x, y);
									} else if ((stack.getItem() instanceof ItemArmor)) {
										int pLevel = EnchantmentHelper
												.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
										int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId,
												stack);
										int uLevel = EnchantmentHelper
												.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
										if (pLevel > 0) {
											drawEnchantTag("P" + pLevel, x, y);
											y -= 9;
										}
										if (tLevel > 0) {
											drawEnchantTag("Th" + tLevel, x, y);
											y -= 9;
										}
										if (uLevel > 0) {
											drawEnchantTag("Unb" + uLevel, x, y);
										}
									} else if ((stack.getItem() instanceof ItemBow)) {
										int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId,
												stack);
										int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId,
												stack);
										int fireLevel = EnchantmentHelper
												.getEnchantmentLevel(Enchantment.flame.effectId, stack);
										if (powLevel > 0) {
											drawEnchantTag("Pow" + powLevel, x, y);
											y -= 9;
										}
										if (punLevel > 0) {
											drawEnchantTag("Pun" + punLevel, x, y);
											y -= 9;
										}
										if (fireLevel > 0) {
											drawEnchantTag("Fir" + fireLevel, x, y);
										}
									} else if (stack.getRarity() == EnumRarity.EPIC) {
										drawEnchantTag("§lGod", x, y);
									}
									if (this.dura.getValue()) {
										if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemSword
												|| stack.getItem() instanceof ItemTool
												|| stack.getItem() instanceof ItemArmor) {

											GlStateManager.pushMatrix();
											GlStateManager.scale(0.5, 0.5, 0.5);
											mc.fontRendererObj.func_175063_a(durability + "", (x + 5) / (float) 0.5,
													((float) y - 55) / (float) 0.5, 0xffffff);
											GlStateManager.popMatrix();
										}
									}
									x += 16;
								}
							}
						}
					}
					GlStateManager.popMatrix();
				}
			}
			GlStateManager.popMatrix();
		}
	}
}