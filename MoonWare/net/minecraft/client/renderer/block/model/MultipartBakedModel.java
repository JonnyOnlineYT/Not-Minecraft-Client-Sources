package net.minecraft.client.renderer.block.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class MultipartBakedModel implements IBakedModel
{
    private final Map<Predicate<IBlockState>, IBakedModel> selectors;
    protected final boolean ambientOcclusion;
    protected final boolean gui3D;
    protected final TextureAtlasSprite particleTexture;
    protected final ItemCameraTransforms cameraTransforms;
    protected final ItemOverrideList overrides;

    public MultipartBakedModel(Map<Predicate<IBlockState>, IBakedModel> selectorsIn)
    {
        selectors = selectorsIn;
        IBakedModel ibakedmodel = selectorsIn.values().iterator().next();
        ambientOcclusion = ibakedmodel.isAmbientOcclusion();
        gui3D = ibakedmodel.isGui3d();
        particleTexture = ibakedmodel.getParticleTexture();
        cameraTransforms = ibakedmodel.getItemCameraTransforms();
        overrides = ibakedmodel.getOverrides();
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        List<BakedQuad> list = Lists.newArrayList();

        if (state != null)
        {
            for (Map.Entry<Predicate<IBlockState>, IBakedModel> entry : selectors.entrySet())
            {
                if (entry.getKey().apply(state))
                {
                    list.addAll((entry.getValue()).getQuads(state, side, rand++));
                }
            }
        }

        return list;
    }

    public boolean isAmbientOcclusion()
    {
        return ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return gui3D;
    }

    public boolean isBuiltInRenderer()
    {
        return false;
    }

    public TextureAtlasSprite getParticleTexture()
    {
        return particleTexture;
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return cameraTransforms;
    }

    public ItemOverrideList getOverrides()
    {
        return overrides;
    }

    public static class Builder
    {
        private final Map<Predicate<IBlockState>, IBakedModel> builderSelectors = Maps.newLinkedHashMap();

        public void putModel(Predicate<IBlockState> predicate, IBakedModel model)
        {
            builderSelectors.put(predicate, model);
        }

        public IBakedModel makeMultipartModel()
        {
            return new MultipartBakedModel(builderSelectors);
        }
    }
}
