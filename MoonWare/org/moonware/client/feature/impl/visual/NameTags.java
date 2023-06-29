package org.moonware.client.feature.impl.visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Formatting;
import optifine.CustomColors;
import org.moonware.client.utils.MWFont;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.render.EventRender3D;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.feature.impl.misc.StreamerMode;
import org.moonware.client.helpers.math.MathematicHelper;
import org.moonware.client.helpers.render.rect.RectHelper;
import org.moonware.client.helpers.render2.RenderHelper2;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.ListSetting;
import org.moonware.client.settings.impl.NumberSetting;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class NameTags extends Feature {

    public BooleanSetting armor;
    public BooleanSetting backGround;
    public NumberSetting opacity;
    public NumberSetting size;
    public static ListSetting hpMode = new ListSetting("Health Mode", "HP", () -> true, "HP", "Percentage");
    public static ListSetting hpformat = new ListSetting("Health Format", "Float", () -> hpMode.currentMode.equalsIgnoreCase("HP"), "Float", "Int");
    public BooleanSetting onlygreenHealth = new BooleanSetting("OnlyGreenHealth", false,() -> true);
    public BooleanSetting self = new BooleanSetting("Self", false,() -> true);

    public NameTags() {
        super("NameTags", "Показывает игроков, ник, броню и их здоровье сквозь стены", Type.Visuals);
        size = new NumberSetting("NameTags Size", 0.5f, 0.2f, 2, 0.01f, () -> true);
        backGround = new BooleanSetting("NameTags Background", true, () -> true);
        opacity = new NumberSetting("Background Opacity", 120, 0, 255, 10, () -> backGround.getBoolValue());
        armor = new BooleanSetting("Show Armor", true, () -> true);
        addSettings(hpMode, hpformat, size,self, backGround, opacity, armor,onlygreenHealth);
    }

    @EventTarget
    public void onRender3d(EventRender3D event) {

        for (EntityPlayer entity : Minecraft.world.playerEntities) {
            if (entity != null) {
                if (self.getBoolValue() == false) {
                    if (Minecraft.player.getDistanceToEntity(entity) > 0.0000000000000000000000001f) {
                        double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks()) - RenderManager.renderPosX;
                        double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()) - RenderManager.renderPosY;
                        double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks()) - RenderManager.renderPosZ;

                        String tag;
                        if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) {
                            tag = "Protected";
                        } else if (MoonWare.friendManager.isFriend(entity.getName())) {
                            tag = Formatting.GREEN + "[F] " + Formatting.RESET + entity.getDisplayName().asString();
                        } else {
                            tag = entity.getDisplayName().asString();
                        }

                        y += (entity.isSneaking() ? 0.5F : 0.7F);
                        float distance = Math.min(Math.max(1.2f * (Minecraft.player.getDistanceToEntity(entity) * 0.15f), 1.25f), 6) * 0.015f;
                        int health = (int) entity.getHealth();
                        if (health <= entity.getMaxHealth() * 0.25F) {
                            tag = tag + "\u00a74";
                        } else if (health <= entity.getMaxHealth() * 0.5F) {
                            tag = tag + "\u00a76";
                        } else if (health <= entity.getMaxHealth() * 0.75F) {
                            tag = tag + "\u00a7e";
                        } else if (health <= entity.getMaxHealth()) {
                            tag = tag + "\u00a72";
                        }

                        String hp;
                        if (hpMode.currentMode.equals("Percentage")) {
                            hp = MathematicHelper.round(entity.getHealth() / entity.getMaxHealth() * 100F, 1) + "%";
                        } else {
                            if (hpformat.currentMode.equalsIgnoreCase("Float")) {
                                hp = MathematicHelper.round(entity.getHealth(), 1) + "";
                            } else {
                                hp = MathematicHelper.round((int) entity.getHealth(), 1) + "";
                            }
                        }
                        if (!onlygreenHealth.getBoolValue()) {
                            if (entity.getHealth() <= 20 && entity.getHealth() >= 16) {
                                tag = tag + " " + Formatting.RESET + "[" + Formatting.GREEN + hp + "HP" + Formatting.RESET + "]";
                            } else if (entity.getHealth() < 16 && entity.getHealth() >= 11) {
                                tag = tag + " " + Formatting.RESET + "[" + Formatting.YELLOW + hp + "HP" + Formatting.RESET + "]";
                            } else if (entity.getHealth() < 11 && entity.getHealth() >= 6) {
                                tag = tag + " " + Formatting.RESET + "[" + Formatting.GOLD + hp + "HP" + Formatting.RESET + "]";
                            } else if (entity.getHealth() < 6) {
                                tag = tag + " " + Formatting.RESET + "[" + Formatting.RED + hp + "HP" + Formatting.RESET + "]";
                            }
                        } else {
                            tag = tag + " " + Formatting.RESET + "[" + Formatting.GREEN + hp + "HP" + Formatting.RESET + "]";
                        }

                        float scale = distance;
                        scale = scale * size.getNumberValue();
                        GL11.glPushMatrix();
                        GL11.glTranslatef((float) x, (float) y + 1.4F, (float) z);
                        GL11.glNormal3f(1, 1, 1);
                        float fixed = Minecraft.gameSettings.thirdPersonView == 2 ? -1 : 1;
                        GL11.glRotatef(-Minecraft.getRenderManager().playerViewY, 0, 1, 0);
                        GL11.glRotatef(Minecraft.getRenderManager().playerViewX, fixed, 0, 0);
                        GL11.glScalef(-scale, -scale, scale);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        int width = MWFont.MONTSERRAT_BOLD.get(18).getWidth(tag) / 2;
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                        if (backGround.getBoolValue()) {
                            RenderHelper2.renderBlurredShadow(new Color(31, 31, 31, 180),-width - 2, -(Minecraft.font.height + 1), width + 2, -(Minecraft.font.height + 1) + 10, 5);
                        }

                        MWFont.MONTSERRAT_BOLD.get(18).drawShadow(tag, MathHelper.average(new long[] {-width - 2, width + 2}) - width, -(Minecraft.font.height - 1), Color.WHITE.getRGB());
                        if (armor.getBoolValue()) {
                            renderArmor(entity, 0, -(Minecraft.font.height + 1) - 20);
                        }
                        float yPotion = (float) (y - 45);
                        for (PotionEffect effectPotion : entity.getActivePotionEffects()) {
                            GL11.glDisable(GL11.GL_DEPTH_TEST);
                            Potion effect = Potion.getPotionById(CustomColors.getPotionId(effectPotion.getEffectName()));
                            if (effect != null) {

                                Formatting getPotionColor = null;
                                if ((effectPotion.getDuration() < 200)) {
                                    getPotionColor = Formatting.RED;
                                } else if (effectPotion.getDuration() < 400) {
                                    getPotionColor = Formatting.GOLD;
                                } else if (effectPotion.getDuration() > 400) {
                                    getPotionColor = Formatting.GRAY;
                                }

                                String durationString = Potion.getDurationString(effectPotion);

                                String level = I18n.format(effect.getName());
                                if (effectPotion.getAmplifier() == 1) {
                                    level = level + " " + Formatting.GRAY + I18n.format("enchantment.level.2") + " (" + getPotionColor + durationString + ")";
                                } else if (effectPotion.getAmplifier() == 2) {
                                    level = level + " " + Formatting.GRAY + I18n.format("enchantment.level.3") + " (" + getPotionColor + durationString + ")";
                                } else if (effectPotion.getAmplifier() == 3) {
                                    level = level + " " + Formatting.GRAY + I18n.format("enchantment.level.4") + " (" + getPotionColor + durationString + ")";
                                }

                                MWFont.MONTSERRAT_BOLD.get(18).drawShadow(level, MathHelper.average(new long[] {-width - 2, width + 2}) - width - 20, yPotion, effectPotion.getPotion().getLiquidColor());
                            }
                            yPotion -= 10;
                            GL11.glEnable(GL11.GL_DEPTH_TEST);
                        }

                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glPopMatrix();
                    }
                } else {
                    double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks()) - RenderManager.renderPosX;
                    double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks()) - RenderManager.renderPosY;
                    double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks()) - RenderManager.renderPosZ;

                    String tag;
                    if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.otherNames.getBoolValue()) {
                        tag = "Protected";
                    } else if (MoonWare.friendManager.isFriend(entity.getName())) {
                        tag = Formatting.GREEN + "[F] " + Formatting.RESET + entity.getDisplayName().asString();
                    } else {
                        tag = entity.getDisplayName().asString();
                    }

                    y += (entity.isSneaking() ? 0.5F : 0.7F);
                    float distance = Math.min(Math.max(1.2f * (Minecraft.player.getDistanceToEntity(entity) * 0.15f), 1.25f), 6) * 0.015f;
                    int health = (int) entity.getHealth();
                    if (health <= entity.getMaxHealth() * 0.25F) {
                        tag = tag + "\u00a74";
                    } else if (health <= entity.getMaxHealth() * 0.5F) {
                        tag = tag + "\u00a76";
                    } else if (health <= entity.getMaxHealth() * 0.75F) {
                        tag = tag + "\u00a7e";
                    } else if (health <= entity.getMaxHealth()) {
                        tag = tag + "\u00a72";
                    }

                    String hp;
                    if (hpMode.currentMode.equals("Percentage")) {
                        hp = MathematicHelper.round(entity.getHealth() / entity.getMaxHealth() * 100F, 1) + "%";
                    } else {
                        if (hpformat.currentMode.equalsIgnoreCase("Float")) {
                            hp = MathematicHelper.round(entity.getHealth(), 1) + "";
                        } else {
                            hp = MathematicHelper.round((int) entity.getHealth(), 1) + "";
                        }
                    }
                    if (!onlygreenHealth.getBoolValue()) {
                        if (entity.getHealth() <= 20 && entity.getHealth() >= 16) {
                            tag = tag + " " + Formatting.RESET + "[" + Formatting.GREEN + hp + "HP" + Formatting.RESET + "]";
                        } else if (entity.getHealth() < 16 && entity.getHealth() >= 11) {
                            tag = tag + " " + Formatting.RESET + "[" + Formatting.YELLOW + hp + "HP" + Formatting.RESET + "]";
                        } else if (entity.getHealth() < 11 && entity.getHealth() >= 6) {
                            tag = tag + " " + Formatting.RESET + "[" + Formatting.GOLD + hp + "HP" + Formatting.RESET + "]";
                        } else if (entity.getHealth() < 6) {
                            tag = tag + " " + Formatting.RESET + "[" + Formatting.RED + hp + "HP" + Formatting.RESET + "]";
                        }
                    } else {
                        tag = tag + " " + Formatting.RESET + "[" + Formatting.GREEN + hp + "HP" + Formatting.RESET + "]";
                    }

                    float scale = distance;
                    scale = scale * size.getNumberValue();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float) x, (float) y + 1.4F, (float) z);
                    GL11.glNormal3f(1, 1, 1);
                    float fixed = Minecraft.gameSettings.thirdPersonView == 2 ? -1 : 1;
                    GL11.glRotatef(-Minecraft.getRenderManager().playerViewY, 0, 1, 0);
                    GL11.glRotatef(Minecraft.getRenderManager().playerViewX, fixed, 0, 0);
                    GL11.glScalef(-scale, -scale, scale);
                    GL11.glDisable(GL11.GL_DEPTH_TEST);
                    int width = MWFont.MONTSERRAT_BOLD.get(18).getWidth(tag) / 2;
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                    if (backGround.getBoolValue()) {
                        RectHelper.drawRectWithGlow(-width - 2, -(Minecraft.font.height + 1), width + 2, -(Minecraft.font.height + 1) + 10, 5, 15, new Color(31, 31, 31, 180));
                    }

                    MWFont.MONTSERRAT_BOLD.get(18).drawShadow(tag, MathHelper.average(new long[] {-width - 2, width + 2}) - width, -(Minecraft.font.height - 1), Color.WHITE.getRGB());
                    if (armor.getBoolValue()) {
                        renderArmor(entity, 0, -(Minecraft.font.height + 1) - 20);
                    }
                    float yPotion = (float) (y - 45);
                    for (PotionEffect effectPotion : entity.getActivePotionEffects()) {
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        Potion effect = Potion.getPotionById(CustomColors.getPotionId(effectPotion.getEffectName()));
                        if (effect != null) {

                            Formatting getPotionColor = null;
                            if ((effectPotion.getDuration() < 200)) {
                                getPotionColor = Formatting.RED;
                            } else if (effectPotion.getDuration() < 400) {
                                getPotionColor = Formatting.GOLD;
                            } else if (effectPotion.getDuration() > 400) {
                                getPotionColor = Formatting.GRAY;
                            }

                            String durationString = Potion.getDurationString(effectPotion);

                            String level = I18n.format(effect.getName());
                            if (effectPotion.getAmplifier() == 1) {
                                level = level + " " + Formatting.GRAY + I18n.format("enchantment.level.2") + " (" + getPotionColor + durationString + ")";
                            } else if (effectPotion.getAmplifier() == 2) {
                                level = level + " " + Formatting.GRAY + I18n.format("enchantment.level.3") + " (" + getPotionColor + durationString + ")";
                            } else if (effectPotion.getAmplifier() == 3) {
                                level = level + " " + Formatting.GRAY + I18n.format("enchantment.level.4") + " (" + getPotionColor + durationString + ")";
                            }

                            MWFont.MONTSERRAT_BOLD.get(18).drawShadow(level, MathHelper.average(new long[] {-width - 2, width + 2}) - width - 20, yPotion, effectPotion.getPotion().getLiquidColor());
                        }
                        yPotion -= 10;
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                    }

                    GL11.glEnable(GL11.GL_DEPTH_TEST);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    public void renderArmor(EntityPlayer player, int x, int y) {
        InventoryPlayer items = player.inventory;
        ItemStack offhand = player.getHeldItemOffhand();
        ItemStack inHand = player.getHeldItemMainhand();
        ItemStack boots = items.armorItemInSlot(0);
        ItemStack leggings = items.armorItemInSlot(1);
        ItemStack body = items.armorItemInSlot(2);
        ItemStack helm = items.armorItemInSlot(3);
        ItemStack[] stuff;
        stuff = new ItemStack[]{offhand, inHand, helm, body, leggings, boots};
        ArrayList<ItemStack> stacks = new ArrayList<>();
        ItemStack[] array;
        int length = (array = stuff).length;

        for (int j = 0; j < length; j++) {
            ItemStack i = array[j];
            if ((i != null)) {
                i.getItem();
                stacks.add(i);
            }
        }
        int width = 18 * stacks.size() / 2;
        x -= width;
        GlStateManager.disableDepth();
        for (ItemStack stack : stacks) {
            renderItem(player, stack, x, y);
            x += 18;
        }
        GlStateManager.enableDepth();
    }

    public void renderItem(EntityPlayer e, ItemStack stack, int x, int y) {
        if (stack != null) {
            RenderItem renderItem = Minecraft.getRenderItem();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x - 3, y + 10, 0);
            GlStateManager.popMatrix();

            renderItem.zLevel = -100;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            renderItem.renderItemIntoGUI(stack, x, y);
            renderItem.renderItemOverlayIntoGUI(Minecraft.font, stack, x, y + 2, null);
            RenderHelper.disableStandardItemLighting();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            renderItem.zLevel = 0;

            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            renderEnchant(stack, x + 2, y - 18);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.disableStandardItemLighting();

            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        }
    }

    public void renderEnchant(ItemStack item, float x, int y) {
        int encY = y + 5;
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
        for (Enchantment enchantment : enchantments.keySet()) {
            int level = EnchantmentHelper.getEnchantmentLevel(enchantment, item);
            Minecraft.font.drawStringWithShadow((String.valueOf(enchantment.getName().substring(12).charAt(0)).toUpperCase() + level), x, encY, 16777215);
            encY -= 12;
        }
    }
}