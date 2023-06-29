package net.minecraft.world.storage.loot;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.Namespaced;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LootTableManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(RandomValueRange.class, new RandomValueRange.Serializer()).registerTypeAdapter(LootPool.class, new LootPool.Serializer()).registerTypeAdapter(LootTable.class, new LootTable.Serializer()).registerTypeHierarchyAdapter(LootEntry.class, new LootEntry.Serializer()).registerTypeHierarchyAdapter(LootFunction.class, new LootFunctionManager.Serializer()).registerTypeHierarchyAdapter(LootCondition.class, new LootConditionManager.Serializer()).registerTypeHierarchyAdapter(LootContext.EntityTarget.class, new LootContext.EntityTarget.Serializer()).create();
    private final LoadingCache<Namespaced, LootTable> registeredLootTables = CacheBuilder.newBuilder().build(new LootTableManager.Loader());
    private final File baseFolder;

    public LootTableManager(@Nullable File folder)
    {
        baseFolder = folder;
        reloadLootTables();
    }

    public LootTable getLootTableFromLocation(Namespaced ressources)
    {
        return registeredLootTables.getUnchecked(ressources);
    }

    public void reloadLootTables()
    {
        registeredLootTables.invalidateAll();

        for (Namespaced resourcelocation : LootTableList.getAll())
        {
            getLootTableFromLocation(resourcelocation);
        }
    }

    class Loader extends CacheLoader<Namespaced, LootTable>
    {
        private Loader()
        {
        }

        public LootTable load(Namespaced p_load_1_) throws Exception
        {
            if (p_load_1_.getPath().contains("."))
            {
                LOGGER.debug("Invalid loot table name '{}' (can't contain periods)", p_load_1_);
                return LootTable.EMPTY_LOOT_TABLE;
            }
            else
            {
                LootTable loottable = loadLootTable(p_load_1_);

                if (loottable == null)
                {
                    loottable = loadBuiltinLootTable(p_load_1_);
                }

                if (loottable == null)
                {
                    loottable = LootTable.EMPTY_LOOT_TABLE;
                    LOGGER.warn("Couldn't find resource table {}", p_load_1_);
                }

                return loottable;
            }
        }

        @Nullable
        private LootTable loadLootTable(Namespaced resource)
        {
            if (baseFolder == null)
            {
                return null;
            }
            else
            {
                File file1 = new File(new File(baseFolder, resource.getNamespace()), resource.getPath() + ".json");

                if (file1.exists())
                {
                    if (file1.isFile())
                    {
                        String s;

                        try
                        {
                            s = Files.toString(file1, StandardCharsets.UTF_8);
                        }
                        catch (IOException ioexception)
                        {
                            LOGGER.warn("Couldn't load loot table {} from {}", resource, file1, ioexception);
                            return LootTable.EMPTY_LOOT_TABLE;
                        }

                        try
                        {
                            return JsonUtils.gsonDeserialize(GSON_INSTANCE, s, LootTable.class);
                        }
                        catch (IllegalArgumentException | JsonParseException jsonparseexception)
                        {
                            LOGGER.error("Couldn't load loot table {} from {}", resource, file1, jsonparseexception);
                            return LootTable.EMPTY_LOOT_TABLE;
                        }
                    }
                    else
                    {
                        LOGGER.warn("Expected to find loot table {} at {} but it was a folder.", resource, file1);
                        return LootTable.EMPTY_LOOT_TABLE;
                    }
                }
                else
                {
                    return null;
                }
            }
        }

        @Nullable
        private LootTable loadBuiltinLootTable(Namespaced resource)
        {
            URL url = LootTableManager.class.getResource("/assets/" + resource.getNamespace() + "/loot_tables/" + resource.getPath() + ".json");

            if (url != null)
            {
                String s;

                try
                {
                    s = Resources.toString(url, StandardCharsets.UTF_8);
                }
                catch (IOException ioexception)
                {
                    LOGGER.warn("Couldn't load loot table {} from {}", resource, url, ioexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }

                try
                {
                    return JsonUtils.gsonDeserialize(GSON_INSTANCE, s, LootTable.class);
                }
                catch (JsonParseException jsonparseexception)
                {
                    LOGGER.error("Couldn't load loot table {} from {}", resource, url, jsonparseexception);
                    return LootTable.EMPTY_LOOT_TABLE;
                }
            }
            else
            {
                return null;
            }
        }
    }
}
