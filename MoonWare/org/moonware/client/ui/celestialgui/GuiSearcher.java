/*
 * Decompiled with CFR 0.150.
 */
package org.moonware.client.ui.celestialgui;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.OS;
import net.minecraft.util.math.MathHelper;
import org.moonware.client.utils.MWFont;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.misc.StreamerMode;
import org.moonware.client.helpers.render.rect.RectHelper;

import java.awt.*;

public class GuiSearcher
extends Gui {
    private final int id;
    private final Font fontInstance;
    private final int width;
    private final int height;
    public int xPosition;
    public int yPosition;
    public int maxStringLength = 32;
    public boolean isFocused;
    private String text = "";
    private int cursorCounter;
    private boolean enableBackgroundDrawing = true;
    private boolean canLoseFocus = true;
    private boolean isEnabled = true;
    private int lineScrollOffset;
    private int cursorPosition;
    private int selectionEnd;
    private int enabledColor = 0xE0E0E0;
    private int disabledColor = 0x707070;
    private boolean visible = true;
    private GuiPageButtonList.GuiResponder guiResponder;
    private Predicate<String> validator = Predicates.alwaysTrue();

    public GuiSearcher(int componentId, Font fontrendererObj, int x, int y, int par5Width, int par6Height) {
        id = componentId;
        fontInstance = fontrendererObj;
        xPosition = x;
        yPosition = y;
        width = par5Width;
        height = par6Height;
    }

    public void setGuiResponder(GuiPageButtonList.GuiResponder guiResponderIn) {
        guiResponder = guiResponderIn;
    }

    public void updateCursorCounter() {
        ++cursorCounter;
    }

    public String getText() {
        return text;
    }

    public void setText(String textIn) {
        if (validator.apply(textIn)) {
            text = textIn.length() > maxStringLength ? textIn.substring(0, maxStringLength) : textIn;
            setCursorPositionEnd();
        }
    }

    public String getSelectedText() {
        int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
        int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
        return text.substring(i, j);
    }

    public void setValidator(Predicate<String> theValidator) {
        validator = theValidator;
    }

    public void writeText(String textToWrite) {
        int l;
        String s = "";
        String s1 = ChatAllowedCharacters.filterAllowedCharacters(textToWrite);
        int i = cursorPosition < selectionEnd ? cursorPosition : selectionEnd;
        int j = cursorPosition < selectionEnd ? selectionEnd : cursorPosition;
        int k = maxStringLength - text.length() - (i - j);
        if (!text.isEmpty()) {
            s = s + text.substring(0, i);
        }
        if (k < s1.length()) {
            s = s + s1.substring(0, k);
            l = k;
        } else {
            s = s + s1;
            l = s1.length();
        }
        if (!text.isEmpty() && j < text.length()) {
            s = s + text.substring(j);
        }
        if (validator.apply(s)) {
            text = s;
            moveCursorBy(i - selectionEnd + l);
            func_190516_a(id, text);
        }
    }

    public void func_190516_a(int p_190516_1_, String p_190516_2_) {
        if (guiResponder != null) {
            guiResponder.setEntryValue(p_190516_1_, p_190516_2_);
        }
    }

    public void deleteWords(int num) {
        if (!text.isEmpty()) {
            if (selectionEnd != cursorPosition) {
                writeText("");
            } else {
                deleteFromCursor(getNthWordFromCursor(num) - cursorPosition);
            }
        }
    }

    public void deleteFromCursor(int num) {
        if (!text.isEmpty()) {
            if (selectionEnd != cursorPosition) {
                writeText("");
            } else {
                boolean flag = num < 0;
                int i = flag ? cursorPosition + num : cursorPosition;
                int j = flag ? cursorPosition : cursorPosition + num;
                String s = "";
                if (i >= 0) {
                    s = text.substring(0, i);
                }
                if (j < text.length()) {
                    s = s + text.substring(j);
                }
                if (validator.apply(s)) {
                    text = s;
                    if (flag) {
                        moveCursorBy(num);
                    }
                    func_190516_a(id, text);
                }
            }
        }
    }

    public int getId() {
        return id;
    }

    public int getNthWordFromCursor(int numWords) {
        return getNthWordFromPos(numWords, getCursorPosition());
    }

    public int getNthWordFromPos(int n, int pos) {
        return getNthWordFromPosWS(n, pos, true);
    }

    public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
        int i = pos;
        boolean flag = n < 0;
        int j = Math.abs(n);
        for (int k = 0; k < j; ++k) {
            if (!flag) {
                int l = text.length();
                if ((i = text.indexOf(32, i)) == -1) {
                    i = l;
                    continue;
                }
                while (skipWs && i < l && text.charAt(i) == ' ') {
                    ++i;
                }
                continue;
            }
            while (skipWs && i > 0 && text.charAt(i - 1) == ' ') {
                --i;
            }
            while (i > 0 && text.charAt(i - 1) != ' ') {
                --i;
            }
        }
        return i;
    }

    public void moveCursorBy(int num) {
        setCursorPosition(selectionEnd + num);
    }

    public void setCursorPositionZero() {
        setCursorPosition(0);
    }

    public void setCursorPositionEnd() {
        setCursorPosition(text.length());
    }

    public boolean textboxKeyTyped(char typedChar, int keyCode) {
        if (!isFocused) {
            return false;
        }
        if (Screen.isSelectAll(keyCode)) {
            setCursorPositionEnd();
            setSelectionPos(0);
            return true;
        }
        if (Screen.isCopy(keyCode)) {
            OS.setClipboard(getSelectedText());
            return true;
        }
        if (Screen.isPaste(keyCode)) {
            if (isEnabled) {
                writeText(OS.getClipboard());
            }
            return true;
        }
        if (Screen.isCut(keyCode)) {
            OS.setClipboard(getSelectedText());
            if (isEnabled) {
                writeText("");
            }
            return true;
        }
        switch (keyCode) {
            case 14: {
                if (Screen.hasControlDown()) {
                    if (isEnabled) {
                        deleteWords(-1);
                    }
                } else if (isEnabled) {
                    deleteFromCursor(-1);
                }
                return true;
            }
            case 199: {
                if (Screen.hasShiftDown()) {
                    setSelectionPos(0);
                } else {
                    setCursorPositionZero();
                }
                return true;
            }
            case 203: {
                if (Screen.hasShiftDown()) {
                    if (Screen.hasControlDown()) {
                        setSelectionPos(getNthWordFromPos(-1, getSelectionEnd()));
                    } else {
                        setSelectionPos(getSelectionEnd() - 1);
                    }
                } else if (Screen.hasControlDown()) {
                    setCursorPosition(getNthWordFromCursor(-1));
                } else {
                    moveCursorBy(-1);
                }
                return true;
            }
            case 205: {
                if (Screen.hasShiftDown()) {
                    if (Screen.hasControlDown()) {
                        setSelectionPos(getNthWordFromPos(1, getSelectionEnd()));
                    } else {
                        setSelectionPos(getSelectionEnd() + 1);
                    }
                } else if (Screen.hasControlDown()) {
                    setCursorPosition(getNthWordFromCursor(1));
                } else {
                    moveCursorBy(1);
                }
                return true;
            }
            case 207: {
                if (Screen.hasShiftDown()) {
                    setSelectionPos(text.length());
                } else {
                    setCursorPositionEnd();
                }
                return true;
            }
            case 211: {
                if (Screen.hasControlDown()) {
                    if (isEnabled) {
                        deleteWords(1);
                    }
                } else if (isEnabled) {
                    deleteFromCursor(1);
                }
                return true;
            }
        }
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
            if (isEnabled) {
                writeText(Character.toString(typedChar));
            }
            return true;
        }
        return false;
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean flag;
        boolean bl = flag = mouseX >= xPosition && mouseX < xPosition + width && mouseY >= yPosition && mouseY < yPosition + height;
        if (canLoseFocus) {
            setFocused(flag);
        }
        if (isFocused && flag && mouseButton == 0) {
            int i = mouseX - xPosition;
            if (enableBackgroundDrawing) {
                i -= 4;
            }
            String s = fontInstance.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
            setCursorPosition(fontInstance.trimStringToWidth(s, i).length() + lineScrollOffset);
            return true;
        }
        return false;
    }

    public void drawTextBox() {
        if (getVisible()) {
            if (getEnableBackgroundDrawing()) {
                RectHelper.drawRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(30, 30, 30, 185).getRGB());
                RectHelper.drawGradientRect(xPosition, yPosition, xPosition + width, yPosition + height, new Color(30, 30, 30, 190).darker().getRGB(), new Color(30, 30, 30, 190).brighter().getRGB());
            }
            int i = -1;
            int j = cursorPosition - lineScrollOffset;
            int k = selectionEnd - lineScrollOffset;
            String s = fontInstance.trimStringToWidth(text.substring(lineScrollOffset), getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = isFocused && cursorCounter / 6 % 2 == 0 && flag;
            int l = enableBackgroundDrawing ? xPosition + 4 : xPosition;
            int i1 = enableBackgroundDrawing ? yPosition + (height - 8) / 2 : yPosition;
            int j1 = l;
            if (k > s.length()) {
                k = s.length();
            }
            if (!s.isEmpty()) {
                String s1;
                String string = s1 = flag ? s.substring(0, j) : s;
                if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.warpSpoof.getCurrentValue() && s1.startsWith("/warp")) {
                    s1 = "/warp ";
                }
                if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.loginSpoof.getCurrentValue() && s1.startsWith("/l")) {
                    s1 = "/l ";
                }
                if (MoonWare.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.loginSpoof.getCurrentValue() && s1.startsWith("/reg")) {
                    s1 = "/reg ";
                }
                j1 = (int) MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow(s1, l, i1, i);
            }
            boolean flag2 = cursorPosition < text.length() || text.length() >= getMaxStringLength();
            int k1 = j1;
            if (!flag) {
                k1 = j > 0 ? l + width : l;
            } else if (flag2) {
                k1 = j1 - 1;
                --j1;
            }
            if (!s.isEmpty() && flag && j < s.length()) {
                j1 = (int) MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow(s.substring(j), j1, i1, i);
            }
            if (flag1) {
                if (flag2) {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + MWFont.SF_UI_DISPLAY_REGULAR.get(18).getHeight(), -3092272);
                } else {
                    MWFont.SF_UI_DISPLAY_REGULAR.get(18).drawShadow("_", k1, i1, i);
                }
            }
            if (k != j) {
                int l1 = l + MWFont.SF_UI_DISPLAY_REGULAR.get(18).getWidth(s.substring(0, k));
                drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + MWFont.SF_UI_DISPLAY_REGULAR.get(18).getHeight());
            }
        }
    }

    private void drawCursorVertical(int startX, int startY, int endX, int endY) {
        if (startX < endX) {
            int i = startX;
            startX = endX;
            endX = i;
        }
        if (startY < endY) {
            int j = startY;
            startY = endY;
            endY = j;
        }
        if (endX > xPosition + width) {
            endX = xPosition + width;
        }
        if (startX > xPosition + width) {
            startX = xPosition + width;
        }
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(0.0f, 0.0f, 255.0f, 255.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(startX, endY, 0.0).endVertex();
        bufferbuilder.pos(endX, endY, 0.0).endVertex();
        bufferbuilder.pos(endX, startY, 0.0).endVertex();
        bufferbuilder.pos(startX, startY, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public int getMaxStringLength() {
        return maxStringLength;
    }

    public void setMaxStringLength(int length) {
        maxStringLength = length;
        if (text.length() > length) {
            text = text.substring(0, length);
        }
    }

    public int getCursorPosition() {
        return cursorPosition;
    }

    public void setCursorPosition(int pos) {
        cursorPosition = pos;
        int i = text.length();
        cursorPosition = MathHelper.clamp(cursorPosition, 0, i);
        setSelectionPos(cursorPosition);
    }

    public boolean getEnableBackgroundDrawing() {
        return enableBackgroundDrawing;
    }

    public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn) {
        enableBackgroundDrawing = enableBackgroundDrawingIn;
    }

    public void setTextColor(int color) {
        enabledColor = color;
    }

    public void setDisabledTextColour(int color) {
        disabledColor = color;
    }

    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean isFocusedIn) {
        if (isFocusedIn && !isFocused) {
            cursorCounter = 0;
        }
        isFocused = isFocusedIn;
        if (Minecraft.screen != null) {
            Minecraft.screen.func_193975_a(isFocusedIn);
        }
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getSelectionEnd() {
        return selectionEnd;
    }

    public int getWidth() {
        return getEnableBackgroundDrawing() ? width - 8 : width;
    }

    public void setSelectionPos(int position) {
        int i = text.length();
        if (position > i) {
            position = i;
        }
        if (position < 0) {
            position = 0;
        }
        selectionEnd = position;
        if (fontInstance != null) {
            if (lineScrollOffset > i) {
                lineScrollOffset = i;
            }
            int j = getWidth();
            String s = fontInstance.trimStringToWidth(text.substring(lineScrollOffset), j);
            int k = s.length() + lineScrollOffset;
            if (position == lineScrollOffset) {
                lineScrollOffset -= fontInstance.trimStringToWidth(text, j, true).length();
            }
            if (position > k) {
                lineScrollOffset += position - k;
            } else if (position <= lineScrollOffset) {
                lineScrollOffset -= lineScrollOffset - position;
            }
            lineScrollOffset = MathHelper.clamp(lineScrollOffset, 0, i);
        }
    }

    public void setCanLoseFocus(boolean canLoseFocusIn) {
        canLoseFocus = canLoseFocusIn;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean isVisible) {
        visible = isVisible;
    }
}

