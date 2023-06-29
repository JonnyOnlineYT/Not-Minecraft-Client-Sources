package ru.rockstar.client.features.impl.movement;

import net.minecraft.util.text.TextFormatting;
import ru.rockstar.Main;
import ru.rockstar.api.event.EventTarget;
import ru.rockstar.api.event.event.EventUpdate;
import ru.rockstar.api.utils.movement.MovementHelper;
import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;
import ru.rockstar.client.features.impl.combat.KillAura;
import ru.rockstar.client.features.impl.display.TimerIndicator;
import ru.rockstar.client.ui.settings.impl.BooleanSetting;
import ru.rockstar.client.ui.settings.impl.NumberSetting;

public class Timer extends Feature {
    public static NumberSetting timer;
    public static int tick = 0;
    private final NumberSetting Ticks;
    public static BooleanSetting autoDisable;
    public static BooleanSetting targetCheck = new BooleanSetting("TargetCheck", "Проверяет есть ли у киллауры таргет, только после этого использует данный модуль", false, () -> true);
    public static BooleanSetting cdCheck = new BooleanSetting("CoolDownCheck", "Проверяет полный ли кулдаун", false, () -> targetCheck.getBoolValue());
    public static int ticks;
    
    public Timer() {
        super("Timer", "Увеличивает скорость игры", 0, Category.MOVEMENT);
        timer = new NumberSetting("Timer", 2.0F, 0.1F, 10.0F, 0.1F, () -> true);
        this.Ticks = new NumberSetting("Ticks", 55f, 1f, 100f, 1f, () -> autoDisable.getBoolValue());
        autoDisable = new BooleanSetting("AutoDisable", "Автоматически выключает таймер", false, () -> true);
        addSettings(timer, autoDisable, Ticks, targetCheck);

    }
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	ticks = (int) Ticks.getNumberValue();
        if (this.isToggled()) { 
        	if (MovementHelper.isMoving() || mc.player.motionY > 0) {
        		if (targetCheck.getBoolValue()) {
        			boolean n = false;
        			if (KillAura.target != null && Main.instance.featureDirector.getFeatureByClass(KillAura.class).isToggled()) {
        					n = false;
        			} else {
        					n = true;
        			}
        			if (n) {
        				 mc.timer.timerSpeed = timer.getNumberValue();
        			} else {
        				mc.timer.timerSpeed = 1;
        			}
        		} else {
        			 mc.timer.timerSpeed = timer.getNumberValue();
        		}
                 this.tick += 0.5f * (timer.getNumberValue());
        	}
            this.setModuleName("Timer " + TextFormatting.GRAY + "[" + timer.getNumberValue() + "]");
        }
        if(tick >= 100)
        {
        	tick = 0;
        }
        
        if(tick >= Ticks.getNumberValue())
        {
        	tick = (int) TimerIndicator.indicatorTimer;
            if (autoDisable.getBoolValue()) {
            	this.toggle();
            }
        }
       
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Timer.mc.timer.timerSpeed = 1.0f;
    }
    @Override
    public void onEnable() {
    	tick = (int) TimerIndicator.indicatorTimer;
        super.onEnable();
    }
}