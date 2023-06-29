package optifine;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.EnumFacing;

public class RenderInfoLazy
{
    private RenderChunk renderChunk;
    private RenderGlobal.ContainerLocalRenderInformation renderInfo;

    public RenderChunk getRenderChunk()
    {
        return renderChunk;
    }

    public void setRenderChunk(RenderChunk p_setRenderChunk_1_)
    {
        renderChunk = p_setRenderChunk_1_;
        renderInfo = null;
    }

    public RenderGlobal.ContainerLocalRenderInformation getRenderInfo()
    {
        if (renderInfo == null)
        {
            renderInfo = new RenderGlobal.ContainerLocalRenderInformation(renderChunk, null, 0);
        }

        return renderInfo;
    }
}
