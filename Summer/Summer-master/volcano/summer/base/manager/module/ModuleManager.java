package volcano.summer.base.manager.module;

import java.io.IOException;
import java.util.ArrayList;

import volcano.summer.base.Summer;
import volcano.summer.base.manager.file.files.Modules;
import volcano.summer.client.modules.combat.AntiBot;
import volcano.summer.client.modules.combat.AutoArmor;
import volcano.summer.client.modules.combat.AutoClicker;
import volcano.summer.client.modules.combat.AutoPot;
import volcano.summer.client.modules.combat.AutoSoup;
import volcano.summer.client.modules.combat.AutoSword;
import volcano.summer.client.modules.combat.Criticals;
import volcano.summer.client.modules.combat.Velocity;
import volcano.summer.client.modules.combat.aura.KillAura;
import volcano.summer.client.modules.misc.Breaker;
import volcano.summer.client.modules.misc.Crash;
import volcano.summer.client.modules.misc.Fullbright;
import volcano.summer.client.modules.misc.InvCleaner;
import volcano.summer.client.modules.misc.ItemPhysic;
import volcano.summer.client.modules.misc.Keystrokes;
import volcano.summer.client.modules.misc.MCF;
import volcano.summer.client.modules.misc.Murder;
import volcano.summer.client.modules.misc.Regen;
import volcano.summer.client.modules.movement.Dolphin;
import volcano.summer.client.modules.movement.Flight;
import volcano.summer.client.modules.movement.InventoryMove;
import volcano.summer.client.modules.movement.Jesus;
import volcano.summer.client.modules.movement.LongJump;
import volcano.summer.client.modules.movement.NoSlowDown;
import volcano.summer.client.modules.movement.Speed;
import volcano.summer.client.modules.movement.Sprint;
import volcano.summer.client.modules.movement.StepTEST;
import volcano.summer.client.modules.player.AutoEat;
import volcano.summer.client.modules.player.AutoMine;
import volcano.summer.client.modules.player.ChestSteal;
import volcano.summer.client.modules.player.FreeCam;
import volcano.summer.client.modules.player.NoFall;
import volcano.summer.client.modules.player.NoVoid;
import volcano.summer.client.modules.player.Phase;
import volcano.summer.client.modules.player.Scaffold;
import volcano.summer.client.modules.render.Animations;
import volcano.summer.client.modules.render.Chams;
import volcano.summer.client.modules.render.ChestESP;
import volcano.summer.client.modules.render.ClickGui;
import volcano.summer.client.modules.render.Crosshair;
import volcano.summer.client.modules.render.HUD;
import volcano.summer.client.modules.render.Nametags;
import volcano.summer.client.modules.render.PlayerESP;
import volcano.summer.client.modules.render.SpawnerESP;
import volcano.summer.client.modules.render.Wings;
import volcano.summer.client.modules.render.Xray;

public class ModuleManager {

	public static ArrayList<Module> mods = new ArrayList<Module>();

	public void addModules() {
		mods.add(new Sprint());
		mods.add(new HUD());
		mods.add(new StepTEST());
		mods.add(new Velocity());
		mods.add(new NoSlowDown());
		mods.add(new Nametags());
		mods.add(new NoFall());
		mods.add(new PlayerESP());
		mods.add(new ChestESP());
		mods.add(new Jesus());
		mods.add(new FreeCam());
		mods.add(new ChestSteal());
		mods.add(new AutoArmor());
		mods.add(new Flight());
		mods.add(new InventoryMove());
		mods.add(new MCF());
		mods.add(new Speed());
		mods.add(new AutoClicker());
		mods.add(new LongJump());
		mods.add(new ClickGui());
		mods.add(new Scaffold());
		mods.add(new Phase());
		mods.add(new AutoSoup());
		mods.add(new Regen());
		mods.add(new SpawnerESP());
		mods.add(new Xray());
		mods.add(new Chams());
		mods.add(new InvCleaner());
		mods.add(new Murder());
		mods.add(new NoVoid());
		mods.add(new Fullbright());
		mods.add(new Breaker());
		mods.add(new Dolphin());
		mods.add(new ItemPhysic());
		mods.add(new AntiBot());
		mods.add(new Keystrokes());
		mods.add(new KillAura());
		mods.add(new Criticals());
		mods.add(new AutoPot());
		mods.add(new AutoSword());
		mods.add(new Crash());
		mods.add(new AutoMine());
		mods.add(new AutoEat());
		mods.add(new Wings());
		mods.add(new Animations());
		mods.add(new Crosshair());
		// mods.add(new Test());

	}

	public ArrayList<Module> getMods() {
		return this.mods;
	}

	public Module getModule(Class<? extends Module> clazz) {
		for (Module m : Summer.moduleManager.mods) {
			if (m.getClass() == clazz) {
				return m;
			}
		}
		return null;
	}

	public ArrayList<Module> getModsInCategory(Module.Category cat) {
		ArrayList<Module> gay = new ArrayList();
		for (Module m : this.mods) {
			if (m.category == cat) {
				gay.add(m);
			}
		}
		return gay;
	}

	public Module getModuleByString(String modName) {
		for (Module m : mods) {
			if (m.name.equalsIgnoreCase(modName)) {
				return m;
			}
		}
		return null;
	}

	public void setModuleState(String modName, boolean state) {
		for (Module m : Summer.moduleManager.mods) {
			if (m.name.equalsIgnoreCase(modName.trim())) {
				m.setState(state);
				try {
					Summer.fileManager.getFile(Modules.class).saveFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}

	public ArrayList<Module> getToggledModules() {
		ArrayList<Module> mods = new ArrayList<Module>();
		for (Module mod : this.mods) {
			if (mod.getState()) {
				mods.add(mod);
			}
		}
		return mods;
	}
}
