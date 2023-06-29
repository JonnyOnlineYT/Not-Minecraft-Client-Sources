package ua.apraxia.draggable;

import net.minecraft.util.math.MathHelper;
import ua.apraxia.Hexbyte;
import ua.apraxia.draggable.component.DraggableComponent;

public class DraggableScreen {

    public void draw(int mouseX, int mouseY) {
        for(DraggableComponent draggableComponent : Hexbyte.getInstance().draggable.getComponents()) {
            if(draggableComponent.allowDraw()) {
                drawComponent(mouseX, mouseY, draggableComponent);
            }
        }
    }

    private void drawComponent(int mouseX, int mouseY, DraggableComponent draggableComponent) {
        draggableComponent.draw(mouseX, mouseY);

        boolean hover = MathHelper.isMouseHoveringOnRect(draggableComponent.getX(), draggableComponent.getY(), draggableComponent.getWidth(), draggableComponent.getHeight(), mouseX, mouseY);


    }

    public void click(int mouseX, int mouseY) {
        for(DraggableComponent draggableComponent : Hexbyte.getInstance().draggable.getComponents()) {
            if(draggableComponent.allowDraw()) {
                draggableComponent.click(mouseX, mouseY);
            }
        }
    }

    public void release() {
        for(DraggableComponent draggableComponent : Hexbyte.getInstance().draggable.getComponents()) {
            draggableComponent.release();
        }
    }

}