package net.minecraft.client.gui.toasts;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import java.util.List;

public class RecipeToast implements Toast {
    private final List<ItemStack> items = Lists.newArrayList();
    private long additionalTime;
    private boolean changeTime;

    public RecipeToast(ItemStack stack) {
        items.add(stack);
    }

    @Override
    public Toast.Visibility draw(long delta) {
        if (changeTime) {
            additionalTime = delta;
            changeTime = false;
        }
        if (items.isEmpty()) return Toast.Visibility.HIDE;
        Minecraft.getTextureManager().bindTexture(Toast.TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        Gui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        Minecraft.font.drawString(I18n.format("recipe.toast.title"), 30, 7, -11534256);
        Minecraft.font.drawString(I18n.format("recipe.toast.description"), 30, 18, -16777216);
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getRenderItem().renderItemAndEffectIntoGUI(null, items.get((int) (delta / (5000L / (long) items.size()) % (long) items.size())), 8, 8);
        return delta - additionalTime >= 5000L ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }

    public void addItem(ItemStack stack) {
        items.add(stack);
        changeTime = true;
    }

    public static void addOrUpdate(IRecipe recipe) {
        RecipeToast recipetoast = ToastHud.getToast(RecipeToast.class, Toast.field_193655_b);
        if (recipetoast == null) {
            ToastHud.queue(new RecipeToast(recipe.getRecipeOutput()));
        } else {
            recipetoast.addItem(recipe.getRecipeOutput());
        }
    }
}
