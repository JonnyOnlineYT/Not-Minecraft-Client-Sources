package ru.rockstar.client.ui.draggable.impl;

import java.awt.Color;
import java.util.Objects;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import ru.rockstar.Main;
import ru.rockstar.api.utils.other.Util;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.Aura;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.display.InventoryPreview;
import ru.rockstar.client.features.impl.visuals.NameProtect;
import ru.rockstar.client.ui.draggable.DraggableModule;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;

public class TargetHudComponent extends DraggableModule {
	public int lastA = 0;
    public int lastW = 0;
    public int lastS = 0;
    public int lastD = 0;
    public int lastJ = 0;
    public int lastS2 = 0;
    public long deltaAnim;
    public static int x;
    public static int y;
    
    public TargetHudComponent() {
        super("TargetHudComponent", 188, 100);
    }

    @Override
    public int getWidth() {
        return 250;
    }

    @Override
    public int getHeight() {
        return 100;
    }

    @Override
    public void render(int mouseX, int mouseY) {
    	x = getX();
        y = getY();
    	double healthBarWidth = 0;
        double hudHeight;
        float progress = 1.0f;
    	float progress2 = 1.0f;
    	ScaledResolution sr = new ScaledResolution(mc);
    	EntityLivingBase target = mc.player;
    	BooleanSetting targetHud = KillAura.targetHud;
        String mode = KillAura.targetHudMode.getOptions();
        if (target != null) {
            if (mode.equalsIgnoreCase("Rockstar Old")) {
                if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                	final float x = getX();
                    final float y = getY();
                    int color = 15;
                    double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                    healthBarWidth = AnimationHelper.animate(hpWidth, healthBarWidth, 3 * Feature.deltaTime());
                    DrawHelper.drawGradientRect(x + 120, y - 14, x + 122, y - 14 + 39 * progress2, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                    if (progress2 >= 1.0f) {
                    	DrawHelper.drawNewRect(x + 122, y - 14, x + 122 + 128 * progress, y + 25, new Color(color, color, color, 0).getRGB());
                        DrawHelper.drawNewRect(x + 122, y - 13.5f, x + 122 + 128 * progress, y + 24.5f, new Color(0, 0, 0, 150).getRGB());
                        DrawHelper.drawGradientRect(x + 120 + 130 * progress, y - 14, x + 122 + 130 * progress, y + 25, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                    }
                    if (progress == 1.0) {
                    	if (!target.getName().isEmpty()) {
                    		Util.drawHead2(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) x + 216, (int) (y - 9f));
                    	}
                        mc.mntsb.drawStringWithShadow("Hp: " + (int) target.getHealth() + " | Distance: " + (int) mc.player.getDistanceToEntity(target), x + 101.0f + 35.0f - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 4f, -1);
                        mc.mntsb.drawStringWithShadow(target.getName(), x + 128, y - 7.0f, -1);
                        mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                        mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 222, (int) y - 35);
                    }
                    if (progress >= 0.5 && progress2 >= 1.0f) {
                    	 DrawHelper.drawGradientRect1(x + 160 - 32, y + 13.0f, x + 160.0f + (healthBarWidth - 32) * progress, y + 20.0f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
                    }
                } else {
                    healthBarWidth = 92.0;
                    hudHeight = 0.0;
                    target = null;
                }
            }
        }
        
        if (target != null && mc.world != null) {
            if (mode.equalsIgnoreCase("Rockstar")) {
                if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                	final float x = TargetHudComponent.x;
                    final float y = TargetHudComponent.y;
                    int color = 15;
                    double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                    healthBarWidth = AnimationHelper.animate(hpWidth, healthBarWidth, 3 * Feature.deltaTime());
                    RoundedUtil.drawGradientHorizontal(x + 182 - 60 * progress2, y - 13.5f * progress2, (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 60 : 60 + 70) * progress2, 9.5f + 30 * progress2, 7, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
                    
                    GlStateManager.pushMatrix();
                    GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                    RenderUtils.scissorRect(x + 182 - 60, y - 13.5f, x + (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 115 : 115 + 70) - 60 + 128, y - 13.5f + 9.5f + 30);
                    if (!target.getName().isEmpty()) {
                    	Util.drawHead2(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) ((int) x + 216 + (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 30 : 30 + 70) - 100 * progress), (int) (y - 9f));
                    }
                    mc.mntsb_13.drawStringWithShadow("Distance: " + String.format("%.1f", Float.valueOf(mc.player.getDistanceToEntity(target))), x + 101.0f - 70 + 105.0f * progress - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 5.5f, -1);
                    mc.mntsb_20.drawStringWithShadow(target.getName(), x + 128 - 105 + 105 * progress, y - 7.0f, -1);
                    mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                    mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 222, (int) y - 35);
                    
                    //DrawHelper.drawGradientRect1(x - 1 + 160 - 32 - 105 + 105 * progress, y + 13.0f - 1, x + 1 - 105 + 105 * progress + 160.0f + (78 - 32), y + 1 + 20.0f, ClientHelper.getClientColor().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
                    
                    if (target.hurtTime > 0) {
                    DrawHelper.drawTriangle((float) (x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32)) - 4.5f, y + 8, 4.0F, 4.0F, new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB(), new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB());
                    RenderUtils.drawFilledCircle((float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), 4, new Color(50, 50, 50, 255 * target.hurtTime / 10));
                    DrawHelper.drawGradientRect1(x, y, x, y, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                    mc.mntsb_10.drawCenteredString("" + (int) target.getHealth(), (float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), -1);
                    }
                    
                    DrawHelper.drawGradientRect1(x + 160 - 32 - 105 + 105 * progress, y + 13.0f, x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32), y + 20.0f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                    
                    GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                    GlStateManager.popMatrix();
                    
                } else {
                    healthBarWidth = 92.0;
                    hudHeight = 0.0;
                    target = null;
                }
            }
        }
        
        
        if (target != null && mc.world != null) {
            if (mode.equalsIgnoreCase("Rockstar New")) {
                if (targetHud.getBoolValue() && target instanceof EntityPlayer) {
                	final float x = TargetHudComponent.x;
                    final float y = TargetHudComponent.y;
                    int color = 15;
                    double hpWidth = (target.getHealth() / target.getMaxHealth() * 78);
                    healthBarWidth = AnimationHelper.animate(hpWidth, healthBarWidth, 3 * Feature.deltaTime());
                    RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(x + 182 - 60 * progress2, y - 13.5f * progress2, (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 60 : 60 + 70) * progress2, 9.5f + 30 * progress2, 7, 1, 15, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
                    
                    GlStateManager.pushMatrix();
                    GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                    RenderUtils.scissorRect(x + 182 - 60, y - 13.5f, x + (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 115 : 115 + 70) - 60 + 128, y - 13.5f + 9.5f + 30);
                    if (!target.getName().isEmpty()) {
                    	Util.drawHead2(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) ((int) x + 216 + (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 30 : 30 + 70) - 100 * progress), (int) (y - 9f));
                    }
                    mc.mntsb_13.drawStringWithShadow("Distance: " + String.format("%.1f", Float.valueOf(mc.player.getDistanceToEntity(target))), x + 101.0f - 70 + 105.0f * progress - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 5.5f, -1);
                    mc.mntsb_20.drawStringWithShadow(target.getName(), x + 128 - 105 + 105 * progress, y - 7.0f, -1);
                    mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                    mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 222, (int) y - 35);
                    
                    //DrawHelper.drawGradientRect1(x - 1 + 160 - 32 - 105 + 105 * progress, y + 13.0f - 1, x + 1 - 105 + 105 * progress + 160.0f + (78 - 32), y + 1 + 20.0f, ClientHelper.getClientColor().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
                    
                    if (target.hurtTime > 0) {
                    DrawHelper.drawTriangle((float) (x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32)) - 4.5f, y + 8, 4.0F, 4.0F, new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB(), new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB());
                    RenderUtils.drawFilledCircle((float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), 4, new Color(50, 50, 50, 255 * target.hurtTime / 10));
                    DrawHelper.drawGradientRect1(x, y, x, y, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                    mc.mntsb_10.drawCenteredString("" + (int) target.getHealth(), (float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), -1);
                    }
                    
                    DrawHelper.drawGradientRect1(x + 160 - 32 - 105 + 105 * progress, y + 13.0f, x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32), y + 20.0f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                    
                    GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                    GlStateManager.popMatrix();
                    
                } else {
                    healthBarWidth = 92.0;
                    hudHeight = 0.0;
                    target = null;
                }
            }
        }
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
    	x = getX();
        y = getY();
    	double healthBarWidth = 0;
        double hudHeight;
        float progress = 1.0f;
    	float progress2 = 1.0f;
    	ScaledResolution sr = new ScaledResolution(mc);
    	EntityLivingBase target = Aura.target;
        if (target != null && mc.world != null) {
                if (target instanceof EntityPlayer) {
                	final float x = TargetHudComponent.x;
                    final float y = TargetHudComponent.y;
                    int color = 15;
                    double hpWidth = ((target.getMaxHealth()));
                    healthBarWidth = AnimationHelper.animate(hpWidth, healthBarWidth, 3 * Feature.deltaTime());
                    RoundedUtil.drawHorizontalGradientOutlinedRoundedRect(x + 182 - 60 * progress2, y - 13.5f * progress2, (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 60 : 60 + 70) * progress2, 9.5f + 30 * progress2, 7, 1, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
                    
                    GlStateManager.pushMatrix();
                    GlStateManager.enable(GL11.GL_SCISSOR_TEST);
                    RenderUtils.scissorRect(x + 182 - 60, y - 13.5f, x + (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 115 : 115 + 70) - 60 + 128, y - 13.5f + 9.5f + 30);
                    if (!target.getName().isEmpty()) {
                    	Util.drawHead2(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(target.getUniqueID()).getLocationSkin(), (int) ((int) x + 216 + (mc.mntsb_20.getStringWidth(target.getName()) > 50 ? mc.mntsb_20.getStringWidth(target.getName()) + 30 : 30 + 70) - 100 * progress), (int) (y - 9f));
                    }
                    mc.mntsb_13.drawStringWithShadow("Distance: " + String.format("%.1f", Float.valueOf(mc.player.getDistanceToEntity(target))), x + 101.0f - 70 + 105.0f * progress - mc.neverlose500_16.getStringWidth(String.valueOf((int) target.getHealth() / 2.0f)) / 2.0f, y + 5.5f, -1);
                    mc.mntsb_20.drawStringWithShadow(target.getName(), x + 128 - 105 + 105 * progress, y - 7.0f, -1);
                    mc.getRenderItem().renderItemOverlays(mc.neverlose500_18, target.getHeldItem(EnumHand.OFF_HAND), (int) x + 228, (int) y - 35);
                    mc.getRenderItem().renderItemIntoGUI(target.getHeldItem(EnumHand.OFF_HAND), (int)  x + 222, (int) y - 35);
                    
                    //DrawHelper.drawGradientRect1(x - 1 + 160 - 32 - 105 + 105 * progress, y + 13.0f - 1, x + 1 - 105 + 105 * progress + 160.0f + (78 - 32), y + 1 + 20.0f, ClientHelper.getClientColor().darker().getRGB(), ClientHelper.getClientColor().darker().getRGB());
                    
                    if (target.hurtTime > 0) {
                    DrawHelper.drawTriangle((float) (x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32)) - 4.5f, y + 8, 4.0F, 4.0F, new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB(), new Color(50, 50, 50, 255 * target.hurtTime / 10).getRGB());
                    RenderUtils.drawFilledCircle((float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), 4, new Color(50, 50, 50, 255 * target.hurtTime / 10));
                    DrawHelper.drawGradientRect1(x, y, x, y, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                    mc.mntsb_10.drawCenteredString("" + (int) target.getHealth(), (float) (x - 105 + 105 * progress + 160 + (healthBarWidth - 32.5f)), (int) (y + 6), -1);
                    }
                    
                    DrawHelper.drawGradientRect1(x + 160 - 32 - 105 + 105 * progress, y + 13.0f, x - 105 + 105 * progress + 160.0f + (healthBarWidth - 32), y + 20.0f, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().brighter().getRGB());
                    
                    GlStateManager.disable(GL11.GL_SCISSOR_TEST);
                    GlStateManager.popMatrix();
                    
                } else {
                    healthBarWidth = 92.0;
                    hudHeight = 0.0;
                    target = null;
                }
            }
        super.draw();
    }
}