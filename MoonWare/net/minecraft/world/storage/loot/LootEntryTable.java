package net.minecraft.world.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Collection;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import net.minecraft.world.storage.loot.conditions.LootCondition;

public class LootEntryTable extends LootEntry
{
    protected final Namespaced table;

    public LootEntryTable(Namespaced tableIn, int weightIn, int qualityIn, LootCondition[] conditionsIn)
    {
        super(weightIn, qualityIn, conditionsIn);
        table = tableIn;
    }

    public void addLoot(Collection<ItemStack> stacks, Random rand, LootContext context)
    {
        LootTable loottable = context.getLootTableManager().getLootTableFromLocation(table);
        Collection<ItemStack> collection = loottable.generateLootForPools(rand, context);
        stacks.addAll(collection);
    }

    protected void serialize(JsonObject json, JsonSerializationContext context)
    {
        json.addProperty("name", table.toString());
    }

    public static LootEntryTable deserialize(JsonObject object, JsonDeserializationContext deserializationContext, int weightIn, int qualityIn, LootCondition[] conditionsIn)
    {
        Namespaced resourcelocation = new Namespaced(JsonUtils.getString(object, "name"));
        return new LootEntryTable(resourcelocation, weightIn, qualityIn, conditionsIn);
    }
}
