package splash.client.modules.visual;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.theme.type.FontType;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.api.value.impl.NumberValue;
import splash.client.events.font.FontChangeEvent;
import splash.client.events.key.EventKey;
import splash.client.events.render.EventRender2D;
import splash.client.events.scoreboard.EventScoreboard;
import splash.client.themes.splash.SplashTheme;
import splash.client.themes.virtue.VirtueTheme;
import splash.utilities.math.MathUtils;
import splash.utilities.visual.ColorUtilities;
import splash.utilities.visual.RenderUtilities;

/**
 * Author: Ice Created: 15:19, 31-May-20 Project: Client
 */
public class UI extends Module {
	int y = 0;

	public ModeValue<LogoMode> logo = new ModeValue<>("Logo", LogoMode.SPLASH, this);
	public ModeValue<TabGUIMode> tabgui = new ModeValue<>("TabGUI", TabGUIMode.OFF, this);
	public ModeValue<FontType> fontValue = new ModeValue<>("Font", FontType.CUSTOM, this);
	public ModeValue<FontMode> fontMode = new ModeValue<>("Font Mode", FontMode.ARIAL, this);
	public ModeValue<Case> arraylistCaseValue = new ModeValue<>("Case", Case.LOWERCASE, this);
	public ModeValue<ArrayColor> arrayColor = new ModeValue<>("Array Color", ArrayColor.BLUE, this);
	public NumberValue<Double> saturation = new NumberValue<Double>("Saturation", 0.5D, 0D, 1D, this);
	public ModeValue<TabColor> tabguiColorValue = new ModeValue<>("TabGUI Color", TabColor.PULSE, this);
	public NumberValue<Integer> redColorValue = new NumberValue<>("R", 0, 1, 255, this);
	public NumberValue<Integer> greenColorValue = new NumberValue<>("G", 255, 1, 255, this);
	public NumberValue<Integer> blueColorValue = new NumberValue<>("B", 226, 1, 255, this);
	public NumberValue<Integer> xValue = new NumberValue<>("Array X Offset", 1, 0, 60, this);
	public NumberValue<Integer> sidebarThickness = new NumberValue<>("Sidebar Thickness", 5, 5, 20, this);
	public NumberValue<Integer> backgroundDarkness = new NumberValue<>("Background Opacity", 80, 0, 255, this);
	public BooleanValue<Boolean> sideBar = new BooleanValue<>("Array Sidebar", true, this);
	public BooleanValue<Boolean> arrayBorder = new BooleanValue<>("Array Border", true, this);
	public BooleanValue<Boolean> notificationsValue = new BooleanValue<>("Notifications", true, this);
	public BooleanValue<Boolean> bandiCam = new BooleanValue<>("Bandicam Logo", false, this);
	public BooleanValue<Boolean> scoreboardValue = new BooleanValue<>("Scoreboard", true, this);
	public BooleanValue<Boolean> coords = new BooleanValue<>("Coords", true, this);
	public BooleanValue<Boolean> border = new BooleanValue<>("Border", false, this);
	public float value;
	public boolean reversing;
	public static int color;
	
	
	public UI() {
		super("UI", "Shows user interface.", ModuleCategory.VISUALS);
		color = 3;
		value = 400;
	}

	ScaledResolution scalRes;
	
	public enum Case {
		UPPERCASE, LOWERCASE;
	}

	public enum FontMode {
		ARIAL, SFUI
	}
	
	public enum TabColor {
		PULSE, SOLID;
	}

	public enum LogoMode {
		SPLASH, VIRTUE, HELIUM, SPLASHBOX, SIGHT, ASTOLFO
	}
	
	public enum ArrayColor {
		SOLID, HELIUM, EXHIBITION, BLUE, RED, GREEN, PURPLE, CATEGORY, ASTOLFO
	}


	public enum TabGUIMode {
		NORMAL, OFF
	}

