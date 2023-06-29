package ru.rockstar.client.ui.csgui;


import java.util.Comparator;

import ru.rockstar.client.ui.csgui.component.Component;
import ru.rockstar.client.ui.csgui.component.impl.ModuleComponent;

public class SorterHelper implements Comparator<Component> {
    
    @Override
    public int compare(Component component, Component component2) {
        if (component instanceof ModuleComponent && component2 instanceof ModuleComponent) {
            return component.getName().compareTo(component2.getName());
        }
        return 0;
    }

}