package splash.client.themes.splash;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import splash.Splash;
import splash.api.module.Module;
import splash.api.theme.Theme;
import splash.api.theme.type.FontType;
import splash.api.value.Value;
import splash.client.modules.visual.UI;
import splash.client.modules.visual.UI.ArrayColor;
import splash.client.themes.virtue.tab.VirtueTabGUI;
import splash.utilities.animation.AnimationUtility;
import splash.utilities.math.MathUtils;
import splash.utilities.system.ClientLogger;
import splash.utilities.visual.ColorUtilities;
import splash.utilities.visual.RenderUtilities;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPath;

import org.lwjgl.opengl.GL11;
import org.omg.CosNaming._BindingIteratorImplBase;

import com.ibm.icu.text.UFormat;

/**
 * Author: Ice Created: 15:18, 31-May-20 Project: Client
 */
public class SplashTheme extends Theme {
	int y = 0;
	public double positionX = 4;
	public double editX;

	public SplashTheme() {
		super("Splash");
	}

	@Override
	public void drawWatermark() {
		ScaledResolution scaledRes = new ScaledResolution(mc);
		RenderUtilities.INSTANCE.drawImage(new ResourceLocation("splash/splashPNGLogo.png"),
				(scaledRes.getScaledWidth() / 2000 - 5), -12, 960, 540);

	}

