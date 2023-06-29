package net.minecraft.client.renderer.block.model;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel implements IBakedModel
{
    private final int totalWeight;
    private final List<WeightedBakedModel.WeightedModel> models;
    private final IBakedModel baseModel;

    public WeightedBakedModel(List<WeightedBakedModel.WeightedModel> modelsIn)
    {
        models = modelsIn;
        totalWeight = WeightedRandom.getTotalWeight(modelsIn);
        baseModel = (modelsIn.get(0)).model;
    }

    private IBakedModel getRandomModel(long p_188627_1_)
    {
        return WeightedRandom.getRandomItem(models, Math.abs((int)p_188627_1_ >> 16) % totalWeight).model;
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        return getRandomModel(rand).getQuads(state, side, rand);
    }

    public boolean isAmbientOcclusion()
    {
        return baseModel.isAmbientOcclusion();
    }

    public boolean isGui3d()
    {
        return baseModel.isGui3d();
    }

    public boolean isBuiltInRenderer()
    {
        return baseModel.isBuiltInRenderer();
    }

    public TextureAtlasSprite getParticleTexture()
    {
        return baseModel.getParticleTexture();
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return baseModel.getItemCameraTransforms();
    }

    public ItemOverrideList getOverrides()
    {
        return baseModel.getOverrides();
    }

    public static class Builder
    {
        private final List<WeightedBakedModel.WeightedModel> listItems = Lists.newArrayList();

        public WeightedBakedModel.Builder add(IBakedModel model, int weight)
        {
            listItems.add(new WeightedBakedModel.WeightedModel(model, weight));
            return this;
        }

        public WeightedBakedModel build()
        {
            Collections.sort(listItems);
            return new WeightedBakedModel(listItems);
        }

        public IBakedModel first()
        {
            return (listItems.get(0)).model;
        }
    }

    static class WeightedModel extends WeightedRandom.Item implements Comparable<WeightedBakedModel.WeightedModel>
    {
        protected final IBakedModel model;

        public WeightedModel(IBakedModel modelIn, int itemWeightIn)
        {
            super(itemWeightIn);
            model = modelIn;
        }

        public int compareTo(WeightedBakedModel.WeightedModel p_compareTo_1_)
        {
            return ComparisonChain.start().compare(p_compareTo_1_.itemWeight, itemWeight).result();
        }

        public String toString()
        {
            return "MyWeighedRandomItem{weight=" + itemWeight + ", model=" + model + '}';
        }
    }
}
