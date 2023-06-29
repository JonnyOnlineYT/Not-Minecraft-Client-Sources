package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class SimpleBakedModel implements IBakedModel
{
    protected final List<BakedQuad> generalQuads;
    protected final Map<EnumFacing, List<BakedQuad>> faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;
    protected final ItemOverrideList itemOverrideList;

    public SimpleBakedModel(List<BakedQuad> generalQuadsIn, Map<EnumFacing, List<BakedQuad>> faceQuadsIn, boolean ambientOcclusionIn, boolean gui3dIn, TextureAtlasSprite textureIn, ItemCameraTransforms cameraTransformsIn, ItemOverrideList itemOverrideListIn)
    {
        generalQuads = generalQuadsIn;
        faceQuads = faceQuadsIn;
        ambientOcclusion = ambientOcclusionIn;
        gui3d = gui3dIn;
        texture = textureIn;
        cameraTransforms = cameraTransformsIn;
        itemOverrideList = itemOverrideListIn;
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        return side == null ? generalQuads : faceQuads.get(side);
    }

    public boolean isAmbientOcclusion()
    {
        return ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return gui3d;
    }

    public boolean isBuiltInRenderer()
    {
        return false;
    }

    public TextureAtlasSprite getParticleTexture()
    {
        return texture;
    }

    public ItemCameraTransforms getItemCameraTransforms()
    {
        return cameraTransforms;
    }

    public ItemOverrideList getOverrides()
    {
        return itemOverrideList;
    }

    public static class Builder
    {
        private final List<BakedQuad> builderGeneralQuads;
        private final Map<EnumFacing, List<BakedQuad>> builderFaceQuads;
        private final ItemOverrideList builderItemOverrideList;
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private final boolean builderGui3d;
        private final ItemCameraTransforms builderCameraTransforms;

        public Builder(ModelBlock model, ItemOverrideList overrides)
        {
            this(model.isAmbientOcclusion(), model.isGui3d(), model.getAllTransforms(), overrides);
        }

        public Builder(IBlockState state, IBakedModel model, TextureAtlasSprite texture, BlockPos pos)
        {
            this(model.isAmbientOcclusion(), model.isGui3d(), model.getItemCameraTransforms(), model.getOverrides());
            builderTexture = model.getParticleTexture();
            long i = MathHelper.getPositionRandom(pos);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                addFaceQuads(state, model, texture, enumfacing, i);
            }

            addGeneralQuads(state, model, texture, i);
        }

        private Builder(boolean ambientOcclusion, boolean gui3d, ItemCameraTransforms transforms, ItemOverrideList overrides)
        {
            builderGeneralQuads = Lists.newArrayList();
            builderFaceQuads = Maps.newEnumMap(EnumFacing.class);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                builderFaceQuads.put(enumfacing, Lists.newArrayList());
            }

            builderItemOverrideList = overrides;
            builderAmbientOcclusion = ambientOcclusion;
            builderGui3d = gui3d;
            builderCameraTransforms = transforms;
        }

        private void addFaceQuads(IBlockState p_188644_1_, IBakedModel p_188644_2_, TextureAtlasSprite p_188644_3_, EnumFacing p_188644_4_, long p_188644_5_)
        {
            for (BakedQuad bakedquad : p_188644_2_.getQuads(p_188644_1_, p_188644_4_, p_188644_5_))
            {
                addFaceQuad(p_188644_4_, new BakedQuadRetextured(bakedquad, p_188644_3_));
            }
        }

        private void addGeneralQuads(IBlockState p_188645_1_, IBakedModel p_188645_2_, TextureAtlasSprite p_188645_3_, long p_188645_4_)
        {
            for (BakedQuad bakedquad : p_188645_2_.getQuads(p_188645_1_, null, p_188645_4_))
            {
                addGeneralQuad(new BakedQuadRetextured(bakedquad, p_188645_3_));
            }
        }

        public SimpleBakedModel.Builder addFaceQuad(EnumFacing facing, BakedQuad quad)
        {
            (builderFaceQuads.get(facing)).add(quad);
            return this;
        }

        public SimpleBakedModel.Builder addGeneralQuad(BakedQuad quad)
        {
            builderGeneralQuads.add(quad);
            return this;
        }

        public SimpleBakedModel.Builder setTexture(TextureAtlasSprite texture)
        {
            builderTexture = texture;
            return this;
        }

        public IBakedModel makeBakedModel()
        {
            if (builderTexture == null)
            {
                throw new RuntimeException("Missing particle!");
            }
            else
            {
                return new SimpleBakedModel(builderGeneralQuads, builderFaceQuads, builderAmbientOcclusion, builderGui3d, builderTexture, builderCameraTransforms, builderItemOverrideList);
            }
        }
    }
}
