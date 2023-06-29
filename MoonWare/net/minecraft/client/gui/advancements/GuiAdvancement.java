package net.minecraft.client.gui.advancements;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementDisplay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;

public class GuiAdvancement extends Gui
{
    private static final Namespaced field_191827_a = new Namespaced("textures/gui/advancements/widgets.png");
    private static final Pattern field_192996_f = Pattern.compile("(.+) \\S+");
    private final GuiAdvancementTab field_191828_f;
    private final Advancement field_191829_g;
    private final AdvancementDisplay field_191830_h;
    private final String field_191831_i;
    private final int field_191832_j;
    private final List<String> field_192997_l;
    private final Minecraft field_191833_k;
    private GuiAdvancement field_191834_l;
    private final List<GuiAdvancement> field_191835_m = Lists.newArrayList();
    private AdvancementProgress field_191836_n;
    private final int field_191837_o;
    private final int field_191826_p;

    public GuiAdvancement(GuiAdvancementTab p_i47385_1_, Minecraft p_i47385_2_, Advancement p_i47385_3_, AdvancementDisplay p_i47385_4_)
    {
        field_191828_f = p_i47385_1_;
        field_191829_g = p_i47385_3_;
        field_191830_h = p_i47385_4_;
        field_191833_k = p_i47385_2_;
        field_191831_i = Minecraft.font.trimStringToWidth(p_i47385_4_.func_192297_a().asFormattedString(), 163);
        field_191837_o = MathHelper.floor(p_i47385_4_.func_192299_e() * 28.0F);
        field_191826_p = MathHelper.floor(p_i47385_4_.func_192296_f() * 27.0F);
        int i = p_i47385_3_.getRequirements().length;
        int j = String.valueOf(i).length();
        int k = i > 1 ? Minecraft.font.getStringWidth("  ") + Minecraft.font.getStringWidth("0") * j * 2 + Minecraft.font.getStringWidth("/") : 0;
        int l = 29 + Minecraft.font.getStringWidth(field_191831_i) + k;
        String s = p_i47385_4_.func_193222_b().asFormattedString();
        field_192997_l = func_192995_a(s, l);

        for (String s1 : field_192997_l)
        {
            l = Math.max(l, Minecraft.font.getStringWidth(s1));
        }

        field_191832_j = l + 3 + 5;
    }

    private List<String> func_192995_a(String p_192995_1_, int p_192995_2_)
    {
        if (p_192995_1_.isEmpty())
        {
            return Collections.emptyList();
        }
        else
        {
            List<String> list = Minecraft.font.split(p_192995_1_, p_192995_2_);

            if (list.size() < 2)
            {
                return list;
            }
            else
            {
                String s = list.get(0);
                String s1 = list.get(1);
                int i = Minecraft.font.getStringWidth(s + ' ' + s1.split(" ")[0]);

                if (i - p_192995_2_ <= 10)
                {
                    return Minecraft.font.split(p_192995_1_, i);
                }
                else
                {
                    Matcher matcher = field_192996_f.matcher(s);

                    if (matcher.matches())
                    {
                        int j = Minecraft.font.getStringWidth(matcher.group(1));

                        if (p_192995_2_ - j <= 10)
                        {
                            return Minecraft.font.split(p_192995_1_, j);
                        }
                    }

                    return list;
                }
            }
        }
    }

    @Nullable
    private GuiAdvancement func_191818_a(Advancement p_191818_1_)
    {
        while (true)
        {
            p_191818_1_ = p_191818_1_.getParent();

            if (p_191818_1_ == null || p_191818_1_.getDisplay() != null)
            {
                break;
            }
        }

        if (p_191818_1_ != null && p_191818_1_.getDisplay() != null)
        {
            return field_191828_f.func_191794_b(p_191818_1_);
        }
        else
        {
            return null;
        }
    }

    public void func_191819_a(int p_191819_1_, int p_191819_2_, boolean p_191819_3_)
    {
        if (field_191834_l != null)
        {
            int i = p_191819_1_ + field_191834_l.field_191837_o + 13;
            int j = p_191819_1_ + field_191834_l.field_191837_o + 26 + 4;
            int k = p_191819_2_ + field_191834_l.field_191826_p + 13;
            int l = p_191819_1_ + field_191837_o + 13;
            int i1 = p_191819_2_ + field_191826_p + 13;
            int j1 = p_191819_3_ ? -16777216 : -1;

            if (p_191819_3_)
            {
                drawHorizontalLine(j, i, k - 1, j1);
                drawHorizontalLine(j + 1, i, k, j1);
                drawHorizontalLine(j, i, k + 1, j1);
                drawHorizontalLine(l, j - 1, i1 - 1, j1);
                drawHorizontalLine(l, j - 1, i1, j1);
                drawHorizontalLine(l, j - 1, i1 + 1, j1);
                drawVerticalLine(j - 1, i1, k, j1);
                drawVerticalLine(j + 1, i1, k, j1);
            }
            else
            {
                drawHorizontalLine(j, i, k, j1);
                drawHorizontalLine(l, j, i1, j1);
                drawVerticalLine(j, i1, k, j1);
            }
        }

        for (GuiAdvancement guiadvancement : field_191835_m)
        {
            guiadvancement.func_191819_a(p_191819_1_, p_191819_2_, p_191819_3_);
        }
    }

