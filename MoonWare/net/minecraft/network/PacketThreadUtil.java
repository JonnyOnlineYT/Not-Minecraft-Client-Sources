package net.minecraft.network;

import net.minecraft.util.IThreadListener;

public class PacketThreadUtil
{
    public static <T extends INetHandler> void checkThreadAndEnqueue(Packet<T> packetIn, T processor, IThreadListener scheduler) throws ThreadQuickExitException
    {
        if (!scheduler.isThisThread())
        {
            scheduler.addScheduledTask(new Runnable()
            {
                public void run()
                {
                    packetIn.processPacket(processor);
                }
            });
            throw ThreadQuickExitException.INSTANCE;
        }
    }
}
