package de.strafe.modules;

import com.google.common.collect.Lists;
import de.strafe.modules.combat.Killaura;
import de.strafe.modules.combat.Velocity;
import de.strafe.modules.gui.ClickGui;
import de.strafe.modules.misc.ConsoleSpammer;
import de.strafe.modules.misc.Crasher;
import de.strafe.modules.misc.Spammer;
import de.strafe.modules.misc.Timer;
import de.strafe.modules.movement.*;
import de.strafe.modules.player.ChestStealer;
import de.strafe.modules.player.FastPlace;
import de.strafe.modules.player.NoFall;
import de.strafe.modules.render.ChestEsp;
import de.strafe.modules.render.Esp;
import de.strafe.modules.render.FullBright;
import de.strafe.modules.render.HUD;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleManager {

    public final List<Module> modules = Lists.newArrayList();

    public ModuleManager() {
        modules.add(new Velocity());
        modules.add(new Speed());
        modules.add(new Step());
        modules.add(new ClickGui());
        modules.add(new Fly());
        modules.add(new FastPlace());
        modules.add(new NoFall());
        modules.add(new LongJump());
        modules.add(new FullBright());
        modules.add(new Sprint());
        modules.add(new Killaura());
        modules.add(new Esp());
        modules.add(new HUD());
        modules.add(new ChestStealer());
        modules.add(new ChestEsp());
        modules.add((new Timer()));
        modules.add((new Spammer()));
        modules.add((new ConsoleSpammer()));
        modules.add((new Crasher()));
        modules.sort(Comparator.comparing(m -> -Minecraft.getMinecraft().fontRendererObj.getStringWidth(m.getName())));
    }

    public Module getModule(String name) {
        return modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Module> getModules(Category category) {
        List<Module> m2 = new ArrayList<>();

        for (Module i : this.modules) {
            if (i.getCategory() == category) {
                m2.add(i);
            }
        }

        return m2;
    }
}
