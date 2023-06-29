package ru.rockstar.client.features.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import optifine.CustomColors;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.event.event.EventNameTags;
import ru.rockstar.api.utils.math.MathHelper;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.display.ClientFont;
import ru.rockstar.client.ui.espgble.impl.NameTagsComponent;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NameTags extends Feature {

    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap();
    public static NumberSetting ntsize;
    public NumberSetting borderOpacity;
    public static BooleanSetting ntbg;
    public BooleanSetting showArmor;
    public BooleanSetting showPotions;
    public static BooleanSetting showCoords;
    public BooleanSetting baxerCheck;


    public NameTags() {
        super("NameTags", "Показывает игроков, ник, броню и их здоровье сквозь стены", 0, Category.VISUALS);
        ntsize = new NumberSetting("NameTags Size", "Размер неймтагов", 1F, 0.5F, 2F, 0.1f, () -> true);
        ntbg = new BooleanSetting("NameTags Glow", "Накладывает красивый эффект", false, () -> true);
        borderOpacity = new NumberSetting("Border Opacity", "Прозрачность бэк-граунда", 10, 0, 255, 1, () -> ntbg.getBoolValue());
        showArmor = new BooleanSetting("Show Armor", true, () -> true);
        showPotions = new BooleanSetting("Show Potion", true, () -> true);
        showCoords = new BooleanSetting("Show Coords", false, () -> true);
        baxerCheck = new BooleanSetting("Baxer Check", false, () -> true);
        addSettings(ntsize,ntbg,showArmor,showPotions, showCoords);
    }

    @EventTarget
    public void onNickRemove(EventNameTags event) {
        if (isToggled()) {
            event.setCancelled(true);
        }
    }

    public static TextFormatting getHealthColor(float health) {
        if (health <= 4)
            return TextFormatting.RED;
        else if (health <= 8)
            return TextFormatting.GOLD;
        else if (health <= 12)
            return TextFormatting.YELLOW;
        else if (health <= 16)
            return TextFormatting.DARK_GREEN;
        else
            return TextFormatting.GREEN;
    }

    @EventTarget
    public void on3D(Event3D event) {
        try {
            updatePositions();
        } catch (Exception ignored) {
        }
    }

    @EventTarget
    public void on2D(Event2D event) {

        GlStateManager.pushMatrix();
        for (EntityLivingBase entity : entityPositions.keySet()) {
            double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.getRenderPartialTicks()) - mc.getRenderManager().renderPosX;
            double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.getRenderPartialTicks()) - mc.getRenderManager().renderPosY;
            double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.getRenderPartialTicks()) - mc.getRenderManager().renderPosZ;

            boolean hui = false;
        	if (entity.getHeldItemOffhand().getItem() == Items.END_CRYSTAL || entity.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
        		if (!Main.instance.friendManager.isFriend(entity.getName())) {
        			hui = true;
        		}
        	}
        	
            GlStateManager.pushMatrix();
           	 String aboba = (baxerCheck.getBoolValue()) ? (hui ? TextFormatting.RED + " Baxer" : "") : "";
            if (entity instanceof EntityPlayer && entity != mc.player) {
                double[] array = entityPositions.get(entity);
                if (array[3] < 0.0 || array[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                ScaledResolution sr = new ScaledResolution(mc);
                
                GlStateManager.translate((array[0]) / sr.getScaleFactor(), (array[1]) / sr.getScaleFactor(), 0.0);
                
                this.scale();
                String string = "";
                if (Main.instance.featureDirector.getFeatureByClass(NameProtect.class).isToggled()) {
                    string = "Protected";
                } else if (Main.instance.friendManager.isFriend(entity.getName())) {
                    string = ChatFormatting.GREEN + "[F] " + ChatFormatting.RESET + entity.getDisplayName().getUnformattedText();
                } else {
                    string = entity.getDisplayName().getUnformattedText();
                }
                String string2 = "" + MathHelper.round(entity.getHealth(), 1);
                if (showCoords.getBoolValue()) {
                	float width = mc.mntsb_20.getStringWidth("XYZ: " + (int) entity.posX + " " + (int) entity.posY + " " + (int) entity.posZ + " | " + string2 + " " + string + aboba) + 2;
                    if (ntbg.getBoolValue()) {
                    	GlStateManager.enableBlend();
                    	GlStateManager.enableAlpha();
                        DrawHelper.drawRectWithGlow((-width / 2), -10.0D, (width / 2 + 5), 3, 7, 5, new Color(1,1,1,255));
                        GlStateManager.disableBlend();
                    }
                    mc.mntsb_20.drawStringWithShadow(TextFormatting.GRAY + "XYZ: " + (int) entity.posX + " " + (int) entity.posY + " " + (int) entity.posZ + " | " + string + " " + getHealthColor(entity.getHealth()) + string2 + aboba, (-width / 2 + 2), -7.5f, -1);
                } else {
                	float width = mc.mntsb_20.getStringWidth(/*string2 + " " +*/ string) + 2;
                    GlStateManager.translate(0.0D, -10, 0.0D);
                    
                    GlStateManager.enableBlend();
                	GlStateManager.enableAlpha();
                   
                    if (ntbg.getBoolValue()) {
                        RenderUtils.drawBlurredShadow((-width / 2), (float) (-10.0D), (width), 10, 15, new Color(0,0,0));
                        
                        
                    }
                    DrawHelper.drawSmoothRect((-width / 2), (float) (-10.0D), (width / 2), 3, new Color(1,1,1,150).getRGB());

                    
                    
                    
                    DrawHelper.drawRect((-width / 40) * 20 - 1, (float) ( 2.0D), (width / 40) * 20 + 1, 5,ClientHelper.getClientColor().darker().getRGB());

                    
                    if (ntbg.getBoolValue()) {
                        RenderUtils.drawBlurredShadow((-width / 40) * entity.getHealth() - 1, (float) ( 2.0D), (width / 20) * entity.getHealth() + 3, 3,  7, ClientHelper.getClientColor());
                        
                        
                    }
                    
                    DrawHelper.drawRect((-width / 40) * entity.getHealth() - 1, (float) ( 2.0D), (width / 40) * entity.getHealth() + 1, 5,ClientHelper.getClientColor().getRGB());

                    
                    GlStateManager.disableBlend();

                    mc.mntsb_20.drawStringWithShadow(string + "ＧＥＦＥＳＴ " + /*getHealthColor(entity.getHealth()) + string2 +*/ aboba, (-width / 2) + 2, -7.5f, -1);
               
                    mc.mntsb_15.drawCenteredStringWithShadow("" + (int) (entity.getHealth() / 2 * 10),(0) , 7, /*entity.getHealth() < 10 ? 0 : */-1);
                }
                if(showPotions.getBoolValue()) {
                    float yPotion = (float) (y - 60);
                    for (PotionEffect effectPotion : entity.getActivePotionEffects()) {
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        Potion effect = Potion.getPotionById(CustomColors.getPotionId(effectPotion.getEffectName()));
                        if (effect != null) {

                            ChatFormatting getPotionColor = null;
                            if ((effectPotion.getDuration() < 200)) {
                                getPotionColor = ChatFormatting.RED;
                            } else if (effectPotion.getDuration() < 400) {
                                getPotionColor = ChatFormatting.GOLD;
                            } else if (effectPotion.getDuration() > 400) {
                                getPotionColor = ChatFormatting.GRAY;
                            }

                            String durationString = Potion.getDurationString(effectPotion);

                            String level = I18n.format(effect.getName());
                            if (effectPotion.getAmplifier() == 1) {
                                level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.2") + " (" + getPotionColor + durationString + ")";
                            } else if (effectPotion.getAmplifier() == 2) {
                                level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.3") + " (" + getPotionColor + durationString + ")";
                            } else if (effectPotion.getAmplifier() == 3) {
                                level = level + " " + ChatFormatting.GRAY + I18n.format("enchantment.level.4") + " (" + getPotionColor + durationString + ")";
                            }

                            if (showCoords.getBoolValue()) {
                            	float width = mc.mntsb_20.getStringWidth("XYZ: " + (int) entity.posX + " " + (int) entity.posY + " " + (int) entity.posZ + " | " + string2 + " " + string) + 2;
                            	mc.fontRendererObj.drawStringWithShadow(level, MathematicHelper.getMiddle((int) -width + 100, (int) width + 100) - width, yPotion,-1);
                            } else {
                            	float width = mc.mntsb_20.getStringWidth(string2 + " " + string) + 2;
                            	mc.fontRendererObj.drawStringWithShadow(level, MathematicHelper.getMiddle((int) -width + 100 - 85, (int) width + 100) - width, yPotion,-1);
                            }
                        }
                        yPotion -= 10;
                        GL11.glEnable(GL11.GL_DEPTH_TEST);

                    }
                }
                ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);

                if (showArmor.getBoolValue()) {
                    ArrayList<ItemStack> list = new ArrayList<>();
                    for (int i = 0; i < 5; ++i) {
                        ItemStack getEquipmentInSlot = ((EntityPlayer) entity).getEquipmentInSlot(i);
                        list.add(getEquipmentInSlot);
                    }
                    int n10 = -(list.size() * 9);
                    mc.getRenderItem().renderItemIntoGUI(heldItemStack, n10 + 105 - (mc.fontRendererObj.getStringWidth("" + n10)), -29);
                    mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, heldItemStack, n10 + 105 - (mc.fontRendererObj.getStringWidth("" + n10)), -28);
                    for (ItemStack itemStack : list) {
                        net.minecraft.client.renderer.RenderHelper.enableGUIStandardItemLighting();
                        mc.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -28);
                        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, n10 + 5, -26);
                        n10 += 3;
                        RenderHelper.disableStandardItemLighting();
                        int n11 = 7;
                        int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), itemStack);
                        int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), itemStack);
                        int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(19)), itemStack);
                        if (getEnchantmentLevel > 0) {
                            this.drawEnchantTag("S" + this.getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
                            n11 += 8;
                        }
                        if (getEnchantmentLevel2 > 0) {
                            this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
                            n11 += 8;
                        }
                        if (getEnchantmentLevel3 > 0) {
                            this.drawEnchantTag("Kb" + this.getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
                        } else if (itemStack.getItem() instanceof ItemArmor) {
                            int getEnchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(0)), itemStack);
                            int getEnchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(7)), itemStack);
                            int getEnchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), itemStack);
                            if (getEnchantmentLevel4 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel4) + getEnchantmentLevel4, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel5 > 0) {
                                this.drawEnchantTag("Th" + this.getColor(getEnchantmentLevel5) + getEnchantmentLevel5, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel6 > 0) {
                                this.drawEnchantTag("U" + this.getColor(getEnchantmentLevel6) + getEnchantmentLevel6, n10, n11);
                            }
                        } else if (itemStack.getItem() instanceof ItemBow) {
                            int getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(48)), itemStack);
                            int getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(49)), itemStack);
                            int getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(50)), itemStack);
                            if (getEnchantmentLevel7 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel8 > 0) {
                                this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel9 > 0) {
                                this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                            }
                        }
                        int n12 = (int) Math.round(255.0 - itemStack.getItemDamage() * 255.0 / itemStack.getMaxDamage());
                        new Color(255 - n12 << 16 | n12 << 8).brighter();
                        if (itemStack.getMaxDamage() - itemStack.getItemDamage() > 0) {
                            GlStateManager.pushMatrix();
                            GlStateManager.disableDepth();
                            GlStateManager.enableDepth();
                            GlStateManager.popMatrix();
                        }
                        n10 += 13.5;
                    }
                }
                GlStateManager.popMatrix();
            }
        }
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
    }

    private void drawEnchantTag(String text, int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n2 -= 7;
        if (!ClientFont.minecraftfont.getBoolValue()) {
            ClientHelper.getFontRender().drawStringWithShadow(text, n + 6, -35 - n2, -1);
        } else {
            mc.fontRendererObj.drawStringWithShadow(text, n + 6, -35 - n2, -1);
        }
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
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity != mc.player) {
                double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * pTicks - mc.getRenderManager().renderPosX;
                double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * pTicks - mc.getRenderManager().renderPosY;
                double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * pTicks - mc.getRenderManager().renderPosZ;
                y += entity.height + 0.2D;
                if ((Objects.requireNonNull(convertTo2D(x, y, z))[2] >= 0.0D) && (Objects.requireNonNull(convertTo2D(x, y, z))[2] < 1.0D)) {
                    entityPositions.put((EntityPlayer) entity, new double[]{Objects.requireNonNull(convertTo2D(x, y, z))[0], Objects.requireNonNull(convertTo2D(x, y, z))[1], Math.abs(convertTo2D(x, y + 1.0D, z, entity)[1] - convertTo2D(x, y, z, entity)[1]), convertTo2D(x, y, z)[2]});
                }
            }
        }
    }

    private double[] convertTo2D(double x, double y, double z, Entity ent) {
        float pTicks = mc.timer.renderPartialTicks;
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        double[] convertedPoints = convertTo2D(x, y, z);
        mc.entityRenderer.setupCameraTransform(pTicks, 0);
        return convertedPoints;
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
            return new double[]{screenCords.get(0), Display.getHeight() - screenCords.get(1), screenCords.get(2)};
        }
        return null;
    }

    private void scale() {
        float n = (mc.gameSettings.smoothCamera ? 2.0f : ntsize.getNumberValue());
        GlStateManager.scale(n, n, n);
    }
}