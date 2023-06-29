package ru.wendoxd.ui.altmanager.altManager.text;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;
import ru.wendoxd.utils.fonts.Fonts;
import ru.wendoxd.utils.visual.RenderUtils;

public class GuiPasswordField extends Gui {

	private final int id;
	private final FontRenderer fontRendererInstance;
	private final int width;
	private final int height;
	public int xPosition;
	public int yPosition;
	private String text = "";
	private int maxStringLength = 32;
	private boolean isFocused;
	private boolean isEnabled = true;
	private int lineScrollOffset;
	private int cursorPosition;
	private int selectionEnd;
	private int enabledColor = 14737632;
	private long inputTicker;

	public GuiPasswordField(int componentId, FontRenderer fontrendererObj, int x, int y, int par5Width,
			int par6Height) {
		this.id = componentId;
		this.fontRendererInstance = fontrendererObj;
		this.xPosition = x;
		this.yPosition = y;
		this.width = par5Width;
		this.height = par6Height;
	}

	public void updateCursorCounter() {
	}

	public String getText() {
		return this.text;
	}

	public void setText(String textIn) {
		if (textIn.length() > this.maxStringLength) {
			this.text = textIn.substring(0, this.maxStringLength);
		} else {
			this.text = textIn;
		}

		this.setCursorPositionEnd();
	}

	public String getSelectedText() {
		int i = Math.min(this.cursorPosition, this.selectionEnd);
		int j = Math.max(this.cursorPosition, this.selectionEnd);
		return this.text.substring(i, j);
	}

	public void writeText(String textToWrite) {
		String s = "";
		String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
		int i = Math.min(this.cursorPosition, this.selectionEnd);
		int j = Math.max(this.cursorPosition, this.selectionEnd);
		int k = this.maxStringLength - this.text.length() - (i - j);

		if (!this.text.isEmpty()) {
			s = s + this.text.substring(0, i);
		}

		int l;

		if (k < s1.length()) {
			s = s + s1.substring(0, k);
			l = k;
		} else {
			s = s + s1;
			l = s1.length();
		}

		if (!this.text.isEmpty() && j < this.text.length()) {
			s = s + this.text.substring(j);
		}

		this.text = s;
		this.moveCursorBy(i - this.selectionEnd + l);
	}

