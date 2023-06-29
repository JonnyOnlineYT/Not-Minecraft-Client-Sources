package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelNamespaced;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemModelMesher
{
    private final Map<Integer, ModelNamespaced> simpleShapes = Maps.newHashMap();
    private final Map<Integer, IBakedModel> simpleShapesCache = Maps.newHashMap();
    private final Map<Item, ItemMeshDefinition> shapers = Maps.newHashMap();
    private final ModelManager modelManager;

    public ItemModelMesher(ModelManager modelManager)
    {
        this.modelManager = modelManager;
    }

    public TextureAtlasSprite getParticleIcon(Item item)
    {
        return getParticleIcon(item, 0);
    }

    public TextureAtlasSprite getParticleIcon(Item item, int meta)
    {
        return getItemModel(new ItemStack(item, 1, meta)).getParticleTexture();
    }

    public IBakedModel getItemModel(ItemStack stack)
    {
        Item item = stack.getItem();
        IBakedModel ibakedmodel = getItemModel(item, getMetadata(stack));

        if (ibakedmodel == null)
        {
            ItemMeshDefinition itemmeshdefinition = shapers.get(item);

            if (itemmeshdefinition != null)
            {
                ibakedmodel = modelManager.getModel(itemmeshdefinition.getModelLocation(stack));
            }
        }

        if (ibakedmodel == null)
        {
            ibakedmodel = modelManager.getMissingModel();
        }

        return ibakedmodel;
    }

    protected int getMetadata(ItemStack stack)
    {
        return stack.getMaxDamage() > 0 ? 0 : stack.getMetadata();
    }

    @Nullable
    protected IBakedModel getItemModel(Item item, int meta)
    {
        return simpleShapesCache.get(Integer.valueOf(getIndex(item, meta)));
    }

    private int getIndex(Item item, int meta)
    {
        return Item.getIdFromItem(item) << 16 | meta;
    }

    public void register(Item item, int meta, ModelNamespaced location)
    {
        simpleShapes.put(Integer.valueOf(getIndex(item, meta)), location);
        simpleShapesCache.put(Integer.valueOf(getIndex(item, meta)), modelManager.getModel(location));
    }

    public void register(Item item, ItemMeshDefinition definition)
    {
        shapers.put(item, definition);
    }

    public ModelManager getModelManager()
    {
        return modelManager;
    }

    public void rebuildCache()
    {
        simpleShapesCache.clear();

        for (Map.Entry<Integer, ModelNamespaced> entry : simpleShapes.entrySet())
        {
            simpleShapesCache.put(entry.getKey(), modelManager.getModel(entry.getValue()));
        }
    }
}
