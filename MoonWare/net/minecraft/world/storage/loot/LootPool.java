package net.minecraft.world.storage.loot;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;

public class LootPool
{
    private final LootEntry[] lootEntries;
    private final LootCondition[] poolConditions;
    private final RandomValueRange rolls;
    private final RandomValueRange bonusRolls;

    public LootPool(LootEntry[] lootEntriesIn, LootCondition[] poolConditionsIn, RandomValueRange rollsIn, RandomValueRange bonusRollsIn)
    {
        lootEntries = lootEntriesIn;
        poolConditions = poolConditionsIn;
        rolls = rollsIn;
        bonusRolls = bonusRollsIn;
    }

    /**
     * generates the contents for a single roll.
     * The first for loop calculates the sum of all the lootentries
     * and the second for loop adds a random item
     * with items with higher weights being more probable.
     */
    protected void createLootRoll(Collection<ItemStack> stacks, Random rand, LootContext context)
    {
        List<LootEntry> list = Lists.newArrayList();
        int i = 0;

        for (LootEntry lootentry : lootEntries)
        {
            if (LootConditionManager.testAllConditions(lootentry.conditions, rand, context))
            {
                int j = lootentry.getEffectiveWeight(context.getLuck());

                if (j > 0)
                {
                    list.add(lootentry);
                    i += j;
                }
            }
        }

        if (i != 0 && !list.isEmpty())
        {
            int k = rand.nextInt(i);

            for (LootEntry lootentry1 : list)
            {
                k -= lootentry1.getEffectiveWeight(context.getLuck());

                if (k < 0)
                {
                    lootentry1.addLoot(stacks, rand, context);
                    return;
                }
            }
        }
    }

    /**
     * generates loot and puts it in an inventory
     */
    public void generateLoot(Collection<ItemStack> stacks, Random rand, LootContext context)
    {
        if (LootConditionManager.testAllConditions(poolConditions, rand, context))
        {
            int i = rolls.generateInt(rand) + MathHelper.floor(bonusRolls.generateFloat(rand) * context.getLuck());

            for (int j = 0; j < i; ++j)
            {
                createLootRoll(stacks, rand, context);
            }
        }
    }

    public static class Serializer implements JsonDeserializer<LootPool>, JsonSerializer<LootPool>
    {
        public LootPool deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "loot pool");
            LootEntry[] alootentry = JsonUtils.deserializeClass(jsonobject, "entries", p_deserialize_3_, LootEntry[].class);
            LootCondition[] alootcondition = JsonUtils.deserializeClass(jsonobject, "conditions", new LootCondition[0], p_deserialize_3_, LootCondition[].class);
            RandomValueRange randomvaluerange = JsonUtils.deserializeClass(jsonobject, "rolls", p_deserialize_3_, RandomValueRange.class);
            RandomValueRange randomvaluerange1 = JsonUtils.deserializeClass(jsonobject, "bonus_rolls", new RandomValueRange(0.0F, 0.0F), p_deserialize_3_, RandomValueRange.class);
            return new LootPool(alootentry, alootcondition, randomvaluerange, randomvaluerange1);
        }

        public JsonElement serialize(LootPool p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            JsonObject jsonobject = new JsonObject();
            jsonobject.add("entries", p_serialize_3_.serialize(p_serialize_1_.lootEntries));
            jsonobject.add("rolls", p_serialize_3_.serialize(p_serialize_1_.rolls));

            if (p_serialize_1_.bonusRolls.getMin() != 0.0F && p_serialize_1_.bonusRolls.getMax() != 0.0F)
            {
                jsonobject.add("bonus_rolls", p_serialize_3_.serialize(p_serialize_1_.bonusRolls));
            }

            if (p_serialize_1_.poolConditions.length > 0)
            {
                jsonobject.add("conditions", p_serialize_3_.serialize(p_serialize_1_.poolConditions));
            }

            return jsonobject;
        }
    }
}
