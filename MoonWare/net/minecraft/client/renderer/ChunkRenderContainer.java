package net.minecraft.client.renderer;

import baritone.Baritone;
import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL14;
import org.moonware.client.event.EventManager;
import org.moonware.client.event.events.impl.render.EventRenderChunkContainer;


import static org.lwjgl.opengl.GL11.*;

public abstract class ChunkRenderContainer
{
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    protected List<RenderChunk> renderChunks = Lists.newArrayListWithCapacity(17424);
    protected boolean initialized;

    public void initialize(double viewEntityXIn, double viewEntityYIn, double viewEntityZIn)
    {
        initialized = true;
        renderChunks.clear();
        viewEntityX = viewEntityXIn;
        viewEntityY = viewEntityYIn;
        viewEntityZ = viewEntityZIn;
    }

    public void preRenderChunk(RenderChunk renderChunkIn)
    {

        if (Baritone.settings().renderCachedChunks.value && !Minecraft.getMinecraft().isSingleplayer() && Minecraft.world.getChunkFromBlockCoords(renderChunkIn.getPosition()).isEmpty()) {
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GL14.glBlendColor(0, 0, 0, Baritone.settings().cachedChunksOpacity.value);
            GlStateManager.tryBlendFuncSeparate(GL_CONSTANT_ALPHA, GL_ONE_MINUS_CONSTANT_ALPHA, GL_ONE, GL_ZERO);
        }

        BlockPos blockpos = renderChunkIn.getPosition();
        GlStateManager.translate((float)((double)blockpos.getX() - viewEntityX), (float)((double)blockpos.getY() - viewEntityY), (float)((double)blockpos.getZ() - viewEntityZ));
        EventRenderChunkContainer eventRenderChunkContainer = new EventRenderChunkContainer(renderChunkIn);
        EventManager.call(eventRenderChunkContainer);
    }

    public void addRenderChunk(RenderChunk renderChunkIn, BlockRenderLayer layer)
    {
        renderChunks.add(renderChunkIn);
    }

    public abstract void renderChunkLayer(BlockRenderLayer layer);
}
