package splash.client.modules.player;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.client.events.network.EventPacketReceive;

import java.util.Random;

public class Killsults extends Module {
    private String alphabet = "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ ";
    private long last;
	 private String[] randomInsults = {"%player% OH NO Dude dont report me!1!!1"
			    , "[BYPASS]%player% is an inbread retard"
			    , "%player% probably uses Novoline"
			    , "%player% I bet you the kind to pearl away from someone that can fly"
			    , "%player% propbably uses remade flux"
			    , "[BYPASS]%player% lives off child support and alomone",
			    "%player% is so broke he steals from goodwill",
			    "[BYPASS]%player% doesnt have the nword pass, nigger",
			    "[BYPASS]%player% you make the gang street dumb when you talk",
			    "%player% can't pvp so I did the world a favor and ended him",
			    "%player% is probably screaming to moderators right now",
			    "%player% cries when someone says juice isnt good",
			    "%player% died trying to block up",
			    "[BYPASS]%player% cries after getting off pornhub lives",
			    "[BYPASS]%player% I bet you make your own futa porn",
			    "[BYPASS]%player% probably got raped by his dad",
			    "[BYPASS]%player% you are the reason they invented 3 different types of autism, you being the third",
			    "%player% even misquitos avoid your sad ass ",
			    "%player% DIES TRYING TO ENDERPEARL AWAY FROM PEOPLE THAT CAN FLY",
			    "%player% has the functioning level of an autistic chimpanzee",
			    "%player% needs to remember to breath",
			    "%player% loses casual matches on R6S to brand new players",
			    "%player% has gender dysphoria",
	 			"[BYPASS]%player% probably buys condoms he wont use",
	 			"[BYPASS]%player% can only get top from himself"};
	public Killsults() {
		super("Killsults", "Sends a message when you kill someone.", ModuleCategory.PLAYER);
	}

	@Collect
	public void onPacket(EventPacketReceive eventPacketReceive) {
		if ((Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null) && mc.thePlayer.ticksExisted > 20) {
	        try {
	            if (onServer("hypixel") && ((EventPacketReceive) eventPacketReceive).getReceivedPacket() instanceof S02PacketChat) {
	                S02PacketChat s02PacketChat = (S02PacketChat)eventPacketReceive.getReceivedPacket();
	                String txt = s02PacketChat.getChatComponent().getUnformattedText().replace("&", "").replace("§", "");
	                boolean teamsMode = (mc.thePlayer.getTeam() != null && !mc.thePlayer.getTeam().getRegisteredName().toLowerCase().replace("&", "").replace("§", "").equalsIgnoreCase("ablank"));
	                int neededPlayers = 0;
	                if (GuiPlayerTabOverlay.getPlayerList().size() > neededPlayers) {
	                    if ((txt.toLowerCase().contains("killed by " + mc.thePlayer.getName().toLowerCase())) || txt.toLowerCase().contains("turned into space dust by " + mc.thePlayer.getName().toLowerCase())) {
	                        String[] txtSpilt = txt.split(" ");
	                        Random random = new Random();
	                        int rand = random.nextInt(randomInsults.length);
	                        String insult = randomInsults[rand];
	                        if (insult.contains("[BYPASS]")) {
	                            String alphabet = "abcdefghijklmnopqrstuvwxyz";
	                            StringBuilder newMsg = new StringBuilder();
	                            for (char c : insult.replace("%player%", txtSpilt[0]).replace("[BYPASS]", "").toLowerCase().toCharArray()) {
	                                try {
	                                    newMsg.append(this.alphabet.charAt(alphabet.indexOf(c)));
	                                } catch (Exception e) {
	                                    newMsg.append(c);
	                                }
	                            }

	                            if (newMsg.toString().replace("%player%", txtSpilt[0]).replace("[BYPASS]", "").length() < 100) {
	                                mc.thePlayer.sendChatMessage((teamsMode ? "/shout " + newMsg.toString().replace("%player%", txtSpilt[0]).replace("[BYPASS]", "") : newMsg.toString().replace("%player%", txtSpilt[0]).replace("[BYPASS]", "")));
	                            }
	                        } else {
	                            if (insult.replace("%player%", txtSpilt[0]).replace("[BYPASS]", "").length() < 100) {
	                                mc.thePlayer.sendChatMessage((teamsMode ? "/shout " + insult.replace("%player%", txtSpilt[0]).replace("[BYPASS]", "") : insult.replace("%player%", txtSpilt[0]).replace("[BYPASS]", "")));
	                            }
	                        }
	                    }
	                }
	            }
	        } catch (Exception ignored) {
	        }
		}
	}
}
