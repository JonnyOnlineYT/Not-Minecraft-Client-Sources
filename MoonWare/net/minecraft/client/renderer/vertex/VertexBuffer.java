package net.minecraft.client.renderer.vertex;

import java.nio.ByteBuffer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

public class VertexBuffer
{
    private int glBufferId;
    private final VertexFormat vertexFormat;
    private int count;

    public VertexBuffer(VertexFormat vertexFormatIn)
    {
        vertexFormat = vertexFormatIn;
        glBufferId = OpenGlHelper.glGenBuffers();
    }

    public void bindBuffer()
    {
        OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, glBufferId);
    }

    public void bufferData(ByteBuffer data)
    {
        bindBuffer();
        OpenGlHelper.glBufferData(OpenGlHelper.GL_ARRAY_BUFFER, data, 35044);
        unbindBuffer();
        count = data.limit() / vertexFormat.getNextOffset();
    }

    public void drawArrays(int mode)
    {
        GlStateManager.glDrawArrays(mode, 0, count);
    }

    public void unbindBuffer()
    {
        OpenGlHelper.glBindBuffer(OpenGlHelper.GL_ARRAY_BUFFER, 0);
    }

    public void deleteGlBuffers()
    {
        if (glBufferId >= 0)
        {
            OpenGlHelper.glDeleteBuffers(glBufferId);
            glBufferId = -1;
        }
    }
}
