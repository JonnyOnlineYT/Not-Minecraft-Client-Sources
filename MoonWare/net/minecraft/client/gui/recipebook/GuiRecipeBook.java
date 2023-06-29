package net.minecraft.client.gui.recipebook;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.stats.RecipeBook;
import net.minecraft.util.Namespaced;
import org.lwjgl.input.Keyboard;

public class GuiRecipeBook extends Gui implements IRecipeUpdateListener
{
    protected static final Namespaced field_191894_a = new Namespaced("textures/gui/recipe_book.png");
    private int field_191903_n;
    private int field_191904_o;
    private int field_191905_p;
    private final GhostRecipe field_191915_z = new GhostRecipe();
    private final List<GuiButtonRecipeTab> field_193018_j = Lists.newArrayList(new GuiButtonRecipeTab(0, CreativeTabs.SEARCH), new GuiButtonRecipeTab(0, CreativeTabs.TOOLS), new GuiButtonRecipeTab(0, CreativeTabs.BUILDING_BLOCKS), new GuiButtonRecipeTab(0, CreativeTabs.MISC), new GuiButtonRecipeTab(0, CreativeTabs.REDSTONE));
    private GuiButtonRecipeTab field_191913_x;
    private GuiButtonToggle field_193960_m;
    private InventoryCrafting field_193961_o;
    private Minecraft field_191888_F;
    private GuiTextField field_193962_q;
    private String field_193963_r = "";
    private RecipeBook field_193964_s;
    private final RecipeBookPage field_193022_s = new RecipeBookPage();
    private RecipeItemHelper field_193965_u = new RecipeItemHelper();
    private int field_193966_v;

    public void func_194303_a(int p_194303_1_, int p_194303_2_, Minecraft p_194303_3_, boolean p_194303_4_, InventoryCrafting p_194303_5_)
    {
        field_191888_F = p_194303_3_;
        field_191904_o = p_194303_1_;
        field_191905_p = p_194303_2_;
        field_193961_o = p_194303_5_;
        field_193964_s = Minecraft.player.func_192035_E();
        field_193966_v = Minecraft.player.inventory.func_194015_p();
        field_191913_x = field_193018_j.get(0);
        field_191913_x.func_191753_b(true);

        if (func_191878_b())
        {
            func_193014_a(p_194303_4_, p_194303_5_);
        }

        Keyboard.enableRepeatEvents(true);
    }

    public void func_193014_a(boolean p_193014_1_, InventoryCrafting p_193014_2_)
    {
        field_191903_n = p_193014_1_ ? 0 : 86;
        int i = (field_191904_o - 147) / 2 - field_191903_n;
        int j = (field_191905_p - 166) / 2;
        field_193965_u.func_194119_a();
        Minecraft.player.inventory.func_194016_a(field_193965_u, false);
        p_193014_2_.func_194018_a(field_193965_u);
        field_193962_q = new GuiTextField(0, Minecraft.font, i + 25, j + 14, 80, Minecraft.font.height + 5);
        field_193962_q.setMaxStringLength(50);
        field_193962_q.setEnableBackgroundDrawing(false);
        field_193962_q.setVisible(true);
        field_193962_q.setTextColor(16777215);
        field_193022_s.func_194194_a(field_191888_F, i, j);
        field_193022_s.func_193732_a(this);
        field_193960_m = new GuiButtonToggle(0, i + 110, j + 12, 26, 16, field_193964_s.func_192815_c());
        field_193960_m.func_191751_a(152, 41, 28, 18, field_191894_a);
        func_193003_g(false);
        func_193949_f();
    }

    public void func_191871_c()
    {
        Keyboard.enableRepeatEvents(false);
    }

    public int func_193011_a(boolean p_193011_1_, int p_193011_2_, int p_193011_3_)
    {
        int i;

        if (func_191878_b() && !p_193011_1_)
        {
            i = 177 + (p_193011_2_ - p_193011_3_ - 200) / 2;
        }
        else
        {
            i = (p_193011_2_ - p_193011_3_) / 2;
        }

        return i;
    }

    public void func_191866_a()
    {
        func_193006_a(!func_191878_b());
    }

    public boolean func_191878_b()
    {
        return field_193964_s.func_192812_b();
    }

