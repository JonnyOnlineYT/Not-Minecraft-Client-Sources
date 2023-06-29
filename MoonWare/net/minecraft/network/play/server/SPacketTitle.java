package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.text.Component;

public class SPacketTitle implements Packet<INetHandlerPlayClient>
{
    private SPacketTitle.Type type;
    private Component message;
    private int fadeInTime;
    private int displayTime;
    private int fadeOutTime;

    public SPacketTitle()
    {
    }

    public SPacketTitle(SPacketTitle.Type typeIn, Component messageIn)
    {
        this(typeIn, messageIn, -1, -1, -1);
    }

    public SPacketTitle(int fadeInTimeIn, int displayTimeIn, int fadeOutTimeIn)
    {
        this(SPacketTitle.Type.TIMES, null, fadeInTimeIn, displayTimeIn, fadeOutTimeIn);
    }

    public SPacketTitle(SPacketTitle.Type typeIn, @Nullable Component messageIn, int fadeInTimeIn, int displayTimeIn, int fadeOutTimeIn)
    {
        type = typeIn;
        message = messageIn;
        fadeInTime = fadeInTimeIn;
        displayTime = displayTimeIn;
        fadeOutTime = fadeOutTimeIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        type = buf.readEnumValue(Type.class);

        if (type == SPacketTitle.Type.TITLE || type == SPacketTitle.Type.SUBTITLE || type == SPacketTitle.Type.ACTIONBAR)
        {
            message = buf.readTextComponent();
        }

        if (type == SPacketTitle.Type.TIMES)
        {
            fadeInTime = buf.readInt();
            displayTime = buf.readInt();
            fadeOutTime = buf.readInt();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(type);

        if (type == SPacketTitle.Type.TITLE || type == SPacketTitle.Type.SUBTITLE || type == SPacketTitle.Type.ACTIONBAR)
        {
            buf.writeTextComponent(message);
        }

        if (type == SPacketTitle.Type.TIMES)
        {
            buf.writeInt(fadeInTime);
            buf.writeInt(displayTime);
            buf.writeInt(fadeOutTime);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleTitle(this);
    }

    public SPacketTitle.Type getType()
    {
        return type;
    }

    public Component getMessage()
    {
        return message;
    }

    public int getFadeInTime()
    {
        return fadeInTime;
    }

    public int getDisplayTime()
    {
        return displayTime;
    }

    public int getFadeOutTime()
    {
        return fadeOutTime;
    }

    public enum Type
    {
        TITLE,
        SUBTITLE,
        ACTIONBAR,
        TIMES,
        CLEAR,
        RESET;

        public static SPacketTitle.Type byName(String name)
        {
            for (SPacketTitle.Type spackettitle$type : values())
            {
                if (spackettitle$type.name().equalsIgnoreCase(name))
                {
                    return spackettitle$type;
                }
            }

            return TITLE;
        }

        public static String[] getNames()
        {
            String[] astring = new String[values().length];
            int i = 0;

            for (SPacketTitle.Type spackettitle$type : values())
            {
                astring[i++] = spackettitle$type.name().toLowerCase(Locale.ROOT);
            }

            return astring;
        }
    }
}
