package org.moonware.client.settings.impl;

import net.minecraft.block.Block;
import org.moonware.client.settings.Setting;
import org.moonware.client.utils.Interpolator;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {

    private boolean state;
    private String desc;
    private Block block;
    public int greenAnim;
    public double stateGuiAnim;
    private int rotVal;
    int trVal;
    int trYVal;
    public BooleanSetting(String name, String desc, boolean state, Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.state = state;
        greenAnim = 1;
        stateGuiAnim = 0;
        this.rotVal = 0;
        this.trVal = 0;
        this.trYVal = 0;
        setVisible(visible);
    }
    public double getrotVal() {
        this.rotVal =  Interpolator.linear(this.rotVal,  getParentMod().isHovered ? 180 + 90 : 90    , 2F / 5);
        return this.rotVal;
    }
    public double getStateGuiAnim() {
        stateGuiAnim = Interpolator.linear(stateGuiAnim, get() ? 10 : 0, 2f /60);
        return stateGuiAnim;
    }
    public BooleanSetting(String name, boolean state, Supplier<Boolean> visible) {
        this.name = name;
        this.state = state;
        setVisible(visible);
    }
    public BooleanSetting(String name, String desc, Supplier<Boolean> visible) {
        this.name = name;
        state = true;
        setVisible(visible);
    }
    public BooleanSetting(String name, boolean state) {
        this.name = name;
        this.state = state;
        setVisible(()-> true);
    }
    public BooleanSetting(String name,String desc, boolean state) {
        this.name = name;
        this.desc = desc;
        this.state = state;
        setVisible(()-> true);
    }
    public boolean get() {
        return state;
    }

    public boolean isToggle() {
        return state;
    }
    public boolean isEnabled() {
        return state;
    }
    public boolean isEnabled(boolean NotWex) {
        return state;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean getBoolValue() {
        return state;
    }
    public boolean getCurrentValue() {
        return state;
    }
    public Block getBlock() {
        return block;
    }
    public void setBlock(Block block) {
        this.block = block;
    }
    public void setValue(boolean state) {
        this.state = state;
    }
    public void setBoolValue(boolean state) {
        this.state = state;
    }
}
