package volcano.summer.screen.tabgui;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Timer;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.module.Module;
import volcano.summer.base.manager.module.Module.Category;
import volcano.summer.client.modules.render.HUD;
import volcano.summer.client.util.GLUtil;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.ClampedValue;
import volcano.summer.client.value.ModeValue;
import volcano.summer.client.value.Value;
import volcano.summer.screen.LogoUtil;
import volcano.summer.screen.Vec2f;

public class OriginalTabGui {
	public TimerUtil timer = new TimerUtil();
	private ArrayList<String> category = new ArrayList();
	private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	private int selectedTab;
	private int selectedMod;
	private int selectedOption;
	public int start = 30;
	public int start1 = this.start + 14;
	public int otherstart = 17;
	public int otherstart1 = this.otherstart + 14;
	public int end;
	public int screen = 0;
	public long lastUpdateTime;
	public boolean editMode = false;
	public boolean listening = false;
	public int trans = 1 + fr.getStringWidth("S" + "\u00A7fummer b" + Summer.version) + 2;
	public int catewidth = 1 + fr.getStringWidth("S" + "\u00A7fummer b" + Summer.version);

	public OriginalTabGui() {
		Category[] arrayOfModCategory;
		int j = (arrayOfModCategory = Category.values()).length;
		for (int i = 0; i < j; i++) {
			Category mc = arrayOfModCategory[i];
			this.category.add(
					mc.toString().substring(0, 1) + mc.toString().substring(1, mc.toString().length()).toLowerCase());
		}
	}

	public void draw(double width, double height) {
		LogoUtil.INSTANCE.drawImage(LogoUtil.INSTANCE.getAssset("icon"), new Vec2f((((57 - 50) / 2)), 2), 50, 46);
	}

