package net.minecraft.network.datasync;

import java.io.IOException;
import net.minecraft.network.PacketBuffer;

public interface DataSerializer<T> {
   void write(PacketBuffer var1, T var2);

   T read(PacketBuffer var1) throws IOException;

   DataParameter<T> createKey(int var1);

   T func_192717_a(T var1);
}
