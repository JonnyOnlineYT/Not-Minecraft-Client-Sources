package ru.rockstar.client.features.impl.display;

import java.awt.*;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.potion.PotionEffect;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventRender2D;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.PalatteHelper;
import ru.rockstar.api.utils.render.AnimationHelper;
import ru.rockstar.api.utils.render.ClientHelper;
import ru.rockstar.api.utils.render.DrawHelper;
import ru.rockstar.api.utils.render.RenderUtils;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ColorSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class ArrayList extends Feature {
	
	
	
	
	
	
	 public float scale = 2;
	 public static ListSetting cases = new ListSetting("Case", "Default", () -> true, "Default", "UPPER", "lower");
	 public static BooleanSetting suffix = new BooleanSetting("Suffix", true, () -> true);
	    public static ListSetting sideMode = new ListSetting("FeatureList Side", "Left", () -> true, "Right", "Left");
	    public static BooleanSetting background = new BooleanSetting("Background", true, () -> true);
	    public NumberSetting offsetY = new NumberSetting("Offset Y", 9, 3, 12, 0.01f, () -> true);
	    public static ListSetting bgcolorList = new ListSetting("BackGround Color", "Static", () -> background.getBoolValue(), "Astolfo", "Static", "Client", "Fade", "Rainbow", "Custom", "None");
	    public static NumberSetting bgtime = new NumberSetting("Bg ColorTime", 30, 1, 100, 1, () -> background.getBoolValue());
	    public static ColorSetting bgonecolor = new ColorSetting("BackGround One Color", new Color(0x000000).getRGB(), () -> background.getBoolValue() && (bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Static")));
	    public static ColorSetting bgtwocolor = new ColorSetting("BackGround Two Color", new Color(0x000000).getRGB(), () -> background.getBoolValue() && bgcolorList.currentMode.equals("Custom"));
	    public static NumberSetting bgalpha = new NumberSetting("Bg Alpha", 180, 1, 255, 1, () -> background.getBoolValue());
	    public BooleanSetting rightBorder = new BooleanSetting("Right Border", true, () -> true);
	    public static ListSetting colorList = new ListSetting("ArrayList Color", "Astolfo", () -> true, "Astolfo", "Static", "Client", "Fade", "Rainbow", "Custom", "None", "Category");
	    public static NumberSetting time = new NumberSetting("ColorTime", 30, 1, 100, 1, () -> true);
	    public static NumberSetting rainbowSaturation = new NumberSetting("Rainbow Saturation",0.8f, 0.1f, 1f, 0.1f, () -> colorList.currentMode.equals("Rainbow"));
	    public static NumberSetting rainbowBright = new NumberSetting("Rainbow Brightness", 1f, 0.1f, 1f, 0.1f, () -> colorList.currentMode.equals("Rainbow"));
	    public static ColorSetting onecolor = new ColorSetting("One Color", new Color(0x000000).getRGB(), () -> colorList.currentMode.equals("Fade") || colorList.currentMode.equals("Custom") || colorList.currentMode.equals("Static"));
	    public static ColorSetting twocolor = new ColorSetting("Two Color", new Color(0x000000).getRGB(), () -> colorList.currentMode.equals("Custom"));
	    public BooleanSetting onlyBinds = new BooleanSetting("Only Binds", false, () -> true);
	    public BooleanSetting noVisualModules = new BooleanSetting("No Visual Modules", false, () -> true);
	    public BooleanSetting glow = new BooleanSetting("Glow", true, () -> true);
	    public ColorSetting glowColor = new ColorSetting("Glow Color", new Color(0xFFFFFF).getRGB(), () -> glow.getBoolValue());
	    public NumberSetting glowRadius = new NumberSetting("Glow Radius", 30, 0, 50, 1, () -> glow.getBoolValue());
	    public NumberSetting glowAlpha = new NumberSetting("Glow Alpha", 255, 30, 255, 1, () -> glow.getBoolValue());
	    
	    public NumberSetting arrayX = new NumberSetting("Array X", 0, 0, 500, 1, () -> true);
	    public NumberSetting arrayY = new NumberSetting("Array Y", 0, 0, 500, 1, () -> true);

	    public NumberSetting fontY = new NumberSetting("Font Y", 0, 0, 5, 1, () -> true);

	    public ArrayList() {
	        super("ArrayList", "Список включённых модулей", 0, Category.DISPLAY);
	        addSettings(sideMode, cases, onlyBinds, noVisualModules, background, colorList, time, onecolor, twocolor, bgcolorList, bgtime, bgonecolor, bgtwocolor, bgalpha, rainbowSaturation, rainbowBright, rightBorder, glow, glowRadius, glowColor, glowAlpha, offsetY, arrayX, arrayY, fontY);
	    }


	    @EventTarget
	    public void Event2D(EventRender2D event) {
	        if (!isToggled()) return;
	        
	        if (this.mc.gameSettings.showDebugInfo) {
	            return;
	        }
	        java.util.List<Feature> activeModules = Main.instance.featureDirector.getFeatureList();
	        if (cases.getCurrentMode().equalsIgnoreCase("Defalut")) {
		        activeModules.sort(Comparator.comparingDouble(s -> -ClientHelper.getFontRender().getStringWidth(s.getLabel())));
            } else if (cases.getCurrentMode().equalsIgnoreCase("upper")) {
    	        activeModules.sort(Comparator.comparingDouble(s -> -ClientHelper.getFontRender().getStringWidth(s.getLabel().toUpperCase())));
            } else if (cases.getCurrentMode().equalsIgnoreCase("lower")) {
    	        activeModules.sort(Comparator.comparingDouble(s -> -ClientHelper.getFontRender().getStringWidth(s.getLabel().toLowerCase())));
            } else {
            	activeModules.sort(Comparator.comparingDouble(s -> -ClientHelper.getFontRender().getStringWidth(s.getLabel())));
            }
	        float displayWidth = event.getResolution().getScaledWidth() * (event.getResolution().getScaleFactor() / 2F);
	        int y = (int) (5);
	        int yTotal = 0;
	        for (int i = 0; i < Main.instance.featureDirector.getFeatureList().size(); ++i) {
	            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
	        }
	        
	        for (Feature module : activeModules) {
	            module.animYto = AnimationHelper.move(module.animYto, (float) (module.isToggled() ? 1 : 0), (float) (6.5f * Main.deltaTime()), (float) (6.5f * Main.deltaTime()), (float) Main.deltaTime());
	            if (module.animYto > 0.01f) {
	                if (module.getModuleName().equals("ClickGui") || noVisualModules.getBoolValue() && module.getCategory() == Category.VISUALS || onlyBinds.getBoolValue() && module.getKey() == 0)
	                    continue;

	                String mode1 = bgcolorList.getOptions();
	                String mode2 = colorList.getOptions();

	                int color = 0;
	                int colorCustom = onecolor.getColorValue();
	                int colorCustom2 = twocolor.getColorValue();
	                double time = this.time.getNumberValue();

	                int bgcolor = 0;
	                int bgcolorCustom = bgonecolor.getColorValue();
	                int bgcolorCustom2 = bgtwocolor.getColorValue();
	                int bgalpha2 = (int) bgalpha.getNumberValue();
	                double bgtime = this.bgtime.getNumberValue();

	                switch (mode1.toLowerCase()) {
	                    case "rainbow":
	                        bgcolor = PalatteHelper.rainbow((int) (y * bgtime), ArrayList.rainbowSaturation.getNumberValue(), ArrayList.rainbowBright.getNumberValue()).getRGB();
	                        break;
	                    case "astolfo":
	                        bgcolor = PalatteHelper.astolfo(false, (int) (y * 3)).getRGB();
	                        break;
	                    case "static":
	                        bgcolor = new Color(bgcolorCustom).getRGB();
	                        break;
	                    case "custom":
	                    	bgcolor = DrawHelper.fadeColor(new Color(bgcolorCustom).getRGB(), new Color(bgcolorCustom2).getRGB(), (float) Math.abs(((((System.currentTimeMillis() / bgtime) / bgtime) + 11 * 6L / 61 * 2) % 2)));
	                        break;
	                    case "fade":
	                    	bgcolor = DrawHelper.fadeColor(new Color(bgcolorCustom).getRGB(), new Color(bgcolorCustom).darker().darker().getRGB(), (float) Math.abs(((((System.currentTimeMillis() / bgtime) / bgtime) + 11 * 6L / 60 * 2) % 2)));
	                        break;
	                    case "none":
	                        bgcolor = -1;
	                        break;
	                    case "client":
	                        bgcolor = ClientHelper.getClientColor().getRGB();
	                        break;
	                }

	                switch (mode2.toLowerCase()) {
	                    case "rainbow":
	                        color = PalatteHelper.rainbow((int) (y * time), rainbowSaturation.getNumberValue(), ArrayList.rainbowBright.getNumberValue()).getRGB();
	                        break;
	                    case "astolfo":
	                        color = PalatteHelper.astolfo(false, (int) (y * 4)).getRGB();
	                        break;
	                    case "static":
	                        color = new Color(colorCustom).getRGB();
	                        break;
	                    case "custom":
	                        color = DrawHelper.TwoColoreffect(new Color(colorCustom), new Color(colorCustom2), (float) Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0 * (time * 2.55) / 60.0).getRGB();
	                        break;
	                    case "fade":
	                        color = DrawHelper.TwoColoreffect(new Color(colorCustom), new Color(colorCustom).darker().darker(), (float) Math.abs(System.currentTimeMillis() / time) / 100.0 + 6.0 * (time * 2.55) / 60.0).getRGB();
	                        break;
	                    case "none":
	                        color = -1;
	                        break;
	                    case "client":
	                        color = ClientHelper.getClientColor().getRGB();
	                        break;
	                }

	                String mode = sideMode.getOptions();
	                float f = ClientFont.fontMode.currentMode.equalsIgnoreCase("Lato") ? 1f : 0f;
	                
	                String module1 = module.getLabel();
	                if (cases.getCurrentMode().equalsIgnoreCase("Defalut")) {
	                	module1 = module.getLabel();
                    } else if (cases.getCurrentMode().equalsIgnoreCase("upper")) {
                    	module1 = module.getLabel().toUpperCase();
                    } else if (cases.getCurrentMode().equalsIgnoreCase("lower")) {
                    	module1 = module.getLabel().toLowerCase();
                    }
	                if (mode.equalsIgnoreCase("Left")) {

	                	GlStateManager.pushMatrix();
	                	GlStateManager.enableBlend();
	                    GlStateManager.disableAlpha();
	                    
	                    GL11.glTranslated(arrayX.getNumberValue(), arrayY.getNumberValue(), 1);

	                    if (glow.getBoolValue()) {
	                        RenderUtils.drawBlurredShadow(5, (float) y + 40, ClientHelper.getFontRender().getStringWidth(module1) + 3, offsetY.getNumberValue() /** module.animYto*/, (int) glowRadius.getNumberValue(), RenderUtils.injectAlpha(new Color(glowColor.getColorValue()), (int) glowAlpha.getNumberValue()));
	                       // RenderUtils.drawRect(5, (float) y + 40, ClientHelper.getFontRender().getStringWidth(module.getModuleName()) + 3, offsetY.getNumberValue() /** module.animYto*/, (int) new Color(0, 0, 0, 1).getRGB());
	                    }
	                    GL11.glTranslated(1, y, 1);
	                    GL11.glScaled(module.animYto, 1, 1);
	                    GL11.glTranslated(-1, -y, 1);
	                    if (rightBorder.getBoolValue()) {
	                    	RenderUtils.drawRect(3, y + 40, 5, y + offsetY.getNumberValue() /** module.animYto*/ + 40, color);
	                    }
	                    
	                    if (background.getBoolValue()) {
	                    	if (rightBorder.getBoolValue()) {
	                    		RenderUtils.drawGradientRect1(5, y + 40, ClientHelper.getFontRender().getStringWidth(module1) + 7 + 1, y + offsetY.getNumberValue()/** module.animYto*/ + 40, bgcolorList.currentMode.equals("Client") || bgcolorList.currentMode.equals("Astolfo") || bgcolorList.currentMode.equals("Rainbow") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("None") ? new Color( bgcolor).getRGB() : bgonecolor.getColorValue() , bgcolorList.currentMode.equals("Client") || bgcolorList.currentMode.equals("Astolfo") || bgcolorList.currentMode.equals("Rainbow") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("None") ? new Color(bgcolor).darker().getRGB() : bgonecolor.getColorValue());
	                    	} else {
	                    		RenderUtils.drawGradientRect1(5, y + 40, ClientHelper.getFontRender().getStringWidth(module1) + 7 + 1, y + offsetY.getNumberValue()/** module.animYto*/ + 40, bgcolorList.currentMode.equals("Client") || bgcolorList.currentMode.equals("Astolfo") || bgcolorList.currentMode.equals("Rainbow") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("None") ? new Color( bgcolor).getRGB() : bgonecolor.getColorValue() , bgcolorList.currentMode.equals("Client") || bgcolorList.currentMode.equals("Astolfo") || bgcolorList.currentMode.equals("Rainbow") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("None") ? bgcolor : bgonecolor.getColorValue());
	                    	}
	                    }

	                    ClientHelper.getFontRender().drawString(module1, 6, y + 40 + ClientHelper.getFontRender().getFontHeight() - 4 - f/* * module.animYto*/ + fontY.getNumberValue(), color);

	                    GlStateManager.disableBlend();
	                    GlStateManager.enableAlpha();
	                    GlStateManager.popMatrix();

	                } else if (mode.equalsIgnoreCase("Right")) {
	                	GlStateManager.pushMatrix();

	                    GL11.glTranslated(-arrayX.getNumberValue(), arrayY.getNumberValue(), 1);

	                    if (glow.getBoolValue()) {
	                    	RenderUtils.drawBlurredShadow(displayWidth - ClientHelper.getFontRender().getStringWidth(module1) - 3, (float) y, ClientHelper.getFontRender().getStringWidth(module1), offsetY.getNumberValue() /** module.animYto*/, (int) glowRadius.getNumberValue(), RenderUtils.injectAlpha(new Color(glowColor.getColorValue()), (int) glowAlpha.getNumberValue()));
	                    	RenderUtils.drawRect(5, (float) y + 40, ClientHelper.getFontRender().getStringWidth(module1) + 3, offsetY.getNumberValue() /** module.animYto*/, (int) new Color(0, 0, 0, 1).getRGB());
	                    }
	                    GL11.glTranslated(1, y, 1);
	                    GL11.glTranslated(-1, -y, 1);
	                    GL11.glScaled(1 - module.animYto + 1, 1, 1);
	                    
	                    
	                    if (background.getBoolValue()) {
	                    	if (rightBorder.getBoolValue()) {
		                    	RenderUtils.drawGradientRect1((displayWidth - ClientHelper.getFontRender().getStringWidth(module1) - 3), y, (displayWidth) , y + offsetY.getNumberValue()/** module.animYto*/, bgcolorList.currentMode.equals("Client") || bgcolorList.currentMode.equals("Astolfo") || bgcolorList.currentMode.equals("Rainbow") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("None") ? new Color( bgcolor).darker().getRGB() : bgonecolor.getColorValue() , bgcolorList.currentMode.equals("Client") || bgcolorList.currentMode.equals("Astolfo") || bgcolorList.currentMode.equals("Rainbow") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("None") ? new Color(bgcolor).getRGB() : bgonecolor.getColorValue());
	                    	} else {
		                    	RenderUtils.drawGradientRect1((displayWidth - ClientHelper.getFontRender().getStringWidth(module1) - 3), y, (displayWidth) , y + offsetY.getNumberValue()/** module.animYto*/, bgcolorList.currentMode.equals("Client") || bgcolorList.currentMode.equals("Astolfo") || bgcolorList.currentMode.equals("Rainbow") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("None") ? new Color( bgcolor).getRGB() : bgonecolor.getColorValue() , bgcolorList.currentMode.equals("Client") || bgcolorList.currentMode.equals("Astolfo") || bgcolorList.currentMode.equals("Rainbow") || bgcolorList.currentMode.equals("Custom") || bgcolorList.currentMode.equals("Fade") || bgcolorList.currentMode.equals("None") ? new Color(bgcolor).getRGB() : bgonecolor.getColorValue());
	                    	}
	                    }

	                    if (rightBorder.getBoolValue()) {
	                    	RenderUtils.drawRect(displayWidth - 1, y, displayWidth + 1, y + offsetY.getNumberValue() /** module.animYto*/, color);
	                    }
		                    ClientHelper.getFontRender().drawString(module1, displayWidth - ClientHelper.getFontRender().getStringWidth(module1) - 2, y + ClientHelper.getFontRender().getFontHeight() - 3 - f/* * module.animYto*/ + fontY.getNumberValue(), color);
	                    GlStateManager.popMatrix();
	                }
	                y += offsetY.getNumberValue() * module.animYto;
	            }
	        }
	    }
}