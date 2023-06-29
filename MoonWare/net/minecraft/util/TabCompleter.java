package net.minecraft.util;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.TabCompleteEvent;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class TabCompleter
{
    /** The {@link GuiTextField} that is backing this {@link TabCompleter} */
    protected final GuiTextField textField;
    protected final boolean hasTargetBlock;
    protected boolean didComplete;
    protected boolean requestedCompletions;
    protected int completionIdx;
    protected List<String> completions = Lists.newArrayList();

    public TabCompleter(GuiTextField textFieldIn, boolean hasTargetBlockIn)
    {
        textField = textFieldIn;
        hasTargetBlock = hasTargetBlockIn;
    }

    /**
     * Called when tab key pressed. If it's the first time we tried to complete this string, we ask the server for
     * completions. When the server responds, this method gets called again (via setCompletions).
     */
    public void complete()
    {
        if (didComplete)
        {
            textField.deleteFromCursor(0);
            textField.deleteFromCursor(textField.getNthWordFromPosWS(-1, textField.getCursorPosition(), false) - textField.getCursorPosition());

            if (completionIdx >= completions.size())
            {
                completionIdx = 0;
            }
        }
        else
        {
            int i = textField.getNthWordFromPosWS(-1, textField.getCursorPosition(), false);
            completions.clear();
            completionIdx = 0;
            String s = textField.getText().substring(0, textField.getCursorPosition());
            requestCompletions(s);

            if (completions.isEmpty())
            {
                return;
            }

            didComplete = true;
            textField.deleteFromCursor(i - textField.getCursorPosition());
        }

        textField.writeText(completions.get(completionIdx++));
    }

    private void requestCompletions(String prefix)
    {
        if (!(this instanceof ChatScreen.ChatTabCompleter)) {
            return;
        }

        IBaritone baritone = BaritoneAPI.getProvider().getPrimaryBaritone();

        TabCompleteEvent event = new TabCompleteEvent(prefix);
        baritone.getGameEventHandler().onPreTabComplete(event);

        if (event.isCancelled()) {
            event.cancel();
            return;
        }

        if (prefix.length() >= 1)
        {
            Minecraft.player.connection.sendPacket(new CPacketTabComplete(prefix, getTargetBlockPos(), hasTargetBlock));
            requestedCompletions = true;
        }
    }

    @Nullable
    public abstract BlockPos getTargetBlockPos();

    /**
     * Only actually sets completions if they were requested (via requestCompletions)
     */
    public void setCompletions(String... newCompl)
    {
        if (requestedCompletions)
        {
            didComplete = false;
            completions.clear();

            for (String s : newCompl)
            {
                if (!s.isEmpty())
                {
                    completions.add(s);
                }
            }

            String s1 = textField.getText().substring(textField.getNthWordFromPosWS(-1, textField.getCursorPosition(), false));
            String s2 = org.apache.commons.lang3.StringUtils.getCommonPrefix(newCompl);

            if (!s2.isEmpty() && !s1.equalsIgnoreCase(s2))
            {
                textField.deleteFromCursor(0);
                textField.deleteFromCursor(textField.getNthWordFromPosWS(-1, textField.getCursorPosition(), false) - textField.getCursorPosition());
                textField.writeText(s2);
            }
            else if (!completions.isEmpty())
            {
                didComplete = true;
                complete();
            }
        }
    }

    /**
     * Called when new text is entered, or backspace pressed
     */
    public void resetDidComplete()
    {
        didComplete = false;
    }

    public void resetRequested()
    {
        requestedCompletions = false;
    }
}
