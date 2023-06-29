package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptionTranslator
{
    private final Cipher cipher;
    private byte[] inputBuffer = new byte[0];
    private byte[] outputBuffer = new byte[0];

    protected NettyEncryptionTranslator(Cipher cipherIn)
    {
        cipher = cipherIn;
    }

    private byte[] bufToBytes(ByteBuf buf)
    {
        int i = buf.readableBytes();

        if (inputBuffer.length < i)
        {
            inputBuffer = new byte[i];
        }

        buf.readBytes(inputBuffer, 0, i);
        return inputBuffer;
    }

    protected ByteBuf decipher(ChannelHandlerContext ctx, ByteBuf buffer) throws ShortBufferException
    {
        int i = buffer.readableBytes();
        byte[] abyte = bufToBytes(buffer);
        ByteBuf bytebuf = ctx.alloc().heapBuffer(cipher.getOutputSize(i));
        bytebuf.writerIndex(cipher.update(abyte, 0, i, bytebuf.array(), bytebuf.arrayOffset()));
        return bytebuf;
    }

    protected void cipher(ByteBuf in, ByteBuf out) throws ShortBufferException
    {
        int i = in.readableBytes();
        byte[] abyte = bufToBytes(in);
        int j = cipher.getOutputSize(i);

        if (outputBuffer.length < j)
        {
            outputBuffer = new byte[j];
        }

        out.writeBytes(outputBuffer, 0, cipher.update(abyte, 0, i, outputBuffer));
    }
}
