package ua.apraxia.modules.impl.world;

import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.packet.EventReceivePacket;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.ColorSetting;
import ua.apraxia.modules.settings.impl.ModeSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.Utility;
import net.minecraft.network.play.server.SPacketTimeUpdate;

import java.awt.*;

public class WorldFeatures extends Module {
    private long spinTime = 0;

    public static BooleanSetting snow = new BooleanSetting("Snow", true);
    public static ColorSetting weatherColor = new ColorSetting("Weather Color", new Color(0xFFFFFF).getRGB());
    public static BooleanSetting worldColor = new BooleanSetting("World Color", false);
    public static ColorSetting worldColors = new ColorSetting("Color World", new Color(0xFFFFFF).getRGB());
    public BooleanSetting ambience = new BooleanSetting("Ambience", false);
    public ModeSetting ambienceMode = new ModeSetting("Ambience Mode", "Day",  "Night", "Morning", "Sunset", "Spin");
    public SliderSetting ambienceSpeed = new SliderSetting("Speed", 20.f, 0.1f, 1000.f, 1);

    public WorldFeatures() {
        super("WorldFeatures", Categories.World);
        addSetting(snow, weatherColor, worldColor, worldColors, ambience, ambienceMode, ambienceSpeed);
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        if (ambience.value) {
            if (event.getPacket() instanceof SPacketTimeUpdate) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (ambience.value) {
            if (ambienceMode.is("Spin")) {
                Utility.mc.world.setWorldTime(spinTime);
                this.spinTime = (long) (spinTime + ambienceSpeed.value);
            } else if (ambienceMode.is("Day")) {
                Utility.mc.world.setWorldTime(5000);
            } else if (ambienceMode.is("Night")) {
                Utility.mc.world.setWorldTime(17000);
            } else if (ambienceMode.is("Morning")) {
                Utility.mc.world.setWorldTime(0);
            } else if (ambienceMode.is("Sunset")) {
                Utility.mc.world.setWorldTime(13000);
            }
        }
    }
}