	public void drawTabGui() {
		if (!HUD.Tabgui.getStringValue().equalsIgnoreCase("New")) {

			int top = HUD.model.getValue() ? 50 : 17;
			int startx = 1;
			int endX = fr.getStringWidth("S" + "\u00A7fummer b" + Summer.version);
			int otherstartx = endX + 7;
			int backgroundColor = 0xff232323;
			int toggledColor = -1;
			int normalColor = 0x908B8C82;

			int startY = (this.selectedOption * 12 + this.otherstart);
			int endY = (startY + 14);

			this.start = (this.selectedTab * 12 + top);
			this.start1 = (this.start + 14);
			this.otherstart = (this.selectedMod * 12 + this.start);
			this.otherstart1 = (this.otherstart + 14);
			int count = 0;

			GLUtil.drawBorderRect(startx, top, endX + 7, top + 3 + this.category.size() * 10 + 12, 1, backgroundColor,
					1.0F);
			GLUtil.drawBorderRect(startx, this.start, endX + 7, this.start1, 203, Summer.getColor().getRGB(), 1.0F);
			fr.func_175063_a("FPS: \u00A7f" + Minecraft.debugFPS, 3, top + 4 + this.category.size() * 10 + 14,
					Summer.getColor().getRGB());
			int categoryCount = 0;
			for (String s : this.category) {
				if (category.get(this.selectedTab).toLowerCase().equals(s.toLowerCase())) {
					fr.func_175063_a(s, 7, top + 3 + categoryCount * 12, toggledColor);
				} else {
					fr.func_175063_a(s, 6, top + 3 + categoryCount * 12, toggledColor);
				}
				categoryCount++;
			}
			if (screen == 1 || screen == 2) {
				GLUtil.drawBorderRect(otherstartx, this.start, otherstartx + getLongestModWidth() + 7,
						getModsForCategory().size() * 12 + 2 + this.start, 1, backgroundColor, 1.0F);
				GLUtil.drawBorderRect(otherstartx, this.otherstart, otherstartx + getLongestModWidth() + 7,
						this.otherstart1, 203, Summer.getColor().getRGB(), 1.0F);
				int modCount = 0;
				for (Module mod : getModsForCategory()) {
					String name = mod.getName();
					if (Summer.valueManager.getModValues(mod).size() > 0) {
						if (mod == getSelectedMod()) {
							fr.func_175063_a(name, otherstartx + 5, this.start + 3 + modCount * 12,
									mod.getState() ? toggledColor : normalColor);
						} else {
							fr.func_175063_a(name, otherstartx + 2, this.start + 3 + modCount * 12,
									mod.getState() ? toggledColor : normalColor);
						}

						fr.func_175063_a(">", otherstartx + getLongestModWidth() - fr.getStringWidth(">"),
								this.start + 3 + modCount * 12, -1);
					} else {
						if (mod == getSelectedMod()) {
							fr.func_175063_a(name, otherstartx + 5, this.start + 3 + modCount * 12,
									mod.getState() ? toggledColor : normalColor);
						} else {
							fr.func_175063_a(name, otherstartx + 2, this.start + 3 + modCount * 12,
									mod.getState() ? toggledColor : normalColor);
						}

					}
					modCount++;
				}
			}
			if (screen == 2) {
				int x = otherstartx + getLongestModWidth() + 4 + 5;
				Module m = getSelectedMod();

				GLUtil.drawBorderRect(x - 1, this.otherstart, (x - 1) + getLongestOptionWidth() + 12,
						Summer.valueManager.getModValues(getSelectedMod()).size() * 12 + 2 + this.otherstart, 1,
						backgroundColor, 1.0F);
				GLUtil.drawBorderRect(x - 1, startY, (x - 1) + getLongestOptionWidth() + 12, endY, 203,
						Summer.getColor().getRGB(), 1.0F);
				int modCount = 0;
				for (Value s : Summer.valueManager.getModValues(m)) {
					if (s instanceof ClampedValue) {
						ClampedValue cv = (ClampedValue) s;
						int height = fr.FONT_HEIGHT - 3;
						double percentBar = ((double) cv.getValue() - (double) cv.getMin())
								/ ((double) cv.getMax() - (double) cv.getMin());

						if (s == getCurrentOption()) {
							float scale = 0.7F;
							GL11.glScalef(scale, scale, scale);
							fr.func_175063_a(cv.getName() + ": " + (Math.round((double) cv.getValue() * 100D) / 100D),
									(x + 2) / scale, (this.otherstart + 3 + modCount * 12) / scale,
									editMode && cv == getCurrentOption() ? -1 : normalColor);
							GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
							Gui.drawRect(x, this.otherstart + 3 + modCount * 12 + height + 1,
									x + getLongestOptionWidth() + 10,
									this.otherstart + 3 + modCount * 12 + 5 + height - 1, 0xff101010);
							Gui.drawRect(x, this.otherstart + 3 + modCount * 12 + height + 1,
									(int) (x + (percentBar * getLongestOptionWidth() + 10)),
									this.otherstart + 3 + modCount * 12 + 5 + height - 1, -1);
						} else {
							float scale = 0.7F;
							GL11.glScalef(scale, scale, scale);
							fr.func_175063_a(cv.getName() + ": " + (Math.round((double) cv.getValue() * 100D) / 100D),
									(x + 2) / scale, (this.otherstart + 3 + modCount * 12) / scale,
									editMode && cv == getCurrentOption() ? -1 : normalColor);
							GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
							Gui.drawRect(x + 2, this.otherstart + 3 + modCount * 12 + height + 1,
									x + getLongestOptionWidth() + 8,
									this.otherstart + 3 + modCount * 12 + 5 + height - 1, 0xff101010);
							Gui.drawRect(x + 2, this.otherstart + 3 + modCount * 12 + height + 1,
									(int) (x + (percentBar * getLongestOptionWidth() + 8)),
									this.otherstart + 3 + modCount * 12 + 5 + height - 1, -1);
						}

					} else if (s instanceof ModeValue) {
						ModeValue mv = (ModeValue) s;
						if (s == getCurrentOption()) {
							fr.func_175063_a(mv.getName() + ": " + mv.getStringValue(), x + 5,
									this.otherstart + 3 + modCount * 12,
									editMode && s == getCurrentOption() ? -1 : normalColor);
						} else {
							fr.func_175063_a(s.getName() + ": " + mv.getStringValue(), x + 2,
									this.otherstart + 3 + modCount * 12,
									editMode && s == getCurrentOption() ? -1 : normalColor);
						}

					} else if (s instanceof Value) {
						if (s == getCurrentOption()) {
							fr.func_175063_a(s.getName(), x + 5, this.otherstart + 3 + modCount * 12,
									(Boolean) s.getValue() ? -1 : normalColor);
						} else {
							fr.func_175063_a(s.getName(), x + 2, this.otherstart + 3 + modCount * 12,
									(Boolean) s.getValue() ? -1 : normalColor);
						}

					}
					modCount++;
				}

			}

			if (listening) {
				insert();
			}
		}
	}

