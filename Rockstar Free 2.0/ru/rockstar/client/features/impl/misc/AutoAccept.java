package ru.rockstar.client.features.impl.misc;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventPreMotionUpdate;
import ru.rockstar.api.event.event.EventReceivePacket;
import ru.rockstar.api.utils.friend.Friend;
import ru.rockstar.api.utils.friend.FriendManager;
import ru.rockstar.api.utils.world.TimerHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.ListSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class AutoAccept extends Feature
{
	public BooleanSetting onlyFriends = new BooleanSetting("OnlyFriends", true, () -> true);
    public AutoAccept() {
        super("AutoAccept", "Автоматически принимает запросы на телепортацию", 0, Category.MISC);
        addSettings(onlyFriends);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        final SPacketChat message = (SPacketChat)e.getPacket();
        if (message.getChatComponent().getFormattedText().contains("\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd")) {
            if (this.onlyFriends.getBoolValue()) {
                for (final Friend friend : Main.instance.friendManager.getFriends()) {
                    if (message.getChatComponent().getFormattedText().contains(friend.getName())) {
                        if (!this.timerHelper.hasReached((double) 300)) {
                            continue;
                        }
                        mc.player.sendChatMessage("/tpaccept");
                        this.timerHelper.reset();
                    }
                }
            }
            else if (this.timerHelper.hasReached((double) 300)) {
                mc.player.sendChatMessage("/tpaccept");
                this.timerHelper.reset();
            }
        }
    }
}
