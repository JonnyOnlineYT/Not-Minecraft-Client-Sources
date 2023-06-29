package net.minecraft.client.gui.screen;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.OS;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Formatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import optifine.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.moonware.client.helpers.Utils.RoundedUtil;
import org.moonware.client.ui.shader.BackgroundShader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public abstract class Screen extends Gui {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<String> PROTOCOLS = Sets.newHashSet("http", "https");
    protected final Minecraft minecraft = Minecraft.getMinecraft();
    protected final Font font = Minecraft.font;
    protected final RenderItem renderItem = Minecraft.getRenderItem();
    public int width;
    public int height;
    public List<ButtonWidget> widgets = Lists.newArrayList();
    public boolean allowUserInput;
    protected ButtonWidget selectedButton;
    public int eventButton;
    private boolean field_193977_u;
    private final BackgroundShader backgroundShader;
    private final long initTime = System.currentTimeMillis();
    public Screen() {
        try {
            backgroundShader = new BackgroundShader("/assets/noise.fsh");
        } catch (IOException var2) {
            throw new IllegalStateException("Failed to load backgound shader", var2);
        }
    }

    public void init() {}

    public void draw(int mouseX, int mouseY, float partialTick) {
        for (ButtonWidget button : widgets) {
            button.draw(minecraft, mouseX, mouseY, partialTick);
        }
    }

    public void update() {}

    public boolean pauses() {
        return true;
    }

    public void mousePressed(int mouseX, int mouseY, int button) {
        if (button == 0) {
            for (int i = 0; i < widgets.size(); ++i) {
                ButtonWidget guibutton = widgets.get(i);
                if (guibutton.mousePressed(minecraft, mouseX, mouseY)) {
                    selectedButton = guibutton;
                    guibutton.playPressSound(Minecraft.getSoundHandler());
                    actionPerformed(guibutton);
                }
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (selectedButton != null && button == 0) {
            selectedButton.mouseReleased(mouseX, mouseY);
            selectedButton = null;
        }
    }

    public void mouseScrolled(int mouseX, int mouseY, int scroll) {
        
    }

    public void keyPressed(int key, char c) {
        if (key == Keyboard.KEY_ESCAPE && escapeCloses()) {
            close();
        }
    }

    public boolean escapeCloses() {
        return true;
    }

    public void close() {
        Minecraft.openScreen(null);
    }

    public void onClosed() {}

    @Deprecated
    public void actionPerformed(ButtonWidget button) {}

    @Deprecated
    protected <T extends ButtonWidget> T addButton(T button) {
        widgets.add(button);
        return button;
    }

    public void func_193975_a(boolean p_193975_1_) {
        field_193977_u = p_193975_1_;
    }

    public boolean func_193976_p() {
        return field_193977_u;
    }

    public void drawTooltip(ItemStack stack, int x, int y) {
        drawTooltip(getTooltip(stack), x, y);
    }

    public final List<String> getTooltip(ItemStack stack) {
        List<String> list = stack.getTooltip(Minecraft.player, Minecraft.gameSettings.advancedItemTooltips ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL);
        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + list.get(i));
            } else {
                list.set(i, Formatting.GRAY + list.get(i));
            }
        }
        return list;
    }

    public final void drawTooltip(Component component, int x, int y) {
        if (component == null) return;
        HoverEvent event = component.getStyle().getHoverEvent();
        if (event == null) return;
        if (event.getAction() == HoverEvent.Action.SHOW_ITEM) {
            ItemStack stack = ItemStack.EMPTY;
            try {
                NBTBase nbt = JsonToNBT.getTagFromJson(event.getValue().asString());
                if (nbt instanceof NBTTagCompound) {
                    stack = new ItemStack((NBTTagCompound) nbt);
                }
            } catch (Exception ignored) {}
            if (stack.isEmpty()) {
                drawTooltip(Formatting.RED + "Invalid Item!", x, y);
            } else {
                drawTooltip(stack, x, y);
            }
        } else if (event.getAction() == HoverEvent.Action.SHOW_ENTITY) {
            if (Minecraft.gameSettings.advancedItemTooltips) {
                try {
                    NBTTagCompound nbt = JsonToNBT.getTagFromJson(event.getValue().asString());
                    List<String> list = Lists.newArrayList();
                    list.add(nbt.getString("name"));
                    if (nbt.hasKey("type", 8)) {
                        String s = nbt.getString("type");
                        list.add("Type: " + s);
                    }
                    list.add(nbt.getString("id"));
                    drawTooltip(list, x, y);
                } catch (Exception ignored) {
                    drawTooltip(Formatting.RED + "Invalid Entity!", x, y);
                }
            }
        } else if (event.getAction() == HoverEvent.Action.SHOW_TEXT) {
            drawTooltip(Minecraft.font.split(event.getValue().asFormattedString(), Math.max(width / 2, 200)), x, y);
        }
        GlStateManager.disableLighting();
    }

    public final void drawTooltip(String text, int x, int y) {
        drawTooltip(Collections.singletonList(text), x, y);
    }

    public final void drawTooltip(List<String> lines, int x, int y) {
        if (lines.isEmpty()) return;
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        int i = 0;

        for (String s : lines) {
            int j = font.getStringWidth(s);

            if (j > i) {
                i = j;
            }
        }

        int l1 = x + 12;
        int i2 = y - 12;
        int k = 8;

        if (lines.size() > 1) {
            k += 2 + (lines.size() - 1) * 10;
        }

        if (l1 + i > width) {
            l1 -= 28 + i;
        }

        if (i2 + k + 6 > height) {
            i2 = height - k - 6;
        }

        zLevel = 300.0F;
        renderItem.zLevel = 300.0F;
        int l = -267386864;
        drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
        drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
        drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
        drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
        drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
        int i1 = 1347420415;
        int j1 = 1344798847;
        drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
        drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
        drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
        drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);

        for (int k1 = 0; k1 < lines.size(); ++k1) {
            String s1 = lines.get(k1);
            font.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

            if (k1 == 0) {
                i2 += 2;
            }

            i2 += 10;
        }

        zLevel = 0.0F;
        renderItem.zLevel = 0.0F;
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }

    /**
     * Sets the text of the chat
     */
    protected void setText(String newChatText, boolean shouldOverwrite) {
    }

    /**
     * Executes the click event specified by the given chat component
     */
    public boolean handleComponentClick(Component component) {
        if (component == null) {
            return false;
        } else {
            ClickEvent clickevent = component.getStyle().getClickEvent();

            if (hasShiftDown()) {
                if (component.getStyle().getInsertion() != null) {
                    setText(component.getStyle().getInsertion(), false);
                }
            } else if (clickevent != null) {
                if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!Minecraft.gameSettings.chatLinks) {
                        return false;
                    }

                    try {
                        URI uri = new URI(clickevent.getValue());
                        String s = uri.getScheme();
                        if (s == null) {
                            throw new URISyntaxException(clickevent.getValue(), "Missing protocol");
                        }
                        if (!PROTOCOLS.contains(s.toLowerCase(Locale.ROOT))) {
                            throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s.toLowerCase(Locale.ROOT));
                        }
                        if (Minecraft.gameSettings.chatLinksPrompt) {
                            Minecraft.openScreen(new LinkScreen(() -> Minecraft.openScreen(this), clickevent.getValue()));
                        } else {
                            OS.openUri(uri);
                        }
                    } catch (URISyntaxException urisyntaxexception) {
                        LOGGER.error("Can't open url for {}", clickevent, urisyntaxexception);
                    }
                } else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
                    OS.openFile(new File(clickevent.getValue()));
                } else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    setText(clickevent.getValue(), true);
                } else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    sendChatMessage(clickevent.getValue(), false);
                } else {
                    LOGGER.error("Don't know how to handle {}", clickevent);
                }

                return true;
            }

            return false;
        }
    }

    public void sendChatMessage(String msg) {
        sendChatMessage(msg, true);
    }

    public void sendChatMessage(String msg, boolean addToChat) {
        if (addToChat) {
            Minecraft.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        Minecraft.player.sendChatMessage(msg);
    }

    public final void init(int width, int height) {
        this.width = width;
        this.height = height;
        widgets.clear();
        init();
    }

    public void handleInput() {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                handleMouseInput();
            }
        }
        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                handleKeyboardInput();
            }
        }
    }

    @Deprecated
    public void handleMouseInput() {
        int x = Mouse.getEventX() * width / Minecraft.width;
        int y = height - Mouse.getEventY() * height / Minecraft.height - 1;
        int button = Mouse.getEventButton();
        int wheel = Mouse.getEventDWheel();
        if (Mouse.getEventButtonState()) {
            eventButton = button;
            mousePressed(x, y, button);
        } else if (button != -1) {
            eventButton = -1;
            mouseReleased(x, y, button);
        }
        if (wheel != 0) {
            mouseScrolled(x, y, wheel);
        }
    }

    @Deprecated
    public void handleKeyboardInput() {
        char c0 = Keyboard.getEventCharacter();
        if (Keyboard.getEventKey() == 0 && c0 >= ' ' || Keyboard.getEventKeyState()) {
            keyPressed(Keyboard.getEventKey(), c0);
        }
        minecraft.dispatchKeypresses();
    }

    public final void drawDefaultBackground() {
        if (Minecraft.world != null) {
            drawGradientRect(0, 0, width, height, -1072689136, -804253680);
        } else if (Config.isFastRender()) {
            drawDirtBackground();
        } else {
            drawShaderBackground(Mouse.getX(), Mouse.getY());
            RoundedUtil.drawRound(-1,-1,Minecraft.getScaledRoundedWidth() + 2, Minecraft.getScaledRoundedHeight() + 2,2,new Color(1,1,1,91));
        }
    }

    public final void drawDirtBackground() {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        Minecraft.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, height, 0.0D).tex(0.0D, (float) height / 32.0F + 0).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, height, 0.0D).tex((float) width / 32.0F, (float) height / 32.0F + 0).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(width, 0.0D, 0.0D).tex((float) width / 32.0F, 0).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

    public final void drawShaderBackground(int mouseX, int mouseY) {
        GlStateManager.disableCull();
        backgroundShader.useShader(Minecraft.getScaledRoundedWidth(), Minecraft.getScaledRoundedHeight(), mouseX, mouseY, (float) (System.currentTimeMillis() - initTime) / 1000F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1.0f, -1.0f);
        GL11.glVertex2f(-1.0f, 1.0f);
        GL11.glVertex2f(1.0f, 1.0f);
        GL11.glVertex2f(1.0f, -1.0f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        GlStateManager.disableCull();
    }

    public static boolean hasControlDown() {
        return Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    public static boolean hasShiftDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }

    public static boolean hasAltDown() {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }

    public static boolean isCut(int key) {
        return key == 45 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    public static boolean isPaste(int key) {
        return key == 47 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    public static boolean isCopy(int key) {
        return key == 46 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }

    public static boolean isSelectAll(int key) {
        return key == 30 && hasControlDown() && !hasShiftDown() && !hasAltDown();
    }
}
