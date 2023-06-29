package volcano.summer.screen.tabgui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.Minecraft;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.base.manager.module.Module.Category;
import volcano.summer.client.util.Colors;
import volcano.summer.client.util.MathUtil;
import volcano.summer.client.util.R2DUtils;

public class TabGuiRainBow {
	private static final int NO_COLOR = 0;
	private static final int INSIDE_COLOR = -1610612736;
	private static final int BORDER_COLOR = 2013265920;
	private static final int COMPONENT_HEIGHT = 14;
	private static int baseCategoryWidth;
	private static int baseCategoryHeight;
	private static int baseModWidth;
	private static int baseModHeight;
	private static Section section = Section.CATEGORY;
	private static int categoryTab = 0;
	private static int modTab = 0;
	private static int categoryPosition = 15;
	private static int categoryTargetPosition = 15;
	private static int modPosition = 15;
	private static int modTargetPosition = 15;
	private static boolean transitionQuickly;
	private static long lastUpdateTime;

	public static void init() {
		int highestWidth = 0;
		Category[] values;
		int length = (values = Category.values()).length;
		for (int i = 0; i < length; i++) {
			Category category = values[i];
			String name = MathUtil.StringUtil.capitalize(category.name().toLowerCase());
			int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
			highestWidth = Math.max(stringWidth, highestWidth);
		}
		baseCategoryWidth = highestWidth + 6;
		baseCategoryHeight = Category.values().length * 14 + 2;
	}

	public static void render() {
		updateBars();
		R2DUtils.drawRect(2.0F, 14.0F, 2 + baseCategoryWidth, 14 + baseCategoryHeight, -1610612736);
		R2DUtils.drawRect(3.0F, categoryPosition, 2 + baseCategoryWidth - 1, categoryPosition + 14,
				Colors.getRainbow(0L, 1.0F).hashCode());
		int yPos = 15;
		int yPosBottom = 29;
		for (int i = 0; i < Category.values().length; i++) {
			Category category = Category.values()[i];
			String name = MathUtil.StringUtil.capitalize(category.name().toLowerCase());
			Minecraft.getMinecraft().fontRendererObj.func_175063_a(name,
					baseCategoryWidth / 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2 + 3,
					yPos + 3, -1);
			yPos += 14;
			yPosBottom += 14;
		}
		if (section == Section.MODS) {
			R2DUtils.drawRect(baseCategoryWidth + 4, categoryPosition - 1, baseCategoryWidth + 2 + baseModWidth,
					categoryPosition + getModsInCategory(Category.values()[categoryTab]).size() * 14 + 1, -1610612736);
			R2DUtils.drawRect(baseCategoryWidth + 5, modPosition, baseCategoryWidth + baseModWidth + 1,
					modPosition + 14, Colors.getRainbow(0L, 1.0F).hashCode());
			yPos = categoryPosition;
			yPosBottom = categoryPosition + 14;
			for (int i = 0; i < getModsInCategory(Category.values()[categoryTab]).size(); i++) {
				Module mod = getModsInCategory(Category.values()[categoryTab]).get(i);
				String name = mod.name;
				Minecraft.getMinecraft().fontRendererObj.func_175063_a(name,
						baseCategoryWidth + baseModWidth / 2
								- Minecraft.getMinecraft().fontRendererObj.getStringWidth(name) / 2 + 3,
						yPos + 3, mod.state ? -1 : -4210753);
				yPos += 14;
				yPosBottom += 14;
			}
		}
	}