	public String getTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		Splash.getInstance().loadFontRenderer(fontMode.getValue());
	}
	
	private String lastFont = "";

	@Collect
	public void on2D(EventRender2D eventRender2D) {

		ScaledResolution scaledResolution = new ScaledResolution(mc);
		if (!this.fontMode.getValue().name().equalsIgnoreCase(lastFont)) {
			Splash.getInstance().loadFontRenderer(fontMode.getValue());
			this.lastFont = this.fontMode.getValue().name();
		}
		Splash.getInstance().getFontRenderer().updateFontType();
		Splash.getInstance().setClientColor(
				new Color(redColorValue.getValue(), greenColorValue.getValue(), blueColorValue.getValue()));
		Splash.getInstance().getNotificationManager().render();
		if (bandiCam.getValue()) {
			scalRes = new ScaledResolution(mc);
			RenderUtilities.INSTANCE.drawImage(new ResourceLocation("splash/bandicam.png"),
					(scalRes.getScaledWidth() / 2) - 160, -20, 320, 60);
		}
		SplashTheme splashTheme = new SplashTheme();//
		value += MathUtils.getRandomInRange(-3, 6);
		if (value > 1700) {
			value = 600;
		}
		if (value < 600) {
			value = 0;
		}
		switch (logo.getValue()) {
		case SPLASH:
			this.drawSplashLogo();
			break;

		case VIRTUE:
			this.drawVirtueWatermark();
			break;
		
		case SIGHT:
			this.drawSight();
			break;

		case HELIUM:
			this.drawHeliumLogo();
			break;

		case SPLASHBOX:
			this.drawSplashBoxLogo();
			break;
		case ASTOLFO:
			this.drawAstolfoLogo();
			break;

		}

        GL11.glPushMatrix();
        int size = 16;
        float x = 37;
        float y = (scaledResolution.getScaledHeight() - (230)- size * 2) - 5;
        Collection var4 = this.mc.thePlayer.getActivePotionEffects();
        int i = 0;
        if (!var4.isEmpty()) {
            for (Iterator var6 = this.mc.thePlayer.getActivePotionEffects().iterator(); var6.hasNext(); ) {
                PotionEffect var7 = (PotionEffect) var6.next();
                Potion var8 = Potion.potionTypes[var7.getPotionID()];
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                if (var8.hasStatusIcon()) {
                    int var9 = var8.getStatusIconIndex();
                    Gui theGui = new Gui();
                    theGui.drawTexturedModalRect((int) x, (int) y - (18 * i), var9 % 8 * 18, 198 + var9 / 8 * 18, 18, 18);
                    Splash.getInstance().getFontRenderer().drawStringWithShadow("" + (var7.getDuration() <= 300 ? ChatFormatting.RED : ChatFormatting.WHITE) + Potion.getDurationString(var7), (int) x - Splash.getInstance().getFontRenderer().getStringWidth("" + Potion.getDurationString(var7)) - 5, (int) y - (18 * i) + 6, -1);
                    i++;
                }
            }
        }
        GL11.glPopMatrix();
		splashTheme.drawArraylist();
		switch (tabgui.getValue()) {
		case NORMAL:
			splashTheme.drawTabGUI();
			break;
		case OFF:
			break;
		}

		if (border.getValue()) {
			drawBorder();
		}
		
		if (coords.getValue()) {
			if (mc.ingameGUI.getChatGUI().getChatOpen()) {
				Splash.getInstance().getFontRenderer().drawStringWithShadow(
						"XYZ: " + EnumChatFormatting.GRAY + Math.round(mc.thePlayer.posX) + ", " + Math.round(mc.thePlayer.posY) + ", "
								+ Math.round(mc.thePlayer.posZ),
						2, scaledResolution.getScaledHeight() - Splash.getInstance().getFontRenderer().getFontHeightCustom() - 16,
						Splash.getInstance().getClientColor());

			} else {
				Splash.getInstance().getFontRenderer().drawStringWithShadow(
						"XYZ: " + EnumChatFormatting.GRAY + Math.round(mc.thePlayer.posX) + ", " + Math.round(mc.thePlayer.posY) + ", "
								+ Math.round(mc.thePlayer.posZ),
								2, scaledResolution.getScaledHeight() - Splash.getInstance().getFontRenderer().getFontHeightCustom() - 5,
						Splash.getInstance().getClientColor());


			}
		}

	}

	@Collect
	public void onKey(EventKey eventKey) {
		VirtueTheme virtueTheme = new VirtueTheme();
		virtueTheme.onKey(eventKey.getPressedKey());
	}

	@Collect
	public void onFont(FontChangeEvent fontChangeEvent) {
		Splash.getInstance().loadFontRenderer(fontMode.getValue());
	}

	@Collect
	public void onScoreboard(EventScoreboard eventScoreboard) {
		if (!scoreboardValue.getValue()) {
			eventScoreboard.setCancelled(true);
		}
	}

	// LOGOS AND WATERMARKS
	public void drawSplashLogo() {
		ScaledResolution scaledRes = new ScaledResolution(mc);
		RenderUtilities.INSTANCE.drawImage(new ResourceLocation("splash/splashPNGLogo.png"),
				(scaledRes.getScaledWidth() / 2000 - 5), -12, 960, 540);

	}

	public void drawVirtueWatermark() {
		Splash.getInstance().getFontRenderer().drawStringWithShadow(Splash.getInstance().getClientName() + " " + EnumChatFormatting.GRAY + getTime(), 4,
				2, -1);
	}
	
	public void drawSight() {
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("splash/sight.png"));
		GlStateManager.enableAlpha();
		Gui.drawModalRectWithCustomSizedTexture(-32, -22, 0, 0, 1280 / 8, 720 / 8, 1280 / 8, 720 / 8);
		GlStateManager.disableAlpha();
	}

	public void drawHeliumLogo() {
		Splash.getInstance().getFontRenderer().drawStringWithShadow(
				Splash.getInstance().getClientName().substring(0, 1) + EnumChatFormatting.GRAY + Splash.getInstance().getClientName().replace(Splash.getInstance().getClientName().substring(0, 1), ""), 6, 6,
				ColorUtilities.getRainbow(-6000, y * 35).getRGB());
	}
	
	public void drawAstolfoLogo() {
		mc.fontRendererObj.drawStringWithShadow(Splash.getInstance().getClientName().substring(0, 1) + EnumChatFormatting.WHITE + Splash.getInstance().getClientName().replace(Splash.getInstance().getClientName().substring(0, 1), ""), 2, 2, Splash.getInstance().getClientColor());
	}
	
	public void drawSplashBoxLogo() {
		Gui.drawRect(5, 2, 75, 35, new Color(0, 0, 0, 120).getRGB());
		Splash.getInstance().getFontRenderer().drawCenteredStringWithShadow(Splash.getInstance().getClientName(), (float) 37.5, 10, -1);
		Splash.getInstance().getFontRenderer().drawCenteredStringWithShadow(getTime(), (float) 38, 20, -1);
	}

	public void drawBorder() {
		ScaledResolution scaledResolution = new ScaledResolution(mc);
		Gui.drawRect(scaledResolution.getScaledWidth() / 500, scaledResolution.getScaledHeight(), 0, 0,Splash.INSTANCE.getClientColor());

		Gui.drawRect(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), scaledResolution.getScaledWidth() / 1.001, 0,
				Splash.INSTANCE.getClientColor());

		Gui.drawRect(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight() / 500, 0, 0, Splash.INSTANCE.getClientColor());

		Gui.drawRect(scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), 0, scaledResolution.getScaledHeight() / 1.003,
				Splash.INSTANCE.getClientColor());

	}

	public enum Theme {
		SPLASH, VIRTUE, HELIUM, PHOON, SIMPLE, CLEAN;
	}
}
