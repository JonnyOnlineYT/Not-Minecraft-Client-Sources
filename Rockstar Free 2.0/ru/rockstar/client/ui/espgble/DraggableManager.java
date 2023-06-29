package ru.rockstar.client.ui.espgble;


import java.util.ArrayList;

import ru.rockstar.client.ui.espgble.impl.*;


public class DraggableManager {

    public ArrayList<DraggableModule> mods = new ArrayList<>();

    public DraggableManager() {
    	mods.add(new EspComponent());
    	mods.add(new NameTagsComponent());
    }

    public ArrayList<DraggableModule> getMods() {
        return this.mods;
    }
    
    public void setMods(final ArrayList<DraggableModule> mods) {
        this.mods = mods;
    }
}