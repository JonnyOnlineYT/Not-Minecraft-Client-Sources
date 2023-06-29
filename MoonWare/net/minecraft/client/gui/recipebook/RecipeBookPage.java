package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class RecipeBookPage
{
    private List<GuiButtonRecipe> field_193743_h = Lists.newArrayListWithCapacity(20);
    private GuiButtonRecipe field_194201_b;
    private GuiRecipeOverlay field_194202_c = new GuiRecipeOverlay();
    private Minecraft field_193754_s;
    private List<IRecipeUpdateListener> field_193757_v = Lists.newArrayList();
    private List<RecipeList> field_194203_f;
    private GuiButtonToggle field_193740_e;
    private GuiButtonToggle field_193741_f;
    private int field_193737_b;
    private int field_193738_c;
    private RecipeBook field_194204_k;
    private IRecipe field_194205_l;
    private RecipeList field_194206_m;

    public RecipeBookPage()
    {
        for (int i = 0; i < 20; ++i)
        {
            field_193743_h.add(new GuiButtonRecipe());
        }
    }

    public void func_194194_a(Minecraft p_194194_1_, int p_194194_2_, int p_194194_3_)
    {
        field_193754_s = p_194194_1_;
        field_194204_k = Minecraft.player.func_192035_E();

        for (int i = 0; i < field_193743_h.size(); ++i)
        {
            field_193743_h.get(i).func_191770_c(p_194194_2_ + 11 + 25 * (i % 5), p_194194_3_ + 31 + 25 * (i / 5));
        }

        field_193740_e = new GuiButtonToggle(0, p_194194_2_ + 93, p_194194_3_ + 137, 12, 17, false);
        field_193740_e.func_191751_a(1, 208, 13, 18, GuiRecipeBook.field_191894_a);
        field_193741_f = new GuiButtonToggle(0, p_194194_2_ + 38, p_194194_3_ + 137, 12, 17, true);
        field_193741_f.func_191751_a(1, 208, 13, 18, GuiRecipeBook.field_191894_a);
    }

    public void func_193732_a(GuiRecipeBook p_193732_1_)
    {
        field_193757_v.remove(p_193732_1_);
        field_193757_v.add(p_193732_1_);
    }

    public void func_194192_a(List<RecipeList> p_194192_1_, boolean p_194192_2_)
    {
        field_194203_f = p_194192_1_;
        field_193737_b = (int)Math.ceil((double)p_194192_1_.size() / 20.0D);

        if (field_193737_b <= field_193738_c || p_194192_2_)
        {
            field_193738_c = 0;
        }

        func_194198_d();
    }

    private void func_194198_d()
    {
        int i = 20 * field_193738_c;

        for (int j = 0; j < field_193743_h.size(); ++j)
        {
            GuiButtonRecipe guibuttonrecipe = field_193743_h.get(j);

            if (i + j < field_194203_f.size())
            {
                RecipeList recipelist = field_194203_f.get(i + j);
                guibuttonrecipe.func_193928_a(recipelist, this, field_194204_k);
                guibuttonrecipe.visible = true;
            }
            else
            {
                guibuttonrecipe.visible = false;
            }
        }

        func_194197_e();
    }

    private void func_194197_e()
    {
        field_193740_e.visible = field_193737_b > 1 && field_193738_c < field_193737_b - 1;
        field_193741_f.visible = field_193737_b > 1 && field_193738_c > 0;
    }

    public void func_194191_a(int p_194191_1_, int p_194191_2_, int p_194191_3_, int p_194191_4_, float p_194191_5_)
    {
        if (field_193737_b > 1)
        {
            String s = field_193738_c + 1 + "/" + field_193737_b;
            int i = Minecraft.font.getStringWidth(s);
            Minecraft.font.drawString(s, p_194191_1_ - i / 2 + 73, p_194191_2_ + 141, -1);
        }

        RenderHelper.disableStandardItemLighting();
        field_194201_b = null;

        for (GuiButtonRecipe guibuttonrecipe : field_193743_h)
        {
            guibuttonrecipe.draw(field_193754_s, p_194191_3_, p_194191_4_, p_194191_5_);

            if (guibuttonrecipe.visible && guibuttonrecipe.isMouseOver())
            {
                field_194201_b = guibuttonrecipe;
            }
        }

        field_193741_f.draw(field_193754_s, p_194191_3_, p_194191_4_, p_194191_5_);
        field_193740_e.draw(field_193754_s, p_194191_3_, p_194191_4_, p_194191_5_);
        field_194202_c.func_191842_a(p_194191_3_, p_194191_4_, p_194191_5_);
    }

    public void func_193721_a(int p_193721_1_, int p_193721_2_)
    {
        if (Minecraft.screen != null && field_194201_b != null && !field_194202_c.func_191839_a())
        {
            Minecraft.screen.drawTooltip(field_194201_b.func_191772_a(Minecraft.screen), p_193721_1_, p_193721_2_);
        }
    }

    @Nullable
    public IRecipe func_194193_a()
    {
        return field_194205_l;
    }

    @Nullable
    public RecipeList func_194199_b()
    {
        return field_194206_m;
    }

    public void func_194200_c()
    {
        field_194202_c.func_192999_a(false);
    }

    public boolean func_194196_a(int p_194196_1_, int p_194196_2_, int p_194196_3_, int p_194196_4_, int p_194196_5_, int p_194196_6_, int p_194196_7_)
    {
        field_194205_l = null;
        field_194206_m = null;

        if (field_194202_c.func_191839_a())
        {
            if (field_194202_c.func_193968_a(p_194196_1_, p_194196_2_, p_194196_3_))
            {
                field_194205_l = field_194202_c.func_193967_b();
                field_194206_m = field_194202_c.func_193971_a();
            }
            else
            {
                field_194202_c.func_192999_a(false);
            }

            return true;
        }
        else if (field_193740_e.mousePressed(field_193754_s, p_194196_1_, p_194196_2_) && p_194196_3_ == 0)
        {
            field_193740_e.playPressSound(Minecraft.getSoundHandler());
            ++field_193738_c;
            func_194198_d();
            return true;
        }
        else if (field_193741_f.mousePressed(field_193754_s, p_194196_1_, p_194196_2_) && p_194196_3_ == 0)
        {
            field_193741_f.playPressSound(Minecraft.getSoundHandler());
            --field_193738_c;
            func_194198_d();
            return true;
        }
        else
        {
            for (GuiButtonRecipe guibuttonrecipe : field_193743_h)
            {
                if (guibuttonrecipe.mousePressed(field_193754_s, p_194196_1_, p_194196_2_))
                {
                    guibuttonrecipe.playPressSound(Minecraft.getSoundHandler());

                    if (p_194196_3_ == 0)
                    {
                        field_194205_l = guibuttonrecipe.func_193760_e();
                        field_194206_m = guibuttonrecipe.func_191771_c();
                    }
                    else if (!field_194202_c.func_191839_a() && !guibuttonrecipe.func_193929_d())
                    {
                        field_194202_c.func_191845_a(field_193754_s, guibuttonrecipe.func_191771_c(), guibuttonrecipe.x, guibuttonrecipe.y, p_194196_4_ + p_194196_6_ / 2, p_194196_5_ + 13 + p_194196_7_ / 2, (float)guibuttonrecipe.getButtonWidth(), field_194204_k);
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public void func_194195_a(List<IRecipe> p_194195_1_)
    {
        for (IRecipeUpdateListener irecipeupdatelistener : field_193757_v)
        {
            irecipeupdatelistener.func_193001_a(p_194195_1_);
        }
    }
}
