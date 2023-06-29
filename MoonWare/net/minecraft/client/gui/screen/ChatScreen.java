package net.minecraft.client.gui.screen;

import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.Formatting;
import net.minecraft.util.text.TextComponent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.moonware.client.MoonWare;
import org.moonware.client.feature.impl.hud.ArrayGlowComp.StencilUtil;
import org.moonware.client.feature.impl.misc.CustomChat;
import org.moonware.client.helpers.Utils.blur.GaussianBlur;
import org.moonware.client.ui.components.draggable.HudManager;
import org.moonware.client.utils.MWFont;

import javax.annotation.Nullable;
import java.awt.*;

@NoArgsConstructor
public class ChatScreen extends Screen implements ITabCompleter {
    private String historyBuffer = "";
    private int sentHistoryCursor = -1;
    private TabCompleter tabCompleter;
    protected GuiTextField field;
    private long startTime;
    private String value = "";

    public ChatScreen(String value) {
        this.value = value;
    }

    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        sentHistoryCursor = Minecraft.ingameGUI.getChatGUI().getSentMessages().size();
        field = new GuiTextField(0, font, 4, height - 12, width - 4, 12);
        field.setMaxStringLength(256);
        field.setEnableBackgroundDrawing(false);
        field.setFocused(true);
        field.setText(value);
        field.setCanLoseFocus(false);
        tabCompleter = new ChatTabCompleter(field);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTick) {
        GlStateManager.pushMatrix();
        double percent = value(startTime);
        GlStateManager.translate(0.0, 14.0 - 14.0 * percent, 0.0);
        StencilUtil.initStencilToWrite();
        drawRect(2, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(5);
        StencilUtil.uninitStencilBuffer();
        drawRect(2, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
        if (!field.getText().isEmpty() && !(field.getText().startsWith(".help")))
            field.drawTextBox();
        if (field.getText().startsWith(".help")) {
            MWFont.SF_UI_TEXT_SEMIBOLD.get(18).drawGradient(".help", 4, height - 11, -1);
            MWFont.SF_UI_TEXT_SEMIBOLD.get(18).draw("<no args>", 30, height - 11, new Color(231,231,231,120).getRGB());

        }
        if (field.getText().isEmpty()) {
            MWFont.SF_UI_TEXT_SEMIBOLD.get(18).drawGradient(Formatting.RED + "Write " + Formatting.GRAY + "." + Formatting.GREEN + "help " + Formatting.GRAY + "to get active commands.", 4, height - 11, -1);
        }
        Component component = Minecraft.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (component != null && component.getStyle().getHoverEvent() != null) {
            //gdagdrawTooltip(component, mouseX, mouseY);
        }
        GlStateManager.popMatrix();
        super.draw(mouseX, mouseY, partialTick);
    }

    @Override
    public void update() {
        field.update();
        super.update();
    }

    @Override
    public boolean pauses() {
        return false;
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int button) {
        Component component = Minecraft.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (button == 0 && handleComponentClick(component)) return;
        field.mouseClicked(mouseX, mouseY, button);
        HudManager.mousePressed(mouseX, mouseY, button);
        super.mousePressed(mouseX, mouseY, button);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        HudManager.mouseReleased(mouseX, mouseY, button);
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseScrolled(int mouseX, int mouseY, int scroll) {
        super.mouseScrolled(mouseX, mouseY, scroll);
        scroll = MathHelper.clamp(scroll, -1, 1);
        if (!Screen.hasShiftDown()) scroll *= 7;
        Minecraft.ingameGUI.getChatGUI().scroll(scroll);
    }

    @Override
    public void keyPressed(int key, char c) {
        tabCompleter.resetRequested();
        if (key == 15) {
            tabCompleter.complete();
        } else {
            tabCompleter.resetDidComplete();
        }
        if (key == Keyboard.KEY_ESCAPE) {
            close();
        } else if (key == Keyboard.KEY_RETURN || key == Keyboard.KEY_NUMPADENTER) {
            String s = field.getText().trim();
            if (!s.isEmpty()) sendChatMessage(s);
            close();
        } else if (key == 200) {
            getSentHistory(-1);
        } else if (key == 208) {
            getSentHistory(1);
        } else if (key == 201) {
            Minecraft.ingameGUI.getChatGUI().scroll(Minecraft.ingameGUI.getChatGUI().getLineCount() - 1);
        } else if (key == 209) {
            Minecraft.ingameGUI.getChatGUI().scroll(-Minecraft.ingameGUI.getChatGUI().getLineCount() + 1);
        } else {
            field.textboxKeyTyped(c, key);
            super.keyPressed(key, c);
        }
    }

    @Override
    public void onClosed() {
        Keyboard.enableRepeatEvents(false);
        Minecraft.ingameGUI.getChatGUI().resetScroll();
        HudManager.closed();
        super.onClosed();
    }

    @Override
    protected void setText(String newChatText, boolean shouldOverwrite) {
        if (shouldOverwrite) {
            field.setText(newChatText);
        } else {
            field.writeText(newChatText);
        }
    }

    /**
     * input is relative and is applied directly to the sentHistoryCursor so -1 is the previous message, 1 is the next
     * message from the current cursor position
     */
    public void getSentHistory(int msgPos) {
        int i = sentHistoryCursor + msgPos;
        int j = Minecraft.ingameGUI.getChatGUI().getSentMessages().size();
        if ((i = MathHelper.clamp(i, 0, MoonWare.featureManager.getFeatureByClass(CustomChat.class).getState() && CustomChat.infinityLengths.getCurrentValue() ? Integer.MAX_VALUE : j)) != sentHistoryCursor) {
            if (i == j) {
                sentHistoryCursor = j;
                field.setText(historyBuffer);
            } else {
                if (sentHistoryCursor == j) {
                    historyBuffer = field.getText();
                }
                field.setText(Minecraft.ingameGUI.getChatGUI().getSentMessages().get(i));
                sentHistoryCursor = i;
            }
        }
    }

    public static float value(long startTime) {
        return Math.min(1.0f, (float) Math.pow((double) (System.currentTimeMillis() - startTime) / 10.0, 1.4) / 80.0f);
    }

    public void setCompletions(String... newCompletions) {
        tabCompleter.setCompletions(newCompletions);
    }

    public static class ChatTabCompleter extends TabCompleter {
        public ChatTabCompleter(GuiTextField field) {
            super(field, false);
        }

        public void complete() {
            super.complete();
            if (completions.size() > 1) {
                StringBuilder stringbuilder = new StringBuilder();
                for (String s : completions) {
                    if (stringbuilder.length() > 0) {
                        stringbuilder.append(", ");
                    }
                    stringbuilder.append(s);
                }
                Minecraft.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponent(stringbuilder.toString()), 1);
            }
        }

        @Nullable
        public BlockPos getTargetBlockPos() {
            BlockPos blockpos = null;
            if (Minecraft.objectMouseOver != null && Minecraft.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                blockpos = Minecraft.objectMouseOver.getBlockPos();
            }
            return blockpos;
        }
    }
}
