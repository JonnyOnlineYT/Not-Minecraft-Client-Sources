package ua.apraxia.draggable;

import com.google.common.collect.Lists;
import ua.apraxia.draggable.component.DraggableComponent;
import ua.apraxia.draggable.component.impl.*;

import java.util.List;

public class Draggable {

    private DraggableScreen screen;
    private final List<DraggableComponent> components;

    public Draggable() {
        screen = new DraggableScreen();
        components = Lists.newArrayList();
        components.add(new FeatureList());
        components.add(new TargetHUD());
        components.add(new WaterMark());
        components.add(new Potions());
        components.add(new Armor());
    }

    public DraggableScreen getScreen() {
        return screen;
    }

    public List<DraggableComponent> getComponents() {
        return components;
    }

    public DraggableComponent getDraggableComponentByClass(Class<? extends DraggableComponent> classs) {
        for (DraggableComponent draggableComponent : components) {
            if (draggableComponent.getClass() == classs) {
                return draggableComponent;
            }
        }

        return null;
    }

}