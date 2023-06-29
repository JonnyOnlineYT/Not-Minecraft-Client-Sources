package net.minecraft.client.gui.advancements;

import com.google.common.collect.Maps;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import net.minecraft.util.Namespaced;
import org.lwjgl.input.Mouse;

public class GuiScreenAdvancements extends Screen implements ClientAdvancementManager.IListener
{
    private static final Namespaced field_191943_f = new Namespaced("textures/gui/advancements/window.png");
    private static final Namespaced field_191945_g = new Namespaced("textures/gui/advancements/tabs.png");
    private final ClientAdvancementManager field_191946_h;
    private final Map<Advancement, GuiAdvancementTab> field_191947_i = Maps.newLinkedHashMap();
    private GuiAdvancementTab field_191940_s;
    private int field_191941_t;
    private int field_191942_u;
    private boolean field_191944_v;

    public GuiScreenAdvancements(ClientAdvancementManager p_i47383_1_)
    {
        field_191946_h = p_i47383_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void init()
    {
        field_191947_i.clear();
        field_191940_s = null;
        field_191946_h.func_192798_a(this);

        if (field_191940_s == null && !field_191947_i.isEmpty())
        {
            field_191946_h.func_194230_a(field_191947_i.values().iterator().next().func_193935_c(), true);
        }
        else
        {
            field_191946_h.func_194230_a(field_191940_s == null ? null : field_191940_s.func_193935_c(), true);
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onClosed()
    {
        field_191946_h.func_192798_a(null);
        NetHandlerPlayClient nethandlerplayclient = minecraft.getConnection();

        if (nethandlerplayclient != null)
        {
            nethandlerplayclient.sendPacket(CPacketSeenAdvancements.func_194164_a());
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    public void mousePressed(int mouseX, int mouseY, int button)
    {
        if (button == 0)
        {
            int i = (width - 252) / 2;
            int j = (height - 140) / 2;

            for (GuiAdvancementTab guiadvancementtab : field_191947_i.values())
            {
                if (guiadvancementtab.func_191793_c(i, j, mouseX, mouseY))
                {
                    field_191946_h.func_194230_a(guiadvancementtab.func_193935_c(), true);
                    break;
                }
            }
        }

        super.mousePressed(mouseX, mouseY, button);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    public void keyPressed(int key, char c)
    {
        if (key == Minecraft.gameSettings.field_194146_ao.getKeyCode())
        {
            Minecraft.openScreen(null);
            minecraft.setIngameFocus();
        }
        else
        {
            super.keyPressed(key, c);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void draw(int mouseX, int mouseY, float partialTick)
    {
        int i = (width - 252) / 2;
        int j = (height - 140) / 2;

        if (Mouse.isButtonDown(0))
        {
            if (!field_191944_v)
            {
                field_191944_v = true;
            }
            else if (field_191940_s != null)
            {
                field_191940_s.func_191797_b(mouseX - field_191941_t, mouseY - field_191942_u);
            }

            field_191941_t = mouseX;
            field_191942_u = mouseY;
        }
        else
        {
            field_191944_v = false;
        }

        drawDefaultBackground();
        func_191936_c(mouseX, mouseY, i, j);
        func_191934_b(i, j);
        func_191937_d(mouseX, mouseY, i, j);
    }

    private void func_191936_c(int p_191936_1_, int p_191936_2_, int p_191936_3_, int p_191936_4_)
    {
        GuiAdvancementTab guiadvancementtab = field_191940_s;

        if (guiadvancementtab == null)
        {
            Gui.drawRect(p_191936_3_ + 9, p_191936_4_ + 18, p_191936_3_ + 9 + 234, p_191936_4_ + 18 + 113, -16777216);
            String s = I18n.format("advancements.empty");
            int i = font.getStringWidth(s);
            font.drawString(s, p_191936_3_ + 9 + 117 - i / 2, p_191936_4_ + 18 + 56 - font.height / 2, -1);
            font.drawString(":(", p_191936_3_ + 9 + 117 - font.getStringWidth(":(") / 2, p_191936_4_ + 18 + 113 - font.height, -1);
        }
        else
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)(p_191936_3_ + 9), (float)(p_191936_4_ + 18), -400.0F);
            GlStateManager.enableDepth();
            guiadvancementtab.func_191799_a();
            GlStateManager.popMatrix();
            GlStateManager.depthFunc(515);
            GlStateManager.disableDepth();
        }
    }

    public void func_191934_b(int p_191934_1_, int p_191934_2_)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        RenderHelper.disableStandardItemLighting();
        Minecraft.getTextureManager().bindTexture(field_191943_f);
        drawTexturedModalRect(p_191934_1_, p_191934_2_, 0, 0, 252, 140);

        if (field_191947_i.size() > 1)
        {
            Minecraft.getTextureManager().bindTexture(field_191945_g);

            for (GuiAdvancementTab guiadvancementtab : field_191947_i.values())
            {
                guiadvancementtab.func_191798_a(p_191934_1_, p_191934_2_, guiadvancementtab == field_191940_s);
            }

            GlStateManager.enableRescaleNormal();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderHelper.enableGUIStandardItemLighting();

            for (GuiAdvancementTab guiadvancementtab1 : field_191947_i.values())
            {
                guiadvancementtab1.func_191796_a(p_191934_1_, p_191934_2_, renderItem);
            }

            GlStateManager.disableBlend();
        }

        font.drawString(I18n.format("gui.advancements"), p_191934_1_ + 8, p_191934_2_ + 6, 4210752);
    }

    private void func_191937_d(int p_191937_1_, int p_191937_2_, int p_191937_3_, int p_191937_4_)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (field_191940_s != null)
        {
            GlStateManager.pushMatrix();
            GlStateManager.enableDepth();
            GlStateManager.translate((float)(p_191937_3_ + 9), (float)(p_191937_4_ + 18), 400.0F);
            field_191940_s.func_192991_a(p_191937_1_ - p_191937_3_ - 9, p_191937_2_ - p_191937_4_ - 18, p_191937_3_, p_191937_4_);
            GlStateManager.disableDepth();
            GlStateManager.popMatrix();
        }

        if (field_191947_i.size() > 1)
        {
            for (GuiAdvancementTab guiadvancementtab : field_191947_i.values())
            {
                if (guiadvancementtab.func_191793_c(p_191937_3_, p_191937_4_, p_191937_1_, p_191937_2_))
                {
                    drawTooltip(guiadvancementtab.func_191795_d(), p_191937_1_, p_191937_2_);
                }
            }
        }
    }

    public void func_191931_a(Advancement p_191931_1_)
    {
        GuiAdvancementTab guiadvancementtab = GuiAdvancementTab.func_193936_a(minecraft, this, field_191947_i.size(), p_191931_1_);

        if (guiadvancementtab != null)
        {
            field_191947_i.put(p_191931_1_, guiadvancementtab);
        }
    }

    public void func_191928_b(Advancement p_191928_1_)
    {
    }

    public void func_191932_c(Advancement p_191932_1_)
    {
        GuiAdvancementTab guiadvancementtab = func_191935_f(p_191932_1_);

        if (guiadvancementtab != null)
        {
            guiadvancementtab.func_191800_a(p_191932_1_);
        }
    }

    public void func_191929_d(Advancement p_191929_1_)
    {
    }

    public void func_191933_a(Advancement p_191933_1_, AdvancementProgress p_191933_2_)
    {
        GuiAdvancement guiadvancement = func_191938_e(p_191933_1_);

        if (guiadvancement != null)
        {
            guiadvancement.func_191824_a(p_191933_2_);
        }
    }

    public void func_193982_e(@Nullable Advancement p_193982_1_)
    {
        field_191940_s = field_191947_i.get(p_193982_1_);
    }

    public void func_191930_a()
    {
        field_191947_i.clear();
        field_191940_s = null;
    }

    @Nullable
    public GuiAdvancement func_191938_e(Advancement p_191938_1_)
    {
        GuiAdvancementTab guiadvancementtab = func_191935_f(p_191938_1_);
        return guiadvancementtab == null ? null : guiadvancementtab.func_191794_b(p_191938_1_);
    }

    @Nullable
    private GuiAdvancementTab func_191935_f(Advancement p_191935_1_)
    {
        while (p_191935_1_.getParent() != null)
        {
            p_191935_1_ = p_191935_1_.getParent();
        }

        return field_191947_i.get(p_191935_1_);
    }
}
