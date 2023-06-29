package ua.apraxia.modules.impl.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ua.apraxia.eventapi.EventTarget;
import ua.apraxia.eventapi.events.impl.packet.EventReceivePacket;
import ua.apraxia.eventapi.events.impl.player.EventUpdate;
import ua.apraxia.eventapi.events.impl.render.EventRender2D;
import ua.apraxia.modules.Categories;
import ua.apraxia.modules.Module;
import ua.apraxia.modules.settings.impl.BooleanSetting;
import ua.apraxia.modules.settings.impl.SliderSetting;
import ua.apraxia.utility.other.MoveUtility;

public class FreeCam extends Module {

    private final SliderSetting speed = new SliderSetting("Flying Speed", 0.5F, 0.1F, 1F, 0.1F);


    double x, y, z;

    public FreeCam() {
        super("FreeCam", Categories.Player);
        addSetting(speed);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (mc.player.isDead) {
            toggle();
        }
    }

    public void onEnable() {
        super.onEnable();
        if (mc.player.isDead) {
            toggle();
        }
        x = mc.player.posX;
        y = mc.player.posY;
        z = mc.player.posZ;
        EntityOtherPlayerMP ent = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
        ent.inventory = mc.player.inventory;
        ent.inventoryContainer = mc.player.inventoryContainer;
        ent.setHealth(mc.player.getHealth());
        ent.setPositionAndRotation(this.x, mc.player.getEntityBoundingBox().minY, this.z, mc.player.rotationYaw, mc.player.rotationPitch);
        ent.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-1, ent);
    }

    @EventTarget
    public void onScreen(EventRender2D e) {

    }

    @EventTarget
    public void onPreMotion(EventUpdate e) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        mc.player.motionY = 0;
        if (mc.gameSettings.keyBindJump.pressed) {
            mc.player.motionY = speed.value;
        }
        if (mc.gameSettings.keyBindSneak.pressed) {
            mc.player.motionY = -speed.value;
        }
        mc.player.noClip = true;
        MoveUtility.setSpeed(speed.value);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.player.setPosition(x, y, z);
        mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.01, mc.player.posZ, mc.player.onGround));
        mc.player.capabilities.isFlying = false;
        mc.player.noClip = false;
        mc.world.removeEntityFromWorld(-1);
    }
}