    private void func_193006_a(boolean p_193006_1_)
    {
        field_193964_s.func_192813_a(p_193006_1_);

        if (!p_193006_1_)
        {
            field_193022_s.func_194200_c();
        }

        func_193956_j();
    }

    public void func_191874_a(@Nullable Slot p_191874_1_)
    {
        if (p_191874_1_ != null && p_191874_1_.slotNumber <= 9)
        {
            field_191915_z.func_192682_a();

            if (func_191878_b())
            {
                func_193942_g();
            }
        }
    }

    private void func_193003_g(boolean p_193003_1_)
    {
        List<RecipeList> list = RecipeBookClient.field_194086_e.get(field_191913_x.func_191764_e());
        list.forEach((p_193944_1_) ->
        {
            p_193944_1_.func_194210_a(field_193965_u, field_193961_o.getWidth(), field_193961_o.getHeight(), field_193964_s);
        });
        List<RecipeList> list1 = Lists.newArrayList(list);
        list1.removeIf((p_193952_0_) ->
        {
            return !p_193952_0_.func_194209_a();
        });
        list1.removeIf((p_193953_0_) ->
        {
            return !p_193953_0_.func_194212_c();
        });
        String s = field_193962_q.getText();
        if (field_193964_s.func_192815_c())
        {
            list1.removeIf((p_193958_0_) ->
            {
                return !p_193958_0_.func_192708_c();
            });
        }

        field_193022_s.func_194192_a(list1, p_193003_1_);
    }

    private void func_193949_f()
    {
        int i = (field_191904_o - 147) / 2 - field_191903_n - 30;
        int j = (field_191905_p - 166) / 2 + 3;
        int k = 27;
        int l = 0;

        for (GuiButtonRecipeTab guibuttonrecipetab : field_193018_j)
        {
            CreativeTabs creativetabs = guibuttonrecipetab.func_191764_e();

            if (creativetabs == CreativeTabs.SEARCH)
            {
                guibuttonrecipetab.visible = true;
                guibuttonrecipetab.func_191752_c(i, j + 27 * l++);
            }
            else if (guibuttonrecipetab.func_193919_e())
            {
                guibuttonrecipetab.func_191752_c(i, j + 27 * l++);
                guibuttonrecipetab.func_193918_a(field_191888_F);
            }
        }
    }

    public void func_193957_d()
    {
        if (func_191878_b())
        {
            if (field_193966_v != Minecraft.player.inventory.func_194015_p())
            {
                func_193942_g();
                field_193966_v = Minecraft.player.inventory.func_194015_p();
            }
        }
    }

    private void func_193942_g()
    {
        field_193965_u.func_194119_a();
        Minecraft.player.inventory.func_194016_a(field_193965_u, false);
        field_193961_o.func_194018_a(field_193965_u);
        func_193003_g(false);
    }

