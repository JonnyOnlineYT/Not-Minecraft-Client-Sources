package net.minecraft.util;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class ServerRecipeBookHelper
{
    private final Logger field_194330_a = LogManager.getLogger();
    private final RecipeItemHelper field_194331_b = new RecipeItemHelper();
    private EntityPlayerMP field_194332_c;
    private IRecipe field_194333_d;
    private boolean field_194334_e;
    private InventoryCraftResult field_194335_f;
    private InventoryCrafting field_194336_g;
    private List<Slot> field_194337_h;

    public void func_194327_a(EntityPlayerMP p_194327_1_, @Nullable IRecipe p_194327_2_, boolean p_194327_3_)
    {
        if (p_194327_2_ != null && p_194327_1_.func_192037_E().func_193830_f(p_194327_2_))
        {
            field_194332_c = p_194327_1_;
            field_194333_d = p_194327_2_;
            field_194334_e = p_194327_3_;
            field_194337_h = p_194327_1_.openContainer.inventorySlots;
            Container container = p_194327_1_.openContainer;
            field_194335_f = null;
            field_194336_g = null;

            if (container instanceof ContainerWorkbench)
            {
                field_194335_f = ((ContainerWorkbench)container).craftResult;
                field_194336_g = ((ContainerWorkbench)container).craftMatrix;
            }
            else if (container instanceof ContainerPlayer)
            {
                field_194335_f = ((ContainerPlayer)container).craftResult;
                field_194336_g = ((ContainerPlayer)container).craftMatrix;
            }

            if (field_194335_f != null && field_194336_g != null)
            {
                if (func_194328_c() || p_194327_1_.isCreative())
                {
                    field_194331_b.func_194119_a();
                    p_194327_1_.inventory.func_194016_a(field_194331_b, false);
                    field_194336_g.func_194018_a(field_194331_b);

                    if (field_194331_b.func_194116_a(p_194327_2_, null))
                    {
                        func_194329_b();
                    }
                    else
                    {
                        func_194326_a();
                        p_194327_1_.connection.sendPacket(new SPacketPlaceGhostRecipe(p_194327_1_.openContainer.windowId, p_194327_2_));
                    }

                    p_194327_1_.inventory.markDirty();
                }
            }
        }
    }

    private void func_194326_a()
    {
        InventoryPlayer inventoryplayer = field_194332_c.inventory;

        for (int i = 0; i < field_194336_g.getSizeInventory(); ++i)
        {
            ItemStack itemstack = field_194336_g.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                while (itemstack.getCount() > 0)
                {
                    int j = inventoryplayer.storeItemStack(itemstack);

                    if (j == -1)
                    {
                        j = inventoryplayer.getFirstEmptyStack();
                    }

                    ItemStack itemstack1 = itemstack.copy();
                    itemstack1.func_190920_e(1);
                    inventoryplayer.func_191971_c(j, itemstack1);
                    field_194336_g.decrStackSize(i, 1);
                }
            }
        }

        field_194336_g.clear();
        field_194335_f.clear();
    }

    private void func_194329_b()
    {
        boolean flag = field_194333_d.matches(field_194336_g, field_194332_c.world);
        int i = field_194331_b.func_194114_b(field_194333_d, null);

        if (flag)
        {
            boolean flag1 = true;

            for (int j = 0; j < field_194336_g.getSizeInventory(); ++j)
            {
                ItemStack itemstack = field_194336_g.getStackInSlot(j);

                if (!itemstack.isEmpty() && Math.min(i, itemstack.getMaxStackSize()) > itemstack.getCount())
                {
                    flag1 = false;
                }
            }

            if (flag1)
            {
                return;
            }
        }

        int i1 = func_194324_a(i, flag);
        IntList intlist = new IntArrayList();

        if (field_194331_b.func_194118_a(field_194333_d, intlist, i1))
        {
            int j1 = i1;
            IntListIterator intlistiterator = intlist.iterator();

            while (intlistiterator.hasNext())
            {
                int k = intlistiterator.next().intValue();
                int l = RecipeItemHelper.func_194115_b(k).getMaxStackSize();

                if (l < j1)
                {
                    j1 = l;
                }
            }

            if (field_194331_b.func_194118_a(field_194333_d, intlist, j1))
            {
                func_194326_a();
                func_194323_a(j1, intlist);
            }
        }
    }

    private int func_194324_a(int p_194324_1_, boolean p_194324_2_)
    {
        int i = 1;

        if (field_194334_e)
        {
            i = p_194324_1_;
        }
        else if (p_194324_2_)
        {
            i = 64;

            for (int j = 0; j < field_194336_g.getSizeInventory(); ++j)
            {
                ItemStack itemstack = field_194336_g.getStackInSlot(j);

                if (!itemstack.isEmpty() && i > itemstack.getCount())
                {
                    i = itemstack.getCount();
                }
            }

            if (i < 64)
            {
                ++i;
            }
        }

        return i;
    }

    private void func_194323_a(int p_194323_1_, IntList p_194323_2_)
    {
        int i = field_194336_g.getWidth();
        int j = field_194336_g.getHeight();

        if (field_194333_d instanceof ShapedRecipes)
        {
            ShapedRecipes shapedrecipes = (ShapedRecipes) field_194333_d;
            i = shapedrecipes.func_192403_f();
            j = shapedrecipes.func_192404_g();
        }

        int j1 = 1;
        Iterator<Integer> iterator = p_194323_2_.iterator();

        for (int k = 0; k < field_194336_g.getWidth() && j != k; ++k)
        {
            for (int l = 0; l < field_194336_g.getHeight(); ++l)
            {
                if (i == l || !iterator.hasNext())
                {
                    j1 += field_194336_g.getWidth() - l;
                    break;
                }

                Slot slot = field_194337_h.get(j1);
                ItemStack itemstack = RecipeItemHelper.func_194115_b(iterator.next().intValue());

                if (itemstack.isEmpty())
                {
                    ++j1;
                }
                else
                {
                    for (int i1 = 0; i1 < p_194323_1_; ++i1)
                    {
                        func_194325_a(slot, itemstack);
                    }

                    ++j1;
                }
            }

            if (!iterator.hasNext())
            {
                break;
            }
        }
    }

    private void func_194325_a(Slot p_194325_1_, ItemStack p_194325_2_)
    {
        InventoryPlayer inventoryplayer = field_194332_c.inventory;
        int i = inventoryplayer.func_194014_c(p_194325_2_);

        if (i != -1)
        {
            ItemStack itemstack = inventoryplayer.getStackInSlot(i).copy();

            if (!itemstack.isEmpty())
            {
                if (itemstack.getCount() > 1)
                {
                    inventoryplayer.decrStackSize(i, 1);
                }
                else
                {
                    inventoryplayer.removeStackFromSlot(i);
                }

                itemstack.func_190920_e(1);

                if (p_194325_1_.getStack().isEmpty())
                {
                    p_194325_1_.putStack(itemstack);
                }
                else
                {
                    p_194325_1_.getStack().func_190917_f(1);
                }
            }
        }
    }

    private boolean func_194328_c()
    {
        InventoryPlayer inventoryplayer = field_194332_c.inventory;

        for (int i = 0; i < field_194336_g.getSizeInventory(); ++i)
        {
            ItemStack itemstack = field_194336_g.getStackInSlot(i);

            if (!itemstack.isEmpty())
            {
                int j = inventoryplayer.storeItemStack(itemstack);

                if (j == -1)
                {
                    j = inventoryplayer.getFirstEmptyStack();
                }

                if (j == -1)
                {
                    return false;
                }
            }
        }

        return true;
    }
}
