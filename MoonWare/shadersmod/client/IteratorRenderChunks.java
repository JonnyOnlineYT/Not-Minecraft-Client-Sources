package shadersmod.client;

import java.util.Iterator;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.math.BlockPos;
import optifine.BlockPosM;

public class IteratorRenderChunks implements Iterator<RenderChunk>
{
    private ViewFrustum viewFrustum;
    private Iterator3d Iterator3d;
    private BlockPosM posBlock = new BlockPosM(0, 0, 0);

    public IteratorRenderChunks(ViewFrustum viewFrustum, BlockPos posStart, BlockPos posEnd, int width, int height)
    {
        this.viewFrustum = viewFrustum;
        Iterator3d = new Iterator3d(posStart, posEnd, width, height);
    }

    public boolean hasNext()
    {
        return Iterator3d.hasNext();
    }

    public RenderChunk next()
    {
        BlockPos blockpos = Iterator3d.next();
        posBlock.setXyz(blockpos.getX() << 4, blockpos.getY() << 4, blockpos.getZ() << 4);
        RenderChunk renderchunk = viewFrustum.getRenderChunk(posBlock);
        return renderchunk;
    }

    public void remove()
    {
        throw new RuntimeException("Not implemented");
    }
}
