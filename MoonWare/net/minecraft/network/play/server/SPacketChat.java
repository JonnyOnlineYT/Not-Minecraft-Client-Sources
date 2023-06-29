package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.Component;

public class SPacketChat implements Packet<INetHandlerPlayClient>
{
    private Component chatComponent;
    private ChatType type;

    public SPacketChat()
    {
    }

    public SPacketChat(Component componentIn)
    {
        this(componentIn, ChatType.SYSTEM);
    }

    public SPacketChat(Component p_i47428_1_, ChatType p_i47428_2_)
    {
        chatComponent = p_i47428_1_;
        type = p_i47428_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        chatComponent = buf.readTextComponent();
        type = ChatType.func_192582_a(buf.readByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeTextComponent(chatComponent);
        buf.writeByte(type.func_192583_a());
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChat(this);
    }

    public Component getChatComponent()
    {
        return chatComponent;
    }

    /**
     * This method returns true if the type is SYSTEM or ABOVE_HOTBAR, and false if CHAT
     */
    public boolean isSystem()
    {
        return type == ChatType.SYSTEM || type == ChatType.GAME_INFO;
    }

    public ChatType getChatType()
    {
        return type;
    }
}
