package net.minecraft.client.gui.hud;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.moonware.client.utils.FontStorage;
import org.moonware.client.utils.MWFont;
import org.moonware.client.MoonWare;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.packet.EventReceiveMessage;
import org.moonware.client.feature.impl.misc.CustomChat;
import org.moonware.client.helpers.render.AnimationHelper;
import org.moonware.client.utils.CustomFont;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class ChatHud extends Gui {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft minecraft;
    private final List<String> sentMessages = Lists.newArrayList();
    private final List<ChatLine> chatLines = Lists.newArrayList();
    private final List<ChatLine> drawnChatLines = Lists.newArrayList();
    private int scrollPos;
    private boolean isScrolled;
    private String lastMessage;
    private int spamCounter;
    private int line;
    private final HashMap<String, String> strCache = new HashMap();

    private CustomFont getFontRender() {
        CustomFont font = MWFont.SF_UI_DISPLAY_REGULAR.get(18);
        String mode = CustomChat.fontType.getOptions();
        if (mode.equalsIgnoreCase("Comfortaa")) {
            font = FontStorage.comfortaa19;
        } else if (mode.equalsIgnoreCase("SF UI")) {
            font = MWFont.SF_UI_DISPLAY_REGULAR.get(18);
        } else if (mode.equalsIgnoreCase("Verdana")) {
            font = FontStorage.verdanaFontRender;
        } else if (mode.equalsIgnoreCase("RobotoRegular")) {
            font = FontStorage.robotoRegularFontRender;
        } else if (mode.equalsIgnoreCase("Lato")) {
            font = FontStorage.latoFontRender;
        } else if (mode.equalsIgnoreCase("Open Sans")) {
            font = FontStorage.openSansFontRender;
        } else if (mode.equalsIgnoreCase("Ubuntu")) {
            font = FontStorage.ubuntuFontRender;
        } else if (mode.equalsIgnoreCase("LucidaConsole")) {
            font = FontStorage.lucidaConsoleFontRenderer;
        } else if (mode.equalsIgnoreCase("Calibri")) {
            font = FontStorage.calibri;
        } else if (mode.equalsIgnoreCase("Product Sans")) {
            font = FontStorage.productsans;
        } else if (mode.equalsIgnoreCase("RaleWay")) {
            font = FontStorage.raleway;
        } else if (mode.equalsIgnoreCase("Kollektif")) {
            font = FontStorage.kollektif;
        } else if (mode.equalsIgnoreCase("Bebas Book")) {
            font = MWFont.MONTSERRAT_BOLD.get(18);
        }
        return font;
    }
    public void drawChat(int updateCounter) {
        GlStateManager.pushMatrix();
        if (Minecraft.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = getLineCount();
            int j = drawnChatLines.size();
            float f = Minecraft.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (j > 0) {
                boolean flag = getChatOpen();
                float f1 = getChatScale();
                int k = MathHelper.ceil((float) getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 8.0f, 0.0f);
                GlStateManager.scale(f1, f1, 1.0f);
                int l = 0;
                for (int i1 = 0; i1 + scrollPos < drawnChatLines.size() && i1 < i; ++i1) {
                    int j1;
                    ChatLine chatline = drawnChatLines.get(i1 + scrollPos);
                    if (chatline == null || (j1 = updateCounter - chatline.getUpdatedCounter()) >= 200 && !flag) continue;
                    double d0 = (double)j1 / 200.0;
                    d0 = 1.0 - d0;
                    d0 *= 10.0;
                    d0 = MathHelper.clamp(d0, 0.0, 1.0);
                    d0 *= d0;
                    int l1 = (int)(255.0 * d0);
                    if (flag) {
                        l1 = 255;
                    }
                    l1 = (int)((float)l1 * f);
                    ++l;
                    if (l1 <= 3) continue;
                    int j2 = -i1 * 9;
                    if (!MoonWare.featureManager.getFeatureByClass(CustomChat.class).getState() || !CustomChat.noBackground.getCurrentValue()) {
                        drawRect(-2, j2 - 9, k + 4, j2, l1 / 2 << 24);
                    }
                    String s = chatline.getChatComponent().asFormattedString();
                    GlStateManager.enableBlend();
                    if (!MoonWare.featureManager.getFeatureByClass(CustomChat.class).getState() || !CustomChat.customFont.getCurrentValue()) {
                        chatline.setPosY(AnimationHelper.animation2(chatline.getPosY(), (float)j2 - 8.5f, (float)((double)0.1f * Minecraft.frameTime * (double)0.1f)));
                    } else {
                        chatline.setPosY(AnimationHelper.animation2(chatline.getPosY(), j2 - getFontRender().getHeight(), (float)((double)0.1f * Minecraft.frameTime * (double)0.1f)));
                    }
                    chatline.setPosX(AnimationHelper.animation2(chatline.getPosX(), 0.0f, (float)((double)0.1f * Minecraft.frameTime * (double)0.1f)));
                    if (!MoonWare.featureManager.getFeatureByClass(CustomChat.class).getState() || !CustomChat.customFont.getCurrentValue()) {
                        Minecraft.font.drawStringWithShadow(s, flag ? 0.0f : chatline.getPosX(), flag ? (float)((double)j2 - 9.5) + f1 : chatline.getPosY(), 0xFFFFFF + (l1 << 24));
                    } else {
                        getFontRender().drawShadow(s, flag ? 0.0 : (double)chatline.getPosX(), flag ? (double)((float)((double)j2 - 9.5) + f1) : (double)chatline.getPosY(), 0xFFFFFF + (l1 << 24));
                    }
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                if (flag) {
                    int k2 = !MoonWare.featureManager.getFeatureByClass(CustomChat.class).getState() || !CustomChat.customFont.getCurrentValue() ? Minecraft.font.height : getFontRender().getHeight();
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    int l2 = j * k2 + j;
                    int i3 = l * k2 + l;
                    int j3 = scrollPos * i3 / j;
                    int k1 = i3 * i3 / l2;
                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = isScrolled ? 0xCC3333 : 0x3333AA;
                        drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        drawRect(2, -j3, 1, -j3 - k1, 0xCCCCCC + (k3 << 24));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
    }

    public void clearChatMessages(boolean p_146231_1_) {
        drawnChatLines.clear();
        chatLines.clear();
        if (p_146231_1_) {
            sentMessages.clear();
        }
    }

    public void printChatMessage(Component chatComponent) {
        String text = stringFix(chatComponent.asFormattedString());
        if (text.equals(lastMessage)) {
            Minecraft.ingameGUI.getChatGUI().deleteChatLine(line);
            ++spamCounter;
            lastMessage = text;
            chatComponent.append(Formatting.WHITE + " (x" + spamCounter + ")");
        } else {
            spamCounter = 1;
            lastMessage = text;
        }
        ++line;
        if (line > 256) {
            line = 0;
        }
        printChatMessageWithOptionalDeletion(chatComponent, line);
        EventReceiveMessage event = new EventReceiveMessage(chatComponent.asString());
        EventManager.call(event);
    }

    private String stringFix(String str) {
        if (strCache.containsKey(str)) {
            return strCache.get(str);
        }
        str = str.replaceAll("\uf8ff", "");
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (c > '\uff01' && c < '\uff60') {
                sb.append(Character.toChars(c - 65248));
                continue;
            }
            sb.append(c);
        }
        String result = sb.toString();
        strCache.put(str, result);
        return result;
    }

    public void printChatMessageWithOptionalDeletion(Component chatComponent, int chatLineId) {
        setChatLine(chatComponent, chatLineId, Minecraft.ingameGUI.getUpdateCounter(), false);
        LOGGER.info("[CHAT] {}", chatComponent.asString().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
    }

    private void setChatLine(Component chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
        if (chatLineId != 0) {
            deleteChatLine(chatLineId);
        }
        int i = MathHelper.floor((float) getChatWidth() / getChatScale());
        List<Component> list = GuiUtilRenderComponents.splitText(chatComponent, i, Minecraft.font, false, false);
        boolean flag = getChatOpen();
        for (Component itextcomponent : list) {
            if (flag && scrollPos > 0) {
                isScrolled = true;
                scroll(1);
            }
            drawnChatLines.add(0, new ChatLine(updateCounter, itextcomponent, chatLineId));
        }
        while (drawnChatLines.size() > 100) {
            drawnChatLines.remove(drawnChatLines.size() - 1);
        }
        if (!displayOnly) {
            chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));
            while (chatLines.size() > 100) {
                chatLines.remove(chatLines.size() - 1);
            }
        }
    }

    public void refreshChat() {
        drawnChatLines.clear();
        resetScroll();
        for (int i = chatLines.size() - 1; i >= 0; --i) {
            ChatLine chatline = chatLines.get(i);
            setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }

    public List<String> getSentMessages() {
        return sentMessages;
    }

    public void addToSentMessages(String message) {
        if (sentMessages.isEmpty() || !sentMessages.get(sentMessages.size() - 1).equals(message)) {
            sentMessages.add(message);
        }
    }

    public void resetScroll() {
        scrollPos = 0;
        isScrolled = false;
    }

    public void scroll(int amount) {
        scrollPos += amount;
        int i = drawnChatLines.size();
        if (scrollPos > i - getLineCount()) {
            scrollPos = i - getLineCount();
        }
        if (scrollPos <= 0) {
            scrollPos = 0;
            isScrolled = false;
        }
    }

    @Nullable
    public Component getChatComponent(int mouseX, int mouseY) {
        if (!getChatOpen()) {
            return null;
        }
        ScaledResolution scaledresolution = new ScaledResolution(minecraft);
        int i = scaledresolution.getScaleFactor();
        float f = getChatScale();
        int j = mouseX / i - 2;
        int k = mouseY / i - 40;
        j = MathHelper.floor((float)j / f);
        k = MathHelper.floor((float)k / f);
        if (j >= 0 && k >= 0) {
            int l = Math.min(getLineCount(), drawnChatLines.size());
            if (j <= MathHelper.floor((float) getChatWidth() / getChatScale()) && k < Minecraft.font.height * l + l) {
                int i1 = k / Minecraft.font.height + scrollPos;
                if (i1 >= 0 && i1 < drawnChatLines.size()) {
                    ChatLine chatline = drawnChatLines.get(i1);
                    int j1 = 0;
                    for (Component itextcomponent : chatline.getChatComponent()) {
                        if (!(itextcomponent instanceof TextComponent) || (j1 += Minecraft.font.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponent)itextcomponent).getText(), false))) <= j) continue;
                        return itextcomponent;
                    }
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public boolean getChatOpen() {
        return Minecraft.screen instanceof ChatScreen;
    }

    public void deleteChatLine(int id) {
        Iterator<ChatLine> iterator = drawnChatLines.iterator();
        while (iterator.hasNext()) {
            ChatLine chatline = iterator.next();
            if (chatline.getChatLineID() != id) continue;
            iterator.remove();
        }
        iterator = chatLines.iterator();
        while (iterator.hasNext()) {
            ChatLine chatline1 = iterator.next();
            if (chatline1.getChatLineID() != id) continue;
            iterator.remove();
            break;
        }
    }

    public int getChatWidth() {
        return calculateChatboxWidth(Minecraft.gameSettings.chatWidth);
    }

    public int getChatHeight() {
        return calculateChatboxHeight(getChatOpen() ? Minecraft.gameSettings.chatHeightFocused : Minecraft.gameSettings.chatHeightUnfocused);
    }

    public float getChatScale() {
        return Minecraft.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float scale) {
        int i = 320;
        int j = 40;
        return MathHelper.floor(scale * 280.0f + 40.0f);
    }

    public static int calculateChatboxHeight(float scale) {
        int i = 180;
        int j = 20;
        return MathHelper.floor(scale * 160.0f + 20.0f);
    }

    public int getLineCount() {
        return getChatHeight() / 9;
    }
}
