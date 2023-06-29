package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class NettyCompressionEncoder extends MessageToByteEncoder<ByteBuf>
{
    private final byte[] buffer = new byte[8192];
    private final Deflater deflater;
    private int threshold;

    public NettyCompressionEncoder(int thresholdIn)
    {
        threshold = thresholdIn;
        deflater = new Deflater();
    }

    protected void encode(ChannelHandlerContext p_encode_1_, ByteBuf p_encode_2_, ByteBuf p_encode_3_) throws Exception
    {
        int i = p_encode_2_.readableBytes();
        PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);

        if (i < threshold)
        {
            packetbuffer.writeVarIntToBuffer(0);
            packetbuffer.writeBytes(p_encode_2_);
        }
        else
        {
            byte[] abyte = new byte[i];
            p_encode_2_.readBytes(abyte);
            packetbuffer.writeVarIntToBuffer(abyte.length);
            deflater.setInput(abyte, 0, i);
            deflater.finish();

            while (!deflater.finished())
            {
                int j = deflater.deflate(buffer);
                packetbuffer.writeBytes(buffer, 0, j);
            }

            deflater.reset();
        }
    }

    public void setCompressionThreshold(int thresholdIn)
    {
        threshold = thresholdIn;
    }
}