	public static void keyPress(int key) {
		if (section == Section.CATEGORY) {
			switch (key) {
			case 205:
				int highestWidth = 0;
				for (Module module : getModsInCategory(Category.values()[categoryTab])) {
					String name = MathUtil.StringUtil.capitalize(module.name.toLowerCase());
					int stringWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
					highestWidth = Math.max(stringWidth, highestWidth);
				}
				baseModWidth = highestWidth + 6;
				baseModHeight = getModsInCategory(Category.values()[categoryTab]).size() * 14 + 2;
				modTargetPosition = modPosition = categoryTargetPosition;
				modTab = 0;
				section = Section.MODS;
				break;
			case 208:
				categoryTab += 1;
				categoryTargetPosition += 14;
				if (categoryTab <= Category.values().length - 1) {
					break;
				}
				transitionQuickly = true;
				categoryTargetPosition = 15;
				categoryTab = 0;
				break;
			case 200:
				categoryTab -= 1;
				categoryTargetPosition -= 14;
				if (categoryTab >= 0) {
					break;
				}
				transitionQuickly = true;
				categoryTargetPosition = 15 + (Category.values().length - 1) * 14;
				categoryTab = Category.values().length - 1;
			}
		} else if (section == Section.MODS) {
			switch (key) {
			case 203:
				section = Section.CATEGORY;
				break;
			case 205:
				Module mod = getModsInCategory(Category.values()[categoryTab]).get(modTab);
				mod.toggleMod();
				break;
			case 208:
				modTab += 1;
				modTargetPosition += 14;
				if (modTab > getModsInCategory(Category.values()[categoryTab]).size() - 1) {
					transitionQuickly = true;
					modTargetPosition = categoryTargetPosition;
					modTab = 0;
				}
				break;
			case 200:
				modTab -= 1;
				modTargetPosition -= 14;
				if (modTab < 0) {
					transitionQuickly = true;
					modTargetPosition = categoryTargetPosition
							+ (getModsInCategory(Category.values()[categoryTab]).size() - 1) * 14;
					modTab = getModsInCategory(Category.values()[categoryTab]).size() - 1;
				}
				break;
			}
		}
	}

	private static void updateBars() {
		long timeDifference = System.currentTimeMillis() - lastUpdateTime;
		lastUpdateTime = System.currentTimeMillis();
		int increment = transitionQuickly ? 100 : 20;
		increment = Math.max(1, Math.round(increment * timeDifference / 100L));
		if (categoryPosition < categoryTargetPosition) {
			categoryPosition += increment;
			if (categoryPosition >= categoryTargetPosition) {
				categoryPosition = categoryTargetPosition;
				transitionQuickly = false;
			}
		} else if (categoryPosition > categoryTargetPosition) {
			categoryPosition -= increment;
			if (categoryPosition <= categoryTargetPosition) {
				categoryPosition = categoryTargetPosition;
				transitionQuickly = false;
			}
		}
		if (modPosition < modTargetPosition) {
			modPosition += increment;
			if (modPosition >= modTargetPosition) {
				modPosition = modTargetPosition;
				transitionQuickly = false;
			}
		} else if (modPosition > modTargetPosition) {
			modPosition -= increment;
			if (modPosition <= modTargetPosition) {
				modPosition = modTargetPosition;
				transitionQuickly = false;
			}
		}
	}

	private static List<Module> getModsInCategory(Category Category) {
		List<Module> modList = new ArrayList<Module>();
		for (Module mod : getSortedModuleArray()) {
			if (mod.category == Category) {
				modList.add(mod);
			}
		}
		return modList;
	}

	private static List<Module> getSortedModuleArray() {
		final List<Module> list = new ArrayList<Module>();
		for (final Module mod : Summer.moduleManager.mods) {
			list.add(mod);
		}
		list.sort(new Comparator<Module>() {
			@Override
			public int compare(final Module m1, final Module m2) {
				final String s1 = m1.name;
				final String s2 = m2.name;
				final int cmp = Minecraft.getMinecraft().fontRendererObj.getStringWidth(s2)
						- Minecraft.getMinecraft().fontRendererObj.getStringWidth(s1);
				return (cmp != 0) ? cmp : s2.compareTo(s1);
			}
		});
		return list;
	}

	private enum Section {
		CATEGORY("CATEGORY", 0), MODS("MODS", 1);
		private Section(String s, int n) {
		}
	}
}