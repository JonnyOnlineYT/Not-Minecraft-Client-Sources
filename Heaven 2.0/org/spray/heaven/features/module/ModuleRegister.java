package org.spray.heaven.features.module;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.spray.heaven.features.module.modules.combat.AntiCrystal;
import org.spray.heaven.features.module.modules.combat.AutoArmor;
import org.spray.heaven.features.module.modules.combat.AutoTotem;
import org.spray.heaven.features.module.modules.combat.BetterBow;
import org.spray.heaven.features.module.modules.combat.BowAim;
import org.spray.heaven.features.module.modules.combat.Criticals;
import org.spray.heaven.features.module.modules.combat.HitBoxes;
import org.spray.heaven.features.module.modules.combat.KillAura;
import org.spray.heaven.features.module.modules.combat.NoFriendDamage;
import org.spray.heaven.features.module.modules.combat.SuperBow;
import org.spray.heaven.features.module.modules.combat.SuperKnockBack;
import org.spray.heaven.features.module.modules.combat.TriggerBot;
import org.spray.heaven.features.module.modules.combat.Velocity;
import org.spray.heaven.features.module.modules.display.ArrayListMod;
import org.spray.heaven.features.module.modules.display.PotionEffects;
import org.spray.heaven.features.module.modules.display.TargetHUD;
import org.spray.heaven.features.module.modules.display.Watermark;
import org.spray.heaven.features.module.modules.misc.*;
import org.spray.heaven.features.module.modules.misc.AutoTPAccept;
import org.spray.heaven.features.module.modules.movement.AntiLevitation;
import org.spray.heaven.features.module.modules.movement.BedrockClip;
import org.spray.heaven.features.module.modules.movement.DamageFly;
import org.spray.heaven.features.module.modules.movement.Eagle;
import org.spray.heaven.features.module.modules.movement.ElytraStrafe;
import org.spray.heaven.features.module.modules.movement.Fly;
import org.spray.heaven.features.module.modules.movement.HighJump;
import org.spray.heaven.features.module.modules.movement.IceSpeed;
import org.spray.heaven.features.module.modules.movement.InventoryMove;
import org.spray.heaven.features.module.modules.movement.Jesus;
import org.spray.heaven.features.module.modules.movement.JumpWalk;
import org.spray.heaven.features.module.modules.movement.LongJump;
import org.spray.heaven.features.module.modules.movement.NoClip;
import org.spray.heaven.features.module.modules.movement.NoJumpDelay;
import org.spray.heaven.features.module.modules.movement.NoSlowSneak;
import org.spray.heaven.features.module.modules.movement.NoSlowdown;
import org.spray.heaven.features.module.modules.movement.SafeWalk;
import org.spray.heaven.features.module.modules.movement.Speed;
import org.spray.heaven.features.module.modules.movement.Spider;
import org.spray.heaven.features.module.modules.movement.Sprint;
import org.spray.heaven.features.module.modules.movement.StairSpeed;
import org.spray.heaven.features.module.modules.movement.Step;
import org.spray.heaven.features.module.modules.movement.Strafe;
import org.spray.heaven.features.module.modules.movement.TargetStrafe;
import org.spray.heaven.features.module.modules.movement.WaterLeave;
import org.spray.heaven.features.module.modules.movement.WaterSpeed;
import org.spray.heaven.features.module.modules.movement.WebLeave;
import org.spray.heaven.features.module.modules.player.*;
import org.spray.heaven.features.module.modules.render.*;
import org.spray.heaven.protect.events.ClientInitializeEvent;

public class ModuleRegister {

	private final ConcurrentHashMap<String, Module> modules = new ConcurrentHashMap<String, Module>();

