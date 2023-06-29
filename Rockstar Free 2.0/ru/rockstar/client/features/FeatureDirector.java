package ru.rockstar.client.features;

import net.minecraft.client.Minecraft;
import ru.rockstar.client.features.impl.combat.*;
import ru.rockstar.client.features.impl.display.*;
import ru.rockstar.client.features.impl.misc.*;
import ru.rockstar.client.features.impl.movement.*;
import ru.rockstar.client.features.impl.player.*;
import ru.rockstar.client.features.impl.visuals.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FeatureDirector {
    public static ArrayList<Feature> features = new ArrayList<Feature>();

    public FeatureDirector() {

        // Combat.
    	features.add(new AppleGoldenTimer());
    	features.add(new AimBot());
    	features.add(new AntiAim());
    	features.add(new Aura());
        features.add(new NoFriendDamage());
        features.add(new SuperKnockBack());
        features.add(new TriggerBot());
        features.add(new AutoArmor());
        features.add(new ShieldDesync());
        features.add(new CrystalAura());
        features.add(new AutoShield());
        features.add(new AutoGapple());
        features.add(new AutoTotem());
        features.add(new KillAura());
        features.add(new AntiBot());
        features.add(new AutoPotion());
        features.add(new Velocity());
        features.add(new HitBox());
        features.add(new AntiCrystal());
        features.add(new FastBow());
        features.add(new TargetStrafe());
        features.add(new PushAttack());
        features.add(new Reach());

        // Movement.
        features.add(new SafeWalk());
        features.add(new ExploitFly());
        features.add(new GPS());
        features.add(new ElytraStrafe());
        features.add(new HighJump());
        features.add(new DamageFly());
        features.add(new NoSlowDown());
        features.add(new WaterSpeed());
        features.add(new AutoSprint());
        features.add(new AirJump());
        features.add(new GuiMove());
        features.add(new NoFall());
        features.add(new FastClimb());
        features.add(new NoWeb());
        features.add(new LongJump());
        features.add(new Flight());
        features.add(new Timer());
        features.add(new Speed());
        features.add(new Jesus());
        features.add(new Step());
        features.add(new Strafe());
        features.add(new BedrockClip());
        features.add(new SelfDamage());
        // Visuals.
        features.add(new Particles());
        features.add(new Pets());
        features.add(new DamageMarkers());
        features.add(new Wings());
        features.add(new Nimb());
        features.add(new SwingAnimations());
        features.add(new Breadcrumbs());
        features.add(new NameProtect());
        features.add(new NightMode());
        features.add(new ChestEsp());
        features.add(new PearlESP());
        features.add(new EnchantmentColor());
        features.add(new Crosshair());
        features.add(new WaterLeave());
        features.add(new FullBright());
        features.add(new JumpCircle());
        features.add(new ScoreBoard());
        features.add(new ArmorHUD());
        features.add(new XRay());
        features.add(new PenisESP());

        features.add(new ItemESP());
        features.add(new FogColor());
        features.add(new Weather());
        features.add(new ViewModel());
        features.add(new TargetESP());
        features.add(new ChinaHat());
        features.add(new NameTags());
        features.add(new NoRender());
        features.add(new Tracers());
        features.add(new ESP());
        features.add(new Chams());

        // Player.
        features.add(new AirStealer());
        features.add(new RightClickPearl());
        features.add(new FastEat());
        features.add(new InventoryManager());
        features.add(new BedBreaker());
        features.add(new DetectPlayer());
        features.add(new MiddleClickPearl());
        features.add(new KeepSprint());
        features.add(new ChestStealer());
        features.add(new NoInteract());
        features.add(new NoDelay());
        features.add(new XCarry());
        features.add(new Spider());
        features.add(new ItemScroller());
        features.add(new SpeedMine());
        features.add(new DeathCoordinates());
        features.add(new FreeCam());
        features.add(new AutoAuth());
        features.add(new NoServerRotation());
        features.add(new AutoFarm());
        features.add(new Scaffold());
        features.add(new NoPush());
        features.add(new NoClip());
        features.add(new SlowFall());

        // Misc.
        features.add(new TeleportBack());
        features.add(new AutoFold());
        features.add(new Find());
        features.add(new FakeHack());
        features.add(new BeaconRadius());
        features.add(new KTLeave());
        features.add(new DeathSounds());
        features.add(new DiscordRPC());
        features.add(new StaffAlert());
        features.add(new ModuleSoundAlert());
        features.add(new FastWorldLoad());
        features.add(new Teleport());
        features.add(new DuelAccept());
        features.add(new AutoAccept());
        features.add(new Disabler());
        features.add(new Panic());
        features.add(new MCF());

        // Display.
        features.add(new ru.rockstar.client.features.impl.display.ArrayList());
        features.add(new InventoryPreview());
        features.add(new PotionIndicator());
        features.add(new StaffIndicator());
        features.add(new Keystrokes());
        features.add(new KeyBinds());
        features.add(new TimerIndicator());
        features.add(new DamageInfo());
        features.add(new Watermark());
        features.add(new ClickGUI());
     //   features.add(new HUD());

        features.add(new Hotbar());

        features.add(new ClientFont());
        features.add(new Notifications());

        features.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().neverlose500_15.getStringWidth(((Feature) m).getLabel())).reversed());
    }

    public List<Feature> getFeatureList() {
        return this.features;
    }

    public List<Feature> getFeaturesForCategory(Category category) {
        List<Feature> featureList = new ArrayList<>();
        for (Feature feature : getFeatureList()) {
            if (feature.getCategory() == category) {
                featureList.add(feature);
            }
        }
        return featureList;
    }

    public Feature getFeatureByClass(Class<? extends Feature> classFeature) {
        for (Feature feature : getFeatureList()) {
            if (feature != null) {
                if (feature.getClass() == classFeature) {
                    return feature;
                }
            }
        }
        return null;
    }

    public Feature getFeatureByLabel(String name) {
        for (Feature feature : getFeatureList()) {
            if (feature.getLabel().equals(name)) {
                return feature;
            }
        }
        return null;
    }
}
