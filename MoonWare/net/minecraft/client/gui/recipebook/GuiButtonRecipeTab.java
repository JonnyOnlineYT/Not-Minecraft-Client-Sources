package net.minecraft.client.gui.recipebook;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.RecipeBook;

public class GuiButtonRecipeTab extends GuiButtonToggle
{
    private final CreativeTabs field_193921_u;
    private float field_193922_v;

    public GuiButtonRecipeTab(int p_i47588_1_, CreativeTabs p_i47588_2_)
    {
        super(p_i47588_1_, 0, 0, 35, 27, false);
        field_193921_u = p_i47588_2_;
        func_191751_a(153, 2, 35, 0, GuiRecipeBook.field_191894_a);
    }

    public void func_193918_a(Minecraft p_193918_1_)
    {
        RecipeBook recipebook = Minecraft.player.func_192035_E();
        label21:

        for (RecipeList recipelist : RecipeBookClient.field_194086_e.get(field_193921_u))
        {
            Iterator iterator = recipelist.func_194208_a(recipebook.func_192815_c()).iterator();

            while (true)
            {
                if (!iterator.hasNext())
                {
                    continue label21;
                }

                IRecipe irecipe = (IRecipe)iterator.next();

                if (recipebook.func_194076_e(irecipe))
                {
                    break;
                }
            }

            field_193922_v = 15.0F;
            return;
        }
    }

    public void draw(Minecraft p_191745_1_, int p_191745_2_, int p_191745_3_, float partialTick)
    {
        if (visible)
        {
            if (field_193922_v > 0.0F)
            {
                float f = 1.0F + 0.1F * (float)Math.sin(field_193922_v / 15.0F * (float)Math.PI);
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(x + 8), (float)(y + 12), 0.0F);
                GlStateManager.scale(1.0F, f, 1.0F);
                GlStateManager.translate((float)(-(x + 8)), (float)(-(y + 12)), 0.0F);
            }

            hovered = p_191745_2_ >= x && p_191745_3_ >= y && p_191745_2_ < x + width && p_191745_3_ < y + height;
            Minecraft.getTextureManager().bindTexture(field_191760_o);
            GlStateManager.disableDepth();
            int k = field_191756_q;
            int i = field_191757_r;

            if (field_191755_p)
            {
                k += field_191758_s;
            }

            if (hovered)
            {
                i += field_191759_t;
            }

            int j = x;

            if (field_191755_p)
            {
                j -= 2;
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            drawTexturedModalRect(j, y, k, i, width, height);
            GlStateManager.enableDepth();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            func_193920_a(Minecraft.getRenderItem());
            GlStateManager.enableLighting();
            RenderHelper.disableStandardItemLighting();

            if (field_193922_v > 0.0F)
            {
                GlStateManager.popMatrix();
                field_193922_v -= partialTick;
            }
        }
    }

    private void func_193920_a(RenderItem p_193920_1_)
    {
        ItemStack itemstack = field_193921_u.getIconItemStack();

        if (field_193921_u == CreativeTabs.TOOLS)
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, x + 3, y + 5);
            p_193920_1_.renderItemAndEffectIntoGUI(CreativeTabs.COMBAT.getIconItemStack(), x + 14, y + 5);
        }
        else if (field_193921_u == CreativeTabs.MISC)
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, x + 3, y + 5);
            p_193920_1_.renderItemAndEffectIntoGUI(CreativeTabs.FOOD.getIconItemStack(), x + 14, y + 5);
        }
        else
        {
            p_193920_1_.renderItemAndEffectIntoGUI(itemstack, x + 9, y + 5);
        }
    }

    public CreativeTabs func_191764_e()
    {
        return field_193921_u;
    }

    public boolean func_193919_e()
    {
        List<RecipeList> list = RecipeBookClient.field_194086_e.get(field_193921_u);
        visible = false;

        for (RecipeList recipelist : list)
        {
            if (recipelist.func_194209_a() && recipelist.func_194212_c())
            {
                visible = true;
                break;
            }
        }

        return visible;
    }
}
