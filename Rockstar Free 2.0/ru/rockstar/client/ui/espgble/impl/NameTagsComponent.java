package ru.rockstar.client.ui.espgble.impl;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

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
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.event.event.EventNameTags;
import ru.rockstar.api.utils.math.MathHelper;
import ru.rockstar.api.utils.math.MathematicHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.impl.display.ClientFont;
import ru.rockstar.client.features.impl.display.TimerIndicator;
import ru.rockstar.client.features.impl.movement.Timer;
import ru.rockstar.client.features.impl.visuals.ESP;
import ru.rockstar.client.features.impl.visuals.NameProtect;
import ru.rockstar.client.features.impl.visuals.NameTags;
import ru.rockstar.client.ui.espgble.DraggableModule;

public class NameTagsComponent extends DraggableModule {
	int x2,y2;
    public NameTagsComponent() {
        super("EspComponent", 200, 120);
    }
    public static Map<EntityLivingBase, double[]> entityPositions = new HashMap();
    public static int x;
    public static int y;

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 50;
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
        float n = (mc.gameSettings.smoothCamera ? 2.0f : NameTags.ntsize.getNumberValue());
        GlStateManager.scale(n, n, n);
    }

    @Override
    public void render(int mouseX, int mouseY) {
    	x = getX();
    	y = getY();
    	GlStateManager.pushMatrix();
        	EntityLivingBase entity = mc.player;

            boolean hui = false;
        	if (entity.getHeldItemOffhand().getItem() == Items.END_CRYSTAL || entity.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
        		if (!Main.instance.friendManager.isFriend(entity.getName())) {
        			hui = true;
        		}
        	}
        	
            GlStateManager.pushMatrix();
                double[] array = entityPositions.get(entity);
                ScaledResolution sr = new ScaledResolution(mc);
                GlStateManager.translate(getX(), getY(), 0.0);
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
                	float width = mc.mntsb_20.getStringWidth(string2 + " " + string) + 2;
                    GlStateManager.translate(0.0D, -10, 0.0D);
                    if (NameTags.ntbg.getBoolValue()) {
                    	GlStateManager.enableBlend();
                    	GlStateManager.enableAlpha();
                        DrawHelper.drawRectWithGlow((-width / 2), -10.0D, (width / 2 + 5), 3, 7, 5, new Color(1,1,1,255));
                        GlStateManager.disableBlend();
                    }
                    mc.mntsb_20.drawStringWithShadow(string + " " + getHealthColor(entity.getHealth()) + string2, (-width / 2 + 2), -7.5f, -1);
                ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);
                GlStateManager.popMatrix();
        GL11.glPopMatrix();
        GlStateManager.enableBlend();
        super.render(mouseX, mouseY);
    }

    @Override
    public void draw() {
    	if (!TimerIndicator.a) {
    		return;
    	}
    	GlStateManager.pushMatrix();
        GlStateManager.enableTexture2D();
        x2 = (int) getX();
        x = getX();
        y = getY();
    	y2 = (int) getY();
    	GlStateManager.pushMatrix();
    	GlStateManager.enableAlpha();
    	DrawHelper.drawRect(x2 - 1, y2, x2 + 46, y2 + 45,new Color(30,30,30,255).getRGB());
    	DrawHelper.drawGradientRect1(x2 - 3f, y2 - 2.3f, x2 + 48, y2 + 10, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
    	mc.mntsb_19.drawStringWithShadow("TIMER",x2 + 5,y2 + 1,-1);
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400, 11, 8, false, new Color(50,50,50,150));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)),400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)) + 10,11, 8, false, new Color(-1));
    	DrawHelper.drawCircle(x2 + 21.5f,y2 + 27,0,400 - (float) (TimerIndicator.indicatorTimer * (400 / Timer.ticks)), 11, 8, false, ClientHelper.getClientColor());
    	GlStateManager.popMatrix();
    	GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableLighting();
        GlStateManager.popMatrix();
        super.draw();
    }
}