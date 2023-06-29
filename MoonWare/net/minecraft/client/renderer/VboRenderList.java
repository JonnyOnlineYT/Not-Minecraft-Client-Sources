package net.minecraft.client.renderer;

import baritone.Baritone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.BlockRenderLayer;
import optifine.Config;
import org.lwjgl.opengl.GL11;
import shadersmod.client.ShadersRender;

public class VboRenderList extends ChunkRenderContainer
{
    public void renderChunkLayer(BlockRenderLayer layer)
    {
        if (initialized)
        {
            for (RenderChunk renderchunk : renderChunks)
            {
                VertexBuffer vertexbuffer = renderchunk.getVertexBufferByLayer(layer.ordinal());
                GlStateManager.pushMatrix();
                preRenderChunk(renderchunk);
                renderchunk.multModelviewMatrix();
                vertexbuffer.bindBuffer();
                setupArrayPointers();
                vertexbuffer.drawArrays(7);
                if (Baritone.settings().renderCachedChunks.value && !Minecraft.getMinecraft().isSingleplayer()) {
                    // reset the blend func to normal (not dependent on constant alpha)
                    GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                }
                GlStateManager.popMatrix();
            }

            OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
            GlStateManager.resetColor();
            renderChunks.clear();
        }
    }

    private void setupArrayPointers()
    {
        if (Config.isShaders())
        {
            ShadersRender.setupArrayPointersVbo();
        }
        else
        {
            GlStateManager.glVertexPointer(3, 5126, 28, 0);
            GlStateManager.glColorPointer(4, 5121, 28, 12);
            GlStateManager.glTexCoordPointer(2, 5126, 28, 16);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.glTexCoordPointer(2, 5122, 28, 24);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
}
