package ru.rockstar.client.features.impl.misc;

import java.util.*;
import net.minecraft.network.play.server.*;
import ru.rockstar.api.event.*;
import ru.rockstar.api.event.event.*;
import ru.rockstar.api.utils.world.*;
import ru.rockstar.client.features.*;
import ru.rockstar.client.ui.settings.*;
import ru.rockstar.client.ui.settings.impl.*;

import java.util.concurrent.*;

import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;

public class Disabler extends Feature
{
    Queue<SPacketPlayerPosLook> packetList;
    TimerHelper timer;
    public ListSetting disablerMode;
    
    public Disabler() {
        super("Disabler", "Частично отключает анти чит на вас", 0, Category.MISC);
        this.packetList = new ConcurrentLinkedQueue<SPacketPlayerPosLook>();
        this.timer = new TimerHelper();
        this.disablerMode = new ListSetting("Disabler Mode", "ReallyWorld", () -> true, new String[] { "ReallyWorld", "Storm Movement" });
        this.addSettings(this.disablerMode);
    }
    @EventTarget
    public void onPreUpdate(final EventPreMotionUpdate event) {
        if (this.disablerMode.currentMode.equals("Storm Movement")) {
            event.setGround(false);
        }
        if (this.disablerMode.currentMode.equals("ReallyWorld") && Disabler.mc.player.ticksExisted % 6 == 0) {
            event.setGround(true);
        }
    }
}
