package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.block.model.VariantList;

public class Multipart
{
    private final List<Selector> selectors;
    private BlockStateContainer stateContainer;

    public Multipart(List<Selector> selectorsIn)
    {
        selectors = selectorsIn;
    }

    public List<Selector> getSelectors()
    {
        return selectors;
    }

    public Set<VariantList> getVariants()
    {
        Set<VariantList> set = Sets.newHashSet();

        for (Selector selector : selectors)
        {
            set.add(selector.getVariantList());
        }

        return set;
    }

    public void setStateContainer(BlockStateContainer stateContainerIn)
    {
        stateContainer = stateContainerIn;
    }

    public BlockStateContainer getStateContainer()
    {
        return stateContainer;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            if (p_equals_1_ instanceof Multipart)
            {
                Multipart multipart = (Multipart)p_equals_1_;

                if (selectors.equals(multipart.selectors))
                {
                    if (stateContainer == null)
                    {
                        return multipart.stateContainer == null;
                    }

                    return stateContainer.equals(multipart.stateContainer);
                }
            }

            return false;
        }
    }

    public int hashCode()
    {
        return 31 * selectors.hashCode() + (stateContainer == null ? 0 : stateContainer.hashCode());
    }

    public static class Deserializer implements JsonDeserializer<Multipart>
    {
        public Multipart deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
        {
            return new Multipart(getSelectors(p_deserialize_3_, p_deserialize_1_.getAsJsonArray()));
        }

        private List<Selector> getSelectors(JsonDeserializationContext context, JsonArray elements)
        {
            List<Selector> list = Lists.newArrayList();

            for (JsonElement jsonelement : elements)
            {
                list.add(context.deserialize(jsonelement, Selector.class));
            }

            return list;
        }
    }
}
