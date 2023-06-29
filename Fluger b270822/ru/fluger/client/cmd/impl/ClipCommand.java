/*
 * Decompiled with CFR 0.150.
 */
package ru.fluger.client.cmd.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import ru.fluger.client.cmd.CommandAbstract;
import ru.fluger.client.helpers.misc.ChatHelper;

public class ClipCommand
extends CommandAbstract {
    Minecraft mc = Minecraft.getMinecraft();

    public ClipCommand() {
        super("vclip", "vclip | hclip", "\ufffd6.vclip | (hclip) \ufffd7<value>", "vclip", "hclip");
    }

    @Override
    public void execute(String ... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("vclip")) {
                try {
                    int i;
                    ChatHelper.addChatMessage((Object)((Object)TextFormatting.GREEN) + "Successfully vclipped " + Double.valueOf(args[1]) + " blocks.");
                    for (i = 0; i < 10; ++i) {
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, false));
                    }
                    for (i = 0; i < 10; ++i) {
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args[1]), this.mc.player.posZ, false));
                    }
                    this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args[1]), this.mc.player.posZ);
                }
                catch (Exception i) {
                    // empty catch block
                }
            }
            if (args[0].equalsIgnoreCase("hclip")) {
                try {
                    ChatHelper.addChatMessage((Object)((Object)TextFormatting.GREEN) + "Successfully hclipped " + Double.valueOf(args[1]) + " blocks.");
                    float f = this.mc.player.rotationYaw * ((float)Math.PI / 180);
                    double speed = Double.valueOf(args[1]);
                    double x = -((double)MathHelper.sin(f) * speed);
                    double z = (double)MathHelper.cos(f) * speed;
                    this.mc.player.setPosition(this.mc.player.posX + x, this.mc.player.posY, this.mc.player.posZ + z);
                }
                catch (Exception exception) {}
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

