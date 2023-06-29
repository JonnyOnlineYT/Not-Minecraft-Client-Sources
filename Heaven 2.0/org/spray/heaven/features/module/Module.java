package org.spray.heaven.features.module;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import org.spray.heaven.font.IFont;
import org.spray.heaven.main.Wrapper;
import org.spray.heaven.notifications.Notification.Type;
import org.spray.heaven.util.StringUtil;
import org.spray.heaven.util.render.animation.Animation;
import org.spray.heaven.util.render.animation.impl.DecelerateAnimation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Module {

    protected final Minecraft mc = Wrapper.MC;
    private final List<Setting> settings;

    private String name;
    private Category category;
    private boolean visible;
    private int key;

    private String suffix;

    public Animation animation = new DecelerateAnimation(230, 1f);

    private boolean enabled;

    public Module() {
        name = this.getClass().getAnnotation(ModuleInfo.class).name();
        category = this.getClass().getAnnotation(ModuleInfo.class).category();
        visible = this.getClass().getAnnotation(ModuleInfo.class).visible();
        key = this.getClass().getAnnotation(ModuleInfo.class).key();
        settings = new ArrayList<>();
    }

    public void onRender(int width, int height, float tickDelta) {
    }

    public Dimension onDraggableRender(int width, int height, float tickDelta) {
        return null;
    }

    public void onUpdate() {
    }

    public Setting register(Setting setting) {
        settings.add(setting);
        return setting;
    }

    public List<Setting> getSettings() {
        return this.settings;
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

    protected void setState(boolean state) {
        this.enabled = state;
        if (state) {
            enableHandle();

            if (isVisible())
                Wrapper.getNotification().call(I18n.format("if.enabled").replace("$3", getName()), Type.INFO,
                        IFont.DEFAULT_SMALL);
        } else {
            disableHandle();

            if (isVisible())
                Wrapper.getNotification().call(I18n.format("if.disabled").replace("$3", getName()), Type.INFO,
                        IFont.DEFAULT_SMALL);
        }
    }

    public void enable() {
        setState(true);
    }

    public void disable() {
        setState(false);
    }

    public void updateState() {
        setState(!enabled);
    }

    public String getRenderName(boolean indent) {
        return getRenderName(indent, true);
    }

    public String getRenderName(boolean indent, boolean format) {
        String name = indent ? this.name.replaceAll("(\\p{Ll})(\\p{Lu})", "$1 $2") : this.name;
        String s1 = getSuffix().isEmpty() ? "" : "-";
        String s2 = getSuffix().isEmpty() ? "" : " ";
        return format ? name + s2 + TextFormatting.GRAY + s1 + getSuffix() : name + TextFormatting.GRAY + getSuffix();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getSuffix() {
        return suffix == null ? StringUtil.replaceNull(suffix) : " " + suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