	public void drawTopString() {
		float scale = 1.45F;
		GL11.glScalef(scale, scale, scale);
		int sliderColor = -65536;
		GL11.glPushMatrix();
		fr.func_175063_a("S" + "\u00A7fummer b" + Summer.version, 2, 2, Summer.getColor().getRGB());
		GL11.glPopMatrix();
		GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
	}

	public void down() {
		if (screen == 0) {
			if (this.selectedTab >= this.category.size() - 1) {
				this.selectedTab = -1;
			}
			this.selectedTab += 1;
		} else if (screen == 1) {
			if (this.selectedMod >= getModsForCategory().size() - 1) {
				this.selectedMod = -1;
			}
			this.selectedMod += 1;
		}

		else if (screen == 2 && !editMode) {
			if (this.selectedOption >= Summer.valueManager.getModValues(getSelectedMod()).size() - 1) {
				this.selectedOption = -1;
			}
			this.selectedOption += 1;
		}

		if (editMode) {
			Value s = getCurrentOption();
			if (s instanceof ClampedValue) {
				ClampedValue cv = (ClampedValue) s;
				if (cv.onlyInt()) {
					cv.setValue((double) cv.getValue() - 1);
				} else {
					cv.setValue((double) cv.getValue() - 0.1);
				}
				if ((double) cv.getValue() < (double) cv.getMin()) {
					cv.setValue(cv.getMin());
				}
			} else if (s instanceof ModeValue) {
				ModeValue mv = (ModeValue) s;
				mv.next();
			}
		}
	}

	public void up() {
		if (screen == 0) {
			if (this.selectedTab <= 0) {
				this.selectedTab = this.category.size();
			}
			this.selectedTab -= 1;
		} else if (screen == 1) {
			if (this.selectedMod <= 0) {
				this.selectedMod = getModsForCategory().size();
			}
			this.selectedMod -= 1;
		} else if (screen == 2 && !editMode) {
			if (this.selectedOption <= 0) {
				this.selectedOption = Summer.valueManager.getModValues(getSelectedMod()).size();
			}
			this.selectedOption -= 1;
		}

		if (editMode) {
			Value s = getCurrentOption();
			if (s instanceof ClampedValue) {
				ClampedValue cv = (ClampedValue) s;
				if (cv.onlyInt()) {
					cv.setValue((double) cv.getValue() + 1);
				} else {
					cv.setValue((double) cv.getValue() + 0.1);
				}
				if ((double) cv.getValue() > (double) cv.getMax()) {
					cv.setValue(cv.getMax());
				}
			} else if (s instanceof ModeValue) {
				ModeValue mv = (ModeValue) s;
				mv.next();
			}
		}
	}

	public void left() {
		if (screen == 1) {
			screen = 0;
		} else if (screen == 2) {
			this.selectedOption = 0;
			screen = 1;
			editMode = false;
		} else if (screen == 3) {
			screen = 2;
			editMode = false;
		}
	}

	public void right() {
		if (screen == 1) {
			if (Summer.valueManager.getModValues(getSelectedMod()).size() > 0) {
				screen = 2;
			} else {
				enter();
			}
			this.selectedOption = 0;
		} else if (screen == 0) {
			screen = 1;
			this.selectedMod = 0;
		}
	}

	public void enter() {
		if (screen == 1) {
			getModsForCategory().get(this.selectedMod).toggleMod();
		} else if (screen == 2 && getCurrentOption() instanceof Value && !(getCurrentOption() instanceof ClampedValue)
				&& !(getCurrentOption() instanceof ModeValue)) {
			getCurrentOption().setValue(!(boolean) getCurrentOption().getValue());
		} else if (screen == 2) {
			editMode = !editMode;
		}
	}

