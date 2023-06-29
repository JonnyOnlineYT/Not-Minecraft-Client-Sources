package org.spray.infinity.features;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.spray.infinity.features.module.combat.AimBot;
import org.spray.infinity.features.module.combat.AntiBot;
import org.spray.infinity.features.module.combat.AutoArmor;
import org.spray.infinity.features.module.combat.AutoClicker;
import org.spray.infinity.features.module.combat.AutoShield;
import org.spray.infinity.features.module.combat.AutoShift;
import org.spray.infinity.features.module.combat.AutoTotem;
import org.spray.infinity.features.module.combat.BetterBow;
import org.spray.infinity.features.module.combat.BowAim;
import org.spray.infinity.features.module.combat.ClickAura;
import org.spray.infinity.features.module.combat.Criticals;
import org.spray.infinity.features.module.combat.CrossbowAim;
import org.spray.infinity.features.module.combat.HitBoxes;
import org.spray.infinity.features.module.combat.KillAura;
import org.spray.infinity.features.module.combat.Reach;
import org.spray.infinity.features.module.combat.TriggerBot;
import org.spray.infinity.features.module.combat.Velocity;
import org.spray.infinity.features.module.hidden.AntiFabric;
import org.spray.infinity.features.module.hidden.Menu;
import org.spray.infinity.features.module.misc.DiscordRPCMod;
import org.spray.infinity.features.module.misc.NameProtect;
import org.spray.infinity.features.module.movement.AntiWaterPush;
import org.spray.infinity.features.module.movement.Eagle;
import org.spray.infinity.features.module.movement.Fly;
import org.spray.infinity.features.module.movement.InvWalk;
import org.spray.infinity.features.module.movement.Jesus;
import org.spray.infinity.features.module.movement.NoFall;
import org.spray.infinity.features.module.movement.NoSwim;
import org.spray.infinity.features.module.movement.SafeWalk;
import org.spray.infinity.features.module.movement.Speed;
import org.spray.infinity.features.module.movement.Spider;
import org.spray.infinity.features.module.movement.Sprint;
import org.spray.infinity.features.module.movement.Step;
import org.spray.infinity.features.module.movement.TargetStrafe;
import org.spray.infinity.features.module.player.AntiAim;
import org.spray.infinity.features.module.player.AutoEat;
import org.spray.infinity.features.module.player.AutoFish;
import org.spray.infinity.features.module.player.AutoLeave;
import org.spray.infinity.features.module.player.AutoPotion;
import org.spray.infinity.features.module.player.AutoTool;
import org.spray.infinity.features.module.player.ChatCalculator;
import org.spray.infinity.features.module.player.ChestSteal;
import org.spray.infinity.features.module.player.FakeLags;
import org.spray.infinity.features.module.player.FastBreak;
import org.spray.infinity.features.module.player.FastEXP;
import org.spray.infinity.features.module.player.FreeCam;
import org.spray.infinity.features.module.player.MClickPearl;
import org.spray.infinity.features.module.player.NoSlow;
import org.spray.infinity.features.module.player.PacketKick;
import org.spray.infinity.features.module.player.PingSpoof;
import org.spray.infinity.features.module.player.Refill;
import org.spray.infinity.features.module.player.Scaffold;
import org.spray.infinity.features.module.player.XCarry;
import org.spray.infinity.features.module.visual.ArmorHUD;
import org.spray.infinity.features.module.visual.ESP;
import org.spray.infinity.features.module.visual.FullBright;
import org.spray.infinity.features.module.visual.HUD;
import org.spray.infinity.features.module.visual.ItemESP;
import org.spray.infinity.features.module.visual.NameTags;
import org.spray.infinity.features.module.visual.NoFire;
import org.spray.infinity.features.module.visual.NoHurtCam;
import org.spray.infinity.features.module.visual.NoServerRotation;
import org.spray.infinity.features.module.visual.ScoreboardMod;
import org.spray.infinity.features.module.visual.ShulkerView;
import org.spray.infinity.features.module.visual.StorageESP;
import org.spray.infinity.features.module.visual.TargetInfo;
import org.spray.infinity.features.module.visual.Tracers;
import org.spray.infinity.features.module.visual.ViewModel;
import org.spray.infinity.features.module.visual.XRay;
import org.spray.infinity.features.module.world.CameraClip;
import org.spray.infinity.features.module.world.MClickFriend;
import org.spray.infinity.features.module.world.NoClip;
import org.spray.infinity.features.module.world.Timer;

