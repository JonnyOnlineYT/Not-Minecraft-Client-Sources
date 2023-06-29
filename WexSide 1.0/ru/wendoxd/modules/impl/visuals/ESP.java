package ru.wendoxd.modules.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Enchantments;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import ru.wendoxd.WexSide;
import ru.wendoxd.command.impl.VClipCommand;
import ru.wendoxd.events.Event;
import ru.wendoxd.events.impl.player.EventTick;
import ru.wendoxd.events.impl.render.EventRender2D;
import ru.wendoxd.events.impl.render.EventRender3D;
import ru.wendoxd.modules.Module;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.CheckBox;
import ru.wendoxd.ui.menu.elements.tabelements.MultiSelectBox;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.combat.CastHelper;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.ColorShell;
import ru.wendoxd.utils.visual.RenderUtils;
import ru.wendoxd.utils.visual.animation.AstolfoAnimation;
import ru.wendoxd.utils.visual.pallete.PaletteHelper;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ESP extends Module {
	public static Frame pearlPredict = new Frame("Predictions");
	public static CheckBox predict = new CheckBox("Predict").markColorPicker().markArrayList("Predict");
	public static MultiSelectBox predicts = new MultiSelectBox("Entities", new String[] { "Pearl", "Arrow", "Hook" },
			() -> predict.isEnabled(true));
	public static AstolfoAnimation astolfo = new AstolfoAnimation();

	@Override
	protected void initSettings() {
		MenuAPI.visuals.register(pearlPredict);
		pearlPredict.register(predict, predicts);
	}

	public List<PredictedPosition> getPredictedPositions(Entity entity) {
		if (entity instanceof EntityEnderPearl) {
			return ((EntityEnderPearl) entity).predictedPositions;
		}
		if (entity instanceof EntityFishHook) {
			return ((EntityFishHook) entity).predictedPositions;
		}
		if (entity instanceof EntityArrow) {
			return ((EntityArrow) entity).predictedPositions;
		}
		return null;
	}

	public void draw(List<PredictedPosition> list, Entity entity) {
		boolean first = true;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(predict.getColor().getRed() / 255.f, predict.getColor().getGreen() / 255.f,
				predict.getColor().getBlue() / 255.f, predict.getColor().getAlpha() / 255.f);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glLineWidth(0.5f);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (int i = 0; i < list.size(); i++) {
			PredictedPosition pp = list.get(i);
			Vec3d v = new Vec3d(pp.pos.xCoord, pp.pos.yCoord, pp.pos.zCoord);
			if (list.size() > 2 && first) {
				PredictedPosition next = list.get(i + 1);
				v.xCoord = v.xCoord + (next.pos.xCoord - v.xCoord) * mc.getRenderPartialTicks();
				v.yCoord = v.yCoord + (next.pos.yCoord - v.yCoord) * mc.getRenderPartialTicks();
				v.zCoord = v.zCoord + (next.pos.zCoord - v.zCoord) * mc.getRenderPartialTicks();
			}
			GL11.glVertex3d(v.xCoord - mc.getRenderManager().renderPosX, v.yCoord - mc.getRenderManager().renderPosY,
					v.zCoord - mc.getRenderManager().renderPosZ);
			first = false;
		}
		list.removeIf(w -> w.tick < entity.ticksExisted);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender3D) {
			for (Entity entity : mc.world.loadedEntityList) {
				if (predict.isEnabled(false)) {
					if (entity instanceof EntityEnderPearl) {
						draw(((EntityEnderPearl) entity).predictedPositions, entity);
					}
					if (entity instanceof EntityFishHook) {
						if (((EntityFishHook) entity).caughtEntity != null) {
							((EntityFishHook) entity).predictedPositions.clear();
						}
						draw(((EntityFishHook) entity).predictedPositions, entity);
					}
					if (entity instanceof EntityArrow) {
						draw(((EntityArrow) entity).predictedPositions, entity);
					}
				}
			}
		}
		if (event instanceof EventRender2D) {
			for (Entity entity : mc.world.loadedEntityList) {
				CastHelper castHelper = new CastHelper();
				castHelper.apply(CastHelper.EntityType.PLAYERS);
				castHelper.apply(CastHelper.EntityType.MOBS);
				castHelper.apply(CastHelper.EntityType.ANIMALS);
				castHelper.apply(CastHelper.EntityType.VILLAGERS);
				castHelper.apply(CastHelper.EntityType.FRIENDS);
				castHelper.apply(CastHelper.EntityType.SELF);
				CastHelper.EntityType type;
				if ((type = CastHelper.isInstanceof(entity, castHelper.build())) != null
						&& RenderUtils.isInViewFrustum(entity)) {
					CheckBox box = visuals_entitiestab.get(type.ordinal()).box;
					ColorShell color = box.getColor();
					double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks();
					double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks();
					double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks();
					double width = entity.width / 1.5;
					double height = entity.height
							+ ((entity.isSneaking() || (entity == mc.player && mc.player.isSneaking()) ? -0.3D : 0.2D));
					AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height,
							z + width);
					List<Vector3d> vectors = Arrays.asList(
							new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ),
							new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ),
							new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ),
							new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ),
							new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ),
							new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ),
							new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ),
							new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ));

					mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 0);

					ScaledResolution sr = ((EventRender2D) event).getScaledResolution();
					Vector4d position = null;
					for (Vector3d vector : vectors) {
						vector = RenderUtils.vectorTo2D(sr.getScaleFactor(),
								vector.x - mc.getRenderManager().renderPosX,
								vector.y - mc.getRenderManager().renderPosY,
								vector.z - mc.getRenderManager().renderPosZ);
						if (vector != null && vector.z > 0 && vector.z < 1) {

							if (position == null) {
								position = new Vector4d(vector.x, vector.y, vector.z, 0);
							}

							position.x = Math.min(vector.x, position.x);
							position.y = Math.min(vector.y, position.y);
							position.z = Math.max(vector.x, position.z);
							position.w = Math.max(vector.y, position.w);
						}
					}
					boolean cancel = mc.player == entity && mc.gameSettings.thirdPersonView == 0;
					if (position != null) {
						mc.entityRenderer.setupOverlayRendering(2);
						double posX = position.x;
						double posY = position.y;
						double endPosX = position.z;
						double endPosY = position.w;
						if (box.isEnabled() && !cancel) {
							int black = RenderUtils.rgba(25, 25, 25, 255);
							int build = color.build();
							double koef = 1 / 7.;
							//
							RenderUtils.drawRect(posX - 0.5f, posY - 1f, endPosX + 1, posY + 0.5f, black);
							if (!color.isRainbow()) {
								RenderUtils.drawRect(posX, posY - 0.5f, endPosX, posY, build);
							} else {
								TargetHUD.drawGradientSideways(posX, posY - 0.5f, endPosX, posY, astolfo.getColor(koef),
										astolfo.getColor(0));
							}
							//
							RenderUtils.drawRect(posX - 0.5f, posY, posX + 1, endPosY + 0.5f, black);
							if (!color.isRainbow()) {
								RenderUtils.drawRect(posX, posY - 0.5f, posX + 0.5f, endPosY, build);
							} else {
								TargetHUD.drawGradient(posX, posY - 0.5f, posX + 0.5f, endPosY,
										astolfo.getColor(koef * 1), astolfo.getColor(koef * 2));
							}
							//
							RenderUtils.drawRect(endPosX - 0.5f, posY, endPosX + 1, endPosY + 0.5f, black);
							if (!color.isRainbow()) {
								RenderUtils.drawRect(endPosX, posY - 0.5f, endPosX + 0.5f, endPosY, build);
							} else {
								TargetHUD.drawGradient(endPosX, posY - 0.5f, endPosX + 0.5f, endPosY,
										astolfo.getColor(koef * 7), astolfo.getColor(koef * 6));
							}
							//
							RenderUtils.drawRect(posX + 0.5f, endPosY - 1f, endPosX, endPosY + 0.5f, black);
							if (!color.isRainbow()) {
								RenderUtils.drawRect(posX, endPosY - 0.5f, endPosX, endPosY, build);
							} else {
								TargetHUD.drawGradientSideways(posX + 0.5f, endPosY - 0.5f, endPosX, endPosY,
										astolfo.getColor(koef * 2), astolfo.getColor(koef * 6));
							}
						}

						EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
						float targetHP = entityLivingBase.getHealth();
						float maxHealth = entityLivingBase.getMaxHealth();
						targetHP = MathHelper.clamp(targetHP, 0, 24);
						double hpPercentage = targetHP / maxHealth;

						CheckBox health = visuals_entitiestab.get(type.ordinal()).health;
						if (health.isEnabled() && !cancel) {
							RenderUtils.drawRect(posX - 4, posY - 0.5f, posX - 2, endPosY + 0.5f,
									RenderUtils.rgba(35, 35, 35, 255));
							TargetHUD.drawGradient(posX - 3.5f, endPosY + ((posY - endPosY) * hpPercentage),
									posX - 2.5f, endPosY, astolfo.getColor(0), astolfo.getColor(1 / 6.));
						}
						MultiSelectBox msb = visuals_entitiestab.get(type.ordinal()).info;
						if (msb != null) {
							EntityLivingBase contextEntity = (EntityLivingBase) entity;
							float center = (float) (posX + (endPosX - posX) / 2f);
							float maxOffsetY = 0;
							if (msb.get(0)) {

								String name;

								if (WexSide.friendManager.isFriend(contextEntity.getName())) {
									name = StreamerMode.streamermode.isEnabled(false) && StreamerMode.type.get(7)
											? "Protected".concat(" §f[§a" + (int) contextEntity.getHealth() + " HP§f]")
											: entity.getDisplayName().getFormattedText()
													.concat(" §f[" + PaletteHelper.getHealthStr(contextEntity)
															+ (int) contextEntity.getHealth() + " HP§f]");
								} else {
									name = StreamerMode.streamermode.isEnabled(false) && StreamerMode.type.get(1)
											? "Protected".concat(" §f[§a" + (int) contextEntity.getHealth() + " HP§f]")
											: entity.getDisplayName().getFormattedText()
													.concat(" §f[" + PaletteHelper.getHealthStr(contextEntity)
															+ (int) contextEntity.getHealth() + " HP§f]");
								}
								if (contextEntity instanceof EntityOtherPlayerMP) {
									EntityOtherPlayerMP eopmp = (EntityOtherPlayerMP) contextEntity;
									if (eopmp.bot) {
										name = "§cБот";
									}
								}
								VClipCommand.class.getClass();
								float nameWidth = Fonts.MNTSB_16.getStringWidth(name.isEmpty() ? "" : name);
								RenderUtils.drawRect(center - nameWidth / 2f - 2, (float) posY - 15,
										center + nameWidth / 2f + 2, (float) posY - 4,
										RenderUtils.rgba(30, 30, 30, 150));
								Fonts.MNTSB_16.drawString(name, center - nameWidth / 2f, (float) posY - 12,
										RenderUtils.rgba(215, 215, 215, 255));
								maxOffsetY += 15;
							}
							if (msb.get(1) || msb.get(2) || msb.get(3)) {
								final List<ItemStack> stacks = new ArrayList<>();
								if (msb.get(2)) {
									stacks.add(contextEntity.getHeldItemMainhand());
								}
								if (msb.get(3)) {
									stacks.add(contextEntity.getHeldItemOffhand());
								}
								if (msb.get(1)) {
									entity.getArmorInventoryList().forEach(stacks::add);
								}
								stacks.removeIf(w -> w.getItem() instanceof ItemAir);
								int totalSize = stacks.size() * 10;
								if (!stacks.isEmpty() && msb.get(5)) {
									RenderUtils.drawRect(center - totalSize - 2, posY - maxOffsetY - 19,
											center + totalSize + 2, posY - maxOffsetY,
											RenderUtils.rgba(30, 30, 30, 150));
								}
								maxOffsetY += msb.get(5) ? 19 : 18;
								int iterable = 0;
								for (ItemStack stack : stacks) {
									if (stack != null) {
										RenderHelper.enableGUIStandardItemLighting();
										drawItemStack(stack, center + iterable * 20 - totalSize + 2,
												posY - maxOffsetY + 1, null, false);
										RenderHelper.disableStandardItemLighting();
										iterable++;
										ArrayList<String> lines = new ArrayList<>();
										buildEnchantments(lines, stack);
										if (msb.get(5))
											RenderUtils.drawRect(center + iterable * 20 - totalSize - 20,
													posY - maxOffsetY - lines.size() * 7 - 4,
													center + iterable * 20 - totalSize, posY - maxOffsetY,
													RenderUtils.rgba(30, 30, 30, 150));
										int i = 0;
										for (String s : lines) {
											Fonts.MNTSB_12.drawCenteredString(s,
													center + iterable * 20 - totalSize - 10,
													(float) posY - maxOffsetY - (msb.get(5) ? 6 : 4) - (i * 7),
													RenderUtils.rgba(200, 200, 200, 255));
											i++;
										}
									}
								}
							}
							if (msb.get(4)) {
								List<String> potions = new ArrayList<>();
								for (PotionEffect potionEffect : ((EntityLivingBase) entity).getActivePotionEffects()) {
									String power = "";
									ChatFormatting potionColor = null;
									if ((potionEffect.getDuration() < 200)) {
										potionColor = ChatFormatting.RED;
									} else if (potionEffect.getDuration() < 400) {
										potionColor = ChatFormatting.GOLD;
									} else if (potionEffect.getDuration() > 400) {
										potionColor = ChatFormatting.GREEN;
									}
									if (potionEffect.getDuration() != 0) {
										if (potionEffect.getAmplifier() == 0) {
											power = "I";
										} else if (potionEffect.getAmplifier() == 1) {
											power = "II";
										} else if (potionEffect.getAmplifier() == 2) {
											power = "III";
										} else if (potionEffect.getAmplifier() == 3) {
											power = "IV";
										} else if (potionEffect.getAmplifier() == 4) {
											power = "V";
										}
										potions.add(I18n.format(potionEffect.getPotion().getName()) + " " + power
												+ TextFormatting.GRAY + " " + potionColor + getDuration(potionEffect));
									}
								}
								float startY = (float) (endPosY + 3);
								potions.sort(Comparator.comparingInt(String::length));
								for (String s : potions) {
									Fonts.MNTSB_14.drawCenteredString(s, center, startY,
											RenderUtils.rgba(200, 200, 200, 255));
									startY += 8;
								}
							}
						}
					}
					mc.entityRenderer.setupOverlayRendering(2);
					GlStateManager.enableDepth();
					GlStateManager.enableBlend();
					GlStateManager.enableAlpha();
				}
			}
		}
		if (event instanceof EventTick) {
			astolfo.update();
		}
	}

	public static String getDuration(PotionEffect potionEffect) {
		if (potionEffect.getIsPotionDurationMax()) {
			return "**:**";
		} else {
			return StringUtils.ticksToElapsedTime(potionEffect.getDuration());
		}
	}

	public static void buildEnchantments(ArrayList<String> list, ItemStack stack) {
		Item item = stack.getItem();
		int protection = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);
		int thorns = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, stack);
		int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
		int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
		int feather = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, stack);
		int depth = EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack);
		int vanishing_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, stack);
		int binding_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURCE, stack);
		int sweeping = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
		int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
		int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
		int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
		int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
		int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
		int flame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);
		int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
		int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
		int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
		int silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
		int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
		int fireprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack);
		int blastprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);

		if (item instanceof ItemAxe) {
			if (sharpness > 0) {
				list.add("Shr" + sharpness);
			}
			if (efficiency > 0) {
				list.add("Eff" + efficiency);
			}
			if (unbreaking > 0) {
				list.add("Unb" + unbreaking);
			}
		}
		if (item instanceof ItemArmor) {
			if (vanishing_curse > 0) {
				list.add("Vanish ");
			}
			if (fireprot > 0) {
				list.add("Firp" + fireprot);
			}
			if (blastprot > 0) {
				list.add("Bla" + blastprot);
			}
			if (binding_curse > 0) {
				list.add("§cBindi ");
			}
			if (depth > 0) {
				list.add("Dep" + depth);
			}
			if (feather > 0) {
				list.add("Fea" + feather);
			}
			if (protection > 0) {
				list.add("Pro" + protection);
			}
			if (thorns > 0) {
				list.add("Thr" + thorns);
			}
			if (mending > 0) {
				list.add("Men ");
			}
			if (unbreaking > 0) {
				list.add("Unb" + unbreaking);
			}
		}
		if (item instanceof ItemBow) {
			if (vanishing_curse > 0) {
				list.add("Vanish ");
			}
			if (binding_curse > 0) {
				list.add("Binding ");
			}
			if (infinity > 0) {
				list.add("Inf" + infinity);
			}
			if (power > 0) {
				list.add("Pow" + power);
			}
			if (punch > 0) {
				list.add("Pun" + punch);
			}
			if (mending > 0) {
				list.add("Men ");
			}
			if (flame > 0) {
				list.add("Fla" + flame);
			}
			if (unbreaking > 0) {
				list.add("Unb" + unbreaking);
			}
		}
		if (item instanceof ItemSword) {
			if (vanishing_curse > 0) {
				list.add("Vanish ");
			}
			if (looting > 0) {
				list.add("Loot" + looting);
			}
			if (binding_curse > 0) {
				list.add("Bindi ");
			}
			if (sweeping > 0) {
				list.add("Swe" + sweeping);
			}
			if (sharpness > 0) {
				list.add("Shr" + sharpness);
			}
			if (knockback > 0) {
				list.add("Kno" + knockback);
			}
			if (fireAspect > 0) {
				list.add("Fir" + fireAspect);
			}
			if (unbreaking > 0) {
				list.add("Unb" + unbreaking);
			}
			if (mending > 0) {
				list.add("Men ");
			}
		}
		if (item instanceof ItemTool) {
			if (unbreaking > 0) {
				list.add("Unb" + unbreaking);
			}
			if (mending > 0) {
				list.add("Men ");
			}
			if (vanishing_curse > 0) {
				list.add("Vanish ");
			}
			if (binding_curse > 0) {
				list.add("Binding ");
			}
			if (efficiency > 0) {
				list.add("Eff" + efficiency);
			}
			if (silktouch > 0) {
				list.add("Sil" + silktouch);
			}
			if (fortune > 0) {
				list.add("For" + fortune);
			}
		}
	}

	public static void drawItemStack(ItemStack stack, double x, double y, String altText, boolean withoutOverlay) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
		if (!withoutOverlay)
			mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, altText);
		GL11.glPopMatrix();
	}

	public static class PredictedPosition {
		public ColorShell color;
		public Vec3d pos;
		public int tick;
	}
}
