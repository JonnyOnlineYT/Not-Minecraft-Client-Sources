package net.minecraft.client.gui.recipebook;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.Namespaced;
import net.minecraft.util.math.MathHelper;

public class GuiButtonRecipe extends ButtonWidget
{
    private static final Namespaced field_191780_o = new Namespaced("textures/gui/recipe_book.png");
    private RecipeBook field_193930_p;
    private RecipeList field_191774_p;
    private float field_193931_r;
    private float field_191778_t;
    private int field_193932_t;

    public GuiButtonRecipe()
    {
        super(0, 0, 0, 25, 25, "");
    }

    public void func_193928_a(RecipeList p_193928_1_, RecipeBookPage p_193928_2_, RecipeBook p_193928_3_)
    {
        field_191774_p = p_193928_1_;
        field_193930_p = p_193928_3_;
        List<IRecipe> list = p_193928_1_.func_194208_a(p_193928_3_.func_192815_c());

        for (IRecipe irecipe : list)
        {
            if (p_193928_3_.func_194076_e(irecipe))
            {
                p_193928_2_.func_194195_a(list);
                field_191778_t = 15.0F;
                break;
            }
        }
    }

    public RecipeList func_191771_c()
    {
        return field_191774_p;
    }

    public void func_191770_c(int p_191770_1_, int p_191770_2_)
    {
        x = p_191770_1_;
        y = p_191770_2_;
    }

    public void draw(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float partialTick)
    {
        if (visible)
        {
            if (!Screen.hasControlDown())
            {
                field_193931_r += partialTick;
            }

            hovered = p_191745_2_ >= x && p_191745_3_ >= y && p_191745_2_ < x + width && p_191745_3_ < y + height;
            RenderHelper.enableGUIStandardItemLighting();
            Minecraft.getTextureManager().bindTexture(field_191780_o);
            GlStateManager.disableLighting();
            int i = 29;

            if (!field_191774_p.func_192708_c())
            {
                i += 25;
            }

            int j = 206;

            if (field_191774_p.func_194208_a(field_193930_p.func_192815_c()).size() > 1)
            {
                j += 25;
            }

            boolean flag = field_191778_t > 0.0F;

            if (flag)
            {
                float f = 1.0F + 0.1F * (float)Math.sin(field_191778_t / 15.0F * (float)Math.PI);
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(x + 8), (float)(y + 12), 0.0F);
                GlStateManager.scale(f, f, 1.0F);
                GlStateManager.translate((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
                field_191778_t -= partialTick;
            }

            drawTexturedModalRect(x, y, i, j, width, height);
            List<IRecipe> list = func_193927_f();
            field_193932_t = MathHelper.floor(field_193931_r / 30.0F) % list.size();
            ItemStack itemstack = list.get(field_193932_t).getRecipeOutput();
            int k = 4;

            if (field_191774_p.func_194211_e() && func_193927_f().size() > 1)
            {
                Minecraft.getRenderItem().renderItemAndEffectIntoGUI(itemstack, x + k + 1, y + k + 1);
                --k;
            }

            Minecraft.getRenderItem().renderItemAndEffectIntoGUI(itemstack, x + k, y + k);

            if (flag)
            {
                GlStateManager.popMatrix();
            }

            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();
        }
    }

    private List<IRecipe> func_193927_f()
    {
        List<IRecipe> list = field_191774_p.func_194207_b(true);

        if (!field_193930_p.func_192815_c())
        {
            list.addAll(field_191774_p.func_194207_b(false));
        }

        return list;
    }

    public boolean func_193929_d()
    {
        return func_193927_f().size() == 1;
    }

    public IRecipe func_193760_e()
    {
        List<IRecipe> list = func_193927_f();
        return list.get(field_193932_t);
    }

    public List<String> func_191772_a(Screen p_191772_1_)
    {
        ItemStack itemstack = func_193927_f().get(field_193932_t).getRecipeOutput();
        List<String> list = p_191772_1_.getTooltip(itemstack);

        if (field_191774_p.func_194208_a(field_193930_p.func_192815_c()).size() > 1)
        {
            list.add(I18n.format("gui.recipebook.moreRecipes"));
        }

        return list;
    }

    public int getButtonWidth()
    {
        return 25;
    }
}
