package net.minecraft.crash;

import java.util.concurrent.Callable;
import net.minecraft.client.renderer.OpenGlHelper;

public class CrashReporter3 implements Callable
{
	
	CrashReport field_71490_a;

    CrashReporter3(CrashReport p_i1340_1_)
    {
        this.field_71490_a = p_i1340_1_;
    }

    public String call()
    {
        return OpenGlHelper.func_183029_j();
    }
}
