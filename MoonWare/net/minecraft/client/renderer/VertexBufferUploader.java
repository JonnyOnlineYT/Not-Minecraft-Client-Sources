package net.minecraft.client.renderer;

import net.minecraft.client.renderer.vertex.VertexBuffer;

public class VertexBufferUploader extends WorldVertexBufferUploader
{
    private VertexBuffer vertexBuffer;

    public void draw(BufferBuilder vertexBufferIn)
    {
        vertexBufferIn.reset();
        vertexBuffer.bufferData(vertexBufferIn.getByteBuffer());
    }

    public void setVertexBuffer(VertexBuffer vertexBufferIn)
    {
        vertexBuffer = vertexBufferIn;
    }
}