	public ModuleRegister(ClientInitializeEvent event) {
		event.check();
		
		// Display
		addModule(new TargetHUD());
		addModule(new Watermark());
		addModule(new Notifications());
		addModule(new ArrayListMod());
		addModule(new PotionEffects());
		
		// Visual
		addModule(new Tracers());
		addModule(new ChinaHat());
		addModule(new ESP());
		addModule(new NameTags());
		addModule(new ViewModel());
		addModule(new CustomModels());
		addModule(new CameraClip());
		addModule(new DamageParticles());
		addModule(new Brightness());
		addModule(new ItemPhysics());
		addModule(new Trails());
		addModule(new NoRender());
		addModule(new HitAnimation());
		addModule(new ClickUIMod());
		addModule(new JumpCircles());
		addModule(new BlockOverlay());

		// Combat
		addModule(new AntiCrystal());
		addModule(new SuperBow());
		addModule(new AutoTotem());
		addModule(new AutoArmor());
		addModule(new BowAim());
		addModule(new Criticals());
		addModule(new HitBoxes());
		addModule(new KillAura());
		addModule(new NoFriendDamage());
		addModule(new NoFriendDamage());
		addModule(new SuperKnockBack());
		addModule(new TriggerBot());
		addModule(new Velocity());
		addModule(new BetterBow());
//		addModule(new DeathBow());
//        addModule(new FastBow()); better bow...

		// Player
		addModule(new AntiBot());
		addModule(new AutoLeave());
		addModule(new AutoPotion());
		addModule(new AutoTool());
		addModule(new HandFlicker());
		addModule(new NoServerRotation());
		addModule(new FreeCam());
		addModule(new NoWaterCollision());
		addModule(new MiddleClickFriend());
		addModule(new MiddleClickPearl());
		addModule(new AntiAFK());
		addModule(new AutoFish());
		addModule(new NoInteract());
		addModule(new XCarry());
		addModule(new AutoGapple());
		addModule(new FastPlace());
		addModule(new NoPush());
		addModule(new ItemScroller());
		addModule(new NoWeb());
//        addModule(new Teleport());
		addModule(new ChestStealer());
		addModule(new AirStuck());
//        addModule(new AntiVoid());
		addModule(new AutoRespawn());
		addModule(new InventoryDroper());

		// Misc
		addModule(new TimerMod());
		addModule(new GAppleCooldown());
		addModule(new WorldTime());
		addModule(new AutoJoin());
		addModule(new AutoTPAccept());
		addModule(new DiscordRPCMod());
		addModule(new FakePlayer());
		addModule(new AutoMath());
		addModule(new StaffAlert());
		addModule(new NameProtect());
		addModule(new ChatHistory());
		addModule(new BPS());

		// Movement
		addModule(new BedrockClip());
		addModule(new DamageFly());
		addModule(new Eagle());
		addModule(new HighJump());
		addModule(new JumpWalk());
		addModule(new Strafe());
        addModule(new IceSpeed()); 
		addModule(new InventoryMove());
		addModule(new Jesus());
		addModule(new LongJump());
		addModule(new NoClip());
		addModule(new NoJumpDelay());
		addModule(new NoSlowSneak());
		addModule(new NoSlowdown());
		addModule(new SafeWalk());
		addModule(new Speed());
		addModule(new Spider());
		addModule(new ElytraStrafe());
		addModule(new Sprint());
		addModule(new Step());
		addModule(new TargetStrafe());
		addModule(new WaterLeave());
		addModule(new WaterSpeed());
		addModule(new WebLeave());
		addModule(new Fly());
//		addModule(new Phase());
//        addModule(new AirJump());
		addModule(new AntiLevitation());
		addModule(new StairSpeed());
//        addModule(new WebProtect());
	}

	public ConcurrentHashMap<String, Module> getModules() {
		return modules;
	}

	public void addModule(Module module) {
		modules.put(module.getName().toLowerCase(), module);
	}

	public <T extends Module> T get(String name) {
		return (T) getModules().get(name.toLowerCase());
	}

	public List<Module> getByCategory(Category category) {
		List<Module> list = new ArrayList<>();
		getModules().forEach((k, m) -> {
			if (m.getCategory() == category)
				list.add(m);
		});
		return list;
	}

	// Hooks
	public void onRender(int width, int height, float tickDelta) {
		modules.values().stream().filter(module -> module.isEnabled())
				.forEach(module -> module.onRender(width, height, tickDelta));
	}

	public void onUpdate() {
		modules.values().stream().filter(module -> module.isEnabled()).forEach(module -> module.onUpdate());
	}

	public void onKeyPressed(int key) {
		if (key == 0)
			return;

		getModules().values().forEach(m -> {
			m.getSettings().forEach(s -> s.onKeyPressed(key));

			if (m.getKey() == key)
				m.updateState();
		});
	}

}
