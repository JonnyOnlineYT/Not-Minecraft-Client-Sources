package summer.cheat.cheats.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MovementInput;
import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventUpdate;
import summer.base.manager.config.Cheats;

public class Sprint extends Cheats {
    private Setting omni;
    public Minecraft mc = Minecraft.getMinecraft();
    private Setting hide;

    public Sprint() {
        super("Sprint", "Sprint all the time", Selection.MOVEMENT, false);
        Summer.INSTANCE.settingsManager.Property(omni = new Setting("Omni", this, false));
        Summer.INSTANCE.settingsManager.Property(hide = new Setting("Hide", this, false));

    }

    @Override
    public void onSetup() {
    }

    @Override
    public void onDisable() {
        Minecraft.thePlayer.setSprinting(false);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
    	//e.setPitch(90.0f);
    	//e.setLastYaw(0);
    	if(!hide.getValBoolean()) {
    		this.setDisplayName("Sprint");
    	}else {
    		this.setDisplayName("");
    	}
        if (e.isPre() && Minecraft.thePlayer.moveForward > 0.0 && !Minecraft.thePlayer.isSneaking() && !Minecraft.thePlayer.isCollidedHorizontally && !Minecraft.thePlayer.isUsingItem() && Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6) {
            Minecraft.thePlayer.setSprinting(true);
        }
        if (omni.getValBoolean()) {
        	if(!hide.getValBoolean())
        		this.setDisplayName("Sprint\u00A77 " + "Omni");
            if (e.isPre() && !Minecraft.thePlayer.isSneaking() && MovementInput.moveForward != 0.0f || MovementInput.moveStrafe != 0.0f && !Minecraft.thePlayer.isSneaking() && !Minecraft.thePlayer.isCollidedHorizontally && !Minecraft.thePlayer.isUsingItem() && Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6) {
                Minecraft.thePlayer.setSprinting(true);
            }
        }
    }
}