package org.spray.heaven.features.module.modules.combat;

import org.lwjgl.input.Keyboard;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;

@ModuleInfo(name = "DeathBow", category = Category.COMBAT, visible = true, key = Keyboard.KEY_NONE)
public class DeathBow extends Module {

    private Setting damageLevel = register(new Setting("Damage Level", 50, 0, 100));

    private boolean shooting;
    private long lastShootTime;

    @Override
    public void onEnable() {
        shooting = false;
        lastShootTime = System.currentTimeMillis();
    }

    @EventTarget
    public void onPacketSend(PacketEvent event) {
        if (!event.getType().equals(EventType.SEND))
            return;

        if (event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                ItemStack handStack = mc.player.getHeldItem(EnumHand.MAIN_HAND);
                if (!handStack.isEmpty()) {
                    handStack.getItem();
                    if (handStack.getItem() instanceof ItemBow && mc.player.getFoodStats().getFoodLevel() > 1)
                        doSpoofs();

                }
            }
        }
    }

    private void doSpoofs() {
        if (System.currentTimeMillis() - lastShootTime >= 0) {
            shooting = true;
            lastShootTime = System.currentTimeMillis();
            mc.player.connection
                    .sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
            for (int i = 0; i < damageLevel.getValue(); ++i) {
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
                        mc.player.posY - 10.0000000001f, mc.player.posZ, false));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,
                        mc.player.posY + 10.0000000001f, mc.player.posZ, false));
            }
            shooting = false;
        }
    }
}