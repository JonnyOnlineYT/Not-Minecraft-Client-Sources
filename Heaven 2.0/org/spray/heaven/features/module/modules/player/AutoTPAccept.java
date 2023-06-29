package org.spray.heaven.features.module.modules.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketChat;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.PacketEvent;
import org.spray.heaven.features.misc.FriendManager;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.util.Timer;

@ModuleInfo(name = "AutoTPAccept", category = Category.PLAYER, visible = true, key = Keyboard.KEY_NONE)
public class AutoTPAccept  extends Module {
    /*
    private final Timer timerHelper = new Timer();
    private final Setting friendsOnly = register(new Setting("Use swords", true));
    public final Setting delay = register(new Setting("Min Delay", 300.0D, 0.0D, 300D));



    @EventTarget
    public void onReceivePacket(PacketEvent e) {
        SPacketChat message = (SPacketChat) e.getPacket();
        if (message.getChatComponent().getFormattedText().contains("телепортироваться")) {
            if (this.friendsOnly.isToggle()) {
                for (FriendManager.Friend friend : friendsOnly()) {
                    if (!message.getChatComponent().getFormattedText().contains(friend.getName()) || !this.timerHelper.hasReached(this.delay.getValue()))
                        continue;
                    mc.player.sendChatMessage("/tpaccept");
                    timerHelper.reset();
                }
            } else if (this.timerHelper.hasReached(this.delay.getValue())) {
                mc.player.sendChatMessage("/tpaccept");
                timerHelper.reset();
            }
        }
    }

     */
}
