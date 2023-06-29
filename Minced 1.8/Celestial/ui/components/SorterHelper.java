package Celestial.ui.components;

import Celestial.ui.celestun4ik.component.impl.ModuleComponent;
import Celestial.ui.celestun4ik.component.Component;

import java.util.Comparator;

public class SorterHelper implements Comparator<Component> {

    @Override
    public int compare(Component component, Component component2) {
        if (component instanceof ModuleComponent && component2 instanceof ModuleComponent) {
            return component.getName().compareTo(component2.getName());
        }
        return 0;
    }
}