    public void func_191817_b(int p_191817_1_, int p_191817_2_)
    {
        if (!field_191830_h.func_193224_j() || field_191836_n != null && field_191836_n.func_192105_a())
        {
            float f = field_191836_n == null ? 0.0F : field_191836_n.func_192103_c();
            AdvancementState advancementstate;

            if (f >= 1.0F)
            {
                advancementstate = AdvancementState.OBTAINED;
            }
            else
            {
                advancementstate = AdvancementState.UNOBTAINED;
            }

            Minecraft.getTextureManager().bindTexture(field_191827_a);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            drawTexturedModalRect(p_191817_1_ + field_191837_o + 3, p_191817_2_ + field_191826_p, field_191830_h.func_192291_d().func_192309_b(), 128 + advancementstate.func_192667_a() * 26, 26, 26);
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, field_191830_h.func_192298_b(), p_191817_1_ + field_191837_o + 8, p_191817_2_ + field_191826_p + 5);
        }

        for (GuiAdvancement guiadvancement : field_191835_m)
        {
            guiadvancement.func_191817_b(p_191817_1_, p_191817_2_);
        }
    }

    public void func_191824_a(AdvancementProgress p_191824_1_)
    {
        field_191836_n = p_191824_1_;
    }

    public void func_191822_a(GuiAdvancement p_191822_1_)
    {
        field_191835_m.add(p_191822_1_);
    }

    public void func_191821_a(int p_191821_1_, int p_191821_2_, float p_191821_3_, int p_191821_4_, int p_191821_5_)
    {
        boolean flag = p_191821_4_ + p_191821_1_ + field_191837_o + field_191832_j + 26 >= field_191828_f.func_193934_g().width;
        String s = field_191836_n == null ? null : field_191836_n.func_193126_d();
        int i = s == null ? 0 : Minecraft.font.getStringWidth(s);
        boolean flag1 = 113 - p_191821_2_ - field_191826_p - 26 <= 6 + field_192997_l.size() * Minecraft.font.height;
        float f = field_191836_n == null ? 0.0F : field_191836_n.func_192103_c();
        int j = MathHelper.floor(f * (float) field_191832_j);
        AdvancementState advancementstate;
        AdvancementState advancementstate1;
        AdvancementState advancementstate2;

        if (f >= 1.0F)
        {
            j = field_191832_j / 2;
            advancementstate = AdvancementState.OBTAINED;
            advancementstate1 = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.OBTAINED;
        }
        else if (j < 2)
        {
            j = field_191832_j / 2;
            advancementstate = AdvancementState.UNOBTAINED;
            advancementstate1 = AdvancementState.UNOBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
        }
        else if (j > field_191832_j - 2)
        {
            j = field_191832_j / 2;
            advancementstate = AdvancementState.OBTAINED;
            advancementstate1 = AdvancementState.OBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
        }
        else
        {
            advancementstate = AdvancementState.OBTAINED;
            advancementstate1 = AdvancementState.UNOBTAINED;
            advancementstate2 = AdvancementState.UNOBTAINED;
        }

        int k = field_191832_j - j;
        Minecraft.getTextureManager().bindTexture(field_191827_a);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        int l = p_191821_2_ + field_191826_p;
        int i1;

        if (flag)
        {
            i1 = p_191821_1_ + field_191837_o - field_191832_j + 26 + 6;
        }
        else
        {
            i1 = p_191821_1_ + field_191837_o;
        }

        int j1 = 32 + field_192997_l.size() * Minecraft.font.height;

        if (!field_192997_l.isEmpty())
        {
            if (flag1)
            {
                func_192994_a(i1, l + 26 - j1, field_191832_j, j1, 10, 200, 26, 0, 52);
            }
            else
            {
                func_192994_a(i1, l, field_191832_j, j1, 10, 200, 26, 0, 52);
            }
        }

        drawTexturedModalRect(i1, l, 0, advancementstate.func_192667_a() * 26, j, 26);
        drawTexturedModalRect(i1 + j, l, 200 - k, advancementstate1.func_192667_a() * 26, k, 26);
        drawTexturedModalRect(p_191821_1_ + field_191837_o + 3, p_191821_2_ + field_191826_p, field_191830_h.func_192291_d().func_192309_b(), 128 + advancementstate2.func_192667_a() * 26, 26, 26);

        if (flag)
        {
            Minecraft.font.drawString(field_191831_i, (float)(i1 + 5), (float)(p_191821_2_ + field_191826_p + 9), -1, true);

            if (s != null)
            {
                Minecraft.font.drawString(s, (float)(p_191821_1_ + field_191837_o - i), (float)(p_191821_2_ + field_191826_p + 9), -1, true);
            }
        }
        else
        {
            Minecraft.font.drawString(field_191831_i, (float)(p_191821_1_ + field_191837_o + 32), (float)(p_191821_2_ + field_191826_p + 9), -1, true);

            if (s != null)
            {
                Minecraft.font.drawString(s, (float)(p_191821_1_ + field_191837_o + field_191832_j - i - 5), (float)(p_191821_2_ + field_191826_p + 9), -1, true);
            }
        }

        if (flag1)
        {
            for (int k1 = 0; k1 < field_192997_l.size(); ++k1)
            {
                Minecraft.font.drawString(field_192997_l.get(k1), (float)(i1 + 5), (float)(l + 26 - j1 + 7 + k1 * Minecraft.font.height), -5592406, false);
            }
        }
        else
        {
            for (int l1 = 0; l1 < field_192997_l.size(); ++l1)
            {
                Minecraft.font.drawString(field_192997_l.get(l1), (float)(i1 + 5), (float)(p_191821_2_ + field_191826_p + 9 + 17 + l1 * Minecraft.font.height), -5592406, false);
            }
        }

        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, field_191830_h.func_192298_b(), p_191821_1_ + field_191837_o + 8, p_191821_2_ + field_191826_p + 5);
    }

    protected void func_192994_a(int p_192994_1_, int p_192994_2_, int p_192994_3_, int p_192994_4_, int p_192994_5_, int p_192994_6_, int p_192994_7_, int p_192994_8_, int p_192994_9_)
    {
        drawTexturedModalRect(p_192994_1_, p_192994_2_, p_192994_8_, p_192994_9_, p_192994_5_, p_192994_5_);
        func_192993_a(p_192994_1_ + p_192994_5_, p_192994_2_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
        drawTexturedModalRect(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_, p_192994_5_, p_192994_5_);
        drawTexturedModalRect(p_192994_1_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
        func_192993_a(p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_);
        drawTexturedModalRect(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_4_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_7_ - p_192994_5_, p_192994_5_, p_192994_5_);
        func_192993_a(p_192994_1_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
        func_192993_a(p_192994_1_ + p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_3_ - p_192994_5_ - p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_ - p_192994_5_ - p_192994_5_, p_192994_7_ - p_192994_5_ - p_192994_5_);
        func_192993_a(p_192994_1_ + p_192994_3_ - p_192994_5_, p_192994_2_ + p_192994_5_, p_192994_5_, p_192994_4_ - p_192994_5_ - p_192994_5_, p_192994_8_ + p_192994_6_ - p_192994_5_, p_192994_9_ + p_192994_5_, p_192994_6_, p_192994_7_ - p_192994_5_ - p_192994_5_);
    }

    protected void func_192993_a(int p_192993_1_, int p_192993_2_, int p_192993_3_, int p_192993_4_, int p_192993_5_, int p_192993_6_, int p_192993_7_, int p_192993_8_)
    {
        for (int i = 0; i < p_192993_3_; i += p_192993_7_)
        {
            int j = p_192993_1_ + i;
            int k = Math.min(p_192993_7_, p_192993_3_ - i);

            for (int l = 0; l < p_192993_4_; l += p_192993_8_)
            {
                int i1 = p_192993_2_ + l;
                int j1 = Math.min(p_192993_8_, p_192993_4_ - l);
                drawTexturedModalRect(j, i1, p_192993_5_, p_192993_6_, k, j1);
            }
        }
    }

    public boolean func_191816_c(int p_191816_1_, int p_191816_2_, int p_191816_3_, int p_191816_4_)
    {
        if (!field_191830_h.func_193224_j() || field_191836_n != null && field_191836_n.func_192105_a())
        {
            int i = p_191816_1_ + field_191837_o;
            int j = i + 26;
            int k = p_191816_2_ + field_191826_p;
            int l = k + 26;
            return p_191816_3_ >= i && p_191816_3_ <= j && p_191816_4_ >= k && p_191816_4_ <= l;
        }
        else
        {
            return false;
        }
    }

    public void func_191825_b()
    {
        if (field_191834_l == null && field_191829_g.getParent() != null)
        {
            field_191834_l = func_191818_a(field_191829_g);

            if (field_191834_l != null)
            {
                field_191834_l.func_191822_a(this);
            }
        }
    }

    public int func_191820_c()
    {
        return field_191826_p;
    }

    public int func_191823_d()
    {
        return field_191837_o;
    }
}
