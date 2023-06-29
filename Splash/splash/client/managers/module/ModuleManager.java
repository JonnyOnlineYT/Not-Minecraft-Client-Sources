package splash.client.managers.module;

import java.util.ArrayList;

import splash.api.manager.ClientManager;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.modules.NullModule;
import splash.client.modules.combat.AimAssist;
import splash.client.modules.combat.AntiBot;
import splash.client.modules.combat.Aura;
import splash.client.modules.combat.AutoClicker;
import splash.client.modules.combat.AutoUse;
import splash.client.modules.combat.Criticals;
import splash.client.modules.combat.Reach;
import splash.client.modules.combat.TargetStrafe;
import splash.client.modules.combat.Velocity;
import splash.client.modules.combat.WTap;
import splash.client.modules.misc.Autoplay;
import splash.client.modules.misc.CakeEater;
import splash.client.modules.misc.Disabler;
import splash.client.modules.misc.Spammer;
import splash.client.modules.movement.AntiVoid;
import splash.client.modules.movement.FlagDetector;
import splash.client.modules.movement.Flight;
import splash.client.modules.movement.InventoryMove;
import splash.client.modules.movement.LongJump;
import splash.client.modules.movement.NoSlow;
import splash.client.modules.movement.Phase;
import splash.client.modules.movement.Speed;
import splash.client.modules.movement.Sprint;
import splash.client.modules.movement.Step;
import splash.client.modules.player.ChestStealer;
import splash.client.modules.player.InventoryManager;
import splash.client.modules.player.Killsults;
import splash.client.modules.player.NoFall;
import splash.client.modules.player.NoRotate;
import splash.client.modules.player.Scaffold;
import splash.client.modules.visual.Ambiance;
import splash.client.modules.visual.Animations;
import splash.client.modules.visual.Chams;
import splash.client.modules.visual.ESP;
import splash.client.modules.visual.Freecam;
import splash.client.modules.visual.GUI;
import splash.client.modules.visual.Gamma;
import splash.client.modules.visual.HitEffects;
import splash.client.modules.visual.ItemPhysics;
import splash.client.modules.visual.Nametags;
import splash.client.modules.visual.NoHurtCam;
import splash.client.modules.visual.UI;

/**
 * Author: Ice
 * Created: 00:25, 30-May-20
 * Project: Client
 */
public class ModuleManager extends ClientManager<Module> {

    public ModuleManager() {
    	/*Combat*/
	    addContent(new TargetStrafe());
	    addContent(new AutoClicker());
    	addContent(new AimAssist());
	    addContent(new Criticals());
	    addContent(new Velocity());
	    addContent(new AntiBot());
	    addContent(new AutoUse());
	    addContent(new Reach());
	    addContent(new WTap());
	    addContent(new Aura());
    	/*Visual*/
	    addContent(new ItemPhysics());
	    addContent(new HitEffects());
	    addContent(new Animations());
	    addContent(new NoHurtCam());
	    addContent(new Ambiance());
	    addContent(new Nametags());
	    addContent(new Gamma());
	    addContent(new Chams());
	    addContent(new ESP());
	    addContent(new GUI());
	    addContent(new UI());
    	/*Move*/
	    addContent(new InventoryMove());
	    addContent(new FlagDetector());
	    addContent(new LongJump()); 
	    addContent(new AntiVoid());
	    addContent(new Flight());
	    addContent(new Sprint());
	    addContent(new NoSlow());
	    addContent(new Speed());
	    addContent(new Phase());
	    addContent(new Step());
	    /*Player*/
	    addContent(new InventoryManager());
	    addContent(new ChestStealer());
	    addContent(new Killsults());
	    addContent(new Scaffold());
	    addContent(new NoRotate());
	    addContent(new NoFall());
    	/*Misc*/
	    addContent(new CakeEater());
	    addContent(new Autoplay());
	    addContent(new Disabler());
	    addContent(new Spammer());
	    addContent(new Freecam());

    }

    @Override
    public String managerName() {
        return "ModuleManager";
    }

    public Module getModuleByDisplayName(String moduleDisplayName) {
        for(Module module : getContents()) {
            if(module.getModuleDisplayName().equalsIgnoreCase(moduleDisplayName)) {
                return module;
            }
        }
        return new NullModule();
    }

    public Module getModuleByClass(Class clazz) {
        for(Module module : getContents()) {
            if(module.getClass() == clazz) {
                return module;
            }
        }
        return new NullModule();
    }

    public ArrayList<Module> getModulesInCategory(ModuleCategory moduleCategory) {
        ArrayList<Module> modules = new ArrayList<>();
        for(Module module : getContents()) {
            if(module.getModuleCategory() == moduleCategory) {
                modules.add(module);
            }
        }
        return modules;
    }

    public ArrayList<Module> getModulesForRender() {
        ArrayList<Module> modulesForRender = new ArrayList<>();
        for(Module module : getContents()) {
            if(module.isModuleActive() && checkVisibility(module)) {
                modulesForRender.add(module);
            }
        }
        return modulesForRender;
    }

    public boolean checkVisibility(Module module) {
        return module != getModuleByClass(UI.class) && module != getModuleByClass(GUI.class);
    }
}
