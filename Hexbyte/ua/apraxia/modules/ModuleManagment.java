package ua.apraxia.modules;

import ua.apraxia.modules.impl.combat.*;
import ua.apraxia.modules.impl.exploit.BowExploit;
import ua.apraxia.modules.impl.legit.FastBow;
import ua.apraxia.modules.impl.legit.HitBox;
import ua.apraxia.modules.impl.legit.TriggerBot;
import ua.apraxia.modules.impl.move.*;
import ua.apraxia.modules.impl.player.*;
import ua.apraxia.modules.impl.render.HitFlyingParticles;
import ua.apraxia.modules.impl.display.*;
import ua.apraxia.modules.impl.other.*;
import ua.apraxia.modules.impl.world.HitMCParticles;
import ua.apraxia.modules.impl.render.*;
import ua.apraxia.modules.impl.world.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModuleManagment {
    private ArrayList<Module> modules = new ArrayList<Module>();

    public ModuleManagment() {
        modules.add(new HitFlyingParticles());
        modules.add(new NoRenderOverlay());
        modules.add(new WorldFeatures());
        modules.add(new ItemESP());
        modules.add(new ItemScroller());
        modules.add(new Spider());
        modules.add(new Tracers());
        modules.add(new NoSlowDown());
        modules.add(new Timer());
        modules.add(new Velocity());
        modules.add(new NameTags());
        modules.add(new HitBox());
        modules.add(new GPS());
        modules.add(new Aura());
        modules.add(new NoServerRotations());
        modules.add(new Speed());
        modules.add(new TriggerBot());
        modules.add(new FastBow());
        modules.add(new BowExploit());
        modules.add(new ItemAnimations());
        modules.add(new NoPush());
        modules.add(new AntiAFK());
        modules.add(new NoJumpDelay());
        modules.add(new HitMCParticles());
        modules.add(new JumpCircles());
        modules.add(new MiddleClickPearl());
        modules.add(new DeathCoords());
        modules.add(new ModuleList());
        modules.add(new ChatHistory());
        modules.add(new AutoPotion());
        modules.add(new NoInteract());
        modules.add(new ScoreboardFeatures());
        modules.add(new AutoTotem());
        modules.add(new FreeCam());
        modules.add(new Strafe());
        modules.add(new ItemPhysics());
        modules.add(new FullBright());
        modules.add(new PearlTracers());
        modules.add(new ElytraHelper());
        modules.add(new Crosshair());
        modules.add(new PotionHUD());
        modules.add(new TargetHUD());
        modules.add(new ViewModel());
        modules.add(new ChinaHat());
        modules.add(new WindowGUI());
        modules.add(new GuiMove());
        modules.add(new Sprint());
        modules.add(new HUD());
    }

    public List<Module> getAllFeatures() {
        return this.modules;
    }

    public ArrayList<Module> getModulesForCategory(Categories category) {

        ArrayList<Module> returned = new ArrayList<>();

        for(Module module : modules) {
            if(module.getModuleCategory() == category) returned.add(module);
        }

        return returned;
    }

    public Module getModule(Class moduleClass) {
        Iterator var2 = this.modules.iterator();

        Module module;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            module = (Module)var2.next();
        } while(module.getClass() != moduleClass);

        return module;
    }

    public Module getFeature(String name) {
        for (Module feature : getAllFeatures()) {
            if (feature.getModuleName().equals(name)) {
                return feature;
            }
        }
        return null;
    }

    public ArrayList<Module> getModules() {
        return modules;
    }
}
