package de.strafe.modules.render;

import com.eventapi.EventManager;
import com.eventapi.EventTarget;
import de.strafe.events.EventRender2D;
import de.strafe.modules.Category;
import de.strafe.modules.Module;
import de.strafe.utils.RenderUtil;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;

public class ChestEsp extends Module {
    public ChestEsp() {
        super("Chest Esp", 0, Category.RENDER);
    }

    @EventTarget
    public void onUpdate(EventRender2D event) {
        for (Object o : mc.theWorld.loadedEntityList) {
            if (o instanceof TileEntityChest || (o instanceof TileEntityChest)) {
                TileEntityChest chest = (TileEntityChest)o;
                TileEntityEnderChest enderChest = (TileEntityEnderChest) o;
                RenderUtil.blockESPBox(((TileEntityChest) o).getPos());
                int x= chest.getPos().getX();
                int y= chest.getPos().getY();
                int z= chest.getPos().getZ();
                double xPos = RenderManager.renderPosX;
                double yPos = RenderManager.renderPosY;
                double zPos = RenderManager.renderPosZ;
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        EventManager.register(this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        EventManager.unregister(this);
    }
}