package net.minecraft.stats;

import com.google.common.collect.Lists;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketRecipeBook;
import net.minecraft.util.Namespaced;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;

public class RecipeBookServer extends RecipeBook
{
    private static final Logger field_192828_d = LogManager.getLogger();

    public void func_193835_a(List<IRecipe> p_193835_1_, EntityPlayerMP p_193835_2_)
    {
        List<IRecipe> list = Lists.newArrayList();

        for (IRecipe irecipe : p_193835_1_)
        {
            if (!field_194077_a.get(RecipeBook.func_194075_d(irecipe)) && !irecipe.func_192399_d())
            {
                func_194073_a(irecipe);
                func_193825_e(irecipe);
                list.add(irecipe);
                CriteriaTriggers.field_192126_f.func_192225_a(p_193835_2_, irecipe);
            }
        }

        func_194081_a(SPacketRecipeBook.State.ADD, p_193835_2_, list);
    }

    public void func_193834_b(List<IRecipe> p_193834_1_, EntityPlayerMP p_193834_2_)
    {
        List<IRecipe> list = Lists.newArrayList();

        for (IRecipe irecipe : p_193834_1_)
        {
            if (field_194077_a.get(RecipeBook.func_194075_d(irecipe)))
            {
                func_193831_b(irecipe);
                list.add(irecipe);
            }
        }

        func_194081_a(SPacketRecipeBook.State.REMOVE, p_193834_2_, list);
    }

    private void func_194081_a(SPacketRecipeBook.State p_194081_1_, EntityPlayerMP p_194081_2_, List<IRecipe> p_194081_3_)
    {
        p_194081_2_.connection.sendPacket(new SPacketRecipeBook(p_194081_1_, p_194081_3_, Collections.emptyList(), field_192818_b, field_192819_c));
    }

    public NBTTagCompound func_192824_e()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setBoolean("isGuiOpen", field_192818_b);
        nbttagcompound.setBoolean("isFilteringCraftable", field_192819_c);
        NBTTagList nbttaglist = new NBTTagList();

        for (IRecipe irecipe : func_194079_d())
        {
            nbttaglist.appendTag(new NBTTagString(CraftingManager.field_193380_a.getNameForObject(irecipe).toString()));
        }

        nbttagcompound.setTag("recipes", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (IRecipe irecipe1 : func_194080_e())
        {
            nbttaglist1.appendTag(new NBTTagString(CraftingManager.field_193380_a.getNameForObject(irecipe1).toString()));
        }

        nbttagcompound.setTag("toBeDisplayed", nbttaglist1);
        return nbttagcompound;
    }

    public void func_192825_a(NBTTagCompound p_192825_1_)
    {
        field_192818_b = p_192825_1_.getBoolean("isGuiOpen");
        field_192819_c = p_192825_1_.getBoolean("isFilteringCraftable");
        NBTTagList nbttaglist = p_192825_1_.getTagList("recipes", 8);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            Namespaced resourcelocation = new Namespaced(nbttaglist.getStringTagAt(i));
            IRecipe irecipe = CraftingManager.func_193373_a(resourcelocation);

            if (irecipe == null)
            {
                field_192828_d.info("Tried to load unrecognized recipe: {} removed now.", resourcelocation);
            }
            else
            {
                func_194073_a(irecipe);
            }
        }

        NBTTagList nbttaglist1 = p_192825_1_.getTagList("toBeDisplayed", 8);

        for (int j = 0; j < nbttaglist1.tagCount(); ++j)
        {
            Namespaced resourcelocation1 = new Namespaced(nbttaglist1.getStringTagAt(j));
            IRecipe irecipe1 = CraftingManager.func_193373_a(resourcelocation1);

            if (irecipe1 == null)
            {
                field_192828_d.info("Tried to load unrecognized recipe: {} removed now.", resourcelocation1);
            }
            else
            {
                func_193825_e(irecipe1);
            }
        }
    }

    private List<IRecipe> func_194079_d()
    {
        List<IRecipe> list = Lists.newArrayList();

        for (int i = field_194077_a.nextSetBit(0); i >= 0; i = field_194077_a.nextSetBit(i + 1))
        {
            list.add(CraftingManager.field_193380_a.getObjectById(i));
        }

        return list;
    }

    private List<IRecipe> func_194080_e()
    {
        List<IRecipe> list = Lists.newArrayList();

        for (int i = field_194078_b.nextSetBit(0); i >= 0; i = field_194078_b.nextSetBit(i + 1))
        {
            list.add(CraftingManager.field_193380_a.getObjectById(i));
        }

        return list;
    }

    public void func_192826_c(EntityPlayerMP p_192826_1_)
    {
        p_192826_1_.connection.sendPacket(new SPacketRecipeBook(SPacketRecipeBook.State.INIT, func_194079_d(), func_194080_e(), field_192818_b, field_192819_c));
    }
}
