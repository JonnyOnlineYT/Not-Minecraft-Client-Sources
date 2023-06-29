package ru.rockstar.client.features.impl.display;

import com.mojang.realmsclient.gui.ChatFormatting;

import javafx.animation.Interpolator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.ColorUtil;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.api.utils.render.RoundedUtil;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Watermark extends Feature {
    public static ListSetting waterMarkMode;
    public static ListSetting bgMode;
    public static BooleanSetting logo;
    public static BooleanSetting cords;
    public static BooleanSetting rect;
    public static BooleanSetting username = new BooleanSetting("Username", false, () -> true);
    public static BooleanSetting server = new BooleanSetting("Server", false, () -> true);
    public static BooleanSetting fps = new BooleanSetting("Fps", false, () -> true);
    public static BooleanSetting time = new BooleanSetting("Time", false, () -> true);
    public static Boolean a = false;
    public static float progress;
    public float s1 = 0f;
	public float s2 = 0f;
    private long lastMS;
    
    public Watermark() {
        super("Watermark", "Client logo.", Keyboard.KEY_NONE, Category.DISPLAY);
        waterMarkMode = new ListSetting("WaterMark Mode", "Rockstar Styled", () -> true, "NoRender", "OnetapV2", "Neverlose", "Rockstar", "Rockstar New", "Rockstar Styled", "WexSide");
        cords = new BooleanSetting("Cords", false, () -> waterMarkMode.getOptions().equalsIgnoreCase("Rockstar") || waterMarkMode.getOptions().equalsIgnoreCase("Rockstar New"));
        this.addSettings(waterMarkMode, bgMode, logo, rect, cords, username, server, fps, time);

    }

    @EventTarget
    public void ebatkopat(Event2D render) {
    	if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        else {
            this.progress = (System.currentTimeMillis() - this.lastMS) / 850.0f;
        }
    	
    	  if (this.mc.gameSettings.showDebugInfo) {
              return;
          }
    	  
    	if (waterMarkMode.getOptions().equalsIgnoreCase("Rockstar")) {
        	a = true;
        }
    	String server;
        if (mc.isSingleplayer()) {
            server = "localhost";
        } else {
            server = mc.getCurrentServerData().serverIP.toLowerCase();
        }
        boolean set1 = false;
        if (Watermark.username.getBoolValue()) {
        	set1 = true;
        }
        boolean set2 = false;
        if (Watermark.server.getBoolValue()) {
        	set2 = true;
        }
        boolean set3 = false;
        if (Watermark.fps.getBoolValue()) {
        	set3 = true;
        }
        boolean set4 = false;
        if (Watermark.time.getBoolValue()) {
        	set4 = true;
        }
        String time = (new SimpleDateFormat("HH:mm:ss")).format(Calendar.getInstance().getTime());

        String text = "–ÓÍÒÚ‡˜ËÍ 3.0" + (set1 ? ("ß8 | ßf" + mc.player.getName()) : "") + (set2 ? "ß8 | ßf" + server : "") + (set3 ? (" ß8 | " + "ßf fps: ßa" + Minecraft.getDebugFPS()) : "") + (set4 ? (" ß8 | ßf " + time) : "");
        
        
        // Astolfo fix.
        int yTotal = 0;
        int i;
        for (i = 0; i < Main.instance.featureDirector.getFeatureList().size(); ++i) {
            yTotal += Minecraft.getMinecraft().sfui16.getFontHeight() + 5;
        } // End.

        String watermark = waterMarkMode.getOptions();
        String name = mc.player.getName();

        this.setModuleName("Watermark ß7" + watermark);

        GlStateManager.pushMatrix();
        if (watermark.equalsIgnoreCase("NoRender")) {
            Minecraft.getMinecraft().sfui18.drawStringWithShadow(Main.instance.name + ChatFormatting.GRAY + " (" + ChatFormatting.WHITE + time + ChatFormatting.GRAY + ")", 3, 4, ClientHelper.getClientColor().getRGB());
        } else if (watermark.equalsIgnoreCase("OnetapV2")) {
            Gui.drawRect(5, 6, Minecraft.getMinecraft().neverlose500_15.getStringWidth(text) + 9, 18, new Color(31, 31, 31, 255).getRGB());
            for (float l = 0; l < Minecraft.getMinecraft().neverlose500_15.getStringWidth(text) + 4; l += 1f) {
                Gui.drawRect(5 + l, 5, l + 6, 6, ClientHelper.getClientColor(5, l, 5).getRGB());
            }
            Gui.drawRect(5, 6f, Minecraft.getMinecraft().neverlose500_15.getStringWidth(text) + 9, 6.5f, new Color(20, 20, 20, 100).getRGB());

            Minecraft.getMinecraft().neverlose500_15.drawStringWithShadow(text, 7, 10.5, -1);
        } else if (watermark.equalsIgnoreCase("Neverlose")) {
            String text1 = name + " | " + mc.getDebugFPS() + " fps" + " | " + server;
            String scam = "ROCKSTAR FREE | " + name + " | " + mc.getDebugFPS() + " fps" + " | " + server;
            DrawHelper.drawGlowRoundedRect(2, 4, Minecraft.getMinecraft().neverlose500_15.getStringWidth(scam) + 15, 20, new Color(10, 10, 10, 200).getRGB(), 5, 5);
            DrawHelper.drawSmoothRect(5, 6, Minecraft.getMinecraft().neverlose500_15.getStringWidth(scam) + 12, 18, new Color(10, 10, 10, 255).getRGB());
            Minecraft.getMinecraft().lato15.drawStringWithShadow("ROCKSTAR FREE", 7.5, 10, ClientHelper.getClientColor().getRGB());
            Minecraft.getMinecraft().lato15.drawStringWithShadow("ROCKSTAR FREE", 8, 10.5, -1);
            Minecraft.getMinecraft().neverlose500_15.drawStringWithShadow("| " + text1, 7 + Minecraft.getMinecraft().neverlose500_15.getStringWidth("ROCKSTAR FREE | "), 10.5, -1);
        } else if (watermark.equalsIgnoreCase("Rockstar")) {
        	
        	
        	String cord = "XYZ: " + (int) Minecraft.getMinecraft().player.posX + " " + (int) Minecraft.getMinecraft().player.posY + " " + (int) Minecraft.getMinecraft().player.posZ;
			DrawHelper.drawGradientRoundedRect(5, 6.5f, (Minecraft.getMinecraft().mntsb_20.getStringWidth(text) + 11) * progress, 22.5f -10, 5, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
			Minecraft.getMinecraft().mntsb_20.drawStringWithShadow(text, 12, 10.5f, -1);
			if (Watermark.cords.getBoolValue()) {
				DrawHelper.drawGradientRoundedRect(5, 26.5f, (Minecraft.getMinecraft().mntsb_20.getStringWidth(cord) + 18) * progress - 5, 39.5f - 27.5f, 5,ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
				Minecraft.getMinecraft().mntsb_20.drawStringWithShadow(cord, 12, 31, -1);
        	}
			
			
			
        } else if (watermark.equalsIgnoreCase("Rockstar New")) {
        	/*
        	Color gradientColor1 = Color.WHITE;
            Color gradientColor2 = new Color(ClientHelper.getClientColor().getRGB());

            gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 0, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor(), true);
            gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 90, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor(), true);
        	*/
            
        	String cord = "XYZ: " + (int) Minecraft.getMinecraft().player.posX + " " + (int) Minecraft.getMinecraft().player.posY + " " + (int) Minecraft.getMinecraft().player.posZ;
        	
        	
        	RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(5, 6.5f, (Minecraft.getMinecraft().mntsb_20.getStringWidth(text) + 11) * progress + 3, 22.5f -10 + 3, 5, 1, 15, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
			Minecraft.getMinecraft().mntsb_20.drawStringWithShadow(text, 12, 10.5f, -1);
			if (Watermark.cords.getBoolValue()) {
				RoundedUtil.drawHorizontalGradientOutlinedRoundedRectWithGlow(5, 26.5f, (Minecraft.getMinecraft().mntsb_20.getStringWidth(cord) + 18) * progress - 5 + 3, 40 - 27.5f + 3, 5,  1, 15, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
				Minecraft.getMinecraft().mntsb_20.drawStringWithShadow(cord, 12, 31, -1);
        	}
			
			
			
			//DrawHelper.drawGradientRect1(5f, 21.5f, ((Minecraft.getMinecraft().mntsb_20.getStringWidth(text) + 11) * progress) / 2, 22.5f, new Color(ClientHelper.getClientColor().getRed(), ClientHelper.getClientColor().getGreen(), ClientHelper.getClientColor().getBlue(), 0).getRGB(), ClientHelper.getClientColor().getRGB());
			
			//DrawHelper.drawGradientRect1(((Minecraft.getMinecraft().mntsb_20.getStringWidth(text) + 11) * progress) / 2, 21.5f, ((Minecraft.getMinecraft().mntsb_20.getStringWidth(text) + 11) * progress), 22.5f, ClientHelper.getClientColor().getRGB(), new Color(ClientHelper.getClientColor().getRed(), ClientHelper.getClientColor().getGreen(), ClientHelper.getClientColor().getBlue(), 0).getRGB());
			
			
			

        }
        else if (watermark.equalsIgnoreCase("WexSide")) {
        	GlStateManager.enableBlend();
        	DrawHelper.drawGlowRoundedRect(8 - 3, 8 - 3, Minecraft.getMinecraft().mntsb.getStringWidth(text) + 26 + 3, 21 + 3, new Color(1, 1, 1, 255).getRGB(), 10, 9);
        	GlStateManager.disableBlend();
        	GlStateManager.enableAlpha();
        	Minecraft.getMinecraft().i30.drawString("r", 9.8f, 9.8f, new Color(0,255,255,255).getRGB());
        	Minecraft.getMinecraft().i30.drawString("r", 10, 10, -1);
        	Minecraft.getMinecraft().mntsb.drawStringWithShadow(text, 23, 12.5f, new Color(240,240,240,255).getRGB());
        }
        
        if (watermark.equalsIgnoreCase("Rockstar Styled")) {
        	this.s1 = (float)Interpolator.LINEAR.interpolate((double)this.s1, (mc.player.ticksExisted) % 100 > 50 ? 0 : 1, 0.03);
        	this.s2 = (float)Interpolator.LINEAR.interpolate((double)this.s2, (mc.player.ticksExisted) % 100 > 50 ? 1 : 0, 0.03);

        	
        	String cord = "XYZ: " + (int) Minecraft.getMinecraft().player.posX + " " + (int) Minecraft.getMinecraft().player.posY + " " + (int) Minecraft.getMinecraft().player.posZ;
			RoundedUtil.drawRound(5, 6.5f, (100) * progress, 37.5f -10, 7, new Color(40,40,40));
			RoundedUtil.drawGradientRound(7.5f, 9f, 22.5f, 32.5f -10, 5, ClientHelper.getClientColor().darker(), ClientHelper.getClientColor(), ClientHelper.getClientColor(), ClientHelper.getClientColor().darker());
			GlStateManager.pushMatrix();
			GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            DrawHelper.scissorRect(33, 6, 33 + 100, 35);
            
			Minecraft.getMinecraft().mntsb_25.drawString("–Œ —“¿–", 33, 14.5f - 30 * s1, -1);
			Minecraft.getMinecraft().mntsb_25.drawString("‘–»ÿ ¿", 33, 14.5f + 30 * s2, -1);
			GlStateManager.disable(GL11.GL_SCISSOR_TEST);
			Gui.drawRect(0,0,0,0,-1);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.enable(GL11.GL_SCISSOR_TEST);
            DrawHelper.scissorRect(10.5f, 13, 10.5f + 17, 13 + 17);
            
			mc.i50.drawString("r", 10.5f, 13, -1);
			
			GlStateManager.disable(GL11.GL_SCISSOR_TEST);
			Gui.drawRect(0,0,0,0,-1);
			GlStateManager.popMatrix();
			if (Watermark.cords.getBoolValue()) {
				//DrawHelper.drawGradientRoundedRect(5, 26.5f, (Minecraft.getMinecraft().mntsb_20.getStringWidth(cord) + 18) * progress - 5, 39.5f - 27.5f, 5,ClientHelper.getClientColor().darker(), ClientHelper.getClientColor());
				//Minecraft.getMinecraft().mntsb_20.drawStringWithShadow(cord, 12, 31, -1);
        	}
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public void onEnable() {
    	this.lastMS = System.currentTimeMillis();
    	this.progress = 0.0f;
        super.onEnable();
    }
}

