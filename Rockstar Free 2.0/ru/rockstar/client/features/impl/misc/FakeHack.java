package ru.rockstar.client.features.impl.misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotion;
import ru.rockstar.api.utils.combat.RotationHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class FakeHack extends Feature
{
    public static ArrayList<String> fakeHackers;
    private final BooleanSetting hackerSneak;
    private final BooleanSetting hackerSpin;
    private final NumberSetting hackerAttackDistance;
    public float rot;
    
    public FakeHack() {
        super("FakeHack", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0441\u0434\u0435\u043b\u0430\u0442\u044c \u043b\u0435\u0433\u0438\u0442\u043d\u043e\u0433\u043e \u0438\u0433\u0440\u043e\u043a\u0430 \u0447\u0438\u0442\u0435\u0440\u043e\u043c", 0, Category.MISC);
        this.rot = 0.0f;
        this.hackerAttackDistance = new NumberSetting("Hacker Attack Range", 3.0f, 1.0f, 7.0f, 1.0f, () -> true);
        this.hackerSneak = new BooleanSetting("Hacker Sneaking", false, () -> true);
        this.hackerSpin = new BooleanSetting("Hacker Spin", false, () -> true);
        this.addSettings(this.hackerAttackDistance, this.hackerSneak, this.hackerSpin);
    }
    
    public static boolean isFakeHacker(final EntityPlayer player) {
        for (final String name : FakeHack.fakeHackers) {
            final EntityPlayer en = FakeHack.mc.world.getPlayerEntityByName(name);
            if (en == null) {
                continue;
            }
            if (player.isEntityEqual(en)) {
                return true;
            }
        }
        return false;
    }
    
    public static void removeHacker(final EntityPlayer en) {
        final Iterator<String> hackers = FakeHack.fakeHackers.iterator();
        while (hackers.hasNext()) {
            final String name = hackers.next();
            if (FakeHack.mc.world.getPlayerEntityByName(name) == null) {
                continue;
            }
            if (!en.isEntityEqual(Objects.requireNonNull(FakeHack.mc.world.getPlayerEntityByName(name)))) {
                continue;
            }
            Objects.requireNonNull(FakeHack.mc.world.getPlayerEntityByName(name)).setSneaking(false);
            hackers.remove();
        }
    }
    
    @Override
    public void onDisable() {
        for (final String name : FakeHack.fakeHackers) {
            if (this.hackerSneak.getBoolValue()) {
                final EntityPlayer player = FakeHack.mc.world.getPlayerEntityByName(name);
                assert player != null;
                player.setSneaking(false);
                player.setSprinting(false);
            }
        }
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        for (int i = 0; i < 3; ++i) {
            Main.msg("To use this function write - .fakehack (nick)", true);
        }
        FakeHack.fakeHackers.clear();
        super.onEnable();
    }
    
    @EventTarget
    public void onPreUpdate(final EventPreMotion event) {
        for (final String name : FakeHack.fakeHackers) {
            final EntityPlayer player = FakeHack.mc.world.getPlayerEntityByName(name);
            if (player == null) {
                continue;
            }
            if (this.hackerSneak.getBoolValue()) {
                player.setSneaking(true);
                player.setSprinting(true);
            }
            else {
                player.setSneaking(false);
                player.setSprinting(false);
            }
            final float[] rots = RotationHelper.getFacePosEntityRemote(player, FakeHack.mc.player);
            final float hackerReach = this.hackerAttackDistance.getNumberValue();
            if (!this.hackerSpin.getBoolValue()) {
                if (player.getDistanceToEntity(FakeHack.mc.player) <= hackerReach) {
                    player.rotationYaw = rots[0];
                    player.rotationYawHead = rots[0];
                    player.rotationPitch = rots[1];
                }
            }
            else {
                final float speed = 30.0f;
                final float yaw = (float)Math.floor(this.spinAim(speed));
                player.rotationYaw = yaw;
                player.rotationYawHead = yaw;
            }
            if (FakeHack.mc.player.ticksExisted % 4 == 0 && player.getDistanceToEntity(FakeHack.mc.player) <= hackerReach) {
                player.swingArm(EnumHand.MAIN_HAND);
                if (FakeHack.mc.player.getDistanceToEntity(player) <= hackerReach) {
                    FakeHack.mc.player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0f, 1.0f);
                }
            }
            if (FakeHack.mc.player.getDistanceToEntity(player) <= hackerReach || this.hackerSneak.getBoolValue() || this.hackerSpin.getBoolValue()) {
                continue;
            }
            final float yaw2 = 75.0f;
            player.rotationYaw = yaw2;
            player.rotationPitch = 0.0f;
            player.rotationYawHead = yaw2;
        }
    }
    
    public float spinAim(final float rots) {
        return this.rot += rots;
    }
    
    static {
        FakeHack.fakeHackers = new ArrayList<String>();
    }
}
