package splash.client.modules.misc;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.notification.Notification;
import splash.client.events.network.EventPacketReceive;
import splash.utilities.time.Stopwatch;

public class Autoplay extends Module {
	private Stopwatch stopwatch;
	
    public Autoplay() {
        super("Autoplay", "Auto joins games.", ModuleCategory.MISC);
        stopwatch = new Stopwatch();
    }
    
    @Override
    public void onDisable() {
    	stopwatch.reset();
    	super.onDisable();
    	
    }

    @Collect
    public void onEventReceive(EventPacketReceive eventPacketReceive) {
        if (eventPacketReceive.getReceivedPacket() instanceof S02PacketChat) {
            S02PacketChat s02PacketChat = (S02PacketChat) eventPacketReceive.getReceivedPacket();

            if (!s02PacketChat.getChatComponent().getUnformattedText().isEmpty()) {
                String message = s02PacketChat.getChatComponent().getUnformattedText();
                
                if (message.contains("You won! Want to play again?") || message.contains("You died! Want to play again?") && isHypixel()) {
                	// NOTIFICATION
                	Thread thread = new Thread(){
                		public void run(){
                        	Splash.getInstance().getNotificationManager().show(new Notification("Sending you to a game!", "Sending you to a game!", 2));
                        	try { Thread.sleep(2000); } catch (Exception e) {};
                    		mc.getNetHandler().addToSendQueueNoEvent(new C01PacketChatMessage("/play solo_insane"));
                		}
                	};

                	thread.start();
                  
                }

            }
        }
    }
    public boolean isHypixel () {
        return mc.getCurrentServerData().serverIP.contains("hypixel");
    }
}
