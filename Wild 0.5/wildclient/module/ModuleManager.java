// 
// Decompiled by Procyon v0.5.36
// 

package black.nigger.wildclient.module;

import java.util.Iterator;
import black.nigger.wildclient.module.misc.Panic;
import black.nigger.wildclient.module.misc.Timer;
import black.nigger.wildclient.module.misc.PingFuck;
import black.nigger.wildclient.module.combat.FastEat;
import black.nigger.wildclient.module.misc.FreeCam;
import black.nigger.wildclient.module.misc.FastUse;
import black.nigger.wildclient.module.misc.Phase;
import black.nigger.wildclient.module.misc.NoJumpDelay;
import black.nigger.wildclient.module.misc.AutoRespawn;
import black.nigger.wildclient.module.misc.AntiAFK;
import black.nigger.wildclient.module.misc.NoPush;
import black.nigger.wildclient.module.combat.Regen;
import black.nigger.wildclient.module.combat.Autototem;
import black.nigger.wildclient.module.combat.AutoGApple;
import black.nigger.wildclient.module.combat.DamagePlus;
import black.nigger.wildclient.module.movement.Strafe;
import black.nigger.wildclient.module.combat.Derp;
import black.nigger.wildclient.module.combat.Velocity;
import black.nigger.wildclient.module.combat.HitBox;
import black.nigger.wildclient.module.combat.AimAssist;
import black.nigger.wildclient.module.combat.Killaura;
import black.nigger.wildclient.module.combat.AutoShiftTap;
import black.nigger.wildclient.module.combat.TargetStrafe;
import black.nigger.wildclient.module.movement.BackMotion;
import black.nigger.wildclient.module.movement.DamageFly;
import black.nigger.wildclient.module.movement.InvWalk;
import black.nigger.wildclient.module.movement.NoSlowdown;
import black.nigger.wildclient.module.movement.Speed;
import black.nigger.wildclient.module.movement.LongJump;
import black.nigger.wildclient.module.movement.Fly;
import black.nigger.wildclient.module.movement.Sprint;
import black.nigger.wildclient.module.render.FullBright;
import black.nigger.wildclient.module.render.NoHurtCum;
import black.nigger.wildclient.module.render.PlayerESP;
import black.nigger.wildclient.module.render.ArmorHUD;
import black.nigger.wildclient.module.render.Tracers;
import black.nigger.wildclient.module.render.HUD;
import black.nigger.wildclient.module.render.ClickGUI;
import java.util.ArrayList;

public class ModuleManager
{
    public ArrayList<Module> modules;
    
    public ModuleManager() {
        (this.modules = new ArrayList<Module>()).clear();
        this.modules.add(new ClickGUI());
        this.modules.add(new HUD());
        this.modules.add(new Tracers());
        this.modules.add(new ArmorHUD());
        this.modules.add(new PlayerESP());
        this.modules.add(new NoHurtCum());
        this.modules.add(new FullBright());
        this.getModule("HUD").setToggled(true);
        this.modules.add(new Sprint());
        this.modules.add(new Fly());
        this.modules.add(new LongJump());
        this.modules.add(new Speed());
        this.modules.add(new NoSlowdown());
        this.modules.add(new InvWalk());
        this.modules.add(new DamageFly());
        this.modules.add(new BackMotion());
        this.modules.add(new TargetStrafe());
        this.modules.add(new AutoShiftTap());
        this.modules.add(new Killaura());
        this.modules.add(new AimAssist());
        this.modules.add(new HitBox());
        this.modules.add(new Velocity());
        this.modules.add(new Derp());
        this.modules.add(new Strafe());
        this.modules.add(new DamagePlus());
        this.modules.add(new AutoGApple());
        this.modules.add(new Autototem());
        this.modules.add(new Regen());
        this.modules.add(new NoPush());
        this.modules.add(new AntiAFK());
        this.modules.add(new AutoRespawn());
        this.modules.add(new NoJumpDelay());
        this.modules.add(new Phase());
        this.modules.add(new FastUse());
        this.modules.add(new FreeCam());
        this.modules.add(new FastEat());
        this.modules.add(new PingFuck());
        this.modules.add(new Timer());
        this.modules.add(new Panic());
    }
    
    public Module getModule(final String name) {
        for (final Module m : this.modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
    
    public ArrayList<Module> getModuleList() {
        return this.modules;
    }
    
    public ArrayList<Module> getModulesList() {
        return this.modules;
    }
    
    public ArrayList<Module> getModulesInCategory(final Category c) {
        final ArrayList<Module> mods = new ArrayList<Module>();
        for (final Module m : this.modules) {
            if (m.getCategory() == c) {
                mods.add(m);
            }
        }
        return mods;
    }
}
