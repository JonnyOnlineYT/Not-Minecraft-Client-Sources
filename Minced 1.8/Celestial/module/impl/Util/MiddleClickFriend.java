package Celestial.module.impl.Util;

import Celestial.event.EventTarget;
import Celestial.event.events.impl.input.EventMouse;
import Celestial.friend.Friend;
import Celestial.module.Module;
import Celestial.module.ModuleCategory;
import Celestial.utils.Helper;
import Celestial.utils.other.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import Celestial.Smertnix;
import net.minecraft.entity.player.EntityPlayer;

public class MiddleClickFriend extends Module {


    public MiddleClickFriend() {
        super("MiddleClickFriend", "Добавить игрока в друзья(колесиком мыши)", ModuleCategory.Util);
    }

    @EventTarget
    public void onMouseEvent(EventMouse event) {
        if (event.getKey() == 2 && Helper.mc.pointedEntity instanceof EntityPlayer) {
            if (Smertnix.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(Helper.mc.pointedEntity.getName()))) {
                Smertnix.instance.friendManager.getFriends().remove(Smertnix.instance.friendManager.getFriend(Helper.mc.pointedEntity.getName()));
                ChatUtils.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + Helper.mc.pointedEntity.getName() + "'" + " as Friend!");
            } else {
                Smertnix.instance.friendManager.addFriend(new Friend(Helper.mc.pointedEntity.getName()));
                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + "'" + Helper.mc.pointedEntity.getName() + "'" + " as Friend!");
            }
        }
    }
}