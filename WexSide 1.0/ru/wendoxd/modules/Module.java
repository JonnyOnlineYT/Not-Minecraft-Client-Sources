package ru.wendoxd.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import ru.wendoxd.WexSide;
import ru.wendoxd.events.Event;
import ru.wendoxd.modules.impl.combat.*;
import ru.wendoxd.modules.impl.movement.*;
import ru.wendoxd.modules.impl.player.*;
import ru.wendoxd.modules.impl.visuals.*;
import ru.wendoxd.ui.menu.elements.Frame;
import ru.wendoxd.ui.menu.elements.tabelements.*;
import ru.wendoxd.ui.menu.utils.MenuAPI;
import ru.wendoxd.utils.combat.CastHelper;
import ru.wendoxd.utils.visual.RenderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Module {
	protected static Minecraft mc;
	protected static Frame visuals_hud;
	protected static Frame visuals_entities;
	protected static Frame visuals_menu;
	protected static Frame modelChangerFrame;
	public static Struct<EntitiesTab> visuals_entitiestab;
	public static Slider oofdist;
	public static CheckBox modelChanger;
	public static SelectBox change_sword;
	public static SelectBox change_shield;
	public static SelectBox change_totem;
	public static CheckBox visuals_menu_blur;
	public static CheckBox visuals_menu_desc;
	public static ColorPicker visuals_menu_background;

	public static void registerModules() {
		mc = Minecraft.getMinecraft();
		visuals_hud = new Frame("HUD").ignoreSort();
		visuals_entities = new Frame("Entities").ignoreSort();
		visuals_menu = new Frame("Menu").ignoreSort();
		modelChangerFrame = new Frame("ModelChanger");
		initMenu();
		MenuAPI.visuals.register(visuals_entities, modelChangerFrame);
		MenuAPI.hud.register(visuals_hud, visuals_menu);
		WexSide.getModules().forEach(Module::initSettings);
		visuals_entitiestab = new Struct<>(new EntitiesTab(), visuals_entities, "Target", "Players", "Mobs", "Animals",
				"Villagers", "Friends", "Self");
		oofdist = new Slider("OOF", 1, 5, 50, 0.5, () -> {
			for (int i = 0; i < 5; i++) {
				CheckBox cb = visuals_entitiestab.get(i).oofarrows;
				if (cb != null && cb.isEnabled())
					return true;
			}
			return false;
		});
		modelChanger = new CheckBox("ModelChanger");
		change_sword = new SelectBox("Sword", new String[] { "Default", "Ice" }, () -> modelChanger.isEnabled(true));
		change_totem = new SelectBox("Totem", new String[] { "Default", "Joker" }, () -> modelChanger.isEnabled(true));
		change_shield = new SelectBox("Shield", new String[] { "Default", "Pig" }, () -> modelChanger.isEnabled(true));
		modelChangerFrame.register(modelChanger, change_sword, change_totem, change_shield);
		visuals_menu.register(visuals_menu_blur = new CheckBox("Background Blur"));
		visuals_menu.register(visuals_menu_background = new ColorPicker("Background"));
		visuals_menu.register(visuals_menu_desc = new CheckBox("Descriptions"));
		visuals_menu_desc.setEnabled(true);
		visuals_menu_background.getColor().setRGB(60, 60, 255);
		visuals_hud.register(oofdist);
		iterateButtons(PrimaryButton::releasePath);
		iterateButtons(primaryButton -> iterateButtons(pb -> {
			if (primaryButton != pb && primaryButton.getPath().equals(pb.getPath())) {
				Minecraft.LOGGER.info("Path " + primaryButton.getPath() + " already exists.");
			}
		}));
	}

	public static void iterateButtons(Consumer<PrimaryButton> consumer) {
		for (ru.wendoxd.ui.menu.elements.Tab tab : MenuAPI.tabs) {
			for (Frame frame : tab.getFrames()) {
				for (PrimaryButton button : frame.getButtons()) {
					consumer.accept(button);
				}
			}
		}
	}

	public static void initMenu() {
		WexSide.registerModule(new Aura(), new AntiBot(), new Auto(), new AutoGApple(), new FastBow(), new HitBoxes(),
				new KeepSprint(), new PushAttack(), new Reach(), new SuperBow(), new TargetStrafe(), new Velocity(),
				new CrystalAura(), new AntiLevitation(), new Speed(), new BaritoneHandler(), new HighJump(),
				new Eagle(), new Flight(), new FastLadder(), new Jesus(), new AutoSprint(), new Step(), new Spider(),
				new SafeWalk(), new Timer(), new WaterLeave(), new WaterSpeed(), new AirJump(), new RodLeave(),
				new Weather(), new ESP(), new Tracers(), new Title(), new SwingAnimations(), new ItemESP(),
				new Trails(), new Chams(), new TargetHUD(), new NoOverlay(), new FullBright(), new HUD(), new Trails(),
				new JumpCircles(), new DiscordRPC(), new BetterChat(), new BlockESP(), new EnchantmentColor(),
				new StreamerMode(), new ItemPhysics(), new OutOfFovArrows(), new Keybinds(), new Notifications(),
				new ChunkAnimator(), new Indicators(), new Crosshair(), new Glow(), new AutoRespawn(),
				new AutoRegister(), new AutoTPAccept(), new AntiPush(), new AntiAFK(), new AutoPotion(), new AutoTool(),
				new ChestStealer(), new DeathCoordinates(), new FreeCam(), new GuiWalk(), new ItemScroller(),
				new NoDelay(), new NoFall(), new NoWeb(), new NoClip(), new NoInteract(), new PingSpoof(),
				new StaffAlert(), new SpeedMine(), new Scaffold(), new XCarry(), new MiddleClickFriend(),
				new NoServerRotation(), new Helper(), new ChinaHat(), new EdgeJump(), new NoSlowDown(), new AutoFish(),
				new AutoLeave(), new NoDamageFriend(), new AutoArmor(), new PortalGodMode(),
				new ClientSounds(), new AntiInvisible(), new TriggerBot(), new LongJump(), new BedrockClip(),
				new GlideFly(), new HitChams(), new WexMovement(), new PlayerFinder(), new ElytraFix());
	}
	public void update_animation() {
	}
	protected void initSettings() {
	}

	public void onEvent(Event event) {
	}

	public static double getEyeHeight(EntityLivingBase entity) {
		double eyeHeight = entity.getEyeHeight();
		if (mc.player.isSneaking()) {
			eyeHeight -= 0.2;
		}
		if (entity instanceof EntityPlayer) {
			CastHelper castHelper = new CastHelper();
			castHelper.apply(CastHelper.EntityType.PLAYERS);
			castHelper.apply(CastHelper.EntityType.FRIENDS);
			castHelper.apply(CastHelper.EntityType.SELF);
			CastHelper.EntityType type;
			if ((type = CastHelper.isInstanceof(entity, castHelper.build())) != null
					&& RenderUtils.isInViewFrustum(entity)) {
				SelectBox box = visuals_entitiestab.get(type.ordinal()).model;
				int s = box.get();
				if (s == 1) {
					eyeHeight -= 0.2;
				}
				if (s == 2) {
					eyeHeight += 0.3;
				}
				if (s == 3) {
					eyeHeight += 0.1;
				}
				if (s == 4) {
					eyeHeight += 0.15;
				}
			}
		}
		return eyeHeight + 0.15;
	}

	public static class Struct<T extends Tab> {
		private final List<T> tabs = new ArrayList<>();

		public Struct(T tab, Frame contextFrame, String name, String... conditions) {
			SelectBox selectBox = new SelectBox(name, conditions);
			contextFrame.register(selectBox);
			for (int i = 0; i < conditions.length; i++) {
				Tab t = tab.register(selectBox, i);
				for (PrimaryButton button : t.buttons) {
					contextFrame.register(button);
					button.modifyPath("_" + i);
				}
				this.tabs.add((T) t);
			}
		}

		public T get(int id) {
			return this.tabs.get(id);
		}
	}

	public static abstract class Tab {
		private final List<PrimaryButton> buttons = new ArrayList<>();
		private Tab contextTab;

		static Supplier<Boolean> buildCondition(SelectBox box, int id) {
			return () -> box.get() == id;
		}

		public abstract Tab register(SelectBox box, int id);

		public void setContextTab(Tab contextTab) {
			this.contextTab = contextTab;
		}

		public void register(PrimaryButton button, SelectBox box, int id) {
			button.setEnabledCondition(buildCondition(box, id));
			button.ignoreVisible();
			this.contextTab.buttons.add(button);
		}

		public void register(PrimaryButton button, SelectBox box, int id, CheckBox owner) {
			button.addVisibleCondition(() -> box.get() == id && owner.isEnabled(true));
			button.setEnabledCondition(owner::isEnabled);
			button.ignoreVisible();
			this.contextTab.buttons.add(button);
		}
	}

	public static class EntitiesTab extends Tab {
		public CheckBox box, health, trails, jumpcircles, chinahat, glow, wallhack, chams, tracers, oofarrows;
		public SelectBox model;
		public MultiSelectBox info;

		@Override
		public Tab register(SelectBox sb, int id) {
			EntitiesTab tab = new EntitiesTab();
			setContextTab(tab);
			register(tab.box = new CheckBox("Box").markColorPicker(), sb, id);
			register(tab.health = new CheckBox("Health"), sb, id);
			if (id == 0 || id == 4) {
				register(tab.info = new MultiSelectBox("Info",
						new String[] { "Name", "Armor", "MainHand", "OffHand", "Potions", "Shadow" }), sb, id);
			}
			if (id == 0) {
				register(tab.glow = new CheckBox("Outline").markColorPicker(), sb, id);
			}
			register(tab.chinahat = new CheckBox("China Hat").markColorPicker(), sb, id);
			register(tab.wallhack = new CheckBox("WallHack"), sb, id);
			register(tab.chams = new CheckBox("Chams").markColorPicker(), sb, id);
			if (id != 5) {
				register(tab.oofarrows = new CheckBox("OOF Arrows").markColorPicker(), sb, id);
				register(tab.tracers = new CheckBox("Tracers").markColorPicker(), sb, id);
			}
			if (id == 0 || id == 4 || id == 5) {
				register(tab.model = new SelectBox("Model",
						new String[] { "Default", "Amogus", "Demon", "Rabbit", "Sonic" }), sb, id);
			}
			if (id == 0 || id == 4 || id == 5) {
				register(tab.trails = new CheckBox("Trails").markColorPicker(), sb, id);
				register(tab.jumpcircles = new CheckBox("JumpCircles").markColorPicker(), sb, id);
			}
			return tab;
		}
	}
}