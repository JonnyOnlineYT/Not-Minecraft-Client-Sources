package net.minecraft.client.renderer.chunk;

import baritone.Baritone;
import baritone.api.BaritoneAPI;
import baritone.api.utils.IPlayerContext;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import optifine.*;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.render.EventRenderChunk;
import shadersmod.client.SVertexBuilder;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class RenderChunk
{
    private World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    public CompiledChunk compiledChunk = CompiledChunk.DUMMY;
    private final ReentrantLock lockCompileTask = new ReentrantLock();
    private final ReentrantLock lockCompiledChunk = new ReentrantLock();
    private ChunkCompileTaskGenerator compileTask;
    private final Set<TileEntity> setTileEntities = Sets.newHashSet();
    private final int index;
    private final FloatBuffer modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
    private final VertexBuffer[] vertexBuffers = new VertexBuffer[BlockRenderLayer.values().length];
    public AxisAlignedBB boundingBox;
    private int frameIndex = -1;
    private boolean needsUpdate = true;
    private final BlockPos.MutableBlockPos position = new BlockPos.MutableBlockPos(-1, -1, -1);
    private final BlockPos.MutableBlockPos[] mapEnumFacing = new BlockPos.MutableBlockPos[6];
    private boolean needsUpdateCustom;
    private static BlockRenderLayer[] ENUM_WORLD_BLOCK_LAYERS = BlockRenderLayer.values();
    private BlockRenderLayer[] blockLayersSingle = new BlockRenderLayer[1];
    private boolean isMipmaps = Config.isMipmaps();
    private boolean fixBlockLayer = !Reflector.BetterFoliageClient.exists();
    private boolean playerUpdate;
    private RenderChunk[] renderChunksOfset16 = new RenderChunk[6];
    private Chunk chunk;

    public RenderChunk(World p_i47120_1_, RenderGlobal p_i47120_2_, int p_i47120_3_)
    {
        for (int i = 0; i < mapEnumFacing.length; ++i)
        {
            mapEnumFacing[i] = new BlockPos.MutableBlockPos();
        }

        world = p_i47120_1_;
        renderGlobal = p_i47120_2_;
        index = p_i47120_3_;

        if (OpenGlHelper.useVbo())
        {
            for (int j = 0; j < BlockRenderLayer.values().length; ++j)
            {
                vertexBuffers[j] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }

    public boolean setFrameIndex(int frameIndexIn)
    {
        if (frameIndex == frameIndexIn)
        {
            return false;
        }
        else
        {
            frameIndex = frameIndexIn;
            return true;
        }
    }

    public VertexBuffer getVertexBufferByLayer(int layer)
    {
        return vertexBuffers[layer];
    }

    /**
     * Sets the RenderChunk base position
     */
    public void setPosition(int p_189562_1_, int p_189562_2_, int p_189562_3_)
    {
        if (p_189562_1_ != position.getX() || p_189562_2_ != position.getY() || p_189562_3_ != position.getZ())
        {
            stopCompileTask();
            position.setPos(p_189562_1_, p_189562_2_, p_189562_3_);
            boundingBox = new AxisAlignedBB(p_189562_1_, p_189562_2_, p_189562_3_, p_189562_1_ + 16, p_189562_2_ + 16, p_189562_3_ + 16);

            for (EnumFacing enumfacing : EnumFacing.VALUES)
            {
                mapEnumFacing[enumfacing.ordinal()].setPos(position).move(enumfacing, 16);
                renderChunksOfset16[enumfacing.ordinal()] = null;
            }

            chunk = null;
            initModelviewMatrix();
        }
        EventRenderChunk eventRenderChunk = new EventRenderChunk(this, new BlockPos(p_189562_1_, p_189562_2_, p_189562_3_));
        EventManager.call(eventRenderChunk);
    }

    public void resortTransparency(float x, float y, float z, ChunkCompileTaskGenerator generator)
    {
        CompiledChunk compiledchunk = generator.getCompiledChunk();

        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(BlockRenderLayer.TRANSLUCENT))
        {
            BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(BlockRenderLayer.TRANSLUCENT);
            preRenderBlocks(bufferbuilder, position);
            bufferbuilder.setVertexState(compiledchunk.getState());
            postRenderBlocks(BlockRenderLayer.TRANSLUCENT, x, y, z, bufferbuilder, compiledchunk);
        }
    }

    public void rebuildChunk(float x, float y, float z, ChunkCompileTaskGenerator generator)
    {
        CompiledChunk compiledchunk = new CompiledChunk();
        int i = 1;
        BlockPos blockpos = position;
        BlockPos blockpos1 = blockpos.add(15, 15, 15);
        generator.getLock().lock();

        try
        {
            if (generator.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING)
            {
                return;
            }

            generator.setCompiledChunk(compiledchunk);
        }
        finally
        {
            generator.getLock().unlock();
        }

        VisGraph lvt_9_1_ = new VisGraph();
        HashSet lvt_10_1_ = Sets.newHashSet();

        if (world != null)
        {
            ChunkCacheOF chunkcacheof = makeChunkCacheOF();

            if (!chunkcacheof.isEmpty())
            {

                if (Baritone.settings().renderCachedChunks.value && !Minecraft.getMinecraft().isSingleplayer()) {
                    Baritone baritone = (Baritone) BaritoneAPI.getProvider().getPrimaryBaritone();
                    IPlayerContext ctx = baritone.getPlayerContext();
                    if (ctx.player() != null && ctx.world() != null && baritone.bsi != null) {
                        BlockPos position = getPosition();
                        // RenderChunk extends from -1,-1,-1 to +16,+16,+16
                        // then the constructor of ChunkCache extends it one more (presumably to get things like the connected status of fences? idk)
                        // so if ANY of the adjacent chunks are loaded, we are unempty
                        for (int dx = -1; dx <= 1; dx++) {
                            for (int dz = -1; dz <= 1; dz++) {
                                if (baritone.bsi.isLoaded(16 * dx + position.getX(), 16 * dz + position.getZ())) {
                                    return;
                                }
                            }
                        }
                    }
                }
                
                ++renderChunksUpdated;
                chunkcacheof.renderStart();
                boolean[] aboolean = new boolean[ENUM_WORLD_BLOCK_LAYERS.length];
                BlockRendererDispatcher blockrendererdispatcher = Minecraft.getBlockRenderDispatcher();
                boolean flag = Reflector.ForgeBlock_canRenderInLayer.exists();
                boolean flag1 = Reflector.ForgeHooksClient_setRenderLayer.exists();

                for (Object blockposm0 : BlockPosM.getAllInBoxMutable(blockpos, blockpos1))
                {
                    BlockPosM blockposm = (BlockPosM) blockposm0;
                    IBlockState iblockstate = chunkcacheof.getBlockState(blockposm);

                    if (Baritone.settings().renderCachedChunks.value && !Minecraft.getMinecraft().isSingleplayer()) {
                        Baritone baritone = (Baritone) BaritoneAPI.getProvider().getPrimaryBaritone();
                        IPlayerContext ctx = baritone.getPlayerContext();
                        if (ctx.player() != null && ctx.world() != null && baritone.bsi != null) {
                             baritone.bsi.get0(blockposm);
                        }
                    }

                    Block block = iblockstate.getBlock();

                    if (iblockstate.isOpaqueCube())
                    {
                        lvt_9_1_.setOpaqueCube(blockposm);
                    }

                    if (ReflectorForge.blockHasTileEntity(iblockstate))
                    {
                        TileEntity tileentity = chunkcacheof.getTileEntity(blockposm, Chunk.EnumCreateEntityType.CHECK);

                        if (tileentity != null)
                        {
                            TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tileentity);

                            if (tileentityspecialrenderer != null)
                            {
                                if (tileentityspecialrenderer.isGlobalRenderer(tileentity))
                                {
                                    lvt_10_1_.add(tileentity);
                                }
                                else
                                {
                                    compiledchunk.addTileEntity(tileentity);
                                }
                            }
                        }
                    }

                    BlockRenderLayer[] ablockrenderlayer;

                    if (flag)
                    {
                        ablockrenderlayer = ENUM_WORLD_BLOCK_LAYERS;
                    }
                    else
                    {
                        ablockrenderlayer = blockLayersSingle;
                        ablockrenderlayer[0] = block.getBlockLayer();
                    }

                    for (int j = 0; j < ablockrenderlayer.length; ++j)
                    {
                        BlockRenderLayer blockrenderlayer = ablockrenderlayer[j];

                        if (flag)
                        {
                            boolean flag2 = Reflector.callBoolean(block, Reflector.ForgeBlock_canRenderInLayer, iblockstate, blockrenderlayer);

                            if (!flag2)
                            {
                                continue;
                            }
                        }

                        if (flag1)
                        {
                            Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, blockrenderlayer);
                        }

                        if (fixBlockLayer)
                        {
                            blockrenderlayer = fixBlockLayer(block, blockrenderlayer);
                        }

                        int k = blockrenderlayer.ordinal();

                        if (block.getDefaultState().getRenderType() != EnumBlockRenderType.INVISIBLE)
                        {
                            BufferBuilder bufferbuilder = generator.getRegionRenderCacheBuilder().getWorldRendererByLayerId(k);
                            bufferbuilder.setBlockLayer(blockrenderlayer);
                            RenderEnv renderenv = bufferbuilder.getRenderEnv(chunkcacheof, iblockstate, blockposm);
                            renderenv.setRegionRenderCacheBuilder(generator.getRegionRenderCacheBuilder());

                            if (!compiledchunk.isLayerStarted(blockrenderlayer))
                            {
                                compiledchunk.setLayerStarted(blockrenderlayer);
                                preRenderBlocks(bufferbuilder, blockpos);
                            }

                            aboolean[k] |= blockrendererdispatcher.renderBlock(iblockstate, blockposm, chunkcacheof, bufferbuilder);

                            if (renderenv.isOverlaysRendered())
                            {
                                postRenderOverlays(generator.getRegionRenderCacheBuilder(), compiledchunk, aboolean);
                                renderenv.setOverlaysRendered(false);
                            }
                        }
                    }

                    if (flag1)
                    {
                        Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, (Object[]) null);
                    }
                }

                for (BlockRenderLayer blockrenderlayer1 : ENUM_WORLD_BLOCK_LAYERS)
                {
                    if (aboolean[blockrenderlayer1.ordinal()])
                    {
                        compiledchunk.setLayerUsed(blockrenderlayer1);
                    }

                    if (compiledchunk.isLayerStarted(blockrenderlayer1))
                    {
                        if (Config.isShaders())
                        {
                            SVertexBuilder.calcNormalChunkLayer(generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer1));
                        }

                        postRenderBlocks(blockrenderlayer1, x, y, z, generator.getRegionRenderCacheBuilder().getWorldRendererByLayer(blockrenderlayer1), compiledchunk);
                    }
                }

                chunkcacheof.renderFinish();
            }

            compiledchunk.setVisibility(lvt_9_1_.computeVisibility());
            lockCompileTask.lock();

            try
            {
                Set<TileEntity> set = Sets.newHashSet(lvt_10_1_);
                Set<TileEntity> set1 = Sets.newHashSet(setTileEntities);
                set.removeAll(setTileEntities);
                set1.removeAll(lvt_10_1_);
                setTileEntities.clear();
                setTileEntities.addAll(lvt_10_1_);
                renderGlobal.updateTileEntities(set1, set);
            }
            finally
            {
                lockCompileTask.unlock();
            }
        }
    }

    protected void finishCompileTask()
    {
        lockCompileTask.lock();

        try
        {
            if (compileTask != null && compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE)
            {
                compileTask.finish();
                compileTask = null;
            }
        }
        finally
        {
            lockCompileTask.unlock();
        }
    }

    public ReentrantLock getLockCompileTask()
    {
        return lockCompileTask;
    }

    public ChunkCompileTaskGenerator makeCompileTaskChunk()
    {
        lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;

        try
        {
            finishCompileTask();
            compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK, getDistanceSq());
            resetChunkCache();
            chunkcompiletaskgenerator = compileTask;
        }
        finally
        {
            lockCompileTask.unlock();
        }

        return chunkcompiletaskgenerator;
    }

    private void resetChunkCache()
    {
        int i = 1;
    }

    @Nullable
    public ChunkCompileTaskGenerator makeCompileTaskTransparency()
    {
        lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator1;

        try
        {
            if (compileTask != null && compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING)
            {
                ChunkCompileTaskGenerator chunkcompiletaskgenerator2 = null;
                return chunkcompiletaskgenerator2;
            }

            if (compileTask != null && compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE)
            {
                compileTask.finish();
                compileTask = null;
            }

            compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY, getDistanceSq());
            compileTask.setCompiledChunk(compiledChunk);
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = compileTask;
            chunkcompiletaskgenerator1 = chunkcompiletaskgenerator;
        }
        finally
        {
            lockCompileTask.unlock();
        }

        return chunkcompiletaskgenerator1;
    }

    protected double getDistanceSq()
    {
        EntityPlayerSP entityplayersp = Minecraft.player;
        double d0 = boundingBox.minX + 8.0D - entityplayersp.posX;
        double d1 = boundingBox.minY + 8.0D - entityplayersp.posY;
        double d2 = boundingBox.minZ + 8.0D - entityplayersp.posZ;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    private void preRenderBlocks(BufferBuilder worldRendererIn, BlockPos pos)
    {
        worldRendererIn.begin(7, DefaultVertexFormats.BLOCK);
        worldRendererIn.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
    }

    private void postRenderBlocks(BlockRenderLayer layer, float x, float y, float z, BufferBuilder worldRendererIn, CompiledChunk compiledChunkIn)
    {
        if (layer == BlockRenderLayer.TRANSLUCENT && !compiledChunkIn.isLayerEmpty(layer))
        {
            worldRendererIn.sortVertexData(x, y, z);
            compiledChunkIn.setState(worldRendererIn.getVertexState());
        }

        worldRendererIn.finishDrawing();
    }

    private void initModelviewMatrix()
    {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        float f = 1.000001F;
        GlStateManager.translate(-8.0F, -8.0F, -8.0F);
        GlStateManager.scale(1.000001F, 1.000001F, 1.000001F);
        GlStateManager.translate(8.0F, 8.0F, 8.0F);
        GlStateManager.getFloat(2982, modelviewMatrix);
        GlStateManager.popMatrix();
    }

    public void multModelviewMatrix()
    {
        GlStateManager.multMatrix(modelviewMatrix);
    }

    public CompiledChunk getCompiledChunk()
    {
        return compiledChunk;
    }

    public void setCompiledChunk(CompiledChunk compiledChunkIn)
    {
        lockCompiledChunk.lock();

        try
        {
            compiledChunk = compiledChunkIn;
        }
        finally
        {
            lockCompiledChunk.unlock();
        }
    }

    public void stopCompileTask()
    {
        finishCompileTask();
        compiledChunk = CompiledChunk.DUMMY;
    }

    public void deleteGlResources()
    {
        stopCompileTask();
        world = null;

        for (int i = 0; i < BlockRenderLayer.values().length; ++i)
        {
            if (vertexBuffers[i] != null)
            {
                vertexBuffers[i].deleteGlBuffers();
            }
        }
    }

    public BlockPos getPosition()
    {
        return position;
    }

    public void setNeedsUpdate(boolean needsUpdateIn)
    {
        if (needsUpdate)
        {
            needsUpdateIn |= needsUpdateCustom;
        }

        needsUpdate = true;
        needsUpdateCustom = needsUpdateIn;

        if (isWorldPlayerUpdate())
        {
            playerUpdate = true;
        }
    }

    public void clearNeedsUpdate()
    {
        needsUpdate = false;
        needsUpdateCustom = false;
        playerUpdate = false;
    }

    public boolean isNeedsUpdate()
    {
        return needsUpdate;
    }

    public boolean isNeedsUpdateCustom()
    {
        return needsUpdate && needsUpdateCustom;
    }

    public BlockPos getBlockPosOffset16(EnumFacing p_181701_1_)
    {
        return mapEnumFacing[p_181701_1_.ordinal()];
    }

    public World getWorld()
    {
        return world;
    }

    private boolean isWorldPlayerUpdate()
    {
        if (world instanceof ClientLevel)
        {
            ClientLevel worldclient = (ClientLevel) world;
            return worldclient.isPlayerUpdate();
        }
        else
        {
            return false;
        }
    }

    public boolean isPlayerUpdate()
    {
        return playerUpdate;
    }

    private BlockRenderLayer fixBlockLayer(Block p_fixBlockLayer_1_, BlockRenderLayer p_fixBlockLayer_2_)
    {
        if (isMipmaps)
        {
            if (p_fixBlockLayer_2_ == BlockRenderLayer.CUTOUT)
            {
                if (p_fixBlockLayer_1_ instanceof BlockRedstoneWire)
                {
                    return p_fixBlockLayer_2_;
                }

                if (p_fixBlockLayer_1_ instanceof BlockCactus)
                {
                    return p_fixBlockLayer_2_;
                }

                return BlockRenderLayer.CUTOUT_MIPPED;
            }
        }
        else if (p_fixBlockLayer_2_ == BlockRenderLayer.CUTOUT_MIPPED)
        {
            return BlockRenderLayer.CUTOUT;
        }

        return p_fixBlockLayer_2_;
    }

    private void postRenderOverlays(RegionRenderCacheBuilder p_postRenderOverlays_1_, CompiledChunk p_postRenderOverlays_2_, boolean[] p_postRenderOverlays_3_)
    {
        postRenderOverlay(BlockRenderLayer.CUTOUT, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
        postRenderOverlay(BlockRenderLayer.CUTOUT_MIPPED, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
        postRenderOverlay(BlockRenderLayer.TRANSLUCENT, p_postRenderOverlays_1_, p_postRenderOverlays_2_, p_postRenderOverlays_3_);
    }

    private void postRenderOverlay(BlockRenderLayer p_postRenderOverlay_1_, RegionRenderCacheBuilder p_postRenderOverlay_2_, CompiledChunk p_postRenderOverlay_3_, boolean[] p_postRenderOverlay_4_)
    {
        BufferBuilder bufferbuilder = p_postRenderOverlay_2_.getWorldRendererByLayer(p_postRenderOverlay_1_);

        if (bufferbuilder.isDrawing())
        {
            p_postRenderOverlay_3_.setLayerStarted(p_postRenderOverlay_1_);
            p_postRenderOverlay_4_[p_postRenderOverlay_1_.ordinal()] = true;
        }
    }

    private ChunkCacheOF makeChunkCacheOF()
    {
        BlockPos blockpos = position.add(-1, -1, -1);
        ChunkCache chunkcache = createRegionRenderCache(world, blockpos, position.add(16, 16, 16), 1);

        if (Reflector.MinecraftForgeClient_onRebuildChunk.exists())
        {
            Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, world, position, chunkcache);
        }

        ChunkCacheOF chunkcacheof = new ChunkCacheOF(chunkcache, blockpos, 1);
        return chunkcacheof;
    }

    public RenderChunk getRenderChunkOffset16(ViewFrustum p_getRenderChunkOffset16_1_, EnumFacing p_getRenderChunkOffset16_2_)
    {
        RenderChunk renderchunk = renderChunksOfset16[p_getRenderChunkOffset16_2_.ordinal()];

        if (renderchunk == null)
        {
            BlockPos blockpos = getBlockPosOffset16(p_getRenderChunkOffset16_2_);
            renderchunk = p_getRenderChunkOffset16_1_.getRenderChunk(blockpos);
            renderChunksOfset16[p_getRenderChunkOffset16_2_.ordinal()] = renderchunk;
        }

        return renderchunk;
    }

    public Chunk getChunk(World p_getChunk_1_)
    {
        if (chunk != null && chunk.isLoaded())
        {
            return chunk;
        }
        else
        {
            chunk = p_getChunk_1_.getChunkFromBlockCoords(getPosition());
            return chunk;
        }
    }

    protected ChunkCache createRegionRenderCache(World p_createRegionRenderCache_1_, BlockPos p_createRegionRenderCache_2_, BlockPos p_createRegionRenderCache_3_, int p_createRegionRenderCache_4_)
    {
        return new ChunkCache(p_createRegionRenderCache_1_, p_createRegionRenderCache_2_, p_createRegionRenderCache_3_, p_createRegionRenderCache_4_);
    }
}
