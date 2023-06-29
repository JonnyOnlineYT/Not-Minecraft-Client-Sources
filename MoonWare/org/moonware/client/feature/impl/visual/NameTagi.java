package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.Formatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.moonware.client.utils.MWFont;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender2D;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.combat.AntiBot;
import org.moonware.client.feature.impl.misc.StreamerMode;
import org.moonware.client.feature.impl.visual.util.ColorUtils;
import org.moonware.client.feature.impl.visual.util.RenderUtils2;
import org.moonware.client.helpers.Helper;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NameTagi extends Feature {
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap<EntityLivingBase, double[]>();
    public BooleanSetting armor = new BooleanSetting("Show Armor", true, () -> true);
    public BooleanSetting potion = new BooleanSetting("Show Potions", true, () -> true);
    public BooleanSetting glowNameTags = new BooleanSetting("Glow Potions", true, () -> true);

    public BooleanSetting backGround = new BooleanSetting("NameTags Background", true, () -> true);
    public BooleanSetting offHand = new BooleanSetting("OffHand Render", true, () -> true);

    public ListSetting backGroundMode = new ListSetting("Background Mode", "Shader", () -> false, "Default", "Shader");
    public NumberSetting opacity = new NumberSetting("Background Opacity", 150.0f, 0.0f, 255.0f, 5.0f, () -> backGround.getBoolValue());
    public NumberSetting size = new NumberSetting("NameTags Size", 1.0f, 0.5f, 2.0f, 0.1f, () -> true);

    public NameTagi() {
        super("NameTags", "Показывает игроков, ник, броню и их здоровье сквозь стены", Type.Visuals);
        addSettings(size, backGround, backGroundMode, opacity, offHand, armor, potion, glowNameTags);
    }

    public static Formatting getHealthColor(float health) {
        if (health <= 4.0f) {
            return Formatting.RED;
        }
        if (health <= 8.0f) {
            return Formatting.GOLD;
        }
        if (health <= 12.0f) {
            return Formatting.YELLOW;
        }
        if (health <= 16.0f) {
            return Formatting.DARK_GREEN;
        }
        return Formatting.GREEN;
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        updatePositions();
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        ScaledResolution sr = new ScaledResolution(Helper.mc);
        GlStateManager.pushMatrix();
        for (EntityLivingBase entity : entityPositions.keySet()) {

            if (entity == null || entity == Minecraft.player || AntiBot.isBotPlayer.contains(entity))
                continue;
            GlStateManager.pushMatrix();
            if (entity instanceof EntityPlayer) {
                double[] array = entityPositions.get(entity);
                if (array[3] < 0.0 || array[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                double scaleFactor = sr.getScaleFactor();
                GlStateManager.translate(array[0] / scaleFactor, array[1] / scaleFactor + (double) (MoonWare.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Chni") || CustomModel.modelMode.currentMode.equals("Red Panda")) && !CustomModel.onlyMe.getBoolValue() ? 20 : 0), 0.0);
                String stringName = MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue() ? "Protected" : (MoonWare.friendManager.isFriend(entity.getName()) ? (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.friendNames.getBoolValue() ? Formatting.GREEN + "F" + "Protected" : Formatting.GREEN + "[F] " + Formatting.RESET + entity.getDisplayName().asString()) : entity.getDisplayName().asString());
                if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) {
                    stringName = "Protected";
                } else if (MoonWare.friendManager.isFriend(entity.getName())) {
                    stringName = Formatting.GREEN + "[F] " + Formatting.RESET + entity.getDisplayName().asString();
                } else {
                    stringName = entity.getDisplayName().asString();
                }
                String stringHP = MathematicHelper.round(entity.getHealth(), 1) + " ";
                String stringHP2 = "" + stringHP;
                float width = (float) (MWFont.MONTSERRAT_BOLD.get(18).getWidth(stringHP2 + " " + stringName) + 5);
                GlStateManager.translate(0.0, -10.0, 0.0);
                if (backGround.getBoolValue() && backGroundMode.currentMode.equals("Default")) {
                    RenderUtils2.drawRect(-width / 2.0f, -10.0, width / 2.0f, 2.0, ColorUtils.getColor(0, (int) opacity.getNumberValue()));
                } else if (backGroundMode.currentMode.equals("Shader")) {
                    //RenderUtils2.drawBlur(7,() -> RenderUtils2.drawBlurredShadow(-width / 2.0f - 2.0f, -10.0f, width + 3.0f, 11.0f, 7, new Color(0, 0, 0, (int) opacity.getNumberValue())));
                    RenderUtils2.drawBlurredShadow(-width / 2.0f - 2.0f, -10.0f, width + 3.0f, 11.0f, 7, new Color(0, 0, 0, (int) opacity.getNumberValue()));
                }
                MWFont.MONTSERRAT_BOLD.get(18).drawShadow(stringName + " " + getHealthColor(entity.getHealth()) + stringHP2, -width / 2.0f + 2.0f, -7.5, -1);
                ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);
                ArrayList<ItemStack> list = new ArrayList<>();
                for (int i = 0; i < 5; ++i) {
                    ItemStack getEquipmentInSlot = ((EntityPlayer) entity).getEquipmentInSlot(i);
                    list.add(getEquipmentInSlot);
                }
                int n10 = -(list.size() * 9) - 8;
                if (armor.getBoolValue()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.0f, -2.0f, 0.0f);
                    Minecraft.getRenderItem().renderItemIntoGUI(heldItemStack, n10 + 86, -28);
                    Minecraft.getRenderItem().renderItemOverlays(MWFont.MONTSERRAT_BOLD.get(18), heldItemStack, n10 + 86, -28);

                    for (ItemStack itemStack : list) {
                        RenderHelper.enableGUIStandardItemLighting();
                        Minecraft.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -28);
                        Minecraft.getRenderItem().renderItemOverlays(MWFont.MONTSERRAT_BOLD.get(18), itemStack, n10 + 5, -28);
                        n10 += 3;
                        RenderHelper.disableStandardItemLighting();
                        int n11 = 7;
                        int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), itemStack);
                        int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), itemStack);
                        int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(19)), itemStack);
                        if (getEnchantmentLevel > 0) {
                            drawEnchantTag("S" + getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
                            n11 += 8;
                        }
                        if (getEnchantmentLevel2 > 0) {
                            drawEnchantTag("F" + getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
                            n11 += 8;
                        }
                        if (getEnchantmentLevel3 > 0) {
                            drawEnchantTag("Kb" + getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
                        } else if (itemStack.getItem() instanceof ItemArmor) {
                            int getEnchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(0)), itemStack);
                            int getEnchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(7)), itemStack);
                            int getEnchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), itemStack);
                            if (getEnchantmentLevel4 > 0) {
                                drawEnchantTag("P" + getColor(getEnchantmentLevel4) + getEnchantmentLevel4, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel5 > 0) {
                                drawEnchantTag("Th" + getColor(getEnchantmentLevel5) + getEnchantmentLevel5, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel6 > 0) {
                                drawEnchantTag("U" + getColor(getEnchantmentLevel6) + getEnchantmentLevel6, n10, n11);
                            }
                        } else if (itemStack.getItem() instanceof ItemBow) {
                            int getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(48)), itemStack);
                            int getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(49)), itemStack);
                            int getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(50)), itemStack);
                            if (getEnchantmentLevel7 > 0) {
                                drawEnchantTag("P" + getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel8 > 0) {
                                drawEnchantTag("P" + getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel9 > 0) {
                                drawEnchantTag("F" + getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                            }
                        }
                        n10 = (int) ((double) n10 + 13.5);
                    }
                    GlStateManager.popMatrix();
                }
                if (potion.getBoolValue()) {
                    drawPotionEffect((EntityPlayer) entity);
                }
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
    }

    private void drawPotionEffect(EntityPlayer entity) {
        int tagWidth = 0;
        int stringY = -25;
        for (PotionEffect potionEffect : entity.getActivePotionEffects()) {
            Potion potion = potionEffect.getPotion();
            boolean potRanOut = (double) potionEffect.getDuration() != 0.0;
            String power = "";
            if (!entity.isPotionActive(potion) || !potRanOut) continue;
            if (potionEffect.getAmplifier() == 1) {
                power = "II";
            } else if (potionEffect.getAmplifier() == 2) {
                power = "III";
            } else if (potionEffect.getAmplifier() == 3) {
                power = "IV";
            }
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            float xValue = (float) tagWidth - ((float) MWFont.MONTSERRAT_BOLD.get(18).getWidth(I18n.format(potion.getName()) + " " + power + Formatting.GRAY + " " + Potion.getDurationString(potionEffect)) / 2.0f);
            if (glowNameTags.getBoolValue()) {
                MWFont.MONTSERRAT_BOLD.get(18).drawBlurredStringWithShadow(I18n.format(potion.getName()) + " " + power + Formatting.GRAY + " " + Potion.getDurationString(potionEffect), xValue, stringY, 20, new Color(255, 255, 255, 60), -1);
            } else {
                MWFont.MONTSERRAT_BOLD.get(18).drawShadow(I18n.format(potion.getName()) + " " + power + Formatting.GRAY + " " + Potion.getDurationString(potionEffect), xValue, stringY, -1);

            }
            stringY -= 10;
            GlStateManager.popMatrix();
        }
        //DrawHelper.startSmooth();
    }

    private void drawEnchantTag(String text, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n2 -= 7;
        MWFont.MONTSERRAT_BOLD.get(18).drawShadow(text, n + 6, -35 - n2, -1);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private String getColor(int n) {
        if (n != 1) {
            if (n == 2) {
                return "";
            }
            if (n == 3) {
                return "";
            }
            if (n == 4) {
                return "";
            }
            if (n >= 5) {
                return "";
            }
        }
        return "";
    }

    private void updatePositions() {
        entityPositions.clear();
        float pTicks = Minecraft.timer.renderPartialTicks;
        for (Entity o : Minecraft.world.loadedEntityList) {
            if (o == Minecraft.player || !(o instanceof EntityPlayer)) continue;
            double x = o.lastTickPosX + (o.posX - o.lastTickPosX) * (double) pTicks - Minecraft.getRenderManager().viewerPosX;
            double y = o.lastTickPosY + (o.posY - o.lastTickPosY) * (double) pTicks - Minecraft.getRenderManager().viewerPosY;
            double z = o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * (double) pTicks - Minecraft.getRenderManager().viewerPosZ;
            if (!(Objects.requireNonNull(convertTo2D(x, y += (double) o.height + 0.2, z))[2] >= 0.0) || !(Objects.requireNonNull(convertTo2D(x, y, z))[2] < 2.0))
                continue;
            entityPositions.put((EntityPlayer) o, new double[]{Objects.requireNonNull(convertTo2D(x, y, z))[0], Objects.requireNonNull(convertTo2D(x, y, z))[1], Math.abs(Objects.requireNonNull(convertTo2D(x, y + 1.0, z))[1] - Objects.requireNonNull(convertTo2D(x, y, z))[1]), Objects.requireNonNull(convertTo2D(x, y, z))[2]});
        }
    }

    private double[] convertTo2D(double x, double y, double z) {
        FloatBuffer screenCords = BufferUtils.createFloatBuffer(3);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        boolean result = GLU.gluProject((float) x, (float) y, (float) z, modelView, projection, viewport, screenCords);
        if (result) {
            return new double[]{screenCords.get(0), (float) Display.getHeight() - screenCords.get(1), screenCords.get(2)};
        }
        return null;
    }

    private void scale() {
        float n = Minecraft.gameSettings.smoothCamera ? 2.0f : size.getNumberValue();
        GlStateManager.scale(n, n, n);
    }
}