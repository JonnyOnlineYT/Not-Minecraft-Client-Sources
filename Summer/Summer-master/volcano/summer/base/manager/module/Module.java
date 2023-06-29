package volcano.summer.base.manager.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.StringUtils;
import volcano.summer.base.Summer;
import volcano.summer.base.manager.event.Event;
import volcano.summer.base.manager.file.files.Modules;
import volcano.summer.client.events.EventMotion2;
import volcano.summer.client.util.TimerUtil;
import volcano.summer.client.value.Value;

public abstract class Module {

	public static Minecraft mc = Minecraft.getMinecraft();
	public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	private static List<Value> values;
	public String name;
	public String displayName = "";
	public int bind;
	public Category category;
	public boolean state, hidden = false;
	public String realName;
	public int transition;
	public static Module THIS;

	public Module(String name, int bind, Category category) {
		this.name = name;
		this.bind = bind;
		this.category = category;
		this.values = new ArrayList<Value>();
		THIS = this;
	}

	public boolean getModCategory(Category cat) {
		return category == cat;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	TimerUtil antiNoise = new TimerUtil();

	public void toggleMod() {
		if (this.state) {
			this.onDisable();
			if (this.mc.theWorld != null && antiNoise.hasPassed(100)) {
				mc.thePlayer.playSound("random.click", 0.4F, 0.8F);
				antiNoise.reset();
			}
			
			this.transition = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getName())) + 10;
			
			this.state = false;
		} else {
			this.onEnable();
			if (this.mc.theWorld != null && antiNoise.hasPassed(100)) {
				mc.thePlayer.playSound("random.click", 0.5F, 1.0F);
				antiNoise.reset();
			}
			this.transition = mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getName())) - 10;
			
			this.state = true;
			try {
				Summer.fileManager.getFile(Modules.class).saveFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int getTransition() {
		return transition;
	}

	public void setTransition(int transition) {
		this.transition = transition;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public boolean drawDisplayName(float x, float y, int color) {
		if (this.state) {
			Minecraft.getMinecraft().fontRendererObj
					.func_175063_a(String.format("%s" + (this.displayName.length() > 0 ? " §7[%s]" : ""),
							new Object[] { this.name, this.displayName }), x, y, color);
			return true;
		}
		return false;
	}

	public void setBind(int bind) {
		this.bind = bind;
	}

	public boolean getState() {
		return this.state;
	}

	public void setState(boolean state) {
		this.state = state;
		try {
			Summer.fileManager.getFile(Modules.class).saveFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public abstract void onEnable();

	public abstract void onDisable();

	public abstract void onEvent(Event event);
	

	public enum Category {
		COMBAT, MOVEMENT, PLAYER, RENDER, MISC
	}

	public String getName() {
		return this.name;
	}

	public int getBind() {
		return this.bind;
	}

	public static List<Value> getProperties() {
		return values;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public Category getCategory() {
		return this.category;
	}

	
		
	
}