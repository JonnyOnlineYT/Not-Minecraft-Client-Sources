package net.minecraft.client.gui.chat;

import com.mojang.text2speech.Narrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class NarratorChatListener implements IChatListener
{
    public static final NarratorChatListener field_193643_a = new NarratorChatListener();
    private final Narrator field_192580_a = Narrator.getNarrator();

    public void func_192576_a(ChatType p_192576_1_, ITextComponent p_192576_2_)
    {
    }

    public void func_193641_a(int p_193641_1_)
    {
    }

    public boolean func_193640_a()
    {
        return this.field_192580_a.active();
    }

    public void func_193642_b()
    {

    }
}
