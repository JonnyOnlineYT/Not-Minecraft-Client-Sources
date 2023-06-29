package org.spray.heaven.features.module.modules.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import org.lwjgl.input.Keyboard;
import org.spray.heaven.events.TickEvent;
import org.spray.heaven.features.module.Category;
import org.spray.heaven.features.module.Module;
import org.spray.heaven.features.module.ModuleInfo;
import org.spray.heaven.features.module.Setting;
import org.spray.heaven.main.Wrapper;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

@ModuleInfo(name = "XRay", category = Category.RENDER, visible = true, key = Keyboard.KEY_NONE)
public class XRay extends Module {

    public Setting blocks = register(new Setting("Blocks", new ConcurrentLinkedQueue<Integer>(Arrays.asList(42, 56)),
            new ConcurrentLinkedQueue<Integer>(Arrays.asList(Block.getIdFromBlock(Blocks.DIAMOND_ORE),
                    Block.getIdFromBlock(Blocks.GOLD_ORE), Block.getIdFromBlock(Blocks.IRON_ORE), Block.getIdFromBlock(Blocks.REDSTONE_ORE),
                    Block.getIdFromBlock(Blocks.LAPIS_ORE), Block.getIdFromBlock(Blocks.COAL_ORE), Block.getIdFromBlock(Blocks.EMERALD_ORE),
                    Block.getIdFromBlock(Blocks.QUARTZ_ORE)))));

    public Setting opacity = register(new Setting("Opacity", 30, 0, 100) {
        @Override
        public void onUpdate() {
            mc.renderGlobal.loadRenderers();
        }
    });

    private boolean editedAmbientOcclusion;

    @Override
    public void onEnable() {
        mc.renderGlobal.loadRenderers();
        if (mc.gameSettings.ambientOcclusion == 0) {
            mc.gameSettings.ambientOcclusion = 1;
            editedAmbientOcclusion = true;
        }
    }

    @Override
    public void onDisable() {
        mc.renderGlobal.loadRenderers();
        mc.world.provider.generateLightBrightnessTable();
        if (editedAmbientOcclusion) {
            mc.gameSettings.ambientOcclusion = 0;
            editedAmbientOcclusion = false;
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        Arrays.fill(Wrapper.getWorld().provider.getLightBrightnessTable(), 1.0F);
    }

    public boolean isValid(Block block) {
        return blocks.getBlocks().contains(Block.getIdFromBlock(block));
    }

}