	public String getTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}

	@Override
	public void drawTabGUI() {
		new VirtueTabGUI().drawTabGui();
	}

	@Override
	public void onKey(int keyCode) {

	}

	@Override
	public void drawArraylist() {
		UI ui = (UI) Splash.getInstance().getModuleManager().getModuleByClass(UI.class);
		ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft());
		int yStart = 0;
		int y2 = yStart;
		float x, oldX, ygay = 0, oldY = 0, width;
		float finishedY = oldY + (ygay - oldY);
		int yStartNew = 1;
		int color = 0;
		List<Module> mods = Splash.getInstance().getModuleManager().getModulesForRender();
		mods.sort((o1, o2) -> (int) (Splash.getInstance().getFontRenderer().getStringWidthCustom(o2.getFullModuleDisplayName()) - Splash.getInstance().getFontRenderer().getStringWidthCustom(o1.getFullModuleDisplayName())));
		int yTotal = 0;
		for (int i = 0; i < mods.size(); i++) {
			yTotal += Splash.getInstance().getFontRenderer().getFontHeightCustom() + 5;
		}
		for (int i = 0; i < mods.size(); i++) {
			int fadeColor = ColorUtilities.fadeColor(-yStart * 20, ui);
			Module module = mods.get(i);
			UI.color = fadeColor;
			if (ui.arrayColor.getValue().name().equalsIgnoreCase("Category")) {//
				fadeColor = module.getColor();
			}
			if (ui.arrayColor.getValue().name().equalsIgnoreCase("Solid")) {
				fadeColor = Splash.INSTANCE.getClientColor();
			}
			if (ui.arrayColor.getValue().name().equalsIgnoreCase("Helium")) {
				fadeColor = ColorUtilities.getRainbow(-6000, y * -20).getRGB();
			}
			if (ui.arrayColor.getValue().name().equalsIgnoreCase("Exhibition")) {
				fadeColor  =ColorUtilities.getRainbow(-3000, y * -9).getRGB();
			}
			if (ui.arrayColor.getValue().name().equalsIgnoreCase("Astolfo")) {
				fadeColor = ColorUtilities.astolfoColors(yStart, yTotal);
			}
			double length = Splash.getInstance().getFontRenderer()
					.getStringWidthCustom(module.getFullModuleDisplayName());
			int startX = (int) (s.getScaledWidth()
					- Splash.getInstance().getFontRenderer().getStringWidthCustom(module.getFullModuleDisplayName()));
			AnimationUtility.animate(module);

			if (Splash.getInstance().getValueManager()
					.getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font")
					.getValue() == FontType.CUSTOM) {
				Gui.drawRect(
						(int) (s.getScaledWidth() - this.positionX - ui.xValue.getValue() - 1 - length
								+ module.getSlideAnimation()),
						yStart - 1
								- 1,
						s.getScaledWidth()
								- ui.xValue.getValue(),
						(yStart + 11), new Color(30, 30, 30, ((UI) Splash.getInstance().getModuleManager()
								.getModuleByClass(UI.class)).backgroundDarkness.getValue()).getRGB());

				Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(),
						(float) (startX + module.getSlideAnimation() - 3 - ui.xValue.getValue()), yStartNew, fadeColor);

			} else if (Splash.getInstance().getValueManager()
					.getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font")
					.getValue() == FontType.MINEMAN) {
				Gui.drawRect(
						(int) (s.getScaledWidth() - this.positionX - 1 - ui.xValue.getValue() - length
								+ module.getSlideAnimation()),
						yStart - 2,
						s.getScaledWidth()
								- ui.xValue.getValue(),
						(yStart + 12), new Color(30, 30, 30, ((UI) Splash.getInstance().getModuleManager()
								.getModuleByClass(UI.class)).backgroundDarkness.getValue()).getRGB());

				Splash.getInstance().getFontRenderer().drawStringWithShadow(module.getFullModuleDisplayName(),
						(float) (startX + module.getSlideAnimation() - 2 - ui.xValue.getValue()), yStart + 2,
						fadeColor);

			}

			if (ui.arrayBorder.getValue()) {
				if (Splash.getInstance().getValueManager()
						.getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font")
						.getValue() == FontType.MINEMAN) {
					Gui.drawRect(
							(int) (s.getScaledWidth() - this.positionX - 2 - ui.xValue.getValue() - length
									+ module.getSlideAnimation()),
							yStart - 2, (int) (s.getScaledWidth() - this.positionX - 2 - ui.xValue.getValue() - length
									+ module.getSlideAnimation()) + 1,
							yStart + 12, fadeColor);
				} else {

					Gui.drawRect(
							(int) (s.getScaledWidth() - this.positionX - 2 - ui.xValue.getValue() - length
									+ module.getSlideAnimation()),
							yStart - 1, (int) (s.getScaledWidth() - this.positionX - 2 - ui.xValue.getValue() - length
									+ module.getSlideAnimation()) + 1,
							yStart + 12, fadeColor);
				}

				if (module == mods.get(mods.size() - 1)) {
					Gui.drawRect(
							(int) (s.getScaledWidth() - this.positionX - 2 - ui.xValue.getValue() - length
									+ module.getSlideAnimation()),
							yStart + 11, s.getScaledWidth() - ui.xValue.getValue(), yStart + 12, fadeColor);
				} else {
					double length2 = Splash.getInstance().getFontRenderer()
							.getStringWidthCustom(mods.get(i + 1).getFullModuleDisplayName());
					Gui.drawRect(
							(int) (s.getScaledWidth() - this.positionX - 2 - ui.xValue.getValue() - length
									+ module.getSlideAnimation()),
							yStart + 11, (int) (s.getScaledWidth() - this.positionX - ui.xValue.getValue() - length2
									+ module.getSlideAnimation() - 1),
							yStart + 12, fadeColor);
				}

				if (module == mods.get(0)) {
					if (Splash.getInstance().getValueManager()
							.getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font")
							.getValue() == FontType.MINEMAN) {

						Gui.drawRect(
								(int) (s.getScaledWidth() - this.positionX - 1 - ui.xValue.getValue() - length
										+ module.getSlideAnimation()),
								yStart - 1, s.getScaledWidth() - ui.xValue.getValue(), yStart - 2, fadeColor);
					} else {
						Gui.drawRect(
								(int) (s.getScaledWidth() - this.positionX - 2 - ui.xValue.getValue() - length
										+ module.getSlideAnimation()),
								yStart - 1, s.getScaledWidth() - ui.xValue.getValue(), yStart - 2, fadeColor);

					}
				}
			}

			if (ui.sideBar.getValue()) {
				if (Splash.getInstance().getValueManager()
						.getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Font")
						.getValue() == FontType.MINEMAN) {
					Gui.drawRect(
							(int) (s.getScaledWidth() - positionX + ui.sidebarThickness.getValue()
									- ui.xValue.getValue()),
							yStart - 2, (int) (s.getScaledWidth() - positionX + 4 - ui.xValue.getValue()),
							yStart + 10 + 2, fadeColor);

				} else if (ui.arrayBorder.getValue()) {
					Gui.drawRect(
							(int) (s.getScaledWidth() - positionX + ui.sidebarThickness.getValue()
									- ui.xValue.getValue()),
							yStartNew - 3, (int) (s.getScaledWidth() - positionX + 4 - ui.xValue.getValue()),
							yStart + 11 + 1, fadeColor);

				} else {
					Gui.drawRect(
							(int) (s.getScaledWidth() - positionX + ui.sidebarThickness.getValue()
									- ui.xValue.getValue()),
							yStartNew - 3, (int) (s.getScaledWidth() - positionX + 4 - ui.xValue.getValue()),
							yStart + 10 + 1, fadeColor);

				}

			}

			finishedY += 12;
			y += 13;
			yStart += Splash.getInstance().getFontRenderer().getFontHeightCustom() + 5;
			yStartNew += Splash.getInstance().getFontRenderer().getFontHeightCustom() + 5;
		}
	}
}