package ua.apraxia.modules.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
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
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.eventapi.events.impl.render.EventRender3D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.font.Fonts;
import ua.apraxia.utility.math.MathUtility;
import ua.apraxia.utility.render.ColorUtilityAlt;
import ua.apraxia.utility.render.RenderUtility;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

public class NameTags extends Module {
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap<EntityLivingBase, double[]>();
    public BooleanSetting armor = new BooleanSetting("Show Armor", true);
    public BooleanSetting potion = new BooleanSetting("Show Potions", true);
   // public BooleanSetting backGround = new BooleanSetting("NameTags Background", true);
    public SliderSetting size = new SliderSetting("NameTags Size", 1.0f, 0.5f, 2.0f, 0.1f);

    public NameTags() {
        super("Name Tags", Categories.Render);
        addSetting(armor, potion);
    }

    public static TextFormatting getHealthColor(float health) {
        if (health <= 4.0f) {
            return TextFormatting.RED;
        }
        if (health <= 8.0f) {
            return TextFormatting.GOLD;
        }
        if (health <= 12.0f) {
            return TextFormatting.YELLOW;
        }
        if (health <= 16.0f) {
            return TextFormatting.DARK_GREEN;
        }
        return TextFormatting.GREEN;
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        updatePositions();
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        ScaledResolution sr = new ScaledResolution(mc);
        GlStateManager.pushMatrix();
        for (EntityLivingBase entity : entityPositions.keySet()) {
            if (entity == null || entity == mc.player)
                continue;
            GlStateManager.pushMatrix();
            if (entity instanceof EntityPlayer) {
                double[] array = entityPositions.get(entity);
                if (array[3] < 0.0 || array[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                double scaleFactor = sr.getScaleFactor();
                GlStateManager.translate(array[0] / scaleFactor, array[1] / scaleFactor + (double) 0, 0.0);
                String stringName = entity.getDisplayName().getUnformattedText().contains("\uff27\uff25\uff26\uff25\uff33\uff34") ? "ХУЕСОС" : entity.getDisplayName().getUnformattedText();
              //  float width = (float) (mc.fontRenderer.getStringWidth(stringName) + 5);
                GlStateManager.translate(0.0, -10.0, 0.0);
                String stringHP = MathUtility.round(entity.getHealth(), 1) + " ";
                String stringHP2 = "" + stringHP;
                float width = (float) (mc.fontRenderer.getStringWidth(stringHP2 + " " + stringName));
                boolean hp = false;

                if (entity.getHealth() > 20) {
                    hp = true;
                } else {
                    hp = false;
                }
               // RenderUtility.drawBlurredShadow(-width / 2 , -(mc.fontRenderer.FONT_HEIGHT +2), mc.fontRenderer.getStringWidth(stringName + " " + getHealthColor(entity.getHealth())+ stringHP2), 15, 2, new Color(1, 1, 1, 120));
                mc.fontRenderer.drawString(stringName + " " + getHealthColor(entity.getHealth())+ stringHP2, (int) (-width / 2.0f + 2.0f), (int) -7.5, -1);
                ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);
                ArrayList<ItemStack> list = new ArrayList<>();
                for (int i = 0; i < 5; ++i) {
                    ItemStack getEquipmentInSlot = ((EntityPlayer) entity).getEquipmentInSlot(i);
                    list.add(getEquipmentInSlot);
                }
                int n10 = -(list.size() * 9) - 8;
                if (armor.value) {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0.0f, -2.0f, 0.0f);
                    mc.getRenderItem().renderItemIntoGUI(heldItemStack, n10 + 86, -28);
                    mc.getRenderItem().renderItemOverlays(mc.fontRenderer, heldItemStack, n10 + 86, -28);

                    for (ItemStack itemStack : list) {
                        RenderHelper.enableGUIStandardItemLighting();
                        mc.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -28);
                        mc.getRenderItem().renderItemOverlays(mc.fontRenderer, itemStack, n10 + 5, -28);
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
                if (potion.value) {
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
        if (entity.getTotalArmorValue() > 0 || !entity.getHeldItemMainhand().isEmpty() && !entity.getHeldItemOffhand().isEmpty()) {
            stringY -= 37;
        }
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
            float xValue = (float) tagWidth - ((float) mc.fontRenderer.getStringWidth(I18n.format(potion.getName()) + " " + power + TextFormatting.GRAY + " " + Potion.getDurationString(potionEffect)) / 2.0f);

            mc.fontRenderer.drawString(I18n.format(potion.getName()) + " " + power + TextFormatting.GRAY + " " + Potion.getDurationString(potionEffect), (int) xValue, stringY, -1);

            stringY -= 10;
            GlStateManager.popMatrix();
        }
    }

    private void drawEnchantTag(String text, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n2 -= 7;
        mc.fontRenderer.drawString(text, n + 6, -35 - n2, -1);
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
        float pTicks = mc.timer.renderPartialTicks;
        for (Entity o : mc.world.loadedEntityList) {
            if (o == mc.player || !(o instanceof EntityPlayer)) continue;
            double x = o.lastTickPosX + (o.posX - o.lastTickPosX) * (double) pTicks - mc.getRenderManager().viewerPosX;
            double y = o.lastTickPosY + (o.posY - o.lastTickPosY) * (double) pTicks - mc.getRenderManager().viewerPosY;
            double z = o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * (double) pTicks - mc.getRenderManager().viewerPosZ;
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
        float n = mc.gameSettings.smoothCamera ? 2.0f : size.value;
        GlStateManager.scale(n, n, n);
    }
}