package ru.rockstar.client.features.impl.display;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.Event2D;
import ru.rockstar.api.event.event.Event3D;
import ru.rockstar.api.utils.combat.RangeHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.visuals.Tracers;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Indicators extends Feature {
	public static NumberSetting x;
    public static NumberSetting y;
    int x2,y2;
    public static float progress;
    private long lastMS;
    public Indicators() {
        super("KillAuraIndicator", "Показывает информацию о таргете киллауры", Keyboard.KEY_NONE, Category.DISPLAY);
        x = new NumberSetting("Indicators X", 0, 0, 1500, 1, () -> true);
        y = new NumberSetting("Indicators Y", 0, 0, 1500, 1, () -> true);
        addSettings(x,y);
    }

    @EventTarget
    public void ebatkopat(Event2D render) {
    	if (this.progress >= 1.0f) {
            this.progress = 1.0f;
        }
        else {
            this.progress = (System.currentTimeMillis() - this.lastMS) / 850.0f;
        }
    	if (KillAura.target != null && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
    		x2 = (int) x.getNumberValue();
        	y2 = (int) y.getNumberValue();
        	boolean a = mc.player.getHealth() - KillAura.target.getHealth() > 0;
        	boolean win = KillAura.target.getHealth() < mc.player.getHealth();
        	boolean aboba = KillAura.target.isBlocking() && KillAura.target.isHandActive() && KillAura.target.isActiveItemStackBlocking(2) && KillAura.mc.player.getDistanceToEntity(KillAura.target) < RangeHelper.getRange();
        	GlStateManager.pushMatrix();
        	GlStateManager.enableAlpha();
        	DrawHelper.drawRectWithGlow(x2, y2, x2 + 100, y2 + (35) * progress,7,9,new Color(0,0,0,150));
        	DrawHelper.drawGradientRect(x2 - 3, y2 - 2.3f, x2 + 103, y2, ClientHelper.getClientColor().getRGB(), ClientHelper.getClientColor().getRGB() - 100);
        	mc.neverlose500_13.drawStringWithShadow("KILL-AURA DEBUG ",x2 + 3,y2 + 5,-1);
        	DrawHelper.drawCircle(x2 + 86.5f,y2 + 12,-95,-95 + mc.player.getCooledAttackStrength(0.5f) * 400, 7, 4, false, ClientHelper.getClientColor());
        	DrawHelper.drawNewRect(x2 + 3, y2 + 14, x2 + 3 + 3, y2 + 14 + 7, aboba ? new Color(0,200,0,255).getRGB() : new Color(255,0,0,255).getRGB());
        	mc.neverlose500_13.drawStringWithShadow("SHIELDED (" + aboba + ")",x2 + 8,y2 + 16,-1);
        	mc.neverlose500_13.drawStringWithShadow("WIN",x2 + 3,y2 + 27.5f,-1);
        	DrawHelper.drawNewRect(x2 + 3 + 16, y2 + 26f, x2 + 16 + 3 + 40, y2 + 28.5f + 3, new Color(100,100,100,255).getRGB());
        	DrawHelper.drawNewRect(x2 + 3 + 16, y2 + 26f, x2 + 16 + 3 + (a ? (mc.player.getHealth() - KillAura.target.getHealth()) * 2 : 2), y2 + 28.5f + 3, win ? new Color(0,200,0,255).getRGB() : new Color(255,0,0,255).getRGB());
        	GlStateManager.disableAlpha();
        	GlStateManager.popMatrix();
    	} else {
    		this.progress = 0f;
    	}
    }
    @Override
    public void onEnable() {
    	this.lastMS = System.currentTimeMillis();
    	this.progress = 0.0f;
        super.onEnable();
    }
    @EventTarget
    public void onEvent3D(Event3D event) {
    }
}

