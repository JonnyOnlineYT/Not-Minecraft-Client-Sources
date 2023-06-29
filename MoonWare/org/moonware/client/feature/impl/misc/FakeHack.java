package org.moonware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import org.moonware.client.utils.MWUtils;
import org.moonware.client.event.EventTarget;
import org.moonware.client.event.events.impl.player.EventPreMotion;
import org.moonware.client.feature.Feature;
import org.moonware.client.feature.impl.Type;
import org.moonware.client.helpers.math.RotationHelper;
import org.moonware.client.settings.impl.BooleanSetting;
import org.moonware.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Iterator;

public class FakeHack extends Feature {

    public static ArrayList<String> fakeHackers = new ArrayList<String>();
    private final BooleanSetting hackerSneak;
    private final BooleanSetting hackerSpin;
    private final NumberSetting hackerAttackDistance;
    public float rot;

    public FakeHack() {
        super("FakeHack", "Позволяет сделать легитного игрока читером", Type.Other);
        hackerAttackDistance = new NumberSetting("Hacker Attack Range", 3, 1, 7, 1, () -> true);
        hackerSneak = new BooleanSetting("Hacker Sneaking", false, () -> true);
        hackerSpin = new BooleanSetting("Hacker Spin", false, () -> true);
        addSettings(hackerAttackDistance, hackerSneak, hackerSpin);
    }

    public static boolean isFakeHacker(EntityPlayer player) {
        for (String name : fakeHackers) {
            EntityPlayer en = Minecraft.world.getPlayerEntityByName(name);
            if (en == null) {
                return false;
            }
            if (player.isEntityEqual(en)) {
                return true;
            }
        }
        return false;
    }

    public static void removeHacker(EntityPlayer entityPlayer) {
        Iterator<String> hackers = fakeHackers.iterator();
        while (hackers.hasNext()) {
            String name = hackers.next();
            if (Minecraft.world.getPlayerEntityByName(name) == null) {
                continue;
            }
            if (entityPlayer.isEntityEqual(Minecraft.world.getPlayerEntityByName(name))) {
                Minecraft.world.getPlayerEntityByName(name).setSneaking(false);
                hackers.remove();
            }
        }
    }

    @Override
    public void onDisable() {
        for (String name : fakeHackers) {
            if (hackerSneak.getBoolValue()) {
                EntityPlayer player = Minecraft.world.getPlayerEntityByName(name);
                assert player != null;
                player.setSneaking(false);
                player.setSprinting(false);
            }
        }
        super.onDisable();
    }

    @Override
    public void onEnable() {
        MWUtils.sendChat("To use this function write - " + ".fakehack add (nick)");
        fakeHackers.clear();
        super.onEnable();
    }

    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
        for (String name : fakeHackers) {
            EntityPlayer player = Minecraft.world.getPlayerEntityByName(name);
            if (player == null) {
                return;
            }
            if (hackerSneak.getBoolValue()) {
                player.setSneaking(true);
                player.setSprinting(true);
            } else {
                player.setSneaking(false);
                player.setSprinting(false);
            }
            float[] rots = RotationHelper.getFacePosRemote(player, Minecraft.player, true);
            float hackerReach = hackerAttackDistance.getNumberValue();
            if (!hackerSpin.getBoolValue()) {
                if (player.getDistanceToEntity(Minecraft.player) <= hackerReach) {
                    player.rotationYaw = rots[0];
                    player.rotationYawHead = rots[0];
                    player.rotationPitch = rots[1];
                }
            } else {
                float speed = 30;
                float yaw = ((float) (Math.floor(spinAim(speed))));
                player.rotationYaw = yaw;
                player.rotationYawHead = yaw;
            }

            if (Minecraft.player.ticksExisted % 4 == 0 && player.getDistanceToEntity(Minecraft.player) <= hackerReach) {
                player.swingArm(EnumHand.MAIN_HAND);
                if (Minecraft.player.getDistanceToEntity(player) <= hackerReach) {
                    Minecraft.player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, 1.0F, 1.0F);
                }
            }
            if (Minecraft.player.getDistanceToEntity(player) > hackerReach && !hackerSneak.getBoolValue() && !hackerSpin.getBoolValue()) {
                float yaw = 75;
                player.rotationYaw = yaw;
                player.rotationPitch = 0;
                player.rotationYawHead = yaw;
            }
        }
    }

    public float spinAim(float rots) {
        rot += rots;
        return rot;
    }
}