    public void func_191861_a(int p_191861_1_, int p_191861_2_, float p_191861_3_)
    {
        if (func_191878_b())
        {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 100.0F);
            Minecraft.getTextureManager().bindTexture(field_191894_a);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            int i = (field_191904_o - 147) / 2 - field_191903_n;
            int j = (field_191905_p - 166) / 2;
            drawTexturedModalRect(i, j, 1, 1, 147, 166);
            field_193962_q.drawTextBox();
            RenderHelper.disableStandardItemLighting();

            for (GuiButtonRecipeTab guibuttonrecipetab : field_193018_j)
            {
                guibuttonrecipetab.draw(field_191888_F, p_191861_1_, p_191861_2_, p_191861_3_);
            }

            field_193960_m.draw(field_191888_F, p_191861_1_, p_191861_2_, p_191861_3_);
            field_193022_s.func_194191_a(i, j, p_191861_1_, p_191861_2_, p_191861_3_);
            GlStateManager.popMatrix();
        }
    }

    public void func_191876_c(int p_191876_1_, int p_191876_2_, int p_191876_3_, int p_191876_4_)
    {
        if (func_191878_b())
        {
            field_193022_s.func_193721_a(p_191876_3_, p_191876_4_);

            if (field_193960_m.isMouseOver())
            {
                String s1 = I18n.format(field_193960_m.func_191754_c() ? "gui.recipebook.toggleRecipes.craftable" : "gui.recipebook.toggleRecipes.all");

                if (Minecraft.screen != null)
                {
                    Minecraft.screen.drawTooltip(s1, p_191876_3_, p_191876_4_);
                }
            }

            func_193015_d(p_191876_1_, p_191876_2_, p_191876_3_, p_191876_4_);
        }
    }

    private void func_193015_d(int p_193015_1_, int p_193015_2_, int p_193015_3_, int p_193015_4_)
    {
        ItemStack itemstack = null;

        for (int i = 0; i < field_191915_z.func_192684_b(); ++i)
        {
            GhostRecipe.GhostIngredient ghostrecipe$ghostingredient = field_191915_z.func_192681_a(i);
            int j = ghostrecipe$ghostingredient.func_193713_b() + p_193015_1_;
            int k = ghostrecipe$ghostingredient.func_193712_c() + p_193015_2_;

            if (p_193015_3_ >= j && p_193015_4_ >= k && p_193015_3_ < j + 16 && p_193015_4_ < k + 16)
            {
                itemstack = ghostrecipe$ghostingredient.func_194184_c();
            }
        }

        if (itemstack != null && Minecraft.screen != null)
        {
            Minecraft.screen.drawTooltip(Minecraft.screen.getTooltip(itemstack), p_193015_3_, p_193015_4_);
        }
    }

    public void func_191864_a(int p_191864_1_, int p_191864_2_, boolean p_191864_3_, float p_191864_4_)
    {
        field_191915_z.func_194188_a(field_191888_F, p_191864_1_, p_191864_2_, p_191864_3_, p_191864_4_);
    }

    public boolean func_191862_a(int p_191862_1_, int p_191862_2_, int p_191862_3_)
    {
        if (func_191878_b() && !Minecraft.player.isSpectator())
        {
            if (field_193022_s.func_194196_a(p_191862_1_, p_191862_2_, p_191862_3_, (field_191904_o - 147) / 2 - field_191903_n, (field_191905_p - 166) / 2, 147, 166))
            {
                IRecipe irecipe = field_193022_s.func_194193_a();
                RecipeList recipelist = field_193022_s.func_194199_b();

                if (irecipe != null && recipelist != null)
                {
                    if (!recipelist.func_194213_a(irecipe) && field_191915_z.func_192686_c() == irecipe)
                    {
                        return false;
                    }

                    field_191915_z.func_192682_a();
                    Minecraft.playerController.func_194338_a(Minecraft.player.openContainer.windowId, irecipe, Screen.hasShiftDown(), Minecraft.player);

                    if (!func_191880_f() && p_191862_3_ == 0)
                    {
                        func_193006_a(false);
                    }
                }

                return true;
            }
            else if (p_191862_3_ != 0)
            {
                return false;
            }
            else if (field_193962_q.mouseClicked(p_191862_1_, p_191862_2_, p_191862_3_))
            {
                return true;
            }
            else if (field_193960_m.mousePressed(field_191888_F, p_191862_1_, p_191862_2_))
            {
                boolean flag = !field_193964_s.func_192815_c();
                field_193964_s.func_192810_b(flag);
                field_193960_m.func_191753_b(flag);
                field_193960_m.playPressSound(Minecraft.getSoundHandler());
                func_193956_j();
                func_193003_g(false);
                return true;
            }
            else
            {
                for (GuiButtonRecipeTab guibuttonrecipetab : field_193018_j)
                {
                    if (guibuttonrecipetab.mousePressed(field_191888_F, p_191862_1_, p_191862_2_))
                    {
                        if (field_191913_x != guibuttonrecipetab)
                        {
                            guibuttonrecipetab.playPressSound(Minecraft.getSoundHandler());
                            field_191913_x.func_191753_b(false);
                            field_191913_x = guibuttonrecipetab;
                            field_191913_x.func_191753_b(true);
                            func_193003_g(true);
                        }

                        return true;
                    }
                }

                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean func_193955_c(int p_193955_1_, int p_193955_2_, int p_193955_3_, int p_193955_4_, int p_193955_5_, int p_193955_6_)
    {
        if (!func_191878_b())
        {
            return true;
        }
        else
        {
            boolean flag = p_193955_1_ < p_193955_3_ || p_193955_2_ < p_193955_4_ || p_193955_1_ >= p_193955_3_ + p_193955_5_ || p_193955_2_ >= p_193955_4_ + p_193955_6_;
            boolean flag1 = p_193955_3_ - 147 < p_193955_1_ && p_193955_1_ < p_193955_3_ && p_193955_4_ < p_193955_2_ && p_193955_2_ < p_193955_4_ + p_193955_6_;
            return flag && !flag1 && !field_191913_x.mousePressed(field_191888_F, p_193955_1_, p_193955_2_);
        }
    }

    public boolean func_191859_a(char p_191859_1_, int p_191859_2_)
    {
        if (func_191878_b() && !Minecraft.player.isSpectator())
        {
            if (p_191859_2_ == 1 && !func_191880_f())
            {
                func_193006_a(false);
                return true;
            }
            else
            {
                if (GameSettings.isKeyDown(Minecraft.gameSettings.keyBindChat) && !field_193962_q.isFocused())
                {
                    field_193962_q.setFocused(true);
                }
                else if (field_193962_q.textboxKeyTyped(p_191859_1_, p_191859_2_))
                {
                    String s1 = field_193962_q.getText().toLowerCase(Locale.ROOT);
                    func_193716_a(s1);

                    if (!s1.equals(field_193963_r))
                    {
                        func_193003_g(false);
                        field_193963_r = s1;
                    }

                    return true;
                }

                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private void func_193716_a(String p_193716_1_)
    {
        if ("excitedze".equals(p_193716_1_))
        {
            LanguageManager languagemanager = Minecraft.getLanguageManager();
            Language language = languagemanager.func_191960_a("en_pt");

            if (languagemanager.getCurrentLanguage().compareTo(language) == 0)
            {
                return;
            }

            languagemanager.setCurrentLanguage(language);
            Minecraft.gameSettings.language = language.getCode();
            Minecraft.refreshResources();
            Minecraft.font.setUnicodeFlag(Minecraft.getLanguageManager().isCurrentLocaleUnicode() || Minecraft.gameSettings.forceUnicodeFont);
            Minecraft.font.setBidiFlag(languagemanager.isCurrentLanguageBidirectional());
            Minecraft.gameSettings.saveOptions();
        }
    }

    private boolean func_191880_f()
    {
        return field_191903_n == 86;
    }

    public void func_193948_e()
    {
        func_193949_f();

        if (func_191878_b())
        {
            func_193003_g(false);
        }
    }

    public void func_193001_a(List<IRecipe> p_193001_1_)
    {
        for (IRecipe irecipe : p_193001_1_)
        {
            Minecraft.player.func_193103_a(irecipe);
        }
    }

    public void func_193951_a(IRecipe p_193951_1_, List<Slot> p_193951_2_)
    {
        ItemStack itemstack = p_193951_1_.getRecipeOutput();
        field_191915_z.func_192685_a(p_193951_1_);
        field_191915_z.func_194187_a(Ingredient.func_193369_a(itemstack), (p_193951_2_.get(0)).xDisplayPosition, (p_193951_2_.get(0)).yDisplayPosition);
        int i = field_193961_o.getWidth();
        int j = field_193961_o.getHeight();
        int k = p_193951_1_ instanceof ShapedRecipes ? ((ShapedRecipes)p_193951_1_).func_192403_f() : i;
        int l = 1;
        Iterator<Ingredient> iterator = p_193951_1_.func_192400_c().iterator();

        for (int i1 = 0; i1 < j; ++i1)
        {
            for (int j1 = 0; j1 < k; ++j1)
            {
                if (!iterator.hasNext())
                {
                    return;
                }

                Ingredient ingredient = iterator.next();

                if (ingredient != Ingredient.field_193370_a)
                {
                    Slot slot = p_193951_2_.get(l);
                    field_191915_z.func_194187_a(ingredient, slot.xDisplayPosition, slot.yDisplayPosition);
                }

                ++l;
            }

            if (k < i)
            {
                l += i - k;
            }
        }
    }

    private void func_193956_j()
    {
        if (field_191888_F.getConnection() != null)
        {
            field_191888_F.getConnection().sendPacket(new CPacketRecipeInfo(func_191878_b(), field_193964_s.func_192815_c()));
        }
    }
}