	public void deleteWords(int num) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				this.deleteFromCursor(this.getNthWordFromCursor(num) - this.cursorPosition);
			}
		}
	}

	public void deleteFromCursor(int num) {
		if (!this.text.isEmpty()) {
			if (this.selectionEnd != this.cursorPosition) {
				this.writeText("");
			} else {
				boolean flag = num < 0;
				int i = flag ? this.cursorPosition + num : this.cursorPosition;
				int j = flag ? this.cursorPosition : this.cursorPosition + num;
				String s = "";

				if (i >= 0) {
					s = this.text.substring(0, i);
				}

				if (j < this.text.length()) {
					s = s + this.text.substring(j);
				}

				this.text = s;

				if (flag) {
					this.moveCursorBy(num);
				}
			}
		}
	}

	public int getId() {
		return this.id;
	}

	public int getNthWordFromCursor(int numWords) {
		return this.getNthWordFromPos(numWords, this.getCursorPosition());
	}

	public int getNthWordFromPos(int n, int pos) {
		return this.getNthWordFromPosWS(n, pos, true);
	}

	public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
		int i = pos;
		boolean flag = n < 0;
		int j = Math.abs(n);

		for (int k = 0; k < j; ++k) {
			if (!flag) {
				int l = this.text.length();
				i = this.text.indexOf(32, i);

				if (i == -1) {
					i = l;
				} else {
					while (skipWs && i < l && this.text.charAt(i) == ' ') {
						++i;
					}
				}
			} else {
				while (skipWs && i > 0 && this.text.charAt(i - 1) == ' ') {
					--i;
				}

				while (i > 0 && this.text.charAt(i - 1) != ' ') {
					--i;
				}
			}
		}

		return i;
	}

	public void moveCursorBy(int num) {
		this.setCursorPosition(this.selectionEnd + num);
	}

	public void setCursorPositionEnd() {
		this.setCursorPosition(this.text.length());
	}

	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		if (!this.isFocused) {
			return false;
		} else if (GuiScreen.isKeyComboCtrlA(keyCode)) {
			this.setCursorPositionEnd();
			this.setSelectionPos(0);
			return true;
		} else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
			GuiScreen.setClipboardString(this.getSelectedText());
			return true;
		} else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
			if (this.isEnabled) {
				this.writeText(GuiScreen.getClipboardString());
			}

			return true;
		} else {
			if (keyCode == 14) {
				if (GuiScreen.isCtrlKeyDown()) {
					if (this.isEnabled) {
						this.deleteWords(-1);
					}
				} else if (this.isEnabled) {
					this.deleteFromCursor(-1);
				}

				return true;
			}
			if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
				if (this.isEnabled) {
					this.writeText(Character.toString(typedChar));
				}

				return true;
			} else {
				return false;
			}
		}
	}

	public boolean mouseClicked(int mouseX, int mouseY) {
		boolean flag = mouseX >= this.xPosition && mouseX < this.xPosition + this.width && mouseY >= this.yPosition
				&& mouseY < this.yPosition + this.height;

		this.setFocused(flag);
		return false;
	}

	public void drawTextBox() {
		RenderUtils.drawRoundedRect(this.xPosition, this.yPosition, this.xPosition + this.width,
				this.yPosition + this.height, RenderUtils.rgba(40, 40, 40, 150), 4);

		int i = this.enabledColor;
		int j = this.cursorPosition - this.lineScrollOffset;
		String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset),
				this.getWidth());
		boolean flag = j >= 0 && j <= s.length();
		int l = this.xPosition + 4;
		int i1 = this.yPosition + (this.height - 8) / 2;

		int size = (int) Fonts.MNTSB_16.drawStringWithShadow(repeat('*', (flag ? s.substring(0, j) : s).length()),
				(float) l + 1, (float) i1 + 3.5F, i);

		if (isFocused) {
			Fonts.MNTSB_14.drawStringWithShadow((inputTicker % 100 < 50 ? "_" : ""), size + 1, (float) i1 + 2, i);
			inputTicker++;
		}
	}

	public String repeat(char c, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	public int getMaxStringLength() {
		return this.maxStringLength;
	}

	public void setMaxStringLength(int length) {
		this.maxStringLength = length;

		if (this.text.length() > length) {
			this.text = this.text.substring(0, length);
		}
	}

	public int getCursorPosition() {
		return this.cursorPosition;
	}

	public void setCursorPosition(int pos) {
		this.cursorPosition = pos;
		int i = this.text.length();
		this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
		this.setSelectionPos(this.cursorPosition);
	}

	public void setTextColor(int color) {
		this.enabledColor = color;
	}

	public boolean isFocused() {
		return this.isFocused;
	}

	public void setFocused(boolean isFocusedIn) {
		if (isFocusedIn && !this.isFocused) {
		}

		this.isFocused = isFocusedIn;

		if (Minecraft.getMinecraft().currentScreen != null) {
			Minecraft.getMinecraft().currentScreen.func_193975_a(isFocusedIn);
		}
	}

	public void setEnabled(boolean enabled) {
		this.isEnabled = enabled;
	}

	public int getSelectionEnd() {
		return this.selectionEnd;
	}

	public int getWidth() {
		return this.width - 8;
	}

	public void setSelectionPos(int position) {
		int i = this.text.length();

		if (position > i) {
			position = i;
		}

		if (position < 0) {
			position = 0;
		}

		this.selectionEnd = position;

		if (this.fontRendererInstance != null) {
			if (this.lineScrollOffset > i) {
				this.lineScrollOffset = i;
			}

			int j = this.getWidth();
			String s = this.fontRendererInstance.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
			int k = s.length() + this.lineScrollOffset;

			if (position == this.lineScrollOffset) {
				this.lineScrollOffset -= this.fontRendererInstance.trimStringToWidth(this.text, j, true).length();
			}

			if (position > k) {
				this.lineScrollOffset += position - k;
			} else if (position <= this.lineScrollOffset) {
				this.lineScrollOffset -= this.lineScrollOffset - position;
			}

			this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
		}
	}
}