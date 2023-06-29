package ru.rockstar.client.features.impl.movement;

import java.util.concurrent.TimeUnit;

import ru.rockstar.client.features.Category;
import ru.rockstar.client.features.Feature;

public class HighJump extends Feature {
    public HighJump() {
        super("HighJump", "Подкидывает вас высоко вверх", 0, Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
    		if (mc.player.onGround) {
                mc.player.jump();
            }
            new Thread(() -> {
                mc.player.motionY = 9f;
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mc.player.motionY = 8.742f;
                this.toggle();
            }).start();
    }
}