import net.minecraft.client.util.math.MatrixStack;

public class ModuleManager {

	private List<Module> modules = Arrays.asList(
			// hidden
			new AntiFabric(),

			new KillAura(), new HUD(), new Sprint(), new Menu(), new Criticals(), new BowAim(), new XRay(),
			new Velocity(), new Scaffold(), new SafeWalk(), new HitBoxes(), new AimBot(), new ClickAura(), new Timer(),
			new AutoTotem(), new AutoClicker(), new MClickPearl(), new AutoTool(), new NoSlow(), new FastEXP(),
			new AutoShield(), new Refill(), new AutoPotion(), new BetterBow(), new NameProtect(), new ChestSteal(),
			new FreeCam(), new AutoEat(), new ChatCalculator(), new ESP(), new Tracers(), new Spider(), new ItemESP(),
			new AutoLeave(), new InvWalk(), new Reach(), new TriggerBot(), new AntiBot(), new AutoArmor(),
			new TargetStrafe(), new StorageESP(), new FullBright(), new XCarry(), new DiscordRPCMod(), new Jesus(),
			new Speed(), new PacketKick(), new FastBreak(), new AutoFish(), new ArmorHUD(), new NameTags(),
			new NoClip(), new FakeLags(), new CameraClip(), new AntiAim(), new PingSpoof(), new Step(),
			new MClickFriend(), new TargetInfo(), new AntiWaterPush(), new AutoShift(), new CrossbowAim(), new Fly(),
			new ShulkerView(), new Eagle(), new NoSwim(), new NoHurtCam(), new NoServerRotation(), new NoFall(),
			new ViewModel(), new ScoreboardMod(), new NoFire());

	public List<Module> getModules() {
		return modules;
	}

	public Module get(Class<?> clas) {
		return getModules().stream().filter(module -> module.getClass() == clas).findFirst().orElse(null);
	}

	public Module getByName(String name) {
		return getModules().stream().filter(module -> module.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public List<Module> getByCategory(Category category) {
		return getModules().stream().filter(m -> m.getCategory().equals(category)).collect(Collectors.toList());
	}

	public List<Module> getEnableModules() {
		return getModules().stream().filter(m -> m.isEnabled() && m.getCategory() != Category.HIDDEN)
				.collect(Collectors.toList());
	}

	public List<Module> getFullEnableModules() {
		return getModules().stream().filter(m -> m.isEnabled()).collect(Collectors.toList());
	}

	// Hooks

	/**
	 * Keyboard class , method = onKey
	 * 
	 * @param keyCode
	 */
	public void onKeyPressed(int keyCode) {
		getModules().forEach(m -> {
			if (m.getKey() == keyCode)
				m.updateState();
		});
	}

	/**
	 * Update Client Player ticks (old method name onUpdate)
	 */
	public void onPlayerTick() {
		getModules().forEach(m -> {
			if (m.isEnabled())
				m.onUpdate();
		});
	}

	/**
	 * Rendering HUD in game (2D)
	 * 
	 * @param scaledWidth
	 * @param scaledHeight
	 */
	public void onRender(MatrixStack matrices, float tickDelta, int scaledWidth, int scaledHeight) {
		getModules().forEach(m -> {
			if (m.isEnabled())
				m.onRender(matrices, tickDelta, scaledWidth, scaledHeight);
		});
	}

}