	public void insert() {
		if (screen == 1) {
			listening = true;
			Module mod = getSelectedMod();
			ScaledResolution s = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth,
					Minecraft.getMinecraft().displayHeight);
			Gui.drawRect(0, 0, Minecraft.getMinecraft().currentScreen.width,
					Minecraft.getMinecraft().currentScreen.height, 0x88101010);
			GL11.glPushMatrix();
			GL11.glTranslatef(s.getScaledWidth() / 2, s.getScaledHeight() / 2, 0.0F);
			GL11.glScalef(4.0F, 4.0F, 0F);
			Minecraft.getMinecraft().fontRendererObj.drawTotalCenteredStringWithShadow("Listening...", 0, -10,
					0xffffffff);
			GL11.glScalef(0.5F, 0.5F, 0F);
			Minecraft.getMinecraft().fontRendererObj.drawTotalCenteredStringWithShadow(
					"Press 'ESCAPE' to unbind " + mod.getName()
							+ (mod.getBind() > -1 ? " (" + Keyboard.getKeyName(mod.getBind()) + ")" : ""),
					0, 0, 0xffffffff);
			GL11.glScalef(0.25F, 0.25F, 0F);
			GL11.glPopMatrix();
		}

	}

	public void keyTyped(int keyCode) {
		Module mod = getSelectedMod();
		if (keyCode != Keyboard.KEY_ESCAPE) {
			Summer.tellPlayer("Bound '" + mod.getName() + "'" + " to '" + Keyboard.getKeyName(keyCode) + "'");
			mod.setBind(keyCode);
		} else {
			Summer.tellPlayer("Unbound '" + mod.getName() + "'");
			mod.setBind(Keyboard.KEY_NONE);
		}
		listening = false;
	}

	public void keyPress(int key) {
		if (listening) {
			keyTyped(key);
		} else {
			if (key == Keyboard.KEY_RETURN) {
				enter();
			}
			if (key == Keyboard.KEY_DOWN) {
				down();
			}
			if (key == Keyboard.KEY_UP) {
				up();
			}
			if (key == Keyboard.KEY_RIGHT) {
				right();
			}
			if (key == Keyboard.KEY_LEFT) {
				left();
			}
			if (key == Keyboard.KEY_INSERT) {
				insert();
			}
		}

	}

	public Value getCurrentOption() {
		return Summer.valueManager.getModValues(getSelectedMod()).get(this.selectedOption);

	}

	public Module getSelectedMod() {
		return getModsForCategory().get(this.selectedMod);
	}

	private ArrayList<Module> getModsForCategory() {
		ArrayList<Module> mods = new ArrayList();
		for (Module mod : Summer.moduleManager.getMods()) {
			if (mod.getModCategory(Category.valueOf(this.category.get(this.selectedTab).toUpperCase()))) {
				mods.add(mod);
			}
		}
		return mods;
	}

	private int getLongestModWidth() {
		int longest = 0;
		for (Module mod : getModsForCategory()) {
			if (Summer.valueManager.getModValues(mod).size() > 0) {
				if (fr.getStringWidth(mod.getName() + " \u00A7f>") > longest) {
					longest = fr.getStringWidth(" " + mod.getName() + " \u00A7f>>");
				}
			} else {
				if (fr.getStringWidth(mod.getName()) > longest) {
					longest = fr.getStringWidth(" " + mod.getName());
				}
			}

		}
		return longest;
	}

	private int getLongestOptionWidth() {
		int longest = 0;
		for (Value mod : Summer.valueManager.getModValues(getSelectedMod())) {
			if (mod instanceof ModeValue) {
				ModeValue mv = (ModeValue) mod;
				if (fr.getStringWidth(mod.getName() + ": " + mv.getStringValue()) > longest) {
					longest = fr.getStringWidth(mod.getName() + ": " + mv.getStringValue());
				}
			} else if (mod instanceof ClampedValue) {
				ClampedValue cv = (ClampedValue) mod;
				if (fr.getStringWidth(
						" " + mod.getName() + ": " + (Math.round((double) cv.getValue() * 100D) / 100D)) > longest) {
					longest = fr.getStringWidth(
							" " + mod.getName() + ": " + (Math.round((double) cv.getValue() * 100D) / 100D));
				}
			} else if (mod instanceof Value) {
				if (fr.getStringWidth(" " + mod.getName()) > longest) {
					longest = fr.getStringWidth(" " + mod.getName());
				}
			}
		}
		return longest;
	}

	private int getLongestCatWidth() {
		int longest = 0;
		for (Category cat : Category.values()) {
			if (fr.getStringWidth(cat.name()) > longest) {
				longest = fr.getStringWidth("   " + cat.name());
			}
		}
		return longest;
	}

	public void drawTabGui2() {
		if (!HUD.Tabgui.getStringValue().equalsIgnoreCase("Old")) {

			if (!(trans >= catewidth + 2 + getLongestModWidth() - 7 + 5 + 7)) {
				trans += 5;
			}
			if (screen == 0) {
				trans = catewidth + 2;
			}
			int top = HUD.model.getValue() ? 50 : 17;
			int startx = 1;
			int endX = fr.getStringWidth("S" + "\u00A7fummer b " + Summer.version);
			int otherstartx = endX + 1;
			int backgroundColor = 0x908B8C82;
			int toggledColor = -1;
			int normalColor = 0x908B8C82;
			int startY = (this.selectedOption * 12 + this.otherstart);
			int endY = (startY + 14);
			this.start = (this.selectedTab * 12 + top);
			this.start1 = (this.start + 14);
			this.otherstart = (this.selectedMod * 12 + this.start);
			this.otherstart1 = (this.otherstart + 14);
			int count = 0;

			GLUtil.drawBorderRect(startx, top, endX, top + 3 + this.category.size() * 10 + 9, 1, backgroundColor, 1.0F);
			GLUtil.drawRect(startx, top + 1, startx + 2, top + 3 + this.category.size() * 10 + 8,
					Summer.getColor().getRGB());
			GLUtil.drawBorderRect(startx, this.start, endX, this.start1, 203, Summer.getColor().getRGB(), 1.0F);
			// drawTopString2();
			fr.func_175063_a("FPS: \u00A7f" + Minecraft.debugFPS, 3, top + 3 + this.category.size() * 10 + 14,
					Summer.getColor().getRGB());
			int categoryCount = 0;
			for (String s : this.category) {
				if (category.get(this.selectedTab).toLowerCase().equals(s.toLowerCase())) {
					fr.func_175063_a(s, 10, top + 3 + categoryCount * 12, toggledColor);
				} else {
					fr.func_175063_a(s, 8, top + 3 + categoryCount * 12, toggledColor);
				}
				categoryCount++;
			}
			if (screen == 1 || screen == 2) {
				GLUtil.drawBorderRect(otherstartx, this.start, trans, getModsForCategory().size() * 12 + 2 + this.start,
						1, backgroundColor, 1.0F);
				GLUtil.drawBorderRect(otherstartx, this.otherstart, trans, this.otherstart1, 203,
						Summer.getColor().getRGB(), 1.0F);

				if ((trans >= catewidth + 2 + getLongestModWidth() - 7 + 5 + 7)) {

					int modCount = 0;
					for (Module mod : getModsForCategory()) {
						String name = mod.getName();
						if (Summer.valueManager.getModValues(mod).size() > 0) {
							if (mod == getSelectedMod()) {
								fr.func_175063_a(name, otherstartx + 6, this.start + 3 + modCount * 12,
										mod.getState() ? toggledColor : normalColor);
							} else {
								fr.func_175063_a(name, otherstartx + 2, this.start + 3 + modCount * 12,
										mod.getState() ? toggledColor : normalColor);
							}

							fr.func_175063_a(">", otherstartx + getLongestModWidth() - fr.getStringWidth(">"),
									this.start + 3 + modCount * 12, -1);
						} else {
							if (mod == getSelectedMod()) {
								fr.func_175063_a(name, otherstartx + 5, this.start + 3 + modCount * 12,
										mod.getState() ? toggledColor : normalColor);
							} else {
								fr.func_175063_a(name, otherstartx + 2, this.start + 3 + modCount * 12,
										mod.getState() ? toggledColor : normalColor);
							}

						}
						modCount++;
					}
				}
			}
			if (screen == 2) {

				int x = otherstartx + getLongestModWidth() + 4 + 5;
				Module m = getSelectedMod();

				GLUtil.drawBorderRect(x - 1, this.otherstart, (x - 1) + getLongestOptionWidth() + 12,
						Summer.valueManager.getModValues(getSelectedMod()).size() * 12 + 2 + this.otherstart, 1,
						backgroundColor, 1.0F);
				GLUtil.drawBorderRect(x - 1, startY, (x - 1) + getLongestOptionWidth() + 12, endY, 203,
						Summer.getColor().getRGB(), 1.0F);
				int modCount = 0;
				for (Value s : Summer.valueManager.getModValues(m)) {
					if (s instanceof ClampedValue) {
						ClampedValue cv = (ClampedValue) s;
						int height = fr.FONT_HEIGHT - 3;
						double percentBar = ((double) cv.getValue() - (double) cv.getMin())
								/ ((double) cv.getMax() - (double) cv.getMin());

						if (s == getCurrentOption()) {
							float scale = 0.7F;
							GL11.glScalef(scale, scale, scale);
							fr.func_175063_a(cv.getName() + ": " + (Math.round((double) cv.getValue() * 100D) / 100D),
									(x + 2) / scale, (this.otherstart + 3 + modCount * 12) / scale,
									editMode && cv == getCurrentOption() ? -1 : normalColor);
							GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
							Gui.drawRect(x, this.otherstart + 3 + modCount * 12 + height + 1,
									x + getLongestOptionWidth() + 10,
									this.otherstart + 3 + modCount * 12 + 5 + height - 1, 0xff101010);
							Gui.drawRect(x, this.otherstart + 3 + modCount * 12 + height + 1,
									(int) (x + (percentBar * getLongestOptionWidth() + 10)),
									this.otherstart + 3 + modCount * 12 + 5 + height - 1, -1);
						} else {
							float scale = 0.7F;
							GL11.glScalef(scale, scale, scale);
							fr.func_175063_a(cv.getName() + ": " + (Math.round((double) cv.getValue() * 100D) / 100D),
									(x + 2) / scale, (this.otherstart + 3 + modCount * 12) / scale,
									editMode && cv == getCurrentOption() ? -1 : normalColor);
							GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
							Gui.drawRect(x + 2, this.otherstart + 3 + modCount * 12 + height + 1,
									x + getLongestOptionWidth() + 8,
									this.otherstart + 3 + modCount * 12 + 5 + height - 1, 0xff101010);
							Gui.drawRect(x + 2, this.otherstart + 3 + modCount * 12 + height + 1,
									(int) (x + (percentBar * getLongestOptionWidth() + 8)),
									this.otherstart + 3 + modCount * 12 + 5 + height - 1, -1);
						}

					} else if (s instanceof ModeValue) {
						ModeValue mv = (ModeValue) s;
						if (s == getCurrentOption()) {
							fr.func_175063_a(mv.getName() + ": " + mv.getStringValue(), x + 5,
									this.otherstart + 3 + modCount * 12,
									editMode && s == getCurrentOption() ? -1 : normalColor);
						} else {
							fr.func_175063_a(s.getName() + ": " + mv.getStringValue(), x + 2,
									this.otherstart + 3 + modCount * 12,
									editMode && s == getCurrentOption() ? -1 : normalColor);
						}

					} else if (s instanceof Value) {
						if (s == getCurrentOption()) {
							fr.func_175063_a(s.getName(), x + 5, this.otherstart + 3 + modCount * 12,
									(Boolean) s.getValue() ? -1 : normalColor);
						} else {
							fr.func_175063_a(s.getName(), x + 2, this.otherstart + 3 + modCount * 12,
									(Boolean) s.getValue() ? -1 : normalColor);
						}

					}
					modCount++;
				}

			}

			if (listening) {
				insert();

			}
		}

	}

	public void drawTopString2() {
		float scale = 1.45F;
		GL11.glScalef(scale, scale, scale);
		int sliderColor = -65536;
		GL11.glPushMatrix();
		if (timer.hasReached4(2500)) {
			fr.func_175063_a("S" + "\u00A7fummer", 2, 2, Summer.getColor().getRGB());
			if (timer.hasReached4(5000)) {
				timer.reset2();
			}
		} else if (timer.hasReached4(2500)) {
			fr.func_175063_a("S" + "\u00A7uummer", 2, 2, Summer.getColor().getRGB());
		} else if (timer.hasReached4(2100)) {
			fr.func_175063_a("S" + "\u00A7uumme", 2, 2, Summer.getColor().getRGB());
		} else if (timer.hasReached4(1700)) {
			fr.func_175063_a("S" + "\u00A7uumm", 2, 2, Summer.getColor().getRGB());
		} else if (timer.hasReached4(1300)) {
			fr.func_175063_a("S" + "\u00A7uum", 2, 2, Summer.getColor().getRGB());
		} else if (timer.hasReached4(900)) {
			fr.func_175063_a("S" + "\u00A7uu", 2, 2, Summer.getColor().getRGB());
		} else if (timer.hasReached4(500)) {
			fr.func_175063_a("S", 2, 2, Summer.getColor().getRGB());
		}
//		fr.func_175063_a("S" + "\u00A7fummer", 2, 2, Summer.getColor().getRGB());
		GL11.glPopMatrix();
		GL11.glScalef(1.0F / scale, 1.0F / scale, 1.0F / scale);
	}
}
