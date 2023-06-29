package splash.api.module;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.text.WordUtils;
import splash.Splash;
import splash.api.module.category.ModuleCategory;
import splash.api.notification.Notification;
import splash.api.value.Value;
import splash.api.value.impl.ModeValue;
import splash.client.modules.visual.UI;
import splash.utilities.system.ClientLogger;
import splash.utilities.visual.ColorUtilities;

/**
 * Author: Ice
 * Created: 00:19, 30-May-20
 * Project: Abyss B21 - CBT Edition
 */
public abstract class Module {

    private String moduleDisplayName;
    private String moduleDescription;
    private String[] moduleAliases;

    private ModuleCategory moduleCategory;
    
    public float renderSlide = 0;
    
    private int moduleMacro;
    private boolean moduleActive;
    private double slideAnimation = 0;
    private int color;
    public Minecraft mc = Minecraft.getMinecraft();
    
    public Module(String moduleDisplayName, String moduleDescription, ModuleCategory moduleCategory) {
        this.moduleDisplayName = moduleDisplayName;
        this.moduleDescription = moduleDescription;
        this.moduleCategory = moduleCategory;
    }

    public String getModuleDisplayName() {
        return moduleDisplayName;
    }

    public String getFullModuleDisplayName() {
        for(Value value : Splash.getInstance().getValueManager().getValuesFrom(this)) {
            if(value instanceof ModeValue  && !(this instanceof UI)) {
                ModeValue modeValue = (ModeValue) value;
                if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Case").getValue() == UI.Case.LOWERCASE) {
                    return moduleDisplayName.toLowerCase() + EnumChatFormatting.GRAY + " " + WordUtils.capitalizeFully(((ModeValue) value).getFixedValue()).toLowerCase() + "".toLowerCase();
                } else {
                    return moduleDisplayName + EnumChatFormatting.GRAY + " " + WordUtils.capitalizeFully(((ModeValue) value).getFixedValue()) + "".toLowerCase();
                }
            }
        }
        if(Splash.getInstance().getValueManager().getValue(Splash.getInstance().getModuleManager().getModuleByClass(UI.class), "Case").getValue() == UI.Case.LOWERCASE) {
            return  moduleDisplayName.toLowerCase();
        } else {
            return moduleDisplayName;
        }
    }

    public void setModuleDisplayName(String moduleDisplayName) {
        this.moduleDisplayName = moduleDisplayName;
    }

    public String getModuleDescription() {
        return moduleDescription;
    }

    public void setModuleDescription(String moduleDescription) {
        this.moduleDescription = moduleDescription;
    }

    public String[] getModuleAliases() {
        return moduleAliases;
    }

    public void setModuleAliases(String[] moduleAliases) {
        this.moduleAliases = moduleAliases;
    }

    public ModuleCategory getModuleCategory() {
        return moduleCategory;
    }

    public void setModuleCategory(ModuleCategory moduleCategory) {
        this.moduleCategory = moduleCategory;
    }

    public int getModuleMacro() {
        return moduleMacro;
    }

    public void setModuleMacro(int moduleMacro) {
        this.moduleMacro = moduleMacro;
    }

    public boolean isModuleActive() {
        return moduleActive;
    }

    public void setModuleActive(boolean moduleActive) {
        this.moduleActive = moduleActive;
    }

    public double getSlideAnimation() {
        return slideAnimation;
    }

    public void setSlideAnimation(double slideAnimation) {
        this.slideAnimation = slideAnimation;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

	public void onEnable() {
        Splash.getInstance().getEventBus().register(this);
        if ((boolean) Splash.INSTANCE.getValueManager().getValue(Splash.INSTANCE.getModuleManager().getModuleByClass(UI.class), "Notifications").getValue()) {
        	Splash.getInstance().getNotificationManager().show(new Notification(getModuleDisplayName() + EnumChatFormatting.GREEN + " Enabled", "", 1));
        }
        setColor(ColorUtilities.getGerman(0.9F));
        setSlideAnimation(Splash.getInstance().getFontRenderer().getStringWidth(getFullModuleDisplayName()) - 1);
        if (mc.theWorld != null) Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.4f, 1f);
    }

    public void onDisable() {
        Splash.getInstance().getEventBus().unregister(this);
        if((boolean)Splash.INSTANCE.getValueManager().getValue(Splash.INSTANCE.getModuleManager().getModuleByClass(UI.class), "Notifications").getValue()) {
        	Splash.getInstance().getNotificationManager().show(new Notification(getModuleDisplayName() + EnumChatFormatting.RED + " Disabled", "", 1));
        }
        setColor(ColorUtilities.getGerman(0.9F));
        setSlideAnimation(Splash.getInstance().getFontRenderer().getStringWidth(getFullModuleDisplayName()) + 1);
        if (mc.theWorld != null) Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.3f, 0.8f);
    }

    public void activateModule() {
        if(isModuleActive()) {
            onDisable();
        } else {
            onEnable();
        }
        setModuleActive(!isModuleActive());
    }

    public Value getValue(String valueName) {
        if(Splash.getInstance().getValueManager().getValuesFrom(this) != null) {
            if(Splash.getInstance().getValueManager().getValue(this, valueName) != null) {
                return Splash.getInstance().getValueManager().getValue(this, valueName);
            } else {
                ClientLogger.printErrorToConsole("VALUE NOT FOUND");
            }
        }
        return null;
    }
    
    
    /*Sends the server a packet simulating motion on the x y z axis specified (C04 if not moving, C06 if moving)*/
    public void sendPosition(double x, double y, double z, boolean ground, boolean movement) {
		if (movement) {
			mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z, mc.thePlayer.serverSideYaw, mc.thePlayer.serverSidePitch, ground));
		} else {
			mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z, ground));
		}
    }
    
    public boolean onServer(String name) {
    	if (mc.getCurrentServerData() != null) {
    		return mc.getCurrentServerData().serverIP.toLowerCase().contains(name);
    	}
    	return false;
    }
}
