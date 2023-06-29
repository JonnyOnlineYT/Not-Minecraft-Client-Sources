package volcano.summer.client.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.module.Module;
import volcano.summer.client.events.EventRender2D;
import volcano.summer.client.util.Colors;
import volcano.summer.client.util.R2DUtils;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;

public class HUD extends Module {

	public static ModeValue Tabgui;
	public static ModeValue rbmode;
	public static ModeValue amode;
	int ypos;
	TimerUtil timer = new TimerUtil();
	public static Value<Boolean> rainbow;
	public static Value<Boolean> model;
	public static Value<Boolean> tabgui;
	public static Value<Boolean> fastchat;
//	public static Value<Boolean> cape;
	private GuiIngame gui;
	private int posY;
	private String colorEnum = "\u00a7f";

	public HUD() {
		super("HUD", 0, Category.RENDER);
		rainbow = new Value<Boolean>("Rainbow", "rainbow", false, this);
		rbmode = new ModeValue("RBMode", "Rainbow", "Solid", new String[] { "Solid", "Motion" }, this);
		tabgui = new Value<Boolean>("Tabgui", "tabgui", true, this);
		Tabgui = new ModeValue("TabguiMode", "Tabgui", "Old", new String[] { "Old", "New" }, this);
		model = new Value<Boolean>("Icon", "icon", true, this);
		amode = new ModeValue("Arraylist", "Arraylist", "New", new String[] { "Classic", "New", "Old" }, this);
		fastchat = new Value<Boolean>("FastChat", "fastchat", false, this);
//		cape = new Value<Boolean>("Cape", "cape", false, this);
		this.ypos = 2;
		this.gui = new GuiIngame(mc);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof EventRender2D && mc.theWorld != null) {
			if (!mc.gameSettings.showDebugInfo) {
				// this.renderFps();
				// this.renderName();
				this.renderInfo(Summer.getColor().getRGB());
				if (this.amode.getStringValue().equalsIgnoreCase("New")) {
					this.renderModules();
				}
				if (this.amode.getStringValue().equalsIgnoreCase("Classic")) {
					this.renderModules2();
				}
				if (this.amode.getStringValue().equalsIgnoreCase("Old")) {
					this.renderModules3();
				}

				// this.renderModules();
				// this.renderArmor(((EventRender2D) event).sr);
				// this.renderCoords();
				if (!(this.mc.currentScreen instanceof volcano.summer.screen.clickgui.one2.ClickGui)) {
					Summer.getClickGui().drawPinned();
				}
				if (this.model.getValue()) {
					Summer.getTabGui().draw(((EventRender2D) event).getWidth(), ((EventRender2D) event).getHeight());
				} else {
					if (this.Tabgui.getStringValue().equalsIgnoreCase("Old")) {
						Summer.getTabGui().drawTopString();
					}
					if (this.Tabgui.getStringValue().equalsIgnoreCase("New")) {
						Summer.getTabGui().drawTopString2();
					}
				}
				if (this.tabgui.getValue()) {
					Summer.getTabGui().drawTabGui();
					Summer.getTabGui().drawTabGui2();
				}
			}
		}
	}

	public static void drawSmallString(String s, int x2, int y2, int color) {
		GL11.glPushMatrix();
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		Minecraft.getMinecraft().fontRendererObj.func_175063_a(s, x2 * 2, y2 * 2, color);
		GL11.glPopMatrix();
	}

	private void renderModules() {
		if (!HUD.amode.getStringValue().equalsIgnoreCase("Classic")) {
			if (!HUD.amode.getStringValue().equalsIgnoreCase("Old")) {
				if (this.rainbow.getValue()) {
					this.ypos = 1;
					if (!HUD.rbmode.getStringValue().equalsIgnoreCase("Motion")) {

						for (final Module m : this.getRenderMods()) {
							if (m.getTransition() > 0) {
								m.setTransition(m.getTransition() - 1);
							}
							final String nig = (m.displayName.length() > 0) ? (" \u00a77" + m.displayName) : "";
							int x = ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
									.getStringWidth(String.valueOf(m.name) + nig) - 4;
							final int fuckyou = (m.displayName.length() > 0)
									? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
									: 0;
							final Color color = new Color(
									Color.HSBtoRGB((float) (Minecraft.thePlayer.ticksExisted / 36.0
											+ Math.sin(36.0 * 1.5707963267948966)) % 1.0f, 0.4098824f, 1.0f));
							Gui.drawVerticalLine(x - 1, ypos - 2, ypos + mc.fontRendererObj.FONT_HEIGHT,
									color.getRGB());
							Gui.drawHorizontalLine(x - 1, Minecraft.getMinecraft().displayWidth,
									ypos + mc.fontRendererObj.FONT_HEIGHT - 1, color.getRGB());
							R2DUtils.drawRect(
									ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
											.getStringWidth(String.valueOf(m.name) + nig) - 4,
									this.ypos - 2, ScaledResolution.getScaledWidth(), this.ypos + 8, 0xff232323);
							R2DUtils.drawRect(ScaledResolution.getScaledWidth() - 2f, this.ypos - 2,
									ScaledResolution.getScaledWidth(), this.ypos + 8, color);
							Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
									ScaledResolution.getScaledWidth()
											- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2
											- fuckyou + m.getTransition(),
									this.ypos, color.getRGB());
							this.ypos += 10;
						}
					}

				} else {
					this.ypos = 1;
					for (final Module m : this.getRenderMods()) {
						if (m.getTransition() > 0) {
							m.setTransition(m.getTransition() - 1);
						}
						final String nig = (m.displayName.length() > 0) ? (" \u00a77" + m.displayName) : "";
						int x = ScaledResolution.getScaledWidth()
								- Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf(m.name) + nig)
								- 4;
						final int fuckyou = (m.displayName.length() > 0)
								? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
								: 0;
						final int color = Colors.getColor().getRGB();
						R2DUtils.drawRect(
								ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
										.getStringWidth(String.valueOf(m.name) + nig) - 4,
								this.ypos - 2, ScaledResolution.getScaledWidth(), this.ypos + 8, 0xff232323);
						R2DUtils.drawRect(ScaledResolution.getScaledWidth() - 2f, this.ypos - 2,
								ScaledResolution.getScaledWidth(), this.ypos + 8, color);
						Gui.drawVerticalLine(x - 1, ypos - 2, ypos + mc.fontRendererObj.FONT_HEIGHT, color);
						Gui.drawHorizontalLine(x - 1, Minecraft.getMinecraft().displayWidth,
								ypos + mc.fontRendererObj.FONT_HEIGHT - 1, color);
						Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
								ScaledResolution.getScaledWidth()
										- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2 - fuckyou
										+ m.getTransition(),
								this.ypos, color);
						this.ypos += 10;
					}

				}
				if (this.rainbow.getValue()) {
					if (!HUD.rbmode.getStringValue().equalsIgnoreCase("Solid")) {
						this.ypos = 1;
						for (final Module m : this.getRenderMods()) {
							if (m.getTransition() > 0) {
								m.setTransition(m.getTransition() - 1);
							}
							final String nig = (m.displayName.length() > 0) ? (" \u00a77" + m.displayName) : "";
							int x = ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
									.getStringWidth(String.valueOf(m.name) + nig) - 4;
							final int fuckyou = (m.displayName.length() > 0)
									? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
									: 0;
							final int color = getRainbow(6000, -15 * ypos);
							R2DUtils.drawRect(
									ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
											.getStringWidth(String.valueOf(m.name) + nig) - 4,
									this.ypos - 2, ScaledResolution.getScaledWidth(), this.ypos + 8, 0xff232323);
							R2DUtils.drawRect(ScaledResolution.getScaledWidth() - 2f, this.ypos - 2,
									ScaledResolution.getScaledWidth(), this.ypos + 8, color);
							Gui.drawVerticalLine(x - 1, ypos - 2, ypos + mc.fontRendererObj.FONT_HEIGHT, color);
							Gui.drawHorizontalLine(x - 1, Minecraft.getMinecraft().displayWidth,
									ypos + mc.fontRendererObj.FONT_HEIGHT - 1, color);
							Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
									ScaledResolution.getScaledWidth()
											- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2
											- fuckyou + m.getTransition(),
									this.ypos, color);
							this.ypos += 10;
						}
					}
				}
			}
		}
	}

	private void renderModules2() {
		if (!HUD.amode.getStringValue().equalsIgnoreCase("New")) {
			if (!HUD.amode.getStringValue().equalsIgnoreCase("Old")) {
				if (this.rainbow.getValue()) {
					this.ypos = 1;
					if (!HUD.rbmode.getStringValue().equalsIgnoreCase("Motion")) {

						for (final Module m : this.getRenderMods()) {
							if (m.getTransition() > 0) {
								m.setTransition(m.getTransition() - 1);
							}
							final String nig = (m.displayName.length() > 0) ? (" \u00a77" + m.displayName) : "";
							int x = ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
									.getStringWidth(String.valueOf(m.name) + nig) - 4;
							final int fuckyou = (m.displayName.length() > 0)
									? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
									: 0;
							final Color color = new Color(
									Color.HSBtoRGB((float) (Minecraft.thePlayer.ticksExisted / 36.0
											+ Math.sin(36.0 * 1.5707963267948966)) % 1.0f, 0.4098824f, 1.0f));
							Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
									ScaledResolution.getScaledWidth()
											- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2
											- fuckyou + m.getTransition(),
									this.ypos, color.getRGB());
							this.ypos += 10;
						}
					}

				} else {
					this.ypos = 1;
					for (final Module m : this.getRenderMods()) {
						if (m.getTransition() > 0) {
							m.setTransition(m.getTransition() - 1);
						}
						final String nig = (m.displayName.length() > 0) ? (" \u00a77" + m.displayName) : "";
						int x = ScaledResolution.getScaledWidth()
								- Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf(m.name) + nig)
								- 4;
						final int fuckyou = (m.displayName.length() > 0)
								? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
								: 0;
						final int color = Colors.getColor().getRGB();
						Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
								ScaledResolution.getScaledWidth()
										- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2 - fuckyou
										+ m.getTransition(),
								this.ypos, color);
						this.ypos += 10;
					}

				}
				if (this.rainbow.getValue()) {
					if (!HUD.rbmode.getStringValue().equalsIgnoreCase("Solid")) {
						this.ypos = 1;
						for (final Module m : this.getRenderMods()) {
							if (m.getTransition() > 0) {
								m.setTransition(m.getTransition() - 1);
							}
							final String nig = (m.displayName.length() > 0) ? (" \u00a77" + m.displayName) : "";
							int x = ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
									.getStringWidth(String.valueOf(m.name) + nig) - 4;
							final int fuckyou = (m.displayName.length() > 0)
									? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
									: 0;
							final int color = getRainbow(6000, -15 * ypos);
							Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
									ScaledResolution.getScaledWidth()
											- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2
											- fuckyou + m.getTransition(),
									this.ypos, color);
							this.ypos += 10;
						}
					}
				}
			}
		}
	}

	private void renderModules3() {
		if (!HUD.amode.getStringValue().equalsIgnoreCase("Classic")) {
			if (!HUD.amode.getStringValue().equalsIgnoreCase("New")) {
				if (this.rainbow.getValue()) {
					this.ypos = 2;
					if (!HUD.rbmode.getStringValue().equalsIgnoreCase("Motion")) {

						for (final Module m : this.getRenderMods()) {
							final String nig = (m.displayName.length() > 0) ? (" §7" + m.displayName) : "";
							final int fuckyou = (m.displayName.length() > 0)
									? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
									: 0;
							final Color color = new Color(
									Color.HSBtoRGB((float) (Minecraft.thePlayer.ticksExisted / 36.0
											+ Math.sin(36.0 * 1.5707963267948966)) % 1.0f, 0.4098824f, 1.0f));
							R2DUtils.drawRect(
									ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
											.getStringWidth(String.valueOf(m.name) + nig) - 4,
									this.ypos - 2, ScaledResolution.getScaledWidth(), this.ypos + 8, Integer.MIN_VALUE);
							R2DUtils.drawRect(ScaledResolution.getScaledWidth() - 2f, this.ypos - 2,
									ScaledResolution.getScaledWidth(), this.ypos + 8, color);
							Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
									ScaledResolution.getScaledWidth()
											- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2
											- fuckyou,
									this.ypos, color.getRGB());
							this.ypos += 10;
						}
					}

				} else {
					this.ypos = 2;
					for (final Module m : this.getRenderMods()) {
						final String nig = (m.displayName.length() > 0) ? (" §7" + m.displayName) : "";
						final int fuckyou = (m.displayName.length() > 0)
								? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
								: 0;
						final int color = Colors.getColor().getRGB();
						R2DUtils.drawRect(
								ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
										.getStringWidth(String.valueOf(m.name) + nig) - 4,
								this.ypos - 2, ScaledResolution.getScaledWidth(), this.ypos + 8, Integer.MIN_VALUE);
						R2DUtils.drawRect(ScaledResolution.getScaledWidth() - 2f, this.ypos - 2,
								ScaledResolution.getScaledWidth(), this.ypos + 8, color);
						Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
								ScaledResolution.getScaledWidth()
										- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2 - fuckyou,
								this.ypos, color);
						this.ypos += 10;
					}

				}
				if (this.rainbow.getValue()) {
					if (!HUD.rbmode.getStringValue().equalsIgnoreCase("Solid")) {
						this.ypos = 2;
						for (final Module m : this.getRenderMods()) {
							final String nig = (m.displayName.length() > 0) ? (" §7" + m.displayName) : "";
							final int fuckyou = (m.displayName.length() > 0)
									? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.displayName) + 4)
									: 0;
							final int color = getRainbow(6000, -15 * ypos);
							R2DUtils.drawRect(
									ScaledResolution.getScaledWidth() - Minecraft.getMinecraft().fontRendererObj
											.getStringWidth(String.valueOf(m.name) + nig) - 4,
									this.ypos - 2, ScaledResolution.getScaledWidth(), this.ypos + 8, Integer.MIN_VALUE);
							R2DUtils.drawRect(ScaledResolution.getScaledWidth() - 2f, this.ypos - 2,
									ScaledResolution.getScaledWidth(), this.ypos + 8, color);
							Minecraft.getMinecraft().fontRendererObj.func_175063_a(String.valueOf(m.name) + nig,
									ScaledResolution.getScaledWidth()
											- Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.name) - 2
											- fuckyou,
									this.ypos, color);
							this.ypos += 10;
						}
					}
				}
			}
		}
	}

	public static Color fade(float offset) {
		float hue = (System.nanoTime() + offset) / 1.0E10F % 8.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.001F)).intValue()),
				16);
		Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0F, c.getGreen() / 255.0F, c.getBlue() / 255.0F, c.getAlpha() / 255.0F);
	}

	public int indexOfMod(final Module m) {
		int index = 0;
		for (final Module horse : this.getRenderMods()) {
			if (m == horse) {
				return index;
			}
			++index;
		}
		return index;
	}

	public void renderInfo(final int color) {
		if (this.gui.getChatGUI().getChatOpen()) {
			++this.posY;
		} else {
			--this.posY;
		}
		this.posY = MathHelper.clamp_int(this.posY, 10, 24);
		final int[] pos = { (int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ };
		Minecraft.getMinecraft().fontRendererObj.func_175063_a(
				String.format("XYZ: " + this.colorEnum + "%s %s %s", pos[0], pos[1], pos[2]), 2.0f,
				R2DUtils.height() - this.posY, color);
		final Object[] array = { null };
		final int n = 0;
		array[n] = Minecraft.func_175610_ah();
		int yP = this.posY;
		Potion[] potionTypes;
		for (int length = (potionTypes = Potion.potionTypes).length, j = 0; j < length; ++j) {
			final Potion p = potionTypes[j];
			if (p != null) {
				if (mc.thePlayer.isPotionActive(p.id)) {
					final String pot = String.format("%s " + this.colorEnum + "%s",
							I18n.format(p.getName(), new Object[0]),
							Potion.getDurationString(mc.thePlayer.getActivePotionEffect(p)));
					Minecraft.getMinecraft().fontRendererObj.func_175063_a(pot,
							R2DUtils.width() - Minecraft.getMinecraft().fontRendererObj.getStringWidth(pot) - 2,
							R2DUtils.height() - yP, p.getLiquidColor());
					yP += 12;
				}
			}
		}
		for (int i = 0; i < 5; ++i) {
			final ItemStack ia = mc.thePlayer.getEquipmentInSlot(i);
			if (ia != null) {
				RenderHelper.enableStandardItemLighting();
				int height;
				int width = height = 0;
				if (i == 0) {
					width = R2DUtils.width() / 2 - 9;
					height = R2DUtils.height() - 52;
					mc.getRenderItem().func_175042_a(ia, width, height);
					mc.getRenderItem().func_175030_a(Minecraft.getMinecraft().fontRendererObj, ia, width, height);
				} else if (i >= 3) {
					width = R2DUtils.width() / 2 - 110;
					height = R2DUtils.height() - ((i == 4) ? 32 : 17);
					mc.getRenderItem().func_175042_a(ia, width, height);
					mc.getRenderItem().func_175030_a(Minecraft.getMinecraft().fontRendererObj, ia, width, height);
				} else {
					width = R2DUtils.width() / 2 + 92;
					height = R2DUtils.height() - ((i == 2) ? 32 : 17);
					mc.getRenderItem().func_175042_a(ia, width, height);
					mc.getRenderItem().func_175030_a(Minecraft.getMinecraft().fontRendererObj, ia, width, height);
				}
				RenderHelper.disableStandardItemLighting();
			}
		}
	}

	private List<Module> getRenderMods() {
		final List<Module> psuedoMods = new ArrayList<Module>();
		for (final Module m : Summer.moduleManager.getMods()) {
			if ((m.state) && (m.getName() != "ClickGui") && (m.getName() != "HUD") && (m.getName() != "Hypixel")
					&& (m.getName() != "NCP")) {
				psuedoMods.add(m);
			}
		}
		psuedoMods.sort((o1, o2) -> {
			final int o1l = ((o1.displayName.length() > 0)
					? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(o1.displayName)
							+ Minecraft.getMinecraft().fontRendererObj.getStringWidth("12"))
					: 0);
			final int o2l = ((o2.displayName.length() > 0)
					? (Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.displayName)
							+ Minecraft.getMinecraft().fontRendererObj.getStringWidth("12"))
					: 0);
			if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(o1.name)
					+ o1l > Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.name) + o2l) {
				return -1;
			} else if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(o1.name)
					+ o2l == Minecraft.getMinecraft().fontRendererObj.getStringWidth(o2.name) + o2l) {
				return 0;
			} else {
				return 1;
			}
		});
		return psuedoMods;
	}

	public static double rn = 1.5D;

	private int Color() {
		int cxd = 0;
		cxd = (int) (cxd + rn);
		if (cxd > 50) {
			cxd = 0;
		}
		Minecraft.getMinecraft();
		Color color = new Color(Color.HSBtoRGB(
				(float) (Minecraft.thePlayer.ticksExisted / 60.0D + Math.sin(cxd / 60.0D * 1.5707963267948966D)) % 1.0F,
				0.5882353F, 1.0F));
		int col = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200).getRGB();
		return col;
	}

	public static Color fade(long offset, float fade) {
		float hue = (System.nanoTime() + offset) / 1.0E10F % 1.0F;
		long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
				16);
		Color c = new Color((int) color);
		return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
				c.getAlpha() / 255.0F);
	}

	public static int getRainbow(int speed, int offset) {
		float hue = (System.currentTimeMillis() + offset) % speed;
		hue /= speed;
		return Color.getHSBColor(hue, 1.0F, 1.0F).getRGB();
	}

	public static int RGBtoHEX(int r, int g, int b, int a) {
		return (a << 24) + (r << 16) + (g << 8) + b;
	}
}
