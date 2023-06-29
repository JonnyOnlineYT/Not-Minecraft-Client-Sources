//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "Deb"!

// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module.combat;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import black.nigger.wildclient.module.Category;
import black.nigger.wildclient.module.Counter;
import black.nigger.wildclient.module.Module;

public class AutoShiftTap extends Module
{
    Counter counter;
    public int ticks;
    
    public AutoShiftTap() {
        super("AutoShiftTap", "AutoShift", Category.COMBAT);
        this.counter = new Counter();
        this.ticks = 0;
    }
    
    @Override
    public void onEnable() {
        this.ticks = 0;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent
    public void OnPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (AutoShiftTap.mc.gameSettings.keyBindAttack.isKeyDown()) {
            KeyBinding.setKeyBindState(AutoShiftTap.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            this.ticks = 0;
        }
        if (this.ticks == 2) {
            KeyBinding.setKeyBindState(AutoShiftTap.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
        ++this.ticks;
    }
}
