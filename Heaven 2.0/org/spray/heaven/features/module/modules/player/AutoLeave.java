package org.spray.heaven.features.module.modules.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.MathUtil;

import java.util.Arrays;

@ModuleInfo(name = "AutoLeave", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AutoLeave extends Module {

    private final Setting mode =
            register(new Setting("Mode", "/spawn", Arrays.asList("/spawn", "/home", "/hub", "/lobby")));
    private final Setting radius = register(new Setting("Radius", 30, 1, 100));

    private final Setting ignoreFriends = register(new Setting("Ignore Friends", true));

    @Override
    public void onUpdate() {
        for (EntityPlayer player : mc.world.playerEntities) {
            if (player == mc.player || ignoreFriends.isToggle() && Wrapper.getFriend().contains(player.getName()))
                continue;

            double distance = mc.player.getDistanceToEntity(player);
            if (distance <= radius.getValue()) {
                Wrapper.message(ChatFormatting.YELLOW + "[AutoLeave]: " + ChatFormatting.WHITE + "замечен игрок " +
                        ChatFormatting.GREEN + player.getName() + ChatFormatting.WHITE + " в радиусе " +
                        ChatFormatting.GREEN + MathUtil.round(distance, 1));
                mc.player.sendChatMessage(mode.getCurrentMode());
                disable();
            }
        }
    }

}
