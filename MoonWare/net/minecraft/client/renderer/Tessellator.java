package net.minecraft.client.renderer;

public class Tessellator
{
    private final BufferBuilder worldRenderer;
    private final WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();

    /** The static instance of the Tessellator. */
    private static final Tessellator INSTANCE = new Tessellator(2097152);

    public static Tessellator getInstance()
    {
        return INSTANCE;
    }

    public Tessellator(int bufferSize)
    {
        worldRenderer = new BufferBuilder(bufferSize);
    }

    /**
     * Draws the data set up in this tessellator and resets the state to prepare for new drawing.
     */
    public void draw()
    {
        worldRenderer.finishDrawing();
        vboUploader.draw(worldRenderer);
    }

    public BufferBuilder getBuffer()
    {
        return worldRenderer;
    }
}
