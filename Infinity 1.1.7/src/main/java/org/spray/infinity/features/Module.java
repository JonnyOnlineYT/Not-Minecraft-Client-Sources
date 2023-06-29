package org.spray.infinity.features;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.spray.infinity.utils.StringUtil;

import com.darkmagician6.eventapi.EventManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class Module {

	private Category category;
	private String desc;
	private String name;
	private String suffix;

	// Use only GLFW.GLFW_KEY_YOURKEY or for KEY_NONE use code -2
	private int key, defaultKey;

	private boolean visible, defaultVisible;
	private boolean state, defaultEnabled;

	private List<Setting> settings;

	public float animY = 0;

	public Module() {
		name = (getClass().getAnnotation(ModuleInfo.class).name());
		key = (getClass().getAnnotation(ModuleInfo.class).key());
		visible = (getClass().getAnnotation(ModuleInfo.class).visible());
		category = (getClass().getAnnotation(ModuleInfo.class).category());
		desc = (getClass().getAnnotation(ModuleInfo.class).desc());
		settings = new ArrayList<>();
	}

	public void onUpdate() {
	}

	// 2d render
	public void onRender(MatrixStack matrices, float tickDelta, int scaledWidth, int scaledHeight) {
	}

	public void enable() {
		setState(true);
	}
	
	public void disable() {
		setState(false);
	}
	
	public void updateState() {
		setState(!state);
	}

	protected void setState(boolean state) {
		this.state = state;
		if (state)
			enableHandle();
		else
			disableHandle();
	}

	public void enableHandle() {
		EventManager.register(this);
		onEnable();
	}

	public void disableHandle() {
		EventManager.unregister(this);
		onDisable();
	}

	public void onEnable() {
	}

	public void onDisable() {
	}

	public void updateSettings() {
		for (final Field field : getClass().getDeclaredFields()) {
			try {
				field.setAccessible(true);
				final Object o = field.get(this);
				if (o instanceof Setting) {
					Setting sett = (Setting) o;
					this.addSettings(sett);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Setting> getSettings() {
		this.updateSettings();
		return this.settings;
	}

	public void addSettings(Setting settings) {
		if (!this.settings.contains(settings))
			this.settings.add(settings);
	}

	public void addSettings(Collection<Setting> c) {
		this.settings.addAll(c);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public String getRenderName() {
		return getName() + " " + Formatting.WHITE + StringUtil.replaceNull(getSuffix());
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isEnabled() {
		return state;
	}

	public Category getCategory() {
		return category;
	}

	public String getSuffix() {
		return suffix;
	}

	/**
	 * use only tick update methods
	 * 
	 * @param suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public int getDefaultKey() {
		return defaultKey;
	}

	public boolean isDefaultVisible() {
		return defaultVisible;
	}

	public boolean isDefaultEnabled() {
		return defaultEnabled;
	}

}
